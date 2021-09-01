/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLSubPartitionBy;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnDefinition;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MySqlObject;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MySqlSubPartitionByList
/*    */   extends SQLSubPartitionBy
/*    */   implements MySqlObject
/*    */ {
/*    */   private SQLExpr expr;
/* 32 */   private List<SQLColumnDefinition> columns = new ArrayList<>();
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 36 */     if (visitor instanceof MySqlASTVisitor) {
/* 37 */       accept0((MySqlASTVisitor)visitor);
/*    */     } else {
/* 39 */       throw new IllegalArgumentException("not support visitor type : " + visitor.getClass().getName());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 45 */     if (visitor.visit(this)) {
/* 46 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.expr);
/* 47 */       acceptChild((SQLASTVisitor)visitor, this.columns);
/* 48 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.subPartitionsCount);
/*    */     } 
/* 50 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLExpr getExpr() {
/* 54 */     return this.expr;
/*    */   }
/*    */   
/*    */   public void setExpr(SQLExpr expr) {
/* 58 */     if (expr != null) {
/* 59 */       expr.setParent((SQLObject)this);
/*    */     }
/* 61 */     this.expr = expr;
/*    */   }
/*    */   
/*    */   public List<SQLColumnDefinition> getColumns() {
/* 65 */     return this.columns;
/*    */   }
/*    */   
/*    */   public void addColumn(SQLColumnDefinition column) {
/* 69 */     if (column != null) {
/* 70 */       column.setParent((SQLObject)this);
/*    */     }
/* 72 */     this.columns.add(column);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlSubPartitionByList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */