/*    */ package com.tranboot.client.druid.sql.visitor.functions;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLEvalVisitor;

import java.util.List;

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
/*    */ 
/*    */ 
/*    */ public class Isnull
/*    */   implements Function
/*    */ {
/* 30 */   public static final Isnull instance = new Isnull();
/*    */   
/*    */   public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
/* 33 */     List<SQLExpr> parameters = x.getParameters();
/* 34 */     if (parameters.size() == 0) {
/* 35 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 38 */     SQLExpr condition = parameters.get(0);
/* 39 */     condition.accept((SQLASTVisitor)visitor);
/* 40 */     Object itemValue = condition.getAttributes().get("eval.value");
/* 41 */     if (itemValue == SQLEvalVisitor.EVAL_VALUE_NULL)
/* 42 */       return Boolean.TRUE; 
/* 43 */     if (itemValue == null) {
/* 44 */       return null;
/*    */     }
/* 46 */     return Boolean.FALSE;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\visitor\functions\Isnull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */