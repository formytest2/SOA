package com.tranboot.client.core.txc;

import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.github.bluecatlee.gs4d.transaction.api.model.RedisTransactionModel;
import com.github.bluecatlee.gs4d.transaction.api.service.TransactionInitService;
import com.tranboot.client.core.JdbcTemplatePointCut;
import com.tranboot.client.exception.TxcTransactionException;
import com.tranboot.client.model.DBType;
import com.tranboot.client.service.txc.TxcManualRollbackSqlService;
import com.tranboot.client.service.txc.TxcMqService;
import com.tranboot.client.service.txc.TxcRedisService;
import com.tranboot.client.service.txc.TxcShardSettingReader;
import com.tranboot.client.service.txc.impl.TxcManualRollbackSQLServiceXMLImpl;
import com.tranboot.client.service.txc.impl.TxcManualRollbackSqlServiceNullImpl;
import com.tranboot.client.service.txc.impl.TxcShardSettingReaderDubboImpl;
import com.tranboot.client.spring.ContextUtils;
import com.tranboot.client.utils.CustomAopProxy;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.LinkedList;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.TargetSource;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * txc事务组件扫描
 *
 *      初始化入口 外部需要把TxcTransactionScaner注册到spring
 *      如外部项目在spring xml配置中引入：
 *          <bean class="com.tranboot.client.core.txc.TxcTransactionScaner">
 * 		        <property name="systemName" value="order" />
 * 		        <property name="systemId" value="2" />
 * 		        <property name="txcMqNameServerAddr" value="${mq.name.server.addr}" />
 * 	        </bean>
 *
 *      BeanDefinitionRegistryPostProcessor 继承了 BeanFactoryPostProcessor(bean工厂后置处理器) 主要作用了注册bean定义以及注册bean
 *      AbstractAutoProxyCreator(创建自动代理) 实现了BeanPostProcessor接口 用于在 bean 初始化完成之后创建它的代理
 *
 */
public class TxcTransactionScaner extends AbstractAutoProxyCreator implements InitializingBean, ApplicationContextAware, BeanDefinitionRegistryPostProcessor {

    private static final long serialVersionUID = -6378434453422374519L;

    private String systemName;
    private Integer systemId;
    private String txcMqNameServerAddr;
    private String manualSql;
    private TxcMethodInterceptor interceptor;

    // 注册TxcMqService
    private void registryTxcMqService(BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder bdbuilder = BeanDefinitionBuilder.genericBeanDefinition(TxcMqService.class);
        bdbuilder.addPropertyValue("nameServerAddr", this.txcMqNameServerAddr);
        registry.registerBeanDefinition("txcMqService", bdbuilder.getBeanDefinition());
    }

    // 注册TxcManualRollbackSqlService
    private void registryTxcManualRollbackSqlService(ConfigurableListableBeanFactory beanFactory) {
        if (this.manualSql == null) {
            beanFactory.registerSingleton(TxcManualRollbackSqlService.class.getName(), new TxcManualRollbackSqlServiceNullImpl());
        } else {
            beanFactory.registerSingleton(TxcManualRollbackSqlService.class.getName(), new TxcManualRollbackSQLServiceXMLImpl(this.manualSql));
        }

    }

    // 注册TxcShardSettingReader
    private void registryTxcShardSettingReader(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.registerSingleton(TxcShardSettingReader.class.getName(), new TxcShardSettingReaderDubboImpl());
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        return new Object[]{this.interceptor};
    }

    @Override
    protected boolean shouldProxyTargetClass(Class<?> beanClass, String beanName) {
        return true;
    }

    @Override
    public void customizeProxyFactory(ProxyFactory proxyFactory) {
        proxyFactory.setFrozen(true);
        this.setFrozen(true);
        proxyFactory.setExposeProxy(true);
    }

    @Override
    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
        Class<?> localClass = bean.getClass();
        Method localMethod;
        if (JdbcTemplate.class.isAssignableFrom(localClass)) {
            DataSource ds = ((JdbcTemplate)bean).getDataSource();
            String datasource = null;
            DBType dbType = DBType.MYSQL;

            try {
                if (Class.forName("com.alibaba.druid.pool.DruidAbstractDataSource").isAssignableFrom(ds.getClass())) {
                    Method getUrl = Class.forName("com.alibaba.druid.pool.DruidAbstractDataSource").getMethod("getUrl");
                    String driverUrl = (String)getUrl.invoke(ds);
                    datasource = this.extractDbName(driverUrl);
                    localMethod = Class.forName("com.alibaba.druid.pool.DruidAbstractDataSource").getMethod("getDriverClassName");
                    String driverClass = (String)localMethod.invoke(ds);
                    if (driverClass.equals("oracle.jdbc.OracleDriver") || driverClass.equals("oracle.jdbc.driver.OracleDriver")) {
                        dbType = DBType.ORACLE;
                    }
                } else {
                    this.logger.warn("连接池没有使用druid,无法提取出数据库名、数据库类型");
                }
            } catch (Exception e) {
                throw new TxcTransactionException(e, "txc生成jdbctemplate拦截对象失败");
            }

            AspectJExpressionPointcut pointcut1 = new AspectJExpressionPointcut();
            pointcut1.setExpression(JdbcTemplatePointCut.update1);
            DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(pointcut1, new TxcJdbcTemplateInterceptor1(datasource, ds, (TxcRedisService)ContextUtils.getBean(TxcRedisService.class), dbType));
            AspectJExpressionPointcut pointcut2 = new AspectJExpressionPointcut();
            pointcut2.setExpression(JdbcTemplatePointCut.update2);
            DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(pointcut2, new TxcJdbcTemplateInterceptor2(datasource, ds, (TxcRedisService)ContextUtils.getBean(TxcRedisService.class), dbType));
            AspectJExpressionPointcut pointcut3 = new AspectJExpressionPointcut();
            pointcut3.setExpression(JdbcTemplatePointCut.batchUpdate1);
            DefaultPointcutAdvisor advisor3 = new DefaultPointcutAdvisor(pointcut3, new TxcJdbcTemplateInterceptor3(datasource, ds, (TxcRedisService)ContextUtils.getBean(TxcRedisService.class), dbType));
            AspectJExpressionPointcut pointcut4 = new AspectJExpressionPointcut();
            pointcut4.setExpression(JdbcTemplatePointCut.batchUpdate2);
            DefaultPointcutAdvisor advisor4 = new DefaultPointcutAdvisor(pointcut4, new TxcJdbcTemplateInterceptor4(datasource, ds, (TxcRedisService)ContextUtils.getBean(TxcRedisService.class), dbType));
            bean = this.createProxy(localClass, beanName, new Object[]{advisor1, advisor2, advisor3, advisor4}, new SingletonTargetSource(bean));       // 创建代理 即拦截器
            return bean;
        } else if (PlatformTransactionManager.class.isAssignableFrom(localClass)) {
            try {
                return CustomAopProxy.proxy(CustomAopProxy.getTargetClass(bean), new TxcDataSourceTransactionManagerInterceptor((DataSourceTransactionManager)bean, (TxcRedisService)ContextUtils.getBean(TxcRedisService.class)));
            } catch (Exception e) {
                throw new TxcTransactionException(e, "txc生成transactionManager拦截对象失败");
            }
        } else {
            Method[] arrayOfMethod = localClass.getMethods();
            LinkedList<TxcMethodContext> localLinkedList = new LinkedList();
            Method[] methodArr = arrayOfMethod;
            int methodArrLen = arrayOfMethod.length;

            for(int i = 0; i < methodArrLen; ++i) {
                localMethod = methodArr[i];
                TxcTransaction localTxcTransaction = (TxcTransaction)localMethod.getAnnotation(TxcTransaction.class);
                if (localTxcTransaction != null) {
                    localLinkedList.add(new TxcMethodContext(localTxcTransaction, localMethod));
                }
            }

            if (localLinkedList.size() != 0) {
                TxcMethodInterceptor i = new TxcMethodInterceptor(localLinkedList);
                this.interceptor = i;
                if (!AopUtils.isAopProxy(bean)) {
                    bean = super.wrapIfNecessary(bean, beanName, cacheKey);
                } else {
                    bean = this.createProxy(localClass, beanName, this.getAdvicesAndAdvisorsForBean((Class)null, (String)null, (TargetSource)null), new SingletonTargetSource(bean));
                }

                return bean;
            } else {
                return bean;
            }
        }
    }

    public String getSystemName() {
        return this.systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Integer getSystemId() {
        return this.systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

    public String getTxcMqNameServerAddr() {
        return this.txcMqNameServerAddr;
    }

    public void setTxcMqNameServerAddr(String txcMqNameServerAddr) {
        this.txcMqNameServerAddr = txcMqNameServerAddr;
    }

    public String getManualSql() {
        return this.manualSql;
    }

    public void setManualSql(String manualSql) {
        this.manualSql = manualSql;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isBlank(this.txcMqNameServerAddr)) {
            throw new Exception("txcMqNameServerAddr cannot be null");
        } else if (StringUtils.isBlank(this.systemName)) {
            throw new Exception("txc systemName cannot be null");
        } else if (this.systemId == null) {
            throw new Exception("txc systemId cannot be null");
        } else {
            // 初始化ContextUtils
            ContextUtils.setServerIp(InetAddress.getLocalHost().getHostAddress());
            ContextUtils.setSystemId(this.systemId);
            ContextUtils.setSystemName(this.systemName);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ContextUtils.setApplicationContext(applicationContext);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.registryTxcManualRollbackSqlService(beanFactory);
        this.registryTxcShardSettingReader(beanFactory);
    }

    // 【这里是初始化的核心 手工注册了txcRedisService、txcMqService等】
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String[] candidates = registry.getBeanDefinitionNames();
        boolean transactionInitServiceRegistried = false;
        boolean jedisConnectionFactoryRegistried = false;
        String jedisConnectionFactoryBeanName = null;
        String stringRedisTemplateBeanName = null;
        String stringRedisSerializerBeanName = null;
        String[] candidatesArr = candidates;
        int candidatesLen = candidates.length;

        for(int i = 0; i < candidatesLen; ++i) {
            String candidate = candidatesArr[i];
            BeanDefinition bd = registry.getBeanDefinition(candidate);
            if (candidate.equals("redisConnectionFactory")) {
                jedisConnectionFactoryBeanName = candidate;
                jedisConnectionFactoryRegistried = true;
            } else if (candidate.equals("stringRedisTemplate")) {
                stringRedisTemplateBeanName = candidate;
            } else if (bd.getBeanClassName() != null) {
                if (bd.getBeanClassName().equals(ReferenceBean.class.getName())) {
                    MutablePropertyValues propertyValue = registry.getBeanDefinition(candidate).getPropertyValues();
                    String dubbo_interface = propertyValue.get("interface").toString();     // dubbbo会给bean定义添加interface属性，值为bean的类型全路径名
                    if (dubbo_interface.equals(TransactionInitService.class.getName())) {
                        transactionInitServiceRegistried = true;
                    }
                }

                if (bd.getBeanClassName().equals(JedisConnectionFactory.class.getName())) {
                    jedisConnectionFactoryBeanName = candidate;
                    jedisConnectionFactoryRegistried = true;
                }

                if (bd.getBeanClassName().equals(StringRedisTemplate.class.getName())) {
                    stringRedisTemplateBeanName = candidate;
                }

                if (bd.getBeanClassName().equals(StringRedisSerializer.class.getName())) {
                    stringRedisSerializerBeanName = candidate;
                }
            }
        }

        if (!transactionInitServiceRegistried) {
            throw new NoSuchBeanDefinitionException("TransactionInitService");
        } else if (!jedisConnectionFactoryRegistried) {
            throw new NoSuchBeanDefinitionException("JedisConnectionFactory");
        } else {
            BeanDefinitionBuilder txcRedisTemplateBeanBuilder;
            if (stringRedisTemplateBeanName == null) {
                txcRedisTemplateBeanBuilder = BeanDefinitionBuilder.genericBeanDefinition(StringRedisTemplate.class);
                txcRedisTemplateBeanBuilder.addConstructorArgReference(jedisConnectionFactoryBeanName);
                stringRedisTemplateBeanName = "stringRedisTemplate";
                registry.registerBeanDefinition(stringRedisTemplateBeanName, txcRedisTemplateBeanBuilder.getBeanDefinition());     // 注册stringRedisTemplate
            }

            txcRedisTemplateBeanBuilder = BeanDefinitionBuilder.genericBeanDefinition(RedisTemplate.class);
            txcRedisTemplateBeanBuilder.addPropertyReference("connectionFactory", jedisConnectionFactoryBeanName);
            if (stringRedisSerializerBeanName != null) {
                txcRedisTemplateBeanBuilder.addPropertyReference("keySerializer", stringRedisSerializerBeanName);
                txcRedisTemplateBeanBuilder.addPropertyReference("hashKeySerializer", stringRedisSerializerBeanName);
            } else {
                txcRedisTemplateBeanBuilder.addPropertyValue("keySerializer", new StringRedisSerializer());
                txcRedisTemplateBeanBuilder.addPropertyValue("hashKeySerializer", new StringRedisSerializer());
            }

            RedisSerializer<RedisTransactionModel> valueSer = new Jackson2JsonRedisSerializer(RedisTransactionModel.class);
            txcRedisTemplateBeanBuilder.addPropertyValue("valueSerializer", valueSer);
            txcRedisTemplateBeanBuilder.addPropertyValue("hashValueSerializer", valueSer);
            registry.registerBeanDefinition("txcRedisTemplate", txcRedisTemplateBeanBuilder.getBeanDefinition());               // 注册txcRedisTemplate
            BeanDefinitionBuilder txcRedisServiceBeanBuilder = BeanDefinitionBuilder.genericBeanDefinition(TxcRedisService.class);
            txcRedisServiceBeanBuilder.addConstructorArgReference("txcRedisTemplate");
            txcRedisServiceBeanBuilder.addConstructorArgReference(stringRedisTemplateBeanName);
            registry.registerBeanDefinition("txcRedisService", txcRedisServiceBeanBuilder.getBeanDefinition());                 // 注册txcRedisService
            this.registryTxcMqService(registry);                                                                                   // 注册TxcMqService
        }
    }

    private String extractDbName(String jdbcUrl) {
        try {
            String datasource = StringUtils.substringBefore(StringUtils.substringAfterLast(jdbcUrl, "/"), "?");
            return datasource;
        } catch (Exception e) {
            this.logger.error("提取数据库名失败" + e.getMessage());
            return null;
        }
    }
}

