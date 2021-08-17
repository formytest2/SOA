package com.github.bluecatlee.gs4d.message.api.utils;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.message.api.model.JobMessageModel;
import com.github.bluecatlee.gs4d.message.api.model.OrderMessageModel;
import com.github.bluecatlee.gs4d.message.api.model.SimpleMessage;
import com.github.bluecatlee.gs4d.message.api.request.*;
import com.github.bluecatlee.gs4d.message.api.response.*;
import com.github.bluecatlee.gs4d.message.api.service.MessageCenterSendListService;
import com.github.bluecatlee.gs4d.message.api.service.MessageCenterSendService;
import com.tranboot.client.core.txc.TxcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
@Lazy(false)
public class MessageSendUtil {

    private static Logger log = LoggerFactory.getLogger(MessageSendUtil.class);

    private static String ipAddress;
    public static JdbcTemplate jdbcTemplate;

    public static MessageSendUtil info;

    @Resource
    private MessageCenterSendService sendService;
    @Resource
    private MessageCenterSendListService sendListService;

    // sys_rocket_mq_send_log的series
    private static ThreadLocal<List<Long>> threadLocal = new ThreadLocal<List<Long>>() {
        public List<Long> initialValue() {
            return new ArrayList();
        }
    };

    private static ThreadLocal<List<SimpleMessage>> simpleOrderMessageThreadLocal = new ThreadLocal<List<SimpleMessage>>() {
        public List<SimpleMessage> initialValue() {
            return new ArrayList();
        }
    };
    private static ThreadLocal<Long> orderMessageGroupIdThreadLocal = new ThreadLocal<Long>() {
        public Long initialValue() {
            return 0L;
        }
    };
    static String insertSql = "insert into sys_msg_trans_refind_id(series,system_name,ip_address,topic,tag,data_sign,tenant_num_id,create_dtme,has_confirm)values(?,?,?,?,?,?,?,?,'N')";
    public static int shutdownTimeout = 1200;

    public static MessageCenterSendListService getSendListService() {
        return info.sendListService;
    }

    public static MessageCenterSendService getSendService() {
        return info.sendService;
    }

    public static void setSendService(MessageCenterSendService sendService) {
        info.sendService = sendService;
    }

    public static void initTransMessage(String zkAddress, String dubboGroup, JdbcTemplate jdbcTemplateClient, Integer dubboPort) {
        ipAddress = IPUtil.getIP();
        jdbcTemplate = jdbcTemplateClient;
    }

    public static void initTransMessage(JdbcTemplate jdbcTemplateClient) {
        ipAddress = IPUtil.getIP();
        jdbcTemplate = jdbcTemplateClient;
    }

    @PostConstruct
    public void init() {
        info = this;
        info.sendService = this.sendService;
    }

    /**
     * 消息预发送
     * @param topic
     * @param tag
     * @param msgKey
     * @param body
     * @param systemId
     * @param dataSign
     * @return
     */
    public static long sendPrepMsgUseThreadLocal(String topic, String tag, String msgKey, String body, Long systemId, Long dataSign) {
        return sendPrepMsgUseThreadLocal(topic, tag, msgKey, body, systemId, dataSign, 1L);
    }

    /**
     * 消息预发送
     * @param topic
     * @param tag
     * @param msgKey
     * @param body
     * @param systemId
     * @param dataSign
     * @param tenantNumId
     * @return
     */
    public static long sendPrepMsgUseThreadLocal(String topic, String tag, String msgKey, String body, Long systemId, Long dataSign, Long tenantNumId) {
        try {
            if (ipAddress == null) {
                ipAddress = IPUtil.getIP();
            }

            SimpleMessage message = new SimpleMessage(topic, tag, msgKey, body, systemId, dataSign, tenantNumId);
            message.setClientIp(ipAddress);
            if (TxcContext.getCurrentXid() != null) {
                message.setTransactionId(TxcContext.getCurrentXid());
            }

            PrepSimpleMessageRequest request = new PrepSimpleMessageRequest();
            request.setDataSign(dataSign);
            request.setTenantNumId(tenantNumId);
            request.setSimpleMessage(message);
            PrepSimpleMessageResponse response = info.sendService.sendPrepSimpleMessage(request);
            if (response.getCode() != MessagePack.OK) {
                throw new RuntimeException("发送消息异常,原因:" + response.getMessage());
            } else {
                if (jdbcTemplate != null) {
                    insertSeries(response.getSeries(), topic, tag, systemId.toString(), dataSign, tenantNumId, jdbcTemplate);
                }

                ((List)threadLocal.get()).add(response.getSeries());
                return response.getSeries();
            }
        } catch (Exception e) {
            log.error("发送消息异常,异常信息" + e.getMessage(), e);
            throw new RuntimeException("发送消息异常,异常信息" + e.getMessage(), e);
        }
    }

    /**
     * 消息预发送
     * @param topic
     * @param tag
     * @param msgKey
     * @param body
     * @param systemId
     * @param dataSign
     * @param tenantNumId
     * @param jdbcTemplate
     * @return
     */
    public static long sendPrepMsgUseThreadLocal(String topic, String tag, String msgKey, String body, Long systemId, Long dataSign, Long tenantNumId, JdbcTemplate jdbcTemplate) {
        try {
            if (ipAddress == null) {
                ipAddress = IPUtil.getIP();
            }

            SimpleMessage message = new SimpleMessage(topic, tag, msgKey, body, systemId, dataSign, tenantNumId);
            message.setClientIp(ipAddress);
            if (TxcContext.getCurrentXid() != null) {
                message.setTransactionId(TxcContext.getCurrentXid());
            }

            PrepSimpleMessageRequest request = new PrepSimpleMessageRequest();
            request.setDataSign(dataSign);
            request.setTenantNumId(tenantNumId);
            request.setSimpleMessage(message);
            PrepSimpleMessageResponse response = info.sendService.sendPrepSimpleMessage(request);
            if (response.getCode() != MessagePack.OK) {
                throw new RuntimeException("发送消息异常,原因:" + response.getMessage());
            } else {
                if (jdbcTemplate != null) {
                    insertSeries(response.getSeries(), topic, tag, systemId.toString(), dataSign, tenantNumId, jdbcTemplate);
                }

                ((List)threadLocal.get()).add(response.getSeries());
                return response.getSeries();
            }
        } catch (Exception e) {
            log.error("发送消息异常,异常信息" + e.getMessage(), e);
            throw new RuntimeException("发送消息异常,异常信息" + e.getMessage(), e);
        }
    }

    /**
     * 消息预发送
     * @param message
     * @param jdbcTemplate
     * @return
     */
    public static long sendPrepMsgUseThreadLocal(SimpleMessage message, JdbcTemplate jdbcTemplate) {
        try {
            if (ipAddress == null) {
                ipAddress = IPUtil.getIP();
            }

            message.setClientIp(ipAddress);
            if (TxcContext.getCurrentXid() != null) {
                message.setTransactionId(TxcContext.getCurrentXid());
            }

            PrepSimpleMessageRequest request = new PrepSimpleMessageRequest();
            request.setDataSign(message.getDataSign());
            request.setTenantNumId(message.getTenantNumId());
            request.setSimpleMessage(message);
            PrepSimpleMessageResponse response = info.sendService.sendPrepSimpleMessage(request);
            if (response.getCode() != MessagePack.OK) {
                throw new RuntimeException("发送消息异常,原因:" + response.getMessage());
            } else {
                // 保存消息发送日志sys_rocket_mq_send_log的行号series
                if (jdbcTemplate != null) {
                    insertSeries(response.getSeries(), message.getTopic(), message.getTag(), message.getFromSystem().toString(), message.getDataSign(), message.getTenantNumId(), jdbcTemplate);
                }

                ((List)threadLocal.get()).add(response.getSeries());
                return response.getSeries();
            }
        } catch (Exception e) {
            log.error("发送消息异常,异常信息" + e.getMessage(), e);
            throw new RuntimeException("发送消息异常,异常信息" + e.getMessage(), e);
        }
    }

    /**
     * 消息预发送
     * @param message
     * @return
     */
    public static long sendPrepMsgUseThreadLocal(SimpleMessage message) {
        try {
            if (ipAddress == null) {
                ipAddress = IPUtil.getIP();
            }

            message.setClientIp(ipAddress);
            if (TxcContext.getCurrentXid() != null) {
                message.setTransactionId(TxcContext.getCurrentXid());
            }

            PrepSimpleMessageRequest request = new PrepSimpleMessageRequest();
            request.setDataSign(message.getDataSign());
            request.setTenantNumId(message.getTenantNumId());
            request.setSimpleMessage(message);
            PrepSimpleMessageResponse response = info.sendService.sendPrepSimpleMessage(request);
            if (response.getCode() != MessagePack.OK) {
                throw new RuntimeException("发送消息异常,原因:" + response.getMessage());
            } else {
                if (jdbcTemplate != null) {
                    insertSeries(response.getSeries(), message.getTopic(), message.getTag(), message.getFromSystem().toString(), message.getDataSign(), message.getTenantNumId(), jdbcTemplate);
                }

                ((List)threadLocal.get()).add(response.getSeries());
                return response.getSeries();
            }
        } catch (Exception e) {
            log.error("发送消息异常,异常信息" + e.getMessage(), e);
            throw new RuntimeException("发送消息异常,异常信息" + e.getMessage(), e);
        }
    }

    /**
     * 立即发送消息
     * @param message
     * @return
     */
    public static SimpleMessageRightNowSendResponse sendSimpleMessageRightNow(SimpleMessage message) {
        SimpleMessageRightNowSendResponse response = new SimpleMessageRightNowSendResponse();

        try {
            if (ipAddress == null) {
                ipAddress = IPUtil.getIP();
            }

            message.setClientIp(ipAddress);
            SimpleMessageRightNowSendRequest request = new SimpleMessageRightNowSendRequest();
            request.setDataSign(message.getDataSign());
            request.setTenantNumId(message.getTenantNumId());
            request.setSimpleMessage(message);
            response = info.sendService.sendSimpleMessageRightNow(request);
            if (response.getCode() != 0L) {
                log.error("消息中心立即发送消息失败,原因：" + response.getMessage() + ",消息tag:" + message.getTag() + ",消息内容:" + message.getBody());
            }
        } catch (Exception e) {
            log.error("消息中心立即发送消息失败", e);
            response.setCode(-1L);
            response.setMessage("发消息失败,原因" + e.getMessage());
        }

        return response;
    }

    /**
     * 立即发送消息
     * @param topic
     * @param tag
     * @param msgKey
     * @param body
     * @param systemId
     * @param dataSign
     * @param tenantNumId
     * @return
     */
    public static SimpleMessageRightNowSendResponse sendSimpleMessageRightNow(String topic, String tag, String msgKey, String body, Long systemId, Long dataSign, Long tenantNumId) {
        SimpleMessageRightNowSendResponse response = new SimpleMessageRightNowSendResponse();

        try {
            if (ipAddress == null) {
                ipAddress = IPUtil.getIP();
            }

            SimpleMessage message = new SimpleMessage();
            message.setBody(body);
            message.setDataSign(dataSign);
            message.setTenantNumId(tenantNumId);
            message.setFromSystem(systemId);
            message.setMsgKey(msgKey);
            message.setTag(tag);
            message.setTopic(topic);
            message.setClientIp(ipAddress);
            SimpleMessageRightNowSendRequest request = new SimpleMessageRightNowSendRequest();
            request.setDataSign(message.getDataSign());
            request.setTenantNumId(message.getTenantNumId());
            request.setSimpleMessage(message);
            response = info.sendService.sendSimpleMessageRightNow(request);
            if (response.getCode() != 0L) {
                log.error("消息中心立即发送消息失败,原因：" + response.getMessage() + ",消息tag:" + message.getTag() + ",消息内容:" + message.getBody());
            }
        } catch (Exception e) {
            log.error("消息中心立即发送消息失败", e);
            response.setCode(-1L);
            response.setMessage("消息中心立即发送消息失败" + e.getMessage());
        }

        return response;
    }

    /**
     * 立即发送消息（批量）
     * @param messages
     * @return
     */
    public static SimpleMessageListRightNowSendResponse sendSimpleMessageListRightNow(List<SimpleMessage> messages) {
        SimpleMessageListRightNowSendResponse response = new SimpleMessageListRightNowSendResponse();

        try {
            if (ipAddress == null) {
                ipAddress = IPUtil.getIP();
            }

            Iterator iterator = messages.iterator();

            while(iterator.hasNext()) {
                SimpleMessage message = (SimpleMessage)iterator.next();
                message.setClientIp(ipAddress);
            }

            SimpleMessageListRightNowSendRequest request = new SimpleMessageListRightNowSendRequest();
            request.setDataSign(((SimpleMessage)messages.get(0)).getDataSign());
            request.setTenantNumId(((SimpleMessage)messages.get(0)).getTenantNumId());
            request.setSimpleMessageList(messages);
            response = info.sendListService.sendSimpleMessageListRightNow(request);
        } catch (Exception e) {
            log.error("消息中心批量立即发送消息失败", e);
            response.setCode(-1L);
            response.setMessage("消息中心立即发送消息失败" + e.getMessage());
        }

        return response;
    }

    /**
     * 立刻发送定时消息（批量）
     * @param messages
     * @return
     */
    public static JobMessageListRightNowSendResponse sendJobMessageListRightNow(List<SimpleMessage> messages) {
        JobMessageListRightNowSendResponse response = new JobMessageListRightNowSendResponse();

        try {
            if (ipAddress == null) {
                ipAddress = IPUtil.getIP();
            }

            Iterator iterator = messages.iterator();

            while(iterator.hasNext()) {
                SimpleMessage message = (SimpleMessage)iterator.next();
                message.setClientIp(ipAddress);
            }

            JobMessageListRightNowSendRequest request = new JobMessageListRightNowSendRequest();
            request.setDataSign(((SimpleMessage)messages.get(0)).getDataSign());
            request.setTenantNumId(((SimpleMessage)messages.get(0)).getTenantNumId());
            request.setSimpleMessageList(messages);
            response = info.sendListService.sendJobMessageListRightNow(request);
        } catch (Exception e) {
            log.error("消息中心批量立即发送定时消息失败", e);
            response.setCode(-1L);
            response.setMessage("消息中心立即发送定时消息失败" + e.getMessage());
        }

        return response;
    }


    /**
     * 确认发送消息
     * @return
     */
    public static MsgConfirmSendResponse confirmMsgUseThreadLocal() {
        MsgConfirmSendResponse response = new MsgConfirmSendResponse();

        try {
            List<Long> list = (List)threadLocal.get();
            if (!list.isEmpty()) {
                try {
                    MsgConfirmSendRequest request = new MsgConfirmSendRequest();
                    request.setSeries(list);
                    List<Long> transMess = new ArrayList();
                    if (list != null && list.size() > 0) {
                        response = info.sendService.confirmSendMsg(request);
                        transMess.addAll(list);
                        response.setSeries(transMess);
                    }

                    if (response.getCode() != MessagePack.OK) {
                        throw new RuntimeException("确认消息异常,原因:" + response.getMessage());
                    }
                } catch (Exception e) {
                    log.error("消息中心确认发送消息失败", e);
                }
            }

            if ((Long)orderMessageGroupIdThreadLocal.get() != 0L) {
                try {
                    OrderSendMessageConfirmRequest request = new OrderSendMessageConfirmRequest();
                    request.setOrderMessageGroupId((Long)orderMessageGroupIdThreadLocal.get());
                    OrderSendMessageConfirmResponse responses = info.sendService.confirmOrderSendMessage(request);
                    if (responses.getCode() != MessagePack.OK) {
                        throw new RuntimeException("确认消息异常,原因:" + responses.getMessage());
                    }
                } catch (Exception e) {
                    response.setCode(-1L);
                    response.setMessage(e.getMessage());
                    log.error("消息中心确认发送消息失败", e);
                }
            }
        } finally {
            threadLocal.remove();
            simpleOrderMessageThreadLocal.remove();
            orderMessageGroupIdThreadLocal.remove();
        }

        return response;
    }

    /**
     * 取消发送消息
     * @return
     */
    public static MsgCancelSendResponse cancelSendmMsgm() {
        return cancelMsgUseThreadLocal();
    }

    /**
     * 取消发送消息
     * @return
     */
    public static MsgCancelSendResponse cancelMsgUseThreadLocal() {
        MsgCancelSendResponse response = new MsgCancelSendResponse();

        try {
            List<Long> list = (List)threadLocal.get();
            if (!list.isEmpty()) {
                try {
                    MsgCancelSendRequest request = new MsgCancelSendRequest();
                    request.setSeries(list);
                    if (list != null && list.size() > 0) {
                        response = info.sendService.cancelSendMsg(request);
                    }

                    if (response.getCode() != MessagePack.OK) {
                        throw new RuntimeException("取消消息异常,原因:" + response.getMessage());
                    }
                } catch (Exception e) {
                    log.error("消息中心取消发送消息失败", e);
                }
            }

            if ((Long)orderMessageGroupIdThreadLocal.get() != 0L) {
                try {
                    OrderSendMessageCancelRequest request = new OrderSendMessageCancelRequest();
                    request.setOrderMessageGroupId((Long)orderMessageGroupIdThreadLocal.get());
                    OrderSendMessageCancelResponse responses = info.sendService.cancelOrderSendMessage(request);
                    if (responses.getCode() != MessagePack.OK) {
                        throw new RuntimeException("确认消息异常,原因:" + responses.getMessage());
                    }
                } catch (Exception e) {
                    response.setCode(-1L);
                    response.setMessage(e.getMessage());
                    log.error("消息中心确认发送消息失败", e);
                }
            }
        } finally {
            threadLocal.remove();
            simpleOrderMessageThreadLocal.remove();
            orderMessageGroupIdThreadLocal.remove();
        }

        return response;
    }

    /**
     * 预发送消息到多个topic
     * @param topic     父topic 关联了多个子topic
     * @param msgKey
     * @param body
     * @param systemId
     * @param dataSign
     * @param tenantNumId
     * @return
     */
    public static PrepOTMSimpleMessageSendResponse sendPrepOTMMsgUseThreadLocal(String topic, String msgKey, String body, Long systemId, Long dataSign, Long tenantNumId) {
        new PrepOTMSimpleMessageSendResponse();

        try {
            if (ipAddress == null) {
                ipAddress = IPUtil.getIP();
            }

            PrepOTMSimpleMessageSendRequest request = new PrepOTMSimpleMessageSendRequest();
            SimpleMessage message = new SimpleMessage(topic, (String)null, msgKey, body, systemId, dataSign, tenantNumId);
            message.setClientIp(ipAddress);
            if (TxcContext.getCurrentXid() != null) {
                message.setTransactionId(TxcContext.getCurrentXid());
            }

            request.setTenantNumId(tenantNumId);
            request.setDataSign(dataSign);
            request.setSimpleMessage(message);
            PrepOTMSimpleMessageSendResponse response = info.sendService.sendPrepOTMSimpleMessage(request);
            List<Long> series = response.getSeries();

            for(int i = 0; i < series.size(); ++i) {
                ((List)threadLocal.get()).add(series.get(i));
                if (jdbcTemplate != null) {
                    insertSeries((Long)series.get(i), topic, "一对多没有tag", systemId.toString(), dataSign, tenantNumId, jdbcTemplate);
                }
            }

            if (response.getCode() != MessagePack.OK) {
                throw new RuntimeException("一对多消息异常,原因:" + response.getMessage());
            } else {
                return response;
            }
        } catch (Exception e) {
            log.error("消息中心一对多发送预消息失败", e);
            throw new RuntimeException("消息中心一对多发送预消息失败,异常信息" + e.getMessage(), e);
        }
    }

    /**
     * 预发送消息到多个topic
     * @param topic
     * @param tag
     * @param msgKey
     * @param body
     * @param systemId
     * @param dataSign
     * @param tenantNumId
     * @param jdbcTemplate
     * @return
     */
    public static PrepOTMSimpleMessageSendResponse sendPrepOTMMsgUseThreadLocal(String topic, String tag, String msgKey, String body, Long systemId, Long dataSign, Long tenantNumId, JdbcTemplate jdbcTemplate) {
        new PrepOTMSimpleMessageSendResponse();

        try {
            if (ipAddress == null) {
                ipAddress = IPUtil.getIP();
            }

            PrepOTMSimpleMessageSendRequest request = new PrepOTMSimpleMessageSendRequest();
            SimpleMessage message = new SimpleMessage(topic, (String)null, msgKey, body, systemId, dataSign, tenantNumId);
            message.setClientIp(ipAddress);
            if (TxcContext.getCurrentXid() != null) {
                message.setTransactionId(TxcContext.getCurrentXid());
            }

            request.setTenantNumId(tenantNumId);
            request.setDataSign(dataSign);
            request.setSimpleMessage(message);
            PrepOTMSimpleMessageSendResponse response = info.sendService.sendPrepOTMSimpleMessage(request);
            List<Long> series = response.getSeries();

            for(int i = 0; i < series.size(); ++i) {
                ((List)threadLocal.get()).add(series.get(i));
                if (jdbcTemplate != null) {
                    insertSeries((Long)series.get(i), topic, "一对多没有tag", systemId.toString(), dataSign, tenantNumId, jdbcTemplate);
                }
            }

            if (response.getCode() != MessagePack.OK) {
                throw new RuntimeException("发送一对多消息异常,原因:" + response.getMessage());
            } else {
                return response;
            }
        } catch (Exception e) {
            log.error("消息中心一对多发送预消息失败", e);
            throw new RuntimeException("消息中心一对多发送预消息失败,异常信息" + e.getMessage(), e);
        }
    }

    /**
     * 预发送消息到多个topic
     * @param message
     * @return
     */
    public static PrepOTMSimpleMessageSendResponse sendPrepOTMMsgUseThreadLocal(SimpleMessage message) {
        new PrepOTMSimpleMessageSendResponse();

        try {
            if (ipAddress == null) {
                ipAddress = IPUtil.getIP();
            }

            message.setClientIp(ipAddress);
            if (TxcContext.getCurrentXid() != null) {
                message.setTransactionId(TxcContext.getCurrentXid());
            }

            PrepOTMSimpleMessageSendRequest request = new PrepOTMSimpleMessageSendRequest();
            request.setTenantNumId(message.getTenantNumId());
            request.setDataSign(message.getDataSign());
            request.setSimpleMessage(message);
            PrepOTMSimpleMessageSendResponse response = info.sendService.sendPrepOTMSimpleMessage(request);
            List<Long> series = response.getSeries();
            if (series == null) {
                return response;
            } else {
                for(int i = 0; i < series.size(); ++i) {
                    ((List)threadLocal.get()).add(series.get(i));
                    if (jdbcTemplate != null) {
                        insertSeries((Long)series.get(i), message.getTopic(), "一对多没有tag", message.getFromSystem().toString(), message.getDataSign(), message.getTenantNumId(), jdbcTemplate);
                    }
                }

                if (response.getCode() != MessagePack.OK) {
                    throw new RuntimeException("一对多发送消息异常,原因:" + response.getMessage());
                } else {
                    return response;
                }
            }
        } catch (Exception e) {
            log.error("消息中心一对多发送预消息失败", e);
            throw new RuntimeException("消息中心一对多发送预消息失败,异常信息" + e.getMessage(), e);
        }
    }

    /**
     * 预发送消息到多个topic
     * @param message
     * @param jdbcTemplate
     * @return
     */
    public static PrepOTMSimpleMessageSendResponse sendPrepOTMMsgUseThreadLocal(SimpleMessage message, JdbcTemplate jdbcTemplate) {
        new PrepOTMSimpleMessageSendResponse();

        try {
            if (ipAddress == null) {
                ipAddress = IPUtil.getIP();
            }

            message.setClientIp(ipAddress);
            if (TxcContext.getCurrentXid() != null) {
                message.setTransactionId(TxcContext.getCurrentXid());
            }

            PrepOTMSimpleMessageSendRequest request = new PrepOTMSimpleMessageSendRequest();
            request.setTenantNumId(message.getTenantNumId());
            request.setDataSign(message.getDataSign());
            request.setSimpleMessage(message);
            PrepOTMSimpleMessageSendResponse response = info.sendService.sendPrepOTMSimpleMessage(request);
            List<Long> series = response.getSeries();

            for(int i = 0; i < series.size(); ++i) {
                ((List)threadLocal.get()).add(series.get(i));
                if (jdbcTemplate != null) {
                    insertSeries((Long)series.get(i), message.getTopic(), "一对多没有tag", message.getFromSystem().toString(), message.getDataSign(), message.getTenantNumId(), jdbcTemplate);
                }
            }

            if (response.getCode() != MessagePack.OK) {
                throw new RuntimeException("一对多预发送消息异常,原因:" + response.getMessage());
            } else {
                return response;
            }
        } catch (Exception e) {
            log.error("消息中心一对多发送预消息失败", e);
            throw new RuntimeException("消息中心一对多发送预消息失败,异常信息" + e.getMessage(), e);
        }
    }

    /**
     * 立即发送消息到多个topic
     * @param message
     * @return
     */
    public static OTMSimpleMessageRightNowSendResponse sendSimpleOTMMessageRightNow(SimpleMessage message) {
        OTMSimpleMessageRightNowSendResponse response = new OTMSimpleMessageRightNowSendResponse();

        try {
            if (ipAddress == null) {
                ipAddress = IPUtil.getIP();
            }

            message.setClientIp(ipAddress);
            OTMSimpleMessageRightNowSendRequest request = new OTMSimpleMessageRightNowSendRequest();
            request.setDataSign(message.getDataSign());
            request.setTenantNumId(message.getTenantNumId());
            request.setSimpleMessage(message);
            response = info.sendService.sendOTMSimpleMessageRightNow(request);
        } catch (Exception e) {
            log.error("消息中心一对多立即发送消息失败", e);
            response.setCode(-1L);
            response.setMessage("消息中心立即发送一对多消息失败" + e.getMessage());
        }

        return response;
    }

    public static String generateMessageKeyToRedis(Long series) {
        String key = "messageSeries_" + series;
        return key;
    }

    public static String generateMessageReturnKeyToRedis(Long series) {
        String key = "messageSeries_return_" + series;
        return key;
    }

    public static String generateTranstionMessageKeyToRedis(Long transactionId) {
        String key = "messageTransaction_" + transactionId;
        return key;
    }

    private static void insertSeries(Long series, String topic, String tag, String systemName, Long dataSign, Long tenantNumId, JdbcTemplate jdbcTemplate) {
        String date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
        if (jdbcTemplate.update(insertSql, new Object[]{series, systemName, ipAddress, topic, tag, dataSign, tenantNumId, date}) <= 0) {
            throw new RuntimeException("消息本地事务表插入失败!");
        }
    }

    /**
     * 确认事务消息
     * @param transactionId
     * @return
     */
    public static TransactionMessageConfirmResponse confirmTransactionMessage(Long transactionId) {
        TransactionMessageConfirmResponse response = new TransactionMessageConfirmResponse();

        try {
            TransactionMessageConfirmRequest request = new TransactionMessageConfirmRequest();
            request.setTransactionId(transactionId);
            response = info.sendService.confirmTransactionMessage(request);
            if (response.getCode() != MessagePack.OK) {
                throw new RuntimeException("事务消息确认异常,原因:" + response.getMessage());
            }
        } catch (Exception e) {
            log.error("事务消息确认异常,原因:" + response.getMessage(), e);
        }

        return response;
    }

    /**
     * 取消事务消息
     * @param transactionId
     * @return
     */
    public static TransactionMessageCancelResponse cancelTransactionMessage(Long transactionId) {
        TransactionMessageCancelResponse response = new TransactionMessageCancelResponse();

        try {
            TransactionMessageCancelRequest request = new TransactionMessageCancelRequest();
            request.setTransactionId(transactionId);
            response = info.sendService.cancelTransactionMessage(request);
            if (response.getCode() != MessagePack.OK) {
                throw new RuntimeException("事务消息取消异常,原因:" + response.getMessage());
            }
        } catch (Exception e) {
            log.error("事务消息取消异常,原因:" + response.getMessage(), e);
        }

        return response;
    }

    public static long sendPrepOrderFlowMsgUseThreadLocal() {
        try {
            if (((List)simpleOrderMessageThreadLocal.get()).size() < 1) {
                throw new RuntimeException("当前线程并没有添加任何顺序消息实体，请先添加！");
            } else {
                PrepOrderSimpleMessageRequest request = new PrepOrderSimpleMessageRequest();
                request.setDataSign(((SimpleMessage)((List)simpleOrderMessageThreadLocal.get()).get(0)).getDataSign());
                request.setTenantNumId(((SimpleMessage)((List)simpleOrderMessageThreadLocal.get()).get(0)).getTenantNumId());
                request.setSimpleMessage((List)simpleOrderMessageThreadLocal.get());
                PrepOrderSimpleMessageResponse response = info.sendService.sendPrepOrderSimpleMessage(request);
                if (response.getCode() != MessagePack.OK) {
                    throw new RuntimeException("发送消息异常,原因:" + response.getMessage());
                } else {
                    if (jdbcTemplate != null) {
                        for(int i = 0; i < response.getOrderMessageModel().size(); ++i) {
                            insertSeries(((OrderMessageModel)response.getOrderMessageModel().get(i)).getSeries(), ((OrderMessageModel)response.getOrderMessageModel().get(i)).getTopic(), ((OrderMessageModel)response.getOrderMessageModel().get(i)).getTag(), ((OrderMessageModel)response.getOrderMessageModel().get(i)).getSystemId().toString(), ((OrderMessageModel)response.getOrderMessageModel().get(i)).getDataSign(), ((OrderMessageModel)response.getOrderMessageModel().get(i)).getTenantNumId(), jdbcTemplate);
                        }
                    }

                    orderMessageGroupIdThreadLocal.set(response.getOrderMessageGroupId());
                    return response.getOrderMessageGroupId();
                }
            }
        } catch (Exception e) {
            log.error("发送消息异常,异常信息" + e.getMessage(), e);
            throw new RuntimeException("发送消息异常,异常信息" + e.getMessage(), e);
        }
    }

    public static OrderMessageRightNowResponse sendOrderFlowMessageRightNow() {
        try {
            if (((List)simpleOrderMessageThreadLocal.get()).size() < 1) {
                throw new RuntimeException("当前线程并没有添加任何顺序消息实体，请先添加！");
            } else {
                OrderMessageRightNowRequest request = new OrderMessageRightNowRequest();
                request.setDataSign(((SimpleMessage)((List)simpleOrderMessageThreadLocal.get()).get(0)).getDataSign());
                request.setTenantNumId(((SimpleMessage)((List)simpleOrderMessageThreadLocal.get()).get(0)).getTenantNumId());
                request.setSimpleMessage((List)simpleOrderMessageThreadLocal.get());
                OrderMessageRightNowResponse response = info.sendService.sendOrderMessageRightNow(request);
                if (response.getCode() != MessagePack.OK) {
                    throw new RuntimeException("发送消息异常,原因:" + response.getMessage());
                } else {
                    simpleOrderMessageThreadLocal.remove();
                    return response;
                }
            }
        } catch (Exception e) {
            simpleOrderMessageThreadLocal.remove();
            log.error("发送消息异常,异常信息" + e.getMessage(), e);
            throw new RuntimeException("发送消息异常,异常信息" + e.getMessage(), e);
        }
    }

    public static Integer addOrderFlowMessage(String topic, String tag, String msgKey, String body, Long systemId, Long dataSign, Long tenantNumId) {
        if (ipAddress == null) {
            ipAddress = IPUtil.getIP();
        }

        SimpleMessage message = new SimpleMessage(topic, tag, msgKey, body, systemId, dataSign, tenantNumId);
        message.setClientIp(ipAddress);
        if (TxcContext.getCurrentXid() != null) {
            message.setTransactionId(TxcContext.getCurrentXid());
        }

        ((List)simpleOrderMessageThreadLocal.get()).add(message);
        return ((List)simpleOrderMessageThreadLocal.get()).size();
    }

    public static JobCronMessageRightNowSendResponse sendJobCronMessageRightNow(JobMessageModel jobMessageModel) {
        new JobCronMessageRightNowSendResponse();

        try {
            JobCronMessageRightNowSendRequest request = new JobCronMessageRightNowSendRequest();
            request.setDataSign(jobMessageModel.getDataSign());
            request.setTenantNumId(jobMessageModel.getTenantNumId());
            jobMessageModel.setClientIp(ipAddress);
            request.setJobMessageModel(jobMessageModel);
            JobCronMessageRightNowSendResponse response = info.sendService.sendJobCronMessageRightNow(request);
            if (response.getCode() != MessagePack.OK) {
                throw new RuntimeException("发送定时cron消息异常,原因:" + response.getMessage());
            } else {
                return response;
            }
        } catch (Exception e) {
            log.error("发送定时cron消息异常,异常信息" + e.getMessage(), e);
            throw new RuntimeException("发送消息异常,异常信息" + e.getMessage(), e);
        }
    }

    public static JobCronMessageCancelResponse cancelJobCronMessage(Long series) {
        new JobCronMessageCancelResponse();

        try {
            JobCronMessageCancelRequest request = new JobCronMessageCancelRequest();
            request.setSeries(series);
            JobCronMessageCancelResponse response = info.sendService.cancelJobCronMessage(request);
            if (response.getCode() != MessagePack.OK) {
                throw new RuntimeException("取消定时cron消息异常,原因:" + response.getMessage());
            } else {
                return response;
            }
        } catch (Exception e) {
            log.error("取消定时cron消息异常,异常信息" + e.getMessage(), e);
            throw new RuntimeException("取消定时cron消息异常,异常信息" + e.getMessage(), e);
        }
    }

    public static void clearLocal() {
        ((List)threadLocal.get()).clear();
        ((List)simpleOrderMessageThreadLocal.get()).clear();
        orderMessageGroupIdThreadLocal.set(0L);
    }

}

