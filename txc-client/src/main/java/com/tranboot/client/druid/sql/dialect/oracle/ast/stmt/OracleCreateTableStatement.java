/*     */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLPartitionBy;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateTableStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.OracleLobStorageClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.OracleStorageClause;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
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
/*     */ public class OracleCreateTableStatement
/*     */   extends SQLCreateTableStatement
/*     */   implements OracleDDLStatement
/*     */ {
/*     */   private SQLName tablespace;
/*     */   private boolean inMemoryMetadata;
/*     */   private boolean cursorSpecificSegment;
/*     */   private Boolean parallel;
/*     */   private OracleStorageClause storage;
/*     */   private OracleLobStorageClause lobStorage;
/*     */   private boolean organizationIndex = false;
/*     */   private SQLExpr ptcfree;
/*     */   private SQLExpr pctused;
/*     */   private SQLExpr initrans;
/*     */   private SQLExpr maxtrans;
/*     */   private Boolean logging;
/*     */   private Boolean compress;
/*     */   private boolean onCommit;
/*     */   private boolean preserveRows;
/*     */   private Boolean cache;
/*     */   private SQLPartitionBy partitioning;
/*     */   private DeferredSegmentCreation deferredSegmentCreation;
/*     */   
/*     */   public OracleCreateTableStatement() {
/*  61 */     super("oracle");
/*     */   }
/*     */   
/*     */   public OracleLobStorageClause getLobStorage() {
/*  65 */     return this.lobStorage;
/*     */   }
/*     */   
/*     */   public void setLobStorage(OracleLobStorageClause lobStorage) {
/*  69 */     this.lobStorage = lobStorage;
/*     */   }
/*     */   
/*     */   public DeferredSegmentCreation getDeferredSegmentCreation() {
/*  73 */     return this.deferredSegmentCreation;
/*     */   }
/*     */   
/*     */   public void setDeferredSegmentCreation(DeferredSegmentCreation deferredSegmentCreation) {
/*  77 */     this.deferredSegmentCreation = deferredSegmentCreation;
/*     */   }
/*     */   
/*     */   public SQLPartitionBy getPartitioning() {
/*  81 */     return this.partitioning;
/*     */   }
/*     */   
/*     */   public void setPartitioning(SQLPartitionBy partitioning) {
/*  85 */     this.partitioning = partitioning;
/*     */   }
/*     */   
/*     */   public Boolean getCache() {
/*  89 */     return this.cache;
/*     */   }
/*     */   
/*     */   public void setCache(Boolean cache) {
/*  93 */     this.cache = cache;
/*     */   }
/*     */   
/*     */   public boolean isOnCommit() {
/*  97 */     return this.onCommit;
/*     */   }
/*     */   
/*     */   public void setOnCommit(boolean onCommit) {
/* 101 */     this.onCommit = onCommit;
/*     */   }
/*     */   
/*     */   public boolean isPreserveRows() {
/* 105 */     return this.preserveRows;
/*     */   }
/*     */   
/*     */   public void setPreserveRows(boolean preserveRows) {
/* 109 */     this.preserveRows = preserveRows;
/*     */   }
/*     */   
/*     */   public Boolean getLogging() {
/* 113 */     return this.logging;
/*     */   }
/*     */   
/*     */   public void setLogging(Boolean logging) {
/* 117 */     this.logging = logging;
/*     */   }
/*     */   
/*     */   public Boolean getCompress() {
/* 121 */     return this.compress;
/*     */   }
/*     */   
/*     */   public void setCompress(Boolean compress) {
/* 125 */     this.compress = compress;
/*     */   }
/*     */   
/*     */   public SQLExpr getPtcfree() {
/* 129 */     return this.ptcfree;
/*     */   }
/*     */   
/*     */   public void setPtcfree(SQLExpr ptcfree) {
/* 133 */     this.ptcfree = ptcfree;
/*     */   }
/*     */   
/*     */   public SQLExpr getPctused() {
/* 137 */     return this.pctused;
/*     */   }
/*     */   
/*     */   public void setPctused(SQLExpr pctused) {
/* 141 */     this.pctused = pctused;
/*     */   }
/*     */   
/*     */   public SQLExpr getInitrans() {
/* 145 */     return this.initrans;
/*     */   }
/*     */   
/*     */   public void setInitrans(SQLExpr initrans) {
/* 149 */     this.initrans = initrans;
/*     */   }
/*     */   
/*     */   public SQLExpr getMaxtrans() {
/* 153 */     return this.maxtrans;
/*     */   }
/*     */   
/*     */   public void setMaxtrans(SQLExpr maxtrans) {
/* 157 */     this.maxtrans = maxtrans;
/*     */   }
/*     */   
/*     */   public boolean isOrganizationIndex() {
/* 161 */     return this.organizationIndex;
/*     */   }
/*     */   
/*     */   public void setOrganizationIndex(boolean organizationIndex) {
/* 165 */     this.organizationIndex = organizationIndex;
/*     */   }
/*     */   
/*     */   public Boolean getParallel() {
/* 169 */     return this.parallel;
/*     */   }
/*     */   
/*     */   public void setParallel(Boolean parallel) {
/* 173 */     this.parallel = parallel;
/*     */   }
/*     */   
/*     */   public boolean isCursorSpecificSegment() {
/* 177 */     return this.cursorSpecificSegment;
/*     */   }
/*     */   
/*     */   public void setCursorSpecificSegment(boolean cursorSpecificSegment) {
/* 181 */     this.cursorSpecificSegment = cursorSpecificSegment;
/*     */   }
/*     */   
/*     */   public boolean isInMemoryMetadata() {
/* 185 */     return this.inMemoryMetadata;
/*     */   }
/*     */   
/*     */   public void setInMemoryMetadata(boolean inMemoryMetadata) {
/* 189 */     this.inMemoryMetadata = inMemoryMetadata;
/*     */   }
/*     */   
/*     */   public SQLName getTablespace() {
/* 193 */     return this.tablespace;
/*     */   }
/*     */   
/*     */   public void setTablespace(SQLName tablespace) {
/* 197 */     this.tablespace = tablespace;
/*     */   }
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/* 201 */     accept0((OracleASTVisitor)visitor);
/*     */   }
/*     */   
/*     */   public OracleStorageClause getStorage() {
/* 205 */     return this.storage;
/*     */   }
/*     */   
/*     */   public void setStorage(OracleStorageClause storage) {
/* 209 */     this.storage = storage;
/*     */   }
/*     */   
/*     */   public void accept0(OracleASTVisitor visitor) {
/* 213 */     if (visitor.visit(this)) {
/* 214 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.tableSource);
/* 215 */       acceptChild((SQLASTVisitor)visitor, this.tableElementList);
/* 216 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.tablespace);
/* 217 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.select);
/* 218 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.storage);
/* 219 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.partitioning);
/*     */     } 
/* 221 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public enum DeferredSegmentCreation {
/* 225 */     IMMEDIATE, DEFERRED;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleCreateTableStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */