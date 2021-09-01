/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

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
/*    */ public class SQLAlterTableDisableKeys
/*    */   extends SQLObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 25 */     visitor.visit(this);
/* 26 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterTableDisableKeys.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */