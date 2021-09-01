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
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.jdbc.core.JdbcTemplate;

public class TxcJdbcTemplateInterceptor4 extends AbstractJdbcTemplateInterceptor {
    public TxcJdbcTemplateInterceptor4(String datasource, DataSource ds, TxcRedisService txcRedisService, DBType dbType) {
        super(datasource, ds, txcRedisService, dbType);
    }

    public Object txc(MethodInvocation invocation, BranchRollbackMessage message, int transactionType, List<ReentrantRedLock> locks) throws TxcDBException {
        final JdbcTemplate proxyedObject = (JdbcTemplate)invocation.getThis();

        try {
            Object[] arguments = invocation.getArguments();
            String sql = (String)arguments[0];
            final String txc = TxcRollbackSqlManagerV2.txc(message);
            final TxcSQLTransform transformed = LRUCache.getTxcTransformedSql(sql);
            TxcSQL txcSql = LRUCache.getTxcSql(transformed.sql, new Callable<TxcSQL>() {
                public TxcSQL call() throws Exception {
                    return (new TxcSqlProcessorWraper(transformed.sql, proxyedObject, TxcJdbcTemplateInterceptor4.this.dbType, TxcJdbcTemplateInterceptor4.this.datasource)).parse();
                }
            });
            if (transformed.sqlType != SQLType.DELETE) {
                arguments[0] = transformed.sql;
                List<Object[]> args = (List)arguments[1];
                arguments[1] = args.stream().map(new Function<Object[], Object[]>() {
                    public Object[] apply(Object[] t) {
                        return transformed.addArgs(t, txc);
                    }
                }).collect(Collectors.toList());
            }

            List<Object[]> args = (List)arguments[1];
            List<BranchRollbackInfo> rollbacks = new ArrayList();
            int i = 0;

            while(true) {
                if (i >= args.size()) {
                    message.addBranchRollbackInfo(this.datasource, rollbacks);
                    this.txcRedisService.hput(TxcRedisService.buildTransactionModel(message));
                    break;
                }

                BranchRollbackInfo rollbackInfo = new BranchRollbackInfo();
                List<SqlParamModel> redisModel = new ArrayList();
                List<RollbackSqlInfo> rollbackSqls = txcSql.rollbackSql((Object[])args.get(i), proxyedObject);
                Iterator var17 = rollbackSqls.iterator();

                while(var17.hasNext()) {
                    RollbackSqlInfo rollbackSql = (RollbackSqlInfo)var17.next();
                    String lockKey = this.datasource + "-" + txcSql.getTableName() + "-" + rollbackSql.pkv();
                    ReentrantRedLock lock = this.lock(lockKey, TxcContext.getTxcTimeout(), locks);
                    SqlParamModel model = this.redisSqlModel(rollbackSql, txcSql, transactionType, txc, lockKey, lock.getRedLockValue());
                    redisModel.add(model);
                }

                rollbackInfo.setRollbackSql(redisModel);
                rollbacks.add(rollbackInfo);
                ++i;
            }
        } catch (Exception var28) {
            throw new TxcTransactionException(var28, var28.getMessage());
        }

        Context context = this.processTimer.time();

        Object var30;
        try {
            var30 = invocation.proceed();
        } catch (Throwable var26) {
            throw new TxcDBException(var26, var26.getMessage());
        } finally {
            context.stop();
        }

        return var30;
    }
}

