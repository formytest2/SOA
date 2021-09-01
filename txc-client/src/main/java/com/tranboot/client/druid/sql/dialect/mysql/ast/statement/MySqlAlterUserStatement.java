/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

import java.util.ArrayList;
import java.util.List;

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
/*    */ public class MySqlAlterUserStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/* 26 */   private final List<SQLExpr> users = new ArrayList<>();
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 29 */     if (visitor.visit(this)) {
/* 30 */       acceptChild((SQLASTVisitor)visitor, this.users);
/*    */     }
/* 32 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public List<SQLExpr> getUsers() {
/* 36 */     return this.users;
/*    */   }
/*    */   
/*    */   public void addUser(SQLExpr user) {
/* 40 */     if (user != null) {
/* 41 */       user.setParent((SQLObject)this);
/*    */     }
/* 43 */     this.users.add(user);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlAlterUserStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */