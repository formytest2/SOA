/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
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
/*    */ public class OracleIsSetExpr
/*    */   extends SQLExprImpl
/*    */   implements OracleExpr
/*    */ {
/*    */   private SQLExpr nestedTable;
/*    */   
/*    */   public OracleIsSetExpr() {}
/*    */   
/*    */   public OracleIsSetExpr(SQLExpr nestedTable) {
/* 31 */     this.nestedTable = nestedTable;
/*    */   }
/*    */   
/*    */   public SQLExpr getNestedTable() {
/* 35 */     return this.nestedTable;
/*    */   }
/*    */   
/*    */   public void setNestedTable(SQLExpr nestedTable) {
/* 39 */     this.nestedTable = nestedTable;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 44 */     accept0((OracleASTVisitor)visitor);
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 49 */     if (visitor.visit(this)) {
/* 50 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.nestedTable);
/*    */     }
/* 52 */     visitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 57 */     int prime = 31;
/* 58 */     int result = 1;
/* 59 */     result = 31 * result + ((this.nestedTable == null) ? 0 : this.nestedTable.hashCode());
/* 60 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 65 */     if (this == obj) {
/* 66 */       return true;
/*    */     }
/* 68 */     if (obj == null) {
/* 69 */       return false;
/*    */     }
/* 71 */     if (getClass() != obj.getClass()) {
/* 72 */       return false;
/*    */     }
/* 74 */     OracleIsSetExpr other = (OracleIsSetExpr)obj;
/* 75 */     if (this.nestedTable == null) {
/* 76 */       if (other.nestedTable != null) {
/* 77 */         return false;
/*    */       }
/* 79 */     } else if (!this.nestedTable.equals(other.nestedTable)) {
/* 80 */       return false;
/*    */     } 
/* 82 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\expr\OracleIsSetExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */