/*     */ package com.tranboot.client.druid.sql.dialect.oracle.ast.clause;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLOrderBy;
import com.tranboot.client.druid.sql.ast.statement.SQLSelect;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

import java.util.ArrayList;
import java.util.List;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */

/*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelClause
/*     */   extends OracleSQLObjectImpl
/*     */ {
/*  29 */   private final List<CellReferenceOption> cellReferenceOptions = new ArrayList<>();
/*     */   private ReturnRowsClause returnRowsClause;
/*  31 */   private final List<ReferenceModelClause> referenceModelClauses = new ArrayList<>();
/*     */   
/*     */   private MainModelClause mainModel;
/*     */   
/*     */   public void accept0(OracleASTVisitor visitor) {
/*  36 */     if (visitor.visit(this)) {
/*  37 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.returnRowsClause);
/*  38 */       acceptChild((SQLASTVisitor)visitor, this.referenceModelClauses);
/*  39 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.mainModel);
/*     */     } 
/*  41 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public MainModelClause getMainModel() {
/*  45 */     return this.mainModel;
/*     */   }
/*     */   
/*     */   public void setMainModel(MainModelClause mainModel) {
/*  49 */     this.mainModel = mainModel;
/*     */   }
/*     */   
/*     */   public ReturnRowsClause getReturnRowsClause() {
/*  53 */     return this.returnRowsClause;
/*     */   }
/*     */   
/*     */   public void setReturnRowsClause(ReturnRowsClause returnRowsClause) {
/*  57 */     this.returnRowsClause = returnRowsClause;
/*     */   }
/*     */   
/*     */   public List<ReferenceModelClause> getReferenceModelClauses() {
/*  61 */     return this.referenceModelClauses;
/*     */   }
/*     */   
/*     */   public List<CellReferenceOption> getCellReferenceOptions() {
/*  65 */     return this.cellReferenceOptions;
/*     */   }
/*     */   
/*     */   public enum CellReferenceOption {
/*  69 */     IgnoreNav("IGNORE NAV"), KeepNav("KEEP NAV"), UniqueDimension("UNIQUE DIMENSION"),
/*  70 */     UniqueSingleReference("UNIQUE SINGLE REFERENCE");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     CellReferenceOption(String name) {
/*  81 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String toString() {
/*  85 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ReturnRowsClause
/*     */     extends OracleSQLObjectImpl
/*     */   {
/*     */     private boolean all = false;
/*     */     
/*     */     public boolean isAll() {
/*  95 */       return this.all;
/*     */     }
/*     */     
/*     */     public void setAll(boolean all) {
/*  99 */       this.all = all;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept0(OracleASTVisitor visitor) {
/* 104 */       visitor.visit(this);
/* 105 */       visitor.endVisit(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ReferenceModelClause
/*     */     extends OracleSQLObjectImpl {
/*     */     private SQLExpr name;
/*     */     private SQLSelect subQuery;
/* 113 */     private final List<CellReferenceOption> cellReferenceOptions = new ArrayList<>();
/*     */
/*     */     public List<CellReferenceOption> getCellReferenceOptions() {
/* 116 */       return this.cellReferenceOptions;
/*     */     }
/*     */
/*     */     public SQLExpr getName() {
/* 120 */       return this.name;
/*     */     }
/*     */
/*     */     public void setName(SQLExpr name) {
/* 124 */       this.name = name;
/*     */     }
/*     */
/*     */     public SQLSelect getSubQuery() {
/* 128 */       return this.subQuery;
/*     */     }
/*     */
/*     */     public void setSubQuery(SQLSelect subQuery) {
/* 132 */       this.subQuery = subQuery;
/*     */     }
/*     */
/*     */
/*     */     public void accept0(OracleASTVisitor visitor) {}
/*     */   }
/*     */
/*     */
/*     */   public static class ModelColumnClause
/*     */     extends OracleSQLObjectImpl
/*     */   {
/*     */     private ModelClause.QueryPartitionClause queryPartitionClause;
/*     */     private String alias;
/* 145 */     private final List<ModelColumn> dimensionByColumns = new ArrayList<>();
/* 146 */     private final List<ModelColumn> measuresColumns = new ArrayList<>();
/*     */
/*     */     public List<ModelColumn> getDimensionByColumns() {
/* 149 */       return this.dimensionByColumns;
/*     */     }
/*     */
/*     */     public List<ModelColumn> getMeasuresColumns() {
/* 153 */       return this.measuresColumns;
/*     */     }
/*     */
/*     */     public ModelClause.QueryPartitionClause getQueryPartitionClause() {
/* 157 */       return this.queryPartitionClause;
/*     */     }
/*     */
/*     */     public void setQueryPartitionClause(ModelClause.QueryPartitionClause queryPartitionClause) {
/* 161 */       this.queryPartitionClause = queryPartitionClause;
/*     */     }
/*     */
/*     */     public String getAlias() {
/* 165 */       return this.alias;
/*     */     }
/*     */
/*     */     public void setAlias(String alias) {
/* 169 */       this.alias = alias;
/*     */     }
/*     */
/*     */
/*     */     public void accept0(OracleASTVisitor visitor) {
/* 174 */       if (visitor.visit(this)) {
/* 175 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.queryPartitionClause);
/* 176 */         acceptChild((SQLASTVisitor)visitor, this.dimensionByColumns);
/* 177 */         acceptChild((SQLASTVisitor)visitor, this.measuresColumns);
/*     */       }
/* 179 */       visitor.endVisit(this);
/*     */     }
/*     */   }
/*     */
/*     */   public static class ModelColumn
/*     */     extends OracleSQLObjectImpl
/*     */   {
/*     */     private SQLExpr expr;
/*     */     private String alias;
/*     */
/*     */     public SQLExpr getExpr() {
/* 190 */       return this.expr;
/*     */     }
/*     */
/*     */     public void setExpr(SQLExpr expr) {
/* 194 */       this.expr = expr;
/*     */     }
/*     */
/*     */     public String getAlias() {
/* 198 */       return this.alias;
/*     */     }
/*     */
/*     */     public void setAlias(String alias) {
/* 202 */       this.alias = alias;
/*     */     }
/*     */
/*     */
/*     */     public void accept0(OracleASTVisitor visitor) {
/* 207 */       if (visitor.visit(this)) {
/* 208 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.expr);
/*     */       }
/* 210 */       visitor.endVisit(this);
/*     */     }
/*     */   }
/*     */
/*     */   public static class QueryPartitionClause
/*     */     extends OracleSQLObjectImpl
/*     */   {
/* 217 */     private List<SQLExpr> exprList = new ArrayList<>();
/*     */
/*     */     public List<SQLExpr> getExprList() {
/* 220 */       return this.exprList;
/*     */     }
/*     */
/*     */     public void setExprList(List<SQLExpr> exprList) {
/* 224 */       this.exprList = exprList;
/*     */     }
/*     */
/*     */
/*     */     public void accept0(OracleASTVisitor visitor) {
/* 229 */       if (visitor.visit(this)) {
/* 230 */         acceptChild((SQLASTVisitor)visitor, this.exprList);
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public static class MainModelClause
/*     */     extends OracleSQLObjectImpl
/*     */   {
/*     */     private SQLExpr mainModelName;
/*     */     private ModelClause.ModelColumnClause modelColumnClause;
/* 241 */     private final List<CellReferenceOption> cellReferenceOptions = new ArrayList<>();
/*     */     private ModelClause.ModelRulesClause modelRulesClause;
/*     */
/*     */     public ModelClause.ModelRulesClause getModelRulesClause() {
/* 245 */       return this.modelRulesClause;
/*     */     }
/*     */
/*     */     public void setModelRulesClause(ModelClause.ModelRulesClause modelRulesClause) {
/* 249 */       this.modelRulesClause = modelRulesClause;
/*     */     }
/*     */
/*     */     public List<CellReferenceOption> getCellReferenceOptions() {
/* 253 */       return this.cellReferenceOptions;
/*     */     }
/*     */
/*     */     public ModelClause.ModelColumnClause getModelColumnClause() {
/* 257 */       return this.modelColumnClause;
/*     */     }
/*     */
/*     */     public void setModelColumnClause(ModelClause.ModelColumnClause modelColumnClause) {
/* 261 */       this.modelColumnClause = modelColumnClause;
/*     */     }
/*     */
/*     */     public SQLExpr getMainModelName() {
/* 265 */       return this.mainModelName;
/*     */     }
/*     */
/*     */     public void setMainModelName(SQLExpr mainModelName) {
/* 269 */       this.mainModelName = mainModelName;
/*     */     }
/*     */
/*     */
/*     */     public void accept0(OracleASTVisitor visitor) {
/* 274 */       if (visitor.visit(this)) {
/* 275 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.mainModelName);
/* 276 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.modelColumnClause);
/* 277 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.modelRulesClause);
/*     */       }
/* 279 */       visitor.endVisit(this);
/*     */     }
/*     */   }
/*     */
/*     */   public static class ModelRulesClause
/*     */     extends OracleSQLObjectImpl
/*     */   {
/* 286 */     private final List<ModelRuleOption> options = new ArrayList<>();
/*     */     private SQLExpr iterate;
/*     */     private SQLExpr until;
/* 289 */     private final List<CellAssignmentItem> cellAssignmentItems = new ArrayList<>();
/*     */
/*     */     public SQLExpr getUntil() {
/* 292 */       return this.until;
/*     */     }
/*     */
/*     */     public void setUntil(SQLExpr until) {
/* 296 */       this.until = until;
/*     */     }
/*     */
/*     */     public SQLExpr getIterate() {
/* 300 */       return this.iterate;
/*     */     }
/*     */
/*     */     public void setIterate(SQLExpr iterate) {
/* 304 */       this.iterate = iterate;
/*     */     }
/*     */
/*     */     public List<ModelRuleOption> getOptions() {
/* 308 */       return this.options;
/*     */     }
/*     */
/*     */     public List<CellAssignmentItem> getCellAssignmentItems() {
/* 312 */       return this.cellAssignmentItems;
/*     */     }
/*     */
/*     */
/*     */     public void accept0(OracleASTVisitor visitor) {
/* 317 */       if (visitor.visit(this)) {
/* 318 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.iterate);
/* 319 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.until);
/* 320 */         acceptChild((SQLASTVisitor)visitor, this.cellAssignmentItems);
/*     */       }
/* 322 */       visitor.endVisit(this);
/*     */     }
/*     */   }
/*     */
/*     */   public enum ModelRuleOption
/*     */   {
/* 328 */     UPSERT("UPSERT"), UPDATE("UPDATE"), AUTOMATIC_ORDER("AUTOMATIC ORDER"), SEQUENTIAL_ORDER("SEQUENTIAL ORDER");
/*     */
/*     */
/*     */     public final String name;
/*     */
/*     */
/*     */     ModelRuleOption(String name) {
/* 335 */       this.name = name;
/*     */     }
/*     */
/*     */     public String toString() {
/* 339 */       return this.name;
/*     */     }
/*     */   }
/*     */
/*     */   public static class CellAssignmentItem
/*     */     extends OracleSQLObjectImpl {
/*     */     private ModelClause.ModelRuleOption option;
/*     */     private ModelClause.CellAssignment cellAssignment;
/*     */     private SQLOrderBy orderBy;
/*     */     private SQLExpr expr;
/*     */
/*     */     public ModelClause.ModelRuleOption getOption() {
/* 351 */       return this.option;
/*     */     }
/*     */
/*     */     public void setOption(ModelClause.ModelRuleOption option) {
/* 355 */       this.option = option;
/*     */     }
/*     */
/*     */     public ModelClause.CellAssignment getCellAssignment() {
/* 359 */       return this.cellAssignment;
/*     */     }
/*     */
/*     */     public void setCellAssignment(ModelClause.CellAssignment cellAssignment) {
/* 363 */       this.cellAssignment = cellAssignment;
/*     */     }
/*     */     
/*     */     public SQLOrderBy getOrderBy() {
/* 367 */       return this.orderBy;
/*     */     }
/*     */     
/*     */     public void setOrderBy(SQLOrderBy orderBy) {
/* 371 */       this.orderBy = orderBy;
/*     */     }
/*     */     
/*     */     public SQLExpr getExpr() {
/* 375 */       return this.expr;
/*     */     }
/*     */     
/*     */     public void setExpr(SQLExpr expr) {
/* 379 */       this.expr = expr;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept0(OracleASTVisitor visitor) {
/* 384 */       if (visitor.visit(this)) {
/* 385 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.cellAssignment);
/* 386 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.orderBy);
/* 387 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.expr);
/*     */       } 
/* 389 */       visitor.endVisit(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CellAssignment
/*     */     extends OracleSQLObjectImpl
/*     */   {
/*     */     private SQLExpr measureColumn;
/* 397 */     private final List<SQLExpr> conditions = new ArrayList<>();
/*     */     
/*     */     public List<SQLExpr> getConditions() {
/* 400 */       return this.conditions;
/*     */     }
/*     */     
/*     */     public SQLExpr getMeasureColumn() {
/* 404 */       return this.measureColumn;
/*     */     }
/*     */     
/*     */     public void setMeasureColumn(SQLExpr measureColumn) {
/* 408 */       this.measureColumn = measureColumn;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept0(OracleASTVisitor visitor) {
/* 413 */       if (visitor.visit(this)) {
/* 414 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.measureColumn);
/* 415 */         acceptChild((SQLASTVisitor)visitor, this.conditions);
/*     */       } 
/* 417 */       visitor.endVisit(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\clause\ModelClause.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */