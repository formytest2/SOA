/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
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
/*    */ 
/*    */ public class MySqlSetPasswordStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/*    */   private SQLName user;
/*    */   private SQLExpr password;
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 28 */     if (visitor.visit(this)) {
/* 29 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.user);
/* 30 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.password);
/*    */     } 
/* 32 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLName getUser() {
/* 36 */     return this.user;
/*    */   }
/*    */   
/*    */   public void setUser(SQLName user) {
/* 40 */     if (user != null) {
/* 41 */       user.setParent((SQLObject)this);
/*    */     }
/*    */     
/* 44 */     this.user = user;
/*    */   }
/*    */   
/*    */   public SQLExpr getPassword() {
/* 48 */     return this.password;
/*    */   }
/*    */   
/*    */   public void setPassword(SQLExpr password) {
/* 52 */     if (password != null) {
/* 53 */       password.setParent((SQLObject)this);
/*    */     }
/* 55 */     this.password = password;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlSetPasswordStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */