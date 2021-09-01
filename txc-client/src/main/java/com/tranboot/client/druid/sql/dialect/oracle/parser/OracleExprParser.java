/*      */ package com.tranboot.client.druid.sql.dialect.oracle.parser;
/*      */ 
/*      */

import com.tranboot.client.druid.sql.ast.*;
import com.tranboot.client.druid.sql.ast.expr.*;
import com.tranboot.client.druid.sql.ast.statement.*;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleDataTypeIntervalDay;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleDataTypeIntervalYear;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleDataTypeTimestamp;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.OracleLobStorageClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.OracleStorageClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.*;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.*;
import com.tranboot.client.druid.sql.parser.Lexer;
import com.tranboot.client.druid.sql.parser.ParserException;
import com.tranboot.client.druid.sql.parser.SQLExprParser;
import com.tranboot.client.druid.sql.parser.Token;

import java.math.BigInteger;

/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */

/*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class OracleExprParser
/*      */   extends SQLExprParser
/*      */ {
/*      */   public boolean allowStringAdditive = false;
/*   92 */   public static final String[] AGGREGATE_FUNCTIONS = new String[] { "AVG", "CORR", "COVAR_POP", "COVAR_SAMP", "COUNT", "CUME_DIST", "DENSE_RANK", "FIRST", "FIRST_VALUE", "LAG", "LAST", "LAST_VALUE", "LISTAGG", "LEAD", "MAX", "MIN", "NTILE", "PERCENT_RANK", "PERCENTILE_CONT", "PERCENTILE_DISC", "RANK", "RATIO_TO_REPORT", "REGR_SLOPE", "REGR_INTERCEPT", "REGR_COUNT", "REGR_R2", "REGR_AVGX", "REGR_AVGY", "REGR_SXX", "REGR_SYY", "REGR_SXY", "ROW_NUMBER", "STDDEV", "STDDEV_POP", "STDDEV_SAMP", "SUM", "VAR_POP", "VAR_SAMP", "VARIANCE", "WM_CONCAT" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public OracleExprParser(Lexer lexer) {
/*  136 */     super(lexer);
/*  137 */     this.aggregateFunctions = AGGREGATE_FUNCTIONS;
/*  138 */     this.dbType = "oracle";
/*      */   }
/*      */   
/*      */   public OracleExprParser(String text) {
/*  142 */     this(new OracleLexer(text));
/*  143 */     this.lexer.nextToken();
/*  144 */     this.dbType = "oracle";
/*      */   }
/*      */   
/*      */   protected boolean isCharType(String dataTypeName) {
/*  148 */     return ("varchar2".equalsIgnoreCase(dataTypeName) || "nvarchar2"
/*  149 */       .equalsIgnoreCase(dataTypeName) || "char"
/*  150 */       .equalsIgnoreCase(dataTypeName) || "varchar"
/*  151 */       .equalsIgnoreCase(dataTypeName) || "nchar"
/*  152 */       .equalsIgnoreCase(dataTypeName) || "nvarchar"
/*  153 */       .equalsIgnoreCase(dataTypeName));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public SQLDataType parseDataType() {
/*      */     String typeName;
/*  160 */     if (this.lexer.token() == Token.CONSTRAINT || this.lexer.token() == Token.COMMA) {
/*  161 */       return null;
/*      */     }
/*      */     
/*  164 */     if (this.lexer.token() == Token.DEFAULT || this.lexer.token() == Token.NOT || this.lexer.token() == Token.NULL) {
/*  165 */       return null;
/*      */     }
/*      */     
/*  168 */     if (this.lexer.token() == Token.INTERVAL) {
/*  169 */       this.lexer.nextToken();
/*  170 */       if (identifierEquals("YEAR")) {
/*  171 */         this.lexer.nextToken();
/*  172 */         OracleDataTypeIntervalYear oracleDataTypeIntervalYear = new OracleDataTypeIntervalYear();
/*      */         
/*  174 */         if (this.lexer.token() == Token.LPAREN) {
/*  175 */           this.lexer.nextToken();
/*  176 */           oracleDataTypeIntervalYear.addArgument(expr());
/*  177 */           accept(Token.RPAREN);
/*      */         } 
/*      */         
/*  180 */         accept(Token.TO);
/*  181 */         acceptIdentifier("MONTH");
/*      */         
/*  183 */         return (SQLDataType)oracleDataTypeIntervalYear;
/*      */       } 
/*  185 */       acceptIdentifier("DAY");
/*  186 */       OracleDataTypeIntervalDay interval = new OracleDataTypeIntervalDay();
/*  187 */       if (this.lexer.token() == Token.LPAREN) {
/*  188 */         this.lexer.nextToken();
/*  189 */         interval.addArgument(expr());
/*  190 */         accept(Token.RPAREN);
/*      */       } 
/*      */       
/*  193 */       accept(Token.TO);
/*  194 */       acceptIdentifier("SECOND");
/*      */       
/*  196 */       if (this.lexer.token() == Token.LPAREN) {
/*  197 */         this.lexer.nextToken();
/*  198 */         interval.getFractionalSeconds().add(expr());
/*  199 */         accept(Token.RPAREN);
/*      */       } 
/*      */       
/*  202 */       return (SQLDataType)interval;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  207 */     if (identifierEquals("LONG")) {
/*  208 */       this.lexer.nextToken();
/*  209 */       acceptIdentifier("RAW");
/*  210 */       typeName = "LONG RAW";
/*      */     } else {
/*  212 */       SQLName typeExpr = name();
/*  213 */       typeName = typeExpr.toString();
/*      */     } 
/*      */     
/*  216 */     if ("TIMESTAMP".equalsIgnoreCase(typeName)) {
/*  217 */       OracleDataTypeTimestamp timestamp = new OracleDataTypeTimestamp();
/*      */       
/*  219 */       if (this.lexer.token() == Token.LPAREN) {
/*  220 */         this.lexer.nextToken();
/*  221 */         timestamp.addArgument(expr());
/*  222 */         accept(Token.RPAREN);
/*      */       } 
/*      */       
/*  225 */       if (this.lexer.token() == Token.WITH) {
/*  226 */         this.lexer.nextToken();
/*      */         
/*  228 */         if (identifierEquals("LOCAL")) {
/*  229 */           this.lexer.nextToken();
/*  230 */           timestamp.setWithLocalTimeZone(true);
/*      */         } else {
/*  232 */           timestamp.setWithTimeZone(true);
/*      */         } 
/*      */         
/*  235 */         acceptIdentifier("TIME");
/*  236 */         acceptIdentifier("ZONE");
/*      */       } 
/*      */       
/*  239 */       return (SQLDataType)timestamp;
/*      */     } 
/*      */     
/*  242 */     if (isCharType(typeName)) {
/*  243 */       SQLCharacterDataType charType = new SQLCharacterDataType(typeName);
/*      */       
/*  245 */       if (this.lexer.token() == Token.LPAREN) {
/*  246 */         this.lexer.nextToken();
/*      */         
/*  248 */         charType.addArgument(expr());
/*      */         
/*  250 */         if (identifierEquals("CHAR")) {
/*  251 */           this.lexer.nextToken();
/*  252 */           charType.setCharType("CHAR");
/*  253 */         } else if (identifierEquals("BYTE")) {
/*  254 */           this.lexer.nextToken();
/*  255 */           charType.setCharType("BYTE");
/*      */         } 
/*      */         
/*  258 */         accept(Token.RPAREN);
/*      */       } 
/*      */       
/*  261 */       return parseCharTypeRest(charType);
/*      */     } 
/*      */     
/*  264 */     if (this.lexer.token() == Token.PERCENT) {
/*  265 */       this.lexer.nextToken();
/*  266 */       if (identifierEquals("TYPE")) {
/*  267 */         this.lexer.nextToken();
/*  268 */         typeName = typeName + "%TYPE";
/*  269 */       } else if (identifierEquals("ROWTYPE")) {
/*  270 */         this.lexer.nextToken();
/*  271 */         typeName = typeName + "%ROWTYPE";
/*      */       } else {
/*  273 */         throw new ParserException("syntax error : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  279 */     SQLDataTypeImpl sQLDataTypeImpl = new SQLDataTypeImpl(typeName);
/*  280 */     return parseDataTypeRest((SQLDataType)sQLDataTypeImpl); } public SQLExpr primary() { OracleSysdateExpr oracleSysdateExpr1; SQLExpr sQLExpr4; SQLUnaryExpr sQLUnaryExpr4; SQLIntegerExpr sQLIntegerExpr2; SQLNumberExpr sQLNumberExpr2; OracleBinaryFloatExpr oracleBinaryFloatExpr2; OracleBinaryDoubleExpr oracleBinaryDoubleExpr2; SQLExpr sQLExpr3; SQLUnaryExpr sQLUnaryExpr3; SQLIntegerExpr sQLIntegerExpr1; SQLNumberExpr sQLNumberExpr1; OracleBinaryFloatExpr oracleBinaryFloatExpr1; OracleBinaryDoubleExpr oracleBinaryDoubleExpr1; SQLExpr sQLExpr2; SQLUnaryExpr sQLUnaryExpr2; SQLExpr sQLExpr1; SQLUnaryExpr sQLUnaryExpr1; OracleCursorExpr oracleCursorExpr1; SQLIdentifierExpr sQLIdentifierExpr; OracleSysdateExpr sysdate; String alias; OracleBinaryFloatExpr floatExpr; OracleBinaryDoubleExpr doubleExpr;
/*      */     Number integerValue;
/*      */     OracleSelect select;
/*      */     OracleCursorExpr cursorExpr;
/*  284 */     Token tok = this.lexer.token();
/*      */     
/*  286 */     SQLExpr sqlExpr = null;
/*  287 */     switch (tok) {
/*      */       case SYSDATE:
/*  289 */         this.lexer.nextToken();
/*  290 */         sysdate = new OracleSysdateExpr();
/*  291 */         if (this.lexer.token() == Token.MONKEYS_AT) {
/*  292 */           this.lexer.nextToken();
/*  293 */           accept(Token.BANG);
/*  294 */           sysdate.setOption("!");
/*      */         } 
/*  296 */         oracleSysdateExpr1 = sysdate;
/*  297 */         return primaryRest((SQLExpr)oracleSysdateExpr1);
/*      */       case PRIOR:
/*  299 */         this.lexer.nextToken();
/*  300 */         sQLExpr4 = expr();
/*  301 */         sQLUnaryExpr4 = new SQLUnaryExpr(SQLUnaryOperator.Prior, sQLExpr4);
/*  302 */         return primaryRest((SQLExpr)sQLUnaryExpr4);
/*      */       case COLON:
/*  304 */         this.lexer.nextToken();
/*  305 */         if (this.lexer.token() == Token.LITERAL_INT) {
/*  306 */           String name = ":" + this.lexer.numberString();
/*  307 */           this.lexer.nextToken();
/*  308 */           return (SQLExpr)new SQLVariantRefExpr(name);
/*  309 */         }  if (this.lexer.token() == Token.IDENTIFIER) {
/*  310 */           String name = this.lexer.stringVal();
/*  311 */           if (name.charAt(0) == 'B' || name.charAt(0) == 'b') {
/*  312 */             this.lexer.nextToken();
/*  313 */             return (SQLExpr)new SQLVariantRefExpr(":" + name);
/*      */           } 
/*  315 */           throw new ParserException("syntax error : " + this.lexer.token() + " " + this.lexer.stringVal());
/*      */         } 
/*  317 */         throw new ParserException("syntax error : " + this.lexer.token());
/*      */       
/*      */       case LITERAL_ALIAS:
/*  320 */         alias = '"' + this.lexer.stringVal() + '"';
/*  321 */         this.lexer.nextToken();
/*  322 */         return primaryRest((SQLExpr)new SQLIdentifierExpr(alias));
/*      */       case BINARY_FLOAT:
/*  324 */         floatExpr = new OracleBinaryFloatExpr();
/*  325 */         floatExpr.setValue(Float.valueOf(Float.parseFloat(this.lexer.numberString())));
/*  326 */         this.lexer.nextToken();
/*  327 */         return primaryRest((SQLExpr)floatExpr);
/*      */       case BINARY_DOUBLE:
/*  329 */         doubleExpr = new OracleBinaryDoubleExpr();
/*  330 */         doubleExpr.setValue(Double.valueOf(Double.parseDouble(this.lexer.numberString())));
/*      */         
/*  332 */         this.lexer.nextToken();
/*  333 */         return primaryRest((SQLExpr)doubleExpr);
/*      */       case TABLE:
/*  335 */         this.lexer.nextToken();
/*  336 */         return primaryRest((SQLExpr)new SQLIdentifierExpr("TABLE"));
/*      */       case PLUS:
/*  338 */         this.lexer.nextToken();
/*  339 */         switch (this.lexer.token())
/*      */         { case LITERAL_INT:
/*  341 */             sQLIntegerExpr2 = new SQLIntegerExpr(this.lexer.integerValue());
/*  342 */             this.lexer.nextToken();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  365 */             return primaryRest((SQLExpr)sQLIntegerExpr2);case LITERAL_FLOAT: sQLNumberExpr2 = new SQLNumberExpr(this.lexer.decimalValue()); this.lexer.nextToken(); return primaryRest((SQLExpr)sQLNumberExpr2);case BINARY_FLOAT: oracleBinaryFloatExpr2 = new OracleBinaryFloatExpr(Float.valueOf(Float.parseFloat(this.lexer.numberString()))); this.lexer.nextToken(); return primaryRest((SQLExpr)oracleBinaryFloatExpr2);case BINARY_DOUBLE: oracleBinaryDoubleExpr2 = new OracleBinaryDoubleExpr(Double.valueOf(Double.parseDouble(this.lexer.numberString()))); this.lexer.nextToken(); return primaryRest((SQLExpr)oracleBinaryDoubleExpr2);case LPAREN: this.lexer.nextToken(); sQLExpr3 = expr(); accept(Token.RPAREN); sQLUnaryExpr3 = new SQLUnaryExpr(SQLUnaryOperator.Plus, sQLExpr3); return primaryRest((SQLExpr)sQLUnaryExpr3); }  throw new ParserException("TODO");
/*      */       case SUB:
/*  367 */         this.lexer.nextToken();
/*  368 */         switch (this.lexer.token()) {
/*      */           case LITERAL_INT:
/*  370 */             integerValue = this.lexer.integerValue();
/*  371 */             if (integerValue instanceof Integer) {
/*  372 */               int intVal = ((Integer)integerValue).intValue();
/*  373 */               if (intVal == Integer.MIN_VALUE) {
/*  374 */                 integerValue = Long.valueOf(intVal * -1L);
/*      */               } else {
/*  376 */                 integerValue = Integer.valueOf(intVal * -1);
/*      */               } 
/*  378 */             } else if (integerValue instanceof Long) {
/*  379 */               long longVal = ((Long)integerValue).longValue();
/*  380 */               if (longVal == 2147483648L) {
/*  381 */                 integerValue = Integer.valueOf((int)(longVal * -1L));
/*      */               } else {
/*  383 */                 integerValue = Long.valueOf(longVal * -1L);
/*      */               } 
/*      */             } else {
/*  386 */               integerValue = ((BigInteger)integerValue).negate();
/*      */             } 
/*  388 */             sQLIntegerExpr1 = new SQLIntegerExpr(integerValue);
/*  389 */             this.lexer.nextToken();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  417 */             return primaryRest((SQLExpr)sQLIntegerExpr1);case LITERAL_FLOAT: sQLNumberExpr1 = new SQLNumberExpr(this.lexer.decimalValue().negate()); this.lexer.nextToken(); return primaryRest((SQLExpr)sQLNumberExpr1);case BINARY_FLOAT: oracleBinaryFloatExpr1 = new OracleBinaryFloatExpr(Float.valueOf(Float.parseFloat(this.lexer.numberString()) * -1.0F)); this.lexer.nextToken(); return primaryRest((SQLExpr)oracleBinaryFloatExpr1);case BINARY_DOUBLE: oracleBinaryDoubleExpr1 = new OracleBinaryDoubleExpr(Double.valueOf(Double.parseDouble(this.lexer.numberString()) * -1.0D)); this.lexer.nextToken(); return primaryRest((SQLExpr)oracleBinaryDoubleExpr1);case VARIANT: case IDENTIFIER: sQLExpr2 = expr(); sQLUnaryExpr2 = new SQLUnaryExpr(SQLUnaryOperator.Negative, sQLExpr2); return primaryRest((SQLExpr)sQLUnaryExpr2);case LPAREN: this.lexer.nextToken(); sQLExpr1 = expr(); accept(Token.RPAREN); sQLUnaryExpr1 = new SQLUnaryExpr(SQLUnaryOperator.Negative, sQLExpr1); return primaryRest((SQLExpr)sQLUnaryExpr1);
/*      */         }  throw new ParserException("TODO " + this.lexer.token());
/*      */       case CURSOR:
/*  420 */         this.lexer.nextToken();
/*  421 */         accept(Token.LPAREN);
/*      */         
/*  423 */         select = createSelectParser().select();
/*  424 */         cursorExpr = new OracleCursorExpr((SQLSelect)select);
/*      */         
/*  426 */         accept(Token.RPAREN);
/*      */         
/*  428 */         oracleCursorExpr1 = cursorExpr;
/*  429 */         return primaryRest((SQLExpr)oracleCursorExpr1);
/*      */       case MODEL:
/*      */       case PCTFREE:
/*      */       case INITRANS:
/*      */       case MAXTRANS:
/*      */       case SEGMENT:
/*      */       case CREATION:
/*      */       case IMMEDIATE:
/*      */       case DEFERRED:
/*      */       case STORAGE:
/*      */       case NEXT:
/*      */       case MINEXTENTS:
/*      */       case MAXEXTENTS:
/*      */       case MAXSIZE:
/*      */       case PCTINCREASE:
/*      */       case FLASH_CACHE:
/*      */       case CELL_FLASH_CACHE:
/*      */       case KEEP:
/*      */       case NONE:
/*      */       case LOB:
/*      */       case STORE:
/*      */       case ROW:
/*      */       case CHUNK:
/*      */       case CACHE:
/*      */       case NOCACHE:
/*      */       case LOGGING:
/*      */       case NOCOMPRESS:
/*      */       case KEEP_DUPLICATES:
/*      */       case EXCEPTIONS:
/*      */       case PURGE:
/*  459 */         sQLIdentifierExpr = new SQLIdentifierExpr(this.lexer.stringVal());
/*  460 */         this.lexer.nextToken();
/*  461 */         return primaryRest((SQLExpr)sQLIdentifierExpr);
/*      */     } 
/*  463 */     return super.primary(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SQLExpr methodRest(SQLExpr expr, boolean acceptLPAREN) {
/*  469 */     if (acceptLPAREN) {
/*  470 */       accept(Token.LPAREN);
/*      */     }
/*      */     
/*  473 */     if (this.lexer.token() == Token.PLUS) {
/*  474 */       this.lexer.nextToken();
/*  475 */       accept(Token.RPAREN);
/*  476 */       return (SQLExpr)new OracleOuterExpr(expr);
/*      */     } 
/*      */     
/*  479 */     if (expr instanceof SQLIdentifierExpr) {
/*  480 */       String methodName = ((SQLIdentifierExpr)expr).getName();
/*  481 */       SQLMethodInvokeExpr methodExpr = new SQLMethodInvokeExpr(methodName);
/*  482 */       if ("trim".equalsIgnoreCase(methodName)) {
/*  483 */         if (identifierEquals("LEADING") || 
/*  484 */           identifierEquals("TRAILING") || 
/*  485 */           identifierEquals("BOTH")) {
/*      */           
/*  487 */           methodExpr.putAttribute("trim_option", this.lexer.stringVal());
/*  488 */           this.lexer.nextToken();
/*      */         } 
/*      */         
/*  491 */         if (this.lexer.token() != Token.FROM) {
/*  492 */           SQLExpr trim_character = primary();
/*  493 */           trim_character.setParent((SQLObject)methodExpr);
/*  494 */           if (trim_character instanceof SQLCharExpr) {
/*  495 */             methodExpr.putAttribute("trim_character", trim_character);
/*      */           } else {
/*  497 */             methodExpr.getParameters().add(trim_character);
/*      */           } 
/*      */         } 
/*      */         
/*  501 */         if (this.lexer.token() == Token.FROM) {
/*  502 */           this.lexer.nextToken();
/*  503 */           SQLExpr trim_source = expr();
/*  504 */           methodExpr.addParameter(trim_source);
/*      */         } 
/*      */         
/*  507 */         accept(Token.RPAREN);
/*  508 */         return primaryRest((SQLExpr)methodExpr);
/*      */       } 
/*      */     } 
/*      */     
/*  512 */     return super.methodRest(expr, false); } public SQLExpr primaryRest(SQLExpr expr) { OracleSizeExpr oracleSizeExpr;
/*      */     OracleDbLinkExpr oracleDbLinkExpr;
/*      */     OracleIntervalExpr oracleIntervalExpr;
/*      */     OracleDatetimeExpr oracleDatetimeExpr;
/*  516 */     if (expr.getClass() == SQLIdentifierExpr.class) {
/*  517 */       String ident = ((SQLIdentifierExpr)expr).getName();
/*  518 */       if ("TIMESTAMP".equalsIgnoreCase(ident)) {
/*  519 */         if (this.lexer.token() != Token.LITERAL_ALIAS && this.lexer.token() != Token.LITERAL_CHARS) {
/*  520 */           return (SQLExpr)new SQLIdentifierExpr("TIMESTAMP");
/*      */         }
/*      */         
/*  523 */         SQLTimestampExpr timestamp = new SQLTimestampExpr();
/*      */         
/*  525 */         String literal = this.lexer.stringVal();
/*  526 */         timestamp.setLiteral(literal);
/*  527 */         accept(Token.LITERAL_CHARS);
/*      */         
/*  529 */         if (identifierEquals("AT")) {
/*  530 */           this.lexer.nextToken();
/*  531 */           acceptIdentifier("TIME");
/*  532 */           acceptIdentifier("ZONE");
/*      */           
/*  534 */           String timezone = this.lexer.stringVal();
/*  535 */           timestamp.setTimeZone(timezone);
/*  536 */           accept(Token.LITERAL_CHARS);
/*      */         } 
/*      */ 
/*      */         
/*  540 */         return primaryRest((SQLExpr)timestamp);
/*      */       } 
/*      */     } 
/*  543 */     if (this.lexer.token() == Token.IDENTIFIER && expr instanceof com.tranboot.client.druid.sql.ast.expr.SQLNumericLiteralExpr) {
/*  544 */       String ident = this.lexer.stringVal();
/*      */       
/*  546 */       if (ident.length() == 1) {
/*  547 */         char unit = ident.charAt(0);
/*  548 */         switch (unit) {
/*      */           case 'E':
/*      */           case 'G':
/*      */           case 'K':
/*      */           case 'M':
/*      */           case 'P':
/*      */           case 'T':
/*      */           case 'e':
/*      */           case 'g':
/*      */           case 'k':
/*      */           case 'm':
/*      */           case 'p':
/*      */           case 't':
/*  561 */             oracleSizeExpr = new OracleSizeExpr(expr, OracleSizeExpr.Unit.valueOf(ident.toUpperCase()));
/*  562 */             this.lexer.nextToken();
/*      */             break;
/*      */         } 
/*      */ 
/*      */ 
/*      */       
/*      */       } 
/*      */     } 
/*  570 */     if (this.lexer.token() == Token.DOTDOT) {
/*  571 */       this.lexer.nextToken();
/*  572 */       SQLExpr upBound = expr();
/*      */       
/*  574 */       return (SQLExpr)new OracleRangeExpr((SQLExpr)oracleSizeExpr, upBound);
/*      */     } 
/*      */     
/*  577 */     if (this.lexer.token() == Token.MONKEYS_AT) {
/*  578 */       this.lexer.nextToken();
/*      */       
/*  580 */       OracleDbLinkExpr dblink = new OracleDbLinkExpr();
/*  581 */       dblink.setExpr((SQLExpr)oracleSizeExpr);
/*      */       
/*  583 */       if (this.lexer.token() == Token.BANG) {
/*  584 */         dblink.setDbLink("!");
/*  585 */         this.lexer.nextToken();
/*      */       } else {
/*  587 */         String link = this.lexer.stringVal();
/*  588 */         accept(Token.IDENTIFIER);
/*  589 */         dblink.setDbLink(link);
/*      */       } 
/*      */       
/*  592 */       oracleDbLinkExpr = dblink;
/*      */     } 
/*      */     
/*  595 */     if (identifierEquals("DAY") || identifierEquals("YEAR")) {
/*  596 */       this.lexer.mark();
/*      */       
/*  598 */       String name = this.lexer.stringVal();
/*  599 */       this.lexer.nextToken();
/*      */       
/*  601 */       if (this.lexer.token() == Token.COMMA) {
/*  602 */         this.lexer.reset();
/*  603 */         return (SQLExpr)oracleDbLinkExpr;
/*      */       } 
/*      */       
/*  606 */       OracleIntervalExpr interval = new OracleIntervalExpr();
/*  607 */       interval.setValue((SQLExpr)oracleDbLinkExpr);
/*  608 */       OracleIntervalType type = OracleIntervalType.valueOf(name);
/*  609 */       interval.setType(type);
/*      */       
/*  611 */       if (this.lexer.token() == Token.LPAREN) {
/*  612 */         this.lexer.nextToken();
/*  613 */         if (this.lexer.token() != Token.LITERAL_INT) {
/*  614 */           throw new ParserException("syntax error");
/*      */         }
/*  616 */         interval.setPrecision(Integer.valueOf(this.lexer.integerValue().intValue()));
/*  617 */         this.lexer.nextToken();
/*  618 */         accept(Token.RPAREN);
/*      */       } 
/*      */       
/*  621 */       accept(Token.TO);
/*  622 */       if (identifierEquals("SECOND")) {
/*  623 */         this.lexer.nextToken();
/*  624 */         interval.setToType(OracleIntervalType.SECOND);
/*  625 */         if (this.lexer.token() == Token.LPAREN) {
/*  626 */           this.lexer.nextToken();
/*  627 */           if (this.lexer.token() != Token.LITERAL_INT) {
/*  628 */             throw new ParserException("syntax error");
/*      */           }
/*  630 */           interval.setFactionalSecondsPrecision(Integer.valueOf(this.lexer.integerValue().intValue()));
/*  631 */           this.lexer.nextToken();
/*  632 */           accept(Token.RPAREN);
/*      */         } 
/*      */       } else {
/*  635 */         interval.setToType(OracleIntervalType.MONTH);
/*  636 */         this.lexer.nextToken();
/*      */       } 
/*      */       
/*  639 */       oracleIntervalExpr = interval;
/*      */     } 
/*      */     
/*  642 */     if (identifierEquals("AT")) {
/*  643 */       char markChar = this.lexer.current();
/*  644 */       int markBp = this.lexer.bp();
/*  645 */       this.lexer.nextToken();
/*  646 */       if (identifierEquals("LOCAL")) {
/*  647 */         this.lexer.nextToken();
/*  648 */         oracleDatetimeExpr = new OracleDatetimeExpr((SQLExpr)oracleIntervalExpr, (SQLExpr)new SQLIdentifierExpr("LOCAL"));
/*      */       } else {
/*  650 */         if (identifierEquals("TIME")) {
/*  651 */           this.lexer.nextToken();
/*      */         } else {
/*  653 */           this.lexer.reset(markBp, markChar, Token.IDENTIFIER);
/*  654 */           return (SQLExpr)oracleDatetimeExpr;
/*      */         } 
/*  656 */         acceptIdentifier("ZONE");
/*      */         
/*  658 */         SQLExpr timeZone = primary();
/*  659 */         oracleDatetimeExpr = new OracleDatetimeExpr((SQLExpr)oracleDatetimeExpr, timeZone);
/*      */       } 
/*      */     } 
/*      */     
/*  663 */     SQLExpr restExpr = super.primaryRest((SQLExpr)oracleDatetimeExpr);
/*      */     
/*  665 */     if (restExpr != oracleDatetimeExpr && restExpr instanceof SQLMethodInvokeExpr) {
/*  666 */       SQLMethodInvokeExpr methodInvoke = (SQLMethodInvokeExpr)restExpr;
/*  667 */       if (methodInvoke.getParameters().size() == 1) {
/*  668 */         SQLExpr paramExpr = methodInvoke.getParameters().get(0);
/*  669 */         if (paramExpr instanceof SQLIdentifierExpr && "+".equals(((SQLIdentifierExpr)paramExpr).getName())) {
/*  670 */           OracleOuterExpr outerExpr = new OracleOuterExpr();
/*  671 */           if (methodInvoke.getOwner() == null) {
/*  672 */             outerExpr.setExpr((SQLExpr)new SQLIdentifierExpr(methodInvoke.getMethodName()));
/*      */           } else {
/*  674 */             outerExpr.setExpr((SQLExpr)new SQLPropertyExpr(methodInvoke.getOwner(), methodInvoke.getMethodName()));
/*      */           } 
/*  676 */           return (SQLExpr)outerExpr;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  681 */     return restExpr; }
/*      */   
/*      */   protected SQLExpr dotRest(SQLExpr expr) {
/*      */     SQLExpr sQLExpr;
/*  685 */     if (this.lexer.token() == Token.LITERAL_ALIAS) {
/*  686 */       String name = '"' + this.lexer.stringVal() + '"';
/*  687 */       this.lexer.nextToken();
/*  688 */       SQLPropertyExpr sQLPropertyExpr = new SQLPropertyExpr(expr, name);
/*      */       
/*  690 */       if (this.lexer.token() == Token.DOT) {
/*  691 */         this.lexer.nextToken();
/*  692 */         sQLExpr = dotRest((SQLExpr)sQLPropertyExpr);
/*      */       } 
/*      */       
/*  695 */       return sQLExpr;
/*      */     } 
/*      */     
/*  698 */     if (identifierEquals("NEXTVAL")) {
/*  699 */       if (sQLExpr instanceof SQLIdentifierExpr) {
/*  700 */         SQLIdentifierExpr identExpr = (SQLIdentifierExpr)sQLExpr;
/*  701 */         SQLSequenceExpr seqExpr = new SQLSequenceExpr((SQLName)identExpr, SQLSequenceExpr.Function.NextVal);
/*  702 */         this.lexer.nextToken();
/*  703 */         return (SQLExpr)seqExpr;
/*      */       } 
/*  705 */     } else if (identifierEquals("CURRVAL") && 
/*  706 */       sQLExpr instanceof SQLIdentifierExpr) {
/*  707 */       SQLIdentifierExpr identExpr = (SQLIdentifierExpr)sQLExpr;
/*  708 */       SQLSequenceExpr seqExpr = new SQLSequenceExpr((SQLName)identExpr, SQLSequenceExpr.Function.CurrVal);
/*  709 */       this.lexer.nextToken();
/*  710 */       return (SQLExpr)seqExpr;
/*      */     } 
/*      */ 
/*      */     
/*  714 */     return super.dotRest(sQLExpr);
/*      */   }
/*      */   protected SQLAggregateExpr parseAggregateExpr(String methodName) {
/*      */     SQLAggregateExpr aggregateExpr;
/*  718 */     methodName = methodName.toUpperCase();
/*      */ 
/*      */     
/*  721 */     if (this.lexer.token() == Token.UNIQUE) {
/*  722 */       aggregateExpr = new SQLAggregateExpr(methodName, SQLAggregateOption.UNIQUE);
/*  723 */       this.lexer.nextToken();
/*  724 */     } else if (this.lexer.token() == Token.ALL) {
/*  725 */       aggregateExpr = new SQLAggregateExpr(methodName, SQLAggregateOption.ALL);
/*  726 */       this.lexer.nextToken();
/*  727 */     } else if (this.lexer.token() == Token.DISTINCT) {
/*  728 */       aggregateExpr = new SQLAggregateExpr(methodName, SQLAggregateOption.DISTINCT);
/*  729 */       this.lexer.nextToken();
/*      */     } else {
/*  731 */       aggregateExpr = new SQLAggregateExpr(methodName);
/*      */     } 
/*  733 */     exprList(aggregateExpr.getArguments(), (SQLObject)aggregateExpr);
/*      */     
/*  735 */     if (this.lexer.stringVal().equalsIgnoreCase("IGNORE")) {
/*  736 */       this.lexer.nextToken();
/*  737 */       identifierEquals("NULLS");
/*  738 */       aggregateExpr.setIgnoreNulls(true);
/*      */     } 
/*      */     
/*  741 */     accept(Token.RPAREN);
/*      */     
/*  743 */     if (identifierEquals("WITHIN")) {
/*  744 */       this.lexer.nextToken();
/*  745 */       accept(Token.GROUP);
/*  746 */       accept(Token.LPAREN);
/*  747 */       SQLOrderBy withinGroup = parseOrderBy();
/*  748 */       aggregateExpr.setWithinGroup(withinGroup);
/*  749 */       accept(Token.RPAREN);
/*      */     } 
/*      */     
/*  752 */     if (this.lexer.token() == Token.KEEP) {
/*  753 */       this.lexer.nextToken();
/*      */       
/*  755 */       SQLKeep keep = new SQLKeep();
/*  756 */       accept(Token.LPAREN);
/*  757 */       acceptIdentifier("DENSE_RANK");
/*  758 */       if (identifierEquals("FIRST")) {
/*  759 */         this.lexer.nextToken();
/*  760 */         keep.setDenseRank(SQLKeep.DenseRank.FIRST);
/*      */       } else {
/*  762 */         acceptIdentifier("LAST");
/*  763 */         keep.setDenseRank(SQLKeep.DenseRank.LAST);
/*      */       } 
/*      */       
/*  766 */       SQLOrderBy orderBy = parseOrderBy();
/*  767 */       keep.setOrderBy(orderBy);
/*      */       
/*  769 */       aggregateExpr.setKeep(keep);
/*      */       
/*  771 */       accept(Token.RPAREN);
/*      */     } 
/*      */     
/*  774 */     if (this.lexer.token() == Token.OVER) {
/*  775 */       OracleAnalytic over = new OracleAnalytic();
/*      */       
/*  777 */       this.lexer.nextToken();
/*  778 */       accept(Token.LPAREN);
/*      */       
/*  780 */       if (identifierEquals("PARTITION")) {
/*  781 */         this.lexer.nextToken();
/*  782 */         accept(Token.BY);
/*      */         
/*  784 */         if (this.lexer.token() == Token.LPAREN) {
/*  785 */           this.lexer.nextToken();
/*  786 */           exprList(over.getPartitionBy(), (SQLObject)over);
/*  787 */           accept(Token.RPAREN);
/*      */         } else {
/*  789 */           exprList(over.getPartitionBy(), (SQLObject)over);
/*      */         } 
/*      */       } 
/*      */       
/*  793 */       over.setOrderBy(parseOrderBy());
/*  794 */       if (over.getOrderBy() != null) {
/*  795 */         OracleAnalyticWindowing windowing = null;
/*  796 */         if (this.lexer.stringVal().equalsIgnoreCase("ROWS")) {
/*  797 */           this.lexer.nextToken();
/*  798 */           windowing = new OracleAnalyticWindowing();
/*  799 */           windowing.setType(OracleAnalyticWindowing.Type.ROWS);
/*  800 */         } else if (this.lexer.stringVal().equalsIgnoreCase("RANGE")) {
/*  801 */           this.lexer.nextToken();
/*  802 */           windowing = new OracleAnalyticWindowing();
/*  803 */           windowing.setType(OracleAnalyticWindowing.Type.RANGE);
/*      */         } 
/*      */         
/*  806 */         if (windowing != null) {
/*  807 */           if (this.lexer.stringVal().equalsIgnoreCase("CURRENT")) {
/*  808 */             this.lexer.nextToken();
/*  809 */             if (this.lexer.stringVal().equalsIgnoreCase("ROW")) {
/*  810 */               this.lexer.nextToken();
/*  811 */               windowing.setExpr((SQLExpr)new SQLIdentifierExpr("CURRENT ROW"));
/*  812 */               over.setWindowing(windowing);
/*      */             } 
/*  814 */             throw new ParserException("syntax error");
/*      */           } 
/*  816 */           if (this.lexer.stringVal().equalsIgnoreCase("UNBOUNDED")) {
/*  817 */             this.lexer.nextToken();
/*  818 */             if (this.lexer.stringVal().equalsIgnoreCase("PRECEDING")) {
/*  819 */               this.lexer.nextToken();
/*  820 */               windowing.setExpr((SQLExpr)new SQLIdentifierExpr("UNBOUNDED PRECEDING"));
/*      */             } else {
/*  822 */               throw new ParserException("syntax error");
/*      */             } 
/*      */           } 
/*      */           
/*  826 */           over.setWindowing(windowing);
/*      */         } 
/*      */       } 
/*      */       
/*  830 */       accept(Token.RPAREN);
/*      */       
/*  832 */       aggregateExpr.setOver((SQLOver)over);
/*      */     } 
/*  834 */     return aggregateExpr;
/*      */   }
/*      */ 
/*      */   
/*      */   private OracleIntervalType parseIntervalType() {
/*  839 */     String currentTokenUpperValue = this.lexer.stringVal();
/*  840 */     this.lexer.nextToken();
/*      */     
/*  842 */     if (currentTokenUpperValue.equals("YEAR")) {
/*  843 */       return OracleIntervalType.YEAR;
/*      */     }
/*  845 */     if (currentTokenUpperValue.equals("MONTH")) {
/*  846 */       return OracleIntervalType.MONTH;
/*      */     }
/*  848 */     if (currentTokenUpperValue.equals("HOUR")) {
/*  849 */       return OracleIntervalType.HOUR;
/*      */     }
/*  851 */     if (currentTokenUpperValue.equals("MINUTE")) {
/*  852 */       return OracleIntervalType.MINUTE;
/*      */     }
/*  854 */     if (currentTokenUpperValue.equals("SECOND")) {
/*  855 */       return OracleIntervalType.SECOND;
/*      */     }
/*  857 */     throw new ParserException("syntax error");
/*      */   }
/*      */ 
/*      */   
/*      */   public OracleSelectParser createSelectParser() {
/*  862 */     return new OracleSelectParser(this);
/*      */   }
/*      */   
/*      */   protected SQLExpr parseInterval() {
/*  866 */     accept(Token.INTERVAL);
/*      */     
/*  868 */     OracleIntervalExpr interval = new OracleIntervalExpr();
/*  869 */     if (this.lexer.token() != Token.LITERAL_CHARS) {
/*  870 */       return (SQLExpr)new SQLIdentifierExpr("INTERVAL");
/*      */     }
/*  872 */     interval.setValue((SQLExpr)new SQLCharExpr(this.lexer.stringVal()));
/*  873 */     this.lexer.nextToken();
/*      */ 
/*      */     
/*  876 */     OracleIntervalType type = OracleIntervalType.valueOf(this.lexer.stringVal());
/*  877 */     interval.setType(type);
/*  878 */     this.lexer.nextToken();
/*      */     
/*  880 */     if (this.lexer.token() == Token.LPAREN) {
/*  881 */       this.lexer.nextToken();
/*  882 */       if (this.lexer.token() != Token.LITERAL_INT) {
/*  883 */         throw new ParserException("syntax error");
/*      */       }
/*  885 */       interval.setPrecision(Integer.valueOf(this.lexer.integerValue().intValue()));
/*  886 */       this.lexer.nextToken();
/*      */       
/*  888 */       if (this.lexer.token() == Token.COMMA) {
/*  889 */         this.lexer.nextToken();
/*  890 */         if (this.lexer.token() != Token.LITERAL_INT) {
/*  891 */           throw new ParserException("syntax error");
/*      */         }
/*  893 */         interval.setFactionalSecondsPrecision(Integer.valueOf(this.lexer.integerValue().intValue()));
/*  894 */         this.lexer.nextToken();
/*      */       } 
/*  896 */       accept(Token.RPAREN);
/*      */     } 
/*      */     
/*  899 */     if (this.lexer.token() == Token.TO) {
/*  900 */       this.lexer.nextToken();
/*  901 */       if (identifierEquals("SECOND")) {
/*  902 */         this.lexer.nextToken();
/*  903 */         interval.setToType(OracleIntervalType.SECOND);
/*  904 */         if (this.lexer.token() == Token.LPAREN) {
/*  905 */           this.lexer.nextToken();
/*  906 */           if (this.lexer.token() != Token.LITERAL_INT) {
/*  907 */             throw new ParserException("syntax error");
/*      */           }
/*  909 */           interval.setToFactionalSecondsPrecision(Integer.valueOf(this.lexer.integerValue().intValue()));
/*  910 */           this.lexer.nextToken();
/*  911 */           accept(Token.RPAREN);
/*      */         } 
/*      */       } else {
/*  914 */         interval.setToType(OracleIntervalType.MONTH);
/*  915 */         this.lexer.nextToken();
/*      */       } 
/*      */     } 
/*      */     
/*  919 */     return (SQLExpr)interval;
/*      */   }
/*      */   public SQLExpr relationalRest(SQLExpr expr) {
/*      */     SQLBinaryOpExpr sQLBinaryOpExpr;
/*  923 */     if (this.lexer.token() == Token.IS) {
/*  924 */       this.lexer.nextToken();
/*      */       
/*  926 */       if (this.lexer.token() == Token.NOT)
/*  927 */       { this.lexer.nextToken();
/*  928 */         SQLExpr rightExpr = primary();
/*  929 */         sQLBinaryOpExpr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.IsNot, rightExpr, getDbType()); }
/*  930 */       else { OracleIsSetExpr oracleIsSetExpr; if (identifierEquals("A")) {
/*  931 */           this.lexer.nextToken();
/*  932 */           accept(Token.SET);
/*  933 */           oracleIsSetExpr = new OracleIsSetExpr((SQLExpr)sQLBinaryOpExpr);
/*      */         } else {
/*  935 */           SQLExpr rightExpr = primary();
/*  936 */           sQLBinaryOpExpr = new SQLBinaryOpExpr((SQLExpr)oracleIsSetExpr, SQLBinaryOperator.Is, rightExpr, getDbType());
/*      */         }  }
/*      */       
/*  939 */       return (SQLExpr)sQLBinaryOpExpr;
/*      */     } 
/*  941 */     return super.relationalRest((SQLExpr)sQLBinaryOpExpr);
/*      */   }
/*      */ 
/*      */   
/*      */   public SQLName name() {
/*  946 */     SQLName name = super.name();
/*      */     
/*  948 */     if (this.lexer.token() == Token.MONKEYS_AT) {
/*  949 */       this.lexer.nextToken();
/*  950 */       if (this.lexer.token() != Token.IDENTIFIER) {
/*  951 */         throw new ParserException("syntax error, expect identifier, but " + this.lexer.token());
/*      */       }
/*  953 */       OracleDbLinkExpr dbLink = new OracleDbLinkExpr();
/*  954 */       dbLink.setExpr((SQLExpr)name);
/*  955 */       dbLink.setDbLink(this.lexer.stringVal());
/*  956 */       this.lexer.nextToken();
/*  957 */       return (SQLName)dbLink;
/*      */     } 
/*      */     
/*  960 */     return name;
/*      */   }
/*      */   
/*      */   public SQLExpr equalityRest(SQLExpr expr) {
/*      */     SQLBinaryOpExpr sQLBinaryOpExpr;
/*  965 */     if (this.lexer.token() == Token.EQ) {
/*  966 */       this.lexer.nextToken();
/*      */       
/*  968 */       if (this.lexer.token() == Token.GT) {
/*  969 */         this.lexer.nextToken();
/*  970 */         SQLExpr sQLExpr = expr();
/*  971 */         String argumentName = ((SQLIdentifierExpr)expr).getName();
/*  972 */         return (SQLExpr)new OracleArgumentExpr(argumentName, sQLExpr);
/*      */       } 
/*      */       
/*  975 */       SQLExpr rightExp = shift();
/*      */       
/*  977 */       rightExp = equalityRest(rightExp);
/*      */       
/*  979 */       sQLBinaryOpExpr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Equality, rightExp, getDbType());
/*  980 */     } else if (this.lexer.token() == Token.BANGEQ) {
/*  981 */       this.lexer.nextToken();
/*  982 */       SQLExpr rightExp = shift();
/*      */       
/*  984 */       rightExp = equalityRest(rightExp);
/*      */       
/*  986 */       sQLBinaryOpExpr = new SQLBinaryOpExpr((SQLExpr)sQLBinaryOpExpr, SQLBinaryOperator.NotEqual, rightExp, getDbType());
/*      */     } 
/*      */     
/*  989 */     return (SQLExpr)sQLBinaryOpExpr;
/*      */   }
/*      */   
/*      */   public OraclePrimaryKey parsePrimaryKey() {
/*  993 */     this.lexer.nextToken();
/*  994 */     accept(Token.KEY);
/*      */     
/*  996 */     OraclePrimaryKey primaryKey = new OraclePrimaryKey();
/*  997 */     accept(Token.LPAREN);
/*  998 */     exprList(primaryKey.getColumns(), (SQLObject)primaryKey);
/*  999 */     accept(Token.RPAREN);
/*      */ 
/*      */     
/* 1002 */     if (this.lexer.token() == Token.USING) {
/* 1003 */       OracleUsingIndexClause using = parseUsingIndex();
/* 1004 */       primaryKey.setUsing(using);
/*      */     } 
/* 1006 */     return primaryKey;
/*      */   }
/*      */   
/*      */   private OracleUsingIndexClause parseUsingIndex() {
/* 1010 */     accept(Token.USING);
/* 1011 */     accept(Token.INDEX);
/*      */     
/* 1013 */     OracleUsingIndexClause using = new OracleUsingIndexClause();
/*      */     
/*      */     while (true) {
/* 1016 */       while (this.lexer.token() == Token.TABLESPACE) {
/* 1017 */         this.lexer.nextToken();
/* 1018 */         using.setTablespace(name());
/*      */       } 
/* 1020 */       if (this.lexer.token() == Token.PCTFREE) {
/* 1021 */         this.lexer.nextToken();
/* 1022 */         using.setPtcfree(expr()); continue;
/*      */       } 
/* 1024 */       if (this.lexer.token() == Token.INITRANS) {
/* 1025 */         this.lexer.nextToken();
/* 1026 */         using.setInitrans(expr()); continue;
/*      */       } 
/* 1028 */       if (this.lexer.token() == Token.MAXTRANS) {
/* 1029 */         this.lexer.nextToken();
/* 1030 */         using.setMaxtrans(expr()); continue;
/*      */       } 
/* 1032 */       if (this.lexer.token() == Token.COMPUTE) {
/* 1033 */         this.lexer.nextToken();
/* 1034 */         acceptIdentifier("STATISTICS");
/* 1035 */         using.setComputeStatistics(true); continue;
/*      */       } 
/* 1037 */       if (this.lexer.token() == Token.ENABLE) {
/* 1038 */         this.lexer.nextToken();
/* 1039 */         using.setEnable(Boolean.valueOf(true)); continue;
/*      */       } 
/* 1041 */       if (this.lexer.token() == Token.DISABLE) {
/* 1042 */         this.lexer.nextToken();
/* 1043 */         using.setEnable(Boolean.valueOf(false)); continue;
/*      */       } 
/* 1045 */       if (this.lexer.token() == Token.STORAGE) {
/* 1046 */         OracleStorageClause storage = parseStorage();
/* 1047 */         using.setStorage(storage); continue;
/*      */       }  break;
/* 1049 */     }  if (this.lexer.token() == Token.IDENTIFIER) {
/* 1050 */       using.setTablespace(name());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1056 */     return using;
/*      */   }
/*      */   
/*      */   public SQLColumnDefinition parseColumnRest(SQLColumnDefinition column) {
/* 1060 */     column = super.parseColumnRest(column);
/*      */     
/* 1062 */     if (this.lexer.token() == Token.ENABLE) {
/* 1063 */       this.lexer.nextToken();
/* 1064 */       column.setEnable(Boolean.TRUE);
/*      */     } 
/*      */     
/* 1067 */     return column;
/*      */   }
/*      */   public SQLExpr exprRest(SQLExpr expr) {
/*      */     SQLBinaryOpExpr sQLBinaryOpExpr;
/* 1071 */     expr = super.exprRest(expr);
/*      */     
/* 1073 */     if (this.lexer.token() == Token.COLONEQ) {
/* 1074 */       this.lexer.nextToken();
/* 1075 */       SQLExpr right = expr();
/* 1076 */       sQLBinaryOpExpr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Assignment, right, getDbType());
/*      */     } 
/*      */     
/* 1079 */     return (SQLExpr)sQLBinaryOpExpr;
/*      */   }
/*      */   
/*      */   public OracleLobStorageClause parseLobStorage() {
/* 1083 */     this.lexer.nextToken();
/*      */     
/* 1085 */     OracleLobStorageClause clause = new OracleLobStorageClause();
/*      */     
/* 1087 */     accept(Token.LPAREN);
/* 1088 */     names(clause.getItems());
/* 1089 */     accept(Token.RPAREN);
/*      */     
/* 1091 */     accept(Token.STORE);
/* 1092 */     accept(Token.AS);
/*      */     
/*      */     while (true) {
/* 1095 */       while (identifierEquals("SECUREFILE")) {
/* 1096 */         this.lexer.nextToken();
/* 1097 */         clause.setSecureFile(true);
/*      */       } 
/*      */ 
/*      */       
/* 1101 */       if (identifierEquals("BASICFILE")) {
/* 1102 */         this.lexer.nextToken();
/* 1103 */         clause.setBasicFile(true); continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 1107 */     if (this.lexer.token() == Token.LPAREN) {
/* 1108 */       this.lexer.nextToken();
/*      */       
/*      */       while (true) {
/* 1111 */         while (this.lexer.token() == Token.TABLESPACE) {
/* 1112 */           this.lexer.nextToken();
/* 1113 */           clause.setTableSpace(name());
/*      */         } 
/*      */ 
/*      */         
/* 1117 */         if (this.lexer.token() == Token.ENABLE) {
/* 1118 */           this.lexer.nextToken();
/* 1119 */           accept(Token.STORAGE);
/* 1120 */           accept(Token.IN);
/* 1121 */           accept(Token.ROW);
/* 1122 */           clause.setEnable(Boolean.valueOf(true));
/*      */           
/*      */           continue;
/*      */         } 
/* 1126 */         if (this.lexer.token() == Token.CHUNK) {
/* 1127 */           this.lexer.nextToken();
/* 1128 */           clause.setChunk(primary());
/*      */           
/*      */           continue;
/*      */         } 
/* 1132 */         if (this.lexer.token() == Token.NOCACHE) {
/* 1133 */           this.lexer.nextToken();
/* 1134 */           clause.setCache(Boolean.valueOf(false));
/* 1135 */           if (this.lexer.token() == Token.LOGGING) {
/* 1136 */             this.lexer.nextToken();
/* 1137 */             clause.setLogging(Boolean.valueOf(true));
/*      */           } 
/*      */           
/*      */           continue;
/*      */         } 
/* 1142 */         if (this.lexer.token() == Token.NOCOMPRESS) {
/* 1143 */           this.lexer.nextToken();
/* 1144 */           clause.setCompress(Boolean.valueOf(false));
/*      */           
/*      */           continue;
/*      */         } 
/* 1148 */         if (this.lexer.token() == Token.KEEP_DUPLICATES) {
/* 1149 */           this.lexer.nextToken();
/* 1150 */           clause.setKeepDuplicate(Boolean.valueOf(true));
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/*      */         break;
/*      */       } 
/* 1157 */       accept(Token.RPAREN);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1162 */     return clause;
/*      */   }
/*      */   
/*      */   public OracleStorageClause parseStorage() {
/* 1166 */     this.lexer.nextToken();
/* 1167 */     accept(Token.LPAREN);
/*      */     
/* 1169 */     OracleStorageClause storage = new OracleStorageClause();
/*      */     while (true) {
/* 1171 */       while (identifierEquals("INITIAL")) {
/* 1172 */         this.lexer.nextToken();
/* 1173 */         storage.setInitial(expr());
/*      */       } 
/* 1175 */       if (this.lexer.token() == Token.NEXT) {
/* 1176 */         this.lexer.nextToken();
/* 1177 */         storage.setNext(expr()); continue;
/*      */       } 
/* 1179 */       if (this.lexer.token() == Token.MINEXTENTS) {
/* 1180 */         this.lexer.nextToken();
/* 1181 */         storage.setMinExtents(expr()); continue;
/*      */       } 
/* 1183 */       if (this.lexer.token() == Token.MAXEXTENTS) {
/* 1184 */         this.lexer.nextToken();
/* 1185 */         storage.setMaxExtents(expr()); continue;
/*      */       } 
/* 1187 */       if (this.lexer.token() == Token.MAXSIZE) {
/* 1188 */         this.lexer.nextToken();
/* 1189 */         storage.setMaxSize(expr()); continue;
/*      */       } 
/* 1191 */       if (this.lexer.token() == Token.PCTINCREASE) {
/* 1192 */         this.lexer.nextToken();
/* 1193 */         storage.setPctIncrease(expr()); continue;
/*      */       } 
/* 1195 */       if (identifierEquals("FREELISTS")) {
/* 1196 */         this.lexer.nextToken();
/* 1197 */         storage.setFreeLists(expr()); continue;
/*      */       } 
/* 1199 */       if (identifierEquals("FREELIST")) {
/* 1200 */         this.lexer.nextToken();
/* 1201 */         acceptIdentifier("GROUPS");
/* 1202 */         storage.setFreeListGroups(expr()); continue;
/*      */       } 
/* 1204 */       if (identifierEquals("BUFFER_POOL")) {
/* 1205 */         this.lexer.nextToken();
/* 1206 */         storage.setBufferPool(expr()); continue;
/*      */       } 
/* 1208 */       if (identifierEquals("OBJNO")) {
/* 1209 */         this.lexer.nextToken();
/* 1210 */         storage.setObjno(expr()); continue;
/*      */       } 
/* 1212 */       if (this.lexer.token() == Token.FLASH_CACHE) {
/* 1213 */         OracleStorageClause.FlashCacheType flashCacheType; this.lexer.nextToken();
/*      */         
/* 1215 */         if (this.lexer.token() == Token.KEEP) {
/* 1216 */           flashCacheType = OracleStorageClause.FlashCacheType.KEEP;
/* 1217 */           this.lexer.nextToken();
/* 1218 */         } else if (this.lexer.token() == Token.NONE) {
/* 1219 */           flashCacheType = OracleStorageClause.FlashCacheType.NONE;
/* 1220 */           this.lexer.nextToken();
/*      */         } else {
/* 1222 */           accept(Token.DEFAULT);
/* 1223 */           flashCacheType = OracleStorageClause.FlashCacheType.DEFAULT;
/*      */         } 
/* 1225 */         storage.setFlashCache(flashCacheType); continue;
/*      */       } 
/* 1227 */       if (this.lexer.token() == Token.CELL_FLASH_CACHE) {
/* 1228 */         OracleStorageClause.FlashCacheType flashCacheType; this.lexer.nextToken();
/*      */         
/* 1230 */         if (this.lexer.token() == Token.KEEP) {
/* 1231 */           flashCacheType = OracleStorageClause.FlashCacheType.KEEP;
/* 1232 */           this.lexer.nextToken();
/* 1233 */         } else if (this.lexer.token() == Token.NONE) {
/* 1234 */           flashCacheType = OracleStorageClause.FlashCacheType.NONE;
/* 1235 */           this.lexer.nextToken();
/*      */         } else {
/* 1237 */           accept(Token.DEFAULT);
/* 1238 */           flashCacheType = OracleStorageClause.FlashCacheType.DEFAULT;
/*      */         } 
/* 1240 */         storage.setCellFlashCache(flashCacheType);
/*      */         
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 1246 */     accept(Token.RPAREN);
/* 1247 */     return storage;
/*      */   }
/*      */   
/*      */   public SQLUnique parseUnique() {
/* 1251 */     accept(Token.UNIQUE);
/*      */     
/* 1253 */     OracleUnique unique = new OracleUnique();
/* 1254 */     accept(Token.LPAREN);
/* 1255 */     exprList(unique.getColumns(), (SQLObject)unique);
/* 1256 */     accept(Token.RPAREN);
/*      */     
/* 1258 */     if (this.lexer.token() == Token.USING) {
/* 1259 */       OracleUsingIndexClause using = parseUsingIndex();
/* 1260 */       unique.setUsing(using);
/*      */     } 
/*      */     
/* 1263 */     return (SQLUnique)unique;
/*      */   }
/*      */   
/*      */   public OracleConstraint parseConstaint() {
/* 1267 */     OracleConstraint constraint = (OracleConstraint)super.parseConstaint();
/*      */     
/*      */     while (true) {
/* 1270 */       while (this.lexer.token() == Token.EXCEPTIONS) {
/* 1271 */         this.lexer.nextToken();
/* 1272 */         accept(Token.INTO);
/* 1273 */         SQLName exceptionsInto = name();
/* 1274 */         constraint.setExceptionsInto(exceptionsInto);
/*      */       } 
/*      */ 
/*      */       
/* 1278 */       if (this.lexer.token() == Token.DISABLE) {
/* 1279 */         this.lexer.nextToken();
/* 1280 */         constraint.setEnable(Boolean.valueOf(false));
/*      */         
/*      */         continue;
/*      */       } 
/* 1284 */       if (this.lexer.token() == Token.ENABLE) {
/* 1285 */         this.lexer.nextToken();
/* 1286 */         constraint.setEnable(Boolean.valueOf(true));
/*      */         
/*      */         continue;
/*      */       } 
/* 1290 */       if (this.lexer.token() == Token.INITIALLY) {
/* 1291 */         this.lexer.nextToken();
/*      */         
/* 1293 */         if (this.lexer.token() == Token.IMMEDIATE) {
/* 1294 */           this.lexer.nextToken();
/* 1295 */           constraint.setInitially(OracleConstraint.Initially.IMMEDIATE); continue;
/*      */         } 
/* 1297 */         accept(Token.DEFERRED);
/* 1298 */         constraint.setInitially(OracleConstraint.Initially.DEFERRED);
/*      */ 
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/* 1304 */       if (this.lexer.token() == Token.NOT) {
/* 1305 */         this.lexer.nextToken();
/* 1306 */         if (identifierEquals("DEFERRABLE")) {
/* 1307 */           this.lexer.nextToken();
/* 1308 */           constraint.setDeferrable(Boolean.valueOf(false));
/*      */           continue;
/*      */         } 
/* 1311 */         throw new ParserException("TODO " + this.lexer.token());
/*      */       } 
/*      */       
/* 1314 */       if (identifierEquals("DEFERRABLE")) {
/* 1315 */         this.lexer.nextToken();
/* 1316 */         constraint.setDeferrable(Boolean.valueOf(true));
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/*      */       break;
/*      */     } 
/* 1323 */     return constraint;
/*      */   }
/*      */   
/*      */   protected SQLForeignKeyConstraint createForeignKey() {
/* 1327 */     return (SQLForeignKeyConstraint)new OracleForeignKey();
/*      */   }
/*      */   
/*      */   protected SQLCheck createCheck() {
/* 1331 */     return (SQLCheck)new OracleCheck();
/*      */   }
/*      */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\parser\OracleExprParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */