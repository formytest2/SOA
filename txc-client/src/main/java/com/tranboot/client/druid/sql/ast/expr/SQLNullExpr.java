/*    */ package com.tranboot.client.druid.sql.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLEvalVisitor;

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
/*    */ 
/*    */ public class SQLNullExpr
/*    */   extends SQLExprImpl
/*    */   implements SQLLiteralExpr, SQLValuableExpr
/*    */ {
/*    */   public void output(StringBuffer buf) {
/* 30 */     buf.append("NULL");
/*    */   }
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 34 */     visitor.visit(this);
/*    */     
/* 36 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 40 */     return 0;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 44 */     return o instanceof SQLNullExpr;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getValue() {
/* 49 */     return SQLEvalVisitor.EVAL_VALUE_NULL;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLNullExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */