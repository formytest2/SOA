/*     */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLCommentHint;
import com.tranboot.client.druid.sql.ast.SQLLimit;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLOrderBy;
import com.tranboot.client.druid.sql.ast.statement.SQLDeleteStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLTableSource;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MySqlDeleteStatement
/*     */   extends SQLDeleteStatement
/*     */ {
/*     */   private boolean lowPriority = false;
/*     */   private boolean quick = false;
/*     */   private boolean ignore = false;
/*     */   private SQLTableSource using;
/*     */   private SQLOrderBy orderBy;
/*     */   private SQLLimit limit;
/*     */   private List<SQLCommentHint> hints;
/*     */   
/*     */   public MySqlDeleteStatement() {
/*  44 */     super("mysql");
/*     */   }
/*     */   
/*     */   public List<SQLCommentHint> getHints() {
/*  48 */     if (this.hints == null) {
/*  49 */       this.hints = new ArrayList<>();
/*     */     }
/*  51 */     return this.hints;
/*     */   }
/*     */   
/*     */   public int getHintsSize() {
/*  55 */     if (this.hints == null) {
/*  56 */       return 0;
/*     */     }
/*     */     
/*  59 */     return this.hints.size();
/*     */   }
/*     */   
/*     */   public boolean isLowPriority() {
/*  63 */     return this.lowPriority;
/*     */   }
/*     */   
/*     */   public void setLowPriority(boolean lowPriority) {
/*  67 */     this.lowPriority = lowPriority;
/*     */   }
/*     */   
/*     */   public boolean isQuick() {
/*  71 */     return this.quick;
/*     */   }
/*     */   
/*     */   public void setQuick(boolean quick) {
/*  75 */     this.quick = quick;
/*     */   }
/*     */   
/*     */   public boolean isIgnore() {
/*  79 */     return this.ignore;
/*     */   }
/*     */   
/*     */   public void setIgnore(boolean ignore) {
/*  83 */     this.ignore = ignore;
/*     */   }
/*     */   
/*     */   public SQLTableSource getUsing() {
/*  87 */     return this.using;
/*     */   }
/*     */   
/*     */   public void setUsing(SQLTableSource using) {
/*  91 */     this.using = using;
/*     */   }
/*     */   
/*     */   public SQLOrderBy getOrderBy() {
/*  95 */     return this.orderBy;
/*     */   }
/*     */   
/*     */   public void setOrderBy(SQLOrderBy orderBy) {
/*  99 */     this.orderBy = orderBy;
/*     */   }
/*     */   
/*     */   public SQLLimit getLimit() {
/* 103 */     return this.limit;
/*     */   }
/*     */   
/*     */   public void setLimit(SQLLimit limit) {
/* 107 */     if (limit != null) {
/* 108 */       limit.setParent((SQLObject)this);
/*     */     }
/* 110 */     this.limit = limit;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/* 115 */     if (visitor instanceof MySqlASTVisitor) {
/* 116 */       accept0((MySqlASTVisitor)visitor);
/*     */     } else {
/* 118 */       throw new IllegalArgumentException("not support visitor type : " + visitor.getClass().getName());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void output(StringBuffer buf) {
/* 123 */     (new MySqlOutputVisitor(buf)).visit(this);
/*     */   }
/*     */   
/*     */   protected void accept0(MySqlASTVisitor visitor) {
/* 127 */     if (visitor.visit(this)) {
/* 128 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getTableSource());
/* 129 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getWhere());
/* 130 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getFrom());
/* 131 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getUsing());
/* 132 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.orderBy);
/* 133 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.limit);
/*     */     } 
/*     */     
/* 136 */     visitor.endVisit(this);
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlDeleteStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */