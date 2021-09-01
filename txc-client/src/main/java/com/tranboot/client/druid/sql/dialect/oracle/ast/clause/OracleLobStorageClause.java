/*     */ package com.tranboot.client.druid.sql.dialect.oracle.ast.clause;
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
/*     */ public class OracleLobStorageClause
/*     */   extends OracleSQLObjectImpl
/*     */ {
/*  28 */   private final List<SQLName> items = new ArrayList<>();
/*     */   
/*     */   private boolean secureFile = false;
/*     */   
/*     */   private boolean basicFile = false;
/*     */   
/*     */   private SQLName tableSpace;
/*     */   
/*     */   private Boolean enable;
/*     */   
/*     */   private SQLExpr chunk;
/*     */   
/*     */   private Boolean cache;
/*     */   
/*     */   private Boolean logging;
/*     */   private Boolean compress;
/*     */   private Boolean keepDuplicate;
/*     */   
/*     */   public void accept0(OracleASTVisitor visitor) {
/*  47 */     if (visitor.visit(this)) {
/*  48 */       acceptChild((SQLASTVisitor)visitor, this.items);
/*  49 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.tableSpace);
/*     */     } 
/*  51 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public Boolean getEnable() {
/*  55 */     return this.enable;
/*     */   }
/*     */   
/*     */   public void setEnable(Boolean enable) {
/*  59 */     this.enable = enable;
/*     */   }
/*     */   
/*     */   public SQLExpr getChunk() {
/*  63 */     return this.chunk;
/*     */   }
/*     */   
/*     */   public void setChunk(SQLExpr chunk) {
/*  67 */     this.chunk = chunk;
/*     */   }
/*     */   
/*     */   public List<SQLName> getItems() {
/*  71 */     return this.items;
/*     */   }
/*     */   
/*     */   public boolean isSecureFile() {
/*  75 */     return this.secureFile;
/*     */   }
/*     */   
/*     */   public void setSecureFile(boolean secureFile) {
/*  79 */     this.secureFile = secureFile;
/*     */   }
/*     */   
/*     */   public boolean isBasicFile() {
/*  83 */     return this.basicFile;
/*     */   }
/*     */   
/*     */   public void setBasicFile(boolean basicFile) {
/*  87 */     this.basicFile = basicFile;
/*     */   }
/*     */   
/*     */   public SQLName getTableSpace() {
/*  91 */     return this.tableSpace;
/*     */   }
/*     */   
/*     */   public void setTableSpace(SQLName tableSpace) {
/*  95 */     this.tableSpace = tableSpace;
/*     */   }
/*     */   
/*     */   public Boolean getCache() {
/*  99 */     return this.cache;
/*     */   }
/*     */   
/*     */   public void setCache(Boolean cache) {
/* 103 */     this.cache = cache;
/*     */   }
/*     */   
/*     */   public Boolean getLogging() {
/* 107 */     return this.logging;
/*     */   }
/*     */   
/*     */   public void setLogging(Boolean logging) {
/* 111 */     this.logging = logging;
/*     */   }
/*     */   
/*     */   public Boolean getCompress() {
/* 115 */     return this.compress;
/*     */   }
/*     */   
/*     */   public void setCompress(Boolean compress) {
/* 119 */     this.compress = compress;
/*     */   }
/*     */   
/*     */   public Boolean getKeepDuplicate() {
/* 123 */     return this.keepDuplicate;
/*     */   }
/*     */   
/*     */   public void setKeepDuplicate(Boolean keepDuplicate) {
/* 127 */     this.keepDuplicate = keepDuplicate;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\clause\OracleLobStorageClause.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */