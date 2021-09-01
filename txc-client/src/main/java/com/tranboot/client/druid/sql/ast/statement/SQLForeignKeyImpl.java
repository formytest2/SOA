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
/*    */ public class SQLForeignKeyImpl
/*    */   extends SQLConstraintImpl
/*    */   implements SQLForeignKeyConstraint
/*    */ {
/*    */   private SQLName referencedTableName;
/* 27 */   private List<SQLName> referencingColumns = new ArrayList<>();
/* 28 */   private List<SQLName> referencedColumns = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<SQLName> getReferencingColumns() {
/* 36 */     return this.referencingColumns;
/*    */   }
/*    */ 
/*    */   
/*    */   public SQLName getReferencedTableName() {
/* 41 */     return this.referencedTableName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setReferencedTableName(SQLName value) {
/* 46 */     this.referencedTableName = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<SQLName> getReferencedColumns() {
/* 51 */     return this.referencedColumns;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 56 */     if (visitor.visit(this)) {
/* 57 */       acceptChild(visitor, (SQLObject)getName());
/* 58 */       acceptChild(visitor, (SQLObject)getReferencedTableName());
/* 59 */       acceptChild(visitor, getReferencingColumns());
/* 60 */       acceptChild(visitor, getReferencedColumns());
/*    */     } 
/* 62 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLForeignKeyImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */