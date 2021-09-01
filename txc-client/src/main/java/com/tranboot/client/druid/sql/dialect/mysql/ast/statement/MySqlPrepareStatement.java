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
/*    */ 
/*    */ public class MySqlPrepareStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/*    */   private SQLName name;
/*    */   private SQLExpr from;
/*    */   
/*    */   public MySqlPrepareStatement() {}
/*    */   
/*    */   public MySqlPrepareStatement(SQLName name, SQLExpr from) {
/* 31 */     this.name = name;
/* 32 */     this.from = from;
/*    */   }
/*    */   
/*    */   public SQLName getName() {
/* 36 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(SQLName name) {
/* 40 */     this.name = name;
/*    */   }
/*    */   
/*    */   public SQLExpr getFrom() {
/* 44 */     return this.from;
/*    */   }
/*    */   
/*    */   public void setFrom(SQLExpr from) {
/* 48 */     this.from = from;
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 52 */     if (visitor.visit(this)) {
/* 53 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.name);
/* 54 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.from);
/*    */     } 
/* 56 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlPrepareStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */