/*     */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.expr.SQLLiteralExpr;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MySqlLoadXmlStatement
/*     */   extends MySqlStatementImpl
/*     */ {
/*     */   private boolean lowPriority = false;
/*     */   private boolean concurrent = false;
/*     */   private boolean local = false;
/*     */   private SQLLiteralExpr fileName;
/*     */   private boolean replicate = false;
/*     */   private boolean ignore = false;
/*     */   private SQLName tableName;
/*     */   private String charset;
/*     */   private SQLExpr rowsIdentifiedBy;
/*     */   private SQLExpr ignoreLinesNumber;
/*  45 */   private final List<SQLExpr> setList = new ArrayList<>();
/*     */   
/*     */   public SQLExpr getRowsIdentifiedBy() {
/*  48 */     return this.rowsIdentifiedBy;
/*     */   }
/*     */   
/*     */   public void setRowsIdentifiedBy(SQLExpr rowsIdentifiedBy) {
/*  52 */     this.rowsIdentifiedBy = rowsIdentifiedBy;
/*     */   }
/*     */   
/*     */   public boolean isLowPriority() {
/*  56 */     return this.lowPriority;
/*     */   }
/*     */   
/*     */   public void setLowPriority(boolean lowPriority) {
/*  60 */     this.lowPriority = lowPriority;
/*     */   }
/*     */   
/*     */   public boolean isConcurrent() {
/*  64 */     return this.concurrent;
/*     */   }
/*     */   
/*     */   public void setConcurrent(boolean concurrent) {
/*  68 */     this.concurrent = concurrent;
/*     */   }
/*     */   
/*     */   public boolean isLocal() {
/*  72 */     return this.local;
/*     */   }
/*     */   
/*     */   public void setLocal(boolean local) {
/*  76 */     this.local = local;
/*     */   }
/*     */   
/*     */   public SQLLiteralExpr getFileName() {
/*  80 */     return this.fileName;
/*     */   }
/*     */   
/*     */   public void setFileName(SQLLiteralExpr fileName) {
/*  84 */     this.fileName = fileName;
/*     */   }
/*     */   
/*     */   public boolean isReplicate() {
/*  88 */     return this.replicate;
/*     */   }
/*     */   
/*     */   public void setReplicate(boolean replicate) {
/*  92 */     this.replicate = replicate;
/*     */   }
/*     */   
/*     */   public boolean isIgnore() {
/*  96 */     return this.ignore;
/*     */   }
/*     */   
/*     */   public void setIgnore(boolean ignore) {
/* 100 */     this.ignore = ignore;
/*     */   }
/*     */   
/*     */   public SQLName getTableName() {
/* 104 */     return this.tableName;
/*     */   }
/*     */   
/*     */   public void setTableName(SQLName tableName) {
/* 108 */     this.tableName = tableName;
/*     */   }
/*     */   
/*     */   public String getCharset() {
/* 112 */     return this.charset;
/*     */   }
/*     */   
/*     */   public void setCharset(String charset) {
/* 116 */     this.charset = charset;
/*     */   }
/*     */   
/*     */   public SQLExpr getIgnoreLinesNumber() {
/* 120 */     return this.ignoreLinesNumber;
/*     */   }
/*     */   
/*     */   public void setIgnoreLinesNumber(SQLExpr ignoreLinesNumber) {
/* 124 */     this.ignoreLinesNumber = ignoreLinesNumber;
/*     */   }
/*     */   
/*     */   public List<SQLExpr> getSetList() {
/* 128 */     return this.setList;
/*     */   }
/*     */   
/*     */   public void accept0(MySqlASTVisitor visitor) {
/* 132 */     if (visitor.visit(this)) {
/* 133 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.fileName);
/* 134 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.tableName);
/* 135 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.rowsIdentifiedBy);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 141 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.ignoreLinesNumber);
/* 142 */       acceptChild((SQLASTVisitor)visitor, this.setList);
/*     */     } 
/* 144 */     visitor.endVisit(this);
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlLoadXmlStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */