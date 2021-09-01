/*     */ package com.tranboot.client.druid.sql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*     */ public class SQLExprTableSource
/*     */   extends SQLTableSourceImpl
/*     */ {
/*     */   protected SQLExpr expr;
/*     */   private List<SQLName> partitions;
/*     */   
/*     */   public SQLExprTableSource() {}
/*     */   
/*     */   public SQLExprTableSource(SQLExpr expr) {
/*  36 */     this(expr, (String)null);
/*     */   }
/*     */   
/*     */   public SQLExprTableSource(SQLExpr expr, String alias) {
/*  40 */     setExpr(expr);
/*  41 */     setAlias(alias);
/*     */   }
/*     */   
/*     */   public SQLExpr getExpr() {
/*  45 */     return this.expr;
/*     */   }
/*     */   
/*     */   public void setExpr(SQLExpr expr) {
/*  49 */     if (expr != null) {
/*  50 */       expr.setParent(this);
/*     */     }
/*  52 */     this.expr = expr;
/*     */   }
/*     */   
/*     */   public List<SQLName> getPartitions() {
/*  56 */     if (this.partitions == null) {
/*  57 */       this.partitions = new ArrayList<>(2);
/*     */     }
/*     */     
/*  60 */     return this.partitions;
/*     */   }
/*     */   
/*     */   public int getPartitionSize() {
/*  64 */     if (this.partitions == null) {
/*  65 */       return 0;
/*     */     }
/*  67 */     return this.partitions.size();
/*     */   }
/*     */   
/*     */   public void addPartition(SQLName partition) {
/*  71 */     if (partition != null) {
/*  72 */       partition.setParent(this);
/*     */     }
/*     */     
/*  75 */     if (this.partitions == null) {
/*  76 */       this.partitions = new ArrayList<>(2);
/*     */     }
/*  78 */     this.partitions.add(partition);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  83 */     if (visitor.visit(this)) {
/*  84 */       acceptChild(visitor, (SQLObject)this.expr);
/*     */     }
/*  86 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public void output(StringBuffer buf) {
/*  90 */     this.expr.output(buf);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  95 */     int prime = 31;
/*  96 */     int result = 1;
/*  97 */     result = 31 * result + ((this.expr == null) ? 0 : this.expr.hashCode());
/*  98 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 103 */     if (this == obj) return true; 
/* 104 */     if (obj == null) return false; 
/* 105 */     if (getClass() != obj.getClass()) return false; 
/* 106 */     SQLExprTableSource other = (SQLExprTableSource)obj;
/* 107 */     if (this.expr == null)
/* 108 */     { if (other.expr != null) return false;  }
/* 109 */     else if (!this.expr.equals(other.expr)) { return false; }
/* 110 */      return true;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLExprTableSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */