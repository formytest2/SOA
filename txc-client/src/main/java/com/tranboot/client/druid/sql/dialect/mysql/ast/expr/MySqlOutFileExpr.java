/*     */ package com.tranboot.client.druid.sql.dialect.mysql.ast.expr;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.expr.SQLLiteralExpr;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MySqlObjectImpl;
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
/*     */ public class MySqlOutFileExpr
/*     */   extends MySqlObjectImpl
/*     */   implements SQLExpr
/*     */ {
/*     */   private SQLExpr file;
/*     */   private String charset;
/*     */   private SQLExpr columnsTerminatedBy;
/*     */   private boolean columnsEnclosedOptionally = false;
/*     */   private SQLLiteralExpr columnsEnclosedBy;
/*     */   private SQLLiteralExpr columnsEscaped;
/*     */   private SQLLiteralExpr linesStartingBy;
/*     */   private SQLLiteralExpr linesTerminatedBy;
/*     */   private SQLExpr ignoreLinesNumber;
/*     */   
/*     */   public MySqlOutFileExpr() {}
/*     */   
/*     */   public MySqlOutFileExpr(SQLExpr file) {
/*  42 */     this.file = file;
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept0(MySqlASTVisitor visitor) {
/*  47 */     if (visitor.visit(this)) {
/*  48 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.file);
/*     */     }
/*  50 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public SQLExpr getFile() {
/*  54 */     return this.file;
/*     */   }
/*     */   
/*     */   public void setFile(SQLExpr file) {
/*  58 */     this.file = file;
/*     */   }
/*     */   
/*     */   public String getCharset() {
/*  62 */     return this.charset;
/*     */   }
/*     */   
/*     */   public void setCharset(String charset) {
/*  66 */     this.charset = charset;
/*     */   }
/*     */   
/*     */   public SQLExpr getColumnsTerminatedBy() {
/*  70 */     return this.columnsTerminatedBy;
/*     */   }
/*     */   
/*     */   public void setColumnsTerminatedBy(SQLExpr columnsTerminatedBy) {
/*  74 */     this.columnsTerminatedBy = columnsTerminatedBy;
/*     */   }
/*     */   
/*     */   public boolean isColumnsEnclosedOptionally() {
/*  78 */     return this.columnsEnclosedOptionally;
/*     */   }
/*     */   
/*     */   public void setColumnsEnclosedOptionally(boolean columnsEnclosedOptionally) {
/*  82 */     this.columnsEnclosedOptionally = columnsEnclosedOptionally;
/*     */   }
/*     */   
/*     */   public SQLLiteralExpr getColumnsEnclosedBy() {
/*  86 */     return this.columnsEnclosedBy;
/*     */   }
/*     */   
/*     */   public void setColumnsEnclosedBy(SQLLiteralExpr columnsEnclosedBy) {
/*  90 */     this.columnsEnclosedBy = columnsEnclosedBy;
/*     */   }
/*     */   
/*     */   public SQLLiteralExpr getColumnsEscaped() {
/*  94 */     return this.columnsEscaped;
/*     */   }
/*     */   
/*     */   public void setColumnsEscaped(SQLLiteralExpr columnsEscaped) {
/*  98 */     this.columnsEscaped = columnsEscaped;
/*     */   }
/*     */   
/*     */   public SQLLiteralExpr getLinesStartingBy() {
/* 102 */     return this.linesStartingBy;
/*     */   }
/*     */   
/*     */   public void setLinesStartingBy(SQLLiteralExpr linesStartingBy) {
/* 106 */     this.linesStartingBy = linesStartingBy;
/*     */   }
/*     */   
/*     */   public SQLLiteralExpr getLinesTerminatedBy() {
/* 110 */     return this.linesTerminatedBy;
/*     */   }
/*     */   
/*     */   public void setLinesTerminatedBy(SQLLiteralExpr linesTerminatedBy) {
/* 114 */     this.linesTerminatedBy = linesTerminatedBy;
/*     */   }
/*     */   
/*     */   public SQLExpr getIgnoreLinesNumber() {
/* 118 */     return this.ignoreLinesNumber;
/*     */   }
/*     */   
/*     */   public void setIgnoreLinesNumber(SQLExpr ignoreLinesNumber) {
/* 122 */     this.ignoreLinesNumber = ignoreLinesNumber;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\expr\MySqlOutFileExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */