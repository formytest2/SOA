package com.github.bluecatlee.gs4d.transaction.utils;

public class Constants {

    public static String SUB_SYSTEM = "transaction";

    public static final String U = "dbsync_lock_now";
    public static final Integer V = 0;
    public static String TransactionDeleteJobLockKey = "TransactionDeleteJobLock";
    public static String TransactionLockKeyPrefix = "transactionLock_";
    public static String TransactionRollBacSuccButUpdateLogErrLockKey = "transactionRollBacSuccButUpdateLogErrLock";
    public static String aa = "transactionLock_";
    public static String TransactionCompensJobLockKey = "transactionCompensJobLock";
    public static String ac = "transactionRollBackLock";
    public static String TransactionOvertimeJobLockKey = "transactionOvertimeJobLock";

    public static final String DISTRIBUTED_TRANSACTION_SEQUENCE = "distributed_transaction_sequence";
    public static final String TRANSACTION_ID = "transaction_id";
    public static final String ag = "transaction_shared_series";
    public static final String TRANSACTION_ROLLBACK_FLAG_SERIES = "transaction_rollback_flag_series";

    public static final String ai = "0";
    public static final String aj = "1";

    public static final Integer rollback_success = 0;          // 回滚操作执行结果 0成功 -1失败
    public static final Integer rollback_fail = -1;

    public static final Integer am = 0;
    public static final Integer an = 1;
    public static final Integer error_end = 2;  // 事务状态：异常结束

    public static final Integer ap = 3;                 // transaction_state: 3回滚失败？
    public static final Integer sql_status_2 = 2;      // sql_status:  0超时，1为需要回滚，2为不用回滚

    public static final String aq = "Y";
    public static final String ar = "N";

    public static String getTransactionLockKey(Long transactionId) {
        return TransactionLockKeyPrefix + transactionId;
    }

    public static String getTransactionRollBacSuccButUpdateLogErrLockKey(Long suffix) {
        return TransactionRollBacSuccButUpdateLogErrLockKey + suffix;
    }
}

