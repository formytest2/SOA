package com.github.bluecatlee.gs4d.message.producer.service.impl;

import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ValidateBusinessException;
import com.github.bluecatlee.gs4d.common.utils.ExceptionUtil;
import com.github.bluecatlee.gs4d.common.utils.Md5Util;
import com.github.bluecatlee.gs4d.message.api.model.JobMessageModel;
import com.github.bluecatlee.gs4d.message.api.model.SimpleMessage;
import com.github.bluecatlee.gs4d.message.api.request.*;
import com.github.bluecatlee.gs4d.message.api.response.*;
import com.github.bluecatlee.gs4d.message.api.service.MessageCenterSendListService;
import com.github.bluecatlee.gs4d.message.api.service.MessageCenterSendService;
import com.github.bluecatlee.gs4d.message.producer.model.SYS_ROCKET_MQ_MESSAGE_ENCRYLOG;
import com.github.bluecatlee.gs4d.message.producer.model.SYS_ROCKET_MQ_SEND_LOG;
import com.github.bluecatlee.gs4d.message.producer.utils.AbstractRocketMqUtil;
import com.github.bluecatlee.gs4d.message.producer.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 批量消息发送实现
 */
@Service("messageListService")
public class MessageSendListServiceImpl extends AbstractMessageSendService implements MessageCenterSendListService {

    private static Logger logger = LoggerFactory.getLogger(MessageSendListServiceImpl.class);

    @Autowired
    private MessageCenterSendService messageCenterSendService;

    public PrepSimpleMessageListSendResponse sendPrepSimpleMessageList(PrepSimpleMessageListSendRequest prepSimpleMessageListSendRequest) {
        if (prepSimpleMessageListSendRequest.getSimpleMessageList().size() > 200) {
            throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "批量欲发送消息数量不能超过200");
        } else {
            Iterator iterator = prepSimpleMessageListSendRequest.getSimpleMessageList().iterator();

            while(iterator.hasNext()) {
                SimpleMessage simpleMessage = (SimpleMessage)iterator.next();
                if (Constants.notInsertDbTopicTagList.contains(simpleMessage.getTopic() + "#" + simpleMessage.getTag())) {
                    throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "当前topic,tag数据库配置为不插入数据库,请调用立即发送消息接口" + simpleMessage.getTopic());
                }
            }

            PrepSimpleMessageListSendResponse prepSimpleMessageListSendResponse = new PrepSimpleMessageListSendResponse();
            ArrayList seriesList = new ArrayList();

            try {
                ArrayList sysRocketMqSendLogList = new ArrayList();
                StringBuffer sb = new StringBuffer();
                Iterator simpleMessageIterator = prepSimpleMessageListSendRequest.getSimpleMessageList().iterator();

                while(simpleMessageIterator.hasNext()) {
                    SimpleMessage simpleMessage = (SimpleMessage)simpleMessageIterator.next();
                    SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog = new SYS_ROCKET_MQ_SEND_LOG();
                    this.assemble(sysRocketMqSendLog, simpleMessage);
                    if (sysRocketMqSendLog != null && Constants.distinctTopicTagList.contains(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG())) {
                        String msgmd = Md5Util.md5Signature(sysRocketMqSendLog.getMESSAGE_BODY());
                        List encrylogs = this.sysRocketMqMessageEncrylogDao.query(msgmd, simpleMessage.getTopic(), simpleMessage.getTag(), simpleMessage.getDataSign(), simpleMessage.getTenantNumId());
                        if (encrylogs != null && !encrylogs.isEmpty()) {
                            sysRocketMqSendLog.setSERIES(((SYS_ROCKET_MQ_MESSAGE_ENCRYLOG)encrylogs.get(0)).getMsgSeries());
                        } else {
                            this.sysRocketMqMessageEncrylogDao.insert(sysRocketMqSendLog.getSERIES(), msgmd, simpleMessage.getTopic(), simpleMessage.getTag(), simpleMessage.getDataSign(), simpleMessage.getTenantNumId());
                        }
                    } else if (sysRocketMqSendLog != null && Constants.notDistinctTopicTagList.contains(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG())) {
                        List encrylogs = this.sysRocketMqMessageEncrylogDao.query(sysRocketMqSendLog.getMESSAGE_KEY(), simpleMessage.getTopic(), simpleMessage.getTag(), simpleMessage.getDataSign(), simpleMessage.getTenantNumId());
                        if (encrylogs != null && !encrylogs.isEmpty()) {
                            sysRocketMqSendLog.setSERIES(((SYS_ROCKET_MQ_MESSAGE_ENCRYLOG)encrylogs.get(0)).getMsgSeries());
                        } else {
                            this.sysRocketMqMessageEncrylogDao.insert(sysRocketMqSendLog.getSERIES(), sysRocketMqSendLog.getMESSAGE_KEY(), simpleMessage.getTopic(), simpleMessage.getTag(), simpleMessage.getDataSign(), simpleMessage.getTenantNumId());
                        }
                    }

                    sysRocketMqSendLogList.add(sysRocketMqSendLog);
                    seriesList.add(sysRocketMqSendLog.getSERIES());
                    this.addSysRocketMqSendLogToRedis(sysRocketMqSendLog);
                }

                if (sb.length() > 0) {
                    throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, sb.toString());
                }

                if (sysRocketMqSendLogList.size() < 0) {
                    throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, sb.append("没有需要发送的消息").toString());
                }

                try {
                    this.sysRocketMqSendLogDao.batchInsert(sysRocketMqSendLogList);
                } catch (Exception e) {
                    throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, sb.append("插入消息失败").toString());
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                ExceptionUtil.processException(e, prepSimpleMessageListSendResponse);
            }

            prepSimpleMessageListSendResponse.setSeries(seriesList);
            return prepSimpleMessageListSendResponse;
        }
    }

    public SimpleMessageListRightNowSendResponse sendSimpleMessageListRightNow(SimpleMessageListRightNowSendRequest simpleMessageListRightNowSendRequest) {
        if (simpleMessageListRightNowSendRequest.getSimpleMessageList().size() > 200) {
            throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "批量立即发送消息数量不能超过200");
        } else {
            SimpleMessageListRightNowSendResponse simpleMessageListRightNowSendResponse = new SimpleMessageListRightNowSendResponse();
            ArrayList seriesList = new ArrayList();

            try {
                ArrayList sysRocketMqSendLogList = new ArrayList();
                StringBuffer sb = new StringBuffer();
                Iterator iterator = simpleMessageListRightNowSendRequest.getSimpleMessageList().iterator();

                while(true) {
                    while(iterator.hasNext()) {
                        SimpleMessage simpleMessage = (SimpleMessage)iterator.next();
                        SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog;
                        if (Constants.notInsertDbTopicTagList.contains(simpleMessage.getTopic() + "#" + simpleMessage.getTag())) {
                            sysRocketMqSendLog = new SYS_ROCKET_MQ_SEND_LOG();
                            sysRocketMqSendLog.setMESSAGE_BODY(simpleMessage.getBody());
                            sysRocketMqSendLog.setINSTANCE_ID(0L);
                            sysRocketMqSendLog.setHAS_LOG("N");
                            sysRocketMqSendLog.setSERIES(1L);
                            sysRocketMqSendLog.setMESSAGE_TOPIC(simpleMessage.getTopic());
                            sysRocketMqSendLog.setMESSAGE_TAG(simpleMessage.getTag());
                            sysRocketMqSendLog.setMESSAGE_KEY(simpleMessage.getMsgKey());
                            sysRocketMqSendLog.setTENANT_NUM_ID(simpleMessage.getTenantNumId());
                            sysRocketMqSendLog.setDATA_SIGN(simpleMessage.getDataSign());
                            AbstractRocketMqUtil.send(simpleMessage.getTopic(), simpleMessage.getTag(), simpleMessage.getMsgKey(), sysRocketMqSendLog, simpleMessage.getTenantNumId());
                        } else {
                            sysRocketMqSendLog = this.assemble(new SYS_ROCKET_MQ_SEND_LOG());
                            sysRocketMqSendLog = this.assemble(sysRocketMqSendLog, simpleMessage);
                            if (sysRocketMqSendLog != null && Constants.distinctTopicTagList.contains(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG())) {
                                String msgmd = Md5Util.md5Signature(sysRocketMqSendLog.getMESSAGE_BODY());
                                List mqMessageEncrylogs = this.sysRocketMqMessageEncrylogDao.query(msgmd, simpleMessage.getTopic(), simpleMessage.getTag(), simpleMessage.getDataSign(), simpleMessage.getTenantNumId());
                                if (mqMessageEncrylogs != null && !mqMessageEncrylogs.isEmpty()) {
                                    seriesList.add(((SYS_ROCKET_MQ_MESSAGE_ENCRYLOG)mqMessageEncrylogs.get(0)).getMsgSeries());
                                } else {
                                    this.sysRocketMqMessageEncrylogDao.insert(sysRocketMqSendLog.getSERIES(), msgmd, simpleMessage.getTopic(), simpleMessage.getTag(), simpleMessage.getDataSign(), simpleMessage.getTenantNumId());
                                    sysRocketMqSendLogList.add(sysRocketMqSendLog);
                                    seriesList.add(sysRocketMqSendLog.getSERIES());
                                    this.addSysRocketMqSendLogToRedis(sysRocketMqSendLog);
                                }
                            } else if (sysRocketMqSendLog != null && Constants.notDistinctTopicTagList.contains(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG())) {
                                List mqMessageEncrylogs = this.sysRocketMqMessageEncrylogDao.query(sysRocketMqSendLog.getMESSAGE_KEY(), simpleMessage.getTopic(), simpleMessage.getTag(), simpleMessage.getDataSign(), simpleMessage.getTenantNumId());
                                if (mqMessageEncrylogs != null && !mqMessageEncrylogs.isEmpty()) {
                                    seriesList.add(((SYS_ROCKET_MQ_MESSAGE_ENCRYLOG)mqMessageEncrylogs.get(0)).getMsgSeries());
                                } else {
                                    this.sysRocketMqMessageEncrylogDao.insert(sysRocketMqSendLog.getSERIES(), sysRocketMqSendLog.getMESSAGE_KEY(), simpleMessage.getTopic(), simpleMessage.getTag(), simpleMessage.getDataSign(), simpleMessage.getTenantNumId());
                                    sysRocketMqSendLogList.add(sysRocketMqSendLog);
                                    seriesList.add(sysRocketMqSendLog.getSERIES());
                                    this.addSysRocketMqSendLogToRedis(sysRocketMqSendLog);
                                }
                            } else {
                                sysRocketMqSendLogList.add(sysRocketMqSendLog);
                                seriesList.add(sysRocketMqSendLog.getSERIES());
                                this.addSysRocketMqSendLogToRedis(sysRocketMqSendLog);
                            }
                        }
                    }

                    if (sb.length() > 0) {
                        throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, sb.toString());
                    }

                    if (sysRocketMqSendLogList.size() < 0) {
                        throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, sb.append("没有需要发送的消息").toString());
                    }

                    try {
                        this.sysRocketMqSendLogDao.batchInsert(sysRocketMqSendLogList);
                        AbstractRocketMqUtil.send(sysRocketMqSendLogList, this.stringRedisTemplate);
                    } catch (Exception e) {
                        throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, sb.append("发送消息失败失败").toString() + "原因" + e.getMessage());
                    }

                    simpleMessageListRightNowSendResponse.setSeries(seriesList);
                    break;
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                ExceptionUtil.processException(e, simpleMessageListRightNowSendResponse);
            }

            return simpleMessageListRightNowSendResponse;
        }
    }

    public JobMessageListRightNowSendResponse sendJobMessageListRightNow(JobMessageListRightNowSendRequest jobMessageListRightNowSendRequest) {
        JobMessageListRightNowSendResponse jobMessageListRightNowSendResponse = new JobMessageListRightNowSendResponse();

        try {
            ArrayList list = new ArrayList();
            Iterator iterator = jobMessageListRightNowSendRequest.getSimpleMessageList().iterator();

            while(iterator.hasNext()) {
                SimpleMessage simpleMessage = (SimpleMessage)iterator.next();
                if (simpleMessage.getJobTargetTime() == null) {
                    throw new Exception("定时消息必须传入时间！");
                }

                SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog = new SYS_ROCKET_MQ_SEND_LOG();
                this.assemble(sysRocketMqSendLog, simpleMessage);
                sysRocketMqSendLog.setORDER_MESS_FLAG(Constants.mess_flag_job);
                sysRocketMqSendLog.setCONSUMER_SUCCESS_TIME(simpleMessage.getJobTargetTime());
                list.add(sysRocketMqSendLog);
            }

            this.sysTransationFailedLogDao.batchInsert(list);
        } catch (Exception e) {
            ExceptionUtil.processException(e, jobMessageListRightNowSendResponse);
        }

        return jobMessageListRightNowSendResponse;
    }

    public JobListCronMessageRightNowSendResponse sendJobListCronMessageRightNow(JobListCronMessageRightNowSendRequest jobListCronMessageRightNowSendRequest) {
        JobListCronMessageRightNowSendResponse jobListCronMessageRightNowSendResponse = new JobListCronMessageRightNowSendResponse();

        try {
            ArrayList list = new ArrayList();
            List jobMessageModels = jobListCronMessageRightNowSendRequest.getJobMessageModels();
            Iterator iterator = jobMessageModels.iterator();

            while(iterator.hasNext()) {
                JobMessageModel jobMessageModel = (JobMessageModel)iterator.next();
                JobCronMessageRightNowSendRequest jobCronMessageRightNowSendRequest = new JobCronMessageRightNowSendRequest();
                jobCronMessageRightNowSendRequest.setDataSign(jobListCronMessageRightNowSendRequest.getDataSign());
                jobCronMessageRightNowSendRequest.setTenantNumId(jobListCronMessageRightNowSendRequest.getTenantNumId());
                jobCronMessageRightNowSendRequest.setJobMessageModel(jobMessageModel);
                JobCronMessageRightNowSendResponse jobCronMessageRightNowSendResponse = this.messageCenterSendService.sendJobCronMessageRightNow(jobCronMessageRightNowSendRequest);
                if (jobCronMessageRightNowSendResponse.getCode() != 0L) {
                    throw new RuntimeException("本次发送定时消息失败,原因：" + jobCronMessageRightNowSendResponse.getMessage());
                }

                list.add(jobCronMessageRightNowSendResponse.getSeries());
            }

            jobListCronMessageRightNowSendResponse.setSeries(list);
        } catch (Exception e) {
            ExceptionUtil.processException(e, jobListCronMessageRightNowSendResponse);
        }

        return jobListCronMessageRightNowSendResponse;
    }
}

