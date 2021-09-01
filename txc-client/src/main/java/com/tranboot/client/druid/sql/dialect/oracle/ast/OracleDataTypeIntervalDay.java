/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLDataTypeImpl;
import com.tranboot.client.druid.sql.ast.SQLExpr;
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
/*    */ 
/*    */ public class OracleDataTypeIntervalDay
/*    */   extends SQLDataTypeImpl
/*    */   implements OracleSQLObject
/*    */ {
/*    */   private boolean toSecond = false;
/* 30 */   protected final List<SQLExpr> fractionalSeconds = new ArrayList<>();
/*    */   
/*    */   public OracleDataTypeIntervalDay() {
/* 33 */     setName("INTERVAL DAY");
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 38 */     accept0((OracleASTVisitor)visitor);
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 43 */     if (visitor.visit(this)) {
/* 44 */       acceptChild((SQLASTVisitor)visitor, getArguments());
/*    */     }
/* 46 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public boolean isToSecond() {
/* 50 */     return this.toSecond;
/*    */   }
/*    */   
/*    */   public void setToSecond(boolean toSecond) {
/* 54 */     this.toSecond = toSecond;
/*    */   }
/*    */   
/*    */   public List<SQLExpr> getFractionalSeconds() {
/* 58 */     return this.fractionalSeconds;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\OracleDataTypeIntervalDay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */