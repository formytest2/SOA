package com.tranboot.client.core;

public class JdbcTemplatePointCut {
    public static final String update1 = "execution(public * update(String))";
    public static final String update2 = "execution(public * update(String,Object[],..))";
    public static final String batchUpdate1 = "execution(public * batchUpdate(String...))";
    public static final String batchUpdate2 = "execution(public * batchUpdate(String,java.util.List<Object[]>,..))";

    public JdbcTemplatePointCut() {
    }
}
