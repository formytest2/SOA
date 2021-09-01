/*     */ package com.tranboot.client.druid.sql.ast.expr;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.SQLUtils;
import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*     */ public class SQLTimestampExpr
/*     */   extends SQLExprImpl
/*     */ {
/*     */   protected String literal;
/*     */   protected String timeZone;
/*     */   protected boolean withTimeZone = false;
/*     */   
/*     */   public String getLiteral() {
/*  33 */     return this.literal;
/*     */   }
/*     */   
/*     */   public void setLiteral(String literal) {
/*  37 */     this.literal = literal;
/*     */   }
/*     */   
/*     */   public String getTimeZone() {
/*  41 */     return this.timeZone;
/*     */   }
/*     */   
/*     */   public void setTimeZone(String timeZone) {
/*  45 */     this.timeZone = timeZone;
/*     */   }
/*     */   
/*     */   public boolean isWithTimeZone() {
/*  49 */     return this.withTimeZone;
/*     */   }
/*     */   
/*     */   public void setWithTimeZone(boolean withTimeZone) {
/*  53 */     this.withTimeZone = withTimeZone;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  58 */     int prime = 31;
/*  59 */     int result = 1;
/*  60 */     result = 31 * result + ((this.literal == null) ? 0 : this.literal.hashCode());
/*  61 */     result = 31 * result + ((this.timeZone == null) ? 0 : this.timeZone.hashCode());
/*  62 */     result = 31 * result + (this.withTimeZone ? 1231 : 1237);
/*  63 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  68 */     if (this == obj) {
/*  69 */       return true;
/*     */     }
/*  71 */     if (obj == null) {
/*  72 */       return false;
/*     */     }
/*  74 */     if (getClass() != obj.getClass()) {
/*  75 */       return false;
/*     */     }
/*  77 */     SQLTimestampExpr other = (SQLTimestampExpr)obj;
/*  78 */     if (this.literal == null) {
/*  79 */       if (other.literal != null) {
/*  80 */         return false;
/*     */       }
/*  82 */     } else if (!this.literal.equals(other.literal)) {
/*  83 */       return false;
/*     */     } 
/*  85 */     if (this.timeZone == null) {
/*  86 */       if (other.timeZone != null) {
/*  87 */         return false;
/*     */       }
/*  89 */     } else if (!this.timeZone.equals(other.timeZone)) {
/*  90 */       return false;
/*     */     } 
/*  92 */     if (this.withTimeZone != other.withTimeZone) {
/*  93 */       return false;
/*     */     }
/*  95 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/* 100 */     visitor.visit(this);
/*     */     
/* 102 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 106 */     return SQLUtils.toSQLString((SQLObject)this, null);
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLTimestampExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */