/*     */ package com.tranboot.client.druid.sql.ast.expr;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.SQLName;
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
/*     */ public class SQLPropertyExpr
/*     */   extends SQLExprImpl
/*     */   implements SQLName
/*     */ {
/*     */   private SQLExpr owner;
/*     */   private String name;
/*     */   
/*     */   public SQLPropertyExpr(SQLExpr owner, String name) {
/*  29 */     setOwner(owner);
/*  30 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLPropertyExpr() {}
/*     */ 
/*     */   
/*     */   public String getSimpleName() {
/*  38 */     return this.name;
/*     */   }
/*     */   
/*     */   public SQLExpr getOwner() {
/*  42 */     return this.owner;
/*     */   }
/*     */   
/*     */   public void setOwner(SQLExpr owner) {
/*  46 */     if (owner != null) {
/*  47 */       owner.setParent((SQLObject)this);
/*     */     }
/*  49 */     this.owner = owner;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  53 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  57 */     this.name = name;
/*     */   }
/*     */   
/*     */   public void output(StringBuffer buf) {
/*  61 */     this.owner.output(buf);
/*  62 */     buf.append(".");
/*  63 */     buf.append(this.name);
/*     */   }
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  67 */     if (visitor.visit(this)) {
/*  68 */       acceptChild(visitor, (SQLObject)this.owner);
/*     */     }
/*     */     
/*  71 */     visitor.endVisit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  76 */     int prime = 31;
/*  77 */     int result = 1;
/*  78 */     result = 31 * result + ((this.name == null) ? 0 : this.name.hashCode());
/*  79 */     result = 31 * result + ((this.owner == null) ? 0 : this.owner.hashCode());
/*  80 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  85 */     if (this == obj) {
/*  86 */       return true;
/*     */     }
/*  88 */     if (obj == null) {
/*  89 */       return false;
/*     */     }
/*  91 */     if (!(obj instanceof SQLPropertyExpr)) {
/*  92 */       return false;
/*     */     }
/*  94 */     SQLPropertyExpr other = (SQLPropertyExpr)obj;
/*  95 */     if (this.name == null) {
/*  96 */       if (other.name != null) {
/*  97 */         return false;
/*     */       }
/*  99 */     } else if (!this.name.equals(other.name)) {
/* 100 */       return false;
/*     */     } 
/* 102 */     if (this.owner == null) {
/* 103 */       if (other.owner != null) {
/* 104 */         return false;
/*     */       }
/* 106 */     } else if (!this.owner.equals(other.owner)) {
/* 107 */       return false;
/*     */     } 
/* 109 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLPropertyExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */