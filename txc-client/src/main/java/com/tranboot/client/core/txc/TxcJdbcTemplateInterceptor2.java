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

public class TxcJdbcTemplateInterceptor2 extends AbstractJdbcTemplateInterceptor {
    public TxcJdbcTemplateInterceptor2(String datasource, DataSource ds, TxcRedisService txcRedisService, DBType dbType) {
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
                    return (new TxcSqlProcessorWraper(transformed.sql, proxyedObject, TxcJdbcTemplateInterceptor2.this.dbType, TxcJdbcTemplateInterceptor2.this.datasource)).parse();
                }
            });
            if (transformed.sqlType != SQLType.DELETE) {
                arguments[0] = transformed.sql;
                arguments[1] = transformed.addArgs((Object[])((Object[])arguments[1]), txc);
            }

            List<RollbackSqlInfo> rollbackSqls = txcSql.rollbackSql((Object[])((Object[])arguments[1]), proxyedObject);
            Iterator var14 = rollbackSqls.iterator();

            while(true) {
                if (!var14.hasNext()) {
                    rollbackInfo.setRollbackSql(redisModel);
                    message.addBranchRollbackInfo(this.datasource, rollbackInfo);
                    this.txcRedisService.hput(TxcRedisService.buildTransactionModel(message));
                    break;
                }

                RollbackSqlInfo rollbackSql = (RollbackSqlInfo)var14.next();
                String lockKey = this.datasource + "-" + txcSql.getTableName() + "-" + rollbackSql.pkv();
                ReentrantRedLock lock = this.lock(lockKey, TxcContext.getTxcTimeout(), locks);
                SqlParamModel model = this.redisSqlModel(rollbackSql, txcSql, transactionType, txc, lockKey, lock.getRedLockValue());
                redisModel.add(model);
            }
        } catch (Exception var25) {
            throw new TxcTransactionException(var25, var25.getMessage());
        }

        Context context = this.processTimer.time();

        Object var28;
        try {
            Object rt = invocation.proceed();
            var28 = rt;
        } catch (Throwable var23) {
            throw new TxcDBException(var23, var23.getMessage());
        } finally {
            context.stop();
        }

        return var28;
    }
}

