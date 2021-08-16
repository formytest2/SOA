package com.github.bluecatlee.gs4d.message.producer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.bluecatlee.gs4d.common.bean.AbstractRedisRequest;
import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.utils.ExceptionUtil;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.export.api.request.MessageCommonRefoundRequest;
import com.github.bluecatlee.gs4d.export.api.response.MessageCommonRefoundResponse;
import com.github.bluecatlee.gs4d.export.api.service.ExportDataService;
import com.github.bluecatlee.gs4d.message.api.model.SimpleMessage;
import com.github.bluecatlee.gs4d.message.api.response.SimpleMessageRightNowSendResponse;
import com.github.bluecatlee.gs4d.message.api.service.MessageCenterProductorScheduleHandleFailedMessageService;
import com.github.bluecatlee.gs4d.message.api.utils.MessageSendUtil;
import com.github.bluecatlee.gs4d.message.producer.dao.RedisExpirekeyListenDao;
import com.github.bluecatlee.gs4d.message.producer.dao.SysRocketMqSendLogDao;
import com.github.bluecatlee.gs4d.message.producer.dao.SysRocketMqSendLogHistoryDao;
import com.github.bluecatlee.gs4d.message.producer.dao.SysTransationFailedLogDao;
import com.github.bluecatlee.gs4d.message.producer.model.SYS_ROCKET_MQ_SEND_LOG;
import com.github.bluecatlee.gs4d.message.producer.utils.AbstractRocketMqUtil;
import com.github.bluecatlee.gs4d.message.producer.utils.Constants;
import com.github.bluecatlee.gs4d.monitor.api.request.MessageCenterErrorLogDealRequest;
import com.github.bluecatlee.gs4d.transaction.request.MessagecenterRecallRequest;
import com.github.bluecatlee.gs4d.transaction.response.MessagecenterRecallResponse;
import com.github.bluecatlee.gs4d.transaction.service.MessagecenterRecallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * redis监听器
 */
public class RedisMsgPubSubListener extends JedisPubSub {

    private static Logger logger = LoggerFactory.getLogger(RedisMsgPubSubListener.class);

    private StringRedisTemplate stringRedisTemplate;
    private SysRocketMqSendLogDao sysRocketMqSendLogDao;
    private JdbcTemplate jdbcTemplate;
    private String consumerFiledTopic;
    private Long dataSign;
    private SysRocketMqSendLogHistoryDao sysRocketMqSendLogHistoryDao;
    private ExportDataService exportDataService;
    private SysTransationFailedLogDao sysTransationFailedLogDao;
    private static ScheduledSendMsgJob ce = new ScheduledSendMsgJob();
    private Map<String, Map<String, String>> redisKeyHeadToTopicMap = new HashMap();    // redisKeyHead  ->  topicToTagMap(topic -> tag)
    private MessagecenterRecallService messagecenterRecallService;
    private RedisExpirekeyListenDao redisExpirekeyListenDao;

    public RedisMsgPubSubListener(StringRedisTemplate stringRedisTemplate, SysRocketMqSendLogDao sysRocketMqSendLogDao, JdbcTemplate jdbcTemplate, String consumerFiledTopic, Long dataSign, MessageCenterProductorScheduleHandleFailedMessageService messageCenterProductorScheduleHandleFailedMessageService, PlatformTransactionManager platformTransactionManager, ExportDataService exportDataService, SysTransationFailedLogDao sysTransationFailedLogDao, Map<String, Map<String, String>> redisKeyHeadToTopicMap, MessagecenterRecallService messagecenterRecallService, RedisExpirekeyListenDao redisExpirekeyListenDao) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.sysRocketMqSendLogDao = sysRocketMqSendLogDao;
        this.jdbcTemplate = jdbcTemplate;
        this.consumerFiledTopic = consumerFiledTopic;
        this.dataSign = dataSign;
        this.exportDataService = exportDataService;
        this.sysTransationFailedLogDao = sysTransationFailedLogDao;
        this.redisKeyHeadToTopicMap = redisKeyHeadToTopicMap;
        this.messagecenterRecallService = messagecenterRecallService;
        this.redisExpirekeyListenDao = redisExpirekeyListenDao;
    }

    public void onMessage(String channel, String message) {
        if(channel.contains("__:expired")) {                // 只处理key失效的消息
            if(message.startsWith("messageSeries_")) {      // 消息发送日志sysRocketMqSendLog的缓存
                this.dealRocketMqSendLog(message);
            } else {
                this.dealExpiredKey(message);
            }

            String value = (String)this.stringRedisTemplate.opsForValue().get(message);
            if(StringUtil.isAllNullOrBlank(new String[]{value})) {
                this.stringRedisTemplate.delete(message);
            }

        }
    }

    private void dealExpiredKey(String message) {
        Iterator iterator = this.redisKeyHeadToTopicMap.entrySet().iterator();

        while(true) {
            Entry entry;
            do {
                if(!iterator.hasNext()) {
                    return;
                }

                entry = (Entry)iterator.next();
            } while(!message.startsWith((String)entry.getKey()));

            logger.info("收到过期消息" + message);
            SimpleMessage simpleMessage = new SimpleMessage();
            AbstractRedisRequest abstractRedisRequest = new AbstractRedisRequest();
            abstractRedisRequest.setRedisKey(message);
            simpleMessage.setBody(JSONObject.toJSONString(abstractRedisRequest));
            simpleMessage.setMsgKey(message);
            simpleMessage.setDataSign(Long.valueOf(0L));
            String topic = null;
            String tag = null;

            Entry innerEntry;
            for(Iterator innerIterator = ((Map)entry.getValue()).entrySet().iterator(); innerIterator.hasNext(); tag = (String)innerEntry.getValue()) {
                innerEntry = (Entry)innerIterator.next();
                topic = (String)innerEntry.getKey();
            }

            simpleMessage.setTopic(topic);
            simpleMessage.setTag(tag);
            simpleMessage.setTenantNumId(Long.valueOf(1L));
            simpleMessage.setFromSystem(Long.valueOf(33L));
            SimpleMessageRightNowSendResponse simpleMessageRightNowSendResponse = MessageSendUtil.sendSimpleMessageRightNow(simpleMessage);
            if(simpleMessageRightNowSendResponse.getCode() != 0L) {
                throw new RuntimeException("本次业务发送消息失败,消息内容" + JSONObject.toJSONString(message));
            }

            try {
                this.redisExpirekeyListenDao.updateByRedisKeyHead((String)entry.getKey());
            } catch (Exception e) {
                logger.error("redis表更新失败,理由:" + e.getMessage(), e);
            }
        }
    }

    private void dealRocketMqSendLog(String message) {
        MessagePack messagePack = new MessagePack();
        MessageCenterErrorLogDealRequest messageCenterErrorLogDealRequest = new MessageCenterErrorLogDealRequest();
        String series = message.substring(message.indexOf("_") + 1, message.length());
        logger.info("消息回查开始series为" + series);
        SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog = new SYS_ROCKET_MQ_SEND_LOG();

        try {
            if(this.stringRedisTemplate.opsForValue().get("messageRefindSeries_" + series) != null) {
                return;
            }

            this.stringRedisTemplate.opsForValue().set("messageRefindSeries_" + series, series, 60L, TimeUnit.SECONDS);
            sysRocketMqSendLog = this.sysRocketMqSendLogDao.queryBySeriesStrict(Long.valueOf(series));
            if(sysRocketMqSendLog.getMESSAGE_TOPIC().equals(this.consumerFiledTopic)) {
                return;
            }

            messageCenterErrorLogDealRequest.setTenantNumId(sysRocketMqSendLog.getTENANT_NUM_ID());
            messageCenterErrorLogDealRequest.setDataSign(sysRocketMqSendLog.getDATA_SIGN());
            ArrayList list;
            if(sysRocketMqSendLog.getMSG_STATUS() == Constants.eJ) {
                MessagecenterRecallRequest messagecenterRecallRequest = new MessagecenterRecallRequest();
                messagecenterRecallRequest.setTransactionId(sysRocketMqSendLog.getWORKFLOW_ID());
                MessagecenterRecallResponse messagecenterRecallResponse = this.messagecenterRecallService.recallMessagecenter(messagecenterRecallRequest);
                if(messagecenterRecallResponse.getCode().longValue() == MessagePack.OK && messagecenterRecallResponse.getState().longValue() == 1L) {
                    list = new ArrayList();
                    sysRocketMqSendLog.setSTEP_ID(Constants.ez);
                    list.add(sysRocketMqSendLog);
                    AbstractRocketMqUtil.send(list, this.stringRedisTemplate);
                    messagePack.setMessage("事务回查成功。序号" + series);
                } else if(messagecenterRecallResponse.getCode().longValue() == MessagePack.OK && messagecenterRecallResponse.getState().longValue() == 2L) {
                    list = new ArrayList();
                    list.add(sysRocketMqSendLog);
                    sysRocketMqSendLog.setSTEP_ID(Constants.eE);
                    sysRocketMqSendLog.setMSG_STATUS(Constants.send_cancel);

                    try {
                        this.sysTransationFailedLogDao.insert(sysRocketMqSendLog);
                        this.sysRocketMqSendLogDao.batchDelete(list);
                    } catch (Exception e) {
                        logger.warn("redis监听过期事务消息，发现重复series" + sysRocketMqSendLog.getSERIES());
                    }

                    messagePack.setMessage("发现取消的序号" + series);
                }
            } else if(!StringUtil.isAllNotNullOrBlank(new String[]{sysRocketMqSendLog.getCONSUMER_SUCCESS()})) {
                MessageCommonRefoundRequest messageCommonRefoundRequest = new MessageCommonRefoundRequest();
                messageCommonRefoundRequest.setMsgSeries(sysRocketMqSendLog.getSERIES().toString());
                messageCommonRefoundRequest.setSysNumId(Long.valueOf(sysRocketMqSendLog.getFROM_SYSTEM()));
                messageCommonRefoundRequest.setTenantNumId(sysRocketMqSendLog.getTENANT_NUM_ID());
                messageCommonRefoundRequest.setDataSign(sysRocketMqSendLog.getDATA_SIGN());
                MessageCommonRefoundResponse messageCommonRefoundResponse = this.exportDataService.messageCommonRefound(messageCommonRefoundRequest);
                if(messageCommonRefoundResponse.getCode() == MessagePack.OK) {
                    list = new ArrayList();
                    sysRocketMqSendLog.setSTEP_ID(Constants.ez);
                    list.add(sysRocketMqSendLog);
                    AbstractRocketMqUtil.send(list, this.stringRedisTemplate);
                    messagePack.setMessage("事务回查成功。序号" + series);
                } else {
                    if(messageCommonRefoundResponse.getCode() != ExceptionType.BE40160.getCode()) {
                        messagePack.setMessage("事务回查导入项目失败，原因" + messageCommonRefoundResponse.getMessage());
                        throw new RuntimeException("事务回查导入项目失败，原因" + messageCommonRefoundResponse.getMessage());
                    }

                    list = new ArrayList();
                    list.add(sysRocketMqSendLog);
                    sysRocketMqSendLog.setSTEP_ID(Constants.eE);
                    sysRocketMqSendLog.setMSG_STATUS(Constants.send_cancel);
                    this.sysTransationFailedLogDao.insert(sysRocketMqSendLog);
                    this.sysRocketMqSendLogDao.batchDelete(list);
                    messagePack.setMessage("发现取消的序号" + series);
                }
            } else {
                logger.info("消息中心过期消息与实际不符，序列号:" + series);
            }
        } catch (Exception e) {
            messageCenterErrorLogDealRequest.setDataSign(this.dataSign);
            messageCenterErrorLogDealRequest.setTenantNumId(Long.valueOf(1L));
            logger.error(e.getMessage(), e);
            ExceptionUtil.processException(e, messagePack);
            sysRocketMqSendLog.setFAIL_DETAIL(e.getMessage());
            sysRocketMqSendLog.setSTEP_ID(Constants.eD);
            this.sysTransationFailedLogDao.insert(sysRocketMqSendLog);
            this.sysRocketMqSendLogDao.deleteBySeries(Long.valueOf(series));
        }

        logger.info("回查series" + series + "结束");
    }
}
