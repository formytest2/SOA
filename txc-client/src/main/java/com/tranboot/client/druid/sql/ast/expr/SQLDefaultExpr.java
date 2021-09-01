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
/*    */ public class SQLDefaultExpr
/*    */   extends SQLExprImpl
/*    */   implements SQLLiteralExpr
/*    */ {
/*    */   public boolean equals(Object o) {
/* 25 */     return o instanceof SQLDefaultExpr;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 30 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 35 */     visitor.visit(this);
/* 36 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 40 */     return "DEFAULT";
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLDefaultExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */