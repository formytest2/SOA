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
/*    */ public class SQLAlterTableDropKey
/*    */   extends SQLObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/*    */   private SQLName keyName;
/*    */   
/*    */   public SQLName getKeyName() {
/* 27 */     return this.keyName;
/*    */   }
/*    */   
/*    */   public void setKeyName(SQLName keyName) {
/* 31 */     this.keyName = keyName;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 36 */     if (visitor.visit(this)) {
/* 37 */       acceptChild(visitor, (SQLObject)this.keyName);
/*    */     }
/* 39 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterTableDropKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */