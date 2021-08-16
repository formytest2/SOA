##通用查询表
CREATE TABLE `common_query` (
  `series` bigint(18) NOT NULL COMMENT '行号',
  `sql_name` varchar(1000) NOT NULL COMMENT 'sql执行描述',
  `sql_id` varchar(1000) NOT NULL COMMENT 'sql编号',
  `sql_content` text NOT NULL COMMENT 'sql内容',
  `param_content` text NOT NULL COMMENT 'sql参数',
  `jdbc_name` varchar(100) NOT NULL COMMENT 'jdbc连接名字',
  `create_dtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_updtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `create_user_id` bigint(20) DEFAULT '0' COMMENT '用户',
  `last_update_user_id` bigint(20) DEFAULT '0' COMMENT '更新用户',
  `cancel_sign` char(1) NOT NULL DEFAULT 'N' COMMENT 'n为可使用,y为不使用',
  `tenant_num_id` int(11) NOT NULL DEFAULT '0' COMMENT '租户id',
  `data_sign` tinyint(4) DEFAULT '0' COMMENT '0是正式,1是测试',
  `db_type` varchar(100) DEFAULT 'MySQL' COMMENT '数据库类型',
  `annotate_prefix` varchar(50) DEFAULT NULL,
  `sub_sql_id` varchar(256) DEFAULT '',
  `no_data_exception` char(1) DEFAULT 'N' COMMENT '无数据是否报错，Y是，N不报错',
  `cache_sign` int(3) DEFAULT '0' COMMENT '缓存使用标识0:不使用1：直接缓存，2缓存服务',
  `method_name` varchar(100) DEFAULT '' COMMENT '方法名称',
  `cache_live_time` int(11) DEFAULT '0' COMMENT '缓存存活时间',
  `return_handle_content` text COMMENT '返回参数 处理',
  `_dble_op_time` bigint(20) DEFAULT NULL COMMENT 'field for checking consistency',
  `cancelsign` char(1) DEFAULT 'N' COMMENT 'n为可使用,y为不使用',
  `remark` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

#系统数据源名称表
CREATE TABLE `ex_arc_doc_system` (
  `system_num_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '系统别',
  `tenant_num_id` int(11) NOT NULL DEFAULT '0' COMMENT '租户ID',
  `data_sign` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: 正式  1：测试',
  `system_name` varchar(50) NOT NULL COMMENT '系统名称',
  `create_dtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_updtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `create_user_id` bigint(20) DEFAULT '0' COMMENT '用户',
  `last_update_user_id` bigint(20) DEFAULT '0' COMMENT '更新用户',
  `datasouce_name` varchar(100) DEFAULT '' COMMENT '系统默认数据源名称',
  `_dble_op_time` bigint(20) DEFAULT NULL COMMENT 'field for checking consistency',
  PRIMARY KEY (`system_num_id`,`data_sign`,`tenant_num_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;



