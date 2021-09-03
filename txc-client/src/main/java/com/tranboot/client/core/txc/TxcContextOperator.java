package com.tranboot.client.core.txc;

public class TxcContextOperator {

    private static ITxcContextOperate operate = null;

    public static Object getContextParam(String key) {
        return operate.getUserData(key);
    }

    public static Object setContextParam(String key, Object value) {
        return operate.putUserData(key, value);
    }

    public static Object removeContextParam(String key) {
        return operate.removeUserData(key);
    }

    public static Object removeContext() {
        return operate.removeUserData();
    }

    static {
        if (operate == null) {
            operate = new TxcContextOperateByThreadLocal();
        }
    }

}

