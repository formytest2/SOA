/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast;
/*    */ 
/*    */

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
/*    */ public class MySqlUseIndexHint
/*    */   extends MySqlIndexHintImpl
/*    */ {
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 24 */     if (visitor.visit(this)) {
/* 25 */       acceptChild((SQLASTVisitor)visitor, getIndexList());
/*    */     }
/* 27 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\MySqlUseIndexHint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */