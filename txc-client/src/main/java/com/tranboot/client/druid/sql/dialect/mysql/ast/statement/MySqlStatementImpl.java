/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLStatementImpl;
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
/*    */ public abstract class MySqlStatementImpl
/*    */   extends SQLStatementImpl
/*    */   implements MySqlStatement
/*    */ {
/*    */   public MySqlStatementImpl() {
/* 26 */     super("mysql");
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 31 */     if (visitor instanceof MySqlASTVisitor) {
/* 32 */       accept0((MySqlASTVisitor)visitor);
/*    */     } else {
/* 34 */       throw new IllegalArgumentException("not support visitor type : " + visitor.getClass().getName());
/*    */     } 
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 39 */     throw new UnsupportedOperationException(getClass().getName());
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlStatementImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */