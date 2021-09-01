package com.tranboot.client.druid.sql.visitor;

import com.tranboot.client.druid.sql.ast.SQLCommentHint;
import com.tranboot.client.druid.sql.ast.SQLDataType;
import com.tranboot.client.druid.sql.ast.SQLDeclareItem;
import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLKeep;
import com.tranboot.client.druid.sql.ast.SQLLimit;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLOrderBy;
import com.tranboot.client.druid.sql.ast.SQLOver;
import com.tranboot.client.druid.sql.ast.SQLParameter;
import com.tranboot.client.druid.sql.ast.SQLPartition;
import com.tranboot.client.druid.sql.ast.SQLPartitionByHash;
import com.tranboot.client.druid.sql.ast.SQLPartitionByList;
import com.tranboot.client.druid.sql.ast.SQLPartitionByRange;
import com.tranboot.client.druid.sql.ast.SQLPartitionValue;
import com.tranboot.client.druid.sql.ast.SQLSubPartition;
import com.tranboot.client.druid.sql.ast.SQLSubPartitionByHash;
import com.tranboot.client.druid.sql.ast.SQLSubPartitionByList;
import com.tranboot.client.druid.sql.ast.expr.SQLAggregateExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLAllColumnExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLAllExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLAnyExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLArrayExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBetweenExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOpExpr;
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
import com.tranboot.client.druid.sql.ast.statement.SQLBlockStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCallStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCharacterDataType;
import com.tranboot.client.druid.sql.ast.statement.SQLCheck;
import com.tranboot.client.druid.sql.ast.statement.SQLCloseStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnCheck;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnDefinition;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnPrimaryKey;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnReference;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnUniqueKey;
import com.tranboot.client.druid.sql.ast.statement.SQLCommentStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateDatabaseStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateProcedureStatement;
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
import com.tranboot.client.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.tranboot.client.druid.sql.ast.statement.SQLGrantStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLIfStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLInsertStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLJoinTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLLoopStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLMergeStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLNotNullConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLNullConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLOpenStatement;
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
import com.tranboot.client.druid.sql.ast.statement.SQLStartTransactionStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLTruncateStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLUnionQuery;
import com.tranboot.client.druid.sql.ast.statement.SQLUnionQueryTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLUnique;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateSetItem;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLUseStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnDefinition.Identity;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateViewStatement.Column;
import com.tranboot.client.druid.sql.ast.statement.SQLIfStatement.Else;
import com.tranboot.client.druid.sql.ast.statement.SQLIfStatement.ElseIf;
import com.tranboot.client.druid.sql.ast.statement.SQLInsertStatement.ValuesClause;
import com.tranboot.client.druid.sql.ast.statement.SQLMergeStatement.MergeInsertClause;
import com.tranboot.client.druid.sql.ast.statement.SQLMergeStatement.MergeUpdateClause;
import com.tranboot.client.druid.sql.ast.statement.SQLWithSubqueryClause.Entry;
import java.util.Iterator;

public class SQLASTVisitorAdapter implements SQLASTVisitor {
    public SQLASTVisitorAdapter() {
    }

    public void endVisit(SQLAllColumnExpr x) {
    }

    public void endVisit(SQLBetweenExpr x) {
    }

    public void endVisit(SQLBinaryOpExpr x) {
    }

    public void endVisit(SQLCaseExpr x) {
    }

    public void endVisit(Item x) {
    }

    public void endVisit(SQLCharExpr x) {
    }

    public void endVisit(SQLIdentifierExpr x) {
    }

    public void endVisit(SQLInListExpr x) {
    }

    public void endVisit(SQLIntegerExpr x) {
    }

    public void endVisit(SQLExistsExpr x) {
    }

    public void endVisit(SQLNCharExpr x) {
    }

    public void endVisit(SQLNotExpr x) {
    }

    public void endVisit(SQLNullExpr x) {
    }

    public void endVisit(SQLNumberExpr x) {
    }

    public void endVisit(SQLPropertyExpr x) {
    }

    public void endVisit(SQLSelectGroupByClause x) {
    }

    public void endVisit(SQLSelectItem x) {
    }

    public void endVisit(SQLSelectStatement selectStatement) {
    }

    public void postVisit(SQLObject astNode) {
    }

    public void preVisit(SQLObject astNode) {
    }

    public boolean visit(SQLAllColumnExpr x) {
        return true;
    }

    public boolean visit(SQLBetweenExpr x) {
        return true;
    }

    public boolean visit(SQLBinaryOpExpr x) {
        return true;
    }

    public boolean visit(SQLCaseExpr x) {
        return true;
    }

    public boolean visit(Item x) {
        return true;
    }

    public boolean visit(SQLCastExpr x) {
        return true;
    }

    public boolean visit(SQLCharExpr x) {
        return true;
    }

    public boolean visit(SQLExistsExpr x) {
        return true;
    }

    public boolean visit(SQLIdentifierExpr x) {
        return true;
    }

    public boolean visit(SQLInListExpr x) {
        return true;
    }

    public boolean visit(SQLIntegerExpr x) {
        return true;
    }

    public boolean visit(SQLNCharExpr x) {
        return true;
    }

    public boolean visit(SQLNotExpr x) {
        return true;
    }

    public boolean visit(SQLNullExpr x) {
        return true;
    }

    public boolean visit(SQLNumberExpr x) {
        return true;
    }

    public boolean visit(SQLPropertyExpr x) {
        return true;
    }

    public boolean visit(SQLSelectGroupByClause x) {
        return true;
    }

    public boolean visit(SQLSelectItem x) {
        return true;
    }

    public void endVisit(SQLCastExpr x) {
    }

    public boolean visit(SQLSelectStatement astNode) {
        return true;
    }

    public void endVisit(SQLAggregateExpr x) {
    }

    public boolean visit(SQLAggregateExpr x) {
        return true;
    }

    public boolean visit(SQLVariantRefExpr x) {
        return true;
    }

    public void endVisit(SQLVariantRefExpr x) {
    }

    public boolean visit(SQLQueryExpr x) {
        return true;
    }

    public void endVisit(SQLQueryExpr x) {
    }

    public boolean visit(SQLSelect x) {
        return true;
    }

    public void endVisit(SQLSelect select) {
    }

    public boolean visit(SQLSelectQueryBlock x) {
        return true;
    }

    public void endVisit(SQLSelectQueryBlock x) {
    }

    public boolean visit(SQLExprTableSource x) {
        return true;
    }

    public void endVisit(SQLExprTableSource x) {
    }

    public boolean visit(SQLOrderBy x) {
        return true;
    }

    public void endVisit(SQLOrderBy x) {
    }

    public boolean visit(SQLSelectOrderByItem x) {
        return true;
    }

    public void endVisit(SQLSelectOrderByItem x) {
    }

    public boolean visit(SQLDropTableStatement x) {
        return true;
    }

    public void endVisit(SQLDropTableStatement x) {
    }

    public boolean visit(SQLCreateTableStatement x) {
        return true;
    }

    public void endVisit(SQLCreateTableStatement x) {
    }

    public boolean visit(SQLColumnDefinition x) {
        return true;
    }

    public void endVisit(SQLColumnDefinition x) {
    }

    public boolean visit(Identity x) {
        return true;
    }

    public void endVisit(Identity x) {
    }

    public boolean visit(SQLDataType x) {
        return true;
    }

    public void endVisit(SQLDataType x) {
    }

    public boolean visit(SQLDeleteStatement x) {
        return true;
    }

    public void endVisit(SQLDeleteStatement x) {
    }

    public boolean visit(SQLCurrentOfCursorExpr x) {
        return true;
    }

    public void endVisit(SQLCurrentOfCursorExpr x) {
    }

    public boolean visit(SQLInsertStatement x) {
        return true;
    }

    public void endVisit(SQLInsertStatement x) {
    }

    public boolean visit(SQLUpdateSetItem x) {
        return true;
    }

    public void endVisit(SQLUpdateSetItem x) {
    }

    public boolean visit(SQLUpdateStatement x) {
        return true;
    }

    public void endVisit(SQLUpdateStatement x) {
    }

    public boolean visit(SQLCreateViewStatement x) {
        return true;
    }

    public void endVisit(SQLCreateViewStatement x) {
    }

    public boolean visit(Column x) {
        return true;
    }

    public void endVisit(Column x) {
    }

    public boolean visit(SQLNotNullConstraint x) {
        return true;
    }

    public void endVisit(SQLNotNullConstraint x) {
    }

    public void endVisit(SQLMethodInvokeExpr x) {
    }

    public boolean visit(SQLMethodInvokeExpr x) {
        return true;
    }

    public void endVisit(SQLUnionQuery x) {
    }

    public boolean visit(SQLUnionQuery x) {
        return true;
    }

    public boolean visit(SQLUnaryExpr x) {
        return true;
    }

    public void endVisit(SQLUnaryExpr x) {
    }

    public boolean visit(SQLHexExpr x) {
        return false;
    }

    public void endVisit(SQLHexExpr x) {
    }

    public void endVisit(SQLSetStatement x) {
    }

    public boolean visit(SQLSetStatement x) {
        return true;
    }

    public void endVisit(SQLAssignItem x) {
    }

    public boolean visit(SQLAssignItem x) {
        return true;
    }

    public void endVisit(SQLCallStatement x) {
    }

    public boolean visit(SQLCallStatement x) {
        return true;
    }

    public void endVisit(SQLJoinTableSource x) {
    }

    public boolean visit(SQLJoinTableSource x) {
        return true;
    }

    public boolean visit(ValuesClause x) {
        return true;
    }

    public void endVisit(ValuesClause x) {
    }

    public void endVisit(SQLSomeExpr x) {
    }

    public boolean visit(SQLSomeExpr x) {
        return true;
    }

    public void endVisit(SQLAnyExpr x) {
    }

    public boolean visit(SQLAnyExpr x) {
        return true;
    }

    public void endVisit(SQLAllExpr x) {
    }

    public boolean visit(SQLAllExpr x) {
        return true;
    }

    public void endVisit(SQLInSubQueryExpr x) {
    }

    public boolean visit(SQLInSubQueryExpr x) {
        return true;
    }

    public void endVisit(SQLListExpr x) {
    }

    public boolean visit(SQLListExpr x) {
        return true;
    }

    public void endVisit(SQLSubqueryTableSource x) {
    }

    public boolean visit(SQLSubqueryTableSource x) {
        return true;
    }

    public void endVisit(SQLTruncateStatement x) {
    }

    public boolean visit(SQLTruncateStatement x) {
        return true;
    }

    public void endVisit(SQLDefaultExpr x) {
    }

    public boolean visit(SQLDefaultExpr x) {
        return true;
    }

    public void endVisit(SQLCommentStatement x) {
    }

    public boolean visit(SQLCommentStatement x) {
        return true;
    }

    public void endVisit(SQLUseStatement x) {
    }

    public boolean visit(SQLUseStatement x) {
        return true;
    }

    public boolean visit(SQLAlterTableAddColumn x) {
        return true;
    }

    public void endVisit(SQLAlterTableAddColumn x) {
    }

    public boolean visit(SQLAlterTableDropColumnItem x) {
        return true;
    }

    public void endVisit(SQLAlterTableDropColumnItem x) {
    }

    public boolean visit(SQLDropIndexStatement x) {
        return true;
    }

    public void endVisit(SQLDropIndexStatement x) {
    }

    public boolean visit(SQLDropViewStatement x) {
        return true;
    }

    public void endVisit(SQLDropViewStatement x) {
    }

    public boolean visit(SQLSavePointStatement x) {
        return true;
    }

    public void endVisit(SQLSavePointStatement x) {
    }

    public boolean visit(SQLRollbackStatement x) {
        return true;
    }

    public void endVisit(SQLRollbackStatement x) {
    }

    public boolean visit(SQLReleaseSavePointStatement x) {
        return true;
    }

    public void endVisit(SQLReleaseSavePointStatement x) {
    }

    public boolean visit(SQLCommentHint x) {
        return true;
    }

    public void endVisit(SQLCommentHint x) {
    }

    public void endVisit(SQLCreateDatabaseStatement x) {
    }

    public boolean visit(SQLCreateDatabaseStatement x) {
        return true;
    }

    public boolean visit(SQLAlterTableDropIndex x) {
        return true;
    }

    public void endVisit(SQLAlterTableDropIndex x) {
    }

    public void endVisit(SQLOver x) {
    }

    public boolean visit(SQLOver x) {
        return true;
    }

    public void endVisit(SQLKeep x) {
    }

    public boolean visit(SQLKeep x) {
        return true;
    }

    public void endVisit(SQLColumnPrimaryKey x) {
    }

    public boolean visit(SQLColumnPrimaryKey x) {
        return true;
    }

    public void endVisit(SQLColumnUniqueKey x) {
    }

    public boolean visit(SQLColumnUniqueKey x) {
        return true;
    }

    public void endVisit(SQLWithSubqueryClause x) {
    }

    public boolean visit(SQLWithSubqueryClause x) {
        return true;
    }

    public void endVisit(Entry x) {
    }

    public boolean visit(Entry x) {
        return true;
    }

    public boolean visit(SQLCharacterDataType x) {
        return true;
    }

    public void endVisit(SQLCharacterDataType x) {
    }

    public void endVisit(SQLAlterTableAlterColumn x) {
    }

    public boolean visit(SQLAlterTableAlterColumn x) {
        return true;
    }

    public boolean visit(SQLCheck x) {
        return true;
    }

    public void endVisit(SQLCheck x) {
    }

    public boolean visit(SQLAlterTableDropForeignKey x) {
        return true;
    }

    public void endVisit(SQLAlterTableDropForeignKey x) {
    }

    public boolean visit(SQLAlterTableDropPrimaryKey x) {
        return true;
    }

    public void endVisit(SQLAlterTableDropPrimaryKey x) {
    }

    public boolean visit(SQLAlterTableDisableKeys x) {
        return true;
    }

    public void endVisit(SQLAlterTableDisableKeys x) {
    }

    public boolean visit(SQLAlterTableEnableKeys x) {
        return true;
    }

    public void endVisit(SQLAlterTableEnableKeys x) {
    }

    public boolean visit(SQLAlterTableStatement x) {
        return true;
    }

    public void endVisit(SQLAlterTableStatement x) {
    }

    public boolean visit(SQLAlterTableDisableConstraint x) {
        return true;
    }

    public void endVisit(SQLAlterTableDisableConstraint x) {
    }

    public boolean visit(SQLAlterTableEnableConstraint x) {
        return true;
    }

    public void endVisit(SQLAlterTableEnableConstraint x) {
    }

    public boolean visit(SQLColumnCheck x) {
        return true;
    }

    public void endVisit(SQLColumnCheck x) {
    }

    public boolean visit(SQLExprHint x) {
        return true;
    }

    public void endVisit(SQLExprHint x) {
    }

    public boolean visit(SQLAlterTableDropConstraint x) {
        return true;
    }

    public void endVisit(SQLAlterTableDropConstraint x) {
    }

    public boolean visit(SQLUnique x) {
        Iterator var2 = x.getColumns().iterator();

        while(var2.hasNext()) {
            SQLExpr column = (SQLExpr)var2.next();
            column.accept(this);
        }

        return false;
    }

    public void endVisit(SQLUnique x) {
    }

    public boolean visit(SQLCreateIndexStatement x) {
        return true;
    }

    public void endVisit(SQLCreateIndexStatement x) {
    }

    public boolean visit(SQLPrimaryKeyImpl x) {
        return true;
    }

    public void endVisit(SQLPrimaryKeyImpl x) {
    }

    public boolean visit(SQLAlterTableRenameColumn x) {
        return true;
    }

    public void endVisit(SQLAlterTableRenameColumn x) {
    }

    public boolean visit(SQLColumnReference x) {
        return true;
    }

    public void endVisit(SQLColumnReference x) {
    }

    public boolean visit(SQLForeignKeyImpl x) {
        return true;
    }

    public void endVisit(SQLForeignKeyImpl x) {
    }

    public boolean visit(SQLDropSequenceStatement x) {
        return true;
    }

    public void endVisit(SQLDropSequenceStatement x) {
    }

    public boolean visit(SQLDropTriggerStatement x) {
        return true;
    }

    public void endVisit(SQLDropTriggerStatement x) {
    }

    public void endVisit(SQLDropUserStatement x) {
    }

    public boolean visit(SQLDropUserStatement x) {
        return true;
    }

    public void endVisit(SQLExplainStatement x) {
    }

    public boolean visit(SQLExplainStatement x) {
        return true;
    }

    public void endVisit(SQLGrantStatement x) {
    }

    public boolean visit(SQLGrantStatement x) {
        return true;
    }

    public void endVisit(SQLDropDatabaseStatement x) {
    }

    public boolean visit(SQLDropDatabaseStatement x) {
        return true;
    }

    public void endVisit(SQLAlterTableAddIndex x) {
    }

    public boolean visit(SQLAlterTableAddIndex x) {
        return true;
    }

    public void endVisit(SQLAlterTableAddConstraint x) {
    }

    public boolean visit(SQLAlterTableAddConstraint x) {
        return true;
    }

    public void endVisit(SQLCreateTriggerStatement x) {
    }

    public boolean visit(SQLCreateTriggerStatement x) {
        return true;
    }

    public void endVisit(SQLDropFunctionStatement x) {
    }

    public boolean visit(SQLDropFunctionStatement x) {
        return true;
    }

    public void endVisit(SQLDropTableSpaceStatement x) {
    }

    public boolean visit(SQLDropTableSpaceStatement x) {
        return true;
    }

    public void endVisit(SQLDropProcedureStatement x) {
    }

    public boolean visit(SQLDropProcedureStatement x) {
        return true;
    }

    public void endVisit(SQLBooleanExpr x) {
    }

    public boolean visit(SQLBooleanExpr x) {
        return true;
    }

    public void endVisit(SQLUnionQueryTableSource x) {
    }

    public boolean visit(SQLUnionQueryTableSource x) {
        return true;
    }

    public void endVisit(SQLTimestampExpr x) {
    }

    public boolean visit(SQLTimestampExpr x) {
        return true;
    }

    public void endVisit(SQLRevokeStatement x) {
    }

    public boolean visit(SQLRevokeStatement x) {
        return true;
    }

    public void endVisit(SQLBinaryExpr x) {
    }

    public boolean visit(SQLBinaryExpr x) {
        return true;
    }

    public void endVisit(SQLAlterTableRename x) {
    }

    public boolean visit(SQLAlterTableRename x) {
        return true;
    }

    public void endVisit(SQLAlterViewRenameStatement x) {
    }

    public boolean visit(SQLAlterViewRenameStatement x) {
        return true;
    }

    public void endVisit(SQLShowTablesStatement x) {
    }

    public boolean visit(SQLShowTablesStatement x) {
        return true;
    }

    public void endVisit(SQLAlterTableAddPartition x) {
    }

    public boolean visit(SQLAlterTableAddPartition x) {
        return true;
    }

    public void endVisit(SQLAlterTableDropPartition x) {
    }

    public boolean visit(SQLAlterTableDropPartition x) {
        return true;
    }

    public void endVisit(SQLAlterTableRenamePartition x) {
    }

    public boolean visit(SQLAlterTableRenamePartition x) {
        return true;
    }

    public void endVisit(SQLAlterTableSetComment x) {
    }

    public boolean visit(SQLAlterTableSetComment x) {
        return true;
    }

    public void endVisit(SQLAlterTableSetLifecycle x) {
    }

    public boolean visit(SQLAlterTableSetLifecycle x) {
        return true;
    }

    public void endVisit(SQLAlterTableEnableLifecycle x) {
    }

    public boolean visit(SQLAlterTableEnableLifecycle x) {
        return true;
    }

    public void endVisit(SQLAlterTableDisableLifecycle x) {
    }

    public boolean visit(SQLAlterTableDisableLifecycle x) {
        return true;
    }

    public void endVisit(SQLAlterTableTouch x) {
    }

    public boolean visit(SQLAlterTableTouch x) {
        return true;
    }

    public void endVisit(SQLArrayExpr x) {
    }

    public boolean visit(SQLArrayExpr x) {
        return true;
    }

    public void endVisit(SQLOpenStatement x) {
    }

    public boolean visit(SQLOpenStatement x) {
        return true;
    }

    public void endVisit(SQLFetchStatement x) {
    }

    public boolean visit(SQLFetchStatement x) {
        return true;
    }

    public void endVisit(SQLCloseStatement x) {
    }

    public boolean visit(SQLCloseStatement x) {
        return true;
    }

    public boolean visit(SQLGroupingSetExpr x) {
        return true;
    }

    public void endVisit(SQLGroupingSetExpr x) {
    }

    public boolean visit(SQLIfStatement x) {
        return true;
    }

    public void endVisit(SQLIfStatement x) {
    }

    public boolean visit(Else x) {
        return true;
    }

    public void endVisit(Else x) {
    }

    public boolean visit(ElseIf x) {
        return true;
    }

    public void endVisit(ElseIf x) {
    }

    public boolean visit(SQLLoopStatement x) {
        return true;
    }

    public void endVisit(SQLLoopStatement x) {
    }

    public boolean visit(SQLParameter x) {
        return true;
    }

    public void endVisit(SQLParameter x) {
    }

    public boolean visit(SQLCreateProcedureStatement x) {
        return true;
    }

    public void endVisit(SQLCreateProcedureStatement x) {
    }

    public boolean visit(SQLBlockStatement x) {
        return true;
    }

    public void endVisit(SQLBlockStatement x) {
    }

    public boolean visit(SQLAlterTableDropKey x) {
        return true;
    }

    public void endVisit(SQLAlterTableDropKey x) {
    }

    public boolean visit(SQLDeclareItem x) {
        return true;
    }

    public void endVisit(SQLDeclareItem x) {
    }

    public boolean visit(SQLPartitionValue x) {
        return true;
    }

    public void endVisit(SQLPartitionValue x) {
    }

    public boolean visit(SQLPartition x) {
        return true;
    }

    public void endVisit(SQLPartition x) {
    }

    public boolean visit(SQLPartitionByRange x) {
        return true;
    }

    public void endVisit(SQLPartitionByRange x) {
    }

    public boolean visit(SQLPartitionByHash x) {
        return true;
    }

    public void endVisit(SQLPartitionByHash x) {
    }

    public boolean visit(SQLPartitionByList x) {
        return true;
    }

    public void endVisit(SQLPartitionByList x) {
    }

    public boolean visit(SQLSubPartition x) {
        return true;
    }

    public void endVisit(SQLSubPartition x) {
    }

    public boolean visit(SQLSubPartitionByHash x) {
        return true;
    }

    public void endVisit(SQLSubPartitionByHash x) {
    }

    public boolean visit(SQLSubPartitionByList x) {
        return true;
    }

    public void endVisit(SQLSubPartitionByList x) {
    }

    public boolean visit(SQLAlterDatabaseStatement x) {
        return true;
    }

    public void endVisit(SQLAlterDatabaseStatement x) {
    }

    public boolean visit(SQLAlterTableConvertCharSet x) {
        return true;
    }

    public void endVisit(SQLAlterTableConvertCharSet x) {
    }

    public boolean visit(SQLAlterTableReOrganizePartition x) {
        return true;
    }

    public void endVisit(SQLAlterTableReOrganizePartition x) {
    }

    public boolean visit(SQLAlterTableCoalescePartition x) {
        return true;
    }

    public void endVisit(SQLAlterTableCoalescePartition x) {
    }

    public boolean visit(SQLAlterTableTruncatePartition x) {
        return true;
    }

    public void endVisit(SQLAlterTableTruncatePartition x) {
    }

    public boolean visit(SQLAlterTableDiscardPartition x) {
        return true;
    }

    public void endVisit(SQLAlterTableDiscardPartition x) {
    }

    public boolean visit(SQLAlterTableImportPartition x) {
        return true;
    }

    public void endVisit(SQLAlterTableImportPartition x) {
    }

    public boolean visit(SQLAlterTableAnalyzePartition x) {
        return true;
    }

    public void endVisit(SQLAlterTableAnalyzePartition x) {
    }

    public boolean visit(SQLAlterTableCheckPartition x) {
        return true;
    }

    public void endVisit(SQLAlterTableCheckPartition x) {
    }

    public boolean visit(SQLAlterTableOptimizePartition x) {
        return true;
    }

    public void endVisit(SQLAlterTableOptimizePartition x) {
    }

    public boolean visit(SQLAlterTableRebuildPartition x) {
        return true;
    }

    public void endVisit(SQLAlterTableRebuildPartition x) {
    }

    public boolean visit(SQLAlterTableRepairPartition x) {
        return true;
    }

    public void endVisit(SQLAlterTableRepairPartition x) {
    }

    public boolean visit(SQLSequenceExpr x) {
        return true;
    }

    public void endVisit(SQLSequenceExpr x) {
    }

    public boolean visit(SQLMergeStatement x) {
        return true;
    }

    public void endVisit(SQLMergeStatement x) {
    }

    public boolean visit(MergeUpdateClause x) {
        return true;
    }

    public void endVisit(MergeUpdateClause x) {
    }

    public boolean visit(MergeInsertClause x) {
        return true;
    }

    public void endVisit(MergeInsertClause x) {
    }

    public boolean visit(SQLErrorLoggingClause x) {
        return true;
    }

    public void endVisit(SQLErrorLoggingClause x) {
    }

    public boolean visit(SQLNullConstraint x) {
        return true;
    }

    public void endVisit(SQLNullConstraint x) {
    }

    public boolean visit(SQLCreateSequenceStatement x) {
        return true;
    }

    public void endVisit(SQLCreateSequenceStatement x) {
    }

    public boolean visit(SQLDateExpr x) {
        return true;
    }

    public void endVisit(SQLDateExpr x) {
    }

    public boolean visit(SQLLimit x) {
        return true;
    }

    public void endVisit(SQLLimit x) {
    }

    public void endVisit(SQLStartTransactionStatement x) {
    }

    public boolean visit(SQLStartTransactionStatement x) {
        return true;
    }
}

