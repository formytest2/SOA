/*     */ package com.tranboot.client.druid.sql.dialect.mysql.visitor;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.expr.*;
import com.tranboot.client.druid.sql.visitor.SQLEvalVisitor;
import com.tranboot.client.druid.sql.visitor.SQLEvalVisitorUtils;
import com.tranboot.client.druid.sql.visitor.functions.Function;

import java.util.ArrayList;
import java.util.HashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MySqlEvalVisitorImpl
/*     */   extends MySqlASTVisitorAdapter
/*     */   implements SQLEvalVisitor
/*     */ {
/*  45 */   private Map<String, Function> functions = new HashMap<>();
/*  46 */   private List<Object> parameters = new ArrayList();
/*     */   
/*  48 */   private int variantIndex = -1;
/*     */   
/*     */   private boolean markVariantIndex = true;
/*     */   
/*     */   public MySqlEvalVisitorImpl() {
/*  53 */     this(new ArrayList(1));
/*     */   }
/*     */   
/*     */   public MySqlEvalVisitorImpl(List<Object> parameters) {
/*  57 */     this.parameters = parameters;
/*     */   }
/*     */   
/*     */   public List<Object> getParameters() {
/*  61 */     return this.parameters;
/*     */   }
/*     */   
/*     */   public void setParameters(List<Object> parameters) {
/*  65 */     this.parameters = parameters;
/*     */   }
/*     */   
/*     */   public boolean visit(SQLCharExpr x) {
/*  69 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */   
/*     */   public int incrementAndGetVariantIndex() {
/*  73 */     return ++this.variantIndex;
/*     */   }
/*     */   
/*     */   public int getVariantIndex() {
/*  77 */     return this.variantIndex;
/*     */   }
/*     */   
/*     */   public boolean visit(SQLVariantRefExpr x) {
/*  81 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */   
/*     */   public boolean visit(SQLBinaryOpExpr x) {
/*  85 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */   
/*     */   public boolean visit(SQLUnaryExpr x) {
/*  89 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */   
/*     */   public boolean visit(SQLIntegerExpr x) {
/*  93 */     return SQLEvalVisitorUtils.visit(this, (SQLNumericLiteralExpr)x);
/*     */   }
/*     */   
/*     */   public boolean visit(SQLNumberExpr x) {
/*  97 */     return SQLEvalVisitorUtils.visit(this, (SQLNumericLiteralExpr)x);
/*     */   }
/*     */   
/*     */   public boolean visit(SQLHexExpr x) {
/* 101 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean visit(SQLBinaryExpr x) {
/* 106 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean visit(SQLCaseExpr x) {
/* 111 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean visit(SQLBetweenExpr x) {
/* 116 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean visit(SQLInListExpr x) {
/* 121 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean visit(SQLNullExpr x) {
/* 126 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean visit(SQLMethodInvokeExpr x) {
/* 131 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean visit(SQLQueryExpr x) {
/* 136 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */   
/*     */   public boolean isMarkVariantIndex() {
/* 140 */     return this.markVariantIndex;
/*     */   }
/*     */   
/*     */   public void setMarkVariantIndex(boolean markVariantIndex) {
/* 144 */     this.markVariantIndex = markVariantIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public Function getFunction(String funcName) {
/* 149 */     return this.functions.get(funcName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerFunction(String funcName, Function function) {
/* 154 */     this.functions.put(funcName, function);
/*     */   }
/*     */   
/*     */   public boolean visit(SQLIdentifierExpr x) {
/* 158 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */ 
/*     */   
/*     */   public void unregisterFunction(String funcName) {
/* 163 */     this.functions.remove(funcName);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean visit(SQLBooleanExpr x) {
/* 168 */     x.getAttributes().put("eval.value", Boolean.valueOf(x.getValue()));
/* 169 */     return false;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\visitor\MySqlEvalVisitorImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */