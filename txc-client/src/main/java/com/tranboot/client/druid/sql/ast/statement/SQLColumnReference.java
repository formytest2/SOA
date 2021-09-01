/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*    */ public class SQLColumnReference
/*    */   extends SQLConstraintImpl
/*    */   implements SQLColumnConstraint
/*    */ {
/*    */   private SQLName table;
/* 27 */   private List<SQLName> columns = new ArrayList<>();
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 31 */     if (visitor.visit(this)) {
/* 32 */       acceptChild(visitor, (SQLObject)getName());
/*    */     }
/* 34 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLName getTable() {
/* 38 */     return this.table;
/*    */   }
/*    */   
/*    */   public void setTable(SQLName table) {
/* 42 */     this.table = table;
/*    */   }
/*    */   
/*    */   public List<SQLName> getColumns() {
/* 46 */     return this.columns;
/*    */   }
/*    */   
/*    */   public void setColumns(List<SQLName> columns) {
/* 50 */     this.columns = columns;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLColumnReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */