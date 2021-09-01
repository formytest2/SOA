/*    */ package com.tranboot.client.druid.sql.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExprImpl;
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
/*    */ 
/*    */ public class SQLAllColumnExpr
/*    */   extends SQLExprImpl
/*    */ {
/*    */   public void output(StringBuffer buf) {
/* 28 */     buf.append("*");
/*    */   }
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 32 */     visitor.visit(this);
/* 33 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 37 */     return 0;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 41 */     return o instanceof SQLAllColumnExpr;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLAllColumnExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */