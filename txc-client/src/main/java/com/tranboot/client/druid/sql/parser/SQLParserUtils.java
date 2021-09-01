/*    */ package com.tranboot.client.druid.sql.parser;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.dialect.mysql.parser.MySqlExprParser;
import com.tranboot.client.druid.sql.dialect.mysql.parser.MySqlLexer;
import com.tranboot.client.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.tranboot.client.druid.sql.dialect.oracle.parser.OracleExprParser;
import com.tranboot.client.druid.sql.dialect.oracle.parser.OracleLexer;
import com.tranboot.client.druid.sql.dialect.oracle.parser.OracleStatementParser;

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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SQLParserUtils
/*    */ {
/*    */   public static SQLStatementParser createSQLStatementParser(String sql, String dbType) {
/* 29 */     if ("oracle".equals(dbType) || "AliOracle".equals(dbType)) {
/* 30 */       return (SQLStatementParser)new OracleStatementParser(sql);
/*    */     }
/*    */     
/* 33 */     if ("mysql".equals(dbType)) {
/* 34 */       return (SQLStatementParser)new MySqlStatementParser(sql);
/*    */     }
/*    */     
/* 37 */     if ("mariadb".equals(dbType)) {
/* 38 */       return (SQLStatementParser)new MySqlStatementParser(sql);
/*    */     }
/*    */ 
/*    */     
/* 42 */     if ("h2".equals(dbType)) {
/* 43 */       return (SQLStatementParser)new MySqlStatementParser(sql);
/*    */     }
/*    */     
/* 46 */     return new SQLStatementParser(sql, dbType);
/*    */   }
/*    */   
/*    */   public static SQLExprParser createExprParser(String sql, String dbType) {
/* 50 */     if ("oracle".equals(dbType) || "AliOracle".equals(dbType)) {
/* 51 */       return (SQLExprParser)new OracleExprParser(sql);
/*    */     }
/*    */     
/* 54 */     if ("mysql".equals(dbType) || "mariadb"
/* 55 */       .equals(dbType) || "h2"
/* 56 */       .equals(dbType)) {
/* 57 */       return (SQLExprParser)new MySqlExprParser(sql);
/*    */     }
/*    */     
/* 60 */     return new SQLExprParser(sql);
/*    */   }
/*    */   
/*    */   public static Lexer createLexer(String sql, String dbType) {
/* 64 */     if ("oracle".equals(dbType) || "AliOracle".equals(dbType)) {
/* 65 */       return (Lexer)new OracleLexer(sql);
/*    */     }
/*    */     
/* 68 */     if ("mysql".equals(dbType) || "mariadb"
/* 69 */       .equals(dbType) || "h2"
/* 70 */       .equals(dbType)) {
/* 71 */       return (Lexer)new MySqlLexer(sql);
/*    */     }
/* 73 */     return new Lexer(sql);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\parser\SQLParserUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */