/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatementImpl;
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
/*    */ public class SQLDropUserStatement
/*    */   extends SQLStatementImpl
/*    */   implements SQLDDLStatement
/*    */ {
/* 27 */   private List<SQLExpr> users = new ArrayList<>(2);
/*    */ 
/*    */   
/*    */   public SQLDropUserStatement() {}
/*    */ 
/*    */   
/*    */   public SQLDropUserStatement(String dbType) {
/* 34 */     super(dbType);
/*    */   }
/*    */   
/*    */   public List<SQLExpr> getUsers() {
/* 38 */     return this.users;
/*    */   }
/*    */   
/*    */   public void addUser(SQLExpr user) {
/* 42 */     if (user != null) {
/* 43 */       user.setParent((SQLObject)this);
/*    */     }
/* 45 */     this.users.add(user);
/*    */   }
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 49 */     if (visitor.visit(this)) {
/* 50 */       acceptChild(visitor, this.users);
/*    */     }
/* 52 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLDropUserStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */