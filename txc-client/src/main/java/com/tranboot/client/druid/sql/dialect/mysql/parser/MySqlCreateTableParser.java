/*     */ package com.tranboot.client.druid.sql.dialect.mysql.parser;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.*;
import com.tranboot.client.druid.sql.ast.expr.SQLIntegerExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNumberExpr;
import com.tranboot.client.druid.sql.ast.statement.*;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MySqlKey;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MySqlUnique;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MysqlForeignKey;
import com.tranboot.client.druid.sql.dialect.mysql.ast.expr.MySqlOrderingExpr;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.*;
import com.tranboot.client.druid.sql.parser.ParserException;
import com.tranboot.client.druid.sql.parser.SQLCreateTableParser;
import com.tranboot.client.druid.sql.parser.SQLExprParser;
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
/*     */ public class MySqlCreateTableParser
/*     */   extends SQLCreateTableParser
/*     */ {
/*     */   public MySqlCreateTableParser(String sql) {
/*  59 */     super(new MySqlExprParser(sql));
/*     */   }
/*     */   
/*     */   public MySqlCreateTableParser(SQLExprParser exprParser) {
/*  63 */     super(exprParser);
/*     */   }
/*     */   
/*     */   public SQLCreateTableStatement parseCrateTable() {
/*  67 */     return (SQLCreateTableStatement)parseCrateTable(true);
/*     */   }
/*     */   
/*     */   public MySqlExprParser getExprParser() {
/*  71 */     return (MySqlExprParser)this.exprParser;
/*     */   }
/*     */   
/*     */   public MySqlCreateTableStatement parseCrateTable(boolean acceptCreate) {
/*  75 */     if (acceptCreate) {
/*  76 */       accept(Token.CREATE);
/*     */     }
/*  78 */     MySqlCreateTableStatement stmt = new MySqlCreateTableStatement();
/*     */     
/*  80 */     if (identifierEquals("TEMPORARY")) {
/*  81 */       this.lexer.nextToken();
/*  82 */       stmt.setType(SQLCreateTableStatement.Type.GLOBAL_TEMPORARY);
/*     */     } 
/*     */     
/*  85 */     accept(Token.TABLE);
/*     */     
/*  87 */     if (this.lexer.token() == Token.IF || identifierEquals("IF")) {
/*  88 */       this.lexer.nextToken();
/*  89 */       accept(Token.NOT);
/*  90 */       accept(Token.EXISTS);
/*     */       
/*  92 */       stmt.setIfNotExiists(true);
/*     */     } 
/*     */     
/*  95 */     stmt.setName(this.exprParser.name());
/*     */     
/*  97 */     if (this.lexer.token() == Token.LIKE) {
/*  98 */       this.lexer.nextToken();
/*  99 */       SQLName name = this.exprParser.name();
/* 100 */       stmt.setLike(name);
/*     */     } 
/*     */     
/* 103 */     if (this.lexer.token() == Token.LPAREN) {
/* 104 */       this.lexer.nextToken();
/*     */       
/* 106 */       if (this.lexer.token() == Token.LIKE) {
/* 107 */         this.lexer.nextToken();
/* 108 */         SQLName name = this.exprParser.name();
/* 109 */         stmt.setLike(name);
/*     */       } else {
/*     */         while (true) {
/* 112 */           if (this.lexer.token() == Token.IDENTIFIER || this.lexer
/* 113 */             .token() == Token.LITERAL_CHARS) {
/* 114 */             SQLColumnDefinition column = this.exprParser.parseColumn();
/* 115 */             stmt.getTableElementList().add(column);
/* 116 */           } else if (this.lexer.token() == Token.CONSTRAINT || this.lexer
/* 117 */             .token() == Token.PRIMARY || this.lexer
/* 118 */             .token() == Token.UNIQUE) {
/* 119 */             SQLTableConstraint constraint = parseConstraint();
/* 120 */             stmt.getTableElementList().add(constraint);
/* 121 */           } else if (this.lexer.token() == Token.INDEX) {
/* 122 */             this.lexer.nextToken();
/*     */             
/* 124 */             MySqlTableIndex idx = new MySqlTableIndex();
/*     */             
/* 126 */             if (this.lexer.token() == Token.IDENTIFIER &&
/* 127 */               !"USING".equalsIgnoreCase(this.lexer.stringVal())) {
/* 128 */               idx.setName(this.exprParser.name());
/*     */             }
/*     */ 
/*     */             
/* 132 */             if (identifierEquals("USING")) {
/* 133 */               this.lexer.nextToken();
/* 134 */               idx.setIndexType(this.lexer.stringVal());
/* 135 */               this.lexer.nextToken();
/*     */             } 
/*     */             
/* 138 */             accept(Token.LPAREN);
/*     */             while (true) {
/* 140 */               idx.addColumn(this.exprParser.expr());
/* 141 */               if (this.lexer.token() != Token.COMMA) {
/*     */                 break;
/*     */               }
/* 144 */               this.lexer.nextToken();
/*     */             } 
/*     */             
/* 147 */             accept(Token.RPAREN);
/*     */             
/* 149 */             if (identifierEquals("USING")) {
/* 150 */               this.lexer.nextToken();
/* 151 */               idx.setIndexType(this.lexer.stringVal());
/* 152 */               this.lexer.nextToken();
/*     */             } 
/*     */             
/* 155 */             stmt.getTableElementList().add(idx);
/* 156 */           } else if (this.lexer.token() == Token.KEY) {
/* 157 */             stmt.getTableElementList().add(parseConstraint());
/* 158 */           } else if (this.lexer.token() == Token.PRIMARY) {
/* 159 */             SQLTableConstraint pk = parseConstraint();
/* 160 */             pk.setParent((SQLObject)stmt);
/* 161 */             stmt.getTableElementList().add(pk);
/* 162 */           } else if (this.lexer.token() == Token.FOREIGN) {
/* 163 */             MysqlForeignKey mysqlForeignKey = getExprParser().parseForeignKey();
/* 164 */             mysqlForeignKey.setParent((SQLObject)stmt);
/* 165 */             stmt.getTableElementList().add(mysqlForeignKey);
/* 166 */           } else if (this.lexer.token() == Token.CHECK) {
/* 167 */             SQLCheck check = this.exprParser.parseCheck();
/* 168 */             stmt.getTableElementList().add(check);
/*     */           } else {
/* 170 */             SQLColumnDefinition column = this.exprParser.parseColumn();
/* 171 */             stmt.getTableElementList().add(column);
/*     */           } 
/*     */           
/* 174 */           if (this.lexer.token() != Token.COMMA) {
/*     */             break;
/*     */           }
/* 177 */           this.lexer.nextToken();
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 182 */       accept(Token.RPAREN);
/*     */     } 
/*     */     
/*     */     while (true) {
/* 186 */       while (identifierEquals("ENGINE")) {
/* 187 */         this.lexer.nextToken();
/* 188 */         if (this.lexer.token() == Token.EQ) {
/* 189 */           this.lexer.nextToken();
/*     */         }
/* 191 */         stmt.getTableOptions().put("ENGINE", this.exprParser.expr());
/*     */       } 
/*     */ 
/*     */       
/* 195 */       if (identifierEquals("AUTO_INCREMENT")) {
/* 196 */         this.lexer.nextToken();
/* 197 */         if (this.lexer.token() == Token.EQ) {
/* 198 */           this.lexer.nextToken();
/*     */         }
/* 200 */         stmt.getTableOptions().put("AUTO_INCREMENT", this.exprParser.expr());
/*     */         
/*     */         continue;
/*     */       } 
/* 204 */       if (identifierEquals("AVG_ROW_LENGTH")) {
/* 205 */         this.lexer.nextToken();
/* 206 */         if (this.lexer.token() == Token.EQ) {
/* 207 */           this.lexer.nextToken();
/*     */         }
/* 209 */         stmt.getTableOptions().put("AVG_ROW_LENGTH", this.exprParser.expr());
/*     */         
/*     */         continue;
/*     */       } 
/* 213 */       if (this.lexer.token() == Token.DEFAULT) {
/* 214 */         this.lexer.nextToken();
/* 215 */         parseTableOptionCharsetOrCollate(stmt);
/*     */         
/*     */         continue;
/*     */       } 
/* 219 */       if (parseTableOptionCharsetOrCollate(stmt)) {
/*     */         continue;
/*     */       }
/*     */       
/* 223 */       if (identifierEquals("CHECKSUM")) {
/* 224 */         this.lexer.nextToken();
/* 225 */         if (this.lexer.token() == Token.EQ) {
/* 226 */           this.lexer.nextToken();
/*     */         }
/* 228 */         stmt.getTableOptions().put("CHECKSUM", this.exprParser.expr());
/*     */         
/*     */         continue;
/*     */       } 
/* 232 */       if (this.lexer.token() == Token.COMMENT) {
/* 233 */         this.lexer.nextToken();
/* 234 */         if (this.lexer.token() == Token.EQ) {
/* 235 */           this.lexer.nextToken();
/*     */         }
/* 237 */         stmt.getTableOptions().put("COMMENT", this.exprParser.expr());
/*     */         
/*     */         continue;
/*     */       } 
/* 241 */       if (identifierEquals("CONNECTION")) {
/* 242 */         this.lexer.nextToken();
/* 243 */         if (this.lexer.token() == Token.EQ) {
/* 244 */           this.lexer.nextToken();
/*     */         }
/* 246 */         stmt.getTableOptions().put("CONNECTION", this.exprParser.expr());
/*     */         
/*     */         continue;
/*     */       } 
/* 250 */       if (identifierEquals("DATA")) {
/* 251 */         this.lexer.nextToken();
/* 252 */         acceptIdentifier("DIRECTORY");
/* 253 */         if (this.lexer.token() == Token.EQ) {
/* 254 */           this.lexer.nextToken();
/*     */         }
/* 256 */         stmt.getTableOptions().put("DATA DIRECTORY", this.exprParser.expr());
/*     */         
/*     */         continue;
/*     */       } 
/* 260 */       if (identifierEquals("DELAY_KEY_WRITE")) {
/* 261 */         this.lexer.nextToken();
/* 262 */         if (this.lexer.token() == Token.EQ) {
/* 263 */           this.lexer.nextToken();
/*     */         }
/* 265 */         stmt.getTableOptions().put("DELAY_KEY_WRITE", this.exprParser.expr());
/*     */         
/*     */         continue;
/*     */       } 
/* 269 */       if (identifierEquals("INDEX")) {
/* 270 */         this.lexer.nextToken();
/* 271 */         acceptIdentifier("DIRECTORY");
/* 272 */         if (this.lexer.token() == Token.EQ) {
/* 273 */           this.lexer.nextToken();
/*     */         }
/* 275 */         stmt.getTableOptions().put("INDEX DIRECTORY", this.exprParser.expr());
/*     */         
/*     */         continue;
/*     */       } 
/* 279 */       if (identifierEquals("INSERT_METHOD")) {
/* 280 */         this.lexer.nextToken();
/* 281 */         if (this.lexer.token() == Token.EQ) {
/* 282 */           this.lexer.nextToken();
/*     */         }
/* 284 */         stmt.getTableOptions().put("INSERT_METHOD", this.exprParser.expr());
/*     */         
/*     */         continue;
/*     */       } 
/* 288 */       if (identifierEquals("KEY_BLOCK_SIZE")) {
/* 289 */         this.lexer.nextToken();
/* 290 */         if (this.lexer.token() == Token.EQ) {
/* 291 */           this.lexer.nextToken();
/*     */         }
/* 293 */         stmt.getTableOptions().put("KEY_BLOCK_SIZE", this.exprParser.expr());
/*     */         
/*     */         continue;
/*     */       } 
/* 297 */       if (identifierEquals("MAX_ROWS")) {
/* 298 */         this.lexer.nextToken();
/* 299 */         if (this.lexer.token() == Token.EQ) {
/* 300 */           this.lexer.nextToken();
/*     */         }
/* 302 */         stmt.getTableOptions().put("MAX_ROWS", this.exprParser.expr());
/*     */         
/*     */         continue;
/*     */       } 
/* 306 */       if (identifierEquals("MIN_ROWS")) {
/* 307 */         this.lexer.nextToken();
/* 308 */         if (this.lexer.token() == Token.EQ) {
/* 309 */           this.lexer.nextToken();
/*     */         }
/* 311 */         stmt.getTableOptions().put("MIN_ROWS", this.exprParser.expr());
/*     */         
/*     */         continue;
/*     */       } 
/* 315 */       if (identifierEquals("PACK_KEYS")) {
/* 316 */         this.lexer.nextToken();
/* 317 */         if (this.lexer.token() == Token.EQ) {
/* 318 */           this.lexer.nextToken();
/*     */         }
/* 320 */         stmt.getTableOptions().put("PACK_KEYS", this.exprParser.expr());
/*     */         
/*     */         continue;
/*     */       } 
/* 324 */       if (identifierEquals("PASSWORD")) {
/* 325 */         this.lexer.nextToken();
/* 326 */         if (this.lexer.token() == Token.EQ) {
/* 327 */           this.lexer.nextToken();
/*     */         }
/* 329 */         stmt.getTableOptions().put("PASSWORD", this.exprParser.expr());
/*     */         
/*     */         continue;
/*     */       } 
/* 333 */       if (identifierEquals("ROW_FORMAT")) {
/* 334 */         this.lexer.nextToken();
/* 335 */         if (this.lexer.token() == Token.EQ) {
/* 336 */           this.lexer.nextToken();
/*     */         }
/* 338 */         stmt.getTableOptions().put("ROW_FORMAT", this.exprParser.expr());
/*     */         
/*     */         continue;
/*     */       } 
/* 342 */       if (identifierEquals("STATS_AUTO_RECALC")) {
/* 343 */         this.lexer.nextToken();
/* 344 */         if (this.lexer.token() == Token.EQ) {
/* 345 */           this.lexer.nextToken();
/*     */         }
/*     */         
/* 348 */         stmt.getTableOptions().put("STATS_AUTO_RECALC", this.exprParser.expr());
/*     */         
/*     */         continue;
/*     */       } 
/* 352 */       if (identifierEquals("STATS_PERSISTENT")) {
/* 353 */         this.lexer.nextToken();
/* 354 */         if (this.lexer.token() == Token.EQ) {
/* 355 */           this.lexer.nextToken();
/*     */         }
/*     */         
/* 358 */         stmt.getTableOptions().put("STATS_PERSISTENT", this.exprParser.expr());
/*     */         
/*     */         continue;
/*     */       } 
/* 362 */       if (identifierEquals("STATS_SAMPLE_PAGES")) {
/* 363 */         this.lexer.nextToken();
/* 364 */         if (this.lexer.token() == Token.EQ) {
/* 365 */           this.lexer.nextToken();
/*     */         }
/*     */         
/* 368 */         stmt.getTableOptions().put("STATS_SAMPLE_PAGES", this.exprParser.expr());
/*     */         
/*     */         continue;
/*     */       } 
/* 372 */       if (this.lexer.token() == Token.UNION) {
/* 373 */         this.lexer.nextToken();
/* 374 */         if (this.lexer.token() == Token.EQ) {
/* 375 */           this.lexer.nextToken();
/*     */         }
/*     */         
/* 378 */         accept(Token.LPAREN);
/* 379 */         SQLTableSource tableSrc = createSQLSelectParser().parseTableSource();
/* 380 */         stmt.getTableOptions().put("UNION", tableSrc);
/* 381 */         accept(Token.RPAREN);
/*     */         
/*     */         continue;
/*     */       } 
/* 385 */       if (this.lexer.token() == Token.TABLESPACE) {
/* 386 */         this.lexer.nextToken();
/*     */         
/* 388 */         MySqlCreateTableStatement.TableSpaceOption option = new MySqlCreateTableStatement.TableSpaceOption();
/* 389 */         option.setName(this.exprParser.name());
/*     */         
/* 391 */         if (identifierEquals("STORAGE")) {
/* 392 */           this.lexer.nextToken();
/* 393 */           option.setStorage((SQLExpr)this.exprParser.name());
/*     */         } 
/*     */         
/* 396 */         stmt.getTableOptions().put("TABLESPACE", option);
/*     */         
/*     */         continue;
/*     */       } 
/* 400 */       if (identifierEquals("TABLEGROUP")) {
/* 401 */         this.lexer.nextToken();
/*     */         
/* 403 */         SQLName tableGroup = this.exprParser.name();
/* 404 */         stmt.setTableGroup(tableGroup);
/*     */         
/*     */         continue;
/*     */       } 
/* 408 */       if (identifierEquals("TYPE")) {
/* 409 */         this.lexer.nextToken();
/* 410 */         accept(Token.EQ);
/* 411 */         stmt.getTableOptions().put("TYPE", this.exprParser.expr());
/* 412 */         this.lexer.nextToken();
/*     */         
/*     */         continue;
/*     */       } 
/* 416 */       if (this.lexer.token() == Token.PARTITION) {
/* 417 */         SQLPartitionByList sQLPartitionByList; this.lexer.nextToken();
/* 418 */         accept(Token.BY);
/*     */ 
/*     */ 
/*     */         
/* 422 */         boolean linera = false;
/* 423 */         if (identifierEquals("LINEAR")) {
/* 424 */           this.lexer.nextToken();
/* 425 */           linera = true;
/*     */         } 
/*     */         
/* 428 */         if (this.lexer.token() == Token.KEY) {
/* 429 */           MySqlPartitionByKey clause = new MySqlPartitionByKey();
/* 430 */           this.lexer.nextToken();
/*     */           
/* 432 */           if (linera) {
/* 433 */             clause.setLinear(true);
/*     */           }
/*     */           
/* 436 */           accept(Token.LPAREN);
/* 437 */           if (this.lexer.token() != Token.RPAREN) {
/*     */             while (true) {
/* 439 */               clause.addColumn(this.exprParser.name());
/* 440 */               if (this.lexer.token() == Token.COMMA) {
/* 441 */                 this.lexer.nextToken();
/*     */                 continue;
/*     */               } 
/*     */               break;
/*     */             } 
/*     */           }
/* 447 */           accept(Token.RPAREN);
/*     */           
/* 449 */           MySqlPartitionByKey mySqlPartitionByKey1 = clause;
/*     */           
/* 451 */           partitionClauseRest((SQLPartitionBy)clause);
/* 452 */         } else if (identifierEquals("HASH")) {
/* 453 */           this.lexer.nextToken();
/* 454 */           SQLPartitionByHash clause = new SQLPartitionByHash();
/*     */           
/* 456 */           if (linera) {
/* 457 */             clause.setLinear(true);
/*     */           }
/*     */           
/* 460 */           if (this.lexer.token() == Token.KEY) {
/* 461 */             this.lexer.nextToken();
/* 462 */             clause.setKey(true);
/*     */           } 
/*     */           
/* 465 */           accept(Token.LPAREN);
/* 466 */           clause.setExpr(this.exprParser.expr());
/* 467 */           accept(Token.RPAREN);
/* 468 */           SQLPartitionByHash sQLPartitionByHash1 = clause;
/*     */           
/* 470 */           partitionClauseRest((SQLPartitionBy)clause);
/*     */         }
/* 472 */         else if (identifierEquals("RANGE")) {
/* 473 */           SQLPartitionByRange clause = partitionByRange();
/* 474 */           SQLPartitionByRange sQLPartitionByRange1 = clause;
/*     */           
/* 476 */           partitionClauseRest((SQLPartitionBy)clause);
/*     */         }
/* 478 */         else if (identifierEquals("LIST")) {
/* 479 */           this.lexer.nextToken();
/* 480 */           SQLPartitionByList clause = new SQLPartitionByList();
/*     */           
/* 482 */           if (this.lexer.token() == Token.LPAREN) {
/* 483 */             this.lexer.nextToken();
/* 484 */             clause.setExpr(this.exprParser.expr());
/* 485 */             accept(Token.RPAREN);
/*     */           } else {
/* 487 */             acceptIdentifier("COLUMNS");
/* 488 */             accept(Token.LPAREN);
/*     */             while (true) {
/* 490 */               clause.addColumn(this.exprParser.name());
/* 491 */               if (this.lexer.token() == Token.COMMA) {
/* 492 */                 this.lexer.nextToken();
/*     */                 continue;
/*     */               } 
/*     */               break;
/*     */             } 
/* 497 */             accept(Token.RPAREN);
/*     */           } 
/* 499 */           sQLPartitionByList = clause;
/*     */           
/* 501 */           partitionClauseRest((SQLPartitionBy)clause);
/*     */         } else {
/* 503 */           throw new ParserException("TODO " + this.lexer.token() + " " + this.lexer.stringVal());
/*     */         } 
/*     */         
/* 506 */         if (this.lexer.token() == Token.LPAREN) {
/* 507 */           this.lexer.nextToken();
/*     */           while (true) {
/* 509 */             SQLPartition partitionDef = getExprParser().parsePartition();
/*     */             
/* 511 */             sQLPartitionByList.addPartition(partitionDef);
/*     */             
/* 513 */             if (this.lexer.token() == Token.COMMA) {
/* 514 */               this.lexer.nextToken();
/*     */               
/*     */               continue;
/*     */             } 
/*     */             break;
/*     */           } 
/* 520 */           accept(Token.RPAREN);
/*     */         } 
/*     */         
/* 523 */         stmt.setPartitioning((SQLPartitionBy)sQLPartitionByList);
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/*     */       break;
/*     */     } 
/*     */     
/* 531 */     if (this.lexer.token() == Token.ON) {
/* 532 */       throw new ParserException("TODO");
/*     */     }
/*     */     
/* 535 */     if (this.lexer.token() == Token.AS) {
/* 536 */       this.lexer.nextToken();
/*     */     }
/*     */     
/* 539 */     if (this.lexer.token() == Token.SELECT) {
/* 540 */       SQLSelect query = (new MySqlSelectParser(this.exprParser)).select();
/* 541 */       stmt.setSelect(query);
/*     */     } 
/*     */     
/* 544 */     while (this.lexer.token() == Token.HINT) {
/* 545 */       this.exprParser.parseHints(stmt.getOptionHints());
/*     */     }
/* 547 */     return stmt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SQLPartitionByRange partitionByRange() {
/* 553 */     acceptIdentifier("RANGE");
/*     */     
/* 555 */     SQLPartitionByRange clause = new SQLPartitionByRange();
/*     */     
/* 557 */     if (this.lexer.token() == Token.LPAREN) {
/* 558 */       this.lexer.nextToken();
/* 559 */       clause.setExpr(this.exprParser.expr());
/* 560 */       accept(Token.RPAREN);
/*     */     } else {
/* 562 */       acceptIdentifier("COLUMNS");
/* 563 */       accept(Token.LPAREN);
/*     */       while (true) {
/* 565 */         clause.addColumn(this.exprParser.name());
/* 566 */         if (this.lexer.token() == Token.COMMA) {
/* 567 */           this.lexer.nextToken();
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/* 572 */       accept(Token.RPAREN);
/*     */     } 
/* 574 */     return clause;
/*     */   }
/*     */   
/*     */   protected void partitionClauseRest(SQLPartitionBy clause) {
/* 578 */     if (identifierEquals("PARTITIONS")) {
/* 579 */       this.lexer.nextToken();
/*     */       
/* 581 */       SQLIntegerExpr countExpr = this.exprParser.integerExpr();
/* 582 */       clause.setPartitionsCount((SQLExpr)countExpr);
/*     */     } 
/*     */     
/* 585 */     if (this.lexer.token() == Token.PARTITION) {
/* 586 */       this.lexer.nextToken();
/*     */       
/* 588 */       if (identifierEquals("NUM")) {
/* 589 */         this.lexer.nextToken();
/*     */       }
/*     */       
/* 592 */       clause.setPartitionsCount(this.exprParser.expr());
/*     */       
/* 594 */       clause.putAttribute("ads.partition", Boolean.TRUE);
/*     */     } 
/*     */     
/* 597 */     if (identifierEquals("SUBPARTITION")) {
/* 598 */       MySqlSubPartitionByList mySqlSubPartitionByList; this.lexer.nextToken();
/* 599 */       accept(Token.BY);
/*     */       
/* 601 */       SQLSubPartitionBy subPartitionByClause = null;
/*     */       
/* 603 */       boolean linear = false;
/* 604 */       if (identifierEquals("LINEAR")) {
/* 605 */         this.lexer.nextToken();
/* 606 */         linear = true;
/*     */       } 
/*     */       
/* 609 */       if (this.lexer.token() == Token.KEY) {
/* 610 */         MySqlSubPartitionByKey subPartitionKey = new MySqlSubPartitionByKey();
/* 611 */         this.lexer.nextToken();
/*     */         
/* 613 */         if (linear) {
/* 614 */           clause.setLinear(true);
/*     */         }
/*     */         
/* 617 */         accept(Token.LPAREN);
/*     */         while (true) {
/* 619 */           subPartitionKey.addColumn(this.exprParser.name());
/* 620 */           if (this.lexer.token() == Token.COMMA) {
/* 621 */             this.lexer.nextToken();
/*     */             continue;
/*     */           } 
/*     */           break;
/*     */         } 
/* 626 */         accept(Token.RPAREN);
/*     */         
/* 628 */         MySqlSubPartitionByKey mySqlSubPartitionByKey1 = subPartitionKey;
/*     */       }
/* 630 */       else if (identifierEquals("HASH")) {
/* 631 */         this.lexer.nextToken();
/* 632 */         SQLSubPartitionByHash subPartitionHash = new SQLSubPartitionByHash();
/*     */         
/* 634 */         if (linear) {
/* 635 */           clause.setLinear(true);
/*     */         }
/*     */         
/* 638 */         if (this.lexer.token() == Token.KEY) {
/* 639 */           this.lexer.nextToken();
/* 640 */           subPartitionHash.setKey(true);
/*     */         } 
/*     */         
/* 643 */         accept(Token.LPAREN);
/* 644 */         subPartitionHash.setExpr(this.exprParser.expr());
/* 645 */         accept(Token.RPAREN);
/* 646 */         SQLSubPartitionByHash sQLSubPartitionByHash1 = subPartitionHash;
/*     */       }
/* 648 */       else if (identifierEquals("LIST")) {
/* 649 */         this.lexer.nextToken();
/* 650 */         MySqlSubPartitionByList subPartitionList = new MySqlSubPartitionByList();
/*     */         
/* 652 */         if (this.lexer.token() == Token.LPAREN) {
/* 653 */           this.lexer.nextToken();
/* 654 */           SQLExpr expr = this.exprParser.expr();
/*     */           
/* 656 */           if (expr instanceof com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr && (identifierEquals("bigint") || identifierEquals("long"))) {
/* 657 */             String dataType = this.lexer.stringVal();
/* 658 */             this.lexer.nextToken();
/*     */             
/* 660 */             SQLColumnDefinition column = this.exprParser.createColumnDefinition();
/* 661 */             column.setName((SQLName)expr);
/* 662 */             column.setDataType((SQLDataType)new SQLDataTypeImpl(dataType));
/* 663 */             subPartitionList.addColumn(column);
/*     */             
/* 665 */             subPartitionList.putAttribute("ads.subPartitionList", Boolean.TRUE);
/*     */           } else {
/* 667 */             subPartitionList.setExpr(expr);
/*     */           } 
/* 669 */           accept(Token.RPAREN);
/*     */         } else {
/* 671 */           acceptIdentifier("COLUMNS");
/* 672 */           accept(Token.LPAREN);
/*     */           while (true) {
/* 674 */             subPartitionList.addColumn(this.exprParser.parseColumn());
/* 675 */             if (this.lexer.token() == Token.COMMA) {
/* 676 */               this.lexer.nextToken();
/*     */               continue;
/*     */             } 
/*     */             break;
/*     */           } 
/* 681 */           accept(Token.RPAREN);
/*     */         } 
/* 683 */         mySqlSubPartitionByList = subPartitionList;
/*     */       } 
/*     */       
/* 686 */       if (identifierEquals("SUBPARTITION")) {
/* 687 */         this.lexer.nextToken();
/* 688 */         acceptIdentifier("OPTIONS");
/* 689 */         accept(Token.LPAREN);
/*     */         
/* 691 */         SQLAssignItem option = this.exprParser.parseAssignItem();
/* 692 */         accept(Token.RPAREN);
/*     */         
/* 694 */         option.setParent((SQLObject)mySqlSubPartitionByList);
/*     */         
/* 696 */         mySqlSubPartitionByList.getOptions().add(option);
/*     */       } 
/*     */       
/* 699 */       if (identifierEquals("SUBPARTITIONS")) {
/* 700 */         this.lexer.nextToken();
/* 701 */         Number intValue = this.lexer.integerValue();
/* 702 */         SQLNumberExpr numExpr = new SQLNumberExpr(intValue);
/* 703 */         mySqlSubPartitionByList.setSubPartitionsCount((SQLExpr)numExpr);
/* 704 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 707 */       if (mySqlSubPartitionByList != null) {
/* 708 */         mySqlSubPartitionByList.setLinear(linear);
/*     */         
/* 710 */         clause.setSubPartitionBy((SQLSubPartitionBy)mySqlSubPartitionByList);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean parseTableOptionCharsetOrCollate(MySqlCreateTableStatement stmt) {
/* 716 */     if (identifierEquals("CHARACTER")) {
/* 717 */       this.lexer.nextToken();
/* 718 */       accept(Token.SET);
/* 719 */       if (this.lexer.token() == Token.EQ) {
/* 720 */         this.lexer.nextToken();
/*     */       }
/* 722 */       stmt.getTableOptions().put("CHARACTER SET", this.exprParser.expr());
/* 723 */       return true;
/*     */     } 
/*     */     
/* 726 */     if (identifierEquals("CHARSET")) {
/* 727 */       this.lexer.nextToken();
/* 728 */       if (this.lexer.token() == Token.EQ) {
/* 729 */         this.lexer.nextToken();
/*     */       }
/* 731 */       stmt.getTableOptions().put("CHARSET", this.exprParser.expr());
/* 732 */       return true;
/*     */     } 
/*     */     
/* 735 */     if (identifierEquals("COLLATE")) {
/* 736 */       this.lexer.nextToken();
/* 737 */       if (this.lexer.token() == Token.EQ) {
/* 738 */         this.lexer.nextToken();
/*     */       }
/* 740 */       stmt.getTableOptions().put("COLLATE", this.exprParser.expr());
/* 741 */       return true;
/*     */     } 
/*     */     
/* 744 */     return false;
/*     */   }
/*     */   
/*     */   protected SQLTableConstraint parseConstraint() {
/* 748 */     SQLName name = null;
/* 749 */     boolean hasConstaint = false;
/* 750 */     if (this.lexer.token() == Token.CONSTRAINT) {
/* 751 */       hasConstaint = true;
/* 752 */       this.lexer.nextToken();
/*     */     } 
/*     */     
/* 755 */     if (this.lexer.token() == Token.IDENTIFIER) {
/* 756 */       name = this.exprParser.name();
/*     */     }
/*     */     
/* 759 */     if (this.lexer.token() == Token.KEY) {
/* 760 */       this.lexer.nextToken();
/*     */       
/* 762 */       MySqlKey key = new MySqlKey();
/* 763 */       key.setHasConstaint(hasConstaint);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 771 */       if (this.lexer.token() == Token.IDENTIFIER || this.lexer.token() == Token.LITERAL_ALIAS) {
/* 772 */         SQLName indexName = this.exprParser.name();
/* 773 */         if (indexName != null) {
/* 774 */           key.setIndexName(indexName);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 779 */       if (identifierEquals("USING")) {
/* 780 */         this.lexer.nextToken();
/* 781 */         key.setIndexType(this.lexer.stringVal());
/* 782 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 785 */       accept(Token.LPAREN); while (true) {
/*     */         MySqlOrderingExpr mySqlOrderingExpr;
/* 787 */         SQLExpr expr = this.exprParser.expr();
/* 788 */         if (this.lexer.token() == Token.ASC) {
/* 789 */           this.lexer.nextToken();
/* 790 */           mySqlOrderingExpr = new MySqlOrderingExpr(expr, SQLOrderingSpecification.ASC);
/* 791 */         } else if (this.lexer.token() == Token.DESC) {
/* 792 */           this.lexer.nextToken();
/* 793 */           mySqlOrderingExpr = new MySqlOrderingExpr((SQLExpr)mySqlOrderingExpr, SQLOrderingSpecification.DESC);
/*     */         } 
/*     */         
/* 796 */         key.addColumn((SQLExpr)mySqlOrderingExpr);
/* 797 */         if (this.lexer.token() != Token.COMMA) {
/*     */           break;
/*     */         }
/* 800 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 803 */       accept(Token.RPAREN);
/*     */       
/* 805 */       if (name != null) {
/* 806 */         key.setName(name);
/*     */       }
/*     */       
/* 809 */       if (identifierEquals("USING")) {
/* 810 */         this.lexer.nextToken();
/* 811 */         key.setIndexType(this.lexer.stringVal());
/* 812 */         this.lexer.nextToken();
/*     */       } 
/*     */       
/* 815 */       return (SQLTableConstraint)key;
/*     */     } 
/*     */     
/* 818 */     if (this.lexer.token() == Token.PRIMARY) {
/* 819 */       MySqlPrimaryKey pk = getExprParser().parsePrimaryKey();
/* 820 */       pk.setName(name);
/* 821 */       pk.setHasConstaint(hasConstaint);
/* 822 */       return (SQLTableConstraint)pk;
/*     */     } 
/*     */     
/* 825 */     if (this.lexer.token() == Token.UNIQUE) {
/* 826 */       MySqlUnique uk = getExprParser().parseUnique();
/* 827 */       uk.setName(name);
/* 828 */       uk.setHasConstaint(hasConstaint);
/* 829 */       return (SQLTableConstraint)uk;
/*     */     } 
/*     */     
/* 832 */     if (this.lexer.token() == Token.FOREIGN) {
/* 833 */       MysqlForeignKey fk = getExprParser().parseForeignKey();
/* 834 */       fk.setName(name);
/* 835 */       fk.setHasConstraint(hasConstaint);
/* 836 */       return (SQLTableConstraint)fk;
/*     */     } 
/*     */     
/* 839 */     throw new ParserException("TODO :" + this.lexer.token());
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\parser\MySqlCreateTableParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */