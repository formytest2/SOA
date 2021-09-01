/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLObjectImpl;
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
/*    */ public class SQLUpdateSetItem
/*    */   extends SQLObjectImpl
/*    */ {
/*    */   private SQLExpr column;
/*    */   private SQLExpr value;
/*    */   
/*    */   public SQLExpr getColumn() {
/* 32 */     return this.column;
/*    */   }
/*    */   
/*    */   public void setColumn(SQLExpr column) {
/* 36 */     this.column = column;
/*    */   }
/*    */   
/*    */   public SQLExpr getValue() {
/* 40 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(SQLExpr value) {
/* 44 */     this.value = value;
/*    */   }
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 48 */     this.column.output(buf);
/* 49 */     buf.append(" = ");
/* 50 */     this.value.output(buf);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 55 */     if (visitor.visit(this)) {
/* 56 */       acceptChild(visitor, (SQLObject)this.column);
/* 57 */       acceptChild(visitor, (SQLObject)this.value);
/*    */     } 
/*    */     
/* 60 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLUpdateSetItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */