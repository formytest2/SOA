/*    */ package com.tranboot.client.druid.sql.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*    */ public class SQLCurrentOfCursorExpr
/*    */   extends SQLExprImpl
/*    */ {
/*    */   private SQLName cursorName;
/*    */   
/*    */   public SQLCurrentOfCursorExpr() {}
/*    */   
/*    */   public SQLCurrentOfCursorExpr(SQLName cursorName) {
/* 31 */     this.cursorName = cursorName;
/*    */   }
/*    */   
/*    */   public SQLName getCursorName() {
/* 35 */     return this.cursorName;
/*    */   }
/*    */   
/*    */   public void setCursorName(SQLName cursorName) {
/* 39 */     this.cursorName = cursorName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 44 */     buf.append("CURRENT OF ");
/* 45 */     this.cursorName.output(buf);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 50 */     if (visitor.visit(this)) {
/* 51 */       acceptChild(visitor, (SQLObject)this.cursorName);
/*    */     }
/* 53 */     visitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 58 */     int prime = 31;
/* 59 */     int result = 1;
/* 60 */     result = 31 * result + ((this.cursorName == null) ? 0 : this.cursorName.hashCode());
/* 61 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 66 */     if (this == obj) {
/* 67 */       return true;
/*    */     }
/* 69 */     if (obj == null) {
/* 70 */       return false;
/*    */     }
/* 72 */     if (getClass() != obj.getClass()) {
/* 73 */       return false;
/*    */     }
/* 75 */     SQLCurrentOfCursorExpr other = (SQLCurrentOfCursorExpr)obj;
/* 76 */     if (this.cursorName == null) {
/* 77 */       if (other.cursorName != null) {
/* 78 */         return false;
/*    */       }
/* 80 */     } else if (!this.cursorName.equals(other.cursorName)) {
/* 81 */       return false;
/*    */     } 
/* 83 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLCurrentOfCursorExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */