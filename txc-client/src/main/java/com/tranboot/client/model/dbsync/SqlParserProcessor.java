package com.tranboot.client.model.dbsync;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitorAdapter;
import com.tranboot.client.sqlast.MySQLRewriteVistorAop;
import com.tranboot.client.sqlast.OracleRewriteVistorAop;
import com.tranboot.client.sqlast.SQLASTVisitorAspectAdapter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SqlParserProcessor {
    SQLStatement statement;
    private Integer targetPrimaryValueIndex;
    private Integer sourcePartitionValueIndex;
    private Integer targetPartitionValueIndex;
    private boolean needSplit = false;
    private Object targetPrimaryValue;
    private Object sourcePartitionValue;
    private Object targetPartitionValue;
    private Map<String, SQLVariantRefExpr> updateItemsIndex;
    private Map<String, SQLVariantRefExpr> conditionIndex;
    private SqlParser sqlParser;

    public SqlParserProcessor(SQLStatement statement, SqlParser parser) {
        this.statement = statement;
        this.sqlParser = parser;
    }

    public SqlTransformResult parse() {
        SQLASTVisitorAdapter rewriter = null;
        SqlTransformResult result;
        if ("mysql".equals(this.dbType())) {
            rewriter = new MySQLRewriteVistorAop(new SqlParserProcessor.SQLASTVisitorAspectAdvice());
        } else {
            if (!"oracle".equals(this.dbType())) {
                result = new SqlTransformResult(this.statement, this.sqlParser);
                return result;
            }

            rewriter = new OracleRewriteVistorAop(new SqlParserProcessor.SQLASTVisitorAspectAdvice());
        }

        this.statement.accept((SQLASTVisitor)rewriter);
        if (this.targetPrimaryValue == null && this.targetPrimaryValueIndex == null) {
            return SqlTransformResult.b;
        } else {
            result = new SqlTransformResult(this.statement, this.sqlParser);
            result.setConditionIndex(this.conditionIndex);
            result.setUpdateItemsIndex(this.updateItemsIndex);
            result.setTargetPrimaryValueIndex(this.targetPrimaryValueIndex);
            result.setSourcePartitionValueIndex(this.sourcePartitionValueIndex);
            result.setTargetPartitionValueIndex(this.targetPartitionValueIndex);
            result.setTargetPrimaryValue(this.targetPrimaryValue);
            result.setTargetPartitionValue(this.targetPartitionValue);
            result.setSourcePartitionValue(this.sourcePartitionValue);
            result.setNeedSplit(this.needSplit);
            return result;
        }
    }

    public String dbType() {
        return this.sqlParser.getMapper().getSourceDbType();
    }

    class SQLASTVisitorAspectAdvice extends SQLASTVisitorAspectAdapter {
        SQLASTVisitorAspectAdvice() {
        }

        public SQLExprTableSource tableAspect(SQLExprTableSource table) {
            SQLIdentifierExpr newTable = new SQLIdentifierExpr(SqlParserProcessor.this.sqlParser.getMapper().getTargetTable());
            table.setExpr(newTable);
            return table;
        }

        public SQLUpdateSetItem updateItemAspect(SQLUpdateSetItem updateItem) {
            if (SqlParserProcessor.this.updateItemsIndex == null) {
                SqlParserProcessor.this.updateItemsIndex = new HashMap();
            }

            String fieldName = null;
            if (updateItem.getColumn() instanceof SQLIdentifierExpr) {
                fieldName = ((SQLIdentifierExpr)updateItem.getColumn()).getName();
            }

            if (updateItem.getColumn() instanceof SQLPropertyExpr) {
                fieldName = ((SQLPropertyExpr)updateItem.getColumn()).getName();
            }

            if (fieldName == null) {
                return null;
            } else {
                String targetField = SqlParserProcessor.this.sqlParser.getMapper().mapField(fieldName);
                if (updateItem.getColumn() instanceof SQLIdentifierExpr) {
                    ((SQLIdentifierExpr)updateItem.getColumn()).setName(targetField);
                }

                if (updateItem.getColumn() instanceof SQLPropertyExpr) {
                    ((SQLPropertyExpr)updateItem.getColumn()).setName(targetField);
                    ((SQLPropertyExpr)updateItem.getColumn()).setParent(updateItem);
                }

                if (updateItem.getValue() instanceof SQLBinaryOpExpr) {
                    SQLBinaryOpExpr value = (SQLBinaryOpExpr)updateItem.getValue();
                    String rightField;
                    if (value.getLeft() instanceof SQLIdentifierExpr) {
                        rightField = ((SQLIdentifierExpr)value.getLeft()).getName();
                        ((SQLIdentifierExpr)value.getLeft()).setName(SqlParserProcessor.this.sqlParser.getMapper().mapField(rightField));
                    } else if (value.getLeft() instanceof SQLPropertyExpr) {
                        rightField = ((SQLPropertyExpr)value.getLeft()).getName();
                        ((SQLPropertyExpr)value.getLeft()).setName(SqlParserProcessor.this.sqlParser.getMapper().mapField(rightField));
                    }
                }

                if (updateItem.getValue() instanceof SQLVariantRefExpr) {
                    SqlParserProcessor.this.updateItemsIndex.put(fieldName, (SQLVariantRefExpr)updateItem.getValue());
                }

                return updateItem;
            }
        }

        public String insertColumnAspect(String columnName, SQLInsertStatement parent) {
            return SqlParserProcessor.this.sqlParser.getMapper().mapField(columnName);
        }

        public String updateColumnAspect(String columnName) {
            return SqlParserProcessor.this.sqlParser.getMapper().mapField(columnName);
        }

        public String whereColumnAspect(String columnName, SQLVariantRefExpr right) {
            if (SqlParserProcessor.this.conditionIndex == null) {
                SqlParserProcessor.this.conditionIndex = new HashMap();
            }

            SqlParserProcessor.this.conditionIndex.put(columnName, right);
            if (columnName.toLowerCase().equals(SqlParserProcessor.this.sqlParser.getMapper().getSourceKeyField())) {
                SqlParserProcessor.this.targetPrimaryValueIndex = right.getIndex();
            }

            if (columnName.toLowerCase().equals(SqlParserProcessor.this.sqlParser.getRouter().getSourcePartitionKey())) {
                SqlParserProcessor.this.sourcePartitionValueIndex = right.getIndex();
            }

            if (columnName.toLowerCase().equals(SqlParserProcessor.this.sqlParser.getRouter().getTargetPartitionKey())) {
                SqlParserProcessor.this.targetPartitionValueIndex = right.getIndex();
            }

            return SqlParserProcessor.this.sqlParser.getMapper().mapField(columnName);
        }

        public void insertEnterPoint(SQLInsertStatement insertStatement) {
            List<SQLExpr> columns = insertStatement.getColumns();
            Iterator<SQLExpr> columnIterator = columns.iterator();

            for(int i = 0; columnIterator.hasNext(); ++i) {
                SQLIdentifierExpr _column = (SQLIdentifierExpr)columnIterator.next();
                SQLInsertStatement.ValuesClause values;
                if (SqlParserProcessor.this.sqlParser.getMapper().needExclude(_column.getName())) {
                    columnIterator.remove();
                    values = insertStatement.getValues();
                    values.getValues().remove(i);
                } else {
                    SQLExpr value;
                    if (_column.getName().toLowerCase().equals(SqlParserProcessor.this.sqlParser.getMapper().getSourceKeyField())) {
                        values = insertStatement.getValues();
                        value = (SQLExpr)values.getValues().get(i);
                        if (value instanceof SQLVariantRefExpr) {
                            SqlParserProcessor.this.targetPrimaryValueIndex = ((SQLVariantRefExpr)value).getIndex();
                        } else if (value instanceof SQLLiteralExpr) {
                            SqlParserProcessor.this.targetPrimaryValue = (SQLLiteralExpr)value;
                        } else {
                            SqlParserProcessor.this.targetPrimaryValueIndex = i;
                        }
                    }

                    if (_column.getName().toLowerCase().equals(SqlParserProcessor.this.sqlParser.getRouter().getSourcePartitionKey())) {
                        values = insertStatement.getValues();
                        value = (SQLExpr)values.getValues().get(i);
                        if (value instanceof SQLVariantRefExpr) {
                            SqlParserProcessor.this.sourcePartitionValueIndex = ((SQLVariantRefExpr)value).getIndex();
                        } else if (value instanceof SQLLiteralExpr) {
                            SqlParserProcessor.this.sourcePartitionValue = (SQLLiteralExpr)value;
                        } else {
                            SqlParserProcessor.this.sourcePartitionValueIndex = i;
                        }
                    }

                    if (_column.getName().toLowerCase().equals(SqlParserProcessor.this.sqlParser.getRouter().getTargetPartitionKey())) {
                        values = insertStatement.getValues();
                        value = (SQLExpr)values.getValues().get(i);
                        if (value instanceof SQLVariantRefExpr) {
                            SqlParserProcessor.this.targetPartitionValueIndex = ((SQLVariantRefExpr)value).getIndex();
                        } else if (value instanceof SQLLiteralExpr) {
                            SqlParserProcessor.this.targetPartitionValue = (SQLLiteralExpr)value;
                        } else {
                            SqlParserProcessor.this.targetPartitionValueIndex = i;
                        }
                    }
                }
            }

        }

        public void updateEnterPoint(SQLUpdateStatement updateStatement) {
            List<SQLUpdateSetItem> setItems = updateStatement.getItems();
            Iterator iterator = setItems.iterator();

            while(iterator.hasNext()) {
                SQLUpdateSetItem item = (SQLUpdateSetItem)iterator.next();
                String fieldName = null;
                if (item.getColumn() instanceof SQLIdentifierExpr) {
                    fieldName = ((SQLIdentifierExpr)item.getColumn()).getName();
                }

                if (item.getColumn() instanceof SQLPropertyExpr) {
                    fieldName = ((SQLPropertyExpr)item.getColumn()).getName();
                }

                if (SqlParserProcessor.this.sqlParser.getMapper().needExclude(fieldName)) {
                    iterator.remove();
                }
            }

        }

        public String whereColumnAspect(String columnName, SQLLiteralExpr right) {
            if (columnName.toLowerCase().equals(SqlParserProcessor.this.sqlParser.getMapper().getSourceKeyField())) {
                SqlParserProcessor.this.targetPrimaryValue = right;
            }

            if (columnName.toLowerCase().equals(SqlParserProcessor.this.sqlParser.getRouter().getSourcePartitionKey())) {
                SqlParserProcessor.this.sourcePartitionValue = right;
            }

            if (columnName.toLowerCase().equals(SqlParserProcessor.this.sqlParser.getRouter().getTargetPartitionKey())) {
                SqlParserProcessor.this.targetPartitionValue = right;
            }

            return SqlParserProcessor.this.sqlParser.getMapper().mapField(columnName);
        }

        public String whereColumnAspect(String columnName, SQLInListExpr in) {
            if (columnName.toLowerCase().equals(SqlParserProcessor.this.sqlParser.getMapper().getTargetKeyField())) {
                SqlParserProcessor.this.needSplit = true;
                List<SQLExpr> exprs = in.getTargetList();
                if (exprs.size() == 1 && exprs.get(0) instanceof SQLVariantRefExpr) {
                    SqlParserProcessor.this.targetPrimaryValueIndex = ((SQLVariantRefExpr)exprs.get(0)).getIndex();
                } else {
                    SqlParserProcessor.this.targetPrimaryValue = exprs;
                }
            }

            return SqlParserProcessor.this.sqlParser.getMapper().mapField(columnName);
        }
    }
}

