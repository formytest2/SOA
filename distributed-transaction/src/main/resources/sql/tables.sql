CREATE TABLE `transaction_log` (
  `transaction_id` bigint(20) unsigned NOT NULL COMMENT '分布式事务ID',
  `start_dtme` datetime DEFAULT NULL COMMENT '分布式事务开始时间',
  `end_dtme` datetime DEFAULT NULL COMMENT '分布式事务结束时间',
  `ip_address` varchar(20) NOT NULL COMMENT 'ip地址',
  `transaction_state` bigint(1) NOT NULL COMMENT '分布式事务状态0为正在执行，1为正常结束，2为异常结束',
  `transaction_sign` varchar(1) NOT NULL COMMENT '分布式事务核销标识 N为失败，Y为成功',
  `From_system` varchar(30) DEFAULT '' COMMENT '分布式事务入口项目名',
  `Method_name` varchar(100) DEFAULT '' COMMENT '分布式事务入口方法名',
  `Transaction_rollback_flag` tinyint(1) DEFAULT '0' COMMENT '事务回滚标识 0为并行回滚，1为串行回滚',
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

CREATE TABLE `transaction_sql_log` (
  `series` bigint(18) NOT NULL,
  `transaction_id` bigint(20) unsigned NOT NULL COMMENT '分布式事务ID',
  `transaction_db_id` bigint(20) unsigned NOT NULL COMMENT '数据库事务ID',
  `source_db` varchar(30) NOT NULL COMMENT '数据原数据库',
  `table_name` varchar(100) DEFAULT NULL COMMENT '数据表名字',
  `commit_sql_dtme` datetime DEFAULT NULL COMMENT '正向sql执行结束时间',
  `rollback_sql_dtme` datetime DEFAULT NULL COMMENT '反向sql执行结束时间',
  `sql` text COMMENT '正向sql语句',
  `sql_param` text COMMENT 'sql语句中的参数（sql参数的json）',
  `transaction_sign` varchar(1) NOT NULL COMMENT '分布式事务核销标识 N为失败，Y为成功',
  `transaction_error_log` text,
  `sql_level` int(3) DEFAULT '0' COMMENT 'sql执行级别，等级越高sql的回滚最先执行',
  `sql_is_out_time` varchar(1) DEFAULT 'N' COMMENT '是否属于事务超时，是的话Y，不是的话N',
  `sql_key_value` mediumtext COMMENT '数据库主键内容',
  `sql_id` varchar(200) DEFAULT '' COMMENT 'sqlId',
  `biz_redis_key` varchar(300) DEFAULT '' COMMENT '业务中的redis锁',
  `sql_type` bigint(1) DEFAULT '0' COMMENT '原sql类型，0为insert，1为update，2为delete',
  `txc` varchar(50) DEFAULT NULL,
  `sql_status` bigint(1) DEFAULT '0' COMMENT 'sql状态0超时，1为需要回滚，2为不用回滚',
  `biz_redis_value` varchar(300) DEFAULT '' COMMENT 'redis的内容',
  `sql_timeout` bigint(20) DEFAULT NULL COMMENT 'sql超时时间',
  `shard` varchar(100) DEFAULT '' COMMENT '分库字段',
  `result_num` bigint(10) DEFAULT '0' COMMENT '影响行数',
  PRIMARY KEY (`series`),
  KEY `ix_transaction_sql_log_1` (`transaction_db_id`) USING BTREE,
  KEY `ix_transaction_sql_log_2` (`transaction_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `transaction_shared` (
  `series` bigint(20) unsigned NOT NULL COMMENT '序列号',
  `db_name` varchar(50) DEFAULT NULL COMMENT 'schema名字',
  `table_name` varchar(50) DEFAULT NULL COMMENT '表名字',
  `shared_column` varchar(50) NOT NULL COMMENT '分库字段名字',
  `CREATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `cancelsign` varchar(1) DEFAULT 'N' COMMENT '取消标识，N是在用的，Y是取消的',
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分布式事务分库字段配置表';

CREATE TABLE `transaction_rollback_flag` (
  `series` bigint(18) NOT NULL,
  `transaction_key` varchar(80) NOT NULL COMMENT '分布式事务key',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`series`),
  KEY `ix_transaction_rollback_flag_1` (`transaction_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
