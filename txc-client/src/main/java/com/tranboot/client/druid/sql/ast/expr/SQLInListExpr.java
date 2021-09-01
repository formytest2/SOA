/*     */ package com.tranboot.client.druid.sql.ast.expr;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

import java.io.Serializable;
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
/*     */ public class SQLInListExpr
/*     */   extends SQLExprImpl
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private boolean not = false;
/*     */   private SQLExpr expr;
/*  31 */   private List<SQLExpr> targetList = new ArrayList<>();
/*     */ 
/*     */   
/*     */   public SQLInListExpr() {}
/*     */ 
/*     */   
/*     */   public SQLInListExpr(SQLExpr expr) {
/*  38 */     setExpr(expr);
/*     */   }
/*     */   
/*     */   public SQLInListExpr(SQLExpr expr, boolean not) {
/*  42 */     setExpr(expr);
/*  43 */     this.not = not;
/*     */   }
/*     */   
/*     */   public boolean isNot() {
/*  47 */     return this.not;
/*     */   }
/*     */   
/*     */   public void setNot(boolean not) {
/*  51 */     this.not = not;
/*     */   }
/*     */   
/*     */   public SQLExpr getExpr() {
/*  55 */     return this.expr;
/*     */   }
/*     */   
/*     */   public void setExpr(SQLExpr expr) {
/*  59 */     if (expr != null) {
/*  60 */       expr.setParent((SQLObject)this);
/*     */     }
/*     */     
/*  63 */     this.expr = expr;
/*     */   }
/*     */   
/*     */   public List<SQLExpr> getTargetList() {
/*  67 */     return this.targetList;
/*     */   }
/*     */   
/*     */   public void setTargetList(List<SQLExpr> targetList) {
/*  71 */     this.targetList = targetList;
/*     */   }
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  75 */     if (visitor.visit(this)) {
/*  76 */       acceptChild(visitor, (SQLObject)this.expr);
/*  77 */       acceptChild(visitor, this.targetList);
/*     */     } 
/*     */     
/*  80 */     visitor.endVisit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  85 */     int prime = 31;
/*  86 */     int result = 1;
/*  87 */     result = 31 * result + ((this.expr == null) ? 0 : this.expr.hashCode());
/*  88 */     result = 31 * result + (this.not ? 1231 : 1237);
/*  89 */     result = 31 * result + ((this.targetList == null) ? 0 : this.targetList.hashCode());
/*  90 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  95 */     if (this == obj) {
/*  96 */       return true;
/*     */     }
/*  98 */     if (obj == null) {
/*  99 */       return false;
/*     */     }
/* 101 */     if (getClass() != obj.getClass()) {
/* 102 */       return false;
/*     */     }
/* 104 */     SQLInListExpr other = (SQLInListExpr)obj;
/* 105 */     if (this.expr == null) {
/* 106 */       if (other.expr != null) {
/* 107 */         return false;
/*     */       }
/* 109 */     } else if (!this.expr.equals(other.expr)) {
/* 110 */       return false;
/*     */     } 
/* 112 */     if (this.not != other.not) {
/* 113 */       return false;
/*     */     }
/* 115 */     if (this.targetList == null) {
/* 116 */       if (other.targetList != null) {
/* 117 */         return false;
/*     */       }
/* 119 */     } else if (!this.targetList.equals(other.targetList)) {
/* 120 */       return false;
/*     */     } 
/* 122 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLInListExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */