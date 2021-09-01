/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
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
/*    */ public class OracleLockTableStatement
/*    */   extends OracleStatementImpl
/*    */ {
/*    */   private SQLName table;
/*    */   private LockMode lockMode;
/*    */   private boolean noWait = false;
/*    */   private SQLExpr wait;
/*    */   
/*    */   public boolean isNoWait() {
/* 30 */     return this.noWait;
/*    */   }
/*    */   
/*    */   public void setNoWait(boolean noWait) {
/* 34 */     this.noWait = noWait;
/*    */   }
/*    */   
/*    */   public SQLExpr getWait() {
/* 38 */     return this.wait;
/*    */   }
/*    */   
/*    */   public void setWait(SQLExpr wait) {
/* 42 */     this.wait = wait;
/*    */   }
/*    */   
/*    */   public SQLName getTable() {
/* 46 */     return this.table;
/*    */   }
/*    */   
/*    */   public void setTable(SQLName table) {
/* 50 */     this.table = table;
/*    */   }
/*    */   
/*    */   public LockMode getLockMode() {
/* 54 */     return this.lockMode;
/*    */   }
/*    */   
/*    */   public void setLockMode(LockMode lockMode) {
/* 58 */     this.lockMode = lockMode;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 63 */     if (visitor.visit(this)) {
/* 64 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.table);
/* 65 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.wait);
/*    */     } 
/* 67 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public enum LockMode {
/* 71 */     EXCLUSIVE, SHARE;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleLockTableStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */