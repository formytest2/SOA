/*     */ package com.tranboot.client.druid.sql.dialect.mysql.ast.clause;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLIfStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlStatementImpl;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MySqlCaseStatement
/*     */   extends MySqlStatementImpl
/*     */ {
/*     */   private SQLExpr condition;
/*  36 */   private List<MySqlWhenStatement> whenList = new ArrayList<>();
/*     */   
/*     */   private SQLIfStatement.Else elseItem;
/*     */   
/*     */   public SQLExpr getCondition() {
/*  41 */     return this.condition;
/*     */   }
/*     */   
/*     */   public void setCondition(SQLExpr condition) {
/*  45 */     this.condition = condition;
/*     */   }
/*     */   
/*     */   public List<MySqlWhenStatement> getWhenList() {
/*  49 */     return this.whenList;
/*     */   }
/*     */   
/*     */   public void setWhenList(List<MySqlWhenStatement> whenList) {
/*  53 */     this.whenList = whenList;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addWhenStatement(MySqlWhenStatement stmt) {
/*  58 */     this.whenList.add(stmt);
/*     */   }
/*     */   
/*     */   public SQLIfStatement.Else getElseItem() {
/*  62 */     return this.elseItem;
/*     */   }
/*     */   
/*     */   public void setElseItem(SQLIfStatement.Else elseItem) {
/*  66 */     this.elseItem = elseItem;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void accept0(MySqlASTVisitor visitor) {
/*  72 */     if (visitor.visit(this)) {
/*  73 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.condition);
/*  74 */       acceptChild((SQLASTVisitor)visitor, this.whenList);
/*  75 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.elseItem);
/*     */     } 
/*  77 */     visitor.endVisit(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class MySqlWhenStatement
/*     */     extends MySqlObjectImpl
/*     */   {
/*     */     private SQLExpr condition;
/*     */ 
/*     */     
/*  88 */     private List<SQLStatement> statements = new ArrayList<>();
/*     */ 
/*     */     
/*     */     public void accept0(MySqlASTVisitor visitor) {
/*  92 */       if (visitor.visit(this)) {
/*  93 */         acceptChild((SQLASTVisitor)visitor, (SQLObject)this.condition);
/*  94 */         acceptChild((SQLASTVisitor)visitor, this.statements);
/*     */       } 
/*  96 */       visitor.endVisit(this);
/*     */     }
/*     */     
/*     */     public SQLExpr getCondition() {
/* 100 */       return this.condition;
/*     */     }
/*     */     
/*     */     public void setCondition(SQLExpr condition) {
/* 104 */       this.condition = condition;
/*     */     }
/*     */     
/*     */     public List<SQLStatement> getStatements() {
/* 108 */       return this.statements;
/*     */     }
/*     */     
/*     */     public void setStatements(List<SQLStatement> statements) {
/* 112 */       this.statements = statements;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\clause\MySqlCaseStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */