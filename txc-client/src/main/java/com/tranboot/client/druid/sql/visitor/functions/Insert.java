/*    */ package com.tranboot.client.druid.sql.visitor.functions;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
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
/*    */ public class Insert
/*    */   implements Function
/*    */ {
/* 26 */   public static final Insert instance = new Insert();
/*    */   
/*    */   public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
/* 29 */     if (x.getParameters().size() != 4) {
/* 30 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 33 */     SQLExpr param0 = x.getParameters().get(0);
/* 34 */     SQLExpr param1 = x.getParameters().get(1);
/* 35 */     SQLExpr param2 = x.getParameters().get(2);
/* 36 */     SQLExpr param3 = x.getParameters().get(3);
/* 37 */     param0.accept((SQLASTVisitor)visitor);
/* 38 */     param1.accept((SQLASTVisitor)visitor);
/* 39 */     param2.accept((SQLASTVisitor)visitor);
/* 40 */     param3.accept((SQLASTVisitor)visitor);
/*    */     
/* 42 */     Object param0Value = param0.getAttributes().get("eval.value");
/* 43 */     Object param1Value = param1.getAttributes().get("eval.value");
/* 44 */     Object param2Value = param2.getAttributes().get("eval.value");
/* 45 */     Object param3Value = param3.getAttributes().get("eval.value");
/*    */     
/* 47 */     if (!(param0Value instanceof String)) {
/* 48 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/* 50 */     if (!(param1Value instanceof Number)) {
/* 51 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/* 53 */     if (!(param2Value instanceof Number)) {
/* 54 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/* 56 */     if (!(param3Value instanceof String)) {
/* 57 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 60 */     String str = (String)param0Value;
/* 61 */     int pos = ((Number)param1Value).intValue();
/* 62 */     int len = ((Number)param2Value).intValue();
/* 63 */     String newstr = (String)param3Value;
/*    */     
/* 65 */     if (pos <= 0) {
/* 66 */       return str;
/*    */     }
/*    */     
/* 69 */     if (pos == 1) {
/* 70 */       if (len > str.length()) {
/* 71 */         return newstr;
/*    */       }
/* 73 */       return newstr + str.substring(len);
/*    */     } 
/*    */     
/* 76 */     String first = str.substring(0, pos - 1);
/* 77 */     if (pos + len - 1 > str.length()) {
/* 78 */       return first + newstr;
/*    */     }
/*    */     
/* 81 */     return first + newstr + str.substring(pos + len - 1);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\visitor\functions\Insert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */