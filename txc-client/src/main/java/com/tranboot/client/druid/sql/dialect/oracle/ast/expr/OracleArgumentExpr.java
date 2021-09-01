/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
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
/*    */ public class OracleArgumentExpr
/*    */   extends OracleSQLObjectImpl
/*    */   implements SQLExpr
/*    */ {
/*    */   private String argumentName;
/*    */   private SQLExpr value;
/*    */   
/*    */   public OracleArgumentExpr() {}
/*    */   
/*    */   public OracleArgumentExpr(String argumentName, SQLExpr value) {
/* 32 */     this.argumentName = argumentName;
/* 33 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getArgumentName() {
/* 37 */     return this.argumentName;
/*    */   }
/*    */   
/*    */   public void setArgumentName(String argumentName) {
/* 41 */     this.argumentName = argumentName;
/*    */   }
/*    */   
/*    */   public SQLExpr getValue() {
/* 45 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(SQLExpr value) {
/* 49 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 54 */     if (visitor.visit(this)) {
/* 55 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.value);
/*    */     }
/* 57 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\expr\OracleArgumentExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */