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
/*     */ public class SQLDropViewStatement
/*     */   extends SQLStatementImpl
/*     */   implements SQLDDLStatement
/*     */ {
/*  27 */   protected List<SQLExprTableSource> tableSources = new ArrayList<>();
/*     */   
/*     */   protected boolean cascade = false;
/*     */   
/*     */   protected boolean restrict = false;
/*     */   
/*     */   protected boolean ifExists = false;
/*     */   
/*     */   public SQLDropViewStatement() {}
/*     */   
/*     */   public SQLDropViewStatement(String dbType) {
/*  38 */     super(dbType);
/*     */   }
/*     */   
/*     */   public SQLDropViewStatement(SQLName name) {
/*  42 */     this(new SQLExprTableSource((SQLExpr)name));
/*     */   }
/*     */   
/*     */   public SQLDropViewStatement(SQLExprTableSource tableSource) {
/*  46 */     this.tableSources.add(tableSource);
/*     */   }
/*     */   
/*     */   public List<SQLExprTableSource> getTableSources() {
/*  50 */     return this.tableSources;
/*     */   }
/*     */   
/*     */   public void addPartition(SQLExprTableSource tableSource) {
/*  54 */     if (tableSource != null) {
/*  55 */       tableSource.setParent((SQLObject)this);
/*     */     }
/*  57 */     this.tableSources.add(tableSource);
/*     */   }
/*     */   
/*     */   public void setName(SQLName name) {
/*  61 */     addTableSource(new SQLExprTableSource((SQLExpr)name));
/*     */   }
/*     */   
/*     */   public void addTableSource(SQLName name) {
/*  65 */     addTableSource(new SQLExprTableSource((SQLExpr)name));
/*     */   }
/*     */   
/*     */   public void addTableSource(SQLExprTableSource tableSource) {
/*  69 */     this.tableSources.add(tableSource);
/*     */   }
/*     */   
/*     */   public boolean isCascade() {
/*  73 */     return this.cascade;
/*     */   }
/*     */   
/*     */   public void setCascade(boolean cascade) {
/*  77 */     this.cascade = cascade;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  82 */     if (visitor.visit(this)) {
/*  83 */       acceptChild(visitor, this.tableSources);
/*     */     }
/*  85 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public boolean isRestrict() {
/*  89 */     return this.restrict;
/*     */   }
/*     */   
/*     */   public void setRestrict(boolean restrict) {
/*  93 */     this.restrict = restrict;
/*     */   }
/*     */   
/*     */   public boolean isIfExists() {
/*  97 */     return this.ifExists;
/*     */   }
/*     */   
/*     */   public void setIfExists(boolean ifExists) {
/* 101 */     this.ifExists = ifExists;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLDropViewStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */