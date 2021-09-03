package com.github.bluecatlee.gs4d.transaction.service.impl;

import com.github.bluecatlee.gs4d.sequence.utils.SeqGetUtil;
import com.github.bluecatlee.gs4d.transaction.api.request.SharedColumnGetRequest;
import com.github.bluecatlee.gs4d.transaction.api.request.TransactionInitRequest;
import com.github.bluecatlee.gs4d.transaction.api.response.SharedColumnGetResponse;
import com.github.bluecatlee.gs4d.transaction.api.response.TransactionInitResponse;
import com.github.bluecatlee.gs4d.transaction.api.service.TransactionInitService;
import com.github.bluecatlee.gs4d.transaction.dao.TransactionLogDao;
import com.github.bluecatlee.gs4d.transaction.dao.TransactionSharedDao;
import com.github.bluecatlee.gs4d.transaction.model.TRANSACTION_LOG;
import com.github.bluecatlee.gs4d.transaction.model.TRANSACTION_SHARED;
import com.github.bluecatlee.gs4d.transaction.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("transactionInitService")
public class TransactionInitServiceImpl implements TransactionInitService {

    protected static Logger logger = LoggerFactory.getLogger(TransactionInitServiceImpl.class);

    @Autowired
    private TransactionLogDao transactionLogDao;

    @Autowired
    private TransactionSharedDao transactionSharedDao;

    public TransactionInitResponse initTransaction(TransactionInitRequest transactionInitRequest) {
        TransactionInitResponse transactionInitResponse = new TransactionInitResponse();

        try {
            TRANSACTION_LOG transactionLog = new TRANSACTION_LOG();
            transactionLog.setFROM_SYSTEM(transactionInitRequest.getFromSystem());
            transactionLog.setIP_ADDRESS(transactionInitRequest.getIpAddress());
            transactionLog.setMETHOD_NAME(transactionInitRequest.getMethodName());
            Long transactionId = SeqGetUtil.getNoSubSequence(Constants.TRANSACTION_ID);
            transactionLog.setTRANSACTION_ID(transactionId);
            transactionLog.setTRANSACTION_ROLLBACK_FLAG(transactionInitRequest.getTransactionRollbackFlag().toString());
            this.transactionLogDao.insert(transactionLog);
            Long nowTime = this.transactionLogDao.getNowTime();
            transactionInitResponse.setTransactionStartTime(nowTime);
            transactionInitResponse.setTransactionId(transactionId);
        } catch (Exception e) {
            transactionInitResponse.setCode(-1L);
            transactionInitResponse.setMessage(e.getMessage());
            logger.error("分布式事务插入主表失败，原因" + e.getMessage(), e);
        }

        return transactionInitResponse;
    }

    // 根据库名和表名查分库字段
    public SharedColumnGetResponse getSharedColumn(SharedColumnGetRequest sharedColumnGetRequest) {
        SharedColumnGetResponse sharedColumnGetResponse = new SharedColumnGetResponse();

        try {
            List sharedColumnList = this.transactionSharedDao.querySharedColumn(sharedColumnGetRequest.getSchema(), sharedColumnGetRequest.getTable());
            if (sharedColumnList == null || sharedColumnList.isEmpty()) {
                throw new Exception("本次查询失败，没有查到数据库：" + sharedColumnGetRequest.getSchema() + "，数据表：" + sharedColumnGetRequest.getTable());
            }

            sharedColumnGetResponse.setSharedColumnName(((TRANSACTION_SHARED)sharedColumnList.get(0)).getSHARED_COLUMN());
        } catch (Exception e) {
            sharedColumnGetResponse.setCode(-1L);
            sharedColumnGetResponse.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }

        return sharedColumnGetResponse;
    }
}

