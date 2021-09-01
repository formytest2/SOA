/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
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
/*    */ public class OracleSetTransactionStatement
/*    */   extends OracleSQLObjectImpl
/*    */   implements OracleStatement
/*    */ {
/*    */   private boolean readOnly = false;
/*    */   private String dbType;
/*    */   private SQLExpr name;
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 31 */     if (visitor.visit(this)) {
/* 32 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.name);
/*    */     }
/* 34 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLExpr getName() {
/* 38 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(SQLExpr name) {
/* 42 */     this.name = name;
/*    */   }
/*    */   
/*    */   public boolean isReadOnly() {
/* 46 */     return this.readOnly;
/*    */   }
/*    */   
/*    */   public void setReadOnly(boolean readOnly) {
/* 50 */     this.readOnly = readOnly;
/*    */   }
/*    */   
/*    */   public String getDbType() {
/* 54 */     return this.dbType;
/*    */   }
/*    */   
/*    */   public void setDbType(String dbType) {
/* 58 */     this.dbType = dbType;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleSetTransactionStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */