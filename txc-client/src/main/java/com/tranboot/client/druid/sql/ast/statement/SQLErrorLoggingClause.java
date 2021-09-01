/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
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
/*    */ 
/*    */ 
/*    */ public class SQLErrorLoggingClause
/*    */   extends SQLObjectImpl
/*    */ {
/*    */   private SQLName into;
/*    */   private SQLExpr simpleExpression;
/*    */   private SQLExpr limit;
/*    */   
/*    */   public void accept0(SQLASTVisitor visitor) {
/* 31 */     if (visitor.visit(this)) {
/* 32 */       acceptChild(visitor, (SQLObject)this.into);
/* 33 */       acceptChild(visitor, (SQLObject)this.simpleExpression);
/* 34 */       acceptChild(visitor, (SQLObject)this.limit);
/*    */     } 
/* 36 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLName getInto() {
/* 40 */     return this.into;
/*    */   }
/*    */   
/*    */   public void setInto(SQLName into) {
/* 44 */     this.into = into;
/*    */   }
/*    */   
/*    */   public SQLExpr getSimpleExpression() {
/* 48 */     return this.simpleExpression;
/*    */   }
/*    */   
/*    */   public void setSimpleExpression(SQLExpr simpleExpression) {
/* 52 */     this.simpleExpression = simpleExpression;
/*    */   }
/*    */   
/*    */   public SQLExpr getLimit() {
/* 56 */     return this.limit;
/*    */   }
/*    */   
/*    */   public void setLimit(SQLExpr limit) {
/* 60 */     this.limit = limit;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLErrorLoggingClause.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */