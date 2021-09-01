/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLPartitionBy;
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
/*    */ public class MySqlPartitionByKey
/*    */   extends SQLPartitionBy
/*    */   implements MySqlObject
/*    */ {
/* 29 */   private List<SQLName> columns = new ArrayList<>();
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 33 */     if (visitor instanceof MySqlASTVisitor) {
/* 34 */       accept0((MySqlASTVisitor)visitor);
/*    */     } else {
/* 36 */       throw new IllegalArgumentException("not support visitor type : " + visitor.getClass().getName());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 42 */     if (visitor.visit(this)) {
/* 43 */       acceptChild((SQLASTVisitor)visitor, this.columns);
/* 44 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.partitionsCount);
/* 45 */       acceptChild((SQLASTVisitor)visitor, getPartitions());
/* 46 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.subPartitionBy);
/*    */     } 
/* 48 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public List<SQLName> getColumns() {
/* 52 */     return this.columns;
/*    */   }
/*    */   
/*    */   public void addColumn(SQLName column) {
/* 56 */     if (column != null) {
/* 57 */       column.setParent((SQLObject)this);
/*    */     }
/* 59 */     this.columns.add(column);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlPartitionByKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */