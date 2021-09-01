/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*    */ public class MySqlHelpStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/*    */   private SQLExpr content;
/*    */   
/*    */   public SQLExpr getContent() {
/* 26 */     return this.content;
/*    */   }
/*    */   
/*    */   public void setContent(SQLExpr content) {
/* 30 */     this.content = content;
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 34 */     if (visitor.visit(this)) {
/* 35 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.content);
/*    */     }
/* 37 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlHelpStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */