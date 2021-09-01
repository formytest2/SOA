/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLObjectImpl;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class OracleSQLObjectImpl
/*    */   extends SQLObjectImpl
/*    */   implements OracleSQLObject
/*    */ {
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 30 */     accept0((OracleASTVisitor)visitor);
/*    */   }
/*    */   
/*    */   public abstract void accept0(OracleASTVisitor paramOracleASTVisitor);
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\OracleSQLObjectImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */