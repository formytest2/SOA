/*    */ package com.tranboot.client.druid.sql.parser;
/*    */ 
/*    */ import com.tranboot.client.druid.sql.ast.statement.SQLTableConstraint;

/*    */
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SQLDDLParser
/*    */   extends SQLStatementParser
/*    */ {
/*    */   public SQLDDLParser(String sql) {
/* 23 */     super(sql);
/*    */   }
/*    */   
/*    */   public SQLDDLParser(SQLExprParser exprParser) {
/* 27 */     super(exprParser);
/*    */   }
/*    */   
/*    */   protected SQLTableConstraint parseConstraint() {
/* 31 */     if (this.lexer.token() == Token.CONSTRAINT) {
/* 32 */       this.lexer.nextToken();
/*    */     }
/*    */     
/* 35 */     if (this.lexer.token() == Token.IDENTIFIER) {
/* 36 */       this.exprParser.name();
/* 37 */       throw new ParserException("TODO");
/*    */     } 
/*    */     
/* 40 */     if (this.lexer.token() == Token.PRIMARY) {
/* 41 */       this.lexer.nextToken();
/* 42 */       accept(Token.KEY);
/*    */       
/* 44 */       throw new ParserException("TODO");
/*    */     } 
/*    */     
/* 47 */     throw new ParserException("TODO");
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\parser\SQLDDLParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */