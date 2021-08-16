package com.github.bluecatlee.gs4d.gateway.bean;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import java.lang.reflect.Method;

public class MethodAndRequestClass {

    private Method method;
    // 0-AbstractRequest 1-AbstractUserSessionRequest; 2-AbstractOptionalSessionRequest 3-AbstractSessionRequest
    private int typeNumId = 0;
    private Class<? extends AbstractRequest> requestClass;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public int getTypeNumId() {
        return typeNumId;
    }

    public void setTypeNumId(int typeNumId) {
        this.typeNumId = typeNumId;
    }

    public Class<? extends AbstractRequest> getRequestClass() {
        return requestClass;
    }

    public void setRequestClass(Class<? extends AbstractRequest> requestClass) {
        this.requestClass = requestClass;
    }

}
