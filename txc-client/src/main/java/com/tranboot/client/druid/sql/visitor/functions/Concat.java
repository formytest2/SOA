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
/*    */ public class Concat
/*    */   implements Function
/*    */ {
/* 26 */   public static final Concat instance = new Concat();
/*    */   
/*    */   public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
/* 29 */     StringBuilder buf = new StringBuilder();
/*    */     
/* 31 */     for (SQLExpr item : x.getParameters()) {
/* 32 */       item.accept((SQLASTVisitor)visitor);
/*    */       
/* 34 */       Object itemValue = item.getAttributes().get("eval.value");
/* 35 */       if (itemValue == null) {
/* 36 */         return null;
/*    */       }
/* 38 */       buf.append(itemValue.toString());
/*    */     } 
/*    */     
/* 41 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\visitor\functions\Concat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */