/*     */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

/*     */
/*     */
/*     */
/*     */

/*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OracleCreateDatabaseDbLinkStatement
/*     */   extends OracleStatementImpl
/*     */ {
/*     */   private boolean shared;
/*     */   private boolean _public;
/*     */   private SQLName name;
/*     */   private SQLName user;
/*     */   private String password;
/*     */   private SQLExpr using;
/*     */   private SQLExpr authenticatedUser;
/*     */   private String authenticatedPassword;
/*     */   
/*     */   public boolean isShared() {
/*  39 */     return this.shared;
/*     */   }
/*     */   
/*     */   public void setShared(boolean shared) {
/*  43 */     this.shared = shared;
/*     */   }
/*     */   
/*     */   public boolean isPublic() {
/*  47 */     return this._public;
/*     */   }
/*     */   
/*     */   public void setPublic(boolean value) {
/*  51 */     this._public = value;
/*     */   }
/*     */   
/*     */   public SQLName getName() {
/*  55 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(SQLName name) {
/*  59 */     this.name = name;
/*     */   }
/*     */   
/*     */   public SQLName getUser() {
/*  63 */     return this.user;
/*     */   }
/*     */   
/*     */   public void setUser(SQLName user) {
/*  67 */     this.user = user;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/*  71 */     return this.password;
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/*  75 */     this.password = password;
/*     */   }
/*     */   
/*     */   public SQLExpr getUsing() {
/*  79 */     return this.using;
/*     */   }
/*     */   
/*     */   public void setUsing(SQLExpr using) {
/*  83 */     this.using = using;
/*     */   }
/*     */   
/*     */   public SQLExpr getAuthenticatedUser() {
/*  87 */     return this.authenticatedUser;
/*     */   }
/*     */   
/*     */   public void setAuthenticatedUser(SQLExpr authenticatedUser) {
/*  91 */     this.authenticatedUser = authenticatedUser;
/*     */   }
/*     */   
/*     */   public String getAuthenticatedPassword() {
/*  95 */     return this.authenticatedPassword;
/*     */   }
/*     */   
/*     */   public void setAuthenticatedPassword(String authenticatedPassword) {
/*  99 */     this.authenticatedPassword = authenticatedPassword;
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept0(OracleASTVisitor visitor) {
/* 104 */     if (visitor.visit(this)) {
/* 105 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.name);
/* 106 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.user);
/* 107 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.using);
/* 108 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.authenticatedUser);
/*     */     } 
/* 110 */     visitor.endVisit(this);
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleCreateDatabaseDbLinkStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */