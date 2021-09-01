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
/*     */ 
/*     */ 
/*     */ public class SQLMergeStatement
/*     */   extends SQLStatementImpl
/*     */ {
/*  30 */   private final List<SQLHint> hints = new ArrayList<>();
/*     */   
/*     */   private SQLTableSource into;
/*     */   private String alias;
/*     */   private SQLTableSource using;
/*     */   private SQLExpr on;
/*     */   private MergeUpdateClause updateClause;
/*     */   private MergeInsertClause insertClause;
/*     */   private SQLErrorLoggingClause errorLoggingClause;
/*     */   
/*     */   public void accept0(SQLASTVisitor visitor) {
/*  41 */     if (visitor.visit(this)) {
/*  42 */       acceptChild(visitor, this.into);
/*  43 */       acceptChild(visitor, this.using);
/*  44 */       acceptChild(visitor, (SQLObject)this.on);
/*  45 */       acceptChild(visitor, (SQLObject)this.updateClause);
/*  46 */       acceptChild(visitor, (SQLObject)this.insertClause);
/*  47 */       acceptChild(visitor, (SQLObject)this.errorLoggingClause);
/*     */     } 
/*  49 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public String getAlias() {
/*  53 */     return this.alias;
/*     */   }
/*     */   
/*     */   public void setAlias(String alias) {
/*  57 */     this.alias = alias;
/*     */   }
/*     */   
/*     */   public SQLTableSource getInto() {
/*  61 */     return this.into;
/*     */   }
/*     */   
/*     */   public void setInto(SQLName into) {
/*  65 */     setInto(new SQLExprTableSource((SQLExpr)into));
/*     */   }
/*     */   
/*     */   public void setInto(SQLTableSource into) {
/*  69 */     if (into != null) {
/*  70 */       into.setParent((SQLObject)this);
/*     */     }
/*  72 */     this.into = into;
/*     */   }
/*     */   
/*     */   public SQLTableSource getUsing() {
/*  76 */     return this.using;
/*     */   }
/*     */   
/*     */   public void setUsing(SQLTableSource using) {
/*  80 */     this.using = using;
/*     */   }
/*     */   
/*     */   public SQLExpr getOn() {
/*  84 */     return this.on;
/*     */   }
/*     */   
/*     */   public void setOn(SQLExpr on) {
/*  88 */     this.on = on;
/*     */   }
/*     */   
/*     */   public MergeUpdateClause getUpdateClause() {
/*  92 */     return this.updateClause;
/*     */   }
/*     */   
/*     */   public void setUpdateClause(MergeUpdateClause updateClause) {
/*  96 */     this.updateClause = updateClause;
/*     */   }
/*     */   
/*     */   public MergeInsertClause getInsertClause() {
/* 100 */     return this.insertClause;
/*     */   }
/*     */   
/*     */   public void setInsertClause(MergeInsertClause insertClause) {
/* 104 */     this.insertClause = insertClause;
/*     */   }
/*     */   
/*     */   public SQLErrorLoggingClause getErrorLoggingClause() {
/* 108 */     return this.errorLoggingClause;
/*     */   }
/*     */   
/*     */   public void setErrorLoggingClause(SQLErrorLoggingClause errorLoggingClause) {
/* 112 */     this.errorLoggingClause = errorLoggingClause;
/*     */   }
/*     */   
/*     */   public List<SQLHint> getHints() {
/* 116 */     return this.hints;
/*     */   }
/*     */   
/*     */   public static class MergeUpdateClause
/*     */     extends SQLObjectImpl {
/* 121 */     private List<SQLUpdateSetItem> items = new ArrayList<>();
/*     */     private SQLExpr where;
/*     */     private SQLExpr deleteWhere;
/*     */     
/*     */     public List<SQLUpdateSetItem> getItems() {
/* 126 */       return this.items;
/*     */     }
/*     */     
/*     */     public void addItem(SQLUpdateSetItem item) {
/* 130 */       if (item != null) {
/* 131 */         item.setParent((SQLObject)this);
/*     */       }
/* 133 */       this.items.add(item);
/*     */     }
/*     */     
/*     */     public SQLExpr getWhere() {
/* 137 */       return this.where;
/*     */     }
/*     */     
/*     */     public void setWhere(SQLExpr where) {
/* 141 */       this.where = where;
/*     */     }
/*     */     
/*     */     public SQLExpr getDeleteWhere() {
/* 145 */       return this.deleteWhere;
/*     */     }
/*     */     
/*     */     public void setDeleteWhere(SQLExpr deleteWhere) {
/* 149 */       this.deleteWhere = deleteWhere;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept0(SQLASTVisitor visitor) {
/* 154 */       if (visitor.visit(this)) {
/* 155 */         acceptChild(visitor, this.items);
/* 156 */         acceptChild(visitor, (SQLObject)this.where);
/* 157 */         acceptChild(visitor, (SQLObject)this.deleteWhere);
/*     */       } 
/* 159 */       visitor.endVisit(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class MergeInsertClause
/*     */     extends SQLObjectImpl
/*     */   {
/* 166 */     private List<SQLExpr> columns = new ArrayList<>();
/* 167 */     private List<SQLExpr> values = new ArrayList<>();
/*     */     
/*     */     private SQLExpr where;
/*     */     
/*     */     public void accept0(SQLASTVisitor visitor) {
/* 172 */       if (visitor.visit(this)) {
/* 173 */         acceptChild(visitor, this.columns);
/* 174 */         acceptChild(visitor, this.columns);
/* 175 */         acceptChild(visitor, this.columns);
/*     */       } 
/* 177 */       visitor.endVisit(this);
/*     */     }
/*     */     
/*     */     public List<SQLExpr> getColumns() {
/* 181 */       return this.columns;
/*     */     }
/*     */     
/*     */     public void setColumns(List<SQLExpr> columns) {
/* 185 */       this.columns = columns;
/*     */     }
/*     */     
/*     */     public List<SQLExpr> getValues() {
/* 189 */       return this.values;
/*     */     }
/*     */     
/*     */     public void setValues(List<SQLExpr> values) {
/* 193 */       this.values = values;
/*     */     }
/*     */     
/*     */     public SQLExpr getWhere() {
/* 197 */       return this.where;
/*     */     }
/*     */     
/*     */     public void setWhere(SQLExpr where) {
/* 201 */       this.where = where;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLMergeStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */