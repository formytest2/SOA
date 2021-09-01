/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.SQLUtils;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLSelect;
import com.tranboot.client.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.FlashbackQueryClause;
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
/*    */ 
/*    */ 
/*    */ public class OracleSelectSubqueryTableSource
/*    */   extends SQLSubqueryTableSource
/*    */   implements OracleSelectTableSource
/*    */ {
/*    */   protected OracleSelectPivotBase pivot;
/*    */   protected FlashbackQueryClause flashback;
/*    */   
/*    */   public OracleSelectSubqueryTableSource() {}
/*    */   
/*    */   public FlashbackQueryClause getFlashback() {
/* 35 */     return this.flashback;
/*    */   }
/*    */   
/*    */   public void setFlashback(FlashbackQueryClause flashback) {
/* 39 */     this.flashback = flashback;
/*    */   }
/*    */   
/*    */   public OracleSelectSubqueryTableSource(String alias) {
/* 43 */     super(alias);
/*    */   }
/*    */   
/*    */   public OracleSelectSubqueryTableSource(SQLSelect select, String alias) {
/* 47 */     super(select, alias);
/*    */   }
/*    */   
/*    */   public OracleSelectSubqueryTableSource(SQLSelect select) {
/* 51 */     super(select);
/*    */   }
/*    */   
/*    */   public OracleSelectPivotBase getPivot() {
/* 55 */     return this.pivot;
/*    */   }
/*    */   
/*    */   public void setPivot(OracleSelectPivotBase pivot) {
/* 59 */     this.pivot = pivot;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 64 */     accept0((OracleASTVisitor)visitor);
/*    */   }
/*    */   
/*    */   protected void accept0(OracleASTVisitor visitor) {
/* 68 */     if (visitor.visit(this)) {
/* 69 */       acceptChild((SQLASTVisitor)visitor, getHints());
/* 70 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.select);
/* 71 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.pivot);
/* 72 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.flashback);
/*    */     } 
/* 74 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 78 */     return SQLUtils.toOracleString((SQLObject)this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleSelectSubqueryTableSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */