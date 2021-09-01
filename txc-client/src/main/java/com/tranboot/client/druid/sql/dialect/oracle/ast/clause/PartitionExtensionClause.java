/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.clause;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
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
/*    */ public class PartitionExtensionClause
/*    */   extends OracleSQLObjectImpl
/*    */ {
/*    */   private boolean subPartition;
/*    */   private SQLName partition;
/* 29 */   private final List<SQLName> target = new ArrayList<>();
/*    */   
/*    */   public boolean isSubPartition() {
/* 32 */     return this.subPartition;
/*    */   }
/*    */   
/*    */   public void setSubPartition(boolean subPartition) {
/* 36 */     this.subPartition = subPartition;
/*    */   }
/*    */   
/*    */   public SQLName getPartition() {
/* 40 */     return this.partition;
/*    */   }
/*    */   
/*    */   public void setPartition(SQLName partition) {
/* 44 */     this.partition = partition;
/*    */   }
/*    */   
/*    */   public List<SQLName> getFor() {
/* 48 */     return this.target;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 53 */     if (visitor.visit(this)) {
/* 54 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.partition);
/* 55 */       acceptChild((SQLASTVisitor)visitor, this.target);
/*    */     } 
/* 57 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\clause\PartitionExtensionClause.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */