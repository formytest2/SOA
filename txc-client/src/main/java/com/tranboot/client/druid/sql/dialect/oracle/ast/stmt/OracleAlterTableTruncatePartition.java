/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
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
/*    */ public class OracleAlterTableTruncatePartition
/*    */   extends OracleAlterTableItem
/*    */ {
/*    */   private SQLName name;
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 27 */     if (visitor.visit(this)) {
/* 28 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.name);
/*    */     }
/* 30 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLName getName() {
/* 34 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(SQLName name) {
/* 38 */     this.name = name;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleAlterTableTruncatePartition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */