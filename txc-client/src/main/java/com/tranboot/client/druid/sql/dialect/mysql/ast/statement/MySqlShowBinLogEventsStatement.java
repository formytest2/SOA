/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLLimit;
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
/*    */ public class MySqlShowBinLogEventsStatement
/*    */   extends MySqlStatementImpl
/*    */   implements MySqlShowStatement
/*    */ {
/*    */   private SQLExpr in;
/*    */   private SQLExpr from;
/*    */   private SQLLimit limit;
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 29 */     if (visitor.visit(this)) {
/* 30 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.in);
/* 31 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.from);
/* 32 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.limit);
/*    */     } 
/* 34 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLExpr getIn() {
/* 38 */     return this.in;
/*    */   }
/*    */   
/*    */   public void setIn(SQLExpr in) {
/* 42 */     this.in = in;
/*    */   }
/*    */   
/*    */   public SQLExpr getFrom() {
/* 46 */     return this.from;
/*    */   }
/*    */   
/*    */   public void setFrom(SQLExpr from) {
/* 50 */     this.from = from;
/*    */   }
/*    */   
/*    */   public SQLLimit getLimit() {
/* 54 */     return this.limit;
/*    */   }
/*    */   
/*    */   public void setLimit(SQLLimit limit) {
/* 58 */     if (limit != null) {
/* 59 */       limit.setParent((SQLObject)this);
/*    */     }
/* 61 */     this.limit = limit;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlShowBinLogEventsStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */