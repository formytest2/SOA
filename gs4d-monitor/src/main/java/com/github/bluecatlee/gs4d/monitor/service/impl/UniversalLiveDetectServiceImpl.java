package com.github.bluecatlee.gs4d.monitor.service.impl;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ValidateBusinessException;
import com.github.bluecatlee.gs4d.common.utils.ExceptionUtil;
import com.github.bluecatlee.gs4d.common.utils.MyJsonMapper;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.monitor.api.request.SystemLiveDetectRequest;
import com.github.bluecatlee.gs4d.monitor.api.response.SystemLiveDetectResponse;
import com.github.bluecatlee.gs4d.monitor.api.service.UniversalLiveDetectService;
import com.github.bluecatlee.gs4d.monitor.utils.UniversalLiveDetectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UniversalLiveDetectServiceImpl implements UniversalLiveDetectService {

    private static Logger log = LoggerFactory.getLogger(UniversalLiveDetectServiceImpl.class);

    private static MyJsonMapper mapper = new MyJsonMapper();

    static {
        mapper.getMapper().setPropertyNamingStrategy(PropertyNamingStrategy.LowerCaseStrategy.SNAKE_CASE);
    }

    /**
     * 基于检查数据库连接的探测
     * @param request
     * @return
     */
    public SystemLiveDetectResponse detectSystemLive(SystemLiveDetectRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("begin detectSystemLive request:{}", mapper.toJson(request));
        }
        SystemLiveDetectResponse response = new SystemLiveDetectResponse();
        try {
            request.validate(request.getSubSystem(), ExceptionType.VCE10007);
            if (!"none".equals(UniversalLiveDetectUtil.dbType)) {
                if (UniversalLiveDetectUtil.jdbcTemplate == null) {
                    throw new ValidateBusinessException(request.getSubSystem(), ExceptionType.VBE20007, "当前业务项目没有初始化JDBC,无法探测系统:" + request.getSubSystem() + "是否存活!");
                }
                String sql;
                if ("oracle".equals(UniversalLiveDetectUtil.dbType)) {
                    sql = "select 1 from dual";
                } else if ("mysql".equals(UniversalLiveDetectUtil.dbType)) {
                    sql = "select 'abc' ";
                } else {
                    throw new ValidateBusinessException(request.getSubSystem(), ExceptionType.VBE20007, "不支持数据库类别:" + UniversalLiveDetectUtil.dbType + ",无法探测系统:" + request.getSubSystem() + "是否存活!");
                }
                String str = (String)UniversalLiveDetectUtil.jdbcTemplate.queryForObject(sql, String.class);
                if (StringUtil.isAllNullOrBlank(new String[] {str})) {
                    response.setCode(ExceptionType.BE40144.getCode());
                    response.setMessage("探测系统:" + request.getSubSystem() + "时发现系统停止!");
                }
            }
        } catch (Exception e) {
            ExceptionUtil.processException(e, (MessagePack)response);
        }
        if (log.isDebugEnabled()) {
            log.debug("end detectSystemLive response:{}", response.toLowerCaseJson());
        }
        return response;
    }
}
