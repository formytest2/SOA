package com.github.bluecatlee.gs4d.transaction.model;

import java.io.Serializable;
import java.util.List;

public class TableKeyValueListModel implements Serializable {
    private List<TableKeyValueModel> tableKeyValueModel;

    public List<TableKeyValueModel> getTableKeyValueModel() {
        return this.tableKeyValueModel;
    }

    public void setTableKeyValueModel(List<TableKeyValueModel> tableKeyValueModel) {
        this.tableKeyValueModel = tableKeyValueModel;
    }

    public String toString() {
        return "TableKeyValueListModel [tableKeyValueModel=" + this.tableKeyValueModel + "]";
    }
}

