package com.tranboot.client.test;

import com.tranboot.client.model.txc.TxcSQLTransformer;

/**
 * TxcSQLTransformer测试
 */
public class TxcSQLTransformerTest {

    public static void main(String[] args) {
//        String sql = "select a.id, a.name FROM users a, money b where a.id=b.id and a.id in (select id from innertables)";
        String sql = "update user set age = 10 where id = 1";
        TxcSQLTransformer txcSQLTransformer = new TxcSQLTransformer();
        try {
            txcSQLTransformer.transform(sql);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
