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
/*    */ public class SQLSequenceExpr
/*    */   extends SQLExprImpl
/*    */ {
/*    */   private SQLName sequence;
/*    */   private Function function;
/*    */   
/*    */   public SQLSequenceExpr() {}
/*    */   
/*    */   public SQLSequenceExpr(SQLName sequence, Function function) {
/* 32 */     this.sequence = sequence;
/* 33 */     this.function = function;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 38 */     if (visitor.visit(this)) {
/* 39 */       acceptChild(visitor, (SQLObject)this.sequence);
/*    */     }
/* 41 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public enum Function {
/* 45 */     NextVal("NEXTVAL"), CurrVal("CURRVAL"), PrevVal("PREVVAL");
/*    */     
/*    */     public final String name_lcase;
/*    */     public final String name;
/*    */     
/*    */     Function(String name) {
/* 51 */       this.name = name;
/* 52 */       this.name_lcase = name.toLowerCase();
/*    */     }
/*    */   }
/*    */   
/*    */   public SQLName getSequence() {
/* 57 */     return this.sequence;
/*    */   }
/*    */   
/*    */   public void setSequence(SQLName sequence) {
/* 61 */     this.sequence = sequence;
/*    */   }
/*    */   
/*    */   public Function getFunction() {
/* 65 */     return this.function;
/*    */   }
/*    */   
/*    */   public void setFunction(Function function) {
/* 69 */     this.function = function;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 74 */     int prime = 31;
/* 75 */     int result = 1;
/* 76 */     result = 31 * result + ((this.function == null) ? 0 : this.function.hashCode());
/* 77 */     result = 31 * result + ((this.sequence == null) ? 0 : this.sequence.hashCode());
/* 78 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 83 */     if (this == obj) return true; 
/* 84 */     if (obj == null) return false; 
/* 85 */     if (getClass() != obj.getClass()) return false; 
/* 86 */     SQLSequenceExpr other = (SQLSequenceExpr)obj;
/* 87 */     if (this.function != other.function) return false; 
/* 88 */     if (this.sequence == null)
/* 89 */     { if (other.sequence != null) return false;  }
/* 90 */     else if (!this.sequence.equals(other.sequence)) { return false; }
/* 91 */      return true;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLSequenceExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */