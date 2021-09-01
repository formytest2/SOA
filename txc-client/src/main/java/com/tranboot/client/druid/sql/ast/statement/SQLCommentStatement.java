/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatement;
import com.tranboot.client.druid.sql.ast.SQLStatementImpl;
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
/*    */ public class SQLCommentStatement
/*    */   extends SQLStatementImpl
/*    */   implements SQLStatement
/*    */ {
/*    */   private SQLExpr on;
/*    */   private Type type;
/*    */   private SQLExpr comment;
/*    */   
/*    */   public enum Type
/*    */   {
/* 26 */     TABLE, COLUMN;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SQLExpr getComment() {
/* 34 */     return this.comment;
/*    */   }
/*    */   
/*    */   public void setComment(SQLExpr comment) {
/* 38 */     this.comment = comment;
/*    */   }
/*    */   
/*    */   public Type getType() {
/* 42 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(Type type) {
/* 46 */     this.type = type;
/*    */   }
/*    */   
/*    */   public SQLExpr getOn() {
/* 50 */     return this.on;
/*    */   }
/*    */   
/*    */   public void setOn(SQLExpr on) {
/* 54 */     this.on = on;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 59 */     if (visitor.visit(this)) {
/* 60 */       acceptChild(visitor, (SQLObject)this.on);
/* 61 */       acceptChild(visitor, (SQLObject)this.comment);
/*    */     } 
/* 63 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLCommentStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */