/*     */ package com.tranboot.client.druid.sql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatementImpl;
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
/*     */ public class SQLTruncateStatement
/*     */   extends SQLStatementImpl
/*     */ {
/*  27 */   protected List<SQLExprTableSource> tableSources = new ArrayList<>(2);
/*     */   
/*     */   private boolean purgeSnapshotLog = false;
/*     */   
/*     */   private boolean only;
/*     */   
/*     */   private Boolean restartIdentity;
/*     */   
/*     */   private Boolean cascade;
/*     */   
/*     */   private boolean dropStorage = false;
/*     */   
/*     */   private boolean reuseStorage = false;
/*     */   private boolean immediate = false;
/*     */   private boolean ignoreDeleteTriggers = false;
/*     */   private boolean restrictWhenDeleteTriggers = false;
/*     */   private boolean continueIdentity = false;
/*     */   
/*     */   public SQLTruncateStatement() {}
/*     */   
/*     */   public SQLTruncateStatement(String dbType) {
/*  48 */     super(dbType);
/*     */   }
/*     */   
/*     */   public List<SQLExprTableSource> getTableSources() {
/*  52 */     return this.tableSources;
/*     */   }
/*     */   
/*     */   public void setTableSources(List<SQLExprTableSource> tableSources) {
/*  56 */     this.tableSources = tableSources;
/*     */   }
/*     */   
/*     */   public void addTableSource(SQLName name) {
/*  60 */     SQLExprTableSource tableSource = new SQLExprTableSource((SQLExpr)name);
/*  61 */     tableSource.setParent((SQLObject)this);
/*  62 */     this.tableSources.add(tableSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  67 */     if (visitor.visit(this)) {
/*  68 */       acceptChild(visitor, this.tableSources);
/*     */     }
/*  70 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public boolean isPurgeSnapshotLog() {
/*  74 */     return this.purgeSnapshotLog;
/*     */   }
/*     */   
/*     */   public void setPurgeSnapshotLog(boolean purgeSnapshotLog) {
/*  78 */     this.purgeSnapshotLog = purgeSnapshotLog;
/*     */   }
/*     */   
/*     */   public boolean isOnly() {
/*  82 */     return this.only;
/*     */   }
/*     */   
/*     */   public void setOnly(boolean only) {
/*  86 */     this.only = only;
/*     */   }
/*     */   
/*     */   public Boolean getRestartIdentity() {
/*  90 */     return this.restartIdentity;
/*     */   }
/*     */   
/*     */   public void setRestartIdentity(Boolean restartIdentity) {
/*  94 */     this.restartIdentity = restartIdentity;
/*     */   }
/*     */   
/*     */   public Boolean getCascade() {
/*  98 */     return this.cascade;
/*     */   }
/*     */   
/*     */   public void setCascade(Boolean cascade) {
/* 102 */     this.cascade = cascade;
/*     */   }
/*     */   
/*     */   public boolean isDropStorage() {
/* 106 */     return this.dropStorage;
/*     */   }
/*     */   
/*     */   public void setDropStorage(boolean dropStorage) {
/* 110 */     this.dropStorage = dropStorage;
/*     */   }
/*     */   
/*     */   public boolean isReuseStorage() {
/* 114 */     return this.reuseStorage;
/*     */   }
/*     */   
/*     */   public void setReuseStorage(boolean reuseStorage) {
/* 118 */     this.reuseStorage = reuseStorage;
/*     */   }
/*     */   
/*     */   public boolean isImmediate() {
/* 122 */     return this.immediate;
/*     */   }
/*     */   
/*     */   public void setImmediate(boolean immediate) {
/* 126 */     this.immediate = immediate;
/*     */   }
/*     */   
/*     */   public boolean isIgnoreDeleteTriggers() {
/* 130 */     return this.ignoreDeleteTriggers;
/*     */   }
/*     */   
/*     */   public void setIgnoreDeleteTriggers(boolean ignoreDeleteTriggers) {
/* 134 */     this.ignoreDeleteTriggers = ignoreDeleteTriggers;
/*     */   }
/*     */   
/*     */   public boolean isRestrictWhenDeleteTriggers() {
/* 138 */     return this.restrictWhenDeleteTriggers;
/*     */   }
/*     */   
/*     */   public void setRestrictWhenDeleteTriggers(boolean restrictWhenDeleteTriggers) {
/* 142 */     this.restrictWhenDeleteTriggers = restrictWhenDeleteTriggers;
/*     */   }
/*     */   
/*     */   public boolean isContinueIdentity() {
/* 146 */     return this.continueIdentity;
/*     */   }
/*     */   
/*     */   public void setContinueIdentity(boolean continueIdentity) {
/* 150 */     this.continueIdentity = continueIdentity;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLTruncateStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */