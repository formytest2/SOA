/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLUnique;
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
/*    */ public class OracleUnique
/*    */   extends SQLUnique
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
/* 44 */       acceptChild((SQLASTVisitor)visitor, getColumns());
/* 45 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.using);
/* 46 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.exceptionsInto);
/*    */     } 
/* 48 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public Boolean getDeferrable() {
/* 52 */     return this.deferrable;
/*    */   }
/*    */   
/*    */   public void setDeferrable(Boolean deferrable) {
/* 56 */     this.deferrable = deferrable;
/*    */   }
/*    */   
/*    */   public SQLName getExceptionsInto() {
/* 60 */     return this.exceptionsInto;
/*    */   }
/*    */   
/*    */   public void setExceptionsInto(SQLName exceptionsInto) {
/* 64 */     this.exceptionsInto = exceptionsInto;
/*    */   }
/*    */   
/*    */   public OracleUsingIndexClause getUsing() {
/* 68 */     return this.using;
/*    */   }
/*    */   
/*    */   public void setUsing(OracleUsingIndexClause using) {
/* 72 */     if (using != null) {
/* 73 */       using.setParent((SQLObject)this);
/*    */     }
/* 75 */     this.using = using;
/*    */   }
/*    */   
/*    */   public OracleConstraint.Initially getInitially() {
/* 79 */     return this.initially;
/*    */   }
/*    */   
/*    */   public void setInitially(OracleConstraint.Initially initially) {
/* 83 */     this.initially = initially;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleUnique.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */