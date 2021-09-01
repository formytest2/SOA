/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObjectImpl;
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
/*    */ public class SQLAlterTableDropColumnItem
/*    */   extends SQLObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/* 27 */   private List<SQLName> columns = new ArrayList<>();
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean cascade = false;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 37 */     if (visitor.visit(this)) {
/* 38 */       acceptChild(visitor, this.columns);
/*    */     }
/* 40 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public List<SQLName> getColumns() {
/* 44 */     return this.columns;
/*    */   }
/*    */   
/*    */   public void addColumn(SQLName column) {
/* 48 */     if (column != null) {
/* 49 */       column.setParent(this);
/*    */     }
/* 51 */     this.columns.add(column);
/*    */   }
/*    */   
/*    */   public boolean isCascade() {
/* 55 */     return this.cascade;
/*    */   }
/*    */   
/*    */   public void setCascade(boolean cascade) {
/* 59 */     this.cascade = cascade;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterTableDropColumnItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */