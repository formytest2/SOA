##缓存方法-表定义 表
CREATE TABLE `ec_cache_method_schema_define` (
  `series` bigint(20) NOT NULL DEFAULT '0' COMMENT '行号',
  `tenant_num_id` int(11) NOT NULL DEFAULT '0' COMMENT '租户ID',
  `data_sign` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: 正式  1：测试',
  `sub_system` varchar(30) DEFAULT NULL COMMENT '子系统',
  `method_name` varchar(50) NOT NULL COMMENT '方法名称',
  `sql_content` varchar(1500) NOT NULL COMMENT 'sql内容',
  `db` varchar(50) DEFAULT NULL COMMENT '数据库',
  `cache_method` varchar(100) DEFAULT NULL,
  `cache_multi_col` varchar(200) DEFAULT NULL COMMENT '缓存键拼装,用#隔开',
  `ttl` bigint(20) DEFAULT '0' COMMENT '缓存存活时间(单位秒),0 永远有效',
  `list_sign` tinyint(4) DEFAULT '0' COMMENT '缓存类别 0:非列表 1:列表',
  `create_dtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_updtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `create_user_id` bigint(20) DEFAULT '0' COMMENT '用户',
  `last_update_user_id` bigint(20) DEFAULT '0' COMMENT '更新用户',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `allow_list_empty_sign` tinyint(4) DEFAULT '0' COMMENT '允许列表为空，0:不允许 1：允许',
  PRIMARY KEY (`series`),
  UNIQUE KEY `ux_ec_cache_schema_define_01` (`tenant_num_id`,`data_sign`,`method_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

##缓存方法-关联表 表
CREATE TABLE `ec_cache_method_reference_table` (
  `series` bigint(20) NOT NULL DEFAULT '0' COMMENT '行号',
  `tenant_num_id` int(11) NOT NULL DEFAULT '0' COMMENT '租户ID',
  `data_sign` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: 正式  1：测试',
  `method_name` varchar(50) NOT NULL COMMENT '方法名称',
  `primary_key_series` bigint(20) NOT NULL DEFAULT '0' COMMENT '缓存表主键行号',
  `col_convert` varchar(200) DEFAULT NULL COMMENT '栏位转换,可以为空,格式:数据库栏位1#别名1,数据库栏位2#别名2,等',
  `create_dtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_updtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `create_user_id` bigint(20) DEFAULT '0' COMMENT '用户',
  `last_update_user_id` bigint(20) DEFAULT '0' COMMENT '更新用户',
  PRIMARY KEY (`series`),
  UNIQUE KEY `ux_ec_cache_method_reference_table_01` (`tenant_num_id`,`data_sign`,`method_name`,`primary_key_series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

##通用缓存依赖
CREATE TABLE `ec_common_cache_dependence` (
  `series` bigint(20) NOT NULL DEFAULT '0' COMMENT '行号',
  `tenant_num_id` int(11) NOT NULL DEFAULT '0' COMMENT '租户ID',
  `data_sign` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: 正式  1：测试',
  `method_name` varchar(50) NOT NULL COMMENT '方法名',
  `db` varchar(50) DEFAULT NULL COMMENT '数据库(准确来说是数据源名称)',
  `cache_key` varchar(255) NOT NULL COMMENT '缓存键名',
  `params` varchar(255) NOT NULL COMMENT '方法入参,刷新用',
  `table_name` varchar(50) NOT NULL COMMENT '表名',
  `table_series` bigint(20) DEFAULT '0' COMMENT '引用表的行号',
  `create_dtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_updtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `create_user_id` bigint(20) DEFAULT '0' COMMENT '用户',
  `last_update_user_id` bigint(20) DEFAULT '0' COMMENT '更新用户',
  `dubbo_group` varchar(20) DEFAULT '' COMMENT 'dubbo分组',
  `_mycat_op_time` bigint(20) DEFAULT NULL COMMENT '全局表保存修改时间戳的字段名',
  PRIMARY KEY (`series`) USING BTREE,
  KEY `ix_ec_common_cache_dependence_01` (`table_name`,`table_series`) USING BTREE,
  KEY `ix_ec_common_cache_dependence_02` (`cache_key`) USING BTREE,
  KEY `mycat_op_time` (`_mycat_op_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='通用缓存表依赖数据';

CREATE TABLE `ec_cache_table_primary_key` (
  `series` bigint(20) NOT NULL DEFAULT '0' COMMENT '行号',
  `tenant_num_id` int(11) NOT NULL DEFAULT '0' COMMENT '租户ID',
  `data_sign` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: 正式  1：测试',
  `db` varchar(50) NOT NULL COMMENT '数据库',
  `table_name` varchar(50) NOT NULL COMMENT '表名',
  `primary_key_multi_col` varchar(200) NOT NULL COMMENT '主键栏位拼接,用逗号隔开',
  `create_dtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_updtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `create_user_id` bigint(20) DEFAULT '0' COMMENT '用户',
  `last_update_user_id` bigint(20) DEFAULT '0' COMMENT '更新用户',
  `record_sign` tinyint(4) DEFAULT '0' COMMENT '按行记录引用记录，0:不按行记录 1：按行记录引用记录',
  PRIMARY KEY (`series`),
  KEY `ix_ec_cache_table_primary_key_01` (`tenant_num_id`,`data_sign`,`table_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
