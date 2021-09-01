/*     */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLInsertStatement;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;
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
/*     */ 
/*     */ public class MySqlInsertStatement
/*     */   extends SQLInsertStatement
/*     */ {
/*     */   private boolean lowPriority = false;
/*     */   private boolean delayed = false;
/*     */   private boolean highPriority = false;
/*     */   private boolean ignore = false;
/*     */   private boolean rollbackOnFail = false;
/*  35 */   private final List<SQLExpr> duplicateKeyUpdate = new ArrayList<>();
/*     */   
/*     */   public List<SQLExpr> getDuplicateKeyUpdate() {
/*  38 */     return this.duplicateKeyUpdate;
/*     */   }
/*     */   
/*     */   public boolean isLowPriority() {
/*  42 */     return this.lowPriority;
/*     */   }
/*     */   
/*     */   public void setLowPriority(boolean lowPriority) {
/*  46 */     this.lowPriority = lowPriority;
/*     */   }
/*     */   
/*     */   public boolean isDelayed() {
/*  50 */     return this.delayed;
/*     */   }
/*     */   
/*     */   public void setDelayed(boolean delayed) {
/*  54 */     this.delayed = delayed;
/*     */   }
/*     */   
/*     */   public boolean isHighPriority() {
/*  58 */     return this.highPriority;
/*     */   }
/*     */   
/*     */   public void setHighPriority(boolean highPriority) {
/*  62 */     this.highPriority = highPriority;
/*     */   }
/*     */   
/*     */   public boolean isIgnore() {
/*  66 */     return this.ignore;
/*     */   }
/*     */   
/*     */   public void setIgnore(boolean ignore) {
/*  70 */     this.ignore = ignore;
/*     */   }
/*     */   
/*     */   public boolean isRollbackOnFail() {
/*  74 */     return this.rollbackOnFail;
/*     */   }
/*     */   
/*     */   public void setRollbackOnFail(boolean rollbackOnFail) {
/*  78 */     this.rollbackOnFail = rollbackOnFail;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  83 */     if (visitor instanceof MySqlASTVisitor) {
/*  84 */       accept0((MySqlASTVisitor)visitor);
/*     */     } else {
/*  86 */       throw new IllegalArgumentException("not support visitor type : " + visitor.getClass().getName());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void output(StringBuffer buf) {
/*  91 */     (new MySqlOutputVisitor(buf)).visit(this);
/*     */   }
/*     */   
/*     */   protected void accept0(MySqlASTVisitor visitor) {
/*  95 */     if (visitor.visit(this)) {
/*  96 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getTableSource());
/*  97 */       acceptChild((SQLASTVisitor)visitor, getColumns());
/*  98 */       acceptChild((SQLASTVisitor)visitor, getValuesList());
/*  99 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getQuery());
/* 100 */       acceptChild((SQLASTVisitor)visitor, getDuplicateKeyUpdate());
/*     */     } 
/*     */     
/* 103 */     visitor.endVisit(this);
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlInsertStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */