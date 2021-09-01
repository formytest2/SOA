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
/*     */ public class SQLDropTableStatement
/*     */   extends SQLStatementImpl
/*     */   implements SQLDDLStatement
/*     */ {
/*  27 */   protected List<SQLExprTableSource> tableSources = new ArrayList<>();
/*     */   
/*     */   private boolean purge;
/*     */   
/*     */   protected boolean cascade = false;
/*     */   
/*     */   protected boolean restrict = false;
/*     */   
/*     */   protected boolean ifExists = false;
/*     */   private boolean temporary = false;
/*     */   
/*     */   public SQLDropTableStatement() {}
/*     */   
/*     */   public SQLDropTableStatement(String dbType) {
/*  41 */     super(dbType);
/*     */   }
/*     */   
/*     */   public SQLDropTableStatement(SQLName name, String dbType) {
/*  45 */     this(new SQLExprTableSource((SQLExpr)name), dbType);
/*     */   }
/*     */   
/*     */   public SQLDropTableStatement(SQLName name) {
/*  49 */     this(name, (String)null);
/*     */   }
/*     */   
/*     */   public SQLDropTableStatement(SQLExprTableSource tableSource) {
/*  53 */     this(tableSource, (String)null);
/*     */   }
/*     */   
/*     */   public SQLDropTableStatement(SQLExprTableSource tableSource, String dbType) {
/*  57 */     this(dbType);
/*  58 */     this.tableSources.add(tableSource);
/*     */   }
/*     */   
/*     */   public List<SQLExprTableSource> getTableSources() {
/*  62 */     return this.tableSources;
/*     */   }
/*     */   
/*     */   public void addPartition(SQLExprTableSource tableSource) {
/*  66 */     if (tableSource != null) {
/*  67 */       tableSource.setParent((SQLObject)this);
/*     */     }
/*  69 */     this.tableSources.add(tableSource);
/*     */   }
/*     */   
/*     */   public void setName(SQLName name) {
/*  73 */     addTableSource(new SQLExprTableSource((SQLExpr)name));
/*     */   }
/*     */   
/*     */   public void addTableSource(SQLName name) {
/*  77 */     addTableSource(new SQLExprTableSource((SQLExpr)name));
/*     */   }
/*     */   
/*     */   public void addTableSource(SQLExprTableSource tableSource) {
/*  81 */     this.tableSources.add(tableSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  86 */     if (visitor.visit(this)) {
/*  87 */       acceptChild(visitor, this.tableSources);
/*     */     }
/*  89 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public boolean isPurge() {
/*  93 */     return this.purge;
/*     */   }
/*     */   
/*     */   public void setPurge(boolean purge) {
/*  97 */     this.purge = purge;
/*     */   }
/*     */   
/*     */   public boolean isIfExists() {
/* 101 */     return this.ifExists;
/*     */   }
/*     */   
/*     */   public void setIfExists(boolean ifExists) {
/* 105 */     this.ifExists = ifExists;
/*     */   }
/*     */   
/*     */   public boolean isCascade() {
/* 109 */     return this.cascade;
/*     */   }
/*     */   
/*     */   public void setCascade(boolean cascade) {
/* 113 */     this.cascade = cascade;
/*     */   }
/*     */   
/*     */   public boolean isRestrict() {
/* 117 */     return this.restrict;
/*     */   }
/*     */   
/*     */   public void setRestrict(boolean restrict) {
/* 121 */     this.restrict = restrict;
/*     */   }
/*     */   
/*     */   public boolean isTemporary() {
/* 125 */     return this.temporary;
/*     */   }
/*     */   
/*     */   public void setTemporary(boolean temporary) {
/* 129 */     this.temporary = temporary;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLDropTableStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */