/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.SQLUtils;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatementImpl;
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
/*    */ public abstract class OracleStatementImpl
/*    */   extends SQLStatementImpl
/*    */   implements OracleStatement
/*    */ {
/*    */   public OracleStatementImpl() {
/* 27 */     super("oracle");
/*    */   }
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 31 */     accept0((OracleASTVisitor)visitor);
/*    */   }
/*    */   
/*    */   public abstract void accept0(OracleASTVisitor paramOracleASTVisitor);
/*    */   
/*    */   public String toString() {
/* 37 */     return SQLUtils.toOracleString((SQLObject)this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleStatementImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */