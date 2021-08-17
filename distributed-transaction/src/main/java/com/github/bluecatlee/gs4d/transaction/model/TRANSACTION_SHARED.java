package com.github.bluecatlee.gs4d.transaction.model;

public class TRANSACTION_SHARED {
    private Long SERIES;
    private String DB_NAME;
    private String TABLE_NAME;
    private String SHARED_COLUMN;


    public Long getSERIES() {
        return this.SERIES;
    }

    public void setSERIES(Long SERIES) {
        this.SERIES = SERIES;
    }

    public String getDB_NAME() {
        return this.DB_NAME;
    }

    public void setDB_NAME(String DB_NAME) {
        this.DB_NAME = DB_NAME;
    }

    public String getTABLE_NAME() {
        return this.TABLE_NAME;
    }

    public void setTABLE_NAME(String TABLE_NAME) {
        this.TABLE_NAME = TABLE_NAME;
    }

    public String getSHARED_COLUMN() {
        return this.SHARED_COLUMN;
    }

    public void setSHARED_COLUMN(String SHARED_COLUMN) {
        this.SHARED_COLUMN = SHARED_COLUMN;
    }
}

