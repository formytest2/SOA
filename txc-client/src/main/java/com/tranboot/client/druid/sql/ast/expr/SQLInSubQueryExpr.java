/*     */ package com.tranboot.client.druid.sql.ast.expr;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
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
/*     */ 
/*     */ 
/*     */ public class SQLInSubQueryExpr
/*     */   extends SQLExprImpl
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private boolean not = false;
/*     */   private SQLExpr expr;
/*     */   public SQLSelect subQuery;
/*     */   
/*     */   public SQLInSubQueryExpr() {}
/*     */   
/*     */   public boolean isNot() {
/*  38 */     return this.not;
/*     */   }
/*     */   
/*     */   public void setNot(boolean not) {
/*  42 */     this.not = not;
/*     */   }
/*     */   
/*     */   public SQLExpr getExpr() {
/*  46 */     return this.expr;
/*     */   }
/*     */   
/*     */   public void setExpr(SQLExpr expr) {
/*  50 */     if (expr != null) {
/*  51 */       expr.setParent((SQLObject)this);
/*     */     }
/*  53 */     this.expr = expr;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLInSubQueryExpr(SQLSelect select) {
/*  58 */     this.subQuery = select;
/*     */   }
/*     */   
/*     */   public SQLSelect getSubQuery() {
/*  62 */     return this.subQuery;
/*     */   }
/*     */   
/*     */   public void setSubQuery(SQLSelect subQuery) {
/*  66 */     if (subQuery != null) {
/*  67 */       subQuery.setParent((SQLObject)this);
/*     */     }
/*  69 */     this.subQuery = subQuery;
/*     */   }
/*     */   
/*     */   public void output(StringBuffer buf) {
/*  73 */     this.subQuery.output(buf);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  78 */     if (visitor.visit(this)) {
/*  79 */       acceptChild(visitor, (SQLObject)this.expr);
/*  80 */       acceptChild(visitor, (SQLObject)this.subQuery);
/*     */     } 
/*     */     
/*  83 */     visitor.endVisit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  88 */     int prime = 31;
/*  89 */     int result = 1;
/*  90 */     result = 31 * result + ((this.expr == null) ? 0 : this.expr.hashCode());
/*  91 */     result = 31 * result + (this.not ? 1231 : 1237);
/*  92 */     result = 31 * result + ((this.subQuery == null) ? 0 : this.subQuery.hashCode());
/*  93 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  98 */     if (this == obj) {
/*  99 */       return true;
/*     */     }
/* 101 */     if (obj == null) {
/* 102 */       return false;
/*     */     }
/* 104 */     if (getClass() != obj.getClass()) {
/* 105 */       return false;
/*     */     }
/* 107 */     SQLInSubQueryExpr other = (SQLInSubQueryExpr)obj;
/* 108 */     if (this.expr == null) {
/* 109 */       if (other.expr != null) {
/* 110 */         return false;
/*     */       }
/* 112 */     } else if (!this.expr.equals(other.expr)) {
/* 113 */       return false;
/*     */     } 
/* 115 */     if (this.not != other.not) {
/* 116 */       return false;
/*     */     }
/* 118 */     if (this.subQuery == null) {
/* 119 */       if (other.subQuery != null) {
/* 120 */         return false;
/*     */       }
/* 122 */     } else if (!this.subQuery.equals(other.subQuery)) {
/* 123 */       return false;
/*     */     } 
/* 125 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLInSubQueryExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */