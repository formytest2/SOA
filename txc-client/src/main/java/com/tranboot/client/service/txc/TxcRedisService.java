package com.tranboot.client.service.txc;

import com.codahale.metrics.Timer.Context;
import com.github.bluecatlee.gs4d.transaction.api.model.RedisTransactionModel;
import com.github.bluecatlee.gs4d.transaction.api.model.SqlParamModel;
import com.github.bluecatlee.gs4d.transaction.api.utils.TransactionApiUtil;
import com.tranboot.client.exception.TxcRedisException;
import com.tranboot.client.model.txc.BranchRollbackInfo;
import com.tranboot.client.model.txc.BranchRollbackMessage;
import com.tranboot.client.utils.MetricsReporter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

public class TxcRedisService {

    protected Logger logger = LoggerFactory.getLogger(TxcRedisService.class);
    public static final int INIT_STATUS = 0;
    public static final int COMMITTABLE_STATUS = 1;
    public static final int ROLLBACKABLE_STATUS = 2;
    private RedisTemplate<String, RedisTransactionModel> txcRedisTemplate;
    private StringRedisTemplate stringRedisTemplate;

    public TxcRedisService(RedisTemplate<String, RedisTransactionModel> txcRedisTemplate, StringRedisTemplate stringRedisTemplate) {
        this.txcRedisTemplate = txcRedisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 通过redis获取系统时间
     * @return
     * @throws TxcRedisException
     */
    public long getCurrentTimeMillisFromRedis() throws TxcRedisException {
        Context context = MetricsReporter.time.time();

        long currentTimeMillis;
        try {
            currentTimeMillis = (Long)this.stringRedisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    return redisConnection.time();
                }
            });
        } catch (Exception e) {
            throw new TxcRedisException(e, "获取redis 系统时间失败");
        } finally {
            context.stop();
        }

        return currentTimeMillis;
    }

    public void hput(RedisTransactionModel model) throws TxcRedisException {
        Context context = MetricsReporter.hput.time();

        try {
            this.logger.debug("redis hput:{}", model.toString());
            String redisKey = TransactionApiUtil.redisKeyStart + model.getTransactionId();
            this.txcRedisTemplate.opsForHash().put(redisKey, String.valueOf(model.getTransactionSqlId()), model);
            this.txcRedisTemplate.expire(redisKey, 24L, TimeUnit.HOURS);
            this.logger.debug("redis hput finished", model.toString());
        } catch (Exception e) {
            throw new TxcRedisException(e, "txc reids存入失败");
        } finally {
            context.stop();
        }

    }

    public void hput(List<RedisTransactionModel> models) throws TxcRedisException {
        Iterator iterator = models.iterator();

        while(iterator.hasNext()) {
            RedisTransactionModel model = (RedisTransactionModel)iterator.next();
            this.hput(model);
        }

    }

    public static RedisTransactionModel buildTransactionModel(BranchRollbackMessage message) {
        RedisTransactionModel model = new RedisTransactionModel();
        model.setSourceDb(message.getDataSource());
        model.setTransactionId(message.getXid());
        model.setTransactionSqlId(message.getBranchId());
        model.setTransactionStartDtme(message.getTransactionStartDate());
        model.setStatus(message.getStatus());
        List<SqlParamModel> sqls = new ArrayList();
        if (message.getInfo() != null) {
            Iterator iterator = message.getInfo().iterator();

            while(iterator.hasNext()) {
                BranchRollbackInfo info = (BranchRollbackInfo)iterator.next();
                sqls.addAll(info.getRollbackSql());
            }
        }

        model.setSqlParamModel(sqls);
        model.setTransactionOutTimeSecond(message.getTransactionOutTimeSecond());
        return model;
    }
}

