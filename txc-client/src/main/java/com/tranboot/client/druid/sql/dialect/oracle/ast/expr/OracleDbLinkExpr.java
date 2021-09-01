/*     */ package com.tranboot.client.druid.sql.dialect.oracle.ast.expr;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
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
/*     */ public class OracleDbLinkExpr
/*     */   extends SQLExprImpl
/*     */   implements SQLName, OracleExpr
/*     */ {
/*     */   private SQLExpr expr;
/*     */   private String dbLink;
/*     */   
/*     */   public String getSimpleName() {
/*  34 */     return this.dbLink;
/*     */   }
/*     */   
/*     */   public SQLExpr getExpr() {
/*  38 */     return this.expr;
/*     */   }
/*     */   
/*     */   public void setExpr(SQLExpr expr) {
/*  42 */     this.expr = expr;
/*     */   }
/*     */   
/*     */   public String getDbLink() {
/*  46 */     return this.dbLink;
/*     */   }
/*     */   
/*     */   public void setDbLink(String dbLink) {
/*  50 */     this.dbLink = dbLink;
/*     */   }
/*     */   
/*     */   public void output(StringBuffer buf) {
/*  54 */     this.expr.output(buf);
/*  55 */     buf.append("@");
/*  56 */     buf.append(this.dbLink);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  61 */     accept0((OracleASTVisitor)visitor);
/*     */   }
/*     */   
/*     */   public void accept0(OracleASTVisitor visitor) {
/*  65 */     if (visitor.visit(this)) {
/*  66 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.expr);
/*     */     }
/*     */     
/*  69 */     visitor.endVisit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  74 */     int prime = 31;
/*  75 */     int result = 1;
/*  76 */     result = 31 * result + ((this.dbLink == null) ? 0 : this.dbLink.hashCode());
/*  77 */     result = 31 * result + ((this.expr == null) ? 0 : this.expr.hashCode());
/*  78 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  83 */     if (this == obj) {
/*  84 */       return true;
/*     */     }
/*  86 */     if (obj == null) {
/*  87 */       return false;
/*     */     }
/*  89 */     if (getClass() != obj.getClass()) {
/*  90 */       return false;
/*     */     }
/*  92 */     OracleDbLinkExpr other = (OracleDbLinkExpr)obj;
/*  93 */     if (this.dbLink == null) {
/*  94 */       if (other.dbLink != null) {
/*  95 */         return false;
/*     */       }
/*  97 */     } else if (!this.dbLink.equals(other.dbLink)) {
/*  98 */       return false;
/*     */     } 
/* 100 */     if (this.expr == null) {
/* 101 */       if (other.expr != null) {
/* 102 */         return false;
/*     */       }
/* 104 */     } else if (!this.expr.equals(other.expr)) {
/* 105 */       return false;
/*     */     } 
/* 107 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\expr\OracleDbLinkExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */