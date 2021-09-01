/*     */ package com.tranboot.client.druid.sql.ast.expr;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*     */ public class SQLBetweenExpr
/*     */   extends SQLExprImpl
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public SQLExpr testExpr;
/*     */   private boolean not;
/*     */   public SQLExpr beginExpr;
/*     */   public SQLExpr endExpr;
/*     */   
/*     */   public SQLBetweenExpr() {}
/*     */   
/*     */   public SQLBetweenExpr(SQLExpr testExpr, SQLExpr beginExpr, SQLExpr endExpr) {
/*  37 */     setTestExpr(testExpr);
/*  38 */     setBeginExpr(beginExpr);
/*  39 */     setEndExpr(endExpr);
/*     */   }
/*     */   
/*     */   public SQLBetweenExpr(SQLExpr testExpr, boolean not, SQLExpr beginExpr, SQLExpr endExpr) {
/*  43 */     this(testExpr, beginExpr, endExpr);
/*  44 */     this.not = not;
/*     */   }
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  48 */     if (visitor.visit(this)) {
/*  49 */       acceptChild(visitor, (SQLObject)this.testExpr);
/*  50 */       acceptChild(visitor, (SQLObject)this.beginExpr);
/*  51 */       acceptChild(visitor, (SQLObject)this.endExpr);
/*     */     } 
/*  53 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public SQLExpr getTestExpr() {
/*  57 */     return this.testExpr;
/*     */   }
/*     */   
/*     */   public void setTestExpr(SQLExpr testExpr) {
/*  61 */     if (testExpr != null) {
/*  62 */       testExpr.setParent((SQLObject)this);
/*     */     }
/*  64 */     this.testExpr = testExpr;
/*     */   }
/*     */   
/*     */   public boolean isNot() {
/*  68 */     return this.not;
/*     */   }
/*     */   
/*     */   public void setNot(boolean not) {
/*  72 */     this.not = not;
/*     */   }
/*     */   
/*     */   public SQLExpr getBeginExpr() {
/*  76 */     return this.beginExpr;
/*     */   }
/*     */   
/*     */   public void setBeginExpr(SQLExpr beginExpr) {
/*  80 */     if (beginExpr != null) {
/*  81 */       beginExpr.setParent((SQLObject)this);
/*     */     }
/*  83 */     this.beginExpr = beginExpr;
/*     */   }
/*     */   
/*     */   public SQLExpr getEndExpr() {
/*  87 */     return this.endExpr;
/*     */   }
/*     */   
/*     */   public void setEndExpr(SQLExpr endExpr) {
/*  91 */     if (endExpr != null) {
/*  92 */       endExpr.setParent((SQLObject)this);
/*     */     }
/*  94 */     this.endExpr = endExpr;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  99 */     int prime = 31;
/* 100 */     int result = 1;
/* 101 */     result = 31 * result + ((this.beginExpr == null) ? 0 : this.beginExpr.hashCode());
/* 102 */     result = 31 * result + ((this.endExpr == null) ? 0 : this.endExpr.hashCode());
/* 103 */     result = 31 * result + (this.not ? 1231 : 1237);
/* 104 */     result = 31 * result + ((this.testExpr == null) ? 0 : this.testExpr.hashCode());
/* 105 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 110 */     if (this == obj) {
/* 111 */       return true;
/*     */     }
/* 113 */     if (obj == null) {
/* 114 */       return false;
/*     */     }
/* 116 */     if (getClass() != obj.getClass()) {
/* 117 */       return false;
/*     */     }
/* 119 */     SQLBetweenExpr other = (SQLBetweenExpr)obj;
/* 120 */     if (this.beginExpr == null) {
/* 121 */       if (other.beginExpr != null) {
/* 122 */         return false;
/*     */       }
/* 124 */     } else if (!this.beginExpr.equals(other.beginExpr)) {
/* 125 */       return false;
/*     */     } 
/* 127 */     if (this.endExpr == null) {
/* 128 */       if (other.endExpr != null) {
/* 129 */         return false;
/*     */       }
/* 131 */     } else if (!this.endExpr.equals(other.endExpr)) {
/* 132 */       return false;
/*     */     } 
/* 134 */     if (this.not != other.not) {
/* 135 */       return false;
/*     */     }
/* 137 */     if (this.testExpr == null) {
/* 138 */       if (other.testExpr != null) {
/* 139 */         return false;
/*     */       }
/* 141 */     } else if (!this.testExpr.equals(other.testExpr)) {
/* 142 */       return false;
/*     */     } 
/* 144 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLBetweenExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */