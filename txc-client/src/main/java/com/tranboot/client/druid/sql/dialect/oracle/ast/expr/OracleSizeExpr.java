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
/*    */ 
/*    */ public class OracleSizeExpr
/*    */   extends OracleSQLObjectImpl
/*    */   implements OracleExpr
/*    */ {
/*    */   private SQLExpr value;
/*    */   private Unit unit;
/*    */   
/*    */   public OracleSizeExpr() {}
/*    */   
/*    */   public OracleSizeExpr(SQLExpr value, Unit unit) {
/* 33 */     this.value = value;
/* 34 */     this.unit = unit;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 39 */     if (visitor.visit(this)) {
/* 40 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.value);
/*    */     }
/* 42 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLExpr getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(SQLExpr value) {
/* 50 */     this.value = value;
/*    */   }
/*    */   
/*    */   public Unit getUnit() {
/* 54 */     return this.unit;
/*    */   }
/*    */   
/*    */   public void setUnit(Unit unit) {
/* 58 */     this.unit = unit;
/*    */   }
/*    */   
/*    */   public enum Unit {
/* 62 */     K, M, G, T, P, E;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\expr\OracleSizeExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */