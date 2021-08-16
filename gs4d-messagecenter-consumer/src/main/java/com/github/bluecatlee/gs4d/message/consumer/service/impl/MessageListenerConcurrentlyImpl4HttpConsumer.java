package com.github.bluecatlee.gs4d.message.consumer.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.github.bluecatlee.gs4d.common.exception.BusinessException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ValidateBusinessException;
import com.github.bluecatlee.gs4d.common.exception.ValidateClientException;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.message.api.model.MsgConsumeModel;
import com.github.bluecatlee.gs4d.message.api.model.OrderFlowerModel;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqHttpConsumer;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqTopic;
import com.github.bluecatlee.gs4d.message.consumer.model.SysRocketMqSendLog;
import com.github.bluecatlee.gs4d.message.consumer.utils.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class MessageListenerConcurrentlyImpl4HttpConsumer implements MessageListenerConcurrently {
    
    private static Logger logger = LoggerFactory.getLogger(MessageListenerConcurrentlyImpl4HttpConsumer.class);
    
    private PlatformMqTopic platformMqTopic;
    private PlatformMqHttpConsumer platformMqHttpConsumer;
    private Integer dataSign;
    private Integer retries;
    public static ExecutorService executorService;
    private Integer zkDataSign;

    public MessageListenerConcurrentlyImpl4HttpConsumer(PlatformMqTopic platformMqTopic, PlatformMqHttpConsumer platformMqHttpConsumer, Integer dataSign, Integer retries, Integer zkDataSign) {
        this.platformMqTopic = platformMqTopic;
        this.platformMqHttpConsumer = platformMqHttpConsumer;
        this.dataSign = dataSign;
        this.retries = retries;
        this.zkDataSign = zkDataSign;
    }

    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgExtList, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        MessagePack messagePack = new MessagePack();
        SysRocketMqSendLog sysRocketMqSendLog = null;
        Long startTime = 0L;
        Long duration = 0L;
        Object correctCodesList = new ArrayList();
        ConsumeConcurrentlyStatus consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.RECONSUME_LATER;

        String messageBody;
        try {
            MessageExt msgExt = (MessageExt)msgExtList.get(0);
            String body = new String(msgExt.getBody());
            if (StringUtil.isNullOrBlankTrim(body)) {
                throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.BE40101, "消息消费者接收到的信息为空,消息TOPIC:" + this.platformMqTopic.getTOPIC() + "TAG:" + this.platformMqTopic.getTAG());
            }

            sysRocketMqSendLog = JSON.parseObject(body, SysRocketMqSendLog.class);
            correctCodesList = Constants.topicTagToCorrectCodesList.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG());
            sysRocketMqSendLog.setRETRY_TIMES(msgExt.getReconsumeTimes());
            messageBody = sysRocketMqSendLog.getMESSAGE_BODY();
            JSONObject jsonObject = JSON.parseObject(messageBody);
            jsonObject.put("retryTimes", msgExt.getReconsumeTimes() + "");
            messageBody = jsonObject.toJSONString();
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = null;
            if (SeqUtil.test == this.dataSign) {
                httpPost = new HttpPost(this.platformMqHttpConsumer.getURL_TEST());
            } else if (SeqUtil.develop == this.dataSign) {
                httpPost = new HttpPost(this.platformMqHttpConsumer.getURL_DEVELOP());
            } else if (SeqUtil.prod == this.dataSign) {
                if (this.zkDataSign != null && this.zkDataSign != 0) {
                    if (sysRocketMqSendLog.getDATA_SIGN() == (long)SeqUtil.prod) {
                        httpPost = new HttpPost(this.platformMqHttpConsumer.getURL());
                    } else {
                        httpPost = new HttpPost(this.platformMqHttpConsumer.getURL_TEST());
                    }
                } else {
                    httpPost = new HttpPost(this.platformMqHttpConsumer.getURL());
                }
            }

            if (StringUtil.isAllNotNullOrBlank(new String[]{this.platformMqHttpConsumer.getHTTP_HEAD()})) {
                httpPost.setHeader("Content-type", this.platformMqHttpConsumer.getHTTP_HEAD());
                httpPost.setEntity(new StringEntity(messageBody, Charset.forName("UTF-8")));
            } else {
                ArrayList parameters = new ArrayList();
                parameters.add(new BasicNameValuePair(this.platformMqHttpConsumer.getPARAM_NAME(), messageBody));
                httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
            }

            startTime = System.currentTimeMillis();
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            duration = System.currentTimeMillis() - startTime;
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "http消费者行号" + this.platformMqHttpConsumer.getSERIES() + "配置的http方法的调用异常" + httpResponse.getStatusLine().getStatusCode());
            }

            JSONObject responseEntity = JSON.parseObject(EntityUtils.toString(httpResponse.getEntity()));
            if (responseEntity == null) {
                throw new ValidateClientException(Constants.SUB_SYSTEM, ExceptionType.VCE10031, "http消费者行号" + this.platformMqHttpConsumer.getSERIES() + "配置的http方法的没有返回值为null ");
            }

            Long code = responseEntity.getLong("Code");
            if (code == null) {
                code = responseEntity.getLong("code");
                if (code == null) {
                    throw new ValidateClientException(Constants.SUB_SYSTEM, ExceptionType.VCE10031, "http消费者行号" + this.platformMqHttpConsumer.getSERIES() + "配置的http方法的没有返回Code字段");
                }
            }

            messagePack.setCode(code);
            messagePack.setConsumerRes(responseEntity.toString());
            if (code != MessagePack.OK && correctCodesList != null && !((List)correctCodesList).contains(String.valueOf(messagePack.getCode()))) {
                throw new BusinessException(Constants.SUB_SYSTEM, ExceptionType.BE40101, "http消费者行号" + this.platformMqHttpConsumer.getSERIES() + "配置的http方法的执行失败");
            }
        } catch (Exception e) {
            duration = System.currentTimeMillis() - startTime;
            logger.error(e.getMessage(), e);
            ExceptionUtil.processException(e, messagePack);
        }

        if (sysRocketMqSendLog.getHAS_LOG().equals("N")) {
            return messagePack.getCode() != 0L && (correctCodesList == null || !((List)correctCodesList).contains(String.valueOf(messagePack.getCode()))) ? ConsumeConcurrentlyStatus.RECONSUME_LATER : ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } else {
            try {
                if (messagePack.getCode() != MessagePack.OK && (correctCodesList == null || !((List)correctCodesList).contains(String.valueOf(messagePack.getCode())))) {
                    messagePack.setMessage("业务异常");
                    List retryIntervalList = (List) Constants.topicTagToRetryIntervalList.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG());
                    if (retryIntervalList != null && !retryIntervalList.isEmpty()) {
                        logger.warn("当前内容：" + consumeConcurrentlyContext.getDelayLevelWhenNextConsume() + "当前坐标" + retryIntervalList.indexOf(consumeConcurrentlyContext.getDelayLevelWhenNextConsume()));
                        logger.warn("当前集合长度" + retryIntervalList.size());
                        if (sysRocketMqSendLog.getRETRY_TIMES() == 0) {
                            consumeConcurrentlyContext.setDelayLevelWhenNextConsume(Integer.valueOf((String)retryIntervalList.get(0)));
                        } else {
                            consumeConcurrentlyContext.setDelayLevelWhenNextConsume(Integer.valueOf((String)retryIntervalList.get(sysRocketMqSendLog.getRETRY_TIMES() % retryIntervalList.size())));
                        }
                    }

                    Integer delayLevelWhenNextConsume = consumeConcurrentlyContext.getDelayLevelWhenNextConsume();
                    String retryTime = (String) Constants.retryIntervalList.get(delayLevelWhenNextConsume);
                    MsgConsumeModel msgConsumeModel = new MsgConsumeModel();
                    msgConsumeModel.setDataSign(sysRocketMqSendLog.getDATA_SIGN());
                    msgConsumeModel.setTenantNumId(sysRocketMqSendLog.getTENANT_NUM_ID());
                    msgConsumeModel.setMsgSeries(sysRocketMqSendLog.getSERIES());
                    msgConsumeModel.setNextretryTime(retryTime);
                    msgConsumeModel.setTaskTarget(this.platformMqTopic.getTASK_TARGET());
                    msgConsumeModel.setRetryTimes(sysRocketMqSendLog.getRETRY_TIMES());
                    msgConsumeModel.setRetryMax(this.retries);
                    msgConsumeModel.setConsumerSuccessDetail(messagePack.toJson());
                    msgConsumeModel.setConsumeTime(duration);
                    msgConsumeModel.setStepId(sysRocketMqSendLog.getSTEP_ID());
                    AbstractRocketMqUtil.a(Constants.MESS_SQL, Constants.MESS_UPDATE_FAIL_SQL, sysRocketMqSendLog.getSERIES().toString(), JSONObject.toJSONString(msgConsumeModel));
                    consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.RECONSUME_LATER;
                } else {
                    MsgConsumeModel msgConsumeModel = new MsgConsumeModel();
                    msgConsumeModel.setDataSign(sysRocketMqSendLog.getDATA_SIGN());
                    msgConsumeModel.setTenantNumId(sysRocketMqSendLog.getTENANT_NUM_ID());
                    msgConsumeModel.setMsgSeries(sysRocketMqSendLog.getSERIES());
                    msgConsumeModel.setTaskTarget(this.platformMqTopic.getTASK_TARGET());
                    msgConsumeModel.setRetryTimes(sysRocketMqSendLog.getRETRY_TIMES());
                    msgConsumeModel.setRetryMax(this.retries);
                    msgConsumeModel.setConsumerSuccessDetail(messagePack.toJson());
                    msgConsumeModel.setConsumeTime(duration);
                    msgConsumeModel.setStepId(sysRocketMqSendLog.getSTEP_ID());
                    AbstractRocketMqUtil.a(Constants.MESS_SQL, Constants.MESS_UPDATE_SUCC_SQL, sysRocketMqSendLog.getSERIES().toString(), JSONObject.toJSONString(msgConsumeModel));
                    if (sysRocketMqSendLog.getORDER_MESS_FLAG() != null && sysRocketMqSendLog.getORDER_MESS_FLAG() == Constants.eA) {
                        OrderFlowerModel orderFlowerModel = new OrderFlowerModel();
                        orderFlowerModel.setWorkflowId(sysRocketMqSendLog.getWORKFLOW_ID());
                        AbstractRocketMqUtil.a(Constants.FLOWER_TOPIC, Constants.ORDER_FLOWER_TAG, sysRocketMqSendLog.getSERIES().toString(), JSONObject.toJSONString(orderFlowerModel));
                    }

                    consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            return consumeConcurrentlyStatus;
        }
    }
}
