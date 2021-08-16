#消息主题表
CREATE TABLE `platform_mq_topic` (
  `series` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `msg_id` bigint(20) NOT NULL COMMENT '自定义唯一业务主键',
  `topic_alias` varchar(150) DEFAULT NULL,
  `topic` varchar(150) NOT NULL COMMENT '消息主题(不能为空,与tag构成唯一主键)',
  `tag_alias` varchar(150) DEFAULT NULL,
  `tag` varchar(150) NOT NULL DEFAULT '0',
  `from_system` varchar(20) NOT NULL COMMENT '消息来源什么系统',
  `create_date_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `create_user_name` varchar(10) NOT NULL COMMENT '创建人名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注说明信息',
  `consumer_type` smallint(6) NOT NULL DEFAULT '0' COMMENT '消费者类型 1 表示dubbo 2 表示http',
  `consumer_series` bigint(20) DEFAULT NULL COMMENT '消费者编号',
  `consumer_instance_count` smallint(6) DEFAULT '1' COMMENT '消费者实例数',
  `cancelsign` varchar(1) DEFAULT 'N',
  `consumer_interval` bigint(20) DEFAULT '0' COMMENT '消费者拉取数据间隔，单位毫秒，默认0',
  `retries` int(10) DEFAULT '3' COMMENT '重试次数',
  `retries_test` int(10) DEFAULT '3' COMMENT '测试环境的重试次数',
  `retries_develop` int(10) DEFAULT '3' COMMENT '开发环境下的重试次数',
  `task_target` varchar(30) DEFAULT NULL COMMENT '这是每个任务的消费指标，用来扩充服务器用',
  `is_distinct` varchar(2) DEFAULT 'N' COMMENT '是否去重',
  `mess_batch_num` int(5) DEFAULT '1' COMMENT '消息批量拿取数量默认1',
  `consumer_thread_min` int(4) DEFAULT '3' COMMENT '消费者最小线程数',
  `consumer_thread_max` int(4) DEFAULT '5' COMMENT '消费者最大线程数',
  `tenant_num_id` bigint(7) NOT NULL DEFAULT '1' COMMENT '租户ID',
  `wether_order_mess` varchar(1) DEFAULT 'N' COMMENT '是否顺序消费',
  `retry_interval` varchar(90) DEFAULT '' COMMENT '重试间隔(只需要输入以下集合中延时时间的index即可)0，1s，5s，10s，30s，1m，2m，3m，4m，5m，6m，7m，8m，9m，10m，20m，30m，1h，2h',
  `system_num_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '系统ID',
  `wether_insertdb` varchar(1) DEFAULT 'Y' COMMENT '是否插入本地数据库',
  `correct_codes` varchar(140) DEFAULT '' COMMENT '业务项目返回这些code视为消费正确',
  `mq_queue` bigint(10) DEFAULT '8' COMMENT '队列数量',
  `wether_handle_failedmess` varchar(1) DEFAULT 'Y' COMMENT '是否处理消费失败的消息',
  `retry_max` bigint(1) DEFAULT '20' COMMENT '最大重试次数',
  `zk_data_sign` bigint(1) DEFAULT '0' COMMENT 'zk的dataSign,默认都是0，如果类似Pos这样的项目发消息没有dataSign需要用这个字段去区分zk的地址，0为线上地址，1为线下地址',
  `mq_namesrv` varchar(80) DEFAULT NULL COMMENT 'mq路由',
  `consumer_ip` varchar(80) DEFAULT NULL COMMENT '消费者IP',
  `consumer_distinct` char(1) DEFAULT 'Y',
  `consumer_pull_delay` bigint(10) DEFAULT NULL COMMENT '消费端拉取延时(单位毫秒)最多延时一分钟',
  `datasouce_name` varchar(100) DEFAULT '' COMMENT '数据源名称',
  `_mycat_op_time` bigint(20) DEFAULT NULL COMMENT '全局表保存修改时间戳的字段名',
  PRIMARY KEY (`series`),
  UNIQUE KEY `platform_mq_topic_uq1` (`topic`,`tag`),
  UNIQUE KEY `platform_mq_topic_uq2` (`msg_id`),
  KEY `platform_mq_topic_uq3` (`tag`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

#一对多topic(父topic)
CREATE TABLE `platform_mq_topic_many` (
  `series` bigint(20) NOT NULL DEFAULT '0' COMMENT '行号',
  `topic` varchar(50) NOT NULL COMMENT '一对多的topic',
  `remark` varchar(80) DEFAULT NULL COMMENT '备注',
  `tenant_num_id` int(11) NOT NULL DEFAULT '0' COMMENT '租户ID',
  `create_dtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `cancelsign` varchar(3) DEFAULT 'N' COMMENT '取消标识',
  `_dble_op_time` bigint(20) DEFAULT NULL COMMENT 'field for checking consistency',
  PRIMARY KEY (`series`),
  UNIQUE KEY `platform_mq_topic_many_uq1` (`topic`,`tenant_num_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

#父子topic关联表
CREATE TABLE `platform_mq_topic_relation` (
  `series` bigint(20) NOT NULL DEFAULT '0' COMMENT '行号',
  `topic_father_series` bigint(20) NOT NULL COMMENT '一对多的topic',
  `topic_son_series` bigint(20) DEFAULT NULL COMMENT '备注',
  `create_dtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `_dble_op_time` bigint(20) DEFAULT NULL COMMENT 'field for checking consistency',
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

#消息发送日志表
CREATE TABLE `sys_rocket_mq_send_log` (
  `series` bigint(18) NOT NULL COMMENT '行号',
  `message_id` varchar(100) DEFAULT '' COMMENT 'rocket mq 自身产生消息id',
  `message_key` varchar(100) DEFAULT '' COMMENT '业务系统生成的唯一序号',
  `message_body` mediumtext,
  `message_topic` varchar(50) DEFAULT '' COMMENT '消息主题',
  `message_tag` varchar(50) DEFAULT '' COMMENT '消息标签',
  `message_name_addr` varchar(100) DEFAULT '' COMMENT 'mq的命名服务器地址',
  `producer_name` varchar(100) DEFAULT '' COMMENT '消息生产者名称',
  `create_dtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_updtme` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `create_user_id` bigint(18) unsigned DEFAULT '1' COMMENT '创建用户',
  `last_update_user_id` bigint(18) unsigned DEFAULT '1' COMMENT '更新用户',
  `cancelsign` char(1) DEFAULT 'N' COMMENT '删除',
  `fail_detail` text COMMENT '消息失败的原因',
  `tenant_num_id` int(11) unsigned DEFAULT '0' COMMENT '租户ID',
  `data_sign` tinyint(4) unsigned DEFAULT '0' COMMENT '0: 正式  1：测试',
  `workflow_id` bigint(20) DEFAULT NULL COMMENT '需要被流程管理流程设计id',
  `instance_id` bigint(20) DEFAULT NULL COMMENT '流程实例id',
  `step_id` int(20) DEFAULT NULL COMMENT '步骤id',
  `from_system` varchar(100) DEFAULT '' COMMENT '来源系统名称',
  `msg_status` int(5) unsigned DEFAULT '0' COMMENT '消息状态：1 发送成功，2 预发送，3 发送失败，4,取消发送',
  `consumer_success` varchar(2) DEFAULT '' COMMENT '消费成功标识，默认是空，成功变成Y,失败是N',
  `retry_times` bigint(7) DEFAULT '0' COMMENT '重试次数',
  `consumer_success_time` datetime DEFAULT NULL COMMENT '消费成功时间',
  `task_target` varchar(30) DEFAULT NULL COMMENT '这是每个任务的消费指标，用来扩充服务器用',
  `order_mess_flag` bigint(2) DEFAULT '0' COMMENT '顺序消息表示，为1就是顺序消息，0是正常消息',
  `response_detail` text COMMENT '返回值详细信息',
  `next_retry_interval` varchar(12) DEFAULT NULL COMMENT '下一次的重试间隔',
  `client_ip` varchar(30) DEFAULT NULL COMMENT '客户端IP',
  PRIMARY KEY (`series`),
  KEY `ix_sys_rocket_mq_send_log_1` (`message_key`),
  KEY `ix_sys_rocket_mq_send_log_2` (`message_topic`),
  KEY `ix_sys_rocket_mq_send_log_3` (`message_tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='消息发送日志';

#消息发送日志历史表
CREATE TABLE `sys_rocket_mq_send_log_history` (
  `series` bigint(18) NOT NULL COMMENT '行号',
  `message_id` varchar(100) DEFAULT '' COMMENT 'rocket mq 自身产生消息id',
  `message_key` varchar(100) DEFAULT '' COMMENT '业务系统生成的唯一序号',
  `message_body` mediumtext,
  `message_topic` varchar(50) DEFAULT '' COMMENT '消息主题',
  `message_tag` varchar(50) DEFAULT '' COMMENT '消息标签',
  `message_name_addr` varchar(100) DEFAULT '' COMMENT 'mq的命名服务器地址',
  `producer_name` varchar(100) DEFAULT '' COMMENT '消息生产者名称',
  `create_dtme` datetime DEFAULT NULL COMMENT '创建时间',
  `last_updtme` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `create_user_id` bigint(18) unsigned DEFAULT '1' COMMENT '创建用户',
  `last_update_user_id` bigint(18) unsigned DEFAULT '1' COMMENT '更新用户',
  `cancelsign` char(1) DEFAULT 'N' COMMENT '删除',
  `fail_detail` text COMMENT '消息失败的原因',
  `tenant_num_id` int(11) unsigned DEFAULT '0' COMMENT '租户ID',
  `data_sign` tinyint(4) unsigned DEFAULT '0' COMMENT '0: 正式  1：测试',
  `workflow_id` bigint(20) DEFAULT NULL COMMENT '需要被流程管理流程设计id',
  `instance_id` bigint(20) DEFAULT NULL COMMENT '流程实例id',
  `step_id` int(20) DEFAULT NULL COMMENT '步骤id',
  `from_system` varchar(100) DEFAULT '' COMMENT '来源系统名称',
  `msg_status` int(5) unsigned DEFAULT '0' COMMENT '消息状态：1 发送成功，2 预发送，3 发送失败，4,取消发送',
  `consumer_success` varchar(2) DEFAULT '' COMMENT '消费成功标识，默认是空，成功变成Y,失败是N',
  `retry_times` bigint(7) DEFAULT '0' COMMENT '重试次数',
  `consumer_success_time` datetime DEFAULT NULL COMMENT '消费成功时间',
  `task_target` varchar(30) DEFAULT NULL COMMENT '这是每个任务的消费指标，用来扩充服务器用',
  `order_mess_flag` bigint(2) DEFAULT '0' COMMENT '顺序消息表示，为1就是顺序消息，0是正常消息',
  `response_detail` text COMMENT '返回值详细信息',
  `next_retry_interval` varchar(12) DEFAULT '0' COMMENT '下一次的重试间隔',
  `client_ip` varchar(30) DEFAULT '' COMMENT '客户端IP',
  `_dble_op_time` bigint(20) DEFAULT NULL COMMENT 'field for checking consistency',
  KEY `idx_sys_rocket_mq_send_log_4` (`message_key`),
  KEY `idx_sys_rocket_mq_send_log_1` (`message_topic`),
  KEY `idx_sys_rocket_mq_send_log_5` (`message_tag`),
  KEY `idx_sys_rocket_mq_send_log_2` (`msg_status`),
  KEY `idx_sys_rocket_mq_send_log_6` (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='消息发送历史表';

#消息回查客户端series记录表
CREATE TABLE `sys_msg_trans_refind_id` (
  `series` bigint(18) NOT NULL COMMENT '行号',
  `system_name` varchar(50) DEFAULT '' COMMENT '系统名',
  `ip_address` varchar(30) DEFAULT '' COMMENT '业务系统生成的唯一序号',
  `topic` varchar(50) DEFAULT '' COMMENT '消息topic',
  `tag` varchar(50) DEFAULT '' COMMENT '消息tag',
  `create_dtme` varchar(50) DEFAULT '' COMMENT '创建时间',
  `last_updtme` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `data_sign` bigint(10) NOT NULL COMMENT '正式测试标识0为正式，1为测试',
  `txc` varchar(50) DEFAULT '' COMMENT '分布式事务ID',
  `tenant_num_id` bigint(10) NOT NULL COMMENT '租户ID ',
  `has_confirm` varchar(1) DEFAULT NULL COMMENT '是否确认',
  `_dble_op_time` bigint(20) DEFAULT NULL COMMENT 'field for checking consistency',
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息回查客户端series记录表';

#消息加密表(对消息内容加密 实现消息去重)
CREATE TABLE `sys_rocket_mq_message_encrylog` (
  `msgmd` varchar(50) NOT NULL COMMENT '消息MD5内容',
  `msg_series` bigint(18) NOT NULL COMMENT '行号',
  `data_sign` bigint(3) DEFAULT NULL COMMENT '线上标识，0为线上，其余都为线下',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  `create_dtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `topic` varchar(70) DEFAULT NULL COMMENT '消息topic',
  `tag` varchar(70) DEFAULT '' COMMENT '消息tag',
  `tenant_num_id` bigint(3) DEFAULT '1' COMMENT '租户号',
  PRIMARY KEY (`msg_series`),
  KEY `idx_sys_rocket_mq_message_encrylog_1` (`msgmd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

#定时消息日志表
CREATE TABLE `sys_rocket_mq_job_log` (
  `series` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '行号',
  `message_key` varchar(100) DEFAULT '' COMMENT '业务系统生成的唯一序号',
  `message_detail` text,
  `message_topic` varchar(50) DEFAULT '' COMMENT '消息主题',
  `message_tag` varchar(50) DEFAULT '' COMMENT '消息标签',
  `system_id` bigint(5) DEFAULT '0' COMMENT '系统编号',
  `cron` varchar(200) DEFAULT '' COMMENT 'cron时间表达式',
  `client_ip` varchar(50) DEFAULT '' COMMENT '发送者ip',
  `create_dtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_updtme` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `tenant_num_id` int(11) unsigned DEFAULT '0' COMMENT '租户ID',
  `data_sign` tinyint(4) unsigned DEFAULT '0' COMMENT '0: 正式 1：测试',
  `cancelsign` varchar(1) DEFAULT 'N' COMMENT '取消标识',
  `error_log` varchar(500) DEFAULT '' COMMENT '''发送日志''',
  `send_log_series` bigint(20) DEFAULT '0' COMMENT 'sendlog的series',
  PRIMARY KEY (`series`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='定时消息日志';

#redis过期键表
CREATE TABLE `redis_expired_key_topic` (
  `series` bigint(20) unsigned NOT NULL,
  `redis_key_head` varchar(60) NOT NULL DEFAULT '' COMMENT 'redis的key开头部分',
  `topic` varchar(60) NOT NULL DEFAULT '' COMMENT '消息主题(不能为空)',
  `tag` varchar(60) NOT NULL DEFAULT '' COMMENT '消息的tag',
  `cancelsign` varchar(1) DEFAULT 'N' COMMENT '取消标识,N代表不取消,Y代表取消',
  `remark` varchar(60) DEFAULT '' COMMENT '备注',
  `create_user` varchar(30) DEFAULT '' COMMENT '创建人',
  `create_dtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '数据插入时间',
  PRIMARY KEY (`series`),
  UNIQUE KEY `redis_expired_key_topic1` (`redis_key_head`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#事务失败日志表
CREATE TABLE `sys_transation_failed_log` (
  `series` bigint(18) NOT NULL COMMENT '行号',
  `message_id` varchar(100) DEFAULT '' COMMENT 'rocket mq 自身产生消息id',
  `message_key` varchar(100) DEFAULT '' COMMENT '业务系统生成的唯一序号',
  `message_body` text,
  `message_topic` varchar(50) DEFAULT '' COMMENT '消息主题',
  `message_tag` varchar(50) DEFAULT '' COMMENT '消息标签',
  `message_name_addr` varchar(100) DEFAULT '' COMMENT 'mq的命名服务器地址',
  `producer_name` varchar(100) DEFAULT '' COMMENT '消息生产者名称',
  `create_dtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_updtme` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `create_user_id` bigint(18) unsigned DEFAULT '1' COMMENT '创建用户',
  `last_update_user_id` bigint(18) unsigned DEFAULT '1' COMMENT '更新用户',
  `cancelsign` char(1) DEFAULT 'N' COMMENT '删除',
  `fail_detail` text COMMENT '消息失败的原因',
  `tenant_num_id` int(11) unsigned DEFAULT '0' COMMENT '租户ID',
  `data_sign` tinyint(4) unsigned DEFAULT '0' COMMENT '0: 正式  1：测试',
  `workflow_id` bigint(20) DEFAULT NULL COMMENT '需要被流程管理流程设计id',
  `instance_id` bigint(20) DEFAULT NULL COMMENT '流程实例id',
  `step_id` int(20) DEFAULT NULL COMMENT '步骤id',
  `from_system` varchar(100) DEFAULT '' COMMENT '来源系统名称',
  `msg_status` int(5) unsigned DEFAULT '0' COMMENT '消息状态：1 发送成功，2 预发送，3 发送失败，4,取消发送',
  `consumer_success` varchar(2) DEFAULT '' COMMENT '消费成功标识，默认是空，成功变成Y,失败是N',
  `retry_times` bigint(7) DEFAULT '0' COMMENT '重试次数',
  `consumer_success_time` datetime DEFAULT NULL COMMENT '消费成功时间',
  `task_target` varchar(30) DEFAULT NULL COMMENT '这是每个任务的消费指标，用来扩充服务器用',
  `order_mess_flag` bigint(2) DEFAULT '0' COMMENT '顺序消息表示，为1就是顺序消息，0是正常消息',
  `response_detail` text COMMENT '返回值详细信息',
  `next_retry_interval` varchar(12) DEFAULT NULL COMMENT '下一次的重试间隔',
  `client_ip` varchar(30) DEFAULT NULL COMMENT '客户端IP',
  PRIMARY KEY (`series`),
  KEY `ix_sys_rocket_mq_send_log_1` (`message_key`) USING BTREE,
  KEY `ix_sys_rocket_mq_send_log_2` (`message_topic`) USING BTREE,
  KEY `ix_sys_rocket_mq_send_log_3` (`message_tag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#顺序消息发送日志表
CREATE TABLE `sys_order_message_send_log` (
  `series` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '行号',
  `message_key` varchar(100) DEFAULT '' COMMENT '业务系统生成的唯一序号',
  `message_body` text,
  `message_topic` varchar(50) DEFAULT '' COMMENT '消息主题',
  `message_tag` varchar(50) DEFAULT '' COMMENT '消息标签',
  `create_dtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_updtme` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `tenant_num_id` int(11) unsigned DEFAULT '0' COMMENT '租户ID',
  `data_sign` tinyint(4) unsigned DEFAULT '0' COMMENT '0: 正式  1：测试',
  `from_system` varchar(100) DEFAULT '' COMMENT '来源系统名称',
  `order_message_group_id` bigint(18) DEFAULT '0' COMMENT '顺序消息小组id',
  `order_id` bigint(20) DEFAULT '0' COMMENT '用来排序的id',
  `client_ip` varchar(30) DEFAULT '' COMMENT '生产者ip地址',
  `cancel_sign` varchar(1) DEFAULT 'N' COMMENT '取消标识',
  `send_sign` varchar(1) DEFAULT 'N' COMMENT '发送标识，没有发送是N，已经发送是Y',
  PRIMARY KEY (`series`),
  KEY `ix_sys_order_message_send_log_1` (`order_message_group_id`) USING BTREE,
  KEY `ix_sys_order_message_send_log_2` (`message_tag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='顺序消息发送日志';

