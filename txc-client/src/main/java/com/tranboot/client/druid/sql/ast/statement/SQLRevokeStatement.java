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
/*    */ 
/*    */ 
/*    */ public class SQLRevokeStatement
/*    */   extends SQLStatementImpl
/*    */ {
/* 28 */   private final List<SQLExpr> privileges = new ArrayList<>();
/*    */   
/*    */   private SQLObject on;
/*    */   
/*    */   private SQLExpr from;
/*    */   
/*    */   private SQLObjectType objectType;
/*    */ 
/*    */   
/*    */   public SQLRevokeStatement() {}
/*    */   
/*    */   public SQLRevokeStatement(String dbType) {
/* 40 */     super(dbType);
/*    */   }
/*    */   
/*    */   public SQLObject getOn() {
/* 44 */     return this.on;
/*    */   }
/*    */   
/*    */   public void setOn(SQLObject on) {
/* 48 */     this.on = on;
/*    */   }
/*    */   
/*    */   public SQLExpr getFrom() {
/* 52 */     return this.from;
/*    */   }
/*    */   
/*    */   public void setFrom(SQLExpr from) {
/* 56 */     this.from = from;
/*    */   }
/*    */   
/*    */   public List<SQLExpr> getPrivileges() {
/* 60 */     return this.privileges;
/*    */   }
/*    */   
/*    */   public SQLObjectType getObjectType() {
/* 64 */     return this.objectType;
/*    */   }
/*    */   
/*    */   public void setObjectType(SQLObjectType objectType) {
/* 68 */     this.objectType = objectType;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 73 */     if (visitor.visit(this)) {
/* 74 */       acceptChild(visitor, this.on);
/* 75 */       acceptChild(visitor, (SQLObject)this.from);
/*    */     } 
/* 77 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLRevokeStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */