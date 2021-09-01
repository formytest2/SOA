/*     */ package com.tranboot.client.druid.sql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatement;
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
/*     */ 
/*     */ 
/*     */ public class SQLCreateTriggerStatement
/*     */   extends SQLStatementImpl
/*     */ {
/*     */   private SQLName name;
/*     */   private boolean orReplace = false;
/*     */   private TriggerType triggerType;
/*  33 */   private final List<TriggerEvent> triggerEvents = new ArrayList<>();
/*     */   
/*     */   private SQLName on;
/*     */   
/*     */   private boolean forEachRow = false;
/*     */   
/*     */   private SQLStatement body;
/*     */ 
/*     */   
/*     */   public SQLCreateTriggerStatement() {}
/*     */ 
/*     */   
/*     */   public SQLCreateTriggerStatement(String dbType) {
/*  46 */     super(dbType);
/*     */   }
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  50 */     if (visitor.visit(this)) {
/*  51 */       acceptChild(visitor, (SQLObject)this.name);
/*  52 */       acceptChild(visitor, (SQLObject)this.on);
/*  53 */       acceptChild(visitor, (SQLObject)this.body);
/*     */     } 
/*  55 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public SQLName getOn() {
/*  59 */     return this.on;
/*     */   }
/*     */   
/*     */   public void setOn(SQLName on) {
/*  63 */     this.on = on;
/*     */   }
/*     */   
/*     */   public SQLName getName() {
/*  67 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(SQLName name) {
/*  71 */     if (name != null) {
/*  72 */       name.setParent((SQLObject)this);
/*     */     }
/*  74 */     this.name = name;
/*     */   }
/*     */   
/*     */   public SQLStatement getBody() {
/*  78 */     return this.body;
/*     */   }
/*     */   
/*     */   public void setBody(SQLStatement body) {
/*  82 */     if (body != null) {
/*  83 */       body.setParent((SQLObject)this);
/*     */     }
/*  85 */     this.body = body;
/*     */   }
/*     */   
/*     */   public boolean isOrReplace() {
/*  89 */     return this.orReplace;
/*     */   }
/*     */   
/*     */   public void setOrReplace(boolean orReplace) {
/*  93 */     this.orReplace = orReplace;
/*     */   }
/*     */   
/*     */   public TriggerType getTriggerType() {
/*  97 */     return this.triggerType;
/*     */   }
/*     */   
/*     */   public void setTriggerType(TriggerType triggerType) {
/* 101 */     this.triggerType = triggerType;
/*     */   }
/*     */   
/*     */   public List<TriggerEvent> getTriggerEvents() {
/* 105 */     return this.triggerEvents;
/*     */   }
/*     */   
/*     */   public boolean isForEachRow() {
/* 109 */     return this.forEachRow;
/*     */   }
/*     */   
/*     */   public void setForEachRow(boolean forEachRow) {
/* 113 */     this.forEachRow = forEachRow;
/*     */   }
/*     */   
/*     */   public enum TriggerType {
/* 117 */     BEFORE, AFTER, INSTEAD_OF;
/*     */   }
/*     */   
/*     */   public enum TriggerEvent {
/* 121 */     INSERT, UPDATE, DELETE;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLCreateTriggerStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */