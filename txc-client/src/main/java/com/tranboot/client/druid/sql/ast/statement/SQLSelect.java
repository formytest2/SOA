/*     */ package com.tranboot.client.druid.sql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.SQLUtils;
import com.tranboot.client.druid.sql.ast.*;
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
/*     */ public class SQLSelect
/*     */   extends SQLObjectImpl
/*     */ {
/*     */   protected SQLWithSubqueryClause withSubQuery;
/*     */   protected SQLSelectQuery query;
/*     */   protected SQLOrderBy orderBy;
/*     */   protected List<SQLHint> hints;
/*     */   
/*     */   public SQLSelect() {}
/*     */   
/*     */   public List<SQLHint> getHints() {
/*  42 */     if (this.hints == null) {
/*  43 */       this.hints = new ArrayList<>(2);
/*     */     }
/*  45 */     return this.hints;
/*     */   }
/*     */   
/*     */   public int getHintsSize() {
/*  49 */     if (this.hints == null) {
/*  50 */       return 0;
/*     */     }
/*  52 */     return this.hints.size();
/*     */   }
/*     */   
/*     */   public SQLSelect(SQLSelectQuery query) {
/*  56 */     setQuery(query);
/*     */   }
/*     */   
/*     */   public SQLWithSubqueryClause getWithSubQuery() {
/*  60 */     return this.withSubQuery;
/*     */   }
/*     */   
/*     */   public void setWithSubQuery(SQLWithSubqueryClause withSubQuery) {
/*  64 */     this.withSubQuery = withSubQuery;
/*     */   }
/*     */   
/*     */   public SQLSelectQuery getQuery() {
/*  68 */     return this.query;
/*     */   }
/*     */   
/*     */   public void setQuery(SQLSelectQuery query) {
/*  72 */     if (query != null) {
/*  73 */       query.setParent((SQLObject)this);
/*     */     }
/*  75 */     this.query = query;
/*     */   }
/*     */   
/*     */   public SQLOrderBy getOrderBy() {
/*  79 */     return this.orderBy;
/*     */   }
/*     */   
/*     */   public void setOrderBy(SQLOrderBy orderBy) {
/*  83 */     this.orderBy = orderBy;
/*     */   }
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  87 */     if (visitor.visit(this)) {
/*  88 */       acceptChild(visitor, this.query);
/*  89 */       acceptChild(visitor, (SQLObject)this.orderBy);
/*  90 */       acceptChild(visitor, this.hints);
/*     */     } 
/*     */     
/*  93 */     visitor.endVisit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  98 */     int prime = 31;
/*  99 */     int result = 1;
/* 100 */     result = 31 * result + ((this.orderBy == null) ? 0 : this.orderBy.hashCode());
/* 101 */     result = 31 * result + ((this.query == null) ? 0 : this.query.hashCode());
/* 102 */     result = 31 * result + ((this.withSubQuery == null) ? 0 : this.withSubQuery.hashCode());
/* 103 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 108 */     if (this == obj) return true; 
/* 109 */     if (obj == null) return false; 
/* 110 */     if (getClass() != obj.getClass()) return false; 
/* 111 */     SQLSelect other = (SQLSelect)obj;
/* 112 */     if (this.orderBy == null)
/* 113 */     { if (other.orderBy != null) return false;  }
/* 114 */     else if (!this.orderBy.equals(other.orderBy)) { return false; }
/* 115 */      if (this.query == null)
/* 116 */     { if (other.query != null) return false;  }
/* 117 */     else if (!this.query.equals(other.query)) { return false; }
/* 118 */      if (this.withSubQuery == null)
/* 119 */     { if (other.withSubQuery != null) return false;  }
/* 120 */     else if (!this.withSubQuery.equals(other.withSubQuery)) { return false; }
/* 121 */      return true;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 125 */     SQLObject parent = getParent();
/* 126 */     if (parent instanceof SQLStatement) {
/* 127 */       String dbType = ((SQLStatement)parent).getDbType();
/*     */       
/* 129 */       if (dbType != null) {
/* 130 */         return SQLUtils.toSQLString((SQLObject)this, dbType);
/*     */       }
/*     */     } 
/*     */     
/* 134 */     return super.toString();
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLSelect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */