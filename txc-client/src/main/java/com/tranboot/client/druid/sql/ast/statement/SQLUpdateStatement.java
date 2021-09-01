/*     */ package com.tranboot.client.druid.sql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatementImpl;
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
/*     */ public class SQLUpdateStatement
/*     */   extends SQLStatementImpl
/*     */ {
/*  28 */   protected final List<SQLUpdateSetItem> items = new ArrayList<>();
/*     */   
/*     */   protected SQLExpr where;
/*     */   
/*     */   protected SQLTableSource from;
/*     */   
/*     */   protected SQLTableSource tableSource;
/*     */   protected List<SQLExpr> returning;
/*     */   
/*     */   public SQLUpdateStatement() {}
/*     */   
/*     */   public SQLUpdateStatement(String dbType) {
/*  40 */     super(dbType);
/*     */   }
/*     */   
/*     */   public SQLTableSource getTableSource() {
/*  44 */     return this.tableSource;
/*     */   }
/*     */   
/*     */   public void setTableSource(SQLExpr expr) {
/*  48 */     setTableSource(new SQLExprTableSource(expr));
/*     */   }
/*     */   
/*     */   public void setTableSource(SQLTableSource tableSource) {
/*  52 */     if (tableSource != null) {
/*  53 */       tableSource.setParent((SQLObject)this);
/*     */     }
/*  55 */     this.tableSource = tableSource;
/*     */   }
/*     */   
/*     */   public SQLName getTableName() {
/*  59 */     if (this.tableSource instanceof SQLExprTableSource) {
/*  60 */       SQLExprTableSource exprTableSource = (SQLExprTableSource)this.tableSource;
/*  61 */       return (SQLName)exprTableSource.getExpr();
/*     */     } 
/*  63 */     return null;
/*     */   }
/*     */   
/*     */   public SQLExpr getWhere() {
/*  67 */     return this.where;
/*     */   }
/*     */   
/*     */   public void setWhere(SQLExpr where) {
/*  71 */     if (where != null) {
/*  72 */       where.setParent((SQLObject)this);
/*     */     }
/*  74 */     this.where = where;
/*     */   }
/*     */   
/*     */   public List<SQLUpdateSetItem> getItems() {
/*  78 */     return this.items;
/*     */   }
/*     */   
/*     */   public void addItem(SQLUpdateSetItem item) {
/*  82 */     this.items.add(item);
/*  83 */     item.setParent((SQLObject)this);
/*     */   }
/*     */   
/*     */   public List<SQLExpr> getReturning() {
/*  87 */     if (this.returning == null) {
/*  88 */       this.returning = new ArrayList<>(2);
/*     */     }
/*     */     
/*  91 */     return this.returning;
/*     */   }
/*     */   
/*     */   public SQLTableSource getFrom() {
/*  95 */     return this.from;
/*     */   }
/*     */   
/*     */   public void setFrom(SQLTableSource from) {
/*  99 */     if (from != null) {
/* 100 */       from.setParent((SQLObject)this);
/*     */     }
/* 102 */     this.from = from;
/*     */   }
/*     */ 
/*     */   
/*     */   public void output(StringBuffer buf) {
/* 107 */     buf.append("UPDATE ");
/*     */     
/* 109 */     this.tableSource.output(buf);
/*     */     
/* 111 */     buf.append(" SET ");
/* 112 */     for (int i = 0, size = this.items.size(); i < size; i++) {
/* 113 */       if (i != 0) {
/* 114 */         buf.append(", ");
/*     */       }
/* 116 */       ((SQLUpdateSetItem)this.items.get(i)).output(buf);
/*     */     } 
/*     */     
/* 119 */     if (this.where != null) {
/* 120 */       buf.append(" WHERE ");
/* 121 */       this.where.output(buf);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/* 127 */     if (visitor.visit(this)) {
/* 128 */       acceptChild(visitor, this.tableSource);
/* 129 */       acceptChild(visitor, this.items);
/* 130 */       acceptChild(visitor, (SQLObject)this.where);
/*     */     } 
/* 132 */     visitor.endVisit(this);
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLUpdateStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */