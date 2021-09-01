/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */ import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;

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
/*    */ public class OraclePLSQLCommitStatement
/*    */   extends OracleStatementImpl
/*    */ {
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 27 */     visitor.visit(this);
/* 28 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OraclePLSQLCommitStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */