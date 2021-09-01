/*     */ package com.tranboot.client.druid.sql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatementImpl;
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
/*     */ public class SQLCreateSequenceStatement
/*     */   extends SQLStatementImpl
/*     */ {
/*     */   private SQLName name;
/*     */   private SQLExpr startWith;
/*     */   private SQLExpr incrementBy;
/*     */   private SQLExpr minValue;
/*     */   private SQLExpr maxValue;
/*     */   private boolean noMaxValue;
/*     */   private boolean noMinValue;
/*     */   private Boolean cycle;
/*     */   private Boolean cache;
/*     */   private Boolean order;
/*     */   
/*     */   public void accept0(SQLASTVisitor visitor) {
/*  42 */     if (visitor.visit(this)) {
/*  43 */       acceptChild(visitor, (SQLObject)this.name);
/*  44 */       acceptChild(visitor, (SQLObject)this.startWith);
/*  45 */       acceptChild(visitor, (SQLObject)this.incrementBy);
/*  46 */       acceptChild(visitor, (SQLObject)this.minValue);
/*  47 */       acceptChild(visitor, (SQLObject)this.maxValue);
/*     */     } 
/*  49 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public SQLName getName() {
/*  53 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(SQLName name) {
/*  57 */     this.name = name;
/*     */   }
/*     */   
/*     */   public SQLExpr getStartWith() {
/*  61 */     return this.startWith;
/*     */   }
/*     */   
/*     */   public void setStartWith(SQLExpr startWith) {
/*  65 */     this.startWith = startWith;
/*     */   }
/*     */   
/*     */   public SQLExpr getIncrementBy() {
/*  69 */     return this.incrementBy;
/*     */   }
/*     */   
/*     */   public void setIncrementBy(SQLExpr incrementBy) {
/*  73 */     this.incrementBy = incrementBy;
/*     */   }
/*     */   
/*     */   public SQLExpr getMaxValue() {
/*  77 */     return this.maxValue;
/*     */   }
/*     */   
/*     */   public void setMaxValue(SQLExpr maxValue) {
/*  81 */     this.maxValue = maxValue;
/*     */   }
/*     */   
/*     */   public Boolean getCycle() {
/*  85 */     return this.cycle;
/*     */   }
/*     */   
/*     */   public void setCycle(Boolean cycle) {
/*  89 */     this.cycle = cycle;
/*     */   }
/*     */   
/*     */   public Boolean getCache() {
/*  93 */     return this.cache;
/*     */   }
/*     */   
/*     */   public void setCache(Boolean cache) {
/*  97 */     this.cache = cache;
/*     */   }
/*     */   
/*     */   public Boolean getOrder() {
/* 101 */     return this.order;
/*     */   }
/*     */   
/*     */   public void setOrder(Boolean order) {
/* 105 */     this.order = order;
/*     */   }
/*     */   
/*     */   public SQLExpr getMinValue() {
/* 109 */     return this.minValue;
/*     */   }
/*     */   
/*     */   public void setMinValue(SQLExpr minValue) {
/* 113 */     this.minValue = minValue;
/*     */   }
/*     */   
/*     */   public boolean isNoMaxValue() {
/* 117 */     return this.noMaxValue;
/*     */   }
/*     */   
/*     */   public void setNoMaxValue(boolean noMaxValue) {
/* 121 */     this.noMaxValue = noMaxValue;
/*     */   }
/*     */   
/*     */   public boolean isNoMinValue() {
/* 125 */     return this.noMinValue;
/*     */   }
/*     */   
/*     */   public void setNoMinValue(boolean noMinValue) {
/* 129 */     this.noMinValue = noMinValue;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLCreateSequenceStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */