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
/*     */ public abstract class FlashbackQueryClause
/*     */   extends OracleSQLObjectImpl
/*     */ {
/*     */   private Type type;
/*     */   
/*     */   public Type getType() {
/*  26 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(Type type) {
/*  30 */     this.type = type;
/*     */   }
/*     */   
/*     */   public enum Type {
/*  34 */     SCN, TIMESTAMP;
/*     */   }
/*     */   
/*     */   public static class VersionsFlashbackQueryClause
/*     */     extends FlashbackQueryClause {
/*     */     private SQLExpr begin;
/*     */     private SQLExpr end;
/*     */     
/*     */     public SQLExpr getBegin() {
/*  43 */       return this.begin;
/*     */     }
/*     */     
/*     */     public void setBegin(SQLExpr begin) {
/*  47 */       this.begin = begin;
/*     */     }
/*     */     
/*     */     public SQLExpr getEnd() {
/*  51 */       return this.end;
/*     */     }
/*     */     
/*     */     public void setEnd(SQLExpr end) {
/*  55 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept0(OracleASTVisitor visitor) {
/*  60 */       if (visitor.visit(this)) {
/*  61 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.begin);
/*  62 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.end);
/*     */       } 
/*  64 */       visitor.endVisit(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AsOfFlashbackQueryClause
/*     */     extends FlashbackQueryClause {
/*     */     private SQLExpr expr;
/*     */     
/*     */     public SQLExpr getExpr() {
/*  73 */       return this.expr;
/*     */     }
/*     */     
/*     */     public void setExpr(SQLExpr expr) {
/*  77 */       this.expr = expr;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept0(OracleASTVisitor visitor) {
/*  82 */       if (visitor.visit(this)) {
/*  83 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.expr);
/*     */       }
/*  85 */       visitor.endVisit(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AsOfSnapshotClause
/*     */     extends FlashbackQueryClause {
/*     */     private SQLExpr expr;
/*     */     
/*     */     public SQLExpr getExpr() {
/*  94 */       return this.expr;
/*     */     }
/*     */     
/*     */     public void setExpr(SQLExpr expr) {
/*  98 */       this.expr = expr;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept0(OracleASTVisitor visitor) {
/* 103 */       if (visitor.visit(this)) {
/* 104 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.expr);
/*     */       }
/* 106 */       visitor.endVisit(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\clause\FlashbackQueryClause.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */