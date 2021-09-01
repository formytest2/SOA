/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLPropertyExpr;
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
/*    */ public class MySqlShowKeysStatement
/*    */   extends MySqlStatementImpl
/*    */   implements MySqlShowStatement
/*    */ {
/*    */   private SQLName table;
/*    */   private SQLName database;
/*    */   
/*    */   public SQLName getTable() {
/* 29 */     return this.table;
/*    */   }
/*    */   
/*    */   public void setTable(SQLName table) {
/* 33 */     if (table instanceof SQLPropertyExpr) {
/* 34 */       SQLPropertyExpr propExpr = (SQLPropertyExpr)table;
/* 35 */       setDatabase((SQLName)propExpr.getOwner());
/* 36 */       this.table = (SQLName)new SQLIdentifierExpr(propExpr.getName());
/*    */       return;
/*    */     } 
/* 39 */     this.table = table;
/*    */   }
/*    */   
/*    */   public SQLName getDatabase() {
/* 43 */     return this.database;
/*    */   }
/*    */   
/*    */   public void setDatabase(SQLName database) {
/* 47 */     this.database = database;
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 51 */     if (visitor.visit(this)) {
/* 52 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.table);
/* 53 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.database);
/*    */     } 
/* 55 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlShowKeysStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */