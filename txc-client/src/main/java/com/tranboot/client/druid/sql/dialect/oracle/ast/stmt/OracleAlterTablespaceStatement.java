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
/*    */ public class OracleAlterTablespaceStatement
/*    */   extends OracleStatementImpl
/*    */ {
/*    */   private SQLName name;
/*    */   private OracleAlterTablespaceItem item;
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 28 */     if (visitor.visit(this)) {
/* 29 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.name);
/* 30 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.item);
/*    */     } 
/* 32 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLName getName() {
/* 36 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(SQLName name) {
/* 40 */     this.name = name;
/*    */   }
/*    */   
/*    */   public OracleAlterTablespaceItem getItem() {
/* 44 */     return this.item;
/*    */   }
/*    */   
/*    */   public void setItem(OracleAlterTablespaceItem item) {
/* 48 */     this.item = item;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleAlterTablespaceStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */