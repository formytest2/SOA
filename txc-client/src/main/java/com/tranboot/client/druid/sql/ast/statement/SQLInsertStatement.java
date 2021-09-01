/*     */ package com.tranboot.client.druid.sql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLObjectImpl;
import com.tranboot.client.druid.sql.ast.SQLStatement;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SQLInsertStatement
/*     */   extends SQLInsertInto
/*     */   implements SQLStatement
/*     */ {
/*     */   private String dbType;
/*     */   protected boolean upsert = false;
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  37 */     if (visitor.visit(this)) {
/*  38 */       acceptChild(visitor, this.tableSource);
/*  39 */       acceptChild(visitor, this.columns);
/*  40 */       acceptChild(visitor, this.valuesList);
/*  41 */       acceptChild(visitor, (SQLObject)this.query);
/*     */     } 
/*     */     
/*  44 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public boolean isUpsert() {
/*  48 */     return this.upsert;
/*     */   }
/*     */   
/*     */   public void setUpsert(boolean upsert) {
/*  52 */     this.upsert = upsert;
/*     */   }
/*     */   
/*     */   public static class ValuesClause
/*     */     extends SQLObjectImpl {
/*     */     private final List<SQLExpr> values;
/*     */     
/*     */     public ValuesClause() {
/*  60 */       this(new ArrayList<>());
/*     */     }
/*     */     
/*     */     public ValuesClause(List<SQLExpr> values) {
/*  64 */       this.values = values;
/*  65 */       for (int i = 0; i < values.size(); i++) {
/*  66 */         ((SQLExpr)values.get(i)).setParent((SQLObject)this);
/*     */       }
/*     */     }
/*     */     
/*     */     public void addValue(SQLExpr value) {
/*  71 */       value.setParent((SQLObject)this);
/*  72 */       this.values.add(value);
/*     */     }
/*     */     
/*     */     public List<SQLExpr> getValues() {
/*  76 */       return this.values;
/*     */     }
/*     */     
/*     */     public void output(StringBuffer buf) {
/*  80 */       buf.append(" VALUES (");
/*  81 */       for (int i = 0, size = this.values.size(); i < size; i++) {
/*  82 */         if (i != 0) {
/*  83 */           buf.append(", ");
/*     */         }
/*  85 */         ((SQLExpr)this.values.get(i)).output(buf);
/*     */       } 
/*  87 */       buf.append(")");
/*     */     }
/*     */ 
/*     */     
/*     */     protected void accept0(SQLASTVisitor visitor) {
/*  92 */       if (visitor.visit(this)) {
/*  93 */         acceptChild(visitor, this.values);
/*     */       }
/*     */       
/*  96 */       visitor.endVisit(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDbType() {
/* 102 */     return this.dbType;
/*     */   }
/*     */   
/*     */   public void setDbType(String dbType) {
/* 106 */     this.dbType = dbType;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLInsertStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */