/*      */ package com.tranboot.client.druid.sql.dialect.oracle.parser;
/*      */ 
/*      */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLOrderBy;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOperator;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLListExpr;
import com.tranboot.client.druid.sql.ast.statement.*;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.*;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.*;
import com.tranboot.client.druid.sql.parser.ParserException;
import com.tranboot.client.druid.sql.parser.SQLExprParser;
import com.tranboot.client.druid.sql.parser.SQLSelectParser;
import com.tranboot.client.druid.sql.parser.Token;

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
/*      */ public class OracleSelectParser
/*      */   extends SQLSelectParser
/*      */ {
/*      */   public OracleSelectParser(String sql) {
/*   75 */     super(new OracleExprParser(sql));
/*      */   }
/*      */   
/*      */   public OracleSelectParser(SQLExprParser exprParser) {
/*   79 */     super(exprParser);
/*      */   }
/*      */   
/*      */   public OracleSelect select() {
/*   83 */     OracleSelect select = new OracleSelect();
/*      */     
/*   85 */     withSubquery((SQLSelect)select);
/*      */     
/*   87 */     SQLSelectQuery query = query();
/*   88 */     select.setQuery(query);
/*      */     
/*   90 */     SQLOrderBy orderBy = parseOrderBy();
/*   91 */     select.setOrderBy(orderBy);
/*   92 */     if (orderBy != null && query instanceof SQLSelectQueryBlock) {
/*   93 */       SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;
/*   94 */       parseFetchClause(queryBlock);
/*      */     } 
/*      */     
/*   97 */     if (this.lexer.token() == Token.FOR) {
/*   98 */       this.lexer.nextToken();
/*   99 */       accept(Token.UPDATE);
/*      */       
/*  101 */       OracleSelectForUpdate forUpdate = new OracleSelectForUpdate();
/*      */       
/*  103 */       if (this.lexer.token() == Token.OF) {
/*  104 */         this.lexer.nextToken();
/*  105 */         this.exprParser.exprList(forUpdate.getOf(), (SQLObject)forUpdate);
/*      */       } 
/*      */       
/*  108 */       if (this.lexer.token() == Token.NOWAIT) {
/*  109 */         this.lexer.nextToken();
/*  110 */         forUpdate.setNotWait(true);
/*  111 */       } else if (this.lexer.token() == Token.WAIT) {
/*  112 */         this.lexer.nextToken();
/*  113 */         forUpdate.setWait(this.exprParser.primary());
/*  114 */       } else if (identifierEquals("SKIP")) {
/*  115 */         this.lexer.nextToken();
/*  116 */         acceptIdentifier("LOCKED");
/*  117 */         forUpdate.setSkipLocked(true);
/*      */       } 
/*      */       
/*  120 */       select.setForUpdate(forUpdate);
/*      */     } 
/*      */     
/*  123 */     if (select.getOrderBy() == null) {
/*  124 */       select.setOrderBy(this.exprParser.parseOrderBy());
/*      */     }
/*      */     
/*  127 */     if (this.lexer.token() == Token.WITH) {
/*  128 */       this.lexer.nextToken();
/*      */       
/*  130 */       if (identifierEquals("READ")) {
/*  131 */         this.lexer.nextToken();
/*      */         
/*  133 */         if (identifierEquals("ONLY")) {
/*  134 */           this.lexer.nextToken();
/*      */         } else {
/*  136 */           throw new ParserException("syntax error");
/*      */         } 
/*      */         
/*  139 */         select.setRestriction((OracleSelectRestriction)new OracleSelectRestriction.ReadOnly());
/*  140 */       } else if (this.lexer.token() == Token.CHECK) {
/*  141 */         this.lexer.nextToken();
/*      */         
/*  143 */         if (identifierEquals("OPTION")) {
/*  144 */           this.lexer.nextToken();
/*      */         } else {
/*  146 */           throw new ParserException("syntax error");
/*      */         } 
/*      */         
/*  149 */         OracleSelectRestriction.CheckOption checkOption = new OracleSelectRestriction.CheckOption();
/*      */         
/*  151 */         if (this.lexer.token() == Token.CONSTRAINT) {
/*  152 */           this.lexer.nextToken();
/*  153 */           throw new ParserException("TODO");
/*      */         } 
/*      */         
/*  156 */         select.setRestriction((OracleSelectRestriction)checkOption);
/*      */       } else {
/*  158 */         throw new ParserException("syntax error");
/*      */       } 
/*      */     } 
/*      */     
/*  162 */     return select;
/*      */   }
/*      */   
/*      */   protected void withSubquery(SQLSelect select) {
/*  166 */     if (this.lexer.token() == Token.WITH) {
/*  167 */       this.lexer.nextToken();
/*      */       
/*  169 */       SQLWithSubqueryClause subqueryFactoringClause = new SQLWithSubqueryClause();
/*      */       while (true) {
/*  171 */         OracleWithSubqueryEntry entry = new OracleWithSubqueryEntry();
/*  172 */         entry.setName((SQLIdentifierExpr)this.exprParser.name());
/*      */         
/*  174 */         if (this.lexer.token() == Token.LPAREN) {
/*  175 */           this.lexer.nextToken();
/*  176 */           this.exprParser.names(entry.getColumns());
/*  177 */           accept(Token.RPAREN);
/*      */         } 
/*      */         
/*  180 */         accept(Token.AS);
/*  181 */         accept(Token.LPAREN);
/*  182 */         entry.setSubQuery((SQLSelect)select());
/*  183 */         accept(Token.RPAREN);
/*      */         
/*  185 */         if (identifierEquals("SEARCH")) {
/*  186 */           this.lexer.nextToken();
/*  187 */           SearchClause searchClause = new SearchClause();
/*      */           
/*  189 */           if (this.lexer.token() != Token.IDENTIFIER) {
/*  190 */             throw new ParserException("syntax erorr : " + this.lexer.token());
/*      */           }
/*      */           
/*  193 */           searchClause.setType(SearchClause.Type.valueOf(this.lexer.stringVal()));
/*  194 */           this.lexer.nextToken();
/*      */           
/*  196 */           acceptIdentifier("FIRST");
/*  197 */           accept(Token.BY);
/*      */           
/*  199 */           searchClause.addItem(this.exprParser.parseSelectOrderByItem());
/*      */           
/*  201 */           while (this.lexer.token() == Token.COMMA) {
/*  202 */             this.lexer.nextToken();
/*  203 */             searchClause.addItem(this.exprParser.parseSelectOrderByItem());
/*      */           } 
/*      */           
/*  206 */           accept(Token.SET);
/*      */           
/*  208 */           searchClause.setOrderingColumn((SQLIdentifierExpr)this.exprParser.name());
/*      */           
/*  210 */           entry.setSearchClause(searchClause);
/*      */         } 
/*      */         
/*  213 */         if (identifierEquals("CYCLE")) {
/*  214 */           this.lexer.nextToken();
/*  215 */           CycleClause cycleClause = new CycleClause();
/*  216 */           this.exprParser.exprList(cycleClause.getAliases(), (SQLObject)cycleClause);
/*  217 */           accept(Token.SET);
/*  218 */           cycleClause.setMark(this.exprParser.expr());
/*  219 */           accept(Token.TO);
/*  220 */           cycleClause.setValue(this.exprParser.expr());
/*  221 */           accept(Token.DEFAULT);
/*  222 */           cycleClause.setDefaultValue(this.exprParser.expr());
/*  223 */           entry.setCycleClause(cycleClause);
/*      */         } 
/*      */         
/*  226 */         subqueryFactoringClause.addEntry((SQLWithSubqueryClause.Entry)entry);
/*      */         
/*  228 */         if (this.lexer.token() == Token.COMMA) {
/*  229 */           this.lexer.nextToken();
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/*      */         break;
/*      */       } 
/*  236 */       select.setWithSubQuery(subqueryFactoringClause);
/*      */     } 
/*      */   }
/*      */   
/*      */   public SQLSelectQuery query() {
/*  241 */     if (this.lexer.token() == Token.LPAREN) {
/*  242 */       this.lexer.nextToken();
/*      */       
/*  244 */       SQLSelectQuery select = query();
/*  245 */       accept(Token.RPAREN);
/*      */       
/*  247 */       return queryRest(select);
/*      */     } 
/*      */     
/*  250 */     OracleSelectQueryBlock queryBlock = new OracleSelectQueryBlock();
/*  251 */     if (this.lexer.token() == Token.SELECT) {
/*  252 */       this.lexer.nextToken();
/*      */       
/*  254 */       if (this.lexer.token() == Token.COMMENT) {
/*  255 */         this.lexer.nextToken();
/*      */       }
/*      */       
/*  258 */       parseHints(queryBlock);
/*      */       
/*  260 */       if (this.lexer.token() == Token.DISTINCT) {
/*  261 */         queryBlock.setDistionOption(2);
/*  262 */         this.lexer.nextToken();
/*  263 */       } else if (this.lexer.token() == Token.UNIQUE) {
/*  264 */         queryBlock.setDistionOption(3);
/*  265 */         this.lexer.nextToken();
/*  266 */       } else if (this.lexer.token() == Token.ALL) {
/*  267 */         queryBlock.setDistionOption(1);
/*  268 */         this.lexer.nextToken();
/*      */       } 
/*      */       
/*  271 */       this.exprParser.parseHints(queryBlock.getHints());
/*      */       
/*  273 */       parseSelectList((SQLSelectQueryBlock)queryBlock);
/*      */     } 
/*      */     
/*  276 */     parseInto(queryBlock);
/*      */     
/*  278 */     parseFrom((SQLSelectQueryBlock)queryBlock);
/*      */     
/*  280 */     parseWhere((SQLSelectQueryBlock)queryBlock);
/*      */     
/*  282 */     parseHierachical(queryBlock);
/*      */     
/*  284 */     parseGroupBy((SQLSelectQueryBlock)queryBlock);
/*      */     
/*  286 */     parseModelClause(queryBlock);
/*      */     
/*  288 */     parseFetchClause((SQLSelectQueryBlock)queryBlock);
/*      */     
/*  290 */     return queryRest((SQLSelectQuery)queryBlock);
/*      */   }
/*      */   
/*      */   public SQLSelectQuery queryRest(SQLSelectQuery selectQuery) {
/*  294 */     if (this.lexer.token() == Token.UNION) {
/*  295 */       SQLUnionQuery union = new SQLUnionQuery();
/*  296 */       union.setLeft(selectQuery);
/*      */       
/*  298 */       this.lexer.nextToken();
/*      */       
/*  300 */       if (this.lexer.token() == Token.ALL) {
/*  301 */         union.setOperator(SQLUnionOperator.UNION_ALL);
/*  302 */         this.lexer.nextToken();
/*  303 */       } else if (this.lexer.token() == Token.DISTINCT) {
/*  304 */         union.setOperator(SQLUnionOperator.DISTINCT);
/*  305 */         this.lexer.nextToken();
/*      */       } 
/*      */       
/*  308 */       SQLSelectQuery right = query();
/*      */       
/*  310 */       union.setRight(right);
/*      */       
/*  312 */       return queryRest((SQLSelectQuery)union);
/*      */     } 
/*      */     
/*  315 */     if (this.lexer.token() == Token.INTERSECT) {
/*  316 */       this.lexer.nextToken();
/*      */       
/*  318 */       SQLUnionQuery union = new SQLUnionQuery();
/*  319 */       union.setLeft(selectQuery);
/*      */       
/*  321 */       union.setOperator(SQLUnionOperator.INTERSECT);
/*      */       
/*  323 */       SQLSelectQuery right = query();
/*  324 */       union.setRight(right);
/*      */       
/*  326 */       return (SQLSelectQuery)union;
/*      */     } 
/*      */     
/*  329 */     if (this.lexer.token() == Token.MINUS) {
/*  330 */       this.lexer.nextToken();
/*      */       
/*  332 */       SQLUnionQuery union = new SQLUnionQuery();
/*  333 */       union.setLeft(selectQuery);
/*      */       
/*  335 */       union.setOperator(SQLUnionOperator.MINUS);
/*      */       
/*  337 */       SQLSelectQuery right = query();
/*  338 */       union.setRight(right);
/*      */       
/*  340 */       return (SQLSelectQuery)union;
/*      */     } 
/*      */     
/*  343 */     return selectQuery;
/*      */   }
/*      */   
/*      */   private void parseModelClause(OracleSelectQueryBlock queryBlock) {
/*  347 */     if (this.lexer.token() != Token.MODEL) {
/*      */       return;
/*      */     }
/*      */     
/*  351 */     this.lexer.nextToken();
/*      */     
/*  353 */     ModelClause model = new ModelClause();
/*  354 */     parseCellReferenceOptions(model.getCellReferenceOptions());
/*      */     
/*  356 */     if (identifierEquals("RETURN")) {
/*  357 */       this.lexer.nextToken();
/*  358 */       ModelClause.ReturnRowsClause returnRowsClause = new ModelClause.ReturnRowsClause();
/*  359 */       if (this.lexer.token() == Token.ALL) {
/*  360 */         this.lexer.nextToken();
/*  361 */         returnRowsClause.setAll(true);
/*      */       } else {
/*  363 */         acceptIdentifier("UPDATED");
/*      */       } 
/*  365 */       acceptIdentifier("ROWS");
/*      */       
/*  367 */       model.setReturnRowsClause(returnRowsClause);
/*      */     } 
/*      */     
/*  370 */     while (identifierEquals("REFERENCE")) {
/*  371 */       ModelClause.ReferenceModelClause referenceModelClause = new ModelClause.ReferenceModelClause();
/*  372 */       this.lexer.nextToken();
/*      */       
/*  374 */       SQLExpr name = expr();
/*  375 */       referenceModelClause.setName(name);
/*      */       
/*  377 */       accept(Token.ON);
/*  378 */       accept(Token.LPAREN);
/*  379 */       OracleSelect subQuery = select();
/*  380 */       accept(Token.RPAREN);
/*  381 */       referenceModelClause.setSubQuery((SQLSelect)subQuery);
/*      */       
/*  383 */       parseModelColumnClause(referenceModelClause);
/*      */       
/*  385 */       parseCellReferenceOptions(referenceModelClause.getCellReferenceOptions());
/*      */       
/*  387 */       model.getReferenceModelClauses().add(referenceModelClause);
/*      */     } 
/*      */     
/*  390 */     parseMainModelClause(model);
/*      */     
/*  392 */     queryBlock.setModelClause(model);
/*      */   }
/*      */   
/*      */   private void parseMainModelClause(ModelClause modelClause) {
/*  396 */     ModelClause.MainModelClause mainModel = new ModelClause.MainModelClause();
/*      */     
/*  398 */     if (identifierEquals("MAIN")) {
/*  399 */       this.lexer.nextToken();
/*  400 */       mainModel.setMainModelName(expr());
/*      */     } 
/*      */     
/*  403 */     ModelClause.ModelColumnClause modelColumnClause = new ModelClause.ModelColumnClause();
/*  404 */     parseQueryPartitionClause(modelColumnClause);
/*  405 */     mainModel.setModelColumnClause(modelColumnClause);
/*      */     
/*  407 */     acceptIdentifier("DIMENSION");
/*  408 */     accept(Token.BY);
/*  409 */     accept(Token.LPAREN);
/*      */     while (true) {
/*  411 */       if (this.lexer.token() == Token.RPAREN) {
/*  412 */         this.lexer.nextToken();
/*      */         
/*      */         break;
/*      */       } 
/*  416 */       ModelClause.ModelColumn column = new ModelClause.ModelColumn();
/*  417 */       column.setExpr(expr());
/*  418 */       column.setAlias(as());
/*  419 */       modelColumnClause.getDimensionByColumns().add(column);
/*      */       
/*  421 */       if (this.lexer.token() == Token.COMMA) {
/*  422 */         this.lexer.nextToken();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  427 */     acceptIdentifier("MEASURES");
/*  428 */     accept(Token.LPAREN);
/*      */     while (true) {
/*  430 */       if (this.lexer.token() == Token.RPAREN) {
/*  431 */         this.lexer.nextToken();
/*      */         
/*      */         break;
/*      */       } 
/*  435 */       ModelClause.ModelColumn column = new ModelClause.ModelColumn();
/*  436 */       column.setExpr(expr());
/*  437 */       column.setAlias(as());
/*  438 */       modelColumnClause.getMeasuresColumns().add(column);
/*      */       
/*  440 */       if (this.lexer.token() == Token.COMMA) {
/*  441 */         this.lexer.nextToken();
/*      */       }
/*      */     } 
/*      */     
/*  445 */     mainModel.setModelColumnClause(modelColumnClause);
/*      */     
/*  447 */     parseCellReferenceOptions(mainModel.getCellReferenceOptions());
/*      */     
/*  449 */     parseModelRulesClause(mainModel);
/*      */     
/*  451 */     modelClause.setMainModel(mainModel);
/*      */   }
/*      */   
/*      */   private void parseModelRulesClause(ModelClause.MainModelClause mainModel) {
/*  455 */     ModelClause.ModelRulesClause modelRulesClause = new ModelClause.ModelRulesClause();
/*  456 */     if (identifierEquals("RULES")) {
/*  457 */       this.lexer.nextToken();
/*  458 */       if (this.lexer.token() == Token.UPDATE) {
/*  459 */         modelRulesClause.getOptions().add(ModelClause.ModelRuleOption.UPDATE);
/*  460 */         this.lexer.nextToken();
/*  461 */       } else if (identifierEquals("UPSERT")) {
/*  462 */         modelRulesClause.getOptions().add(ModelClause.ModelRuleOption.UPSERT);
/*  463 */         this.lexer.nextToken();
/*      */       } 
/*      */       
/*  466 */       if (identifierEquals("AUTOMATIC")) {
/*  467 */         this.lexer.nextToken();
/*  468 */         accept(Token.ORDER);
/*  469 */         modelRulesClause.getOptions().add(ModelClause.ModelRuleOption.AUTOMATIC_ORDER);
/*  470 */       } else if (identifierEquals("SEQUENTIAL")) {
/*  471 */         this.lexer.nextToken();
/*  472 */         accept(Token.ORDER);
/*  473 */         modelRulesClause.getOptions().add(ModelClause.ModelRuleOption.SEQUENTIAL_ORDER);
/*      */       } 
/*      */     } 
/*      */     
/*  477 */     if (identifierEquals("ITERATE")) {
/*  478 */       this.lexer.nextToken();
/*  479 */       accept(Token.LPAREN);
/*  480 */       modelRulesClause.setIterate(expr());
/*  481 */       accept(Token.RPAREN);
/*      */       
/*  483 */       if (identifierEquals("UNTIL")) {
/*  484 */         this.lexer.nextToken();
/*  485 */         accept(Token.LPAREN);
/*  486 */         modelRulesClause.setUntil(expr());
/*  487 */         accept(Token.RPAREN);
/*      */       } 
/*      */     } 
/*      */     
/*  491 */     accept(Token.LPAREN);
/*      */     while (true) {
/*  493 */       if (this.lexer.token() == Token.RPAREN) {
/*  494 */         this.lexer.nextToken();
/*      */         
/*      */         break;
/*      */       } 
/*  498 */       ModelClause.CellAssignmentItem item = new ModelClause.CellAssignmentItem();
/*  499 */       if (this.lexer.token() == Token.UPDATE) {
/*  500 */         item.setOption(ModelClause.ModelRuleOption.UPDATE);
/*  501 */       } else if (identifierEquals("UPSERT")) {
/*  502 */         item.setOption(ModelClause.ModelRuleOption.UPSERT);
/*      */       } 
/*      */       
/*  505 */       item.setCellAssignment(parseCellAssignment());
/*  506 */       item.setOrderBy(parseOrderBy());
/*  507 */       accept(Token.EQ);
/*  508 */       item.setExpr(expr());
/*      */       
/*  510 */       modelRulesClause.getCellAssignmentItems().add(item);
/*      */     } 
/*      */     
/*  513 */     mainModel.setModelRulesClause(modelRulesClause);
/*      */   }
/*      */   
/*      */   private ModelClause.CellAssignment parseCellAssignment() {
/*  517 */     ModelClause.CellAssignment cellAssignment = new ModelClause.CellAssignment();
/*      */     
/*  519 */     cellAssignment.setMeasureColumn(expr());
/*  520 */     accept(Token.LBRACKET);
/*  521 */     this.exprParser.exprList(cellAssignment.getConditions(), (SQLObject)cellAssignment);
/*  522 */     accept(Token.RBRACKET);
/*      */     
/*  524 */     return cellAssignment;
/*      */   }
/*      */   
/*      */   private void parseQueryPartitionClause(ModelClause.ModelColumnClause modelColumnClause) {
/*  528 */     if (identifierEquals("PARTITION")) {
/*  529 */       ModelClause.QueryPartitionClause queryPartitionClause = new ModelClause.QueryPartitionClause();
/*      */       
/*  531 */       this.lexer.nextToken();
/*  532 */       accept(Token.BY);
/*  533 */       if (this.lexer.token() == Token.LPAREN) {
/*  534 */         this.lexer.nextToken();
/*  535 */         this.exprParser.exprList(queryPartitionClause.getExprList(), (SQLObject)queryPartitionClause);
/*  536 */         accept(Token.RPAREN);
/*      */       } else {
/*  538 */         this.exprParser.exprList(queryPartitionClause.getExprList(), (SQLObject)queryPartitionClause);
/*      */       } 
/*  540 */       modelColumnClause.setQueryPartitionClause(queryPartitionClause);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void parseModelColumnClause(ModelClause.ReferenceModelClause referenceModelClause) {
/*  545 */     throw new ParserException();
/*      */   }
/*      */   
/*      */   private void parseCellReferenceOptions(List<ModelClause.CellReferenceOption> options) {
/*  549 */     if (identifierEquals("IGNORE")) {
/*  550 */       this.lexer.nextToken();
/*  551 */       acceptIdentifier("NAV");
/*  552 */       options.add(ModelClause.CellReferenceOption.IgnoreNav);
/*  553 */     } else if (identifierEquals("KEEP")) {
/*  554 */       this.lexer.nextToken();
/*  555 */       acceptIdentifier("NAV");
/*  556 */       options.add(ModelClause.CellReferenceOption.KeepNav);
/*      */     } 
/*      */     
/*  559 */     if (this.lexer.token() == Token.UNIQUE) {
/*  560 */       this.lexer.nextToken();
/*  561 */       if (identifierEquals("DIMENSION")) {
/*  562 */         this.lexer.nextToken();
/*  563 */         options.add(ModelClause.CellReferenceOption.UniqueDimension);
/*      */       } else {
/*  565 */         acceptIdentifier("SINGLE");
/*  566 */         acceptIdentifier("REFERENCE");
/*  567 */         options.add(ModelClause.CellReferenceOption.UniqueDimension);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected String as() {
/*  573 */     if (this.lexer.token() == Token.CONNECT) {
/*  574 */       return null;
/*      */     }
/*      */     
/*  577 */     return super.as();
/*      */   }
/*      */   
/*      */   private void parseHierachical(OracleSelectQueryBlock queryBlock) {
/*  581 */     OracleSelectHierachicalQueryClause hierachical = null;
/*      */     
/*  583 */     if (this.lexer.token() == Token.CONNECT) {
/*  584 */       hierachical = new OracleSelectHierachicalQueryClause();
/*  585 */       this.lexer.nextToken();
/*  586 */       accept(Token.BY);
/*      */       
/*  588 */       if (this.lexer.token() == Token.PRIOR) {
/*  589 */         this.lexer.nextToken();
/*  590 */         hierachical.setPrior(true);
/*      */       } 
/*      */       
/*  593 */       if (identifierEquals("NOCYCLE")) {
/*  594 */         hierachical.setNoCycle(true);
/*  595 */         this.lexer.nextToken();
/*      */         
/*  597 */         if (this.lexer.token() == Token.PRIOR) {
/*  598 */           this.lexer.nextToken();
/*  599 */           hierachical.setPrior(true);
/*      */         } 
/*      */       } 
/*  602 */       hierachical.setConnectBy(this.exprParser.expr());
/*      */     } 
/*      */     
/*  605 */     if (this.lexer.token() == Token.START) {
/*  606 */       this.lexer.nextToken();
/*  607 */       if (hierachical == null) {
/*  608 */         hierachical = new OracleSelectHierachicalQueryClause();
/*      */       }
/*  610 */       accept(Token.WITH);
/*      */       
/*  612 */       hierachical.setStartWith(this.exprParser.expr());
/*      */     } 
/*      */     
/*  615 */     if (this.lexer.token() == Token.CONNECT) {
/*  616 */       if (hierachical == null) {
/*  617 */         hierachical = new OracleSelectHierachicalQueryClause();
/*      */       }
/*      */       
/*  620 */       this.lexer.nextToken();
/*  621 */       accept(Token.BY);
/*      */       
/*  623 */       if (this.lexer.token() == Token.PRIOR) {
/*  624 */         this.lexer.nextToken();
/*  625 */         hierachical.setPrior(true);
/*      */       } 
/*      */       
/*  628 */       if (identifierEquals("NOCYCLE")) {
/*  629 */         hierachical.setNoCycle(true);
/*  630 */         this.lexer.nextToken();
/*      */         
/*  632 */         if (this.lexer.token() == Token.PRIOR) {
/*  633 */           this.lexer.nextToken();
/*  634 */           hierachical.setPrior(true);
/*      */         } 
/*      */       } 
/*  637 */       hierachical.setConnectBy(this.exprParser.expr());
/*      */     } 
/*      */     
/*  640 */     if (hierachical != null) {
/*  641 */       queryBlock.setHierachicalQueryClause(hierachical);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public SQLTableSource parseTableSource() {
/*  647 */     if (this.lexer.token() == Token.LPAREN) {
/*  648 */       OracleSelectSubqueryTableSource tableSource; this.lexer.nextToken();
/*      */       
/*  650 */       if (this.lexer.token() == Token.SELECT || this.lexer.token() == Token.WITH) {
/*  651 */         tableSource = new OracleSelectSubqueryTableSource((SQLSelect)select());
/*  652 */       } else if (this.lexer.token() == Token.LPAREN) {
/*  653 */         tableSource = new OracleSelectSubqueryTableSource((SQLSelect)select());
/*      */       } else {
/*  655 */         throw new ParserException("TODO :" + this.lexer.token());
/*      */       } 
/*  657 */       accept(Token.RPAREN);
/*      */       
/*  659 */       parsePivot((OracleSelectTableSource)tableSource);
/*      */       
/*  661 */       return parseTableSourceRest((OracleSelectTableSource)tableSource);
/*      */     } 
/*      */     
/*  664 */     if (this.lexer.token() == Token.SELECT) {
/*  665 */       throw new ParserException("TODO");
/*      */     }
/*      */     
/*  668 */     OracleSelectTableReference tableReference = new OracleSelectTableReference();
/*      */     
/*  670 */     if (identifierEquals("ONLY")) {
/*  671 */       this.lexer.nextToken();
/*  672 */       tableReference.setOnly(true);
/*  673 */       accept(Token.LPAREN);
/*  674 */       parseTableSourceQueryTableExpr(tableReference);
/*  675 */       accept(Token.RPAREN);
/*      */     } else {
/*  677 */       parseTableSourceQueryTableExpr(tableReference);
/*  678 */       parsePivot((OracleSelectTableSource)tableReference);
/*      */     } 
/*      */     
/*  681 */     return parseTableSourceRest((OracleSelectTableSource)tableReference);
/*      */   }
/*      */   
/*      */   private void parseTableSourceQueryTableExpr(OracleSelectTableReference tableReference) {
/*  685 */     tableReference.setExpr(this.exprParser.expr());
/*      */ 
/*      */     
/*  688 */     FlashbackQueryClause clause = flashback();
/*  689 */     tableReference.setFlashback(clause);
/*      */ 
/*      */     
/*  692 */     if (identifierEquals("SAMPLE")) {
/*  693 */       this.lexer.nextToken();
/*      */       
/*  695 */       SampleClause sample = new SampleClause();
/*      */       
/*  697 */       if (identifierEquals("BLOCK")) {
/*  698 */         sample.setBlock(true);
/*  699 */         this.lexer.nextToken();
/*      */       } 
/*      */       
/*  702 */       accept(Token.LPAREN);
/*  703 */       this.exprParser.exprList(sample.getPercent(), (SQLObject)sample);
/*  704 */       accept(Token.RPAREN);
/*      */       
/*  706 */       if (identifierEquals("SEED")) {
/*  707 */         this.lexer.nextToken();
/*  708 */         accept(Token.LPAREN);
/*  709 */         sample.setSeedValue(expr());
/*  710 */         accept(Token.RPAREN);
/*      */       } 
/*      */       
/*  713 */       tableReference.setSampleClause(sample);
/*      */     } 
/*      */     
/*  716 */     if (identifierEquals("PARTITION")) {
/*  717 */       this.lexer.nextToken();
/*  718 */       PartitionExtensionClause partition = new PartitionExtensionClause();
/*      */       
/*  720 */       if (this.lexer.token() == Token.LPAREN) {
/*  721 */         this.lexer.nextToken();
/*  722 */         partition.setPartition(this.exprParser.name());
/*  723 */         accept(Token.RPAREN);
/*      */       } else {
/*  725 */         accept(Token.FOR);
/*  726 */         accept(Token.LPAREN);
/*  727 */         this.exprParser.names(partition.getFor());
/*  728 */         accept(Token.RPAREN);
/*      */       } 
/*      */       
/*  731 */       tableReference.setPartition(partition);
/*      */     } 
/*      */     
/*  734 */     if (identifierEquals("SUBPARTITION")) {
/*  735 */       this.lexer.nextToken();
/*  736 */       PartitionExtensionClause partition = new PartitionExtensionClause();
/*  737 */       partition.setSubPartition(true);
/*      */       
/*  739 */       if (this.lexer.token() == Token.LPAREN) {
/*  740 */         this.lexer.nextToken();
/*  741 */         partition.setPartition(this.exprParser.name());
/*  742 */         accept(Token.RPAREN);
/*      */       } else {
/*  744 */         accept(Token.FOR);
/*  745 */         accept(Token.LPAREN);
/*  746 */         this.exprParser.names(partition.getFor());
/*  747 */         accept(Token.RPAREN);
/*      */       } 
/*      */       
/*  750 */       tableReference.setPartition(partition);
/*      */     } 
/*      */     
/*  753 */     if (identifierEquals("VERSIONS")) {
/*  754 */       this.lexer.nextToken();
/*      */       
/*  756 */       if (this.lexer.token() == Token.BETWEEN) {
/*  757 */         this.lexer.nextToken();
/*      */         
/*  759 */         FlashbackQueryClause.VersionsFlashbackQueryClause versionsFlashbackQueryClause = new FlashbackQueryClause.VersionsFlashbackQueryClause();
/*  760 */         if (identifierEquals("SCN")) {
/*  761 */           versionsFlashbackQueryClause.setType(FlashbackQueryClause.Type.SCN);
/*  762 */           this.lexer.nextToken();
/*      */         } else {
/*  764 */           acceptIdentifier("TIMESTAMP");
/*  765 */           versionsFlashbackQueryClause.setType(FlashbackQueryClause.Type.TIMESTAMP);
/*      */         } 
/*      */         
/*  768 */         SQLBinaryOpExpr binaryExpr = (SQLBinaryOpExpr)this.exprParser.expr();
/*  769 */         if (binaryExpr.getOperator() != SQLBinaryOperator.BooleanAnd) {
/*  770 */           throw new ParserException("syntax error : " + binaryExpr.getOperator());
/*      */         }
/*      */         
/*  773 */         versionsFlashbackQueryClause.setBegin(binaryExpr.getLeft());
/*  774 */         versionsFlashbackQueryClause.setEnd(binaryExpr.getRight());
/*      */         
/*  776 */         tableReference.setFlashback((FlashbackQueryClause)versionsFlashbackQueryClause);
/*      */       } else {
/*  778 */         throw new ParserException("TODO");
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private FlashbackQueryClause flashback() {
/*  785 */     if (this.lexer.token() == Token.AS) {
/*  786 */       this.lexer.nextToken();
/*      */     }
/*      */     
/*  789 */     if (this.lexer.token() == Token.OF) {
/*  790 */       this.lexer.nextToken();
/*      */       
/*  792 */       if (identifierEquals("SCN")) {
/*  793 */         FlashbackQueryClause.AsOfFlashbackQueryClause asOfFlashbackQueryClause = new FlashbackQueryClause.AsOfFlashbackQueryClause();
/*  794 */         asOfFlashbackQueryClause.setType(FlashbackQueryClause.Type.SCN);
/*  795 */         this.lexer.nextToken();
/*  796 */         asOfFlashbackQueryClause.setExpr(this.exprParser.expr());
/*      */         
/*  798 */         return (FlashbackQueryClause)asOfFlashbackQueryClause;
/*  799 */       }  if (identifierEquals("SNAPSHOT")) {
/*  800 */         this.lexer.nextToken();
/*  801 */         accept(Token.LPAREN);
/*  802 */         FlashbackQueryClause.AsOfSnapshotClause asOfSnapshotClause = new FlashbackQueryClause.AsOfSnapshotClause();
/*  803 */         asOfSnapshotClause.setExpr(expr());
/*  804 */         accept(Token.RPAREN);
/*      */         
/*  806 */         return (FlashbackQueryClause)asOfSnapshotClause;
/*      */       } 
/*  808 */       FlashbackQueryClause.AsOfFlashbackQueryClause clause = new FlashbackQueryClause.AsOfFlashbackQueryClause();
/*  809 */       acceptIdentifier("TIMESTAMP");
/*  810 */       clause.setType(FlashbackQueryClause.Type.TIMESTAMP);
/*  811 */       clause.setExpr(this.exprParser.expr());
/*      */       
/*  813 */       return (FlashbackQueryClause)clause;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  818 */     return null;
/*      */   }
/*      */   
/*      */   protected SQLTableSource parseTableSourceRest(OracleSelectTableSource tableSource) {
/*  822 */     if (this.lexer.token() == Token.AS) {
/*  823 */       this.lexer.nextToken();
/*      */       
/*  825 */       if (this.lexer.token() == Token.OF) {
/*  826 */         tableSource.setFlashback(flashback());
/*      */       }
/*      */       
/*  829 */       tableSource.setAlias(as());
/*  830 */     } else if ((tableSource.getAlias() == null || tableSource.getAlias().length() == 0) && 
/*  831 */       this.lexer.token() != Token.LEFT && this.lexer.token() != Token.RIGHT && this.lexer.token() != Token.FULL) {
/*  832 */       tableSource.setAlias(as());
/*      */     } 
/*      */ 
/*      */     
/*  836 */     if (this.lexer.token() == Token.HINT) {
/*  837 */       this.exprParser.parseHints(tableSource.getHints());
/*      */     }
/*      */     
/*  840 */     SQLJoinTableSource.JoinType joinType = null;
/*      */     
/*  842 */     if (this.lexer.token() == Token.LEFT) {
/*  843 */       this.lexer.nextToken();
/*  844 */       if (this.lexer.token() == Token.OUTER) {
/*  845 */         this.lexer.nextToken();
/*      */       }
/*  847 */       accept(Token.JOIN);
/*  848 */       joinType = SQLJoinTableSource.JoinType.LEFT_OUTER_JOIN;
/*      */     } 
/*      */     
/*  851 */     if (this.lexer.token() == Token.RIGHT) {
/*  852 */       this.lexer.nextToken();
/*  853 */       if (this.lexer.token() == Token.OUTER) {
/*  854 */         this.lexer.nextToken();
/*      */       }
/*  856 */       accept(Token.JOIN);
/*  857 */       joinType = SQLJoinTableSource.JoinType.RIGHT_OUTER_JOIN;
/*      */     } 
/*      */     
/*  860 */     if (this.lexer.token() == Token.FULL) {
/*  861 */       this.lexer.nextToken();
/*  862 */       if (this.lexer.token() == Token.OUTER) {
/*  863 */         this.lexer.nextToken();
/*      */       }
/*  865 */       accept(Token.JOIN);
/*  866 */       joinType = SQLJoinTableSource.JoinType.FULL_OUTER_JOIN;
/*      */     } 
/*      */     
/*  869 */     if (this.lexer.token() == Token.INNER) {
/*  870 */       this.lexer.nextToken();
/*  871 */       accept(Token.JOIN);
/*  872 */       joinType = SQLJoinTableSource.JoinType.INNER_JOIN;
/*      */     } 
/*  874 */     if (this.lexer.token() == Token.CROSS) {
/*  875 */       this.lexer.nextToken();
/*  876 */       accept(Token.JOIN);
/*  877 */       joinType = SQLJoinTableSource.JoinType.CROSS_JOIN;
/*      */     } 
/*      */     
/*  880 */     if (this.lexer.token() == Token.JOIN) {
/*  881 */       this.lexer.nextToken();
/*  882 */       joinType = SQLJoinTableSource.JoinType.JOIN;
/*      */     } 
/*      */     
/*  885 */     if (this.lexer.token() == Token.COMMA) {
/*  886 */       this.lexer.nextToken();
/*  887 */       joinType = SQLJoinTableSource.JoinType.COMMA;
/*      */     } 
/*      */     
/*  890 */     if (joinType != null) {
/*  891 */       OracleSelectJoin join = new OracleSelectJoin();
/*  892 */       join.setLeft((SQLTableSource)tableSource);
/*  893 */       join.setJoinType(joinType);
/*  894 */       join.setRight(parseTableSource());
/*      */       
/*  896 */       if (this.lexer.token() == Token.ON) {
/*  897 */         this.lexer.nextToken();
/*  898 */         join.setCondition(this.exprParser.expr());
/*  899 */       } else if (this.lexer.token() == Token.USING) {
/*  900 */         this.lexer.nextToken();
/*  901 */         accept(Token.LPAREN);
/*  902 */         this.exprParser.exprList(join.getUsing(), (SQLObject)join);
/*  903 */         accept(Token.RPAREN);
/*      */       } 
/*      */       
/*  906 */       return parseTableSourceRest((OracleSelectTableSource)join);
/*      */     } 
/*      */     
/*  909 */     return (SQLTableSource)tableSource;
/*      */   }
/*      */ 
/*      */   
/*      */   private void parsePivot(OracleSelectTableSource tableSource) {
/*  914 */     if (identifierEquals("PIVOT")) {
/*  915 */       this.lexer.nextToken();
/*      */       
/*  917 */       OracleSelectPivot pivot = new OracleSelectPivot();
/*      */       
/*  919 */       if (identifierEquals("XML")) {
/*  920 */         this.lexer.nextToken();
/*  921 */         pivot.setXml(true);
/*      */       } 
/*      */       
/*  924 */       accept(Token.LPAREN);
/*      */       while (true) {
/*  926 */         OracleSelectPivot.Item item = new OracleSelectPivot.Item();
/*  927 */         item.setExpr(this.exprParser.expr());
/*  928 */         item.setAlias(as());
/*  929 */         pivot.addItem(item);
/*      */         
/*  931 */         if (this.lexer.token() != Token.COMMA) {
/*      */           break;
/*      */         }
/*  934 */         this.lexer.nextToken();
/*      */       } 
/*      */       
/*  937 */       accept(Token.FOR);
/*      */       
/*  939 */       if (this.lexer.token() == Token.LPAREN) {
/*  940 */         this.lexer.nextToken();
/*      */         while (true) {
/*  942 */           pivot.getPivotFor().add(new SQLIdentifierExpr(this.lexer.stringVal()));
/*  943 */           this.lexer.nextToken();
/*      */           
/*  945 */           if (this.lexer.token() != Token.COMMA) {
/*      */             break;
/*      */           }
/*  948 */           this.lexer.nextToken();
/*      */         } 
/*      */         
/*  951 */         accept(Token.RPAREN);
/*      */       } else {
/*  953 */         pivot.getPivotFor().add(new SQLIdentifierExpr(this.lexer.stringVal()));
/*  954 */         this.lexer.nextToken();
/*      */       } 
/*      */       
/*  957 */       accept(Token.IN);
/*  958 */       accept(Token.LPAREN);
/*  959 */       if (this.lexer.token() == Token.LPAREN) {
/*  960 */         throw new ParserException("TODO");
/*      */       }
/*      */       
/*  963 */       if (this.lexer.token() == Token.SELECT) {
/*  964 */         throw new ParserException("TODO");
/*      */       }
/*      */       
/*      */       while (true) {
/*  968 */         OracleSelectPivot.Item item = new OracleSelectPivot.Item();
/*  969 */         item.setExpr(this.exprParser.expr());
/*  970 */         item.setAlias(as());
/*  971 */         pivot.getPivotIn().add(item);
/*      */         
/*  973 */         if (this.lexer.token() != Token.COMMA) {
/*      */           break;
/*      */         }
/*      */         
/*  977 */         this.lexer.nextToken();
/*      */       } 
/*      */       
/*  980 */       accept(Token.RPAREN);
/*      */       
/*  982 */       accept(Token.RPAREN);
/*      */       
/*  984 */       tableSource.setPivot((OracleSelectPivotBase)pivot);
/*  985 */     } else if (identifierEquals("UNPIVOT")) {
/*  986 */       this.lexer.nextToken();
/*      */       
/*  988 */       OracleSelectUnPivot unPivot = new OracleSelectUnPivot();
/*  989 */       if (identifierEquals("INCLUDE")) {
/*  990 */         this.lexer.nextToken();
/*  991 */         acceptIdentifier("NULLS");
/*  992 */         unPivot.setNullsIncludeType(OracleSelectUnPivot.NullsIncludeType.INCLUDE_NULLS);
/*  993 */       } else if (identifierEquals("EXCLUDE")) {
/*  994 */         this.lexer.nextToken();
/*  995 */         acceptIdentifier("NULLS");
/*  996 */         unPivot.setNullsIncludeType(OracleSelectUnPivot.NullsIncludeType.EXCLUDE_NULLS);
/*      */       } 
/*      */       
/*  999 */       accept(Token.LPAREN);
/*      */       
/* 1001 */       if (this.lexer.token() == Token.LPAREN) {
/* 1002 */         this.lexer.nextToken();
/* 1003 */         this.exprParser.exprList(unPivot.getItems(), (SQLObject)unPivot);
/* 1004 */         accept(Token.RPAREN);
/*      */       } else {
/* 1006 */         unPivot.addItem(this.exprParser.expr());
/*      */       } 
/*      */       
/* 1009 */       accept(Token.FOR);
/*      */       
/* 1011 */       if (this.lexer.token() == Token.LPAREN) {
/* 1012 */         this.lexer.nextToken();
/*      */         while (true) {
/* 1014 */           unPivot.getPivotFor().add(new SQLIdentifierExpr(this.lexer.stringVal()));
/* 1015 */           this.lexer.nextToken();
/*      */           
/* 1017 */           if (this.lexer.token() != Token.COMMA) {
/*      */             break;
/*      */           }
/* 1020 */           this.lexer.nextToken();
/*      */         } 
/*      */         
/* 1023 */         accept(Token.RPAREN);
/*      */       } else {
/* 1025 */         unPivot.getPivotFor().add(new SQLIdentifierExpr(this.lexer.stringVal()));
/* 1026 */         this.lexer.nextToken();
/*      */       } 
/*      */       
/* 1029 */       accept(Token.IN);
/* 1030 */       accept(Token.LPAREN);
/* 1031 */       if (this.lexer.token() == Token.LPAREN) {
/* 1032 */         throw new ParserException("TODO");
/*      */       }
/*      */       
/* 1035 */       if (this.lexer.token() == Token.SELECT) {
/* 1036 */         throw new ParserException("TODO");
/*      */       }
/*      */       
/*      */       while (true) {
/* 1040 */         OracleSelectPivot.Item item = new OracleSelectPivot.Item();
/* 1041 */         item.setExpr(this.exprParser.expr());
/* 1042 */         item.setAlias(as());
/* 1043 */         unPivot.getPivotIn().add(item);
/*      */         
/* 1045 */         if (this.lexer.token() != Token.COMMA) {
/*      */           break;
/*      */         }
/*      */         
/* 1049 */         this.lexer.nextToken();
/*      */       } 
/*      */       
/* 1052 */       accept(Token.RPAREN);
/*      */       
/* 1054 */       accept(Token.RPAREN);
/*      */       
/* 1056 */       tableSource.setPivot((OracleSelectPivotBase)unPivot);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void parseInto(OracleSelectQueryBlock x) {
/* 1061 */     if (this.lexer.token() == Token.INTO) {
/* 1062 */       this.lexer.nextToken();
/* 1063 */       SQLExpr expr = expr();
/* 1064 */       if (this.lexer.token() != Token.COMMA) {
/* 1065 */         x.setInto(expr);
/*      */         return;
/*      */       } 
/* 1068 */       SQLListExpr list = new SQLListExpr();
/* 1069 */       list.addItem(expr);
/* 1070 */       while (this.lexer.token() == Token.COMMA) {
/* 1071 */         this.lexer.nextToken();
/* 1072 */         list.addItem(expr());
/*      */       } 
/* 1074 */       x.setInto((SQLExpr)list);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void parseHints(OracleSelectQueryBlock queryBlock) {
/* 1079 */     this.exprParser.parseHints(queryBlock.getHints());
/*      */   }
/*      */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\parser\OracleSelectParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */