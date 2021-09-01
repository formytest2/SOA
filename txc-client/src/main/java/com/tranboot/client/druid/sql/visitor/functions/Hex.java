/*    */ package com.tranboot.client.druid.sql.visitor.functions;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.parser.ParserException;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLEvalVisitor;
import com.tranboot.client.druid.util.HexBin;

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
/*    */ public class Hex
/*    */   implements Function
/*    */ {
/* 28 */   public static final Hex instance = new Hex();
/*    */   
/*    */   public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
/* 31 */     if (x.getParameters().size() != 1) {
/* 32 */       throw new ParserException("argument's != 1, " + x.getParameters().size());
/*    */     }
/*    */     
/* 35 */     SQLExpr param0 = x.getParameters().get(0);
/* 36 */     param0.accept((SQLASTVisitor)visitor);
/*    */     
/* 38 */     Object param0Value = param0.getAttributes().get("eval.value");
/* 39 */     if (param0Value == null) {
/* 40 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 43 */     if (param0Value instanceof String) {
/* 44 */       byte[] bytes = ((String)param0Value).getBytes();
/* 45 */       String result = HexBin.encode(bytes);
/* 46 */       return result;
/*    */     } 
/*    */     
/* 49 */     if (param0Value instanceof Number) {
/* 50 */       long value = ((Number)param0Value).longValue();
/* 51 */       String result = Long.toHexString(value).toUpperCase();
/* 52 */       return result;
/*    */     } 
/*    */     
/* 55 */     return SQLEvalVisitor.EVAL_ERROR;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\visitor\functions\Hex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */