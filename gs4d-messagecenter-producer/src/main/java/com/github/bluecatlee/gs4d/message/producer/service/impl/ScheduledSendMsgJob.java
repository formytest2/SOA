package com.github.bluecatlee.gs4d.message.producer.service.impl;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseStrategy;
import com.github.bluecatlee.gs4d.common.utils.lock.CuratorFrameworkLock;
import com.github.bluecatlee.gs4d.common.utils.MyJsonMapper;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.message.api.model.SimpleObjectMessage;
import com.github.bluecatlee.gs4d.message.api.request.JobMessageByBodySendRequest;
import com.github.bluecatlee.gs4d.message.api.response.JobMessageByBodySendResponse;
import com.github.bluecatlee.gs4d.message.producer.model.SYS_ROCKET_MQ_JOB_LOG;
import com.github.bluecatlee.gs4d.message.producer.utils.AbstractRocketMqUtil;
import com.github.bluecatlee.gs4d.message.producer.utils.Constants;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 定时发送消息任务
 */
public class ScheduledSendMsgJob implements Job {

    private static Logger logger = LoggerFactory.getLogger(ScheduledSendMsgJob.class);
    static MyJsonMapper mapper = new MyJsonMapper();

    static {
        mapper = MyJsonMapper.nonEmptyMapper();
        mapper.getMapper().setPropertyNamingStrategy(LowerCaseStrategy.SNAKE_CASE);
    }

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        Long series = jobDetail.getJobDataMap().getLong("series");
        logger.info("序列" + series + "开始定时消费");
        String uuid = (String) Constants.jobLogSeriesToUuid.get(series);

        try {
            if (StringUtil.isAllNullOrBlank(new String[]{uuid})) {
                return;
            }

            CuratorFrameworkLock curatorFrameworkLock = new CuratorFrameworkLock(AbstractRocketMqUtil.zkAddress, Constants.getJobMessageLockName(series));
            if (!curatorFrameworkLock.tryCuratorLock() && !uuid.equals(curatorFrameworkLock.CuratorFrameworkGetUUidByLock())) {
                return;
            }

            curatorFrameworkLock.CuratorFrameworkSetUUidByLock(uuid);
        } catch (Exception e) {
            logger.error("定时cron消息分布式锁加锁失败！原因:" + e.getMessage(), e);
            return;
        }

        List sysRocketMqJobLogs = AbstractRocketMqUtil.sysRocketMqJobLogDaoE.queryBySeries(series);
        if (sysRocketMqJobLogs != null && !sysRocketMqJobLogs.isEmpty()) {
            String cancelsign = ((SYS_ROCKET_MQ_JOB_LOG)sysRocketMqJobLogs.get(0)).getCANCELSIGN();
//            String messageTopic = ((SYS_ROCKET_MQ_JOB_LOG)sysRocketMqJobLogs.get(0)).getMESSAGE_TOPIC();
//            String messageTag = ((SYS_ROCKET_MQ_JOB_LOG)sysRocketMqJobLogs.get(0)).getMESSAGE_TAG();
            if ("Y".equals(cancelsign)) {
                SchedulerUtil.cancelJob(series.toString(), series.toString(), series.toString(), series.toString());    // 取消定时任务
            } else {
                String messageDetail = ((SYS_ROCKET_MQ_JOB_LOG)sysRocketMqJobLogs.get(0)).getMESSAGE_DETAIL();
                SimpleObjectMessage simpleObjectMessage = mapper.fromJson(messageDetail, SimpleObjectMessage.class);
                JobMessageByBodySendRequest jobMessageByBodySendRequest = new JobMessageByBodySendRequest();
                jobMessageByBodySendRequest.setDataSign(simpleObjectMessage.getDataSign());
                jobMessageByBodySendRequest.setTenantNumId(simpleObjectMessage.getTenantNumId());
                jobMessageByBodySendRequest.setMessage(simpleObjectMessage);
                JobMessageByBodySendResponse jobMessageByBodySendResponse = AbstractRocketMqUtil.jobmessageServiceE.sendJobMessageByBody(jobMessageByBodySendRequest);
                Long sendLogSeries = jobMessageByBodySendResponse.getSeries();
                String message = jobMessageByBodySendResponse.getMessage();
                AbstractRocketMqUtil.sysRocketMqJobLogDaoE.updateSendLog(series, sendLogSeries, message);
            }
        }
    }
}

