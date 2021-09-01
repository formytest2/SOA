package com.tranboot.client.druid.sql.visitor;

import com.tranboot.client.druid.sql.ast.SQLCommentHint;
import com.tranboot.client.druid.sql.ast.SQLDataType;
import com.tranboot.client.druid.sql.ast.SQLDeclareItem;
import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLKeep;
import com.tranboot.client.druid.sql.ast.SQLLimit;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLOrderBy;
import com.tranboot.client.druid.sql.ast.SQLOrderingSpecification;
import com.tranboot.client.druid.sql.ast.SQLOver;
import com.tranboot.client.druid.sql.ast.SQLParameter;
import com.tranboot.client.druid.sql.ast.SQLPartition;
import com.tranboot.client.druid.sql.ast.SQLPartitionBy;
import com.tranboot.client.druid.sql.ast.SQLPartitionByHash;
import com.tranboot.client.druid.sql.ast.SQLPartitionByList;
import com.tranboot.client.druid.sql.ast.SQLPartitionByRange;
import com.tranboot.client.druid.sql.ast.SQLPartitionValue;
import com.tranboot.client.druid.sql.ast.SQLStatement;
import com.tranboot.client.druid.sql.ast.SQLSubPartition;
import com.tranboot.client.druid.sql.ast.SQLSubPartitionByHash;
import com.tranboot.client.druid.sql.ast.SQLSubPartitionByList;
import com.tranboot.client.druid.sql.ast.SQLKeep.DenseRank;
import com.tranboot.client.druid.sql.ast.SQLOver.WindowingType;
import com.tranboot.client.druid.sql.ast.SQLParameter.ParameterType;
import com.tranboot.client.druid.sql.ast.SQLPartitionValue.Operator;
import com.tranboot.client.druid.sql.ast.expr.SQLAggregateExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLAllColumnExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLAllExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLAnyExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLArrayExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBetweenExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOperator;
import com.tranboot.client.druid.sql.ast.expr.SQLBooleanExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCaseExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCastExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCharExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCurrentOfCursorExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLDateExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLDefaultExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLExistsExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLGroupingSetExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLHexExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLInListExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLInSubQueryExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLIntegerExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLListExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNCharExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNotExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNullExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNumberExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLPropertyExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLQueryExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLSequenceExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLSomeExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLTimestampExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLUnaryExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLVariantRefExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCaseExpr.Item;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterDatabaseStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableAddColumn;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableAddConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableAddIndex;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableAddPartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableAlterColumn;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableAnalyzePartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableCheckPartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableCoalescePartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableConvertCharSet;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDisableConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDisableKeys;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDisableLifecycle;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDiscardPartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDropColumnItem;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDropConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDropForeignKey;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDropIndex;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDropKey;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDropPartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDropPrimaryKey;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableEnableConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableEnableKeys;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableEnableLifecycle;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableImportPartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableItem;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableOptimizePartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableReOrganizePartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableRebuildPartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableRename;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableRenameColumn;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableRenamePartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableRepairPartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableSetComment;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableSetLifecycle;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableTouch;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableTruncatePartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterViewRenameStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLAssignItem;
import com.tranboot.client.druid.sql.ast.statement.SQLCallStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCharacterDataType;
import com.tranboot.client.druid.sql.ast.statement.SQLCheck;
import com.tranboot.client.druid.sql.ast.statement.SQLCloseStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnCheck;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnDefinition;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnPrimaryKey;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnReference;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnUniqueKey;
import com.tranboot.client.druid.sql.ast.statement.SQLCommentStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateDatabaseStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateSequenceStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateTableStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateTriggerStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateViewStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDeleteStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropDatabaseStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropFunctionStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropIndexStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropProcedureStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropSequenceStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropTableSpaceStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropTableStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropTriggerStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropUserStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropViewStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLErrorLoggingClause;
import com.tranboot.client.druid.sql.ast.statement.SQLExplainStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLExprHint;
import com.tranboot.client.druid.sql.ast.statement.SQLExprTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLFetchStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLForeignKeyConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.tranboot.client.druid.sql.ast.statement.SQLGrantStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLIfStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLInsertStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLJoinTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLLoopStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLMergeStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLNotNullConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLNullConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLObjectType;
import com.tranboot.client.druid.sql.ast.statement.SQLOpenStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLPrimaryKey;
import com.tranboot.client.druid.sql.ast.statement.SQLPrimaryKeyImpl;
import com.tranboot.client.druid.sql.ast.statement.SQLReleaseSavePointStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLRevokeStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLRollbackStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLSavePointStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLSelect;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectItem;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLSetStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLShowTablesStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLTableElement;
import com.tranboot.client.druid.sql.ast.statement.SQLTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLTruncateStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLUnionQuery;
import com.tranboot.client.druid.sql.ast.statement.SQLUnionQueryTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLUnique;
import com.tranboot.client.druid.sql.ast.statement.SQLUniqueConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateSetItem;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLUseStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnDefinition.Identity;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateTableStatement.Type;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateTriggerStatement.TriggerEvent;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateTriggerStatement.TriggerType;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateViewStatement.Column;
import com.tranboot.client.druid.sql.ast.statement.SQLIfStatement.Else;
import com.tranboot.client.druid.sql.ast.statement.SQLIfStatement.ElseIf;
import com.tranboot.client.druid.sql.ast.statement.SQLInsertStatement.ValuesClause;
import com.tranboot.client.druid.sql.ast.statement.SQLJoinTableSource.JoinType;
import com.tranboot.client.druid.sql.ast.statement.SQLMergeStatement.MergeInsertClause;
import com.tranboot.client.druid.sql.ast.statement.SQLMergeStatement.MergeUpdateClause;
import com.tranboot.client.druid.sql.ast.statement.SQLWithSubqueryClause.Entry;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SQLASTOutputVisitor extends SQLASTVisitorAdapter implements ParameterizedVisitor, PrintableVisitor {
    protected final Appendable appender;
    private String indent = "\t";
    private int indentCount = 0;
    private boolean prettyFormat = true;
    protected boolean ucase = true;
    protected int selectListNumberOfLine = 5;
    protected boolean groupItemSingleLine = false;
    protected List<Object> parameters;
    protected Set<String> tables;
    protected boolean exportTables = false;
    protected String dbType;
    protected Map<String, String> tableMapping;
    protected int replaceCount;
    protected boolean parameterized = false;
    protected boolean parameterizedMergeInList = false;
    protected boolean shardingSupport = false;

    public SQLASTOutputVisitor(Appendable appender) {
        this.appender = appender;
    }

    public SQLASTOutputVisitor(Appendable appender, String dbType) {
        this.appender = appender;
        this.dbType = dbType;
    }

    public SQLASTOutputVisitor(Appendable appender, boolean parameterized) {
        this.appender = appender;
        this.parameterized = parameterized;
    }

    public int getParametersSize() {
        return this.parameters == null ? 0 : this.parameters.size();
    }

    public int getReplaceCount() {
        return this.replaceCount;
    }

    public void incrementReplaceCunt() {
        ++this.replaceCount;
    }

    public void addTableMapping(String srcTable, String destTable) {
        if (this.tableMapping == null) {
            this.tableMapping = new HashMap();
        }

        this.tableMapping.put(srcTable, destTable);
    }

    public void setTableMapping(Map<String, String> tableMapping) {
        this.tableMapping = tableMapping;
    }

    public List<Object> getParameters() {
        if (this.parameters == null) {
            this.parameters = new ArrayList();
        }

        return this.parameters;
    }

    public Set<String> getTables() {
        return this.tables;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    public int getIndentCount() {
        return this.indentCount;
    }

    public Appendable getAppender() {
        return this.appender;
    }

    public boolean isPrettyFormat() {
        return this.prettyFormat;
    }

    public void setPrettyFormat(boolean prettyFormat) {
        this.prettyFormat = prettyFormat;
    }

    public void decrementIndent() {
        --this.indentCount;
    }

    public void incrementIndent() {
        ++this.indentCount;
    }

    public boolean isParameterized() {
        return this.parameterized;
    }

    public void setParameterized(boolean parameterized) {
        this.parameterized = parameterized;
    }

    public boolean isParameterizedMergeInList() {
        return this.parameterizedMergeInList;
    }

    public void setParameterizedMergeInList(boolean parameterizedMergeInList) {
        this.parameterizedMergeInList = parameterizedMergeInList;
    }

    public boolean isExportTables() {
        return this.exportTables;
    }

    public void setExportTables(boolean exportTables) {
        this.exportTables = exportTables;
    }

    public void print(char value) {
        if (this.appender != null) {
            try {
                this.appender.append(value);
            } catch (IOException var3) {
                throw new RuntimeException("println error", var3);
            }
        }
    }

    public void print(int value) {
        if (this.appender != null) {
            this.print0(Integer.toString(value));
        }
    }

    public void print(Date date) {
        if (this.appender != null) {
            SimpleDateFormat dateFormat;
            if (date instanceof Timestamp) {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            } else {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            }

            this.print0("'" + dateFormat.format(date) + "'");
        }
    }

    public void print(long value) {
        if (this.appender != null) {
            this.print0(Long.toString(value));
        }
    }

    public void print(String text) {
        if (this.appender != null) {
            this.print0(text);
        }
    }

    protected void print0(String text) {
        if (this.appender != null) {
            try {
                this.appender.append(text);
            } catch (IOException var3) {
                throw new RuntimeException("println error", var3);
            }
        }
    }

    protected void printAlias(String alias) {
        if (alias != null && alias.length() > 0) {
            this.print(' ');
            this.print0(alias);
        }

    }

    protected void printAndAccept(List<? extends SQLObject> nodes, String seperator) {
        int i = 0;

        for(int size = nodes.size(); i < size; ++i) {
            if (i != 0) {
                this.print0(seperator);
            }

            ((SQLObject)nodes.get(i)).accept(this);
        }

    }

    protected void printSelectList(List<SQLSelectItem> selectList) {
        this.incrementIndent();
        int i = 0;

        for(int size = selectList.size(); i < size; ++i) {
            if (i != 0) {
                if (i % this.selectListNumberOfLine == 0) {
                    this.println();
                }

                this.print0(", ");
            }

            ((SQLSelectItem)selectList.get(i)).accept(this);
        }

        this.decrementIndent();
    }

    protected void printlnAndAccept(List<? extends SQLObject> nodes, String seperator) {
        int i = 0;

        for(int size = nodes.size(); i < size; ++i) {
            if (i != 0) {
                this.println(seperator);
            }

            ((SQLObject)nodes.get(i)).accept(this);
        }

    }

    public void printIndent() {
        for(int i = 0; i < this.indentCount; ++i) {
            this.print0(this.indent);
        }

    }

    public void println() {
        if (!this.isPrettyFormat()) {
            this.print(' ');
        } else {
            this.print0("\n");
            this.printIndent();
        }
    }

    public void println(String text) {
        this.print(text);
        this.println();
    }

    protected void println0(String text) {
        this.print0(text);
        this.println();
    }

    public boolean visit(SQLBetweenExpr x) {
        x.getTestExpr().accept(this);
        if (x.isNot()) {
            this.print0(this.ucase ? " NOT BETWEEN " : " not between ");
        } else {
            this.print0(this.ucase ? " BETWEEN " : " between ");
        }

        x.getBeginExpr().accept(this);
        this.print0(this.ucase ? " AND " : " and ");
        x.getEndExpr().accept(this);
        return false;
    }

    public boolean visit(SQLBinaryOpExpr x) {
        if (this.parameterized) {
            x = ParameterizedOutputVisitorUtils.merge(this, x);
        }

        if (this.parameters != null && this.parameters.size() > 0 && x.getOperator() == SQLBinaryOperator.Equality && x.getRight() instanceof SQLVariantRefExpr) {
            SQLVariantRefExpr right = (SQLVariantRefExpr)x.getRight();
            int index = right.getIndex();
            if (index >= 0 && index < this.parameters.size()) {
                Object param = this.parameters.get(index);
                if (param instanceof Collection) {
                    x.getLeft().accept(this);
                    this.print0(" IN (");
                    right.accept(this);
                    this.print(')');
                    return false;
                }
            }
        }

        SQLObject parent = x.getParent();
        boolean isRoot = parent instanceof SQLSelectQueryBlock;
        boolean relational = x.getOperator() == SQLBinaryOperator.BooleanAnd || x.getOperator() == SQLBinaryOperator.BooleanOr;
        if (isRoot && relational) {
            this.incrementIndent();
        }

        List<SQLExpr> groupList = new ArrayList();

        SQLExpr left;
        SQLBinaryOpExpr binaryLeft;
        for(left = x.getLeft(); left instanceof SQLBinaryOpExpr && ((SQLBinaryOpExpr)left).getOperator() == x.getOperator(); left = binaryLeft.getLeft()) {
            binaryLeft = (SQLBinaryOpExpr)left;
            groupList.add(binaryLeft.getRight());
        }

        groupList.add(left);

        for(int i = groupList.size() - 1; i >= 0; --i) {
            SQLExpr item = (SQLExpr)groupList.get(i);
            if (relational && this.isPrettyFormat() && item.hasBeforeComment()) {
                this.printlnComments(item.getBeforeCommentsDirect());
            }

            if (this.isPrettyFormat() && item.hasBeforeComment()) {
                this.printlnComments(item.getBeforeCommentsDirect());
            }

            this.visitBinaryLeft(item, x.getOperator());
            if (this.isPrettyFormat() && item.hasAfterComment()) {
                this.print(' ');
                this.printComment(item.getAfterCommentsDirect(), "\n");
            }

            if (i != groupList.size() - 1 && this.isPrettyFormat() && item.getParent().hasAfterComment()) {
                this.print(' ');
                this.printComment(item.getParent().getAfterCommentsDirect(), "\n");
            }

            if (relational) {
                this.println();
            } else {
                this.print0(" ");
            }

            this.printOperator(x.getOperator());
            this.print0(" ");
        }

        this.visitorBinaryRight(x);
        if (isRoot && relational) {
            this.decrementIndent();
        }

        return false;
    }

    protected void printOperator(SQLBinaryOperator operator) {
        this.print0(this.ucase ? operator.name : operator.name_lcase);
    }

    private void visitorBinaryRight(SQLBinaryOpExpr x) {
        if (this.isPrettyFormat() && x.getRight().hasBeforeComment()) {
            this.printlnComments(x.getRight().getBeforeCommentsDirect());
        }

        if (x.getRight() instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr right = (SQLBinaryOpExpr)x.getRight();
            boolean rightRational = right.getOperator() == SQLBinaryOperator.BooleanAnd || right.getOperator() == SQLBinaryOperator.BooleanOr;
            if (right.getOperator().priority >= x.getOperator().priority) {
                if (rightRational) {
                    this.incrementIndent();
                }

                this.print('(');
                right.accept(this);
                this.print(')');
                if (rightRational) {
                    this.decrementIndent();
                }
            } else {
                right.accept(this);
            }
        } else {
            x.getRight().accept(this);
        }

        if (x.getRight().hasAfterComment() && this.isPrettyFormat()) {
            this.print(' ');
            this.printlnComments(x.getRight().getAfterCommentsDirect());
        }

    }

    private void visitBinaryLeft(SQLExpr left, SQLBinaryOperator op) {
        if (left instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr binaryLeft = (SQLBinaryOpExpr)left;
            boolean leftRational = binaryLeft.getOperator() == SQLBinaryOperator.BooleanAnd || binaryLeft.getOperator() == SQLBinaryOperator.BooleanOr;
            if (binaryLeft.getOperator().priority > op.priority) {
                if (leftRational) {
                    this.incrementIndent();
                }

                this.print('(');
                left.accept(this);
                this.print(')');
                if (leftRational) {
                    this.decrementIndent();
                }
            } else {
                left.accept(this);
            }
        } else {
            left.accept(this);
        }

    }

    public boolean visit(SQLCaseExpr x) {
        this.print0(this.ucase ? "CASE " : "case ");
        if (x.getValueExpr() != null) {
            x.getValueExpr().accept(this);
            this.print0(" ");
        }

        this.printAndAccept(x.getItems(), " ");
        if (x.getElseExpr() != null) {
            this.print0(this.ucase ? " ELSE " : " else ");
            x.getElseExpr().accept(this);
        }

        this.print0(this.ucase ? " END" : " end");
        return false;
    }

    public boolean visit(Item x) {
        this.print0(this.ucase ? "WHEN " : "when ");
        x.getConditionExpr().accept(this);
        this.print0(this.ucase ? " THEN " : " then ");
        x.getValueExpr().accept(this);
        return false;
    }

    public boolean visit(SQLCastExpr x) {
        this.print0(this.ucase ? "CAST(" : "cast(");
        x.getExpr().accept(this);
        this.print0(this.ucase ? " AS " : " as ");
        x.getDataType().accept(this);
        this.print0(")");
        return false;
    }

    public boolean visit(SQLCharExpr x) {
        if (this.parameterized && ParameterizedOutputVisitorUtils.checkParameterize(x)) {
            this.print('?');
            this.incrementReplaceCunt();
            if (this instanceof ExportParameterVisitor || this.parameters != null) {
                ExportParameterVisitorUtils.exportParameter(this.parameters, x);
            }

            return false;
        } else {
            if (x.getText() == null) {
                this.print0(this.ucase ? "NULL" : "null");
            } else {
                this.print('\'');
                this.print0(x.getText().replaceAll("'", "''"));
                this.print('\'');
            }

            return false;
        }
    }

    public boolean visit(SQLDataType x) {
        this.print0(x.getName());
        if (x.getArguments().size() > 0) {
            this.print('(');
            this.printAndAccept(x.getArguments(), ", ");
            this.print(')');
        }

        return false;
    }

    public boolean visit(SQLCharacterDataType x) {
        this.visit((SQLDataType)x);
        return false;
    }

    public boolean visit(SQLExistsExpr x) {
        if (x.isNot()) {
            this.print0(this.ucase ? "NOT EXISTS (" : "not exists (");
        } else {
            this.print0(this.ucase ? "EXISTS (" : "exists (");
        }

        this.incrementIndent();
        this.println();
        x.getSubQuery().accept(this);
        this.println();
        this.decrementIndent();
        this.print(')');
        return false;
    }

    public boolean visit(SQLIdentifierExpr x) {
        String name = x.getName();
        if (!this.parameterized) {
            this.print0(x.getName());
            return false;
        } else {
            return this.printName(x, name);
        }
    }

    private boolean printName(SQLName x, String name) {
        boolean shardingSupport = this.shardingSupport;
        if (shardingSupport) {
            SQLObject parent = x.getParent();
            shardingSupport = parent instanceof SQLExprTableSource || parent instanceof SQLPropertyExpr;
        }

        if (shardingSupport) {
            int pos = name.lastIndexOf(95);
            if (pos != -1 && pos != name.length() - 1) {
                boolean quote = name.charAt(0) == '`' && name.charAt(name.length() - 1) == '`';
                boolean isNumber = true;
                int end = name.length();
                if (quote) {
                    --end;
                }

                for(int i = pos + 1; i < end; ++i) {
                    char ch = name.charAt(i);
                    if (ch < '0' || ch > '9') {
                        isNumber = false;
                        break;
                    }
                }

                if (isNumber) {
                    boolean isAlias = false;

                    for(SQLObject parent = x.getParent(); parent != null; parent = parent.getParent()) {
                        if (parent instanceof SQLSelectQueryBlock) {
                            SQLTableSource from = ((SQLSelectQueryBlock)parent).getFrom();
                            if (quote) {
                                String name2 = name.substring(1, name.length() - 1);
                                if (this.isTableSourceAlias(from, name, name2)) {
                                    isAlias = true;
                                }
                            } else if (this.isTableSourceAlias(from, name)) {
                                isAlias = true;
                            }
                            break;
                        }
                    }

                    if (!isAlias) {
                        int start = quote ? 1 : 0;
                        String realName = name.substring(start, pos);
                        this.print0(realName);
                        this.incrementReplaceCunt();
                        return false;
                    }

                    this.print0(name);
                    return false;
                }
            }

            int numberCount = 0;

            int numPos;
            for(numPos = name.length() - 1; numPos >= 0; --numPos) {
                char ch = name.charAt(numPos);
                if (ch < '0' || ch > '9') {
                    break;
                }

                ++numberCount;
            }

            if (numberCount > 1) {
                numPos = name.length() - numberCount;
                String realName = name.substring(0, numPos);
                this.print0(realName);
                this.incrementReplaceCunt();
                return false;
            }
        }

        this.print0(name);
        return false;
    }

    public boolean visit(SQLInListExpr x) {
        List list;
        boolean printLn;
        if (this.parameterized) {
            list = x.getTargetList();
            printLn = true;
            if (list.size() == 1 && list.get(0) instanceof SQLVariantRefExpr) {
                printLn = false;
            }

            x.getExpr().accept(this);
            if (x.isNot()) {
                this.print(this.isUppCase() ? " NOT IN (?)" : " not in (?)");
            } else {
                this.print(this.isUppCase() ? " IN (?)" : " in (?)");
            }

            if (printLn) {
                this.incrementReplaceCunt();
                if (this instanceof ExportParameterVisitor || this.parameters != null) {
                    if (this.parameterizedMergeInList) {
                        List<Object> subList = new ArrayList(x.getTargetList().size());
                        Iterator var9 = x.getTargetList().iterator();

                        while(var9.hasNext()) {
                            SQLExpr target = (SQLExpr)var9.next();
                            ExportParameterVisitorUtils.exportParameter(subList, target);
                        }

                        if (subList != null) {
                            this.parameters.add(subList);
                        }
                    } else {
                        Iterator var8 = x.getTargetList().iterator();

                        while(var8.hasNext()) {
                            SQLExpr target = (SQLExpr)var8.next();
                            ExportParameterVisitorUtils.exportParameter(this.parameters, target);
                        }
                    }
                }
            }

            return false;
        } else {
            x.getExpr().accept(this);
            if (x.isNot()) {
                this.print0(this.ucase ? " NOT IN (" : " not in (");
            } else {
                this.print0(this.ucase ? " IN (" : " in (");
            }

            list = x.getTargetList();
            printLn = false;
            int i;
            int size;
            if (list.size() > 5) {
                printLn = true;
                i = 0;

                for(size = list.size(); i < size; ++i) {
                    if (!(list.get(i) instanceof SQLCharExpr)) {
                        printLn = false;
                        break;
                    }
                }
            }

            if (printLn) {
                this.incrementIndent();
                this.println();
                i = 0;

                for(size = list.size(); i < size; ++i) {
                    if (i != 0) {
                        this.print0(", ");
                        this.println();
                    }

                    ((SQLExpr)list.get(i)).accept(this);
                }

                this.decrementIndent();
                this.println();
            } else {
                this.printAndAccept(x.getTargetList(), ", ");
            }

            this.print(')');
            return false;
        }
    }

    public boolean visit(SQLIntegerExpr x) {
        if (this.parameterized && ParameterizedOutputVisitorUtils.checkParameterize(x)) {
            if (!ParameterizedOutputVisitorUtils.checkParameterize(x)) {
                return SQLASTOutputVisitorUtils.visit(this, x);
            } else {
                this.print('?');
                this.incrementReplaceCunt();
                if (this instanceof ExportParameterVisitor || this.parameters != null) {
                    ExportParameterVisitorUtils.exportParameter(this.parameters, x);
                }

                return false;
            }
        } else {
            return SQLASTOutputVisitorUtils.visit(this, x);
        }
    }

    public boolean visit(SQLMethodInvokeExpr x) {
        if (x.getOwner() != null) {
            x.getOwner().accept(this);
            this.print('.');
        }

        this.printFunctionName(x.getMethodName());
        this.print('(');
        this.printAndAccept(x.getParameters(), ", ");
        SQLExpr from = x.getFrom();
        if (from != null) {
            this.print0(this.ucase ? " FROM " : " from ");
            from.accept(this);
        }

        this.print(')');
        return false;
    }

    protected void printFunctionName(String name) {
        this.print0(name);
    }

    public boolean visit(SQLAggregateExpr x) {
        this.print0(this.ucase ? x.getMethodName() : x.getMethodName().toLowerCase());
        this.print('(');
        if (x.getOption() != null) {
            this.print0(x.getOption().toString());
            this.print(' ');
        }

        this.printAndAccept(x.getArguments(), ", ");
        this.visitAggreateRest(x);
        this.print(')');
        if (x.getWithinGroup() != null) {
            this.print0(this.ucase ? " WITHIN GROUP (" : " within group (");
            x.getWithinGroup().accept(this);
            this.print(')');
        }

        if (x.getKeep() != null) {
            this.print(' ');
            x.getKeep().accept(this);
        }

        if (x.getOver() != null) {
            this.print(' ');
            x.getOver().accept(this);
        }

        return false;
    }

    protected void visitAggreateRest(SQLAggregateExpr aggregateExpr) {
    }

    public boolean visit(SQLAllColumnExpr x) {
        this.print('*');
        return true;
    }

    public boolean visit(SQLNCharExpr x) {
        if (this.parameterized && ParameterizedOutputVisitorUtils.checkParameterize(x)) {
            this.print('?');
            this.incrementReplaceCunt();
            if (this instanceof ExportParameterVisitor || this.parameters != null) {
                ExportParameterVisitorUtils.exportParameter(this.parameters, x);
            }

            return false;
        } else {
            if (x.getText() != null && x.getText().length() != 0) {
                this.print0(this.ucase ? "N'" : "n'");
                this.print0(x.getText().replace("'", "''"));
                this.print('\'');
            } else {
                this.print0(this.ucase ? "NULL" : "null");
            }

            return false;
        }
    }

    public boolean visit(SQLNotExpr x) {
        this.print0(this.ucase ? "NOT " : "not ");
        SQLExpr expr = x.getExpr();
        boolean needQuote = false;
        if (expr instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr)expr;
            needQuote = binaryOpExpr.getOperator().isLogical();
        }

        if (needQuote) {
            this.print('(');
        }

        expr.accept(this);
        if (needQuote) {
            this.print(')');
        }

        return false;
    }

    public boolean visit(SQLNullExpr x) {
        if (this.parameterized && ParameterizedOutputVisitorUtils.checkParameterize(x)) {
            SQLObject parent = x.getParent();
            if (parent instanceof ValuesClause) {
                this.print('?');
                this.incrementReplaceCunt();
                if (this instanceof ExportParameterVisitor || this.parameters != null) {
                    this.getParameters().add((Object)null);
                }

                return false;
            }
        }

        this.print0(this.ucase ? "NULL" : "null");
        return false;
    }

    public boolean visit(SQLNumberExpr x) {
        if (this.parameterized && ParameterizedOutputVisitorUtils.checkParameterize(x)) {
            this.print('?');
            this.incrementReplaceCunt();
            if (this instanceof ExportParameterVisitor || this.parameters != null) {
                ExportParameterVisitorUtils.exportParameter(this.getParameters(), x);
            }

            return false;
        } else {
            return SQLASTOutputVisitorUtils.visit(this, x);
        }
    }

    public boolean visit(SQLPropertyExpr x) {
        SQLExpr owner = x.getOwner();
        String mapTableName = null;
        String ownerName = null;
        if (owner instanceof SQLIdentifierExpr) {
            ownerName = ((SQLIdentifierExpr)owner).getName();
            if (this.tableMapping != null) {
                mapTableName = (String)this.tableMapping.get(ownerName);
                if (mapTableName == null && ownerName.length() > 2 && ownerName.charAt(0) == '`' && ownerName.charAt(ownerName.length() - 1) == '`') {
                    ownerName = ownerName.substring(1, ownerName.length() - 1);
                    mapTableName = (String)this.tableMapping.get(ownerName);
                }
            }
        }

        if (mapTableName != null) {
            for(SQLObject parent = x.getParent(); parent != null; parent = parent.getParent()) {
                if (parent instanceof SQLSelectQueryBlock) {
                    SQLTableSource from = ((SQLSelectQueryBlock)parent).getFrom();
                    if (this.isTableSourceAlias(from, mapTableName, ownerName)) {
                        mapTableName = null;
                    }
                    break;
                }
            }
        }

        if (mapTableName != null) {
            this.print0(mapTableName);
        } else {
            owner.accept(this);
        }

        this.print('.');
        String name = x.getName();
        this.printName(x, name);
        return false;
    }

    protected boolean isTableSourceAlias(SQLTableSource from, String... tableNames) {
        String alias = from.getAlias();
        if (alias != null) {
            String[] var4 = tableNames;
            int var5 = tableNames.length;

            int var6;
            String tableName;
            for(var6 = 0; var6 < var5; ++var6) {
                tableName = var4[var6];
                if (alias.equalsIgnoreCase(tableName)) {
                    return true;
                }
            }

            if (alias.length() > 2 && alias.charAt(0) == '`' && alias.charAt(alias.length() - 1) == '`') {
                alias = alias.substring(1, alias.length() - 1);
                var4 = tableNames;
                var5 = tableNames.length;

                for(var6 = 0; var6 < var5; ++var6) {
                    tableName = var4[var6];
                    if (alias.equalsIgnoreCase(tableName)) {
                        return true;
                    }
                }
            }
        }

        if (!(from instanceof SQLJoinTableSource)) {
            return false;
        } else {
            SQLJoinTableSource join = (SQLJoinTableSource)from;
            return this.isTableSourceAlias(join.getLeft(), tableNames) || this.isTableSourceAlias(join.getRight(), tableNames);
        }
    }

    public boolean visit(SQLQueryExpr x) {
        SQLObject parent = x.getParent();
        if (parent instanceof SQLSelect) {
            parent = parent.getParent();
        }

        if (parent instanceof SQLStatement) {
            this.incrementIndent();
            this.println();
            x.getSubQuery().accept(this);
            this.decrementIndent();
        } else if (parent instanceof ValuesClause) {
            this.println();
            this.print('(');
            x.getSubQuery().accept(this);
            this.print(')');
            this.println();
        } else {
            this.print('(');
            this.incrementIndent();
            this.println();
            x.getSubQuery().accept(this);
            this.println();
            this.decrementIndent();
            this.print(')');
        }

        return false;
    }

    public boolean visit(SQLSelectGroupByClause x) {
        int itemSize = x.getItems().size();
        if (itemSize > 0) {
            this.print0(this.ucase ? "GROUP BY " : "group by ");
            this.incrementIndent();

            for(int i = 0; i < itemSize; ++i) {
                if (i != 0) {
                    if (this.groupItemSingleLine) {
                        this.println(", ");
                    } else {
                        this.print(", ");
                    }
                }

                ((SQLExpr)x.getItems().get(i)).accept(this);
            }

            this.decrementIndent();
        }

        if (x.getHaving() != null) {
            this.println();
            this.print0(this.ucase ? "HAVING " : "having ");
            x.getHaving().accept(this);
        }

        if (x.isWithRollUp()) {
            this.print0(this.ucase ? " WITH ROLLUP" : " with rollup");
        }

        if (x.isWithCube()) {
            this.print0(this.ucase ? " WITH CUBE" : " with cube");
        }

        return false;
    }

    public boolean visit(SQLSelect x) {
        x.getQuery().setParent(x);
        if (x.getWithSubQuery() != null) {
            x.getWithSubQuery().accept(this);
            this.println();
        }

        x.getQuery().accept(this);
        if (x.getOrderBy() != null) {
            this.println();
            x.getOrderBy().accept(this);
        }

        if (x.getHintsSize() > 0) {
            this.printAndAccept(x.getHints(), "");
        }

        return false;
    }

    public boolean visit(SQLSelectQueryBlock x) {
        if (this.isPrettyFormat() && x.hasBeforeComment()) {
            this.printComment(x.getBeforeCommentsDirect(), "\n");
        }

        this.print0(this.ucase ? "SELECT " : "select ");
        if ("informix".equals(this.dbType)) {
            this.printFetchFirst(x);
        }

        if (1 == x.getDistionOption()) {
            this.print0(this.ucase ? "ALL " : "all ");
        } else if (2 == x.getDistionOption()) {
            this.print0(this.ucase ? "DISTINCT " : "distinct ");
        } else if (3 == x.getDistionOption()) {
            this.print0(this.ucase ? "UNIQUE " : "unique ");
        }

        this.printSelectList(x.getSelectList());
        if (x.getFrom() != null) {
            this.println();
            this.print0(this.ucase ? "FROM " : "from ");
            x.getFrom().accept(this);
        }

        SQLExpr where = x.getWhere();
        if (where != null) {
            this.println();
            this.print0(this.ucase ? "WHERE " : "where ");
            where.setParent(x);
            where.accept(this);
        }

        if (x.getGroupBy() != null) {
            this.println();
            x.getGroupBy().accept(this);
        }

        if (x.getOrderBy() != null) {
            this.println();
            x.getOrderBy().accept(this);
        }

        if (!"informix".equals(this.dbType)) {
            this.printFetchFirst(x);
        }

        return false;
    }

    protected void printFetchFirst(SQLSelectQueryBlock x) {
        SQLLimit limit = x.getLimit();
        if (limit != null) {
            SQLExpr offset = limit.getOffset();
            SQLExpr first = limit.getRowCount();
            if (limit != null) {
                if ("informix".equals(this.dbType)) {
                    if (offset != null) {
                        this.print0(this.ucase ? "SKIP " : "skip ");
                        offset.accept(this);
                    }

                    this.print0(this.ucase ? " FIRST " : " first ");
                    first.accept(this);
                    this.print(' ');
                } else if (!"db2".equals(this.dbType) && !"oracle".equals(this.dbType) && !"sqlserver".equals(this.dbType)) {
                    this.println();
                    limit.accept(this);
                } else {
                    SQLObject parent = x.getParent();
                    if (parent instanceof SQLSelect) {
                        SQLOrderBy orderBy = ((SQLSelect)parent).getOrderBy();
                        if (orderBy != null && orderBy.getItems().size() > 0) {
                            this.println();
                            this.print0(this.ucase ? "ORDER BY " : "order by ");
                            this.printAndAccept(orderBy.getItems(), ", ");
                        }
                    }

                    this.println();
                    if (offset != null) {
                        this.print0(this.ucase ? "OFFSET " : "offset ");
                        offset.accept(this);
                        this.print0(this.ucase ? " ROWS " : " rows ");
                    }

                    this.print0(this.ucase ? "FETCH FIRST " : "fetch first ");
                    first.accept(this);
                    this.print0(this.ucase ? " ROWS ONLY" : " rows only");
                }
            }

        }
    }

    public boolean visit(SQLSelectItem x) {
        if (x.isConnectByRoot()) {
            this.print0(this.ucase ? "CONNECT_BY_ROOT " : "connect_by_root ");
        }

        x.getExpr().accept(this);
        String alias = x.getAlias();
        if (alias != null && alias.length() > 0) {
            this.print0(this.ucase ? " AS " : " as ");
            if (alias.indexOf(32) != -1 && alias.charAt(0) != '"' && alias.charAt(0) != '\'') {
                this.print('"');
                this.print0(alias);
                this.print('"');
            } else {
                this.print0(alias);
            }
        }

        return false;
    }

    public boolean visit(SQLOrderBy x) {
        if (x.getItems().size() > 0) {
            if (x.isSibings()) {
                this.print0(this.ucase ? "ORDER SIBLINGS BY " : "order siblings by ");
            } else {
                this.print0(this.ucase ? "ORDER BY " : "order by ");
            }

            this.printAndAccept(x.getItems(), ", ");
        }

        return false;
    }

    public boolean visit(SQLSelectOrderByItem x) {
        x.getExpr().accept(this);
        if (x.getType() != null) {
            this.print(' ');
            SQLOrderingSpecification type = x.getType();
            this.print0(this.ucase ? type.name : type.name_lcase);
        }

        if (x.getCollate() != null) {
            this.print0(this.ucase ? " COLLATE " : " collate ");
            this.print0(x.getCollate());
        }

        if (x.getNullsOrderType() != null) {
            this.print(' ');
            this.print0(x.getNullsOrderType().toFormalString());
        }

        return false;
    }

    protected void addTable(String table) {
        if (this.tables == null) {
            this.tables = new LinkedHashSet();
        }

        this.tables.add(table);
    }

    protected void printTableSourceExpr(SQLExpr expr) {
        if (this.exportTables) {
            this.addTable(expr.toString());
        }

        if (this.tableMapping != null && expr instanceof SQLName) {
            String tableName = expr.toString();
            String destTableName = (String)this.tableMapping.get(tableName);
            if (destTableName == null) {
                if (expr instanceof SQLPropertyExpr) {
                    SQLPropertyExpr propertyExpr = (SQLPropertyExpr)expr;
                    String propName = propertyExpr.getName();
                    destTableName = (String)this.tableMapping.get(propName);
                    if (destTableName == null && propName.length() > 2 && propName.charAt(0) == '`' && propName.charAt(propName.length() - 1) == '`') {
                        destTableName = (String)this.tableMapping.get(propName.substring(1, propName.length() - 1));
                    }

                    if (destTableName != null) {
                        propertyExpr.getOwner().accept(this);
                        this.print('.');
                        this.print(destTableName);
                        return;
                    }
                } else if (expr instanceof SQLIdentifierExpr) {
                    boolean quote = tableName.length() > 2 && tableName.charAt(0) == '`' && tableName.charAt(tableName.length() - 1) == '`';
                    if (quote) {
                        destTableName = (String)this.tableMapping.get(tableName.substring(1, tableName.length() - 1));
                    }
                }
            }

            if (destTableName != null) {
                this.print0(destTableName);
                return;
            }
        }

        expr.accept(this);
    }

    public boolean visit(SQLExprTableSource x) {
        this.printTableSourceExpr(x.getExpr());
        if (x.getAlias() != null) {
            this.print(' ');
            this.print0(x.getAlias());
        }

        if (this.isPrettyFormat() && x.hasAfterComment()) {
            this.print(' ');
            this.printComment(x.getAfterCommentsDirect(), "\n");
        }

        return false;
    }

    public boolean visit(SQLSelectStatement stmt) {
        List<SQLCommentHint> headHints = stmt.getHeadHintsDirect();
        if (headHints != null) {
            Iterator var3 = headHints.iterator();

            while(var3.hasNext()) {
                SQLCommentHint hint = (SQLCommentHint)var3.next();
                hint.accept(this);
                this.println();
            }
        }

        SQLSelect select = stmt.getSelect();
        select.accept(this);
        return false;
    }

    public boolean visit(SQLVariantRefExpr x) {
        int index = x.getIndex();
        if (index >= 0 && this.parameters != null && index < this.parameters.size()) {
            Object param = this.parameters.get(index);
            SQLObject parent = x.getParent();
            boolean in;
            if (parent instanceof SQLInListExpr) {
                in = true;
            } else if (parent instanceof SQLBinaryOpExpr) {
                SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr)parent;
                if (binaryOpExpr.getOperator() == SQLBinaryOperator.Equality) {
                    in = true;
                } else {
                    in = false;
                }
            } else {
                in = false;
            }

            if (in && param instanceof Collection) {
                boolean first = true;

                for(Iterator var7 = ((Collection)param).iterator(); var7.hasNext(); first = false) {
                    Object item = var7.next();
                    if (!first) {
                        this.print0(", ");
                    }

                    this.printParameter(item);
                }
            } else {
                this.printParameter(param);
            }

            return false;
        } else {
            this.print0(x.getName());
            return false;
        }
    }

    public void printParameter(Object param) {
        if (param == null) {
            this.print0(this.ucase ? "NULL" : "null");
        } else if (!(param instanceof Number) && !(param instanceof Boolean)) {
            if (param instanceof String) {
                SQLCharExpr charExpr = new SQLCharExpr((String)param);
                this.visit(charExpr);
            } else if (param instanceof Date) {
                this.print((Date)param);
            } else if (param instanceof InputStream) {
                this.print0("'<InputStream>");
            } else if (param instanceof Reader) {
                this.print0("'<Reader>");
            } else if (param instanceof Blob) {
                this.print0("'<Blob>");
            } else if (param instanceof NClob) {
                this.print0("'<NClob>");
            } else if (param instanceof Clob) {
                this.print0("'<Clob>");
            } else {
                this.print0("'" + param.getClass().getName() + "'");
            }
        } else {
            this.print0(param.toString());
        }
    }

    public boolean visit(SQLDropTableStatement x) {
        if (x.isTemporary()) {
            this.print0(this.ucase ? "DROP TEMPORARY TABLE " : "drop temporary table ");
        } else {
            this.print0(this.ucase ? "DROP TABLE " : "drop table ");
        }

        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }

        this.printAndAccept(x.getTableSources(), ", ");
        if (x.isCascade()) {
            this.printCascade();
        }

        if (x.isRestrict()) {
            this.print0(this.ucase ? " RESTRICT" : " restrict");
        }

        if (x.isPurge()) {
            this.print0(this.ucase ? " PURGE" : " purge");
        }

        return false;
    }

    protected void printCascade() {
        this.print0(this.ucase ? " CASCADE" : " cascade");
    }

    public boolean visit(SQLDropViewStatement x) {
        this.print0(this.ucase ? "DROP VIEW " : "drop view ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }

        this.printAndAccept(x.getTableSources(), ", ");
        if (x.isCascade()) {
            this.printCascade();
        }

        return false;
    }

    public boolean visit(SQLTableElement x) {
        if (x instanceof SQLColumnDefinition) {
            return this.visit((SQLColumnDefinition)x);
        } else {
            throw new RuntimeException("TODO");
        }
    }

    public boolean visit(SQLColumnDefinition x) {
        x.getName().accept(this);
        if (x.getDataType() != null) {
            this.print(' ');
            x.getDataType().accept(this);
        }

        if (x.getDefaultExpr() != null) {
            this.visitColumnDefault(x);
        }

        Iterator var2 = x.getConstraints().iterator();

        while(var2.hasNext()) {
            SQLColumnConstraint item = (SQLColumnConstraint)var2.next();
            boolean newLine = item instanceof SQLForeignKeyConstraint || item instanceof SQLPrimaryKey || item instanceof SQLColumnCheck || item instanceof SQLColumnCheck || item.getName() != null;
            if (newLine) {
                this.incrementIndent();
                this.println();
            } else {
                this.print(' ');
            }

            item.accept(this);
            if (newLine) {
                this.decrementIndent();
            }
        }

        if (x.getEnable() != null && x.getEnable()) {
            this.print0(this.ucase ? " ENABLE" : " enable");
        }

        if (x.getComment() != null) {
            this.print0(this.ucase ? " COMMENT " : " comment ");
            x.getComment().accept(this);
        }

        return false;
    }

    public boolean visit(Identity x) {
        this.print0(this.ucase ? "IDENTITY" : "identity");
        if (x.getSeed() != null) {
            this.print0(" (");
            this.print(x.getSeed());
            this.print0(", ");
            this.print(x.getIncrement());
            this.print(')');
        }

        return false;
    }

    protected void visitColumnDefault(SQLColumnDefinition x) {
        this.print0(this.ucase ? " DEFAULT " : " default ");
        x.getDefaultExpr().accept(this);
    }

    public boolean visit(SQLDeleteStatement x) {
        SQLTableSource from = x.getFrom();
        if (from == null) {
            this.print0(this.ucase ? "DELETE FROM " : "delete from ");
            this.printTableSourceExpr(x.getTableName());
        } else {
            this.print0(this.ucase ? "DELETE " : "delete ");
            this.printTableSourceExpr(x.getTableName());
            this.print0(this.ucase ? " FROM " : " from ");
            from.accept(this);
        }

        if (x.getWhere() != null) {
            this.println();
            this.print0(this.ucase ? "WHERE " : "where ");
            this.incrementIndent();
            x.getWhere().setParent(x);
            x.getWhere().accept(this);
            this.decrementIndent();
        }

        return false;
    }

    public boolean visit(SQLCurrentOfCursorExpr x) {
        this.print0(this.ucase ? "CURRENT OF " : "current of ");
        x.getCursorName().accept(this);
        return false;
    }

    public boolean visit(SQLInsertStatement x) {
        if (x.isUpsert()) {
            this.print0(this.ucase ? "UPSERT INTO " : "upsert into ");
        } else {
            this.print0(this.ucase ? "INSERT INTO " : "insert into ");
        }

        x.getTableSource().accept(this);
        this.printInsertColumns(x.getColumns());
        if (!x.getValuesList().isEmpty()) {
            this.println();
            this.print0(this.ucase ? "VALUES " : "values ");
            this.printAndAccept(x.getValuesList(), ", ");
        } else if (x.getQuery() != null) {
            this.println();
            x.getQuery().setParent(x);
            x.getQuery().accept(this);
        }

        return false;
    }

    protected void printInsertColumns(List<SQLExpr> columns) {
        int size = columns.size();
        if (size > 0) {
            if (size > 5) {
                this.incrementIndent();
                this.println();
            } else {
                this.print(' ');
            }

            this.print('(');

            for(int i = 0; i < size; ++i) {
                if (i != 0) {
                    if (i % 5 == 0) {
                        this.println();
                    }

                    this.print0(", ");
                }

                SQLExpr column = (SQLExpr)columns.get(i);
                column.accept(this);
                String dataType = (String)column.getAttribute("dataType");
                if (dataType != null) {
                    this.print(' ');
                    this.print(dataType);
                }
            }

            this.print(')');
            if (size > 5) {
                this.decrementIndent();
            }
        }

    }

    public boolean visit(SQLUpdateSetItem x) {
        x.getColumn().accept(this);
        this.print0(" = ");
        x.getValue().accept(this);
        return false;
    }

    public boolean visit(SQLUpdateStatement x) {
        this.print0(this.ucase ? "UPDATE " : "update ");
        x.getTableSource().accept(this);
        this.println();
        this.print0(this.ucase ? "SET " : "set ");
        int i = 0;

        for(int size = x.getItems().size(); i < size; ++i) {
            if (i != 0) {
                this.print0(", ");
            }

            ((SQLUpdateSetItem)x.getItems().get(i)).accept(this);
        }

        if (x.getWhere() != null) {
            this.println();
            this.incrementIndent();
            this.print0(this.ucase ? "WHERE " : "where ");
            x.getWhere().setParent(x);
            x.getWhere().accept(this);
            this.decrementIndent();
        }

        return false;
    }

    public boolean visit(SQLCreateTableStatement x) {
        this.print0(this.ucase ? "CREATE TABLE " : "create table ");
        if (Type.GLOBAL_TEMPORARY.equals(x.getType())) {
            this.print0(this.ucase ? "GLOBAL TEMPORARY " : "global temporary ");
        } else if (Type.LOCAL_TEMPORARY.equals(x.getType())) {
            this.print0(this.ucase ? "LOCAL TEMPORARY " : "local temporary ");
        }

        this.printTableSourceExpr(x.getName());
        int size = x.getTableElementList().size();
        if (size > 0) {
            this.print0(" (");
            this.incrementIndent();
            this.println();

            for(int i = 0; i < size; ++i) {
                if (i != 0) {
                    this.print(',');
                    this.println();
                }

                ((SQLTableElement)x.getTableElementList().get(i)).accept(this);
            }

            this.decrementIndent();
            this.println();
            this.print(')');
        }

        if (x.getInherits() != null) {
            this.print0(this.ucase ? " INHERITS (" : " inherits (");
            x.getInherits().accept(this);
            this.print(')');
        }

        return false;
    }

    public boolean visit(SQLUniqueConstraint x) {
        if (x.getName() != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            x.getName().accept(this);
            this.print(' ');
        }

        this.print0(this.ucase ? "UNIQUE (" : "unique (");
        int i = 0;

        for(int size = x.getColumns().size(); i < size; ++i) {
            if (i != 0) {
                this.print0(", ");
            }

            ((SQLExpr)x.getColumns().get(i)).accept(this);
        }

        this.print(')');
        return false;
    }

    public boolean visit(SQLNotNullConstraint x) {
        if (x.getName() != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            x.getName().accept(this);
            this.print(' ');
        }

        this.print0(this.ucase ? "NOT NULL" : "not null");
        return false;
    }

    public boolean visit(SQLNullConstraint x) {
        if (x.getName() != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            x.getName().accept(this);
            this.print(' ');
        }

        this.print0(this.ucase ? "NULL" : "null");
        return false;
    }

    public boolean visit(SQLUnionQuery x) {
        x.getLeft().accept(this);
        this.println();
        this.print0(this.ucase ? x.getOperator().name : x.getOperator().name_lcase);
        this.println();
        boolean needParen = false;
        if (x.getOrderBy() != null) {
            needParen = true;
        }

        if (needParen) {
            this.print('(');
            x.getRight().accept(this);
            this.print(')');
        } else {
            x.getRight().accept(this);
        }

        if (x.getOrderBy() != null) {
            this.println();
            x.getOrderBy().accept(this);
        }

        return false;
    }

    public boolean visit(SQLUnaryExpr x) {
        this.print0(x.getOperator().name);
        SQLExpr expr = x.getExpr();
        switch(x.getOperator()) {
            case BINARY:
            case Prior:
            case ConnectByRoot:
                this.print(' ');
                expr.accept(this);
                return false;
            default:
                if (expr instanceof SQLBinaryOpExpr) {
                    this.print('(');
                    expr.accept(this);
                    this.print(')');
                } else if (expr instanceof SQLUnaryExpr) {
                    this.print('(');
                    expr.accept(this);
                    this.print(')');
                } else {
                    expr.accept(this);
                }

                return false;
        }
    }

    public boolean visit(SQLHexExpr x) {
        if (this.parameterized && ParameterizedOutputVisitorUtils.checkParameterize(x)) {
            this.print('?');
            this.incrementReplaceCunt();
            if (this instanceof ExportParameterVisitor || this.parameters != null) {
                ExportParameterVisitorUtils.exportParameter(this.parameters, x);
            }

            return false;
        } else {
            this.print0("0x");
            this.print0(x.getHex());
            String charset = (String)x.getAttribute("USING");
            if (charset != null) {
                this.print0(this.ucase ? " USING " : " using ");
                this.print0(charset);
            }

            return false;
        }
    }

    public boolean visit(SQLSetStatement x) {
        this.print0(this.ucase ? "SET " : "set ");
        this.printAndAccept(x.getItems(), ", ");
        if (x.getHints() != null && x.getHints().size() > 0) {
            this.print(' ');
            this.printAndAccept(x.getHints(), " ");
        }

        return false;
    }

    public boolean visit(SQLAssignItem x) {
        x.getTarget().accept(this);
        this.print0(" = ");
        x.getValue().accept(this);
        return false;
    }

    public boolean visit(SQLCallStatement x) {
        if (x.isBrace()) {
            this.print('{');
        }

        if (x.getOutParameter() != null) {
            x.getOutParameter().accept(this);
            this.print0(" = ");
        }

        this.print0(this.ucase ? "CALL " : "call ");
        x.getProcedureName().accept(this);
        this.print('(');
        this.printAndAccept(x.getParameters(), ", ");
        this.print(')');
        if (x.isBrace()) {
            this.print('}');
        }

        return false;
    }

    public boolean visit(SQLJoinTableSource x) {
        x.getLeft().accept(this);
        this.incrementIndent();
        if (x.getJoinType() == JoinType.COMMA) {
            this.print(',');
        } else {
            this.println();
            this.printJoinType(x.getJoinType());
        }

        this.print(' ');
        x.getRight().accept(this);
        if (x.getCondition() != null) {
            this.incrementIndent();
            this.print0(this.ucase ? " ON " : " on ");
            x.getCondition().accept(this);
            this.decrementIndent();
        }

        if (x.getUsing().size() > 0) {
            this.print0(this.ucase ? " USING (" : " using (");
            this.printAndAccept(x.getUsing(), ", ");
            this.print(')');
        }

        if (x.getAlias() != null) {
            this.print0(this.ucase ? " AS " : " as ");
            this.print0(x.getAlias());
        }

        this.decrementIndent();
        return false;
    }

    protected void printJoinType(JoinType joinType) {
        this.print0(this.ucase ? joinType.name : joinType.name_lcase);
    }

    public boolean visit(ValuesClause x) {
        this.print('(');
        this.incrementIndent();
        int i = 0;

        for(int size = x.getValues().size(); i < size; ++i) {
            if (i != 0) {
                if (i % 5 == 0) {
                    this.println();
                }

                this.print0(", ");
            }

            SQLExpr expr = (SQLExpr)x.getValues().get(i);
            expr.setParent(x);
            expr.accept(this);
        }

        this.decrementIndent();
        this.print(')');
        return false;
    }

    public boolean visit(SQLSomeExpr x) {
        this.print0(this.ucase ? "SOME (" : "some (");
        this.incrementIndent();
        x.getSubQuery().accept(this);
        this.decrementIndent();
        this.print(')');
        return false;
    }

    public boolean visit(SQLAnyExpr x) {
        this.print0(this.ucase ? "ANY (" : "any (");
        this.incrementIndent();
        x.getSubQuery().accept(this);
        this.decrementIndent();
        this.print(')');
        return false;
    }

    public boolean visit(SQLAllExpr x) {
        this.print0(this.ucase ? "ALL (" : "all (");
        this.incrementIndent();
        x.getSubQuery().accept(this);
        this.decrementIndent();
        this.print(')');
        return false;
    }

    public boolean visit(SQLInSubQueryExpr x) {
        x.getExpr().accept(this);
        if (x.isNot()) {
            this.print0(this.ucase ? " NOT IN (" : " not in (");
        } else {
            this.print0(this.ucase ? " IN (" : " in (");
        }

        this.incrementIndent();
        x.getSubQuery().accept(this);
        this.decrementIndent();
        this.print(')');
        return false;
    }

    public boolean visit(SQLListExpr x) {
        this.print('(');
        this.printAndAccept(x.getItems(), ", ");
        this.print(')');
        return false;
    }

    public boolean visit(SQLSubqueryTableSource x) {
        this.print('(');
        this.incrementIndent();
        x.getSelect().accept(this);
        this.println();
        this.decrementIndent();
        this.print(')');
        if (x.getAlias() != null) {
            this.print(' ');
            this.print0(x.getAlias());
        }

        return false;
    }

    public boolean visit(SQLTruncateStatement x) {
        this.print0(this.ucase ? "TRUNCATE TABLE " : "truncate table ");
        this.printAndAccept(x.getTableSources(), ", ");
        if (x.isDropStorage()) {
            this.print0(this.ucase ? " DROP STORAGE" : " drop storage");
        }

        if (x.isReuseStorage()) {
            this.print0(this.ucase ? " REUSE STORAGE" : " reuse storage");
        }

        if (x.isIgnoreDeleteTriggers()) {
            this.print0(this.ucase ? " IGNORE DELETE TRIGGERS" : " ignore delete triggers");
        }

        if (x.isRestrictWhenDeleteTriggers()) {
            this.print0(this.ucase ? " RESTRICT WHEN DELETE TRIGGERS" : " restrict when delete triggers");
        }

        if (x.isContinueIdentity()) {
            this.print0(this.ucase ? " CONTINUE IDENTITY" : " continue identity");
        }

        if (x.isImmediate()) {
            this.print0(this.ucase ? " IMMEDIATE" : " immediate");
        }

        return false;
    }

    public boolean visit(SQLDefaultExpr x) {
        this.print0(this.ucase ? "DEFAULT" : "default");
        return false;
    }

    public void endVisit(SQLCommentStatement x) {
    }

    public boolean visit(SQLCommentStatement x) {
        this.print0(this.ucase ? "COMMENT ON " : "comment on ");
        if (x.getType() != null) {
            this.print0(x.getType().name());
            this.print(' ');
        }

        x.getOn().accept(this);
        this.print0(this.ucase ? " IS " : " is ");
        x.getComment().accept(this);
        return false;
    }

    public boolean visit(SQLUseStatement x) {
        this.print0(this.ucase ? "USE " : "use ");
        x.getDatabase().accept(this);
        return false;
    }

    protected boolean isOdps() {
        return "odps".equals(this.dbType);
    }

    public boolean visit(SQLAlterTableAddColumn x) {
        boolean odps = this.isOdps();
        if (odps) {
            this.print0(this.ucase ? "ADD COLUMNS (" : "add columns (");
        } else {
            this.print0(this.ucase ? "ADD (" : "add (");
        }

        this.printAndAccept(x.getColumns(), ", ");
        this.print(')');
        return false;
    }

    public boolean visit(SQLAlterTableDropColumnItem x) {
        this.print0(this.ucase ? "DROP COLUMN " : "drop column ");
        this.printAndAccept(x.getColumns(), ", ");
        if (x.isCascade()) {
            this.print0(this.ucase ? " CASCADE" : " cascade");
        }

        return false;
    }

    public void endVisit(SQLAlterTableAddColumn x) {
    }

    public boolean visit(SQLDropIndexStatement x) {
        this.print0(this.ucase ? "DROP INDEX " : "drop index ");
        x.getIndexName().accept(this);
        SQLExprTableSource table = x.getTableName();
        if (table != null) {
            this.print0(this.ucase ? " ON " : " on ");
            table.accept(this);
        }

        return false;
    }

    public boolean visit(SQLSavePointStatement x) {
        this.print0(this.ucase ? "SAVEPOINT " : "savepoint ");
        x.getName().accept(this);
        return false;
    }

    public boolean visit(SQLReleaseSavePointStatement x) {
        this.print0(this.ucase ? "RELEASE SAVEPOINT " : "release savepoint ");
        x.getName().accept(this);
        return false;
    }

    public boolean visit(SQLRollbackStatement x) {
        this.print0(this.ucase ? "ROLLBACK" : "rollback");
        if (x.getTo() != null) {
            this.print0(this.ucase ? " TO " : " to ");
            x.getTo().accept(this);
        }

        return false;
    }

    public boolean visit(SQLCommentHint x) {
        this.print0("/*");
        this.print0(x.getText());
        this.print0("*/");
        return false;
    }

    public boolean visit(SQLCreateDatabaseStatement x) {
        this.print0(this.ucase ? "CREATE DATABASE " : "create database ");
        if (x.isIfNotExists()) {
            this.print0(this.ucase ? "IF NOT EXISTS " : "if not exists ");
        }

        x.getName().accept(this);
        if (x.getCharacterSet() != null) {
            this.print0(this.ucase ? " CHARACTER SET " : " character set ");
            this.print0(x.getCharacterSet());
        }

        if (x.getCollate() != null) {
            this.print0(this.ucase ? " COLLATE " : " collate ");
            this.print0(x.getCollate());
        }

        return false;
    }

    public boolean visit(SQLCreateViewStatement x) {
        this.print0(this.ucase ? "CREATE " : "create ");
        if (x.isOrReplace()) {
            this.print0(this.ucase ? "OR REPLACE " : "or replace ");
        }

        this.incrementIndent();
        String algorithm = x.getAlgorithm();
        if (algorithm != null && algorithm.length() > 0) {
            this.print0(this.ucase ? "ALGORITHM = " : "algorithm = ");
            this.print0(algorithm);
            this.println();
        }

        SQLName definer = x.getDefiner();
        if (definer != null) {
            this.print0(this.ucase ? "DEFINER = " : "definer = ");
            definer.accept(this);
            this.println();
        }

        String sqlSecurity = x.getSqlSecurity();
        if (sqlSecurity != null && sqlSecurity.length() > 0) {
            this.print0(this.ucase ? "SQL SECURITY = " : "sql security = ");
            this.print0(sqlSecurity);
            this.println();
        }

        this.decrementIndent();
        this.print0(this.ucase ? "VIEW " : "view ");
        if (x.isIfNotExists()) {
            this.print0(this.ucase ? "IF NOT EXISTS " : "if not exists ");
        }

        x.getName().accept(this);
        if (x.getColumns().size() > 0) {
            this.println();
            this.print('(');
            this.incrementIndent();
            this.println();

            for(int i = 0; i < x.getColumns().size(); ++i) {
                if (i != 0) {
                    this.print0(", ");
                    this.println();
                }

                ((Column)x.getColumns().get(i)).accept(this);
            }

            this.decrementIndent();
            this.println();
            this.print(')');
        }

        if (x.getComment() != null) {
            this.println();
            this.print0(this.ucase ? "COMMENT " : "comment ");
            x.getComment().accept(this);
        }

        this.println();
        this.print0(this.ucase ? "AS" : "as");
        this.println();
        x.getSubQuery().accept(this);
        return false;
    }

    public boolean visit(Column x) {
        x.getExpr().accept(this);
        if (x.getComment() != null) {
            this.print0(this.ucase ? " COMMENT " : " comment ");
            x.getComment().accept(this);
        }

        return false;
    }

    public boolean visit(SQLAlterTableDropIndex x) {
        this.print0(this.ucase ? "DROP INDEX " : "drop index ");
        x.getIndexName().accept(this);
        return false;
    }

    public boolean visit(SQLOver x) {
        this.print0(this.ucase ? "OVER (" : "over (");
        if (x.getPartitionBy().size() > 0) {
            this.print0(this.ucase ? "PARTITION BY " : "partition by ");
            this.printAndAccept(x.getPartitionBy(), ", ");
            this.print(' ');
        }

        if (x.getOrderBy() != null) {
            x.getOrderBy().accept(this);
        }

        if (x.getOf() != null) {
            this.print0(this.ucase ? " OF " : " of ");
            x.getOf().accept(this);
        }

        if (x.getWindowing() != null) {
            if (WindowingType.ROWS.equals(x.getWindowingType())) {
                this.print0(this.ucase ? " ROWS " : " rows ");
            } else if (WindowingType.RANGE.equals(x.getWindowingType())) {
                this.print0(this.ucase ? " RANGE " : " range ");
            }

            this.printWindowingExpr(x.getWindowing());
            if (x.isWindowingPreceding()) {
                this.print0(this.ucase ? " PRECEDING" : " preceding");
            } else if (x.isWindowingFollowing()) {
                this.print0(this.ucase ? " FOLLOWING" : " following");
            }
        }

        if (x.getWindowingBetweenBegin() != null) {
            if (WindowingType.ROWS.equals(x.getWindowingType())) {
                this.print0(this.ucase ? " ROWS BETWEEN " : " rows between ");
            } else if (WindowingType.RANGE.equals(x.getWindowingType())) {
                this.print0(this.ucase ? " RANGE BETWEEN " : " range between ");
            }

            this.printWindowingExpr(x.getWindowingBetweenBegin());
            if (x.isWindowingBetweenBeginPreceding()) {
                this.print0(this.ucase ? " PRECEDING" : " preceding");
            } else if (x.isWindowingBetweenBeginFollowing()) {
                this.print0(this.ucase ? " FOLLOWING" : " following");
            }

            this.print0(this.ucase ? " AND " : " and ");
            this.printWindowingExpr(x.getWindowingBetweenEnd());
            if (x.isWindowingBetweenEndPreceding()) {
                this.print0(this.ucase ? " PRECEDING" : " preceding");
            } else if (x.isWindowingBetweenEndFollowing()) {
                this.print0(this.ucase ? " FOLLOWING" : " following");
            }
        }

        this.print(')');
        return false;
    }

    void printWindowingExpr(SQLExpr expr) {
        if (expr instanceof SQLIdentifierExpr) {
            String ident = ((SQLIdentifierExpr)expr).getName();
            this.print0(this.ucase ? ident : ident.toLowerCase());
        } else {
            expr.accept(this);
        }

    }

    public boolean visit(SQLKeep x) {
        if (x.getDenseRank() == DenseRank.FIRST) {
            this.print0(this.ucase ? "KEEP (DENSE_RANK FIRST " : "keep (dense_rank first ");
        } else {
            this.print0(this.ucase ? "KEEP (DENSE_RANK LAST " : "keep (dense_rank last ");
        }

        x.getOrderBy().accept(this);
        this.print(')');
        return false;
    }

    public boolean visit(SQLColumnPrimaryKey x) {
        if (x.getName() != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            x.getName().accept(this);
            this.print(' ');
        }

        this.print0(this.ucase ? "PRIMARY KEY" : "primary key");
        return false;
    }

    public boolean visit(SQLColumnUniqueKey x) {
        if (x.getName() != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            x.getName().accept(this);
            this.print(' ');
        }

        this.print0(this.ucase ? "UNIQUE" : "unique");
        return false;
    }

    public boolean visit(SQLColumnCheck x) {
        if (x.getName() != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            x.getName().accept(this);
            this.print(' ');
        }

        this.print0(this.ucase ? "CHECK (" : "check (");
        x.getExpr().accept(this);
        this.print(')');
        if (x.getEnable() != null) {
            if (x.getEnable()) {
                this.print0(this.ucase ? " ENABLE" : " enable");
            } else {
                this.print0(this.ucase ? " DISABLE" : " disable");
            }
        }

        return false;
    }

    public boolean visit(SQLWithSubqueryClause x) {
        this.print0(this.ucase ? "WITH" : "with");
        if (x.getRecursive() == Boolean.TRUE) {
            this.print0(this.ucase ? " RECURSIVE" : " recursive");
        }

        this.incrementIndent();
        this.println();
        this.printlnAndAccept(x.getEntries(), ", ");
        this.decrementIndent();
        return false;
    }

    public boolean visit(Entry x) {
        x.getName().accept(this);
        if (x.getColumns().size() > 0) {
            this.print0(" (");
            this.printAndAccept(x.getColumns(), ", ");
            this.print(')');
        }

        this.println();
        this.print0(this.ucase ? "AS" : "as");
        this.println();
        this.print('(');
        this.incrementIndent();
        this.println();
        x.getSubQuery().accept(this);
        this.decrementIndent();
        this.println();
        this.print(')');
        return false;
    }

    public boolean visit(SQLAlterTableAlterColumn x) {
        boolean odps = this.isOdps();
        if (odps) {
            this.print0(this.ucase ? "CHANGE COLUMN " : "change column ");
        } else {
            this.print0(this.ucase ? "ALTER COLUMN " : "alter column ");
        }

        x.getColumn().accept(this);
        if (x.isSetNotNull()) {
            this.print0(this.ucase ? " SET NOT NULL" : " set not null");
        }

        if (x.isDropNotNull()) {
            this.print0(this.ucase ? " DROP NOT NULL" : " drop not null");
        }

        if (x.getSetDefault() != null) {
            this.print0(this.ucase ? " SET DEFAULT " : " set default ");
            x.getSetDefault().accept(this);
        }

        if (x.isDropDefault()) {
            this.print0(this.ucase ? " DROP DEFAULT" : " drop default");
        }

        return false;
    }

    public boolean visit(SQLCheck x) {
        if (x.getName() != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            x.getName().accept(this);
            this.print(' ');
        }

        this.print0(this.ucase ? "CHECK (" : "check (");
        this.incrementIndent();
        x.getExpr().accept(this);
        this.decrementIndent();
        this.print(')');
        return false;
    }

    public boolean visit(SQLAlterTableDropForeignKey x) {
        this.print0(this.ucase ? "DROP FOREIGN KEY " : "drop foreign key ");
        x.getIndexName().accept(this);
        return false;
    }

    public boolean visit(SQLAlterTableDropPrimaryKey x) {
        this.print0(this.ucase ? "DROP PRIMARY KEY" : "drop primary key");
        return false;
    }

    public boolean visit(SQLAlterTableDropKey x) {
        this.print0(this.ucase ? "DROP KEY " : "drop key ");
        x.getKeyName().accept(this);
        return false;
    }

    public boolean visit(SQLAlterTableEnableKeys x) {
        this.print0(this.ucase ? "ENABLE KEYS" : "enable keys");
        return false;
    }

    public boolean visit(SQLAlterTableDisableKeys x) {
        this.print0(this.ucase ? "DISABLE KEYS" : "disable keys");
        return false;
    }

    public boolean visit(SQLAlterTableDisableConstraint x) {
        this.print0(this.ucase ? "DISABLE CONSTRAINT " : "disable constraint ");
        x.getConstraintName().accept(this);
        return false;
    }

    public boolean visit(SQLAlterTableEnableConstraint x) {
        this.print0(this.ucase ? "ENABLE CONSTRAINT " : "enable constraint ");
        x.getConstraintName().accept(this);
        return false;
    }

    public boolean visit(SQLAlterTableDropConstraint x) {
        this.print0(this.ucase ? "DROP CONSTRAINT " : "drop constraint ");
        x.getConstraintName().accept(this);
        return false;
    }

    public boolean visit(SQLAlterTableStatement x) {
        this.print0(this.ucase ? "ALTER TABLE " : "alter table ");
        this.printTableSourceExpr(x.getName());
        this.incrementIndent();

        for(int i = 0; i < x.getItems().size(); ++i) {
            SQLAlterTableItem item = (SQLAlterTableItem)x.getItems().get(i);
            if (i != 0) {
                this.print(',');
            }

            this.println();
            item.accept(this);
        }

        this.decrementIndent();
        if (x.isMergeSmallFiles()) {
            this.print0(this.ucase ? " MERGE SMALLFILES" : " merge smallfiles");
        }

        return false;
    }

    public boolean visit(SQLExprHint x) {
        x.getExpr().accept(this);
        return false;
    }

    public boolean visit(SQLCreateIndexStatement x) {
        this.print0(this.ucase ? "CREATE " : "create ");
        if (x.getType() != null) {
            this.print0(x.getType());
            this.print(' ');
        }

        this.print0(this.ucase ? "INDEX " : "index ");
        x.getName().accept(this);
        this.print0(this.ucase ? " ON " : " on ");
        x.getTable().accept(this);
        this.print0(" (");
        this.printAndAccept(x.getItems(), ", ");
        this.print(')');
        if (x.getUsing() != null) {
            this.print0(this.ucase ? " USING " : " using ");
            this.print0(x.getUsing());
        }

        return false;
    }

    public boolean visit(SQLUnique x) {
        if (x.getName() != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            x.getName().accept(this);
            this.print(' ');
        }

        this.print0(this.ucase ? "UNIQUE (" : "unique (");
        this.printAndAccept(x.getColumns(), ", ");
        this.print(')');
        return false;
    }

    public boolean visit(SQLPrimaryKeyImpl x) {
        if (x.getName() != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            x.getName().accept(this);
            this.print(' ');
        }

        this.print0(this.ucase ? "PRIMARY KEY (" : "primary key (");
        this.printAndAccept(x.getColumns(), ", ");
        this.print(')');
        return false;
    }

    public boolean visit(SQLAlterTableRenameColumn x) {
        this.print0(this.ucase ? "RENAME COLUMN " : "rename column ");
        x.getColumn().accept(this);
        this.print0(this.ucase ? " TO " : " to ");
        x.getTo().accept(this);
        return false;
    }

    public boolean visit(SQLColumnReference x) {
        if (x.getName() != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            x.getName().accept(this);
            this.print(' ');
        }

        this.print0(this.ucase ? "REFERENCES " : "references ");
        x.getTable().accept(this);
        this.print0(" (");
        this.printAndAccept(x.getColumns(), ", ");
        this.print(')');
        return false;
    }

    public boolean visit(SQLForeignKeyImpl x) {
        if (x.getName() != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            x.getName().accept(this);
            this.print(' ');
        }

        this.print0(this.ucase ? "FOREIGN KEY (" : "foreign key (");
        this.printAndAccept(x.getReferencingColumns(), ", ");
        this.print(')');
        this.print0(this.ucase ? " REFERENCES " : " references ");
        x.getReferencedTableName().accept(this);
        if (x.getReferencedColumns().size() > 0) {
            this.print0(" (");
            this.printAndAccept(x.getReferencedColumns(), ", ");
            this.print(')');
        }

        return false;
    }

    public boolean visit(SQLDropSequenceStatement x) {
        this.print0(this.ucase ? "DROP SEQUENCE " : "drop sequence ");
        x.getName().accept(this);
        return false;
    }

    public void endVisit(SQLDropSequenceStatement x) {
    }

    public boolean visit(SQLDropTriggerStatement x) {
        this.print0(this.ucase ? "DROP TRIGGER " : "drop trigger ");
        x.getName().accept(this);
        return false;
    }

    public void endVisit(SQLDropUserStatement x) {
    }

    public boolean visit(SQLDropUserStatement x) {
        this.print0(this.ucase ? "DROP USER " : "drop user ");
        this.printAndAccept(x.getUsers(), ", ");
        return false;
    }

    public boolean visit(SQLExplainStatement x) {
        this.print0(this.ucase ? "EXPLAIN" : "explain");
        if (x.getHints() != null && x.getHints().size() > 0) {
            this.print(' ');
            this.printAndAccept(x.getHints(), " ");
        }

        this.println();
        x.getStatement().accept(this);
        return false;
    }

    public boolean visit(SQLGrantStatement x) {
        this.print0(this.ucase ? "GRANT " : "grant ");
        this.printAndAccept(x.getPrivileges(), ", ");
        this.printGrantOn(x);
        if (x.getTo() != null) {
            this.print0(this.ucase ? " TO " : " to ");
            x.getTo().accept(this);
        }

        boolean with = false;
        if (x.getMaxQueriesPerHour() != null) {
            if (!with) {
                this.print0(this.ucase ? " WITH" : " with");
                with = true;
            }

            this.print0(this.ucase ? " MAX_QUERIES_PER_HOUR " : " max_queries_per_hour ");
            x.getMaxQueriesPerHour().accept(this);
        }

        if (x.getMaxUpdatesPerHour() != null) {
            if (!with) {
                this.print0(this.ucase ? " WITH" : " with");
                with = true;
            }

            this.print0(this.ucase ? " MAX_UPDATES_PER_HOUR " : " max_updates_per_hour ");
            x.getMaxUpdatesPerHour().accept(this);
        }

        if (x.getMaxConnectionsPerHour() != null) {
            if (!with) {
                this.print0(this.ucase ? " WITH" : " with");
                with = true;
            }

            this.print0(this.ucase ? " MAX_CONNECTIONS_PER_HOUR " : " max_connections_per_hour ");
            x.getMaxConnectionsPerHour().accept(this);
        }

        if (x.getMaxUserConnections() != null) {
            if (!with) {
                this.print0(this.ucase ? " WITH" : " with");
                with = true;
            }

            this.print0(this.ucase ? " MAX_USER_CONNECTIONS " : " max_user_connections ");
            x.getMaxUserConnections().accept(this);
        }

        if (x.isAdminOption()) {
            if (!with) {
                this.print0(this.ucase ? " WITH" : " with");
                with = true;
            }

            this.print0(this.ucase ? " ADMIN OPTION" : " admin option");
        }

        if (x.getIdentifiedBy() != null) {
            this.print0(this.ucase ? " IDENTIFIED BY " : " identified by ");
            x.getIdentifiedBy().accept(this);
        }

        return false;
    }

    protected void printGrantOn(SQLGrantStatement x) {
        if (x.getOn() != null) {
            this.print0(this.ucase ? " ON " : " on ");
            SQLObjectType objectType = x.getObjectType();
            if (objectType != null) {
                this.print0(this.ucase ? objectType.name : objectType.name_lcase);
                this.print(' ');
            }

            x.getOn().accept(this);
        }

    }

    public boolean visit(SQLRevokeStatement x) {
        this.print0(this.ucase ? "ROVOKE " : "rovoke ");
        this.printAndAccept(x.getPrivileges(), ", ");
        if (x.getOn() != null) {
            this.print0(this.ucase ? " ON " : " on ");
            if (x.getObjectType() != null) {
                this.print0(x.getObjectType().name());
                this.print(' ');
            }

            x.getOn().accept(this);
        }

        if (x.getFrom() != null) {
            this.print0(this.ucase ? " FROM " : " from ");
            x.getFrom().accept(this);
        }

        return false;
    }

    public boolean visit(SQLDropDatabaseStatement x) {
        this.print0(this.ucase ? "DROP DATABASE " : "drop databasE ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }

        x.getDatabase().accept(this);
        return false;
    }

    public boolean visit(SQLDropFunctionStatement x) {
        this.print0(this.ucase ? "DROP FUNCTION " : "drop function ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }

        x.getName().accept(this);
        return false;
    }

    public boolean visit(SQLDropTableSpaceStatement x) {
        this.print0(this.ucase ? "DROP TABLESPACE " : "drop tablespace ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }

        x.getName().accept(this);
        return false;
    }

    public boolean visit(SQLDropProcedureStatement x) {
        this.print0(this.ucase ? "DROP PROCEDURE " : "drop procedure ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }

        x.getName().accept(this);
        return false;
    }

    public boolean visit(SQLAlterTableAddIndex x) {
        this.print0(this.ucase ? "ADD " : "add ");
        if (x.getType() != null) {
            this.print0(x.getType());
            this.print(' ');
        }

        if (x.isUnique()) {
            this.print0(this.ucase ? "UNIQUE " : "unique ");
        }

        if (x.isKey()) {
            this.print0(this.ucase ? "KEY " : "key ");
        } else {
            this.print0(this.ucase ? "INDEX " : "index ");
        }

        if (x.getName() != null) {
            x.getName().accept(this);
            this.print(' ');
        }

        this.print('(');
        this.printAndAccept(x.getItems(), ", ");
        this.print(')');
        if (x.getUsing() != null) {
            this.print0(this.ucase ? " USING " : " using ");
            this.print0(x.getUsing());
        }

        return false;
    }

    public boolean visit(SQLAlterTableAddConstraint x) {
        if (x.isWithNoCheck()) {
            this.print0(this.ucase ? "WITH NOCHECK " : "with nocheck ");
        }

        this.print0(this.ucase ? "ADD " : "add ");
        x.getConstraint().accept(this);
        return false;
    }

    public boolean visit(SQLCreateTriggerStatement x) {
        this.print0(this.ucase ? "CREATE " : "create ");
        if (x.isOrReplace()) {
            this.print0(this.ucase ? "OR REPLEACE " : "or repleace ");
        }

        this.print0(this.ucase ? "TRIGGER " : "trigger ");
        x.getName().accept(this);
        this.incrementIndent();
        this.println();
        if (TriggerType.INSTEAD_OF.equals(x.getTriggerType())) {
            this.print0(this.ucase ? "INSTEAD OF" : "instead of");
        } else {
            String triggerTypeName = x.getTriggerType().name();
            this.print0(this.ucase ? triggerTypeName : triggerTypeName.toLowerCase());
        }

        Iterator var4 = x.getTriggerEvents().iterator();

        while(var4.hasNext()) {
            TriggerEvent event = (TriggerEvent)var4.next();
            this.print(' ');
            this.print0(event.name());
        }

        this.println();
        this.print0(this.ucase ? "ON " : "on ");
        x.getOn().accept(this);
        if (x.isForEachRow()) {
            this.println();
            this.print0(this.ucase ? "FOR EACH ROW" : "for each row");
        }

        this.decrementIndent();
        this.println();
        x.getBody().accept(this);
        return false;
    }

    public boolean visit(SQLBooleanExpr x) {
        this.print0(x.getValue() ? "true" : "false");
        return false;
    }

    public void endVisit(SQLBooleanExpr x) {
    }

    public boolean visit(SQLUnionQueryTableSource x) {
        this.print('(');
        this.incrementIndent();
        this.println();
        x.getUnion().accept(this);
        this.decrementIndent();
        this.println();
        this.print(')');
        if (x.getAlias() != null) {
            this.print(' ');
            this.print0(x.getAlias());
        }

        return false;
    }

    public boolean visit(SQLTimestampExpr x) {
        this.print0(this.ucase ? "TIMESTAMP " : "timestamp ");
        if (x.isWithTimeZone()) {
            this.print0(this.ucase ? " WITH TIME ZONE " : " with time zone ");
        }

        this.print('\'');
        this.print0(x.getLiteral());
        this.print('\'');
        if (x.getTimeZone() != null) {
            this.print0(this.ucase ? " AT TIME ZONE '" : " at time zone '");
            this.print0(x.getTimeZone());
            this.print('\'');
        }

        return false;
    }

    public boolean visit(SQLBinaryExpr x) {
        this.print0("b'");
        this.print0(x.getValue());
        this.print('\'');
        return false;
    }

    public boolean visit(SQLAlterTableRename x) {
        this.print0(this.ucase ? "RENAME TO " : "rename to ");
        x.getTo().accept(this);
        return false;
    }

    public boolean visit(SQLShowTablesStatement x) {
        this.print0(this.ucase ? "SHOW TABLES" : "show tables");
        if (x.getDatabase() != null) {
            this.print0(this.ucase ? " FROM " : " from ");
            x.getDatabase().accept(this);
        }

        if (x.getLike() != null) {
            this.print0(this.ucase ? " LIKE " : " like ");
            x.getLike().accept(this);
        }

        return false;
    }

    protected void printComment(List<String> comments, String seperator) {
        if (comments != null) {
            for(int i = 0; i < comments.size(); ++i) {
                if (i != 0) {
                    this.print0(seperator);
                }

                String comment = (String)comments.get(i);
                this.print0(comment);
            }
        }

    }

    protected void printlnComments(List<String> comments) {
        if (comments != null) {
            for(int i = 0; i < comments.size(); ++i) {
                String comment = (String)comments.get(i);
                this.print0(comment);
                this.println();
            }
        }

    }

    public boolean visit(SQLAlterViewRenameStatement x) {
        this.print0(this.ucase ? "ALTER VIEW " : "alter view ");
        x.getName().accept(this);
        this.print0(this.ucase ? " RENAME TO " : " rename to ");
        x.getTo().accept(this);
        return false;
    }

    public boolean visit(SQLAlterTableAddPartition x) {
        this.print0(this.ucase ? "ADD " : "add ");
        if (x.isIfNotExists()) {
            this.print0(this.ucase ? "IF NOT EXISTS " : "if not exists ");
        }

        if (x.getPartitionCount() != null) {
            this.print0(this.ucase ? "PARTITION PARTITIONS " : "partition partitions ");
            x.getPartitionCount().accept(this);
        }

        if (x.getPartitions().size() > 0) {
            this.print0(this.ucase ? "PARTITION (" : "partition (");
            this.printAndAccept(x.getPartitions(), ", ");
            this.print(')');
        }

        return false;
    }

    public boolean visit(SQLAlterTableReOrganizePartition x) {
        this.print0(this.ucase ? "REORGANIZE " : "reorganize ");
        this.printAndAccept(x.getNames(), ", ");
        this.print0(this.ucase ? " INTO (" : " into (");
        this.printAndAccept(x.getPartitions(), ", ");
        this.print(')');
        return false;
    }

    public boolean visit(SQLAlterTableDropPartition x) {
        this.print0(this.ucase ? "DROP " : "drop ");
        if (x.isIfExists()) {
            this.print0(this.ucase ? "IF EXISTS " : "if exists ");
        }

        this.print0(this.ucase ? "PARTITION " : "partition ");
        if (x.getPartitions().size() == 1 && x.getPartitions().get(0) instanceof SQLName) {
            ((SQLObject)x.getPartitions().get(0)).accept(this);
        } else {
            this.print('(');
            this.printAndAccept(x.getPartitions(), ", ");
            this.print(')');
        }

        if (x.isPurge()) {
            this.print0(this.ucase ? " PURGE" : " purge");
        }

        return false;
    }

    public boolean visit(SQLAlterTableRenamePartition x) {
        this.print0(this.ucase ? "PARTITION (" : "partition (");
        this.printAndAccept(x.getPartition(), ", ");
        this.print0(this.ucase ? ") RENAME TO PARTITION(" : ") rename to partition(");
        this.printAndAccept(x.getTo(), ", ");
        this.print(')');
        return false;
    }

    public boolean visit(SQLAlterTableSetComment x) {
        this.print0(this.ucase ? "SET COMMENT " : "set comment ");
        x.getComment().accept(this);
        return false;
    }

    public boolean visit(SQLAlterTableSetLifecycle x) {
        this.print0(this.ucase ? "SET LIFECYCLE " : "set lifecycle ");
        x.getLifecycle().accept(this);
        return false;
    }

    public boolean visit(SQLAlterTableEnableLifecycle x) {
        if (x.getPartition().size() != 0) {
            this.print0(this.ucase ? "PARTITION (" : "partition (");
            this.printAndAccept(x.getPartition(), ", ");
            this.print0(") ");
        }

        this.print0(this.ucase ? "ENABLE LIFECYCLE" : "enable lifecycle");
        return false;
    }

    public boolean visit(SQLAlterTableDisableLifecycle x) {
        if (x.getPartition().size() != 0) {
            this.print0(this.ucase ? "PARTITION (" : "partition (");
            this.printAndAccept(x.getPartition(), ", ");
            this.print0(") ");
        }

        this.print0(this.ucase ? "DISABLE LIFECYCLE" : "disable lifecycle");
        return false;
    }

    public boolean visit(SQLAlterTableTouch x) {
        this.print0(this.ucase ? "TOUCH" : "touch");
        if (x.getPartition().size() != 0) {
            this.print0(this.ucase ? " PARTITION (" : " partition (");
            this.printAndAccept(x.getPartition(), ", ");
            this.print(')');
        }

        return false;
    }

    public boolean visit(SQLArrayExpr x) {
        x.getExpr().accept(this);
        this.print('[');
        this.printAndAccept(x.getValues(), ", ");
        this.print(']');
        return false;
    }

    public boolean visit(SQLOpenStatement x) {
        this.print0(this.ucase ? "OPEN " : "open ");
        this.print0(x.getCursorName());
        return false;
    }

    public boolean visit(SQLFetchStatement x) {
        this.print0(this.ucase ? "FETCH " : "fetch ");
        x.getCursorName().accept(this);
        this.print0(this.ucase ? " INTO " : " into ");
        this.printAndAccept(x.getInto(), ", ");
        return false;
    }

    public boolean visit(SQLCloseStatement x) {
        this.print0(this.ucase ? "CLOSE " : "close ");
        this.print0(x.getCursorName());
        return false;
    }

    public boolean visit(SQLGroupingSetExpr x) {
        this.print0(this.ucase ? "GROUPING SETS" : "grouping sets");
        this.print0(" (");
        this.printAndAccept(x.getParameters(), ", ");
        this.print(')');
        return false;
    }

    public boolean visit(SQLIfStatement x) {
        this.print0(this.ucase ? "IF " : "if ");
        x.getCondition().accept(this);
        this.incrementIndent();
        this.println();
        int i = 0;

        for(int size = x.getStatements().size(); i < size; ++i) {
            SQLStatement item = (SQLStatement)x.getStatements().get(i);
            item.setParent(x);
            item.accept(this);
            if (i != size - 1) {
                this.println();
            }
        }

        this.decrementIndent();
        Iterator var5 = x.getElseIfList().iterator();

        while(var5.hasNext()) {
            ElseIf elseIf = (ElseIf)var5.next();
            this.println();
            elseIf.accept(this);
        }

        if (x.getElseItem() != null) {
            this.println();
            x.getElseItem().accept(this);
        }

        return false;
    }

    public boolean visit(Else x) {
        this.print0(this.ucase ? "ELSE" : "else");
        this.incrementIndent();
        this.println();
        int i = 0;

        for(int size = x.getStatements().size(); i < size; ++i) {
            if (i != 0) {
                this.println();
            }

            SQLStatement item = (SQLStatement)x.getStatements().get(i);
            item.setParent(x);
            item.accept(this);
        }

        this.decrementIndent();
        return false;
    }

    public boolean visit(ElseIf x) {
        this.print0(this.ucase ? "ELSE IF" : "else if");
        x.getCondition().accept(this);
        this.print0(this.ucase ? " THEN" : " then");
        this.incrementIndent();
        this.println();
        int i = 0;

        for(int size = x.getStatements().size(); i < size; ++i) {
            if (i != 0) {
                this.println();
            }

            SQLStatement item = (SQLStatement)x.getStatements().get(i);
            item.setParent(x);
            item.accept(this);
        }

        this.decrementIndent();
        return false;
    }

    public boolean visit(SQLLoopStatement x) {
        this.print0(this.ucase ? "LOOP" : "loop");
        this.incrementIndent();
        this.println();
        int i = 0;

        for(int size = x.getStatements().size(); i < size; ++i) {
            SQLStatement item = (SQLStatement)x.getStatements().get(i);
            item.setParent(x);
            item.accept(this);
            if (i != size - 1) {
                this.println();
            }
        }

        this.decrementIndent();
        this.println();
        this.print0(this.ucase ? "END LOOP" : "end loop");
        return false;
    }

    public boolean visit(SQLParameter x) {
        if (x.getDataType().getName().equalsIgnoreCase("CURSOR")) {
            this.print0(this.ucase ? "CURSOR " : "cursor ");
            x.getName().accept(this);
            this.print0(this.ucase ? " IS" : " is");
            this.incrementIndent();
            this.println();
            SQLSelect select = ((SQLQueryExpr)x.getDefaultValue()).getSubQuery();
            select.accept(this);
            this.decrementIndent();
        } else {
            if (x.getParamType() == ParameterType.IN) {
                this.print0(this.ucase ? "IN " : "in ");
            } else if (x.getParamType() == ParameterType.OUT) {
                this.print0(this.ucase ? "OUT " : "out ");
            } else if (x.getParamType() == ParameterType.INOUT) {
                this.print0(this.ucase ? "INOUT " : "inout ");
            }

            x.getName().accept(this);
            this.print(' ');
            x.getDataType().accept(this);
            if (x.getDefaultValue() != null) {
                this.print0(" := ");
                x.getDefaultValue().accept(this);
            }
        }

        return false;
    }

    public boolean visit(SQLDeclareItem x) {
        x.getName().accept(this);
        if (x.getType() == com.tranboot.client.druid.sql.ast.SQLDeclareItem.Type.TABLE) {
            this.print0(this.ucase ? " TABLE" : " table");
            int size = x.getTableElementList().size();
            if (size > 0) {
                this.print0(" (");
                this.incrementIndent();
                this.println();

                for(int i = 0; i < size; ++i) {
                    if (i != 0) {
                        this.print(',');
                        this.println();
                    }

                    ((SQLTableElement)x.getTableElementList().get(i)).accept(this);
                }

                this.decrementIndent();
                this.println();
                this.print(')');
            }
        } else if (x.getType() == com.tranboot.client.druid.sql.ast.SQLDeclareItem.Type.CURSOR) {
            this.print0(this.ucase ? " CURSOR" : " cursor");
        } else {
            if (x.getDataType() != null) {
                this.print(' ');
                x.getDataType().accept(this);
            }

            if (x.getValue() != null) {
                if ("mysql".equals(this.getDbType())) {
                    this.print0(this.ucase ? " DEFAULT " : " default ");
                } else {
                    this.print0(" = ");
                }

                x.getValue().accept(this);
            }
        }

        return false;
    }

    public boolean visit(SQLPartitionValue x) {
        if (x.getOperator() == Operator.LessThan && !"oracle".equals(this.getDbType()) && x.getItems().size() == 1 && x.getItems().get(0) instanceof SQLIdentifierExpr) {
            SQLIdentifierExpr ident = (SQLIdentifierExpr)x.getItems().get(0);
            if ("MAXVALUE".equalsIgnoreCase(ident.getName())) {
                this.print0(this.ucase ? "VALUES LESS THAN MAXVALUE" : "values less than maxvalue");
                return false;
            }
        }

        if (x.getOperator() == Operator.LessThan) {
            this.print0(this.ucase ? "VALUES LESS THAN (" : "values less than (");
        } else if (x.getOperator() == Operator.In) {
            this.print0(this.ucase ? "VALUES IN (" : "values in (");
        } else {
            this.print(this.ucase ? "VALUES (" : "values (");
        }

        this.printAndAccept(x.getItems(), ", ");
        this.print(')');
        return false;
    }

    public String getDbType() {
        return this.dbType;
    }

    public boolean isUppCase() {
        return this.ucase;
    }

    public void setUppCase(boolean val) {
        this.ucase = val;
    }

    public boolean visit(SQLPartition x) {
        this.print0(this.ucase ? "PARTITION " : "partition ");
        x.getName().accept(this);
        if (x.getValues() != null) {
            this.print(' ');
            x.getValues().accept(this);
        }

        if (x.getDataDirectory() != null) {
            this.incrementIndent();
            this.println();
            this.print0(this.ucase ? "DATA DIRECTORY " : "data directory ");
            x.getDataDirectory().accept(this);
            this.decrementIndent();
        }

        if (x.getIndexDirectory() != null) {
            this.incrementIndent();
            this.println();
            this.print0(this.ucase ? "INDEX DIRECTORY " : "index directory ");
            x.getIndexDirectory().accept(this);
            this.decrementIndent();
        }

        if (x.getTableSpace() != null) {
            this.print0(this.ucase ? " TABLESPACE " : " tablespace ");
            x.getTableSpace().accept(this);
        }

        if (x.getEngine() != null) {
            this.print0(this.ucase ? " STORAGE ENGINE " : " storage engine ");
            x.getEngine().accept(this);
        }

        if (x.getMaxRows() != null) {
            this.print0(this.ucase ? " MAX_ROWS " : " max_rows ");
            x.getMaxRows().accept(this);
        }

        if (x.getMinRows() != null) {
            this.print0(this.ucase ? " MIN_ROWS " : " min_rows ");
            x.getMinRows().accept(this);
        }

        if (x.getComment() != null) {
            this.print0(this.ucase ? " COMMENT " : " comment ");
            x.getComment().accept(this);
        }

        if (x.getSubPartitionsCount() != null) {
            this.incrementIndent();
            this.println();
            this.print0(this.ucase ? "SUBPARTITIONS " : "subpartitions ");
            x.getSubPartitionsCount().accept(this);
            this.decrementIndent();
        }

        if (x.getSubPartitions().size() > 0) {
            this.println();
            this.print('(');
            this.incrementIndent();

            for(int i = 0; i < x.getSubPartitions().size(); ++i) {
                if (i != 0) {
                    this.print(',');
                }

                this.println();
                ((SQLSubPartition)x.getSubPartitions().get(i)).accept(this);
            }

            this.decrementIndent();
            this.println();
            this.print(')');
        }

        return false;
    }

    public boolean visit(SQLPartitionByRange x) {
        this.print0(this.ucase ? "PARTITION BY RANGE" : "partition by range");
        if (x.getExpr() != null) {
            this.print0(" (");
            x.getExpr().accept(this);
            this.print(')');
        } else {
            if ("mysql".equals(this.getDbType())) {
                this.print0(this.ucase ? " COLUMNS (" : " columns (");
            } else {
                this.print0(" (");
            }

            this.printAndAccept(x.getColumns(), ", ");
            this.print(')');
        }

        if (x.getInterval() != null) {
            this.print0(this.ucase ? " INTERVAL " : " interval ");
            x.getInterval().accept(this);
        }

        this.printPartitionsCountAndSubPartitions(x);
        this.println();
        this.print('(');
        this.incrementIndent();
        int i = 0;

        for(int size = x.getPartitions().size(); i < size; ++i) {
            if (i != 0) {
                this.print(',');
            }

            this.println();
            ((SQLPartition)x.getPartitions().get(i)).accept(this);
        }

        this.decrementIndent();
        this.println();
        this.print(')');
        return false;
    }

    public boolean visit(SQLPartitionByList x) {
        this.print0(this.ucase ? "PARTITION BY LIST " : "partition by list ");
        if (x.getExpr() != null) {
            this.print('(');
            x.getExpr().accept(this);
            this.print0(")");
        } else {
            this.print0(this.ucase ? "COLUMNS (" : "columns (");
            this.printAndAccept(x.getColumns(), ", ");
            this.print0(")");
        }

        this.printPartitionsCountAndSubPartitions(x);
        List<SQLPartition> partitions = x.getPartitions();
        int partitionsSize = partitions.size();
        if (partitionsSize > 0) {
            this.println();
            this.incrementIndent();
            this.print('(');

            for(int i = 0; i < partitionsSize; ++i) {
                this.println();
                ((SQLPartition)partitions.get(i)).accept(this);
                if (i != partitionsSize - 1) {
                    this.print0(", ");
                }
            }

            this.decrementIndent();
            this.println();
            this.print(')');
        }

        return false;
    }

    public boolean visit(SQLPartitionByHash x) {
        if (x.isLinear()) {
            this.print0(this.ucase ? "PARTITION BY LINEAR HASH " : "partition by linear hash ");
        } else {
            this.print0(this.ucase ? "PARTITION BY HASH " : "partition by hash ");
        }

        if (x.isKey()) {
            this.print0(this.ucase ? "KEY" : "key");
        }

        this.print('(');
        x.getExpr().accept(this);
        this.print(')');
        this.printPartitionsCountAndSubPartitions(x);
        return false;
    }

    protected void printPartitionsCountAndSubPartitions(SQLPartitionBy x) {
        if (x.getPartitionsCount() != null) {
            if (Boolean.TRUE.equals(x.getAttribute("ads.partition"))) {
                this.print0(this.ucase ? " PARTITION NUM " : " partition num ");
            } else {
                this.print0(this.ucase ? " PARTITIONS " : " partitions ");
            }

            x.getPartitionsCount().accept(this);
        }

        if (x.getSubPartitionBy() != null) {
            this.println();
            x.getSubPartitionBy().accept(this);
        }

        if (x.getStoreIn().size() > 0) {
            this.println();
            this.print0(this.ucase ? "STORE IN (" : "store in (");
            this.printAndAccept(x.getStoreIn(), ", ");
            this.print(')');
        }

    }

    public boolean visit(SQLSubPartitionByHash x) {
        if (x.isLinear()) {
            this.print0(this.ucase ? "SUBPARTITION BY LINEAR HASH " : "subpartition by linear hash ");
        } else {
            this.print0(this.ucase ? "SUBPARTITION BY HASH " : "subpartition by hash ");
        }

        if (x.isKey()) {
            this.print0(this.ucase ? "KEY" : "key");
        }

        this.print('(');
        x.getExpr().accept(this);
        this.print(')');
        if (x.getSubPartitionsCount() != null) {
            this.print0(this.ucase ? " SUBPARTITIONS " : " subpartitions ");
            x.getSubPartitionsCount().accept(this);
        }

        return false;
    }

    public boolean visit(SQLSubPartitionByList x) {
        if (x.isLinear()) {
            this.print0(this.ucase ? "SUBPARTITION BY LINEAR HASH " : "subpartition by linear hash ");
        } else {
            this.print0(this.ucase ? "SUBPARTITION BY HASH " : "subpartition by hash ");
        }

        this.print('(');
        x.getColumn().accept(this);
        this.print(')');
        if (x.getSubPartitionsCount() != null) {
            this.print0(this.ucase ? " SUBPARTITIONS " : " subpartitions ");
            x.getSubPartitionsCount().accept(this);
        }

        if (x.getSubPartitionTemplate().size() > 0) {
            this.incrementIndent();
            this.println();
            this.print0(this.ucase ? "SUBPARTITION TEMPLATE (" : "subpartition template (");
            this.incrementIndent();
            this.println();
            this.printlnAndAccept(x.getSubPartitionTemplate(), ",");
            this.decrementIndent();
            this.println();
            this.print(')');
            this.decrementIndent();
        }

        return false;
    }

    public boolean visit(SQLSubPartition x) {
        this.print0(this.ucase ? "SUBPARTITION " : "subpartition ");
        x.getName().accept(this);
        if (x.getValues() != null) {
            this.print(' ');
            x.getValues().accept(this);
        }

        return false;
    }

    public boolean visit(SQLAlterDatabaseStatement x) {
        this.print0(this.ucase ? "ALTER DATABASE " : "alter database ");
        x.getName().accept(this);
        if (x.isUpgradeDataDirectoryName()) {
            this.print0(this.ucase ? " UPGRADE DATA DIRECTORY NAME" : " upgrade data directory name");
        }

        return false;
    }

    public boolean visit(SQLAlterTableConvertCharSet x) {
        this.print0(this.ucase ? "CONVERT TO CHARACTER SET " : "convert to character set ");
        x.getCharset().accept(this);
        if (x.getCollate() != null) {
            this.print0(this.ucase ? "COLLATE " : "collate ");
            x.getCollate().accept(this);
        }

        return false;
    }

    public boolean visit(SQLAlterTableCoalescePartition x) {
        this.print0(this.ucase ? "COALESCE PARTITION " : "coalesce partition ");
        x.getCount().accept(this);
        return false;
    }

    public boolean visit(SQLAlterTableTruncatePartition x) {
        this.print0(this.ucase ? "TRUNCATE PARTITION " : "truncate partition ");
        this.printPartitions(x.getPartitions());
        return false;
    }

    public boolean visit(SQLAlterTableDiscardPartition x) {
        this.print0(this.ucase ? "DISCARD PARTITION " : "discard partition ");
        this.printPartitions(x.getPartitions());
        return false;
    }

    public boolean visit(SQLAlterTableImportPartition x) {
        this.print0(this.ucase ? "IMPORT PARTITION " : "import partition ");
        this.printPartitions(x.getPartitions());
        return false;
    }

    public boolean visit(SQLAlterTableAnalyzePartition x) {
        this.print0(this.ucase ? "ANALYZE PARTITION " : "analyze partition ");
        this.printPartitions(x.getPartitions());
        return false;
    }

    protected void printPartitions(List<SQLName> partitions) {
        if (partitions.size() == 1 && "ALL".equalsIgnoreCase(((SQLName)partitions.get(0)).getSimpleName())) {
            this.print0(this.ucase ? "ALL" : "all");
        } else {
            this.printAndAccept(partitions, ", ");
        }

    }

    public boolean visit(SQLAlterTableCheckPartition x) {
        this.print0(this.ucase ? "CHECK PARTITION " : "check partition ");
        this.printPartitions(x.getPartitions());
        return false;
    }

    public boolean visit(SQLAlterTableOptimizePartition x) {
        this.print0(this.ucase ? "OPTIMIZE PARTITION " : "optimize partition ");
        this.printPartitions(x.getPartitions());
        return false;
    }

    public boolean visit(SQLAlterTableRebuildPartition x) {
        this.print0(this.ucase ? "REBUILD PARTITION " : "rebuild partition ");
        this.printPartitions(x.getPartitions());
        return false;
    }

    public boolean visit(SQLAlterTableRepairPartition x) {
        this.print0(this.ucase ? "REPAIR PARTITION " : "repair partition ");
        this.printPartitions(x.getPartitions());
        return false;
    }

    public boolean visit(SQLSequenceExpr x) {
        x.getSequence().accept(this);
        this.print('.');
        this.print0(this.ucase ? x.getFunction().name : x.getFunction().name_lcase);
        return false;
    }

    public boolean visit(SQLMergeStatement x) {
        this.print0(this.ucase ? "MERGE " : "merge ");
        if (x.getHints().size() > 0) {
            this.printAndAccept(x.getHints(), ", ");
            this.print(' ');
        }

        this.print0(this.ucase ? "INTO " : "into ");
        x.getInto().accept(this);
        if (x.getAlias() != null) {
            this.print(' ');
            this.print0(x.getAlias());
        }

        this.println();
        this.print0(this.ucase ? "USING " : "using ");
        x.getUsing().accept(this);
        this.print0(this.ucase ? " ON (" : " on (");
        x.getOn().accept(this);
        this.print0(") ");
        if (x.getUpdateClause() != null) {
            this.println();
            x.getUpdateClause().accept(this);
        }

        if (x.getInsertClause() != null) {
            this.println();
            x.getInsertClause().accept(this);
        }

        if (x.getErrorLoggingClause() != null) {
            this.println();
            x.getErrorLoggingClause().accept(this);
        }

        return false;
    }

    public boolean visit(MergeUpdateClause x) {
        this.print0(this.ucase ? "WHEN MATCHED THEN UPDATE SET " : "when matched then update set ");
        this.printAndAccept(x.getItems(), ", ");
        if (x.getWhere() != null) {
            this.incrementIndent();
            this.println();
            this.print0(this.ucase ? "WHERE " : "where ");
            x.getWhere().setParent(x);
            x.getWhere().accept(this);
            this.decrementIndent();
        }

        if (x.getDeleteWhere() != null) {
            this.incrementIndent();
            this.println();
            this.print0(this.ucase ? "DELETE WHERE " : "delete where ");
            x.getDeleteWhere().setParent(x);
            x.getDeleteWhere().accept(this);
            this.decrementIndent();
        }

        return false;
    }

    public boolean visit(MergeInsertClause x) {
        this.print0(this.ucase ? "WHEN NOT MATCHED THEN INSERT" : "when not matched then insert");
        if (x.getColumns().size() > 0) {
            this.print(' ');
            this.printAndAccept(x.getColumns(), ", ");
        }

        this.print0(this.ucase ? " VALUES (" : " values (");
        this.printAndAccept(x.getValues(), ", ");
        this.print(')');
        if (x.getWhere() != null) {
            this.incrementIndent();
            this.println();
            this.print0(this.ucase ? "WHERE " : "where ");
            x.getWhere().setParent(x);
            x.getWhere().accept(this);
            this.decrementIndent();
        }

        return false;
    }

    public boolean visit(SQLErrorLoggingClause x) {
        this.print0(this.ucase ? "LOG ERRORS " : "log errors ");
        if (x.getInto() != null) {
            this.print0(this.ucase ? "INTO " : "into ");
            x.getInto().accept(this);
            this.print(' ');
        }

        if (x.getSimpleExpression() != null) {
            this.print('(');
            x.getSimpleExpression().accept(this);
            this.print(')');
        }

        if (x.getLimit() != null) {
            this.print0(this.ucase ? " REJECT LIMIT " : " reject limit ");
            x.getLimit().accept(this);
        }

        return false;
    }

    public boolean visit(SQLCreateSequenceStatement x) {
        this.print0(this.ucase ? "CREATE SEQUENCE " : "create sequence ");
        x.getName().accept(this);
        if (x.getStartWith() != null) {
            this.print0(this.ucase ? " START WITH " : " start with ");
            x.getStartWith().accept(this);
        }

        if (x.getIncrementBy() != null) {
            this.print0(this.ucase ? " INCREMENT BY " : " increment by ");
            x.getIncrementBy().accept(this);
        }

        if (x.getMaxValue() != null) {
            this.print0(this.ucase ? " MAXVALUE " : " maxvalue ");
            x.getMaxValue().accept(this);
        }

        if (x.isNoMaxValue()) {
            this.print0(this.ucase ? " NOMAXVALUE" : " nomaxvalue");
        }

        if (x.getMinValue() != null) {
            this.print0(this.ucase ? " MINVALUE " : " minvalue ");
            x.getMinValue().accept(this);
        }

        if (x.isNoMinValue()) {
            this.print0(this.ucase ? " NOMINVALUE" : " nominvalue");
        }

        if (x.getCycle() != null) {
            if (x.getCycle()) {
                this.print0(this.ucase ? " CYCLE" : " cycle");
            } else {
                this.print0(this.ucase ? " NOCYCLE" : " nocycle");
            }
        }

        if (x.getCache() != null) {
            if (x.getCache()) {
                this.print0(this.ucase ? " CACHE" : " cache");
            } else {
                this.print0(this.ucase ? " NOCACHE" : " nocache");
            }
        }

        return false;
    }

    public boolean visit(SQLDateExpr x) {
        this.print0(this.ucase ? "DATE '" : "date '");
        this.print0(x.getLiteral());
        this.print('\'');
        return false;
    }

    public boolean visit(SQLLimit x) {
        this.print0(this.ucase ? "LIMIT " : "limit ");
        if (x.getOffset() != null) {
            x.getOffset().accept(this);
            this.print0(", ");
        }

        x.getRowCount().accept(this);
        return false;
    }
}

