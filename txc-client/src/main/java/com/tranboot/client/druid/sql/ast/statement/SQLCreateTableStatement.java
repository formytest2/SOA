/*     */ package com.tranboot.client.druid.sql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatementImpl;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

import java.util.ArrayList;
import java.util.HashMap;
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
/*     */ public class SQLCreateTableStatement
/*     */   extends SQLStatementImpl
/*     */   implements SQLDDLStatement
/*     */ {
/*     */   protected boolean ifNotExiists = false;
/*     */   protected Type type;
/*     */   protected SQLExprTableSource tableSource;
/*  32 */   protected List<SQLTableElement> tableElementList = new ArrayList<>();
/*     */ 
/*     */   
/*     */   private SQLExprTableSource inherits;
/*     */   
/*     */   protected SQLSelect select;
/*     */ 
/*     */   
/*     */   public SQLCreateTableStatement() {}
/*     */ 
/*     */   
/*     */   public SQLCreateTableStatement(String dbType) {
/*  44 */     super(dbType);
/*     */   }
/*     */   
/*     */   public SQLName getName() {
/*  48 */     if (this.tableSource == null) {
/*  49 */       return null;
/*     */     }
/*     */     
/*  52 */     return (SQLName)this.tableSource.getExpr();
/*     */   }
/*     */   
/*     */   public void setName(SQLName name) {
/*  56 */     setTableSource(new SQLExprTableSource((SQLExpr)name));
/*     */   }
/*     */   
/*     */   public SQLExprTableSource getTableSource() {
/*  60 */     return this.tableSource;
/*     */   }
/*     */   
/*     */   public void setTableSource(SQLExprTableSource tableSource) {
/*  64 */     if (tableSource != null) {
/*  65 */       tableSource.setParent((SQLObject)this);
/*     */     }
/*  67 */     this.tableSource = tableSource;
/*     */   }
/*     */   
/*     */   public Type getType() {
/*  71 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(Type type) {
/*  75 */     this.type = type;
/*     */   }
/*     */   
/*     */   public enum Type {
/*  79 */     GLOBAL_TEMPORARY, LOCAL_TEMPORARY;
/*     */   }
/*     */   
/*     */   public List<SQLTableElement> getTableElementList() {
/*  83 */     return this.tableElementList;
/*     */   }
/*     */   
/*     */   public boolean isIfNotExiists() {
/*  87 */     return this.ifNotExiists;
/*     */   }
/*     */   
/*     */   public void setIfNotExiists(boolean ifNotExiists) {
/*  91 */     this.ifNotExiists = ifNotExiists;
/*     */   }
/*     */   
/*     */   public SQLExprTableSource getInherits() {
/*  95 */     return this.inherits;
/*     */   }
/*     */   
/*     */   public void setInherits(SQLExprTableSource inherits) {
/*  99 */     if (inherits != null) {
/* 100 */       inherits.setParent((SQLObject)this);
/*     */     }
/* 102 */     this.inherits = inherits;
/*     */   }
/*     */   
/*     */   public SQLSelect getSelect() {
/* 106 */     return this.select;
/*     */   }
/*     */   
/*     */   public void setSelect(SQLSelect select) {
/* 110 */     this.select = select;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/* 115 */     if (visitor.visit(this)) {
/* 116 */       acceptChild(visitor, this.tableSource);
/* 117 */       acceptChild(visitor, this.tableElementList);
/* 118 */       acceptChild(visitor, this.inherits);
/* 119 */       acceptChild(visitor, (SQLObject)this.select);
/*     */     } 
/* 121 */     visitor.endVisit(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addBodyBeforeComment(List<String> comments) {
/* 126 */     if (this.attributes == null) {
/* 127 */       this.attributes = new HashMap<>(1);
/*     */     }
/*     */     
/* 130 */     List<String> attrComments = (List<String>)this.attributes.get("format.body_before_comment");
/* 131 */     if (attrComments == null) {
/* 132 */       this.attributes.put("format.body_before_comment", comments);
/*     */     } else {
/* 134 */       attrComments.addAll(comments);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getBodyBeforeCommentsDirect() {
/* 140 */     if (this.attributes == null) {
/* 141 */       return null;
/*     */     }
/*     */     
/* 144 */     return (List<String>)this.attributes.get("format.body_before_comment");
/*     */   }
/*     */   
/*     */   public boolean hasBodyBeforeComment() {
/* 148 */     List<String> comments = getBodyBeforeCommentsDirect();
/* 149 */     if (comments == null) {
/* 150 */       return false;
/*     */     }
/*     */     
/* 153 */     return !comments.isEmpty();
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLCreateTableStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */