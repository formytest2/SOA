/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
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
/*    */ public class OracleExceptionStatement
/*    */   extends OracleStatementImpl
/*    */   implements OracleStatement
/*    */ {
/* 28 */   private List<Item> items = new ArrayList<>();
/*    */   
/*    */   public List<Item> getItems() {
/* 31 */     return this.items;
/*    */   }
/*    */   
/*    */   public void addItem(Item item) {
/* 35 */     if (item != null) {
/* 36 */       item.setParent((SQLObject)this);
/*    */     }
/*    */     
/* 39 */     this.items.add(item);
/*    */   }
/*    */   
/*    */   public static class Item
/*    */     extends OracleSQLObjectImpl {
/*    */     private SQLExpr when;
/* 45 */     private List<SQLStatement> statements = new ArrayList<>();
/*    */     
/*    */     public SQLExpr getWhen() {
/* 48 */       return this.when;
/*    */     }
/*    */     
/*    */     public void setWhen(SQLExpr when) {
/* 52 */       this.when = when;
/*    */     }
/*    */     
/*    */     public List<SQLStatement> getStatements() {
/* 56 */       return this.statements;
/*    */     }
/*    */     
/*    */     public void setStatements(List<SQLStatement> statements) {
/* 60 */       this.statements = statements;
/*    */     }
/*    */ 
/*    */     
/*    */     public void accept0(OracleASTVisitor visitor) {
/* 65 */       if (visitor.visit(this)) {
/* 66 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.when);
/* 67 */         acceptChild((SQLASTVisitor)visitor, this.statements);
/*    */       } 
/* 69 */       visitor.endVisit(this);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 76 */     if (visitor.visit(this)) {
/* 77 */       acceptChild((SQLASTVisitor)visitor, this.items);
/*    */     }
/* 79 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleExceptionStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */