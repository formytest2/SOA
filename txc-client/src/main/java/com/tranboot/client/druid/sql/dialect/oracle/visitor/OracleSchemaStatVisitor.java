/*      */ package com.tranboot.client.druid.sql.dialect.oracle.visitor;
/*      */ 
/*      */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLPropertyExpr;
import com.tranboot.client.druid.sql.ast.statement.*;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleDataTypeIntervalDay;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleDataTypeIntervalYear;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleDataTypeTimestamp;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.*;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.*;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.*;
import com.tranboot.client.druid.sql.visitor.SchemaStatVisitor;
import com.tranboot.client.druid.stat.TableStat;

import java.util.ArrayList;
import java.util.List;
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
/*      */ public class OracleSchemaStatVisitor
/*      */   extends SchemaStatVisitor
/*      */   implements OracleASTVisitor
/*      */ {
/*      */   public OracleSchemaStatVisitor() {
/*  144 */     this(new ArrayList());
/*      */   }
/*      */   
/*      */   public OracleSchemaStatVisitor(List<Object> parameters) {
/*  148 */     super(parameters);
/*  149 */     this.variants.put("DUAL", null);
/*  150 */     this.variants.put("NOTFOUND", null);
/*  151 */     this.variants.put("TRUE", null);
/*  152 */     this.variants.put("FALSE", null);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDbType() {
/*  157 */     return "oracle";
/*      */   }
/*      */   
/*      */   protected TableStat.Column getColumn(SQLExpr expr) {
/*  161 */     if (expr instanceof OracleOuterExpr) {
/*  162 */       expr = ((OracleOuterExpr)expr).getExpr();
/*      */     }
/*      */     
/*  165 */     return super.getColumn(expr);
/*      */   }
/*      */   
/*      */   public boolean visit(OracleSelectTableReference x) {
/*  169 */     SQLExpr expr = x.getExpr();
/*      */     
/*  171 */     if (expr instanceof SQLMethodInvokeExpr) {
/*  172 */       SQLMethodInvokeExpr methodInvoke = (SQLMethodInvokeExpr)expr;
/*  173 */       if ("TABLE".equalsIgnoreCase(methodInvoke.getMethodName()) && methodInvoke.getParameters().size() == 1) {
/*  174 */         expr = methodInvoke.getParameters().get(0);
/*      */       }
/*      */     } 
/*      */     
/*  178 */     Map<String, String> aliasMap = getAliasMap();
/*      */     
/*  180 */     if (expr instanceof SQLName) {
/*      */       String ident;
/*  182 */       if (expr instanceof SQLPropertyExpr) {
/*  183 */         String owner = ((SQLPropertyExpr)expr).getOwner().toString();
/*  184 */         String name = ((SQLPropertyExpr)expr).getName();
/*      */         
/*  186 */         if (aliasMap.containsKey(owner)) {
/*  187 */           owner = aliasMap.get(owner);
/*      */         }
/*  189 */         ident = owner + "." + name;
/*      */       } else {
/*  191 */         ident = expr.toString();
/*      */       } 
/*      */       
/*  194 */       if (containsSubQuery(ident)) {
/*  195 */         return false;
/*      */       }
/*      */       
/*  198 */       if ("DUAL".equalsIgnoreCase(ident)) {
/*  199 */         return false;
/*      */       }
/*      */       
/*  202 */       x.putAttribute("_table_", ident);
/*      */       
/*  204 */       TableStat stat = getTableStat(ident);
/*      */       
/*  206 */       TableStat.Mode mode = getMode();
/*  207 */       switch (mode) {
/*      */         case Delete:
/*  209 */           stat.incrementDeleteCount();
/*      */           break;
/*      */         case Insert:
/*  212 */           stat.incrementInsertCount();
/*      */           break;
/*      */         case Update:
/*  215 */           stat.incrementUpdateCount();
/*      */           break;
/*      */         case Select:
/*  218 */           stat.incrementSelectCount();
/*      */           break;
/*      */         case Merge:
/*  221 */           stat.incrementMergeCount();
/*      */           break;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  227 */       putAliasMap(aliasMap, x.getAlias(), ident);
/*  228 */       putAliasMap(aliasMap, ident, ident);
/*  229 */       return false;
/*      */     } 
/*      */     
/*  232 */     accept((SQLObject)x.getExpr());
/*      */     
/*  234 */     return false;
/*      */   }
/*      */   
/*      */   public void endVisit(OracleSelect x) {
/*  238 */     endVisit((SQLSelect)x);
/*      */   }
/*      */   
/*      */   public boolean visit(OracleSelect x) {
/*  242 */     return visit((SQLSelect)x);
/*      */   }
/*      */   
/*      */   public void endVisit(SQLSelect x) {
/*  246 */     if (x.getQuery() != null) {
/*  247 */       String table = (String)x.getQuery().getAttribute("_table_");
/*  248 */       if (table != null) {
/*  249 */         x.putAttribute("_table_", table);
/*      */       }
/*      */     } 
/*  252 */     restoreCurrentTable((SQLObject)x);
/*      */   }
/*      */   
/*      */   public boolean visit(OracleUpdateStatement x) {
/*  256 */     setAliasMap();
/*  257 */     setMode((SQLObject)x, TableStat.Mode.Update);
/*      */     
/*  259 */     SQLTableSource tableSource = x.getTableSource();
/*  260 */     SQLExpr tableExpr = null;
/*      */     
/*  262 */     if (tableSource instanceof SQLExprTableSource) {
/*  263 */       tableExpr = ((SQLExprTableSource)tableSource).getExpr();
/*      */     }
/*      */     
/*  266 */     if (tableExpr instanceof SQLName) {
/*  267 */       String ident = tableExpr.toString();
/*  268 */       setCurrentTable(ident);
/*      */       
/*  270 */       TableStat stat = getTableStat(ident);
/*  271 */       stat.incrementUpdateCount();
/*      */       
/*  273 */       Map<String, String> aliasMap = getAliasMap();
/*  274 */       aliasMap.put(ident, ident);
/*  275 */       aliasMap.put(tableSource.getAlias(), ident);
/*      */     } else {
/*  277 */       tableSource.accept(this);
/*      */     } 
/*      */     
/*  280 */     accept(x.getItems());
/*  281 */     accept((SQLObject)x.getWhere());
/*      */     
/*  283 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void endVisit(OracleUpdateStatement x) {}
/*      */   
/*      */   public boolean visit(OracleDeleteStatement x) {
/*  290 */     return visit((SQLDeleteStatement)x);
/*      */   }
/*      */ 
/*      */   
/*      */   public void endVisit(OracleDeleteStatement x) {}
/*      */   
/*      */   public boolean visit(OracleSelectQueryBlock x) {
/*  297 */     if (x.getWhere() != null) {
/*  298 */       x.getWhere().setParent((SQLObject)x);
/*      */     }
/*      */     
/*  301 */     if (x.getInto() instanceof SQLName) {
/*  302 */       String tableName = x.getInto().toString();
/*  303 */       TableStat stat = getTableStat(tableName);
/*  304 */       if (stat != null) {
/*  305 */         stat.incrementInsertCount();
/*      */       }
/*      */     } 
/*      */     
/*  309 */     visit((SQLSelectQueryBlock)x);
/*      */     
/*  311 */     return true;
/*      */   }
/*      */   
/*      */   public void endVisit(OracleSelectQueryBlock x) {
/*  315 */     endVisit((SQLSelectQueryBlock)x);
/*      */   }
/*      */   
/*      */   public boolean visit(SQLPropertyExpr x) {
/*  319 */     if ("ROWNUM".equalsIgnoreCase(x.getName())) {
/*  320 */       return false;
/*      */     }
/*      */     
/*  323 */     return super.visit(x);
/*      */   }
/*      */   
/*      */   public boolean visit(SQLIdentifierExpr x) {
/*  327 */     if ("ROWNUM".equalsIgnoreCase(x.getName())) {
/*  328 */       return false;
/*      */     }
/*      */     
/*  331 */     if ("SYSDATE".equalsIgnoreCase(x.getName())) {
/*  332 */       return false;
/*      */     }
/*      */     
/*  335 */     if ("+".equalsIgnoreCase(x.getName())) {
/*  336 */       return false;
/*      */     }
/*      */     
/*  339 */     if ("LEVEL".equals(x.getName())) {
/*  340 */       return false;
/*      */     }
/*      */     
/*  343 */     return super.visit(x);
/*      */   }
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
/*      */   
/*      */   public void endVisit(OracleSelectUnPivot x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OraclePLSQLCommitStatement astNode) {
/*  424 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAnalytic x) {
/*  430 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAnalyticWindowing x) {
/*  436 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleDbLinkExpr x) {
/*  442 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleIntervalExpr x) {
/*  448 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleOuterExpr x) {
/*  454 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSelectForUpdate x) {
/*  460 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSelectHierachicalQueryClause x) {
/*  466 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSelectJoin x) {
/*  471 */     visit((SQLJoinTableSource)x);
/*      */     
/*  473 */     for (SQLExpr item : x.getUsing()) {
/*  474 */       if (item instanceof SQLIdentifierExpr) {
/*  475 */         String columnName = ((SQLIdentifierExpr)item).getName();
/*  476 */         String leftTable = (String)x.getLeft().getAttribute("_table_");
/*  477 */         String rightTable = (String)x.getRight().getAttribute("_table_");
/*  478 */         if (leftTable != null && rightTable != null) {
/*  479 */           TableStat.Relationship relationship = new TableStat.Relationship();
/*  480 */           relationship.setLeft(new TableStat.Column(leftTable, columnName));
/*  481 */           relationship.setRight(new TableStat.Column(rightTable, columnName));
/*  482 */           relationship.setOperator("USING");
/*  483 */           this.relationships.add(relationship);
/*      */         } 
/*      */         
/*  486 */         if (leftTable != null) {
/*  487 */           addColumn(leftTable, columnName);
/*      */         }
/*      */         
/*  490 */         if (rightTable != null) {
/*  491 */           addColumn(rightTable, columnName);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  496 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSelectPivot x) {
/*  502 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSelectPivot.Item x) {
/*  508 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSelectRestriction.CheckOption x) {
/*  514 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSelectRestriction.ReadOnly x) {
/*  520 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSelectSubqueryTableSource x) {
/*  525 */     accept((SQLObject)x.getSelect());
/*  526 */     accept((SQLObject)x.getPivot());
/*  527 */     accept((SQLObject)x.getFlashback());
/*      */     
/*  529 */     String table = (String)x.getSelect().getAttribute("_table_");
/*  530 */     if (x.getAlias() != null) {
/*  531 */       if (table != null) {
/*  532 */         this.aliasMap.put(x.getAlias(), table);
/*      */       }
/*  534 */       addSubQuery(x.getAlias(), (SQLObject)x.getSelect());
/*  535 */       setCurrentTable(x.getAlias());
/*      */     } 
/*      */     
/*  538 */     if (table != null) {
/*  539 */       x.putAttribute("_table_", table);
/*      */     }
/*  541 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSelectUnPivot x) {
/*  547 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(SampleClause x) {
/*  552 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(SampleClause x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleSelectTableReference x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(PartitionExtensionClause x) {
/*  568 */     return true;
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
/*  579 */     return true;
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
/*  590 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(FlashbackQueryClause.AsOfFlashbackQueryClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleWithSubqueryEntry x) {
/*  600 */     Map<String, String> aliasMap = getAliasMap();
/*  601 */     if (aliasMap != null) {
/*  602 */       String alias = null;
/*  603 */       if (x.getName() != null) {
/*  604 */         alias = x.getName().toString();
/*      */       }
/*      */       
/*  607 */       if (alias != null) {
/*  608 */         putAliasMap(aliasMap, alias, null);
/*  609 */         addSubQuery(alias, (SQLObject)x.getSubQuery());
/*      */       } 
/*      */     } 
/*  612 */     x.getSubQuery().accept(this);
/*  613 */     return false;
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
/*  624 */     return true;
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
/*  635 */     return true;
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
/*  646 */     return true;
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
/*  657 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleBinaryDoubleExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleCursorExpr x) {
/*  668 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleCursorExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleIsSetExpr x) {
/*  679 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleIsSetExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(ModelClause.ReturnRowsClause x) {
/*  690 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(ModelClause.ReturnRowsClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(ModelClause.MainModelClause x) {
/*  701 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(ModelClause.MainModelClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(ModelClause.ModelColumnClause x) {
/*  712 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(ModelClause.ModelColumnClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(ModelClause.QueryPartitionClause x) {
/*  723 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(ModelClause.QueryPartitionClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(ModelClause.ModelColumn x) {
/*  734 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(ModelClause.ModelColumn x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(ModelClause.ModelRulesClause x) {
/*  745 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(ModelClause.ModelRulesClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(ModelClause.CellAssignmentItem x) {
/*  756 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(ModelClause.CellAssignmentItem x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(ModelClause.CellAssignment x) {
/*  767 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(ModelClause.CellAssignment x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(ModelClause x) {
/*  777 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(ModelClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(SQLMergeStatement.MergeUpdateClause x) {
/*  787 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(SQLMergeStatement.MergeUpdateClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(SQLMergeStatement.MergeInsertClause x) {
/*  797 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(SQLMergeStatement.MergeInsertClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleReturningClause x) {
/*  807 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleReturningClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleInsertStatement x) {
/*  817 */     return visit((SQLInsertStatement)x);
/*      */   }
/*      */ 
/*      */   
/*      */   public void endVisit(OracleInsertStatement x) {
/*  822 */     endVisit((SQLInsertStatement)x);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleMultiInsertStatement.InsertIntoClause x) {
/*  828 */     if (x.getTableName() instanceof SQLName) {
/*  829 */       String ident = x.getTableName().toString();
/*  830 */       setCurrentTable((SQLObject)x, ident);
/*      */       
/*  832 */       TableStat stat = getTableStat(ident);
/*  833 */       stat.incrementInsertCount();
/*      */       
/*  835 */       Map<String, String> aliasMap = getAliasMap();
/*  836 */       if (aliasMap != null) {
/*  837 */         if (x.getAlias() != null) {
/*  838 */           putAliasMap(aliasMap, x.getAlias(), ident);
/*      */         }
/*  840 */         putAliasMap(aliasMap, ident, ident);
/*      */       } 
/*      */     } 
/*      */     
/*  844 */     accept(x.getColumns());
/*  845 */     accept((SQLObject)x.getQuery());
/*  846 */     accept((SQLObject)x.getReturning());
/*  847 */     accept((SQLObject)x.getErrorLogging());
/*      */     
/*  849 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleMultiInsertStatement.InsertIntoClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleMultiInsertStatement x) {
/*  859 */     x.putAttribute("_original_use_mode", getMode());
/*  860 */     setMode((SQLObject)x, TableStat.Mode.Insert);
/*      */     
/*  862 */     setAliasMap();
/*      */     
/*  864 */     accept((SQLObject)x.getSubQuery());
/*      */     
/*  866 */     for (OracleMultiInsertStatement.Entry entry : x.getEntries()) {
/*  867 */       entry.setParent((SQLObject)x);
/*      */     }
/*      */     
/*  870 */     accept(x.getEntries());
/*      */     
/*  872 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleMultiInsertStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleMultiInsertStatement.ConditionalInsertClause x) {
/*  882 */     for (OracleMultiInsertStatement.ConditionalInsertClauseItem item : x.getItems()) {
/*  883 */       item.setParent((SQLObject)x);
/*      */     }
/*  885 */     if (x.getElseItem() != null) {
/*  886 */       x.getElseItem().setParent((SQLObject)x);
/*      */     }
/*  888 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleMultiInsertStatement.ConditionalInsertClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleMultiInsertStatement.ConditionalInsertClauseItem x) {
/*  898 */     SQLObject parent = x.getParent();
/*  899 */     if (parent instanceof OracleMultiInsertStatement.ConditionalInsertClause) {
/*  900 */       parent = parent.getParent();
/*      */     }
/*  902 */     if (parent instanceof OracleMultiInsertStatement) {
/*  903 */       SQLSelect subQuery = ((OracleMultiInsertStatement)parent).getSubQuery();
/*  904 */       if (subQuery != null) {
/*  905 */         String table = (String)subQuery.getAttribute("_table_");
/*  906 */         setCurrentTable((SQLObject)x, table);
/*      */       } 
/*      */     } 
/*  909 */     x.getWhen().accept(this);
/*  910 */     x.getThen().accept(this);
/*  911 */     restoreCurrentTable((SQLObject)x);
/*  912 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleMultiInsertStatement.ConditionalInsertClauseItem x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterSessionStatement x) {
/*  922 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterSessionStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleExprStatement x) {
/*  932 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleExprStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleLockTableStatement x) {
/*  942 */     String tableName = x.getTable().toString();
/*  943 */     getTableStat(tableName);
/*  944 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleLockTableStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleDatetimeExpr x) {
/*  954 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleDatetimeExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSysdateExpr x) {
/*  964 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleSysdateExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleExceptionStatement.Item x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleExceptionStatement.Item x) {
/*  979 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(OracleExceptionStatement x) {
/*  984 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleExceptionStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleArgumentExpr x) {
/*  994 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleArgumentExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSetTransactionStatement x) {
/* 1004 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleSetTransactionStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleExplainStatement x) {
/* 1014 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleExplainStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterProcedureStatement x) {
/* 1024 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterProcedureStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTableDropPartition x) {
/* 1034 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTableDropPartition x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTableTruncatePartition x) {
/* 1044 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTableTruncatePartition x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(SQLAlterTableStatement x) {
/* 1054 */     restoreCurrentTable((SQLObject)x);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTableSplitPartition.TableSpaceItem x) {
/* 1059 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTableSplitPartition.TableSpaceItem x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTableSplitPartition.UpdateIndexesClause x) {
/* 1069 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTableSplitPartition.UpdateIndexesClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTableSplitPartition.NestedTablePartitionSpec x) {
/* 1079 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTableSplitPartition.NestedTablePartitionSpec x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTableSplitPartition x) {
/* 1089 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTableSplitPartition x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTableModify x) {
/* 1099 */     SQLAlterTableStatement stmt = (SQLAlterTableStatement)x.getParent();
/* 1100 */     String table = stmt.getName().toString();
/*      */     
/* 1102 */     for (SQLColumnDefinition column : x.getColumns()) {
/* 1103 */       String columnName = column.getName().toString();
/* 1104 */       addColumn(table, columnName);
/*      */     } 
/*      */ 
/*      */     
/* 1108 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTableModify x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleCreateIndexStatement x) {
/* 1118 */     return visit((SQLCreateIndexStatement)x);
/*      */   }
/*      */ 
/*      */   
/*      */   public void endVisit(OracleCreateIndexStatement x) {
/* 1123 */     restoreCurrentTable((SQLObject)x);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterIndexStatement x) {
/* 1128 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterIndexStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleForStatement x) {
/* 1138 */     x.getRange().setParent((SQLObject)x);
/*      */     
/* 1140 */     SQLName index = x.getIndex();
/* 1141 */     getVariants().put(index.toString(), x);
/*      */     
/* 1143 */     x.getRange().accept(this);
/* 1144 */     accept(x.getStatements());
/*      */     
/* 1146 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleForStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterIndexStatement.Rebuild x) {
/* 1156 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterIndexStatement.Rebuild x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleRangeExpr x) {
/* 1166 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleRangeExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OraclePrimaryKey x) {
/* 1176 */     accept(x.getColumns());
/*      */     
/* 1178 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OraclePrimaryKey x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleCreateTableStatement x) {
/* 1188 */     visit((SQLCreateTableStatement)x);
/*      */     
/* 1190 */     if (x.getSelect() != null) {
/* 1191 */       x.getSelect().accept(this);
/*      */     }
/*      */     
/* 1194 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void endVisit(OracleCreateTableStatement x) {
/* 1199 */     endVisit((SQLCreateTableStatement)x);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(OracleStorageClause x) {
/* 1204 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleStorageClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleGotoStatement x) {
/* 1214 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleGotoStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleLabelStatement x) {
/* 1224 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleLabelStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleCommitStatement x) {
/* 1234 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleCommitStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTriggerStatement x) {
/* 1244 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTriggerStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterSynonymStatement x) {
/* 1254 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterSynonymStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(FlashbackQueryClause.AsOfSnapshotClause x) {
/* 1264 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(FlashbackQueryClause.AsOfSnapshotClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterViewStatement x) {
/* 1274 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterViewStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTableMoveTablespace x) {
/* 1284 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTableMoveTablespace x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSizeExpr x) {
/* 1294 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleSizeExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleFileSpecification x) {
/* 1304 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleFileSpecification x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTablespaceAddDataFile x) {
/* 1314 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTablespaceAddDataFile x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleAlterTablespaceStatement x) {
/* 1324 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleAlterTablespaceStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleExitStatement x) {
/* 1334 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleExitStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleSavePointStatement x) {
/* 1344 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleSavePointStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleCreateDatabaseDbLinkStatement x) {
/* 1354 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleCreateDatabaseDbLinkStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleDropDbLinkStatement x) {
/* 1364 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleDropDbLinkStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleDataTypeTimestamp x) {
/* 1374 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleDataTypeTimestamp x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleDataTypeIntervalYear x) {
/* 1384 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleDataTypeIntervalYear x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleDataTypeIntervalDay x) {
/* 1394 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleDataTypeIntervalDay x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleUsingIndexClause x) {
/* 1404 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleUsingIndexClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleLobStorageClause x) {
/* 1414 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleLobStorageClause x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleUnique x) {
/* 1424 */     return visit((SQLUnique)x);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleUnique x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleForeignKey x) {
/* 1434 */     return visit((SQLForeignKeyImpl)x);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(OracleForeignKey x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(OracleCheck x) {
/* 1444 */     return visit((SQLCheck)x);
/*      */   }
/*      */   
/*      */   public void endVisit(OracleCheck x) {}
/*      */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\visitor\OracleSchemaStatVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */