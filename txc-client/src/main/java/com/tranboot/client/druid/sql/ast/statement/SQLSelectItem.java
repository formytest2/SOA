/*     */ package com.tranboot.client.druid.sql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLObjectImpl;
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
/*     */ public class SQLSelectItem
/*     */   extends SQLObjectImpl
/*     */ {
/*     */   protected SQLExpr expr;
/*     */   protected String alias;
/*     */   protected boolean connectByRoot = false;
/*     */   
/*     */   public SQLSelectItem() {}
/*     */   
/*     */   public SQLSelectItem(SQLExpr expr) {
/*  33 */     this(expr, null);
/*     */   }
/*     */   
/*     */   public SQLSelectItem(SQLExpr expr, String alias) {
/*  37 */     this.expr = expr;
/*  38 */     this.alias = alias;
/*     */     
/*  40 */     if (expr != null) {
/*  41 */       expr.setParent((SQLObject)this);
/*     */     }
/*     */   }
/*     */   
/*     */   public SQLSelectItem(SQLExpr expr, String alias, boolean connectByRoot) {
/*  46 */     this.connectByRoot = connectByRoot;
/*  47 */     this.expr = expr;
/*  48 */     this.alias = alias;
/*     */     
/*  50 */     if (expr != null) {
/*  51 */       expr.setParent((SQLObject)this);
/*     */     }
/*     */   }
/*     */   
/*     */   public SQLExpr getExpr() {
/*  56 */     return this.expr;
/*     */   }
/*     */   
/*     */   public void setExpr(SQLExpr expr) {
/*  60 */     this.expr = expr;
/*  61 */     if (expr != null) {
/*  62 */       expr.setParent((SQLObject)this);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getAlias() {
/*  67 */     return this.alias;
/*     */   }
/*     */   
/*     */   public void setAlias(String alias) {
/*  71 */     this.alias = alias;
/*     */   }
/*     */   
/*     */   public void output(StringBuffer buf) {
/*  75 */     if (this.connectByRoot) {
/*  76 */       buf.append(" CONNECT_BY_ROOT ");
/*     */     }
/*  78 */     this.expr.output(buf);
/*  79 */     if (this.alias != null && this.alias.length() != 0) {
/*  80 */       buf.append(" AS ");
/*  81 */       buf.append(this.alias);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  86 */     if (visitor.visit(this)) {
/*  87 */       acceptChild(visitor, (SQLObject)this.expr);
/*     */     }
/*  89 */     visitor.endVisit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  94 */     int prime = 31;
/*  95 */     int result = 1;
/*  96 */     result = 31 * result + ((this.alias == null) ? 0 : this.alias.hashCode());
/*  97 */     result = 31 * result + ((this.expr == null) ? 0 : this.expr.hashCode());
/*  98 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 103 */     if (this == obj) return true; 
/* 104 */     if (obj == null) return false; 
/* 105 */     if (getClass() != obj.getClass()) return false; 
/* 106 */     SQLSelectItem other = (SQLSelectItem)obj;
/* 107 */     if (this.alias == null)
/* 108 */     { if (other.alias != null) return false;  }
/* 109 */     else if (!this.alias.equals(other.alias)) { return false; }
/* 110 */      if (this.expr == null)
/* 111 */     { if (other.expr != null) return false;  }
/* 112 */     else if (!this.expr.equals(other.expr)) { return false; }
/* 113 */      return true;
/*     */   }
/*     */   
/*     */   public boolean isConnectByRoot() {
/* 117 */     return this.connectByRoot;
/*     */   }
/*     */   
/*     */   public void setConnectByRoot(boolean connectByRoot) {
/* 121 */     this.connectByRoot = connectByRoot;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLSelectItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */