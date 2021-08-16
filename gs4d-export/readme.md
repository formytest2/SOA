可配置的数据导出

    数据导出，当然不仅仅局限于查询，insert、update等操作也能处理
    可以实现通用查询，降低服务之间的耦合性，高频查询还可以结合缓存；
    对于一些不定期新增或修改的报表sql，用可配置化的方式可以避免每次的编码操作，直接通过网关路由到导出服务即可快速响应数据
   
     
    sql_content：sql模板，定义sql的基础内容，?为参数值占位符，[]中的条件为可选条件
        如：select name from user where id = ? and [ data_sign = ? ];
    param_content：定义sql字段(类型、默认值、是否模糊查询等)  
      
    核心逻辑就是把入参按照param_content的定义转换成sql参数，组装给sql_content成完成的sql并执行。
    对于查询最基本的实现包括：模糊查询、in、分页
      
   
    扩展功能就是可以实现select子查询、insert into select等
     
    此外可以结合缓存、sequence、多数据源等功能 
     
    还可以设置其他一些导入导出功能(需要http请求的)、获取监控数据的功能 todo
    
    作为一个导出服务，还应该支持导出文件等形式 todo
  
   
  
 
 
定义param_content  
&emsp;&emsp;&emsp;字段规则以jsonObject的形式配置，如下。多个字段的规则按照jsonArray的形式

    NAME：字段名  NAME字段本身是必填的
    TYPE：字段类型 支持NUMBER、STRING、DECIMAL、DATETIME
    MUSTHAVE：是否必填
      
    DEFAULT：默认值         
    DEFAULT_FLAG：是否使用默认值(这个字段只要有值就不会使用默认值 todo 改造)
        注意 如果DEFAULT和DEFAULT_FLAG均未配置 则上面四种类型的默认值分别为：0  ' '  0.0  'NULL_DATE_DEFAULTVALUE'
     
    FUZZYQUERY：模糊查询 支持before(前模糊)、after(后模糊)、around(全模糊)
     
    QTYPE：in
        当使用in语句时 参数值可以是逗号分隔的多值字符串
     
    
    FUNCTION：功能，如：SEQ、AUTO_SEQ、SHARD_ID
            SEQ、AUTO_SEQ是为insert语句设置主键时用的；
            SHARD_ID是为select语句需要分片号时用的
            
    SEQ_NAME：当FUNCTION=SEQ或AUTO_SEQ时使用
     
    mapfile：