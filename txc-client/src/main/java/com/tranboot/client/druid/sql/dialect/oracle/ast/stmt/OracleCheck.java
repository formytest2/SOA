/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLCheck;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleSQLObject;
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
/*    */ public class OracleCheck
/*    */   extends SQLCheck
/*    */   implements OracleConstraint, OracleSQLObject
/*    */ {
/*    */   private OracleUsingIndexClause using;
/*    */   private SQLName exceptionsInto;
/*    */   private OracleConstraint.Initially initially;
/*    */   private Boolean deferrable;
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 33 */     if (visitor instanceof OracleASTVisitor) {
/* 34 */       accept0((OracleASTVisitor)visitor);
/*    */       
/*    */       return;
/*    */     } 
/* 38 */     accept(visitor);
/*    */   }
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 42 */     if (visitor.visit(this)) {
/* 43 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getName());
/* 44 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getExpr());
/*    */       
/* 46 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.using);
/* 47 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.exceptionsInto);
/*    */     } 
/* 49 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public Boolean getDeferrable() {
/* 53 */     return this.deferrable;
/*    */   }
/*    */   
/*    */   public void setDeferrable(Boolean deferrable) {
/* 57 */     this.deferrable = deferrable;
/*    */   }
/*    */   
/*    */   public OracleConstraint.Initially getInitially() {
/* 61 */     return this.initially;
/*    */   }
/*    */   
/*    */   public void setInitially(OracleConstraint.Initially initially) {
/* 65 */     this.initially = initially;
/*    */   }
/*    */   
/*    */   public SQLName getExceptionsInto() {
/* 69 */     return this.exceptionsInto;
/*    */   }
/*    */   
/*    */   public void setExceptionsInto(SQLName exceptionsInto) {
/* 73 */     this.exceptionsInto = exceptionsInto;
/*    */   }
/*    */   
/*    */   public OracleUsingIndexClause getUsing() {
/* 77 */     return this.using;
/*    */   }
/*    */   
/*    */   public void setUsing(OracleUsingIndexClause using) {
/* 81 */     if (using != null) {
/* 82 */       using.setParent((SQLObject)this);
/*    */     }
/* 84 */     this.using = using;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */