/*    */ package com.tranboot.client.druid.sql.visitor.functions;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLEvalVisitor;
import com.tranboot.client.druid.sql.visitor.SQLEvalVisitorUtils;

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
/*    */ public class Least
/*    */   implements Function
/*    */ {
/* 27 */   public static final Least instance = new Least();
/*    */   
/*    */   public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
/* 30 */     Object result = null;
/* 31 */     for (SQLExpr item : x.getParameters()) {
/* 32 */       item.accept((SQLASTVisitor)visitor);
/*    */       
/* 34 */       Object itemValue = item.getAttributes().get("eval.value");
/* 35 */       if (result == null) {
/* 36 */         result = itemValue; continue;
/*    */       } 
/* 38 */       if (SQLEvalVisitorUtils.lt(itemValue, result)) {
/* 39 */         result = itemValue;
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/* 44 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\visitor\functions\Least.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */