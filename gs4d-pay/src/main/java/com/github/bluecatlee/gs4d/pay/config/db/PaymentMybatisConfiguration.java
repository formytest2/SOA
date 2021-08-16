package com.github.bluecatlee.gs4d.pay.config.db;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

import javax.annotation.Resource;
import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
@Slf4j
@MapperScan(basePackages={"com.github.bluecatlee.gs4d.pay"}, sqlSessionTemplateRef = "paymentSqlSessionTemplate")
public class PaymentMybatisConfiguration {

    @Resource(name="payment")
    public DataSource paymentDataSource;


    @Bean(name="paymentSqlSessionFactory")
    public SqlSessionFactory paymentSqlSessionFactory(@Qualifier("payment")DataSource paymentDataSource) {
        try {
            SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
            sessionFactory.setDataSource(paymentDataSource);
            sessionFactory.setTypeAliasesPackage("com.github.bluecatlee.gs4d.pay");
            sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                    .getResources("classpath*:mybatis/payment/*Mapper.xml"));
            sessionFactory.setConfigLocation(new DefaultResourceLoader()
                    .getResource("mybatis/mybatis-config.xml"));
            return sessionFactory.getObject();
        } catch (Exception e) {
            log.error("Could not confiure mybatis session factory", e);
            return null;
        }
    }

    @Bean(name = "paymentSqlSessionTemplate")
    public SqlSessionTemplate paymentSqlSessionTemplate(@Qualifier("paymentSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name="paymentTransactionManager")
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(paymentDataSource);
    }
}