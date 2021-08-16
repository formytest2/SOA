package com.github.bluecatlee.gs4d.monitor.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.monitor.api.model.NoticeUserName;
import com.github.bluecatlee.gs4d.monitor.api.model.PlatformErrorCode;

import java.util.List;

public class MessageCodeUserFindResponse extends MessagePack {
    private static final long serialVersionUID = 1L;

    private List<NoticeUserName> noticeUser;

    private List<NoticeUserName> watchUser;

    private PlatformErrorCode errorCode;

    public List<NoticeUserName> getNoticeUser() {
        return this.noticeUser;
    }

    public void setNoticeUser(List<NoticeUserName> noticeUser) {
        this.noticeUser = noticeUser;
    }

    public List<NoticeUserName> getWatchUser() {
        return this.watchUser;
    }

    public void setWatchUser(List<NoticeUserName> watchUser) {
        this.watchUser = watchUser;
    }

    public PlatformErrorCode getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(PlatformErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
