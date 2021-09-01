/*      */ package com.tranboot.client.druid.sql.dialect.mysql.parser;
/*      */ 
/*      */

import com.tranboot.client.druid.sql.ast.*;
import com.tranboot.client.druid.sql.ast.expr.*;
import com.tranboot.client.druid.sql.ast.statement.SQLAssignItem;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnDefinition;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MySqlUnique;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MysqlForeignKey;
import com.tranboot.client.druid.sql.dialect.mysql.ast.expr.*;
import com.tranboot.client.druid.sql.parser.*;

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
/*      */ public class MySqlExprParser
/*      */   extends SQLExprParser
/*      */ {
/*   63 */   public static String[] AGGREGATE_FUNCTIONS = new String[] { "AVG", "COUNT", "GROUP_CONCAT", "MAX", "MIN", "STDDEV", "SUM" };
/*      */   
/*      */   public MySqlExprParser(Lexer lexer) {
/*   66 */     super(lexer, "mysql");
/*   67 */     this.aggregateFunctions = AGGREGATE_FUNCTIONS;
/*      */   }
/*      */   
/*      */   public MySqlExprParser(String sql) {
/*   71 */     this(new MySqlLexer(sql));
/*   72 */     this.lexer.nextToken();
/*      */   }
/*      */   
/*      */   public SQLExpr relationalRest(SQLExpr expr) {
/*   76 */     if (identifierEquals("REGEXP")) {
/*   77 */       this.lexer.nextToken();
/*   78 */       SQLExpr rightExp = equality();
/*      */       
/*   80 */       rightExp = relationalRest(rightExp);
/*      */       
/*   82 */       return (SQLExpr)new SQLBinaryOpExpr(expr, SQLBinaryOperator.RegExp, rightExp, "mysql");
/*      */     } 
/*      */     
/*   85 */     return super.relationalRest(expr);
/*      */   }
/*      */   public SQLExpr multiplicativeRest(SQLExpr expr) {
/*      */     SQLExpr sQLExpr;
/*   89 */     Token token = this.lexer.token();
/*   90 */     if (token == Token.IDENTIFIER && "MOD".equalsIgnoreCase(this.lexer.stringVal())) {
/*   91 */       this.lexer.nextToken();
/*   92 */       SQLExpr rightExp = primary();
/*      */       
/*   94 */       rightExp = relationalRest(rightExp);
/*      */       
/*   96 */       return (SQLExpr)new SQLBinaryOpExpr(expr, SQLBinaryOperator.Modulus, rightExp, "mysql");
/*   97 */     }  if (token == Token.DIV) {
/*   98 */       this.lexer.nextToken();
/*   99 */       SQLExpr rightExp = bitXor();
/*  100 */       SQLBinaryOpExpr sQLBinaryOpExpr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.DIV, rightExp, getDbType());
/*  101 */       sQLExpr = multiplicativeRest((SQLExpr)sQLBinaryOpExpr);
/*      */     } 
/*      */     
/*  104 */     return super.multiplicativeRest(sQLExpr);
/*      */   }
/*      */   
/*      */   public SQLExpr notRationalRest(SQLExpr expr) {
/*  108 */     if (identifierEquals("REGEXP")) {
/*  109 */       this.lexer.nextToken();
/*  110 */       SQLExpr rightExp = primary();
/*      */       
/*  112 */       rightExp = relationalRest(rightExp);
/*      */       
/*  114 */       return (SQLExpr)new SQLBinaryOpExpr(expr, SQLBinaryOperator.NotRegExp, rightExp, "mysql");
/*      */     } 
/*      */     
/*  117 */     return super.notRationalRest(expr); } public SQLExpr primary() {
/*      */     String aliasValue;
/*      */     SQLVariantRefExpr varRefExpr;
/*      */     SQLUnaryExpr binaryExpr;
/*  121 */     Token tok = this.lexer.token();
/*      */     
/*  123 */     if (identifierEquals("outfile")) {
/*  124 */       this.lexer.nextToken();
/*  125 */       SQLExpr file = primary();
/*  126 */       MySqlOutFileExpr mySqlOutFileExpr = new MySqlOutFileExpr(file);
/*      */       
/*  128 */       return primaryRest((SQLExpr)mySqlOutFileExpr);
/*      */     } 
/*      */ 
/*      */     
/*  132 */     switch (tok) {
/*      */       case LITERAL_ALIAS:
/*  134 */         aliasValue = this.lexer.stringVal();
/*  135 */         this.lexer.nextToken();
/*  136 */         return primaryRest((SQLExpr)new SQLCharExpr(aliasValue));
/*      */       case VARIANT:
/*  138 */         varRefExpr = new SQLVariantRefExpr(this.lexer.stringVal());
/*  139 */         this.lexer.nextToken();
/*  140 */         if (varRefExpr.getName().equalsIgnoreCase("@@global")) {
/*  141 */           accept(Token.DOT);
/*  142 */           varRefExpr = new SQLVariantRefExpr(this.lexer.stringVal(), true);
/*  143 */           this.lexer.nextToken();
/*  144 */         } else if (varRefExpr.getName().equals("@") && this.lexer.token() == Token.LITERAL_CHARS) {
/*  145 */           varRefExpr.setName("@'" + this.lexer.stringVal() + "'");
/*  146 */           this.lexer.nextToken();
/*  147 */         } else if (varRefExpr.getName().equals("@@") && this.lexer.token() == Token.LITERAL_CHARS) {
/*  148 */           varRefExpr.setName("@@'" + this.lexer.stringVal() + "'");
/*  149 */           this.lexer.nextToken();
/*      */         } 
/*  151 */         return primaryRest((SQLExpr)varRefExpr);
/*      */       case VALUES:
/*  153 */         this.lexer.nextToken();
/*  154 */         if (this.lexer.token() != Token.LPAREN) {
/*  155 */           throw new ParserException("syntax error, illegal values clause");
/*      */         }
/*  157 */         return methodRest((SQLExpr)new SQLIdentifierExpr("VALUES"), true);
/*      */       case BINARY:
/*  159 */         this.lexer.nextToken();
/*  160 */         if (this.lexer.token() == Token.COMMA || this.lexer.token() == Token.SEMI || this.lexer.token() == Token.EOF) {
/*  161 */           return (SQLExpr)new SQLIdentifierExpr("BINARY");
/*      */         }
/*  163 */         binaryExpr = new SQLUnaryExpr(SQLUnaryOperator.BINARY, expr());
/*  164 */         return primaryRest((SQLExpr)binaryExpr);
/*      */       
/*      */       case CACHE:
/*      */       case GROUP:
/*  168 */         this.lexer.nextToken();
/*  169 */         return primaryRest((SQLExpr)new SQLIdentifierExpr(this.lexer.stringVal()));
/*      */     } 
/*  171 */     return super.primary();
/*      */   }
/*      */   public final SQLExpr primaryRest(SQLExpr expr) {
/*      */     SQLMethodInvokeExpr sQLMethodInvokeExpr2;
/*      */     SQLBinaryExpr sQLBinaryExpr;
/*      */     SQLMethodInvokeExpr sQLMethodInvokeExpr1;
/*  177 */     if (expr == null) {
/*  178 */       throw new IllegalArgumentException("expr");
/*      */     }
/*      */     
/*  181 */     if (this.lexer.token() == Token.LITERAL_CHARS) {
/*  182 */       MySqlCharExpr mySqlCharExpr; if (expr instanceof SQLIdentifierExpr) {
/*  183 */         SQLIdentifierExpr identExpr = (SQLIdentifierExpr)expr;
/*  184 */         String ident = identExpr.getName();
/*      */         
/*  186 */         if (ident.equalsIgnoreCase("x")) {
/*  187 */           String charValue = this.lexer.stringVal();
/*  188 */           this.lexer.nextToken();
/*  189 */           SQLHexExpr sQLHexExpr = new SQLHexExpr(charValue);
/*      */           
/*  191 */           return primaryRest((SQLExpr)sQLHexExpr);
/*  192 */         }  if (ident.equalsIgnoreCase("b")) {
/*  193 */           String charValue = this.lexer.stringVal();
/*  194 */           this.lexer.nextToken();
/*  195 */           sQLBinaryExpr = new SQLBinaryExpr(charValue);
/*      */           
/*  197 */           return primaryRest((SQLExpr)sQLBinaryExpr);
/*  198 */         }  if (ident.startsWith("_")) {
/*  199 */           String charValue = this.lexer.stringVal();
/*  200 */           this.lexer.nextToken();
/*      */           
/*  202 */           MySqlCharExpr mysqlCharExpr = new MySqlCharExpr(charValue);
/*  203 */           mysqlCharExpr.setCharset(identExpr.getName());
/*  204 */           if (identifierEquals("COLLATE")) {
/*  205 */             this.lexer.nextToken();
/*      */             
/*  207 */             String collate = this.lexer.stringVal();
/*  208 */             mysqlCharExpr.setCollate(collate);
/*  209 */             accept(Token.IDENTIFIER);
/*      */           } 
/*      */           
/*  212 */           mySqlCharExpr = mysqlCharExpr;
/*      */           
/*  214 */           return primaryRest((SQLExpr)mySqlCharExpr);
/*      */         } 
/*  216 */       } else if (mySqlCharExpr instanceof SQLCharExpr) {
/*  217 */         SQLMethodInvokeExpr concat = new SQLMethodInvokeExpr("CONCAT");
/*  218 */         concat.addParameter((SQLExpr)mySqlCharExpr);
/*      */         while (true)
/*  220 */         { String chars = this.lexer.stringVal();
/*  221 */           concat.addParameter((SQLExpr)new SQLCharExpr(chars));
/*  222 */           this.lexer.nextToken();
/*  223 */           if (this.lexer.token() != Token.LITERAL_CHARS && this.lexer.token() != Token.LITERAL_ALIAS)
/*  224 */           { sQLMethodInvokeExpr2 = concat; break; }  } 
/*      */       } 
/*  226 */     } else if (this.lexer.token() == Token.IDENTIFIER) {
/*  227 */       if (sQLMethodInvokeExpr2 instanceof SQLHexExpr)
/*  228 */       { if ("USING".equalsIgnoreCase(this.lexer.stringVal())) {
/*  229 */           this.lexer.nextToken();
/*  230 */           if (this.lexer.token() != Token.IDENTIFIER) {
/*  231 */             throw new ParserException("syntax error, illegal hex");
/*      */           }
/*  233 */           String charSet = this.lexer.stringVal();
/*  234 */           this.lexer.nextToken();
/*  235 */           sQLMethodInvokeExpr2.getAttributes().put("USING", charSet);
/*      */           
/*  237 */           return primaryRest((SQLExpr)sQLMethodInvokeExpr2);
/*      */         }  }
/*  239 */       else { SQLBinaryOpExpr sQLBinaryOpExpr; if ("COLLATE".equalsIgnoreCase(this.lexer.stringVal())) {
/*  240 */           this.lexer.nextToken();
/*      */           
/*  242 */           if (this.lexer.token() == Token.EQ) {
/*  243 */             this.lexer.nextToken();
/*      */           }
/*      */           
/*  246 */           if (this.lexer.token() != Token.IDENTIFIER) {
/*  247 */             throw new ParserException("syntax error");
/*      */           }
/*      */           
/*  250 */           String collate = this.lexer.stringVal();
/*  251 */           this.lexer.nextToken();
/*      */           
/*  253 */           SQLBinaryOpExpr binaryExpr = new SQLBinaryOpExpr((SQLExpr)sQLMethodInvokeExpr2, SQLBinaryOperator.COLLATE, (SQLExpr)new SQLIdentifierExpr(collate), "mysql");
/*      */ 
/*      */           
/*  256 */           sQLBinaryOpExpr = binaryExpr;
/*      */           
/*  258 */           return primaryRest((SQLExpr)sQLBinaryOpExpr);
/*  259 */         }  if (sQLBinaryOpExpr instanceof SQLVariantRefExpr) {
/*  260 */           if ("COLLATE".equalsIgnoreCase(this.lexer.stringVal())) {
/*  261 */             this.lexer.nextToken();
/*      */             
/*  263 */             if (this.lexer.token() != Token.IDENTIFIER) {
/*  264 */               throw new ParserException("syntax error");
/*      */             }
/*      */             
/*  267 */             String collate = this.lexer.stringVal();
/*  268 */             this.lexer.nextToken();
/*      */             
/*  270 */             sQLBinaryOpExpr.putAttribute("COLLATE", collate);
/*      */             
/*  272 */             return primaryRest((SQLExpr)sQLBinaryOpExpr);
/*      */           } 
/*  274 */         } else if (sQLBinaryOpExpr instanceof SQLIntegerExpr) {
/*  275 */           SQLIntegerExpr intExpr = (SQLIntegerExpr)sQLBinaryOpExpr;
/*  276 */           String binaryString = this.lexer.stringVal();
/*  277 */           if (intExpr.getNumber().intValue() == 0 && binaryString.startsWith("b")) {
/*  278 */             this.lexer.nextToken();
/*  279 */             sQLBinaryExpr = new SQLBinaryExpr(binaryString.substring(1));
/*      */             
/*  281 */             return primaryRest((SQLExpr)sQLBinaryExpr);
/*      */           } 
/*      */         }  }
/*      */     
/*      */     } 
/*  286 */     if (this.lexer.token() == Token.LPAREN && sQLBinaryExpr instanceof SQLIdentifierExpr) {
/*  287 */       SQLIdentifierExpr identExpr = (SQLIdentifierExpr)sQLBinaryExpr;
/*  288 */       String ident = identExpr.getName();
/*      */       
/*  290 */       if ("EXTRACT".equalsIgnoreCase(ident)) {
/*  291 */         this.lexer.nextToken();
/*      */         
/*  293 */         if (this.lexer.token() != Token.IDENTIFIER) {
/*  294 */           throw new ParserException("syntax error");
/*      */         }
/*      */         
/*  297 */         String unitVal = this.lexer.stringVal();
/*  298 */         MySqlIntervalUnit unit = MySqlIntervalUnit.valueOf(unitVal.toUpperCase());
/*  299 */         this.lexer.nextToken();
/*      */         
/*  301 */         accept(Token.FROM);
/*      */         
/*  303 */         SQLExpr value = expr();
/*      */         
/*  305 */         MySqlExtractExpr extract = new MySqlExtractExpr();
/*  306 */         extract.setValue(value);
/*  307 */         extract.setUnit(unit);
/*  308 */         accept(Token.RPAREN);
/*      */         
/*  310 */         MySqlExtractExpr mySqlExtractExpr1 = extract;
/*      */         
/*  312 */         return primaryRest((SQLExpr)mySqlExtractExpr1);
/*  313 */       }  if ("SUBSTRING".equalsIgnoreCase(ident)) {
/*  314 */         this.lexer.nextToken();
/*  315 */         SQLMethodInvokeExpr methodInvokeExpr = new SQLMethodInvokeExpr(ident);
/*      */         while (true) {
/*  317 */           SQLExpr param = expr();
/*  318 */           methodInvokeExpr.addParameter(param);
/*      */           
/*  320 */           if (this.lexer.token() == Token.COMMA) {
/*  321 */             this.lexer.nextToken(); continue;
/*      */           }  break;
/*  323 */         }  if (this.lexer.token() == Token.FROM) {
/*  324 */           this.lexer.nextToken();
/*  325 */           SQLExpr from = expr();
/*  326 */           methodInvokeExpr.addParameter(from);
/*      */           
/*  328 */           if (this.lexer.token() == Token.FOR) {
/*  329 */             this.lexer.nextToken();
/*  330 */             SQLExpr forExpr = expr();
/*  331 */             methodInvokeExpr.addParameter(forExpr);
/*      */           }
/*      */         
/*  334 */         } else if (this.lexer.token() != Token.RPAREN) {
/*      */ 
/*      */           
/*  337 */           throw new ParserException("syntax error");
/*      */         } 
/*      */ 
/*      */         
/*  341 */         accept(Token.RPAREN);
/*  342 */         sQLMethodInvokeExpr1 = methodInvokeExpr;
/*      */         
/*  344 */         return primaryRest((SQLExpr)sQLMethodInvokeExpr1);
/*  345 */       }  if ("TRIM".equalsIgnoreCase(ident)) {
/*  346 */         this.lexer.nextToken();
/*  347 */         SQLMethodInvokeExpr methodInvokeExpr = new SQLMethodInvokeExpr(ident);
/*      */         
/*  349 */         if (this.lexer.token() == Token.IDENTIFIER) {
/*  350 */           String flagVal = this.lexer.stringVal();
/*  351 */           if ("LEADING".equalsIgnoreCase(flagVal)) {
/*  352 */             this.lexer.nextToken();
/*  353 */             methodInvokeExpr.getAttributes().put("TRIM_TYPE", "LEADING");
/*  354 */           } else if ("BOTH".equalsIgnoreCase(flagVal)) {
/*  355 */             this.lexer.nextToken();
/*  356 */             methodInvokeExpr.getAttributes().put("TRIM_TYPE", "BOTH");
/*  357 */           } else if ("TRAILING".equalsIgnoreCase(flagVal)) {
/*  358 */             this.lexer.nextToken();
/*  359 */             methodInvokeExpr.putAttribute("TRIM_TYPE", "TRAILING");
/*      */           } 
/*      */         } 
/*      */         
/*  363 */         SQLExpr param = expr();
/*  364 */         methodInvokeExpr.addParameter(param);
/*      */         
/*  366 */         if (this.lexer.token() == Token.FROM) {
/*  367 */           this.lexer.nextToken();
/*  368 */           SQLExpr from = expr();
/*  369 */           methodInvokeExpr.putAttribute("FROM", from);
/*      */         } 
/*      */         
/*  372 */         accept(Token.RPAREN);
/*  373 */         sQLMethodInvokeExpr1 = methodInvokeExpr;
/*      */         
/*  375 */         return primaryRest((SQLExpr)sQLMethodInvokeExpr1);
/*  376 */       }  if ("MATCH".equalsIgnoreCase(ident)) {
/*  377 */         this.lexer.nextToken();
/*  378 */         MySqlMatchAgainstExpr matchAgainstExpr = new MySqlMatchAgainstExpr();
/*      */         
/*  380 */         if (this.lexer.token() == Token.RPAREN) {
/*  381 */           this.lexer.nextToken();
/*      */         } else {
/*  383 */           exprList(matchAgainstExpr.getColumns(), (SQLObject)matchAgainstExpr);
/*  384 */           accept(Token.RPAREN);
/*      */         } 
/*      */         
/*  387 */         acceptIdentifier("AGAINST");
/*      */         
/*  389 */         accept(Token.LPAREN);
/*  390 */         SQLExpr against = primary();
/*  391 */         matchAgainstExpr.setAgainst(against);
/*      */         
/*  393 */         if (this.lexer.token() == Token.IN) {
/*  394 */           this.lexer.nextToken();
/*  395 */           if (identifierEquals("NATURAL")) {
/*  396 */             this.lexer.nextToken();
/*  397 */             acceptIdentifier("LANGUAGE");
/*  398 */             acceptIdentifier("MODE");
/*  399 */             if (this.lexer.token() == Token.WITH) {
/*  400 */               this.lexer.nextToken();
/*  401 */               acceptIdentifier("QUERY");
/*  402 */               acceptIdentifier("EXPANSION");
/*  403 */               matchAgainstExpr.setSearchModifier(MySqlMatchAgainstExpr.SearchModifier.IN_NATURAL_LANGUAGE_MODE_WITH_QUERY_EXPANSION);
/*      */             } else {
/*  405 */               matchAgainstExpr.setSearchModifier(MySqlMatchAgainstExpr.SearchModifier.IN_NATURAL_LANGUAGE_MODE);
/*      */             } 
/*  407 */           } else if (identifierEquals("BOOLEAN")) {
/*  408 */             this.lexer.nextToken();
/*  409 */             acceptIdentifier("MODE");
/*  410 */             matchAgainstExpr.setSearchModifier(MySqlMatchAgainstExpr.SearchModifier.IN_BOOLEAN_MODE);
/*      */           } else {
/*  412 */             throw new ParserException("TODO");
/*      */           } 
/*  414 */         } else if (this.lexer.token() == Token.WITH) {
/*  415 */           throw new ParserException("TODO");
/*      */         } 
/*      */         
/*  418 */         accept(Token.RPAREN);
/*      */         
/*  420 */         MySqlMatchAgainstExpr mySqlMatchAgainstExpr1 = matchAgainstExpr;
/*      */         
/*  422 */         return primaryRest((SQLExpr)mySqlMatchAgainstExpr1);
/*  423 */       }  if ("CONVERT".equalsIgnoreCase(ident) || "CHAR".equalsIgnoreCase(ident)) {
/*  424 */         this.lexer.nextToken();
/*  425 */         SQLMethodInvokeExpr methodInvokeExpr = new SQLMethodInvokeExpr(ident);
/*      */         
/*  427 */         if (this.lexer.token() != Token.RPAREN) {
/*  428 */           exprList(methodInvokeExpr.getParameters(), (SQLObject)methodInvokeExpr);
/*      */         }
/*      */         
/*  431 */         if (identifierEquals("USING")) {
/*  432 */           this.lexer.nextToken();
/*  433 */           if (this.lexer.token() != Token.IDENTIFIER) {
/*  434 */             throw new ParserException("syntax error");
/*      */           }
/*  436 */           String charset = this.lexer.stringVal();
/*  437 */           this.lexer.nextToken();
/*  438 */           methodInvokeExpr.putAttribute("USING", charset);
/*      */         } 
/*      */         
/*  441 */         accept(Token.RPAREN);
/*      */         
/*  443 */         sQLMethodInvokeExpr1 = methodInvokeExpr;
/*      */         
/*  445 */         return primaryRest((SQLExpr)sQLMethodInvokeExpr1);
/*  446 */       }  if ("POSITION".equalsIgnoreCase(ident)) {
/*  447 */         accept(Token.LPAREN);
/*  448 */         SQLExpr subStr = primary();
/*  449 */         accept(Token.IN);
/*  450 */         SQLExpr str = expr();
/*  451 */         accept(Token.RPAREN);
/*      */         
/*  453 */         SQLMethodInvokeExpr locate = new SQLMethodInvokeExpr("LOCATE");
/*  454 */         locate.addParameter(subStr);
/*  455 */         locate.addParameter(str);
/*      */         
/*  457 */         sQLMethodInvokeExpr1 = locate;
/*  458 */         return primaryRest((SQLExpr)sQLMethodInvokeExpr1);
/*      */       } 
/*      */     } 
/*      */     
/*  462 */     if (this.lexer.token() == Token.VARIANT && "@".equals(this.lexer.stringVal())) {
/*  463 */       this.lexer.nextToken();
/*  464 */       MySqlUserName userName = new MySqlUserName();
/*  465 */       if (sQLMethodInvokeExpr1 instanceof SQLCharExpr) {
/*  466 */         userName.setUserName(((SQLCharExpr)sQLMethodInvokeExpr1).toString());
/*      */       } else {
/*  468 */         userName.setUserName(((SQLIdentifierExpr)sQLMethodInvokeExpr1).getName());
/*      */       } 
/*      */       
/*  471 */       if (this.lexer.token() == Token.LITERAL_CHARS) {
/*  472 */         userName.setHost("'" + this.lexer.stringVal() + "'");
/*      */       } else {
/*  474 */         userName.setHost(this.lexer.stringVal());
/*      */       } 
/*  476 */       this.lexer.nextToken();
/*  477 */       return (SQLExpr)userName;
/*      */     } 
/*      */     
/*  480 */     if (this.lexer.token() == Token.ERROR) {
/*  481 */       throw new ParserException("syntax error, token: " + this.lexer.token() + " " + this.lexer.stringVal() + ", pos : " + this.lexer
/*  482 */           .pos());
/*      */     }
/*      */     
/*  485 */     return super.primaryRest((SQLExpr)sQLMethodInvokeExpr1);
/*      */   }
/*      */   
/*      */   public SQLSelectParser createSelectParser() {
/*  489 */     return new MySqlSelectParser(this);
/*      */   }
/*      */   
/*      */   protected SQLExpr parseInterval() {
/*  493 */     accept(Token.INTERVAL);
/*      */     
/*  495 */     if (this.lexer.token() == Token.LPAREN) {
/*  496 */       this.lexer.nextToken();
/*      */       
/*  498 */       SQLMethodInvokeExpr methodInvokeExpr = new SQLMethodInvokeExpr("INTERVAL");
/*  499 */       if (this.lexer.token() != Token.RPAREN) {
/*  500 */         exprList(methodInvokeExpr.getParameters(), (SQLObject)methodInvokeExpr);
/*      */       }
/*      */       
/*  503 */       accept(Token.RPAREN);
/*      */ 
/*      */ 
/*      */       
/*  507 */       if (methodInvokeExpr.getParameters().size() == 1 && this.lexer
/*  508 */         .token() == Token.IDENTIFIER) {
/*  509 */         SQLExpr sQLExpr = methodInvokeExpr.getParameters().get(0);
/*  510 */         String str = this.lexer.stringVal();
/*  511 */         this.lexer.nextToken();
/*      */         
/*  513 */         MySqlIntervalExpr mySqlIntervalExpr = new MySqlIntervalExpr();
/*  514 */         mySqlIntervalExpr.setValue(sQLExpr);
/*  515 */         mySqlIntervalExpr.setUnit(MySqlIntervalUnit.valueOf(str.toUpperCase()));
/*  516 */         return (SQLExpr)mySqlIntervalExpr;
/*      */       } 
/*  518 */       return primaryRest((SQLExpr)methodInvokeExpr);
/*      */     } 
/*      */     
/*  521 */     SQLExpr value = expr();
/*      */     
/*  523 */     if (this.lexer.token() != Token.IDENTIFIER) {
/*  524 */       throw new ParserException("Syntax error");
/*      */     }
/*      */     
/*  527 */     String unit = this.lexer.stringVal();
/*  528 */     this.lexer.nextToken();
/*      */     
/*  530 */     MySqlIntervalExpr intervalExpr = new MySqlIntervalExpr();
/*  531 */     intervalExpr.setValue(value);
/*  532 */     intervalExpr.setUnit(MySqlIntervalUnit.valueOf(unit.toUpperCase()));
/*      */     
/*  534 */     return (SQLExpr)intervalExpr;
/*      */   }
/*      */ 
/*      */   
/*      */   public SQLColumnDefinition parseColumn() {
/*  539 */     SQLColumnDefinition column = new SQLColumnDefinition();
/*  540 */     column.setName(name());
/*  541 */     column.setDataType(parseDataType());
/*      */     
/*  543 */     return parseColumnRest(column);
/*      */   }
/*      */   
/*      */   public SQLColumnDefinition parseColumnRest(SQLColumnDefinition column) {
/*  547 */     if (this.lexer.token() == Token.ON) {
/*  548 */       this.lexer.nextToken();
/*  549 */       accept(Token.UPDATE);
/*  550 */       SQLExpr expr = expr();
/*  551 */       column.setOnUpdate(expr);
/*      */     } 
/*  553 */     if (identifierEquals("CHARSET")) {
/*  554 */       this.lexer.nextToken();
/*  555 */       MySqlCharExpr charSetCollateExpr = new MySqlCharExpr();
/*  556 */       charSetCollateExpr.setCharset(this.lexer.stringVal());
/*  557 */       this.lexer.nextToken();
/*  558 */       if (identifierEquals("COLLATE")) {
/*  559 */         this.lexer.nextToken();
/*  560 */         charSetCollateExpr.setCollate(this.lexer.stringVal());
/*  561 */         this.lexer.nextToken();
/*      */       } 
/*  563 */       column.setCharsetExpr((SQLExpr)charSetCollateExpr);
/*  564 */       return parseColumnRest(column);
/*      */     } 
/*  566 */     if (identifierEquals("AUTO_INCREMENT")) {
/*  567 */       this.lexer.nextToken();
/*  568 */       column.setAutoIncrement(true);
/*  569 */       return parseColumnRest(column);
/*      */     } 
/*      */     
/*  572 */     if (identifierEquals("precision") && column.getDataType().getName().equalsIgnoreCase("double")) {
/*  573 */       this.lexer.nextToken();
/*      */     }
/*      */     
/*  576 */     if (this.lexer.token() == Token.PARTITION) {
/*  577 */       throw new ParserException("syntax error " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */     }
/*      */     
/*  580 */     if (identifierEquals("STORAGE")) {
/*  581 */       this.lexer.nextToken();
/*  582 */       SQLExpr expr = expr();
/*  583 */       column.setStorage(expr);
/*      */     } 
/*      */     
/*  586 */     if (this.lexer.token() == Token.AS) {
/*  587 */       this.lexer.nextToken();
/*  588 */       accept(Token.LPAREN);
/*  589 */       SQLExpr expr = expr();
/*  590 */       column.setAsExpr(expr);
/*  591 */       accept(Token.RPAREN);
/*      */     } 
/*      */     
/*  594 */     if (identifierEquals("STORED")) {
/*  595 */       this.lexer.nextToken();
/*  596 */       column.setSorted(true);
/*      */     } 
/*      */     
/*  599 */     if (identifierEquals("VIRTUAL")) {
/*  600 */       this.lexer.nextToken();
/*  601 */       column.setVirtual(true);
/*      */     } 
/*      */     
/*  604 */     super.parseColumnRest(column);
/*      */     
/*  606 */     return column;
/*      */   }
/*      */   
/*      */   protected SQLDataType parseDataTypeRest(SQLDataType dataType) {
/*  610 */     super.parseDataTypeRest(dataType);
/*      */     
/*  612 */     if (identifierEquals("UNSIGNED")) {
/*  613 */       this.lexer.nextToken();
/*  614 */       dataType.getAttributes().put("UNSIGNED", Boolean.valueOf(true));
/*      */     } 
/*      */     
/*  617 */     if (identifierEquals("ZEROFILL")) {
/*  618 */       this.lexer.nextToken();
/*  619 */       dataType.getAttributes().put("ZEROFILL", Boolean.valueOf(true));
/*      */     } 
/*      */     
/*  622 */     return dataType;
/*      */   }
/*      */   
/*      */   public SQLExpr orRest(SQLExpr expr) {
/*      */     SQLBinaryOpExpr sQLBinaryOpExpr;
/*      */     while (true) {
/*  628 */       if (this.lexer.token() == Token.OR || this.lexer.token() == Token.BARBAR) {
/*  629 */         this.lexer.nextToken();
/*  630 */         SQLExpr rightExp = and();
/*      */         
/*  632 */         sQLBinaryOpExpr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.BooleanOr, rightExp, "mysql"); continue;
/*  633 */       }  if (this.lexer.token() == Token.XOR) {
/*  634 */         this.lexer.nextToken();
/*  635 */         SQLExpr rightExp = and();
/*      */         
/*  637 */         sQLBinaryOpExpr = new SQLBinaryOpExpr((SQLExpr)sQLBinaryOpExpr, SQLBinaryOperator.BooleanXor, rightExp, "mysql");
/*      */         
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/*  643 */     return (SQLExpr)sQLBinaryOpExpr;
/*      */   }
/*      */   public SQLExpr additiveRest(SQLExpr expr) {
/*      */     SQLExpr sQLExpr;
/*  647 */     if (this.lexer.token() == Token.PLUS) {
/*  648 */       this.lexer.nextToken();
/*  649 */       SQLExpr rightExp = multiplicative();
/*      */       
/*  651 */       SQLBinaryOpExpr sQLBinaryOpExpr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Add, rightExp, "mysql");
/*  652 */       sQLExpr = additiveRest((SQLExpr)sQLBinaryOpExpr);
/*  653 */     } else if (this.lexer.token() == Token.SUB) {
/*  654 */       this.lexer.nextToken();
/*  655 */       SQLExpr rightExp = multiplicative();
/*      */       
/*  657 */       SQLBinaryOpExpr sQLBinaryOpExpr = new SQLBinaryOpExpr(sQLExpr, SQLBinaryOperator.Subtract, rightExp, "mysql");
/*  658 */       sQLExpr = additiveRest((SQLExpr)sQLBinaryOpExpr);
/*      */     } 
/*      */     
/*  661 */     return sQLExpr;
/*      */   }
/*      */   public SQLAssignItem parseAssignItem() {
/*      */     SQLIdentifierExpr sQLIdentifierExpr;
/*  665 */     SQLAssignItem item = new SQLAssignItem();
/*      */     
/*  667 */     SQLExpr var = primary();
/*      */     
/*  669 */     String ident = null;
/*  670 */     if (var instanceof SQLIdentifierExpr) {
/*  671 */       ident = ((SQLIdentifierExpr)var).getName();
/*      */       
/*  673 */       if ("GLOBAL".equalsIgnoreCase(ident)) {
/*  674 */         ident = this.lexer.stringVal();
/*  675 */         this.lexer.nextToken();
/*  676 */         SQLVariantRefExpr sQLVariantRefExpr = new SQLVariantRefExpr(ident, true);
/*  677 */       } else if ("SESSION".equalsIgnoreCase(ident)) {
/*  678 */         ident = this.lexer.stringVal();
/*  679 */         this.lexer.nextToken();
/*  680 */         SQLVariantRefExpr sQLVariantRefExpr = new SQLVariantRefExpr(ident, false);
/*      */       } else {
/*  682 */         SQLVariantRefExpr sQLVariantRefExpr = new SQLVariantRefExpr(ident);
/*      */       } 
/*      */     } 
/*      */     
/*  686 */     if (!"NAMES".equalsIgnoreCase(ident))
/*      */     {
/*  688 */       if ("CHARACTER".equalsIgnoreCase(ident)) {
/*  689 */         sQLIdentifierExpr = new SQLIdentifierExpr("CHARACTER SET");
/*  690 */         accept(Token.SET);
/*  691 */         if (this.lexer.token() == Token.EQ) {
/*  692 */           this.lexer.nextToken();
/*      */         }
/*      */       }
/*  695 */       else if (this.lexer.token() == Token.COLONEQ) {
/*  696 */         this.lexer.nextToken();
/*      */       } else {
/*  698 */         accept(Token.EQ);
/*      */       } 
/*      */     }
/*      */     
/*  702 */     if (this.lexer.token() == Token.ON) {
/*  703 */       this.lexer.nextToken();
/*  704 */       item.setValue((SQLExpr)new SQLIdentifierExpr("ON"));
/*      */     } else {
/*  706 */       item.setValue(expr());
/*      */     } 
/*      */     
/*  709 */     item.setTarget((SQLExpr)sQLIdentifierExpr);
/*  710 */     return item;
/*      */   }
/*      */   
/*      */   public SQLName nameRest(SQLName name) {
/*  714 */     if (this.lexer.token() == Token.VARIANT && "@".equals(this.lexer.stringVal())) {
/*  715 */       this.lexer.nextToken();
/*  716 */       MySqlUserName userName = new MySqlUserName();
/*  717 */       userName.setUserName(((SQLIdentifierExpr)name).getName());
/*      */       
/*  719 */       if (this.lexer.token() == Token.LITERAL_CHARS) {
/*  720 */         userName.setHost("'" + this.lexer.stringVal() + "'");
/*      */       } else {
/*  722 */         userName.setHost(this.lexer.stringVal());
/*      */       } 
/*  724 */       this.lexer.nextToken();
/*  725 */       return (SQLName)userName;
/*      */     } 
/*  727 */     return super.nameRest(name);
/*      */   }
/*      */ 
/*      */   
/*      */   public MySqlPrimaryKey parsePrimaryKey() {
/*  732 */     accept(Token.PRIMARY);
/*  733 */     accept(Token.KEY);
/*      */     
/*  735 */     MySqlPrimaryKey primaryKey = new MySqlPrimaryKey();
/*      */     
/*  737 */     if (identifierEquals("USING")) {
/*  738 */       this.lexer.nextToken();
/*  739 */       primaryKey.setIndexType(this.lexer.stringVal());
/*  740 */       this.lexer.nextToken();
/*      */     } 
/*      */     
/*  743 */     accept(Token.LPAREN);
/*      */     while (true) {
/*  745 */       primaryKey.addColumn(expr());
/*  746 */       if (this.lexer.token() != Token.COMMA) {
/*      */         break;
/*      */       }
/*  749 */       this.lexer.nextToken();
/*      */     } 
/*      */     
/*  752 */     accept(Token.RPAREN);
/*      */     
/*  754 */     return primaryKey;
/*      */   }
/*      */   
/*      */   public MySqlUnique parseUnique() {
/*  758 */     accept(Token.UNIQUE);
/*      */     
/*  760 */     if (this.lexer.token() == Token.KEY) {
/*  761 */       this.lexer.nextToken();
/*      */     }
/*      */     
/*  764 */     if (this.lexer.token() == Token.INDEX) {
/*  765 */       this.lexer.nextToken();
/*      */     }
/*      */     
/*  768 */     MySqlUnique unique = new MySqlUnique();
/*      */     
/*  770 */     if (this.lexer.token() != Token.LPAREN) {
/*  771 */       SQLName indexName = name();
/*  772 */       unique.setIndexName(indexName);
/*      */     } 
/*      */ 
/*      */     
/*  776 */     if (identifierEquals("USING")) {
/*  777 */       this.lexer.nextToken();
/*  778 */       unique.setIndexType(this.lexer.stringVal());
/*  779 */       this.lexer.nextToken();
/*      */     } 
/*      */     
/*  782 */     accept(Token.LPAREN); while (true) {
/*      */       MySqlOrderingExpr mySqlOrderingExpr;
/*  784 */       SQLExpr column = expr();
/*  785 */       if (this.lexer.token() == Token.ASC) {
/*  786 */         mySqlOrderingExpr = new MySqlOrderingExpr(column, SQLOrderingSpecification.ASC);
/*  787 */         this.lexer.nextToken();
/*  788 */       } else if (this.lexer.token() == Token.DESC) {
/*  789 */         mySqlOrderingExpr = new MySqlOrderingExpr((SQLExpr)mySqlOrderingExpr, SQLOrderingSpecification.DESC);
/*  790 */         this.lexer.nextToken();
/*      */       } 
/*  792 */       unique.addColumn((SQLExpr)mySqlOrderingExpr);
/*  793 */       if (this.lexer.token() != Token.COMMA) {
/*      */         break;
/*      */       }
/*  796 */       this.lexer.nextToken();
/*      */     } 
/*      */     
/*  799 */     accept(Token.RPAREN);
/*      */     
/*  801 */     if (identifierEquals("USING")) {
/*  802 */       this.lexer.nextToken();
/*  803 */       unique.setIndexType(this.lexer.stringVal());
/*  804 */       this.lexer.nextToken();
/*      */     } 
/*      */     
/*  807 */     return unique;
/*      */   }
/*      */   
/*      */   public MysqlForeignKey parseForeignKey() {
/*  811 */     accept(Token.FOREIGN);
/*  812 */     accept(Token.KEY);
/*      */     
/*  814 */     MysqlForeignKey fk = new MysqlForeignKey();
/*      */     
/*  816 */     if (this.lexer.token() != Token.LPAREN) {
/*  817 */       SQLName indexName = name();
/*  818 */       fk.setIndexName(indexName);
/*      */     } 
/*      */     
/*  821 */     accept(Token.LPAREN);
/*  822 */     names(fk.getReferencingColumns());
/*  823 */     accept(Token.RPAREN);
/*      */     
/*  825 */     accept(Token.REFERENCES);
/*      */     
/*  827 */     fk.setReferencedTableName(name());
/*      */     
/*  829 */     accept(Token.LPAREN);
/*  830 */     names(fk.getReferencedColumns());
/*  831 */     accept(Token.RPAREN);
/*      */     
/*  833 */     if (identifierEquals("MATCH")) {
/*  834 */       if (identifierEquals("FULL")) {
/*  835 */         fk.setReferenceMatch(MysqlForeignKey.Match.FULL);
/*  836 */       } else if (identifierEquals("PARTIAL")) {
/*  837 */         fk.setReferenceMatch(MysqlForeignKey.Match.PARTIAL);
/*  838 */       } else if (identifierEquals("SIMPLE")) {
/*  839 */         fk.setReferenceMatch(MysqlForeignKey.Match.SIMPLE);
/*      */       } 
/*      */     }
/*      */     
/*  843 */     while (this.lexer.token() == Token.ON) {
/*  844 */       this.lexer.nextToken();
/*      */       
/*  846 */       if (this.lexer.token() == Token.DELETE) {
/*  847 */         this.lexer.nextToken();
/*      */         
/*  849 */         MysqlForeignKey.Option option = parseReferenceOption();
/*  850 */         fk.setOnDelete(option); continue;
/*  851 */       }  if (this.lexer.token() == Token.UPDATE) {
/*  852 */         this.lexer.nextToken();
/*      */         
/*  854 */         MysqlForeignKey.Option option = parseReferenceOption();
/*  855 */         fk.setOnUpdate(option); continue;
/*      */       } 
/*  857 */       throw new ParserException("syntax error, expect DELETE or UPDATE, actual " + this.lexer.token() + " " + this.lexer
/*  858 */           .stringVal());
/*      */     } 
/*      */     
/*  861 */     return fk;
/*      */   }
/*      */   
/*      */   protected MysqlForeignKey.Option parseReferenceOption() {
/*      */     MysqlForeignKey.Option option;
/*  866 */     if (this.lexer.token() == Token.RESTRICT || identifierEquals("RESTRICT")) {
/*  867 */       option = MysqlForeignKey.Option.RESTRICT;
/*  868 */       this.lexer.nextToken();
/*  869 */     } else if (identifierEquals("CASCADE")) {
/*  870 */       option = MysqlForeignKey.Option.CASCADE;
/*  871 */       this.lexer.nextToken();
/*  872 */     } else if (this.lexer.token() == Token.SET) {
/*  873 */       this.lexer.nextToken();
/*  874 */       accept(Token.NULL);
/*  875 */       option = MysqlForeignKey.Option.SET_NULL;
/*  876 */     } else if (identifierEquals("ON")) {
/*  877 */       this.lexer.nextToken();
/*  878 */       if (identifierEquals("ACTION")) {
/*  879 */         option = MysqlForeignKey.Option.NO_ACTION;
/*  880 */         this.lexer.nextToken();
/*      */       } else {
/*  882 */         throw new ParserException("syntax error, expect ACTION, actual " + this.lexer.token() + " " + this.lexer
/*  883 */             .stringVal());
/*      */       } 
/*      */     } else {
/*  886 */       throw new ParserException("syntax error, expect ACTION, actual " + this.lexer.token() + " " + this.lexer
/*  887 */           .stringVal());
/*      */     } 
/*      */     
/*  890 */     return option;
/*      */   }
/*      */   
/*      */   protected SQLAggregateExpr parseAggregateExprRest(SQLAggregateExpr aggregateExpr) {
/*  894 */     if (this.lexer.token() == Token.ORDER) {
/*  895 */       SQLOrderBy orderBy = parseOrderBy();
/*  896 */       aggregateExpr.putAttribute("ORDER BY", orderBy);
/*      */     } 
/*  898 */     if (identifierEquals("SEPARATOR")) {
/*  899 */       this.lexer.nextToken();
/*      */       
/*  901 */       SQLExpr seperator = primary();
/*      */       
/*  903 */       aggregateExpr.putAttribute("SEPARATOR", seperator);
/*      */     } 
/*  905 */     return aggregateExpr;
/*      */   }
/*      */   
/*      */   public MySqlOrderingExpr parseSelectGroupByItem() {
/*  909 */     MySqlOrderingExpr item = new MySqlOrderingExpr();
/*      */     
/*  911 */     item.setExpr(expr());
/*      */     
/*  913 */     if (this.lexer.token() == Token.ASC) {
/*  914 */       this.lexer.nextToken();
/*  915 */       item.setType(SQLOrderingSpecification.ASC);
/*  916 */     } else if (this.lexer.token() == Token.DESC) {
/*  917 */       this.lexer.nextToken();
/*  918 */       item.setType(SQLOrderingSpecification.DESC);
/*      */     } 
/*      */     
/*  921 */     return item;
/*      */   }
/*      */   
/*      */   public SQLPartition parsePartition() {
/*  925 */     accept(Token.PARTITION);
/*      */     
/*  927 */     SQLPartition partitionDef = new SQLPartition();
/*      */     
/*  929 */     partitionDef.setName(name());
/*      */     
/*  931 */     SQLPartitionValue values = parsePartitionValues();
/*  932 */     if (values != null) {
/*  933 */       partitionDef.setValues(values);
/*      */     }
/*      */     
/*      */     while (true) {
/*  937 */       boolean storage = false;
/*  938 */       if (identifierEquals("DATA")) {
/*  939 */         this.lexer.nextToken();
/*  940 */         acceptIdentifier("DIRECTORY");
/*  941 */         if (this.lexer.token() == Token.EQ) {
/*  942 */           this.lexer.nextToken();
/*      */         }
/*  944 */         partitionDef.setDataDirectory(expr()); continue;
/*  945 */       }  if (this.lexer.token() == Token.TABLESPACE) {
/*  946 */         this.lexer.nextToken();
/*  947 */         if (this.lexer.token() == Token.EQ) {
/*  948 */           this.lexer.nextToken();
/*      */         }
/*  950 */         SQLName tableSpace = name();
/*  951 */         partitionDef.setTableSpace(tableSpace); continue;
/*  952 */       }  if (this.lexer.token() == Token.INDEX) {
/*  953 */         this.lexer.nextToken();
/*  954 */         acceptIdentifier("DIRECTORY");
/*  955 */         if (this.lexer.token() == Token.EQ) {
/*  956 */           this.lexer.nextToken();
/*      */         }
/*  958 */         partitionDef.setIndexDirectory(expr()); continue;
/*  959 */       }  if (identifierEquals("MAX_ROWS")) {
/*  960 */         this.lexer.nextToken();
/*  961 */         if (this.lexer.token() == Token.EQ) {
/*  962 */           this.lexer.nextToken();
/*      */         }
/*  964 */         SQLExpr maxRows = primary();
/*  965 */         partitionDef.setMaxRows(maxRows); continue;
/*  966 */       }  if (identifierEquals("MIN_ROWS")) {
/*  967 */         this.lexer.nextToken();
/*  968 */         if (this.lexer.token() == Token.EQ) {
/*  969 */           this.lexer.nextToken();
/*      */         }
/*  971 */         SQLExpr minRows = primary();
/*  972 */         partitionDef.setMaxRows(minRows); continue;
/*  973 */       }  if (!identifierEquals("ENGINE"))
/*  974 */         if (!(storage = (this.lexer.token() == Token.STORAGE || identifierEquals("STORAGE")))) {
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
/*  986 */           if (this.lexer.token() == Token.COMMENT) {
/*  987 */             this.lexer.nextToken();
/*  988 */             if (this.lexer.token() == Token.EQ) {
/*  989 */               this.lexer.nextToken();
/*      */             }
/*  991 */             SQLExpr comment = primary();
/*  992 */             partitionDef.setComment(comment); continue;
/*      */           }  break;
/*      */         }   if (storage)
/*      */         this.lexer.nextToken();  acceptIdentifier("ENGINE"); if (this.lexer.token() == Token.EQ)
/*      */         this.lexer.nextToken();  SQLName engine = name(); partitionDef.setEngine((SQLExpr)engine);
/*      */     } 
/*  998 */     if (this.lexer.token() == Token.LPAREN) {
/*  999 */       this.lexer.nextToken();
/*      */       
/*      */       while (true) {
/* 1002 */         acceptIdentifier("SUBPARTITION");
/*      */         
/* 1004 */         SQLName subPartitionName = name();
/* 1005 */         SQLSubPartition subPartition = new SQLSubPartition();
/* 1006 */         subPartition.setName(subPartitionName);
/*      */         
/* 1008 */         partitionDef.addSubPartition(subPartition);
/*      */         
/* 1010 */         if (this.lexer.token() == Token.COMMA) {
/* 1011 */           this.lexer.nextToken();
/*      */           
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/* 1017 */       accept(Token.RPAREN);
/*      */     } 
/* 1019 */     return partitionDef;
/*      */   }
/*      */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\parser\MySqlExprParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */