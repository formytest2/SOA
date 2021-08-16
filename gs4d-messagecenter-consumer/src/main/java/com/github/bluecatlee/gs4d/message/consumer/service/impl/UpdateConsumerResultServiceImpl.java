package com.github.bluecatlee.gs4d.message.consumer.service.impl;

import com.github.bluecatlee.gs4d.common.exception.DatabaseOperateException;
import com.github.bluecatlee.gs4d.message.consumer.dao.SysRocketMqConsumeFailedlogDao;
import com.github.bluecatlee.gs4d.message.consumer.dao.SysRocketMqSendLogDao;
import com.github.bluecatlee.gs4d.message.consumer.dao.SysRocketMqSendLogHistoryDao;
import com.github.bluecatlee.gs4d.message.consumer.model.SysRocketMqSendLog;
import com.github.bluecatlee.gs4d.message.consumer.service.UpdateConsumerResultService;
import com.github.bluecatlee.gs4d.message.consumer.utils.SeqUtil;
import com.github.bluecatlee.gs4d.message.consumer.utils.TransactionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

/**
 * 更新消费结果service
 */
@Service
public class UpdateConsumerResultServiceImpl implements UpdateConsumerResultService {

    private static Logger logger = LoggerFactory.getLogger(UpdateConsumerResultServiceImpl.class);
    
    @Autowired
    private SysRocketMqSendLogDao sysRocketMqSendLogDao;

    @Autowired
    private SysRocketMqConsumeFailedlogDao sysRocketMqConsumeFailedlogDao;
    
    @Value("#{settings['consumerFiledTag']}")
    private String consumerFiledTag;
    
    @Autowired
    private SysRocketMqSendLogHistoryDao sysRocketMqSendLogHistoryDao;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    /**
     * 更新SysRocketMqSendLog消息发送日志为消费成功
     * @param series
     * @param taskTarget
     * @param messagePack
     * @param consumeTime
     * @param stepId
     * @return
     */
    public Integer updateConsumerSuccessResult(Long series, String taskTarget, String messagePack, Long consumeTime, Integer stepId) {
        Integer result = 0;
        SysRocketMqSendLog sysRocketMqSendLog = null;

        try {
            sysRocketMqSendLog = this.sysRocketMqSendLogDao.queryBySeriesStrict(series);
        } catch (DatabaseOperateException e) {
            logger.error(e.getMessage(), e);
            result = -1;
            return result;
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            result = -2;
            return result;
        }

        TransactionStatus transactionStatus = this.platformTransactionManager.getTransaction(TransactionUtil.newTransactionDefinition(-1));

        try {
            sysRocketMqSendLog.setMSG_STATUS(1);
            sysRocketMqSendLog.setCONSUMER_SUCCESS("Y");
            sysRocketMqSendLog.setTASK_TARGET(taskTarget);
            sysRocketMqSendLog.setRESPONSE_DETAIL(messagePack);
            if (consumeTime == null) {
                consumeTime = 0L;
            }

            sysRocketMqSendLog.setINSTANCE_ID(consumeTime);
            sysRocketMqSendLog.setNEXT_RETRY_INTERVAL("0");
            if (stepId == null) {
                sysRocketMqSendLog.setSTEP_ID(0);
            } else {
                sysRocketMqSendLog.setSTEP_ID(stepId);
            }

            if (sysRocketMqSendLog.getRESPONSE_DETAIL().length() > 500) {
                sysRocketMqSendLog.setRESPONSE_DETAIL(sysRocketMqSendLog.getRESPONSE_DETAIL().substring(0, 500));
            }

            this.sysRocketMqSendLogHistoryDao.insert(sysRocketMqSendLog);
            this.sysRocketMqSendLogDao.delete(series);                      // 【核心】 消费成功后则删除消息发送日志 而对应的数据会轮滚到消息发送日志历史表
            this.platformTransactionManager.commit(transactionStatus);
        } catch (DuplicateKeyException e) {
            logger.info("发现消息重复" + series);
            this.platformTransactionManager.commit(transactionStatus);
        } catch (Throwable e) {
            this.platformTransactionManager.rollback(transactionStatus);
            logger.error("消息中心消费判断效率是否合格时更新数据库时失败", e);
            result = -3;
        }

        return result;
    }

    public Integer updateConsumerFailedResult(Long series, Integer retries, String taskTarget, String messagePack, String retryInterval, Integer retryTimes, String tag, Long consumeTime, Long tenantNumId, Long dataSign, Integer stepId) {
        Integer result = 0;

        try {
            if (retryTimes.equals(retries)) {
                retryInterval = "0";
            }

            if (consumeTime == null) {
                consumeTime = 0L;
            }

            this.sysRocketMqSendLogDao.update(taskTarget, messagePack, series, retryInterval, consumeTime, stepId); // 更新消息发送日志表
            if (retryTimes.equals(retries) && !tag.equals(this.consumerFiledTag) && dataSign == (long) SeqUtil.prod) {
                try {
                    this.sysRocketMqConsumeFailedlogDao.insert(series, 1L, tenantNumId, dataSign);  // 超过最大消费次数依旧失败后则存表sys_rocket_mq_consumer_failedlog
                } catch (Exception e) {
                    logger.error("消息中心消费失败更新数据库时失败,序列号" + series);
                }
            }
        } catch (Exception e) {
            logger.error("消息中心消费失败更新数据库时失败", e);
        }

        return result;
    }
}

