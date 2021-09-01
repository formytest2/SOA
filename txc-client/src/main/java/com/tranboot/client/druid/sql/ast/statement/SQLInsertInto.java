/*     */ package com.tranboot.client.druid.sql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLObjectImpl;

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
/*     */ public abstract class SQLInsertInto
/*     */   extends SQLObjectImpl
/*     */ {
/*     */   protected SQLExprTableSource tableSource;
/*  30 */   protected final List<SQLExpr> columns = new ArrayList<>();
/*     */   
/*     */   protected SQLSelect query;
/*  33 */   protected List<SQLInsertStatement.ValuesClause> valuesList = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAlias() {
/*  40 */     return this.tableSource.getAlias();
/*     */   }
/*     */   
/*     */   public void setAlias(String alias) {
/*  44 */     this.tableSource.setAlias(alias);
/*     */   }
/*     */   
/*     */   public SQLExprTableSource getTableSource() {
/*  48 */     return this.tableSource;
/*     */   }
/*     */   
/*     */   public void setTableSource(SQLExprTableSource tableSource) {
/*  52 */     if (tableSource != null) {
/*  53 */       tableSource.setParent((SQLObject)this);
/*     */     }
/*  55 */     this.tableSource = tableSource;
/*     */   }
/*     */   
/*     */   public SQLName getTableName() {
/*  59 */     return (SQLName)this.tableSource.getExpr();
/*     */   }
/*     */   
/*     */   public void setTableName(SQLName tableName) {
/*  63 */     setTableSource(new SQLExprTableSource((SQLExpr)tableName));
/*     */   }
/*     */   
/*     */   public void setTableSource(SQLName tableName) {
/*  67 */     setTableSource(new SQLExprTableSource((SQLExpr)tableName));
/*     */   }
/*     */   
/*     */   public SQLSelect getQuery() {
/*  71 */     return this.query;
/*     */   }
/*     */   
/*     */   public void setQuery(SQLSelect query) {
/*  75 */     this.query = query;
/*     */   }
/*     */   
/*     */   public List<SQLExpr> getColumns() {
/*  79 */     return this.columns;
/*     */   }
/*     */   
/*     */   public void addColumn(SQLExpr column) {
/*  83 */     if (column != null) {
/*  84 */       column.setParent((SQLObject)this);
/*     */     }
/*  86 */     this.columns.add(column);
/*     */   }
/*     */   
/*     */   public SQLInsertStatement.ValuesClause getValues() {
/*  90 */     if (this.valuesList.size() == 0) {
/*  91 */       return null;
/*     */     }
/*  93 */     return this.valuesList.get(0);
/*     */   }
/*     */   
/*     */   public void setValues(SQLInsertStatement.ValuesClause values) {
/*  97 */     if (this.valuesList.size() == 0) {
/*  98 */       this.valuesList.add(values);
/*     */     } else {
/* 100 */       this.valuesList.set(0, values);
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<SQLInsertStatement.ValuesClause> getValuesList() {
/* 105 */     return this.valuesList;
/*     */   }
/*     */   
/*     */   public void setValuesList(List<SQLInsertStatement.ValuesClause> valuesList) {
/* 109 */     this.valuesList = valuesList;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLInsertInto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */