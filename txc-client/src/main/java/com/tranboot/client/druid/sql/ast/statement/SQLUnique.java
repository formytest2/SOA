/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
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
/*    */ public class SQLUnique
/*    */   extends SQLConstraintImpl
/*    */   implements SQLUniqueConstraint, SQLTableElement
/*    */ {
/* 26 */   private final List<SQLExpr> columns = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<SQLExpr> getColumns() {
/* 33 */     return this.columns;
/*    */   }
/*    */   
/*    */   public void addColumn(SQLExpr column) {
/* 37 */     if (column != null) {
/* 38 */       column.setParent(this);
/*    */     }
/* 40 */     this.columns.add(column);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 45 */     if (visitor.visit(this)) {
/* 46 */       acceptChild(visitor, (SQLObject)getName());
/* 47 */       acceptChild(visitor, getColumns());
/*    */     } 
/* 49 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLUnique.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */