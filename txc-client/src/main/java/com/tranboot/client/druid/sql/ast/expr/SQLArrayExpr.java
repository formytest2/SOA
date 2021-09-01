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
/*    */ 
/*    */ public class SQLArrayExpr
/*    */   extends SQLExprImpl
/*    */ {
/*    */   private SQLExpr expr;
/* 29 */   private List<SQLExpr> values = new ArrayList<>();
/*    */   
/*    */   public SQLExpr getExpr() {
/* 32 */     return this.expr;
/*    */   }
/*    */   
/*    */   public void setExpr(SQLExpr expr) {
/* 36 */     this.expr = expr;
/*    */   }
/*    */   
/*    */   public List<SQLExpr> getValues() {
/* 40 */     return this.values;
/*    */   }
/*    */   
/*    */   public void setValues(List<SQLExpr> values) {
/* 44 */     this.values = values;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 49 */     if (visitor.visit(this)) {
/* 50 */       acceptChild(visitor, (SQLObject)this.expr);
/* 51 */       acceptChild(visitor, this.values);
/*    */     } 
/* 53 */     visitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 58 */     int prime = 31;
/* 59 */     int result = 1;
/* 60 */     result = 31 * result + ((this.expr == null) ? 0 : this.expr.hashCode());
/* 61 */     result = 31 * result + ((this.values == null) ? 0 : this.values.hashCode());
/* 62 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 67 */     if (this == obj) return true; 
/* 68 */     if (obj == null) return false; 
/* 69 */     if (getClass() != obj.getClass()) return false; 
/* 70 */     SQLArrayExpr other = (SQLArrayExpr)obj;
/* 71 */     if (this.expr == null)
/* 72 */     { if (other.expr != null) return false;  }
/* 73 */     else if (!this.expr.equals(other.expr)) { return false; }
/* 74 */      if (this.values == null)
/* 75 */     { if (other.values != null) return false;  }
/* 76 */     else if (!this.values.equals(other.values)) { return false; }
/* 77 */      return true;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLArrayExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */