/*     */ package com.tranboot.client.druid.sql.ast.expr;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLSelect;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

import java.io.Serializable;

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
/*     */ public class SQLExistsExpr
/*     */   extends SQLExprImpl
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public boolean not = false;
/*     */   public SQLSelect subQuery;
/*     */   
/*     */   public SQLExistsExpr() {}
/*     */   
/*     */   public SQLExistsExpr(SQLSelect subQuery) {
/*  35 */     setSubQuery(subQuery);
/*     */   }
/*     */   
/*     */   public SQLExistsExpr(SQLSelect subQuery, boolean not) {
/*  39 */     setSubQuery(subQuery);
/*  40 */     this.not = not;
/*     */   }
/*     */   
/*     */   public boolean isNot() {
/*  44 */     return this.not;
/*     */   }
/*     */   
/*     */   public void setNot(boolean not) {
/*  48 */     this.not = not;
/*     */   }
/*     */   
/*     */   public SQLSelect getSubQuery() {
/*  52 */     return this.subQuery;
/*     */   }
/*     */   
/*     */   public void setSubQuery(SQLSelect subQuery) {
/*  56 */     if (subQuery != null) {
/*  57 */       subQuery.setParent((SQLObject)this);
/*     */     }
/*  59 */     this.subQuery = subQuery;
/*     */   }
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  63 */     if (visitor.visit(this)) {
/*  64 */       acceptChild(visitor, (SQLObject)this.subQuery);
/*     */     }
/*     */     
/*  67 */     visitor.endVisit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  72 */     int prime = 31;
/*  73 */     int result = 1;
/*  74 */     result = 31 * result + (this.not ? 1231 : 1237);
/*  75 */     result = 31 * result + ((this.subQuery == null) ? 0 : this.subQuery.hashCode());
/*  76 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  81 */     if (this == obj) {
/*  82 */       return true;
/*     */     }
/*  84 */     if (obj == null) {
/*  85 */       return false;
/*     */     }
/*  87 */     if (getClass() != obj.getClass()) {
/*  88 */       return false;
/*     */     }
/*  90 */     SQLExistsExpr other = (SQLExistsExpr)obj;
/*  91 */     if (this.not != other.not) {
/*  92 */       return false;
/*     */     }
/*  94 */     if (this.subQuery == null) {
/*  95 */       if (other.subQuery != null) {
/*  96 */         return false;
/*     */       }
/*  98 */     } else if (!this.subQuery.equals(other.subQuery)) {
/*  99 */       return false;
/*     */     } 
/* 101 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLExistsExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */