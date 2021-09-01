/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*    */ public class OracleLabelStatement
/*    */   extends OracleStatementImpl
/*    */ {
/*    */   private SQLName label;
/*    */   
/*    */   public OracleLabelStatement() {}
/*    */   
/*    */   public OracleLabelStatement(SQLName label) {
/* 29 */     this.label = label;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 34 */     if (visitor.visit(this)) {
/* 35 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.label);
/*    */     }
/* 37 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLName getLabel() {
/* 41 */     return this.label;
/*    */   }
/*    */   
/*    */   public void setLabel(SQLName label) {
/* 45 */     this.label = label;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleLabelStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */