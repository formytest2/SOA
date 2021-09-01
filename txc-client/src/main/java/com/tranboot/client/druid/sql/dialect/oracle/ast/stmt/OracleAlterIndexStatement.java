/*     */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OracleAlterIndexStatement
/*     */   extends OracleStatementImpl
/*     */ {
/*     */   private SQLName name;
/*     */   private SQLName renameTo;
/*     */   private boolean compile;
/*     */   private Boolean enable;
/*     */   private Boolean monitoringUsage;
/*     */   private Rebuild rebuild;
/*     */   private SQLExpr parallel;
/*     */   
/*     */   public void accept0(OracleASTVisitor visitor) {
/*  42 */     if (visitor.visit(this)) {
/*  43 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.name);
/*  44 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.renameTo);
/*  45 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.rebuild);
/*  46 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.parallel);
/*     */     } 
/*  48 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public SQLName getRenameTo() {
/*  52 */     return this.renameTo;
/*     */   }
/*     */   
/*     */   public void setRenameTo(SQLName renameTo) {
/*  56 */     this.renameTo = renameTo;
/*     */   }
/*     */   
/*     */   public SQLExpr getParallel() {
/*  60 */     return this.parallel;
/*     */   }
/*     */   
/*     */   public void setParallel(SQLExpr parallel) {
/*  64 */     this.parallel = parallel;
/*     */   }
/*     */   
/*     */   public Boolean getMonitoringUsage() {
/*  68 */     return this.monitoringUsage;
/*     */   }
/*     */   
/*     */   public void setMonitoringUsage(Boolean monitoringUsage) {
/*  72 */     this.monitoringUsage = monitoringUsage;
/*     */   }
/*     */   
/*     */   public Rebuild getRebuild() {
/*  76 */     return this.rebuild;
/*     */   }
/*     */   
/*     */   public void setRebuild(Rebuild rebuild) {
/*  80 */     this.rebuild = rebuild;
/*     */   }
/*     */   
/*     */   public SQLName getName() {
/*  84 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(SQLName name) {
/*  88 */     this.name = name;
/*     */   }
/*     */   
/*     */   public boolean isCompile() {
/*  92 */     return this.compile;
/*     */   }
/*     */   
/*     */   public void setCompile(boolean compile) {
/*  96 */     this.compile = compile;
/*     */   }
/*     */   
/*     */   public Boolean getEnable() {
/* 100 */     return this.enable;
/*     */   }
/*     */   
/*     */   public void setEnable(Boolean enable) {
/* 104 */     this.enable = enable;
/*     */   }
/*     */   
/*     */   public static class Rebuild
/*     */     extends OracleSQLObjectImpl {
/*     */     private SQLObject option;
/*     */     
/*     */     public SQLObject getOption() {
/* 112 */       return this.option;
/*     */     }
/*     */     
/*     */     public void setOption(SQLObject option) {
/* 116 */       this.option = option;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept0(OracleASTVisitor visitor) {
/* 121 */       if (visitor.visit(this)) {
/* 122 */         acceptChild((SQLASTVisitor)visitor, this.option);
/*     */       }
/* 124 */       visitor.endVisit(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleAlterIndexStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */