/*    */ package com.tranboot.client.druid.sql.visitor.functions;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLEvalVisitor;
import com.tranboot.client.druid.sql.visitor.SQLEvalVisitorUtils;

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
/*    */ public class If
/*    */   implements Function
/*    */ {
/* 30 */   public static final If instance = new If();
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
/* 41 */     if (itemValue == null) {
/* 42 */       return null;
/*    */     }
/* 44 */     if (Boolean.TRUE == itemValue || !SQLEvalVisitorUtils.eq(itemValue, Integer.valueOf(0))) {
/* 45 */       SQLExpr trueExpr = parameters.get(1);
/* 46 */       trueExpr.accept((SQLASTVisitor)visitor);
/* 47 */       return trueExpr.getAttributes().get("eval.value");
/*    */     } 
/* 49 */     SQLExpr falseExpr = parameters.get(2);
/* 50 */     falseExpr.accept((SQLASTVisitor)visitor);
/* 51 */     return falseExpr.getAttributes().get("eval.value");
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\visitor\functions\If.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */