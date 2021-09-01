/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*    */ public class OracleSelectUnPivot
/*    */   extends OracleSelectPivotBase
/*    */ {
/*    */   private NullsIncludeType nullsIncludeType;
/* 28 */   private final List<SQLExpr> items = new ArrayList<>();
/*    */   
/* 30 */   private final List<OracleSelectPivot.Item> pivotIn = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 37 */     if (visitor.visit(this)) {
/* 38 */       acceptChild((SQLASTVisitor)visitor, this.items);
/* 39 */       acceptChild((SQLASTVisitor)visitor, this.pivotIn);
/*    */     } 
/* 41 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public List<OracleSelectPivot.Item> getPivotIn() {
/* 45 */     return this.pivotIn;
/*    */   }
/*    */   
/*    */   public List<SQLExpr> getItems() {
/* 49 */     return this.items;
/*    */   }
/*    */   
/*    */   public void addItem(SQLExpr item) {
/* 53 */     if (item != null) {
/* 54 */       item.setParent((SQLObject)this);
/*    */     }
/* 56 */     this.items.add(item);
/*    */   }
/*    */   
/*    */   public NullsIncludeType getNullsIncludeType() {
/* 60 */     return this.nullsIncludeType;
/*    */   }
/*    */   
/*    */   public void setNullsIncludeType(NullsIncludeType nullsIncludeType) {
/* 64 */     this.nullsIncludeType = nullsIncludeType;
/*    */   }
/*    */   
/*    */   public enum NullsIncludeType {
/* 68 */     INCLUDE_NULLS, EXCLUDE_NULLS;
/*    */     
/*    */     public static String toString(NullsIncludeType type, boolean ucase) {
/* 71 */       if (INCLUDE_NULLS.equals(type)) {
/* 72 */         return ucase ? "INCLUDE NULLS" : "include nulls";
/*    */       }
/* 74 */       if (EXCLUDE_NULLS.equals(type)) {
/* 75 */         return ucase ? "EXCLUDE NULLS" : "exclude nulls";
/*    */       }
/*    */       
/* 78 */       throw new IllegalArgumentException();
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleSelectUnPivot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */