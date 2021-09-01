/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.clause;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLDeclareItem;
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
/*    */ public class MySqlDeclareStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/* 30 */   private List<SQLDeclareItem> varList = new ArrayList<>();
/*    */   
/*    */   public List<SQLDeclareItem> getVarList() {
/* 33 */     return this.varList;
/*    */   }
/*    */   
/*    */   public void addVar(SQLDeclareItem expr) {
/* 37 */     this.varList.add(expr);
/*    */   }
/*    */   
/*    */   public void setVarList(List<SQLDeclareItem> varList) {
/* 41 */     this.varList = varList;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 47 */     if (visitor.visit(this)) {
/* 48 */       acceptChild((SQLASTVisitor)visitor, this.varList);
/*    */     }
/* 50 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\clause\MySqlDeclareStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */