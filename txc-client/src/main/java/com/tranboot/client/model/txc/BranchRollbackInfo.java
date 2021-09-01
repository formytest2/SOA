package com.tranboot.client.model.txc;

import com.github.bluecatlee.gs4d.transaction.api.model.SqlParamModel;

import java.io.Serializable;
import java.util.List;

public class BranchRollbackInfo implements Serializable {
    private static final long serialVersionUID = 5006330756385094572L;
    private List<SqlParamModel> rollbackSql;

    public BranchRollbackInfo() {
    }

    public List<SqlParamModel> getRollbackSql() {
        return this.rollbackSql;
    }

    public void setRollbackSql(List<SqlParamModel> rollbackSql) {
        this.rollbackSql = rollbackSql;
    }
}
