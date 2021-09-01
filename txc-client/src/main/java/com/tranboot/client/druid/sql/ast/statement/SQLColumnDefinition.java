/*     */ package com.tranboot.client.druid.sql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.*;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

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
/*     */ public class SQLColumnDefinition
/*     */   extends SQLObjectImpl
/*     */   implements SQLTableElement
/*     */ {
/*     */   protected SQLName name;
/*     */   protected SQLDataType dataType;
/*     */   protected SQLExpr defaultExpr;
/*  32 */   protected final List<SQLColumnConstraint> constraints = new ArrayList<>(0);
/*     */   
/*     */   protected SQLExpr comment;
/*     */   
/*     */   protected Boolean enable;
/*     */   
/*     */   protected boolean autoIncrement = false;
/*     */   
/*     */   protected SQLExpr onUpdate;
/*     */   
/*     */   protected SQLExpr storage;
/*     */   
/*     */   protected SQLExpr charsetExpr;
/*     */   
/*     */   protected SQLExpr asExpr;
/*     */   
/*     */   protected boolean sorted = false;
/*     */   protected boolean virtual = false;
/*     */   protected Identity identity;
/*     */   
/*     */   public Identity getIdentity() {
/*  53 */     return this.identity;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIdentity(Identity identity) {
/*  58 */     if (identity != null) {
/*  59 */       identity.setParent(this);
/*     */     }
/*  61 */     this.identity = identity;
/*     */   }
/*     */   
/*     */   public Boolean getEnable() {
/*  65 */     return this.enable;
/*     */   }
/*     */   
/*     */   public void setEnable(Boolean enable) {
/*  69 */     this.enable = enable;
/*     */   }
/*     */   
/*     */   public SQLName getName() {
/*  73 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(SQLName name) {
/*  77 */     this.name = name;
/*     */   }
/*     */   
/*     */   public SQLDataType getDataType() {
/*  81 */     return this.dataType;
/*     */   }
/*     */   
/*     */   public void setDataType(SQLDataType dataType) {
/*  85 */     this.dataType = dataType;
/*     */   }
/*     */   
/*     */   public SQLExpr getDefaultExpr() {
/*  89 */     return this.defaultExpr;
/*     */   }
/*     */   
/*     */   public void setDefaultExpr(SQLExpr defaultExpr) {
/*  93 */     if (defaultExpr != null) {
/*  94 */       defaultExpr.setParent(this);
/*     */     }
/*  96 */     this.defaultExpr = defaultExpr;
/*     */   }
/*     */   
/*     */   public List<SQLColumnConstraint> getConstraints() {
/* 100 */     return this.constraints;
/*     */   }
/*     */   
/*     */   public void addConstraint(SQLColumnConstraint constraint) {
/* 104 */     if (constraint != null) {
/* 105 */       constraint.setParent(this);
/*     */     }
/* 107 */     this.constraints.add(constraint);
/*     */   }
/*     */ 
/*     */   
/*     */   public void output(StringBuffer buf) {
/* 112 */     this.name.output(buf);
/* 113 */     buf.append(' ');
/* 114 */     this.dataType.output(buf);
/* 115 */     if (this.defaultExpr != null) {
/* 116 */       buf.append(" DEFAULT ");
/* 117 */       this.defaultExpr.output(buf);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/* 123 */     if (visitor.visit(this)) {
/* 124 */       acceptChild(visitor, (SQLObject)this.name);
/* 125 */       acceptChild(visitor, (SQLObject)this.dataType);
/* 126 */       acceptChild(visitor, (SQLObject)this.defaultExpr);
/* 127 */       acceptChild(visitor, this.constraints);
/*     */     } 
/* 129 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public SQLExpr getComment() {
/* 133 */     return this.comment;
/*     */   }
/*     */   
/*     */   public void setComment(SQLExpr comment) {
/* 137 */     this.comment = comment;
/*     */   }
/*     */   
/*     */   public boolean isVirtual() {
/* 141 */     return this.virtual;
/*     */   }
/*     */   
/*     */   public void setVirtual(boolean virtual) {
/* 145 */     this.virtual = virtual;
/*     */   }
/*     */   
/*     */   public boolean isSorted() {
/* 149 */     return this.sorted;
/*     */   }
/*     */   
/*     */   public void setSorted(boolean sorted) {
/* 153 */     this.sorted = sorted;
/*     */   }
/*     */   
/*     */   public SQLExpr getCharsetExpr() {
/* 157 */     return this.charsetExpr;
/*     */   }
/*     */   
/*     */   public void setCharsetExpr(SQLExpr charsetExpr) {
/* 161 */     if (charsetExpr != null) {
/* 162 */       charsetExpr.setParent(this);
/*     */     }
/* 164 */     this.charsetExpr = charsetExpr;
/*     */   }
/*     */   
/*     */   public SQLExpr getAsExpr() {
/* 168 */     return this.asExpr;
/*     */   }
/*     */   
/*     */   public void setAsExpr(SQLExpr asExpr) {
/* 172 */     if (this.charsetExpr != null) {
/* 173 */       this.charsetExpr.setParent(this);
/*     */     }
/* 175 */     this.asExpr = asExpr;
/*     */   }
/*     */   
/*     */   public boolean isAutoIncrement() {
/* 179 */     return this.autoIncrement;
/*     */   }
/*     */   
/*     */   public void setAutoIncrement(boolean autoIncrement) {
/* 183 */     this.autoIncrement = autoIncrement;
/*     */   }
/*     */   
/*     */   public SQLExpr getOnUpdate() {
/* 187 */     return this.onUpdate;
/*     */   }
/*     */   
/*     */   public void setOnUpdate(SQLExpr onUpdate) {
/* 191 */     this.onUpdate = onUpdate;
/*     */   }
/*     */   
/*     */   public SQLExpr getStorage() {
/* 195 */     return this.storage;
/*     */   }
/*     */   
/*     */   public void setStorage(SQLExpr storage) {
/* 199 */     this.storage = storage;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Identity
/*     */     extends SQLObjectImpl
/*     */   {
/*     */     private Integer seed;
/*     */     
/*     */     private Integer increment;
/*     */     
/*     */     private boolean notForReplication;
/*     */ 
/*     */     
/*     */     public Integer getSeed() {
/* 214 */       return this.seed;
/*     */     }
/*     */     
/*     */     public void setSeed(Integer seed) {
/* 218 */       this.seed = seed;
/*     */     }
/*     */     
/*     */     public Integer getIncrement() {
/* 222 */       return this.increment;
/*     */     }
/*     */     
/*     */     public void setIncrement(Integer increment) {
/* 226 */       this.increment = increment;
/*     */     }
/*     */     
/*     */     public boolean isNotForReplication() {
/* 230 */       return this.notForReplication;
/*     */     }
/*     */     
/*     */     public void setNotForReplication(boolean notForReplication) {
/* 234 */       this.notForReplication = notForReplication;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept0(SQLASTVisitor visitor) {
/* 239 */       visitor.visit(this);
/* 240 */       visitor.endVisit(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLColumnDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */