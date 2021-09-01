/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class OracleSelectRestriction
/*    */   extends OracleSQLObjectImpl
/*    */ {
/*    */   public static class CheckOption
/*    */     extends OracleSelectRestriction
/*    */   {
/*    */     private OracleConstraint constraint;
/*    */     
/*    */     public OracleConstraint getConstraint() {
/* 36 */       return this.constraint;
/*    */     }
/*    */     
/*    */     public void setConstraint(OracleConstraint constraint) {
/* 40 */       this.constraint = constraint;
/*    */     }
/*    */     
/*    */     public void accept0(OracleASTVisitor visitor) {
/* 44 */       if (visitor.visit(this)) {
/* 45 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.constraint);
/*    */       }
/*    */       
/* 48 */       visitor.endVisit(this);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static class ReadOnly
/*    */     extends OracleSelectRestriction
/*    */   {
/*    */     public void accept0(OracleASTVisitor visitor) {
/* 59 */       visitor.visit(this);
/*    */       
/* 61 */       visitor.endVisit(this);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleSelectRestriction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */