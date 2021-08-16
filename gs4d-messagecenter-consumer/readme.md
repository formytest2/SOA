消费者

    1.首先是初始化，初始化platform_mq_topic表中配置的所有topic对应的consumer
        1). dubbo消费者(platform_mq_dubbo_consumer)的初始化
            dubbo消费者需要初始化对应的dubbo reference
        2). http消费者(platform_mq_http_consumer)的初始化
        3). mq消费者的初始化：
            基于rocketmq：
                创建相应的DefaultMQPushConsumer对象并设置相应的监听器，
                监听器实现MessageListenerConcurrently或MessageListenerOrderly
                
            基于阿里云openservice：
                创建相应的Consumer或OrderConsumer并注册相应的监听器
                监听器实现MessageListener或MessageOrderListener或BatchMessageListener
                
            (区分并发消费以及顺序消费、单条消费以及批量消费)
        
    2.消费
        如果是dubbo消费者 则调用对应dubbo reference；如果是http消费者 则通过http工具调用对应接口。
        
        消费成功
            删除消息发送日志sys_rocket_mq_send_log 并将数据存到消息发送历史sys_rocket_mq_send_log_history
        消费失败
            更新消息发送日志sys_rocket_mq_send_log，记录消费时间 重试次数等信息，返回给broker对应的消费结果状态(如rocketmq的RECONSUME_LATER)
            如果超过最大重试次数，则将数据记录到sys_rocket_mq_consumer_failedlog表        
            
            
    3.其他及扩展
        仅实现了基于tag的消息过滤 不支持sql过滤和基于类模式的过滤；
        
        对于消费失败的数据(sys_rocket_mq_consumer_failedlog表的数据)做监控报警 todo；
        
        顺序消费的处理 todo 
            顺序消息要保证顺序生产以及顺序消费，
            简单来说可以设计成只有一个队列且该主题的消费组中只有一个消费者
        
        消息消费及工作流的结合 todo    
         