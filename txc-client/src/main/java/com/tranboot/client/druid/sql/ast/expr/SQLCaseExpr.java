/*     */ package com.tranboot.client.druid.sql.ast.expr;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLObjectImpl;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

import java.io.Serializable;
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
/*     */ 
/*     */ public class SQLCaseExpr
/*     */   extends SQLExprImpl
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  30 */   private final List<Item> items = new ArrayList<>();
/*     */ 
/*     */   
/*     */   private SQLExpr valueExpr;
/*     */   
/*     */   private SQLExpr elseExpr;
/*     */ 
/*     */   
/*     */   public SQLExpr getValueExpr() {
/*  39 */     return this.valueExpr;
/*     */   }
/*     */   
/*     */   public void setValueExpr(SQLExpr valueExpr) {
/*  43 */     if (valueExpr != null) {
/*  44 */       valueExpr.setParent((SQLObject)this);
/*     */     }
/*  46 */     this.valueExpr = valueExpr;
/*     */   }
/*     */   
/*     */   public SQLExpr getElseExpr() {
/*  50 */     return this.elseExpr;
/*     */   }
/*     */   
/*     */   public void setElseExpr(SQLExpr elseExpr) {
/*  54 */     if (elseExpr != null) {
/*  55 */       elseExpr.setParent((SQLObject)this);
/*     */     }
/*  57 */     this.elseExpr = elseExpr;
/*     */   }
/*     */   
/*     */   public List<Item> getItems() {
/*  61 */     return this.items;
/*     */   }
/*     */   
/*     */   public void addItem(Item item) {
/*  65 */     if (item != null) {
/*  66 */       item.setParent((SQLObject)this);
/*  67 */       this.items.add(item);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  72 */     if (visitor.visit(this)) {
/*  73 */       acceptChild(visitor, (SQLObject)this.valueExpr);
/*  74 */       acceptChild(visitor, this.items);
/*  75 */       acceptChild(visitor, (SQLObject)this.elseExpr);
/*     */     } 
/*  77 */     visitor.endVisit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Item
/*     */     extends SQLObjectImpl
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     private SQLExpr conditionExpr;
/*     */     private SQLExpr valueExpr;
/*     */     
/*     */     public Item() {}
/*     */     
/*     */     public Item(SQLExpr conditionExpr, SQLExpr valueExpr) {
/*  92 */       setConditionExpr(conditionExpr);
/*  93 */       setValueExpr(valueExpr);
/*     */     }
/*     */     
/*     */     public SQLExpr getConditionExpr() {
/*  97 */       return this.conditionExpr;
/*     */     }
/*     */     
/*     */     public void setConditionExpr(SQLExpr conditionExpr) {
/* 101 */       if (conditionExpr != null) {
/* 102 */         conditionExpr.setParent((SQLObject)this);
/*     */       }
/* 104 */       this.conditionExpr = conditionExpr;
/*     */     }
/*     */     
/*     */     public SQLExpr getValueExpr() {
/* 108 */       return this.valueExpr;
/*     */     }
/*     */     
/*     */     public void setValueExpr(SQLExpr valueExpr) {
/* 112 */       if (valueExpr != null) {
/* 113 */         valueExpr.setParent((SQLObject)this);
/*     */       }
/* 115 */       this.valueExpr = valueExpr;
/*     */     }
/*     */     
/*     */     protected void accept0(SQLASTVisitor visitor) {
/* 119 */       if (visitor.visit(this)) {
/* 120 */         acceptChild(visitor, (SQLObject)this.conditionExpr);
/* 121 */         acceptChild(visitor, (SQLObject)this.valueExpr);
/*     */       } 
/* 123 */       visitor.endVisit(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 128 */       int prime = 31;
/* 129 */       int result = 1;
/* 130 */       result = 31 * result + ((this.conditionExpr == null) ? 0 : this.conditionExpr.hashCode());
/* 131 */       result = 31 * result + ((this.valueExpr == null) ? 0 : this.valueExpr.hashCode());
/* 132 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 137 */       if (this == obj) return true; 
/* 138 */       if (obj == null) return false; 
/* 139 */       if (getClass() != obj.getClass()) return false; 
/* 140 */       Item other = (Item)obj;
/* 141 */       if (this.conditionExpr == null)
/* 142 */       { if (other.conditionExpr != null) return false;  }
/* 143 */       else if (!this.conditionExpr.equals(other.conditionExpr)) { return false; }
/* 144 */        if (this.valueExpr == null)
/* 145 */       { if (other.valueExpr != null) return false;  }
/* 146 */       else if (!this.valueExpr.equals(other.valueExpr)) { return false; }
/* 147 */        return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 154 */     int prime = 31;
/* 155 */     int result = 1;
/* 156 */     result = 31 * result + ((this.elseExpr == null) ? 0 : this.elseExpr.hashCode());
/* 157 */     result = 31 * result + ((this.items == null) ? 0 : this.items.hashCode());
/* 158 */     result = 31 * result + ((this.valueExpr == null) ? 0 : this.valueExpr.hashCode());
/* 159 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 164 */     if (this == obj) {
/* 165 */       return true;
/*     */     }
/* 167 */     if (obj == null) {
/* 168 */       return false;
/*     */     }
/* 170 */     if (getClass() != obj.getClass()) {
/* 171 */       return false;
/*     */     }
/* 173 */     SQLCaseExpr other = (SQLCaseExpr)obj;
/* 174 */     if (this.elseExpr == null) {
/* 175 */       if (other.elseExpr != null) {
/* 176 */         return false;
/*     */       }
/* 178 */     } else if (!this.elseExpr.equals(other.elseExpr)) {
/* 179 */       return false;
/*     */     } 
/* 181 */     if (this.items == null) {
/* 182 */       if (other.items != null) {
/* 183 */         return false;
/*     */       }
/* 185 */     } else if (!this.items.equals(other.items)) {
/* 186 */       return false;
/*     */     } 
/* 188 */     if (this.valueExpr == null) {
/* 189 */       if (other.valueExpr != null) {
/* 190 */         return false;
/*     */       }
/* 192 */     } else if (!this.valueExpr.equals(other.valueExpr)) {
/* 193 */       return false;
/*     */     } 
/* 195 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLCaseExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */