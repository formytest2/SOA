/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLObjectImpl;
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
/*    */ public class SQLAlterTableAlterColumn
/*    */   extends SQLObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/*    */   private SQLColumnDefinition column;
/*    */   private boolean setNotNull;
/*    */   private boolean dropNotNull;
/*    */   private SQLExpr setDefault;
/*    */   private boolean dropDefault;
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 37 */     if (visitor.visit(this)) {
/* 38 */       acceptChild(visitor, this.column);
/* 39 */       acceptChild(visitor, (SQLObject)this.setDefault);
/*    */     } 
/* 41 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLColumnDefinition getColumn() {
/* 45 */     return this.column;
/*    */   }
/*    */   
/*    */   public void setColumn(SQLColumnDefinition column) {
/* 49 */     this.column = column;
/*    */   }
/*    */   
/*    */   public boolean isSetNotNull() {
/* 53 */     return this.setNotNull;
/*    */   }
/*    */   
/*    */   public void setSetNotNull(boolean setNotNull) {
/* 57 */     this.setNotNull = setNotNull;
/*    */   }
/*    */   
/*    */   public boolean isDropNotNull() {
/* 61 */     return this.dropNotNull;
/*    */   }
/*    */   
/*    */   public void setDropNotNull(boolean dropNotNull) {
/* 65 */     this.dropNotNull = dropNotNull;
/*    */   }
/*    */   
/*    */   public SQLExpr getSetDefault() {
/* 69 */     return this.setDefault;
/*    */   }
/*    */   
/*    */   public void setSetDefault(SQLExpr setDefault) {
/* 73 */     this.setDefault = setDefault;
/*    */   }
/*    */   
/*    */   public boolean isDropDefault() {
/* 77 */     return this.dropDefault;
/*    */   }
/*    */   
/*    */   public void setDropDefault(boolean dropDefault) {
/* 81 */     this.dropDefault = dropDefault;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterTableAlterColumn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */