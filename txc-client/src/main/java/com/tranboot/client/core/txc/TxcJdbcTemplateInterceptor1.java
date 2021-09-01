package com.tranboot.client.core.txc;

import com.codahale.metrics.Timer.Context;
import com.github.bluecatlee.gs4d.transaction.api.model.SqlParamModel;
import com.tranboot.client.exception.TxcDBException;
import com.tranboot.client.exception.TxcTransactionException;
import com.tranboot.client.model.DBType;
import com.tranboot.client.model.SQLType;
import com.tranboot.client.model.txc.BranchRollbackInfo;
import com.tranboot.client.model.txc.BranchRollbackMessage;
import com.tranboot.client.model.txc.TxcSQL;
import com.tranboot.client.model.txc.TxcSqlProcessorWraper;
import com.tranboot.client.model.txc.TxcSQL.RollbackSqlInfo;
import com.tranboot.client.model.txc.TxcSQLTransformer.TxcSQLTransform;
import com.tranboot.client.service.txc.TxcRedisService;
import com.tranboot.client.utils.LRUCache;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import javax.sql.DataSource;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.jdbc.core.JdbcTemplate;

public class TxcJdbcTemplateInterceptor1 extends AbstractJdbcTemplateInterceptor {
    public TxcJdbcTemplateInterceptor1(String datasource, DataSource ds, TxcRedisService txcRedisService, DBType dbType) {
        super(datasource, ds, txcRedisService, dbType);
    }

    public Object txc(MethodInvocation invocation, BranchRollbackMessage message, int transactionType, List<ReentrantRedLock> locks) throws TxcDBException {
        final JdbcTemplate proxyedObject = (JdbcTemplate)invocation.getThis();

        try {
            BranchRollbackInfo rollbackInfo = new BranchRollbackInfo();
            List<SqlParamModel> redisModel = new ArrayList();
            String txc = TxcRollbackSqlManagerV2.txc(message);
            Object[] arguments = invocation.getArguments();
            String sql = (String)arguments[0];
            final TxcSQLTransform transformed = LRUCache.getTxcTransformedSql(sql);
            TxcSQL txcSql = LRUCache.getTxcSql(transformed.sql, new Callable<TxcSQL>() {
                public TxcSQL call() throws Exception {
                    return (new TxcSqlProcessorWraper(transformed.sql, proxyedObject, TxcJdbcTemplateInterceptor1.this.dbType, TxcJdbcTemplateInterceptor1.this.datasource)).parse();
                }
            });
            new ArrayList();
            List rollbackSqls;
            if (transformed.sqlType == SQLType.DELETE) {
                rollbackSqls = txcSql.rollbackSql((Object[])null, proxyedObject);
            } else {
                rollbackSqls = txcSql.rollbackSql(new Object[]{txc}, proxyedObject);
            }

            Iterator var14 = rollbackSqls.iterator();

            while(var14.hasNext()) {
                RollbackSqlInfo rollbackSql = (RollbackSqlInfo)var14.next();
                String lockKey = this.datasource + "-" + txcSql.getTableName() + "-" + rollbackSql.pkv();
                ReentrantRedLock lock = this.lock(lockKey, TxcContext.getTxcTimeout(), locks);
                SqlParamModel model = this.redisSqlModel(rollbackSql, txcSql, transactionType, txc, lockKey, lock.getRedLockValue());
                redisModel.add(model);
            }

            rollbackInfo.setRollbackSql(redisModel);
            message.addBranchRollbackInfo(this.datasource, rollbackInfo);
            this.txcRedisService.hput(TxcRedisService.buildTransactionModel(message));
            Context context = this.processTimer.time();

            Object var29;
            try {
                if (transformed.sqlType != SQLType.DELETE) {
                    Integer result = proxyedObject.update(transformed.sql, new Object[]{txc});
                    Integer var30 = result;
                    return var30;
                }

                Object result = invocation.proceed();
                var29 = result;
            } catch (Throwable var23) {
                throw new TxcDBException(var23, var23.getMessage());
            } finally {
                context.stop();
            }

            return var29;
        } catch (Exception var25) {
            throw new TxcTransactionException(var25, var25.getMessage());
        }
    }
}

