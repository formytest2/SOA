/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableItem;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnDefinition;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

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
/*    */ public class MySqlAlterTableModifyColumn
/*    */   extends MySqlObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/*    */   private SQLColumnDefinition newColumnDefinition;
/*    */   private boolean first;
/*    */   private SQLExpr firstColumn;
/*    */   private SQLExpr afterColumn;
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 35 */     if (visitor.visit(this)) {
/* 36 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.newColumnDefinition);
/*    */       
/* 38 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.firstColumn);
/* 39 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.afterColumn);
/*    */     } 
/*    */   }
/*    */   
/*    */   public SQLExpr getFirstColumn() {
/* 44 */     return this.firstColumn;
/*    */   }
/*    */   
/*    */   public void setFirstColumn(SQLExpr firstColumn) {
/* 48 */     this.firstColumn = firstColumn;
/*    */   }
/*    */   
/*    */   public SQLExpr getAfterColumn() {
/* 52 */     return this.afterColumn;
/*    */   }
/*    */   
/*    */   public void setAfterColumn(SQLExpr afterColumn) {
/* 56 */     this.afterColumn = afterColumn;
/*    */   }
/*    */   
/*    */   public SQLColumnDefinition getNewColumnDefinition() {
/* 60 */     return this.newColumnDefinition;
/*    */   }
/*    */   
/*    */   public void setNewColumnDefinition(SQLColumnDefinition newColumnDefinition) {
/* 64 */     this.newColumnDefinition = newColumnDefinition;
/*    */   }
/*    */   
/*    */   public boolean isFirst() {
/* 68 */     return this.first;
/*    */   }
/*    */   
/*    */   public void setFirst(boolean first) {
/* 72 */     this.first = first;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlAlterTableModifyColumn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */