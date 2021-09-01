/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.statement.SQLAssignItem;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

import java.util.ArrayList;
import java.util.List;

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
/*    */ public class OracleAlterSessionStatement
/*    */   extends OracleStatementImpl
/*    */ {
/* 26 */   private List<SQLAssignItem> items = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 30 */     if (visitor.visit(this)) {
/* 31 */       acceptChild((SQLASTVisitor)visitor, this.items);
/*    */     }
/* 33 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public List<SQLAssignItem> getItems() {
/* 37 */     return this.items;
/*    */   }
/*    */   
/*    */   public void setItems(List<SQLAssignItem> items) {
/* 41 */     this.items = items;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleAlterSessionStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */