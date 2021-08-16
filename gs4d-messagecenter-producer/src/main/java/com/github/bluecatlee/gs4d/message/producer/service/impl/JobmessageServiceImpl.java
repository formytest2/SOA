package com.github.bluecatlee.gs4d.message.producer.service.impl;

import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.utils.ExceptionUtil;
import com.github.bluecatlee.gs4d.message.api.model.SimpleMessage;
import com.github.bluecatlee.gs4d.message.api.request.JobMessageByBodySendRequest;
import com.github.bluecatlee.gs4d.message.api.request.SimpleMessageRightNowSendRequest;
import com.github.bluecatlee.gs4d.message.api.response.JobMessageByBodySendResponse;
import com.github.bluecatlee.gs4d.message.api.response.SimpleMessageRightNowSendResponse;
import com.github.bluecatlee.gs4d.message.api.service.MessageCenterSendService;
import com.github.bluecatlee.gs4d.message.producer.dao.SysRocketMqJobLogDao;
import com.github.bluecatlee.gs4d.message.producer.model.SYS_ROCKET_MQ_JOB_LOG;
import com.github.bluecatlee.gs4d.message.producer.service.JobmessageService;
import com.github.bluecatlee.gs4d.message.producer.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * 定时消息实现
 */
@Service
public class JobmessageServiceImpl implements JobmessageService {

    private static Logger logger = LoggerFactory.getLogger(JobmessageServiceImpl.class);

    @Autowired
    private MessageCenterSendService messageCenterSendService;

    @Autowired
    private SysRocketMqJobLogDao sysRocketMqJobLogDao;

    // 定时任务触发时执行对应的任务，即发送消息
    public JobMessageByBodySendResponse sendJobMessageByBody(JobMessageByBodySendRequest jobMessageByBodySendRequest) {
        JobMessageByBodySendResponse jobMessageByBodySendResponse = new JobMessageByBodySendResponse();

        try {
            jobMessageByBodySendRequest.validate(Constants.SUB_SYSTEM, ExceptionType.VCE10031);
            SimpleMessageRightNowSendRequest simpleMessageRightNowSendRequest = new SimpleMessageRightNowSendRequest();
            simpleMessageRightNowSendRequest.setSimpleMessage(new SimpleMessage(jobMessageByBodySendRequest.getMessage().getTopic(), jobMessageByBodySendRequest.getMessage().getTag(), jobMessageByBodySendRequest.getMessage().getMsgKey(), (String)jobMessageByBodySendRequest.getMessage().getBody(), Long.valueOf(jobMessageByBodySendRequest.getMessage().getFromSystem()), jobMessageByBodySendRequest.getDataSign(), jobMessageByBodySendRequest.getTenantNumId()));
            SimpleMessageRightNowSendResponse simpleMessageRightNowSendResponse = this.messageCenterSendService.sendSimpleMessageRightNow(simpleMessageRightNowSendRequest);
            jobMessageByBodySendResponse.setMessage(simpleMessageRightNowSendResponse.getMessage());
            jobMessageByBodySendResponse.setCode(simpleMessageRightNowSendResponse.getCode());
            jobMessageByBodySendResponse.setSeries(simpleMessageRightNowSendResponse.getSeries());
        } catch (Exception e) {
            ExceptionUtil.processException(e, jobMessageByBodySendResponse);
        }

        return jobMessageByBodySendResponse;
    }

    public void initJobMessageToSpringJob() {
        List sysRocketMqJobLogs = this.sysRocketMqJobLogDao.getAllJob();
        Iterator iterator = sysRocketMqJobLogs.iterator();

        while(iterator.hasNext()) {
            SYS_ROCKET_MQ_JOB_LOG sysRocketMqJobLog = (SYS_ROCKET_MQ_JOB_LOG)iterator.next();
            Boolean flag = SchedulerUtil.canTrigger(sysRocketMqJobLog.getCRON());
            if (flag) { // 能触发
                Constants.jobLogSeriesToUuid.put(sysRocketMqJobLog.getSERIES(), UUID.randomUUID().toString());
                ScheduledSendMsgJob job = new ScheduledSendMsgJob();
                // 创建触发器、任务、调度器
                SchedulerUtil.scheduleJob(sysRocketMqJobLog.getSERIES().toString(), sysRocketMqJobLog.getSERIES().toString(), sysRocketMqJobLog.getSERIES().toString(), sysRocketMqJobLog.getSERIES().toString(), job.getClass(), sysRocketMqJobLog.getCRON());
            } else {    // 不能触发
                this.sysRocketMqJobLogDao.cancelJobLog(sysRocketMqJobLog.getSERIES()); // 更新cancelsign=Y 任务执行时如果发现cancelsign=Y时会取消触发器并删除任务
            }
        }

    }
}

