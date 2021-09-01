/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLStatementImpl;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SQLOpenStatement
/*    */   extends SQLStatementImpl
/*    */ {
/*    */   private String cursorName;
/*    */   
/*    */   public String getCursorName() {
/* 30 */     return this.cursorName;
/*    */   }
/*    */   
/*    */   public void setCursorName(String cursorName) {
/* 34 */     this.cursorName = cursorName;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 40 */     visitor.visit(this);
/* 41 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLOpenStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */