/*     */ package com.tranboot.client.druid.sql.ast.expr;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.*;
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
/*     */ 
/*     */ 
/*     */ public class SQLAggregateExpr
/*     */   extends SQLExprImpl
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected String methodName;
/*     */   protected SQLAggregateOption option;
/*  34 */   protected final List<SQLExpr> arguments = new ArrayList<>();
/*     */   protected SQLKeep keep;
/*     */   protected SQLOver over;
/*     */   protected SQLOrderBy withinGroup;
/*     */   protected boolean ignoreNulls = false;
/*     */   
/*     */   public SQLAggregateExpr(String methodName) {
/*  41 */     this.methodName = methodName;
/*     */   }
/*     */   
/*     */   public SQLAggregateExpr(String methodName, SQLAggregateOption option) {
/*  45 */     this.methodName = methodName;
/*  46 */     this.option = option;
/*     */   }
/*     */   
/*     */   public String getMethodName() {
/*  50 */     return this.methodName;
/*     */   }
/*     */   
/*     */   public void setMethodName(String methodName) {
/*  54 */     this.methodName = methodName;
/*     */   }
/*     */   
/*     */   public SQLOrderBy getWithinGroup() {
/*  58 */     return this.withinGroup;
/*     */   }
/*     */   
/*     */   public void setWithinGroup(SQLOrderBy withinGroup) {
/*  62 */     if (withinGroup != null) {
/*  63 */       withinGroup.setParent((SQLObject)this);
/*     */     }
/*     */     
/*  66 */     this.withinGroup = withinGroup;
/*     */   }
/*     */   
/*     */   public SQLAggregateOption getOption() {
/*  70 */     return this.option;
/*     */   }
/*     */   
/*     */   public void setOption(SQLAggregateOption option) {
/*  74 */     this.option = option;
/*     */   }
/*     */   
/*     */   public List<SQLExpr> getArguments() {
/*  78 */     return this.arguments;
/*     */   }
/*     */   
/*     */   public void addArgument(SQLExpr argument) {
/*  82 */     if (argument != null) {
/*  83 */       argument.setParent((SQLObject)this);
/*     */     }
/*  85 */     this.arguments.add(argument);
/*     */   }
/*     */   
/*     */   public SQLOver getOver() {
/*  89 */     return this.over;
/*     */   }
/*     */   
/*     */   public void setOver(SQLOver over) {
/*  93 */     if (over != null) {
/*  94 */       over.setParent((SQLObject)this);
/*     */     }
/*  96 */     this.over = over;
/*     */   }
/*     */   
/*     */   public SQLKeep getKeep() {
/* 100 */     return this.keep;
/*     */   }
/*     */   
/*     */   public void setKeep(SQLKeep keep) {
/* 104 */     if (keep != null) {
/* 105 */       keep.setParent((SQLObject)this);
/*     */     }
/* 107 */     this.keep = keep;
/*     */   }
/*     */   
/*     */   public boolean isIgnoreNulls() {
/* 111 */     return this.ignoreNulls;
/*     */   }
/*     */   
/*     */   public void setIgnoreNulls(boolean ignoreNulls) {
/* 115 */     this.ignoreNulls = ignoreNulls;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/* 121 */     if (visitor.visit(this)) {
/* 122 */       acceptChild(visitor, this.arguments);
/* 123 */       acceptChild(visitor, (SQLObject)this.over);
/*     */     } 
/*     */     
/* 126 */     visitor.endVisit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 131 */     int prime = 31;
/* 132 */     int result = 1;
/* 133 */     result = 31 * result + ((this.arguments == null) ? 0 : this.arguments.hashCode());
/* 134 */     result = 31 * result + ((this.methodName == null) ? 0 : this.methodName.hashCode());
/* 135 */     result = 31 * result + ((this.option == null) ? 0 : this.option.hashCode());
/* 136 */     result = 31 * result + ((this.over == null) ? 0 : this.over.hashCode());
/* 137 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 142 */     if (this == obj) {
/* 143 */       return true;
/*     */     }
/* 145 */     if (obj == null) {
/* 146 */       return false;
/*     */     }
/* 148 */     if (getClass() != obj.getClass()) {
/* 149 */       return false;
/*     */     }
/* 151 */     SQLAggregateExpr other = (SQLAggregateExpr)obj;
/* 152 */     if (this.arguments == null) {
/* 153 */       if (other.arguments != null) {
/* 154 */         return false;
/*     */       }
/* 156 */     } else if (!this.arguments.equals(other.arguments)) {
/* 157 */       return false;
/*     */     } 
/* 159 */     if (this.methodName == null) {
/* 160 */       if (other.methodName != null) {
/* 161 */         return false;
/*     */       }
/* 163 */     } else if (!this.methodName.equals(other.methodName)) {
/* 164 */       return false;
/*     */     } 
/* 166 */     if (this.over == null) {
/* 167 */       if (other.over != null) {
/* 168 */         return false;
/*     */       }
/* 170 */     } else if (!this.over.equals(other.over)) {
/* 171 */       return false;
/*     */     } 
/* 173 */     if (this.option != other.option) {
/* 174 */       return false;
/*     */     }
/* 176 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLAggregateExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */