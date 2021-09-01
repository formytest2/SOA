/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MySqlObjectImpl;
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
/*    */ 
/*    */ public class MySqlCreateUserStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/* 27 */   private List<UserSpecification> users = new ArrayList<>(2);
/*    */   
/*    */   public List<UserSpecification> getUsers() {
/* 30 */     return this.users;
/*    */   }
/*    */   
/*    */   public void addUser(UserSpecification user) {
/* 34 */     if (user != null) {
/* 35 */       user.setParent((SQLObject)this);
/*    */     }
/* 37 */     this.users.add(user);
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 42 */     if (visitor.visit(this)) {
/* 43 */       acceptChild((SQLASTVisitor)visitor, this.users);
/*    */     }
/* 45 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public static class UserSpecification
/*    */     extends MySqlObjectImpl {
/*    */     private SQLExpr user;
/*    */     private boolean passwordHash = false;
/*    */     private SQLExpr password;
/*    */     private SQLExpr authPlugin;
/*    */     
/*    */     public SQLExpr getUser() {
/* 56 */       return this.user;
/*    */     }
/*    */     
/*    */     public void setUser(SQLExpr user) {
/* 60 */       this.user = user;
/*    */     }
/*    */     
/*    */     public boolean isPasswordHash() {
/* 64 */       return this.passwordHash;
/*    */     }
/*    */     
/*    */     public void setPasswordHash(boolean passwordHash) {
/* 68 */       this.passwordHash = passwordHash;
/*    */     }
/*    */     
/*    */     public SQLExpr getPassword() {
/* 72 */       return this.password;
/*    */     }
/*    */     
/*    */     public void setPassword(SQLExpr password) {
/* 76 */       this.password = password;
/*    */     }
/*    */     
/*    */     public SQLExpr getAuthPlugin() {
/* 80 */       return this.authPlugin;
/*    */     }
/*    */     
/*    */     public void setAuthPlugin(SQLExpr authPlugin) {
/* 84 */       this.authPlugin = authPlugin;
/*    */     }
/*    */ 
/*    */     
/*    */     public void accept0(MySqlASTVisitor visitor) {
/* 89 */       if (visitor.visit(this)) {
/* 90 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.user);
/* 91 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.password);
/* 92 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.authPlugin);
/*    */       } 
/* 94 */       visitor.endVisit(this);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlCreateUserStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */