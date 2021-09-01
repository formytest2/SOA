/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

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
/*    */ public class MySqlShowErrorsStatement
/*    */   extends MySqlStatementImpl
/*    */   implements MySqlShowStatement
/*    */ {
/*    */   private boolean count = false;
/*    */   private SQLLimit limit;
/*    */   
/*    */   public boolean isCount() {
/* 27 */     return this.count;
/*    */   }
/*    */   
/*    */   public void setCount(boolean count) {
/* 31 */     this.count = count;
/*    */   }
/*    */   
/*    */   public SQLLimit getLimit() {
/* 35 */     return this.limit;
/*    */   }
/*    */   
/*    */   public void setLimit(SQLLimit limit) {
/* 39 */     this.limit = limit;
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 43 */     if (visitor.visit(this)) {
/* 44 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.limit);
/*    */     }
/* 46 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlShowErrorsStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */