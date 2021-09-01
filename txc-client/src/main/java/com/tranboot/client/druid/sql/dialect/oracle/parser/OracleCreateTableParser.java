/*     */ package com.tranboot.client.druid.sql.dialect.oracle.parser;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.*;
import com.tranboot.client.druid.sql.ast.expr.SQLIntegerExpr;
import com.tranboot.client.druid.sql.ast.statement.SQLSelect;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.OracleLobStorageClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.OracleStorageClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelect;
import com.tranboot.client.druid.sql.parser.Lexer;
import com.tranboot.client.druid.sql.parser.ParserException;
import com.tranboot.client.druid.sql.parser.SQLCreateTableParser;
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
/*     */ public class OracleCreateTableParser
/*     */   extends SQLCreateTableParser
/*     */ {
/*     */   public OracleCreateTableParser(Lexer lexer) {
/*  44 */     super(new OracleExprParser(lexer));
/*     */   }
/*     */   
/*     */   public OracleCreateTableParser(String sql) {
/*  48 */     super(new OracleExprParser(sql));
/*     */   }
/*     */   
/*     */   protected OracleCreateTableStatement newCreateStatement() {
/*  52 */     return new OracleCreateTableStatement();
/*     */   }
/*     */   
/*     */   public OracleCreateTableStatement parseCrateTable(boolean acceptCreate) {
/*  56 */     OracleCreateTableStatement stmt = (OracleCreateTableStatement)super.parseCrateTable(acceptCreate);
/*     */     
/*     */     while (true) {
/*  59 */       while (this.lexer.token() == Token.TABLESPACE) {
/*  60 */         this.lexer.nextToken();
/*  61 */         stmt.setTablespace(this.exprParser.name());
/*     */       } 
/*  63 */       if (identifierEquals("IN_MEMORY_METADATA")) {
/*  64 */         this.lexer.nextToken();
/*  65 */         stmt.setInMemoryMetadata(true); continue;
/*     */       } 
/*  67 */       if (identifierEquals("CURSOR_SPECIFIC_SEGMENT")) {
/*  68 */         this.lexer.nextToken();
/*  69 */         stmt.setCursorSpecificSegment(true); continue;
/*     */       } 
/*  71 */       if (identifierEquals("NOPARALLEL")) {
/*  72 */         this.lexer.nextToken();
/*  73 */         stmt.setParallel(Boolean.valueOf(false)); continue;
/*     */       } 
/*  75 */       if (this.lexer.token() == Token.LOGGING) {
/*  76 */         this.lexer.nextToken();
/*  77 */         stmt.setLogging(Boolean.TRUE); continue;
/*     */       } 
/*  79 */       if (this.lexer.token() == Token.CACHE) {
/*  80 */         this.lexer.nextToken();
/*  81 */         stmt.setCache(Boolean.TRUE); continue;
/*     */       } 
/*  83 */       if (this.lexer.token() == Token.NOCACHE) {
/*  84 */         this.lexer.nextToken();
/*  85 */         stmt.setCache(Boolean.FALSE); continue;
/*     */       } 
/*  87 */       if (this.lexer.token() == Token.NOCOMPRESS) {
/*  88 */         this.lexer.nextToken();
/*  89 */         stmt.setCompress(Boolean.FALSE); continue;
/*     */       } 
/*  91 */       if (this.lexer.token() == Token.ON) {
/*  92 */         this.lexer.nextToken();
/*  93 */         accept(Token.COMMIT);
/*  94 */         stmt.setOnCommit(true); continue;
/*     */       } 
/*  96 */       if (identifierEquals("PRESERVE")) {
/*  97 */         this.lexer.nextToken();
/*  98 */         acceptIdentifier("ROWS");
/*  99 */         stmt.setPreserveRows(true); continue;
/*     */       } 
/* 101 */       if (identifierEquals("STORAGE")) {
/* 102 */         OracleStorageClause storage = ((OracleExprParser)this.exprParser).parseStorage();
/* 103 */         stmt.setStorage(storage); continue;
/*     */       } 
/* 105 */       if (identifierEquals("organization")) {
/* 106 */         this.lexer.nextToken();
/* 107 */         accept(Token.INDEX);
/* 108 */         stmt.setOrganizationIndex(true); continue;
/*     */       } 
/* 110 */       if (this.lexer.token() == Token.PCTFREE) {
/* 111 */         this.lexer.nextToken();
/* 112 */         stmt.setPtcfree(this.exprParser.expr()); continue;
/*     */       } 
/* 114 */       if (identifierEquals("PCTUSED")) {
/* 115 */         this.lexer.nextToken();
/* 116 */         stmt.setPctused(this.exprParser.expr()); continue;
/*     */       } 
/* 118 */       if (this.lexer.token() == Token.STORAGE) {
/* 119 */         OracleStorageClause storage = ((OracleExprParser)this.exprParser).parseStorage();
/* 120 */         stmt.setStorage(storage); continue;
/*     */       } 
/* 122 */       if (this.lexer.token() == Token.LOB) {
/* 123 */         OracleLobStorageClause lobStorage = ((OracleExprParser)this.exprParser).parseLobStorage();
/* 124 */         stmt.setLobStorage(lobStorage); continue;
/*     */       } 
/* 126 */       if (this.lexer.token() == Token.INITRANS) {
/* 127 */         this.lexer.nextToken();
/* 128 */         stmt.setInitrans(this.exprParser.expr()); continue;
/*     */       } 
/* 130 */       if (this.lexer.token() == Token.MAXTRANS) {
/* 131 */         this.lexer.nextToken();
/* 132 */         stmt.setMaxtrans(this.exprParser.expr()); continue;
/*     */       } 
/* 134 */       if (this.lexer.token() == Token.SEGMENT) {
/* 135 */         this.lexer.nextToken();
/* 136 */         accept(Token.CREATION);
/* 137 */         if (this.lexer.token() == Token.IMMEDIATE) {
/* 138 */           this.lexer.nextToken();
/* 139 */           stmt.setDeferredSegmentCreation(OracleCreateTableStatement.DeferredSegmentCreation.IMMEDIATE); continue;
/*     */         } 
/* 141 */         accept(Token.DEFERRED);
/* 142 */         stmt.setDeferredSegmentCreation(OracleCreateTableStatement.DeferredSegmentCreation.DEFERRED);
/*     */         continue;
/*     */       } 
/* 145 */       if (identifierEquals("PARTITION")) {
/* 146 */         this.lexer.nextToken();
/*     */         
/* 148 */         accept(Token.BY);
/*     */         
/* 150 */         if (identifierEquals("RANGE")) {
/* 151 */           SQLPartitionByRange partitionByRange = partitionByRange();
/* 152 */           partitionClauseRest((SQLPartitionBy)partitionByRange);
/* 153 */           stmt.setPartitioning((SQLPartitionBy)partitionByRange); continue;
/*     */         } 
/* 155 */         if (identifierEquals("HASH")) {
/* 156 */           SQLPartitionByHash partitionByHash = partitionByHash();
/* 157 */           partitionClauseRest((SQLPartitionBy)partitionByHash);
/* 158 */           stmt.setPartitioning((SQLPartitionBy)partitionByHash); continue;
/*     */         } 
/* 160 */         if (identifierEquals("LIST")) {
/* 161 */           SQLPartitionByList partitionByList = partitionByList();
/* 162 */           partitionClauseRest((SQLPartitionBy)partitionByList);
/* 163 */           stmt.setPartitioning((SQLPartitionBy)partitionByList);
/*     */           continue;
/*     */         } 
/* 166 */         throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*     */       } 
/*     */       
/*     */       break;
/*     */     } 
/*     */     
/* 172 */     if (this.lexer.token() == Token.AS) {
/* 173 */       this.lexer.nextToken();
/*     */       
/* 175 */       OracleSelect select = (new OracleSelectParser(this.exprParser)).select();
/* 176 */       stmt.setSelect((SQLSelect)select);
/*     */     } 
/*     */     
/* 179 */     return stmt;
/*     */   }
/*     */   
/*     */   protected SQLPartitionByList partitionByList() {
/* 183 */     acceptIdentifier("LIST");
/* 184 */     SQLPartitionByList partitionByList = new SQLPartitionByList();
/*     */     
/* 186 */     accept(Token.LPAREN);
/* 187 */     partitionByList.setExpr(this.exprParser.expr());
/* 188 */     accept(Token.RPAREN);
/*     */     
/* 190 */     parsePartitionByRest((SQLPartitionBy)partitionByList);
/*     */     
/* 192 */     return partitionByList;
/*     */   }
/*     */   
/*     */   protected SQLPartitionByHash partitionByHash() {
/* 196 */     acceptIdentifier("HASH");
/* 197 */     SQLPartitionByHash partitionByHash = new SQLPartitionByHash();
/*     */     
/* 199 */     if (this.lexer.token() == Token.KEY) {
/* 200 */       this.lexer.nextToken();
/* 201 */       partitionByHash.setKey(true);
/*     */     } 
/*     */     
/* 204 */     accept(Token.LPAREN);
/* 205 */     partitionByHash.setExpr(this.exprParser.expr());
/* 206 */     accept(Token.RPAREN);
/* 207 */     return partitionByHash;
/*     */   }
/*     */   
/*     */   protected SQLPartitionByRange partitionByRange() {
/* 211 */     acceptIdentifier("RANGE");
/* 212 */     accept(Token.LPAREN);
/* 213 */     SQLPartitionByRange clause = new SQLPartitionByRange();
/*     */     while (true) {
/* 215 */       SQLName column = this.exprParser.name();
/* 216 */       clause.addColumn(column);
/*     */       
/* 218 */       if (this.lexer.token() == Token.COMMA) {
/* 219 */         this.lexer.nextToken();
/*     */         
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 225 */     accept(Token.RPAREN);
/*     */     
/* 227 */     if (this.lexer.token() == Token.INTERVAL) {
/* 228 */       this.lexer.nextToken();
/* 229 */       accept(Token.LPAREN);
/* 230 */       clause.setInterval(this.exprParser.expr());
/* 231 */       accept(Token.RPAREN);
/*     */     } 
/*     */     
/* 234 */     parsePartitionByRest((SQLPartitionBy)clause);
/*     */     
/* 236 */     return clause;
/*     */   }
/*     */   
/*     */   protected void parsePartitionByRest(SQLPartitionBy clause) {
/* 240 */     if (this.lexer.token() == Token.STORE) {
/* 241 */       this.lexer.nextToken();
/* 242 */       accept(Token.IN);
/* 243 */       accept(Token.LPAREN);
/*     */       while (true) {
/* 245 */         SQLName tablespace = this.exprParser.name();
/* 246 */         clause.getStoreIn().add(tablespace);
/*     */         
/* 248 */         if (this.lexer.token() == Token.COMMA) {
/* 249 */           this.lexer.nextToken();
/*     */           
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/* 255 */       accept(Token.RPAREN);
/*     */     } 
/*     */     
/* 258 */     if (identifierEquals("SUBPARTITION")) {
/* 259 */       SQLSubPartitionBy subPartitionBy = subPartitionBy();
/* 260 */       clause.setSubPartitionBy(subPartitionBy);
/*     */     } 
/*     */     
/* 263 */     accept(Token.LPAREN);
/*     */     
/*     */     while (true) {
/* 266 */       SQLPartition partition = parsePartition();
/*     */       
/* 268 */       clause.addPartition(partition);
/*     */       
/* 270 */       if (this.lexer.token() == Token.COMMA) {
/* 271 */         this.lexer.nextToken();
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/*     */       break;
/*     */     } 
/* 278 */     accept(Token.RPAREN);
/*     */   }
/*     */   
/*     */   protected SQLPartition parsePartition() {
/* 282 */     acceptIdentifier("PARTITION");
/* 283 */     SQLPartition partition = new SQLPartition();
/* 284 */     partition.setName(this.exprParser.name());
/*     */     
/* 286 */     SQLPartitionValue values = this.exprParser.parsePartitionValues();
/* 287 */     if (values != null) {
/* 288 */       partition.setValues(values);
/*     */     }
/*     */     
/* 291 */     if (this.lexer.token() == Token.LPAREN) {
/* 292 */       this.lexer.nextToken();
/*     */       
/*     */       while (true) {
/* 295 */         SQLSubPartition subPartition = parseSubPartition();
/*     */         
/* 297 */         partition.addSubPartition(subPartition);
/*     */         
/* 299 */         if (this.lexer.token() == Token.COMMA) {
/* 300 */           this.lexer.nextToken();
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/*     */         break;
/*     */       } 
/* 307 */       accept(Token.RPAREN);
/* 308 */     } else if (identifierEquals("SUBPARTITIONS")) {
/* 309 */       this.lexer.nextToken();
/* 310 */       SQLExpr subPartitionsCount = this.exprParser.primary();
/* 311 */       partition.setSubPartitionsCount(subPartitionsCount);
/*     */     } 
/* 313 */     return partition;
/*     */   }
/*     */   
/*     */   protected SQLSubPartition parseSubPartition() {
/* 317 */     acceptIdentifier("SUBPARTITION");
/*     */     
/* 319 */     SQLSubPartition subPartition = new SQLSubPartition();
/* 320 */     SQLName name = this.exprParser.name();
/* 321 */     subPartition.setName(name);
/*     */     
/* 323 */     SQLPartitionValue values = this.exprParser.parsePartitionValues();
/* 324 */     if (values != null) {
/* 325 */       subPartition.setValues(values);
/*     */     }
/*     */     
/* 328 */     return subPartition;
/*     */   }
/*     */   
/*     */   protected void partitionClauseRest(SQLPartitionBy clause) {
/* 332 */     if (identifierEquals("PARTITIONS")) {
/* 333 */       this.lexer.nextToken();
/*     */       
/* 335 */       SQLIntegerExpr countExpr = this.exprParser.integerExpr();
/* 336 */       clause.setPartitionsCount((SQLExpr)countExpr);
/*     */     } 
/*     */     
/* 339 */     if (this.lexer.token() == Token.STORE) {
/* 340 */       this.lexer.nextToken();
/* 341 */       accept(Token.IN);
/* 342 */       accept(Token.LPAREN);
/* 343 */       this.exprParser.names(clause.getStoreIn(), (SQLObject)clause);
/* 344 */       accept(Token.RPAREN);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected SQLSubPartitionBy subPartitionBy() {
/* 349 */     this.lexer.nextToken();
/* 350 */     accept(Token.BY);
/*     */     
/* 352 */     if (identifierEquals("HASH")) {
/* 353 */       this.lexer.nextToken();
/* 354 */       accept(Token.LPAREN);
/*     */       
/* 356 */       SQLSubPartitionByHash byHash = new SQLSubPartitionByHash();
/* 357 */       SQLExpr expr = this.exprParser.expr();
/* 358 */       byHash.setExpr(expr);
/* 359 */       accept(Token.RPAREN);
/*     */       
/* 361 */       return (SQLSubPartitionBy)byHash;
/* 362 */     }  if (identifierEquals("LIST")) {
/* 363 */       this.lexer.nextToken();
/* 364 */       accept(Token.LPAREN);
/*     */       
/* 366 */       SQLSubPartitionByList byList = new SQLSubPartitionByList();
/* 367 */       SQLName column = this.exprParser.name();
/* 368 */       byList.setColumn(column);
/* 369 */       accept(Token.RPAREN);
/*     */       
/* 371 */       if (identifierEquals("SUBPARTITION")) {
/* 372 */         this.lexer.nextToken();
/* 373 */         acceptIdentifier("TEMPLATE");
/* 374 */         accept(Token.LPAREN);
/*     */         
/*     */         while (true) {
/* 377 */           SQLSubPartition subPartition = parseSubPartition();
/* 378 */           subPartition.setParent((SQLObject)byList);
/* 379 */           byList.getSubPartitionTemplate().add(subPartition);
/*     */           
/* 381 */           if (this.lexer.token() == Token.COMMA) {
/* 382 */             this.lexer.nextToken();
/*     */             continue;
/*     */           } 
/*     */           break;
/*     */         } 
/* 387 */         accept(Token.RPAREN);
/*     */       } 
/*     */       
/* 390 */       return (SQLSubPartitionBy)byList;
/*     */     } 
/*     */     
/* 393 */     throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\parser\OracleCreateTableParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */