/*     */ package com.tranboot.client.druid.sql.dialect.oracle.ast.expr;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.expr.SQLLiteralExpr;
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
/*     */ public class OracleIntervalExpr
/*     */   extends SQLExprImpl
/*     */   implements SQLLiteralExpr, OracleExpr
/*     */ {
/*     */   private SQLExpr value;
/*     */   private OracleIntervalType type;
/*     */   private Integer precision;
/*     */   private Integer factionalSecondsPrecision;
/*     */   private OracleIntervalType toType;
/*     */   private Integer toFactionalSecondsPrecision;
/*     */   
/*     */   public SQLExpr getValue() {
/*  38 */     return this.value;
/*     */   }
/*     */   
/*     */   public void setValue(SQLExpr value) {
/*  42 */     this.value = value;
/*     */   }
/*     */   
/*     */   public OracleIntervalType getType() {
/*  46 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(OracleIntervalType type) {
/*  50 */     this.type = type;
/*     */   }
/*     */   
/*     */   public Integer getPrecision() {
/*  54 */     return this.precision;
/*     */   }
/*     */   
/*     */   public void setPrecision(Integer precision) {
/*  58 */     this.precision = precision;
/*     */   }
/*     */   
/*     */   public Integer getFactionalSecondsPrecision() {
/*  62 */     return this.factionalSecondsPrecision;
/*     */   }
/*     */   
/*     */   public void setFactionalSecondsPrecision(Integer factionalSecondsPrecision) {
/*  66 */     this.factionalSecondsPrecision = factionalSecondsPrecision;
/*     */   }
/*     */   
/*     */   public OracleIntervalType getToType() {
/*  70 */     return this.toType;
/*     */   }
/*     */   
/*     */   public void setToType(OracleIntervalType toType) {
/*  74 */     this.toType = toType;
/*     */   }
/*     */   
/*     */   public Integer getToFactionalSecondsPrecision() {
/*  78 */     return this.toFactionalSecondsPrecision;
/*     */   }
/*     */   
/*     */   public void setToFactionalSecondsPrecision(Integer toFactionalSecondsPrecision) {
/*  82 */     this.toFactionalSecondsPrecision = toFactionalSecondsPrecision;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  87 */     accept0((OracleASTVisitor)visitor);
/*     */   }
/*     */   
/*     */   public void accept0(OracleASTVisitor visitor) {
/*  91 */     visitor.visit(this);
/*  92 */     visitor.endVisit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  97 */     int prime = 31;
/*  98 */     int result = 1;
/*  99 */     result = 31 * result + ((this.factionalSecondsPrecision == null) ? 0 : this.factionalSecondsPrecision.hashCode());
/* 100 */     result = 31 * result + ((this.precision == null) ? 0 : this.precision.hashCode());
/* 101 */     result = 31 * result + ((this.toFactionalSecondsPrecision == null) ? 0 : this.toFactionalSecondsPrecision.hashCode());
/* 102 */     result = 31 * result + ((this.toType == null) ? 0 : this.toType.hashCode());
/* 103 */     result = 31 * result + ((this.type == null) ? 0 : this.type.hashCode());
/* 104 */     result = 31 * result + ((this.value == null) ? 0 : this.value.hashCode());
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
/* 119 */     OracleIntervalExpr other = (OracleIntervalExpr)obj;
/* 120 */     if (this.factionalSecondsPrecision == null) {
/* 121 */       if (other.factionalSecondsPrecision != null) {
/* 122 */         return false;
/*     */       }
/* 124 */     } else if (!this.factionalSecondsPrecision.equals(other.factionalSecondsPrecision)) {
/* 125 */       return false;
/*     */     } 
/* 127 */     if (this.precision == null) {
/* 128 */       if (other.precision != null) {
/* 129 */         return false;
/*     */       }
/* 131 */     } else if (!this.precision.equals(other.precision)) {
/* 132 */       return false;
/*     */     } 
/* 134 */     if (this.toFactionalSecondsPrecision == null) {
/* 135 */       if (other.toFactionalSecondsPrecision != null) {
/* 136 */         return false;
/*     */       }
/* 138 */     } else if (!this.toFactionalSecondsPrecision.equals(other.toFactionalSecondsPrecision)) {
/* 139 */       return false;
/*     */     } 
/* 141 */     if (this.toType != other.toType) {
/* 142 */       return false;
/*     */     }
/* 144 */     if (this.type != other.type) {
/* 145 */       return false;
/*     */     }
/* 147 */     if (this.value == null) {
/* 148 */       if (other.value != null) {
/* 149 */         return false;
/*     */       }
/* 151 */     } else if (!this.value.equals(other.value)) {
/* 152 */       return false;
/*     */     } 
/* 154 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\expr\OracleIntervalExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */