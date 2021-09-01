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
/*    */ public class SQLGroupingSetExpr
/*    */   extends SQLExprImpl
/*    */ {
/* 27 */   private final List<SQLExpr> parameters = new ArrayList<>();
/*    */   
/*    */   public List<SQLExpr> getParameters() {
/* 30 */     return this.parameters;
/*    */   }
/*    */   
/*    */   public void addParameter(SQLExpr parameter) {
/* 34 */     if (parameter != null) {
/* 35 */       parameter.setParent((SQLObject)this);
/*    */     }
/* 37 */     this.parameters.add(parameter);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 42 */     if (visitor.visit(this)) {
/* 43 */       acceptChild(visitor, this.parameters);
/*    */     }
/* 45 */     visitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 50 */     int prime = 31;
/* 51 */     int result = 1;
/* 52 */     result = 31 * result + ((this.parameters == null) ? 0 : this.parameters.hashCode());
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
/* 64 */     if (!(obj instanceof SQLGroupingSetExpr)) {
/* 65 */       return false;
/*    */     }
/* 67 */     SQLGroupingSetExpr other = (SQLGroupingSetExpr)obj;
/* 68 */     if (this.parameters == null) {
/* 69 */       if (other.parameters != null) {
/* 70 */         return false;
/*    */       }
/* 72 */     } else if (!this.parameters.equals(other.parameters)) {
/* 73 */       return false;
/*    */     } 
/* 75 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLGroupingSetExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */