/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
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
/*    */ public class SQLAlterTableCoalescePartition
/*    */   extends SQLObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/*    */   private SQLExpr count;
/*    */   
/*    */   public SQLExpr getCount() {
/* 27 */     return this.count;
/*    */   }
/*    */   
/*    */   public void setCount(SQLExpr count) {
/* 31 */     if (count != null) {
/* 32 */       count.setParent(this);
/*    */     }
/* 34 */     this.count = count;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 39 */     if (visitor.visit(this)) {
/* 40 */       acceptChild(visitor, (SQLObject)this.count);
/*    */     }
/* 42 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterTableCoalescePartition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */