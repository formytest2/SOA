/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
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
/*    */ 
/*    */ 
/*    */ public class MySqlAlterTableChangeColumn
/*    */   extends MySqlObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/*    */   private SQLName columnName;
/*    */   private SQLColumnDefinition newColumnDefinition;
/*    */   private boolean first;
/*    */   private SQLExpr firstColumn;
/*    */   private SQLExpr afterColumn;
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 38 */     if (visitor.visit(this)) {
/* 39 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.columnName);
/* 40 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.newColumnDefinition);
/*    */       
/* 42 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.firstColumn);
/* 43 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.afterColumn);
/*    */     } 
/*    */   }
/*    */   
/*    */   public SQLExpr getFirstColumn() {
/* 48 */     return this.firstColumn;
/*    */   }
/*    */   
/*    */   public void setFirstColumn(SQLExpr firstColumn) {
/* 52 */     this.firstColumn = firstColumn;
/*    */   }
/*    */   
/*    */   public SQLExpr getAfterColumn() {
/* 56 */     return this.afterColumn;
/*    */   }
/*    */   
/*    */   public void setAfterColumn(SQLExpr afterColumn) {
/* 60 */     this.afterColumn = afterColumn;
/*    */   }
/*    */   
/*    */   public SQLName getColumnName() {
/* 64 */     return this.columnName;
/*    */   }
/*    */   
/*    */   public void setColumnName(SQLName columnName) {
/* 68 */     this.columnName = columnName;
/*    */   }
/*    */   
/*    */   public SQLColumnDefinition getNewColumnDefinition() {
/* 72 */     return this.newColumnDefinition;
/*    */   }
/*    */   
/*    */   public void setNewColumnDefinition(SQLColumnDefinition newColumnDefinition) {
/* 76 */     this.newColumnDefinition = newColumnDefinition;
/*    */   }
/*    */   
/*    */   public boolean isFirst() {
/* 80 */     return this.first;
/*    */   }
/*    */   
/*    */   public void setFirst(boolean first) {
/* 84 */     this.first = first;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlAlterTableChangeColumn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */