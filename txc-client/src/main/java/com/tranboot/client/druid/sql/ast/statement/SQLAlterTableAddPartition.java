/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
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
/*    */ 
/*    */ public class SQLAlterTableAddPartition
/*    */   extends SQLObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/*    */   private boolean ifNotExists = false;
/* 30 */   private final List<SQLObject> partitions = new ArrayList<>(4);
/*    */   
/*    */   private SQLExpr partitionCount;
/*    */   
/*    */   public List<SQLObject> getPartitions() {
/* 35 */     return this.partitions;
/*    */   }
/*    */   
/*    */   public void addPartition(SQLObject partition) {
/* 39 */     if (partition != null) {
/* 40 */       partition.setParent(this);
/*    */     }
/* 42 */     this.partitions.add(partition);
/*    */   }
/*    */   
/*    */   public boolean isIfNotExists() {
/* 46 */     return this.ifNotExists;
/*    */   }
/*    */   
/*    */   public void setIfNotExists(boolean ifNotExists) {
/* 50 */     this.ifNotExists = ifNotExists;
/*    */   }
/*    */   
/*    */   public SQLExpr getPartitionCount() {
/* 54 */     return this.partitionCount;
/*    */   }
/*    */   
/*    */   public void setPartitionCount(SQLExpr partitionCount) {
/* 58 */     if (partitionCount != null) {
/* 59 */       partitionCount.setParent(this);
/*    */     }
/* 61 */     this.partitionCount = partitionCount;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 66 */     if (visitor.visit(this)) {
/* 67 */       acceptChild(visitor, this.partitions);
/*    */     }
/* 69 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterTableAddPartition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */