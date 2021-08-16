package com.github.bluecatlee.gs4d.cache.api.model;

import java.io.Serializable;

public class CacheKeyGenerateRule implements Serializable {
    private static final long serialVersionUID = 1L;
//    @ApiField(description = "方法名称")
    private String methodName;
//    @ApiField(description = "方法缓存名称,如gb.xx.yy")
    private String cacheMethod;
//    @ApiField(description = "栏位转换")
    private String cacheMultiCol;

    public CacheKeyGenerateRule() {
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getCacheMethod() {
        return this.cacheMethod;
    }

    public void setCacheMethod(String cacheMethod) {
        this.cacheMethod = cacheMethod;
    }

    public String getCacheMultiCol() {
        return this.cacheMultiCol;
    }

    public void setCacheMultiCol(String cacheMultiCol) {
        this.cacheMultiCol = cacheMultiCol;
    }
}
