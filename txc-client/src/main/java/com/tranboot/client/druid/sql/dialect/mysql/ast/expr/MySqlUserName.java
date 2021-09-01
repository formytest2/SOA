/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;

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
/*    */ public class MySqlUserName
/*    */   extends MySqlExprImpl
/*    */   implements SQLName
/*    */ {
/*    */   private String userName;
/*    */   private String host;
/*    */   
/*    */   public String getUserName() {
/* 27 */     return this.userName;
/*    */   }
/*    */   
/*    */   public void setUserName(String userName) {
/* 31 */     this.userName = userName;
/*    */   }
/*    */   
/*    */   public String getHost() {
/* 35 */     return this.host;
/*    */   }
/*    */   
/*    */   public void setHost(String host) {
/* 39 */     this.host = host;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 44 */     visitor.visit(this);
/* 45 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public String getSimpleName() {
/* 49 */     return this.userName + '@' + this.host;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 53 */     return getSimpleName();
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\expr\MySqlUserName.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */