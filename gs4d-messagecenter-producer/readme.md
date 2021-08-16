消息发送工具：

    (前提：发送消息需要指定主题和标签(topic和tag)以及消息内容)

    消息预发送 -> 
        组装数据并插入到sys_rocket_mq_send_log(消息发送日志表)，
        缓存消息发送日志对象sysRocketMqSendLog， key为series，
        将series保存在线程局部变量(ThreadLocal)中,如有需要，可以额外存一份series数据到sys_msg_trans_refind_id(消息回查客户端series记录表)。
     
    预发送消息确认发送 ->
         从线程局部变量中获取保存的预发送消息的series，
         根据series从缓存中获取消息发送日志对象sysRocketMqSendLog，查不到则从表中获取，
         【发送消息】【此时是真正的通过mq生产者发送消息至消息代理broker】，
         清空线程局部变量。
    
    预发送消息取消发送 ->
        从线程局部变量中获取保存的预发送消息的series，
        根据series删除缓存中保存的消息发送日志对象sysRocketMqSendLog，
        将消息发送日志数据保存到sys_rocket_mq_send_log(消息发送日志历史表)，并删除sys_rocket_mq_send_log(消息发送日志表)的记录，
        清空线程局部变量。
        
    立即发送消息 ->   
        如果消息不需要插入数据库，则直接【发送消息】;【platform_mq_topic(消息主题表)保存了相关的配置，并且进行了缓存(内存)】
        否则：
            根据配置进行消息内容滤重，
            组装数据并插入到sys_rocket_mq_send_log(消息发送日志表)，
            缓存消息发送日志对象sysRocketMqSendLog， key为series，
            根据series从缓存中获取消息发送日志对象sysRocketMqSendLog，查不到则从表中获取，
             【发送消息】【此时是真正的通过mq生产者发送消息至消息代理broker】。
        【立即发送消息 相当于 消息预发送 和 预发送消息确认发送 两步的结合】     
        
    预发送一对多消息 ->
        【同一个topic可以对应多个tag 一对多消息就是发送给同一个topic的所有tag】   
        【同：消息预发送】【一对多预发送消息的确认和取消同上】
         
    立即发送一对多消息 ->
        【同：立即发送消息】
    
    ####
        
    确认事务消息 ->
    
    取消事务消息 ->
    
    #####
    
    预发送顺序流消息 ->
    
    立即发送顺序流消息 ->
    
    添加顺序流消息 ->    
        
    ###
    
    立即发送定时消息 ->
        组装数据并插入到sys_transation_failed_log(事务失败日志表)  ???
    
    立即发送定时消息 ->  
        组装定时消息日志对象sysRocketMqJobLog并插入到sys_rocket_mq_job_log(定时消息日志表)，
        创建job任务、trigger触发器以及scheduler调度器，并启动调度器，由调度器定时执行job任务，【quartz框架】
        【任务实现：
            任务触发时，根据series查询sys_rocket_mq_job_log(定时消息日志表)的记录，【series是创建对应job的时候保存在job中的业务字段】
            如果记录的cancelsign取消标识为已取消，则暂停触发器并取消删除任务；
            否则：
                根据配置进行消息内容滤重，
                组装数据并插入到sys_rocket_mq_send_log(消息发送日志表)，
                缓存消息发送日志对象sysRocketMqSendLog， key为series，
                根据series从缓存中获取消息发送日志对象sysRocketMqSendLog，查不到则从表中获取，
                 【发送消息】【此时是真正的通过mq生产者发送消息至消息代理broker】。
                更新sys_rocket_mq_job_log(定时消息日志表)的记录的send_log_series等信息
        】
        
    取消定时消息 ->       
        更新sys_rocket_mq_job_log(定时消息日志表)对应series的记录的取消表示为已取消
        【这里不会直接操作暂停触发器以及删除任务，而是等到任务触发的时候再处理。直接在任务实现逻辑中加分布式锁控制并发问题，此处就不需要重复处理】



发送消息 核心：