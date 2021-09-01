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
/*     */ 
/*     */ public class MySqlLoadDataInFileStatement
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
/*     */   private SQLLiteralExpr columnsTerminatedBy;
/*     */   private boolean columnsEnclosedOptionally = false;
/*     */   private SQLLiteralExpr columnsEnclosedBy;
/*     */   private SQLLiteralExpr columnsEscaped;
/*     */   private SQLLiteralExpr linesStartingBy;
/*     */   private SQLLiteralExpr linesTerminatedBy;
/*     */   private SQLExpr ignoreLinesNumber;
/*  51 */   private List<SQLExpr> setList = new ArrayList<>();
/*     */   
/*  53 */   private List<SQLExpr> columns = new ArrayList<>();
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
/*     */   public SQLLiteralExpr getColumnsTerminatedBy() {
/* 120 */     return this.columnsTerminatedBy;
/*     */   }
/*     */   
/*     */   public void setColumnsTerminatedBy(SQLLiteralExpr columnsTerminatedBy) {
/* 124 */     this.columnsTerminatedBy = columnsTerminatedBy;
/*     */   }
/*     */   
/*     */   public boolean isColumnsEnclosedOptionally() {
/* 128 */     return this.columnsEnclosedOptionally;
/*     */   }
/*     */   
/*     */   public void setColumnsEnclosedOptionally(boolean columnsEnclosedOptionally) {
/* 132 */     this.columnsEnclosedOptionally = columnsEnclosedOptionally;
/*     */   }
/*     */   
/*     */   public SQLLiteralExpr getColumnsEnclosedBy() {
/* 136 */     return this.columnsEnclosedBy;
/*     */   }
/*     */   
/*     */   public void setColumnsEnclosedBy(SQLLiteralExpr columnsEnclosedBy) {
/* 140 */     this.columnsEnclosedBy = columnsEnclosedBy;
/*     */   }
/*     */   
/*     */   public SQLLiteralExpr getColumnsEscaped() {
/* 144 */     return this.columnsEscaped;
/*     */   }
/*     */   
/*     */   public void setColumnsEscaped(SQLLiteralExpr columnsEscaped) {
/* 148 */     this.columnsEscaped = columnsEscaped;
/*     */   }
/*     */   
/*     */   public SQLLiteralExpr getLinesStartingBy() {
/* 152 */     return this.linesStartingBy;
/*     */   }
/*     */   
/*     */   public void setLinesStartingBy(SQLLiteralExpr linesStartingBy) {
/* 156 */     this.linesStartingBy = linesStartingBy;
/*     */   }
/*     */   
/*     */   public SQLLiteralExpr getLinesTerminatedBy() {
/* 160 */     return this.linesTerminatedBy;
/*     */   }
/*     */   
/*     */   public void setLinesTerminatedBy(SQLLiteralExpr linesTerminatedBy) {
/* 164 */     this.linesTerminatedBy = linesTerminatedBy;
/*     */   }
/*     */   
/*     */   public SQLExpr getIgnoreLinesNumber() {
/* 168 */     return this.ignoreLinesNumber;
/*     */   }
/*     */   
/*     */   public void setIgnoreLinesNumber(SQLExpr ignoreLinesNumber) {
/* 172 */     this.ignoreLinesNumber = ignoreLinesNumber;
/*     */   }
/*     */   
/*     */   public List<SQLExpr> getSetList() {
/* 176 */     return this.setList;
/*     */   }
/*     */   
/*     */   public void accept0(MySqlASTVisitor visitor) {
/* 180 */     if (visitor.visit(this)) {
/* 181 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.fileName);
/* 182 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.tableName);
/* 183 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.columnsTerminatedBy);
/* 184 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.columnsEnclosedBy);
/* 185 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.columnsEscaped);
/* 186 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.linesStartingBy);
/* 187 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.linesTerminatedBy);
/* 188 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.ignoreLinesNumber);
/* 189 */       acceptChild((SQLASTVisitor)visitor, this.setList);
/*     */     } 
/* 191 */     visitor.endVisit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<SQLExpr> getColumns() {
/* 196 */     return this.columns;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setColumns(List<SQLExpr> columns) {
/* 201 */     this.columns = columns;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSetList(List<SQLExpr> setList) {
/* 206 */     this.setList = setList;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlLoadDataInFileStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */