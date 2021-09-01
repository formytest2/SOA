/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLPrimaryKey;
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
/*    */ public class MySqlPrimaryKey
/*    */   extends MySqlKey
/*    */   implements SQLPrimaryKey
/*    */ {
/*    */   protected void accept0(MySqlASTVisitor visitor) {
/* 28 */     if (visitor.visit(this)) {
/* 29 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getName());
/* 30 */       acceptChild((SQLASTVisitor)visitor, getColumns());
/*    */     } 
/* 32 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\MySqlPrimaryKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */