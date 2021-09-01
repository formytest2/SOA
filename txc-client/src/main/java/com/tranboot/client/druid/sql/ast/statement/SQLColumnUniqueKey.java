/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*    */ public class SQLColumnUniqueKey
/*    */   extends SQLConstraintImpl
/*    */   implements SQLColumnConstraint
/*    */ {
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 24 */     if (visitor.visit(this)) {
/* 25 */       acceptChild(visitor, (SQLObject)getName());
/*    */     }
/* 27 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLColumnUniqueKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */