CREATE TABLE `commoncalltable` (
  `cmd` varchar(128) NOT NULL COMMENT '前端调用命令',
  `funcname` varchar(128) NOT NULL COMMENT '前端调用命令别名',
  `beanid` varchar(128) DEFAULT NULL COMMENT '服务名',
  `method` varchar(128) DEFAULT NULL COMMENT '服务方法',
  `session_sign` tinyint(4) DEFAULT '0' COMMENT '是否最终用户相关方法',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `request_sample` varchar(20000) DEFAULT NULL COMMENT '请求参数范例',
  `sys_num_id` int(11) DEFAULT '0' COMMENT '所属系统',
  `data_sign` int(11) DEFAULT '0' COMMENT '测试标识',
  `other_sys_num_id` varchar(20) DEFAULT NULL,
  `series` bigint(18) DEFAULT NULL,
  UNIQUE KEY `idx_commoncalltable_1` (`funcname`),
  UNIQUE KEY `idx_commoncalltable_2` (`cmd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;