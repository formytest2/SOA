/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

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
/*    */ public class SQLAlterTableRenamePartition
/*    */   extends SQLObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/*    */   private boolean ifNotExists = false;
/* 28 */   private final List<SQLAssignItem> partition = new ArrayList<>(4);
/* 29 */   private final List<SQLAssignItem> to = new ArrayList<>(4);
/*    */   
/*    */   public List<SQLAssignItem> getPartition() {
/* 32 */     return this.partition;
/*    */   }
/*    */   
/*    */   public boolean isIfNotExists() {
/* 36 */     return this.ifNotExists;
/*    */   }
/*    */   
/*    */   public void setIfNotExists(boolean ifNotExists) {
/* 40 */     this.ifNotExists = ifNotExists;
/*    */   }
/*    */   
/*    */   public List<SQLAssignItem> getTo() {
/* 44 */     return this.to;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 49 */     if (visitor.visit(this)) {
/* 50 */       acceptChild(visitor, this.partition);
/* 51 */       acceptChild(visitor, this.to);
/*    */     } 
/* 53 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterTableRenamePartition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */