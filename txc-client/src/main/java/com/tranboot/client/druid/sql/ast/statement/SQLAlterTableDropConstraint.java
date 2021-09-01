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
/*    */ public class SQLAlterTableDropConstraint
/*    */   extends SQLObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/*    */   private SQLName constraintName;
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 28 */     if (visitor.visit(this)) {
/* 29 */       acceptChild(visitor, (SQLObject)this.constraintName);
/*    */     }
/* 31 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLName getConstraintName() {
/* 35 */     return this.constraintName;
/*    */   }
/*    */   
/*    */   public void setConstraintName(SQLName constraintName) {
/* 39 */     this.constraintName = constraintName;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterTableDropConstraint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */