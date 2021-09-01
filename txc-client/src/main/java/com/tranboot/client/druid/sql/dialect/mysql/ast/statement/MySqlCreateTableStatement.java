/*     */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.*;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateTableStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLExprTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLSelect;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
/*     */ public class MySqlCreateTableStatement
/*     */   extends SQLCreateTableStatement
/*     */   implements MySqlStatement
/*     */ {
/*  38 */   private Map<String, SQLObject> tableOptions = new LinkedHashMap<>();
/*     */   
/*     */   private SQLPartitionBy partitioning;
/*     */   
/*  42 */   private List<SQLCommentHint> hints = new ArrayList<>();
/*     */   
/*  44 */   private List<SQLCommentHint> optionHints = new ArrayList<>();
/*     */   
/*     */   private SQLExprTableSource like;
/*     */   
/*     */   private SQLName tableGroup;
/*     */   
/*     */   public MySqlCreateTableStatement() {
/*  51 */     super("mysql");
/*     */   }
/*     */   
/*     */   public SQLExprTableSource getLike() {
/*  55 */     return this.like;
/*     */   }
/*     */   
/*     */   public void setLike(SQLName like) {
/*  59 */     setLike(new SQLExprTableSource((SQLExpr)like));
/*     */   }
/*     */   
/*     */   public void setLike(SQLExprTableSource like) {
/*  63 */     if (like != null) {
/*  64 */       like.setParent((SQLObject)this);
/*     */     }
/*  66 */     this.like = like;
/*     */   }
/*     */   
/*     */   public List<SQLCommentHint> getHints() {
/*  70 */     return this.hints;
/*     */   }
/*     */   
/*     */   public void setHints(List<SQLCommentHint> hints) {
/*  74 */     this.hints = hints;
/*     */   }
/*     */   
/*     */   public void setTableOptions(Map<String, SQLObject> tableOptions) {
/*  78 */     this.tableOptions = tableOptions;
/*     */   }
/*     */   
/*     */   public SQLPartitionBy getPartitioning() {
/*  82 */     return this.partitioning;
/*     */   }
/*     */   
/*     */   public void setPartitioning(SQLPartitionBy partitioning) {
/*  86 */     this.partitioning = partitioning;
/*     */   }
/*     */   
/*     */   public Map<String, SQLObject> getTableOptions() {
/*  90 */     return this.tableOptions;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public SQLSelect getQuery() {
/*  95 */     return this.select;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void setQuery(SQLSelect query) {
/* 100 */     this.select = query;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/* 105 */     if (visitor instanceof MySqlASTVisitor) {
/* 106 */       accept0((MySqlASTVisitor)visitor);
/*     */     } else {
/* 108 */       throw new IllegalArgumentException("not support visitor type : " + visitor.getClass().getName());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void accept0(MySqlASTVisitor visitor) {
/* 113 */     if (visitor.visit(this)) {
/* 114 */       acceptChild((SQLASTVisitor)visitor, getHints());
/* 115 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getTableSource());
/* 116 */       acceptChild((SQLASTVisitor)visitor, getTableElementList());
/* 117 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getLike());
/* 118 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getSelect());
/*     */     } 
/* 120 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public static class TableSpaceOption
/*     */     extends MySqlObjectImpl {
/*     */     private SQLName name;
/*     */     private SQLExpr storage;
/*     */     
/*     */     public SQLName getName() {
/* 129 */       return this.name;
/*     */     }
/*     */     
/*     */     public void setName(SQLName name) {
/* 133 */       this.name = name;
/*     */     }
/*     */     
/*     */     public SQLExpr getStorage() {
/* 137 */       return this.storage;
/*     */     }
/*     */     
/*     */     public void setStorage(SQLExpr storage) {
/* 141 */       this.storage = storage;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept0(MySqlASTVisitor visitor) {
/* 146 */       if (visitor.visit(this)) {
/* 147 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)getName());
/* 148 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)getStorage());
/*     */       } 
/* 150 */       visitor.endVisit(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public List<SQLCommentHint> getOptionHints() {
/* 156 */     return this.optionHints;
/*     */   }
/*     */   
/*     */   public void setOptionHints(List<SQLCommentHint> optionHints) {
/* 160 */     this.optionHints = optionHints;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLName getTableGroup() {
/* 165 */     return this.tableGroup;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTableGroup(SQLName tableGroup) {
/* 170 */     this.tableGroup = tableGroup;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlCreateTableStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */