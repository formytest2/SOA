/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.expr;
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
/*    */ public class OracleDatetimeExpr
/*    */   extends OracleSQLObjectImpl
/*    */   implements SQLExpr
/*    */ {
/*    */   private SQLExpr expr;
/*    */   private SQLExpr timeZone;
/*    */   
/*    */   public OracleDatetimeExpr() {}
/*    */   
/*    */   public OracleDatetimeExpr(SQLExpr expr, SQLExpr timeZone) {
/* 32 */     this.expr = expr;
/* 33 */     this.timeZone = timeZone;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 38 */     if (visitor.visit(this)) {
/* 39 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.expr);
/* 40 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.timeZone);
/*    */     } 
/* 42 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLExpr getExpr() {
/* 46 */     return this.expr;
/*    */   }
/*    */   
/*    */   public void setExpr(SQLExpr expr) {
/* 50 */     this.expr = expr;
/*    */   }
/*    */   
/*    */   public SQLExpr getTimeZone() {
/* 54 */     return this.timeZone;
/*    */   }
/*    */   
/*    */   public void setTimeZone(SQLExpr timeZone) {
/* 58 */     this.timeZone = timeZone;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\expr\OracleDatetimeExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */