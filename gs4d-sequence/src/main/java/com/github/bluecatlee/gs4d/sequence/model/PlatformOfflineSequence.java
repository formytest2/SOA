package com.github.bluecatlee.gs4d.sequence.model;

import java.io.Serializable;
import java.util.Date;

public class PlatformOfflineSequence implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long series;

    private String seqName;

    private Long onlineStartNum;

    private Long onlineEndNum;

    private Long offlineEndNum;

    private Long offlineCurrentNum;

    private Long offlineGetNumCount;

    private Date createDtme;

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }

    public String getSeqName() {
        return this.seqName;
    }

    public void setSeqName(String seqName) {
        this.seqName = seqName;
    }

    public Long getOnlineStartNum() {
        return this.onlineStartNum;
    }

    public void setOnlineStartNum(Long onlineStartNum) {
        this.onlineStartNum = onlineStartNum;
    }

    public Long getOnlineEndNum() {
        return this.onlineEndNum;
    }

    public void setOnlineEndNum(Long onlineEndNum) {
        this.onlineEndNum = onlineEndNum;
    }

    public Long getOfflineEndNum() {
        return this.offlineEndNum;
    }

    public void setOfflineEndNum(Long offlineEndNum) {
        this.offlineEndNum = offlineEndNum;
    }

    public Long getOfflineCurrentNum() {
        return this.offlineCurrentNum;
    }

    public void setOfflineCurrentNum(Long offlineCurrentNum) {
        this.offlineCurrentNum = offlineCurrentNum;
    }

    public Long getOfflineGetNumCount() {
        return this.offlineGetNumCount;
    }

    public void setOfflineGetNumCount(Long offlineGetNumCount) {
        this.offlineGetNumCount = offlineGetNumCount;
    }

    public Date getCreateDtme() {
        return this.createDtme;
    }

    public void setCreateDtme(Date createDtme) {
        this.createDtme = createDtme;
    }
}
