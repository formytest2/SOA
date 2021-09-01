/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLLimit;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLUnionQuery;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MySqlObject;
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
/*    */ public class MySqlUnionQuery
/*    */   extends SQLUnionQuery
/*    */   implements MySqlObject
/*    */ {
/*    */   private SQLLimit limit;
/*    */   
/*    */   public SQLLimit getLimit() {
/* 29 */     return this.limit;
/*    */   }
/*    */   
/*    */   public void setLimit(SQLLimit limit) {
/* 33 */     if (limit != null) {
/* 34 */       limit.setParent((SQLObject)this);
/*    */     }
/* 36 */     this.limit = limit;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 41 */     if (visitor instanceof MySqlASTVisitor) {
/* 42 */       accept0((MySqlASTVisitor)visitor);
/*    */     } else {
/* 44 */       throw new IllegalArgumentException("not support visitor type : " + visitor.getClass().getName());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 50 */     if (visitor.visit(this)) {
/* 51 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getLeft());
/* 52 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getRight());
/* 53 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getOrderBy());
/* 54 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getLimit());
/*    */     } 
/* 56 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlUnionQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */