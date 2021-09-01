/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */ import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

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
/*    */ public class SQLNotNullConstraint
/*    */   extends SQLConstraintImpl
/*    */   implements SQLColumnConstraint
/*    */ {
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 28 */     visitor.visit(this);
/* 29 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLNotNullConstraint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */