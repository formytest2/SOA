/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.clause;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlStatementImpl;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

import java.util.ArrayList;
import java.util.List;

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
/*    */ 
/*    */ 
/*    */ public class MySqlDeclareHandlerStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/*    */   private MySqlHandlerType handleType;
/*    */   private SQLStatement spStatement;
/* 41 */   private List<ConditionValue> conditionValues = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public List<ConditionValue> getConditionValues() {
/* 45 */     return this.conditionValues;
/*    */   }
/*    */   
/*    */   public void setConditionValues(List<ConditionValue> conditionValues) {
/* 49 */     this.conditionValues = conditionValues;
/*    */   }
/*    */   
/*    */   public MySqlHandlerType getHandleType() {
/* 53 */     return this.handleType;
/*    */   }
/*    */   
/*    */   public void setHandleType(MySqlHandlerType handleType) {
/* 57 */     this.handleType = handleType;
/*    */   }
/*    */   
/*    */   public SQLStatement getSpStatement() {
/* 61 */     return this.spStatement;
/*    */   }
/*    */   
/*    */   public void setSpStatement(SQLStatement spStatement) {
/* 65 */     this.spStatement = spStatement;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 71 */     if (visitor.visit(this)) {
/* 72 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.spStatement);
/*    */     }
/* 74 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\clause\MySqlDeclareHandlerStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */