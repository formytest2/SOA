/*     */ package com.tranboot.client.druid.sql.dialect.oracle.visitor;
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
/*     */ public class OracleEvalVisitor
/*     */   extends OracleASTVisitorAdapter
/*     */   implements SQLEvalVisitor
/*     */ {
/*  41 */   private Map<String, Function> functions = new HashMap<>();
/*  42 */   private List<Object> parameters = new ArrayList();
/*     */   
/*  44 */   private int variantIndex = -1;
/*     */   
/*     */   private boolean markVariantIndex = true;
/*     */   
/*     */   public OracleEvalVisitor() {
/*  49 */     this(new ArrayList(1));
/*     */   }
/*     */   
/*     */   public OracleEvalVisitor(List<Object> parameters) {
/*  53 */     this.parameters = parameters;
/*     */   }
/*     */   
/*     */   public List<Object> getParameters() {
/*  57 */     return this.parameters;
/*     */   }
/*     */   
/*     */   public void setParameters(List<Object> parameters) {
/*  61 */     this.parameters = parameters;
/*     */   }
/*     */   
/*     */   public boolean visit(SQLCharExpr x) {
/*  65 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */   
/*     */   public int incrementAndGetVariantIndex() {
/*  69 */     return ++this.variantIndex;
/*     */   }
/*     */   
/*     */   public int getVariantIndex() {
/*  73 */     return this.variantIndex;
/*     */   }
/*     */   
/*     */   public boolean visit(SQLVariantRefExpr x) {
/*  77 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */   
/*     */   public boolean visit(SQLBinaryOpExpr x) {
/*  81 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */   
/*     */   public boolean visit(SQLUnaryExpr x) {
/*  85 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */   
/*     */   public boolean visit(SQLIntegerExpr x) {
/*  89 */     return SQLEvalVisitorUtils.visit(this, (SQLNumericLiteralExpr)x);
/*     */   }
/*     */   
/*     */   public boolean visit(SQLNumberExpr x) {
/*  93 */     return SQLEvalVisitorUtils.visit(this, (SQLNumericLiteralExpr)x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean visit(SQLCaseExpr x) {
/*  98 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean visit(SQLInListExpr x) {
/* 103 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean visit(SQLNullExpr x) {
/* 108 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean visit(SQLMethodInvokeExpr x) {
/* 113 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean visit(SQLQueryExpr x) {
/* 118 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */   
/*     */   public boolean isMarkVariantIndex() {
/* 122 */     return this.markVariantIndex;
/*     */   }
/*     */   
/*     */   public void setMarkVariantIndex(boolean markVariantIndex) {
/* 126 */     this.markVariantIndex = markVariantIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public Function getFunction(String funcName) {
/* 131 */     return this.functions.get(funcName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerFunction(String funcName, Function function) {
/* 136 */     this.functions.put(funcName, function);
/*     */   }
/*     */ 
/*     */   
/*     */   public void unregisterFunction(String funcName) {
/* 141 */     this.functions.remove(funcName);
/*     */   }
/*     */   
/*     */   public boolean visit(SQLIdentifierExpr x) {
/* 145 */     return SQLEvalVisitorUtils.visit(this, x);
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\visitor\OracleEvalVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */