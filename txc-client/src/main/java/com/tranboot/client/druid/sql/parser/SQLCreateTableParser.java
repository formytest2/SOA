/*     */ package com.tranboot.client.druid.sql.parser;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.*;

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
/*     */ public class SQLCreateTableParser
/*     */   extends SQLDDLParser
/*     */ {
/*     */   public SQLCreateTableParser(String sql) {
/*  30 */     super(sql);
/*     */   }
/*     */   
/*     */   public SQLCreateTableParser(SQLExprParser exprParser) {
/*  34 */     super(exprParser);
/*     */   }
/*     */   
/*     */   public SQLCreateTableStatement parseCrateTable() {
/*  38 */     List<String> comments = null;
/*  39 */     if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
/*  40 */       comments = this.lexer.readAndResetComments();
/*     */     }
/*     */     
/*  43 */     SQLCreateTableStatement stmt = parseCrateTable(true);
/*  44 */     if (comments != null) {
/*  45 */       stmt.addBeforeComment(comments);
/*     */     }
/*     */     
/*  48 */     return stmt;
/*     */   }
/*     */   
/*     */   public SQLCreateTableStatement parseCrateTable(boolean acceptCreate) {
/*  52 */     if (acceptCreate) {
/*  53 */       accept(Token.CREATE);
/*     */     }
/*     */     
/*  56 */     SQLCreateTableStatement createTable = newCreateStatement();
/*     */     
/*  58 */     if (identifierEquals("GLOBAL")) {
/*  59 */       this.lexer.nextToken();
/*     */       
/*  61 */       if (identifierEquals("TEMPORARY")) {
/*  62 */         this.lexer.nextToken();
/*  63 */         createTable.setType(SQLCreateTableStatement.Type.GLOBAL_TEMPORARY);
/*     */       } else {
/*  65 */         throw new ParserException("syntax error " + this.lexer.token() + " " + this.lexer.stringVal());
/*     */       } 
/*  67 */     } else if (this.lexer.token() == Token.IDENTIFIER && this.lexer.stringVal().equalsIgnoreCase("LOCAL")) {
/*  68 */       this.lexer.nextToken();
/*  69 */       if (this.lexer.token() == Token.IDENTIFIER && this.lexer.stringVal().equalsIgnoreCase("TEMPORAY")) {
/*  70 */         this.lexer.nextToken();
/*  71 */         createTable.setType(SQLCreateTableStatement.Type.LOCAL_TEMPORARY);
/*     */       } else {
/*  73 */         throw new ParserException("syntax error");
/*     */       } 
/*     */     } 
/*     */     
/*  77 */     accept(Token.TABLE);
/*     */     
/*  79 */     createTable.setName(this.exprParser.name());
/*     */     
/*  81 */     if (this.lexer.token() == Token.LPAREN) {
/*  82 */       this.lexer.nextToken();
/*     */       
/*     */       while (true) {
/*  85 */         if (this.lexer.token() == Token.IDENTIFIER || this.lexer
/*  86 */           .token() == Token.LITERAL_ALIAS)
/*  87 */         { SQLColumnDefinition column = this.exprParser.parseColumn();
/*  88 */           createTable.getTableElementList().add(column); }
/*  89 */         else if (this.lexer.token == Token.PRIMARY || this.lexer.token == Token.UNIQUE || this.lexer.token == Token.CHECK || this.lexer.token == Token.CONSTRAINT)
/*     */         
/*     */         { 
/*     */           
/*  93 */           SQLConstraint constraint = this.exprParser.parseConstaint();
/*  94 */           constraint.setParent((SQLObject)createTable);
/*  95 */           createTable.getTableElementList().add((SQLTableElement)constraint); }
/*  96 */         else { if (this.lexer.token() == Token.TABLESPACE) {
/*  97 */             throw new ParserException("TODO " + this.lexer.token());
/*     */           }
/*  99 */           SQLColumnDefinition column = this.exprParser.parseColumn();
/* 100 */           createTable.getTableElementList().add(column); }
/*     */ 
/*     */         
/* 103 */         if (this.lexer.token() == Token.COMMA) {
/* 104 */           this.lexer.nextToken();
/*     */           
/* 106 */           if (this.lexer.token() == Token.RPAREN) {
/*     */             break;
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         break;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 124 */       accept(Token.RPAREN);
/*     */       
/* 126 */       if (identifierEquals("INHERITS")) {
/* 127 */         this.lexer.nextToken();
/* 128 */         accept(Token.LPAREN);
/* 129 */         SQLName inherits = this.exprParser.name();
/* 130 */         createTable.setInherits(new SQLExprTableSource((SQLExpr)inherits));
/* 131 */         accept(Token.RPAREN);
/*     */       } 
/*     */     } 
/*     */     
/* 135 */     return createTable;
/*     */   }
/*     */   
/*     */   protected SQLCreateTableStatement newCreateStatement() {
/* 139 */     return new SQLCreateTableStatement(getDbType());
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\parser\SQLCreateTableParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */