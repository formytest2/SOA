/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*    */ public class MySqlExecuteStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/*    */   private SQLName statementName;
/* 28 */   private final List<SQLExpr> parameters = new ArrayList<>();
/*    */   
/*    */   public SQLName getStatementName() {
/* 31 */     return this.statementName;
/*    */   }
/*    */   
/*    */   public void setStatementName(SQLName statementName) {
/* 35 */     this.statementName = statementName;
/*    */   }
/*    */   
/*    */   public List<SQLExpr> getParameters() {
/* 39 */     return this.parameters;
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 43 */     if (visitor.visit(this)) {
/* 44 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.statementName);
/* 45 */       acceptChild((SQLASTVisitor)visitor, this.parameters);
/*    */     } 
/* 47 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlExecuteStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */