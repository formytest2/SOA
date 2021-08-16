package com.github.bluecatlee.gs4d.message.producer.service.impl;

import com.github.bluecatlee.gs4d.common.utils.lock.RedisLock;
import com.github.bluecatlee.gs4d.message.producer.dao.SysRocketMqSendLogDao;
import com.github.bluecatlee.gs4d.message.producer.dao.SysTransationFailedLogDao;
import com.github.bluecatlee.gs4d.message.producer.utils.AbstractRocketMqUtil;
import com.github.bluecatlee.gs4d.common.utils.BatchUtil;
import com.github.bluecatlee.gs4d.message.producer.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.TimerTask;

/**
 * TransationFailedLog处理任务
 *      定时从sys_transation_failed_log事务失败日志表中取数据批量新增到sys_rocket_mq_send_log消息发送日志表(后续会发送消息)，并删除事务失败日志
 */
public class TransationFailedLogReHandleTask extends TimerTask {

    private static Logger logger = LoggerFactory.getLogger(TransationFailedLogReHandleTask.class);

    private SysTransationFailedLogDao sysTransationFailedLogDao;
    private MessageSendListServiceImpl messageSendListService;
    private String zookeeperAddress;
    private SysRocketMqSendLogDao sysRocketMqSendLogDao;
    private StringRedisTemplate stringRedisTemplate;
    private static Long batchSize = 300L;

    public TransationFailedLogReHandleTask(SysTransationFailedLogDao sysTransationFailedLogDao, MessageSendListServiceImpl messageSendListService, String zookeeperAddress, SysRocketMqSendLogDao sysRocketMqSendLogDao, StringRedisTemplate stringRedisTemplate) {
        this.sysTransationFailedLogDao = sysTransationFailedLogDao;
        this.messageSendListService = messageSendListService;
        this.zookeeperAddress = zookeeperAddress;
        this.sysRocketMqSendLogDao = sysRocketMqSendLogDao;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void run() {
        RedisLock redisLock = null;
        Boolean locked = false;

        try {
            redisLock = new RedisLock(this.stringRedisTemplate, Constants.TransationFailedLogReHandle_LockKey, 500);
            if (redisLock.lock()) {
                locked = true;
                logger.debug("消息中心开始检索定时消息");
                Long count = this.sysTransationFailedLogDao.getAllJobMessage();
                Long batchCount = BatchUtil.batch(count, batchSize);  // 分批

                for(Long i = 0L; i < batchCount; i = i + 1L) {
                    List list = this.sysTransationFailedLogDao.queryByPage(i * batchSize, batchSize);
                    this.sysRocketMqSendLogDao.batchInsert(list);
                    AbstractRocketMqUtil.send(list, this.stringRedisTemplate);
                    this.sysTransationFailedLogDao.batchDelete(list);
                }

                return;
            }
        } catch (Exception e) {
            logger.error("定时消息执行时出现错误:" + e.getMessage(), e);
            return;
        } finally {
            if (locked) {
                redisLock.unlock();
            }

        }

    }
}

