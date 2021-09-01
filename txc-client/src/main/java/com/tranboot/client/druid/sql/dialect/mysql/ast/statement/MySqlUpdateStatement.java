/*     */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLLimit;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLOrderBy;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateStatement;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

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
/*     */ public class MySqlUpdateStatement
/*     */   extends SQLUpdateStatement
/*     */   implements MySqlStatement
/*     */ {
/*     */   private SQLOrderBy orderBy;
/*     */   private SQLLimit limit;
/*     */   private boolean lowPriority = false;
/*     */   private boolean ignore = false;
/*     */   private boolean commitOnSuccess = false;
/*     */   private boolean rollBackOnFail = false;
/*     */   private boolean queryOnPk = false;
/*     */   private SQLExpr targetAffectRow;
/*     */   
/*     */   public MySqlUpdateStatement() {
/*  39 */     super("mysql");
/*     */   }
/*     */   
/*     */   public SQLLimit getLimit() {
/*  43 */     return this.limit;
/*     */   }
/*     */   
/*     */   public void setLimit(SQLLimit limit) {
/*  47 */     if (limit != null) {
/*  48 */       limit.setParent((SQLObject)this);
/*     */     }
/*  50 */     this.limit = limit;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  55 */     if (visitor instanceof MySqlASTVisitor) {
/*  56 */       accept0((MySqlASTVisitor)visitor);
/*     */     } else {
/*  58 */       throw new IllegalArgumentException("not support visitor type : " + visitor.getClass().getName());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void accept0(MySqlASTVisitor visitor) {
/*  63 */     if (visitor.visit(this)) {
/*  64 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.tableSource);
/*  65 */       acceptChild((SQLASTVisitor)visitor, this.items);
/*  66 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.where);
/*  67 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.orderBy);
/*  68 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.limit);
/*     */     } 
/*  70 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public boolean isLowPriority() {
/*  74 */     return this.lowPriority;
/*     */   }
/*     */   
/*     */   public void setLowPriority(boolean lowPriority) {
/*  78 */     this.lowPriority = lowPriority;
/*     */   }
/*     */   
/*     */   public boolean isIgnore() {
/*  82 */     return this.ignore;
/*     */   }
/*     */   
/*     */   public void setIgnore(boolean ignore) {
/*  86 */     this.ignore = ignore;
/*     */   }
/*     */   
/*     */   public boolean isCommitOnSuccess() {
/*  90 */     return this.commitOnSuccess;
/*     */   }
/*     */   
/*     */   public void setCommitOnSuccess(boolean commitOnSuccess) {
/*  94 */     this.commitOnSuccess = commitOnSuccess;
/*     */   }
/*     */   
/*     */   public boolean isRollBackOnFail() {
/*  98 */     return this.rollBackOnFail;
/*     */   }
/*     */   
/*     */   public void setRollBackOnFail(boolean rollBackOnFail) {
/* 102 */     this.rollBackOnFail = rollBackOnFail;
/*     */   }
/*     */   
/*     */   public boolean isQueryOnPk() {
/* 106 */     return this.queryOnPk;
/*     */   }
/*     */   
/*     */   public void setQueryOnPk(boolean queryOnPk) {
/* 110 */     this.queryOnPk = queryOnPk;
/*     */   }
/*     */   
/*     */   public SQLExpr getTargetAffectRow() {
/* 114 */     return this.targetAffectRow;
/*     */   }
/*     */   
/*     */   public void setTargetAffectRow(SQLExpr targetAffectRow) {
/* 118 */     if (targetAffectRow != null) {
/* 119 */       targetAffectRow.setParent((SQLObject)this);
/*     */     }
/* 121 */     this.targetAffectRow = targetAffectRow;
/*     */   }
/*     */   
/*     */   public SQLOrderBy getOrderBy() {
/* 125 */     return this.orderBy;
/*     */   }
/*     */   
/*     */   public void setOrderBy(SQLOrderBy orderBy) {
/* 129 */     this.orderBy = orderBy;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlUpdateStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */