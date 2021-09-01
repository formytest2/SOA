/*     */ package com.tranboot.client.druid.sql.dialect.oracle.visitor;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOperator;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLIntegerExpr;
import com.tranboot.client.druid.sql.ast.statement.*;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;

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
/*     */ public class OracleToMySqlOutputVisitor
/*     */   extends OracleOutputVisitor
/*     */ {
/*     */   public OracleToMySqlOutputVisitor(Appendable appender, boolean printPostSemi) {
/*  34 */     super(appender, printPostSemi);
/*     */   }
/*     */   
/*     */   public OracleToMySqlOutputVisitor(Appendable appender) {
/*  38 */     super(appender);
/*     */   }
/*     */   
/*     */   public boolean visit(OracleSelectQueryBlock x) {
/*  42 */     boolean parentIsSelectStatment = false;
/*     */     
/*  44 */     if (x.getParent() instanceof SQLSelect) {
/*  45 */       SQLSelect select = (SQLSelect)x.getParent();
/*  46 */       if (select.getParent() instanceof com.tranboot.client.druid.sql.ast.statement.SQLSelectStatement || select.getParent() instanceof SQLSubqueryTableSource) {
/*  47 */         parentIsSelectStatment = true;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  52 */     if (!parentIsSelectStatment) {
/*  53 */       return super.visit(x);
/*     */     }
/*     */     
/*  56 */     if (x.getWhere() instanceof SQLBinaryOpExpr && x
/*  57 */       .getFrom() instanceof SQLSubqueryTableSource) {
/*     */       int rownum;
/*     */       
/*     */       String ident;
/*  61 */       SQLBinaryOpExpr where = (SQLBinaryOpExpr)x.getWhere();
/*  62 */       if (where.getRight() instanceof SQLIntegerExpr && where.getLeft() instanceof SQLIdentifierExpr) {
/*  63 */         rownum = ((SQLIntegerExpr)where.getRight()).getNumber().intValue();
/*  64 */         ident = ((SQLIdentifierExpr)where.getLeft()).getName();
/*     */       } else {
/*  66 */         return super.visit(x);
/*     */       } 
/*     */       
/*  69 */       SQLSelect select = ((SQLSubqueryTableSource)x.getFrom()).getSelect();
/*  70 */       SQLSelectQueryBlock queryBlock = null;
/*  71 */       SQLSelect subSelect = null;
/*  72 */       SQLBinaryOpExpr subWhere = null;
/*  73 */       boolean isSubQueryRowNumMapping = false;
/*     */       
/*  75 */       if (select.getQuery() instanceof SQLSelectQueryBlock) {
/*  76 */         queryBlock = (SQLSelectQueryBlock)select.getQuery();
/*  77 */         if (queryBlock.getWhere() instanceof SQLBinaryOpExpr) {
/*  78 */           subWhere = (SQLBinaryOpExpr)queryBlock.getWhere();
/*     */         }
/*     */         
/*  81 */         for (SQLSelectItem selectItem : queryBlock.getSelectList()) {
/*  82 */           if (isRowNumber(selectItem.getExpr()) && 
/*  83 */             where.getLeft() instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)where
/*  84 */             .getLeft()).getName().equals(selectItem.getAlias())) {
/*  85 */             isSubQueryRowNumMapping = true;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/*  90 */         SQLTableSource subTableSource = queryBlock.getFrom();
/*  91 */         if (subTableSource instanceof SQLSubqueryTableSource) {
/*  92 */           subSelect = ((SQLSubqueryTableSource)subTableSource).getSelect();
/*     */         }
/*     */       } 
/*     */       
/*  96 */       if ("ROWNUM".equalsIgnoreCase(ident)) {
/*  97 */         SQLBinaryOperator op = where.getOperator();
/*  98 */         Integer limit = null;
/*  99 */         if (op == SQLBinaryOperator.LessThanOrEqual) {
/* 100 */           limit = Integer.valueOf(rownum);
/* 101 */         } else if (op == SQLBinaryOperator.LessThan) {
/* 102 */           limit = Integer.valueOf(rownum - 1);
/*     */         } 
/*     */         
/* 105 */         if (limit != null) {
/* 106 */           select.accept(this);
/* 107 */           println();
/* 108 */           print0(this.ucase ? "LIMIT " : "limit ");
/* 109 */           print(limit.intValue());
/* 110 */           return false;
/*     */         } 
/* 112 */       } else if (isSubQueryRowNumMapping) {
/* 113 */         SQLBinaryOperator op = where.getOperator();
/* 114 */         SQLBinaryOperator subOp = subWhere.getOperator();
/*     */         
/* 116 */         if (isRowNumber(subWhere.getLeft()) && subWhere
/* 117 */           .getRight() instanceof SQLIntegerExpr) {
/*     */           
/* 119 */           int subRownum = ((SQLIntegerExpr)subWhere.getRight()).getNumber().intValue();
/*     */           
/* 121 */           Integer offset = null;
/* 122 */           if (op == SQLBinaryOperator.GreaterThanOrEqual) {
/* 123 */             offset = Integer.valueOf(rownum + 1);
/* 124 */           } else if (op == SQLBinaryOperator.GreaterThan) {
/* 125 */             offset = Integer.valueOf(rownum);
/*     */           } 
/*     */           
/* 128 */           if (offset != null) {
/* 129 */             Integer limit = null;
/* 130 */             if (subOp == SQLBinaryOperator.LessThanOrEqual) {
/* 131 */               limit = Integer.valueOf(subRownum - offset.intValue());
/* 132 */             } else if (subOp == SQLBinaryOperator.LessThan) {
/* 133 */               limit = Integer.valueOf(subRownum - 1 - offset.intValue());
/*     */             } 
/*     */             
/* 136 */             if (limit != null) {
/* 137 */               subSelect.accept(this);
/* 138 */               println();
/* 139 */               print0(this.ucase ? "LIMIT " : "limit ");
/* 140 */               print(offset.intValue());
/* 141 */               print0(", ");
/* 142 */               print(limit.intValue());
/* 143 */               return false;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 149 */     return super.visit(x);
/*     */   }
/*     */   
/*     */   static boolean isRowNumber(SQLExpr expr) {
/* 153 */     if (expr instanceof SQLIdentifierExpr) {
/* 154 */       String lownerName = ((SQLIdentifierExpr)expr).getLowerName();
/* 155 */       return "rownum".equals(lownerName);
/*     */     } 
/*     */     
/* 158 */     return false;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\visitor\OracleToMySqlOutputVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */