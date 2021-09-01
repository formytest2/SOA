/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLObjectImpl;
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
/*    */ public class SQLAlterTableEnableLifecycle
/*    */   extends SQLObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/* 26 */   private final List<SQLAssignItem> partition = new ArrayList<>(4);
/*    */   
/*    */   public List<SQLAssignItem> getPartition() {
/* 29 */     return this.partition;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 34 */     if (visitor.visit(this)) {
/* 35 */       acceptChild(visitor, this.partition);
/*    */     }
/* 37 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterTableEnableLifecycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */