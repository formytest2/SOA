/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
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
/*    */ public class OracleAlterProcedureStatement
/*    */   extends OracleStatementImpl
/*    */ {
/*    */   private SQLExpr name;
/*    */   private boolean compile = false;
/*    */   private boolean reuseSettings = false;
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 29 */     if (visitor.visit(this)) {
/* 30 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.name);
/*    */     }
/* 32 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLExpr getName() {
/* 36 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(SQLExpr name) {
/* 40 */     this.name = name;
/*    */   }
/*    */   
/*    */   public boolean isCompile() {
/* 44 */     return this.compile;
/*    */   }
/*    */   
/*    */   public void setCompile(boolean compile) {
/* 48 */     this.compile = compile;
/*    */   }
/*    */   
/*    */   public boolean isReuseSettings() {
/* 52 */     return this.reuseSettings;
/*    */   }
/*    */   
/*    */   public void setReuseSettings(boolean reuseSettings) {
/* 56 */     this.reuseSettings = reuseSettings;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleAlterProcedureStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */