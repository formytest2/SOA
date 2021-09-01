package com.tranboot.client.druid.sql.dialect.mysql.visitor;

import com.tranboot.client.druid.sql.dialect.mysql.ast.*;
import com.tranboot.client.druid.sql.dialect.mysql.ast.clause.*;
import com.tranboot.client.druid.sql.dialect.mysql.ast.expr.*;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.*;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

public interface MySqlASTVisitor extends SQLASTVisitor {
  boolean visit(MySqlTableIndex paramMySqlTableIndex);
  
  void endVisit(MySqlTableIndex paramMySqlTableIndex);
  
  boolean visit(MySqlKey paramMySqlKey);
  
  void endVisit(MySqlKey paramMySqlKey);
  
  boolean visit(MySqlPrimaryKey paramMySqlPrimaryKey);
  
  void endVisit(MySqlPrimaryKey paramMySqlPrimaryKey);
  
  boolean visit(MySqlUnique paramMySqlUnique);
  
  void endVisit(MySqlUnique paramMySqlUnique);
  
  boolean visit(MysqlForeignKey paramMysqlForeignKey);
  
  void endVisit(MysqlForeignKey paramMysqlForeignKey);
  
  void endVisit(MySqlIntervalExpr paramMySqlIntervalExpr);
  
  boolean visit(MySqlIntervalExpr paramMySqlIntervalExpr);
  
  void endVisit(MySqlExtractExpr paramMySqlExtractExpr);
  
  boolean visit(MySqlExtractExpr paramMySqlExtractExpr);
  
  void endVisit(MySqlMatchAgainstExpr paramMySqlMatchAgainstExpr);
  
  boolean visit(MySqlMatchAgainstExpr paramMySqlMatchAgainstExpr);
  
  void endVisit(MySqlPrepareStatement paramMySqlPrepareStatement);
  
  boolean visit(MySqlPrepareStatement paramMySqlPrepareStatement);
  
  void endVisit(MySqlExecuteStatement paramMySqlExecuteStatement);
  
  boolean visit(MysqlDeallocatePrepareStatement paramMysqlDeallocatePrepareStatement);
  
  void endVisit(MysqlDeallocatePrepareStatement paramMysqlDeallocatePrepareStatement);
  
  boolean visit(MySqlExecuteStatement paramMySqlExecuteStatement);
  
  void endVisit(MySqlDeleteStatement paramMySqlDeleteStatement);
  
  boolean visit(MySqlDeleteStatement paramMySqlDeleteStatement);
  
  void endVisit(MySqlInsertStatement paramMySqlInsertStatement);
  
  boolean visit(MySqlInsertStatement paramMySqlInsertStatement);
  
  void endVisit(MySqlLoadDataInFileStatement paramMySqlLoadDataInFileStatement);
  
  boolean visit(MySqlLoadDataInFileStatement paramMySqlLoadDataInFileStatement);
  
  void endVisit(MySqlLoadXmlStatement paramMySqlLoadXmlStatement);
  
  boolean visit(MySqlLoadXmlStatement paramMySqlLoadXmlStatement);
  
  void endVisit(MySqlReplaceStatement paramMySqlReplaceStatement);
  
  boolean visit(MySqlReplaceStatement paramMySqlReplaceStatement);
  
  void endVisit(MySqlCommitStatement paramMySqlCommitStatement);
  
  boolean visit(MySqlCommitStatement paramMySqlCommitStatement);
  
  void endVisit(MySqlRollbackStatement paramMySqlRollbackStatement);
  
  boolean visit(MySqlRollbackStatement paramMySqlRollbackStatement);
  
  void endVisit(MySqlShowColumnsStatement paramMySqlShowColumnsStatement);
  
  boolean visit(MySqlShowColumnsStatement paramMySqlShowColumnsStatement);
  
  void endVisit(MySqlShowDatabasesStatement paramMySqlShowDatabasesStatement);
  
  boolean visit(MySqlShowDatabasesStatement paramMySqlShowDatabasesStatement);
  
  void endVisit(MySqlShowWarningsStatement paramMySqlShowWarningsStatement);
  
  boolean visit(MySqlShowWarningsStatement paramMySqlShowWarningsStatement);
  
  void endVisit(MySqlShowStatusStatement paramMySqlShowStatusStatement);
  
  boolean visit(MySqlShowStatusStatement paramMySqlShowStatusStatement);
  
  void endVisit(MySqlShowAuthorsStatement paramMySqlShowAuthorsStatement);
  
  boolean visit(MySqlShowAuthorsStatement paramMySqlShowAuthorsStatement);
  
  void endVisit(CobarShowStatus paramCobarShowStatus);
  
  boolean visit(CobarShowStatus paramCobarShowStatus);
  
  void endVisit(MySqlKillStatement paramMySqlKillStatement);
  
  boolean visit(MySqlKillStatement paramMySqlKillStatement);
  
  void endVisit(MySqlBinlogStatement paramMySqlBinlogStatement);
  
  boolean visit(MySqlBinlogStatement paramMySqlBinlogStatement);
  
  void endVisit(MySqlResetStatement paramMySqlResetStatement);
  
  boolean visit(MySqlResetStatement paramMySqlResetStatement);
  
  void endVisit(MySqlCreateUserStatement paramMySqlCreateUserStatement);
  
  boolean visit(MySqlCreateUserStatement paramMySqlCreateUserStatement);
  
  void endVisit(MySqlCreateUserStatement.UserSpecification paramUserSpecification);
  
  boolean visit(MySqlCreateUserStatement.UserSpecification paramUserSpecification);
  
  void endVisit(MySqlPartitionByKey paramMySqlPartitionByKey);
  
  boolean visit(MySqlPartitionByKey paramMySqlPartitionByKey);
  
  boolean visit(MySqlSelectQueryBlock paramMySqlSelectQueryBlock);
  
  void endVisit(MySqlSelectQueryBlock paramMySqlSelectQueryBlock);
  
  boolean visit(MySqlOutFileExpr paramMySqlOutFileExpr);
  
  void endVisit(MySqlOutFileExpr paramMySqlOutFileExpr);
  
  boolean visit(MySqlDescribeStatement paramMySqlDescribeStatement);
  
  void endVisit(MySqlDescribeStatement paramMySqlDescribeStatement);
  
  boolean visit(MySqlUpdateStatement paramMySqlUpdateStatement);
  
  void endVisit(MySqlUpdateStatement paramMySqlUpdateStatement);
  
  boolean visit(MySqlSetTransactionStatement paramMySqlSetTransactionStatement);
  
  void endVisit(MySqlSetTransactionStatement paramMySqlSetTransactionStatement);
  
  boolean visit(MySqlSetNamesStatement paramMySqlSetNamesStatement);
  
  void endVisit(MySqlSetNamesStatement paramMySqlSetNamesStatement);
  
  boolean visit(MySqlSetCharSetStatement paramMySqlSetCharSetStatement);
  
  void endVisit(MySqlSetCharSetStatement paramMySqlSetCharSetStatement);
  
  boolean visit(MySqlShowBinaryLogsStatement paramMySqlShowBinaryLogsStatement);
  
  void endVisit(MySqlShowBinaryLogsStatement paramMySqlShowBinaryLogsStatement);
  
  boolean visit(MySqlShowMasterLogsStatement paramMySqlShowMasterLogsStatement);
  
  void endVisit(MySqlShowMasterLogsStatement paramMySqlShowMasterLogsStatement);
  
  boolean visit(MySqlShowCharacterSetStatement paramMySqlShowCharacterSetStatement);
  
  void endVisit(MySqlShowCharacterSetStatement paramMySqlShowCharacterSetStatement);
  
  boolean visit(MySqlShowCollationStatement paramMySqlShowCollationStatement);
  
  void endVisit(MySqlShowCollationStatement paramMySqlShowCollationStatement);
  
  boolean visit(MySqlShowBinLogEventsStatement paramMySqlShowBinLogEventsStatement);
  
  void endVisit(MySqlShowBinLogEventsStatement paramMySqlShowBinLogEventsStatement);
  
  boolean visit(MySqlShowContributorsStatement paramMySqlShowContributorsStatement);
  
  void endVisit(MySqlShowContributorsStatement paramMySqlShowContributorsStatement);
  
  boolean visit(MySqlShowCreateDatabaseStatement paramMySqlShowCreateDatabaseStatement);
  
  void endVisit(MySqlShowCreateDatabaseStatement paramMySqlShowCreateDatabaseStatement);
  
  boolean visit(MySqlShowCreateEventStatement paramMySqlShowCreateEventStatement);
  
  void endVisit(MySqlShowCreateEventStatement paramMySqlShowCreateEventStatement);
  
  boolean visit(MySqlShowCreateFunctionStatement paramMySqlShowCreateFunctionStatement);
  
  void endVisit(MySqlShowCreateFunctionStatement paramMySqlShowCreateFunctionStatement);
  
  boolean visit(MySqlShowCreateProcedureStatement paramMySqlShowCreateProcedureStatement);
  
  void endVisit(MySqlShowCreateProcedureStatement paramMySqlShowCreateProcedureStatement);
  
  boolean visit(MySqlShowCreateTableStatement paramMySqlShowCreateTableStatement);
  
  void endVisit(MySqlShowCreateTableStatement paramMySqlShowCreateTableStatement);
  
  boolean visit(MySqlShowCreateTriggerStatement paramMySqlShowCreateTriggerStatement);
  
  void endVisit(MySqlShowCreateTriggerStatement paramMySqlShowCreateTriggerStatement);
  
  boolean visit(MySqlShowCreateViewStatement paramMySqlShowCreateViewStatement);
  
  void endVisit(MySqlShowCreateViewStatement paramMySqlShowCreateViewStatement);
  
  boolean visit(MySqlShowEngineStatement paramMySqlShowEngineStatement);
  
  void endVisit(MySqlShowEngineStatement paramMySqlShowEngineStatement);
  
  boolean visit(MySqlShowEnginesStatement paramMySqlShowEnginesStatement);
  
  void endVisit(MySqlShowEnginesStatement paramMySqlShowEnginesStatement);
  
  boolean visit(MySqlShowErrorsStatement paramMySqlShowErrorsStatement);
  
  void endVisit(MySqlShowErrorsStatement paramMySqlShowErrorsStatement);
  
  boolean visit(MySqlShowEventsStatement paramMySqlShowEventsStatement);
  
  void endVisit(MySqlShowEventsStatement paramMySqlShowEventsStatement);
  
  boolean visit(MySqlShowFunctionCodeStatement paramMySqlShowFunctionCodeStatement);
  
  void endVisit(MySqlShowFunctionCodeStatement paramMySqlShowFunctionCodeStatement);
  
  boolean visit(MySqlShowFunctionStatusStatement paramMySqlShowFunctionStatusStatement);
  
  void endVisit(MySqlShowFunctionStatusStatement paramMySqlShowFunctionStatusStatement);
  
  boolean visit(MySqlShowGrantsStatement paramMySqlShowGrantsStatement);
  
  void endVisit(MySqlShowGrantsStatement paramMySqlShowGrantsStatement);
  
  boolean visit(MySqlUserName paramMySqlUserName);
  
  void endVisit(MySqlUserName paramMySqlUserName);
  
  boolean visit(MySqlShowIndexesStatement paramMySqlShowIndexesStatement);
  
  void endVisit(MySqlShowIndexesStatement paramMySqlShowIndexesStatement);
  
  boolean visit(MySqlShowKeysStatement paramMySqlShowKeysStatement);
  
  void endVisit(MySqlShowKeysStatement paramMySqlShowKeysStatement);
  
  boolean visit(MySqlShowMasterStatusStatement paramMySqlShowMasterStatusStatement);
  
  void endVisit(MySqlShowMasterStatusStatement paramMySqlShowMasterStatusStatement);
  
  boolean visit(MySqlShowOpenTablesStatement paramMySqlShowOpenTablesStatement);
  
  void endVisit(MySqlShowOpenTablesStatement paramMySqlShowOpenTablesStatement);
  
  boolean visit(MySqlShowPluginsStatement paramMySqlShowPluginsStatement);
  
  void endVisit(MySqlShowPluginsStatement paramMySqlShowPluginsStatement);
  
  boolean visit(MySqlShowPrivilegesStatement paramMySqlShowPrivilegesStatement);
  
  void endVisit(MySqlShowPrivilegesStatement paramMySqlShowPrivilegesStatement);
  
  boolean visit(MySqlShowProcedureCodeStatement paramMySqlShowProcedureCodeStatement);
  
  void endVisit(MySqlShowProcedureCodeStatement paramMySqlShowProcedureCodeStatement);
  
  boolean visit(MySqlShowProcedureStatusStatement paramMySqlShowProcedureStatusStatement);
  
  void endVisit(MySqlShowProcedureStatusStatement paramMySqlShowProcedureStatusStatement);
  
  boolean visit(MySqlShowProcessListStatement paramMySqlShowProcessListStatement);
  
  void endVisit(MySqlShowProcessListStatement paramMySqlShowProcessListStatement);
  
  boolean visit(MySqlShowProfileStatement paramMySqlShowProfileStatement);
  
  void endVisit(MySqlShowProfileStatement paramMySqlShowProfileStatement);
  
  boolean visit(MySqlShowProfilesStatement paramMySqlShowProfilesStatement);
  
  void endVisit(MySqlShowProfilesStatement paramMySqlShowProfilesStatement);
  
  boolean visit(MySqlShowRelayLogEventsStatement paramMySqlShowRelayLogEventsStatement);
  
  void endVisit(MySqlShowRelayLogEventsStatement paramMySqlShowRelayLogEventsStatement);
  
  boolean visit(MySqlShowSlaveHostsStatement paramMySqlShowSlaveHostsStatement);
  
  void endVisit(MySqlShowSlaveHostsStatement paramMySqlShowSlaveHostsStatement);
  
  boolean visit(MySqlShowSlaveStatusStatement paramMySqlShowSlaveStatusStatement);
  
  void endVisit(MySqlShowSlaveStatusStatement paramMySqlShowSlaveStatusStatement);
  
  boolean visit(MySqlShowTableStatusStatement paramMySqlShowTableStatusStatement);
  
  void endVisit(MySqlShowTableStatusStatement paramMySqlShowTableStatusStatement);
  
  boolean visit(MySqlShowTriggersStatement paramMySqlShowTriggersStatement);
  
  void endVisit(MySqlShowTriggersStatement paramMySqlShowTriggersStatement);
  
  boolean visit(MySqlShowVariantsStatement paramMySqlShowVariantsStatement);
  
  void endVisit(MySqlShowVariantsStatement paramMySqlShowVariantsStatement);
  
  boolean visit(MySqlRenameTableStatement.Item paramItem);
  
  void endVisit(MySqlRenameTableStatement.Item paramItem);
  
  boolean visit(MySqlRenameTableStatement paramMySqlRenameTableStatement);
  
  void endVisit(MySqlRenameTableStatement paramMySqlRenameTableStatement);
  
  boolean visit(MySqlUnionQuery paramMySqlUnionQuery);
  
  void endVisit(MySqlUnionQuery paramMySqlUnionQuery);
  
  boolean visit(MySqlUseIndexHint paramMySqlUseIndexHint);
  
  void endVisit(MySqlUseIndexHint paramMySqlUseIndexHint);
  
  boolean visit(MySqlIgnoreIndexHint paramMySqlIgnoreIndexHint);
  
  void endVisit(MySqlIgnoreIndexHint paramMySqlIgnoreIndexHint);
  
  boolean visit(MySqlLockTableStatement paramMySqlLockTableStatement);
  
  void endVisit(MySqlLockTableStatement paramMySqlLockTableStatement);
  
  boolean visit(MySqlUnlockTablesStatement paramMySqlUnlockTablesStatement);
  
  void endVisit(MySqlUnlockTablesStatement paramMySqlUnlockTablesStatement);
  
  boolean visit(MySqlForceIndexHint paramMySqlForceIndexHint);
  
  void endVisit(MySqlForceIndexHint paramMySqlForceIndexHint);
  
  boolean visit(MySqlAlterTableChangeColumn paramMySqlAlterTableChangeColumn);
  
  void endVisit(MySqlAlterTableChangeColumn paramMySqlAlterTableChangeColumn);
  
  boolean visit(MySqlAlterTableCharacter paramMySqlAlterTableCharacter);
  
  void endVisit(MySqlAlterTableCharacter paramMySqlAlterTableCharacter);
  
  boolean visit(MySqlAlterTableOption paramMySqlAlterTableOption);
  
  void endVisit(MySqlAlterTableOption paramMySqlAlterTableOption);
  
  boolean visit(MySqlCreateTableStatement paramMySqlCreateTableStatement);
  
  void endVisit(MySqlCreateTableStatement paramMySqlCreateTableStatement);
  
  boolean visit(MySqlHelpStatement paramMySqlHelpStatement);
  
  void endVisit(MySqlHelpStatement paramMySqlHelpStatement);
  
  boolean visit(MySqlCharExpr paramMySqlCharExpr);
  
  void endVisit(MySqlCharExpr paramMySqlCharExpr);
  
  boolean visit(MySqlAlterTableModifyColumn paramMySqlAlterTableModifyColumn);
  
  void endVisit(MySqlAlterTableModifyColumn paramMySqlAlterTableModifyColumn);
  
  boolean visit(MySqlAlterTableDiscardTablespace paramMySqlAlterTableDiscardTablespace);
  
  void endVisit(MySqlAlterTableDiscardTablespace paramMySqlAlterTableDiscardTablespace);
  
  boolean visit(MySqlAlterTableImportTablespace paramMySqlAlterTableImportTablespace);
  
  void endVisit(MySqlAlterTableImportTablespace paramMySqlAlterTableImportTablespace);
  
  boolean visit(MySqlCreateTableStatement.TableSpaceOption paramTableSpaceOption);
  
  void endVisit(MySqlCreateTableStatement.TableSpaceOption paramTableSpaceOption);
  
  boolean visit(MySqlAnalyzeStatement paramMySqlAnalyzeStatement);
  
  void endVisit(MySqlAnalyzeStatement paramMySqlAnalyzeStatement);
  
  boolean visit(MySqlAlterUserStatement paramMySqlAlterUserStatement);
  
  void endVisit(MySqlAlterUserStatement paramMySqlAlterUserStatement);
  
  boolean visit(MySqlOptimizeStatement paramMySqlOptimizeStatement);
  
  void endVisit(MySqlOptimizeStatement paramMySqlOptimizeStatement);
  
  boolean visit(MySqlSetPasswordStatement paramMySqlSetPasswordStatement);
  
  void endVisit(MySqlSetPasswordStatement paramMySqlSetPasswordStatement);
  
  boolean visit(MySqlHintStatement paramMySqlHintStatement);
  
  void endVisit(MySqlHintStatement paramMySqlHintStatement);
  
  boolean visit(MySqlOrderingExpr paramMySqlOrderingExpr);
  
  void endVisit(MySqlOrderingExpr paramMySqlOrderingExpr);
  
  boolean visit(MySqlWhileStatement paramMySqlWhileStatement);
  
  void endVisit(MySqlWhileStatement paramMySqlWhileStatement);
  
  boolean visit(MySqlCaseStatement paramMySqlCaseStatement);
  
  void endVisit(MySqlCaseStatement paramMySqlCaseStatement);
  
  boolean visit(MySqlDeclareStatement paramMySqlDeclareStatement);
  
  void endVisit(MySqlDeclareStatement paramMySqlDeclareStatement);
  
  boolean visit(MySqlSelectIntoStatement paramMySqlSelectIntoStatement);
  
  void endVisit(MySqlSelectIntoStatement paramMySqlSelectIntoStatement);
  
  boolean visit(MySqlCaseStatement.MySqlWhenStatement paramMySqlWhenStatement);
  
  void endVisit(MySqlCaseStatement.MySqlWhenStatement paramMySqlWhenStatement);
  
  boolean visit(MySqlLeaveStatement paramMySqlLeaveStatement);
  
  void endVisit(MySqlLeaveStatement paramMySqlLeaveStatement);
  
  boolean visit(MySqlIterateStatement paramMySqlIterateStatement);
  
  void endVisit(MySqlIterateStatement paramMySqlIterateStatement);
  
  boolean visit(MySqlRepeatStatement paramMySqlRepeatStatement);
  
  void endVisit(MySqlRepeatStatement paramMySqlRepeatStatement);
  
  boolean visit(MySqlCursorDeclareStatement paramMySqlCursorDeclareStatement);
  
  void endVisit(MySqlCursorDeclareStatement paramMySqlCursorDeclareStatement);
  
  boolean visit(MySqlUpdateTableSource paramMySqlUpdateTableSource);
  
  void endVisit(MySqlUpdateTableSource paramMySqlUpdateTableSource);
  
  boolean visit(MySqlAlterTableAlterColumn paramMySqlAlterTableAlterColumn);
  
  void endVisit(MySqlAlterTableAlterColumn paramMySqlAlterTableAlterColumn);
  
  boolean visit(MySqlSubPartitionByKey paramMySqlSubPartitionByKey);
  
  void endVisit(MySqlSubPartitionByKey paramMySqlSubPartitionByKey);
  
  boolean visit(MySqlSubPartitionByList paramMySqlSubPartitionByList);
  
  void endVisit(MySqlSubPartitionByList paramMySqlSubPartitionByList);
  
  boolean visit(MySqlDeclareHandlerStatement paramMySqlDeclareHandlerStatement);
  
  void endVisit(MySqlDeclareHandlerStatement paramMySqlDeclareHandlerStatement);
  
  boolean visit(MySqlDeclareConditionStatement paramMySqlDeclareConditionStatement);
  
  void endVisit(MySqlDeclareConditionStatement paramMySqlDeclareConditionStatement);
}


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\visitor\MySqlASTVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */