/*    */ package com.tranboot.client.druid.sql.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.SQLUtils;
import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*    */ public class SQLCharExpr
/*    */   extends SQLTextLiteralExpr
/*    */   implements SQLValuableExpr
/*    */ {
/*    */   public SQLCharExpr() {}
/*    */   
/*    */   public SQLCharExpr(String text) {
/* 28 */     super(text);
/*    */   }
/*    */ 
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 33 */     if (this.text == null || this.text.length() == 0) {
/* 34 */       buf.append("NULL");
/*    */     } else {
/* 36 */       buf.append("'");
/* 37 */       buf.append(this.text.replaceAll("'", "''"));
/* 38 */       buf.append("'");
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 43 */     visitor.visit(this);
/* 44 */     visitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getValue() {
/* 49 */     return this.text;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 53 */     return SQLUtils.toSQLString((SQLObject)this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLCharExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */