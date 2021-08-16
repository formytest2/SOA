package com.github.bluecatlee.gs4d.message.consumer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.common.message.MessageExt;
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
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageListenerOrderlyImpl4DubboConsumer extends MessageListenerSupport4DubboConsumer implements MessageListenerOrderly {

    private AtomicInteger cH = new AtomicInteger(0);

    public MessageListenerOrderlyImpl4DubboConsumer(PlatformMqTopic platformMqTopic, PlatformMqDubboConsumer platformMqDubboConsumer, int dataSign, Integer retries, Integer zkDataSign, UpdateConsumerResultService updateConsumerResultService) {
        this.platformMqTopic = platformMqTopic;
        this.platformMqDubboConsumer = platformMqDubboConsumer;
        this.dataSign = dataSign;
        this.retries = retries;
        this.zkDataSign = zkDataSign;
        this.updateConsumerResultService = updateConsumerResultService;
    }

    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgExtList, ConsumeOrderlyContext consumeOrderlyContext) {
        MessagePack messagePack = new MessagePack();
        SysRocketMqSendLog sysRocketMqSendLog = null;
        Object params = null;
        Method method = null;
        ConsumeOrderlyStatus consumeOrderlyStatus = ConsumeOrderlyStatus.SUCCESS;
        Long startTime = 0L;
        Long duration = 0L;

        try {
            sysRocketMqSendLog = this.convert(msgExtList);
            Long consumerSeries = this.platformMqTopic.getCONSUMER_SERIES();
            Object consumerService = this.getConsumerProxy(consumerSeries, sysRocketMqSendLog.getDATA_SIGN());
            params = this.getConsumerParams(consumerSeries, sysRocketMqSendLog);
            method = this.getConsumerMethod(consumerSeries, consumerService, params);
            startTime = System.currentTimeMillis();
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
                consumeOrderlyStatus = ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            } else {
                consumeOrderlyStatus = ConsumeOrderlyStatus.SUCCESS;
            }
        }

        try {
            consumeOrderlyStatus = this.handleConsumeConcurrentlyStatus(sysRocketMqSendLog, messagePack, params, method, consumeOrderlyContext, duration);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            consumeOrderlyStatus = ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            messagePack.setMessage(e.getMessage());
        }

        return consumeOrderlyStatus;
    }

    private ConsumeOrderlyStatus handleConsumeConcurrentlyStatus(SysRocketMqSendLog sysRocketMqSendLog, MessagePack messagePack, Object params, Method method, ConsumeOrderlyContext consumeOrderlyContext, Long consumeTime) {
        ConsumeOrderlyStatus consumeOrderlyStatus = null;
        List correctCodesList = (List) Constants.topicTagToCorrectCodesList.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG());
        MsgConsumeModel msgConsumeModel;
        if (messagePack.getCode() == MessagePack.OK || correctCodesList != null && correctCodesList.contains(String.valueOf(messagePack.getCode()))) {
            logger.info("序列为" + sysRocketMqSendLog.getSERIES() + ",tag为" + sysRocketMqSendLog.getMESSAGE_TAG() + "的序列消费成功");
            if (sysRocketMqSendLog.getJOB_SEND_EMAIL().equals("Y")) {
                this.updateConsumerResultService.updateConsumerSuccessResult(sysRocketMqSendLog.getSERIES(), this.platformMqTopic.getTASK_TARGET(), messagePack.toJson(), consumeTime, sysRocketMqSendLog.getSTEP_ID());
            } else {
                msgConsumeModel = new MsgConsumeModel();
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

            if (sysRocketMqSendLog.getORDER_MESS_FLAG() != null && sysRocketMqSendLog.getORDER_MESS_FLAG() == Constants.eA) {
                OrderFlowerModel orderFlowerModel = new OrderFlowerModel();
                orderFlowerModel.setWorkflowId(sysRocketMqSendLog.getWORKFLOW_ID());
                AbstractRocketMqUtil.a(Constants.FLOWER_TOPIC, Constants.ORDER_FLOWER_TAG, sysRocketMqSendLog.getSERIES().toString(), JSONObject.toJSONString(orderFlowerModel));
            }

            consumeOrderlyStatus = ConsumeOrderlyStatus.SUCCESS;
        } else {
            if (StringUtil.isNullOrBlank(messagePack.getMessage()) || "成功".equals(messagePack.getMessage())) {
                messagePack.setMessage("业务异常");
            }

            logger.info("序列为" + sysRocketMqSendLog.getSERIES() + ",tag为" + sysRocketMqSendLog.getMESSAGE_TAG() + "的序列消费失败，原因" + messagePack.toJson());
            consumeOrderlyContext.setSuspendCurrentQueueTimeMillis(60000L);
            if (sysRocketMqSendLog.getJOB_SEND_EMAIL().equals("Y")) {
                this.updateConsumerResultService.updateConsumerFailedResult(sysRocketMqSendLog.getSERIES(), this.retries, this.platformMqTopic.getTASK_TARGET(), messagePack.toJson(), "1m", sysRocketMqSendLog.getRETRY_TIMES(), sysRocketMqSendLog.getMESSAGE_TAG(), consumeTime, sysRocketMqSendLog.getTENANT_NUM_ID(), sysRocketMqSendLog.getDATA_SIGN(), Constants.eI);
            } else {
                msgConsumeModel = new MsgConsumeModel();
                msgConsumeModel.setDataSign(sysRocketMqSendLog.getDATA_SIGN());
                msgConsumeModel.setTenantNumId(sysRocketMqSendLog.getTENANT_NUM_ID());
                msgConsumeModel.setMsgSeries(sysRocketMqSendLog.getSERIES());
                msgConsumeModel.setNextretryTime("1m");
                msgConsumeModel.setTaskTarget(this.platformMqTopic.getTASK_TARGET());
                msgConsumeModel.setRetryTimes(sysRocketMqSendLog.getRETRY_TIMES());
                msgConsumeModel.setRetryMax(this.retries);
                msgConsumeModel.setConsumerSuccessDetail(messagePack.toJson());
                msgConsumeModel.setConsumeTime(consumeTime);
                msgConsumeModel.setStepId(sysRocketMqSendLog.getSTEP_ID());
                AbstractRocketMqUtil.a(Constants.MESS_SQL, Constants.MESS_UPDATE_FAIL_SQL, sysRocketMqSendLog.getSERIES().toString(), JSONObject.toJSONString(msgConsumeModel));
            }

            consumeOrderlyStatus = ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
        }

        return consumeOrderlyStatus;
    }
}
