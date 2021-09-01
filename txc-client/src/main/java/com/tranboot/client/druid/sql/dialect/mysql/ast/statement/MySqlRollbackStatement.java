/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLRollbackStatement;
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
/*    */ 
/*    */ public class MySqlRollbackStatement
/*    */   extends SQLRollbackStatement
/*    */   implements MySqlStatement
/*    */ {
/*    */   private Boolean chain;
/*    */   private Boolean release;
/*    */   private SQLExpr force;
/*    */   
/*    */   public MySqlRollbackStatement() {
/* 32 */     super("mysql");
/*    */   }
/*    */   
/*    */   public Boolean getChain() {
/* 36 */     return this.chain;
/*    */   }
/*    */   
/*    */   public void setChain(Boolean chain) {
/* 40 */     this.chain = chain;
/*    */   }
/*    */   
/*    */   public Boolean getRelease() {
/* 44 */     return this.release;
/*    */   }
/*    */   
/*    */   public void setRelease(Boolean release) {
/* 48 */     this.release = release;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 53 */     if (visitor instanceof MySqlASTVisitor) {
/* 54 */       accept0((MySqlASTVisitor)visitor);
/*    */     } else {
/* 56 */       throw new IllegalArgumentException("not support visitor type : " + visitor.getClass().getName());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 62 */     if (visitor.visit(this)) {
/* 63 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getTo());
/* 64 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getForce());
/*    */     } 
/*    */     
/* 67 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLExpr getForce() {
/* 71 */     return this.force;
/*    */   }
/*    */   
/*    */   public void setForce(SQLExpr force) {
/* 75 */     this.force = force;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlRollbackStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */