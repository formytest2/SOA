/*     */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLHint;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLErrorLoggingClause;
import com.tranboot.client.druid.sql.ast.statement.SQLInsertInto;
import com.tranboot.client.druid.sql.ast.statement.SQLSelect;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleSQLObject;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.OracleReturningClause;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
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
/*     */ public class OracleMultiInsertStatement
/*     */   extends OracleStatementImpl
/*     */ {
/*     */   private SQLSelect subQuery;
/*     */   private Option option;
/*     */   
/*     */   public enum Option
/*     */   {
/*  35 */     ALL, FIRST;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  40 */   private List<Entry> entries = new ArrayList<>();
/*  41 */   private List<SQLHint> hints = new ArrayList<>(1);
/*     */   
/*     */   public List<SQLHint> getHints() {
/*  44 */     return this.hints;
/*     */   }
/*     */   
/*     */   public void setHints(List<SQLHint> hints) {
/*  48 */     this.hints = hints;
/*     */   }
/*     */   
/*     */   public List<Entry> getEntries() {
/*  52 */     return this.entries;
/*     */   }
/*     */   
/*     */   public void addEntry(Entry entry) {
/*  56 */     if (entry != null) {
/*  57 */       entry.setParent((SQLObject)this);
/*     */     }
/*  59 */     this.entries.add(entry);
/*     */   }
/*     */   
/*     */   public Option getOption() {
/*  63 */     return this.option;
/*     */   }
/*     */   
/*     */   public void setOption(Option option) {
/*  67 */     this.option = option;
/*     */   }
/*     */   
/*     */   public SQLSelect getSubQuery() {
/*  71 */     return this.subQuery;
/*     */   }
/*     */   
/*     */   public void setSubQuery(SQLSelect subQuery) {
/*  75 */     this.subQuery = subQuery;
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept0(OracleASTVisitor visitor) {
/*  80 */     if (visitor.visit(this)) {
/*  81 */       acceptChild((SQLASTVisitor)visitor, this.entries);
/*  82 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.subQuery);
/*     */     } 
/*  84 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public static interface Entry
/*     */     extends OracleSQLObject {}
/*     */   
/*     */   public static class ConditionalInsertClause
/*     */     extends OracleSQLObjectImpl
/*     */     implements Entry {
/*  93 */     private List<ConditionalInsertClauseItem> items = new ArrayList<>();
/*     */     private OracleMultiInsertStatement.InsertIntoClause elseItem;
/*     */
/*     */     public OracleMultiInsertStatement.InsertIntoClause getElseItem() {
/*  97 */       return this.elseItem;
/*     */     }
/*     */
/*     */     public void setElseItem(OracleMultiInsertStatement.InsertIntoClause elseItem) {
/* 101 */       this.elseItem = elseItem;
/*     */     }
/*     */
/*     */     public List<ConditionalInsertClauseItem> getItems() {
/* 105 */       return this.items;
/*     */     }
/*     */
/*     */     public void addItem(OracleMultiInsertStatement.ConditionalInsertClauseItem item) {
/* 109 */       if (item != null) {
/* 110 */         item.setParent((SQLObject)this);
/*     */       }
/* 112 */       this.items.add(item);
/*     */     }
/*     */
/*     */
/*     */     public void accept0(OracleASTVisitor visitor) {
/* 117 */       if (visitor.visit(this)) {
/* 118 */         acceptChild((SQLASTVisitor)visitor, this.items);
/* 119 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.elseItem);
/*     */       }
/* 121 */       visitor.endVisit(this);
/*     */     }
/*     */   }
/*     */
/*     */   public static class ConditionalInsertClauseItem
/*     */     extends OracleSQLObjectImpl
/*     */   {
/*     */     private SQLExpr when;
/*     */     private OracleMultiInsertStatement.InsertIntoClause then;
/*     */
/*     */     public SQLExpr getWhen() {
/* 132 */       return this.when;
/*     */     }
/*     */
/*     */     public void setWhen(SQLExpr when) {
/* 136 */       this.when = when;
/*     */     }
/*     */
/*     */     public OracleMultiInsertStatement.InsertIntoClause getThen() {
/* 140 */       return this.then;
/*     */     }
/*     */
/*     */     public void setThen(OracleMultiInsertStatement.InsertIntoClause then) {
/* 144 */       this.then = then;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept0(OracleASTVisitor visitor) {
/* 149 */       if (visitor.visit(this)) {
/* 150 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.when);
/* 151 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.then);
/*     */       } 
/* 153 */       visitor.endVisit(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class InsertIntoClause
/*     */     extends SQLInsertInto
/*     */     implements OracleSQLObject, Entry
/*     */   {
/*     */     private OracleReturningClause returning;
/*     */     
/*     */     private SQLErrorLoggingClause errorLogging;
/*     */ 
/*     */     
/*     */     public OracleReturningClause getReturning() {
/* 168 */       return this.returning;
/*     */     }
/*     */     
/*     */     public void setReturning(OracleReturningClause returning) {
/* 172 */       this.returning = returning;
/*     */     }
/*     */     
/*     */     public SQLErrorLoggingClause getErrorLogging() {
/* 176 */       return this.errorLogging;
/*     */     }
/*     */     
/*     */     public void setErrorLogging(SQLErrorLoggingClause errorLogging) {
/* 180 */       this.errorLogging = errorLogging;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void accept0(SQLASTVisitor visitor) {
/* 185 */       accept0((OracleASTVisitor)visitor);
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept0(OracleASTVisitor visitor) {
/* 190 */       if (visitor.visit(this)) {
/* 191 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.tableSource);
/* 192 */         acceptChild((SQLASTVisitor)visitor, this.columns);
/* 193 */         acceptChild((SQLASTVisitor)visitor, this.valuesList);
/* 194 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.query);
/* 195 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.returning);
/* 196 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.errorLogging);
/*     */       } 
/*     */       
/* 199 */       visitor.endVisit(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleMultiInsertStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */