/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLObjectImpl;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

import java.util.ArrayList;
import java.util.List;

/*    */
/*    */
/*    */
/*    */
/*    */
/*    */

/*    */
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SQLWithSubqueryClause
/*    */   extends SQLObjectImpl
/*    */ {
/*    */   private Boolean recursive;
/* 29 */   private final List<Entry> entries = new ArrayList<>();
/*    */   
/*    */   public List<Entry> getEntries() {
/* 32 */     return this.entries;
/*    */   }
/*    */   
/*    */   public void addEntry(Entry entrie) {
/* 36 */     if (entrie != null) {
/* 37 */       entrie.setParent((SQLObject)this);
/*    */     }
/* 39 */     this.entries.add(entrie);
/*    */   }
/*    */   
/*    */   public Boolean getRecursive() {
/* 43 */     return this.recursive;
/*    */   }
/*    */   
/*    */   public void setRecursive(Boolean recursive) {
/* 47 */     this.recursive = recursive;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 52 */     if (visitor.visit(this)) {
/* 53 */       acceptChild(visitor, this.entries);
/*    */     }
/* 55 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public static class Entry
/*    */     extends SQLObjectImpl {
/*    */     protected SQLIdentifierExpr name;
/* 61 */     protected final List<SQLName> columns = new ArrayList<>();
/*    */     
/*    */     protected SQLSelect subQuery;
/*    */     
/*    */     protected void accept0(SQLASTVisitor visitor) {
/* 66 */       if (visitor.visit(this)) {
/* 67 */         acceptChild(visitor, (SQLObject)this.name);
/* 68 */         acceptChild(visitor, this.columns);
/* 69 */         acceptChild(visitor, (SQLObject)this.subQuery);
/*    */       } 
/* 71 */       visitor.endVisit(this);
/*    */     }
/*    */     
/*    */     public SQLIdentifierExpr getName() {
/* 75 */       return this.name;
/*    */     }
/*    */     
/*    */     public void setName(SQLIdentifierExpr name) {
/* 79 */       this.name = name;
/*    */     }
/*    */     
/*    */     public SQLSelect getSubQuery() {
/* 83 */       return this.subQuery;
/*    */     }
/*    */     
/*    */     public void setSubQuery(SQLSelect subQuery) {
/* 87 */       this.subQuery = subQuery;
/*    */     }
/*    */     
/*    */     public List<SQLName> getColumns() {
/* 91 */       return this.columns;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLWithSubqueryClause.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */