/*     */ package com.tranboot.client.druid.sql.builder.impl;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.SQLUtils;
import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatement;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOperator;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.statement.SQLDeleteStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLExprTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLTableSource;
import com.tranboot.client.druid.sql.builder.SQLDeleteBuilder;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleDeleteStatement;

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
/*     */ 
/*     */ 
/*     */ public class SQLDeleteBuilderImpl
/*     */   implements SQLDeleteBuilder
/*     */ {
/*     */   private SQLDeleteStatement stmt;
/*     */   private String dbType;
/*     */   
/*     */   public SQLDeleteBuilderImpl(String dbType) {
/*  38 */     this.dbType = dbType;
/*     */   }
/*     */   
/*     */   public SQLDeleteBuilderImpl(String sql, String dbType) {
/*  42 */     List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
/*     */     
/*  44 */     if (stmtList.size() == 0) {
/*  45 */       throw new IllegalArgumentException("not support empty-statement :" + sql);
/*     */     }
/*     */     
/*  48 */     if (stmtList.size() > 1) {
/*  49 */       throw new IllegalArgumentException("not support multi-statement :" + sql);
/*     */     }
/*     */     
/*  52 */     SQLDeleteStatement stmt = (SQLDeleteStatement)stmtList.get(0);
/*  53 */     this.stmt = stmt;
/*  54 */     this.dbType = dbType;
/*     */   }
/*     */   
/*     */   public SQLDeleteBuilderImpl(SQLDeleteStatement stmt, String dbType) {
/*  58 */     this.stmt = stmt;
/*  59 */     this.dbType = dbType;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLDeleteBuilderImpl limit(int rowCount) {
/*  64 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLDeleteBuilderImpl limit(int rowCount, int offset) {
/*  69 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLDeleteBuilder from(String table) {
/*  74 */     return from(table, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLDeleteBuilder from(String table, String alias) {
/*  79 */     SQLDeleteStatement delete = getSQLDeleteStatement();
/*  80 */     SQLExprTableSource from = new SQLExprTableSource((SQLExpr)new SQLIdentifierExpr(table), alias);
/*  81 */     delete.setTableSource((SQLTableSource)from);
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLDeleteBuilder where(String expr) {
/*  87 */     SQLDeleteStatement delete = getSQLDeleteStatement();
/*     */     
/*  89 */     SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
/*  90 */     delete.setWhere(exprObj);
/*     */     
/*  92 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLDeleteBuilder whereAnd(String expr) {
/*  97 */     SQLDeleteStatement delete = getSQLDeleteStatement();
/*     */     
/*  99 */     SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
/* 100 */     SQLExpr newCondition = SQLUtils.buildCondition(SQLBinaryOperator.BooleanAnd, exprObj, false, delete.getWhere());
/* 101 */     delete.setWhere(newCondition);
/*     */     
/* 103 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLDeleteBuilder whereOr(String expr) {
/* 108 */     SQLDeleteStatement delete = getSQLDeleteStatement();
/*     */     
/* 110 */     SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
/* 111 */     SQLExpr newCondition = SQLUtils.buildCondition(SQLBinaryOperator.BooleanOr, exprObj, false, delete.getWhere());
/* 112 */     delete.setWhere(newCondition);
/*     */     
/* 114 */     return this;
/*     */   }
/*     */   
/*     */   public SQLDeleteStatement getSQLDeleteStatement() {
/* 118 */     if (this.stmt == null) {
/* 119 */       this.stmt = createSQLDeleteStatement();
/*     */     }
/* 121 */     return this.stmt;
/*     */   }
/*     */   
/*     */   public SQLDeleteStatement createSQLDeleteStatement() {
/* 125 */     if ("oracle".equals(this.dbType)) {
/* 126 */       return (SQLDeleteStatement)new OracleDeleteStatement();
/*     */     }
/*     */     
/* 129 */     if ("mysql".equals(this.dbType)) {
/* 130 */       return (SQLDeleteStatement)new MySqlDeleteStatement();
/*     */     }
/*     */     
/* 133 */     return new SQLDeleteStatement();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 137 */     return SQLUtils.toSQLString((SQLObject)this.stmt, this.dbType);
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\builder\impl\SQLDeleteBuilderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */