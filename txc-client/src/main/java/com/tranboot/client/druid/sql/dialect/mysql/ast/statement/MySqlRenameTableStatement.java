/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
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
/*    */ public class MySqlRenameTableStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/* 27 */   private List<Item> items = new ArrayList<>(2);
/*    */   
/*    */   public List<Item> getItems() {
/* 30 */     return this.items;
/*    */   }
/*    */   
/*    */   public void addItem(Item item) {
/* 34 */     if (item != null) {
/* 35 */       item.setParent((SQLObject)this);
/*    */     }
/* 37 */     this.items.add(item);
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 41 */     if (visitor.visit(this)) {
/* 42 */       acceptChild((SQLASTVisitor)visitor, this.items);
/*    */     }
/* 44 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public static class Item
/*    */     extends MySqlObjectImpl {
/*    */     private SQLExpr name;
/*    */     private SQLExpr to;
/*    */     
/*    */     public SQLExpr getName() {
/* 53 */       return this.name;
/*    */     }
/*    */     
/*    */     public void setName(SQLExpr name) {
/* 57 */       this.name = name;
/*    */     }
/*    */     
/*    */     public SQLExpr getTo() {
/* 61 */       return this.to;
/*    */     }
/*    */     
/*    */     public void setTo(SQLExpr to) {
/* 65 */       this.to = to;
/*    */     }
/*    */ 
/*    */     
/*    */     public void accept0(MySqlASTVisitor visitor) {
/* 70 */       if (visitor.visit(this)) {
/* 71 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.name);
/* 72 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.to);
/*    */       } 
/* 74 */       visitor.endVisit(this);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlRenameTableStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */