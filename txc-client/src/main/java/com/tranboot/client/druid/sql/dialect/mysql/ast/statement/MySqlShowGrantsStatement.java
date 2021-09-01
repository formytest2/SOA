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
/*    */ public class MySqlShowGrantsStatement
/*    */   extends MySqlStatementImpl
/*    */   implements MySqlShowStatement
/*    */ {
/*    */   private SQLExpr user;
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 26 */     if (visitor.visit(this)) {
/* 27 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.user);
/*    */     }
/* 29 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLExpr getUser() {
/* 33 */     return this.user;
/*    */   }
/*    */   
/*    */   public void setUser(SQLExpr user) {
/* 37 */     if (user != null) {
/* 38 */       user.setParent((SQLObject)this);
/*    */     }
/* 40 */     this.user = user;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlShowGrantsStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */