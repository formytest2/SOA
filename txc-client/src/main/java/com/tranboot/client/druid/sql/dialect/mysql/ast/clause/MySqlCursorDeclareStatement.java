/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.clause;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlStatementImpl;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MySqlCursorDeclareStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/*    */   private String cursorName;
/*    */   private SQLSelectStatement select;
/*    */   
/*    */   public String getCursorName() {
/* 33 */     return this.cursorName;
/*    */   }
/*    */   
/*    */   public void setCursorName(String cursorName) {
/* 37 */     this.cursorName = cursorName;
/*    */   }
/*    */   
/*    */   public SQLSelectStatement getSelect() {
/* 41 */     return this.select;
/*    */   }
/*    */   
/*    */   public void setSelect(SQLSelectStatement select) {
/* 45 */     this.select = select;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 51 */     if (visitor.visit(this)) {
/* 52 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.select);
/*    */     }
/* 54 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\clause\MySqlCursorDeclareStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */