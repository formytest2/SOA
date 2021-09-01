/*     */ package com.tranboot.client.druid.sql.ast.expr;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.SQLUtils;
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
/*     */ 
/*     */ public class SQLBinaryOpExpr
/*     */   extends SQLExprImpl
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private SQLExpr left;
/*     */   private SQLExpr right;
/*     */   private SQLBinaryOperator operator;
/*     */   private String dbType;
/*     */   
/*     */   public SQLBinaryOpExpr() {}
/*     */   
/*     */   public SQLBinaryOpExpr(String dbType) {
/*  38 */     this.dbType = dbType;
/*     */   }
/*     */   
/*     */   public SQLBinaryOpExpr(SQLExpr left, SQLBinaryOperator operator, SQLExpr right) {
/*  42 */     this(left, operator, right, null);
/*     */   }
/*     */   
/*     */   public SQLBinaryOpExpr(SQLExpr left, SQLBinaryOperator operator, SQLExpr right, String dbType) {
/*  46 */     setLeft(left);
/*  47 */     setRight(right);
/*  48 */     this.operator = operator;
/*  49 */     this.dbType = dbType;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLBinaryOpExpr(SQLExpr left, SQLExpr right, SQLBinaryOperator operator) {
/*  54 */     setLeft(left);
/*  55 */     setRight(right);
/*  56 */     this.operator = operator;
/*     */   }
/*     */   
/*     */   public String getDbType() {
/*  60 */     return this.dbType;
/*     */   }
/*     */   
/*     */   public void setDbType(String dbType) {
/*  64 */     this.dbType = dbType;
/*     */   }
/*     */   
/*     */   public SQLExpr getLeft() {
/*  68 */     return this.left;
/*     */   }
/*     */   
/*     */   public void setLeft(SQLExpr left) {
/*  72 */     if (left != null) {
/*  73 */       left.setParent((SQLObject)this);
/*     */     }
/*  75 */     this.left = left;
/*     */   }
/*     */   
/*     */   public SQLExpr getRight() {
/*  79 */     return this.right;
/*     */   }
/*     */   
/*     */   public void setRight(SQLExpr right) {
/*  83 */     if (right != null) {
/*  84 */       right.setParent((SQLObject)this);
/*     */     }
/*  86 */     this.right = right;
/*     */   }
/*     */   
/*     */   public SQLBinaryOperator getOperator() {
/*  90 */     return this.operator;
/*     */   }
/*     */   
/*     */   public void setOperator(SQLBinaryOperator operator) {
/*  94 */     this.operator = operator;
/*     */   }
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  98 */     if (visitor.visit(this)) {
/*  99 */       acceptChild(visitor, (SQLObject)this.left);
/* 100 */       acceptChild(visitor, (SQLObject)this.right);
/*     */     } 
/*     */     
/* 103 */     visitor.endVisit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 108 */     int prime = 31;
/* 109 */     int result = 1;
/* 110 */     result = 31 * result + ((this.left == null) ? 0 : this.left.hashCode());
/* 111 */     result = 31 * result + ((this.operator == null) ? 0 : this.operator.hashCode());
/* 112 */     result = 31 * result + ((this.right == null) ? 0 : this.right.hashCode());
/* 113 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 118 */     if (this == obj) {
/* 119 */       return true;
/*     */     }
/* 121 */     if (obj == null) {
/* 122 */       return false;
/*     */     }
/* 124 */     if (!(obj instanceof SQLBinaryOpExpr)) {
/* 125 */       return false;
/*     */     }
/* 127 */     SQLBinaryOpExpr other = (SQLBinaryOpExpr)obj;
/* 128 */     if (this.left == null) {
/* 129 */       if (other.left != null) {
/* 130 */         return false;
/*     */       }
/* 132 */     } else if (!this.left.equals(other.left)) {
/* 133 */       return false;
/*     */     } 
/* 135 */     if (this.operator != other.operator) {
/* 136 */       return false;
/*     */     }
/* 138 */     if (this.right == null) {
/* 139 */       if (other.right != null) {
/* 140 */         return false;
/*     */       }
/* 142 */     } else if (!this.right.equals(other.right)) {
/* 143 */       return false;
/*     */     } 
/* 145 */     return true;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 149 */     return SQLUtils.toSQLString((SQLObject)this, getDbType());
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLBinaryOpExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */