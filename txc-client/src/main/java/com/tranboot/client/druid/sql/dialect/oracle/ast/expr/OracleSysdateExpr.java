/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;

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
/*    */ public class OracleSysdateExpr
/*    */   extends OracleSQLObjectImpl
/*    */   implements SQLExpr
/*    */ {
/*    */   private String option;
/*    */   
/*    */   public String getOption() {
/* 27 */     return this.option;
/*    */   }
/*    */   
/*    */   public void setOption(String option) {
/* 31 */     this.option = option;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 36 */     visitor.visit(this);
/* 37 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\expr\OracleSysdateExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */