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
/*    */ public class SQLSelectGroupByClause
/*    */   extends SQLObjectImpl
/*    */ {
/* 27 */   private final List<SQLExpr> items = new ArrayList<>();
/*    */   
/*    */   private SQLExpr having;
/*    */   
/*    */   private boolean withRollUp = false;
/*    */   
/*    */   private boolean withCube = false;
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 37 */     if (visitor.visit(this)) {
/* 38 */       acceptChild(visitor, this.items);
/* 39 */       acceptChild(visitor, (SQLObject)this.having);
/*    */     } 
/*    */     
/* 42 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public boolean isWithRollUp() {
/* 46 */     return this.withRollUp;
/*    */   }
/*    */   
/*    */   public void setWithRollUp(boolean withRollUp) {
/* 50 */     this.withRollUp = withRollUp;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isWithCube() {
/* 55 */     return this.withCube;
/*    */   }
/*    */   
/*    */   public void setWithCube(boolean withCube) {
/* 59 */     this.withCube = withCube;
/*    */   }
/*    */   
/*    */   public SQLExpr getHaving() {
/* 63 */     return this.having;
/*    */   }
/*    */   
/*    */   public void setHaving(SQLExpr having) {
/* 67 */     if (having != null) {
/* 68 */       having.setParent((SQLObject)this);
/*    */     }
/*    */     
/* 71 */     this.having = having;
/*    */   }
/*    */   
/*    */   public List<SQLExpr> getItems() {
/* 75 */     return this.items;
/*    */   }
/*    */   
/*    */   public void addItem(SQLExpr sqlExpr) {
/* 79 */     if (sqlExpr != null) {
/* 80 */       sqlExpr.setParent((SQLObject)this);
/* 81 */       this.items.add(sqlExpr);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLSelectGroupByClause.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */