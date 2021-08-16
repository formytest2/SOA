package com.github.bluecatlee.gs4d.monitor.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import java.util.List;
import javax.validation.constraints.NotNull;

public class CodeWatchUserAddRequest extends AbstractRequest {
    private static final long serialVersionUID = 1L;

    private Long series;

    @NotNull(message = "异常codeId不能为空")
    private String errorCodeId;

    @NotNull(message = "异常名称不能为空")
    private String errorName;

    @NotNull(message = "系统名称不能为空")
    private String systemName;

    @NotNull(message = "系统id不能为空")
    private Long systemId;

    private Integer noticeTimeInterval;

    @NotNull(message = "topicSeries/methodId不能为空")
    private Long topicSeries;

    @NotNull(message = "topic名称/方法名称不能为空")
    private String topicName;

    private String emailWatch;

    private String smsWatch;

    private List<String> users;

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }

    public String getErrorCodeId() {
        return this.errorCodeId;
    }

    public void setErrorCodeId(String errorCodeId) {
        this.errorCodeId = errorCodeId;
    }

    public String getErrorName() {
        return this.errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getSystemName() {
        return this.systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Long getSystemId() {
        return this.systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Integer getNoticeTimeInterval() {
        return this.noticeTimeInterval;
    }

    public void setNoticeTimeInterval(Integer noticeTimeInterval) {
        this.noticeTimeInterval = noticeTimeInterval;
    }

    public Long getTopicSeries() {
        return this.topicSeries;
    }

    public void setTopicSeries(Long topicSeries) {
        this.topicSeries = topicSeries;
    }

    public String getTopicName() {
        return this.topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getEmailWatch() {
        return this.emailWatch;
    }

    public void setEmailWatch(String emailWatch) {
        this.emailWatch = emailWatch;
    }

    public String getSmsWatch() {
        return this.smsWatch;
    }

    public void setSmsWatch(String smsWatch) {
        this.smsWatch = smsWatch;
    }

    public List<String> getUsers() {
        return this.users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}

