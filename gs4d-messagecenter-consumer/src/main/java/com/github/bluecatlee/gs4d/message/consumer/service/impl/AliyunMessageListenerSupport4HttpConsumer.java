package com.github.bluecatlee.gs4d.message.consumer.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Message;
import com.github.bluecatlee.gs4d.common.exception.BusinessException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ValidateBusinessException;
import com.github.bluecatlee.gs4d.common.exception.ValidateClientException;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.message.api.model.MsgConsumeModel;
import com.github.bluecatlee.gs4d.message.consumer.dao.SysRocketMqConsumeFailedlogDao;
import com.github.bluecatlee.gs4d.message.consumer.dao.SysRocketMqConsumeLogDao;
import com.github.bluecatlee.gs4d.message.consumer.dao.SysRocketMqSendLogDao;
import com.github.bluecatlee.gs4d.message.consumer.dao.SysRocketMqSendLogHistoryDao;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqHttpConsumer;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqTopic;
import com.github.bluecatlee.gs4d.message.consumer.model.SysRocketMqSendLog;
import com.github.bluecatlee.gs4d.message.consumer.utils.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class AliyunMessageListenerSupport4HttpConsumer {
    
    private static Logger logger = LoggerFactory.getLogger(MessageListenerConcurrentlyImpl4HttpConsumer.class);
    
    protected PlatformMqTopic platformMqTopic;
    protected PlatformMqHttpConsumer platformMqHttpConsumer;
    protected SysRocketMqConsumeLogDao sysRocketMqConsumeLogDao;
    protected Integer dataSign;
    protected Integer retries;
    protected SysRocketMqConsumeFailedlogDao sysRocketMqConsumeFailedlogDao;
    public static ExecutorService executorService;
    protected PlatformTransactionManager platformTransactionManager;
    protected SysRocketMqSendLogHistoryDao sysRocketMqSendLogHistoryDao;
    protected SysRocketMqSendLogDao sysRocketMqSendLogDao;
    protected String consumerFiledToopic;
    protected String consumerFiledTag;
    protected Integer zkDataSign;

    public Integer doConsume(Message message) {
        MessagePack messagePack = new MessagePack();
        SysRocketMqSendLog sysRocketMqSendLog = null;
        Object correctCodesList = new ArrayList();
        Integer result = 0;
        Long startTime = 0L;
        Long duration = 0L;

        String body;
        try {
            body = new String(message.getBody());
            if (StringUtil.isNullOrBlankTrim(body)) {
                throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.BE40101, "消息消费者接收到的信息为空,消息TOPIC:" + this.platformMqTopic.getTOPIC() + "TAG:" + this.platformMqTopic.getTAG());
            }

            sysRocketMqSendLog = (SysRocketMqSendLog)JSON.parseObject(body, SysRocketMqSendLog.class);
            correctCodesList = (List) Constants.topicTagToCorrectCodesList.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG());
            sysRocketMqSendLog.setRETRY_TIMES(message.getReconsumeTimes());
            String messageBody = sysRocketMqSendLog.getMESSAGE_BODY();
            JSONObject jsonObject = JSON.parseObject(messageBody);
            jsonObject.put("retryTimes", message.getReconsumeTimes() + "");
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

            ArrayList parameters = new ArrayList();
            parameters.add(new BasicNameValuePair(this.platformMqHttpConsumer.getPARAM_NAME(), messageBody));
            httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
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

            if (code != MessagePack.OK && correctCodesList != null && !((List)correctCodesList).contains(String.valueOf(messagePack.getCode()))) {
                throw new BusinessException(Constants.SUB_SYSTEM, ExceptionType.BE40101, "http消费者行号" + this.platformMqHttpConsumer.getSERIES() + "配置的http方法的执行失败");
            }

            messagePack.setConsumerRes(responseEntity.toString());
        } catch (Exception e) {
            duration = System.currentTimeMillis() - startTime;
            logger.error(e.getMessage(), e);
            ExceptionUtil.processException(e, messagePack);
        }

        if (sysRocketMqSendLog.getHAS_LOG().equals("N")) {
            return messagePack.getCode() != 0L && (correctCodesList == null || !((List)correctCodesList).contains(String.valueOf(messagePack.getCode()))) ? -1 : 0;
        } else {
            try {
                if (messagePack.getCode() != MessagePack.OK && (correctCodesList == null || !((List)correctCodesList).contains(String.valueOf(messagePack.getCode())))) {
                    messagePack.setMessage("业务异常");
                    body = (String) Constants.fw.get(sysRocketMqSendLog.getRETRY_TIMES());
                    MsgConsumeModel msgConsumeModel = new MsgConsumeModel();
                    msgConsumeModel.setDataSign(sysRocketMqSendLog.getDATA_SIGN());
                    msgConsumeModel.setTenantNumId(sysRocketMqSendLog.getTENANT_NUM_ID());
                    msgConsumeModel.setMsgSeries(sysRocketMqSendLog.getSERIES());
                    msgConsumeModel.setNextretryTime(body);
                    msgConsumeModel.setTaskTarget(this.platformMqTopic.getTASK_TARGET());
                    msgConsumeModel.setRetryTimes(sysRocketMqSendLog.getRETRY_TIMES());
                    msgConsumeModel.setRetryMax(this.retries);
                    msgConsumeModel.setConsumerSuccessDetail(messagePack.toJson());
                    msgConsumeModel.setConsumeTime(duration);
                    msgConsumeModel.setStepId(sysRocketMqSendLog.getSTEP_ID());
                    AbstractRocketMqUtil.a(Constants.MESS_SQL, Constants.MESS_UPDATE_FAIL_SQL, sysRocketMqSendLog.getSERIES().toString(), JSONObject.toJSONString(msgConsumeModel));
                    result = -1;
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
                    result = 0;
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            return result;
        }
    }
}
