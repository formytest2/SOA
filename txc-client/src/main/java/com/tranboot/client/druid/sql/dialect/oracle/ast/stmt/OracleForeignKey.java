/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLForeignKeyImpl;
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
/*    */ public class OracleForeignKey
/*    */   extends SQLForeignKeyImpl
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
/* 44 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getReferencedTableName());
/* 45 */       acceptChild((SQLASTVisitor)visitor, getReferencingColumns());
/* 46 */       acceptChild((SQLASTVisitor)visitor, getReferencedColumns());
/*    */       
/* 48 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.using);
/* 49 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.exceptionsInto);
/*    */     } 
/* 51 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public Boolean getDeferrable() {
/* 55 */     return this.deferrable;
/*    */   }
/*    */   
/*    */   public void setDeferrable(Boolean deferrable) {
/* 59 */     this.deferrable = deferrable;
/*    */   }
/*    */   
/*    */   public OracleConstraint.Initially getInitially() {
/* 63 */     return this.initially;
/*    */   }
/*    */   
/*    */   public void setInitially(OracleConstraint.Initially initially) {
/* 67 */     this.initially = initially;
/*    */   }
/*    */   
/*    */   public SQLName getExceptionsInto() {
/* 71 */     return this.exceptionsInto;
/*    */   }
/*    */   
/*    */   public void setExceptionsInto(SQLName exceptionsInto) {
/* 75 */     this.exceptionsInto = exceptionsInto;
/*    */   }
/*    */   
/*    */   public OracleUsingIndexClause getUsing() {
/* 79 */     return this.using;
/*    */   }
/*    */   
/*    */   public void setUsing(OracleUsingIndexClause using) {
/* 83 */     if (using != null) {
/* 84 */       using.setParent((SQLObject)this);
/*    */     }
/* 86 */     this.using = using;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleForeignKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */