/*     */ package com.tranboot.client.druid.sql.dialect.mysql.ast;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

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
/*     */ 
/*     */ 
/*     */ public class MysqlForeignKey
/*     */   extends SQLForeignKeyImpl
/*     */ {
/*     */   private SQLName indexName;
/*     */   private boolean hasConstraint;
/*     */   private Match referenceMatch;
/*     */   protected Option onUpdate;
/*     */   protected Option onDelete;
/*     */   
/*     */   public SQLName getIndexName() {
/*  37 */     return this.indexName;
/*     */   }
/*     */   
/*     */   public void setIndexName(SQLName indexName) {
/*  41 */     this.indexName = indexName;
/*     */   }
/*     */   
/*     */   public boolean isHasConstraint() {
/*  45 */     return this.hasConstraint;
/*     */   }
/*     */   
/*     */   public void setHasConstraint(boolean hasConstraint) {
/*  49 */     this.hasConstraint = hasConstraint;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  54 */     if (visitor instanceof MySqlASTVisitor) {
/*  55 */       accept0((MySqlASTVisitor)visitor);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void accept0(MySqlASTVisitor visitor) {
/*  60 */     if (visitor.visit(this)) {
/*  61 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getName());
/*  62 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getReferencedTableName());
/*  63 */       acceptChild((SQLASTVisitor)visitor, getReferencingColumns());
/*  64 */       acceptChild((SQLASTVisitor)visitor, getReferencedColumns());
/*     */       
/*  66 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.indexName);
/*     */     } 
/*  68 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public Match getReferenceMatch() {
/*  72 */     return this.referenceMatch;
/*     */   }
/*     */   
/*     */   public void setReferenceMatch(Match referenceMatch) {
/*  76 */     this.referenceMatch = referenceMatch;
/*     */   }
/*     */   
/*     */   public Option getOnUpdate() {
/*  80 */     return this.onUpdate;
/*     */   }
/*     */   
/*     */   public void setOnUpdate(Option onUpdate) {
/*  84 */     this.onUpdate = onUpdate;
/*     */   }
/*     */   
/*     */   public Option getOnDelete() {
/*  88 */     return this.onDelete;
/*     */   }
/*     */   
/*     */   public void setOnDelete(Option onDelete) {
/*  92 */     this.onDelete = onDelete;
/*     */   }
/*     */   
/*     */   public enum Option
/*     */   {
/*  97 */     RESTRICT("RESTRICT"), CASCADE("CASCADE"), SET_NULL("SET NULL"), NO_ACTION("NO ACTION");
/*     */     
/*     */     public final String name_lcase;
/*     */     public final String name;
/*     */     
/*     */     Option(String name) {
/* 103 */       this.name = name;
/* 104 */       this.name_lcase = name.toLowerCase();
/*     */     }
/*     */     
/*     */     public String getText() {
/* 108 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Match
/*     */   {
/* 114 */     FULL("FULL"), PARTIAL("PARTIAL"), SIMPLE("SIMPLE");
/*     */     
/*     */     public final String name_lcase;
/*     */     public final String name;
/*     */     
/*     */     Match(String name) {
/* 120 */       this.name = name;
/* 121 */       this.name_lcase = name.toLowerCase();
/*     */     }
/*     */   }
/*     */   
/*     */   public enum On {
/* 126 */     DELETE("DELETE"),
/* 127 */     UPDATE("UPDATE");
/*     */     
/*     */     public final String name;
/*     */     public final String name_lcase;
/*     */     
/*     */     On(String name) {
/* 133 */       this.name = name;
/* 134 */       this.name_lcase = name.toLowerCase();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\MysqlForeignKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */