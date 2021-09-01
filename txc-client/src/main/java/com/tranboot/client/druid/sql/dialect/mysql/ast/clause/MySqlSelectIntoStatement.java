/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.clause;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLSelect;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlStatementImpl;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

import java.util.ArrayList;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MySqlSelectIntoStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/*    */   private SQLSelect select;
/* 34 */   private List<SQLExpr> varList = new ArrayList<>();
/*    */   
/*    */   public SQLSelect getSelect() {
/* 37 */     return this.select;
/*    */   }
/*    */   
/*    */   public void setSelect(SQLSelect select) {
/* 41 */     this.select = select;
/*    */   }
/*    */   
/*    */   public List<SQLExpr> getVarList() {
/* 45 */     return this.varList;
/*    */   }
/*    */   
/*    */   public void setVarList(List<SQLExpr> varList) {
/* 49 */     this.varList = varList;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 56 */     if (visitor.visit(this)) {
/* 57 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.select);
/* 58 */       acceptChild((SQLASTVisitor)visitor, this.varList);
/*    */     } 
/* 60 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\clause\MySqlSelectIntoStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */