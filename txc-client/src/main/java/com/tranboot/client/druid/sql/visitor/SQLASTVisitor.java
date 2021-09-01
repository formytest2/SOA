package com.tranboot.client.druid.sql.visitor;

import com.tranboot.client.druid.sql.ast.SQLCommentHint;
import com.tranboot.client.druid.sql.ast.SQLDataType;
import com.tranboot.client.druid.sql.ast.SQLDeclareItem;
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

public interface SQLASTVisitor {
    void endVisit(SQLAllColumnExpr var1);

    void endVisit(SQLBetweenExpr var1);

    void endVisit(SQLBinaryOpExpr var1);

    void endVisit(SQLCaseExpr var1);

    void endVisit(Item var1);

    void endVisit(SQLCharExpr var1);

    void endVisit(SQLIdentifierExpr var1);

    void endVisit(SQLInListExpr var1);

    void endVisit(SQLIntegerExpr var1);

    void endVisit(SQLExistsExpr var1);

    void endVisit(SQLNCharExpr var1);

    void endVisit(SQLNotExpr var1);

    void endVisit(SQLNullExpr var1);

    void endVisit(SQLNumberExpr var1);

    void endVisit(SQLPropertyExpr var1);

    void endVisit(SQLSelectGroupByClause var1);

    void endVisit(SQLSelectItem var1);

    void endVisit(SQLSelectStatement var1);

    void postVisit(SQLObject var1);

    void preVisit(SQLObject var1);

    boolean visit(SQLAllColumnExpr var1);

    boolean visit(SQLBetweenExpr var1);

    boolean visit(SQLBinaryOpExpr var1);

    boolean visit(SQLCaseExpr var1);

    boolean visit(Item var1);

    boolean visit(SQLCastExpr var1);

    boolean visit(SQLCharExpr var1);

    boolean visit(SQLExistsExpr var1);

    boolean visit(SQLIdentifierExpr var1);

    boolean visit(SQLInListExpr var1);

    boolean visit(SQLIntegerExpr var1);

    boolean visit(SQLNCharExpr var1);

    boolean visit(SQLNotExpr var1);

    boolean visit(SQLNullExpr var1);

    boolean visit(SQLNumberExpr var1);

    boolean visit(SQLPropertyExpr var1);

    boolean visit(SQLSelectGroupByClause var1);

    boolean visit(SQLSelectItem var1);

    void endVisit(SQLCastExpr var1);

    boolean visit(SQLSelectStatement var1);

    void endVisit(SQLAggregateExpr var1);

    boolean visit(SQLAggregateExpr var1);

    boolean visit(SQLVariantRefExpr var1);

    void endVisit(SQLVariantRefExpr var1);

    boolean visit(SQLQueryExpr var1);

    void endVisit(SQLQueryExpr var1);

    boolean visit(SQLUnaryExpr var1);

    void endVisit(SQLUnaryExpr var1);

    boolean visit(SQLHexExpr var1);

    void endVisit(SQLHexExpr var1);

    boolean visit(SQLSelect var1);

    void endVisit(SQLSelect var1);

    boolean visit(SQLSelectQueryBlock var1);

    void endVisit(SQLSelectQueryBlock var1);

    boolean visit(SQLExprTableSource var1);

    void endVisit(SQLExprTableSource var1);

    boolean visit(SQLOrderBy var1);

    void endVisit(SQLOrderBy var1);

    boolean visit(SQLSelectOrderByItem var1);

    void endVisit(SQLSelectOrderByItem var1);

    boolean visit(SQLDropTableStatement var1);

    void endVisit(SQLDropTableStatement var1);

    boolean visit(SQLCreateTableStatement var1);

    void endVisit(SQLCreateTableStatement var1);

    boolean visit(SQLColumnDefinition var1);

    void endVisit(SQLColumnDefinition var1);

    boolean visit(Identity var1);

    void endVisit(Identity var1);

    boolean visit(SQLDataType var1);

    void endVisit(SQLDataType var1);

    boolean visit(SQLCharacterDataType var1);

    void endVisit(SQLCharacterDataType var1);

    boolean visit(SQLDeleteStatement var1);

    void endVisit(SQLDeleteStatement var1);

    boolean visit(SQLCurrentOfCursorExpr var1);

    void endVisit(SQLCurrentOfCursorExpr var1);

    boolean visit(SQLInsertStatement var1);

    void endVisit(SQLInsertStatement var1);

    boolean visit(ValuesClause var1);

    void endVisit(ValuesClause var1);

    boolean visit(SQLUpdateSetItem var1);

    void endVisit(SQLUpdateSetItem var1);

    boolean visit(SQLUpdateStatement var1);

    void endVisit(SQLUpdateStatement var1);

    boolean visit(SQLCreateViewStatement var1);

    void endVisit(SQLCreateViewStatement var1);

    boolean visit(Column var1);

    void endVisit(Column var1);

    boolean visit(SQLNotNullConstraint var1);

    void endVisit(SQLNotNullConstraint var1);

    void endVisit(SQLMethodInvokeExpr var1);

    boolean visit(SQLMethodInvokeExpr var1);

    void endVisit(SQLUnionQuery var1);

    boolean visit(SQLUnionQuery var1);

    void endVisit(SQLSetStatement var1);

    boolean visit(SQLSetStatement var1);

    void endVisit(SQLAssignItem var1);

    boolean visit(SQLAssignItem var1);

    void endVisit(SQLCallStatement var1);

    boolean visit(SQLCallStatement var1);

    void endVisit(SQLJoinTableSource var1);

    boolean visit(SQLJoinTableSource var1);

    void endVisit(SQLSomeExpr var1);

    boolean visit(SQLSomeExpr var1);

    void endVisit(SQLAnyExpr var1);

    boolean visit(SQLAnyExpr var1);

    void endVisit(SQLAllExpr var1);

    boolean visit(SQLAllExpr var1);

    void endVisit(SQLInSubQueryExpr var1);

    boolean visit(SQLInSubQueryExpr var1);

    void endVisit(SQLListExpr var1);

    boolean visit(SQLListExpr var1);

    void endVisit(SQLSubqueryTableSource var1);

    boolean visit(SQLSubqueryTableSource var1);

    void endVisit(SQLTruncateStatement var1);

    boolean visit(SQLTruncateStatement var1);

    void endVisit(SQLDefaultExpr var1);

    boolean visit(SQLDefaultExpr var1);

    void endVisit(SQLCommentStatement var1);

    boolean visit(SQLCommentStatement var1);

    void endVisit(SQLUseStatement var1);

    boolean visit(SQLUseStatement var1);

    boolean visit(SQLAlterTableAddColumn var1);

    void endVisit(SQLAlterTableAddColumn var1);

    boolean visit(SQLAlterTableDropColumnItem var1);

    void endVisit(SQLAlterTableDropColumnItem var1);

    boolean visit(SQLAlterTableDropIndex var1);

    void endVisit(SQLAlterTableDropIndex var1);

    boolean visit(SQLDropIndexStatement var1);

    void endVisit(SQLDropIndexStatement var1);

    boolean visit(SQLDropViewStatement var1);

    void endVisit(SQLDropViewStatement var1);

    boolean visit(SQLSavePointStatement var1);

    void endVisit(SQLSavePointStatement var1);

    boolean visit(SQLRollbackStatement var1);

    void endVisit(SQLRollbackStatement var1);

    boolean visit(SQLReleaseSavePointStatement var1);

    void endVisit(SQLReleaseSavePointStatement var1);

    void endVisit(SQLCommentHint var1);

    boolean visit(SQLCommentHint var1);

    void endVisit(SQLCreateDatabaseStatement var1);

    boolean visit(SQLCreateDatabaseStatement var1);

    void endVisit(SQLOver var1);

    boolean visit(SQLOver var1);

    void endVisit(SQLKeep var1);

    boolean visit(SQLKeep var1);

    void endVisit(SQLColumnPrimaryKey var1);

    boolean visit(SQLColumnPrimaryKey var1);

    boolean visit(SQLColumnUniqueKey var1);

    void endVisit(SQLColumnUniqueKey var1);

    void endVisit(SQLWithSubqueryClause var1);

    boolean visit(SQLWithSubqueryClause var1);

    void endVisit(Entry var1);

    boolean visit(Entry var1);

    void endVisit(SQLAlterTableAlterColumn var1);

    boolean visit(SQLAlterTableAlterColumn var1);

    boolean visit(SQLCheck var1);

    void endVisit(SQLCheck var1);

    boolean visit(SQLAlterTableDropForeignKey var1);

    void endVisit(SQLAlterTableDropForeignKey var1);

    boolean visit(SQLAlterTableDropPrimaryKey var1);

    void endVisit(SQLAlterTableDropPrimaryKey var1);

    boolean visit(SQLAlterTableDisableKeys var1);

    void endVisit(SQLAlterTableDisableKeys var1);

    boolean visit(SQLAlterTableEnableKeys var1);

    void endVisit(SQLAlterTableEnableKeys var1);

    boolean visit(SQLAlterTableStatement var1);

    void endVisit(SQLAlterTableStatement var1);

    boolean visit(SQLAlterTableDisableConstraint var1);

    void endVisit(SQLAlterTableDisableConstraint var1);

    boolean visit(SQLAlterTableEnableConstraint var1);

    void endVisit(SQLAlterTableEnableConstraint var1);

    boolean visit(SQLColumnCheck var1);

    void endVisit(SQLColumnCheck var1);

    boolean visit(SQLExprHint var1);

    void endVisit(SQLExprHint var1);

    boolean visit(SQLAlterTableDropConstraint var1);

    void endVisit(SQLAlterTableDropConstraint var1);

    boolean visit(SQLUnique var1);

    void endVisit(SQLUnique var1);

    boolean visit(SQLPrimaryKeyImpl var1);

    void endVisit(SQLPrimaryKeyImpl var1);

    boolean visit(SQLCreateIndexStatement var1);

    void endVisit(SQLCreateIndexStatement var1);

    boolean visit(SQLAlterTableRenameColumn var1);

    void endVisit(SQLAlterTableRenameColumn var1);

    boolean visit(SQLColumnReference var1);

    void endVisit(SQLColumnReference var1);

    boolean visit(SQLForeignKeyImpl var1);

    void endVisit(SQLForeignKeyImpl var1);

    boolean visit(SQLDropSequenceStatement var1);

    void endVisit(SQLDropSequenceStatement var1);

    boolean visit(SQLDropTriggerStatement var1);

    void endVisit(SQLDropTriggerStatement var1);

    void endVisit(SQLDropUserStatement var1);

    boolean visit(SQLDropUserStatement var1);

    void endVisit(SQLExplainStatement var1);

    boolean visit(SQLExplainStatement var1);

    void endVisit(SQLGrantStatement var1);

    boolean visit(SQLGrantStatement var1);

    void endVisit(SQLDropDatabaseStatement var1);

    boolean visit(SQLDropDatabaseStatement var1);

    void endVisit(SQLAlterTableAddIndex var1);

    boolean visit(SQLAlterTableAddIndex var1);

    void endVisit(SQLAlterTableAddConstraint var1);

    boolean visit(SQLAlterTableAddConstraint var1);

    void endVisit(SQLCreateTriggerStatement var1);

    boolean visit(SQLCreateTriggerStatement var1);

    void endVisit(SQLDropFunctionStatement var1);

    boolean visit(SQLDropFunctionStatement var1);

    void endVisit(SQLDropTableSpaceStatement var1);

    boolean visit(SQLDropTableSpaceStatement var1);

    void endVisit(SQLDropProcedureStatement var1);

    boolean visit(SQLDropProcedureStatement var1);

    void endVisit(SQLBooleanExpr var1);

    boolean visit(SQLBooleanExpr var1);

    void endVisit(SQLUnionQueryTableSource var1);

    boolean visit(SQLUnionQueryTableSource var1);

    void endVisit(SQLTimestampExpr var1);

    boolean visit(SQLTimestampExpr var1);

    void endVisit(SQLRevokeStatement var1);

    boolean visit(SQLRevokeStatement var1);

    void endVisit(SQLBinaryExpr var1);

    boolean visit(SQLBinaryExpr var1);

    void endVisit(SQLAlterTableRename var1);

    boolean visit(SQLAlterTableRename var1);

    void endVisit(SQLAlterViewRenameStatement var1);

    boolean visit(SQLAlterViewRenameStatement var1);

    void endVisit(SQLShowTablesStatement var1);

    boolean visit(SQLShowTablesStatement var1);

    void endVisit(SQLAlterTableAddPartition var1);

    boolean visit(SQLAlterTableAddPartition var1);

    void endVisit(SQLAlterTableDropPartition var1);

    boolean visit(SQLAlterTableDropPartition var1);

    void endVisit(SQLAlterTableRenamePartition var1);

    boolean visit(SQLAlterTableRenamePartition var1);

    void endVisit(SQLAlterTableSetComment var1);

    boolean visit(SQLAlterTableSetComment var1);

    void endVisit(SQLAlterTableSetLifecycle var1);

    boolean visit(SQLAlterTableSetLifecycle var1);

    void endVisit(SQLAlterTableEnableLifecycle var1);

    boolean visit(SQLAlterTableEnableLifecycle var1);

    void endVisit(SQLAlterTableDisableLifecycle var1);

    boolean visit(SQLAlterTableDisableLifecycle var1);

    void endVisit(SQLAlterTableTouch var1);

    boolean visit(SQLAlterTableTouch var1);

    void endVisit(SQLArrayExpr var1);

    boolean visit(SQLArrayExpr var1);

    void endVisit(SQLOpenStatement var1);

    boolean visit(SQLOpenStatement var1);

    void endVisit(SQLFetchStatement var1);

    boolean visit(SQLFetchStatement var1);

    void endVisit(SQLCloseStatement var1);

    boolean visit(SQLCloseStatement var1);

    boolean visit(SQLGroupingSetExpr var1);

    void endVisit(SQLGroupingSetExpr var1);

    boolean visit(SQLIfStatement var1);

    void endVisit(SQLIfStatement var1);

    boolean visit(ElseIf var1);

    void endVisit(ElseIf var1);

    boolean visit(Else var1);

    void endVisit(Else var1);

    boolean visit(SQLLoopStatement var1);

    void endVisit(SQLLoopStatement var1);

    boolean visit(SQLParameter var1);

    void endVisit(SQLParameter var1);

    boolean visit(SQLCreateProcedureStatement var1);

    void endVisit(SQLCreateProcedureStatement var1);

    boolean visit(SQLBlockStatement var1);

    void endVisit(SQLBlockStatement var1);

    boolean visit(SQLAlterTableDropKey var1);

    void endVisit(SQLAlterTableDropKey var1);

    boolean visit(SQLDeclareItem var1);

    void endVisit(SQLDeclareItem var1);

    boolean visit(SQLPartitionValue var1);

    void endVisit(SQLPartitionValue var1);

    boolean visit(SQLPartition var1);

    void endVisit(SQLPartition var1);

    boolean visit(SQLPartitionByRange var1);

    void endVisit(SQLPartitionByRange var1);

    boolean visit(SQLPartitionByHash var1);

    void endVisit(SQLPartitionByHash var1);

    boolean visit(SQLPartitionByList var1);

    void endVisit(SQLPartitionByList var1);

    boolean visit(SQLSubPartition var1);

    void endVisit(SQLSubPartition var1);

    boolean visit(SQLSubPartitionByHash var1);

    void endVisit(SQLSubPartitionByHash var1);

    boolean visit(SQLSubPartitionByList var1);

    void endVisit(SQLSubPartitionByList var1);

    boolean visit(SQLAlterDatabaseStatement var1);

    void endVisit(SQLAlterDatabaseStatement var1);

    boolean visit(SQLAlterTableConvertCharSet var1);

    void endVisit(SQLAlterTableConvertCharSet var1);

    boolean visit(SQLAlterTableReOrganizePartition var1);

    void endVisit(SQLAlterTableReOrganizePartition var1);

    boolean visit(SQLAlterTableCoalescePartition var1);

    void endVisit(SQLAlterTableCoalescePartition var1);

    boolean visit(SQLAlterTableTruncatePartition var1);

    void endVisit(SQLAlterTableTruncatePartition var1);

    boolean visit(SQLAlterTableDiscardPartition var1);

    void endVisit(SQLAlterTableDiscardPartition var1);

    boolean visit(SQLAlterTableImportPartition var1);

    void endVisit(SQLAlterTableImportPartition var1);

    boolean visit(SQLAlterTableAnalyzePartition var1);

    void endVisit(SQLAlterTableAnalyzePartition var1);

    boolean visit(SQLAlterTableCheckPartition var1);

    void endVisit(SQLAlterTableCheckPartition var1);

    boolean visit(SQLAlterTableOptimizePartition var1);

    void endVisit(SQLAlterTableOptimizePartition var1);

    boolean visit(SQLAlterTableRebuildPartition var1);

    void endVisit(SQLAlterTableRebuildPartition var1);

    boolean visit(SQLAlterTableRepairPartition var1);

    void endVisit(SQLAlterTableRepairPartition var1);

    boolean visit(SQLSequenceExpr var1);

    void endVisit(SQLSequenceExpr var1);

    boolean visit(SQLMergeStatement var1);

    void endVisit(SQLMergeStatement var1);

    boolean visit(MergeUpdateClause var1);

    void endVisit(MergeUpdateClause var1);

    boolean visit(MergeInsertClause var1);

    void endVisit(MergeInsertClause var1);

    boolean visit(SQLErrorLoggingClause var1);

    void endVisit(SQLErrorLoggingClause var1);

    boolean visit(SQLNullConstraint var1);

    void endVisit(SQLNullConstraint var1);

    boolean visit(SQLCreateSequenceStatement var1);

    void endVisit(SQLCreateSequenceStatement var1);

    boolean visit(SQLDateExpr var1);

    void endVisit(SQLDateExpr var1);

    boolean visit(SQLLimit var1);

    void endVisit(SQLLimit var1);

    void endVisit(SQLStartTransactionStatement var1);

    boolean visit(SQLStartTransactionStatement var1);
}

