/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.SQLUtils;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLSelect;
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
/*    */ public class OracleSelect
/*    */   extends SQLSelect
/*    */ {
/*    */   private OracleSelectForUpdate forUpdate;
/*    */   private OracleSelectRestriction restriction;
/*    */   
/*    */   public OracleSelectRestriction getRestriction() {
/* 33 */     return this.restriction;
/*    */   }
/*    */   
/*    */   public void setRestriction(OracleSelectRestriction restriction) {
/* 37 */     this.restriction = restriction;
/*    */   }
/*    */   
/*    */   public OracleSelectForUpdate getForUpdate() {
/* 41 */     return this.forUpdate;
/*    */   }
/*    */   
/*    */   public void setForUpdate(OracleSelectForUpdate forUpdate) {
/* 45 */     this.forUpdate = forUpdate;
/*    */   }
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 49 */     this.query.output(buf);
/* 50 */     buf.append(" ");
/*    */     
/* 52 */     if (this.orderBy != null) {
/* 53 */       this.orderBy.output(buf);
/*    */     }
/*    */   }
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 58 */     accept0((OracleASTVisitor)visitor);
/*    */   }
/*    */   
/*    */   protected void accept0(OracleASTVisitor visitor) {
/* 62 */     if (visitor.visit(this)) {
/* 63 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.withSubQuery);
/* 64 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.query);
/* 65 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.restriction);
/* 66 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.orderBy);
/* 67 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.forUpdate);
/*    */     } 
/* 69 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 73 */     return SQLUtils.toOracleString((SQLObject)this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleSelect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */