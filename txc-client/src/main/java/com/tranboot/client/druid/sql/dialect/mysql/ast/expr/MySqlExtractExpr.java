/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
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
/*    */ public class MySqlExtractExpr
/*    */   extends SQLExprImpl
/*    */   implements MySqlExpr
/*    */ {
/*    */   private SQLExpr value;
/*    */   private MySqlIntervalUnit unit;
/*    */   
/*    */   public SQLExpr getValue() {
/* 32 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(SQLExpr value) {
/* 36 */     this.value = value;
/*    */   }
/*    */   
/*    */   public MySqlIntervalUnit getUnit() {
/* 40 */     return this.unit;
/*    */   }
/*    */   
/*    */   public void setUnit(MySqlIntervalUnit unit) {
/* 44 */     this.unit = unit;
/*    */   }
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 48 */     MySqlASTVisitor mysqlVisitor = (MySqlASTVisitor)visitor;
/* 49 */     mysqlVisitor.visit(this);
/* 50 */     mysqlVisitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 55 */     int prime = 31;
/* 56 */     int result = 1;
/* 57 */     result = 31 * result + ((this.unit == null) ? 0 : this.unit.hashCode());
/* 58 */     result = 31 * result + ((this.value == null) ? 0 : this.value.hashCode());
/* 59 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 64 */     if (this == obj) {
/* 65 */       return true;
/*    */     }
/* 67 */     if (obj == null) {
/* 68 */       return false;
/*    */     }
/* 70 */     if (!(obj instanceof MySqlExtractExpr)) {
/* 71 */       return false;
/*    */     }
/* 73 */     MySqlExtractExpr other = (MySqlExtractExpr)obj;
/* 74 */     if (this.unit != other.unit) {
/* 75 */       return false;
/*    */     }
/* 77 */     if (this.value == null) {
/* 78 */       if (other.value != null) {
/* 79 */         return false;
/*    */       }
/* 81 */     } else if (!this.value.equals(other.value)) {
/* 82 */       return false;
/*    */     } 
/* 84 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\expr\MySqlExtractExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */