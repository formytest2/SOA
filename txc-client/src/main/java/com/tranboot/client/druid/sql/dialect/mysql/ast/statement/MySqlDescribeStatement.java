/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLDescribeStatement;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
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
/*    */ public class MySqlDescribeStatement
/*    */   extends SQLDescribeStatement
/*    */   implements MySqlStatement
/*    */ {
/*    */   private SQLName colName;
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 28 */     if (visitor.visit(this)) {
/* 29 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.object);
/* 30 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.colName);
/*    */     } 
/* 32 */     visitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(SQLASTVisitor visitor) {
/* 37 */     if (visitor instanceof MySqlASTVisitor) {
/* 38 */       accept0((MySqlASTVisitor)visitor);
/*    */     } else {
/* 40 */       throw new IllegalArgumentException("not support visitor type : " + visitor.getClass().getName());
/*    */     } 
/*    */   }
/*    */   
/*    */   public SQLName getColName() {
/* 45 */     return this.colName;
/*    */   }
/*    */   
/*    */   public void setColName(SQLName colName) {
/* 49 */     this.colName = colName;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlDescribeStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */