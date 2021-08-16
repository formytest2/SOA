package com.github.bluecatlee.gs4d.pay.mapper;

import com.github.bluecatlee.gs4d.pay.entity.PayParamLogWithBLOBs;
import tk.mybatis.mapper.common.MySqlMapper;

public interface PayParamLogMapper extends MySqlMapper<PayParamLogWithBLOBs> {

    int insert(PayParamLogWithBLOBs record);

}