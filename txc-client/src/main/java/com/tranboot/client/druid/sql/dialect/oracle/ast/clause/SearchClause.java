/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.clause;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;
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
/*    */ public class SearchClause
/*    */   extends OracleSQLObjectImpl
/*    */ {
/*    */   private Type type;
/*    */   
/*    */   public enum Type
/*    */   {
/* 29 */     DEPTH, BREADTH;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 34 */   private final List<SQLSelectOrderByItem> items = new ArrayList<>();
/*    */   
/*    */   private SQLIdentifierExpr orderingColumn;
/*    */   
/*    */   public Type getType() {
/* 39 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(Type type) {
/* 43 */     this.type = type;
/*    */   }
/*    */   
/*    */   public List<SQLSelectOrderByItem> getItems() {
/* 47 */     return this.items;
/*    */   }
/*    */   
/*    */   public void addItem(SQLSelectOrderByItem item) {
/* 51 */     if (item != null) {
/* 52 */       item.setParent((SQLObject)this);
/*    */     }
/* 54 */     this.items.add(item);
/*    */   }
/*    */   
/*    */   public SQLIdentifierExpr getOrderingColumn() {
/* 58 */     return this.orderingColumn;
/*    */   }
/*    */   
/*    */   public void setOrderingColumn(SQLIdentifierExpr orderingColumn) {
/* 62 */     this.orderingColumn = orderingColumn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 67 */     if (visitor.visit(this)) {
/* 68 */       acceptChild((SQLASTVisitor)visitor, this.items);
/* 69 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.orderingColumn);
/*    */     } 
/* 71 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\clause\SearchClause.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */