package com.tranboot.client.model.dbsync;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.tranboot.client.sqlast.MySQLRewriteVistorAop;
import com.tranboot.client.sqlast.OracleRewriteVistorAop;
import com.tranboot.client.sqlast.SQLASTVisitorAspectAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SqlTransformResult {
    public static final SqlTransformResult a = new SqlTransformResult((SQLStatement)null, (SqlParser)null);
    public static final SqlTransformResult b = new SqlTransformResult((SQLStatement)null, (SqlParser)null);
    private final SQLStatement statement;
    private final SqlParser sqlparser;
    private Integer targetPrimaryValueIndex;
    private Integer sourcePartitionValueIndex;
    private Integer targetPartitionValueIndex;
    private Object targetPrimaryValue;
    private Object sourcePartitionValue;
    private Object targetPartitionValue;
    private boolean needSplit = false;
    private Map<String, SQLVariantRefExpr> updateItemsIndex;
    private Map<String, SQLVariantRefExpr> conditionIndex;

    public SqlTransformResult(SQLStatement statement, SqlParser sqlparser) {
        this.statement = statement;
        this.sqlparser = sqlparser;
    }

    public String[] primaryKeyValue(Object[] args) {
        try {
            if (!this.needSplit) {
                if (this.targetPrimaryValue != null) {
                    return new String[]{this.targetPrimaryValue.toString()};
                } else if (this.targetPrimaryValueIndex == null) {
                    throw new RuntimeException("没有业务主键，无法进行数据双写操作");
                } else {
                    Object keyValue = args[this.targetPrimaryValueIndex];
                    return new String[]{keyValue.toString()};
                }
            } else if (this.targetPrimaryValue == null) {
                return this.targetPrimaryValueIndex == null ? null : args[this.targetPrimaryValueIndex].toString().split(",");
            } else {
                List<Object> values = (List)this.targetPrimaryValue;
                String[] d = new String[values.size()];

                for(int i = 0; i < values.size(); ++i) {
                    d[i] = values.get(i).toString();
                }

                return d;
            }
        } catch (Exception var5) {
            throw var5;
        }
    }

    public String sourcePartitionValue(Object[] args) {
        try {
            return this.sourcePartitionValue != null ? this.sourcePartitionValue.toString() : args[this.sourcePartitionValueIndex].toString();
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public String targetPartitionValue(Object[] args) {
        try {
            return this.targetPartitionValue != null ? this.targetPartitionValue.toString() : args[this.targetPartitionValueIndex].toString();
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public String sql(Object[] args) {
        StringBuilder sbuilder = new StringBuilder();
        if ("mysql".equals(this.getTargetDbType())) {
            MySQLRewriteVistorAop visitor = new MySQLRewriteVistorAop((List)(args == null ? new ArrayList() : Arrays.asList(args)), sbuilder, new SQLASTVisitorAspectAdapter() {
                public SQLExprTableSource tableAspect(SQLExprTableSource table) {
                    return table;
                }
            });
            this.statement.accept(visitor);
        } else if ("oracle".equals(this.getTargetDbType())) {
            OracleRewriteVistorAop visitor = new OracleRewriteVistorAop((List)(args == null ? new ArrayList() : Arrays.asList(args)), sbuilder, new SQLASTVisitorAspectAdapter() {
                public SQLExprTableSource tableAspect(SQLExprTableSource table) {
                    return table;
                }
            });
            this.statement.accept(visitor);
        }

        return sbuilder.toString();
    }

    public Map<String, SQLVariantRefExpr> getUpdateItemsIndex() {
        return this.updateItemsIndex;
    }

    public void setUpdateItemsIndex(Map<String, SQLVariantRefExpr> updateItemsIndex) {
        this.updateItemsIndex = updateItemsIndex;
    }

    public Map<String, SQLVariantRefExpr> getConditionIndex() {
        return this.conditionIndex;
    }

    public void setConditionIndex(Map<String, SQLVariantRefExpr> conditionIndex) {
        this.conditionIndex = conditionIndex;
    }

    public SQLStatement getStatement() {
        return this.statement;
    }

    public String getPrimaryKey() {
        return this.sqlparser.getMapper().getTargetKeyField();
    }

    public String getSourceTable() {
        return this.sqlparser.getMapper().getSourceTable();
    }

    public String getTargetTable() {
        return this.sqlparser.getMapper().getTargetTable();
    }

    public String getTargetDbType() {
        return this.sqlparser.getMapper().getTargetDbType();
    }

    public String getTargetDb() {
        return this.sqlparser.getMapper().getTargetDb();
    }

    public String getSourceDb() {
        return this.sqlparser.getMapper().getSourceDb();
    }

    public String getSourceDbType() {
        return this.sqlparser.getMapper().getSourceDbType();
    }

    public String getTargetPartitionKey() {
        return this.sqlparser.getRouter().getTargetPartitionKey();
    }

    public String getSourcePartitionKey() {
        return this.sqlparser.getRouter().getSourcePartitionKey();
    }

    public void setTargetPrimaryValueIndex(Integer targetPrimaryValueIndex) {
        this.targetPrimaryValueIndex = targetPrimaryValueIndex;
    }

    public void setSourcePartitionValueIndex(Integer sourcePartitionValueIndex) {
        this.sourcePartitionValueIndex = sourcePartitionValueIndex;
    }

    public void setTargetPartitionValueIndex(Integer targetPartitionValueIndex) {
        this.targetPartitionValueIndex = targetPartitionValueIndex;
    }

    public void setTargetPrimaryValue(Object targetPrimaryValue) {
        this.targetPrimaryValue = targetPrimaryValue;
    }

    public void setSourcePartitionValue(Object sourcePartitionValue) {
        this.sourcePartitionValue = sourcePartitionValue;
    }

    public void setTargetPartitionValue(Object targetPartitionValue) {
        this.targetPartitionValue = targetPartitionValue;
    }

    public boolean isNeedSplit() {
        return this.needSplit;
    }

    public void setNeedSplit(boolean needSplit) {
        this.needSplit = needSplit;
    }
}

