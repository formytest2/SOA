GS4D
			
    一个基于Dubbo微服务系统的服务治理SOA实践
    
    
核心功能 <br/>
&emsp;基础功能：<br/>
&emsp;&emsp;通用bean：<br/>
&emsp;&emsp;常用工具类：<br/>
&emsp;&emsp;&emsp;json工具类等<br/>
&emsp;&emsp;异常处理：<br/>
&emsp;&emsp;&emsp;异常码的定义与查询<br/>
&emsp;&emsp;&emsp;异常处理工具类 <br/>
&emsp;&emsp;&emsp;&emsp;解析出特定异常的可读信息<br/>
&emsp;&emsp;&emsp;&emsp;异常的封装(特定异常的特殊处理，如rpc服务调用异常)<br/>
&emsp;&emsp;&emsp;&emsp;通过异常码重新抛出异常<br/>
                    
&emsp;&emsp;&emsp;思想：调用链上每个调用者自行决定业务异常如何处理，<br/>
&emsp;&emsp;&emsp;但是每个服务接口最终都要把异常封装后返回(如果有异常的话)<br/>
                
            
&emsp;可配置(动态新增api)的网关功能：<br/>
&emsp;&emsp;请求认证与授权<br/>
&emsp;&emsp;基于注解的参数校验<br/>
&emsp;&emsp;请求路由<br/>
&emsp;&emsp;文档功能：<br/>
&emsp;&emsp;&emsp;&emsp;对于dubbo微服务 提供api工具(web界面)<br/>
&emsp;&emsp;&emsp;&emsp;&emsp;查看所有接口<br/>
&emsp;&emsp;&emsp;&emsp;&emsp;手动调用接口<br/>
&emsp;&emsp;&emsp;&emsp;&emsp;生成静态文档 todo<br/>
&emsp;&emsp;统计        <br/>
            
&emsp;&emsp;多数据源：   <br/>
&emsp;&emsp;&emsp;分库分表必然要处理多数据源的问题<br/>
        
&emsp;&emsp;可配置的数据导出(通用查询)功能[gs4d-export](https://github.com/bluecatlee/GS4D/blob/main/gs4d-export)<br/>
&emsp;&emsp;&emsp;对于经常复用的sql(基础配置及常用业务的逻辑)单独配置成表，<br/>
&emsp;&emsp;&emsp;并存储于一个数据库中，由一个通用的工具(拥有所有数据源)去进行查询。<br/>
&emsp;&emsp;&emsp;避免在不同的项目中引入大量非核心业务的数据源。<br/>
&emsp;&emsp;&emsp;另一个方案是聚合层拥有所有数据源，但是底层服务很多核心逻辑只能上浮到聚合层进行处理。<br/>
        
&emsp;分布式事务：<br/>
        
&emsp;可配置的通用缓存功能：<br/>
&emsp;&emsp;参考：gs4d-cache-api gs4d-cache-client [gs4d-cache](https://github.com/bluecatlee/GS4D/blob/main/gs4d-cache)<br/>

&emsp;消息中心：<br/>
    &emsp;&emsp;生产者：[gs4d-messagecenter-producer](https://github.com/bluecatlee/GS4D/blob/main/gs4d-messagecenter-producer)<br/>
    &emsp;&emsp;消费者：[gs4d-messagecenter-consumer](https://github.com/bluecatlee/GS4D/blob/main/gs4d-messagecenter-consumer)<br/>
    
    
***********************************
业务系统剥离出去  

&emsp;支付中台及支付系统的设计：<br/>
&emsp;&emsp;参考：gs4d-pay-api [gs4d-pay](https://github.com/bluecatlee/GS4D/blob/main/gs4d-pay)<br/>

&emsp;促销系统的设计：<br/>
&emsp;&emsp;参考：[Promot](https://github.com/bluecatlee/Promot)<br/>