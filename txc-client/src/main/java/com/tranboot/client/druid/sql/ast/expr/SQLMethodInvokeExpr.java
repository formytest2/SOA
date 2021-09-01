/*     */ package com.tranboot.client.druid.sql.ast.expr;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
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
/*     */ 
/*     */ public class SQLMethodInvokeExpr
/*     */   extends SQLExprImpl
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private String methodName;
/*     */   private SQLExpr owner;
/*  32 */   private final List<SQLExpr> parameters = new ArrayList<>();
/*     */   
/*     */   private SQLExpr from;
/*     */ 
/*     */   
/*     */   public SQLMethodInvokeExpr() {}
/*     */ 
/*     */   
/*     */   public SQLMethodInvokeExpr(String methodName) {
/*  41 */     this.methodName = methodName;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLMethodInvokeExpr(String methodName, SQLExpr owner) {
/*  46 */     this.methodName = methodName;
/*  47 */     setOwner(owner);
/*     */   }
/*     */   
/*     */   public String getMethodName() {
/*  51 */     return this.methodName;
/*     */   }
/*     */   
/*     */   public void setMethodName(String methodName) {
/*  55 */     this.methodName = methodName;
/*     */   }
/*     */   
/*     */   public SQLExpr getOwner() {
/*  59 */     return this.owner;
/*     */   }
/*     */   
/*     */   public void setOwner(SQLExpr owner) {
/*  63 */     if (owner != null) {
/*  64 */       owner.setParent((SQLObject)this);
/*     */     }
/*  66 */     this.owner = owner;
/*     */   }
/*     */   
/*     */   public SQLExpr getFrom() {
/*  70 */     return this.from;
/*     */   }
/*     */   
/*     */   public void setFrom(SQLExpr from) {
/*  74 */     this.from = from;
/*     */   }
/*     */   
/*     */   public List<SQLExpr> getParameters() {
/*  78 */     return this.parameters;
/*     */   }
/*     */   
/*     */   public void addParameter(SQLExpr param) {
/*  82 */     if (param != null) {
/*  83 */       param.setParent((SQLObject)this);
/*     */     }
/*  85 */     this.parameters.add(param);
/*     */   }
/*     */   
/*     */   public void output(StringBuffer buf) {
/*  89 */     if (this.owner != null) {
/*  90 */       this.owner.output(buf);
/*  91 */       buf.append(".");
/*     */     } 
/*     */     
/*  94 */     buf.append(this.methodName);
/*  95 */     buf.append("(");
/*  96 */     for (int i = 0, size = this.parameters.size(); i < size; i++) {
/*  97 */       if (i != 0) {
/*  98 */         buf.append(", ");
/*     */       }
/*     */       
/* 101 */       ((SQLExpr)this.parameters.get(i)).output(buf);
/*     */     } 
/* 103 */     buf.append(")");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/* 108 */     if (visitor.visit(this)) {
/* 109 */       acceptChild(visitor, (SQLObject)this.owner);
/* 110 */       acceptChild(visitor, this.parameters);
/*     */     } 
/*     */     
/* 113 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   protected void accept0(OracleASTVisitor visitor) {
/* 117 */     if (visitor.visit(this)) {
/* 118 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.owner);
/* 119 */       acceptChild((SQLASTVisitor)visitor, this.parameters);
/*     */     } 
/*     */     
/* 122 */     visitor.endVisit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 127 */     if (this == o) return true; 
/* 128 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/* 130 */     SQLMethodInvokeExpr that = (SQLMethodInvokeExpr)o;
/*     */     
/* 132 */     if ((this.methodName != null) ? !this.methodName.equals(that.methodName) : (that.methodName != null)) return false; 
/* 133 */     if ((this.owner != null) ? !this.owner.equals(that.owner) : (that.owner != null)) return false; 
/* 134 */     if ((this.parameters != null) ? !this.parameters.equals(that.parameters) : (that.parameters != null)) return false; 
/* 135 */     return (this.from != null) ? this.from.equals(that.from) : ((that.from == null));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 141 */     int result = (this.methodName != null) ? this.methodName.hashCode() : 0;
/* 142 */     result = 31 * result + ((this.owner != null) ? this.owner.hashCode() : 0);
/* 143 */     result = 31 * result + ((this.parameters != null) ? this.parameters.hashCode() : 0);
/* 144 */     result = 31 * result + ((this.from != null) ? this.from.hashCode() : 0);
/* 145 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLMethodInvokeExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */