/*    */ package com.tranboot.client.druid.sql.ast.expr;
/*    */ 
/*    */ import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

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
/*    */ public class SQLNCharExpr
/*    */   extends SQLTextLiteralExpr
/*    */ {
/*    */   public SQLNCharExpr() {}
/*    */   
/*    */   public SQLNCharExpr(String text) {
/* 27 */     super(text);
/*    */   }
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 31 */     if (this.text == null || this.text.length() == 0) {
/* 32 */       buf.append("NULL");
/*    */       
/*    */       return;
/*    */     } 
/* 36 */     buf.append("N'");
/* 37 */     buf.append(this.text.replaceAll("'", "''"));
/* 38 */     buf.append("'");
/*    */   }
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 42 */     visitor.visit(this);
/* 43 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLNCharExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */