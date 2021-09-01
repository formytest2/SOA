/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

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
/*    */ public class SQLAlterTableDropPartition
/*    */   extends SQLObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/*    */   private boolean ifExists = false;
/*    */   private boolean purge;
/* 31 */   private final List<SQLObject> partitions = new ArrayList<>(4);
/*    */   
/*    */   public List<SQLObject> getPartitions() {
/* 34 */     return this.partitions;
/*    */   }
/*    */   
/*    */   public void addPartition(SQLObject partition) {
/* 38 */     if (partition != null) {
/* 39 */       partition.setParent(this);
/*    */     }
/* 41 */     this.partitions.add(partition);
/*    */   }
/*    */   
/*    */   public boolean isIfExists() {
/* 45 */     return this.ifExists;
/*    */   }
/*    */   
/*    */   public void setIfExists(boolean ifExists) {
/* 49 */     this.ifExists = ifExists;
/*    */   }
/*    */   
/*    */   public boolean isPurge() {
/* 53 */     return this.purge;
/*    */   }
/*    */   
/*    */   public void setPurge(boolean purge) {
/* 57 */     this.purge = purge;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 62 */     if (visitor.visit(this)) {
/* 63 */       acceptChild(visitor, this.partitions);
/*    */     }
/* 65 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterTableDropPartition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */