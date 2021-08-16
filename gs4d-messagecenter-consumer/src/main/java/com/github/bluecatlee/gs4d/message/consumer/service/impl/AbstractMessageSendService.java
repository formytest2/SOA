package com.github.bluecatlee.gs4d.message.consumer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.message.api.model.SimpleMessage;
import com.github.bluecatlee.gs4d.message.api.utils.MessageSendUtil;
import com.github.bluecatlee.gs4d.message.consumer.dao.SysRocketMqMessageEncrylogDao;
import com.github.bluecatlee.gs4d.message.consumer.dao.SysRocketMqSendLogDao;
import com.github.bluecatlee.gs4d.message.consumer.model.SysRocketMqSendLog;
import com.github.bluecatlee.gs4d.message.consumer.utils.AbstractRocketMqUtil;
import com.github.bluecatlee.gs4d.message.consumer.utils.Constants;
import com.github.bluecatlee.gs4d.sequence.exception.SequenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.concurrent.TimeUnit;

@Deprecated
public abstract class AbstractMessageSendService {

    protected static Logger messSaveFailedLogger = LoggerFactory.getLogger("messSaveFailedLog");

    @Autowired
    public SysRocketMqSendLogDao sysRocketMqSendLogDao;
    
    @Autowired
    public StringRedisTemplate stringRedisTemplate;
    
    @Autowired
    public JdbcTemplate jdbcTemplate;
    
    @Autowired
    public SysRocketMqMessageEncrylogDao sysRocketMqMessageEncrylogDao;
    
    @Value("#{settings['dataSign']}")
    public Long dataSign;

    public SysRocketMqSendLog assemble(SysRocketMqSendLog sysRocketMqSendLog) {
        String topicTag = sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG() + "#" + sysRocketMqSendLog.getTENANT_NUM_ID();
        String namesrv = (String) Constants.topicTagToNamesrv.get(topicTag);
        if (!StringUtil.isAllNotNullOrBlank(new String[]{namesrv})) {
            namesrv = AbstractRocketMqUtil.defaultNamesrv;
        }

        sysRocketMqSendLog.setMESSAGE_NAME_ADDR(namesrv);
        sysRocketMqSendLog.setPRODUCER_NAME("");
        return sysRocketMqSendLog;
    }

    public SysRocketMqSendLog assemble(SysRocketMqSendLog sysRocketMqSendLog, SimpleMessage simpleMessage) throws SequenceException {
        if (simpleMessage.getTenantNumId() == null) {
            simpleMessage.setTenantNumId(1L);
        }

        this.sysRocketMqSendLogDao.setSeries(sysRocketMqSendLog, simpleMessage.getTenantNumId());
        sysRocketMqSendLog.setWORKFLOW_ID(0L);
        sysRocketMqSendLog.setSTEP_ID(0);
        sysRocketMqSendLog.setINSTANCE_ID(0L);
        sysRocketMqSendLog.setMESSAGE_TOPIC(simpleMessage.getTopic());
        sysRocketMqSendLog.setMESSAGE_TAG(simpleMessage.getTag());
        sysRocketMqSendLog.setMESSAGE_KEY(simpleMessage.getMsgKey());
        sysRocketMqSendLog.setMESSAGE_BODY(simpleMessage.getBody());
        if (StringUtil.isAllNotNullOrBlank(new Object[]{simpleMessage.getDataSign()})) {
            sysRocketMqSendLog.setDATA_SIGN(simpleMessage.getDataSign());
        } else if (this.dataSign == 2L) {
            sysRocketMqSendLog.setDATA_SIGN(1L);
        } else {
            sysRocketMqSendLog.setDATA_SIGN(this.dataSign);
        }

        sysRocketMqSendLog.setFROM_SYSTEM(simpleMessage.getFromSystem().toString());
        if (simpleMessage.getUserId() != null) {
            sysRocketMqSendLog.setCREATE_USER_ID(simpleMessage.getUserId());
        } else {
            sysRocketMqSendLog.setCREATE_USER_ID(0L);
        }

        sysRocketMqSendLog.setTENANT_NUM_ID(simpleMessage.getTenantNumId());
        sysRocketMqSendLog.setMSG_STATUS(2);
        sysRocketMqSendLog.setCANCELSIGN("N");
        sysRocketMqSendLog.setCLIENT_IP(simpleMessage.getClientIp());
        String topicTag = simpleMessage.getTopic() + "#" + simpleMessage.getTag() + "#" + simpleMessage.getTenantNumId();
        String namesrv = (String) Constants.topicTagToNamesrv.get(topicTag);
        if (!StringUtil.isAllNotNullOrBlank(new String[]{namesrv})) {
            namesrv = AbstractRocketMqUtil.defaultNamesrv;
        }

        sysRocketMqSendLog.setMESSAGE_NAME_ADDR(namesrv);
        sysRocketMqSendLog.setPRODUCER_NAME("");
        return sysRocketMqSendLog;
    }

    public void f(SysRocketMqSendLog sysRocketMqSendLog) {
        this.stringRedisTemplate.opsForValue().set(MessageSendUtil.generateMessageKeyToRedis(sysRocketMqSendLog.getSERIES()), JSONObject.toJSONString(sysRocketMqSendLog), 60L, TimeUnit.SECONDS);
    }
}
