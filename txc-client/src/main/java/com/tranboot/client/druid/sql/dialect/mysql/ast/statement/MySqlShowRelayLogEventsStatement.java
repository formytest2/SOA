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
/*    */ public class MySqlShowRelayLogEventsStatement
/*    */   extends MySqlStatementImpl
/*    */   implements MySqlShowStatement
/*    */ {
/*    */   private SQLExpr logName;
/*    */   private SQLExpr from;
/*    */   private SQLLimit limit;
/*    */   
/*    */   public SQLExpr getLogName() {
/* 29 */     return this.logName;
/*    */   }
/*    */   
/*    */   public void setLogName(SQLExpr logName) {
/* 33 */     this.logName = logName;
/*    */   }
/*    */   
/*    */   public SQLExpr getFrom() {
/* 37 */     return this.from;
/*    */   }
/*    */   
/*    */   public void setFrom(SQLExpr from) {
/* 41 */     this.from = from;
/*    */   }
/*    */   
/*    */   public SQLLimit getLimit() {
/* 45 */     return this.limit;
/*    */   }
/*    */   
/*    */   public void setLimit(SQLLimit limit) {
/* 49 */     this.limit = limit;
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 53 */     if (visitor.visit(this)) {
/* 54 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.logName);
/* 55 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.from);
/* 56 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.limit);
/*    */     } 
/* 58 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlShowRelayLogEventsStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */