/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLTableSourceImpl;
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
/*    */ 
/*    */ public class MySqlUpdateTableSource
/*    */   extends SQLTableSourceImpl
/*    */ {
/*    */   private MySqlUpdateStatement update;
/*    */   
/*    */   public MySqlUpdateTableSource(MySqlUpdateStatement update) {
/* 28 */     this.update = update;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 33 */     if (visitor instanceof MySqlASTVisitor) {
/* 34 */       accept0((MySqlASTVisitor)visitor);
/*    */     } else {
/* 36 */       throw new IllegalArgumentException("not support visitor type : " + visitor.getClass().getName());
/*    */     } 
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 41 */     if (visitor.visit(this)) {
/* 42 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.update);
/*    */     }
/* 44 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public MySqlUpdateStatement getUpdate() {
/* 48 */     return this.update;
/*    */   }
/*    */   
/*    */   public void setUpdate(MySqlUpdateStatement update) {
/* 52 */     this.update = update;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlUpdateTableSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */