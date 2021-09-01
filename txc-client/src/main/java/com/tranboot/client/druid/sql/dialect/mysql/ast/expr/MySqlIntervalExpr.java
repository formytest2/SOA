/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*    */ public class MySqlIntervalExpr
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
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 49 */     this.value.output(buf);
/* 50 */     buf.append(' ');
/* 51 */     buf.append(this.unit.name());
/*    */   }
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 55 */     MySqlASTVisitor mysqlVisitor = (MySqlASTVisitor)visitor;
/* 56 */     if (mysqlVisitor.visit(this)) {
/* 57 */       acceptChild(visitor, (SQLObject)this.value);
/*    */     }
/* 59 */     mysqlVisitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 64 */     int prime = 31;
/* 65 */     int result = 1;
/* 66 */     result = 31 * result + ((this.unit == null) ? 0 : this.unit.hashCode());
/* 67 */     result = 31 * result + ((this.value == null) ? 0 : this.value.hashCode());
/* 68 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 73 */     if (this == obj) {
/* 74 */       return true;
/*    */     }
/* 76 */     if (obj == null) {
/* 77 */       return false;
/*    */     }
/* 79 */     if (getClass() != obj.getClass()) {
/* 80 */       return false;
/*    */     }
/* 82 */     MySqlIntervalExpr other = (MySqlIntervalExpr)obj;
/* 83 */     if (this.unit != other.unit) {
/* 84 */       return false;
/*    */     }
/* 86 */     if (this.value == null) {
/* 87 */       if (other.value != null) {
/* 88 */         return false;
/*    */       }
/* 90 */     } else if (!this.value.equals(other.value)) {
/* 91 */       return false;
/*    */     } 
/* 93 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\expr\MySqlIntervalExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */