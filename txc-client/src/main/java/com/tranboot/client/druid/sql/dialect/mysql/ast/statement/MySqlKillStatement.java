/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
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
/*    */ public class MySqlKillStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/*    */   private Type type;
/* 27 */   private List<SQLExpr> threadIds = new ArrayList<>();
/*    */   
/*    */   public enum Type {
/* 30 */     CONNECTION, QUERY;
/*    */   }
/*    */   
/*    */   public Type getType() {
/* 34 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(Type type) {
/* 38 */     this.type = type;
/*    */   }
/*    */   
/*    */   public SQLExpr getThreadId() {
/* 42 */     return this.threadIds.get(0);
/*    */   }
/*    */   
/*    */   public void setThreadId(SQLExpr threadId) {
/* 46 */     this.threadIds.set(0, threadId);
/*    */   }
/*    */   
/*    */   public List<SQLExpr> getThreadIds() {
/* 50 */     return this.threadIds;
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 54 */     if (visitor.visit(this)) {
/* 55 */       acceptChild((SQLASTVisitor)visitor, this.threadIds);
/*    */     }
/* 57 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlKillStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */