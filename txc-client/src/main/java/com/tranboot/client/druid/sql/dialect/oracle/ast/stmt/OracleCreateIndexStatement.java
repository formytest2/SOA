/*     */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OracleCreateIndexStatement
/*     */   extends SQLCreateIndexStatement
/*     */   implements OracleDDLStatement
/*     */ {
/*     */   private boolean online = false;
/*     */   private boolean indexOnlyTopLevel = false;
/*     */   private boolean noParallel;
/*     */   private SQLExpr parallel;
/*     */   private SQLName tablespace;
/*     */   private SQLExpr ptcfree;
/*     */   private SQLExpr pctused;
/*     */   private SQLExpr initrans;
/*     */   private SQLExpr maxtrans;
/*  42 */   private Boolean enable = null;
/*     */   
/*     */   private boolean computeStatistics = false;
/*     */   
/*     */   public OracleCreateIndexStatement() {
/*  47 */     super("oracle");
/*     */   }
/*     */   
/*     */   public SQLName getTablespace() {
/*  51 */     return this.tablespace;
/*     */   }
/*     */   
/*     */   public void setTablespace(SQLName tablespace) {
/*  55 */     this.tablespace = tablespace;
/*     */   }
/*     */   
/*     */   public SQLExpr getParallel() {
/*  59 */     return this.parallel;
/*     */   }
/*     */   
/*     */   public void setParallel(SQLExpr parallel) {
/*  63 */     this.parallel = parallel;
/*     */   }
/*     */   
/*     */   public boolean isNoParallel() {
/*  67 */     return this.noParallel;
/*     */   }
/*     */   
/*     */   public void setNoParallel(boolean noParallel) {
/*  71 */     this.noParallel = noParallel;
/*     */   }
/*     */   
/*     */   public boolean isIndexOnlyTopLevel() {
/*  75 */     return this.indexOnlyTopLevel;
/*     */   }
/*     */   
/*     */   public void setIndexOnlyTopLevel(boolean indexOnlyTopLevel) {
/*  79 */     this.indexOnlyTopLevel = indexOnlyTopLevel;
/*     */   }
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  83 */     accept0((OracleASTVisitor)visitor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept0(OracleASTVisitor visitor) {
/*  88 */     if (visitor.visit(this)) {
/*  89 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getName());
/*  90 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getTable());
/*  91 */       acceptChild((SQLASTVisitor)visitor, getItems());
/*  92 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getTablespace());
/*  93 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.parallel);
/*     */     } 
/*  95 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public SQLExpr getPtcfree() {
/*  99 */     return this.ptcfree;
/*     */   }
/*     */   
/*     */   public void setPtcfree(SQLExpr ptcfree) {
/* 103 */     this.ptcfree = ptcfree;
/*     */   }
/*     */   
/*     */   public SQLExpr getPctused() {
/* 107 */     return this.pctused;
/*     */   }
/*     */   
/*     */   public void setPctused(SQLExpr pctused) {
/* 111 */     this.pctused = pctused;
/*     */   }
/*     */   
/*     */   public SQLExpr getInitrans() {
/* 115 */     return this.initrans;
/*     */   }
/*     */   
/*     */   public void setInitrans(SQLExpr initrans) {
/* 119 */     this.initrans = initrans;
/*     */   }
/*     */   
/*     */   public SQLExpr getMaxtrans() {
/* 123 */     return this.maxtrans;
/*     */   }
/*     */   
/*     */   public void setMaxtrans(SQLExpr maxtrans) {
/* 127 */     this.maxtrans = maxtrans;
/*     */   }
/*     */   
/*     */   public Boolean getEnable() {
/* 131 */     return this.enable;
/*     */   }
/*     */   
/*     */   public void setEnable(Boolean enable) {
/* 135 */     this.enable = enable;
/*     */   }
/*     */   
/*     */   public boolean isComputeStatistics() {
/* 139 */     return this.computeStatistics;
/*     */   }
/*     */   
/*     */   public void setComputeStatistics(boolean computeStatistics) {
/* 143 */     this.computeStatistics = computeStatistics;
/*     */   }
/*     */   
/*     */   public boolean isOnline() {
/* 147 */     return this.online;
/*     */   }
/*     */   
/*     */   public void setOnline(boolean online) {
/* 151 */     this.online = online;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleCreateIndexStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */