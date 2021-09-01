/*      */ package com.tranboot.client.druid.sql.dialect.mysql.visitor;
/*      */ 
/*      */

import com.tranboot.client.druid.sql.ast.SQLDeclareItem;
import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLPropertyExpr;
import com.tranboot.client.druid.sql.ast.statement.*;
import com.tranboot.client.druid.sql.dialect.mysql.ast.*;
import com.tranboot.client.druid.sql.dialect.mysql.ast.clause.*;
import com.tranboot.client.druid.sql.dialect.mysql.ast.expr.*;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.*;
import com.tranboot.client.druid.sql.visitor.SchemaStatVisitor;
import com.tranboot.client.druid.stat.TableStat;

import java.util.Map;

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
/*      */ public class MySqlSchemaStatVisitor
/*      */   extends SchemaStatVisitor
/*      */   implements MySqlASTVisitor
/*      */ {
/*      */   public boolean visit(SQLSelectStatement x) {
/*  156 */     setAliasMap();
/*  157 */     getAliasMap().put("DUAL", null);
/*      */     
/*  159 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDbType() {
/*  164 */     return "mysql";
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlDeleteStatement x) {
/*  169 */     setAliasMap();
/*      */     
/*  171 */     setMode((SQLObject)x, TableStat.Mode.Delete);
/*      */     
/*  173 */     accept((SQLObject)x.getFrom());
/*  174 */     accept((SQLObject)x.getUsing());
/*  175 */     x.getTableSource().accept(this);
/*      */     
/*  177 */     if (x.getTableSource() instanceof SQLExprTableSource) {
/*  178 */       SQLName tableName = (SQLName)((SQLExprTableSource)x.getTableSource()).getExpr();
/*  179 */       String ident = tableName.toString();
/*  180 */       setCurrentTable((SQLObject)x, ident);
/*      */       
/*  182 */       TableStat stat = getTableStat(ident);
/*  183 */       stat.incrementDeleteCount();
/*      */     } 
/*      */     
/*  186 */     accept((SQLObject)x.getWhere());
/*      */     
/*  188 */     accept((SQLObject)x.getOrderBy());
/*  189 */     accept((SQLObject)x.getLimit());
/*      */     
/*  191 */     return false;
/*      */   }
/*      */   
/*      */   public void endVisit(MySqlDeleteStatement x) {
/*  195 */     setAliasMap(null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlInsertStatement x) {
/*  200 */     setModeOrigin((SQLObject)x);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlInsertStatement x) {
/*  205 */     setMode((SQLObject)x, TableStat.Mode.Insert);
/*      */     
/*  207 */     setAliasMap();
/*      */     
/*  209 */     SQLName tableName = x.getTableName();
/*      */     
/*  211 */     String ident = null;
/*  212 */     if (tableName instanceof SQLIdentifierExpr) {
/*  213 */       ident = ((SQLIdentifierExpr)tableName).getName();
/*  214 */     } else if (tableName instanceof SQLPropertyExpr) {
/*  215 */       SQLPropertyExpr propertyExpr = (SQLPropertyExpr)tableName;
/*  216 */       if (propertyExpr.getOwner() instanceof SQLIdentifierExpr) {
/*  217 */         ident = propertyExpr.toString();
/*      */       }
/*      */     } 
/*      */     
/*  221 */     if (ident != null) {
/*  222 */       setCurrentTable((SQLObject)x, ident);
/*      */       
/*  224 */       TableStat stat = getTableStat(ident);
/*  225 */       stat.incrementInsertCount();
/*      */       
/*  227 */       Map<String, String> aliasMap = getAliasMap();
/*  228 */       putAliasMap(aliasMap, x.getAlias(), ident);
/*  229 */       putAliasMap(aliasMap, ident, ident);
/*      */     } 
/*      */     
/*  232 */     accept(x.getColumns());
/*  233 */     accept(x.getValuesList());
/*  234 */     accept((SQLObject)x.getQuery());
/*  235 */     accept(x.getDuplicateKeyUpdate());
/*      */     
/*  237 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlTableIndex x) {
/*  243 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlTableIndex x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlKey x) {
/*  253 */     for (SQLObject item : x.getColumns()) {
/*  254 */       item.accept(this);
/*      */     }
/*  256 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlKey x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlPrimaryKey x) {
/*  266 */     for (SQLObject item : x.getColumns()) {
/*  267 */       item.accept(this);
/*      */     }
/*  269 */     return false;
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
/*  285 */     return true;
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
/*  296 */     return true;
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
/*  307 */     return true;
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
/*  318 */     return true;
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
/*  329 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MysqlDeallocatePrepareStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MysqlDeallocatePrepareStatement x) {
/*  339 */     return true;
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
/*  350 */     return true;
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
/*  361 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlReplaceStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlReplaceStatement x) {
/*  371 */     setMode((SQLObject)x, TableStat.Mode.Replace);
/*      */     
/*  373 */     setAliasMap();
/*      */     
/*  375 */     SQLName tableName = x.getTableName();
/*      */     
/*  377 */     String ident = null;
/*  378 */     if (tableName instanceof SQLIdentifierExpr) {
/*  379 */       ident = ((SQLIdentifierExpr)tableName).getName();
/*  380 */     } else if (tableName instanceof SQLPropertyExpr) {
/*  381 */       SQLPropertyExpr propertyExpr = (SQLPropertyExpr)tableName;
/*  382 */       if (propertyExpr.getOwner() instanceof SQLIdentifierExpr) {
/*  383 */         ident = propertyExpr.toString();
/*      */       }
/*      */     } 
/*      */     
/*  387 */     if (ident != null) {
/*  388 */       setCurrentTable((SQLObject)x, ident);
/*      */       
/*  390 */       TableStat stat = getTableStat(ident);
/*  391 */       stat.incrementInsertCount();
/*      */       
/*  393 */       Map<String, String> aliasMap = getAliasMap();
/*  394 */       putAliasMap(aliasMap, ident, ident);
/*      */     } 
/*      */     
/*  397 */     accept(x.getColumns());
/*  398 */     accept(x.getValuesList());
/*  399 */     accept((SQLObject)x.getQuery());
/*      */     
/*  401 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(SQLStartTransactionStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(SQLStartTransactionStatement x) {
/*  412 */     return true;
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
/*  423 */     return true;
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
/*  434 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowColumnsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowColumnsStatement x) {
/*  444 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowDatabasesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowDatabasesStatement x) {
/*  454 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowWarningsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowWarningsStatement x) {
/*  464 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowStatusStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowStatusStatement x) {
/*  474 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(CobarShowStatus x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(CobarShowStatus x) {
/*  484 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlKillStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlKillStatement x) {
/*  494 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlBinlogStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlBinlogStatement x) {
/*  504 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlResetStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlResetStatement x) {
/*  514 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCreateUserStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCreateUserStatement x) {
/*  524 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCreateUserStatement.UserSpecification x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCreateUserStatement.UserSpecification x) {
/*  534 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlPartitionByKey x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlPartitionByKey x) {
/*  544 */     accept(x.getColumns());
/*  545 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSelectQueryBlock x) {
/*  550 */     return visit((SQLSelectQueryBlock)x);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSelectQueryBlock x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlOutFileExpr x) {
/*  560 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlOutFileExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlDescribeStatement x) {
/*  570 */     String table = x.getObject().toString();
/*  571 */     getTableStat(table);
/*  572 */     if (x.getColName() != null) {
/*  573 */       addColumn(table, x.getColName().toString());
/*      */     }
/*  575 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlDescribeStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlUpdateStatement x) {
/*  586 */     visit((SQLUpdateStatement)x);
/*  587 */     for (SQLExpr item : x.getReturning()) {
/*  588 */       item.accept(this);
/*      */     }
/*      */     
/*  591 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlUpdateStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSetTransactionStatement x) {
/*  601 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSetTransactionStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSetNamesStatement x) {
/*  611 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSetNamesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSetCharSetStatement x) {
/*  621 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSetCharSetStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowAuthorsStatement x) {
/*  631 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowAuthorsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowBinaryLogsStatement x) {
/*  641 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowBinaryLogsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowMasterLogsStatement x) {
/*  651 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowMasterLogsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCollationStatement x) {
/*  661 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCollationStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowBinLogEventsStatement x) {
/*  671 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowBinLogEventsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCharacterSetStatement x) {
/*  681 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCharacterSetStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowContributorsStatement x) {
/*  691 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowContributorsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCreateDatabaseStatement x) {
/*  701 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCreateDatabaseStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCreateEventStatement x) {
/*  711 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCreateEventStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCreateFunctionStatement x) {
/*  721 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCreateFunctionStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCreateProcedureStatement x) {
/*  731 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCreateProcedureStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCreateTableStatement x) {
/*  741 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCreateTableStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCreateTriggerStatement x) {
/*  751 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCreateTriggerStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCreateViewStatement x) {
/*  761 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCreateViewStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowEngineStatement x) {
/*  771 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowEngineStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowEnginesStatement x) {
/*  781 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowEnginesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowErrorsStatement x) {
/*  791 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowErrorsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowEventsStatement x) {
/*  801 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowEventsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowFunctionCodeStatement x) {
/*  811 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowFunctionCodeStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowFunctionStatusStatement x) {
/*  821 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowFunctionStatusStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowGrantsStatement x) {
/*  831 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowGrantsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlUserName x) {
/*  841 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlUserName x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowIndexesStatement x) {
/*  851 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowIndexesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowKeysStatement x) {
/*  861 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowKeysStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowMasterStatusStatement x) {
/*  871 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowMasterStatusStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowOpenTablesStatement x) {
/*  881 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowOpenTablesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowPluginsStatement x) {
/*  891 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowPluginsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowPrivilegesStatement x) {
/*  901 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowPrivilegesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowProcedureCodeStatement x) {
/*  911 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowProcedureCodeStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowProcedureStatusStatement x) {
/*  921 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowProcedureStatusStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowProcessListStatement x) {
/*  931 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowProcessListStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowProfileStatement x) {
/*  941 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowProfileStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowProfilesStatement x) {
/*  951 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowProfilesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowRelayLogEventsStatement x) {
/*  961 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowRelayLogEventsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowSlaveHostsStatement x) {
/*  971 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowSlaveHostsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowSlaveStatusStatement x) {
/*  981 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowSlaveStatusStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowTableStatusStatement x) {
/*  991 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowTableStatusStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowTriggersStatement x) {
/* 1001 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowTriggersStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowVariantsStatement x) {
/* 1011 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowVariantsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlRenameTableStatement.Item x) {
/* 1021 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlRenameTableStatement.Item x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlRenameTableStatement x) {
/* 1031 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlRenameTableStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlUnionQuery x) {
/* 1041 */     return visit((SQLUnionQuery)x);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlUnionQuery x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlUseIndexHint x) {
/* 1051 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlUseIndexHint x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlIgnoreIndexHint x) {
/* 1061 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlIgnoreIndexHint x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlLockTableStatement x) {
/* 1071 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlLockTableStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlUnlockTablesStatement x) {
/* 1081 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlUnlockTablesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlForceIndexHint x) {
/* 1091 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlForceIndexHint x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterTableChangeColumn x) {
/* 1101 */     SQLAlterTableStatement stmt = (SQLAlterTableStatement)x.getParent();
/* 1102 */     String table = stmt.getName().toString();
/*      */     
/* 1104 */     String columnName = x.getColumnName().toString();
/* 1105 */     addColumn(table, columnName);
/* 1106 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterTableChangeColumn x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterTableModifyColumn x) {
/* 1116 */     SQLAlterTableStatement stmt = (SQLAlterTableStatement)x.getParent();
/* 1117 */     String table = stmt.getName().toString();
/*      */     
/* 1119 */     String columnName = x.getNewColumnDefinition().getName().toString();
/* 1120 */     addColumn(table, columnName);
/* 1121 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterTableModifyColumn x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterTableCharacter x) {
/* 1131 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterTableCharacter x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterTableOption x) {
/* 1141 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterTableOption x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCreateTableStatement x) {
/* 1151 */     boolean val = visit((SQLCreateTableStatement)x);
/*      */     
/* 1153 */     for (SQLObject option : x.getTableOptions().values()) {
/* 1154 */       if (option instanceof com.tranboot.client.druid.sql.ast.statement.SQLTableSource) {
/* 1155 */         option.accept(this);
/*      */       }
/*      */     } 
/*      */     
/* 1159 */     return val;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCreateTableStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlHelpStatement x) {
/* 1169 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlHelpStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCharExpr x) {
/* 1179 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCharExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlUnique x) {
/* 1189 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlUnique x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MysqlForeignKey x) {
/* 1199 */     return visit((SQLForeignKeyImpl)x);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MysqlForeignKey x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterTableDiscardTablespace x) {
/* 1209 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterTableDiscardTablespace x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterTableImportTablespace x) {
/* 1219 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterTableImportTablespace x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCreateTableStatement.TableSpaceOption x) {
/* 1229 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCreateTableStatement.TableSpaceOption x) {}
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAnalyzeStatement x) {
/* 1238 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAnalyzeStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterUserStatement x) {
/* 1248 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterUserStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlOptimizeStatement x) {
/* 1258 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlOptimizeStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSetPasswordStatement x) {
/* 1268 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSetPasswordStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlHintStatement x) {
/* 1278 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlHintStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlOrderingExpr x) {
/* 1288 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlOrderingExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterTableAlterColumn x) {
/* 1298 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterTableAlterColumn x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlWhileStatement x) {
/* 1311 */     accept(x.getStatements());
/* 1312 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlWhileStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCaseStatement x) {
/* 1322 */     accept(x.getWhenList());
/* 1323 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCaseStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlDeclareStatement x) {
/* 1333 */     for (SQLDeclareItem item : x.getVarList()) {
/* 1334 */       item.setParent((SQLObject)x);
/*      */       
/* 1336 */       SQLName var = (SQLName)item.getName();
/* 1337 */       this.variants.put(var.toString(), var);
/*      */     } 
/*      */     
/* 1340 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlDeclareStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSelectIntoStatement x) {
/* 1350 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSelectIntoStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCaseStatement.MySqlWhenStatement x) {
/* 1360 */     accept(x.getStatements());
/* 1361 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCaseStatement.MySqlWhenStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlLeaveStatement x) {
/* 1371 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlLeaveStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlIterateStatement x) {
/* 1381 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlIterateStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlRepeatStatement x) {
/* 1391 */     accept(x.getStatements());
/* 1392 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlRepeatStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCursorDeclareStatement x) {
/* 1402 */     accept((SQLObject)x.getSelect());
/* 1403 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCursorDeclareStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlUpdateTableSource x) {
/* 1413 */     if (x.getUpdate() != null) {
/* 1414 */       return visit(x.getUpdate());
/*      */     }
/* 1416 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlUpdateTableSource x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSubPartitionByKey x) {
/* 1426 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSubPartitionByKey x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSubPartitionByList x) {
/* 1436 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSubPartitionByList x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlDeclareHandlerStatement x) {
/* 1447 */     return false;
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
/* 1459 */     return false;
/*      */   }
/*      */   
/*      */   public void endVisit(MySqlDeclareConditionStatement x) {}
/*      */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\visitor\MySqlSchemaStatVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */