package com.github.bluecatlee.gs4d.message.consumer.utils;

import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionUtil {
    private static int timeout = 50;
    public static int gf;
    public static int gg;
    public static int gh;
    public static int gi;
    public static final String gj = "oracle";
    public static final String gk = "mssql";
    public static final String gl = "memcache";
    public static final String gm = "hashmap";
    public static boolean gn = false;
    public static String go;

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
