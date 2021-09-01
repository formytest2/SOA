/*     */ package com.tranboot.client.druid.sql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatementImpl;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
/*     */ public class SQLAlterTableStatement
/*     */   extends SQLStatementImpl
/*     */   implements SQLDDLStatement
/*     */ {
/*     */   private SQLExprTableSource tableSource;
/*  31 */   private List<SQLAlterTableItem> items = new ArrayList<>();
/*     */   
/*     */   private boolean ignore = false;
/*     */   
/*     */   private boolean updateGlobalIndexes = false;
/*     */   
/*     */   private boolean invalidateGlobalIndexes = false;
/*     */   
/*     */   private boolean removePatiting = false;
/*     */   private boolean upgradePatiting = false;
/*  41 */   private Map<String, SQLObject> tableOptions = new LinkedHashMap<>();
/*     */ 
/*     */   
/*     */   private boolean mergeSmallFiles = false;
/*     */ 
/*     */   
/*     */   public SQLAlterTableStatement() {}
/*     */ 
/*     */   
/*     */   public SQLAlterTableStatement(String dbType) {
/*  51 */     super(dbType);
/*     */   }
/*     */   
/*     */   public boolean isIgnore() {
/*  55 */     return this.ignore;
/*     */   }
/*     */   
/*     */   public void setIgnore(boolean ignore) {
/*  59 */     this.ignore = ignore;
/*     */   }
/*     */   
/*     */   public boolean isRemovePatiting() {
/*  63 */     return this.removePatiting;
/*     */   }
/*     */   
/*     */   public void setRemovePatiting(boolean removePatiting) {
/*  67 */     this.removePatiting = removePatiting;
/*     */   }
/*     */   
/*     */   public boolean isUpgradePatiting() {
/*  71 */     return this.upgradePatiting;
/*     */   }
/*     */   
/*     */   public void setUpgradePatiting(boolean upgradePatiting) {
/*  75 */     this.upgradePatiting = upgradePatiting;
/*     */   }
/*     */   
/*     */   public boolean isUpdateGlobalIndexes() {
/*  79 */     return this.updateGlobalIndexes;
/*     */   }
/*     */   
/*     */   public void setUpdateGlobalIndexes(boolean updateGlobalIndexes) {
/*  83 */     this.updateGlobalIndexes = updateGlobalIndexes;
/*     */   }
/*     */   
/*     */   public boolean isInvalidateGlobalIndexes() {
/*  87 */     return this.invalidateGlobalIndexes;
/*     */   }
/*     */   
/*     */   public void setInvalidateGlobalIndexes(boolean invalidateGlobalIndexes) {
/*  91 */     this.invalidateGlobalIndexes = invalidateGlobalIndexes;
/*     */   }
/*     */   
/*     */   public boolean isMergeSmallFiles() {
/*  95 */     return this.mergeSmallFiles;
/*     */   }
/*     */   
/*     */   public void setMergeSmallFiles(boolean mergeSmallFiles) {
/*  99 */     this.mergeSmallFiles = mergeSmallFiles;
/*     */   }
/*     */   
/*     */   public List<SQLAlterTableItem> getItems() {
/* 103 */     return this.items;
/*     */   }
/*     */   
/*     */   public void addItem(SQLAlterTableItem item) {
/* 107 */     if (item != null) {
/* 108 */       item.setParent((SQLObject)this);
/*     */     }
/* 110 */     this.items.add(item);
/*     */   }
/*     */   
/*     */   public SQLExprTableSource getTableSource() {
/* 114 */     return this.tableSource;
/*     */   }
/*     */   
/*     */   public void setTableSource(SQLExprTableSource tableSource) {
/* 118 */     this.tableSource = tableSource;
/*     */   }
/*     */   
/*     */   public SQLName getName() {
/* 122 */     if (getTableSource() == null) {
/* 123 */       return null;
/*     */     }
/* 125 */     return (SQLName)getTableSource().getExpr();
/*     */   }
/*     */   
/*     */   public void setName(SQLName name) {
/* 129 */     setTableSource(new SQLExprTableSource((SQLExpr)name));
/*     */   }
/*     */   
/*     */   public Map<String, SQLObject> getTableOptions() {
/* 133 */     return this.tableOptions;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/* 138 */     if (visitor.visit(this)) {
/* 139 */       acceptChild(visitor, getTableSource());
/* 140 */       acceptChild(visitor, getItems());
/*     */     } 
/* 142 */     visitor.endVisit(this);
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterTableStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */