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
/*    */ 
/*    */ public class SQLAlterTableAddConstraint
/*    */   extends SQLObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/*    */   private SQLConstraint constraint;
/*    */   private boolean withNoCheck = false;
/*    */   
/*    */   public SQLAlterTableAddConstraint() {}
/*    */   
/*    */   public SQLAlterTableAddConstraint(SQLConstraint constraint) {
/* 31 */     setConstraint(constraint);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 36 */     if (visitor.visit(this)) {
/* 37 */       acceptChild(visitor, this.constraint);
/*    */     }
/* 39 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLConstraint getConstraint() {
/* 43 */     return this.constraint;
/*    */   }
/*    */   
/*    */   public void setConstraint(SQLConstraint constraint) {
/* 47 */     if (constraint != null) {
/* 48 */       constraint.setParent(this);
/*    */     }
/* 50 */     this.constraint = constraint;
/*    */   }
/*    */   
/*    */   public boolean isWithNoCheck() {
/* 54 */     return this.withNoCheck;
/*    */   }
/*    */   
/*    */   public void setWithNoCheck(boolean withNoCheck) {
/* 58 */     this.withNoCheck = withNoCheck;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterTableAddConstraint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */