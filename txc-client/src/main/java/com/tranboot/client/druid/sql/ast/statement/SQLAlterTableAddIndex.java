/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
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
/*    */ public class SQLAlterTableAddIndex
/*    */   extends SQLObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/*    */   private boolean unique;
/*    */   private SQLName name;
/* 31 */   private final List<SQLSelectOrderByItem> items = new ArrayList<>();
/*    */   
/*    */   private String type;
/*    */   
/*    */   private String using;
/*    */   
/*    */   private boolean key = false;
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 41 */     if (visitor.visit(this)) {
/* 42 */       acceptChild(visitor, (SQLObject)getName());
/* 43 */       acceptChild(visitor, getItems());
/*    */     } 
/* 45 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public boolean isUnique() {
/* 49 */     return this.unique;
/*    */   }
/*    */   
/*    */   public void setUnique(boolean unique) {
/* 53 */     this.unique = unique;
/*    */   }
/*    */   
/*    */   public List<SQLSelectOrderByItem> getItems() {
/* 57 */     return this.items;
/*    */   }
/*    */   
/*    */   public void addItem(SQLSelectOrderByItem item) {
/* 61 */     if (item != null) {
/* 62 */       item.setParent(this);
/*    */     }
/* 64 */     this.items.add(item);
/*    */   }
/*    */   
/*    */   public SQLName getName() {
/* 68 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(SQLName name) {
/* 72 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getType() {
/* 76 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(String type) {
/* 80 */     this.type = type;
/*    */   }
/*    */   
/*    */   public String getUsing() {
/* 84 */     return this.using;
/*    */   }
/*    */   
/*    */   public void setUsing(String using) {
/* 88 */     this.using = using;
/*    */   }
/*    */   
/*    */   public boolean isKey() {
/* 92 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(boolean key) {
/* 96 */     this.key = key;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterTableAddIndex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */