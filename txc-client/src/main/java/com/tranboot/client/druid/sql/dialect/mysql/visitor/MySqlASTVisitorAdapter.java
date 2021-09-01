/*      */ package com.tranboot.client.druid.sql.dialect.mysql.visitor;
/*      */ 
/*      */

import com.tranboot.client.druid.sql.dialect.mysql.ast.*;
import com.tranboot.client.druid.sql.dialect.mysql.ast.clause.*;
import com.tranboot.client.druid.sql.dialect.mysql.ast.expr.*;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.*;
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
/*      */ public class MySqlASTVisitorAdapter
/*      */   extends SQLASTVisitorAdapter
/*      */   implements MySqlASTVisitor
/*      */ {
/*      */   public boolean visit(MySqlTableIndex x) {
/*  137 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlTableIndex x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlKey x) {
/*  147 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlKey x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlPrimaryKey x) {
/*  158 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlPrimaryKey x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlIntervalExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlIntervalExpr x) {
/*  174 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlExtractExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlExtractExpr x) {
/*  185 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlMatchAgainstExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlMatchAgainstExpr x) {
/*  196 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlPrepareStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlPrepareStatement x) {
/*  207 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlExecuteStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlExecuteStatement x) {
/*  218 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MysqlDeallocatePrepareStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MysqlDeallocatePrepareStatement x) {
/*  228 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlDeleteStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlDeleteStatement x) {
/*  239 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlInsertStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlInsertStatement x) {
/*  250 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlLoadDataInFileStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlLoadDataInFileStatement x) {
/*  261 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlLoadXmlStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlLoadXmlStatement x) {
/*  272 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlReplaceStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlReplaceStatement x) {
/*  283 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCommitStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCommitStatement x) {
/*  294 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlRollbackStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlRollbackStatement x) {
/*  305 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowColumnsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowColumnsStatement x) {
/*  316 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowDatabasesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowDatabasesStatement x) {
/*  327 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowWarningsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowWarningsStatement x) {
/*  338 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowStatusStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowStatusStatement x) {
/*  349 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(CobarShowStatus x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(CobarShowStatus x) {
/*  359 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlKillStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlKillStatement x) {
/*  369 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlBinlogStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlBinlogStatement x) {
/*  379 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlResetStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlResetStatement x) {
/*  389 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCreateUserStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCreateUserStatement x) {
/*  399 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCreateUserStatement.UserSpecification x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCreateUserStatement.UserSpecification x) {
/*  409 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlPartitionByKey x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlPartitionByKey x) {
/*  419 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSelectQueryBlock x) {
/*  424 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSelectQueryBlock x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlOutFileExpr x) {
/*  434 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlOutFileExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlDescribeStatement x) {
/*  444 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlDescribeStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlUpdateStatement x) {
/*  454 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlUpdateStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSetTransactionStatement x) {
/*  464 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSetTransactionStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSetNamesStatement x) {
/*  474 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSetNamesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSetCharSetStatement x) {
/*  484 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSetCharSetStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowAuthorsStatement x) {
/*  494 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowAuthorsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowBinaryLogsStatement x) {
/*  504 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowBinaryLogsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowMasterLogsStatement x) {
/*  514 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowMasterLogsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCollationStatement x) {
/*  524 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCollationStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowBinLogEventsStatement x) {
/*  534 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowBinLogEventsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCharacterSetStatement x) {
/*  544 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCharacterSetStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowContributorsStatement x) {
/*  554 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowContributorsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCreateDatabaseStatement x) {
/*  564 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCreateDatabaseStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCreateEventStatement x) {
/*  574 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCreateEventStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCreateFunctionStatement x) {
/*  584 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCreateFunctionStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCreateProcedureStatement x) {
/*  594 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCreateProcedureStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCreateTableStatement x) {
/*  604 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCreateTableStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCreateTriggerStatement x) {
/*  614 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCreateTriggerStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCreateViewStatement x) {
/*  624 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCreateViewStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowEngineStatement x) {
/*  634 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowEngineStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowEnginesStatement x) {
/*  644 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowEnginesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowErrorsStatement x) {
/*  654 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowErrorsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowEventsStatement x) {
/*  664 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowEventsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowFunctionCodeStatement x) {
/*  674 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowFunctionCodeStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowFunctionStatusStatement x) {
/*  684 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowFunctionStatusStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowGrantsStatement x) {
/*  694 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowGrantsStatement x) {}
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlUserName x) {
/*  703 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlUserName x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowIndexesStatement x) {
/*  713 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowIndexesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowKeysStatement x) {
/*  723 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowKeysStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowMasterStatusStatement x) {
/*  733 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowMasterStatusStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowOpenTablesStatement x) {
/*  743 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowOpenTablesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowPluginsStatement x) {
/*  753 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowPluginsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowPrivilegesStatement x) {
/*  763 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowPrivilegesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowProcedureCodeStatement x) {
/*  773 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowProcedureCodeStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowProcedureStatusStatement x) {
/*  783 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowProcedureStatusStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowProcessListStatement x) {
/*  793 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowProcessListStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowProfileStatement x) {
/*  803 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowProfileStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowProfilesStatement x) {
/*  813 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowProfilesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowRelayLogEventsStatement x) {
/*  823 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowRelayLogEventsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowSlaveHostsStatement x) {
/*  833 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowSlaveHostsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowSlaveStatusStatement x) {
/*  843 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowSlaveStatusStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowTableStatusStatement x) {
/*  853 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowTableStatusStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowTriggersStatement x) {
/*  863 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowTriggersStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowVariantsStatement x) {
/*  873 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowVariantsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlRenameTableStatement.Item x) {
/*  883 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlRenameTableStatement.Item x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlRenameTableStatement x) {
/*  893 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlRenameTableStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlUnionQuery x) {
/*  903 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlUnionQuery x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlUseIndexHint x) {
/*  913 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlUseIndexHint x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlIgnoreIndexHint x) {
/*  923 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlIgnoreIndexHint x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlLockTableStatement x) {
/*  933 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlLockTableStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlUnlockTablesStatement x) {
/*  943 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlUnlockTablesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlForceIndexHint x) {
/*  953 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlForceIndexHint x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterTableChangeColumn x) {
/*  963 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterTableChangeColumn x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterTableCharacter x) {
/*  973 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterTableCharacter x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterTableOption x) {
/*  983 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterTableOption x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCreateTableStatement x) {
/*  993 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCreateTableStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlHelpStatement x) {
/* 1003 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlHelpStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCharExpr x) {
/* 1013 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCharExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlUnique x) {
/* 1023 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlUnique x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MysqlForeignKey x) {
/* 1033 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MysqlForeignKey x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterTableModifyColumn x) {
/* 1043 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterTableModifyColumn x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterTableDiscardTablespace x) {
/* 1053 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterTableDiscardTablespace x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterTableImportTablespace x) {
/* 1063 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterTableImportTablespace x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCreateTableStatement.TableSpaceOption x) {
/* 1073 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCreateTableStatement.TableSpaceOption x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAnalyzeStatement x) {
/* 1083 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAnalyzeStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterUserStatement x) {
/* 1093 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterUserStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlOptimizeStatement x) {
/* 1103 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlOptimizeStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSetPasswordStatement x) {
/* 1113 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSetPasswordStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlHintStatement x) {
/* 1123 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlHintStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlOrderingExpr x) {
/* 1133 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlOrderingExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlWhileStatement x) {
/* 1143 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlWhileStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCaseStatement x) {
/* 1153 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCaseStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlDeclareStatement x) {
/* 1163 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlDeclareStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSelectIntoStatement x) {
/* 1173 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSelectIntoStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCaseStatement.MySqlWhenStatement x) {
/* 1183 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCaseStatement.MySqlWhenStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlLeaveStatement x) {
/* 1194 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlLeaveStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlIterateStatement x) {
/* 1204 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlIterateStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlRepeatStatement x) {
/* 1214 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlRepeatStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCursorDeclareStatement x) {
/* 1224 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCursorDeclareStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlUpdateTableSource x) {
/* 1234 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlUpdateTableSource x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterTableAlterColumn x) {
/* 1244 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterTableAlterColumn x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSubPartitionByKey x) {
/* 1254 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSubPartitionByKey x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSubPartitionByList x) {
/* 1264 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSubPartitionByList x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlDeclareHandlerStatement x) {
/* 1274 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlDeclareHandlerStatement x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlDeclareConditionStatement x) {
/* 1286 */     return false;
/*      */   }
/*      */   
/*      */   public void endVisit(MySqlDeclareConditionStatement x) {}
/*      */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\visitor\MySqlASTVisitorAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */