/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLObjectImpl;
import com.tranboot.client.druid.sql.ast.SQLOrderBy;
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
/*    */ public class SQLUnionQuery
/*    */   extends SQLObjectImpl
/*    */   implements SQLSelectQuery
/*    */ {
/*    */   private SQLSelectQuery left;
/*    */   private SQLSelectQuery right;
/* 26 */   private SQLUnionOperator operator = SQLUnionOperator.UNION;
/*    */   private SQLOrderBy orderBy;
/*    */   
/*    */   public SQLUnionOperator getOperator() {
/* 30 */     return this.operator;
/*    */   }
/*    */   
/*    */   public void setOperator(SQLUnionOperator operator) {
/* 34 */     this.operator = operator;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SQLSelectQuery getLeft() {
/* 42 */     return this.left;
/*    */   }
/*    */   
/*    */   public void setLeft(SQLSelectQuery left) {
/* 46 */     if (left != null) {
/* 47 */       left.setParent(this);
/*    */     }
/* 49 */     this.left = left;
/*    */   }
/*    */   
/*    */   public SQLSelectQuery getRight() {
/* 53 */     return this.right;
/*    */   }
/*    */   
/*    */   public void setRight(SQLSelectQuery right) {
/* 57 */     if (right != null) {
/* 58 */       right.setParent(this);
/*    */     }
/* 60 */     this.right = right;
/*    */   }
/*    */   
/*    */   public SQLOrderBy getOrderBy() {
/* 64 */     return this.orderBy;
/*    */   }
/*    */   
/*    */   public void setOrderBy(SQLOrderBy orderBy) {
/* 68 */     if (orderBy != null) {
/* 69 */       orderBy.setParent(this);
/*    */     }
/* 71 */     this.orderBy = orderBy;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 76 */     if (visitor.visit(this)) {
/* 77 */       acceptChild(visitor, this.left);
/* 78 */       acceptChild(visitor, this.right);
/* 79 */       acceptChild(visitor, (SQLObject)this.orderBy);
/*    */     } 
/* 81 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLUnionQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */