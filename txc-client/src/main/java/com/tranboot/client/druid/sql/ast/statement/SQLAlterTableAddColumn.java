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
/*    */ public class SQLAlterTableAddColumn
/*    */   extends SQLObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/* 27 */   private final List<SQLColumnDefinition> columns = new ArrayList<>();
/*    */ 
/*    */   
/*    */   private SQLName firstColumn;
/*    */   
/*    */   private SQLName afterColumn;
/*    */   
/*    */   private boolean first;
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 38 */     if (visitor.visit(this)) {
/* 39 */       acceptChild(visitor, this.columns);
/*    */     }
/* 41 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public List<SQLColumnDefinition> getColumns() {
/* 45 */     return this.columns;
/*    */   }
/*    */   
/*    */   public void addColumn(SQLColumnDefinition column) {
/* 49 */     if (column != null) {
/* 50 */       column.setParent(this);
/*    */     }
/* 52 */     this.columns.add(column);
/*    */   }
/*    */   
/*    */   public SQLName getFirstColumn() {
/* 56 */     return this.firstColumn;
/*    */   }
/*    */   
/*    */   public void setFirstColumn(SQLName first) {
/* 60 */     this.firstColumn = first;
/*    */   }
/*    */   
/*    */   public boolean isFirst() {
/* 64 */     return this.first;
/*    */   }
/*    */   
/*    */   public void setFirst(boolean first) {
/* 68 */     this.first = first;
/*    */   }
/*    */   
/*    */   public SQLName getAfterColumn() {
/* 72 */     return this.afterColumn;
/*    */   }
/*    */   
/*    */   public void setAfterColumn(SQLName after) {
/* 76 */     this.afterColumn = after;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterTableAddColumn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */