/*     */ package com.tranboot.client.druid.sql.dialect.oracle.ast.clause;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;
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
/*     */ public class OracleStorageClause
/*     */   extends OracleSQLObjectImpl
/*     */ {
/*     */   private SQLExpr initial;
/*     */   private SQLExpr next;
/*     */   private SQLExpr minExtents;
/*     */   private SQLExpr maxExtents;
/*     */   private SQLExpr maxSize;
/*     */   private SQLExpr pctIncrease;
/*     */   private SQLExpr freeLists;
/*     */   private SQLExpr freeListGroups;
/*     */   private SQLExpr bufferPool;
/*     */   private SQLExpr objno;
/*     */   private FlashCacheType flashCache;
/*     */   private FlashCacheType cellFlashCache;
/*     */   
/*     */   public void accept0(OracleASTVisitor visitor) {
/*  39 */     if (visitor.visit(this)) {
/*  40 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.initial);
/*  41 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.next);
/*  42 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.minExtents);
/*  43 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.maxExtents);
/*  44 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.maxSize);
/*  45 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.pctIncrease);
/*  46 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.freeLists);
/*  47 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.freeListGroups);
/*  48 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.bufferPool);
/*  49 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.objno);
/*     */     } 
/*  51 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public SQLExpr getMaxSize() {
/*  55 */     return this.maxSize;
/*     */   }
/*     */   
/*     */   public void setMaxSize(SQLExpr maxSize) {
/*  59 */     this.maxSize = maxSize;
/*     */   }
/*     */   
/*     */   public FlashCacheType getFlashCache() {
/*  63 */     return this.flashCache;
/*     */   }
/*     */   
/*     */   public void setFlashCache(FlashCacheType flashCache) {
/*  67 */     this.flashCache = flashCache;
/*     */   }
/*     */   
/*     */   public FlashCacheType getCellFlashCache() {
/*  71 */     return this.cellFlashCache;
/*     */   }
/*     */   
/*     */   public void setCellFlashCache(FlashCacheType cellFlashCache) {
/*  75 */     this.cellFlashCache = cellFlashCache;
/*     */   }
/*     */   
/*     */   public SQLExpr getPctIncrease() {
/*  79 */     return this.pctIncrease;
/*     */   }
/*     */   
/*     */   public void setPctIncrease(SQLExpr pctIncrease) {
/*  83 */     this.pctIncrease = pctIncrease;
/*     */   }
/*     */   
/*     */   public SQLExpr getNext() {
/*  87 */     return this.next;
/*     */   }
/*     */   
/*     */   public void setNext(SQLExpr next) {
/*  91 */     this.next = next;
/*     */   }
/*     */   
/*     */   public SQLExpr getMinExtents() {
/*  95 */     return this.minExtents;
/*     */   }
/*     */   
/*     */   public void setMinExtents(SQLExpr minExtents) {
/*  99 */     this.minExtents = minExtents;
/*     */   }
/*     */   
/*     */   public SQLExpr getMaxExtents() {
/* 103 */     return this.maxExtents;
/*     */   }
/*     */   
/*     */   public void setMaxExtents(SQLExpr maxExtents) {
/* 107 */     this.maxExtents = maxExtents;
/*     */   }
/*     */   
/*     */   public SQLExpr getObjno() {
/* 111 */     return this.objno;
/*     */   }
/*     */   
/*     */   public void setObjno(SQLExpr objno) {
/* 115 */     this.objno = objno;
/*     */   }
/*     */   
/*     */   public SQLExpr getInitial() {
/* 119 */     return this.initial;
/*     */   }
/*     */   
/*     */   public void setInitial(SQLExpr initial) {
/* 123 */     this.initial = initial;
/*     */   }
/*     */   
/*     */   public SQLExpr getFreeLists() {
/* 127 */     return this.freeLists;
/*     */   }
/*     */   
/*     */   public void setFreeLists(SQLExpr freeLists) {
/* 131 */     this.freeLists = freeLists;
/*     */   }
/*     */   
/*     */   public SQLExpr getFreeListGroups() {
/* 135 */     return this.freeListGroups;
/*     */   }
/*     */   
/*     */   public void setFreeListGroups(SQLExpr freeListGroups) {
/* 139 */     this.freeListGroups = freeListGroups;
/*     */   }
/*     */   
/*     */   public SQLExpr getBufferPool() {
/* 143 */     return this.bufferPool;
/*     */   }
/*     */   
/*     */   public void setBufferPool(SQLExpr bufferPool) {
/* 147 */     this.bufferPool = bufferPool;
/*     */   }
/*     */   
/*     */   public enum FlashCacheType {
/* 151 */     KEEP, NONE, DEFAULT;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\clause\OracleStorageClause.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */