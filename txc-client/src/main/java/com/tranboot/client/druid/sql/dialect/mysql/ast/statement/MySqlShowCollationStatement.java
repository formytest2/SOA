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
/*    */ public class MySqlShowCollationStatement
/*    */   extends MySqlStatementImpl
/*    */   implements MySqlShowStatement
/*    */ {
/*    */   private SQLExpr where;
/*    */   private SQLExpr pattern;
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 27 */     if (visitor.visit(this)) {
/* 28 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.where);
/* 29 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.pattern);
/*    */     } 
/* 31 */     visitor.endVisit(this);
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
/*    */   public SQLExpr getPattern() {
/* 43 */     return this.pattern;
/*    */   }
/*    */   
/*    */   public void setPattern(SQLExpr pattern) {
/* 47 */     this.pattern = pattern;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlShowCollationStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */