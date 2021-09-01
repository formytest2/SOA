/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatementImpl;
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
/*    */ public class SQLDropIndexStatement
/*    */   extends SQLStatementImpl
/*    */   implements SQLDDLStatement
/*    */ {
/*    */   private SQLExpr indexName;
/*    */   private SQLExprTableSource tableName;
/*    */   
/*    */   public SQLDropIndexStatement() {}
/*    */   
/*    */   public SQLDropIndexStatement(String dbType) {
/* 32 */     super(dbType);
/*    */   }
/*    */   
/*    */   public SQLExpr getIndexName() {
/* 36 */     return this.indexName;
/*    */   }
/*    */   
/*    */   public void setIndexName(SQLExpr indexName) {
/* 40 */     this.indexName = indexName;
/*    */   }
/*    */   
/*    */   public SQLExprTableSource getTableName() {
/* 44 */     return this.tableName;
/*    */   }
/*    */   
/*    */   public void setTableName(SQLExpr tableName) {
/* 48 */     setTableName(new SQLExprTableSource(tableName));
/*    */   }
/*    */   
/*    */   public void setTableName(SQLExprTableSource tableName) {
/* 52 */     this.tableName = tableName;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 57 */     if (visitor.visit(this)) {
/* 58 */       acceptChild(visitor, (SQLObject)this.indexName);
/* 59 */       acceptChild(visitor, this.tableName);
/*    */     } 
/* 61 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLDropIndexStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */