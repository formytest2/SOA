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
/*    */ public class MySqlShowEngineStatement
/*    */   extends MySqlStatementImpl
/*    */   implements MySqlShowStatement
/*    */ {
/*    */   private SQLExpr name;
/*    */   private Option option;
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 27 */     if (visitor.visit(this)) {
/* 28 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.name);
/*    */     }
/* 30 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLExpr getName() {
/* 34 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(SQLExpr name) {
/* 38 */     this.name = name;
/*    */   }
/*    */   
/*    */   public Option getOption() {
/* 42 */     return this.option;
/*    */   }
/*    */   
/*    */   public void setOption(Option option) {
/* 46 */     this.option = option;
/*    */   }
/*    */   
/*    */   public enum Option {
/* 50 */     STATUS, MUTEX;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlShowEngineStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */