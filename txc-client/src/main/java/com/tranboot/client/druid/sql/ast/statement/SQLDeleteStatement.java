/*     */ package com.tranboot.client.druid.sql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatementImpl;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
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
/*     */ public class SQLDeleteStatement
/*     */   extends SQLStatementImpl
/*     */ {
/*     */   protected SQLTableSource tableSource;
/*     */   protected SQLExpr where;
/*     */   protected SQLTableSource from;
/*     */   
/*     */   public SQLDeleteStatement() {}
/*     */   
/*     */   public SQLDeleteStatement(String dbType) {
/*  35 */     super(dbType);
/*     */   }
/*     */   
/*     */   public SQLTableSource getTableSource() {
/*  39 */     return this.tableSource;
/*     */   }
/*     */   
/*     */   public SQLExprTableSource getExprTableSource() {
/*  43 */     return (SQLExprTableSource)getTableSource();
/*     */   }
/*     */   
/*     */   public void setTableSource(SQLExpr expr) {
/*  47 */     setTableSource(new SQLExprTableSource(expr));
/*     */   }
/*     */   
/*     */   public void setTableSource(SQLTableSource tableSource) {
/*  51 */     if (tableSource != null) {
/*  52 */       tableSource.setParent((SQLObject)this);
/*     */     }
/*  54 */     this.tableSource = tableSource;
/*     */   }
/*     */   
/*     */   public SQLName getTableName() {
/*  58 */     if (this.tableSource instanceof SQLExprTableSource) {
/*  59 */       SQLExprTableSource exprTableSource = (SQLExprTableSource)this.tableSource;
/*  60 */       return (SQLName)exprTableSource.getExpr();
/*     */     } 
/*     */     
/*  63 */     if (this.tableSource instanceof SQLSubqueryTableSource) {
/*  64 */       SQLSelectQuery selectQuery = ((SQLSubqueryTableSource)this.tableSource).getSelect().getQuery();
/*  65 */       if (selectQuery instanceof SQLSelectQueryBlock) {
/*  66 */         SQLTableSource subQueryTableSource = ((SQLSelectQueryBlock)selectQuery).getFrom();
/*  67 */         if (subQueryTableSource instanceof SQLExprTableSource) {
/*  68 */           SQLExpr subQueryTableSourceExpr = ((SQLExprTableSource)subQueryTableSource).getExpr();
/*  69 */           return (SQLName)subQueryTableSourceExpr;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  74 */     return null;
/*     */   }
/*     */   
/*     */   public void setTableName(SQLName tableName) {
/*  78 */     setTableSource(new SQLExprTableSource((SQLExpr)tableName));
/*     */   }
/*     */   
/*     */   public void setTableName(String name) {
/*  82 */     setTableName((SQLName)new SQLIdentifierExpr(name));
/*     */   }
/*     */   
/*     */   public SQLExpr getWhere() {
/*  86 */     return this.where;
/*     */   }
/*     */   
/*     */   public void setWhere(SQLExpr where) {
/*  90 */     if (where != null) {
/*  91 */       where.setParent((SQLObject)this);
/*     */     }
/*  93 */     this.where = where;
/*     */   }
/*     */   
/*     */   public String getAlias() {
/*  97 */     return this.tableSource.getAlias();
/*     */   }
/*     */   
/*     */   public void setAlias(String alias) {
/* 101 */     this.tableSource.setAlias(alias);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/* 106 */     if (visitor.visit(this)) {
/* 107 */       acceptChild(visitor, this.tableSource);
/* 108 */       acceptChild(visitor, (SQLObject)this.where);
/*     */     } 
/*     */     
/* 111 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public SQLTableSource getFrom() {
/* 115 */     return this.from;
/*     */   }
/*     */   
/*     */   public void setFrom(SQLTableSource from) {
/* 119 */     if (from != null) {
/* 120 */       from.setParent((SQLObject)this);
/*     */     }
/* 122 */     this.from = from;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLDeleteStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */