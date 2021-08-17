package com.github.bluecatlee.gs4d.transaction.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.bluecatlee.gs4d.common.utils.ExceptionUtil;
import com.github.bluecatlee.gs4d.common.utils.JsonUtil;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.transaction.api.model.*;
import com.github.bluecatlee.gs4d.transaction.api.request.*;
import com.github.bluecatlee.gs4d.transaction.api.response.*;
import com.github.bluecatlee.gs4d.transaction.api.service.TransactionWebService;
import com.github.bluecatlee.gs4d.transaction.api.utils.TransactionApiUtil;
import com.github.bluecatlee.gs4d.transaction.dao.TransactionLogDao;
import com.github.bluecatlee.gs4d.transaction.dao.TransactionSharedDao;
import com.github.bluecatlee.gs4d.transaction.dao.TransactionSqlLogDao;
import com.github.bluecatlee.gs4d.transaction.model.TRANSACTION_LOG;
import com.github.bluecatlee.gs4d.transaction.model.TRANSACTION_SHARED;
import com.github.bluecatlee.gs4d.transaction.model.TRANSACTION_SQL_LOG;
import com.github.bluecatlee.gs4d.transaction.utils.AU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@Service("transactionWebService")
public class TransactionWebServiceImpl implements TransactionWebService {

    protected static Logger logger = LoggerFactory.getLogger(TransactionWebServiceImpl.class);

    @Autowired
    private TransactionLogDao transactionLogDao;

    @Autowired
    private TransactionSqlLogDao transactionSqlLogDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TransactionSharedDao transactionSharedDao;

    public TransactionGetResponse getTransaction(TransactionGetRequest var1) {
        TransactionGetResponse var2 = new TransactionGetResponse();

        try {
            logger.debug("分布式事务查询入参是" + JsonUtil.toJson(var1));
            Long var3 = AU.e(var1.getPageNo(), var1.getPageCount());
            StringBuilder var4 = new StringBuilder();
            StringBuilder var5 = new StringBuilder("select a.transaction_id,a.start_dtme,a.end_dtme,a.ip_address,a.transaction_state,a.transaction_sign,a.from_system,a.method_name,a.transaction_rollback_flag from transaction_log a ");
            StringBuilder var6 = new StringBuilder("select count(*) from transaction_log a ");
            if (StringUtil.isAllNotNullOrBlank(new String[]{var1.getSqlSearch()})) {
                var5.append("inner join transaction_sql_log b on a.transaction_id=b.transaction_id where b.`sql` like '%").append(var1.getSqlSearch()).append("%'");
                var6.append("inner join transaction_sql_log b on a.transaction_id=b.transaction_id where b.`sql` like '%").append(var1.getSqlSearch()).append("%'");
            } else {
                var5.append(" where 0=0 ");
                var6.append(" where 0=0 ");
            }

            if (StringUtil.isAllNotNullOrBlank(new Object[]{var1.getTransactionId()})) {
                var4.append(" and a.transaction_id =").append(var1.getTransactionId());
            }

            if (StringUtil.isAllNotNullOrBlank(new String[]{var1.getIpAddress()})) {
                var4.append(" and a.ip_address like '%").append(var1.getIpAddress()).append("%'");
            }

            if (StringUtil.isAllNotNullOrBlank(new Object[]{var1.getTransactionState()})) {
                var4.append(" and a.transaction_state ='").append(var1.getTransactionState()).append("'");
            }

            if (StringUtil.isAllNotNullOrBlank(new String[]{var1.getTransactionSign()})) {
                var4.append(" and a.transaction_sign ='").append(var1.getTransactionSign()).append("'");
            }

            if (StringUtil.isAllNotNullOrBlank(new String[]{var1.getFromSystem()})) {
                var4.append(" and a.from_system like '%").append(var1.getFromSystem()).append("%'");
            }

            if (StringUtil.isAllNotNullOrBlank(new String[]{var1.getMethodName()})) {
                var4.append(" and a.method_name like '%").append(var1.getMethodName()).append("%'");
            }

            if (StringUtil.isAllNotNullOrBlank(new Object[]{var1.getEndDtme()}) && StringUtil.isAllNotNullOrBlank(new Object[]{var1.getStartDtme()})) {
                String var7 = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(var1.getStartDtme());
                String var8 = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(var1.getEndDtme());
                var4.append(" and a.start_dtme between '").append(var7).append("' and '").append(var8).append("'");
            }

            StringBuilder var14 = new StringBuilder();
            var14.append(" order by a.start_dtme desc limit ").append(var3).append(",").append(var1.getPageCount());
            List var15 = this.transactionLogDao.d(var5.toString() + var4.toString() + var14.toString());
            ArrayList var9 = new ArrayList();
            Iterator var10 = var15.iterator();

            while(var10.hasNext()) {
                TRANSACTION_LOG var11 = (TRANSACTION_LOG)var10.next();
                TranscationWebModel var12 = new TranscationWebModel();
                if (var11.getEND_DTME() != null) {
                    var12.setEndDtme((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(var11.getEND_DTME()));
                }

                var12.setFromSystem(var11.getFROM_SYSTEM());
                var12.setIpAddress(var11.getIP_ADDRESS());
                var12.setMethodName(var11.getMETHOD_NAME());
                var12.setRollbackStatus((String) TransactionApiUtil.transactionRollbackFlag.get(Integer.valueOf(var11.getTRANSACTION_ROLLBACK_FLAG())));
                if (var11.getSTART_DTME() != null) {
                    var12.setStartDtme((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(var11.getSTART_DTME()));
                }

                var12.setTransactionId(var11.getTRANSACTION_ID());
                var12.setTransactionSign((String)TransactionApiUtil.transactionSign.get(var11.getTRANSACTION_SIGN()));
                var12.setTransactionState((String)TransactionApiUtil.transactionState.get(var11.getTRANSACTION_STATE()));
                var9.add(var12);
            }

            Long var16 = this.transactionLogDao.e(var6 + var4.toString());
            var2.setTotal(var16);
            var2.setModels(var9);
        } catch (Exception var13) {
            ExceptionUtil.processException(var13, var2);
        }

        logger.debug("分布式事务查询出参是" + JsonUtil.toJson(var2));
        return var2;
    }

    public TransactionDetailGetResponse getTransactionDetail(TransactionDetailGetRequest var1) {
        TransactionDetailGetResponse var2 = new TransactionDetailGetResponse();

        try {
            logger.debug("分布式事务sql明细查询入参是" + JsonUtil.toJson(var1));
            Long var3 = AU.e(var1.getPageNo(), var1.getPageCount());
            StringBuilder var4 = new StringBuilder();
            StringBuilder var5 = new StringBuilder("select transaction_id,transaction_db_id,source_db,table_name,commit_sql_dtme,rollback_sql_dtme,`sql`,sql_param,transaction_sign,sql_level,sql_is_out_time,sql_type,sql_key_value,sql_id,biz_redis_key,sql_status,biz_redis_value,sql_timeout,result_num,transaction_error_log from transaction_sql_log a where 0=0 ");
            StringBuilder var6 = new StringBuilder("select count(*) from transaction_sql_log a where 0=0 ");
            if (StringUtil.isAllNotNullOrBlank(new Object[]{var1.getTransactionId()})) {
                var4.append(" and a.transaction_id =").append(var1.getTransactionId());
            }

            if (StringUtil.isAllNotNullOrBlank(new Object[]{var1.getDbId()})) {
                var4.append(" and a.transaction_db_id = ").append(var1.getDbId());
            }

            if (StringUtil.isAllNotNullOrBlank(new String[]{var1.getTable()})) {
                var4.append(" and a.table_name like '%").append(var1.getTable()).append("%'");
            }

            if (StringUtil.isAllNotNullOrBlank(new String[]{var1.getSqlSearch()})) {
                var4.append(" and a.`sql`  like '%").append(var1.getSqlSearch()).append("%'");
            }

            if (StringUtil.isAllNotNullOrBlank(new Object[]{var1.getEndDtme()}) && StringUtil.isAllNotNullOrBlank(new Object[]{var1.getStartDtme()})) {
                String var7 = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(var1.getStartDtme());
                String var8 = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(var1.getEndDtme());
                var4.append(" and a.rollback_sql_dtme between '").append(var7).append("' and '").append(var8).append("'");
            }

            Long var19 = this.transactionSqlLogDao.g(var6.toString() + var4.toString());
            ArrayList var20 = new ArrayList();
            if (var19 == 0L && StringUtil.isAllNotNullOrBlank(new Object[]{var1.getTransactionId()})) {
                var19 = 1L;
                String var21 = TransactionApiUtil.redisKeyStart + var1.getTransactionId();
                HashOperations var22 = this.stringRedisTemplate.opsForHash();
                Set var23 = var22.keys(var21);
                if (var23.size() > 0) {
                    Iterator var24 = var23.iterator();

                    while(var24.hasNext()) {
                        String var25 = (String)var24.next();
                        RedisTransactionModel var14 = (RedisTransactionModel)JSONObject.parseObject((String)var22.get(var21, var25), RedisTransactionModel.class);
                        Iterator var15 = var14.getSqlParamModel().iterator();

                        while(var15.hasNext()) {
                            SqlParamModel var16 = (SqlParamModel)var15.next();
                            TranscationDetailModel var17 = new TranscationDetailModel();
                            var17.setSourceDb(var14.getSourceDb());
                            var17.setSql(var16.getSql());
                            if (var14.getStatus() != 0) {
                                var17.setSqlIsOutTime("正常回滚sql");
                            } else {
                                var17.setSqlIsOutTime("超时sql");
                            }

                            if (var14.getStatus() != null) {
                                var17.setSqlStatus((String)TransactionApiUtil.sqlStatusMap.get(var14.getStatus()));
                            }

                            var17.setSqlTimeOut(var14.getTransactionOutTimeSecond());
                            var17.setTableName(var16.getTable());
                            var17.setTransactionErrorLog("");
                            var17.setTransactionId(var1.getTransactionId());
                            var17.setTransactionSign("未回滚sql");
                            var17.setDbId(Long.valueOf(var25));
                            var17.setDataFrom("redis");
                            var20.add(var17);
                        }
                    }
                }
            } else {
                StringBuilder var9 = new StringBuilder();
                var9.append(" order by a.commit_sql_dtme desc limit ").append(var3).append(",").append(var1.getPageCount());
                List var10 = this.transactionSqlLogDao.f(var5.toString() + var4.toString() + var9.toString());
                Iterator var11 = var10.iterator();

                while(var11.hasNext()) {
                    TRANSACTION_SQL_LOG var12 = (TRANSACTION_SQL_LOG)var11.next();
                    TranscationDetailModel var13 = new TranscationDetailModel();
                    if (var12.getROLLBACK_SQL_DTME() != null) {
                        var13.setRollbackDtme((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(var12.getROLLBACK_SQL_DTME()));
                    }

                    var13.setResultNum(var12.getRESULT_NUM());
                    var13.setSourceDb(var12.getSOURCE_DB());
                    var13.setSql(var12.getSQL());
                    if (var12.getSQL_IS_OUT_TIME() != null) {
                        var13.setSqlIsOutTime((String)TransactionApiUtil.sqlIsOutTimeMap.get(var12.getSQL_IS_OUT_TIME()));
                    }

                    if (var12.getSQL_STATUS() != null) {
                        var13.setSqlStatus((String)TransactionApiUtil.sqlStatusMap.get(var12.getSQL_STATUS()));
                    }

                    var13.setSqlTimeOut(var12.getSQL_TIMEOUT());
                    var13.setTableName(var12.getTABLE_NAME());
                    var13.setTransactionErrorLog(var12.getTRANSACTION_ERROR_LOG());
                    var13.setTransactionId(var12.getTRANSACTION_ID());
                    if (var12.getTRANSACTION_SIGN() != null) {
                        var13.setTransactionSign((String)TransactionApiUtil.transactionSignMap.get(var12.getTRANSACTION_SIGN()));
                    }

                    var13.setDbId(var12.getTRANSACTION_DB_ID());
                    var13.setDataFrom("mysql");
                    var20.add(var13);
                }
            }

            var2.setTotal(var19);
            var2.setModels(var20);
        } catch (Exception var18) {
            ExceptionUtil.processException(var18, var2);
        }

        logger.debug("分布式事务sql明细查询出参是" + JsonUtil.toJson(var2));
        return var2;
    }

    public TransactionStateListGetResponse getTransactionStateList(TransactionStateListGetRequest var1) {
        TransactionStateListGetResponse var2 = new TransactionStateListGetResponse();

        try {
            ArrayList var3 = new ArrayList();
            Map var4 = TransactionApiUtil.transactionState;
            Iterator var5 = var4.entrySet().iterator();

            while(var5.hasNext()) {
                Entry var6 = (Entry)var5.next();
                TranscationStateListModel var7 = new TranscationStateListModel();
                var7.setTransactionState((Long)var6.getKey());
                var7.setTransactionStateName((String)var6.getValue());
                var3.add(var7);
            }

            var2.setTranscationStateListModel(var3);
        } catch (Exception var8) {
            ExceptionUtil.processException(var8, var2);
        }

        return var2;
    }

    public TransactionSignListGetResponse getTransactionSignList(TransactionSignListGetRequest var1) {
        TransactionSignListGetResponse var2 = new TransactionSignListGetResponse();

        try {
            ArrayList var3 = new ArrayList();
            Map var4 = TransactionApiUtil.transactionSign;
            Iterator var5 = var4.entrySet().iterator();

            while(var5.hasNext()) {
                Entry var6 = (Entry)var5.next();
                TranscationSignListModel var7 = new TranscationSignListModel();
                var7.setTransactionSign((String)var6.getKey());
                var7.setTransactionSignName((String)var6.getValue());
                var3.add(var7);
            }

            var2.setTranscationSignListModel(var3);
        } catch (Exception var8) {
            ExceptionUtil.processException(var8, var2);
        }

        return var2;
    }

    public SharedGetResponse getShared(SharedGetRequest var1) {
        SharedGetResponse var2 = new SharedGetResponse();

        try {
            Long var3 = AU.e(var1.getPageNo(), var1.getPageCount());
            List var4 = this.transactionSharedDao.b(var3, var1.getPageCount());
            Long var5 = this.transactionSharedDao.getTranstionSharedCount();
            ArrayList var6 = new ArrayList();
            Iterator var7 = var4.iterator();

            while(var7.hasNext()) {
                TRANSACTION_SHARED var8 = (TRANSACTION_SHARED)var7.next();
                SharedModel var9 = new SharedModel();
                var9.setDbName(var8.getDB_NAME());
                var9.setSeries(var8.getSERIES());
                var9.setSharedColumn(var8.getSHARED_COLUMN());
                var9.setTableName(var8.getTABLE_NAME());
                var6.add(var9);
            }

            var2.setSharedModels(var6);
            var2.setTotal(var5);
        } catch (Exception var10) {
            ExceptionUtil.processException(var10, var2);
        }

        return var2;
    }

    public SharedInsertResponse insertShared(SharedInsertRequest var1) {
        SharedInsertResponse var2 = new SharedInsertResponse();

        try {
            this.transactionSharedDao.b(var1.getSharedColumn(), var1.getTableName(), var1.getDbName());
        } catch (Exception var4) {
            ExceptionUtil.processException(var4, var2);
        }

        return var2;
    }

    public SharedDeleteResponse deleteShared(SharedDeleteRequest var1) {
        SharedDeleteResponse var2 = new SharedDeleteResponse();

        try {
            this.transactionSharedDao.c(var1.getSeries());
        } catch (Exception var4) {
            ExceptionUtil.processException(var4, var2);
        }

        return var2;
    }
}

