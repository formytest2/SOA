/*     */ package com.tranboot.client.druid.sql.builder.impl;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.SQLUtils;
import com.tranboot.client.druid.sql.ast.*;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOperator;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLIntegerExpr;
import com.tranboot.client.druid.sql.ast.statement.*;
import com.tranboot.client.druid.sql.builder.SQLSelectBuilder;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelect;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;

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
/*     */ public class SQLSelectBuilderImpl
/*     */   implements SQLSelectBuilder
/*     */ {
/*     */   private SQLSelectStatement stmt;
/*     */   private String dbType;
/*     */   
/*     */   public SQLSelectBuilderImpl(String dbType) {
/*  48 */     this(new SQLSelectStatement(), dbType);
/*     */   }
/*     */   
/*     */   public SQLSelectBuilderImpl(String sql, String dbType) {
/*  52 */     List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
/*     */     
/*  54 */     if (stmtList.size() == 0) {
/*  55 */       throw new IllegalArgumentException("not support empty-statement :" + sql);
/*     */     }
/*     */     
/*  58 */     if (stmtList.size() > 1) {
/*  59 */       throw new IllegalArgumentException("not support multi-statement :" + sql);
/*     */     }
/*     */     
/*  62 */     SQLSelectStatement stmt = (SQLSelectStatement)stmtList.get(0);
/*  63 */     this.stmt = stmt;
/*  64 */     this.dbType = dbType;
/*     */   }
/*     */   
/*     */   public SQLSelectBuilderImpl(SQLSelectStatement stmt, String dbType) {
/*  68 */     this.stmt = stmt;
/*  69 */     this.dbType = dbType;
/*     */   }
/*     */   
/*     */   public SQLSelect getSQLSelect() {
/*  73 */     if (this.stmt.getSelect() == null) {
/*  74 */       this.stmt.setSelect(createSelect());
/*     */     }
/*  76 */     return this.stmt.getSelect();
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLSelectStatement getSQLSelectStatement() {
/*  81 */     return this.stmt;
/*     */   }
/*     */   
/*     */   public SQLSelectBuilderImpl select(String... columns) {
/*  85 */     SQLSelectQueryBlock queryBlock = getQueryBlock();
/*     */     
/*  87 */     for (String column : columns) {
/*  88 */       SQLSelectItem selectItem = SQLUtils.toSelectItem(column, this.dbType);
/*  89 */       queryBlock.addSelectItem(selectItem);
/*     */     } 
/*     */     
/*  92 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLSelectBuilderImpl selectWithAlias(String column, String alias) {
/*  97 */     SQLSelectQueryBlock queryBlock = getQueryBlock();
/*     */     
/*  99 */     SQLExpr columnExpr = SQLUtils.toSQLExpr(column, this.dbType);
/* 100 */     SQLSelectItem selectItem = new SQLSelectItem(columnExpr, alias);
/* 101 */     queryBlock.addSelectItem(selectItem);
/*     */     
/* 103 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLSelectBuilderImpl from(String table) {
/* 108 */     return from(table, (String)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLSelectBuilderImpl from(String table, String alias) {
/* 113 */     SQLSelectQueryBlock queryBlock = getQueryBlock();
/* 114 */     SQLExprTableSource from = new SQLExprTableSource((SQLExpr)new SQLIdentifierExpr(table), alias);
/* 115 */     queryBlock.setFrom((SQLTableSource)from);
/*     */     
/* 117 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLSelectBuilderImpl orderBy(String... columns) {
/* 122 */     SQLSelect select = getSQLSelect();
/*     */     
/* 124 */     SQLOrderBy orderBy = select.getOrderBy();
/* 125 */     if (orderBy == null) {
/* 126 */       orderBy = createOrderBy();
/* 127 */       select.setOrderBy(orderBy);
/*     */     } 
/*     */     
/* 130 */     for (String column : columns) {
/* 131 */       SQLSelectOrderByItem orderByItem = SQLUtils.toOrderByItem(column, this.dbType);
/* 132 */       orderBy.addItem(orderByItem);
/*     */     } 
/*     */     
/* 135 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLSelectBuilderImpl groupBy(String expr) {
/* 140 */     SQLSelectQueryBlock queryBlock = getQueryBlock();
/*     */     
/* 142 */     SQLSelectGroupByClause groupBy = queryBlock.getGroupBy();
/* 143 */     if (groupBy == null) {
/* 144 */       groupBy = createGroupBy();
/* 145 */       queryBlock.setGroupBy(groupBy);
/*     */     } 
/*     */     
/* 148 */     SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
/* 149 */     groupBy.addItem(exprObj);
/*     */     
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLSelectBuilderImpl having(String expr) {
/* 156 */     SQLSelectQueryBlock queryBlock = getQueryBlock();
/*     */     
/* 158 */     SQLSelectGroupByClause groupBy = queryBlock.getGroupBy();
/* 159 */     if (groupBy == null) {
/* 160 */       groupBy = createGroupBy();
/* 161 */       queryBlock.setGroupBy(groupBy);
/*     */     } 
/*     */     
/* 164 */     SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
/* 165 */     groupBy.setHaving(exprObj);
/*     */     
/* 167 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLSelectBuilderImpl into(String expr) {
/* 172 */     SQLSelectQueryBlock queryBlock = getQueryBlock();
/*     */     
/* 174 */     SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
/* 175 */     queryBlock.setInto(exprObj);
/*     */     
/* 177 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLSelectBuilderImpl where(String expr) {
/* 182 */     SQLSelectQueryBlock queryBlock = getQueryBlock();
/*     */     
/* 184 */     SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
/* 185 */     queryBlock.setWhere(exprObj);
/*     */     
/* 187 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLSelectBuilderImpl whereAnd(String expr) {
/* 192 */     SQLSelectQueryBlock queryBlock = getQueryBlock();
/*     */     
/* 194 */     SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
/* 195 */     SQLExpr newCondition = SQLUtils.buildCondition(SQLBinaryOperator.BooleanAnd, exprObj, false, queryBlock
/* 196 */         .getWhere());
/* 197 */     queryBlock.setWhere(newCondition);
/*     */     
/* 199 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLSelectBuilderImpl whereOr(String expr) {
/* 204 */     SQLSelectQueryBlock queryBlock = getQueryBlock();
/*     */     
/* 206 */     SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
/* 207 */     SQLExpr newCondition = SQLUtils.buildCondition(SQLBinaryOperator.BooleanOr, exprObj, false, queryBlock
/* 208 */         .getWhere());
/* 209 */     queryBlock.setWhere(newCondition);
/*     */     
/* 211 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLSelectBuilderImpl limit(int rowCount) {
/* 216 */     return limit(rowCount, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLSelectBuilderImpl limit(int rowCount, int offset) {
/* 221 */     SQLSelectQueryBlock queryBlock = getQueryBlock();
/*     */     
/* 223 */     if (queryBlock instanceof MySqlSelectQueryBlock) {
/* 224 */       MySqlSelectQueryBlock mySqlQueryBlock = (MySqlSelectQueryBlock)queryBlock;
/*     */       
/* 226 */       SQLLimit limit = new SQLLimit();
/* 227 */       limit.setRowCount((SQLExpr)new SQLIntegerExpr(Integer.valueOf(rowCount)));
/* 228 */       if (offset > 0) {
/* 229 */         limit.setOffset((SQLExpr)new SQLIntegerExpr(Integer.valueOf(offset)));
/*     */       }
/*     */       
/* 232 */       mySqlQueryBlock.setLimit(limit);
/*     */       
/* 234 */       return this;
/*     */     } 
/*     */     
/* 237 */     if (queryBlock instanceof OracleSelectQueryBlock) {
/* 238 */       OracleSelectQueryBlock oracleQueryBlock = (OracleSelectQueryBlock)queryBlock;
/* 239 */       if (offset <= 0) {
/* 240 */         SQLIntegerExpr sQLIntegerExpr = new SQLIntegerExpr(Integer.valueOf(rowCount));
/* 241 */         SQLExpr newCondition = SQLUtils.buildCondition(SQLBinaryOperator.BooleanAnd, (SQLExpr)sQLIntegerExpr, false, oracleQueryBlock
/* 242 */             .getWhere());
/* 243 */         queryBlock.setWhere(newCondition);
/*     */       } else {
/* 245 */         throw new UnsupportedOperationException("not support offset");
/*     */       } 
/*     */       
/* 248 */       return this;
/*     */     } 
/*     */     
/* 251 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected SQLSelectQueryBlock getQueryBlock() {
/* 255 */     SQLSelect select = getSQLSelect();
/* 256 */     SQLSelectQuery query = select.getQuery();
/* 257 */     if (query == null) {
/* 258 */       query = createSelectQueryBlock();
/* 259 */       select.setQuery(query);
/*     */     } 
/*     */     
/* 262 */     if (!(query instanceof SQLSelectQueryBlock)) {
/* 263 */       throw new IllegalStateException("not support from, class : " + query.getClass().getName());
/*     */     }
/*     */     
/* 266 */     SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;
/* 267 */     return queryBlock;
/*     */   }
/*     */   
/*     */   protected SQLSelect createSelect() {
/* 271 */     if ("oracle".equals(this.dbType)) {
/* 272 */       return (SQLSelect)new OracleSelect();
/*     */     }
/*     */     
/* 275 */     return new SQLSelect();
/*     */   }
/*     */   
/*     */   protected SQLSelectQuery createSelectQueryBlock() {
/* 279 */     if ("mysql".equals(this.dbType)) {
/* 280 */       return (SQLSelectQuery)new MySqlSelectQueryBlock();
/*     */     }
/*     */     
/* 283 */     if ("oracle".equals(this.dbType)) {
/* 284 */       return (SQLSelectQuery)new OracleSelectQueryBlock();
/*     */     }
/*     */     
/* 287 */     return (SQLSelectQuery)new SQLSelectQueryBlock();
/*     */   }
/*     */   
/*     */   protected SQLOrderBy createOrderBy() {
/* 291 */     return new SQLOrderBy();
/*     */   }
/*     */   
/*     */   protected SQLSelectGroupByClause createGroupBy() {
/* 295 */     return new SQLSelectGroupByClause();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 299 */     return SQLUtils.toSQLString((SQLObject)this.stmt, this.dbType);
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\builder\impl\SQLSelectBuilderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */