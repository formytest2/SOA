/*    */ package com.tranboot.client.druid.sql.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*    */ public class SQLListExpr
/*    */   extends SQLExprImpl
/*    */ {
/* 27 */   private final List<SQLExpr> items = new ArrayList<>();
/*    */   
/*    */   public List<SQLExpr> getItems() {
/* 30 */     return this.items;
/*    */   }
/*    */   
/*    */   public void addItem(SQLExpr item) {
/* 34 */     if (item != null) {
/* 35 */       item.setParent((SQLObject)this);
/*    */     }
/* 37 */     this.items.add(item);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 42 */     if (visitor.visit(this)) {
/* 43 */       acceptChild(visitor, this.items);
/*    */     }
/* 45 */     visitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 50 */     int prime = 31;
/* 51 */     int result = 1;
/* 52 */     result = 31 * result + ((this.items == null) ? 0 : this.items.hashCode());
/* 53 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 58 */     if (this == obj) {
/* 59 */       return true;
/*    */     }
/* 61 */     if (obj == null) {
/* 62 */       return false;
/*    */     }
/* 64 */     if (getClass() != obj.getClass()) {
/* 65 */       return false;
/*    */     }
/* 67 */     SQLListExpr other = (SQLListExpr)obj;
/* 68 */     if (this.items == null) {
/* 69 */       if (other.items != null) {
/* 70 */         return false;
/*    */       }
/* 72 */     } else if (!this.items.equals(other.items)) {
/* 73 */       return false;
/*    */     } 
/* 75 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLListExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */