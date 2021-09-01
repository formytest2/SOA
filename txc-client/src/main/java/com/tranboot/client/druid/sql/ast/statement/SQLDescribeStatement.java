/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
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
/*    */ public class SQLDescribeStatement
/*    */   extends SQLStatementImpl
/*    */ {
/*    */   protected SQLName object;
/*    */   
/*    */   public SQLName getObject() {
/* 27 */     return this.object;
/*    */   }
/*    */   
/*    */   public void setObject(SQLName object) {
/* 31 */     this.object = object;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 36 */     throw new UnsupportedOperationException(getClass().getName());
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLDescribeStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */