/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLCommentHint;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLPropertyExpr;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

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
/*    */ public class MySqlShowIndexesStatement
/*    */   extends MySqlStatementImpl
/*    */   implements MySqlShowStatement
/*    */ {
/*    */   private SQLName table;
/*    */   private SQLName database;
/*    */   private List<SQLCommentHint> hints;
/*    */   
/*    */   public SQLName getTable() {
/* 33 */     return this.table;
/*    */   }
/*    */   
/*    */   public void setTable(SQLName table) {
/* 37 */     if (table instanceof SQLPropertyExpr) {
/* 38 */       SQLPropertyExpr propExpr = (SQLPropertyExpr)table;
/* 39 */       setDatabase((SQLName)propExpr.getOwner());
/* 40 */       this.table = (SQLName)new SQLIdentifierExpr(propExpr.getName());
/*    */       return;
/*    */     } 
/* 43 */     this.table = table;
/*    */   }
/*    */   
/*    */   public SQLName getDatabase() {
/* 47 */     return this.database;
/*    */   }
/*    */   
/*    */   public void setDatabase(SQLName database) {
/* 51 */     this.database = database;
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 55 */     if (visitor.visit(this)) {
/* 56 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.table);
/* 57 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.database);
/*    */     } 
/* 59 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public List<SQLCommentHint> getHints() {
/* 63 */     return this.hints;
/*    */   }
/*    */   
/*    */   public void setHints(List<SQLCommentHint> hints) {
/* 67 */     this.hints = hints;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlShowIndexesStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */