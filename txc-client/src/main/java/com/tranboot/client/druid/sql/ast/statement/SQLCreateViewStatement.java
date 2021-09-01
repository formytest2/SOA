/*     */ package com.tranboot.client.druid.sql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.*;
import com.tranboot.client.druid.sql.ast.expr.SQLCharExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLLiteralExpr;
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
/*     */ public class SQLCreateViewStatement
/*     */   extends SQLStatementImpl
/*     */   implements SQLDDLStatement
/*     */ {
/*     */   private boolean orReplace = false;
/*     */   protected SQLName name;
/*     */   protected SQLSelect subQuery;
/*     */   protected boolean ifNotExists = false;
/*     */   protected String algorithm;
/*     */   protected SQLName definer;
/*     */   protected String sqlSecurity;
/*  40 */   protected final List<Column> columns = new ArrayList<>();
/*     */   
/*     */   private Level with;
/*     */   
/*     */   private SQLLiteralExpr comment;
/*     */ 
/*     */   
/*     */   public SQLCreateViewStatement() {}
/*     */ 
/*     */   
/*     */   public SQLCreateViewStatement(String dbType) {
/*  51 */     super(dbType);
/*     */   }
/*     */   
/*     */   public boolean isOrReplace() {
/*  55 */     return this.orReplace;
/*     */   }
/*     */   
/*     */   public void setOrReplace(boolean orReplace) {
/*  59 */     this.orReplace = orReplace;
/*     */   }
/*     */   
/*     */   public SQLName getName() {
/*  63 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(SQLName name) {
/*  67 */     this.name = name;
/*     */   }
/*     */   
/*     */   public Level getWith() {
/*  71 */     return this.with;
/*     */   }
/*     */   
/*     */   public void setWith(Level with) {
/*  75 */     this.with = with;
/*     */   }
/*     */   
/*     */   public SQLSelect getSubQuery() {
/*  79 */     return this.subQuery;
/*     */   }
/*     */   
/*     */   public void setSubQuery(SQLSelect subQuery) {
/*  83 */     this.subQuery = subQuery;
/*     */   }
/*     */   
/*     */   public List<Column> getColumns() {
/*  87 */     return this.columns;
/*     */   }
/*     */   
/*     */   public void addColumn(Column column) {
/*  91 */     if (column != null) {
/*  92 */       column.setParent((SQLObject)this);
/*     */     }
/*  94 */     this.columns.add(column);
/*     */   }
/*     */   
/*     */   public boolean isIfNotExists() {
/*  98 */     return this.ifNotExists;
/*     */   }
/*     */   
/*     */   public void setIfNotExists(boolean ifNotExists) {
/* 102 */     this.ifNotExists = ifNotExists;
/*     */   }
/*     */   
/*     */   public SQLLiteralExpr getComment() {
/* 106 */     return this.comment;
/*     */   }
/*     */   
/*     */   public void setComment(SQLLiteralExpr comment) {
/* 110 */     if (comment != null) {
/* 111 */       comment.setParent((SQLObject)this);
/*     */     }
/* 113 */     this.comment = comment;
/*     */   }
/*     */   
/*     */   public String getAlgorithm() {
/* 117 */     return this.algorithm;
/*     */   }
/*     */   
/*     */   public void setAlgorithm(String algorithm) {
/* 121 */     this.algorithm = algorithm;
/*     */   }
/*     */   
/*     */   public SQLName getDefiner() {
/* 125 */     return this.definer;
/*     */   }
/*     */   
/*     */   public void setDefiner(SQLName definer) {
/* 129 */     if (definer != null) {
/* 130 */       definer.setParent((SQLObject)this);
/*     */     }
/* 132 */     this.definer = definer;
/*     */   }
/*     */   
/*     */   public String getSqlSecurity() {
/* 136 */     return this.sqlSecurity;
/*     */   }
/*     */   
/*     */   public void setSqlSecurity(String sqlSecurity) {
/* 140 */     this.sqlSecurity = sqlSecurity;
/*     */   }
/*     */   
/*     */   public void output(StringBuffer buf) {
/* 144 */     buf.append("CREATE VIEW ");
/* 145 */     this.name.output(buf);
/*     */     
/* 147 */     if (this.columns.size() > 0) {
/* 148 */       buf.append(" (");
/* 149 */       for (int i = 0, size = this.columns.size(); i < size; i++) {
/* 150 */         if (i != 0) {
/* 151 */           buf.append(", ");
/*     */         }
/* 153 */         ((Column)this.columns.get(i)).output(buf);
/*     */       } 
/* 155 */       buf.append(")");
/*     */     } 
/*     */     
/* 158 */     buf.append(" AS ");
/* 159 */     this.subQuery.output(buf);
/*     */     
/* 161 */     if (this.with != null) {
/* 162 */       buf.append(" WITH ");
/* 163 */       buf.append(this.with.name());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/* 169 */     if (visitor.visit(this)) {
/* 170 */       acceptChild(visitor, (SQLObject)this.name);
/* 171 */       acceptChild(visitor, this.columns);
/* 172 */       acceptChild(visitor, (SQLObject)this.comment);
/* 173 */       acceptChild(visitor, (SQLObject)this.subQuery);
/*     */     } 
/* 175 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public enum Level {
/* 179 */     CASCADED, LOCAL;
/*     */   }
/*     */   
/*     */   public static class Column
/*     */     extends SQLObjectImpl {
/*     */     private SQLExpr expr;
/*     */     private SQLCharExpr comment;
/*     */     
/*     */     public SQLExpr getExpr() {
/* 188 */       return this.expr;
/*     */     }
/*     */     
/*     */     public void setExpr(SQLExpr expr) {
/* 192 */       if (expr != null) {
/* 193 */         expr.setParent((SQLObject)this);
/*     */       }
/* 195 */       this.expr = expr;
/*     */     }
/*     */     
/*     */     public SQLCharExpr getComment() {
/* 199 */       return this.comment;
/*     */     }
/*     */     
/*     */     public void setComment(SQLCharExpr comment) {
/* 203 */       if (comment != null) {
/* 204 */         comment.setParent((SQLObject)this);
/*     */       }
/* 206 */       this.comment = comment;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void accept0(SQLASTVisitor visitor) {
/* 211 */       if (visitor.visit(this)) {
/* 212 */         acceptChild(visitor, (SQLObject)this.expr);
/* 213 */         acceptChild(visitor, (SQLObject)this.comment);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLCreateViewStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */