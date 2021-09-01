/*      */ package com.tranboot.client.druid.sql.dialect.oracle.parser;
/*      */ 
/*      */

import com.tranboot.client.druid.sql.ast.*;
import com.tranboot.client.druid.sql.ast.expr.*;
import com.tranboot.client.druid.sql.ast.statement.*;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.OracleReturningClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.*;
import com.tranboot.client.druid.sql.parser.Lexer;
import com.tranboot.client.druid.sql.parser.ParserException;
import com.tranboot.client.druid.sql.parser.SQLStatementParser;
import com.tranboot.client.druid.sql.parser.Token;

import java.util.ArrayList;
import java.util.List;

/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */

/*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class OracleStatementParser
/*      */   extends SQLStatementParser
/*      */ {
/*      */   public OracleStatementParser(String sql) {
/*  107 */     super(new OracleExprParser(sql));
/*      */   }
/*      */   
/*      */   public OracleStatementParser(Lexer lexer) {
/*  111 */     super(new OracleExprParser(lexer));
/*      */   }
/*      */ 
/*      */   
/*      */   public OracleExprParser getExprParser() {
/*  116 */     return (OracleExprParser)this.exprParser;
/*      */   }
/*      */   
/*      */   public OracleCreateTableParser getSQLCreateTableParser() {
/*  120 */     return new OracleCreateTableParser(this.lexer);
/*      */   }
/*      */   
/*      */   protected void parseInsert0_hinits(SQLInsertInto insertStatement) {
/*  124 */     if (insertStatement instanceof OracleInsertStatement) {
/*  125 */       OracleInsertStatement stmt = (OracleInsertStatement)insertStatement;
/*  126 */       getExprParser().parseHints(stmt.getHints());
/*      */     } else {
/*  128 */       List<SQLHint> hints = new ArrayList<>(1);
/*  129 */       getExprParser().parseHints(hints);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void parseStatementList(List<SQLStatement> statementList, int max) {
/*      */     while (true) {
/*  135 */       if (max != -1 && 
/*  136 */         statementList.size() >= max) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*  141 */       if (this.lexer.token() == Token.EOF) {
/*      */         return;
/*      */       }
/*  144 */       if (this.lexer.token() == Token.END) {
/*      */         return;
/*      */       }
/*  147 */       if (this.lexer.token() == Token.ELSE) {
/*      */         return;
/*      */       }
/*      */       
/*  151 */       if (this.lexer.token() == Token.SEMI) {
/*  152 */         this.lexer.nextToken();
/*      */         
/*      */         continue;
/*      */       } 
/*  156 */       if (this.lexer.token() == Token.SELECT) {
/*  157 */         SQLSelectStatement stmt = new SQLSelectStatement((SQLSelect)(new OracleSelectParser(this.exprParser)).select(), "oracle");
/*  158 */         statementList.add(stmt);
/*      */         
/*      */         continue;
/*      */       } 
/*  162 */       if (this.lexer.token() == Token.UPDATE) {
/*  163 */         statementList.add(parseUpdateStatement());
/*      */         
/*      */         continue;
/*      */       } 
/*  167 */       if (this.lexer.token() == Token.CREATE) {
/*  168 */         statementList.add(parseCreate());
/*      */         
/*      */         continue;
/*      */       } 
/*  172 */       if (this.lexer.token() == Token.INSERT) {
/*  173 */         statementList.add(parseInsert());
/*      */         
/*      */         continue;
/*      */       } 
/*  177 */       if (this.lexer.token() == Token.DELETE) {
/*  178 */         statementList.add(parseDeleteStatement());
/*      */         
/*      */         continue;
/*      */       } 
/*  182 */       if (this.lexer.token() == Token.SLASH) {
/*  183 */         this.lexer.nextToken();
/*  184 */         statementList.add(new OraclePLSQLCommitStatement());
/*      */         
/*      */         continue;
/*      */       } 
/*  188 */       if (this.lexer.token() == Token.ALTER) {
/*  189 */         statementList.add(parserAlter());
/*      */         
/*      */         continue;
/*      */       } 
/*  193 */       if (this.lexer.token() == Token.WITH) {
/*  194 */         statementList.add(new SQLSelectStatement((SQLSelect)(new OracleSelectParser(this.exprParser)).select()));
/*      */         
/*      */         continue;
/*      */       } 
/*  198 */       if (this.lexer.token() == Token.LBRACE || identifierEquals("CALL")) {
/*  199 */         statementList.add(parseCall());
/*      */         
/*      */         continue;
/*      */       } 
/*  203 */       if (this.lexer.token() == Token.MERGE) {
/*  204 */         statementList.add(parseMerge());
/*      */         
/*      */         continue;
/*      */       } 
/*  208 */       if (this.lexer.token() == Token.BEGIN) {
/*  209 */         statementList.add(parseBlock());
/*      */         
/*      */         continue;
/*      */       } 
/*  213 */       if (this.lexer.token() == Token.DECLARE) {
/*  214 */         statementList.add(parseBlock());
/*      */         
/*      */         continue;
/*      */       } 
/*  218 */       if (this.lexer.token() == Token.LOCK) {
/*  219 */         statementList.add(parseLock());
/*      */         
/*      */         continue;
/*      */       } 
/*  223 */       if (this.lexer.token() == Token.TRUNCATE) {
/*  224 */         statementList.add(parseTruncate());
/*      */         
/*      */         continue;
/*      */       } 
/*  228 */       if (this.lexer.token() == Token.VARIANT) {
/*  229 */         SQLExpr variant = this.exprParser.primary();
/*  230 */         if (variant instanceof SQLBinaryOpExpr) {
/*  231 */           SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr)variant;
/*  232 */           if (binaryOpExpr.getOperator() == SQLBinaryOperator.Assignment) {
/*  233 */             SQLSetStatement sQLSetStatement = new SQLSetStatement(binaryOpExpr.getLeft(), binaryOpExpr.getRight(), getDbType());
/*  234 */             statementList.add(sQLSetStatement);
/*      */             continue;
/*      */           } 
/*      */         } 
/*  238 */         accept(Token.COLONEQ);
/*  239 */         SQLExpr value = this.exprParser.expr();
/*      */         
/*  241 */         SQLSetStatement stmt = new SQLSetStatement(variant, value, getDbType());
/*  242 */         statementList.add(stmt);
/*      */         
/*      */         continue;
/*      */       } 
/*  246 */       if (this.lexer.token() == Token.EXCEPTION) {
/*  247 */         statementList.add(parseException());
/*      */         
/*      */         continue;
/*      */       } 
/*  251 */       if (identifierEquals("EXIT")) {
/*  252 */         this.lexer.nextToken();
/*  253 */         OracleExitStatement stmt = new OracleExitStatement();
/*  254 */         if (this.lexer.token() == Token.WHEN) {
/*  255 */           this.lexer.nextToken();
/*  256 */           stmt.setWhen(this.exprParser.expr());
/*      */         } 
/*  258 */         statementList.add(stmt);
/*      */         
/*      */         continue;
/*      */       } 
/*  262 */       if (this.lexer.token() == Token.FETCH || identifierEquals("FETCH")) {
/*  263 */         SQLFetchStatement sQLFetchStatement = parseFetch();
/*  264 */         statementList.add(sQLFetchStatement);
/*      */         
/*      */         continue;
/*      */       } 
/*  268 */       if (identifierEquals("ROLLBACK")) {
/*  269 */         SQLRollbackStatement stmt = parseRollback();
/*      */         
/*  271 */         statementList.add(stmt);
/*      */         
/*      */         continue;
/*      */       } 
/*  275 */       if (this.lexer.token() == Token.EXPLAIN) {
/*  276 */         statementList.add(parseExplain());
/*      */         
/*      */         continue;
/*      */       } 
/*  280 */       if (this.lexer.token() == Token.IDENTIFIER) {
/*  281 */         SQLExpr expr = this.exprParser.expr();
/*  282 */         OracleExprStatement stmt = new OracleExprStatement(expr);
/*  283 */         statementList.add(stmt);
/*      */         
/*      */         continue;
/*      */       } 
/*  287 */       if (this.lexer.token() == Token.LPAREN) {
/*  288 */         char ch = this.lexer.current();
/*  289 */         int bp = this.lexer.bp();
/*  290 */         this.lexer.nextToken();
/*      */         
/*  292 */         if (this.lexer.token() == Token.SELECT) {
/*  293 */           this.lexer.reset(bp, ch, Token.LPAREN);
/*  294 */           statementList.add(parseSelect());
/*      */           continue;
/*      */         } 
/*  297 */         throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */       } 
/*      */ 
/*      */       
/*  301 */       if (this.lexer.token() == Token.SET) {
/*  302 */         statementList.add(parseSet());
/*      */         
/*      */         continue;
/*      */       } 
/*  306 */       if (this.lexer.token() == Token.GRANT) {
/*  307 */         statementList.add(parseGrant());
/*      */         
/*      */         continue;
/*      */       } 
/*  311 */       if (this.lexer.token() == Token.REVOKE) {
/*  312 */         statementList.add(parseRevoke());
/*      */         
/*      */         continue;
/*      */       } 
/*  316 */       if (this.lexer.token() == Token.COMMENT) {
/*  317 */         statementList.add(parseComment());
/*      */         continue;
/*      */       } 
/*  320 */       if (this.lexer.token() == Token.FOR) {
/*  321 */         statementList.add(parseFor());
/*      */         continue;
/*      */       } 
/*  324 */       if (this.lexer.token() == Token.LOOP) {
/*  325 */         statementList.add(parseLoop());
/*      */         continue;
/*      */       } 
/*  328 */       if (this.lexer.token() == Token.IF) {
/*  329 */         statementList.add(parseIf());
/*      */         
/*      */         continue;
/*      */       } 
/*  333 */       if (this.lexer.token() == Token.GOTO) {
/*  334 */         this.lexer.nextToken();
/*  335 */         SQLName label = this.exprParser.name();
/*  336 */         OracleGotoStatement stmt = new OracleGotoStatement(label);
/*  337 */         statementList.add(stmt);
/*      */         
/*      */         continue;
/*      */       } 
/*  341 */       if (this.lexer.token() == Token.COMMIT) {
/*  342 */         this.lexer.nextToken();
/*      */         
/*  344 */         if (identifierEquals("WORK")) {
/*  345 */           this.lexer.nextToken();
/*      */         }
/*  347 */         OracleCommitStatement stmt = new OracleCommitStatement();
/*      */         
/*  349 */         if (identifierEquals("WRITE")) {
/*  350 */           stmt.setWrite(true);
/*  351 */           this.lexer.nextToken();
/*      */           
/*      */           while (true) {
/*  354 */             while (this.lexer.token() == Token.WAIT) {
/*  355 */               this.lexer.nextToken();
/*  356 */               stmt.setWait(Boolean.TRUE);
/*      */             } 
/*  358 */             if (this.lexer.token() == Token.NOWAIT) {
/*  359 */               this.lexer.nextToken();
/*  360 */               stmt.setWait(Boolean.FALSE); continue;
/*      */             } 
/*  362 */             if (this.lexer.token() == Token.IMMEDIATE) {
/*  363 */               this.lexer.nextToken();
/*  364 */               stmt.setImmediate(Boolean.TRUE); continue;
/*      */             } 
/*  366 */             if (identifierEquals("BATCH")) {
/*  367 */               this.lexer.nextToken();
/*  368 */               stmt.setImmediate(Boolean.FALSE);
/*      */               
/*      */               continue;
/*      */             } 
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*  376 */         statementList.add(stmt);
/*      */         
/*      */         continue;
/*      */       } 
/*  380 */       if (this.lexer.token() == Token.SAVEPOINT) {
/*  381 */         this.lexer.nextToken();
/*      */         
/*  383 */         OracleSavePointStatement stmt = new OracleSavePointStatement();
/*      */         
/*  385 */         if (this.lexer.token() == Token.TO) {
/*  386 */           this.lexer.nextToken();
/*  387 */           stmt.setTo(this.exprParser.name());
/*      */         } 
/*      */         
/*  390 */         statementList.add(stmt);
/*      */         
/*      */         continue;
/*      */       } 
/*  394 */       if (this.lexer.token() == Token.LTLT) {
/*  395 */         this.lexer.nextToken();
/*  396 */         SQLName label = this.exprParser.name();
/*  397 */         OracleLabelStatement stmt = new OracleLabelStatement(label);
/*  398 */         accept(Token.GTGT);
/*  399 */         statementList.add(stmt);
/*      */         
/*      */         continue;
/*      */       } 
/*  403 */       if (this.lexer.token() == Token.DROP) {
/*  404 */         this.lexer.nextToken();
/*      */         
/*  406 */         if (this.lexer.token() == Token.TABLE) {
/*  407 */           SQLDropTableStatement stmt = parseDropTable(false);
/*  408 */           statementList.add(stmt);
/*      */           
/*      */           continue;
/*      */         } 
/*  412 */         boolean isPublic = false;
/*  413 */         if (identifierEquals("PUBLIC")) {
/*  414 */           this.lexer.nextToken();
/*  415 */           isPublic = true;
/*      */         } 
/*      */         
/*  418 */         if (this.lexer.token() == Token.DATABASE) {
/*  419 */           this.lexer.nextToken();
/*      */           
/*  421 */           if (identifierEquals("LINK")) {
/*  422 */             this.lexer.nextToken();
/*      */             
/*  424 */             OracleDropDbLinkStatement stmt = new OracleDropDbLinkStatement();
/*  425 */             if (isPublic) {
/*  426 */               stmt.setPublic(isPublic);
/*      */             }
/*      */             
/*  429 */             stmt.setName(this.exprParser.name());
/*      */             
/*  431 */             statementList.add(stmt);
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  436 */         if (this.lexer.token() == Token.INDEX) {
/*  437 */           SQLStatement stmt = parseDropIndex();
/*  438 */           statementList.add(stmt);
/*      */           
/*      */           continue;
/*      */         } 
/*  442 */         if (this.lexer.token() == Token.VIEW) {
/*  443 */           SQLDropViewStatement sQLDropViewStatement = parseDropView(false);
/*  444 */           statementList.add(sQLDropViewStatement);
/*      */           
/*      */           continue;
/*      */         } 
/*  448 */         if (this.lexer.token() == Token.SEQUENCE) {
/*  449 */           SQLDropSequenceStatement stmt = parseDropSequece(false);
/*  450 */           statementList.add(stmt);
/*      */           
/*      */           continue;
/*      */         } 
/*  454 */         if (this.lexer.token() == Token.TRIGGER) {
/*  455 */           SQLDropTriggerStatement stmt = parseDropTrigger(false);
/*  456 */           statementList.add(stmt);
/*      */           
/*      */           continue;
/*      */         } 
/*  460 */         if (this.lexer.token() == Token.USER) {
/*  461 */           SQLDropUserStatement stmt = parseDropUser();
/*  462 */           statementList.add(stmt);
/*      */           
/*      */           continue;
/*      */         } 
/*  466 */         if (this.lexer.token() == Token.PROCEDURE) {
/*  467 */           SQLDropProcedureStatement stmt = parseDropProcedure(false);
/*  468 */           statementList.add(stmt);
/*      */           
/*      */           continue;
/*      */         } 
/*  472 */         throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */       } 
/*      */       
/*  475 */       if (this.lexer.token() == Token.NULL) {
/*  476 */         this.lexer.nextToken();
/*  477 */         OracleExprStatement stmt = new OracleExprStatement((SQLExpr)new SQLNullExpr());
/*  478 */         statementList.add(stmt);
/*      */         
/*      */         continue;
/*      */       } 
/*  482 */       if (this.lexer.token() == Token.OPEN) {
/*  483 */         SQLOpenStatement sQLOpenStatement = parseOpen();
/*  484 */         statementList.add(sQLOpenStatement); continue;
/*      */       } 
/*      */       break;
/*      */     } 
/*  488 */     throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */   }
/*      */ 
/*      */   
/*      */   public SQLStatement parseIf() {
/*  493 */     accept(Token.IF);
/*      */     
/*  495 */     SQLIfStatement stmt = new SQLIfStatement();
/*      */     
/*  497 */     stmt.setCondition(this.exprParser.expr());
/*      */     
/*  499 */     accept(Token.THEN);
/*      */     
/*  501 */     parseStatementList(stmt.getStatements());
/*      */     
/*  503 */     while (this.lexer.token() == Token.ELSE) {
/*  504 */       this.lexer.nextToken();
/*      */       
/*  506 */       if (this.lexer.token() == Token.IF) {
/*  507 */         this.lexer.nextToken();
/*      */         
/*  509 */         SQLIfStatement.ElseIf elseIf = new SQLIfStatement.ElseIf();
/*      */         
/*  511 */         elseIf.setCondition(this.exprParser.expr());
/*      */         
/*  513 */         accept(Token.THEN);
/*  514 */         parseStatementList(elseIf.getStatements());
/*      */         
/*  516 */         stmt.getElseIfList().add(elseIf); continue;
/*      */       } 
/*  518 */       SQLIfStatement.Else elseItem = new SQLIfStatement.Else();
/*  519 */       parseStatementList(elseItem.getStatements());
/*  520 */       stmt.setElseItem(elseItem);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  525 */     accept(Token.END);
/*  526 */     accept(Token.IF);
/*      */     
/*  528 */     return (SQLStatement)stmt;
/*      */   }
/*      */   
/*      */   public OracleForStatement parseFor() {
/*  532 */     accept(Token.FOR);
/*      */     
/*  534 */     OracleForStatement stmt = new OracleForStatement();
/*      */     
/*  536 */     stmt.setIndex(this.exprParser.name());
/*  537 */     accept(Token.IN);
/*  538 */     stmt.setRange(this.exprParser.expr());
/*  539 */     accept(Token.LOOP);
/*      */     
/*  541 */     parseStatementList(stmt.getStatements());
/*  542 */     accept(Token.END);
/*  543 */     accept(Token.LOOP);
/*  544 */     return stmt;
/*      */   }
/*      */   
/*      */   public SQLLoopStatement parseLoop() {
/*  548 */     accept(Token.LOOP);
/*      */     
/*  550 */     SQLLoopStatement stmt = new SQLLoopStatement();
/*      */     
/*  552 */     parseStatementList(stmt.getStatements());
/*  553 */     accept(Token.END);
/*  554 */     accept(Token.LOOP);
/*  555 */     return stmt;
/*      */   }
/*      */   
/*      */   public SQLStatement parseSet() {
/*  559 */     accept(Token.SET);
/*  560 */     acceptIdentifier("TRANSACTION");
/*      */     
/*  562 */     OracleSetTransactionStatement stmt = new OracleSetTransactionStatement();
/*      */     
/*  564 */     if (identifierEquals("READ")) {
/*  565 */       this.lexer.nextToken();
/*  566 */       acceptIdentifier("ONLY");
/*  567 */       stmt.setReadOnly(true);
/*      */     } 
/*      */     
/*  570 */     acceptIdentifier("NAME");
/*      */     
/*  572 */     stmt.setName(this.exprParser.expr());
/*  573 */     return (SQLStatement)stmt;
/*      */   }
/*      */   
/*      */   public SQLStatement parserAlter() {
/*  577 */     accept(Token.ALTER);
/*  578 */     if (this.lexer.token() == Token.SESSION) {
/*  579 */       this.lexer.nextToken();
/*      */       
/*  581 */       OracleAlterSessionStatement stmt = new OracleAlterSessionStatement();
/*  582 */       if (this.lexer.token() == Token.SET) {
/*  583 */         this.lexer.nextToken();
/*  584 */         parseAssignItems(stmt.getItems(), (SQLObject)stmt);
/*      */       } else {
/*  586 */         throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */       } 
/*  588 */       return (SQLStatement)stmt;
/*  589 */     }  if (this.lexer.token() == Token.PROCEDURE) {
/*  590 */       this.lexer.nextToken();
/*  591 */       OracleAlterProcedureStatement stmt = new OracleAlterProcedureStatement();
/*  592 */       stmt.setName((SQLExpr)this.exprParser.name());
/*  593 */       if (identifierEquals("COMPILE")) {
/*  594 */         this.lexer.nextToken();
/*  595 */         stmt.setCompile(true);
/*      */       } 
/*      */       
/*  598 */       if (identifierEquals("REUSE")) {
/*  599 */         this.lexer.nextToken();
/*  600 */         acceptIdentifier("SETTINGS");
/*  601 */         stmt.setReuseSettings(true);
/*      */       } 
/*      */       
/*  604 */       return (SQLStatement)stmt;
/*  605 */     }  if (this.lexer.token() == Token.TABLE)
/*  606 */       return parseAlterTable(); 
/*  607 */     if (this.lexer.token() == Token.INDEX) {
/*  608 */       this.lexer.nextToken();
/*  609 */       OracleAlterIndexStatement stmt = new OracleAlterIndexStatement();
/*  610 */       stmt.setName(this.exprParser.name());
/*      */       
/*  612 */       if (identifierEquals("RENAME")) {
/*  613 */         this.lexer.nextToken();
/*  614 */         accept(Token.TO);
/*  615 */         stmt.setRenameTo(this.exprParser.name());
/*      */       } 
/*      */       
/*      */       while (true) {
/*  619 */         while (identifierEquals("rebuild")) {
/*  620 */           this.lexer.nextToken();
/*      */           
/*  622 */           OracleAlterIndexStatement.Rebuild rebuild = new OracleAlterIndexStatement.Rebuild();
/*  623 */           stmt.setRebuild(rebuild);
/*      */         } 
/*  625 */         if (identifierEquals("MONITORING")) {
/*  626 */           this.lexer.nextToken();
/*  627 */           acceptIdentifier("USAGE");
/*  628 */           stmt.setMonitoringUsage(Boolean.TRUE); continue;
/*      */         }  break;
/*  630 */       }  if (identifierEquals("PARALLEL")) {
/*  631 */         this.lexer.nextToken();
/*  632 */         stmt.setParallel(this.exprParser.expr());
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  637 */       return (SQLStatement)stmt;
/*  638 */     }  if (this.lexer.token() == Token.TRIGGER) {
/*  639 */       this.lexer.nextToken();
/*  640 */       OracleAlterTriggerStatement stmt = new OracleAlterTriggerStatement();
/*  641 */       stmt.setName(this.exprParser.name());
/*      */       
/*      */       while (true) {
/*  644 */         while (this.lexer.token() == Token.ENABLE) {
/*  645 */           this.lexer.nextToken();
/*  646 */           stmt.setEnable(Boolean.TRUE);
/*      */         } 
/*  648 */         if (this.lexer.token() == Token.DISABLE) {
/*  649 */           this.lexer.nextToken();
/*  650 */           stmt.setEnable(Boolean.FALSE); continue;
/*      */         } 
/*  652 */         if (identifierEquals("COMPILE")) {
/*  653 */           this.lexer.nextToken();
/*  654 */           stmt.setCompile(true);
/*      */           
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/*  660 */       return (SQLStatement)stmt;
/*  661 */     }  if (identifierEquals("SYNONYM")) {
/*  662 */       this.lexer.nextToken();
/*  663 */       OracleAlterSynonymStatement stmt = new OracleAlterSynonymStatement();
/*  664 */       stmt.setName(this.exprParser.name());
/*      */       
/*      */       while (true) {
/*  667 */         while (this.lexer.token() == Token.ENABLE) {
/*  668 */           this.lexer.nextToken();
/*  669 */           stmt.setEnable(Boolean.TRUE);
/*      */         } 
/*  671 */         if (this.lexer.token() == Token.DISABLE) {
/*  672 */           this.lexer.nextToken();
/*  673 */           stmt.setEnable(Boolean.FALSE); continue;
/*      */         } 
/*  675 */         if (identifierEquals("COMPILE")) {
/*  676 */           this.lexer.nextToken();
/*  677 */           stmt.setCompile(true);
/*      */           
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/*  683 */       return (SQLStatement)stmt;
/*  684 */     }  if (this.lexer.token() == Token.VIEW) {
/*  685 */       this.lexer.nextToken();
/*  686 */       OracleAlterViewStatement stmt = new OracleAlterViewStatement();
/*  687 */       stmt.setName(this.exprParser.name());
/*      */       
/*      */       while (true) {
/*  690 */         while (this.lexer.token() == Token.ENABLE) {
/*  691 */           this.lexer.nextToken();
/*  692 */           stmt.setEnable(Boolean.TRUE);
/*      */         } 
/*  694 */         if (this.lexer.token() == Token.DISABLE) {
/*  695 */           this.lexer.nextToken();
/*  696 */           stmt.setEnable(Boolean.FALSE); continue;
/*      */         } 
/*  698 */         if (identifierEquals("COMPILE")) {
/*  699 */           this.lexer.nextToken();
/*  700 */           stmt.setCompile(true);
/*      */           
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/*  706 */       return (SQLStatement)stmt;
/*  707 */     }  if (this.lexer.token() == Token.TABLESPACE) {
/*  708 */       this.lexer.nextToken();
/*      */       
/*  710 */       OracleAlterTablespaceStatement stmt = new OracleAlterTablespaceStatement();
/*  711 */       stmt.setName(this.exprParser.name());
/*      */       
/*  713 */       if (identifierEquals("ADD")) {
/*  714 */         this.lexer.nextToken();
/*      */         
/*  716 */         if (identifierEquals("DATAFILE")) {
/*  717 */           this.lexer.nextToken();
/*      */           
/*  719 */           OracleAlterTablespaceAddDataFile item = new OracleAlterTablespaceAddDataFile();
/*      */           
/*      */           while (true) {
/*  722 */             OracleFileSpecification file = new OracleFileSpecification();
/*      */             
/*      */             while (true) {
/*  725 */               SQLExpr fileName = this.exprParser.expr();
/*  726 */               file.getFileNames().add(fileName);
/*      */               
/*  728 */               if (this.lexer.token() == Token.COMMA) {
/*  729 */                 this.lexer.nextToken();
/*      */                 
/*      */                 continue;
/*      */               } 
/*      */               
/*      */               break;
/*      */             } 
/*  736 */             if (identifierEquals("SIZE")) {
/*  737 */               this.lexer.nextToken();
/*  738 */               file.setSize(this.exprParser.expr());
/*      */             } 
/*      */             
/*  741 */             if (identifierEquals("AUTOEXTEND")) {
/*  742 */               this.lexer.nextToken();
/*  743 */               if (identifierEquals("OFF")) {
/*  744 */                 this.lexer.nextToken();
/*  745 */                 file.setAutoExtendOff(true);
/*  746 */               } else if (identifierEquals("ON")) {
/*  747 */                 this.lexer.nextToken();
/*  748 */                 file.setAutoExtendOn(this.exprParser.expr());
/*      */               } else {
/*  750 */                 throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */               } 
/*      */             } 
/*      */             
/*  754 */             item.getFiles().add(file);
/*      */             
/*  756 */             if (this.lexer.token() == Token.COMMA) {
/*  757 */               this.lexer.nextToken();
/*      */               
/*      */               continue;
/*      */             } 
/*      */             
/*      */             break;
/*      */           } 
/*  764 */           stmt.setItem((OracleAlterTablespaceItem)item);
/*      */         } else {
/*  766 */           throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */         } 
/*      */       } else {
/*  769 */         throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */       } 
/*      */       
/*  772 */       return (SQLStatement)stmt;
/*      */     } 
/*      */     
/*  775 */     throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */   }
/*      */   
/*      */   private SQLStatement parseAlterTable() {
/*  779 */     this.lexer.nextToken();
/*  780 */     SQLAlterTableStatement stmt = new SQLAlterTableStatement(getDbType());
/*  781 */     stmt.setName(this.exprParser.name());
/*      */     
/*      */     while (true) {
/*  784 */       while (identifierEquals("ADD")) {
/*  785 */         this.lexer.nextToken();
/*      */         
/*  787 */         if (this.lexer.token() == Token.LPAREN) {
/*  788 */           this.lexer.nextToken();
/*      */           
/*  790 */           SQLAlterTableAddColumn item = parseAlterTableAddColumn();
/*      */           
/*  792 */           stmt.addItem((SQLAlterTableItem)item);
/*      */           
/*  794 */           accept(Token.RPAREN); continue;
/*  795 */         }  if (this.lexer.token() == Token.CONSTRAINT) {
/*  796 */           OracleConstraint constraint = ((OracleExprParser)this.exprParser).parseConstaint();
/*  797 */           SQLAlterTableAddConstraint item = new SQLAlterTableAddConstraint();
/*  798 */           constraint.setParent((SQLObject)item);
/*  799 */           item.setParent((SQLObject)stmt);
/*  800 */           item.setConstraint((SQLConstraint)constraint);
/*  801 */           stmt.addItem((SQLAlterTableItem)item); continue;
/*  802 */         }  if (this.lexer.token() == Token.IDENTIFIER) {
/*  803 */           SQLAlterTableAddColumn item = parseAlterTableAddColumn();
/*  804 */           stmt.addItem((SQLAlterTableItem)item); continue;
/*      */         } 
/*  806 */         throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */       } 
/*      */ 
/*      */       
/*  810 */       if (identifierEquals("MOVE")) {
/*  811 */         this.lexer.nextToken();
/*      */         
/*  813 */         if (this.lexer.token() == Token.TABLESPACE) {
/*  814 */           this.lexer.nextToken();
/*      */           
/*  816 */           OracleAlterTableMoveTablespace item = new OracleAlterTableMoveTablespace();
/*  817 */           item.setName(this.exprParser.name());
/*      */           
/*  819 */           stmt.addItem((SQLAlterTableItem)item); break;
/*      */         } 
/*  821 */         throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */       } 
/*  823 */       if (identifierEquals("RENAME")) {
/*  824 */         stmt.addItem(parseAlterTableRename()); break;
/*  825 */       }  if (identifierEquals("MODIFY")) {
/*  826 */         this.lexer.nextToken();
/*      */         
/*  828 */         OracleAlterTableModify item = new OracleAlterTableModify();
/*  829 */         if (this.lexer.token() == Token.LPAREN) {
/*  830 */           this.lexer.nextToken();
/*      */           
/*      */           while (true) {
/*  833 */             SQLColumnDefinition columnDef = this.exprParser.parseColumn();
/*  834 */             item.addColumn(columnDef);
/*  835 */             if (this.lexer.token() == Token.COMMA) {
/*  836 */               this.lexer.nextToken();
/*      */               continue;
/*      */             } 
/*      */             break;
/*      */           } 
/*  841 */           accept(Token.RPAREN);
/*      */         } else {
/*      */           
/*  844 */           SQLColumnDefinition columnDef = this.exprParser.parseColumn();
/*  845 */           item.addColumn(columnDef);
/*      */         } 
/*      */         
/*  848 */         stmt.addItem((SQLAlterTableItem)item); continue;
/*      */       } 
/*  850 */       if (identifierEquals("SPLIT")) {
/*  851 */         parseAlterTableSplit(stmt); continue;
/*      */       } 
/*  853 */       if (this.lexer.token() == Token.TRUNCATE) {
/*  854 */         this.lexer.nextToken();
/*  855 */         if (identifierEquals("PARTITION")) {
/*  856 */           this.lexer.nextToken();
/*  857 */           OracleAlterTableTruncatePartition item = new OracleAlterTableTruncatePartition();
/*  858 */           item.setName(this.exprParser.name());
/*  859 */           stmt.addItem((SQLAlterTableItem)item); continue;
/*      */         } 
/*  861 */         throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */       } 
/*      */       
/*  864 */       if (this.lexer.token() == Token.DROP) {
/*  865 */         parseAlterDrop(stmt); continue;
/*      */       } 
/*  867 */       if (this.lexer.token() == Token.DISABLE) {
/*  868 */         this.lexer.nextToken();
/*  869 */         if (this.lexer.token() == Token.CONSTRAINT) {
/*  870 */           this.lexer.nextToken();
/*  871 */           SQLAlterTableEnableConstraint item = new SQLAlterTableEnableConstraint();
/*  872 */           item.setConstraintName(this.exprParser.name());
/*  873 */           stmt.addItem((SQLAlterTableItem)item); break;
/*      */         } 
/*  875 */         throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */       } 
/*  877 */       if (this.lexer.token() == Token.ENABLE) {
/*  878 */         this.lexer.nextToken();
/*  879 */         if (this.lexer.token() == Token.CONSTRAINT) {
/*  880 */           this.lexer.nextToken();
/*  881 */           SQLAlterTableDisableConstraint item = new SQLAlterTableDisableConstraint();
/*  882 */           item.setConstraintName(this.exprParser.name());
/*  883 */           stmt.addItem((SQLAlterTableItem)item); break;
/*      */         } 
/*  885 */         throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */       } 
/*      */ 
/*      */       
/*      */       break;
/*      */     } 
/*      */     
/*  892 */     if (this.lexer.token() == Token.UPDATE) {
/*  893 */       this.lexer.nextToken();
/*      */       
/*  895 */       if (identifierEquals("GLOBAL")) {
/*  896 */         this.lexer.nextToken();
/*  897 */         acceptIdentifier("INDEXES");
/*  898 */         stmt.setUpdateGlobalIndexes(true);
/*      */       } else {
/*  900 */         throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */       } 
/*      */     } 
/*      */     
/*  904 */     return (SQLStatement)stmt;
/*      */   }
/*      */   
/*      */   public void parseAlterDrop(SQLAlterTableStatement stmt) {
/*  908 */     this.lexer.nextToken();
/*  909 */     if (this.lexer.token() == Token.CONSTRAINT) {
/*  910 */       this.lexer.nextToken();
/*  911 */       SQLAlterTableDropConstraint item = new SQLAlterTableDropConstraint();
/*  912 */       item.setConstraintName(this.exprParser.name());
/*  913 */       stmt.addItem((SQLAlterTableItem)item);
/*  914 */     } else if (this.lexer.token() == Token.LPAREN) {
/*  915 */       this.lexer.nextToken();
/*  916 */       SQLAlterTableDropColumnItem item = new SQLAlterTableDropColumnItem();
/*  917 */       this.exprParser.names(item.getColumns());
/*  918 */       stmt.addItem((SQLAlterTableItem)item);
/*  919 */       accept(Token.RPAREN);
/*  920 */     } else if (this.lexer.token() == Token.COLUMN) {
/*  921 */       this.lexer.nextToken();
/*  922 */       SQLAlterTableDropColumnItem item = new SQLAlterTableDropColumnItem();
/*  923 */       this.exprParser.names(item.getColumns());
/*  924 */       stmt.addItem((SQLAlterTableItem)item);
/*  925 */     } else if (identifierEquals("PARTITION")) {
/*  926 */       this.lexer.nextToken();
/*  927 */       OracleAlterTableDropPartition item = new OracleAlterTableDropPartition();
/*  928 */       item.setName(this.exprParser.name());
/*  929 */       stmt.addItem((SQLAlterTableItem)item);
/*  930 */     } else if (this.lexer.token() == Token.INDEX) {
/*  931 */       this.lexer.nextToken();
/*  932 */       SQLName indexName = this.exprParser.name();
/*  933 */       SQLAlterTableDropIndex item = new SQLAlterTableDropIndex();
/*  934 */       item.setIndexName(indexName);
/*  935 */       stmt.addItem((SQLAlterTableItem)item);
/*      */     } else {
/*  937 */       throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */     } 
/*      */   }
/*      */   
/*      */   private void parseAlterTableSplit(SQLAlterTableStatement stmt) {
/*  942 */     this.lexer.nextToken();
/*  943 */     if (identifierEquals("PARTITION")) {
/*  944 */       this.lexer.nextToken();
/*  945 */       OracleAlterTableSplitPartition item = new OracleAlterTableSplitPartition();
/*  946 */       item.setName(this.exprParser.name());
/*      */       
/*  948 */       if (identifierEquals("AT")) {
/*  949 */         this.lexer.nextToken();
/*  950 */         accept(Token.LPAREN);
/*  951 */         this.exprParser.exprList(item.getAt(), (SQLObject)item);
/*  952 */         accept(Token.RPAREN);
/*      */       } else {
/*  954 */         throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */       } 
/*      */       
/*  957 */       if (this.lexer.token() == Token.INTO) {
/*  958 */         this.lexer.nextToken();
/*  959 */         accept(Token.LPAREN);
/*      */         
/*      */         while (true) {
/*  962 */           OracleAlterTableSplitPartition.NestedTablePartitionSpec spec = new OracleAlterTableSplitPartition.NestedTablePartitionSpec();
/*  963 */           acceptIdentifier("PARTITION");
/*  964 */           spec.setPartition(this.exprParser.name());
/*      */ 
/*      */           
/*  967 */           while (this.lexer.token() == Token.TABLESPACE) {
/*  968 */             this.lexer.nextToken();
/*  969 */             SQLName tablespace = this.exprParser.name();
/*  970 */             spec.getSegmentAttributeItems().add(new OracleAlterTableSplitPartition.TableSpaceItem(tablespace));
/*      */           } 
/*  972 */           if (identifierEquals("PCTREE"))
/*  973 */             throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*  974 */           if (identifierEquals("PCTUSED"))
/*  975 */             throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*  976 */           if (identifierEquals("INITRANS")) {
/*  977 */             throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */           }
/*  979 */           if (identifierEquals("STORAGE")) {
/*  980 */             throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */           }
/*  982 */           if (identifierEquals("LOGGING"))
/*  983 */             throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*  984 */           if (identifierEquals("NOLOGGING"))
/*  985 */             throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*  986 */           if (identifierEquals("FILESYSTEM_LIKE_LOGGING")) {
/*  987 */             throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  994 */           item.getInto().add(spec);
/*      */           
/*  996 */           if (this.lexer.token() == Token.COMMA) {
/*  997 */             this.lexer.nextToken();
/*      */             continue;
/*      */           } 
/*      */           break;
/*      */         } 
/* 1002 */         accept(Token.RPAREN);
/*      */       } 
/*      */       
/* 1005 */       if (this.lexer.token() == Token.UPDATE) {
/* 1006 */         this.lexer.nextToken();
/* 1007 */         acceptIdentifier("INDEXES");
/* 1008 */         OracleAlterTableSplitPartition.UpdateIndexesClause updateIndexes = new OracleAlterTableSplitPartition.UpdateIndexesClause();
/* 1009 */         item.setUpdateIndexes(updateIndexes);
/*      */       } 
/* 1011 */       stmt.addItem((SQLAlterTableItem)item);
/*      */     } else {
/* 1013 */       throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */     } 
/*      */   }
/*      */   
/*      */   public OracleLockTableStatement parseLock() {
/* 1018 */     accept(Token.LOCK);
/* 1019 */     accept(Token.TABLE);
/*      */     
/* 1021 */     OracleLockTableStatement stmt = new OracleLockTableStatement();
/* 1022 */     stmt.setTable(this.exprParser.name());
/*      */     
/* 1024 */     accept(Token.IN);
/* 1025 */     if (this.lexer.token() == Token.SHARE) {
/* 1026 */       stmt.setLockMode(OracleLockTableStatement.LockMode.SHARE);
/* 1027 */       this.lexer.nextToken();
/* 1028 */     } else if (this.lexer.token() == Token.EXCLUSIVE) {
/* 1029 */       stmt.setLockMode(OracleLockTableStatement.LockMode.EXCLUSIVE);
/* 1030 */       this.lexer.nextToken();
/*      */     } 
/* 1032 */     accept(Token.MODE);
/*      */     
/* 1034 */     if (this.lexer.token() == Token.NOWAIT) {
/* 1035 */       this.lexer.nextToken();
/* 1036 */     } else if (this.lexer.token() == Token.WAIT) {
/* 1037 */       this.lexer.nextToken();
/* 1038 */       stmt.setWait(this.exprParser.expr());
/*      */     } 
/* 1040 */     return stmt;
/*      */   }
/*      */   
/*      */   public SQLBlockStatement parseBlock() {
/* 1044 */     SQLBlockStatement block = new SQLBlockStatement();
/*      */     
/* 1046 */     if (this.lexer.token() == Token.DECLARE) {
/* 1047 */       this.lexer.nextToken();
/*      */       
/* 1049 */       parserParameters(block.getParameters());
/* 1050 */       for (SQLParameter param : block.getParameters()) {
/* 1051 */         param.setParent((SQLObject)block);
/*      */       }
/*      */     } 
/*      */     
/* 1055 */     accept(Token.BEGIN);
/*      */     
/* 1057 */     parseStatementList(block.getStatementList());
/*      */     
/* 1059 */     accept(Token.END);
/*      */     
/* 1061 */     return block;
/*      */   }
/*      */   
/*      */   private void parserParameters(List<SQLParameter> parameters) {
/*      */     while (true) {
/* 1066 */       SQLParameter parameter = new SQLParameter();
/*      */       
/* 1068 */       if (this.lexer.token() == Token.CURSOR) {
/* 1069 */         this.lexer.nextToken();
/*      */         
/* 1071 */         parameter.setName((SQLExpr)this.exprParser.name());
/*      */         
/* 1073 */         accept(Token.IS);
/* 1074 */         OracleSelect oracleSelect = createSQLSelectParser().select();
/*      */         
/* 1076 */         SQLDataTypeImpl dataType = new SQLDataTypeImpl();
/* 1077 */         dataType.setName("CURSOR");
/* 1078 */         parameter.setDataType((SQLDataType)dataType);
/*      */         
/* 1080 */         parameter.setDefaultValue((SQLExpr)new SQLQueryExpr((SQLSelect)oracleSelect));
/*      */       } else {
/* 1082 */         parameter.setName((SQLExpr)this.exprParser.name());
/* 1083 */         parameter.setDataType(this.exprParser.parseDataType());
/*      */         
/* 1085 */         if (this.lexer.token() == Token.COLONEQ) {
/* 1086 */           this.lexer.nextToken();
/* 1087 */           parameter.setDefaultValue(this.exprParser.expr());
/*      */         } 
/*      */       } 
/*      */       
/* 1091 */       parameters.add(parameter);
/* 1092 */       if (this.lexer.token() == Token.COMMA || this.lexer.token() == Token.SEMI) {
/* 1093 */         this.lexer.nextToken();
/*      */       }
/*      */       
/* 1096 */       if (this.lexer.token() != Token.BEGIN && this.lexer.token() != Token.RPAREN) {
/*      */         continue;
/*      */       }
/*      */       break;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public OracleSelectParser createSQLSelectParser() {
/* 1105 */     return new OracleSelectParser(this.exprParser);
/*      */   }
/*      */   
/*      */   public OracleStatement parseInsert() {
/* 1109 */     if (this.lexer.token() == Token.LPAREN) {
/* 1110 */       OracleInsertStatement oracleInsertStatement = new OracleInsertStatement();
/* 1111 */       parseInsert0((SQLInsertInto)oracleInsertStatement, false);
/*      */       
/* 1113 */       oracleInsertStatement.setReturning(parseReturningClause());
/* 1114 */       oracleInsertStatement.setErrorLogging(parseErrorLoggingClause());
/*      */       
/* 1116 */       return (OracleStatement)oracleInsertStatement;
/*      */     } 
/*      */     
/* 1119 */     accept(Token.INSERT);
/*      */     
/* 1121 */     List<SQLHint> hints = new ArrayList<>();
/*      */     
/* 1123 */     parseHints(hints);
/*      */     
/* 1125 */     if (this.lexer.token() == Token.INTO) {
/* 1126 */       OracleInsertStatement oracleInsertStatement = new OracleInsertStatement();
/* 1127 */       oracleInsertStatement.setHints(hints);
/*      */       
/* 1129 */       parseInsert0((SQLInsertInto)oracleInsertStatement);
/*      */       
/* 1131 */       oracleInsertStatement.setReturning(parseReturningClause());
/* 1132 */       oracleInsertStatement.setErrorLogging(parseErrorLoggingClause());
/*      */       
/* 1134 */       return (OracleStatement)oracleInsertStatement;
/*      */     } 
/*      */     
/* 1137 */     OracleMultiInsertStatement stmt = parseMultiInsert();
/* 1138 */     stmt.setHints(hints);
/* 1139 */     return (OracleStatement)stmt;
/*      */   }
/*      */   
/*      */   public OracleMultiInsertStatement parseMultiInsert() {
/* 1143 */     OracleMultiInsertStatement stmt = new OracleMultiInsertStatement();
/*      */     
/* 1145 */     if (this.lexer.token() == Token.ALL) {
/* 1146 */       this.lexer.nextToken();
/* 1147 */       stmt.setOption(OracleMultiInsertStatement.Option.ALL);
/* 1148 */     } else if (this.lexer.token() == Token.FIRST) {
/* 1149 */       this.lexer.nextToken();
/* 1150 */       stmt.setOption(OracleMultiInsertStatement.Option.FIRST);
/*      */     } 
/*      */     
/* 1153 */     while (this.lexer.token() == Token.INTO) {
/* 1154 */       OracleMultiInsertStatement.InsertIntoClause clause = new OracleMultiInsertStatement.InsertIntoClause();
/*      */       
/* 1156 */       parseInsert0((SQLInsertInto)clause);
/*      */       
/* 1158 */       clause.setReturning(parseReturningClause());
/* 1159 */       clause.setErrorLogging(parseErrorLoggingClause());
/*      */       
/* 1161 */       stmt.addEntry((OracleMultiInsertStatement.Entry)clause);
/*      */     } 
/*      */     
/* 1164 */     if (this.lexer.token() == Token.WHEN) {
/* 1165 */       OracleMultiInsertStatement.ConditionalInsertClause clause = new OracleMultiInsertStatement.ConditionalInsertClause();
/*      */       
/* 1167 */       while (this.lexer.token() == Token.WHEN) {
/* 1168 */         this.lexer.nextToken();
/*      */         
/* 1170 */         OracleMultiInsertStatement.ConditionalInsertClauseItem item = new OracleMultiInsertStatement.ConditionalInsertClauseItem();
/*      */         
/* 1172 */         item.setWhen(this.exprParser.expr());
/* 1173 */         accept(Token.THEN);
/* 1174 */         OracleMultiInsertStatement.InsertIntoClause insertInto = new OracleMultiInsertStatement.InsertIntoClause();
/* 1175 */         parseInsert0((SQLInsertInto)insertInto);
/* 1176 */         item.setThen(insertInto);
/*      */         
/* 1178 */         clause.addItem(item);
/*      */       } 
/*      */       
/* 1181 */       if (this.lexer.token() == Token.ELSE) {
/* 1182 */         this.lexer.nextToken();
/*      */         
/* 1184 */         OracleMultiInsertStatement.InsertIntoClause insertInto = new OracleMultiInsertStatement.InsertIntoClause();
/* 1185 */         parseInsert0((SQLInsertInto)insertInto, false);
/* 1186 */         clause.setElseItem(insertInto);
/*      */       } 
/* 1188 */       stmt.addEntry((OracleMultiInsertStatement.Entry)clause);
/*      */     } 
/*      */     
/* 1191 */     OracleSelect oracleSelect = createSQLSelectParser().select();
/* 1192 */     stmt.setSubQuery((SQLSelect)oracleSelect);
/*      */     
/* 1194 */     return stmt;
/*      */   }
/*      */   
/*      */   private OracleExceptionStatement parseException() {
/* 1198 */     accept(Token.EXCEPTION);
/* 1199 */     OracleExceptionStatement stmt = new OracleExceptionStatement();
/*      */     
/*      */     do {
/* 1202 */       accept(Token.WHEN);
/* 1203 */       OracleExceptionStatement.Item item = new OracleExceptionStatement.Item();
/* 1204 */       item.setWhen(this.exprParser.expr());
/* 1205 */       accept(Token.THEN);
/* 1206 */       parseStatementList(item.getStatements());
/*      */       
/* 1208 */       stmt.addItem(item);
/*      */     }
/* 1210 */     while (this.lexer.token() == Token.WHEN);
/*      */ 
/*      */ 
/*      */     
/* 1214 */     return stmt;
/*      */   }
/*      */   
/*      */   public OracleReturningClause parseReturningClause() {
/* 1218 */     OracleReturningClause clause = null;
/*      */     
/* 1220 */     if (this.lexer.token() == Token.RETURNING) {
/* 1221 */       this.lexer.nextToken();
/* 1222 */       clause = new OracleReturningClause();
/*      */       
/*      */       while (true) {
/* 1225 */         SQLExpr item = this.exprParser.expr();
/* 1226 */         clause.addItem(item);
/* 1227 */         if (this.lexer.token() == Token.COMMA) {
/* 1228 */           this.lexer.nextToken();
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/* 1233 */       accept(Token.INTO);
/*      */       while (true) {
/* 1235 */         SQLExpr item = this.exprParser.expr();
/* 1236 */         clause.addValue(item);
/* 1237 */         if (this.lexer.token() == Token.COMMA) {
/* 1238 */           this.lexer.nextToken();
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/*      */     } 
/* 1244 */     return clause;
/*      */   }
/*      */   
/*      */   public OracleExplainStatement parseExplain() {
/* 1248 */     accept(Token.EXPLAIN);
/* 1249 */     acceptIdentifier("PLAN");
/* 1250 */     OracleExplainStatement stmt = new OracleExplainStatement();
/*      */     
/* 1252 */     if (this.lexer.token() == Token.SET) {
/* 1253 */       this.lexer.nextToken();
/* 1254 */       acceptIdentifier("STATEMENT_ID");
/* 1255 */       accept(Token.EQ);
/* 1256 */       stmt.setStatementId((SQLCharExpr)this.exprParser.primary());
/*      */     } 
/*      */     
/* 1259 */     if (this.lexer.token() == Token.INTO) {
/* 1260 */       this.lexer.nextToken();
/* 1261 */       stmt.setInto((SQLExpr)this.exprParser.name());
/*      */     } 
/*      */     
/* 1264 */     accept(Token.FOR);
/* 1265 */     stmt.setStatement(parseStatement());
/*      */     
/* 1267 */     return stmt;
/*      */   }
/*      */   
/*      */   public OracleDeleteStatement parseDeleteStatement() {
/* 1271 */     OracleDeleteStatement deleteStatement = new OracleDeleteStatement();
/*      */     
/* 1273 */     if (this.lexer.token() == Token.DELETE) {
/* 1274 */       this.lexer.nextToken();
/*      */       
/* 1276 */       if (this.lexer.token() == Token.COMMENT) {
/* 1277 */         this.lexer.nextToken();
/*      */       }
/*      */       
/* 1280 */       parseHints(deleteStatement.getHints());
/*      */       
/* 1282 */       if (this.lexer.token() == Token.FROM) {
/* 1283 */         this.lexer.nextToken();
/*      */       }
/*      */       
/* 1286 */       if (identifierEquals("ONLY")) {
/* 1287 */         this.lexer.nextToken();
/* 1288 */         accept(Token.LPAREN);
/*      */         
/* 1290 */         SQLName tableName = this.exprParser.name();
/* 1291 */         deleteStatement.setTableName(tableName);
/*      */         
/* 1293 */         accept(Token.RPAREN);
/* 1294 */       } else if (this.lexer.token() == Token.LPAREN) {
/* 1295 */         SQLTableSource tableSource = createSQLSelectParser().parseTableSource();
/* 1296 */         deleteStatement.setTableSource(tableSource);
/*      */       } else {
/* 1298 */         SQLName tableName = this.exprParser.name();
/* 1299 */         deleteStatement.setTableName(tableName);
/*      */       } 
/*      */       
/* 1302 */       deleteStatement.setAlias(as());
/*      */     } 
/*      */     
/* 1305 */     if (this.lexer.token() == Token.WHERE) {
/* 1306 */       this.lexer.nextToken();
/* 1307 */       deleteStatement.setWhere(this.exprParser.expr());
/*      */     } 
/*      */     
/* 1310 */     if (this.lexer.token() == Token.RETURNING) {
/* 1311 */       OracleReturningClause clause = parseReturningClause();
/* 1312 */       deleteStatement.setReturning(clause);
/*      */     } 
/* 1314 */     if (identifierEquals("RETURN") || identifierEquals("RETURNING")) {
/* 1315 */       throw new ParserException("TODO");
/*      */     }
/*      */     
/* 1318 */     if (identifierEquals("LOG")) {
/* 1319 */       throw new ParserException("TODO");
/*      */     }
/*      */     
/* 1322 */     return deleteStatement;
/*      */   }
/*      */   
/*      */   public SQLStatement parseCreateDbLink() {
/* 1326 */     accept(Token.CREATE);
/*      */     
/* 1328 */     OracleCreateDatabaseDbLinkStatement dbLink = new OracleCreateDatabaseDbLinkStatement();
/*      */     
/* 1330 */     if (identifierEquals("SHARED")) {
/* 1331 */       dbLink.setShared(true);
/* 1332 */       this.lexer.nextToken();
/*      */     } 
/*      */     
/* 1335 */     if (identifierEquals("PUBLIC")) {
/* 1336 */       dbLink.setPublic(true);
/* 1337 */       this.lexer.nextToken();
/*      */     } 
/*      */     
/* 1340 */     accept(Token.DATABASE);
/* 1341 */     acceptIdentifier("LINK");
/*      */     
/* 1343 */     dbLink.setName(this.exprParser.name());
/*      */     
/* 1345 */     if (this.lexer.token() == Token.CONNECT) {
/* 1346 */       this.lexer.nextToken();
/* 1347 */       accept(Token.TO);
/*      */       
/* 1349 */       dbLink.setUser(this.exprParser.name());
/*      */       
/* 1351 */       if (this.lexer.token() == Token.IDENTIFIED) {
/* 1352 */         this.lexer.nextToken();
/* 1353 */         accept(Token.BY);
/* 1354 */         dbLink.setPassword(this.lexer.stringVal());
/*      */         
/* 1356 */         if (this.lexer.token() == Token.IDENTIFIER) {
/* 1357 */           this.lexer.nextToken();
/*      */         } else {
/* 1359 */           accept(Token.LITERAL_ALIAS);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1364 */     if (identifierEquals("AUTHENTICATED")) {
/* 1365 */       this.lexer.nextToken();
/* 1366 */       accept(Token.BY);
/* 1367 */       dbLink.setAuthenticatedUser((SQLExpr)this.exprParser.name());
/*      */       
/* 1369 */       accept(Token.IDENTIFIED);
/* 1370 */       accept(Token.BY);
/* 1371 */       dbLink.setPassword(this.lexer.stringVal());
/* 1372 */       accept(Token.IDENTIFIER);
/*      */     } 
/*      */     
/* 1375 */     if (this.lexer.token() == Token.USING) {
/* 1376 */       this.lexer.nextToken();
/* 1377 */       dbLink.setUsing(this.exprParser.expr());
/*      */     } 
/*      */     
/* 1380 */     return (SQLStatement)dbLink;
/*      */   }
/*      */   
/*      */   public OracleCreateIndexStatement parseCreateIndex(boolean acceptCreate) {
/* 1384 */     if (acceptCreate) {
/* 1385 */       accept(Token.CREATE);
/*      */     }
/*      */     
/* 1388 */     OracleCreateIndexStatement stmt = new OracleCreateIndexStatement();
/* 1389 */     if (this.lexer.token() == Token.UNIQUE) {
/* 1390 */       stmt.setType("UNIQUE");
/* 1391 */       this.lexer.nextToken();
/* 1392 */     } else if (identifierEquals("BITMAP")) {
/* 1393 */       stmt.setType("BITMAP");
/* 1394 */       this.lexer.nextToken();
/*      */     } 
/*      */     
/* 1397 */     accept(Token.INDEX);
/*      */     
/* 1399 */     stmt.setName(this.exprParser.name());
/*      */     
/* 1401 */     accept(Token.ON);
/*      */     
/* 1403 */     stmt.setTable(this.exprParser.name());
/*      */     
/* 1405 */     accept(Token.LPAREN);
/*      */     
/*      */     while (true) {
/* 1408 */       SQLSelectOrderByItem item = this.exprParser.parseSelectOrderByItem();
/* 1409 */       stmt.addItem(item);
/* 1410 */       if (this.lexer.token() == Token.COMMA) {
/* 1411 */         this.lexer.nextToken();
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 1416 */     accept(Token.RPAREN);
/*      */     
/*      */     while (true) {
/* 1419 */       while (this.lexer.token() == Token.TABLESPACE) {
/* 1420 */         this.lexer.nextToken();
/* 1421 */         stmt.setTablespace(this.exprParser.name());
/*      */       } 
/* 1423 */       if (this.lexer.token() == Token.PCTFREE) {
/* 1424 */         this.lexer.nextToken();
/* 1425 */         stmt.setPtcfree(this.exprParser.expr()); continue;
/*      */       } 
/* 1427 */       if (this.lexer.token() == Token.INITRANS) {
/* 1428 */         this.lexer.nextToken();
/* 1429 */         stmt.setInitrans(this.exprParser.expr()); continue;
/*      */       } 
/* 1431 */       if (this.lexer.token() == Token.MAXTRANS) {
/* 1432 */         this.lexer.nextToken();
/* 1433 */         stmt.setMaxtrans(this.exprParser.expr()); continue;
/*      */       } 
/* 1435 */       if (this.lexer.token() == Token.COMPUTE) {
/* 1436 */         this.lexer.nextToken();
/* 1437 */         acceptIdentifier("STATISTICS");
/* 1438 */         stmt.setComputeStatistics(true); continue;
/*      */       } 
/* 1440 */       if (this.lexer.token() == Token.ENABLE) {
/* 1441 */         this.lexer.nextToken();
/* 1442 */         stmt.setEnable(Boolean.valueOf(true)); continue;
/*      */       } 
/* 1444 */       if (this.lexer.token() == Token.DISABLE) {
/* 1445 */         this.lexer.nextToken();
/* 1446 */         stmt.setEnable(Boolean.valueOf(false)); continue;
/*      */       } 
/* 1448 */       if (identifierEquals("ONLINE")) {
/* 1449 */         this.lexer.nextToken();
/* 1450 */         stmt.setOnline(true); continue;
/*      */       } 
/* 1452 */       if (identifierEquals("NOPARALLEL")) {
/* 1453 */         this.lexer.nextToken();
/* 1454 */         stmt.setNoParallel(true); continue;
/*      */       } 
/* 1456 */       if (identifierEquals("PARALLEL")) {
/* 1457 */         this.lexer.nextToken();
/* 1458 */         stmt.setParallel(this.exprParser.expr()); continue;
/*      */       } 
/* 1460 */       if (this.lexer.token() == Token.INDEX) {
/* 1461 */         this.lexer.nextToken();
/* 1462 */         acceptIdentifier("ONLY");
/* 1463 */         acceptIdentifier("TOPLEVEL");
/* 1464 */         stmt.setIndexOnlyTopLevel(true);
/*      */         
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 1470 */     return stmt;
/*      */   }
/*      */   
/*      */   public SQLCreateSequenceStatement parseCreateSequence(boolean acceptCreate) {
/* 1474 */     if (acceptCreate) {
/* 1475 */       accept(Token.CREATE);
/*      */     }
/*      */     
/* 1478 */     accept(Token.SEQUENCE);
/*      */     
/* 1480 */     SQLCreateSequenceStatement stmt = new SQLCreateSequenceStatement();
/* 1481 */     stmt.setDbType("oracle");
/* 1482 */     stmt.setName(this.exprParser.name());
/*      */     
/*      */     while (true) {
/* 1485 */       while (this.lexer.token() == Token.START) {
/* 1486 */         this.lexer.nextToken();
/* 1487 */         accept(Token.WITH);
/* 1488 */         stmt.setStartWith(this.exprParser.expr());
/*      */       } 
/* 1490 */       if (identifierEquals("INCREMENT")) {
/* 1491 */         this.lexer.nextToken();
/* 1492 */         accept(Token.BY);
/* 1493 */         stmt.setIncrementBy(this.exprParser.expr()); continue;
/*      */       } 
/* 1495 */       if (this.lexer.token() == Token.CACHE) {
/* 1496 */         this.lexer.nextToken();
/* 1497 */         stmt.setCache(Boolean.TRUE); continue;
/*      */       } 
/* 1499 */       if (this.lexer.token() == Token.NOCACHE) {
/* 1500 */         this.lexer.nextToken();
/* 1501 */         stmt.setCache(Boolean.FALSE); continue;
/*      */       } 
/* 1503 */       if (identifierEquals("CYCLE")) {
/* 1504 */         this.lexer.nextToken();
/* 1505 */         stmt.setCycle(Boolean.TRUE); continue;
/*      */       } 
/* 1507 */       if (identifierEquals("NOCYCLE")) {
/* 1508 */         this.lexer.nextToken();
/* 1509 */         stmt.setCycle(Boolean.FALSE); continue;
/*      */       } 
/* 1511 */       if (identifierEquals("MINVALUE")) {
/* 1512 */         this.lexer.nextToken();
/* 1513 */         stmt.setMinValue(this.exprParser.expr()); continue;
/*      */       } 
/* 1515 */       if (identifierEquals("MAXVALUE")) {
/* 1516 */         this.lexer.nextToken();
/* 1517 */         stmt.setMaxValue(this.exprParser.expr()); continue;
/*      */       } 
/* 1519 */       if (identifierEquals("NOMAXVALUE")) {
/* 1520 */         this.lexer.nextToken();
/* 1521 */         stmt.setNoMaxValue(true); continue;
/*      */       } 
/* 1523 */       if (identifierEquals("NOMINVALUE")) {
/* 1524 */         this.lexer.nextToken();
/* 1525 */         stmt.setNoMinValue(true);
/*      */         
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 1531 */     return stmt;
/*      */   }
/*      */   
/*      */   public SQLCreateProcedureStatement parseCreateProcedure() {
/* 1535 */     SQLCreateProcedureStatement stmt = new SQLCreateProcedureStatement();
/* 1536 */     accept(Token.CREATE);
/* 1537 */     if (this.lexer.token() == Token.OR) {
/* 1538 */       this.lexer.nextToken();
/* 1539 */       accept(Token.REPLACE);
/* 1540 */       stmt.setOrReplace(true);
/*      */     } 
/*      */     
/* 1543 */     accept(Token.PROCEDURE);
/*      */     
/* 1545 */     stmt.setName(this.exprParser.name());
/*      */     
/* 1547 */     if (this.lexer.token() == Token.LPAREN) {
/* 1548 */       this.lexer.nextToken();
/* 1549 */       parserParameters(stmt.getParameters());
/* 1550 */       accept(Token.RPAREN);
/*      */     } 
/*      */     
/* 1553 */     accept(Token.AS);
/*      */     
/* 1555 */     SQLBlockStatement block = parseBlock();
/*      */     
/* 1557 */     stmt.setBlock((SQLStatement)block);
/*      */     
/* 1559 */     return stmt;
/*      */   }
/*      */   
/*      */   public SQLUpdateStatement parseUpdateStatement() {
/* 1563 */     return (SQLUpdateStatement)(new OracleUpdateParser(this.lexer)).parseUpdateStatement();
/*      */   }
/*      */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\parser\OracleStatementParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */