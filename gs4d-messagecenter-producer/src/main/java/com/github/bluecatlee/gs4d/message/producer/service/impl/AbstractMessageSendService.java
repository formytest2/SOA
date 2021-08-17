package com.github.bluecatlee.gs4d.message.producer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.bluecatlee.gs4d.common.utils.GipUtil;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.message.api.model.SimpleMessage;
import com.github.bluecatlee.gs4d.message.api.utils.MessageSendUtil;
import com.github.bluecatlee.gs4d.message.producer.dao.SysOrderMessageSendLogDao;
import com.github.bluecatlee.gs4d.message.producer.dao.SysRocketMqMessageEncrylogDao;
import com.github.bluecatlee.gs4d.message.producer.dao.SysRocketMqSendLogDao;
import com.github.bluecatlee.gs4d.message.producer.dao.SysTransationFailedLogDao;
import com.github.bluecatlee.gs4d.message.producer.model.SYS_ROCKET_MQ_SEND_LOG;
import com.github.bluecatlee.gs4d.message.producer.utils.AbstractRocketMqUtil;
import com.github.bluecatlee.gs4d.message.producer.utils.Constants;
import com.github.bluecatlee.gs4d.sequence.exception.SequenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 抽象消息发送
 */
public abstract class AbstractMessageSendService {

    protected static Logger logger = LoggerFactory.getLogger(AbstractMessageSendService.class);
    protected static Logger messSaveFailedLogger = LoggerFactory.getLogger("messSaveFailedLog");

    @Autowired
    public SysRocketMqSendLogDao sysRocketMqSendLogDao;

    @Autowired
    public StringRedisTemplate stringRedisTemplate;

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Autowired
    public SysRocketMqMessageEncrylogDao sysRocketMqMessageEncrylogDao;

    @Autowired
    public SysTransationFailedLogDao sysTransationFailedLogDao;

    @Autowired
    public SysOrderMessageSendLogDao sysOrderMessageSendLogDao;

    @Value("#{settings['dataSign']}")
    public Long dataSign;
    @Value("#{settings['expressTag']}")
    public String expressTag;

    public SYS_ROCKET_MQ_SEND_LOG assemble(SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog) {
        String topicTag = sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG();
        String namesrv = (String) Constants.topicTagToNamesrv.get(topicTag);
        if (!StringUtil.isAllNotNullOrBlank(new String[]{namesrv})) {
            namesrv = AbstractRocketMqUtil.defaultNamesrv;
        }

        sysRocketMqSendLog.setMESSAGE_NAME_ADDR(namesrv);
        sysRocketMqSendLog.setPRODUCER_NAME("");
        return sysRocketMqSendLog;
    }

    public SYS_ROCKET_MQ_SEND_LOG assemble(SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog, SimpleMessage simpleMessage) throws SequenceException {
        if (simpleMessage.getTenantNumId() == null) {
            simpleMessage.setTenantNumId(1L);
        }

        this.sysRocketMqSendLogDao.setSeries(sysRocketMqSendLog, simpleMessage.getTenantNumId());
        if (simpleMessage.getTransactionId() != null) {
            sysRocketMqSendLog.setWORKFLOW_ID(simpleMessage.getTransactionId());    // 如果有分布式事务id 则存到workflow_id字段
        } else {
            sysRocketMqSendLog.setWORKFLOW_ID(0L);
        }

        sysRocketMqSendLog.setSTEP_ID(Constants.ex);
        sysRocketMqSendLog.setINSTANCE_ID(0L);
        sysRocketMqSendLog.setMESSAGE_TOPIC(simpleMessage.getTopic());
        sysRocketMqSendLog.setMESSAGE_TAG(simpleMessage.getTag());
        sysRocketMqSendLog.setMESSAGE_KEY(simpleMessage.getMsgKey());
        String body;
        String expressedBody;
        if (this.expressTag.contains(simpleMessage.getTag()) && simpleMessage.getBody().length() > 5000) {
            body = simpleMessage.getBody();
            expressedBody = GipUtil.gzip(body);
            sysRocketMqSendLog.setMESSAGE_BODY(expressedBody);
            sysRocketMqSendLog.setEXPRESS_FLAG("Y");
        } else {
            sysRocketMqSendLog.setMESSAGE_BODY(simpleMessage.getBody());
        }

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
        sysRocketMqSendLog.setCANCELSIGN("N");
        sysRocketMqSendLog.setCLIENT_IP(simpleMessage.getClientIp());

        String topicTag = simpleMessage.getTopic() + "#" + simpleMessage.getTag();
        String namesrv = Constants.topicTagToNamesrv.get(topicTag);
        if (!StringUtil.isAllNotNullOrBlank(new String[]{namesrv})) {
            namesrv = AbstractRocketMqUtil.defaultNamesrv;
        }
        sysRocketMqSendLog.setMESSAGE_NAME_ADDR(namesrv);

        sysRocketMqSendLog.setPRODUCER_NAME("");
        if (simpleMessage.getWorkFlowId() != null) {
            sysRocketMqSendLog.setORDER_MESS_FLAG(Constants.mess_flag_transaction);
            sysRocketMqSendLog.setWORKFLOW_ID(simpleMessage.getWorkFlowId());
        } else {
            sysRocketMqSendLog.setORDER_MESS_FLAG(Constants.mess_flag_common);
        }

        if (simpleMessage.getTransactionId() != null) {
            sysRocketMqSendLog.setMSG_STATUS(Constants.transaction_pre_send);       // 事务消息
        } else {
            sysRocketMqSendLog.setMSG_STATUS(Constants.pre_send);
        }

        return sysRocketMqSendLog;
    }

    public void addSysRocketMqSendLogToRedis(SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog) {
        try {
            this.stringRedisTemplate.opsForValue().set(MessageSendUtil.generateMessageKeyToRedis(sysRocketMqSendLog.getSERIES()), JSONObject.toJSONString(sysRocketMqSendLog), 60L, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("消息存入redis失败,原因：" + e.getMessage() + ",消息tag:" + sysRocketMqSendLog.getMESSAGE_TAG() + ",消息内容:" + sysRocketMqSendLog.getMESSAGE_BODY(), e);
        }

    }

    public void pushSysRocketMqSendLogToWorkFlowList(SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog) {
        this.stringRedisTemplate.opsForList().rightPush(MessageSendUtil.generateTranstionMessageKeyToRedis(sysRocketMqSendLog.getWORKFLOW_ID()), JSONObject.toJSONString(sysRocketMqSendLog));
    }

}

