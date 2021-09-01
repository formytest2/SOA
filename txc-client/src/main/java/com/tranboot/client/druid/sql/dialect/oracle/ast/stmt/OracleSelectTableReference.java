/*     */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.SQLUtils;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLExprTableSource;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.FlashbackQueryClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.PartitionExtensionClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.SampleClause;
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
/*     */ 
/*     */ public class OracleSelectTableReference
/*     */   extends SQLExprTableSource
/*     */   implements OracleSelectTableSource
/*     */ {
/*     */   private boolean only = false;
/*     */   protected OracleSelectPivotBase pivot;
/*     */   protected PartitionExtensionClause partition;
/*     */   protected SampleClause sampleClause;
/*     */   protected FlashbackQueryClause flashback;
/*     */   
/*     */   public FlashbackQueryClause getFlashback() {
/*  41 */     return this.flashback;
/*     */   }
/*     */   
/*     */   public void setFlashback(FlashbackQueryClause flashback) {
/*  45 */     this.flashback = flashback;
/*     */   }
/*     */   
/*     */   public PartitionExtensionClause getPartition() {
/*  49 */     return this.partition;
/*     */   }
/*     */   
/*     */   public void setPartition(PartitionExtensionClause partition) {
/*  53 */     this.partition = partition;
/*     */   }
/*     */   
/*     */   public boolean isOnly() {
/*  57 */     return this.only;
/*     */   }
/*     */   
/*     */   public void setOnly(boolean only) {
/*  61 */     this.only = only;
/*     */   }
/*     */   
/*     */   public SampleClause getSampleClause() {
/*  65 */     return this.sampleClause;
/*     */   }
/*     */   
/*     */   public void setSampleClause(SampleClause sampleClause) {
/*  69 */     this.sampleClause = sampleClause;
/*     */   }
/*     */   
/*     */   public OracleSelectPivotBase getPivot() {
/*  73 */     return this.pivot;
/*     */   }
/*     */   
/*     */   public void setPivot(OracleSelectPivotBase pivot) {
/*  77 */     this.pivot = pivot;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  82 */     accept0((OracleASTVisitor)visitor);
/*     */   }
/*     */   
/*     */   protected void accept0(OracleASTVisitor visitor) {
/*  86 */     if (visitor.visit(this)) {
/*  87 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.expr);
/*  88 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.partition);
/*  89 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.sampleClause);
/*  90 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.pivot);
/*     */     } 
/*  92 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public void output(StringBuffer buf) {
/*  96 */     if (this.only) {
/*  97 */       buf.append("ONLY (");
/*  98 */       this.expr.output(buf);
/*  99 */       buf.append(")");
/*     */     } else {
/* 101 */       this.expr.output(buf);
/*     */     } 
/*     */     
/* 104 */     if (this.pivot != null) {
/* 105 */       buf.append(" ");
/* 106 */       this.pivot.output(buf);
/*     */     } 
/*     */     
/* 109 */     if (this.alias != null && this.alias.length() != 0) {
/* 110 */       buf.append(this.alias);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/* 115 */     return SQLUtils.toOracleString((SQLObject)this);
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleSelectTableReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */