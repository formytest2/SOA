/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLObjectImpl;
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
/*    */ public class SQLAlterTableRenameColumn
/*    */   extends SQLObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/*    */   private SQLName column;
/*    */   private SQLName to;
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 33 */     if (visitor.visit(this)) {
/* 34 */       acceptChild(visitor, (SQLObject)this.column);
/* 35 */       acceptChild(visitor, (SQLObject)this.to);
/*    */     } 
/* 37 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLName getColumn() {
/* 41 */     return this.column;
/*    */   }
/*    */   
/*    */   public void setColumn(SQLName column) {
/* 45 */     this.column = column;
/*    */   }
/*    */   
/*    */   public SQLName getTo() {
/* 49 */     return this.to;
/*    */   }
/*    */   
/*    */   public void setTo(SQLName to) {
/* 53 */     this.to = to;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterTableRenameColumn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */