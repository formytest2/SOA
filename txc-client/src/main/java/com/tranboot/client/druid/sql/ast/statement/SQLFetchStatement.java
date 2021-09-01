/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatementImpl;
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
/*    */ public class SQLFetchStatement
/*    */   extends SQLStatementImpl
/*    */ {
/*    */   private SQLName cursorName;
/* 30 */   private List<SQLExpr> into = new ArrayList<>();
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 34 */     if (visitor.visit(this)) {
/* 35 */       acceptChild(visitor, (SQLObject)this.cursorName);
/* 36 */       acceptChild(visitor, this.into);
/*    */     } 
/* 38 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLName getCursorName() {
/* 42 */     return this.cursorName;
/*    */   }
/*    */   
/*    */   public void setCursorName(SQLName cursorName) {
/* 46 */     this.cursorName = cursorName;
/*    */   }
/*    */   
/*    */   public List<SQLExpr> getInto() {
/* 50 */     return this.into;
/*    */   }
/*    */   
/*    */   public void setInto(List<SQLExpr> into) {
/* 54 */     this.into = into;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLFetchStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */