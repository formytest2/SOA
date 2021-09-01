/*     */ package com.tranboot.client.druid.sql.dialect.oracle.parser;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.statement.SQLTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleUpdateStatement;
import com.tranboot.client.druid.sql.parser.Lexer;
import com.tranboot.client.druid.sql.parser.ParserException;
import com.tranboot.client.druid.sql.parser.SQLStatementParser;
import com.tranboot.client.druid.sql.parser.Token;

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
/*     */ public class OracleUpdateParser
/*     */   extends SQLStatementParser
/*     */ {
/*     */   public OracleUpdateParser(String sql) {
/*  29 */     super(new OracleExprParser(sql));
/*     */   }
/*     */   
/*     */   public OracleUpdateParser(Lexer lexer) {
/*  33 */     super(new OracleExprParser(lexer));
/*     */   }
/*     */   
/*     */   public OracleUpdateStatement parseUpdateStatement() {
/*  37 */     OracleUpdateStatement update = new OracleUpdateStatement();
/*     */     
/*  39 */     if (this.lexer.token() == Token.UPDATE) {
/*  40 */       this.lexer.nextToken();
/*     */       
/*  42 */       parseHints(update);
/*     */       
/*  44 */       if (identifierEquals("ONLY")) {
/*  45 */         update.setOnly(true);
/*     */       }
/*     */       
/*  48 */       SQLTableSource tableSource = this.exprParser.createSelectParser().parseTableSource();
/*  49 */       update.setTableSource(tableSource);
/*     */       
/*  51 */       if (update.getAlias() == null || update.getAlias().length() == 0) {
/*  52 */         update.setAlias(as());
/*     */       }
/*     */     } 
/*     */     
/*  56 */     parseUpdateSet((SQLUpdateStatement)update);
/*     */     
/*  58 */     parseWhere(update);
/*     */     
/*  60 */     parseReturn(update);
/*     */     
/*  62 */     parseErrorLoging(update);
/*     */     
/*  64 */     return update;
/*     */   }
/*     */   
/*     */   private void parseErrorLoging(OracleUpdateStatement update) {
/*  68 */     if (identifierEquals("LOG")) {
/*  69 */       throw new ParserException("TODO");
/*     */     }
/*     */   }
/*     */   
/*     */   private void parseReturn(OracleUpdateStatement update) {
/*  74 */     if (identifierEquals("RETURN") || this.lexer.token() == Token.RETURNING) {
/*  75 */       this.lexer.nextToken();
/*     */       
/*     */       while (true) {
/*  78 */         SQLExpr item = this.exprParser.expr();
/*  79 */         update.getReturning().add(item);
/*     */         
/*  81 */         if (this.lexer.token() == Token.COMMA) {
/*  82 */           this.lexer.nextToken();
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/*     */         break;
/*     */       } 
/*  89 */       accept(Token.INTO);
/*     */       
/*     */       while (true) {
/*  92 */         SQLExpr item = this.exprParser.expr();
/*  93 */         update.getReturningInto().add(item);
/*     */         
/*  95 */         if (this.lexer.token() == Token.COMMA) {
/*  96 */           this.lexer.nextToken();
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void parseHints(OracleUpdateStatement update) {
/* 106 */     this.exprParser.parseHints(update.getHints());
/*     */   }
/*     */   
/*     */   private void parseWhere(OracleUpdateStatement update) {
/* 110 */     if (this.lexer.token() == Token.WHERE) {
/* 111 */       this.lexer.nextToken();
/* 112 */       update.setWhere(this.exprParser.expr());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\parser\OracleUpdateParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */