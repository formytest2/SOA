package com.tranboot.client.core.txc;


import java.util.HashMap;
import java.util.Map;

public class TxcContextOperateByThreadLocal implements ITxcContextOperate {
    private static final ThreadLocal<Map<Object, Object>> _threadLocalContext = new ThreadLocal();

    public Object getUserData(String paramString) {
        return this.get().get(paramString);
    }

    public Object putUserData(String key, Object value) {
        return this.get().put(key, value);
    }

    public Object removeUserData(String key) {
        return this.get().remove(key);
    }

    public Object getRpcContext() {
        return null;
    }

    private Map<Object, Object> get() {
        if (_threadLocalContext.get() == null) {
            _threadLocalContext.set(new HashMap());
        }

        return (Map)_threadLocalContext.get();
    }

    public Object removeUserData() {
        Object t = _threadLocalContext.get();
        if (t != null) {
            _threadLocalContext.set(null);
            _threadLocalContext.remove();
        }

        return t;
    }

    public Object removeRpcContext() {
        return null;
    }
}
