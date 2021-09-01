//package com.tranboot.client.service.dbsync;
//
//import com.alibaba.rocketmq.client.exception.MQBrokerException;
//import com.alibaba.rocketmq.client.exception.MQClientException;
//import com.alibaba.rocketmq.remoting.exception.RemotingException;
//import com.gb.soa.omp.dbsync.model.MqSyncModel;
//import com.gb.soa.omp.dbsync.model.RdisSyncDetailModel;
//import com.gb.soa.omp.dbsync.model.RdisSyncModel;
//import com.tranboot.client.core.dbsync.DbSyncSqlInvokeContextManager.SQLInvokeContext;
//import com.tranboot.client.core.dbsync.DbSyncSqlInvokeContextManager.TransactionPack;
//import com.tranboot.client.model.dbsync.SqlTransformResult;
//import com.tranboot.client.utils.FlushableLoggerFactory;
//import com.tranboot.client.utils.FlushableLoggerFactory.FlushableLogger;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.List;
//import org.slf4j.LoggerFactory;
//
//public class DbSyncTaskBuilder {
//    private static final FlushableLogger logger = FlushableLoggerFactory.getLogger(LoggerFactory.getLogger(DbSyncTaskBuilder.DbSyncTask.class));
//    private SqlParserService sqlParserService = new SqlParserService();
//    private DbsyncMqService mqService;
//    private RedisService redisService;
//
//    public DbSyncTaskBuilder(DbsyncMqService mqService, RedisService redisService) {
//        this.mqService = mqService;
//        this.redisService = redisService;
//    }
//
//    public DbSyncTaskBuilder.DbSyncTask build(TransactionPack pack) {
//        return new DbSyncTaskBuilder.DbSyncTask(pack);
//    }
//
//    class DbSyncTask implements Runnable {
//        private TransactionPack pack;
//
//        public DbSyncTask(TransactionPack pack) {
//            this.pack = pack;
//        }
//
//        public String toString() {
//            return "DbsyncTask【" + this.pack.getTransactionNo() + "】";
//        }
//
//        public void run() {
//            List<String> sqls = new ArrayList();
//            List<RdisSyncDetailModel> redisDetails = new ArrayList();
//            DbSyncTaskBuilder.logger.debug(this.pack.getTransactionNo() + "-->开始解析sql任务");
//            String titleBizKey = null;
//            String titleSourceDb = null;
//            String titleSourceTable = null;
//            String titleTargetDB = null;
//            String titleTargetTable = null;
//            long timeStamp = System.currentTimeMillis();
//            DbSyncTaskBuilder.logger.debug(String.format("dbsync sqllog start:{'time':%d,'transactionId':%s}", timeStamp, this.pack.getTransactionNo()));
//
//            try {
//                Iterator var10 = this.pack.getSqlinvokecontexts().iterator();
//
//                while(var10.hasNext()) {
//                    SQLInvokeContext context = (SQLInvokeContext)var10.next();
//                    String sql = context.getSql();
//                    DbSyncTaskBuilder.logger.debug(this.pack.getTransactionNo() + "-->原sql:" + sql);
//                    Object[] args = context.getArgs();
//                    SqlTransformResult result = DbSyncTaskBuilder.this.sqlParserService.parseSql(sql);
//                    if (result != null && result != SqlTransformResult.a) {
//                        if (result == SqlTransformResult.b) {
//                            DbSyncTaskBuilder.logger.error(String.format("sql语句:%s没有主键，无法进行数据双写操作!!!", sql));
//                        } else {
//                            try {
//                                String sql2Send = result.sql(args);
//                                sqls.add(sql2Send);
//                                DbSyncTaskBuilder.logger.debug(this.pack.getTransactionNo() + "-->同步sql:" + sql2Send);
//                            } catch (Exception var19) {
//                                DbSyncTaskBuilder.logger.error(this.pack.getTransactionNo() + "-->双写任务-分析业务sql失败" + sql, var19);
//                            }
//
//                            DbSyncTaskBuilder.logger.debug(this.pack.getTransactionNo() + "-->开始构造model-----" + this.pack.getTransactionNo());
//                            RdisSyncDetailModel redisDetailModel = new RdisSyncDetailModel();
//                            DbSyncTaskBuilder.logger.debug(this.pack.getTransactionNo() + "-->开始设置主键字段");
//                            redisDetailModel.setColumnKeyName(result.getPrimaryKey());
//                            DbSyncTaskBuilder.logger.debug(this.pack.getTransactionNo() + "-->开始设置主键值");
//                            List<String> vv = Arrays.asList(result.primaryKeyValue(args));
//                            DbSyncTaskBuilder.logger.debug(this.pack.getTransactionNo() + "-->主键值数量:" + vv.size());
//                            redisDetailModel.setColumnKeyValue(vv);
//                            DbSyncTaskBuilder.logger.debug(this.pack.getTransactionNo() + "-->开始设置主键值标题");
//                            if (titleBizKey == null) {
//                                titleBizKey = (String)redisDetailModel.getColumnKeyValue().get(0);
//                            }
//
//                            DbSyncTaskBuilder.logger.debug(this.pack.getTransactionNo() + "-->开始设置源表标题");
//                            redisDetailModel.setSourceTable(result.getSourceTable());
//                            if (titleSourceTable == null) {
//                                titleSourceTable = result.getSourceTable();
//                            }
//
//                            DbSyncTaskBuilder.logger.debug(this.pack.getTransactionNo() + "-->开始设置源表分库字段");
//                            redisDetailModel.setSourceTablePartitionName(result.getSourcePartitionKey());
//                            DbSyncTaskBuilder.logger.debug(this.pack.getTransactionNo() + "-->开始设置源表分库字段值");
//                            redisDetailModel.setSourceTablePartitionValue(result.sourcePartitionValue(args));
//                            DbSyncTaskBuilder.logger.debug(this.pack.getTransactionNo() + "-->开始设置目标表分库字段");
//                            redisDetailModel.setTargetTablePartitionName(result.getTargetPartitionKey());
//                            DbSyncTaskBuilder.logger.debug(this.pack.getTransactionNo() + "-->开始设置目标表分库字段值");
//                            redisDetailModel.setTargetTablePartitionValue(result.targetPartitionValue(args));
//                            DbSyncTaskBuilder.logger.debug(this.pack.getTransactionNo() + "-->开始设置源表数据库类型标题");
//                            if (titleSourceDb == null) {
//                                titleSourceDb = result.getSourceDb();
//                            }
//
//                            DbSyncTaskBuilder.logger.debug(this.pack.getTransactionNo() + "-->开始设置目标表数据库类型标题");
//                            if (titleTargetDB == null) {
//                                titleTargetDB = result.getTargetDb();
//                            }
//
//                            DbSyncTaskBuilder.logger.debug(this.pack.getTransactionNo() + "-->开始设置目标表标题");
//                            if (titleTargetTable == null) {
//                                titleTargetTable = result.getTargetTable();
//                            }
//
//                            redisDetails.add(redisDetailModel);
//                            DbSyncTaskBuilder.logger.debug(this.pack.getTransactionNo() + "-->构造model完成");
//                        }
//                    }
//                }
//            } catch (Exception var20) {
//                var20.printStackTrace();
//                DbSyncTaskBuilder.logger.error(this.pack.getTransactionNo() + "-->任务处理-sql解析失败", var20);
//                return;
//            }
//
//            if (redisDetails.size() == 0) {
//                DbSyncTaskBuilder.logger.debug(String.format(this.pack.getTransactionNo() + "-->没有需要双写的sql"));
//            } else {
//                DbSyncTaskBuilder.logger.debug(String.format("dbsync sqllog end:{'time':%d,'transactionId':%s}", timeStamp, this.pack.getTransactionNo()));
//                MqSyncModel mqSyncModel = new MqSyncModel();
//                mqSyncModel.setSql(sqls);
//                mqSyncModel.setTargetDb(titleTargetDB);
//                mqSyncModel.setTargetTable(titleTargetTable);
//                mqSyncModel.setColumnKeyValue(titleBizKey);
//                String redisKey = RedisService.key(titleBizKey);
//                mqSyncModel.setRedisKey(redisKey);
//                RdisSyncModel redisModel = new RdisSyncModel();
//                redisModel.setRdisSyncDetailModel(redisDetails);
//                redisModel.setSourceDb(titleSourceDb);
//                redisModel.setTargetDb(titleTargetDB);
//                String messageKey = DbsyncMqService.messageKey(titleBizKey, titleSourceDb, titleSourceTable, titleTargetDB);
//                mqSyncModel.setMessageKey(messageKey);
//                redisModel.setMessageKey(messageKey);
//                DbSyncTaskBuilder.logger.debug(String.format(this.pack.getTransactionNo() + "-->存入redis kv-> key:%s", redisKey));
//
//                try {
//                    DbSyncTaskBuilder.this.redisService.kvset(redisKey, redisModel);
//                    DbSyncTaskBuilder.logger.debug(this.pack.getTransactionNo() + "-->存入redis kv成功");
//                } catch (Exception var18) {
//                    DbSyncTaskBuilder.logger.error(this.pack.getTransactionNo() + "-->双写任务-写redis失败", var18);
//                    return;
//                }
//
//                try {
//                    DbSyncTaskBuilder.logger.debug(String.format(this.pack.getTransactionNo() + "-->发送mq消息-> key:%s", redisModel.getMessageKey()));
//                    DbSyncTaskBuilder.this.mqService.send(mqSyncModel, redisModel);
//                    DbSyncTaskBuilder.logger.debug(this.pack.getTransactionNo() + "-->发送mq消息成功");
//                } catch (RemotingException | MQBrokerException | InterruptedException | MQClientException var17) {
//                    DbSyncTaskBuilder.logger.error(this.pack.getTransactionNo() + "-->双写任务-发mq失败", var17);
//                    var17.printStackTrace();
//                    return;
//                }
//
//                DbSyncTaskBuilder.logger.debug(this.pack.getTransactionNo() + "-->解析sql任务结束");
//                DbSyncTaskBuilder.logger.flush();
//            }
//        }
//    }
//}
