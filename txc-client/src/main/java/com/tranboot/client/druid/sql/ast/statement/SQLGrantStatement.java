/*     */ package com.tranboot.client.druid.sql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
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
/*     */ 
/*     */ public class SQLGrantStatement
/*     */   extends SQLStatementImpl
/*     */ {
/*  28 */   protected final List<SQLExpr> privileges = new ArrayList<>();
/*     */   
/*     */   protected SQLObject on;
/*     */   
/*     */   protected SQLExpr to;
/*     */   protected SQLObjectType objectType;
/*     */   private SQLExpr maxQueriesPerHour;
/*     */   private SQLExpr maxUpdatesPerHour;
/*     */   
/*     */   public SQLGrantStatement(String dbType) {
/*  38 */     super(dbType);
/*     */   }
/*     */ 
/*     */   
/*     */   private SQLExpr maxConnectionsPerHour;
/*     */   
/*     */   private SQLExpr maxUserConnections;
/*     */   
/*     */   private boolean adminOption;
/*     */   
/*     */   private SQLExpr identifiedBy;
/*     */ 
/*     */   
/*     */   public SQLGrantStatement() {}
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  54 */     if (visitor.visit(this)) {
/*  55 */       acceptChild(visitor, this.on);
/*  56 */       acceptChild(visitor, (SQLObject)this.to);
/*  57 */       acceptChild(visitor, (SQLObject)this.identifiedBy);
/*     */     } 
/*  59 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public SQLObjectType getObjectType() {
/*  63 */     return this.objectType;
/*     */   }
/*     */   
/*     */   public void setObjectType(SQLObjectType objectType) {
/*  67 */     this.objectType = objectType;
/*     */   }
/*     */   
/*     */   public SQLObject getOn() {
/*  71 */     return this.on;
/*     */   }
/*     */   
/*     */   public void setOn(SQLObject on) {
/*  75 */     this.on = on;
/*  76 */     on.setParent((SQLObject)this);
/*     */   }
/*     */   
/*     */   public SQLExpr getTo() {
/*  80 */     return this.to;
/*     */   }
/*     */   
/*     */   public void setTo(SQLExpr to) {
/*  84 */     this.to = to;
/*     */   }
/*     */   
/*     */   public List<SQLExpr> getPrivileges() {
/*  88 */     return this.privileges;
/*     */   }
/*     */   
/*     */   public SQLExpr getMaxQueriesPerHour() {
/*  92 */     return this.maxQueriesPerHour;
/*     */   }
/*     */   
/*     */   public void setMaxQueriesPerHour(SQLExpr maxQueriesPerHour) {
/*  96 */     this.maxQueriesPerHour = maxQueriesPerHour;
/*     */   }
/*     */   
/*     */   public SQLExpr getMaxUpdatesPerHour() {
/* 100 */     return this.maxUpdatesPerHour;
/*     */   }
/*     */   
/*     */   public void setMaxUpdatesPerHour(SQLExpr maxUpdatesPerHour) {
/* 104 */     this.maxUpdatesPerHour = maxUpdatesPerHour;
/*     */   }
/*     */   
/*     */   public SQLExpr getMaxConnectionsPerHour() {
/* 108 */     return this.maxConnectionsPerHour;
/*     */   }
/*     */   
/*     */   public void setMaxConnectionsPerHour(SQLExpr maxConnectionsPerHour) {
/* 112 */     this.maxConnectionsPerHour = maxConnectionsPerHour;
/*     */   }
/*     */   
/*     */   public SQLExpr getMaxUserConnections() {
/* 116 */     return this.maxUserConnections;
/*     */   }
/*     */   
/*     */   public void setMaxUserConnections(SQLExpr maxUserConnections) {
/* 120 */     this.maxUserConnections = maxUserConnections;
/*     */   }
/*     */   
/*     */   public boolean isAdminOption() {
/* 124 */     return this.adminOption;
/*     */   }
/*     */   
/*     */   public void setAdminOption(boolean adminOption) {
/* 128 */     this.adminOption = adminOption;
/*     */   }
/*     */   
/*     */   public SQLExpr getIdentifiedBy() {
/* 132 */     return this.identifiedBy;
/*     */   }
/*     */   
/*     */   public void setIdentifiedBy(SQLExpr identifiedBy) {
/* 136 */     this.identifiedBy = identifiedBy;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLGrantStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */