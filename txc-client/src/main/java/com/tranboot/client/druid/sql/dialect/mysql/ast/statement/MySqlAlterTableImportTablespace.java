/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableItem;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MySqlObject;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MySqlObjectImpl;
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
/*    */ 
/*    */ public class MySqlAlterTableImportTablespace
/*    */   extends MySqlObjectImpl
/*    */   implements SQLAlterTableItem, MySqlObject
/*    */ {
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 28 */     if (visitor instanceof MySqlASTVisitor) {
/* 29 */       accept0((MySqlASTVisitor)visitor);
/*    */     } else {
/* 31 */       throw new IllegalArgumentException("not support visitor type : " + visitor.getClass().getName());
/*    */     } 
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 36 */     visitor.visit(this);
/* 37 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlAlterTableImportTablespace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */