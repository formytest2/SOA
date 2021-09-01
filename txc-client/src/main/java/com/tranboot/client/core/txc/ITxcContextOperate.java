package com.tranboot.client.core.txc;

public interface ITxcContextOperate {
    Object getUserData(String var1);

    Object putUserData(String var1, Object var2);

    Object removeUserData(String var1);

    Object getRpcContext();

    Object removeUserData();

    Object removeRpcContext();
}
