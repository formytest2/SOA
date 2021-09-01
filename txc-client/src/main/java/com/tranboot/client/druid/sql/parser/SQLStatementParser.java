/*      */ package com.tranboot.client.druid.sql.parser;
/*      */ 
/*      */

import com.tranboot.client.druid.sql.ast.*;
import com.tranboot.client.druid.sql.ast.expr.SQLCharExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLLiteralExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLVariantRefExpr;
import com.tranboot.client.druid.sql.ast.statement.*;

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
/*      */ 
/*      */ 
/*      */ 
/*      */ public class SQLStatementParser
/*      */   extends SQLParser
/*      */ {
/*      */   protected SQLExprParser exprParser;
/*      */   protected boolean parseCompleteValues = true;
/*  111 */   protected int parseValuesSize = 3;
/*      */   
/*      */   public SQLStatementParser(String sql) {
/*  114 */     this(sql, (String)null);
/*      */   }
/*      */   
/*      */   public SQLStatementParser(String sql, String dbType) {
/*  118 */     this(new SQLExprParser(sql, dbType));
/*      */   }
/*      */   
/*      */   public SQLStatementParser(SQLExprParser exprParser) {
/*  122 */     super(exprParser.getLexer(), exprParser.getDbType());
/*  123 */     this.exprParser = exprParser;
/*      */   }
/*      */   
/*      */   protected SQLStatementParser(Lexer lexer, String dbType) {
/*  127 */     super(lexer, dbType);
/*      */   }
/*      */   
/*      */   public boolean isKeepComments() {
/*  131 */     return this.lexer.isKeepComments();
/*      */   }
/*      */   
/*      */   public void setKeepComments(boolean keepComments) {
/*  135 */     this.lexer.setKeepComments(keepComments);
/*      */   }
/*      */   
/*      */   public SQLExprParser getExprParser() {
/*  139 */     return this.exprParser;
/*      */   }
/*      */   
/*      */   public List<SQLStatement> parseStatementList() {
/*  143 */     List<SQLStatement> statementList = new ArrayList<>();
/*  144 */     parseStatementList(statementList);
/*  145 */     return statementList;
/*      */   }
/*      */   
/*      */   public void parseStatementList(List<SQLStatement> statementList) {
/*  149 */     parseStatementList(statementList, -1);
/*      */   }
/*      */   
/*      */   public void parseStatementList(List<SQLStatement> statementList, int max) {
/*      */     while (true) {
/*  154 */       if (max != -1 && 
/*  155 */         statementList.size() >= max) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*  160 */       if (this.lexer.token() == Token.EOF || this.lexer.token() == Token.END) {
/*  161 */         if (this.lexer.isKeepComments() && this.lexer.hasComment() && statementList.size() > 0) {
/*  162 */           SQLStatement stmt = statementList.get(statementList.size() - 1);
/*  163 */           stmt.addAfterComment(this.lexer.readAndResetComments());
/*      */         } 
/*      */         
/*      */         return;
/*      */       } 
/*  168 */       if (this.lexer.token() == Token.SEMI) {
/*  169 */         int line0 = this.lexer.getLine();
/*  170 */         this.lexer.nextToken();
/*  171 */         int line1 = this.lexer.getLine();
/*      */         
/*  173 */         if (this.lexer.isKeepComments() && statementList.size() > 0) {
/*  174 */           SQLStatement stmt = statementList.get(statementList.size() - 1);
/*  175 */           if (line1 - line0 <= 1) {
/*  176 */             stmt.addAfterComment(this.lexer.readAndResetComments());
/*      */           }
/*  178 */           stmt.getAttributes().put("format.semi", Boolean.TRUE);
/*      */         } 
/*      */         
/*      */         continue;
/*      */       } 
/*  183 */       if (this.lexer.token() == Token.SELECT) {
/*  184 */         statementList.add(parseSelect());
/*      */         
/*      */         continue;
/*      */       } 
/*  188 */       if (this.lexer.token() == Token.UPDATE) {
/*  189 */         statementList.add(parseUpdateStatement());
/*      */         
/*      */         continue;
/*      */       } 
/*  193 */       if (this.lexer.token() == Token.CREATE) {
/*      */         
/*  195 */         statementList.add(parseCreate());
/*      */         
/*      */         continue;
/*      */       } 
/*  199 */       if (this.lexer.token() == Token.INSERT) {
/*  200 */         SQLStatement insertStatement = parseInsert();
/*  201 */         statementList.add(insertStatement);
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/*  206 */       if (this.lexer.token() == Token.DELETE) {
/*  207 */         statementList.add(parseDeleteStatement());
/*      */         
/*      */         continue;
/*      */       } 
/*  211 */       if (this.lexer.token() == Token.EXPLAIN) {
/*  212 */         statementList.add(parseExplain());
/*      */         
/*      */         continue;
/*      */       } 
/*  216 */       if (this.lexer.token() == Token.SET) {
/*  217 */         statementList.add(parseSet());
/*      */         
/*      */         continue;
/*      */       } 
/*  221 */       if (this.lexer.token() == Token.ALTER) {
/*  222 */         statementList.add(parseAlter());
/*      */         
/*      */         continue;
/*      */       } 
/*  226 */       if (this.lexer.token() == Token.DROP) {
/*  227 */         List<String> beforeComments = null;
/*  228 */         if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
/*  229 */           beforeComments = this.lexer.readAndResetComments();
/*      */         }
/*      */         
/*  232 */         this.lexer.nextToken();
/*      */         
/*  234 */         if (this.lexer.token() == Token.TABLE || identifierEquals("TEMPORARY")) {
/*      */           
/*  236 */           SQLDropTableStatement stmt = parseDropTable(false);
/*      */           
/*  238 */           if (beforeComments != null) {
/*  239 */             stmt.addBeforeComment(beforeComments);
/*      */           }
/*      */           
/*  242 */           statementList.add(stmt); continue;
/*      */         } 
/*  244 */         if (this.lexer.token() == Token.USER) {
/*  245 */           SQLDropUserStatement sQLDropUserStatement = parseDropUser();
/*  246 */           if (beforeComments != null) {
/*  247 */             sQLDropUserStatement.addBeforeComment(beforeComments);
/*      */           }
/*      */           
/*  250 */           statementList.add(sQLDropUserStatement); continue;
/*      */         } 
/*  252 */         if (this.lexer.token() == Token.INDEX) {
/*  253 */           SQLStatement stmt = parseDropIndex();
/*  254 */           if (beforeComments != null) {
/*  255 */             stmt.addBeforeComment(beforeComments);
/*      */           }
/*      */           
/*  258 */           statementList.add(stmt); continue;
/*      */         } 
/*  260 */         if (this.lexer.token() == Token.VIEW) {
/*  261 */           SQLDropViewStatement sQLDropViewStatement = parseDropView(false);
/*      */           
/*  263 */           if (beforeComments != null) {
/*  264 */             sQLDropViewStatement.addBeforeComment(beforeComments);
/*      */           }
/*      */           
/*  267 */           statementList.add(sQLDropViewStatement); continue;
/*      */         } 
/*  269 */         if (this.lexer.token() == Token.TRIGGER) {
/*  270 */           SQLDropTriggerStatement sQLDropTriggerStatement = parseDropTrigger(false);
/*      */           
/*  272 */           if (beforeComments != null) {
/*  273 */             sQLDropTriggerStatement.addBeforeComment(beforeComments);
/*      */           }
/*      */           
/*  276 */           statementList.add(sQLDropTriggerStatement); continue;
/*      */         } 
/*  278 */         if (this.lexer.token() == Token.DATABASE) {
/*  279 */           SQLDropDatabaseStatement sQLDropDatabaseStatement = parseDropDatabase(false);
/*      */           
/*  281 */           if (beforeComments != null) {
/*  282 */             sQLDropDatabaseStatement.addBeforeComment(beforeComments);
/*      */           }
/*      */           
/*  285 */           statementList.add(sQLDropDatabaseStatement); continue;
/*      */         } 
/*  287 */         if (this.lexer.token() == Token.FUNCTION) {
/*  288 */           SQLDropFunctionStatement sQLDropFunctionStatement = parseDropFunction(false);
/*      */           
/*  290 */           if (beforeComments != null) {
/*  291 */             sQLDropFunctionStatement.addBeforeComment(beforeComments);
/*      */           }
/*      */           
/*  294 */           statementList.add(sQLDropFunctionStatement); continue;
/*      */         } 
/*  296 */         if (this.lexer.token() == Token.TABLESPACE) {
/*  297 */           SQLDropTableSpaceStatement sQLDropTableSpaceStatement = parseDropTablespace(false);
/*      */           
/*  299 */           if (beforeComments != null) {
/*  300 */             sQLDropTableSpaceStatement.addBeforeComment(beforeComments);
/*      */           }
/*      */           
/*  303 */           statementList.add(sQLDropTableSpaceStatement); continue;
/*      */         } 
/*  305 */         if (this.lexer.token() == Token.PROCEDURE) {
/*  306 */           SQLDropProcedureStatement sQLDropProcedureStatement = parseDropProcedure(false);
/*      */           
/*  308 */           if (beforeComments != null) {
/*  309 */             sQLDropProcedureStatement.addBeforeComment(beforeComments);
/*      */           }
/*      */           
/*  312 */           statementList.add(sQLDropProcedureStatement); continue;
/*      */         } 
/*  314 */         if (this.lexer.token() == Token.SEQUENCE) {
/*  315 */           SQLDropSequenceStatement sQLDropSequenceStatement = parseDropSequece(false);
/*      */           
/*  317 */           if (beforeComments != null) {
/*  318 */             sQLDropSequenceStatement.addBeforeComment(beforeComments);
/*      */           }
/*      */           
/*  321 */           statementList.add(sQLDropSequenceStatement);
/*      */           continue;
/*      */         } 
/*  324 */         throw new ParserException("TODO " + this.lexer.token());
/*      */       } 
/*      */ 
/*      */       
/*  328 */       if (this.lexer.token() == Token.TRUNCATE) {
/*  329 */         SQLStatement stmt = parseTruncate();
/*  330 */         statementList.add(stmt);
/*      */         
/*      */         continue;
/*      */       } 
/*  334 */       if (this.lexer.token() == Token.USE) {
/*  335 */         SQLUseStatement sQLUseStatement = parseUse();
/*  336 */         statementList.add(sQLUseStatement);
/*      */         
/*      */         continue;
/*      */       } 
/*  340 */       if (this.lexer.token() == Token.GRANT) {
/*  341 */         SQLGrantStatement sQLGrantStatement = parseGrant();
/*  342 */         statementList.add(sQLGrantStatement);
/*      */         
/*      */         continue;
/*      */       } 
/*  346 */       if (this.lexer.token() == Token.REVOKE) {
/*  347 */         SQLRevokeStatement sQLRevokeStatement = parseRevoke();
/*  348 */         statementList.add(sQLRevokeStatement);
/*      */         
/*      */         continue;
/*      */       } 
/*  352 */       if (this.lexer.token() == Token.LBRACE || identifierEquals("CALL")) {
/*  353 */         SQLCallStatement stmt = parseCall();
/*  354 */         statementList.add(stmt);
/*      */         
/*      */         continue;
/*      */       } 
/*  358 */       if (identifierEquals("RENAME")) {
/*  359 */         SQLStatement stmt = parseRename();
/*  360 */         statementList.add(stmt);
/*      */         
/*      */         continue;
/*      */       } 
/*  364 */       if (identifierEquals("RELEASE")) {
/*  365 */         SQLStatement stmt = parseReleaseSavePoint();
/*  366 */         statementList.add(stmt);
/*      */         
/*      */         continue;
/*      */       } 
/*  370 */       if (identifierEquals("SAVEPOINT")) {
/*  371 */         SQLStatement stmt = parseSavePoint();
/*  372 */         statementList.add(stmt);
/*      */         
/*      */         continue;
/*      */       } 
/*  376 */       if (identifierEquals("ROLLBACK")) {
/*  377 */         SQLRollbackStatement stmt = parseRollback();
/*      */         
/*  379 */         statementList.add(stmt);
/*      */         
/*      */         continue;
/*      */       } 
/*  383 */       if (identifierEquals("COMMIT")) {
/*  384 */         SQLStatement stmt = parseCommit();
/*      */         
/*  386 */         statementList.add(stmt);
/*      */         
/*      */         continue;
/*      */       } 
/*  390 */       if (this.lexer.token() == Token.SHOW) {
/*  391 */         SQLStatement stmt = parseShow();
/*      */         
/*  393 */         statementList.add(stmt);
/*      */         
/*      */         continue;
/*      */       } 
/*  397 */       if (this.lexer.token() == Token.LPAREN) {
/*  398 */         char markChar = this.lexer.current();
/*  399 */         int markBp = this.lexer.bp();
/*  400 */         this.lexer.nextToken();
/*  401 */         if (this.lexer.token() == Token.SELECT) {
/*  402 */           this.lexer.reset(markBp, markChar, Token.LPAREN);
/*  403 */           SQLStatement stmt = parseSelect();
/*  404 */           statementList.add(stmt);
/*      */           
/*      */           continue;
/*      */         } 
/*      */       } 
/*  409 */       if (this.lexer.token() == Token.MERGE) {
/*  410 */         statementList.add(parseMerge());
/*      */         
/*      */         continue;
/*      */       } 
/*  414 */       if (parseStatementListDialect(statementList)) {
/*      */         continue;
/*      */       }
/*      */       
/*  418 */       if (this.lexer.token() == Token.COMMENT) {
/*  419 */         statementList.add(parseComment());
/*      */         
/*      */         continue;
/*      */       } 
/*  423 */       if (this.lexer.token() == Token.UPSERT || identifierEquals("UPSERT")) {
/*  424 */         SQLStatement stmt = parseUpsert();
/*  425 */         statementList.add(stmt);
/*      */ 
/*      */         
/*      */         continue;
/*      */       } 
/*      */ 
/*      */       
/*  432 */       printError(this.lexer.token());
/*      */     } 
/*      */   }
/*      */   
/*      */   public SQLStatement parseUpsert() {
/*  437 */     SQLInsertStatement insertStatement = new SQLInsertStatement();
/*      */     
/*  439 */     if (this.lexer.token() == Token.UPSERT || identifierEquals("UPSERT")) {
/*  440 */       this.lexer.nextToken();
/*  441 */       insertStatement.setUpsert(true);
/*      */     } 
/*      */     
/*  444 */     parseInsert0((SQLInsertInto)insertStatement);
/*  445 */     return (SQLStatement)insertStatement;
/*      */   }
/*      */   
/*      */   public SQLRollbackStatement parseRollback() {
/*  449 */     this.lexer.nextToken();
/*      */     
/*  451 */     if (identifierEquals("WORK")) {
/*  452 */       this.lexer.nextToken();
/*      */     }
/*      */     
/*  455 */     SQLRollbackStatement stmt = new SQLRollbackStatement(getDbType());
/*      */     
/*  457 */     if (this.lexer.token() == Token.TO) {
/*  458 */       this.lexer.nextToken();
/*      */       
/*  460 */       if (identifierEquals("SAVEPOINT")) {
/*  461 */         this.lexer.nextToken();
/*      */       }
/*      */       
/*  464 */       stmt.setTo(this.exprParser.name());
/*      */     } 
/*  466 */     return stmt;
/*      */   }
/*      */   
/*      */   public SQLStatement parseCommit() {
/*  470 */     throw new ParserException("TODO " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */   }
/*      */   
/*      */   public SQLStatement parseShow() {
/*  474 */     throw new ParserException("TODO " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */   }
/*      */   
/*      */   public SQLUseStatement parseUse() {
/*  478 */     accept(Token.USE);
/*  479 */     SQLUseStatement stmt = new SQLUseStatement(getDbType());
/*  480 */     stmt.setDatabase(this.exprParser.name());
/*  481 */     return stmt;
/*      */   }
/*      */   
/*      */   public SQLGrantStatement parseGrant() {
/*  485 */     accept(Token.GRANT);
/*  486 */     SQLGrantStatement stmt = new SQLGrantStatement(getDbType());
/*      */     
/*  488 */     parsePrivileages(stmt.getPrivileges(), (SQLObject)stmt);
/*      */     
/*  490 */     if (this.lexer.token() == Token.ON) {
/*  491 */       this.lexer.nextToken();
/*      */       
/*  493 */       if (this.lexer.token() == Token.PROCEDURE) {
/*  494 */         this.lexer.nextToken();
/*  495 */         stmt.setObjectType(SQLObjectType.PROCEDURE);
/*  496 */       } else if (this.lexer.token() == Token.FUNCTION) {
/*  497 */         this.lexer.nextToken();
/*  498 */         stmt.setObjectType(SQLObjectType.FUNCTION);
/*  499 */       } else if (this.lexer.token() == Token.TABLE) {
/*  500 */         this.lexer.nextToken();
/*  501 */         stmt.setObjectType(SQLObjectType.TABLE);
/*  502 */       } else if (this.lexer.token() == Token.USER) {
/*  503 */         this.lexer.nextToken();
/*  504 */         stmt.setObjectType(SQLObjectType.USER);
/*  505 */       } else if (this.lexer.token() == Token.DATABASE) {
/*  506 */         this.lexer.nextToken();
/*  507 */         stmt.setObjectType(SQLObjectType.DATABASE);
/*      */       } 
/*      */       
/*  510 */       if (stmt.getObjectType() != null && this.lexer.token() == Token.COLONCOLON) {
/*  511 */         this.lexer.nextToken();
/*      */       }
/*      */       
/*  514 */       SQLExpr expr = this.exprParser.expr();
/*  515 */       if (stmt.getObjectType() == SQLObjectType.TABLE || stmt.getObjectType() == null) {
/*  516 */         stmt.setOn((SQLObject)new SQLExprTableSource(expr));
/*      */       } else {
/*  518 */         stmt.setOn((SQLObject)expr);
/*      */       } 
/*      */     } 
/*      */     
/*  522 */     if (this.lexer.token() == Token.TO) {
/*  523 */       this.lexer.nextToken();
/*  524 */       stmt.setTo(this.exprParser.expr());
/*      */     } 
/*      */     
/*  527 */     if (this.lexer.token() == Token.WITH) {
/*  528 */       this.lexer.nextToken();
/*      */       
/*      */       while (true) {
/*  531 */         while (identifierEquals("MAX_QUERIES_PER_HOUR")) {
/*  532 */           this.lexer.nextToken();
/*  533 */           stmt.setMaxQueriesPerHour(this.exprParser.primary());
/*      */         } 
/*      */ 
/*      */         
/*  537 */         if (identifierEquals("MAX_UPDATES_PER_HOUR")) {
/*  538 */           this.lexer.nextToken();
/*  539 */           stmt.setMaxUpdatesPerHour(this.exprParser.primary());
/*      */           
/*      */           continue;
/*      */         } 
/*  543 */         if (identifierEquals("MAX_CONNECTIONS_PER_HOUR")) {
/*  544 */           this.lexer.nextToken();
/*  545 */           stmt.setMaxConnectionsPerHour(this.exprParser.primary());
/*      */           
/*      */           continue;
/*      */         } 
/*  549 */         if (identifierEquals("MAX_USER_CONNECTIONS")) {
/*  550 */           this.lexer.nextToken();
/*  551 */           stmt.setMaxUserConnections(this.exprParser.primary());
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  559 */     if (identifierEquals("ADMIN")) {
/*  560 */       this.lexer.nextToken();
/*  561 */       acceptIdentifier("OPTION");
/*  562 */       stmt.setAdminOption(true);
/*      */     } 
/*      */     
/*  565 */     if (this.lexer.token() == Token.IDENTIFIED) {
/*  566 */       this.lexer.nextToken();
/*  567 */       accept(Token.BY);
/*  568 */       stmt.setIdentifiedBy(this.exprParser.expr());
/*      */     } 
/*      */     
/*  571 */     return stmt;
/*      */   }
/*      */   
/*      */   protected void parsePrivileages(List<SQLExpr> privileges, SQLObject parent) {
/*      */     while (true) {
/*  576 */       String privilege = null;
/*  577 */       if (this.lexer.token() == Token.ALL) {
/*  578 */         this.lexer.nextToken();
/*  579 */         if (identifierEquals("PRIVILEGES")) {
/*  580 */           privilege = "ALL PRIVILEGES";
/*      */         } else {
/*  582 */           privilege = "ALL";
/*      */         } 
/*  584 */       } else if (this.lexer.token() == Token.SELECT) {
/*  585 */         privilege = "SELECT";
/*  586 */         this.lexer.nextToken();
/*  587 */       } else if (this.lexer.token() == Token.UPDATE) {
/*  588 */         privilege = "UPDATE";
/*  589 */         this.lexer.nextToken();
/*  590 */       } else if (this.lexer.token() == Token.DELETE) {
/*  591 */         privilege = "DELETE";
/*  592 */         this.lexer.nextToken();
/*  593 */       } else if (this.lexer.token() == Token.INSERT) {
/*  594 */         privilege = "INSERT";
/*  595 */         this.lexer.nextToken();
/*  596 */       } else if (this.lexer.token() == Token.INDEX) {
/*  597 */         this.lexer.nextToken();
/*  598 */         privilege = "INDEX";
/*  599 */       } else if (this.lexer.token() == Token.TRIGGER) {
/*  600 */         this.lexer.nextToken();
/*  601 */         privilege = "TRIGGER";
/*  602 */       } else if (this.lexer.token() == Token.REFERENCES) {
/*  603 */         privilege = "REFERENCES";
/*  604 */         this.lexer.nextToken();
/*  605 */       } else if (this.lexer.token() == Token.CREATE) {
/*  606 */         this.lexer.nextToken();
/*      */         
/*  608 */         if (this.lexer.token() == Token.TABLE) {
/*  609 */           privilege = "CREATE TABLE";
/*  610 */           this.lexer.nextToken();
/*  611 */         } else if (this.lexer.token() == Token.SESSION) {
/*  612 */           privilege = "CREATE SESSION";
/*  613 */           this.lexer.nextToken();
/*  614 */         } else if (this.lexer.token() == Token.TABLESPACE) {
/*  615 */           privilege = "CREATE TABLESPACE";
/*  616 */           this.lexer.nextToken();
/*  617 */         } else if (this.lexer.token() == Token.USER) {
/*  618 */           privilege = "CREATE USER";
/*  619 */           this.lexer.nextToken();
/*  620 */         } else if (this.lexer.token() == Token.VIEW) {
/*  621 */           privilege = "CREATE VIEW";
/*  622 */           this.lexer.nextToken();
/*  623 */         } else if (this.lexer.token() == Token.ANY) {
/*  624 */           this.lexer.nextToken();
/*      */           
/*  626 */           if (this.lexer.token() == Token.TABLE) {
/*  627 */             this.lexer.nextToken();
/*  628 */             privilege = "CREATE ANY TABLE";
/*  629 */           } else if (identifierEquals("MATERIALIZED")) {
/*  630 */             this.lexer.nextToken();
/*  631 */             accept(Token.VIEW);
/*  632 */             privilege = "CREATE ANY MATERIALIZED VIEW";
/*      */           } else {
/*  634 */             throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */           } 
/*  636 */         } else if (identifierEquals("SYNONYM")) {
/*  637 */           privilege = "CREATE SYNONYM";
/*  638 */           this.lexer.nextToken();
/*  639 */         } else if (identifierEquals("ROUTINE")) {
/*  640 */           privilege = "CREATE ROUTINE";
/*  641 */           this.lexer.nextToken();
/*  642 */         } else if (identifierEquals("TEMPORARY")) {
/*  643 */           this.lexer.nextToken();
/*  644 */           accept(Token.TABLE);
/*  645 */           privilege = "CREATE TEMPORARY TABLE";
/*      */         } else {
/*  647 */           throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */         } 
/*  649 */       } else if (this.lexer.token() == Token.ALTER) {
/*  650 */         this.lexer.nextToken();
/*  651 */         if (this.lexer.token() == Token.TABLE) {
/*  652 */           privilege = "ALTER TABLE";
/*  653 */           this.lexer.nextToken();
/*  654 */         } else if (this.lexer.token() == Token.SESSION) {
/*  655 */           privilege = "ALTER SESSION";
/*  656 */           this.lexer.nextToken();
/*  657 */         } else if (this.lexer.token() == Token.ANY) {
/*  658 */           this.lexer.nextToken();
/*      */           
/*  660 */           if (this.lexer.token() == Token.TABLE) {
/*  661 */             this.lexer.nextToken();
/*  662 */             privilege = "ALTER ANY TABLE";
/*  663 */           } else if (identifierEquals("MATERIALIZED")) {
/*  664 */             this.lexer.nextToken();
/*  665 */             accept(Token.VIEW);
/*  666 */             privilege = "ALTER ANY MATERIALIZED VIEW";
/*      */           } else {
/*  668 */             throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */           } 
/*      */         } else {
/*  671 */           throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */         } 
/*  673 */       } else if (this.lexer.token() == Token.DROP) {
/*  674 */         this.lexer.nextToken();
/*  675 */         if (this.lexer.token() == Token.DROP) {
/*  676 */           privilege = "DROP TABLE";
/*  677 */           this.lexer.nextToken();
/*  678 */         } else if (this.lexer.token() == Token.SESSION) {
/*  679 */           privilege = "DROP SESSION";
/*  680 */           this.lexer.nextToken();
/*  681 */         } else if (this.lexer.token() == Token.ANY) {
/*  682 */           this.lexer.nextToken();
/*      */           
/*  684 */           if (this.lexer.token() == Token.TABLE) {
/*  685 */             this.lexer.nextToken();
/*  686 */             privilege = "DROP ANY TABLE";
/*  687 */           } else if (identifierEquals("MATERIALIZED")) {
/*  688 */             this.lexer.nextToken();
/*  689 */             accept(Token.VIEW);
/*  690 */             privilege = "DROP ANY MATERIALIZED VIEW";
/*      */           } else {
/*  692 */             throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */           } 
/*      */         } else {
/*  695 */           privilege = "DROP";
/*      */         } 
/*  697 */       } else if (identifierEquals("USAGE")) {
/*  698 */         privilege = "USAGE";
/*  699 */         this.lexer.nextToken();
/*  700 */       } else if (identifierEquals("EXECUTE")) {
/*  701 */         privilege = "EXECUTE";
/*  702 */         this.lexer.nextToken();
/*  703 */       } else if (identifierEquals("PROXY")) {
/*  704 */         privilege = "PROXY";
/*  705 */         this.lexer.nextToken();
/*  706 */       } else if (identifierEquals("QUERY")) {
/*  707 */         this.lexer.nextToken();
/*  708 */         acceptIdentifier("REWRITE");
/*  709 */         privilege = "QUERY REWRITE";
/*  710 */       } else if (identifierEquals("GLOBAL")) {
/*  711 */         this.lexer.nextToken();
/*  712 */         acceptIdentifier("QUERY");
/*  713 */         acceptIdentifier("REWRITE");
/*  714 */         privilege = "GLOBAL QUERY REWRITE";
/*  715 */       } else if (identifierEquals("INHERIT")) {
/*  716 */         this.lexer.nextToken();
/*  717 */         acceptIdentifier("PRIVILEGES");
/*  718 */         privilege = "INHERIT PRIVILEGES";
/*  719 */       } else if (identifierEquals("EVENT")) {
/*  720 */         this.lexer.nextToken();
/*  721 */         privilege = "EVENT";
/*  722 */       } else if (identifierEquals("FILE")) {
/*  723 */         this.lexer.nextToken();
/*  724 */         privilege = "FILE";
/*  725 */       } else if (this.lexer.token() == Token.GRANT) {
/*  726 */         this.lexer.nextToken();
/*  727 */         acceptIdentifier("OPTION");
/*  728 */         privilege = "GRANT OPTION";
/*  729 */       } else if (this.lexer.token() == Token.LOCK) {
/*  730 */         this.lexer.nextToken();
/*  731 */         acceptIdentifier("TABLES");
/*  732 */         privilege = "LOCK TABLES";
/*  733 */       } else if (identifierEquals("PROCESS")) {
/*  734 */         this.lexer.nextToken();
/*  735 */         privilege = "PROCESS";
/*  736 */       } else if (identifierEquals("RELOAD")) {
/*  737 */         this.lexer.nextToken();
/*  738 */         privilege = "RELOAD";
/*  739 */       } else if (identifierEquals("REPLICATION")) {
/*  740 */         this.lexer.nextToken();
/*  741 */         if (identifierEquals("SLAVE")) {
/*  742 */           this.lexer.nextToken();
/*  743 */           privilege = "REPLICATION SLAVE";
/*      */         } else {
/*  745 */           acceptIdentifier("CLIENT");
/*  746 */           privilege = "REPLICATION CLIENT";
/*      */         } 
/*  748 */       } else if (this.lexer.token() == Token.SHOW) {
/*  749 */         this.lexer.nextToken();
/*      */         
/*  751 */         if (this.lexer.token() == Token.VIEW) {
/*  752 */           this.lexer.nextToken();
/*  753 */           privilege = "SHOW VIEW";
/*      */         } else {
/*  755 */           acceptIdentifier("DATABASES");
/*  756 */           privilege = "SHOW DATABASES";
/*      */         } 
/*  758 */       } else if (identifierEquals("SHUTDOWN")) {
/*  759 */         this.lexer.nextToken();
/*  760 */         privilege = "SHUTDOWN";
/*  761 */       } else if (identifierEquals("SUPER")) {
/*  762 */         this.lexer.nextToken();
/*  763 */         privilege = "SUPER";
/*      */       }
/*  765 */       else if (identifierEquals("CONTROL")) {
/*  766 */         this.lexer.nextToken();
/*  767 */         privilege = "CONTROL";
/*  768 */       } else if (identifierEquals("IMPERSONATE")) {
/*  769 */         this.lexer.nextToken();
/*  770 */         privilege = "IMPERSONATE";
/*      */       } 
/*      */       
/*  773 */       if (privilege != null) {
/*  774 */         SQLExpr sQLExpr; SQLIdentifierExpr sQLIdentifierExpr = new SQLIdentifierExpr(privilege);
/*      */         
/*  776 */         if (this.lexer.token() == Token.LPAREN) {
/*  777 */           sQLExpr = this.exprParser.primaryRest((SQLExpr)sQLIdentifierExpr);
/*      */         }
/*      */         
/*  780 */         sQLExpr.setParent(parent);
/*  781 */         privileges.add(sQLExpr);
/*      */       } 
/*      */       
/*  784 */       if (this.lexer.token() == Token.COMMA) {
/*  785 */         this.lexer.nextToken();
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/*      */   }
/*      */   
/*      */   public SQLRevokeStatement parseRevoke() {
/*  793 */     accept(Token.REVOKE);
/*      */     
/*  795 */     SQLRevokeStatement stmt = new SQLRevokeStatement(getDbType());
/*      */     
/*  797 */     parsePrivileages(stmt.getPrivileges(), (SQLObject)stmt);
/*      */     
/*  799 */     if (this.lexer.token() == Token.ON) {
/*  800 */       this.lexer.nextToken();
/*      */       
/*  802 */       if (this.lexer.token() == Token.PROCEDURE) {
/*  803 */         this.lexer.nextToken();
/*  804 */         stmt.setObjectType(SQLObjectType.PROCEDURE);
/*  805 */       } else if (this.lexer.token() == Token.FUNCTION) {
/*  806 */         this.lexer.nextToken();
/*  807 */         stmt.setObjectType(SQLObjectType.FUNCTION);
/*  808 */       } else if (this.lexer.token() == Token.TABLE) {
/*  809 */         this.lexer.nextToken();
/*  810 */         stmt.setObjectType(SQLObjectType.TABLE);
/*  811 */       } else if (this.lexer.token() == Token.USER) {
/*  812 */         this.lexer.nextToken();
/*  813 */         stmt.setObjectType(SQLObjectType.USER);
/*      */       } 
/*      */       
/*  816 */       SQLExpr expr = this.exprParser.expr();
/*  817 */       if (stmt.getObjectType() == SQLObjectType.TABLE || stmt.getObjectType() == null) {
/*  818 */         stmt.setOn((SQLObject)new SQLExprTableSource(expr));
/*      */       } else {
/*  820 */         stmt.setOn((SQLObject)expr);
/*      */       } 
/*      */     } 
/*      */     
/*  824 */     if (this.lexer.token() == Token.FROM) {
/*  825 */       this.lexer.nextToken();
/*  826 */       stmt.setFrom(this.exprParser.expr());
/*      */     } 
/*      */     
/*  829 */     return stmt;
/*      */   }
/*      */   
/*      */   public SQLStatement parseSavePoint() {
/*  833 */     acceptIdentifier("SAVEPOINT");
/*  834 */     SQLSavePointStatement stmt = new SQLSavePointStatement(getDbType());
/*  835 */     stmt.setName((SQLExpr)this.exprParser.name());
/*  836 */     return (SQLStatement)stmt;
/*      */   }
/*      */   
/*      */   public SQLStatement parseReleaseSavePoint() {
/*  840 */     acceptIdentifier("RELEASE");
/*  841 */     acceptIdentifier("SAVEPOINT");
/*  842 */     SQLReleaseSavePointStatement stmt = new SQLReleaseSavePointStatement(getDbType());
/*  843 */     stmt.setName((SQLExpr)this.exprParser.name());
/*  844 */     return (SQLStatement)stmt;
/*      */   }
/*      */   
/*      */   public SQLStatement parseAlter() {
/*  848 */     accept(Token.ALTER);
/*      */     
/*  850 */     if (this.lexer.token() == Token.TABLE) {
/*  851 */       this.lexer.nextToken();
/*      */       
/*  853 */       SQLAlterTableStatement stmt = new SQLAlterTableStatement(getDbType());
/*  854 */       stmt.setName(this.exprParser.name());
/*      */       
/*      */       while (true) {
/*  857 */         while (this.lexer.token() == Token.DROP)
/*  858 */           parseAlterDrop(stmt); 
/*  859 */         if (identifierEquals("ADD")) {
/*  860 */           this.lexer.nextToken();
/*      */           
/*  862 */           boolean ifNotExists = false;
/*      */           
/*  864 */           if (this.lexer.token() == Token.IF) {
/*  865 */             this.lexer.nextToken();
/*  866 */             accept(Token.NOT);
/*  867 */             accept(Token.EXISTS);
/*  868 */             ifNotExists = true;
/*      */           } 
/*      */           
/*  871 */           if (this.lexer.token() == Token.PRIMARY) {
/*  872 */             SQLPrimaryKey primaryKey = this.exprParser.parsePrimaryKey();
/*  873 */             SQLAlterTableAddConstraint item = new SQLAlterTableAddConstraint((SQLConstraint)primaryKey);
/*  874 */             stmt.addItem((SQLAlterTableItem)item); continue;
/*  875 */           }  if (this.lexer.token() == Token.IDENTIFIER) {
/*  876 */             SQLAlterTableAddColumn item = parseAlterTableAddColumn();
/*  877 */             stmt.addItem((SQLAlterTableItem)item); continue;
/*  878 */           }  if (this.lexer.token() == Token.COLUMN) {
/*  879 */             this.lexer.nextToken();
/*  880 */             SQLAlterTableAddColumn item = parseAlterTableAddColumn();
/*  881 */             stmt.addItem((SQLAlterTableItem)item); continue;
/*  882 */           }  if (this.lexer.token() == Token.CHECK) {
/*  883 */             SQLCheck check = this.exprParser.parseCheck();
/*  884 */             SQLAlterTableAddConstraint item = new SQLAlterTableAddConstraint((SQLConstraint)check);
/*  885 */             stmt.addItem((SQLAlterTableItem)item); continue;
/*  886 */           }  if (this.lexer.token() == Token.CONSTRAINT) {
/*  887 */             SQLConstraint constraint = this.exprParser.parseConstaint();
/*  888 */             SQLAlterTableAddConstraint item = new SQLAlterTableAddConstraint(constraint);
/*  889 */             stmt.addItem((SQLAlterTableItem)item); continue;
/*  890 */           }  if (this.lexer.token() == Token.FOREIGN) {
/*  891 */             SQLForeignKeyConstraint sQLForeignKeyConstraint = this.exprParser.parseForeignKey();
/*  892 */             SQLAlterTableAddConstraint item = new SQLAlterTableAddConstraint((SQLConstraint)sQLForeignKeyConstraint);
/*  893 */             stmt.addItem((SQLAlterTableItem)item); continue;
/*  894 */           }  if (this.lexer.token() == Token.PARTITION) {
/*  895 */             this.lexer.nextToken();
/*  896 */             SQLAlterTableAddPartition addPartition = new SQLAlterTableAddPartition();
/*      */             
/*  898 */             addPartition.setIfNotExists(ifNotExists);
/*      */             
/*  900 */             accept(Token.LPAREN);
/*      */             
/*  902 */             parseAssignItems(addPartition.getPartitions(), (SQLObject)addPartition);
/*      */             
/*  904 */             accept(Token.RPAREN);
/*      */             
/*  906 */             stmt.addItem((SQLAlterTableItem)addPartition); continue;
/*      */           } 
/*  908 */           throw new ParserException("TODO " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */         } 
/*  910 */         if (this.lexer.token() == Token.DISABLE) {
/*  911 */           this.lexer.nextToken();
/*      */           
/*  913 */           if (this.lexer.token() == Token.CONSTRAINT) {
/*  914 */             this.lexer.nextToken();
/*  915 */             SQLAlterTableDisableConstraint sQLAlterTableDisableConstraint = new SQLAlterTableDisableConstraint();
/*  916 */             sQLAlterTableDisableConstraint.setConstraintName(this.exprParser.name());
/*  917 */             stmt.addItem((SQLAlterTableItem)sQLAlterTableDisableConstraint); continue;
/*  918 */           }  if (identifierEquals("LIFECYCLE")) {
/*  919 */             this.lexer.nextToken();
/*  920 */             SQLAlterTableDisableLifecycle sQLAlterTableDisableLifecycle = new SQLAlterTableDisableLifecycle();
/*  921 */             stmt.addItem((SQLAlterTableItem)sQLAlterTableDisableLifecycle); continue;
/*      */           } 
/*  923 */           acceptIdentifier("KEYS");
/*  924 */           SQLAlterTableDisableKeys item = new SQLAlterTableDisableKeys();
/*  925 */           stmt.addItem((SQLAlterTableItem)item); continue;
/*      */         } 
/*  927 */         if (this.lexer.token() == Token.ENABLE) {
/*  928 */           this.lexer.nextToken();
/*  929 */           if (this.lexer.token() == Token.CONSTRAINT) {
/*  930 */             this.lexer.nextToken();
/*  931 */             SQLAlterTableEnableConstraint sQLAlterTableEnableConstraint = new SQLAlterTableEnableConstraint();
/*  932 */             sQLAlterTableEnableConstraint.setConstraintName(this.exprParser.name());
/*  933 */             stmt.addItem((SQLAlterTableItem)sQLAlterTableEnableConstraint); continue;
/*  934 */           }  if (identifierEquals("LIFECYCLE")) {
/*  935 */             this.lexer.nextToken();
/*  936 */             SQLAlterTableEnableLifecycle sQLAlterTableEnableLifecycle = new SQLAlterTableEnableLifecycle();
/*  937 */             stmt.addItem((SQLAlterTableItem)sQLAlterTableEnableLifecycle); continue;
/*      */           } 
/*  939 */           acceptIdentifier("KEYS");
/*  940 */           SQLAlterTableEnableKeys item = new SQLAlterTableEnableKeys();
/*  941 */           stmt.addItem((SQLAlterTableItem)item); continue;
/*      */         } 
/*  943 */         if (this.lexer.token() == Token.ALTER) {
/*  944 */           this.lexer.nextToken();
/*  945 */           if (this.lexer.token() == Token.COLUMN) {
/*  946 */             SQLAlterTableAlterColumn alterColumn = parseAlterColumn();
/*  947 */             stmt.addItem((SQLAlterTableItem)alterColumn); continue;
/*  948 */           }  if (this.lexer.token() == Token.LITERAL_ALIAS) {
/*  949 */             SQLAlterTableAlterColumn alterColumn = parseAlterColumn();
/*  950 */             stmt.addItem((SQLAlterTableItem)alterColumn); continue;
/*      */           } 
/*  952 */           throw new ParserException("TODO " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */         } 
/*  954 */         if (identifierEquals("CHANGE")) {
/*  955 */           this.lexer.nextToken();
/*  956 */           accept(Token.COLUMN);
/*  957 */           SQLName columnName = this.exprParser.name();
/*  958 */           accept(Token.COMMENT);
/*  959 */           SQLExpr comment = this.exprParser.primary();
/*      */           
/*  961 */           SQLColumnDefinition columnDefinition = new SQLColumnDefinition();
/*  962 */           columnDefinition.setName(columnName);
/*  963 */           columnDefinition.setComment(comment);
/*      */           
/*  965 */           SQLAlterTableAlterColumn changeColumn = new SQLAlterTableAlterColumn();
/*      */           
/*  967 */           changeColumn.setColumn(columnDefinition);
/*      */           
/*  969 */           stmt.addItem((SQLAlterTableItem)changeColumn); continue;
/*  970 */         }  if (this.lexer.token() == Token.WITH) {
/*  971 */           this.lexer.nextToken();
/*  972 */           acceptIdentifier("NOCHECK");
/*  973 */           acceptIdentifier("ADD");
/*  974 */           SQLConstraint check = this.exprParser.parseConstaint();
/*      */           
/*  976 */           SQLAlterTableAddConstraint addCheck = new SQLAlterTableAddConstraint();
/*  977 */           addCheck.setWithNoCheck(true);
/*  978 */           addCheck.setConstraint(check);
/*  979 */           stmt.addItem((SQLAlterTableItem)addCheck); continue;
/*  980 */         }  if (identifierEquals("RENAME")) {
/*  981 */           stmt.addItem(parseAlterTableRename()); continue;
/*  982 */         }  if (this.lexer.token() == Token.SET) {
/*  983 */           this.lexer.nextToken();
/*      */           
/*  985 */           if (this.lexer.token() == Token.COMMENT) {
/*  986 */             this.lexer.nextToken();
/*  987 */             SQLAlterTableSetComment setComment = new SQLAlterTableSetComment();
/*  988 */             setComment.setComment(this.exprParser.primary());
/*  989 */             stmt.addItem((SQLAlterTableItem)setComment); continue;
/*  990 */           }  if (identifierEquals("LIFECYCLE")) {
/*  991 */             this.lexer.nextToken();
/*  992 */             SQLAlterTableSetLifecycle setLifecycle = new SQLAlterTableSetLifecycle();
/*  993 */             setLifecycle.setLifecycle(this.exprParser.primary());
/*  994 */             stmt.addItem((SQLAlterTableItem)setLifecycle); continue;
/*      */           } 
/*  996 */           throw new ParserException("TODO " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */         } 
/*  998 */         if (this.lexer.token() == Token.PARTITION) {
/*  999 */           this.lexer.nextToken();
/*      */           
/* 1001 */           SQLAlterTableRenamePartition renamePartition = new SQLAlterTableRenamePartition();
/*      */           
/* 1003 */           accept(Token.LPAREN);
/*      */           
/* 1005 */           parseAssignItems(renamePartition.getPartition(), (SQLObject)renamePartition);
/*      */           
/* 1007 */           accept(Token.RPAREN);
/*      */           
/* 1009 */           if (this.lexer.token() == Token.ENABLE) {
/* 1010 */             this.lexer.nextToken();
/* 1011 */             if (identifierEquals("LIFECYCLE")) {
/* 1012 */               this.lexer.nextToken();
/*      */             }
/*      */             
/* 1015 */             SQLAlterTableEnableLifecycle enableLifeCycle = new SQLAlterTableEnableLifecycle();
/* 1016 */             for (SQLAssignItem condition : renamePartition.getPartition()) {
/* 1017 */               enableLifeCycle.getPartition().add(condition);
/* 1018 */               condition.setParent((SQLObject)enableLifeCycle);
/*      */             } 
/* 1020 */             stmt.addItem((SQLAlterTableItem)enableLifeCycle);
/*      */             
/*      */             continue;
/*      */           } 
/*      */           
/* 1025 */           if (this.lexer.token() == Token.DISABLE) {
/* 1026 */             this.lexer.nextToken();
/* 1027 */             if (identifierEquals("LIFECYCLE")) {
/* 1028 */               this.lexer.nextToken();
/*      */             }
/*      */             
/* 1031 */             SQLAlterTableDisableLifecycle disableLifeCycle = new SQLAlterTableDisableLifecycle();
/* 1032 */             for (SQLAssignItem condition : renamePartition.getPartition()) {
/* 1033 */               disableLifeCycle.getPartition().add(condition);
/* 1034 */               condition.setParent((SQLObject)disableLifeCycle);
/*      */             } 
/* 1036 */             stmt.addItem((SQLAlterTableItem)disableLifeCycle);
/*      */             
/*      */             continue;
/*      */           } 
/*      */           
/* 1041 */           acceptIdentifier("RENAME");
/* 1042 */           accept(Token.TO);
/* 1043 */           accept(Token.PARTITION);
/*      */           
/* 1045 */           accept(Token.LPAREN);
/*      */           
/* 1047 */           parseAssignItems(renamePartition.getTo(), (SQLObject)renamePartition);
/*      */           
/* 1049 */           accept(Token.RPAREN);
/*      */           
/* 1051 */           stmt.addItem((SQLAlterTableItem)renamePartition); continue;
/* 1052 */         }  if (identifierEquals("TOUCH")) {
/* 1053 */           this.lexer.nextToken();
/* 1054 */           SQLAlterTableTouch item = new SQLAlterTableTouch();
/*      */           
/* 1056 */           if (this.lexer.token() == Token.PARTITION) {
/* 1057 */             this.lexer.nextToken();
/*      */             
/* 1059 */             accept(Token.LPAREN);
/* 1060 */             parseAssignItems(item.getPartition(), (SQLObject)item);
/* 1061 */             accept(Token.RPAREN);
/*      */           } 
/*      */           
/* 1064 */           stmt.addItem((SQLAlterTableItem)item); continue;
/* 1065 */         }  if ("odps".equals(this.dbType) && identifierEquals("MERGE")) {
/* 1066 */           this.lexer.nextToken();
/* 1067 */           acceptIdentifier("SMALLFILES");
/* 1068 */           stmt.setMergeSmallFiles(true);
/*      */           
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/* 1074 */       return (SQLStatement)stmt;
/* 1075 */     }  if (this.lexer.token() == Token.VIEW) {
/* 1076 */       this.lexer.nextToken();
/* 1077 */       SQLName viewName = this.exprParser.name();
/*      */       
/* 1079 */       if (identifierEquals("RENAME")) {
/* 1080 */         this.lexer.nextToken();
/* 1081 */         accept(Token.TO);
/*      */         
/* 1083 */         SQLAlterViewRenameStatement stmt = new SQLAlterViewRenameStatement();
/* 1084 */         stmt.setName(viewName);
/*      */         
/* 1086 */         SQLName newName = this.exprParser.name();
/*      */         
/* 1088 */         stmt.setTo(newName);
/*      */         
/* 1090 */         return (SQLStatement)stmt;
/*      */       } 
/* 1092 */       throw new ParserException("TODO " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */     } 
/* 1094 */     throw new ParserException("TODO " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */   }
/*      */   
/*      */   protected SQLAlterTableItem parseAlterTableRename() {
/* 1098 */     acceptIdentifier("RENAME");
/*      */     
/* 1100 */     if (this.lexer.token() == Token.COLUMN) {
/* 1101 */       this.lexer.nextToken();
/* 1102 */       SQLAlterTableRenameColumn renameColumn = new SQLAlterTableRenameColumn();
/* 1103 */       renameColumn.setColumn(this.exprParser.name());
/* 1104 */       accept(Token.TO);
/* 1105 */       renameColumn.setTo(this.exprParser.name());
/* 1106 */       return (SQLAlterTableItem)renameColumn;
/*      */     } 
/*      */     
/* 1109 */     if (this.lexer.token() == Token.TO) {
/* 1110 */       this.lexer.nextToken();
/* 1111 */       SQLAlterTableRename item = new SQLAlterTableRename();
/* 1112 */       item.setTo((SQLExpr)this.exprParser.name());
/* 1113 */       return (SQLAlterTableItem)item;
/*      */     } 
/*      */     
/* 1116 */     throw new ParserException("TODO " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */   }
/*      */   
/*      */   protected SQLAlterTableAlterColumn parseAlterColumn() {
/* 1120 */     this.lexer.nextToken();
/* 1121 */     SQLColumnDefinition column = this.exprParser.parseColumn();
/*      */     
/* 1123 */     SQLAlterTableAlterColumn alterColumn = new SQLAlterTableAlterColumn();
/* 1124 */     alterColumn.setColumn(column);
/* 1125 */     return alterColumn;
/*      */   }
/*      */   
/*      */   public void parseAlterDrop(SQLAlterTableStatement stmt) {
/* 1129 */     this.lexer.nextToken();
/*      */     
/* 1131 */     boolean ifExists = false;
/*      */     
/* 1133 */     if (this.lexer.token() == Token.IF) {
/* 1134 */       this.lexer.nextToken();
/*      */       
/* 1136 */       accept(Token.EXISTS);
/* 1137 */       ifExists = true;
/*      */     } 
/*      */     
/* 1140 */     if (this.lexer.token() == Token.CONSTRAINT) {
/* 1141 */       this.lexer.nextToken();
/* 1142 */       SQLAlterTableDropConstraint item = new SQLAlterTableDropConstraint();
/* 1143 */       item.setConstraintName(this.exprParser.name());
/* 1144 */       stmt.addItem((SQLAlterTableItem)item);
/* 1145 */     } else if (this.lexer.token() == Token.COLUMN) {
/* 1146 */       this.lexer.nextToken();
/* 1147 */       SQLAlterTableDropColumnItem item = new SQLAlterTableDropColumnItem();
/* 1148 */       this.exprParser.names(item.getColumns());
/*      */       
/* 1150 */       if (this.lexer.token == Token.CASCADE) {
/* 1151 */         item.setCascade(true);
/* 1152 */         this.lexer.nextToken();
/*      */       } 
/*      */       
/* 1155 */       stmt.addItem((SQLAlterTableItem)item);
/* 1156 */     } else if (this.lexer.token() == Token.LITERAL_ALIAS) {
/* 1157 */       SQLAlterTableDropColumnItem item = new SQLAlterTableDropColumnItem();
/* 1158 */       this.exprParser.names(item.getColumns());
/*      */       
/* 1160 */       if (this.lexer.token == Token.CASCADE) {
/* 1161 */         item.setCascade(true);
/* 1162 */         this.lexer.nextToken();
/*      */       } 
/*      */       
/* 1165 */       stmt.addItem((SQLAlterTableItem)item);
/* 1166 */     } else if (this.lexer.token() == Token.PARTITION) {
/* 1167 */       SQLAlterTableDropPartition dropPartition = parseAlterTableDropPartition(ifExists);
/*      */       
/* 1169 */       stmt.addItem((SQLAlterTableItem)dropPartition);
/* 1170 */     } else if (this.lexer.token() == Token.INDEX) {
/* 1171 */       this.lexer.nextToken();
/* 1172 */       SQLName indexName = this.exprParser.name();
/* 1173 */       SQLAlterTableDropIndex item = new SQLAlterTableDropIndex();
/* 1174 */       item.setIndexName(indexName);
/* 1175 */       stmt.addItem((SQLAlterTableItem)item);
/*      */     } else {
/* 1177 */       throw new ParserException("TODO " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */     } 
/*      */   }
/*      */   
/*      */   protected SQLAlterTableDropPartition parseAlterTableDropPartition(boolean ifExists) {
/* 1182 */     this.lexer.nextToken();
/* 1183 */     SQLAlterTableDropPartition dropPartition = new SQLAlterTableDropPartition();
/*      */     
/* 1185 */     dropPartition.setIfExists(ifExists);
/*      */     
/* 1187 */     if (this.lexer.token() == Token.LPAREN) {
/* 1188 */       accept(Token.LPAREN);
/*      */       
/* 1190 */       parseAssignItems(dropPartition.getPartitions(), (SQLObject)dropPartition);
/*      */       
/* 1192 */       accept(Token.RPAREN);
/*      */       
/* 1194 */       if (identifierEquals("PURGE")) {
/* 1195 */         this.lexer.nextToken();
/* 1196 */         dropPartition.setPurge(true);
/*      */       } 
/*      */     } else {
/* 1199 */       SQLName partition = this.exprParser.name();
/* 1200 */       dropPartition.addPartition((SQLObject)partition);
/*      */     } 
/*      */ 
/*      */     
/* 1204 */     return dropPartition;
/*      */   }
/*      */   
/*      */   public SQLStatement parseRename() {
/* 1208 */     throw new ParserException("TODO " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */   }
/*      */   
/*      */   protected SQLDropTableStatement parseDropTable(boolean acceptDrop) {
/* 1212 */     if (acceptDrop) {
/* 1213 */       accept(Token.DROP);
/*      */     }
/*      */     
/* 1216 */     SQLDropTableStatement stmt = new SQLDropTableStatement(getDbType());
/*      */     
/* 1218 */     if (identifierEquals("TEMPORARY")) {
/* 1219 */       this.lexer.nextToken();
/* 1220 */       stmt.setTemporary(true);
/*      */     } 
/*      */     
/* 1223 */     accept(Token.TABLE);
/*      */     
/* 1225 */     if (this.lexer.token() == Token.IF) {
/* 1226 */       this.lexer.nextToken();
/* 1227 */       accept(Token.EXISTS);
/* 1228 */       stmt.setIfExists(true);
/*      */     } 
/*      */     
/*      */     while (true) {
/* 1232 */       SQLName name = this.exprParser.name();
/* 1233 */       stmt.addPartition(new SQLExprTableSource((SQLExpr)name));
/* 1234 */       if (this.lexer.token() == Token.COMMA) {
/* 1235 */         this.lexer.nextToken();
/*      */         
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/*      */     while (true) {
/* 1242 */       while (identifierEquals("RESTRICT")) {
/* 1243 */         this.lexer.nextToken();
/* 1244 */         stmt.setRestrict(true);
/*      */       } 
/*      */ 
/*      */       
/* 1248 */       if (identifierEquals("CASCADE")) {
/* 1249 */         this.lexer.nextToken();
/* 1250 */         stmt.setCascade(true);
/*      */         
/* 1252 */         if (identifierEquals("CONSTRAINTS")) {
/* 1253 */           this.lexer.nextToken();
/*      */         }
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/* 1259 */       if (this.lexer.token() == Token.PURGE || identifierEquals("PURGE")) {
/* 1260 */         this.lexer.nextToken();
/* 1261 */         stmt.setPurge(true);
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/*      */       break;
/*      */     } 
/* 1268 */     return stmt;
/*      */   }
/*      */   
/*      */   protected SQLDropSequenceStatement parseDropSequece(boolean acceptDrop) {
/* 1272 */     if (acceptDrop) {
/* 1273 */       accept(Token.DROP);
/*      */     }
/*      */     
/* 1276 */     this.lexer.nextToken();
/*      */     
/* 1278 */     SQLName name = this.exprParser.name();
/*      */     
/* 1280 */     SQLDropSequenceStatement stmt = new SQLDropSequenceStatement(getDbType());
/* 1281 */     stmt.setName(name);
/* 1282 */     return stmt;
/*      */   }
/*      */   
/*      */   protected SQLDropTriggerStatement parseDropTrigger(boolean acceptDrop) {
/* 1286 */     if (acceptDrop) {
/* 1287 */       accept(Token.DROP);
/*      */     }
/*      */     
/* 1290 */     this.lexer.nextToken();
/*      */     
/* 1292 */     SQLName name = this.exprParser.name();
/*      */     
/* 1294 */     SQLDropTriggerStatement stmt = new SQLDropTriggerStatement(getDbType());
/* 1295 */     stmt.setName(name);
/* 1296 */     return stmt;
/*      */   }
/*      */   
/*      */   protected SQLDropViewStatement parseDropView(boolean acceptDrop) {
/* 1300 */     if (acceptDrop) {
/* 1301 */       accept(Token.DROP);
/*      */     }
/*      */     
/* 1304 */     SQLDropViewStatement stmt = new SQLDropViewStatement(getDbType());
/*      */     
/* 1306 */     accept(Token.VIEW);
/*      */     
/* 1308 */     if (this.lexer.token() == Token.IF) {
/* 1309 */       this.lexer.nextToken();
/* 1310 */       accept(Token.EXISTS);
/* 1311 */       stmt.setIfExists(true);
/*      */     } 
/*      */     
/*      */     while (true) {
/* 1315 */       SQLName name = this.exprParser.name();
/* 1316 */       stmt.addPartition(new SQLExprTableSource((SQLExpr)name));
/* 1317 */       if (this.lexer.token() == Token.COMMA) {
/* 1318 */         this.lexer.nextToken();
/*      */         
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 1324 */     if (identifierEquals("RESTRICT")) {
/* 1325 */       this.lexer.nextToken();
/* 1326 */       stmt.setRestrict(true);
/* 1327 */     } else if (identifierEquals("CASCADE")) {
/* 1328 */       this.lexer.nextToken();
/*      */       
/* 1330 */       if (identifierEquals("CONSTRAINTS")) {
/* 1331 */         this.lexer.nextToken();
/*      */       }
/*      */       
/* 1334 */       stmt.setCascade(true);
/*      */     } 
/*      */     
/* 1337 */     return stmt;
/*      */   }
/*      */   
/*      */   protected SQLDropDatabaseStatement parseDropDatabase(boolean acceptDrop) {
/* 1341 */     if (acceptDrop) {
/* 1342 */       accept(Token.DROP);
/*      */     }
/*      */     
/* 1345 */     SQLDropDatabaseStatement stmt = new SQLDropDatabaseStatement(getDbType());
/*      */     
/* 1347 */     accept(Token.DATABASE);
/*      */     
/* 1349 */     if (this.lexer.token() == Token.IF) {
/* 1350 */       this.lexer.nextToken();
/* 1351 */       accept(Token.EXISTS);
/* 1352 */       stmt.setIfExists(true);
/*      */     } 
/*      */     
/* 1355 */     SQLName name = this.exprParser.name();
/* 1356 */     stmt.setDatabase((SQLExpr)name);
/*      */     
/* 1358 */     return stmt;
/*      */   }
/*      */   
/*      */   protected SQLDropFunctionStatement parseDropFunction(boolean acceptDrop) {
/* 1362 */     if (acceptDrop) {
/* 1363 */       accept(Token.DROP);
/*      */     }
/*      */     
/* 1366 */     SQLDropFunctionStatement stmt = new SQLDropFunctionStatement(getDbType());
/*      */     
/* 1368 */     accept(Token.FUNCTION);
/*      */     
/* 1370 */     if (this.lexer.token() == Token.IF) {
/* 1371 */       this.lexer.nextToken();
/* 1372 */       accept(Token.EXISTS);
/* 1373 */       stmt.setIfExists(true);
/*      */     } 
/*      */     
/* 1376 */     SQLName name = this.exprParser.name();
/* 1377 */     stmt.setName(name);
/*      */     
/* 1379 */     return stmt;
/*      */   }
/*      */   
/*      */   protected SQLDropTableSpaceStatement parseDropTablespace(boolean acceptDrop) {
/* 1383 */     SQLDropTableSpaceStatement stmt = new SQLDropTableSpaceStatement(getDbType());
/*      */     
/* 1385 */     if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
/* 1386 */       stmt.addBeforeComment(this.lexer.readAndResetComments());
/*      */     }
/*      */     
/* 1389 */     if (acceptDrop) {
/* 1390 */       accept(Token.DROP);
/*      */     }
/*      */     
/* 1393 */     accept(Token.TABLESPACE);
/*      */     
/* 1395 */     if (this.lexer.token() == Token.IF) {
/* 1396 */       this.lexer.nextToken();
/* 1397 */       accept(Token.EXISTS);
/* 1398 */       stmt.setIfExists(true);
/*      */     } 
/*      */     
/* 1401 */     SQLName name = this.exprParser.name();
/* 1402 */     stmt.setName(name);
/*      */     
/* 1404 */     return stmt;
/*      */   }
/*      */   
/*      */   protected SQLDropProcedureStatement parseDropProcedure(boolean acceptDrop) {
/* 1408 */     if (acceptDrop) {
/* 1409 */       accept(Token.DROP);
/*      */     }
/*      */     
/* 1412 */     SQLDropProcedureStatement stmt = new SQLDropProcedureStatement(getDbType());
/*      */     
/* 1414 */     accept(Token.PROCEDURE);
/*      */     
/* 1416 */     if (this.lexer.token() == Token.IF) {
/* 1417 */       this.lexer.nextToken();
/* 1418 */       accept(Token.EXISTS);
/* 1419 */       stmt.setIfExists(true);
/*      */     } 
/*      */     
/* 1422 */     SQLName name = this.exprParser.name();
/* 1423 */     stmt.setName(name);
/*      */     
/* 1425 */     return stmt;
/*      */   }
/*      */   
/*      */   public SQLStatement parseTruncate() {
/* 1429 */     accept(Token.TRUNCATE);
/* 1430 */     if (this.lexer.token() == Token.TABLE) {
/* 1431 */       this.lexer.nextToken();
/*      */     }
/* 1433 */     SQLTruncateStatement stmt = new SQLTruncateStatement(getDbType());
/*      */     
/* 1435 */     if (this.lexer.token() == Token.ONLY) {
/* 1436 */       this.lexer.nextToken();
/* 1437 */       stmt.setOnly(true);
/*      */     } 
/*      */     
/*      */     while (true) {
/* 1441 */       SQLName name = this.exprParser.name();
/* 1442 */       stmt.addTableSource(name);
/*      */       
/* 1444 */       if (this.lexer.token() == Token.COMMA) {
/* 1445 */         this.lexer.nextToken();
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/*      */       break;
/*      */     } 
/*      */     while (true) {
/* 1453 */       while (this.lexer.token() == Token.PURGE) {
/* 1454 */         this.lexer.nextToken();
/*      */         
/* 1456 */         if (identifierEquals("SNAPSHOT")) {
/* 1457 */           this.lexer.nextToken();
/* 1458 */           acceptIdentifier("LOG");
/* 1459 */           stmt.setPurgeSnapshotLog(true); continue;
/*      */         } 
/* 1461 */         throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1466 */       if (this.lexer.token() == Token.RESTART) {
/* 1467 */         this.lexer.nextToken();
/* 1468 */         accept(Token.IDENTITY);
/* 1469 */         stmt.setRestartIdentity(Boolean.TRUE); continue;
/*      */       } 
/* 1471 */       if (this.lexer.token() == Token.SHARE) {
/* 1472 */         this.lexer.nextToken();
/* 1473 */         accept(Token.IDENTITY);
/* 1474 */         stmt.setRestartIdentity(Boolean.FALSE);
/*      */         
/*      */         continue;
/*      */       } 
/* 1478 */       if (this.lexer.token() == Token.CASCADE) {
/* 1479 */         this.lexer.nextToken();
/* 1480 */         stmt.setCascade(Boolean.TRUE); continue;
/*      */       } 
/* 1482 */       if (this.lexer.token() == Token.RESTRICT) {
/* 1483 */         this.lexer.nextToken();
/* 1484 */         stmt.setCascade(Boolean.FALSE);
/*      */         
/*      */         continue;
/*      */       } 
/* 1488 */       if (this.lexer.token() == Token.DROP) {
/* 1489 */         this.lexer.nextToken();
/* 1490 */         acceptIdentifier("STORAGE");
/* 1491 */         stmt.setDropStorage(true);
/*      */         
/*      */         continue;
/*      */       } 
/* 1495 */       if (identifierEquals("REUSE")) {
/* 1496 */         this.lexer.nextToken();
/* 1497 */         acceptIdentifier("STORAGE");
/* 1498 */         stmt.setReuseStorage(true);
/*      */         
/*      */         continue;
/*      */       } 
/* 1502 */       if (identifierEquals("IGNORE")) {
/* 1503 */         this.lexer.nextToken();
/* 1504 */         accept(Token.DELETE);
/* 1505 */         acceptIdentifier("TRIGGERS");
/* 1506 */         stmt.setIgnoreDeleteTriggers(true);
/*      */         
/*      */         continue;
/*      */       } 
/* 1510 */       if (identifierEquals("RESTRICT")) {
/* 1511 */         this.lexer.nextToken();
/* 1512 */         accept(Token.WHEN);
/* 1513 */         accept(Token.DELETE);
/* 1514 */         acceptIdentifier("TRIGGERS");
/* 1515 */         stmt.setRestrictWhenDeleteTriggers(true);
/*      */         
/*      */         continue;
/*      */       } 
/* 1519 */       if (this.lexer.token() == Token.CONTINUE) {
/* 1520 */         this.lexer.nextToken();
/* 1521 */         accept(Token.IDENTITY);
/*      */         
/*      */         continue;
/*      */       } 
/* 1525 */       if (identifierEquals("IMMEDIATE")) {
/* 1526 */         this.lexer.nextToken();
/* 1527 */         stmt.setImmediate(true);
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/*      */       break;
/*      */     } 
/* 1534 */     return (SQLStatement)stmt;
/*      */   }
/*      */   
/*      */   public SQLStatement parseInsert() {
/* 1538 */     SQLInsertStatement insertStatement = new SQLInsertStatement();
/*      */     
/* 1540 */     if (this.lexer.token() == Token.INSERT) {
/* 1541 */       accept(Token.INSERT);
/*      */     }
/*      */     
/* 1544 */     parseInsert0((SQLInsertInto)insertStatement);
/* 1545 */     return (SQLStatement)insertStatement;
/*      */   }
/*      */   
/*      */   protected void parseInsert0(SQLInsertInto insertStatement) {
/* 1549 */     parseInsert0(insertStatement, true);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void parseInsert0_hinits(SQLInsertInto insertStatement) {}
/*      */ 
/*      */   
/*      */   protected void parseInsert0(SQLInsertInto insertStatement, boolean acceptSubQuery) {
/* 1557 */     if (this.lexer.token() == Token.INTO) {
/* 1558 */       this.lexer.nextToken();
/*      */       
/* 1560 */       SQLName tableName = this.exprParser.name();
/* 1561 */       insertStatement.setTableName(tableName);
/*      */       
/* 1563 */       if (this.lexer.token() == Token.LITERAL_ALIAS) {
/* 1564 */         insertStatement.setAlias(as());
/*      */       }
/*      */       
/* 1567 */       parseInsert0_hinits(insertStatement);
/*      */       
/* 1569 */       if (this.lexer.token() == Token.IDENTIFIER) {
/* 1570 */         insertStatement.setAlias(this.lexer.stringVal());
/* 1571 */         this.lexer.nextToken();
/*      */       } 
/*      */     } 
/*      */     
/* 1575 */     if (this.lexer.token() == Token.LPAREN) {
/* 1576 */       this.lexer.nextToken();
/* 1577 */       parseInsertColumns(insertStatement);
/* 1578 */       accept(Token.RPAREN);
/*      */     } 
/*      */     
/* 1581 */     if (this.lexer.token() == Token.VALUES) {
/* 1582 */       this.lexer.nextToken();
/*      */       while (true) {
/* 1584 */         accept(Token.LPAREN);
/* 1585 */         SQLInsertStatement.ValuesClause values = new SQLInsertStatement.ValuesClause();
/* 1586 */         this.exprParser.exprList(values.getValues(), (SQLObject)values);
/* 1587 */         insertStatement.getValuesList().add(values);
/* 1588 */         accept(Token.RPAREN);
/*      */         
/* 1590 */         if (this.lexer.token() == Token.COMMA) {
/* 1591 */           this.lexer.nextToken();
/*      */           
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/* 1597 */     } else if (acceptSubQuery && (this.lexer.token() == Token.SELECT || this.lexer.token() == Token.LPAREN)) {
/* 1598 */       SQLSelect select = createSQLSelectParser().select();
/* 1599 */       insertStatement.setQuery(select);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void parseInsertColumns(SQLInsertInto insert) {
/* 1604 */     this.exprParser.exprList(insert.getColumns(), (SQLObject)insert);
/*      */   }
/*      */   
/*      */   public boolean parseStatementListDialect(List<SQLStatement> statementList) {
/* 1608 */     return false;
/*      */   }
/*      */   
/*      */   public SQLDropUserStatement parseDropUser() {
/* 1612 */     accept(Token.USER);
/*      */     
/* 1614 */     SQLDropUserStatement stmt = new SQLDropUserStatement(getDbType());
/*      */     while (true) {
/* 1616 */       SQLExpr expr = this.exprParser.expr();
/* 1617 */       stmt.addUser(expr);
/* 1618 */       if (this.lexer.token() == Token.COMMA) {
/* 1619 */         this.lexer.nextToken();
/*      */         
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 1625 */     return stmt;
/*      */   }
/*      */   
/*      */   public SQLStatement parseDropIndex() {
/* 1629 */     accept(Token.INDEX);
/* 1630 */     SQLDropIndexStatement stmt = new SQLDropIndexStatement(getDbType());
/* 1631 */     stmt.setIndexName((SQLExpr)this.exprParser.name());
/*      */     
/* 1633 */     if (this.lexer.token() == Token.ON) {
/* 1634 */       this.lexer.nextToken();
/* 1635 */       stmt.setTableName((SQLExpr)this.exprParser.name());
/*      */     } 
/* 1637 */     return (SQLStatement)stmt;
/*      */   }
/*      */ 
/*      */   
/*      */   public SQLCallStatement parseCall() {
/* 1642 */     boolean brace = false;
/* 1643 */     if (this.lexer.token() == Token.LBRACE) {
/* 1644 */       this.lexer.nextToken();
/* 1645 */       brace = true;
/*      */     } 
/*      */     
/* 1648 */     SQLCallStatement stmt = new SQLCallStatement(getDbType());
/*      */     
/* 1650 */     if (this.lexer.token() == Token.QUES) {
/* 1651 */       this.lexer.nextToken();
/* 1652 */       accept(Token.EQ);
/* 1653 */       stmt.setOutParameter(new SQLVariantRefExpr("?"));
/*      */     } 
/*      */     
/* 1656 */     acceptIdentifier("CALL");
/*      */     
/* 1658 */     stmt.setProcedureName(this.exprParser.name());
/*      */     
/* 1660 */     if (this.lexer.token() == Token.LPAREN) {
/* 1661 */       this.lexer.nextToken();
/* 1662 */       this.exprParser.exprList(stmt.getParameters(), (SQLObject)stmt);
/* 1663 */       accept(Token.RPAREN);
/*      */     } 
/*      */     
/* 1666 */     if (brace) {
/* 1667 */       accept(Token.RBRACE);
/* 1668 */       stmt.setBrace(true);
/*      */     } 
/*      */     
/* 1671 */     return stmt;
/*      */   }
/*      */   
/*      */   public SQLStatement parseSet() {
/* 1675 */     accept(Token.SET);
/* 1676 */     SQLSetStatement stmt = new SQLSetStatement(getDbType());
/*      */     
/* 1678 */     parseAssignItems(stmt.getItems(), (SQLObject)stmt);
/*      */     
/* 1680 */     return (SQLStatement)stmt;
/*      */   }
/*      */   
/*      */   public void parseAssignItems(List<? super SQLAssignItem> items, SQLObject parent) {
/*      */     while (true) {
/* 1685 */       SQLAssignItem item = this.exprParser.parseAssignItem();
/* 1686 */       item.setParent(parent);
/* 1687 */       items.add(item);
/*      */       
/* 1689 */       if (this.lexer.token() == Token.COMMA) {
/* 1690 */         this.lexer.nextToken();
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public SQLStatement parseCreate() {
/* 1699 */     char markChar = this.lexer.current();
/* 1700 */     int markBp = this.lexer.bp();
/*      */     
/* 1702 */     List<String> comments = null;
/* 1703 */     if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
/* 1704 */       comments = this.lexer.readAndResetComments();
/*      */     }
/*      */     
/* 1707 */     accept(Token.CREATE);
/*      */     
/* 1709 */     Token token = this.lexer.token();
/*      */     
/* 1711 */     if (token == Token.TABLE || identifierEquals("GLOBAL")) {
/* 1712 */       SQLCreateTableParser createTableParser = getSQLCreateTableParser();
/* 1713 */       SQLCreateTableStatement stmt = createTableParser.parseCrateTable(false);
/*      */       
/* 1715 */       if (comments != null) {
/* 1716 */         stmt.addBeforeComment(comments);
/*      */       }
/*      */       
/* 1719 */       return (SQLStatement)stmt;
/* 1720 */     }  if (token == Token.INDEX || token == Token.UNIQUE ||
/*      */       
/* 1722 */       identifierEquals("NONCLUSTERED"))
/*      */     {
/* 1724 */       return parseCreateIndex(false); } 
/* 1725 */     if (this.lexer.token() == Token.SEQUENCE)
/* 1726 */       return parseCreateSequence(false); 
/* 1727 */     if (token == Token.OR) {
/* 1728 */       this.lexer.nextToken();
/* 1729 */       accept(Token.REPLACE);
/* 1730 */       if (this.lexer.token() == Token.PROCEDURE) {
/* 1731 */         this.lexer.reset(markBp, markChar, Token.CREATE);
/* 1732 */         return parseCreateProcedure();
/*      */       } 
/*      */       
/* 1735 */       if (this.lexer.token() == Token.VIEW) {
/* 1736 */         this.lexer.reset(markBp, markChar, Token.CREATE);
/* 1737 */         return (SQLStatement)parseCreateView();
/*      */       } 
/*      */ 
/*      */       
/* 1741 */       throw new ParserException("TODO " + this.lexer.token() + " " + this.lexer.stringVal());
/* 1742 */     }  if (token == Token.DATABASE) {
/* 1743 */       this.lexer.nextToken();
/* 1744 */       if (identifierEquals("LINK")) {
/* 1745 */         this.lexer.reset(markBp, markChar, Token.CREATE);
/* 1746 */         return parseCreateDbLink();
/*      */       } 
/*      */       
/* 1749 */       this.lexer.reset(markBp, markChar, Token.CREATE);
/* 1750 */       return parseCreateDatabase();
/* 1751 */     }  if (identifierEquals("PUBLIC") || identifierEquals("SHARE")) {
/* 1752 */       this.lexer.reset(markBp, markChar, Token.CREATE);
/* 1753 */       return parseCreateDbLink();
/* 1754 */     }  if (token == Token.VIEW)
/* 1755 */       return (SQLStatement)parseCreateView();
/* 1756 */     if (token == Token.TRIGGER) {
/* 1757 */       return parseCreateTrigger();
/*      */     }
/*      */     
/* 1760 */     throw new ParserException("TODO " + this.lexer.token());
/*      */   }
/*      */   
/*      */   public SQLStatement parseCreateDbLink() {
/* 1764 */     throw new ParserException("TODO " + this.lexer.token());
/*      */   }
/*      */   
/*      */   public SQLStatement parseCreateTrigger() {
/* 1768 */     accept(Token.TRIGGER);
/*      */     
/* 1770 */     SQLCreateTriggerStatement stmt = new SQLCreateTriggerStatement(getDbType());
/* 1771 */     stmt.setName(this.exprParser.name());
/*      */     
/* 1773 */     if (identifierEquals("BEFORE")) {
/* 1774 */       stmt.setTriggerType(SQLCreateTriggerStatement.TriggerType.BEFORE);
/* 1775 */       this.lexer.nextToken();
/* 1776 */     } else if (identifierEquals("AFTER")) {
/* 1777 */       stmt.setTriggerType(SQLCreateTriggerStatement.TriggerType.AFTER);
/* 1778 */       this.lexer.nextToken();
/* 1779 */     } else if (identifierEquals("INSTEAD")) {
/* 1780 */       this.lexer.nextToken();
/* 1781 */       accept(Token.OF);
/* 1782 */       stmt.setTriggerType(SQLCreateTriggerStatement.TriggerType.INSTEAD_OF);
/*      */     } 
/*      */     
/*      */     while (true) {
/* 1786 */       while (this.lexer.token() == Token.INSERT) {
/* 1787 */         this.lexer.nextToken();
/* 1788 */         stmt.getTriggerEvents().add(SQLCreateTriggerStatement.TriggerEvent.INSERT);
/*      */       } 
/*      */ 
/*      */       
/* 1792 */       if (this.lexer.token() == Token.UPDATE) {
/* 1793 */         this.lexer.nextToken();
/* 1794 */         stmt.getTriggerEvents().add(SQLCreateTriggerStatement.TriggerEvent.UPDATE);
/*      */         
/*      */         continue;
/*      */       } 
/* 1798 */       if (this.lexer.token() == Token.DELETE) {
/* 1799 */         this.lexer.nextToken();
/* 1800 */         stmt.getTriggerEvents().add(SQLCreateTriggerStatement.TriggerEvent.DELETE);
/*      */         
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 1806 */     accept(Token.ON);
/* 1807 */     stmt.setOn(this.exprParser.name());
/*      */     
/* 1809 */     if (this.lexer.token() == Token.FOR) {
/* 1810 */       this.lexer.nextToken();
/* 1811 */       acceptIdentifier("EACH");
/* 1812 */       accept(Token.ROW);
/* 1813 */       stmt.setForEachRow(true);
/*      */     } 
/*      */     
/* 1816 */     List<SQLStatement> body = parseStatementList();
/* 1817 */     if (body == null || body.isEmpty()) {
/* 1818 */       throw new ParserException("syntax error");
/*      */     }
/* 1820 */     stmt.setBody(body.get(0));
/* 1821 */     return (SQLStatement)stmt;
/*      */   }
/*      */   
/*      */   public SQLStatement parseBlock() {
/* 1825 */     throw new ParserException("TODO " + this.lexer.token());
/*      */   }
/*      */   
/*      */   public SQLStatement parseCreateDatabase() {
/* 1829 */     if (this.lexer.token() == Token.CREATE) {
/* 1830 */       this.lexer.nextToken();
/*      */     }
/*      */     
/* 1833 */     accept(Token.DATABASE);
/*      */     
/* 1835 */     SQLCreateDatabaseStatement stmt = new SQLCreateDatabaseStatement(getDbType());
/* 1836 */     stmt.setName(this.exprParser.name());
/* 1837 */     return (SQLStatement)stmt;
/*      */   }
/*      */   
/*      */   public SQLStatement parseCreateProcedure() {
/* 1841 */     throw new ParserException("TODO " + this.lexer.token());
/*      */   }
/*      */   
/*      */   public SQLStatement parseCreateSequence(boolean acceptCreate) {
/* 1845 */     throw new ParserException("TODO " + this.lexer.token());
/*      */   }
/*      */   
/*      */   public SQLStatement parseCreateIndex(boolean acceptCreate) {
/* 1849 */     if (acceptCreate) {
/* 1850 */       accept(Token.CREATE);
/*      */     }
/*      */     
/* 1853 */     SQLCreateIndexStatement stmt = new SQLCreateIndexStatement(getDbType());
/* 1854 */     if (this.lexer.token() == Token.UNIQUE) {
/* 1855 */       this.lexer.nextToken();
/* 1856 */       if (identifierEquals("CLUSTERED")) {
/* 1857 */         this.lexer.nextToken();
/* 1858 */         stmt.setType("UNIQUE CLUSTERED");
/*      */       } else {
/* 1860 */         stmt.setType("UNIQUE");
/*      */       } 
/* 1862 */     } else if (identifierEquals("FULLTEXT")) {
/* 1863 */       stmt.setType("FULLTEXT");
/* 1864 */       this.lexer.nextToken();
/* 1865 */     } else if (identifierEquals("NONCLUSTERED")) {
/* 1866 */       stmt.setType("NONCLUSTERED");
/* 1867 */       this.lexer.nextToken();
/*      */     } 
/*      */     
/* 1870 */     accept(Token.INDEX);
/*      */     
/* 1872 */     stmt.setName(this.exprParser.name());
/*      */     
/* 1874 */     accept(Token.ON);
/*      */     
/* 1876 */     stmt.setTable(this.exprParser.name());
/*      */     
/* 1878 */     accept(Token.LPAREN);
/*      */     
/*      */     while (true) {
/* 1881 */       SQLSelectOrderByItem item = this.exprParser.parseSelectOrderByItem();
/* 1882 */       item.setParent((SQLObject)stmt);
/* 1883 */       stmt.addItem(item);
/* 1884 */       if (this.lexer.token() == Token.COMMA) {
/* 1885 */         this.lexer.nextToken();
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 1890 */     accept(Token.RPAREN);
/*      */     
/* 1892 */     return (SQLStatement)stmt;
/*      */   }
/*      */   
/*      */   public SQLCreateTableParser getSQLCreateTableParser() {
/* 1896 */     return new SQLCreateTableParser(this.exprParser);
/*      */   }
/*      */   
/*      */   public SQLStatement parseSelect() {
/* 1900 */     SQLSelectParser selectParser = createSQLSelectParser();
/* 1901 */     SQLSelect select = selectParser.select();
/* 1902 */     return (SQLStatement)new SQLSelectStatement(select, getDbType());
/*      */   }
/*      */   
/*      */   public SQLSelectParser createSQLSelectParser() {
/* 1906 */     return new SQLSelectParser(this.exprParser);
/*      */   }
/*      */   
/*      */   public SQLUpdateStatement parseUpdateStatement() {
/* 1910 */     SQLUpdateStatement udpateStatement = createUpdateStatement();
/*      */     
/* 1912 */     if (this.lexer.token() == Token.UPDATE) {
/* 1913 */       this.lexer.nextToken();
/*      */       
/* 1915 */       SQLTableSource tableSource = this.exprParser.createSelectParser().parseTableSource();
/* 1916 */       udpateStatement.setTableSource(tableSource);
/*      */     } 
/*      */     
/* 1919 */     parseUpdateSet(udpateStatement);
/*      */     
/* 1921 */     if (this.lexer.token() == Token.WHERE) {
/* 1922 */       this.lexer.nextToken();
/* 1923 */       udpateStatement.setWhere(this.exprParser.expr());
/*      */     } 
/*      */     
/* 1926 */     return udpateStatement;
/*      */   }
/*      */   
/*      */   protected void parseUpdateSet(SQLUpdateStatement update) {
/* 1930 */     accept(Token.SET);
/*      */     
/*      */     while (true) {
/* 1933 */       SQLUpdateSetItem item = this.exprParser.parseUpdateSetItem();
/* 1934 */       update.addItem(item);
/*      */       
/* 1936 */       if (this.lexer.token() != Token.COMMA) {
/*      */         break;
/*      */       }
/*      */       
/* 1940 */       this.lexer.nextToken();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected SQLUpdateStatement createUpdateStatement() {
/* 1945 */     return new SQLUpdateStatement(getDbType());
/*      */   }
/*      */   
/*      */   public SQLDeleteStatement parseDeleteStatement() {
/* 1949 */     SQLDeleteStatement deleteStatement = new SQLDeleteStatement(getDbType());
/*      */     
/* 1951 */     if (this.lexer.token() == Token.DELETE) {
/* 1952 */       this.lexer.nextToken();
/* 1953 */       if (this.lexer.token() == Token.FROM) {
/* 1954 */         this.lexer.nextToken();
/*      */       }
/*      */       
/* 1957 */       if (this.lexer.token() == Token.COMMENT) {
/* 1958 */         this.lexer.nextToken();
/*      */       }
/*      */       
/* 1961 */       SQLName tableName = this.exprParser.name();
/*      */       
/* 1963 */       deleteStatement.setTableName(tableName);
/*      */       
/* 1965 */       if (this.lexer.token() == Token.FROM) {
/* 1966 */         this.lexer.nextToken();
/* 1967 */         SQLTableSource tableSource = createSQLSelectParser().parseTableSource();
/* 1968 */         deleteStatement.setFrom(tableSource);
/*      */       } 
/*      */     } 
/*      */     
/* 1972 */     if (this.lexer.token() == Token.WHERE) {
/* 1973 */       this.lexer.nextToken();
/* 1974 */       SQLExpr where = this.exprParser.expr();
/* 1975 */       deleteStatement.setWhere(where);
/*      */     } 
/*      */     
/* 1978 */     return deleteStatement;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public SQLCreateTableStatement parseCreateTable() {
/* 1984 */     throw new ParserException("TODO");
/*      */   }
/*      */   
/*      */   public SQLCreateViewStatement parseCreateView() {
/* 1988 */     SQLCreateViewStatement createView = new SQLCreateViewStatement(getDbType());
/*      */     
/* 1990 */     if (this.lexer.token() == Token.CREATE) {
/* 1991 */       this.lexer.nextToken();
/*      */     }
/*      */     
/* 1994 */     if (this.lexer.token() == Token.OR) {
/* 1995 */       this.lexer.nextToken();
/* 1996 */       accept(Token.REPLACE);
/* 1997 */       createView.setOrReplace(true);
/*      */     } 
/*      */     
/* 2000 */     if (identifierEquals("ALGORITHM")) {
/* 2001 */       this.lexer.nextToken();
/* 2002 */       accept(Token.EQ);
/* 2003 */       String algorithm = this.lexer.stringVal();
/* 2004 */       createView.setAlgorithm(algorithm);
/* 2005 */       this.lexer.nextToken();
/*      */     } 
/*      */     
/* 2008 */     if (identifierEquals("DEFINER")) {
/* 2009 */       this.lexer.nextToken();
/* 2010 */       accept(Token.EQ);
/* 2011 */       SQLName definer = this.exprParser.name();
/* 2012 */       createView.setDefiner(definer);
/* 2013 */       this.lexer.nextToken();
/*      */     } 
/*      */     
/* 2016 */     if (identifierEquals("SQL")) {
/* 2017 */       this.lexer.nextToken();
/* 2018 */       acceptIdentifier("SECURITY");
/* 2019 */       String sqlSecurity = this.lexer.stringVal();
/* 2020 */       createView.setSqlSecurity(sqlSecurity);
/* 2021 */       this.lexer.nextToken();
/*      */     } 
/*      */     
/* 2024 */     accept(Token.VIEW);
/*      */     
/* 2026 */     if (this.lexer.token() == Token.IF || identifierEquals("IF")) {
/* 2027 */       this.lexer.nextToken();
/* 2028 */       accept(Token.NOT);
/* 2029 */       accept(Token.EXISTS);
/* 2030 */       createView.setIfNotExists(true);
/*      */     } 
/*      */     
/* 2033 */     createView.setName(this.exprParser.name());
/*      */     
/* 2035 */     if (this.lexer.token() == Token.LPAREN) {
/* 2036 */       this.lexer.nextToken();
/*      */       
/*      */       while (true) {
/* 2039 */         SQLCreateViewStatement.Column column = new SQLCreateViewStatement.Column();
/* 2040 */         SQLExpr expr = this.exprParser.expr();
/* 2041 */         column.setExpr(expr);
/*      */         
/* 2043 */         if (this.lexer.token() == Token.COMMENT) {
/* 2044 */           this.lexer.nextToken();
/* 2045 */           column.setComment((SQLCharExpr)this.exprParser.primary());
/*      */         } 
/*      */         
/* 2048 */         column.setParent((SQLObject)createView);
/* 2049 */         createView.addColumn(column);
/*      */         
/* 2051 */         if (this.lexer.token() == Token.COMMA) {
/* 2052 */           this.lexer.nextToken();
/*      */           
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/* 2058 */       accept(Token.RPAREN);
/*      */     } 
/*      */     
/* 2061 */     if (this.lexer.token() == Token.COMMENT) {
/* 2062 */       this.lexer.nextToken();
/* 2063 */       SQLCharExpr comment = (SQLCharExpr)this.exprParser.primary();
/* 2064 */       createView.setComment((SQLLiteralExpr)comment);
/*      */     } 
/*      */     
/* 2067 */     accept(Token.AS);
/*      */     
/* 2069 */     createView.setSubQuery((new SQLSelectParser(this.exprParser)).select());
/* 2070 */     return createView;
/*      */   }
/*      */   
/*      */   public SQLCommentStatement parseComment() {
/* 2074 */     accept(Token.COMMENT);
/* 2075 */     SQLCommentStatement stmt = new SQLCommentStatement();
/*      */     
/* 2077 */     accept(Token.ON);
/*      */     
/* 2079 */     if (this.lexer.token() == Token.TABLE) {
/* 2080 */       stmt.setType(SQLCommentStatement.Type.TABLE);
/* 2081 */       this.lexer.nextToken();
/* 2082 */     } else if (this.lexer.token() == Token.COLUMN) {
/* 2083 */       stmt.setType(SQLCommentStatement.Type.COLUMN);
/* 2084 */       this.lexer.nextToken();
/*      */     } 
/*      */     
/* 2087 */     stmt.setOn((SQLExpr)this.exprParser.name());
/*      */     
/* 2089 */     accept(Token.IS);
/* 2090 */     stmt.setComment(this.exprParser.expr());
/*      */     
/* 2092 */     return stmt;
/*      */   }
/*      */   
/*      */   protected SQLAlterTableAddColumn parseAlterTableAddColumn() {
/* 2096 */     boolean odps = "odps".equals(this.dbType);
/*      */     
/* 2098 */     if (odps) {
/* 2099 */       acceptIdentifier("COLUMNS");
/* 2100 */       accept(Token.LPAREN);
/*      */     } 
/*      */     
/* 2103 */     SQLAlterTableAddColumn item = new SQLAlterTableAddColumn();
/*      */     
/*      */     while (true) {
/* 2106 */       SQLColumnDefinition columnDef = this.exprParser.parseColumn();
/* 2107 */       item.addColumn(columnDef);
/* 2108 */       if (this.lexer.token() == Token.COMMA) {
/* 2109 */         this.lexer.nextToken();
/* 2110 */         if (identifierEquals("ADD")) {
/*      */           break;
/*      */         }
/*      */         
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 2118 */     if (odps) {
/* 2119 */       accept(Token.RPAREN);
/*      */     }
/* 2121 */     return item;
/*      */   }
/*      */   
/*      */   public SQLStatement parseStatement() {
/* 2125 */     return parseStatement(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SQLStatement parseStatement(boolean tryBest) {
/* 2134 */     List<SQLStatement> list = new ArrayList<>();
/* 2135 */     parseStatementList(list, 1);
/* 2136 */     if (tryBest && 
/* 2137 */       this.lexer.token() != Token.EOF) {
/* 2138 */       throw new ParserException("sql syntax error, no terminated. " + this.lexer.token());
/*      */     }
/*      */     
/* 2141 */     return list.get(0);
/*      */   }
/*      */   
/*      */   public SQLExplainStatement parseExplain() {
/* 2145 */     accept(Token.EXPLAIN);
/* 2146 */     if (identifierEquals("PLAN")) {
/* 2147 */       this.lexer.nextToken();
/*      */     }
/*      */     
/* 2150 */     if (this.lexer.token() == Token.FOR) {
/* 2151 */       this.lexer.nextToken();
/*      */     }
/*      */     
/* 2154 */     SQLExplainStatement explain = new SQLExplainStatement(getDbType());
/*      */     
/* 2156 */     if (this.lexer.token == Token.HINT) {
/* 2157 */       explain.setHints(this.exprParser.parseHints());
/*      */     }
/*      */     
/* 2160 */     explain.setStatement(parseStatement());
/*      */     
/* 2162 */     return explain;
/*      */   }
/*      */   
/*      */   protected SQLAlterTableAddIndex parseAlterTableAddIndex() {
/* 2166 */     SQLAlterTableAddIndex item = new SQLAlterTableAddIndex();
/*      */     
/* 2168 */     if (this.lexer.token() == Token.UNIQUE) {
/* 2169 */       item.setUnique(true);
/* 2170 */       this.lexer.nextToken();
/* 2171 */       if (this.lexer.token() == Token.INDEX) {
/* 2172 */         this.lexer.nextToken();
/* 2173 */       } else if (this.lexer.token() == Token.KEY) {
/* 2174 */         item.setKey(true);
/* 2175 */         this.lexer.nextToken();
/*      */       }
/*      */     
/* 2178 */     } else if (this.lexer.token() == Token.INDEX) {
/* 2179 */       accept(Token.INDEX);
/* 2180 */     } else if (this.lexer.token() == Token.KEY) {
/* 2181 */       item.setKey(true);
/* 2182 */       accept(Token.KEY);
/*      */     } 
/*      */ 
/*      */     
/* 2186 */     if (this.lexer.token() == Token.LPAREN) {
/* 2187 */       this.lexer.nextToken();
/*      */     } else {
/* 2189 */       item.setName(this.exprParser.name());
/* 2190 */       accept(Token.LPAREN);
/*      */     } 
/*      */     
/*      */     while (true) {
/* 2194 */       SQLSelectOrderByItem column = this.exprParser.parseSelectOrderByItem();
/* 2195 */       item.addItem(column);
/* 2196 */       if (this.lexer.token() == Token.COMMA) {
/* 2197 */         this.lexer.nextToken();
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 2202 */     accept(Token.RPAREN);
/* 2203 */     return item;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SQLOpenStatement parseOpen() {
/* 2212 */     SQLOpenStatement stmt = new SQLOpenStatement();
/* 2213 */     accept(Token.OPEN);
/* 2214 */     stmt.setCursorName(this.exprParser.name().getSimpleName());
/* 2215 */     accept(Token.SEMI);
/* 2216 */     return stmt;
/*      */   }
/*      */   
/*      */   public SQLFetchStatement parseFetch() {
/* 2220 */     accept(Token.FETCH);
/*      */     
/* 2222 */     SQLFetchStatement stmt = new SQLFetchStatement();
/* 2223 */     stmt.setCursorName(this.exprParser.name());
/* 2224 */     accept(Token.INTO);
/*      */     while (true) {
/* 2226 */       stmt.getInto().add(this.exprParser.name());
/* 2227 */       if (this.lexer.token() == Token.COMMA) {
/* 2228 */         this.lexer.nextToken();
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/*      */       break;
/*      */     } 
/* 2235 */     return stmt;
/*      */   }
/*      */   
/*      */   public SQLStatement parseClose() {
/* 2239 */     SQLCloseStatement stmt = new SQLCloseStatement();
/* 2240 */     accept(Token.CLOSE);
/* 2241 */     stmt.setCursorName(this.exprParser.name().getSimpleName());
/* 2242 */     accept(Token.SEMI);
/* 2243 */     return (SQLStatement)stmt;
/*      */   }
/*      */   
/*      */   public boolean isParseCompleteValues() {
/* 2247 */     return this.parseCompleteValues;
/*      */   }
/*      */   
/*      */   public void setParseCompleteValues(boolean parseCompleteValues) {
/* 2251 */     this.parseCompleteValues = parseCompleteValues;
/*      */   }
/*      */   
/*      */   public int getParseValuesSize() {
/* 2255 */     return this.parseValuesSize;
/*      */   }
/*      */   
/*      */   public void setParseValuesSize(int parseValuesSize) {
/* 2259 */     this.parseValuesSize = parseValuesSize;
/*      */   }
/*      */   
/*      */   public SQLMergeStatement parseMerge() {
/* 2263 */     accept(Token.MERGE);
/*      */     
/* 2265 */     SQLMergeStatement stmt = new SQLMergeStatement();
/*      */     
/* 2267 */     parseHints(stmt.getHints());
/*      */     
/* 2269 */     accept(Token.INTO);
/*      */     
/* 2271 */     if (this.lexer.token() == Token.LPAREN) {
/* 2272 */       this.lexer.nextToken();
/* 2273 */       SQLSelect select = createSQLSelectParser().select();
/* 2274 */       SQLSubqueryTableSource tableSource = new SQLSubqueryTableSource(select);
/* 2275 */       stmt.setInto((SQLTableSource)tableSource);
/* 2276 */       accept(Token.RPAREN);
/*      */     } else {
/* 2278 */       stmt.setInto(this.exprParser.name());
/*      */     } 
/*      */     
/* 2281 */     stmt.setAlias(as());
/*      */     
/* 2283 */     accept(Token.USING);
/*      */     
/* 2285 */     SQLTableSource using = createSQLSelectParser().parseTableSource();
/* 2286 */     stmt.setUsing(using);
/*      */     
/* 2288 */     accept(Token.ON);
/* 2289 */     stmt.setOn(this.exprParser.expr());
/*      */     
/* 2291 */     boolean insertFlag = false;
/* 2292 */     if (this.lexer.token() == Token.WHEN) {
/* 2293 */       this.lexer.nextToken();
/* 2294 */       if (this.lexer.token() == Token.MATCHED) {
/* 2295 */         SQLMergeStatement.MergeUpdateClause updateClause = new SQLMergeStatement.MergeUpdateClause();
/* 2296 */         this.lexer.nextToken();
/* 2297 */         accept(Token.THEN);
/* 2298 */         accept(Token.UPDATE);
/* 2299 */         accept(Token.SET);
/*      */         
/*      */         while (true) {
/* 2302 */           SQLUpdateSetItem item = this.exprParser.parseUpdateSetItem();
/*      */           
/* 2304 */           updateClause.addItem(item);
/* 2305 */           item.setParent((SQLObject)updateClause);
/*      */           
/* 2307 */           if (this.lexer.token() == Token.COMMA) {
/* 2308 */             this.lexer.nextToken();
/*      */             
/*      */             continue;
/*      */           } 
/*      */           
/*      */           break;
/*      */         } 
/* 2315 */         if (this.lexer.token() == Token.WHERE) {
/* 2316 */           this.lexer.nextToken();
/* 2317 */           updateClause.setWhere(this.exprParser.expr());
/*      */         } 
/*      */         
/* 2320 */         if (this.lexer.token() == Token.DELETE) {
/* 2321 */           this.lexer.nextToken();
/* 2322 */           accept(Token.WHERE);
/* 2323 */           updateClause.setWhere(this.exprParser.expr());
/*      */         } 
/*      */         
/* 2326 */         stmt.setUpdateClause(updateClause);
/* 2327 */       } else if (this.lexer.token() == Token.NOT) {
/* 2328 */         this.lexer.nextToken();
/* 2329 */         insertFlag = true;
/*      */       } 
/*      */     } 
/*      */     
/* 2333 */     if (!insertFlag) {
/* 2334 */       if (this.lexer.token() == Token.WHEN) {
/* 2335 */         this.lexer.nextToken();
/*      */       }
/*      */       
/* 2338 */       if (this.lexer.token() == Token.NOT) {
/* 2339 */         this.lexer.nextToken();
/* 2340 */         insertFlag = true;
/*      */       } 
/*      */     } 
/*      */     
/* 2344 */     if (insertFlag) {
/* 2345 */       SQLMergeStatement.MergeInsertClause insertClause = new SQLMergeStatement.MergeInsertClause();
/*      */       
/* 2347 */       accept(Token.MATCHED);
/* 2348 */       accept(Token.THEN);
/* 2349 */       accept(Token.INSERT);
/*      */       
/* 2351 */       if (this.lexer.token() == Token.LPAREN) {
/* 2352 */         accept(Token.LPAREN);
/* 2353 */         this.exprParser.exprList(insertClause.getColumns(), (SQLObject)insertClause);
/* 2354 */         accept(Token.RPAREN);
/*      */       } 
/* 2356 */       accept(Token.VALUES);
/* 2357 */       accept(Token.LPAREN);
/* 2358 */       this.exprParser.exprList(insertClause.getValues(), (SQLObject)insertClause);
/* 2359 */       accept(Token.RPAREN);
/*      */       
/* 2361 */       if (this.lexer.token() == Token.WHERE) {
/* 2362 */         this.lexer.nextToken();
/* 2363 */         insertClause.setWhere(this.exprParser.expr());
/*      */       } 
/*      */       
/* 2366 */       stmt.setInsertClause(insertClause);
/*      */     } 
/*      */     
/* 2369 */     SQLErrorLoggingClause errorClause = parseErrorLoggingClause();
/* 2370 */     stmt.setErrorLoggingClause(errorClause);
/*      */     
/* 2372 */     return stmt;
/*      */   }
/*      */   
/*      */   protected SQLErrorLoggingClause parseErrorLoggingClause() {
/* 2376 */     if (identifierEquals("LOG")) {
/* 2377 */       SQLErrorLoggingClause errorClause = new SQLErrorLoggingClause();
/*      */       
/* 2379 */       this.lexer.nextToken();
/* 2380 */       accept(Token.ERRORS);
/* 2381 */       if (this.lexer.token() == Token.INTO) {
/* 2382 */         this.lexer.nextToken();
/* 2383 */         errorClause.setInto(this.exprParser.name());
/*      */       } 
/*      */       
/* 2386 */       if (this.lexer.token() == Token.LPAREN) {
/* 2387 */         this.lexer.nextToken();
/* 2388 */         errorClause.setSimpleExpression(this.exprParser.expr());
/* 2389 */         accept(Token.RPAREN);
/*      */       } 
/*      */       
/* 2392 */       if (this.lexer.token() == Token.REJECT) {
/* 2393 */         this.lexer.nextToken();
/* 2394 */         accept(Token.LIMIT);
/* 2395 */         errorClause.setLimit(this.exprParser.expr());
/*      */       } 
/*      */       
/* 2398 */       return errorClause;
/*      */     } 
/* 2400 */     return null;
/*      */   }
/*      */   
/*      */   public void parseHints(List<SQLHint> hints) {
/* 2404 */     getExprParser().parseHints(hints);
/*      */   }
/*      */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\parser\SQLStatementParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */