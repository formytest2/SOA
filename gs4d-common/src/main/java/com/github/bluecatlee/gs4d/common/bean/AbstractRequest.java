package com.github.bluecatlee.gs4d.common.bean;

import com.github.bluecatlee.gs4d.common.exception.AbstractExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ValidateClientException;
import com.github.bluecatlee.gs4d.common.valid.BeanValidator;

import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

public class AbstractRequest implements Serializable {

    private static final long serialVersionUID = 4220122809646144375L;

//    @ApiField(description = "租户编号")
    @NotNull(message = "租户编号不能为空!")
    private Long tenantNumId;

//    @ApiField(description = "测试标识")
    @NotNull(message = "测试标识不能为空!")
    private Long dataSign;

//    @ApiField(description = "消息编号")
    private Long messageSeries;

//    @ApiField(description = "服务调用编号")
    private Long requestNumId;

//    @ApiField(description = "是否去重检查,1:需要 0:不需要")
    private Long checkRepeatSign;

//    @ApiField(description = "对外暴露方法名")
    private String exposeMethod;

//    @ApiField(description = "接口参数")
    private String appkey;

    public AbstractRequest() {
    }

    public String getAppkey() {
        return this.appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public Long getTenantNumId() {
        return this.tenantNumId;
    }

    public void setTenantNumId(Long tenantNumId) {
        this.tenantNumId = tenantNumId;
    }

    public Long getDataSign() {
        return this.dataSign;
    }

    public void setDataSign(Long dataSign) {
        this.dataSign = dataSign;
    }

    public Long getMessageSeries() {
        return this.messageSeries;
    }

    public void setMessageSeries(Long messageSeries) {
        this.messageSeries = messageSeries;
    }

    public Long getRequestNumId() {
        return this.requestNumId;
    }

    public void setRequestNumId(Long requestNumId) {
        this.requestNumId = requestNumId;
    }

    public Long getCheckRepeatSign() {
        return this.checkRepeatSign;
    }

    public void setCheckRepeatSign(Long checkRepeatSign) {
        this.checkRepeatSign = checkRepeatSign;
    }

    public String getExposeMethod() {
        return this.exposeMethod;
    }

    public void setExposeMethod(String exposeMethod) {
        this.exposeMethod = exposeMethod;
    }

    public final void validate(String subSystem, ExceptionType et) {
        this.validate(subSystem, et, false);
    }

    public final void validate(String subSystem, AbstractExceptionType et) {
        this.validate(subSystem, et, false);
    }

    public final void validate(String subSystem, ExceptionType et, boolean ignoreExceptionCategory) {
        StringBuffer buffer = new StringBuffer(64);
        Set<ConstraintViolation<Object>> constraintViolations = BeanValidator.validator.validate(this, new Class[0]);
        if (constraintViolations.size() <= 0) {
            this.checkRequest(subSystem, et);
        } else {
            Iterator iterator = constraintViolations.iterator();

            while(iterator.hasNext()) {
                ConstraintViolation<Object> cv = (ConstraintViolation)iterator.next();
                String message = cv.getMessage();
                buffer.append(message);
            }

            throw new ValidateClientException(subSystem, et, buffer.toString(), ignoreExceptionCategory);
        }
    }

    public final void validate(String subSystem, AbstractExceptionType et, boolean ignoreExceptionCategory) {
        StringBuffer buffer = new StringBuffer(64);
        Set<ConstraintViolation<Object>> constraintViolations = BeanValidator.validator.validate(this, new Class[0]);
        if (constraintViolations.size() <= 0) {
            this.checkRequest(subSystem, et);
        } else {
            Iterator iterator = constraintViolations.iterator();

            while(iterator.hasNext()) {
                ConstraintViolation<Object> cv = (ConstraintViolation)iterator.next();
                String message = cv.getMessage();
                buffer.append(message);
            }

            throw new ValidateClientException(subSystem, et, buffer.toString(), ignoreExceptionCategory);
        }
    }

    public void checkRequest(String subSystem, ExceptionType et) {
    }

    public void checkRequest(String subSystem, AbstractExceptionType et) {
    }

}
