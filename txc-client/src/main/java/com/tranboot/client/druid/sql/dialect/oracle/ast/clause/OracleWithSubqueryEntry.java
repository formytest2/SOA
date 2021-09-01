/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.clause;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleSQLObject;
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
/*    */ public class OracleWithSubqueryEntry
/*    */   extends SQLWithSubqueryClause.Entry
/*    */   implements OracleSQLObject
/*    */ {
/*    */   private SearchClause searchClause;
/*    */   private CycleClause cycleClause;
/*    */   
/*    */   public CycleClause getCycleClause() {
/* 29 */     return this.cycleClause;
/*    */   }
/*    */   
/*    */   public void setCycleClause(CycleClause cycleClause) {
/* 33 */     this.cycleClause = cycleClause;
/*    */   }
/*    */   
/*    */   public SearchClause getSearchClause() {
/* 37 */     return this.searchClause;
/*    */   }
/*    */   
/*    */   public void setSearchClause(SearchClause searchClause) {
/* 41 */     this.searchClause = searchClause;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 46 */     if (visitor.visit(this)) {
/* 47 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.name);
/* 48 */       acceptChild((SQLASTVisitor)visitor, this.columns);
/* 49 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.subQuery);
/* 50 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.searchClause);
/* 51 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.cycleClause);
/*    */     } 
/* 53 */     visitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 58 */     accept0((OracleASTVisitor)visitor);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\clause\OracleWithSubqueryEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */