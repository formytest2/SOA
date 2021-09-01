/*     */ package com.tranboot.client.druid.sql.builder.impl;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.SQLUtils;
import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatement;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOperator;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.statement.SQLExprTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateSetItem;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateStatement;
import com.tranboot.client.druid.sql.builder.SQLUpdateBuilder;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleUpdateStatement;

import java.util.List;
import java.util.Map;

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
/*     */ 
/*     */ public class SQLUpdateBuilderImpl
/*     */   extends SQLBuilderImpl
/*     */   implements SQLUpdateBuilder
/*     */ {
/*     */   private SQLUpdateStatement stmt;
/*     */   private String dbType;
/*     */   
/*     */   public SQLUpdateBuilderImpl(String dbType) {
/*  40 */     this.dbType = dbType;
/*     */   }
/*     */   
/*     */   public SQLUpdateBuilderImpl(String sql, String dbType) {
/*  44 */     List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
/*     */     
/*  46 */     if (stmtList.size() == 0) {
/*  47 */       throw new IllegalArgumentException("not support empty-statement :" + sql);
/*     */     }
/*     */     
/*  50 */     if (stmtList.size() > 1) {
/*  51 */       throw new IllegalArgumentException("not support multi-statement :" + sql);
/*     */     }
/*     */     
/*  54 */     SQLUpdateStatement stmt = (SQLUpdateStatement)stmtList.get(0);
/*  55 */     this.stmt = stmt;
/*  56 */     this.dbType = dbType;
/*     */   }
/*     */   
/*     */   public SQLUpdateBuilderImpl(SQLUpdateStatement stmt, String dbType) {
/*  60 */     this.stmt = stmt;
/*  61 */     this.dbType = dbType;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLUpdateBuilderImpl limit(int rowCount) {
/*  66 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLUpdateBuilderImpl limit(int rowCount, int offset) {
/*  71 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLUpdateBuilderImpl from(String table) {
/*  76 */     return from(table, (String)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLUpdateBuilderImpl from(String table, String alias) {
/*  81 */     SQLUpdateStatement update = getSQLUpdateStatement();
/*  82 */     SQLExprTableSource from = new SQLExprTableSource((SQLExpr)new SQLIdentifierExpr(table), alias);
/*  83 */     update.setTableSource((SQLTableSource)from);
/*  84 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLUpdateBuilderImpl where(String expr) {
/*  89 */     SQLUpdateStatement update = getSQLUpdateStatement();
/*     */     
/*  91 */     SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
/*  92 */     update.setWhere(exprObj);
/*     */     
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLUpdateBuilderImpl whereAnd(String expr) {
/*  99 */     SQLUpdateStatement update = getSQLUpdateStatement();
/*     */     
/* 101 */     SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
/* 102 */     SQLExpr newCondition = SQLUtils.buildCondition(SQLBinaryOperator.BooleanAnd, exprObj, false, update.getWhere());
/* 103 */     update.setWhere(newCondition);
/*     */     
/* 105 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLUpdateBuilderImpl whereOr(String expr) {
/* 110 */     SQLUpdateStatement update = getSQLUpdateStatement();
/*     */     
/* 112 */     SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
/* 113 */     SQLExpr newCondition = SQLUtils.buildCondition(SQLBinaryOperator.BooleanOr, exprObj, false, update.getWhere());
/* 114 */     update.setWhere(newCondition);
/*     */     
/* 116 */     return this;
/*     */   }
/*     */   
/*     */   public SQLUpdateBuilderImpl set(String... items) {
/* 120 */     SQLUpdateStatement update = getSQLUpdateStatement();
/* 121 */     for (String item : items) {
/* 122 */       SQLUpdateSetItem updateSetItem = SQLUtils.toUpdateSetItem(item, this.dbType);
/* 123 */       update.addItem(updateSetItem);
/*     */     } 
/*     */     
/* 126 */     return this;
/*     */   }
/*     */   
/*     */   public SQLUpdateBuilderImpl setValue(Map<String, Object> values) {
/* 130 */     for (Map.Entry<String, Object> entry : values.entrySet()) {
/* 131 */       setValue(entry.getKey(), entry.getValue());
/*     */     }
/*     */     
/* 134 */     return this;
/*     */   }
/*     */   
/*     */   public SQLUpdateBuilderImpl setValue(String column, Object value) {
/* 138 */     SQLUpdateStatement update = getSQLUpdateStatement();
/*     */     
/* 140 */     SQLExpr columnExpr = SQLUtils.toSQLExpr(column, this.dbType);
/* 141 */     SQLExpr valueExpr = toSQLExpr(value, this.dbType);
/*     */     
/* 143 */     SQLUpdateSetItem item = new SQLUpdateSetItem();
/* 144 */     item.setColumn(columnExpr);
/* 145 */     item.setValue(valueExpr);
/* 146 */     update.addItem(item);
/*     */     
/* 148 */     return this;
/*     */   }
/*     */   
/*     */   public SQLUpdateStatement getSQLUpdateStatement() {
/* 152 */     if (this.stmt == null) {
/* 153 */       this.stmt = createSQLUpdateStatement();
/*     */     }
/* 155 */     return this.stmt;
/*     */   }
/*     */   
/*     */   public SQLUpdateStatement createSQLUpdateStatement() {
/* 159 */     if ("mysql".equals(this.dbType)) {
/* 160 */       return (SQLUpdateStatement)new MySqlUpdateStatement();
/*     */     }
/*     */     
/* 163 */     if ("oracle".equals(this.dbType)) {
/* 164 */       return (SQLUpdateStatement)new OracleUpdateStatement();
/*     */     }
/*     */     
/* 167 */     return new SQLUpdateStatement();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 171 */     return SQLUtils.toSQLString((SQLObject)this.stmt, this.dbType);
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\builder\impl\SQLUpdateBuilderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */