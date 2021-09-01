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
/*    */ public class SQLAlterTableSetComment
/*    */   extends SQLObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/*    */   private SQLExpr comment;
/*    */   
/*    */   public SQLExpr getComment() {
/* 27 */     return this.comment;
/*    */   }
/*    */   
/*    */   public void setComment(SQLExpr comment) {
/* 31 */     if (comment != null) {
/* 32 */       comment.setParent(this);
/*    */     }
/* 34 */     this.comment = comment;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 39 */     if (visitor.visit(this)) {
/* 40 */       acceptChild(visitor, (SQLObject)this.comment);
/*    */     }
/* 42 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterTableSetComment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */