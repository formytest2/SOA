/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.clause;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlStatementImpl;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;

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
/*    */ public class MySqlIterateStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/*    */   private String labelName;
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 31 */     visitor.visit(this);
/* 32 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public String getLabelName() {
/* 36 */     return this.labelName;
/*    */   }
/*    */   
/*    */   public void setLabelName(String labelName) {
/* 40 */     this.labelName = labelName;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\clause\MySqlIterateStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */