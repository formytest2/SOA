/*     */ package com.tranboot.client.druid.sql.ast.statement;
/*     */ 
/*     */

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
/*     */ public class SQLIfStatement
/*     */   extends SQLStatementImpl
/*     */ {
/*     */   private SQLExpr condition;
/*  30 */   private List<SQLStatement> statements = new ArrayList<>();
/*  31 */   private List<ElseIf> elseIfList = new ArrayList<>();
/*     */   
/*     */   private Else elseItem;
/*     */   
/*     */   public void accept0(SQLASTVisitor visitor) {
/*  36 */     if (visitor.visit(this)) {
/*  37 */       acceptChild(visitor, (SQLObject)this.condition);
/*  38 */       acceptChild(visitor, this.statements);
/*  39 */       acceptChild(visitor, this.elseIfList);
/*  40 */       acceptChild(visitor, (SQLObject)this.elseItem);
/*     */     } 
/*  42 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public SQLExpr getCondition() {
/*  46 */     return this.condition;
/*     */   }
/*     */   
/*     */   public void setCondition(SQLExpr condition) {
/*  50 */     this.condition = condition;
/*     */   }
/*     */   
/*     */   public List<SQLStatement> getStatements() {
/*  54 */     return this.statements;
/*     */   }
/*     */   
/*     */   public void setStatements(List<SQLStatement> statements) {
/*  58 */     this.statements = statements;
/*     */   }
/*     */   
/*     */   public List<ElseIf> getElseIfList() {
/*  62 */     return this.elseIfList;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setElseIfList(List<ElseIf> elseIfList) {
/*  67 */     this.elseIfList = elseIfList;
/*     */   }
/*     */   
/*     */   public Else getElseItem() {
/*  71 */     return this.elseItem;
/*     */   }
/*     */   
/*     */   public void setElseItem(Else elseItem) {
/*  75 */     this.elseItem = elseItem;
/*     */   }
/*     */   
/*     */   public static class ElseIf
/*     */     extends SQLObjectImpl {
/*     */     private SQLExpr condition;
/*  81 */     private List<SQLStatement> statements = new ArrayList<>();
/*     */ 
/*     */     
/*     */     public void accept0(SQLASTVisitor visitor) {
/*  85 */       if (visitor.visit(this)) {
/*  86 */         acceptChild(visitor, (SQLObject)this.condition);
/*  87 */         acceptChild(visitor, this.statements);
/*     */       } 
/*  89 */       visitor.endVisit(this);
/*     */     }
/*     */     
/*     */     public List<SQLStatement> getStatements() {
/*  93 */       return this.statements;
/*     */     }
/*     */     
/*     */     public void setStatements(List<SQLStatement> statements) {
/*  97 */       this.statements = statements;
/*     */     }
/*     */     
/*     */     public SQLExpr getCondition() {
/* 101 */       return this.condition;
/*     */     }
/*     */     
/*     */     public void setCondition(SQLExpr condition) {
/* 105 */       if (condition != null) {
/* 106 */         condition.setParent((SQLObject)this);
/*     */       }
/* 108 */       this.condition = condition;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Else
/*     */     extends SQLObjectImpl {
/* 114 */     private List<SQLStatement> statements = new ArrayList<>();
/*     */ 
/*     */     
/*     */     public void accept0(SQLASTVisitor visitor) {
/* 118 */       if (visitor.visit(this)) {
/* 119 */         acceptChild(visitor, this.statements);
/*     */       }
/* 121 */       visitor.endVisit(this);
/*     */     }
/*     */     
/*     */     public List<SQLStatement> getStatements() {
/* 125 */       return this.statements;
/*     */     }
/*     */     
/*     */     public void setStatements(List<SQLStatement> statements) {
/* 129 */       this.statements = statements;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLIfStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */