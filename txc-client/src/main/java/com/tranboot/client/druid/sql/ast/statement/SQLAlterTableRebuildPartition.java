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
/*    */ public class SQLAlterTableRebuildPartition
/*    */   extends SQLObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/* 27 */   private final List<SQLName> partitions = new ArrayList<>(4);
/*    */   
/*    */   public List<SQLName> getPartitions() {
/* 30 */     return this.partitions;
/*    */   }
/*    */   
/*    */   public void addPartition(SQLName partition) {
/* 34 */     if (partition != null) {
/* 35 */       partition.setParent(this);
/*    */     }
/* 37 */     this.partitions.add(partition);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 42 */     if (visitor.visit(this)) {
/* 43 */       acceptChild(visitor, this.partitions);
/*    */     }
/* 45 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterTableRebuildPartition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */