CREATE TABLE `platform_sequence` (
  `SERIES` bigint(20) NOT NULL COMMENT '主键',
  `SEQ_NAME` varchar(50) NOT NULL COMMENT '序列名称',
  `SEQ_PROJECT` varchar(30) NOT NULL COMMENT '序列号所属项目名',
  `SEQ_PREFIX` varchar(10) NOT NULL COMMENT '序列前缀',
  `SEQ_NUM` varchar(10) NOT NULL COMMENT '序列number',
  `SEQ_VAL` varchar(10) NOT NULL COMMENT '序列value',
  `CURRENT_NUM` bigint(10) NOT NULL COMMENT '当前序列',
  `CREATE_TIME` datetime NOT NULL COMMENT '编辑时间',
  `SEQ_NUM_START` bigint(10) DEFAULT NULL COMMENT 'CURRENT_NUM开始值',
  `SEQ_NUM_END` bigint(10) DEFAULT NULL COMMENT 'CURRENT_NUM结束值',
  `disrupt` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否打乱 0：否 ，1 是',
  `is_store_local` tinyint(2) DEFAULT '0' COMMENT '是否存本地 0：否 ，1 是',
  `_dble_op_time` bigint(20) DEFAULT NULL COMMENT 'field for checking consistency',
  PRIMARY KEY (`SERIES`),
  KEY `ix_platform_sequence` (`SEQ_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

CREATE TABLE `platform_sequence_time` (
  `SEQUENCE_TIME` varchar(20) DEFAULT NULL COMMENT '序列日期 YYYY-MM-DD',
  `LAST_UPDTME` datetime DEFAULT NULL COMMENT '更新日期',
  `_dble_op_time` bigint(20) DEFAULT NULL COMMENT 'field for checking consistency'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

CREATE TABLE `platform_auto_sequence` (
  `SERIES` bigint(20) NOT NULL COMMENT '主键',
  `TENANT_NUM_ID` bigint(10) NOT NULL DEFAULT '6' COMMENT '租户ID',
  `DATA_SIGN` bigint(10) NOT NULL DEFAULT '0' COMMENT '测试标识  0: 正式  1：测试',
  `SEQ_NAME` varchar(50) NOT NULL COMMENT '序列名称',
  `SEQ_PROJECT` varchar(30) NOT NULL COMMENT '序列号所属项目名',
  `SEQ_PREFIX` varchar(20) DEFAULT ' ' COMMENT '序列前缀',
  `CURRENT_NUM` bigint(30) NOT NULL DEFAULT '1' COMMENT '当前序列',
  `IS_YEAR` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否有年 0：否 ，6 是',
  `IS_MONTH` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否有月 0：否 ，6 是',
  `IS_DAY` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否有日 0：否 ，6 是',
  `IS_FLOW_CODE` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否需要流水号  0：否 ，6 是',
  `CREATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `REMARK` varchar(50) DEFAULT ' ' COMMENT '备注',
  `SPARE` varchar(30) DEFAULT ' ' COMMENT '备用字段',
  `init_value` bigint(20) DEFAULT '1' COMMENT '自增序列初始化值',
  `is_clear` tinyint(2) DEFAULT '0' COMMENT '是否清零 0：否 ，6 是',
  `flow_code_length` int(10) NOT NULL DEFAULT '0' COMMENT '流水号的长度',
  `cache_num` tinyint(4) DEFAULT '10' COMMENT '序号缓存长度',
  PRIMARY KEY (`SERIES`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `platform_offline_sequence` (
  `series` bigint(20) NOT NULL,
  `seq_name` varchar(70) DEFAULT NULL COMMENT '序号名字',
  `online_start_num` bigint(20) DEFAULT '0' COMMENT '在线序号开始值',
  `online_end_num` bigint(20) DEFAULT '0' COMMENT '在线序号结束值',
  `offline_end_num` bigint(20) DEFAULT '0' COMMENT '离线序号结束值',
  `offline_current_num` bigint(20) DEFAULT '0' COMMENT '离线序号已经分配到的值',
  `offline_get_num_count` tinyint(2) DEFAULT '0' COMMENT '离线获取当前序号的个数',
  `create_dtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`series`),
  KEY `ix_platform_offline_sequence_1` (`seq_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `platform_offline_sub_unit_sequence` (
  `series` bigint(20) NOT NULL,
  `seq_name` varchar(70) NOT NULL COMMENT '序号名字',
  `start_num` bigint(20) NOT NULL COMMENT '离线序号分配到的开始值',
  `end_num` bigint(20) NOT NULL COMMENT '离线序号分配到的结束值',
  `sub_unit_num_id` bigint(20) NOT NULL COMMENT '门店编号',
  `create_dtme` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`series`),
  KEY `ix_platform_offline_sub_unit_sequence_1` (`seq_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;