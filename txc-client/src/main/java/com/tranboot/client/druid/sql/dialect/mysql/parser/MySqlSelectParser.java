/*     */ package com.tranboot.client.druid.sql.dialect.mysql.parser;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLLiteralExpr;
import com.tranboot.client.druid.sql.ast.statement.*;
import com.tranboot.client.druid.sql.dialect.mysql.ast.*;
import com.tranboot.client.druid.sql.dialect.mysql.ast.expr.MySqlOutFileExpr;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlUnionQuery;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlUpdateTableSource;
import com.tranboot.client.druid.sql.parser.ParserException;
import com.tranboot.client.druid.sql.parser.SQLExprParser;
import com.tranboot.client.druid.sql.parser.SQLSelectParser;
import com.tranboot.client.druid.sql.parser.Token;

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
/*     */ 
/*     */ public class MySqlSelectParser
/*     */   extends SQLSelectParser
/*     */ {
/*     */   protected boolean returningFlag = false;
/*     */   protected MySqlUpdateStatement updateStmt;
/*     */   
/*     */   public MySqlSelectParser(SQLExprParser exprParser) {
/*  55 */     super(exprParser);
/*     */   }
/*     */   
/*     */   public MySqlSelectParser(String sql) {
/*  59 */     this(new MySqlExprParser(sql));
/*     */   }
/*     */   
/*     */   public void parseFrom(SQLSelectQueryBlock queryBlock) {
/*  63 */     if (this.lexer.token() != Token.FROM) {
/*     */       return;
/*     */     }
/*     */     
/*  67 */     this.lexer.nextToken();
/*     */     
/*  69 */     if (this.lexer.token() == Token.UPDATE) {
/*  70 */       this.updateStmt = parseUpdateStatment();
/*  71 */       List<SQLExpr> returnning = this.updateStmt.getReturning();
/*  72 */       for (SQLSelectItem item : queryBlock.getSelectList()) {
/*  73 */         SQLExpr itemExpr = item.getExpr();
/*  74 */         itemExpr.setParent((SQLObject)this.updateStmt);
/*  75 */         returnning.add(itemExpr);
/*     */       } 
/*  77 */       this.returningFlag = true;
/*     */       
/*     */       return;
/*     */     } 
/*  81 */     queryBlock.setFrom(parseTableSource());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SQLSelectQuery query() {
/*  87 */     if (this.lexer.token() == Token.LPAREN) {
/*  88 */       this.lexer.nextToken();
/*     */       
/*  90 */       SQLSelectQuery select = query();
/*  91 */       accept(Token.RPAREN);
/*     */       
/*  93 */       return queryRest(select);
/*     */     } 
/*     */     
/*  96 */     MySqlSelectQueryBlock queryBlock = new MySqlSelectQueryBlock();
/*     */     
/*  98 */     if (this.lexer.token() == Token.SELECT) {
/*  99 */       this.lexer.nextToken();
/*     */       
/* 101 */       if (this.lexer.token() == Token.HINT) {
/* 102 */         this.exprParser.parseHints(queryBlock.getHints());
/*     */       }
/*     */       
/* 105 */       if (this.lexer.token() == Token.COMMENT) {
/* 106 */         this.lexer.nextToken();
/*     */       }
/*     */       
/* 109 */       if (this.lexer.token() == Token.DISTINCT) {
/* 110 */         queryBlock.setDistionOption(2);
/* 111 */         this.lexer.nextToken();
/* 112 */       } else if (identifierEquals("DISTINCTROW")) {
/* 113 */         queryBlock.setDistionOption(4);
/* 114 */         this.lexer.nextToken();
/* 115 */       } else if (this.lexer.token() == Token.ALL) {
/* 116 */         queryBlock.setDistionOption(1);
/* 117 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 120 */       if (identifierEquals("HIGH_PRIORITY")) {
/* 121 */         queryBlock.setHignPriority(true);
/* 122 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 125 */       if (identifierEquals("STRAIGHT_JOIN")) {
/* 126 */         queryBlock.setStraightJoin(true);
/* 127 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 130 */       if (identifierEquals("SQL_SMALL_RESULT")) {
/* 131 */         queryBlock.setSmallResult(true);
/* 132 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 135 */       if (identifierEquals("SQL_BIG_RESULT")) {
/* 136 */         queryBlock.setBigResult(true);
/* 137 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 140 */       if (identifierEquals("SQL_BUFFER_RESULT")) {
/* 141 */         queryBlock.setBufferResult(true);
/* 142 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 145 */       if (identifierEquals("SQL_CACHE")) {
/* 146 */         queryBlock.setCache(Boolean.valueOf(true));
/* 147 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 150 */       if (identifierEquals("SQL_NO_CACHE")) {
/* 151 */         queryBlock.setCache(Boolean.valueOf(false));
/* 152 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 155 */       if (identifierEquals("SQL_CALC_FOUND_ROWS")) {
/* 156 */         queryBlock.setCalcFoundRows(true);
/* 157 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 160 */       parseSelectList((SQLSelectQueryBlock)queryBlock);
/*     */       
/* 162 */       parseInto((SQLSelectQueryBlock)queryBlock);
/*     */     } 
/*     */     
/* 165 */     parseFrom((SQLSelectQueryBlock)queryBlock);
/*     */     
/* 167 */     parseWhere((SQLSelectQueryBlock)queryBlock);
/*     */     
/* 169 */     parseGroupBy((SQLSelectQueryBlock)queryBlock);
/*     */     
/* 171 */     queryBlock.setOrderBy(this.exprParser.parseOrderBy());
/*     */     
/* 173 */     if (this.lexer.token() == Token.LIMIT) {
/* 174 */       queryBlock.setLimit(this.exprParser.parseLimit());
/*     */     }
/*     */     
/* 177 */     if (this.lexer.token() == Token.PROCEDURE) {
/* 178 */       this.lexer.nextToken();
/* 179 */       throw new ParserException("TODO");
/*     */     } 
/*     */     
/* 182 */     parseInto((SQLSelectQueryBlock)queryBlock);
/*     */     
/* 184 */     if (this.lexer.token() == Token.FOR) {
/* 185 */       this.lexer.nextToken();
/* 186 */       accept(Token.UPDATE);
/*     */       
/* 188 */       queryBlock.setForUpdate(true);
/*     */       
/* 190 */       if (identifierEquals("NO_WAIT")) {
/* 191 */         this.lexer.nextToken();
/* 192 */         queryBlock.setNoWait(true);
/* 193 */       } else if (identifierEquals("WAIT")) {
/* 194 */         this.lexer.nextToken();
/* 195 */         SQLExpr waitTime = this.exprParser.primary();
/* 196 */         queryBlock.setWaitTime(waitTime);
/*     */       } 
/*     */     } 
/*     */     
/* 200 */     if (this.lexer.token() == Token.LOCK) {
/* 201 */       this.lexer.nextToken();
/* 202 */       accept(Token.IN);
/* 203 */       acceptIdentifier("SHARE");
/* 204 */       acceptIdentifier("MODE");
/* 205 */       queryBlock.setLockInShareMode(true);
/*     */     } 
/*     */     
/* 208 */     return queryRest((SQLSelectQuery)queryBlock);
/*     */   }
/*     */   
/*     */   public SQLTableSource parseTableSource() {
/* 212 */     if (this.lexer.token() == Token.LPAREN) {
/* 213 */       SQLTableSource tableSource; this.lexer.nextToken();
/*     */       
/* 215 */       if (this.lexer.token() == Token.SELECT || this.lexer.token() == Token.WITH) {
/* 216 */         SQLSelect select = select();
/* 217 */         accept(Token.RPAREN);
/* 218 */         SQLSelectQuery query = queryRest(select.getQuery());
/* 219 */         if (query instanceof SQLUnionQuery) {
/* 220 */           SQLUnionQueryTableSource sQLUnionQueryTableSource = new SQLUnionQueryTableSource((SQLUnionQuery)query);
/*     */         } else {
/* 222 */           SQLSubqueryTableSource sQLSubqueryTableSource = new SQLSubqueryTableSource(select);
/*     */         } 
/* 224 */       } else if (this.lexer.token() == Token.LPAREN) {
/* 225 */         tableSource = parseTableSource();
/* 226 */         accept(Token.RPAREN);
/*     */       } else {
/* 228 */         tableSource = parseTableSource();
/* 229 */         accept(Token.RPAREN);
/*     */       } 
/*     */       
/* 232 */       return parseTableSourceRest(tableSource);
/*     */     } 
/*     */     
/* 235 */     if (this.lexer.token() == Token.UPDATE) {
/* 236 */       MySqlUpdateTableSource mySqlUpdateTableSource = new MySqlUpdateTableSource(parseUpdateStatment());
/* 237 */       return parseTableSourceRest((SQLTableSource)mySqlUpdateTableSource);
/*     */     } 
/*     */     
/* 240 */     if (this.lexer.token() == Token.SELECT) {
/* 241 */       throw new ParserException("TODO");
/*     */     }
/*     */     
/* 244 */     SQLExprTableSource tableReference = new SQLExprTableSource();
/*     */     
/* 246 */     parseTableSourceQueryTableExpr(tableReference);
/*     */     
/* 248 */     SQLTableSource tableSrc = parseTableSourceRest((SQLTableSource)tableReference);
/*     */     
/* 250 */     if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
/* 251 */       tableSrc.addAfterComment(this.lexer.readAndResetComments());
/*     */     }
/*     */     
/* 254 */     return tableSrc;
/*     */   }
/*     */   
/*     */   protected MySqlUpdateStatement parseUpdateStatment() {
/* 258 */     MySqlUpdateStatement update = new MySqlUpdateStatement();
/*     */     
/* 260 */     this.lexer.nextToken();
/*     */     
/* 262 */     if (identifierEquals("LOW_PRIORITY")) {
/* 263 */       this.lexer.nextToken();
/* 264 */       update.setLowPriority(true);
/*     */     } 
/*     */     
/* 267 */     if (identifierEquals("IGNORE")) {
/* 268 */       this.lexer.nextToken();
/* 269 */       update.setIgnore(true);
/*     */     } 
/*     */     
/* 272 */     if (identifierEquals("COMMIT_ON_SUCCESS")) {
/* 273 */       this.lexer.nextToken();
/* 274 */       update.setCommitOnSuccess(true);
/*     */     } 
/*     */     
/* 277 */     if (identifierEquals("ROLLBACK_ON_FAIL")) {
/* 278 */       this.lexer.nextToken();
/* 279 */       update.setRollBackOnFail(true);
/*     */     } 
/*     */     
/* 282 */     if (identifierEquals("QUEUE_ON_PK")) {
/* 283 */       this.lexer.nextToken();
/* 284 */       update.setQueryOnPk(true);
/*     */     } 
/*     */     
/* 287 */     if (identifierEquals("TARGET_AFFECT_ROW")) {
/* 288 */       this.lexer.nextToken();
/* 289 */       SQLExpr targetAffectRow = this.exprParser.expr();
/* 290 */       update.setTargetAffectRow(targetAffectRow);
/*     */     } 
/*     */     
/* 293 */     SQLTableSource updateTableSource = this.exprParser.createSelectParser().parseTableSource();
/* 294 */     update.setTableSource(updateTableSource);
/*     */     
/* 296 */     accept(Token.SET);
/*     */     
/*     */     while (true) {
/* 299 */       SQLUpdateSetItem item = this.exprParser.parseUpdateSetItem();
/* 300 */       update.addItem(item);
/*     */       
/* 302 */       if (this.lexer.token() != Token.COMMA) {
/*     */         break;
/*     */       }
/*     */       
/* 306 */       this.lexer.nextToken();
/*     */     } 
/*     */     
/* 309 */     if (this.lexer.token() == Token.WHERE) {
/* 310 */       this.lexer.nextToken();
/* 311 */       update.setWhere(this.exprParser.expr());
/*     */     } 
/*     */     
/* 314 */     update.setOrderBy(this.exprParser.parseOrderBy());
/* 315 */     update.setLimit(this.exprParser.parseLimit());
/*     */     
/* 317 */     return update;
/*     */   }
/*     */   
/*     */   protected void parseInto(SQLSelectQueryBlock queryBlock) {
/* 321 */     if (this.lexer.token() == Token.INTO) {
/* 322 */       this.lexer.nextToken();
/*     */       
/* 324 */       if (identifierEquals("OUTFILE")) {
/* 325 */         this.lexer.nextToken();
/*     */         
/* 327 */         MySqlOutFileExpr outFile = new MySqlOutFileExpr();
/* 328 */         outFile.setFile(expr());
/*     */         
/* 330 */         queryBlock.setInto((SQLExpr)outFile);
/*     */         
/* 332 */         if (identifierEquals("FIELDS") || identifierEquals("COLUMNS")) {
/* 333 */           this.lexer.nextToken();
/*     */           
/* 335 */           if (identifierEquals("TERMINATED")) {
/* 336 */             this.lexer.nextToken();
/* 337 */             accept(Token.BY);
/*     */           } 
/* 339 */           outFile.setColumnsTerminatedBy(expr());
/*     */           
/* 341 */           if (identifierEquals("OPTIONALLY")) {
/* 342 */             this.lexer.nextToken();
/* 343 */             outFile.setColumnsEnclosedOptionally(true);
/*     */           } 
/*     */           
/* 346 */           if (identifierEquals("ENCLOSED")) {
/* 347 */             this.lexer.nextToken();
/* 348 */             accept(Token.BY);
/* 349 */             outFile.setColumnsEnclosedBy((SQLLiteralExpr)expr());
/*     */           } 
/*     */           
/* 352 */           if (identifierEquals("ESCAPED")) {
/* 353 */             this.lexer.nextToken();
/* 354 */             accept(Token.BY);
/* 355 */             outFile.setColumnsEscaped((SQLLiteralExpr)expr());
/*     */           } 
/*     */         } 
/*     */         
/* 359 */         if (identifierEquals("LINES")) {
/* 360 */           this.lexer.nextToken();
/*     */           
/* 362 */           if (identifierEquals("STARTING")) {
/* 363 */             this.lexer.nextToken();
/* 364 */             accept(Token.BY);
/* 365 */             outFile.setLinesStartingBy((SQLLiteralExpr)expr());
/*     */           } else {
/* 367 */             identifierEquals("TERMINATED");
/* 368 */             this.lexer.nextToken();
/* 369 */             accept(Token.BY);
/* 370 */             outFile.setLinesTerminatedBy((SQLLiteralExpr)expr());
/*     */           } 
/*     */         } 
/*     */       } else {
/* 374 */         queryBlock.setInto((SQLExpr)this.exprParser.name());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected SQLTableSource parseTableSourceRest(SQLTableSource tableSource) {
/* 380 */     if (identifierEquals("USING")) {
/* 381 */       return tableSource;
/*     */     }
/*     */     
/* 384 */     if (this.lexer.token() == Token.USE) {
/* 385 */       this.lexer.nextToken();
/* 386 */       MySqlUseIndexHint hint = new MySqlUseIndexHint();
/* 387 */       parseIndexHint((MySqlIndexHintImpl)hint);
/* 388 */       tableSource.getHints().add(hint);
/*     */     } 
/*     */     
/* 391 */     if (identifierEquals("IGNORE")) {
/* 392 */       this.lexer.nextToken();
/* 393 */       MySqlIgnoreIndexHint hint = new MySqlIgnoreIndexHint();
/* 394 */       parseIndexHint((MySqlIndexHintImpl)hint);
/* 395 */       tableSource.getHints().add(hint);
/*     */     } 
/*     */     
/* 398 */     if (identifierEquals("FORCE")) {
/* 399 */       this.lexer.nextToken();
/* 400 */       MySqlForceIndexHint hint = new MySqlForceIndexHint();
/* 401 */       parseIndexHint((MySqlIndexHintImpl)hint);
/* 402 */       tableSource.getHints().add(hint);
/*     */     } 
/*     */     
/* 405 */     if (this.lexer.token() == Token.PARTITION) {
/* 406 */       this.lexer.nextToken();
/* 407 */       accept(Token.LPAREN);
/* 408 */       this.exprParser.names(((SQLExprTableSource)tableSource).getPartitions(), (SQLObject)tableSource);
/* 409 */       accept(Token.RPAREN);
/*     */     } 
/*     */     
/* 412 */     return super.parseTableSourceRest(tableSource);
/*     */   }
/*     */   
/*     */   private void parseIndexHint(MySqlIndexHintImpl hint) {
/* 416 */     if (this.lexer.token() == Token.INDEX) {
/* 417 */       this.lexer.nextToken();
/*     */     } else {
/* 419 */       accept(Token.KEY);
/*     */     } 
/*     */     
/* 422 */     if (this.lexer.token() == Token.FOR) {
/* 423 */       this.lexer.nextToken();
/*     */       
/* 425 */       if (this.lexer.token() == Token.JOIN) {
/* 426 */         this.lexer.nextToken();
/* 427 */         hint.setOption(MySqlIndexHint.Option.JOIN);
/* 428 */       } else if (this.lexer.token() == Token.ORDER) {
/* 429 */         this.lexer.nextToken();
/* 430 */         accept(Token.BY);
/* 431 */         hint.setOption(MySqlIndexHint.Option.ORDER_BY);
/*     */       } else {
/* 433 */         accept(Token.GROUP);
/* 434 */         accept(Token.BY);
/* 435 */         hint.setOption(MySqlIndexHint.Option.GROUP_BY);
/*     */       } 
/*     */     } 
/*     */     
/* 439 */     accept(Token.LPAREN);
/* 440 */     if (this.lexer.token() == Token.PRIMARY) {
/* 441 */       this.lexer.nextToken();
/* 442 */       hint.getIndexList().add(new SQLIdentifierExpr("PRIMARY"));
/*     */     } else {
/* 444 */       this.exprParser.names(hint.getIndexList());
/*     */     } 
/* 446 */     accept(Token.RPAREN);
/*     */   }
/*     */   
/*     */   protected MySqlUnionQuery createSQLUnionQuery() {
/* 450 */     return new MySqlUnionQuery();
/*     */   }
/*     */   
/*     */   public SQLUnionQuery unionRest(SQLUnionQuery union) {
/* 454 */     if (this.lexer.token() == Token.LIMIT) {
/* 455 */       MySqlUnionQuery mysqlUnionQuery = (MySqlUnionQuery)union;
/* 456 */       mysqlUnionQuery.setLimit(this.exprParser.parseLimit());
/*     */     } 
/* 458 */     return super.unionRest(union);
/*     */   }
/*     */   
/*     */   public MySqlExprParser getExprParser() {
/* 462 */     return (MySqlExprParser)this.exprParser;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\parser\MySqlSelectParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */