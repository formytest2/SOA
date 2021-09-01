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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MySqlDeclareConditionStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/*    */   private String conditionName;
/*    */   private ConditionValue conditionValue;
/*    */   
/*    */   public String getConditionName() {
/* 41 */     return this.conditionName;
/*    */   }
/*    */   
/*    */   public void setConditionName(String conditionName) {
/* 45 */     this.conditionName = conditionName;
/*    */   }
/*    */   
/*    */   public ConditionValue getConditionValue() {
/* 49 */     return this.conditionValue;
/*    */   }
/*    */   
/*    */   public void setConditionValue(ConditionValue conditionValue) {
/* 53 */     this.conditionValue = conditionValue;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 59 */     visitor.visit(this);
/* 60 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\clause\MySqlDeclareConditionStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */