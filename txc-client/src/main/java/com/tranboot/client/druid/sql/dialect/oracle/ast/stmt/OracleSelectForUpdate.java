/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
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
/*    */ public class OracleSelectForUpdate
/*    */   extends OracleSQLObjectImpl
/*    */ {
/* 27 */   private final List<SQLExpr> of = new ArrayList<>();
/*    */ 
/*    */   
/*    */   private boolean notWait = false;
/*    */   
/*    */   private SQLExpr wait;
/*    */   
/*    */   private boolean skipLocked = false;
/*    */ 
/*    */   
/*    */   public boolean isNotWait() {
/* 38 */     return this.notWait;
/*    */   }
/*    */   
/*    */   public void setNotWait(boolean notWait) {
/* 42 */     this.notWait = notWait;
/*    */   }
/*    */   
/*    */   public SQLExpr getWait() {
/* 46 */     return this.wait;
/*    */   }
/*    */   
/*    */   public void setWait(SQLExpr wait) {
/* 50 */     this.wait = wait;
/*    */   }
/*    */   
/*    */   public boolean isSkipLocked() {
/* 54 */     return this.skipLocked;
/*    */   }
/*    */   
/*    */   public void setSkipLocked(boolean skipLocked) {
/* 58 */     this.skipLocked = skipLocked;
/*    */   }
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 62 */     if (visitor.visit(this)) {
/* 63 */       acceptChild((SQLASTVisitor)visitor, this.of);
/* 64 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.wait);
/*    */     } 
/*    */     
/* 67 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public List<SQLExpr> getOf() {
/* 71 */     return this.of;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleSelectForUpdate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */