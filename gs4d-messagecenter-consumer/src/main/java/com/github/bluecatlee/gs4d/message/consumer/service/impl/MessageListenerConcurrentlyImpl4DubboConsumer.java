package com.github.bluecatlee.gs4d.message.consumer.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ValidateBusinessException;
import com.github.bluecatlee.gs4d.common.exception.ValidateClientException;
import com.github.bluecatlee.gs4d.common.utils.GipUtil;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.message.api.model.MsgConsumeModel;
import com.github.bluecatlee.gs4d.message.api.model.OrderFlowerModel;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqDubboConsumer;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqTopic;
import com.github.bluecatlee.gs4d.message.consumer.model.SysRocketMqSendLog;
import com.github.bluecatlee.gs4d.message.consumer.service.UpdateConsumerResultService;
import com.github.bluecatlee.gs4d.message.consumer.utils.AbstractRocketMqUtil;
import com.github.bluecatlee.gs4d.message.consumer.utils.Constants;
import com.github.bluecatlee.gs4d.message.consumer.utils.ExceptionUtil;
import com.github.bluecatlee.gs4d.message.consumer.utils.MessagePack;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageListenerConcurrentlyImpl4DubboConsumer extends MessageListenerSupport4DubboConsumer implements MessageListenerConcurrently {

    private AtomicInteger cH = new AtomicInteger(0);

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public MessageListenerConcurrentlyImpl4DubboConsumer(PlatformMqTopic platformMqTopic, PlatformMqDubboConsumer platformMqDubboConsumer, Integer dataSign, Integer retries, Integer zkDataSign, UpdateConsumerResultService updateConsumerResultService, String noConsumerTag) {
        this.platformMqTopic = platformMqTopic;
        this.platformMqDubboConsumer = platformMqDubboConsumer;
        this.dataSign = dataSign;
        this.retries = retries;
        this.zkDataSign = zkDataSign;
        this.updateConsumerResultService = updateConsumerResultService;
        this.noConsumerTag = noConsumerTag;
    }

    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgExtList, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        MessagePack messagePack = new MessagePack();
        SysRocketMqSendLog sysRocketMqSendLog = null;
        List sysRocketMqSendLogList = null;
        Object params = null;
        Method method = null;
        ConsumeConcurrentlyStatus consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        Long startTime = 0L;
        Long duration = 0L;
        Long consumerSeries;
        Object consumerService;
        if (msgExtList.size() == 1) {
            try {
                sysRocketMqSendLog = this.convert(msgExtList);
                if ("Y".equals(sysRocketMqSendLog.getEXPRESS_FLAG())) {
                    String body = sysRocketMqSendLog.getMESSAGE_BODY();
                    logger.info("发现压缩消息,压缩前" + body);
                    String unexpressedBody = GipUtil.gunzip(body);
                    logger.info("发现压缩消息,压缩后" + unexpressedBody);
                    sysRocketMqSendLog.setMESSAGE_BODY(unexpressedBody);
                }

                if (this.noConsumerTag != null && this.noConsumerTag.contains(sysRocketMqSendLog.getMESSAGE_TAG())) {
                    try {
                        messagePack.setMessage("这个tag是不消费tag,如果想消费,请把消息中心配置文件application.production.properties中的noConsumerTag参数去除掉当前tag");
                        this.handleConsumeConcurrentlyStatus(sysRocketMqSendLog, messagePack, consumeConcurrentlyContext, duration);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        messagePack.setMessage(e.getMessage());
                    }

                    return consumeConcurrentlyStatus;
                }

                consumerSeries = this.platformMqTopic.getCONSUMER_SERIES();
                consumerService = this.getConsumerProxy(consumerSeries, sysRocketMqSendLog.getDATA_SIGN());
                params = this.getConsumerParams(consumerSeries, sysRocketMqSendLog);
                method = this.getConsumerMethod(consumerSeries, consumerService, params);
                startTime = System.currentTimeMillis();
                logger.debug("序列号为" + sysRocketMqSendLog.getSERIES() + ",当前时间为" + this.dateFormat.format(new Date()));  //【消费核心 调用配置的dubbo方法】
                messagePack = this.callDubboMethod(consumerService, method, params, consumerSeries, messagePack);
                duration = System.currentTimeMillis() - startTime;
            } catch (Throwable e) {
                duration = System.currentTimeMillis() - startTime;
                logger.error(e.getMessage(), e);
                ExceptionUtil.processException(e, messagePack);
            }

            List correctCodesList = (List) Constants.topicTagToCorrectCodesList.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG());
            if (sysRocketMqSendLog.getHAS_LOG().equals("N")) {
                if (messagePack.getCode() != 0L && (correctCodesList == null || !correctCodesList.contains(String.valueOf(messagePack.getCode())))) {
                    consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.RECONSUME_LATER;
                } else {
                    consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            }

            try {
                consumeConcurrentlyStatus = this.handleConsumeConcurrentlyStatus(sysRocketMqSendLog, messagePack, consumeConcurrentlyContext, duration);
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
                consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.RECONSUME_LATER;
                messagePack.setMessage(e.getMessage());
            }
        } else {
            try {
                sysRocketMqSendLogList = this.batchConvert(msgExtList);
                consumerSeries = this.platformMqTopic.getCONSUMER_SERIES();
                consumerService = this.getConsumerProxy(consumerSeries, ((SysRocketMqSendLog)sysRocketMqSendLogList.get(0)).getDATA_SIGN());
                params = this.getConsumerParams(consumerSeries, sysRocketMqSendLogList);
                method = this.getConsumerMethod(consumerSeries, consumerService, params);
                startTime = System.currentTimeMillis();
                messagePack = this.callDubboMethod(consumerService, method, params, consumerSeries, messagePack);
                duration = System.currentTimeMillis() - startTime;
            } catch (Throwable e) {
                duration = System.currentTimeMillis() - startTime;
                logger.error(e.getMessage(), e);
                messagePack.setMessage(e.getMessage());
                ExceptionUtil.processException(e, messagePack);
            }

            try {
                for(int i = 0; i < sysRocketMqSendLogList.size(); ++i) {
                    if (!((SysRocketMqSendLog)sysRocketMqSendLogList.get(i)).getHAS_LOG().equals("N")) {
                        consumeConcurrentlyStatus = this.handleConsumeConcurrentlyStatus((SysRocketMqSendLog)sysRocketMqSendLogList.get(i), messagePack, consumeConcurrentlyContext, duration);
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }

        return consumeConcurrentlyStatus;
    }

    private Object getConsumerParams(Long consumerSeries, List<SysRocketMqSendLog> sysRocketMqSendLogList) {
        Object params = null;
        ArrayList list = new ArrayList();

        for(int i = 0; i < sysRocketMqSendLogList.size(); ++i) {
            JSONObject jsonObject = JSON.parseObject((sysRocketMqSendLogList.get(i)).getMESSAGE_BODY());
            jsonObject.put("messageSeries", (sysRocketMqSendLogList.get(i)).getSERIES() + "");
            jsonObject.put("retryTimes", (sysRocketMqSendLogList.get(i)).getRETRY_TIMES());
            list.add(jsonObject.toJSONString());
        }

        try {
            JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(list));
            params = JSONArray.parseObject(jsonArray.toJSONString(), Class.forName(this.platformMqDubboConsumer.getPARAM_ENTITY()));
            return params;
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
            throw new ValidateClientException(Constants.SUB_SYSTEM, ExceptionType.VCE10031, "dubbo消费者行号" + consumerSeries + "配置的dubbo方法的入参实体映射异常(ClassNotFoundException),异常信息" + e.getMessage());
        }
    }

    private List<SysRocketMqSendLog> batchConvert(List<MessageExt> msgExtList) {
        ArrayList list = new ArrayList();

        for(int i = 0; i < msgExtList.size(); ++i) {
            MessageExt msgExt = (MessageExt)msgExtList.get(i);
            String body = new String(msgExt.getBody());
                if (StringUtil.isNullOrBlankTrim(body)) {
                throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "消息消费者接收到的信息为空,消息TOPIC:" + this.platformMqTopic.getTOPIC() + "TAG:" + this.platformMqTopic.getTAG());
            }

            SysRocketMqSendLog sysRocketMqSendLog = (SysRocketMqSendLog)JSONObject.parseObject(body, SysRocketMqSendLog.class);
            sysRocketMqSendLog.setRETRY_TIMES(msgExt.getReconsumeTimes());
            list.add(sysRocketMqSendLog);
        }

        return list;
    }

    private ConsumeConcurrentlyStatus handleConsumeConcurrentlyStatus(SysRocketMqSendLog sysRocketMqSendLog, MessagePack messagePack, ConsumeConcurrentlyContext consumeConcurrentlyContext, Long consumeTime) {
        ConsumeConcurrentlyStatus consumeConcurrentlyStatus = null;
        List correctCodesList = (List) Constants.topicTagToCorrectCodesList.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG());
        if (messagePack.getCode() != MessagePack.OK && (correctCodesList == null || !correctCodesList.contains(String.valueOf(messagePack.getCode())))) {
            if (StringUtil.isNullOrBlank(messagePack.getMessage()) || "成功".equals(messagePack.getMessage())) {
                messagePack.setMessage("业务异常");
            }

            logger.info("序列为" + sysRocketMqSendLog.getSERIES() + ",tag为" + sysRocketMqSendLog.getMESSAGE_TAG() + "的序列消费失败，原因" + messagePack.toJson());
            List retryIntervalList = (List) Constants.topicTagToRetryIntervalList.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG());
            if (retryIntervalList != null && !retryIntervalList.isEmpty()) {
                if (sysRocketMqSendLog.getRETRY_TIMES() == 0) {
                    consumeConcurrentlyContext.setDelayLevelWhenNextConsume(Integer.valueOf((String)retryIntervalList.get(0)));
                } else {
                    consumeConcurrentlyContext.setDelayLevelWhenNextConsume(Integer.valueOf((String)retryIntervalList.get(sysRocketMqSendLog.getRETRY_TIMES() % retryIntervalList.size())));
                }
            }

            Integer delayLevelWhenNextConsume = consumeConcurrentlyContext.getDelayLevelWhenNextConsume();
            String retryInterval = (String) Constants.retryIntervalList.get(delayLevelWhenNextConsume);
            if ("Y".equals(sysRocketMqSendLog.getJOB_SEND_EMAIL())) {
                this.updateConsumerResultService.updateConsumerFailedResult(sysRocketMqSendLog.getSERIES(), delayLevelWhenNextConsume, this.platformMqTopic.getTASK_TARGET(), messagePack.toJson(),
                        retryInterval, sysRocketMqSendLog.getRETRY_TIMES(), sysRocketMqSendLog.getMESSAGE_TAG(), consumeTime, sysRocketMqSendLog.getTENANT_NUM_ID(), sysRocketMqSendLog.getDATA_SIGN(), Constants.eI);
            } else {
                MsgConsumeModel msgConsumeModel = new MsgConsumeModel();
                msgConsumeModel.setDataSign(sysRocketMqSendLog.getDATA_SIGN());
                msgConsumeModel.setTenantNumId(sysRocketMqSendLog.getTENANT_NUM_ID());
                msgConsumeModel.setMsgSeries(sysRocketMqSendLog.getSERIES());
                msgConsumeModel.setNextretryTime(retryInterval);
                msgConsumeModel.setTaskTarget(this.platformMqTopic.getTASK_TARGET());
                msgConsumeModel.setRetryTimes(sysRocketMqSendLog.getRETRY_TIMES());
                msgConsumeModel.setRetryMax(this.retries);
                msgConsumeModel.setConsumerSuccessDetail(messagePack.toJson());
                msgConsumeModel.setConsumeTime(consumeTime);
                msgConsumeModel.setStepId(sysRocketMqSendLog.getSTEP_ID());
                AbstractRocketMqUtil.a(Constants.MESS_SQL, Constants.MESS_UPDATE_FAIL_SQL, sysRocketMqSendLog.getSERIES().toString(), JSONObject.toJSONString(msgConsumeModel));
            }

            consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.RECONSUME_LATER;
        } else {
            logger.info("序列为" + sysRocketMqSendLog.getSERIES() + ",tag为" + sysRocketMqSendLog.getMESSAGE_TAG() + "的序列消费成功");
            if ("Y".equals(sysRocketMqSendLog.getJOB_SEND_EMAIL())) {
                // 更新消息发送日志为成功
                this.updateConsumerResultService.updateConsumerSuccessResult(sysRocketMqSendLog.getSERIES(), this.platformMqTopic.getTASK_TARGET(), messagePack.toJson(), consumeTime, sysRocketMqSendLog.getSTEP_ID());
            } else {
                MsgConsumeModel msgConsumeModel = new MsgConsumeModel();
                msgConsumeModel.setDataSign(sysRocketMqSendLog.getDATA_SIGN());
                msgConsumeModel.setTenantNumId(sysRocketMqSendLog.getTENANT_NUM_ID());
                msgConsumeModel.setMsgSeries(sysRocketMqSendLog.getSERIES());
                msgConsumeModel.setTaskTarget(this.platformMqTopic.getTASK_TARGET());
                msgConsumeModel.setRetryTimes(sysRocketMqSendLog.getRETRY_TIMES());
                msgConsumeModel.setRetryMax(this.retries);
                msgConsumeModel.setConsumerSuccessDetail(messagePack.toJson());
                msgConsumeModel.setConsumeTime(consumeTime);
                msgConsumeModel.setStepId(sysRocketMqSendLog.getSTEP_ID());
                AbstractRocketMqUtil.a(Constants.MESS_SQL, Constants.MESS_UPDATE_SUCC_SQL, sysRocketMqSendLog.getSERIES().toString(), JSONObject.toJSONString(msgConsumeModel));
            }

            if (sysRocketMqSendLog.getORDER_MESS_FLAG() != null && sysRocketMqSendLog.getORDER_MESS_FLAG().equals(Constants.eA)) {
                OrderFlowerModel orderFlowerModel = new OrderFlowerModel();
                orderFlowerModel.setWorkflowId(sysRocketMqSendLog.getWORKFLOW_ID());
                AbstractRocketMqUtil.a(Constants.FLOWER_TOPIC, Constants.ORDER_FLOWER_TAG, sysRocketMqSendLog.getSERIES().toString(), JSONObject.toJSONString(orderFlowerModel));
            }

            consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }

        return consumeConcurrentlyStatus;
    }
}
