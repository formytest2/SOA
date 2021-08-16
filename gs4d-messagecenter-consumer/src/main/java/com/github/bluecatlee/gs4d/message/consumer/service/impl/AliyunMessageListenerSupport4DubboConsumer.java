package com.github.bluecatlee.gs4d.message.consumer.service.impl;

import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Message;
import com.github.bluecatlee.gs4d.common.exception.BusinessException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ValidateBusinessException;
import com.github.bluecatlee.gs4d.common.exception.ValidateClientException;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.message.api.model.MsgConsumeModel;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqDubboConsumer;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqTopic;
import com.github.bluecatlee.gs4d.message.consumer.model.SysRocketMqSendLog;
import com.github.bluecatlee.gs4d.message.consumer.service.UpdateConsumerResultService;
import com.github.bluecatlee.gs4d.message.consumer.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class AliyunMessageListenerSupport4DubboConsumer {
    
    protected static Logger logger = LoggerFactory.getLogger(AliyunMessageListenerSupport4DubboConsumer.class);
    
    protected PlatformMqTopic platformMqTopic;
    protected PlatformMqDubboConsumer platformMqDubboConsumer;
    protected Integer dataSign;
    protected Integer retries;
    public static ExecutorService executorService;
    protected Integer zkDataSign;
    protected UpdateConsumerResultService updateConsumerResultService;

    public Integer doConsume(Message message) {
        MessagePack messagePack = new MessagePack();
        SysRocketMqSendLog sysRocketMqSendLog = null;
        Object params = null;
        Method method = null;
        Integer result = 0;
        Long startTime = 0L;
        Long duration = 0L;

        try {
            sysRocketMqSendLog = this.convert(message, this.platformMqTopic.getTOPIC(), this.platformMqTopic.getTAG());
            Long consumerSeries = this.platformMqTopic.getCONSUMER_SERIES();
            Object consumerService = this.getConsumerProxy(consumerSeries, sysRocketMqSendLog.getDATA_SIGN());
            params = this.getConsumerParams(consumerSeries, sysRocketMqSendLog);
            method = this.getConsumerMethod(consumerSeries, consumerService, params);
            startTime = System.currentTimeMillis();
            messagePack = this.callDubboMethod(consumerService, method, params, consumerSeries, messagePack);
            duration = System.currentTimeMillis() - startTime;
        } catch (Exception e) {
            duration = System.currentTimeMillis() - startTime;
            logger.error(e.getMessage(), e);
            ExceptionUtil.processException(e, messagePack);
        }

        List correctCodesList = (List) Constants.topicTagToCorrectCodesList.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG());
        if (!sysRocketMqSendLog.getHAS_LOG().equals("N")) {
            try {
                result = this.handleConsumeResult(sysRocketMqSendLog, messagePack, duration);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                messagePack.setMessage(e.getMessage());
            }

            return result;
        } else {
            return messagePack.getCode() != 0L && (correctCodesList == null || !correctCodesList.contains(String.valueOf(messagePack.getCode()))) ? -1 : 0;
        }
    }

    private SysRocketMqSendLog convert(Message message, String topic, String tag) {
        String body = new String(message.getBody());
        if (StringUtil.isNullOrBlankTrim(body)) {
            throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "消息消费者接收到的信息为空,消息TOPIC:" + topic + "TAG:" + tag);
        } else {
            SysRocketMqSendLog sysRocketMqSendLog = (SysRocketMqSendLog)JSONObject.parseObject(body, SysRocketMqSendLog.class);
            sysRocketMqSendLog.setRETRY_TIMES(message.getReconsumeTimes());
            return sysRocketMqSendLog;
        }
    }

    private Object getConsumerProxy(Long consumerSeries, Long dataSign) {
        Object consumerServiceProxy = null;
        if (SeqUtil.test == this.dataSign) {
            consumerServiceProxy = Constants.serviceNameDubboGroup2consumerServiceProxy_Test.get(this.platformMqDubboConsumer.getSERVICE_NAME() + "_" + this.platformMqDubboConsumer.getDUBBO_GROUP());
        } else if (SeqUtil.develop == this.dataSign) {
            consumerServiceProxy = Constants.serviceNameDubboGroup2consumerServiceProxy_Devepop.get(this.platformMqDubboConsumer.getSERVICE_NAME() + "_" + this.platformMqDubboConsumer.getDUBBO_GROUP());
        } else if (SeqUtil.prod == this.dataSign) {
            consumerServiceProxy = Constants.serviceNameDubboGroup2consumerServiceProxy.get(this.platformMqDubboConsumer.getSERVICE_NAME() + "_" + this.platformMqDubboConsumer.getDUBBO_GROUP());
        }

        if (consumerServiceProxy == null) {
            throw new BusinessException(Constants.SUB_SYSTEM, ExceptionType.BE40101, "消息对应dubbo消费者(series:" + consumerSeries + ")在项目缓存中没有找到具体的代理类");
        } else {
            return consumerServiceProxy;
        }
    }

    private Object getConsumerParams(Long consumerSeries, SysRocketMqSendLog sysRocketMqSendLog) {
        Object params = null;

        try {
            JSONObject jsonObject = JSON.parseObject(sysRocketMqSendLog.getMESSAGE_BODY());
            jsonObject.put("messageSeries", sysRocketMqSendLog.getSERIES() + "");
            jsonObject.put("retryTimes", sysRocketMqSendLog.getRETRY_TIMES());
            params = JSONObject.parseObject(jsonObject.toJSONString(), Class.forName(this.platformMqDubboConsumer.getPARAM_ENTITY()));
            return params;
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
            throw new ValidateClientException(Constants.SUB_SYSTEM, ExceptionType.VCE10031, "dubbo消费者行号" + consumerSeries + "配置的dubbo方法的入参实体映射异常(ClassNotFoundException),异常信息" + e.getMessage());
        }
    }

    private Method getConsumerMethod(Long consumerSeries, Object consumerService, Object params) {
        Method method = null;

        try {
            method = consumerService.getClass().getMethod(this.platformMqDubboConsumer.getMETHOD_NAME(), params.getClass());
            return method;
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage(), e);
            throw new ValidateClientException(Constants.SUB_SYSTEM, ExceptionType.VCE10031, "dubbo消费者行号" + consumerSeries + "配置的dubbo调用方法未找到(NoSuchMethodException),异常信息" + e.getMessage());
        } catch (SecurityException e) {
            logger.error(e.getMessage(), e);
            throw new ValidateClientException(Constants.SUB_SYSTEM, ExceptionType.VCE10031, "dubbo消费者行号" + consumerSeries + "配置的dubbo调用方法发生异常(SecurityException),异常信息" + e.getMessage());
        }
    }

    private MessagePack callDubboMethod(Object service, Method method, Object args, Long consumerSeries, MessagePack messagePack) {
        try {
            Object result = method.invoke(service, args);
            if (result == null) {
                throw new ValidateClientException(Constants.SUB_SYSTEM, ExceptionType.VCE10031, "dubbo消费者行号" + consumerSeries + "配置的dubbo方法返回值为空");
            }

            String resultJsonStr = JSONObject.toJSONString(result);
            JSONObject jsonObject = null;

            try {
                jsonObject = JSONObject.parseObject(resultJsonStr);
            } catch (Exception e) {
                throw new ValidateClientException(Constants.SUB_SYSTEM, ExceptionType.VCE10031, "dubbo消费者行号" + consumerSeries + "配置的dubbo方法返回值不能解析为JSON对象,返回信息为" + resultJsonStr);
            }

            Long code = jsonObject.getLong("code");
            if (code == null) {
                throw new ValidateClientException(Constants.SUB_SYSTEM, ExceptionType.VCE10031, "dubbo消费者行号" + consumerSeries + "配置的dubbo方法返回值的没有返回Code字段");
            }

            messagePack.setConsumerRes(resultJsonStr);
            messagePack.setCode(code);
        } catch (InvocationTargetException e) {
            logger.error("报错内容：" + e.getMessage(), e);
            if (e.getCause().getClass().equals(RpcException.class)) {
                messagePack.setCode(-1L);
                messagePack.setMessage("dubbo调用失败");
            } else {
                messagePack.setConsumerRes("业务失败异常类：" + e.getCause().getClass().getName());
                messagePack.setMessage("业务失败");
                messagePack.setCode(ExceptionType.BE40117.getCode());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            messagePack.setCode(-1L);
            messagePack.setMessage(e.getMessage());
        }

        return messagePack;
    }

    private Integer handleConsumeResult(SysRocketMqSendLog sysRocketMqSendLog, MessagePack messagePack, Long consumeTime) {
        Integer result = 0;
        List correctCodesList = (List) Constants.topicTagToCorrectCodesList.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG());
        MsgConsumeModel msgConsumeModel;
        if (messagePack.getCode() != MessagePack.OK && (correctCodesList == null || !correctCodesList.contains(String.valueOf(messagePack.getCode())))) {
            if (StringUtil.isNullOrBlank(messagePack.getMessage()) || "成功".equals(messagePack.getMessage())) {
                messagePack.setMessage("业务异常");
            }

            if (sysRocketMqSendLog.getJOB_SEND_EMAIL().equals("Y")) {
                this.updateConsumerResultService.updateConsumerFailedResult(sysRocketMqSendLog.getSERIES(), this.retries, this.platformMqTopic.getTASK_TARGET(), messagePack.toJson(), (String) Constants.fw.get(sysRocketMqSendLog.getRETRY_TIMES()), sysRocketMqSendLog.getRETRY_TIMES(), sysRocketMqSendLog.getMESSAGE_TAG(), consumeTime, sysRocketMqSendLog.getTENANT_NUM_ID(), sysRocketMqSendLog.getDATA_SIGN(), Constants.eI);
            } else {
                msgConsumeModel = new MsgConsumeModel();
                msgConsumeModel.setDataSign(sysRocketMqSendLog.getDATA_SIGN());
                msgConsumeModel.setTenantNumId(sysRocketMqSendLog.getTENANT_NUM_ID());
                msgConsumeModel.setMsgSeries(sysRocketMqSendLog.getSERIES());
                msgConsumeModel.setNextretryTime((String) Constants.fw.get(sysRocketMqSendLog.getRETRY_TIMES()));
                msgConsumeModel.setTaskTarget(this.platformMqTopic.getTASK_TARGET());
                msgConsumeModel.setRetryTimes(sysRocketMqSendLog.getRETRY_TIMES());
                msgConsumeModel.setRetryMax(this.retries);
                msgConsumeModel.setConsumerSuccessDetail(messagePack.toJson());
                msgConsumeModel.setConsumeTime(consumeTime);
                msgConsumeModel.setStepId(sysRocketMqSendLog.getSTEP_ID());
                AbstractRocketMqUtil.a(Constants.MESS_SQL, Constants.MESS_UPDATE_FAIL_SQL, sysRocketMqSendLog.getSERIES().toString(), JSONObject.toJSONString(msgConsumeModel));
            }

            result = -1;
        } else {
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

            result = 0;
        }

        return result;
    }
}
