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
/*    */ 
/*    */ 
/*    */ public class SQLAssignItem
/*    */   extends SQLObjectImpl
/*    */ {
/*    */   private SQLExpr target;
/*    */   private SQLExpr value;
/*    */   
/*    */   public SQLAssignItem() {}
/*    */   
/*    */   public SQLAssignItem(SQLExpr target, SQLExpr value) {
/* 31 */     setTarget(target);
/* 32 */     setValue(value);
/*    */   }
/*    */   
/*    */   public SQLExpr getTarget() {
/* 36 */     return this.target;
/*    */   }
/*    */   
/*    */   public void setTarget(SQLExpr target) {
/* 40 */     if (target != null) {
/* 41 */       target.setParent((SQLObject)this);
/*    */     }
/* 43 */     this.target = target;
/*    */   }
/*    */   
/*    */   public SQLExpr getValue() {
/* 47 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(SQLExpr value) {
/* 51 */     if (value != null) {
/* 52 */       value.setParent((SQLObject)this);
/*    */     }
/* 54 */     this.value = value;
/*    */   }
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 58 */     this.target.output(buf);
/* 59 */     buf.append(" = ");
/* 60 */     this.value.output(buf);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 65 */     if (visitor.visit(this)) {
/* 66 */       acceptChild(visitor, (SQLObject)this.target);
/* 67 */       acceptChild(visitor, (SQLObject)this.value);
/*    */     } 
/* 69 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAssignItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */