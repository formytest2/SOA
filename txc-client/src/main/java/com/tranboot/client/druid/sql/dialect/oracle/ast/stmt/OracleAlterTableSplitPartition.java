/*     */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
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
/*     */ 
/*     */ 
/*     */ public class OracleAlterTableSplitPartition
/*     */   extends OracleAlterTableItem
/*     */ {
/*     */   private SQLName name;
/*  30 */   private List<SQLExpr> at = new ArrayList<>();
/*  31 */   private List<SQLExpr> values = new ArrayList<>();
/*  32 */   private List<NestedTablePartitionSpec> into = new ArrayList<>();
/*     */   
/*  34 */   private UpdateIndexesClause updateIndexes = null;
/*     */ 
/*     */   
/*     */   public void accept0(OracleASTVisitor visitor) {
/*  38 */     if (visitor.visit(this)) {
/*  39 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.name);
/*  40 */       acceptChild((SQLASTVisitor)visitor, this.at);
/*  41 */       acceptChild((SQLASTVisitor)visitor, this.values);
/*  42 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.updateIndexes);
/*     */     } 
/*  44 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public UpdateIndexesClause getUpdateIndexes() {
/*  48 */     return this.updateIndexes;
/*     */   }
/*     */   
/*     */   public void setUpdateIndexes(UpdateIndexesClause updateIndexes) {
/*  52 */     this.updateIndexes = updateIndexes;
/*     */   }
/*     */   
/*     */   public SQLName getName() {
/*  56 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(SQLName name) {
/*  60 */     this.name = name;
/*     */   }
/*     */   
/*     */   public List<SQLExpr> getAt() {
/*  64 */     return this.at;
/*     */   }
/*     */   
/*     */   public void setAt(List<SQLExpr> at) {
/*  68 */     this.at = at;
/*     */   }
/*     */   
/*     */   public List<NestedTablePartitionSpec> getInto() {
/*  72 */     return this.into;
/*     */   }
/*     */   
/*     */   public void setInto(List<NestedTablePartitionSpec> into) {
/*  76 */     this.into = into;
/*     */   }
/*     */   
/*     */   public List<SQLExpr> getValues() {
/*  80 */     return this.values;
/*     */   }
/*     */   
/*     */   public void setValues(List<SQLExpr> values) {
/*  84 */     this.values = values;
/*     */   }
/*     */   
/*     */   public static class NestedTablePartitionSpec
/*     */     extends OracleSQLObjectImpl
/*     */   {
/*     */     private SQLName partition;
/*  91 */     private List<SQLObject> segmentAttributeItems = new ArrayList<>();
/*     */ 
/*     */     
/*     */     public void accept0(OracleASTVisitor visitor) {
/*  95 */       if (visitor.visit(this)) {
/*  96 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.partition);
/*  97 */         acceptChild((SQLASTVisitor)visitor, this.segmentAttributeItems);
/*     */       } 
/*  99 */       visitor.endVisit(this);
/*     */     }
/*     */     
/*     */     public SQLName getPartition() {
/* 103 */       return this.partition;
/*     */     }
/*     */     
/*     */     public void setPartition(SQLName partition) {
/* 107 */       this.partition = partition;
/*     */     }
/*     */     
/*     */     public List<SQLObject> getSegmentAttributeItems() {
/* 111 */       return this.segmentAttributeItems;
/*     */     }
/*     */     
/*     */     public void setSegmentAttributeItems(List<SQLObject> segmentAttributeItems) {
/* 115 */       this.segmentAttributeItems = segmentAttributeItems;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class TableSpaceItem
/*     */     extends OracleSQLObjectImpl
/*     */   {
/*     */     private SQLName tablespace;
/*     */ 
/*     */     
/*     */     public TableSpaceItem() {}
/*     */     
/*     */     public TableSpaceItem(SQLName tablespace) {
/* 129 */       this.tablespace = tablespace;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept0(OracleASTVisitor visitor) {
/* 134 */       if (visitor.visit(this)) {
/* 135 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.tablespace);
/*     */       }
/* 137 */       visitor.endVisit(this);
/*     */     }
/*     */     
/*     */     public SQLName getTablespace() {
/* 141 */       return this.tablespace;
/*     */     }
/*     */     
/*     */     public void setTablespace(SQLName tablespace) {
/* 145 */       this.tablespace = tablespace;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class UpdateIndexesClause
/*     */     extends OracleSQLObjectImpl {
/* 151 */     private List<SQLObject> items = new ArrayList<>();
/*     */ 
/*     */     
/*     */     public void accept0(OracleASTVisitor visitor) {
/* 155 */       if (visitor.visit(this)) {
/* 156 */         acceptChild((SQLASTVisitor)visitor, this.items);
/*     */       }
/* 158 */       visitor.endVisit(this);
/*     */     }
/*     */     
/*     */     public List<SQLObject> getItems() {
/* 162 */       return this.items;
/*     */     }
/*     */     
/*     */     public void setItems(List<SQLObject> items) {
/* 166 */       this.items = items;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleAlterTableSplitPartition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */