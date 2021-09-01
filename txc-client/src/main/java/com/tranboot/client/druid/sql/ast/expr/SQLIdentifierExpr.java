/*     */ package com.tranboot.client.druid.sql.ast.expr;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.SQLName;
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
/*     */ public class SQLIdentifierExpr
/*     */   extends SQLExprImpl
/*     */   implements SQLName
/*     */ {
/*     */   private String name;
/*     */   private transient String lowerName;
/*     */   
/*     */   public SQLIdentifierExpr() {}
/*     */   
/*     */   public SQLIdentifierExpr(String name) {
/*  33 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getSimpleName() {
/*  37 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  41 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  45 */     this.name = name;
/*  46 */     this.lowerName = null;
/*     */   }
/*     */   
/*     */   public String getLowerName() {
/*  50 */     if (this.lowerName == null && this.name != null) {
/*  51 */       this.lowerName = this.name.toLowerCase();
/*     */     }
/*  53 */     return this.lowerName;
/*     */   }
/*     */   
/*     */   public void setLowerName(String lowerName) {
/*  57 */     this.lowerName = lowerName;
/*     */   }
/*     */   
/*     */   public void output(StringBuffer buf) {
/*  61 */     buf.append(this.name);
/*     */   }
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  65 */     visitor.visit(this);
/*     */     
/*  67 */     visitor.endVisit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  72 */     int prime = 31;
/*  73 */     int result = 1;
/*  74 */     result = 31 * result + ((this.name == null) ? 0 : this.name.hashCode());
/*  75 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  80 */     if (this == obj) {
/*  81 */       return true;
/*     */     }
/*  83 */     if (obj == null) {
/*  84 */       return false;
/*     */     }
/*  86 */     if (!(obj instanceof SQLIdentifierExpr)) {
/*  87 */       return false;
/*     */     }
/*  89 */     SQLIdentifierExpr other = (SQLIdentifierExpr)obj;
/*  90 */     if (this.name == null) {
/*  91 */       if (other.name != null) {
/*  92 */         return false;
/*     */       }
/*  94 */     } else if (!this.name.equals(other.name)) {
/*  95 */       return false;
/*     */     } 
/*  97 */     return true;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 101 */     return this.name;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLIdentifierExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */