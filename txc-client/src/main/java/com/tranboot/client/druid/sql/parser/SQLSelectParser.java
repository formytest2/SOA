/*     */ package com.tranboot.client.druid.sql.parser;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.*;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.statement.*;
import com.tranboot.client.druid.sql.dialect.mysql.ast.expr.MySqlOrderingExpr;

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
/*     */ public class SQLSelectParser
/*     */   extends SQLParser
/*     */ {
/*     */   protected SQLExprParser exprParser;
/*     */   
/*     */   public SQLSelectParser(String sql) {
/*  46 */     super(sql);
/*     */   }
/*     */   
/*     */   public SQLSelectParser(Lexer lexer) {
/*  50 */     super(lexer);
/*     */   }
/*     */   
/*     */   public SQLSelectParser(SQLExprParser exprParser) {
/*  54 */     super(exprParser.getLexer(), exprParser.getDbType());
/*  55 */     this.exprParser = exprParser;
/*     */   }
/*     */   
/*     */   public SQLSelect select() {
/*  59 */     SQLSelect select = new SQLSelect();
/*     */     
/*  61 */     withSubquery(select);
/*     */     
/*  63 */     select.setQuery(query());
/*  64 */     select.setOrderBy(parseOrderBy());
/*     */     
/*  66 */     if (select.getOrderBy() == null) {
/*  67 */       select.setOrderBy(parseOrderBy());
/*     */     }
/*     */     
/*  70 */     while (this.lexer.token() == Token.HINT) {
/*  71 */       this.exprParser.parseHints(select.getHints());
/*     */     }
/*     */     
/*  74 */     return select;
/*     */   }
/*     */   
/*     */   protected SQLUnionQuery createSQLUnionQuery() {
/*  78 */     return new SQLUnionQuery();
/*     */   }
/*     */   
/*     */   public SQLUnionQuery unionRest(SQLUnionQuery union) {
/*  82 */     if (this.lexer.token() == Token.ORDER) {
/*  83 */       SQLOrderBy orderBy = this.exprParser.parseOrderBy();
/*  84 */       union.setOrderBy(orderBy);
/*  85 */       return unionRest(union);
/*     */     } 
/*  87 */     return union;
/*     */   }
/*     */   
/*     */   public SQLSelectQuery queryRest(SQLSelectQuery selectQuery) {
/*  91 */     if (this.lexer.token() == Token.UNION) {
/*  92 */       this.lexer.nextToken();
/*     */       
/*  94 */       SQLUnionQuery union = createSQLUnionQuery();
/*  95 */       union.setLeft(selectQuery);
/*     */       
/*  97 */       if (this.lexer.token() == Token.ALL) {
/*  98 */         union.setOperator(SQLUnionOperator.UNION_ALL);
/*  99 */         this.lexer.nextToken();
/* 100 */       } else if (this.lexer.token() == Token.DISTINCT) {
/* 101 */         union.setOperator(SQLUnionOperator.DISTINCT);
/* 102 */         this.lexer.nextToken();
/*     */       } 
/* 104 */       SQLSelectQuery right = query();
/* 105 */       union.setRight(right);
/*     */       
/* 107 */       return (SQLSelectQuery)unionRest(union);
/*     */     } 
/*     */     
/* 110 */     if (this.lexer.token() == Token.EXCEPT) {
/* 111 */       this.lexer.nextToken();
/*     */       
/* 113 */       SQLUnionQuery union = new SQLUnionQuery();
/* 114 */       union.setLeft(selectQuery);
/*     */       
/* 116 */       union.setOperator(SQLUnionOperator.EXCEPT);
/*     */       
/* 118 */       SQLSelectQuery right = query();
/* 119 */       union.setRight(right);
/*     */       
/* 121 */       return (SQLSelectQuery)union;
/*     */     } 
/*     */     
/* 124 */     if (this.lexer.token() == Token.INTERSECT) {
/* 125 */       this.lexer.nextToken();
/*     */       
/* 127 */       SQLUnionQuery union = new SQLUnionQuery();
/* 128 */       union.setLeft(selectQuery);
/*     */       
/* 130 */       union.setOperator(SQLUnionOperator.INTERSECT);
/*     */       
/* 132 */       SQLSelectQuery right = query();
/* 133 */       union.setRight(right);
/*     */       
/* 135 */       return (SQLSelectQuery)union;
/*     */     } 
/*     */     
/* 138 */     if (this.lexer.token() == Token.MINUS) {
/* 139 */       this.lexer.nextToken();
/*     */       
/* 141 */       SQLUnionQuery union = new SQLUnionQuery();
/* 142 */       union.setLeft(selectQuery);
/*     */       
/* 144 */       union.setOperator(SQLUnionOperator.MINUS);
/*     */       
/* 146 */       SQLSelectQuery right = query();
/* 147 */       union.setRight(right);
/*     */       
/* 149 */       return (SQLSelectQuery)union;
/*     */     } 
/*     */     
/* 152 */     return selectQuery;
/*     */   }
/*     */   
/*     */   public SQLSelectQuery query() {
/* 156 */     if (this.lexer.token() == Token.LPAREN) {
/* 157 */       this.lexer.nextToken();
/*     */       
/* 159 */       SQLSelectQuery select = query();
/* 160 */       accept(Token.RPAREN);
/*     */       
/* 162 */       return queryRest(select);
/*     */     } 
/*     */     
/* 165 */     SQLSelectQueryBlock queryBlock = new SQLSelectQueryBlock();
/*     */     
/* 167 */     if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
/* 168 */       queryBlock.addBeforeComment(this.lexer.readAndResetComments());
/*     */     }
/*     */     
/* 171 */     accept(Token.SELECT);
/*     */     
/* 173 */     if (this.lexer.token() == Token.COMMENT) {
/* 174 */       this.lexer.nextToken();
/*     */     }
/*     */     
/* 177 */     if ("informix".equals(this.dbType)) {
/* 178 */       if (identifierEquals("SKIP")) {
/* 179 */         this.lexer.nextToken();
/* 180 */         SQLExpr offset = this.exprParser.primary();
/* 181 */         queryBlock.setOffset(offset);
/*     */       } 
/*     */       
/* 184 */       if (identifierEquals("FIRST")) {
/* 185 */         this.lexer.nextToken();
/* 186 */         SQLExpr first = this.exprParser.primary();
/* 187 */         queryBlock.setFirst(first);
/*     */       } 
/*     */     } 
/*     */     
/* 191 */     if (this.lexer.token() == Token.DISTINCT) {
/* 192 */       queryBlock.setDistionOption(2);
/* 193 */       this.lexer.nextToken();
/* 194 */     } else if (this.lexer.token() == Token.UNIQUE) {
/* 195 */       queryBlock.setDistionOption(3);
/* 196 */       this.lexer.nextToken();
/* 197 */     } else if (this.lexer.token() == Token.ALL) {
/* 198 */       queryBlock.setDistionOption(1);
/* 199 */       this.lexer.nextToken();
/*     */     } 
/*     */     
/* 202 */     parseSelectList(queryBlock);
/*     */     
/* 204 */     parseFrom(queryBlock);
/*     */     
/* 206 */     parseWhere(queryBlock);
/*     */     
/* 208 */     parseGroupBy(queryBlock);
/*     */     
/* 210 */     parseFetchClause(queryBlock);
/*     */     
/* 212 */     return queryRest((SQLSelectQuery)queryBlock);
/*     */   }
/*     */   
/*     */   protected void withSubquery(SQLSelect select) {
/* 216 */     if (this.lexer.token() == Token.WITH) {
/* 217 */       this.lexer.nextToken();
/*     */       
/* 219 */       SQLWithSubqueryClause withQueryClause = new SQLWithSubqueryClause();
/*     */       
/* 221 */       if (this.lexer.token == Token.RECURSIVE || identifierEquals("RECURSIVE")) {
/* 222 */         this.lexer.nextToken();
/* 223 */         withQueryClause.setRecursive(Boolean.valueOf(true));
/*     */       } 
/*     */       
/*     */       while (true) {
/* 227 */         SQLWithSubqueryClause.Entry entry = new SQLWithSubqueryClause.Entry();
/* 228 */         entry.setParent((SQLObject)withQueryClause);
/* 229 */         entry.setName((SQLIdentifierExpr)this.exprParser.name());
/*     */         
/* 231 */         if (this.lexer.token() == Token.LPAREN) {
/* 232 */           this.lexer.nextToken();
/* 233 */           this.exprParser.names(entry.getColumns());
/* 234 */           accept(Token.RPAREN);
/*     */         } 
/*     */         
/* 237 */         accept(Token.AS);
/* 238 */         accept(Token.LPAREN);
/* 239 */         entry.setSubQuery(select());
/* 240 */         accept(Token.RPAREN);
/*     */         
/* 242 */         withQueryClause.addEntry(entry);
/*     */         
/* 244 */         if (this.lexer.token() == Token.COMMA) {
/* 245 */           this.lexer.nextToken();
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/*     */         break;
/*     */       } 
/* 252 */       select.setWithSubQuery(withQueryClause);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void parseWhere(SQLSelectQueryBlock queryBlock) {
/* 257 */     if (this.lexer.token() == Token.WHERE) {
/* 258 */       this.lexer.nextToken();
/*     */       
/* 260 */       List<String> beforeComments = null;
/* 261 */       if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
/* 262 */         beforeComments = this.lexer.readAndResetComments();
/*     */       }
/* 264 */       SQLExpr where = expr();
/*     */       
/* 266 */       if (where != null && beforeComments != null) {
/* 267 */         where.addBeforeComment(beforeComments);
/*     */       }
/*     */       
/* 270 */       if (this.lexer.hasComment() && this.lexer.isKeepComments() && this.lexer
/* 271 */         .token() != Token.INSERT)
/*     */       {
/* 273 */         where.addAfterComment(this.lexer.readAndResetComments());
/*     */       }
/*     */       
/* 276 */       queryBlock.setWhere(where);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void parseGroupBy(SQLSelectQueryBlock queryBlock) {
/* 281 */     if (this.lexer.token() == Token.GROUP) {
/* 282 */       this.lexer.nextToken();
/* 283 */       accept(Token.BY);
/*     */       
/* 285 */       SQLSelectGroupByClause groupBy = new SQLSelectGroupByClause();
/*     */       while (true) {
/* 287 */         SQLExpr item = parseGroupByItem();
/*     */         
/* 289 */         item.setParent((SQLObject)groupBy);
/* 290 */         groupBy.addItem(item);
/*     */         
/* 292 */         if (this.lexer.token() != Token.COMMA) {
/*     */           break;
/*     */         }
/*     */         
/* 296 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 299 */       if (this.lexer.token() == Token.HAVING) {
/* 300 */         this.lexer.nextToken();
/*     */         
/* 302 */         SQLExpr having = this.exprParser.expr();
/* 303 */         groupBy.setHaving(having);
/*     */       } 
/*     */       
/* 306 */       if (this.lexer.token() == Token.WITH) {
/* 307 */         this.lexer.nextToken();
/*     */         
/* 309 */         if (identifierEquals("CUBE")) {
/* 310 */           this.lexer.nextToken();
/* 311 */           groupBy.setWithCube(true);
/*     */         } else {
/* 313 */           acceptIdentifier("ROLLUP");
/* 314 */           groupBy.setWithRollUp(true);
/*     */         } 
/*     */       } 
/*     */       
/* 318 */       queryBlock.setGroupBy(groupBy);
/* 319 */     } else if (this.lexer.token() == Token.HAVING) {
/* 320 */       this.lexer.nextToken();
/*     */       
/* 322 */       SQLSelectGroupByClause groupBy = new SQLSelectGroupByClause();
/* 323 */       groupBy.setHaving(this.exprParser.expr());
/*     */       
/* 325 */       if (this.lexer.token() == Token.GROUP) {
/* 326 */         this.lexer.nextToken();
/* 327 */         accept(Token.BY);
/*     */         
/*     */         while (true) {
/* 330 */           SQLExpr item = parseGroupByItem();
/*     */           
/* 332 */           item.setParent((SQLObject)groupBy);
/* 333 */           groupBy.addItem(item);
/*     */           
/* 335 */           if (this.lexer.token() != Token.COMMA) {
/*     */             break;
/*     */           }
/*     */           
/* 339 */           this.lexer.nextToken();
/*     */         } 
/*     */       } 
/*     */       
/* 343 */       if (this.lexer.token() == Token.WITH) {
/* 344 */         this.lexer.nextToken();
/* 345 */         acceptIdentifier("ROLLUP");
/*     */         
/* 347 */         groupBy.setWithRollUp(true);
/*     */       } 
/*     */       
/* 350 */       if ("mysql".equals(getDbType()) && this.lexer
/* 351 */         .token() == Token.DESC) {
/* 352 */         this.lexer.nextToken();
/*     */       }
/*     */       
/* 355 */       queryBlock.setGroupBy(groupBy);
/*     */     } 
/*     */   }
/*     */   protected SQLExpr parseGroupByItem() {
/*     */     MySqlOrderingExpr mySqlOrderingExpr;
/* 360 */     SQLExpr item = this.exprParser.expr();
/*     */     
/* 362 */     if ("mysql".equals(getDbType())) {
/* 363 */       if (this.lexer.token() == Token.DESC) {
/* 364 */         this.lexer.nextToken();
/* 365 */         mySqlOrderingExpr = new MySqlOrderingExpr(item, SQLOrderingSpecification.DESC);
/* 366 */       } else if (this.lexer.token() == Token.ASC) {
/* 367 */         this.lexer.nextToken();
/* 368 */         mySqlOrderingExpr = new MySqlOrderingExpr((SQLExpr)mySqlOrderingExpr, SQLOrderingSpecification.ASC);
/*     */       } 
/*     */     }
/* 371 */     return (SQLExpr)mySqlOrderingExpr;
/*     */   }
/*     */   
/*     */   protected void parseSelectList(SQLSelectQueryBlock queryBlock) {
/* 375 */     List<SQLSelectItem> selectList = queryBlock.getSelectList();
/*     */     while (true) {
/* 377 */       SQLSelectItem selectItem = this.exprParser.parseSelectItem();
/* 378 */       selectList.add(selectItem);
/* 379 */       selectItem.setParent((SQLObject)queryBlock);
/*     */       
/* 381 */       if (this.lexer.token() != Token.COMMA) {
/*     */         break;
/*     */       }
/*     */       
/* 385 */       this.lexer.nextToken();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void parseFrom(SQLSelectQueryBlock queryBlock) {
/* 390 */     if (this.lexer.token() != Token.FROM) {
/*     */       return;
/*     */     }
/*     */     
/* 394 */     this.lexer.nextToken();
/*     */     
/* 396 */     queryBlock.setFrom(parseTableSource());
/*     */   }
/*     */   
/*     */   public SQLTableSource parseTableSource() {
/* 400 */     if (this.lexer.token() == Token.LPAREN) {
/* 401 */       SQLTableSource tableSource; this.lexer.nextToken();
/*     */       
/* 403 */       if (this.lexer.token() == Token.SELECT || this.lexer.token() == Token.WITH || this.lexer.token == Token.SEL) {
/*     */         
/* 405 */         SQLSelect select = select();
/* 406 */         accept(Token.RPAREN);
/* 407 */         SQLSelectQuery query = queryRest(select.getQuery());
/* 408 */         if (query instanceof SQLUnionQuery) {
/* 409 */           SQLUnionQueryTableSource sQLUnionQueryTableSource = new SQLUnionQueryTableSource((SQLUnionQuery)query);
/*     */         } else {
/* 411 */           SQLSubqueryTableSource sQLSubqueryTableSource = new SQLSubqueryTableSource(select);
/*     */         } 
/* 413 */       } else if (this.lexer.token() == Token.LPAREN) {
/* 414 */         tableSource = parseTableSource();
/* 415 */         accept(Token.RPAREN);
/*     */       } else {
/* 417 */         tableSource = parseTableSource();
/* 418 */         accept(Token.RPAREN);
/*     */       } 
/*     */       
/* 421 */       return parseTableSourceRest(tableSource);
/*     */     } 
/*     */     
/* 424 */     if (this.lexer.token() == Token.SELECT) {
/* 425 */       throw new ParserException("TODO");
/*     */     }
/*     */     
/* 428 */     SQLExprTableSource tableReference = new SQLExprTableSource();
/*     */     
/* 430 */     parseTableSourceQueryTableExpr(tableReference);
/*     */     
/* 432 */     SQLTableSource tableSrc = parseTableSourceRest((SQLTableSource)tableReference);
/*     */     
/* 434 */     if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
/* 435 */       tableSrc.addAfterComment(this.lexer.readAndResetComments());
/*     */     }
/*     */     
/* 438 */     return tableSrc;
/*     */   }
/*     */   
/*     */   protected void parseTableSourceQueryTableExpr(SQLExprTableSource tableReference) {
/* 442 */     if (this.lexer.token() == Token.LITERAL_ALIAS || this.lexer.token() == Token.IDENTIFIED || this.lexer
/* 443 */       .token() == Token.LITERAL_CHARS) {
/* 444 */       tableReference.setExpr((SQLExpr)this.exprParser.name());
/*     */       
/*     */       return;
/*     */     } 
/* 448 */     tableReference.setExpr(expr());
/*     */   }
/*     */   
/*     */   protected SQLTableSource parseTableSourceRest(SQLTableSource tableSource) {
/* 452 */     if ((tableSource.getAlias() == null || tableSource.getAlias().length() == 0) && 
/* 453 */       this.lexer.token() != Token.LEFT && this.lexer.token() != Token.RIGHT && this.lexer.token() != Token.FULL &&
/* 454 */       !identifierEquals("STRAIGHT_JOIN") && !identifierEquals("CROSS") && this.lexer.token != Token.OUTER) {
/* 455 */       String alias = as();
/* 456 */       if (alias != null) {
/* 457 */         tableSource.setAlias(alias);
/* 458 */         return parseTableSourceRest(tableSource);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 463 */     SQLJoinTableSource.JoinType joinType = null;
/*     */     
/* 465 */     if (this.lexer.token() == Token.LEFT) {
/* 466 */       this.lexer.nextToken();
/* 467 */       if (this.lexer.token() == Token.OUTER) {
/* 468 */         this.lexer.nextToken();
/*     */       }
/*     */       
/* 471 */       accept(Token.JOIN);
/* 472 */       joinType = SQLJoinTableSource.JoinType.LEFT_OUTER_JOIN;
/* 473 */     } else if (this.lexer.token() == Token.RIGHT) {
/* 474 */       this.lexer.nextToken();
/* 475 */       if (this.lexer.token() == Token.OUTER) {
/* 476 */         this.lexer.nextToken();
/*     */       }
/* 478 */       accept(Token.JOIN);
/* 479 */       joinType = SQLJoinTableSource.JoinType.RIGHT_OUTER_JOIN;
/* 480 */     } else if (this.lexer.token() == Token.FULL) {
/* 481 */       this.lexer.nextToken();
/* 482 */       if (this.lexer.token() == Token.OUTER) {
/* 483 */         this.lexer.nextToken();
/*     */       }
/* 485 */       accept(Token.JOIN);
/* 486 */       joinType = SQLJoinTableSource.JoinType.FULL_OUTER_JOIN;
/* 487 */     } else if (this.lexer.token() == Token.INNER) {
/* 488 */       this.lexer.nextToken();
/* 489 */       accept(Token.JOIN);
/* 490 */       joinType = SQLJoinTableSource.JoinType.INNER_JOIN;
/* 491 */     } else if (this.lexer.token() == Token.JOIN) {
/* 492 */       this.lexer.nextToken();
/* 493 */       joinType = SQLJoinTableSource.JoinType.JOIN;
/* 494 */     } else if (this.lexer.token() == Token.COMMA) {
/* 495 */       this.lexer.nextToken();
/* 496 */       joinType = SQLJoinTableSource.JoinType.COMMA;
/* 497 */     } else if (identifierEquals("STRAIGHT_JOIN")) {
/* 498 */       this.lexer.nextToken();
/* 499 */       joinType = SQLJoinTableSource.JoinType.STRAIGHT_JOIN;
/* 500 */     } else if (identifierEquals("CROSS")) {
/* 501 */       this.lexer.nextToken();
/* 502 */       if (this.lexer.token() == Token.JOIN) {
/* 503 */         this.lexer.nextToken();
/* 504 */         joinType = SQLJoinTableSource.JoinType.CROSS_JOIN;
/* 505 */       } else if (identifierEquals("APPLY")) {
/* 506 */         this.lexer.nextToken();
/* 507 */         joinType = SQLJoinTableSource.JoinType.CROSS_APPLY;
/*     */       } 
/* 509 */     } else if (this.lexer.token() == Token.OUTER) {
/* 510 */       this.lexer.nextToken();
/* 511 */       if (identifierEquals("APPLY")) {
/* 512 */         this.lexer.nextToken();
/* 513 */         joinType = SQLJoinTableSource.JoinType.OUTER_APPLY;
/*     */       } 
/*     */     } 
/*     */     
/* 517 */     if (joinType != null) {
/* 518 */       SQLJoinTableSource join = new SQLJoinTableSource();
/* 519 */       join.setLeft(tableSource);
/* 520 */       join.setJoinType(joinType);
/* 521 */       join.setRight(parseTableSource());
/*     */       
/* 523 */       if (this.lexer.token() == Token.ON) {
/* 524 */         this.lexer.nextToken();
/* 525 */         join.setCondition(expr());
/* 526 */       } else if (identifierEquals("USING")) {
/* 527 */         this.lexer.nextToken();
/* 528 */         if (this.lexer.token() == Token.LPAREN) {
/* 529 */           this.lexer.nextToken();
/* 530 */           this.exprParser.exprList(join.getUsing(), (SQLObject)join);
/* 531 */           accept(Token.RPAREN);
/*     */         } else {
/* 533 */           join.getUsing().add(expr());
/*     */         } 
/*     */       } 
/*     */       
/* 537 */       return parseTableSourceRest((SQLTableSource)join);
/*     */     } 
/*     */     
/* 540 */     return tableSource;
/*     */   }
/*     */   
/*     */   public SQLExpr expr() {
/* 544 */     return this.exprParser.expr();
/*     */   }
/*     */   
/*     */   public SQLOrderBy parseOrderBy() {
/* 548 */     return this.exprParser.parseOrderBy();
/*     */   }
/*     */   
/*     */   public void acceptKeyword(String ident) {
/* 552 */     if (this.lexer.token() == Token.IDENTIFIER && ident.equalsIgnoreCase(this.lexer.stringVal())) {
/* 553 */       this.lexer.nextToken();
/*     */     } else {
/* 555 */       setErrorEndPos(this.lexer.pos());
/* 556 */       throw new ParserException("syntax error, expect " + ident + ", actual " + this.lexer.token());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void parseFetchClause(SQLSelectQueryBlock queryBlock) {
/* 561 */     if (this.lexer.token() == Token.LIMIT) {
/* 562 */       SQLLimit limit = this.exprParser.parseLimit();
/* 563 */       queryBlock.setLimit(limit);
/*     */       
/*     */       return;
/*     */     } 
/* 567 */     if (identifierEquals("OFFSET") || this.lexer.token() == Token.OFFSET) {
/* 568 */       this.lexer.nextToken();
/* 569 */       SQLExpr offset = this.exprParser.primary();
/* 570 */       queryBlock.setOffset(offset);
/* 571 */       if (identifierEquals("ROW") || identifierEquals("ROWS")) {
/* 572 */         this.lexer.nextToken();
/*     */       }
/*     */     } 
/*     */     
/* 576 */     if (this.lexer.token() == Token.FETCH) {
/* 577 */       this.lexer.nextToken();
/* 578 */       if (this.lexer.token() == Token.FIRST) {
/* 579 */         this.lexer.nextToken();
/*     */       } else {
/* 581 */         acceptIdentifier("FIRST");
/*     */       } 
/* 583 */       SQLExpr first = this.exprParser.primary();
/* 584 */       queryBlock.setFirst(first);
/* 585 */       if (identifierEquals("ROW") || identifierEquals("ROWS")) {
/* 586 */         this.lexer.nextToken();
/*     */       }
/*     */       
/* 589 */       if (this.lexer.token() == Token.ONLY) {
/* 590 */         this.lexer.nextToken();
/*     */       } else {
/* 592 */         acceptIdentifier("ONLY");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\parser\SQLSelectParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */