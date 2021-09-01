/*     */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.expr.SQLQueryExpr;
import com.tranboot.client.druid.sql.ast.statement.SQLExprTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLInsertStatement;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
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
/*     */ public class MySqlReplaceStatement
/*     */   extends MySqlStatementImpl
/*     */ {
/*     */   private boolean lowPriority = false;
/*     */   private boolean delayed = false;
/*     */   private SQLExprTableSource tableSource;
/*  34 */   private final List<SQLExpr> columns = new ArrayList<>();
/*  35 */   private List<SQLInsertStatement.ValuesClause> valuesList = new ArrayList<>();
/*     */   private SQLQueryExpr query;
/*     */   
/*     */   public SQLName getTableName() {
/*  39 */     if (this.tableSource == null) {
/*  40 */       return null;
/*     */     }
/*     */     
/*  43 */     return (SQLName)this.tableSource.getExpr();
/*     */   }
/*     */   
/*     */   public void setTableName(SQLName tableName) {
/*  47 */     setTableSource(new SQLExprTableSource((SQLExpr)tableName));
/*     */   }
/*     */   
/*     */   public SQLExprTableSource getTableSource() {
/*  51 */     return this.tableSource;
/*     */   }
/*     */   
/*     */   public void setTableSource(SQLExprTableSource tableSource) {
/*  55 */     if (tableSource != null) {
/*  56 */       tableSource.setParent((SQLObject)this);
/*     */     }
/*  58 */     this.tableSource = tableSource;
/*     */   }
/*     */   
/*     */   public List<SQLExpr> getColumns() {
/*  62 */     return this.columns;
/*     */   }
/*     */   
/*     */   public void addColumn(SQLExpr column) {
/*  66 */     if (column != null) {
/*  67 */       column.setParent((SQLObject)this);
/*     */     }
/*  69 */     this.columns.add(column);
/*     */   }
/*     */   
/*     */   public boolean isLowPriority() {
/*  73 */     return this.lowPriority;
/*     */   }
/*     */   
/*     */   public void setLowPriority(boolean lowPriority) {
/*  77 */     this.lowPriority = lowPriority;
/*     */   }
/*     */   
/*     */   public boolean isDelayed() {
/*  81 */     return this.delayed;
/*     */   }
/*     */   
/*     */   public void setDelayed(boolean delayed) {
/*  85 */     this.delayed = delayed;
/*     */   }
/*     */   
/*     */   public SQLQueryExpr getQuery() {
/*  89 */     return this.query;
/*     */   }
/*     */   
/*     */   public void setQuery(SQLQueryExpr query) {
/*  93 */     query.setParent((SQLObject)this);
/*  94 */     this.query = query;
/*     */   }
/*     */   
/*     */   public List<SQLInsertStatement.ValuesClause> getValuesList() {
/*  98 */     return this.valuesList;
/*     */   }
/*     */   
/*     */   public void accept0(MySqlASTVisitor visitor) {
/* 102 */     if (visitor.visit(this)) {
/* 103 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.tableSource);
/* 104 */       acceptChild((SQLASTVisitor)visitor, this.columns);
/* 105 */       acceptChild((SQLASTVisitor)visitor, this.valuesList);
/* 106 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.query);
/*     */     } 
/* 108 */     visitor.endVisit(this);
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlReplaceStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */