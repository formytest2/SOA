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
/*    */ public class SQLAlterTableSetLifecycle
/*    */   extends SQLObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/*    */   private SQLExpr lifecycle;
/*    */   
/*    */   public SQLExpr getLifecycle() {
/* 27 */     return this.lifecycle;
/*    */   }
/*    */   
/*    */   public void setLifecycle(SQLExpr comment) {
/* 31 */     if (comment != null) {
/* 32 */       comment.setParent(this);
/*    */     }
/* 34 */     this.lifecycle = comment;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 39 */     if (visitor.visit(this)) {
/* 40 */       acceptChild(visitor, (SQLObject)this.lifecycle);
/*    */     }
/* 42 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterTableSetLifecycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */