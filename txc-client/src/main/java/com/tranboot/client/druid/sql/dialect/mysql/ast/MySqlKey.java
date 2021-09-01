/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLTableConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLUnique;
import com.tranboot.client.druid.sql.ast.statement.SQLUniqueConstraint;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MySqlKey
/*    */   extends SQLUnique
/*    */   implements SQLUniqueConstraint, SQLTableConstraint
/*    */ {
/*    */   private SQLName indexName;
/*    */   private String indexType;
/*    */   private boolean hasConstaint;
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 39 */     if (visitor instanceof MySqlASTVisitor) {
/* 40 */       accept0((MySqlASTVisitor)visitor);
/*    */     }
/*    */   }
/*    */   
/*    */   protected void accept0(MySqlASTVisitor visitor) {
/* 45 */     if (visitor.visit(this)) {
/* 46 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getName());
/* 47 */       acceptChild((SQLASTVisitor)visitor, getColumns());
/* 48 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.indexName);
/*    */     } 
/* 50 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public String getIndexType() {
/* 54 */     return this.indexType;
/*    */   }
/*    */   
/*    */   public void setIndexType(String indexType) {
/* 58 */     this.indexType = indexType;
/*    */   }
/*    */   
/*    */   public SQLName getIndexName() {
/* 62 */     return this.indexName;
/*    */   }
/*    */   
/*    */   public void setIndexName(SQLName indexName) {
/* 66 */     this.indexName = indexName;
/*    */   }
/*    */   
/*    */   public boolean isHasConstaint() {
/* 70 */     return this.hasConstaint;
/*    */   }
/*    */   
/*    */   public void setHasConstaint(boolean hasConstaint) {
/* 74 */     this.hasConstaint = hasConstaint;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\MySqlKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */