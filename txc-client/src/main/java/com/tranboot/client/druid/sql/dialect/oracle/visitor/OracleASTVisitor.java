package com.tranboot.client.druid.sql.dialect.oracle.visitor;

import com.tranboot.client.druid.sql.ast.expr.SQLDateExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleDataTypeIntervalDay;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleDataTypeIntervalYear;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleDataTypeTimestamp;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.CycleClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.OracleLobStorageClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.OracleReturningClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.OracleStorageClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.OracleWithSubqueryEntry;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.PartitionExtensionClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.SampleClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.SearchClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.FlashbackQueryClause.AsOfFlashbackQueryClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.FlashbackQueryClause.AsOfSnapshotClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.FlashbackQueryClause.VersionsFlashbackQueryClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause.CellAssignment;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause.CellAssignmentItem;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause.MainModelClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause.ModelColumn;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause.ModelColumnClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause.ModelRulesClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause.QueryPartitionClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause.ReturnRowsClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleAnalytic;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleAnalyticWindowing;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleArgumentExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleBinaryDoubleExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleBinaryFloatExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleCursorExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleDatetimeExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleDbLinkExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleIntervalExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleIsSetExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleOuterExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleRangeExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleSizeExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleSysdateExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterIndexStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterProcedureStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterSessionStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterSynonymStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableDropPartition;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableModify;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableMoveTablespace;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableSplitPartition;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableTruncatePartition;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTablespaceAddDataFile;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTablespaceStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTriggerStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterViewStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleCheck;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleCommitStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleCreateDatabaseDbLinkStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleCreateIndexStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleDeleteStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleDropDbLinkStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleExceptionStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleExitStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleExplainStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleExprStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleFileSpecification;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleForStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleForeignKey;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleGotoStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleInsertStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleLabelStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleLockTableStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OraclePLSQLCommitStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OraclePrimaryKey;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSavePointStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelect;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectForUpdate;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectHierachicalQueryClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectJoin;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectPivot;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectSubqueryTableSource;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectTableReference;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectUnPivot;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSetTransactionStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleUnique;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleUpdateStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleUsingIndexClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterIndexStatement.Rebuild;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableSplitPartition.NestedTablePartitionSpec;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableSplitPartition.TableSpaceItem;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableSplitPartition.UpdateIndexesClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement.ConditionalInsertClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement.ConditionalInsertClauseItem;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement.InsertIntoClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectPivot.Item;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectRestriction.CheckOption;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectRestriction.ReadOnly;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

public interface OracleASTVisitor extends SQLASTVisitor {
    void endVisit(OraclePLSQLCommitStatement var1);

    void endVisit(OracleAnalytic var1);

    void endVisit(OracleAnalyticWindowing var1);

    void endVisit(SQLDateExpr var1);

    void endVisit(OracleDbLinkExpr var1);

    void endVisit(OracleDeleteStatement var1);

    void endVisit(OracleIntervalExpr var1);

    void endVisit(OracleOuterExpr var1);

    void endVisit(OracleSelectForUpdate var1);

    void endVisit(OracleSelectHierachicalQueryClause var1);

    void endVisit(OracleSelectJoin var1);

    void endVisit(OracleSelectPivot var1);

    void endVisit(Item var1);

    void endVisit(CheckOption var1);

    void endVisit(ReadOnly var1);

    void endVisit(OracleSelectSubqueryTableSource var1);

    void endVisit(OracleSelectUnPivot var1);

    void endVisit(OracleUpdateStatement var1);

    boolean visit(OraclePLSQLCommitStatement var1);

    boolean visit(OracleAnalytic var1);

    boolean visit(OracleAnalyticWindowing var1);

    boolean visit(SQLDateExpr var1);

    boolean visit(OracleDbLinkExpr var1);

    boolean visit(OracleDeleteStatement var1);

    boolean visit(OracleIntervalExpr var1);

    boolean visit(OracleOuterExpr var1);

    boolean visit(OracleSelectForUpdate var1);

    boolean visit(OracleSelectHierachicalQueryClause var1);

    boolean visit(OracleSelectJoin var1);

    boolean visit(OracleSelectPivot var1);

