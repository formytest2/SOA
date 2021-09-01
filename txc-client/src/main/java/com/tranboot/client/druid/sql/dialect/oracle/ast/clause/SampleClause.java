/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.clause;
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
/*    */ 
/*    */ public class SampleClause
/*    */   extends OracleSQLObjectImpl
/*    */ {
/*    */   private boolean block = false;
/* 29 */   private List<SQLExpr> percent = new ArrayList<>();
/*    */   
/*    */   private SQLExpr seedValue;
/*    */   
/*    */   public boolean isBlock() {
/* 34 */     return this.block;
/*    */   }
/*    */   
/*    */   public void setBlock(boolean block) {
/* 38 */     this.block = block;
/*    */   }
/*    */   
/*    */   public List<SQLExpr> getPercent() {
/* 42 */     return this.percent;
/*    */   }
/*    */   
/*    */   public void setPercent(List<SQLExpr> percent) {
/* 46 */     this.percent = percent;
/*    */   }
/*    */   
/*    */   public SQLExpr getSeedValue() {
/* 50 */     return this.seedValue;
/*    */   }
/*    */   
/*    */   public void setSeedValue(SQLExpr seedValue) {
/* 54 */     this.seedValue = seedValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 59 */     if (visitor.visit(this)) {
/* 60 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.seedValue);
/* 61 */       acceptChild((SQLASTVisitor)visitor, this.percent);
/*    */     } 
/* 63 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\clause\SampleClause.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */