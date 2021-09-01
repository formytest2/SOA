/*     */ package com.tranboot.client.druid.sql.dialect.mysql.ast.expr;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*     */ public class MySqlMatchAgainstExpr
/*     */   extends SQLExprImpl
/*     */   implements MySqlExpr
/*     */ {
/*  28 */   private List<SQLExpr> columns = new ArrayList<>();
/*     */   
/*     */   private SQLExpr against;
/*     */   
/*     */   private SearchModifier searchModifier;
/*     */   
/*     */   public List<SQLExpr> getColumns() {
/*  35 */     return this.columns;
/*     */   }
/*     */   
/*     */   public void setColumns(List<SQLExpr> columns) {
/*  39 */     this.columns = columns;
/*     */   }
/*     */   
/*     */   public SQLExpr getAgainst() {
/*  43 */     return this.against;
/*     */   }
/*     */   
/*     */   public void setAgainst(SQLExpr against) {
/*  47 */     this.against = against;
/*     */   }
/*     */   
/*     */   public SearchModifier getSearchModifier() {
/*  51 */     return this.searchModifier;
/*     */   }
/*     */   
/*     */   public void setSearchModifier(SearchModifier searchModifier) {
/*  55 */     this.searchModifier = searchModifier;
/*     */   }
/*     */   
/*     */   public enum SearchModifier {
/*  59 */     IN_BOOLEAN_MODE("IN BOOLEAN MODE"),
/*  60 */     IN_NATURAL_LANGUAGE_MODE("IN NATURAL LANGUAGE MODE"),
/*  61 */     IN_NATURAL_LANGUAGE_MODE_WITH_QUERY_EXPANSION("IN NATURAL LANGUAGE MODE WITH QUERY EXPANSION"),
/*  62 */     WITH_QUERY_EXPANSION("WITH QUERY EXPANSION");
/*     */ 
/*     */     
/*     */     public final String name;
/*     */ 
/*     */     
/*     */     public final String name_lcase;
/*     */ 
/*     */     
/*     */     SearchModifier(String name) {
/*  72 */       this.name = name;
/*  73 */       this.name_lcase = name.toLowerCase();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  79 */     MySqlASTVisitor mysqlVisitor = (MySqlASTVisitor)visitor;
/*  80 */     if (mysqlVisitor.visit(this)) {
/*  81 */       acceptChild(visitor, this.columns);
/*  82 */       acceptChild(visitor, (SQLObject)this.against);
/*     */     } 
/*  84 */     mysqlVisitor.endVisit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  89 */     int prime = 31;
/*  90 */     int result = 1;
/*  91 */     result = 31 * result + ((this.against == null) ? 0 : this.against.hashCode());
/*  92 */     result = 31 * result + ((this.columns == null) ? 0 : this.columns.hashCode());
/*  93 */     result = 31 * result + ((this.searchModifier == null) ? 0 : this.searchModifier.hashCode());
/*  94 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  99 */     if (this == obj) {
/* 100 */       return true;
/*     */     }
/* 102 */     if (obj == null) {
/* 103 */       return false;
/*     */     }
/* 105 */     if (getClass() != obj.getClass()) {
/* 106 */       return false;
/*     */     }
/* 108 */     MySqlMatchAgainstExpr other = (MySqlMatchAgainstExpr)obj;
/* 109 */     if (this.against == null) {
/* 110 */       if (other.against != null) {
/* 111 */         return false;
/*     */       }
/* 113 */     } else if (!this.against.equals(other.against)) {
/* 114 */       return false;
/*     */     } 
/* 116 */     if (this.columns == null) {
/* 117 */       if (other.columns != null) {
/* 118 */         return false;
/*     */       }
/* 120 */     } else if (!this.columns.equals(other.columns)) {
/* 121 */       return false;
/*     */     } 
/* 123 */     if (this.searchModifier != other.searchModifier) {
/* 124 */       return false;
/*     */     }
/* 126 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\expr\MySqlMatchAgainstExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */