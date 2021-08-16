package com.github.bluecatlee.gs4d.message.producer.utils;

import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionUtil {
    private static int timeout = 50;
    public static int fq;
    public static int fr;
    public static int fs;
    public static int ft;
    public static final String fu = "oracle";
    public static final String fv = "mssql";
    public static final String fw = "memcache";
    public static final String fx = "hashmap";
    public static boolean fy = false;
    public static String fz;

    public static DefaultTransactionDefinition newTransactionDefinition() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setTimeout(timeout);
        transactionDefinition.setIsolationLevel(2);
        transactionDefinition.setPropagationBehavior(0);
        return transactionDefinition;
    }

    public static DefaultTransactionDefinition newTransactionDefinition(int timeout) {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setTimeout(timeout);
        transactionDefinition.setIsolationLevel(2);
        transactionDefinition.setPropagationBehavior(0);
        return transactionDefinition;
    }
}

