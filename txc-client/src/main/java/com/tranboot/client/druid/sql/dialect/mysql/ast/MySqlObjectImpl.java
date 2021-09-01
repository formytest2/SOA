/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLObjectImpl;
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
/*    */ public abstract class MySqlObjectImpl
/*    */   extends SQLObjectImpl
/*    */   implements MySqlObject
/*    */ {
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 26 */     if (visitor instanceof MySqlASTVisitor) {
/* 27 */       accept0((MySqlASTVisitor)visitor);
/*    */     } else {
/* 29 */       throw new IllegalArgumentException("not support visitor type : " + visitor.getClass().getName());
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract void accept0(MySqlASTVisitor paramMySqlASTVisitor);
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\MySqlObjectImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */