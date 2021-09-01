/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*    */ 
/*    */ public class SQLAlterTableReOrganizePartition
/*    */   extends SQLObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/* 28 */   private final List<SQLName> names = new ArrayList<>();
/*    */   
/* 30 */   private final List<SQLObject> partitions = new ArrayList<>(4);
/*    */   
/*    */   public List<SQLObject> getPartitions() {
/* 33 */     return this.partitions;
/*    */   }
/*    */   
/*    */   public void addPartition(SQLObject partition) {
/* 37 */     if (partition != null) {
/* 38 */       partition.setParent(this);
/*    */     }
/* 40 */     this.partitions.add(partition);
/*    */   }
/*    */   
/*    */   public List<SQLName> getNames() {
/* 44 */     return this.names;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 49 */     if (visitor.visit(this)) {
/* 50 */       acceptChild(visitor, this.partitions);
/*    */     }
/* 52 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterTableReOrganizePartition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */