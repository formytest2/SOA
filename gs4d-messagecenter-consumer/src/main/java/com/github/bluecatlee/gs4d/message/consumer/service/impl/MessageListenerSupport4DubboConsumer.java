package com.github.bluecatlee.gs4d.message.consumer.service.impl;

import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.filter.ExceptionFilter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.github.bluecatlee.gs4d.common.exception.BusinessException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ValidateBusinessException;
import com.github.bluecatlee.gs4d.common.exception.ValidateClientException;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqDubboConsumer;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqTopic;
import com.github.bluecatlee.gs4d.message.consumer.model.SysRocketMqSendLog;
import com.github.bluecatlee.gs4d.message.consumer.service.UpdateConsumerResultService;
import com.github.bluecatlee.gs4d.message.consumer.utils.Constants;
import com.github.bluecatlee.gs4d.message.consumer.utils.MessagePack;
import com.github.bluecatlee.gs4d.message.consumer.utils.SeqUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class MessageListenerSupport4DubboConsumer extends ExceptionFilter {

    protected static Logger logger = LoggerFactory.getLogger(MessageListenerSupport4DubboConsumer.class);
    
    protected PlatformMqTopic platformMqTopic;
    protected PlatformMqDubboConsumer platformMqDubboConsumer;
    protected Integer dataSign;
    protected Integer retries;
    public static ExecutorService executorService;
    protected Integer zkDataSign;
    protected UpdateConsumerResultService updateConsumerResultService;
    protected String noConsumerTag;

    /**
     * 消息内容转换
     * @param msgExtList
     * @return
     */
    protected SysRocketMqSendLog convert(List<MessageExt> msgExtList) {
        MessageExt msgExt = (MessageExt)msgExtList.get(0);
        String body = new String(msgExt.getBody());
        if (StringUtil.isNullOrBlankTrim(body)) {
            throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "消息消费者接收到的信息为空,消息TOPIC:" + this.platformMqTopic.getTOPIC() + "TAG:" + this.platformMqTopic.getTAG());
        } else {
            SysRocketMqSendLog sysRocketMqSendLog = (SysRocketMqSendLog)JSONObject.parseObject(body, SysRocketMqSendLog.class);
            sysRocketMqSendLog.setRETRY_TIMES(msgExt.getReconsumeTimes());
            return sysRocketMqSendLog;
        }
    }

    /**
     * 获取消费者代理对象
     * @param consumerSeries
     * @param dataSign
     * @return
     */
    protected Object getConsumerProxy(Long consumerSeries, Long dataSign) {
        Object consumerServiceProxy = null;
        if (SeqUtil.test == this.dataSign) {
            consumerServiceProxy = Constants.serviceNameDubboGroup2consumerServiceProxy_Test.get(this.platformMqDubboConsumer.getSERVICE_NAME() + "_" + this.platformMqDubboConsumer.getDUBBO_GROUP());
        } else if (SeqUtil.develop == this.dataSign) {
            consumerServiceProxy = Constants.serviceNameDubboGroup2consumerServiceProxy_Devepop.get(this.platformMqDubboConsumer.getSERVICE_NAME() + "_" + this.platformMqDubboConsumer.getDUBBO_GROUP());
        } else if (SeqUtil.prod == this.dataSign) {
            if (this.zkDataSign != null && this.zkDataSign != 0) {
                if (dataSign == (long)SeqUtil.prod) {
                    consumerServiceProxy = Constants.serviceNameDubboGroup2consumerServiceProxy.get(this.platformMqDubboConsumer.getSERVICE_NAME() + "_" + this.platformMqDubboConsumer.getDUBBO_GROUP());
                } else {
                    consumerServiceProxy = Constants.serviceNameDubboGroup2consumerServiceProxy_Test.get(this.platformMqDubboConsumer.getSERVICE_NAME() + "_" + this.platformMqDubboConsumer.getDUBBO_GROUP() + "_test");
                }
            } else {
                consumerServiceProxy = Constants.serviceNameDubboGroup2consumerServiceProxy.get(this.platformMqDubboConsumer.getSERVICE_NAME() + "_" + this.platformMqDubboConsumer.getDUBBO_GROUP());
            }
        }

        if (consumerServiceProxy == null) {
            throw new BusinessException(Constants.SUB_SYSTEM, ExceptionType.BE40101, "消息对应dubbo消费者(series:" + consumerSeries + ")在项目缓存中没有找到具体的代理类");
        } else {
            return consumerServiceProxy;
        }
    }

    /**
     * 获取消费入参
     * @param consumerSeries        消费者行号
     * @param sysRocketMqSendLog    消息发送日志
     * @return
     */
    protected Object getConsumerParams(Long consumerSeries, SysRocketMqSendLog sysRocketMqSendLog) {
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

    /**
     * 获取消费方法
     * @param consumerSeries    消费者行号
     * @param consumerService   消费者service
     * @param params            消费入参
     * @return
     */
    protected Method getConsumerMethod(Long consumerSeries, Object consumerService, Object params) {
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

    /**
     * 调用dubbo消费者的消费方法
     * @param service           消费者service
     * @param method            消费方法
     * @param args              消费入参
     * @param consumerSeries    消费者series
     * @param messagePack
     * @return
     */
    protected MessagePack callDubboMethod(Object service, Method method, Object args, Long consumerSeries, MessagePack messagePack) {
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
            logger.error("dubboSeries内容为" + consumerSeries + "报错内容：" + e.getMessage() + "消息内容为:" + JSON.toJSONString(args), e);
            if (e.getCause().getClass().equals(RpcException.class)) {
                messagePack.setCode(-1L);
                messagePack.setMessage("dubbo调用失败报错内容：" + e.getMessage() + e.getCause().getMessage());
            } else {
                messagePack.setConsumerRes("业务失败异常类：" + e.getCause().getClass().getName());
                messagePack.setMessage("业务失败报错内容：" + e.getMessage());
                messagePack.setCode(ExceptionType.BE40117.getCode());
            }
        } catch (Exception e) {
            logger.error("超级异常dubboSeries内容为" + consumerSeries + "报错内容：" + e.getMessage(), e);
            logger.error(e.getMessage(), e);
            messagePack.setCode(-1L);
            messagePack.setMessage(e.getMessage());
        }

        return messagePack;
    }
}
