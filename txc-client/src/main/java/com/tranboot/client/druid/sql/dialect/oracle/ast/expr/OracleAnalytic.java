/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.expr;
/*    */ 
/*    */

/*    */

import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLOver;
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
/*    */ public class OracleAnalytic
/*    */   extends SQLOver
/*    */   implements OracleExpr
/*    */ {
/*    */   private OracleAnalyticWindowing windowing;
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 31 */     accept0((OracleASTVisitor)visitor);
/*    */   }
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 35 */     if (visitor.visit(this)) {
/* 36 */       acceptChild((SQLASTVisitor)visitor, this.partitionBy);
/* 37 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.orderBy);
/* 38 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.windowing);
/*    */     } 
/* 40 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public OracleAnalyticWindowing getWindowing() {
/* 44 */     return this.windowing;
/*    */   }
/*    */   
/*    */   public void setWindowing(OracleAnalyticWindowing windowing) {
/* 48 */     this.windowing = windowing;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\expr\OracleAnalytic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */