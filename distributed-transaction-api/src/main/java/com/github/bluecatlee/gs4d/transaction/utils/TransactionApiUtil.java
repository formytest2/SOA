package com.github.bluecatlee.gs4d.transaction.utils;

import java.util.HashMap;
import java.util.Map;

public class TransactionApiUtil {
    public static final String redisKeyStart = "Transaction_";

    public static final String mqTopic = "TRANSACTION_001";

    public static final String mqTag = "TRANSACTION_001_1";

    public static final String mqUpdateSqlTopic = "TRANSACTION_002";

    public static final String mqUpdateSqlTag = "TRANSACTION_002_1";

    public static final String txc = "txc";

    public static final Integer sqlInsertType = Integer.valueOf(0);

    public static final Integer sqlUpdateType = Integer.valueOf(1);

    public static final Integer sqlDeleteType = Integer.valueOf(2);

    public static final Long NO_DEAL = Long.valueOf(0L);

    public static final Long COMMIT = Long.valueOf(1L);

    public static final Long ROLL_BACL = Long.valueOf(2L);

    public static Map<Long, String> transactionState = new HashMap<Long, String>() {

    };

    public static Map<String, String> transactionSign = new HashMap<String, String>() {

    };

    public static Map<Integer, String> transactionRollbackFlag = new HashMap<Integer, String>() {

    };

    public static Map<String, String> sqlIsOutTimeMap = new HashMap<String, String>() {

    };

    public static Map<Integer, String> sqlStatusMap = new HashMap<Integer, String>() {

    };

    public static Map<String, String> transactionSignMap = new HashMap<String, String>() {

    };
}

