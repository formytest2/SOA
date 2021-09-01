/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLCommentHint;
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
/*    */ public class MySqlHintStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/*    */   private List<SQLCommentHint> hints;
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 28 */     if (visitor.visit(this)) {
/* 29 */       acceptChild((SQLASTVisitor)visitor, this.hints);
/*    */     }
/* 31 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public List<SQLCommentHint> getHints() {
/* 35 */     return this.hints;
/*    */   }
/*    */   
/*    */   public void setHints(List<SQLCommentHint> hints) {
/* 39 */     this.hints = hints;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlHintStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */