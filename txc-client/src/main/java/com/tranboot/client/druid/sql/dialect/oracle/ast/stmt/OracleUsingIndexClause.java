/*     */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.OracleStorageClause;
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
/*     */ public class OracleUsingIndexClause
/*     */   extends OracleSQLObjectImpl
/*     */ {
/*     */   private SQLName index;
/*     */   private SQLName tablespace;
/*     */   private SQLExpr ptcfree;
/*     */   private SQLExpr pctused;
/*     */   private SQLExpr initrans;
/*     */   private SQLExpr maxtrans;
/*  34 */   private Boolean enable = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean computeStatistics = false;
/*     */ 
/*     */   
/*     */   private OracleStorageClause storage;
/*     */ 
/*     */ 
/*     */   
/*     */   public void accept0(OracleASTVisitor visitor) {
/*  46 */     if (visitor.visit(this)) {
/*  47 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.index);
/*  48 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.tablespace);
/*  49 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.storage);
/*     */     } 
/*  51 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public OracleStorageClause getStorage() {
/*  55 */     return this.storage;
/*     */   }
/*     */   
/*     */   public void setStorage(OracleStorageClause storage) {
/*  59 */     this.storage = storage;
/*     */   }
/*     */   
/*     */   public Boolean getEnable() {
/*  63 */     return this.enable;
/*     */   }
/*     */   
/*     */   public void setEnable(Boolean enable) {
/*  67 */     this.enable = enable;
/*     */   }
/*     */   
/*     */   public boolean isComputeStatistics() {
/*  71 */     return this.computeStatistics;
/*     */   }
/*     */   
/*     */   public void setComputeStatistics(boolean computeStatistics) {
/*  75 */     this.computeStatistics = computeStatistics;
/*     */   }
/*     */   
/*     */   public SQLName getIndex() {
/*  79 */     return this.index;
/*     */   }
/*     */   
/*     */   public void setIndex(SQLName index) {
/*  83 */     this.index = index;
/*     */   }
/*     */   
/*     */   public SQLName getTablespace() {
/*  87 */     return this.tablespace;
/*     */   }
/*     */   
/*     */   public void setTablespace(SQLName tablespace) {
/*  91 */     this.tablespace = tablespace;
/*     */   }
/*     */   
/*     */   public SQLExpr getPtcfree() {
/*  95 */     return this.ptcfree;
/*     */   }
/*     */   
/*     */   public void setPtcfree(SQLExpr ptcfree) {
/*  99 */     this.ptcfree = ptcfree;
/*     */   }
/*     */   
/*     */   public SQLExpr getPctused() {
/* 103 */     return this.pctused;
/*     */   }
/*     */   
/*     */   public void setPctused(SQLExpr pctused) {
/* 107 */     this.pctused = pctused;
/*     */   }
/*     */   
/*     */   public SQLExpr getInitrans() {
/* 111 */     return this.initrans;
/*     */   }
/*     */   
/*     */   public void setInitrans(SQLExpr initrans) {
/* 115 */     this.initrans = initrans;
/*     */   }
/*     */   
/*     */   public SQLExpr getMaxtrans() {
/* 119 */     return this.maxtrans;
/*     */   }
/*     */   
/*     */   public void setMaxtrans(SQLExpr maxtrans) {
/* 123 */     this.maxtrans = maxtrans;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleUsingIndexClause.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */