/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.clause;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*    */ 
/*    */ 
/*    */ public class OracleReturningClause
/*    */   extends OracleSQLObjectImpl
/*    */ {
/* 27 */   private List<SQLExpr> items = new ArrayList<>();
/* 28 */   private List<SQLExpr> values = new ArrayList<>();
/*    */   
/*    */   public List<SQLExpr> getItems() {
/* 31 */     return this.items;
/*    */   }
/*    */   
/*    */   public void addItem(SQLExpr item) {
/* 35 */     if (item != null) {
/* 36 */       item.setParent((SQLObject)this);
/*    */     }
/* 38 */     this.items.add(item);
/*    */   }
/*    */   
/*    */   public List<SQLExpr> getValues() {
/* 42 */     return this.values;
/*    */   }
/*    */   
/*    */   public void addValue(SQLExpr value) {
/* 46 */     if (value != null) {
/* 47 */       value.setParent((SQLObject)this);
/*    */     }
/* 49 */     this.values.add(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 54 */     if (visitor.visit(this)) {
/* 55 */       acceptChild((SQLASTVisitor)visitor, this.items);
/* 56 */       acceptChild((SQLASTVisitor)visitor, this.values);
/*    */     } 
/* 58 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\clause\OracleReturningClause.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */