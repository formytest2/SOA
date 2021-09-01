/*     */ package com.tranboot.client.druid.sql.dialect.mysql.parser;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLLiteralExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLVariantRefExpr;
import com.tranboot.client.druid.sql.ast.statement.*;
import com.tranboot.client.druid.sql.dialect.mysql.ast.*;
import com.tranboot.client.druid.sql.dialect.mysql.ast.clause.MySqlSelectIntoStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.expr.MySqlOutFileExpr;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlUnionQuery;
import com.tranboot.client.druid.sql.parser.ParserException;
import com.tranboot.client.druid.sql.parser.SQLExprParser;
import com.tranboot.client.druid.sql.parser.SQLSelectParser;
import com.tranboot.client.druid.sql.parser.Token;

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
/*     */ public class MySqlSelectIntoParser
/*     */   extends SQLSelectParser
/*     */ {
/*     */   private List<SQLExpr> argsList;
/*     */   
/*     */   public MySqlSelectIntoParser(SQLExprParser exprParser) {
/*  53 */     super(exprParser);
/*     */   }
/*     */   
/*     */   public MySqlSelectIntoParser(String sql) {
/*  57 */     this(new MySqlExprParser(sql));
/*     */   }
/*     */ 
/*     */   
/*     */   public MySqlSelectIntoStatement parseSelectInto() {
/*  62 */     SQLSelect select = select();
/*  63 */     MySqlSelectIntoStatement stmt = new MySqlSelectIntoStatement();
/*  64 */     stmt.setSelect(select);
/*  65 */     stmt.setVarList(this.argsList);
/*  66 */     return stmt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SQLSelectQuery query() {
/*  72 */     if (this.lexer.token() == Token.LPAREN) {
/*  73 */       this.lexer.nextToken();
/*     */       
/*  75 */       SQLSelectQuery select = query();
/*  76 */       accept(Token.RPAREN);
/*     */       
/*  78 */       return queryRest(select);
/*     */     } 
/*     */     
/*  81 */     MySqlSelectQueryBlock queryBlock = new MySqlSelectQueryBlock();
/*     */     
/*  83 */     if (this.lexer.token() == Token.SELECT) {
/*  84 */       this.lexer.nextToken();
/*     */       
/*  86 */       if (this.lexer.token() == Token.HINT) {
/*  87 */         this.exprParser.parseHints(queryBlock.getHints());
/*     */       }
/*     */       
/*  90 */       if (this.lexer.token() == Token.COMMENT) {
/*  91 */         this.lexer.nextToken();
/*     */       }
/*     */       
/*  94 */       if (this.lexer.token() == Token.DISTINCT) {
/*  95 */         queryBlock.setDistionOption(2);
/*  96 */         this.lexer.nextToken();
/*  97 */       } else if (identifierEquals("DISTINCTROW")) {
/*  98 */         queryBlock.setDistionOption(4);
/*  99 */         this.lexer.nextToken();
/* 100 */       } else if (this.lexer.token() == Token.ALL) {
/* 101 */         queryBlock.setDistionOption(1);
/* 102 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 105 */       if (identifierEquals("HIGH_PRIORITY")) {
/* 106 */         queryBlock.setHignPriority(true);
/* 107 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 110 */       if (identifierEquals("STRAIGHT_JOIN")) {
/* 111 */         queryBlock.setStraightJoin(true);
/* 112 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 115 */       if (identifierEquals("SQL_SMALL_RESULT")) {
/* 116 */         queryBlock.setSmallResult(true);
/* 117 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 120 */       if (identifierEquals("SQL_BIG_RESULT")) {
/* 121 */         queryBlock.setBigResult(true);
/* 122 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 125 */       if (identifierEquals("SQL_BUFFER_RESULT")) {
/* 126 */         queryBlock.setBufferResult(true);
/* 127 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 130 */       if (identifierEquals("SQL_CACHE")) {
/* 131 */         queryBlock.setCache(Boolean.valueOf(true));
/* 132 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 135 */       if (identifierEquals("SQL_NO_CACHE")) {
/* 136 */         queryBlock.setCache(Boolean.valueOf(false));
/* 137 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 140 */       if (identifierEquals("SQL_CALC_FOUND_ROWS")) {
/* 141 */         queryBlock.setCalcFoundRows(true);
/* 142 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 145 */       parseSelectList((SQLSelectQueryBlock)queryBlock);
/*     */       
/* 147 */       this.argsList = parseIntoArgs();
/*     */     } 
/*     */     
/* 150 */     parseFrom((SQLSelectQueryBlock)queryBlock);
/*     */     
/* 152 */     parseWhere((SQLSelectQueryBlock)queryBlock);
/*     */     
/* 154 */     parseGroupBy((SQLSelectQueryBlock)queryBlock);
/*     */     
/* 156 */     queryBlock.setOrderBy(this.exprParser.parseOrderBy());
/*     */     
/* 158 */     if (this.lexer.token() == Token.LIMIT) {
/* 159 */       queryBlock.setLimit(this.exprParser.parseLimit());
/*     */     }
/*     */     
/* 162 */     if (this.lexer.token() == Token.PROCEDURE) {
/* 163 */       this.lexer.nextToken();
/* 164 */       throw new ParserException("TODO");
/*     */     } 
/*     */     
/* 167 */     parseInto((SQLSelectQueryBlock)queryBlock);
/*     */     
/* 169 */     if (this.lexer.token() == Token.FOR) {
/* 170 */       this.lexer.nextToken();
/* 171 */       accept(Token.UPDATE);
/*     */       
/* 173 */       queryBlock.setForUpdate(true);
/*     */     } 
/*     */     
/* 176 */     if (this.lexer.token() == Token.LOCK) {
/* 177 */       this.lexer.nextToken();
/* 178 */       accept(Token.IN);
/* 179 */       acceptIdentifier("SHARE");
/* 180 */       acceptIdentifier("MODE");
/* 181 */       queryBlock.setLockInShareMode(true);
/*     */     } 
/*     */     
/* 184 */     return queryRest((SQLSelectQuery)queryBlock);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<SQLExpr> parseIntoArgs() {
/* 192 */     List<SQLExpr> args = new ArrayList<>();
/* 193 */     if (this.lexer.token() == Token.INTO) {
/* 194 */       accept(Token.INTO);
/*     */       while (true) {
/*     */         SQLVariantRefExpr sQLVariantRefExpr;
/* 197 */         SQLExpr var = this.exprParser.primary();
/* 198 */         if (var instanceof SQLIdentifierExpr)
/*     */         {
/* 200 */           sQLVariantRefExpr = new SQLVariantRefExpr(((SQLIdentifierExpr)var).getName());
/*     */         }
/* 202 */         args.add(sQLVariantRefExpr);
/* 203 */         if (this.lexer.token() == Token.COMMA) {
/* 204 */           accept(Token.COMMA);
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/* 213 */     return args;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void parseInto(SQLSelectQueryBlock queryBlock) {
/* 218 */     if (this.lexer.token() == Token.INTO) {
/* 219 */       this.lexer.nextToken();
/*     */       
/* 221 */       if (identifierEquals("OUTFILE")) {
/* 222 */         this.lexer.nextToken();
/*     */         
/* 224 */         MySqlOutFileExpr outFile = new MySqlOutFileExpr();
/* 225 */         outFile.setFile(expr());
/*     */         
/* 227 */         queryBlock.setInto((SQLExpr)outFile);
/*     */         
/* 229 */         if (identifierEquals("FIELDS") || identifierEquals("COLUMNS")) {
/* 230 */           this.lexer.nextToken();
/*     */           
/* 232 */           if (identifierEquals("TERMINATED")) {
/* 233 */             this.lexer.nextToken();
/* 234 */             accept(Token.BY);
/*     */           } 
/* 236 */           outFile.setColumnsTerminatedBy(expr());
/*     */           
/* 238 */           if (identifierEquals("OPTIONALLY")) {
/* 239 */             this.lexer.nextToken();
/* 240 */             outFile.setColumnsEnclosedOptionally(true);
/*     */           } 
/*     */           
/* 243 */           if (identifierEquals("ENCLOSED")) {
/* 244 */             this.lexer.nextToken();
/* 245 */             accept(Token.BY);
/* 246 */             outFile.setColumnsEnclosedBy((SQLLiteralExpr)expr());
/*     */           } 
/*     */           
/* 249 */           if (identifierEquals("ESCAPED")) {
/* 250 */             this.lexer.nextToken();
/* 251 */             accept(Token.BY);
/* 252 */             outFile.setColumnsEscaped((SQLLiteralExpr)expr());
/*     */           } 
/*     */         } 
/*     */         
/* 256 */         if (identifierEquals("LINES")) {
/* 257 */           this.lexer.nextToken();
/*     */           
/* 259 */           if (identifierEquals("STARTING")) {
/* 260 */             this.lexer.nextToken();
/* 261 */             accept(Token.BY);
/* 262 */             outFile.setLinesStartingBy((SQLLiteralExpr)expr());
/*     */           } else {
/* 264 */             identifierEquals("TERMINATED");
/* 265 */             this.lexer.nextToken();
/* 266 */             accept(Token.BY);
/* 267 */             outFile.setLinesTerminatedBy((SQLLiteralExpr)expr());
/*     */           } 
/*     */         } 
/*     */       } else {
/* 271 */         queryBlock.setInto((SQLExpr)this.exprParser.name());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected SQLTableSource parseTableSourceRest(SQLTableSource tableSource) {
/* 277 */     if (identifierEquals("USING")) {
/* 278 */       return tableSource;
/*     */     }
/*     */     
/* 281 */     if (this.lexer.token() == Token.USE) {
/* 282 */       this.lexer.nextToken();
/* 283 */       MySqlUseIndexHint hint = new MySqlUseIndexHint();
/* 284 */       parseIndexHint((MySqlIndexHintImpl)hint);
/* 285 */       tableSource.getHints().add(hint);
/*     */     } 
/*     */     
/* 288 */     if (identifierEquals("IGNORE")) {
/* 289 */       this.lexer.nextToken();
/* 290 */       MySqlIgnoreIndexHint hint = new MySqlIgnoreIndexHint();
/* 291 */       parseIndexHint((MySqlIndexHintImpl)hint);
/* 292 */       tableSource.getHints().add(hint);
/*     */     } 
/*     */     
/* 295 */     if (identifierEquals("FORCE")) {
/* 296 */       this.lexer.nextToken();
/* 297 */       MySqlForceIndexHint hint = new MySqlForceIndexHint();
/* 298 */       parseIndexHint((MySqlIndexHintImpl)hint);
/* 299 */       tableSource.getHints().add(hint);
/*     */     } 
/*     */     
/* 302 */     return super.parseTableSourceRest(tableSource);
/*     */   }
/*     */   
/*     */   private void parseIndexHint(MySqlIndexHintImpl hint) {
/* 306 */     if (this.lexer.token() == Token.INDEX) {
/* 307 */       this.lexer.nextToken();
/*     */     } else {
/* 309 */       accept(Token.KEY);
/*     */     } 
/*     */     
/* 312 */     if (this.lexer.token() == Token.FOR) {
/* 313 */       this.lexer.nextToken();
/*     */       
/* 315 */       if (this.lexer.token() == Token.JOIN) {
/* 316 */         this.lexer.nextToken();
/* 317 */         hint.setOption(MySqlIndexHint.Option.JOIN);
/* 318 */       } else if (this.lexer.token() == Token.ORDER) {
/* 319 */         this.lexer.nextToken();
/* 320 */         accept(Token.BY);
/* 321 */         hint.setOption(MySqlIndexHint.Option.ORDER_BY);
/*     */       } else {
/* 323 */         accept(Token.GROUP);
/* 324 */         accept(Token.BY);
/* 325 */         hint.setOption(MySqlIndexHint.Option.GROUP_BY);
/*     */       } 
/*     */     } 
/*     */     
/* 329 */     accept(Token.LPAREN);
/* 330 */     if (this.lexer.token() == Token.PRIMARY) {
/* 331 */       this.lexer.nextToken();
/* 332 */       hint.getIndexList().add(new SQLIdentifierExpr("PRIMARY"));
/*     */     } else {
/* 334 */       this.exprParser.names(hint.getIndexList());
/*     */     } 
/* 336 */     accept(Token.RPAREN);
/*     */   }
/*     */   
/*     */   protected MySqlUnionQuery createSQLUnionQuery() {
/* 340 */     return new MySqlUnionQuery();
/*     */   }
/*     */   
/*     */   public SQLUnionQuery unionRest(SQLUnionQuery union) {
/* 344 */     if (this.lexer.token() == Token.LIMIT) {
/* 345 */       MySqlUnionQuery mysqlUnionQuery = (MySqlUnionQuery)union;
/* 346 */       mysqlUnionQuery.setLimit(this.exprParser.parseLimit());
/*     */     } 
/* 348 */     return super.unionRest(union);
/*     */   }
/*     */   
/*     */   public MySqlExprParser getExprParser() {
/* 352 */     return (MySqlExprParser)this.exprParser;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\parser\MySqlSelectIntoParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */