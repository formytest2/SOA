/*     */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

import java.util.ArrayList;
import java.util.List;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */

/*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OracleSelectPivot
/*     */   extends OracleSelectPivotBase
/*     */ {
/*     */   private boolean xml;
/*  28 */   private final List<Item> items = new ArrayList<>();
/*  29 */   private final List<SQLExpr> pivotFor = new ArrayList<>();
/*  30 */   private final List<Item> pivotIn = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void accept0(OracleASTVisitor visitor) {
/*  37 */     if (visitor.visit(this)) {
/*  38 */       acceptChild((SQLASTVisitor)visitor, this.items);
/*  39 */       acceptChild((SQLASTVisitor)visitor, this.pivotFor);
/*  40 */       acceptChild((SQLASTVisitor)visitor, this.pivotIn);
/*     */     } 
/*     */     
/*  43 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public List<Item> getPivotIn() {
/*  47 */     return this.pivotIn;
/*     */   }
/*     */   
/*     */   public List<SQLExpr> getPivotFor() {
/*  51 */     return this.pivotFor;
/*     */   }
/*     */   
/*     */   public boolean isXml() {
/*  55 */     return this.xml;
/*     */   }
/*     */   
/*     */   public List<Item> getItems() {
/*  59 */     return this.items;
/*     */   }
/*     */   
/*     */   public void addItem(Item item) {
/*  63 */     if (item != null) {
/*  64 */       item.setParent((SQLObject)this);
/*     */     }
/*  66 */     this.items.add(item);
/*     */   }
/*     */   
/*     */   public void setXml(boolean xml) {
/*  70 */     this.xml = xml;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Item
/*     */     extends OracleSQLObjectImpl
/*     */   {
/*     */     private String alias;
/*     */     
/*     */     private SQLExpr expr;
/*     */ 
/*     */     
/*     */     public String getAlias() {
/*  83 */       return this.alias;
/*     */     }
/*     */     
/*     */     public void setAlias(String alias) {
/*  87 */       this.alias = alias;
/*     */     }
/*     */     
/*     */     public SQLExpr getExpr() {
/*  91 */       return this.expr;
/*     */     }
/*     */     
/*     */     public void setExpr(SQLExpr expr) {
/*  95 */       this.expr = expr;
/*     */     }
/*     */     
/*     */     public void accept0(OracleASTVisitor visitor) {
/*  99 */       if (visitor.visit(this)) {
/* 100 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.expr);
/*     */       }
/*     */       
/* 103 */       visitor.endVisit(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleSelectPivot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */