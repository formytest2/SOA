/*     */ package com.tranboot.client.druid.sql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLObjectImpl;
import com.tranboot.client.druid.sql.ast.SQLOrderingSpecification;
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
/*     */ public class SQLSelectOrderByItem
/*     */   extends SQLObjectImpl
/*     */ {
/*     */   protected SQLExpr expr;
/*     */   protected String collate;
/*     */   protected SQLOrderingSpecification type;
/*     */   protected NullsOrderType nullsOrderType;
/*     */   
/*     */   public SQLSelectOrderByItem() {}
/*     */   
/*     */   public SQLSelectOrderByItem(SQLExpr expr) {
/*  35 */     setExpr(expr);
/*     */   }
/*     */   
/*     */   public SQLExpr getExpr() {
/*  39 */     return this.expr;
/*     */   }
/*     */   
/*     */   public void setExpr(SQLExpr expr) {
/*  43 */     if (expr != null) {
/*  44 */       expr.setParent((SQLObject)this);
/*     */     }
/*  46 */     this.expr = expr;
/*     */   }
/*     */   
/*     */   public String getCollate() {
/*  50 */     return this.collate;
/*     */   }
/*     */   
/*     */   public void setCollate(String collate) {
/*  54 */     this.collate = collate;
/*     */   }
/*     */   
/*     */   public SQLOrderingSpecification getType() {
/*  58 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(SQLOrderingSpecification type) {
/*  62 */     this.type = type;
/*     */   }
/*     */   
/*     */   public NullsOrderType getNullsOrderType() {
/*  66 */     return this.nullsOrderType;
/*     */   }
/*     */   
/*     */   public void setNullsOrderType(NullsOrderType nullsOrderType) {
/*  70 */     this.nullsOrderType = nullsOrderType;
/*     */   }
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  74 */     if (visitor.visit(this)) {
/*  75 */       acceptChild(visitor, (SQLObject)this.expr);
/*     */     }
/*     */     
/*  78 */     visitor.endVisit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  83 */     int prime = 31;
/*  84 */     int result = 1;
/*  85 */     result = 31 * result + ((this.collate == null) ? 0 : this.collate.hashCode());
/*  86 */     result = 31 * result + ((this.expr == null) ? 0 : this.expr.hashCode());
/*  87 */     result = 31 * result + ((this.type == null) ? 0 : this.type.hashCode());
/*  88 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  93 */     if (this == obj) return true; 
/*  94 */     if (obj == null) return false; 
/*  95 */     if (getClass() != obj.getClass()) return false; 
/*  96 */     SQLSelectOrderByItem other = (SQLSelectOrderByItem)obj;
/*  97 */     if (this.collate == null)
/*  98 */     { if (other.collate != null) return false;  }
/*  99 */     else if (!this.collate.equals(other.collate)) { return false; }
/* 100 */      if (this.expr == null)
/* 101 */     { if (other.expr != null) return false;  }
/* 102 */     else if (!this.expr.equals(other.expr)) { return false; }
/* 103 */      if (this.type != other.type) return false; 
/* 104 */     return true;
/*     */   }
/*     */   
/*     */   public enum NullsOrderType {
/* 108 */     NullsFirst, NullsLast;
/*     */     
/*     */     public String toFormalString() {
/* 111 */       if (NullsFirst.equals(this)) {
/* 112 */         return "NULLS FIRST";
/*     */       }
/*     */       
/* 115 */       if (NullsLast.equals(this)) {
/* 116 */         return "NULLS LAST";
/*     */       }
/*     */       
/* 119 */       throw new IllegalArgumentException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLSelectOrderByItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */