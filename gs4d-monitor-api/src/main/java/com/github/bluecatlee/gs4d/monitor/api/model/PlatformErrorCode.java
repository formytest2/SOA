package com.github.bluecatlee.gs4d.monitor.api.model;

import java.util.HashMap;
import java.util.Map;

public class PlatformErrorCode extends BaseEntity {
    private Long series;

    private String errorCode;

    private String errorCodeId;

    private String errorName;

    private String emailFtl;

    private String systemName;

    private Long systemId;

    private Integer proccessType;

    private Integer noticeTimeInterval;

    private Long methodId;

    private String methodName;

    private String noticeUserNames;

    private String codeDes;

    private String methodDescription;

    private String javaClassName;

    private String javaMethodName;

    public void setSeries(Long series) {
        this.series = series;
    }

    public Long getSeries() {
        return this.series;
    }

    public void setErrorCodeId(String errorCodeId) {
        this.errorCodeId = errorCodeId;
    }

    public String getErrorCodeId() {
        return this.errorCodeId;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorName() {
        return this.errorName;
    }

    public void setEmailFtl(String emailFtl) {
        this.emailFtl = emailFtl;
    }

    public String getEmailFtl() {
        return this.emailFtl;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemName() {
        return this.systemName;
    }

    public void setProccessType(Integer proccessType) {
        this.proccessType = proccessType;
    }

    public Integer getProccessType() {
        return this.proccessType;
    }

    public void setNoticeTimeInterval(Integer noticeTimeInterval) {
        this.noticeTimeInterval = noticeTimeInterval;
    }

    public Integer getNoticeTimeInterval() {
        return this.noticeTimeInterval;
    }

    public Long getSystemId() {
        return this.systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Long getMethodId() {
        return this.methodId;
    }

    public void setMethodId(Long methodId) {
        this.methodId = methodId;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setNoticeUserNames(String noticeUserNames) {
        this.noticeUserNames = noticeUserNames;
    }

    public String getNoticeUserNames() {
        return this.noticeUserNames;
    }

    public String getCodeDes() {
        if (msgCodeDesc.get(getErrorName()) != null)
            return msgCodeDesc.get(getErrorName());
        return getErrorName();
    }

    public void setCodeDes(String codeDes) {
        this.codeDes = codeDes;
    }

    public String getMethodDescription() {
        return this.methodDescription;
    }

    public void setMethodDescription(String methodDescription) {
        this.methodDescription = methodDescription;
    }

    public String getJavaClassName() {
        return this.javaClassName;
    }

    public void setJavaClassName(String javaClassName) {
        this.javaClassName = javaClassName;
    }

    public String getJavaMethodName() {
        return this.javaMethodName;
    }

    public void setJavaMethodName(String javaMethodName) {
        this.javaMethodName = javaMethodName;
    }

    public static Map<String, String> msgCodeDesc = new HashMap<>();

    static {
        msgCodeDesc.put("????????????", "-001");
        msgCodeDesc.put("??????????????????", "-009");
        msgCodeDesc.put("???????????????", "-008");
        msgCodeDesc.put("??????????????????", "-003");
        msgCodeDesc.put("??????????????????", "-004");
        msgCodeDesc.put("redis??????????????????", "-005");
        msgCodeDesc.put("job??????????????????????????????", "-006");
        msgCodeDesc.put("?????????????????????????????????", "-007");
    }
}