    boolean visit(Item var1);

    boolean visit(CheckOption var1);

    boolean visit(ReadOnly var1);

    boolean visit(OracleSelectSubqueryTableSource var1);

    boolean visit(OracleSelectUnPivot var1);

    boolean visit(OracleUpdateStatement var1);

    boolean visit(SampleClause var1);

    void endVisit(SampleClause var1);

    boolean visit(OracleSelectTableReference var1);

    void endVisit(OracleSelectTableReference var1);

    boolean visit(PartitionExtensionClause var1);

    void endVisit(PartitionExtensionClause var1);

    boolean visit(VersionsFlashbackQueryClause var1);

    void endVisit(VersionsFlashbackQueryClause var1);

    boolean visit(AsOfFlashbackQueryClause var1);

    void endVisit(AsOfFlashbackQueryClause var1);

    boolean visit(OracleWithSubqueryEntry var1);

    void endVisit(OracleWithSubqueryEntry var1);

    boolean visit(SearchClause var1);

    void endVisit(SearchClause var1);

    boolean visit(CycleClause var1);

    void endVisit(CycleClause var1);

    boolean visit(OracleBinaryFloatExpr var1);

    void endVisit(OracleBinaryFloatExpr var1);

    boolean visit(OracleBinaryDoubleExpr var1);

    void endVisit(OracleBinaryDoubleExpr var1);

    boolean visit(OracleSelect var1);

    void endVisit(OracleSelect var1);

    boolean visit(OracleCursorExpr var1);

    void endVisit(OracleCursorExpr var1);

    boolean visit(OracleIsSetExpr var1);

    void endVisit(OracleIsSetExpr var1);

    boolean visit(ReturnRowsClause var1);

    void endVisit(ReturnRowsClause var1);

    boolean visit(MainModelClause var1);

    void endVisit(MainModelClause var1);

    boolean visit(ModelColumnClause var1);

    void endVisit(ModelColumnClause var1);

    boolean visit(QueryPartitionClause var1);

    void endVisit(QueryPartitionClause var1);

    boolean visit(ModelColumn var1);

    void endVisit(ModelColumn var1);

    boolean visit(ModelRulesClause var1);

    void endVisit(ModelRulesClause var1);

    boolean visit(CellAssignmentItem var1);

    void endVisit(CellAssignmentItem var1);

    boolean visit(CellAssignment var1);

    void endVisit(CellAssignment var1);

    boolean visit(ModelClause var1);

    void endVisit(ModelClause var1);

    boolean visit(OracleReturningClause var1);

    void endVisit(OracleReturningClause var1);

    boolean visit(OracleInsertStatement var1);

    void endVisit(OracleInsertStatement var1);

    boolean visit(InsertIntoClause var1);

    void endVisit(InsertIntoClause var1);

    boolean visit(OracleMultiInsertStatement var1);

    void endVisit(OracleMultiInsertStatement var1);

    boolean visit(ConditionalInsertClause var1);

    void endVisit(ConditionalInsertClause var1);

    boolean visit(ConditionalInsertClauseItem var1);

    void endVisit(ConditionalInsertClauseItem var1);

    boolean visit(OracleSelectQueryBlock var1);

    void endVisit(OracleSelectQueryBlock var1);

    boolean visit(OracleLockTableStatement var1);

    void endVisit(OracleLockTableStatement var1);

    boolean visit(OracleAlterSessionStatement var1);

    void endVisit(OracleAlterSessionStatement var1);

    boolean visit(OracleExprStatement var1);

    void endVisit(OracleExprStatement var1);

    boolean visit(OracleDatetimeExpr var1);

    void endVisit(OracleDatetimeExpr var1);

    boolean visit(OracleSysdateExpr var1);

    void endVisit(OracleSysdateExpr var1);

    boolean visit(OracleExceptionStatement var1);

    void endVisit(OracleExceptionStatement var1);

    boolean visit(com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleExceptionStatement.Item var1);

    void endVisit(com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleExceptionStatement.Item var1);

    boolean visit(OracleArgumentExpr var1);

    void endVisit(OracleArgumentExpr var1);

    boolean visit(OracleSetTransactionStatement var1);

    void endVisit(OracleSetTransactionStatement var1);

    boolean visit(OracleExplainStatement var1);

    void endVisit(OracleExplainStatement var1);

    boolean visit(OracleAlterProcedureStatement var1);

    void endVisit(OracleAlterProcedureStatement var1);

    boolean visit(OracleAlterTableDropPartition var1);

    void endVisit(OracleAlterTableDropPartition var1);

    boolean visit(OracleAlterTableTruncatePartition var1);

    void endVisit(OracleAlterTableTruncatePartition var1);

    boolean visit(TableSpaceItem var1);

    void endVisit(TableSpaceItem var1);

    boolean visit(UpdateIndexesClause var1);

    void endVisit(UpdateIndexesClause var1);

    boolean visit(NestedTablePartitionSpec var1);

    void endVisit(NestedTablePartitionSpec var1);

    boolean visit(OracleAlterTableSplitPartition var1);

    void endVisit(OracleAlterTableSplitPartition var1);

    boolean visit(OracleAlterTableModify var1);

    void endVisit(OracleAlterTableModify var1);

    boolean visit(OracleCreateIndexStatement var1);

    void endVisit(OracleCreateIndexStatement var1);

    boolean visit(OracleForStatement var1);

    void endVisit(OracleForStatement var1);

    boolean visit(OracleRangeExpr var1);

    void endVisit(OracleRangeExpr var1);

    boolean visit(OracleAlterIndexStatement var1);

    void endVisit(OracleAlterIndexStatement var1);

    boolean visit(OraclePrimaryKey var1);

    void endVisit(OraclePrimaryKey var1);

    boolean visit(OracleCreateTableStatement var1);

    void endVisit(OracleCreateTableStatement var1);

    boolean visit(Rebuild var1);

    void endVisit(Rebuild var1);

    boolean visit(OracleStorageClause var1);

    void endVisit(OracleStorageClause var1);

    boolean visit(OracleGotoStatement var1);

    void endVisit(OracleGotoStatement var1);

    boolean visit(OracleLabelStatement var1);

    void endVisit(OracleLabelStatement var1);

    boolean visit(OracleCommitStatement var1);

    void endVisit(OracleCommitStatement var1);

    boolean visit(OracleAlterTriggerStatement var1);

    void endVisit(OracleAlterTriggerStatement var1);

    boolean visit(OracleAlterSynonymStatement var1);

    void endVisit(OracleAlterSynonymStatement var1);

    boolean visit(OracleAlterViewStatement var1);

    void endVisit(OracleAlterViewStatement var1);

    boolean visit(AsOfSnapshotClause var1);

    void endVisit(AsOfSnapshotClause var1);

    boolean visit(OracleAlterTableMoveTablespace var1);

    void endVisit(OracleAlterTableMoveTablespace var1);

    boolean visit(OracleSizeExpr var1);

    void endVisit(OracleSizeExpr var1);

    boolean visit(OracleFileSpecification var1);

    void endVisit(OracleFileSpecification var1);

    boolean visit(OracleAlterTablespaceAddDataFile var1);

    void endVisit(OracleAlterTablespaceAddDataFile var1);

    boolean visit(OracleAlterTablespaceStatement var1);

    void endVisit(OracleAlterTablespaceStatement var1);

    boolean visit(OracleExitStatement var1);

    void endVisit(OracleExitStatement var1);

    boolean visit(OracleSavePointStatement var1);

    void endVisit(OracleSavePointStatement var1);

    boolean visit(OracleCreateDatabaseDbLinkStatement var1);

    void endVisit(OracleCreateDatabaseDbLinkStatement var1);

    boolean visit(OracleDropDbLinkStatement var1);

    void endVisit(OracleDropDbLinkStatement var1);

    boolean visit(OracleDataTypeTimestamp var1);

    void endVisit(OracleDataTypeTimestamp var1);

    boolean visit(OracleDataTypeIntervalYear var1);

    void endVisit(OracleDataTypeIntervalYear var1);

    boolean visit(OracleDataTypeIntervalDay var1);

    void endVisit(OracleDataTypeIntervalDay var1);

    boolean visit(OracleUsingIndexClause var1);

    void endVisit(OracleUsingIndexClause var1);

    boolean visit(OracleLobStorageClause var1);

    void endVisit(OracleLobStorageClause var1);

    boolean visit(OracleUnique var1);

    void endVisit(OracleUnique var1);

    boolean visit(OracleForeignKey var1);

    void endVisit(OracleForeignKey var1);

    boolean visit(OracleCheck var1);

    void endVisit(OracleCheck var1);
}

