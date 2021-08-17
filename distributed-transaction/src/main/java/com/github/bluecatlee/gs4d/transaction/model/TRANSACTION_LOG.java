package com.github.bluecatlee.gs4d.transaction.model;

import java.util.Date;

public class TRANSACTION_LOG {
    private Long TRANSACTION_ID;
    private Date START_DTME;
    private Date END_DTME;
    private String IP_ADDRESS;
    private Long TRANSACTION_STATE;
    private String TRANSACTION_SIGN;
    private String FROM_SYSTEM;
    private String METHOD_NAME;
    private String TRANSACTION_ROLLBACK_FLAG;
    private Date SysTime;

    public Long getTRANSACTION_ID() {
        return this.TRANSACTION_ID;
    }

    public void setTRANSACTION_ID(Long TRANSACTION_ID) {
        this.TRANSACTION_ID = TRANSACTION_ID;
    }

    public Date getSTART_DTME() {
        return this.START_DTME;
    }

    public void setSTART_DTME(Date START_DTME) {
        this.START_DTME = START_DTME;
    }

    public Date getEND_DTME() {
        return this.END_DTME;
    }

    public void setEND_DTME(Date END_DTME) {
        this.END_DTME = END_DTME;
    }

    public String getIP_ADDRESS() {
        return this.IP_ADDRESS;
    }

    public void setIP_ADDRESS(String IP_ADDRESS) {
        this.IP_ADDRESS = IP_ADDRESS;
    }

    public Long getTRANSACTION_STATE() {
        return this.TRANSACTION_STATE;
    }

    public void setTRANSACTION_STATE(Long TRANSACTION_STATE) {
        this.TRANSACTION_STATE = TRANSACTION_STATE;
    }

    public String getTRANSACTION_SIGN() {
        return this.TRANSACTION_SIGN;
    }

    public void setTRANSACTION_SIGN(String TRANSACTION_SIGN) {
        this.TRANSACTION_SIGN = TRANSACTION_SIGN;
    }

    public String getFROM_SYSTEM() {
        return this.FROM_SYSTEM;
    }

    public void setFROM_SYSTEM(String FROM_SYSTEM) {
        this.FROM_SYSTEM = FROM_SYSTEM;
    }

    public String getMETHOD_NAME() {
        return this.METHOD_NAME;
    }

    public void setMETHOD_NAME(String METHOD_NAME) {
        this.METHOD_NAME = METHOD_NAME;
    }

    public String getTRANSACTION_ROLLBACK_FLAG() {
        return this.TRANSACTION_ROLLBACK_FLAG;
    }

    public void setTRANSACTION_ROLLBACK_FLAG(String TRANSACTION_ROLLBACK_FLAG) {
        this.TRANSACTION_ROLLBACK_FLAG = TRANSACTION_ROLLBACK_FLAG;
    }

    public Date getSysTime() {
        return this.SysTime;
    }

    public void setSysTime(Date SysTime) {
        this.SysTime = SysTime;
    }
}
