package com.github.bluecatlee.gs4d.common.utils;

import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionUtil {

    private static int DEFAULT_TIMEOUT = 300;
    public static int iTimeout;
    public static int iTimeoutJta;
    public static int iDefaultTimeoutJta;
    public static int iSqlTimeout;
    public static final String DB_ORACLE = "oracle";
    public static final String DB_MSSQL = "mssql";
    public static final String MEMCACHE = "memcache";
    public static final String HASHMAP = "hashmap";
    public static boolean runFlag = false;
    public static String type;

    public static DefaultTransactionDefinition newTransactionDefinition() {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setTimeout(DEFAULT_TIMEOUT);
        definition.setIsolationLevel(2);
        definition.setPropagationBehavior(3);
        return definition;
    }

    public static DefaultTransactionDefinition newTransactionDefinition(int seconds) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setTimeout(seconds);
        definition.setIsolationLevel(2);
        definition.setPropagationBehavior(3);
        return definition;
    }
}

