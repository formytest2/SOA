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
/*    */ public class MySqlShowProcedureStatusStatement
/*    */   extends MySqlStatementImpl
/*    */   implements MySqlShowStatement
/*    */ {
/*    */   private SQLExpr like;
/*    */   private SQLExpr where;
/*    */   
/*    */   public SQLExpr getLike() {
/* 27 */     return this.like;
/*    */   }
/*    */   
/*    */   public void setLike(SQLExpr like) {
/* 31 */     this.like = like;
/*    */   }
/*    */   
/*    */   public SQLExpr getWhere() {
/* 35 */     return this.where;
/*    */   }
/*    */   
/*    */   public void setWhere(SQLExpr where) {
/* 39 */     this.where = where;
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 43 */     if (visitor.visit(this)) {
/* 44 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.like);
/* 45 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.where);
/*    */     } 
/* 47 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlShowProcedureStatusStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */