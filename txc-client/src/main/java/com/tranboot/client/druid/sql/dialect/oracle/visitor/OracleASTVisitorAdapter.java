/*      */ package com.tranboot.client.druid.sql.dialect.oracle.visitor;
/*      */ 
/*      */

import com.tranboot.client.druid.sql.ast.statement.SQLMergeStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleDataTypeIntervalDay;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleDataTypeIntervalYear;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleDataTypeTimestamp;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.*;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.*;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.*;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitorAdapter;

/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */

/*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class OracleASTVisitorAdapter
/*      */   extends SQLASTVisitorAdapter
/*      */   implements OracleASTVisitor
/*      */ {
/*      */   public boolean visit(OracleSelect x) {
/*  116 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleSelect x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OraclePLSQLCommitStatement astNode) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAnalytic x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAnalyticWindowing x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleDbLinkExpr x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleDeleteStatement x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleIntervalExpr x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleOuterExpr x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleSelectForUpdate x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleSelectHierachicalQueryClause x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleSelectJoin x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleSelectPivot x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleSelectPivot.Item x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleSelectRestriction.CheckOption x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleSelectRestriction.ReadOnly x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleSelectSubqueryTableSource x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleSelectUnPivot x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleUpdateStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OraclePLSQLCommitStatement astNode) {
/*  210 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAnalytic x) {
/*  216 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAnalyticWindowing x) {
/*  222 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleDbLinkExpr x) {
/*  228 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleDeleteStatement x) {
/*  234 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleIntervalExpr x) {
/*  240 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleOuterExpr x) {
/*  246 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSelectForUpdate x) {
/*  252 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSelectHierachicalQueryClause x) {
/*  258 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSelectJoin x) {
/*  264 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSelectPivot x) {
/*  270 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSelectPivot.Item x) {
/*  276 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSelectRestriction.CheckOption x) {
/*  282 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSelectRestriction.ReadOnly x) {
/*  288 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSelectSubqueryTableSource x) {
/*  294 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSelectUnPivot x) {
/*  300 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleUpdateStatement x) {
/*  306 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(SampleClause x) {
/*  312 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(SampleClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSelectTableReference x) {
/*  323 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleSelectTableReference x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(PartitionExtensionClause x) {
/*  334 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(PartitionExtensionClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(FlashbackQueryClause.VersionsFlashbackQueryClause x) {
/*  345 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(FlashbackQueryClause.VersionsFlashbackQueryClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(FlashbackQueryClause.AsOfFlashbackQueryClause x) {
/*  356 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(FlashbackQueryClause.AsOfFlashbackQueryClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleWithSubqueryEntry x) {
/*  367 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleWithSubqueryEntry x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(SearchClause x) {
/*  378 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(SearchClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(CycleClause x) {
/*  389 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(CycleClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleBinaryFloatExpr x) {
/*  400 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleBinaryFloatExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleBinaryDoubleExpr x) {
/*  411 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleBinaryDoubleExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleCursorExpr x) {
/*  421 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleCursorExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleIsSetExpr x) {
/*  431 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleIsSetExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(ModelClause.ReturnRowsClause x) {
/*  441 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(ModelClause.ReturnRowsClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(ModelClause x) {
/*  451 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(ModelClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(ModelClause.MainModelClause x) {
/*  461 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(ModelClause.MainModelClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(ModelClause.ModelColumnClause x) {
/*  471 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(ModelClause.ModelColumnClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(ModelClause.QueryPartitionClause x) {
/*  481 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(ModelClause.QueryPartitionClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(ModelClause.ModelColumn x) {
/*  491 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(ModelClause.ModelColumn x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(ModelClause.ModelRulesClause x) {
/*  501 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(ModelClause.ModelRulesClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(ModelClause.CellAssignmentItem x) {
/*  511 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(ModelClause.CellAssignmentItem x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(ModelClause.CellAssignment x) {
/*  521 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(ModelClause.CellAssignment x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(SQLMergeStatement.MergeUpdateClause x) {
/*  531 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(SQLMergeStatement.MergeUpdateClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(SQLMergeStatement.MergeInsertClause x) {
/*  541 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(SQLMergeStatement.MergeInsertClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleReturningClause x) {
/*  551 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleReturningClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleInsertStatement x) {
/*  561 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleInsertStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleMultiInsertStatement.InsertIntoClause x) {
/*  571 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleMultiInsertStatement.InsertIntoClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleMultiInsertStatement x) {
/*  581 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleMultiInsertStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleMultiInsertStatement.ConditionalInsertClause x) {
/*  591 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleMultiInsertStatement.ConditionalInsertClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleMultiInsertStatement.ConditionalInsertClauseItem x) {
/*  601 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleMultiInsertStatement.ConditionalInsertClauseItem x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSelectQueryBlock x) {
/*  611 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleSelectQueryBlock x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleLockTableStatement x) {
/*  621 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleLockTableStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterSessionStatement x) {
/*  631 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterSessionStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleExprStatement x) {
/*  641 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleExprStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleDatetimeExpr x) {
/*  651 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleDatetimeExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSysdateExpr x) {
/*  661 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleSysdateExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleExceptionStatement x) {
/*  671 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleExceptionStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleExceptionStatement.Item x) {
/*  681 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleExceptionStatement.Item x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleArgumentExpr x) {
/*  691 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleArgumentExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSetTransactionStatement x) {
/*  701 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleSetTransactionStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleExplainStatement x) {
/*  711 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleExplainStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterProcedureStatement x) {
/*  721 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterProcedureStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTableDropPartition x) {
/*  731 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTableDropPartition x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTableTruncatePartition x) {
/*  741 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTableTruncatePartition x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTableSplitPartition.TableSpaceItem x) {
/*  751 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTableSplitPartition.TableSpaceItem x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTableSplitPartition.UpdateIndexesClause x) {
/*  761 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTableSplitPartition.UpdateIndexesClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTableSplitPartition.NestedTablePartitionSpec x) {
/*  771 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTableSplitPartition.NestedTablePartitionSpec x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTableSplitPartition x) {
/*  781 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTableSplitPartition x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTableModify x) {
/*  791 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTableModify x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleCreateIndexStatement x) {
/*  801 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleCreateIndexStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterIndexStatement x) {
/*  811 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterIndexStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleForStatement x) {
/*  821 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleForStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterIndexStatement.Rebuild x) {
/*  831 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterIndexStatement.Rebuild x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleRangeExpr x) {
/*  841 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleRangeExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OraclePrimaryKey x) {
/*  851 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OraclePrimaryKey x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleCreateTableStatement x) {
/*  861 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleCreateTableStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleStorageClause x) {
/*  871 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleStorageClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleGotoStatement x) {
/*  881 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleGotoStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleLabelStatement x) {
/*  891 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleLabelStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleCommitStatement x) {
/*  901 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleCommitStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTriggerStatement x) {
/*  911 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTriggerStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterSynonymStatement x) {
/*  921 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterSynonymStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(FlashbackQueryClause.AsOfSnapshotClause x) {
/*  931 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(FlashbackQueryClause.AsOfSnapshotClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterViewStatement x) {
/*  941 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterViewStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTableMoveTablespace x) {
/*  951 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTableMoveTablespace x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSizeExpr x) {
/*  961 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleSizeExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleFileSpecification x) {
/*  971 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleFileSpecification x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTablespaceAddDataFile x) {
/*  981 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTablespaceAddDataFile x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTablespaceStatement x) {
/*  991 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTablespaceStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleExitStatement x) {
/* 1001 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleExitStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSavePointStatement x) {
/* 1011 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleSavePointStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleCreateDatabaseDbLinkStatement x) {
/* 1021 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleCreateDatabaseDbLinkStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleDropDbLinkStatement x) {
/* 1031 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleDropDbLinkStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleDataTypeTimestamp x) {
/* 1041 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleDataTypeTimestamp x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleDataTypeIntervalYear x) {
/* 1051 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleDataTypeIntervalYear x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleDataTypeIntervalDay x) {
/* 1061 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleDataTypeIntervalDay x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleUsingIndexClause x) {
/* 1071 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleUsingIndexClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleLobStorageClause x) {
/* 1081 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleLobStorageClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleUnique x) {
/* 1091 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleUnique x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleForeignKey x) {
/* 1101 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleForeignKey x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleCheck x) {
/* 1111 */     return true;
/*      */   }
/*      */   
/*      */   public void endVisit(OracleCheck x) {}
/*      */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\visitor\OracleASTVisitorAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */