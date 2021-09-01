/*      */ package com.tranboot.client.druid.sql.parser;
/*      */ 
/*      */

import com.tranboot.client.druid.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
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
/*      */ public class Lexer
/*      */ {
/*      */   protected final String text;
/*      */   protected int pos;
/*      */   protected int mark;
/*      */   protected char ch;
/*      */   protected char[] buf;
/*      */   protected int bufPos;
/*      */   protected Token token;
/*   61 */   protected Keywords keywods = Keywords.DEFAULT_KEYWORDS;
/*      */   
/*      */   protected String stringVal;
/*      */   
/*   65 */   protected int commentCount = 0;
/*      */   
/*   67 */   protected List<String> comments = new ArrayList<>(2);
/*      */   
/*      */   protected boolean skipComment = true;
/*      */   
/*   71 */   private SavePoint savePoint = null;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean allowComment = true;
/*      */ 
/*      */   
/*   78 */   private int varIndex = -1;
/*      */   
/*      */   protected CommentHandler commentHandler;
/*      */   
/*      */   protected boolean endOfComment = false;
/*      */   
/*      */   protected boolean keepComments = false;
/*      */   
/*   86 */   protected int line = 0;
/*      */   
/*   88 */   protected int lines = 0; protected String dbType;
/*      */   private static final long MULTMIN_RADIX_TEN = -922337203685477580L;
/*      */   private static final long N_MULTMAX_RADIX_TEN = -922337203685477580L;
/*      */   
/*      */   public Lexer(String input) {
/*   93 */     this(input, (CommentHandler)null);
/*      */   }
/*      */   
/*      */   public Lexer(String input, CommentHandler commentHandler) {
/*   97 */     this(input, true);
/*   98 */     this.commentHandler = commentHandler;
/*      */   }
/*      */   
/*      */   public Lexer(String input, CommentHandler commentHandler, String dbType) {
/*  102 */     this(input, true);
/*  103 */     this.commentHandler = commentHandler;
/*  104 */     this.dbType = dbType;
/*      */     
/*  106 */     if ("sqlite".equals(dbType)) {
/*  107 */       this.keywods = Keywords.SQLITE_KEYWORDS;
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isKeepComments() {
/*  112 */     return this.keepComments;
/*      */   }
/*      */   
/*      */   public void setKeepComments(boolean keepComments) {
/*  116 */     this.keepComments = keepComments;
/*      */   }
/*      */   
/*      */   public CommentHandler getCommentHandler() {
/*  120 */     return this.commentHandler;
/*      */   }
/*      */   
/*      */   public void setCommentHandler(CommentHandler commentHandler) {
/*  124 */     this.commentHandler = commentHandler;
/*      */   }
/*      */   
/*      */   public final char charAt(int index) {
/*  128 */     if (index >= this.text.length()) {
/*  129 */       return '\032';
/*      */     }
/*      */     
/*  132 */     return this.text.charAt(index);
/*      */   }
/*      */   
/*      */   public final String addSymbol() {
/*  136 */     return subString(this.mark, this.bufPos);
/*      */   }
/*      */   
/*      */   public final String subString(int offset, int count) {
/*  140 */     return this.text.substring(offset, offset + count);
/*      */   }
/*      */   
/*      */   protected void initBuff(int size) {
/*  144 */     if (this.buf == null) {
/*  145 */       if (size < 32) {
/*  146 */         this.buf = new char[32];
/*      */       } else {
/*  148 */         this.buf = new char[size + 32];
/*      */       } 
/*  150 */     } else if (this.buf.length < size) {
/*  151 */       this.buf = Arrays.copyOf(this.buf, size);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void arraycopy(int srcPos, char[] dest, int destPos, int length) {
/*  156 */     this.text.getChars(srcPos, srcPos + length, dest, destPos);
/*      */   }
/*      */   
/*      */   public boolean isAllowComment() {
/*  160 */     return this.allowComment;
/*      */   }
/*      */   
/*      */   public void setAllowComment(boolean allowComment) {
/*  164 */     this.allowComment = allowComment;
/*      */   }
/*      */   
/*      */   public int nextVarIndex() {
/*  168 */     return ++this.varIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static interface CommentHandler
/*      */   {
/*      */     boolean handle(Token param1Token, String param1String);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Keywords getKeywods() {
/*  181 */     return this.keywods;
/*      */   } private static class SavePoint {
/*      */     int bp; int sp; int np; char ch; Token token; private SavePoint() {} }
/*      */   public void mark() {
/*  185 */     SavePoint savePoint = new SavePoint();
/*  186 */     savePoint.bp = this.pos;
/*  187 */     savePoint.sp = this.bufPos;
/*  188 */     savePoint.np = this.mark;
/*  189 */     savePoint.ch = this.ch;
/*  190 */     savePoint.token = this.token;
/*  191 */     this.savePoint = savePoint;
/*      */   }
/*      */   
/*      */   public void reset() {
/*  195 */     this.pos = this.savePoint.bp;
/*  196 */     this.bufPos = this.savePoint.sp;
/*  197 */     this.mark = this.savePoint.np;
/*  198 */     this.ch = this.savePoint.ch;
/*  199 */     this.token = this.savePoint.token;
/*      */   }
/*      */   
/*      */   public Lexer(String input, boolean skipComment) {
/*  203 */     this.skipComment = skipComment;
/*      */     
/*  205 */     this.text = input;
/*  206 */     this.pos = -1;
/*      */     
/*  208 */     scanChar();
/*      */   }
/*      */   
/*      */   public Lexer(char[] input, int inputLength, boolean skipComment) {
/*  212 */     this(new String(input, 0, inputLength), skipComment);
/*      */   }
/*      */   
/*      */   protected final void scanChar() {
/*  216 */     this.ch = charAt(++this.pos);
/*      */   }
/*      */   
/*      */   protected void unscan() {
/*  220 */     this.ch = charAt(--this.pos);
/*      */   }
/*      */   
/*      */   public boolean isEOF() {
/*  224 */     return (this.pos >= this.text.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void lexError(String key, Object... args) {
/*  231 */     this.token = Token.ERROR;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Token token() {
/*  238 */     return this.token;
/*      */   }
/*      */   
/*      */   public final String getDbType() {
/*  242 */     return this.dbType;
/*      */   }
/*      */   
/*      */   public String info() {
/*  246 */     return this.token + " " + stringVal();
/*      */   }
/*      */   
/*      */   public final void nextTokenComma() {
/*  250 */     if (this.ch == ' ') {
/*  251 */       scanChar();
/*      */     }
/*      */     
/*  254 */     if (this.ch == ',' || this.ch == '，') {
/*  255 */       scanChar();
/*  256 */       this.token = Token.COMMA;
/*      */       
/*      */       return;
/*      */     } 
/*  260 */     if (this.ch == ')') {
/*  261 */       scanChar();
/*  262 */       this.token = Token.RPAREN;
/*      */       
/*      */       return;
/*      */     } 
/*  266 */     nextToken();
/*      */   }
/*      */   
/*      */   public final void nextTokenLParen() {
/*  270 */     if (this.ch == ' ') {
/*  271 */       scanChar();
/*      */     }
/*      */     
/*  274 */     if (this.ch == '(') {
/*  275 */       scanChar();
/*  276 */       this.token = Token.LPAREN;
/*      */       return;
/*      */     } 
/*  279 */     nextToken();
/*      */   }
/*      */   
/*      */   public final void nextTokenValue() {
/*  283 */     if (this.ch == ' ') {
/*  284 */       scanChar();
/*      */     }
/*      */     
/*  287 */     if (this.ch == '\'') {
/*  288 */       this.bufPos = 0;
/*  289 */       scanString();
/*      */       
/*      */       return;
/*      */     } 
/*  293 */     if (this.ch >= '0' && this.ch <= '9') {
/*  294 */       this.bufPos = 0;
/*  295 */       scanNumber();
/*      */       
/*      */       return;
/*      */     } 
/*  299 */     if (this.ch == '?') {
/*  300 */       scanChar();
/*  301 */       this.token = Token.QUES;
/*      */       
/*      */       return;
/*      */     } 
/*  305 */     if (CharTypes.isFirstIdentifierChar(this.ch) && this.ch != 'N') {
/*  306 */       scanIdentifier();
/*      */       
/*      */       return;
/*      */     } 
/*  310 */     nextToken();
/*      */   }
/*      */   
/*      */   public final void nextToken() {
/*  314 */     this.bufPos = 0;
/*  315 */     if (this.comments != null && this.comments.size() > 0) {
/*  316 */       this.comments = null;
/*      */     }
/*      */     
/*  319 */     this.lines = 0;
/*  320 */     int startLine = this.line;
/*      */     while (true) {
/*      */       int nextChar;
/*  323 */       while (CharTypes.isWhitespace(this.ch)) {
/*  324 */         if (this.ch == '\n') {
/*  325 */           this.line++;
/*      */           
/*  327 */           this.lines = this.line - startLine;
/*      */         } 
/*      */         
/*  330 */         scanChar();
/*      */       } 
/*      */ 
/*      */       
/*  334 */       if (this.ch == '$' && charAt(this.pos + 1) == '{') {
/*  335 */         scanVariable();
/*      */         
/*      */         return;
/*      */       } 
/*  339 */       if (CharTypes.isFirstIdentifierChar(this.ch)) {
/*  340 */         if (this.ch == 'N' && 
/*  341 */           charAt(this.pos + 1) == '\'') {
/*  342 */           this.pos++;
/*  343 */           this.ch = '\'';
/*  344 */           scanString();
/*  345 */           this.token = Token.LITERAL_NCHARS;
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/*  350 */         scanIdentifier();
/*      */         
/*      */         return;
/*      */       } 
/*  354 */       switch (this.ch) {
/*      */         case '0':
/*  356 */           if (charAt(this.pos + 1) == 'x') {
/*  357 */             scanChar();
/*  358 */             scanChar();
/*  359 */             scanHexaDecimal();
/*      */           } else {
/*  361 */             scanNumber();
/*      */           } 
/*      */           return;
/*      */         case '1':
/*      */         case '2':
/*      */         case '3':
/*      */         case '4':
/*      */         case '5':
/*      */         case '6':
/*      */         case '7':
/*      */         case '8':
/*      */         case '9':
/*  373 */           scanNumber();
/*      */           return;
/*      */         case ',':
/*      */         case '，':
/*  377 */           scanChar();
/*  378 */           this.token = Token.COMMA;
/*      */           return;
/*      */         case '(':
/*  381 */           scanChar();
/*  382 */           this.token = Token.LPAREN;
/*      */           return;
/*      */         case ')':
/*  385 */           scanChar();
/*  386 */           this.token = Token.RPAREN;
/*      */           return;
/*      */         case '[':
/*  389 */           scanLBracket();
/*      */           return;
/*      */         case ']':
/*  392 */           scanChar();
/*  393 */           this.token = Token.RBRACKET;
/*      */           return;
/*      */         case '{':
/*  396 */           scanChar();
/*  397 */           this.token = Token.LBRACE;
/*      */           return;
/*      */         case '}':
/*  400 */           scanChar();
/*  401 */           this.token = Token.RBRACE;
/*      */           return;
/*      */         case ':':
/*  404 */           scanChar();
/*  405 */           if (this.ch == '=') {
/*  406 */             scanChar();
/*  407 */             this.token = Token.COLONEQ;
/*  408 */           } else if (this.ch == ':') {
/*  409 */             scanChar();
/*  410 */             this.token = Token.COLONCOLON;
/*      */           } else {
/*  412 */             unscan();
/*  413 */             scanVariable();
/*      */           } 
/*      */           return;
/*      */         case '#':
/*  417 */           scanSharp();
/*  418 */           if ((token() == Token.LINE_COMMENT || token() == Token.MULTI_LINE_COMMENT) && this.skipComment) {
/*  419 */             this.bufPos = 0;
/*      */             continue;
/*      */           } 
/*      */           return;
/*      */         case '.':
/*  424 */           scanChar();
/*  425 */           if (isDigit(this.ch) && !CharTypes.isFirstIdentifierChar(charAt(this.pos - 2))) {
/*  426 */             unscan();
/*  427 */             scanNumber(); return;
/*      */           } 
/*  429 */           if (this.ch == '.') {
/*  430 */             scanChar();
/*  431 */             if (this.ch == '.') {
/*  432 */               scanChar();
/*  433 */               this.token = Token.DOTDOTDOT;
/*      */             } else {
/*  435 */               this.token = Token.DOTDOT;
/*      */             } 
/*      */           } else {
/*  438 */             this.token = Token.DOT;
/*      */           } 
/*      */           return;
/*      */         case '\'':
/*  442 */           scanString();
/*      */           return;
/*      */         case '"':
/*  445 */           scanAlias();
/*      */           return;
/*      */         case '*':
/*  448 */           scanChar();
/*  449 */           this.token = Token.STAR;
/*      */           return;
/*      */         case '?':
/*  452 */           scanChar();
/*  453 */           if (this.ch == '?') {
/*  454 */             scanChar();
/*  455 */             this.token = Token.QUESQUES;
/*  456 */           } else if (this.ch == '|') {
/*  457 */             scanChar();
/*  458 */             this.token = Token.QUESBAR;
/*  459 */           } else if (this.ch == '&') {
/*  460 */             scanChar();
/*  461 */             this.token = Token.QUESAMP;
/*      */           } else {
/*  463 */             this.token = Token.QUES;
/*      */           } 
/*      */           return;
/*      */         case ';':
/*  467 */           scanChar();
/*  468 */           this.token = Token.SEMI;
/*      */           return;
/*      */         case '`':
/*  471 */           throw new ParserException("TODO");
/*      */         case '@':
/*  473 */           scanVariable();
/*      */           return;
/*      */         case '-':
/*  476 */           if (charAt(this.pos + 1) == '-') {
/*  477 */             scanComment();
/*  478 */             if ((token() == Token.LINE_COMMENT || token() == Token.MULTI_LINE_COMMENT) && this.skipComment) {
/*  479 */               this.bufPos = 0;
/*      */               continue;
/*      */             } 
/*      */           } else {
/*  483 */             scanOperator();
/*      */           } 
/*      */           return;
/*      */         case '/':
/*  487 */           nextChar = charAt(this.pos + 1);
/*  488 */           if (nextChar == 47 || nextChar == 42) {
/*  489 */             scanComment();
/*  490 */             if ((token() == Token.LINE_COMMENT || token() == Token.MULTI_LINE_COMMENT) && this.skipComment) {
/*  491 */               this.bufPos = 0;
/*      */               continue;
/*      */             } 
/*      */           } else {
/*  495 */             this.token = Token.SLASH;
/*  496 */             scanChar();
/*      */           }  return;
/*      */       }  break;
/*      */     } 
/*  500 */     if (Character.isLetter(this.ch)) {
/*  501 */       scanIdentifier();
/*      */       
/*      */       return;
/*      */     } 
/*  505 */     if (isOperator(this.ch)) {
/*  506 */       scanOperator();
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  511 */     if (isEOF()) {
/*  512 */       this.token = Token.EOF;
/*      */     } else {
/*  514 */       lexError("illegal.char", new Object[] { String.valueOf(this.ch) });
/*  515 */       scanChar();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void scanLBracket() {
/*  525 */     scanChar();
/*  526 */     this.token = Token.LBRACKET;
/*      */   }
/*      */   
/*      */   private final void scanOperator() {
/*  530 */     switch (this.ch) {
/*      */       case '+':
/*  532 */         scanChar();
/*  533 */         this.token = Token.PLUS;
/*      */         return;
/*      */       case '-':
/*  536 */         scanChar();
/*  537 */         if (this.ch == '>') {
/*  538 */           scanChar();
/*  539 */           if (this.ch == '>') {
/*  540 */             scanChar();
/*  541 */             this.token = Token.SUBGTGT;
/*      */           } else {
/*  543 */             this.token = Token.SUBGT;
/*      */           } 
/*      */         } else {
/*  546 */           this.token = Token.SUB;
/*      */         } 
/*      */         return;
/*      */       case '*':
/*  550 */         scanChar();
/*  551 */         this.token = Token.STAR;
/*      */         return;
/*      */       case '/':
/*  554 */         scanChar();
/*  555 */         this.token = Token.SLASH;
/*      */         return;
/*      */       case '&':
/*  558 */         scanChar();
/*  559 */         if (this.ch == '&') {
/*  560 */           scanChar();
/*  561 */           this.token = Token.AMPAMP;
/*      */         } else {
/*  563 */           this.token = Token.AMP;
/*      */         } 
/*      */         return;
/*      */       case '|':
/*  567 */         scanChar();
/*  568 */         if (this.ch == '|') {
/*  569 */           scanChar();
/*  570 */           if (this.ch == '/') {
/*  571 */             scanChar();
/*  572 */             this.token = Token.BARBARSLASH;
/*      */           } else {
/*  574 */             this.token = Token.BARBAR;
/*      */           } 
/*  576 */         } else if (this.ch == '/') {
/*  577 */           scanChar();
/*  578 */           this.token = Token.BARSLASH;
/*      */         } else {
/*  580 */           this.token = Token.BAR;
/*      */         } 
/*      */         return;
/*      */       case '^':
/*  584 */         scanChar();
/*  585 */         this.token = Token.CARET;
/*      */         return;
/*      */       case '%':
/*  588 */         scanChar();
/*  589 */         this.token = Token.PERCENT;
/*      */         return;
/*      */       case '=':
/*  592 */         scanChar();
/*  593 */         if (this.ch == '=') {
/*  594 */           scanChar();
/*  595 */           this.token = Token.EQEQ;
/*      */         } else {
/*  597 */           this.token = Token.EQ;
/*      */         } 
/*      */         return;
/*      */       case '>':
/*  601 */         scanChar();
/*  602 */         if (this.ch == '=') {
/*  603 */           scanChar();
/*  604 */           this.token = Token.GTEQ;
/*  605 */         } else if (this.ch == '>') {
/*  606 */           scanChar();
/*  607 */           this.token = Token.GTGT;
/*      */         } else {
/*  609 */           this.token = Token.GT;
/*      */         } 
/*      */         return;
/*      */       case '<':
/*  613 */         scanChar();
/*  614 */         if (this.ch == '=') {
/*  615 */           scanChar();
/*  616 */           if (this.ch == '>') {
/*  617 */             this.token = Token.LTEQGT;
/*  618 */             scanChar();
/*      */           } else {
/*  620 */             this.token = Token.LTEQ;
/*      */           } 
/*  622 */         } else if (this.ch == '>') {
/*  623 */           scanChar();
/*  624 */           this.token = Token.LTGT;
/*  625 */         } else if (this.ch == '<') {
/*  626 */           scanChar();
/*  627 */           this.token = Token.LTLT;
/*  628 */         } else if (this.ch == '@') {
/*  629 */           scanChar();
/*  630 */           this.token = Token.LT_MONKEYS_AT;
/*      */         } else {
/*  632 */           this.token = Token.LT;
/*      */         } 
/*      */         return;
/*      */       case '!':
/*  636 */         scanChar();
/*  637 */         if (this.ch == '=') {
/*  638 */           scanChar();
/*  639 */           this.token = Token.BANGEQ;
/*  640 */         } else if (this.ch == '>') {
/*  641 */           scanChar();
/*  642 */           this.token = Token.BANGGT;
/*  643 */         } else if (this.ch == '<') {
/*  644 */           scanChar();
/*  645 */           this.token = Token.BANGLT;
/*  646 */         } else if (this.ch == '!') {
/*  647 */           scanChar();
/*  648 */           this.token = Token.BANGBANG;
/*  649 */         } else if (this.ch == '~') {
/*  650 */           scanChar();
/*  651 */           if (this.ch == '*') {
/*  652 */             scanChar();
/*  653 */             this.token = Token.BANG_TILDE_STAR;
/*      */           } else {
/*  655 */             this.token = Token.BANG_TILDE;
/*      */           } 
/*      */         } else {
/*  658 */           this.token = Token.BANG;
/*      */         } 
/*      */         return;
/*      */       case '?':
/*  662 */         scanChar();
/*  663 */         this.token = Token.QUES;
/*      */         return;
/*      */       case '~':
/*  666 */         scanChar();
/*  667 */         if (this.ch == '*') {
/*  668 */           scanChar();
/*  669 */           this.token = Token.TILDE_STAR;
/*  670 */         } else if (this.ch == '=') {
/*  671 */           scanChar();
/*  672 */           this.token = Token.TILDE_EQ;
/*      */         } else {
/*  674 */           this.token = Token.TILDE;
/*      */         } 
/*      */         return;
/*      */     } 
/*  678 */     throw new ParserException("TODO");
/*      */   }
/*      */ 
/*      */   
/*      */   protected void scanString() {
/*  683 */     this.mark = this.pos;
/*  684 */     boolean hasSpecial = false;
/*      */     
/*      */     while (true) {
/*  687 */       if (isEOF()) {
/*  688 */         lexError("unclosed.str.lit", new Object[0]);
/*      */         
/*      */         return;
/*      */       } 
/*  692 */       this.ch = charAt(++this.pos);
/*      */       
/*  694 */       if (this.ch == '\'') {
/*  695 */         scanChar();
/*  696 */         if (this.ch != '\'') {
/*  697 */           this.token = Token.LITERAL_CHARS;
/*      */           break;
/*      */         } 
/*  700 */         if (!hasSpecial) {
/*  701 */           initBuff(this.bufPos);
/*  702 */           arraycopy(this.mark + 1, this.buf, 0, this.bufPos);
/*  703 */           hasSpecial = true;
/*      */         } 
/*  705 */         putChar('\'');
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/*  710 */       if (!hasSpecial) {
/*  711 */         this.bufPos++;
/*      */         
/*      */         continue;
/*      */       } 
/*  715 */       if (this.bufPos == this.buf.length) {
/*  716 */         putChar(this.ch); continue;
/*      */       } 
/*  718 */       this.buf[this.bufPos++] = this.ch;
/*      */     } 
/*      */ 
/*      */     
/*  722 */     if (!hasSpecial) {
/*  723 */       this.stringVal = subString(this.mark + 1, this.bufPos);
/*      */     } else {
/*  725 */       this.stringVal = new String(this.buf, 0, this.bufPos);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected final void scanString2() {
/*  731 */     boolean hasSpecial = false;
/*  732 */     int startIndex = this.pos + 1;
/*  733 */     int endIndex = -1;
/*  734 */     for (int i = startIndex; i < this.text.length(); i++) {
/*  735 */       char ch = this.text.charAt(i);
/*  736 */       if (ch == '\\') {
/*  737 */         hasSpecial = true;
/*      */       
/*      */       }
/*  740 */       else if (ch == '\'') {
/*  741 */         endIndex = i;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  746 */     if (endIndex == -1) {
/*  747 */       throw new ParserException("unclosed str");
/*      */     }
/*      */     
/*  750 */     String stringVal = subString(startIndex, endIndex - startIndex);
/*      */ 
/*      */     
/*  753 */     if (!hasSpecial) {
/*  754 */       this.stringVal = stringVal;
/*  755 */       int pos = endIndex + 1;
/*  756 */       char ch = charAt(pos);
/*  757 */       if (ch != '\'') {
/*  758 */         this.pos = pos;
/*  759 */         this.ch = ch;
/*  760 */         this.token = Token.LITERAL_CHARS;
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/*      */     
/*  766 */     this.mark = this.pos;
/*  767 */     hasSpecial = false;
/*      */     while (true) {
/*  769 */       if (isEOF()) {
/*  770 */         lexError("unclosed.str.lit", new Object[0]);
/*      */         
/*      */         return;
/*      */       } 
/*  774 */       this.ch = charAt(++this.pos);
/*      */       
/*  776 */       if (this.ch == '\\') {
/*  777 */         scanChar();
/*  778 */         if (!hasSpecial) {
/*  779 */           initBuff(this.bufPos);
/*  780 */           arraycopy(this.mark + 1, this.buf, 0, this.bufPos);
/*  781 */           hasSpecial = true;
/*      */         } 
/*      */         
/*  784 */         switch (this.ch) {
/*      */           case '0':
/*  786 */             putChar(false);
/*      */             continue;
/*      */           case '\'':
/*  789 */             putChar('\'');
/*      */             continue;
/*      */           case '"':
/*  792 */             putChar('"');
/*      */             continue;
/*      */           case 'b':
/*  795 */             putChar('\b');
/*      */             continue;
/*      */           case 'n':
/*  798 */             putChar('\n');
/*      */             continue;
/*      */           case 'r':
/*  801 */             putChar('\r');
/*      */             continue;
/*      */           case 't':
/*  804 */             putChar('\t');
/*      */             continue;
/*      */           case '\\':
/*  807 */             putChar('\\');
/*      */             continue;
/*      */           case 'Z':
/*  810 */             putChar('\032');
/*      */             continue;
/*      */         } 
/*  813 */         putChar(this.ch);
/*      */ 
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/*  819 */       if (this.ch == '\'') {
/*  820 */         scanChar();
/*  821 */         if (this.ch != '\'') {
/*  822 */           this.token = Token.LITERAL_CHARS;
/*      */           break;
/*      */         } 
/*  825 */         if (!hasSpecial) {
/*  826 */           initBuff(this.bufPos);
/*  827 */           arraycopy(this.mark + 1, this.buf, 0, this.bufPos);
/*  828 */           hasSpecial = true;
/*      */         } 
/*  830 */         putChar('\'');
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/*  835 */       if (!hasSpecial) {
/*  836 */         this.bufPos++;
/*      */         
/*      */         continue;
/*      */       } 
/*  840 */       if (this.bufPos == this.buf.length) {
/*  841 */         putChar(this.ch); continue;
/*      */       } 
/*  843 */       this.buf[this.bufPos++] = this.ch;
/*      */     } 
/*      */ 
/*      */     
/*  847 */     if (!hasSpecial) {
/*  848 */       this.stringVal = subString(this.mark + 1, this.bufPos);
/*      */     } else {
/*  850 */       this.stringVal = new String(this.buf, 0, this.bufPos);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void scanAlias() {
/*  855 */     this.mark = this.pos;
/*      */     
/*  857 */     if (this.buf == null) {
/*  858 */       this.buf = new char[32];
/*      */     }
/*      */     
/*  861 */     boolean hasSpecial = false;
/*      */     while (true) {
/*  863 */       if (isEOF()) {
/*  864 */         lexError("unclosed.str.lit", new Object[0]);
/*      */         
/*      */         return;
/*      */       } 
/*  868 */       this.ch = charAt(++this.pos);
/*      */       
/*  870 */       if (this.ch == '"' && charAt(this.pos - 1) != '\\') {
/*  871 */         scanChar();
/*  872 */         this.token = Token.LITERAL_ALIAS;
/*      */         
/*      */         break;
/*      */       } 
/*  876 */       if (this.ch == '\\') {
/*  877 */         scanChar();
/*  878 */         if (this.ch == '"') {
/*  879 */           hasSpecial = true;
/*      */         } else {
/*  881 */           unscan();
/*      */         } 
/*      */       } 
/*      */       
/*  885 */       if (this.bufPos == this.buf.length) {
/*  886 */         putChar(this.ch); continue;
/*      */       } 
/*  888 */       this.buf[this.bufPos++] = this.ch;
/*      */     } 
/*      */ 
/*      */     
/*  892 */     if (!hasSpecial) {
/*  893 */       this.stringVal = subString(this.mark + 1, this.bufPos);
/*      */     } else {
/*  895 */       this.stringVal = new String(this.buf, 0, this.bufPos);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void scanAlias2() {
/*  903 */     boolean hasSpecial = false;
/*  904 */     int startIndex = this.pos + 1;
/*  905 */     int endIndex = -1;
/*  906 */     for (int i = startIndex; i < this.text.length(); i++) {
/*  907 */       char ch = this.text.charAt(i);
/*  908 */       if (ch == '\\') {
/*  909 */         hasSpecial = true;
/*      */       
/*      */       }
/*  912 */       else if (ch == '"') {
/*  913 */         endIndex = i;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  918 */     if (endIndex == -1) {
/*  919 */       throw new ParserException("unclosed str");
/*      */     }
/*      */     
/*  922 */     String stringVal = subString(startIndex, endIndex - startIndex);
/*      */ 
/*      */     
/*  925 */     if (!hasSpecial) {
/*  926 */       this.stringVal = stringVal;
/*  927 */       int pos = endIndex + 1;
/*  928 */       char ch = charAt(pos);
/*  929 */       if (ch != '\'') {
/*  930 */         this.pos = pos;
/*  931 */         this.ch = ch;
/*  932 */         this.token = Token.LITERAL_CHARS;
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/*      */     
/*  938 */     this.mark = this.pos;
/*  939 */     hasSpecial = false;
/*      */     while (true) {
/*  941 */       if (isEOF()) {
/*  942 */         lexError("unclosed.str.lit", new Object[0]);
/*      */         
/*      */         return;
/*      */       } 
/*  946 */       this.ch = charAt(++this.pos);
/*      */       
/*  948 */       if (this.ch == '\\') {
/*  949 */         scanChar();
/*  950 */         if (!hasSpecial) {
/*  951 */           initBuff(this.bufPos);
/*  952 */           arraycopy(this.mark + 1, this.buf, 0, this.bufPos);
/*  953 */           hasSpecial = true;
/*      */         } 
/*      */         
/*  956 */         switch (this.ch) {
/*      */           case '0':
/*  958 */             putChar(false);
/*      */             continue;
/*      */           case '\'':
/*  961 */             putChar('\'');
/*      */             continue;
/*      */           case '"':
/*  964 */             putChar('"');
/*      */             continue;
/*      */           case 'b':
/*  967 */             putChar('\b');
/*      */             continue;
/*      */           case 'n':
/*  970 */             putChar('\n');
/*      */             continue;
/*      */           case 'r':
/*  973 */             putChar('\r');
/*      */             continue;
/*      */           case 't':
/*  976 */             putChar('\t');
/*      */             continue;
/*      */           case '\\':
/*  979 */             putChar('\\');
/*      */             continue;
/*      */           case 'Z':
/*  982 */             putChar('\032');
/*      */             continue;
/*      */         } 
/*  985 */         putChar(this.ch);
/*      */ 
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/*  991 */       if (this.ch == '"') {
/*  992 */         scanChar();
/*  993 */         this.token = Token.LITERAL_CHARS;
/*      */         
/*      */         break;
/*      */       } 
/*  997 */       if (!hasSpecial) {
/*  998 */         this.bufPos++;
/*      */         
/*      */         continue;
/*      */       } 
/* 1002 */       if (this.bufPos == this.buf.length) {
/* 1003 */         putChar(this.ch); continue;
/*      */       } 
/* 1005 */       this.buf[this.bufPos++] = this.ch;
/*      */     } 
/*      */ 
/*      */     
/* 1009 */     if (!hasSpecial) {
/* 1010 */       this.stringVal = subString(this.mark + 1, this.bufPos);
/*      */     } else {
/* 1012 */       this.stringVal = new String(this.buf, 0, this.bufPos);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void scanSharp() {
/* 1017 */     scanVariable();
/*      */   }
/*      */   
/*      */   public void scanVariable() {
/* 1021 */     if (this.ch != '@' && this.ch != ':' && this.ch != '#' && this.ch != '$') {
/* 1022 */       throw new ParserException("illegal variable");
/*      */     }
/*      */     
/* 1025 */     this.mark = this.pos;
/* 1026 */     this.bufPos = 1;
/*      */ 
/*      */     
/* 1029 */     char c1 = charAt(this.pos + 1);
/* 1030 */     if (c1 == '@')
/* 1031 */     { if ("postgresql".equalsIgnoreCase(this.dbType)) {
/* 1032 */         this.pos += 2;
/* 1033 */         this.token = Token.MONKEYS_AT_AT;
/* 1034 */         this.ch = charAt(++this.pos);
/*      */         return;
/*      */       } 
/* 1037 */       char ch = charAt(++this.pos);
/*      */       
/* 1039 */       this.bufPos++; }
/* 1040 */     else { if (c1 == '>' && "postgresql".equalsIgnoreCase(this.dbType)) {
/* 1041 */         this.pos += 2;
/* 1042 */         this.token = Token.MONKEYS_AT_GT;
/* 1043 */         this.ch = charAt(++this.pos); return;
/*      */       } 
/* 1045 */       if (c1 == '{') {
/* 1046 */         char ch; this.pos++;
/* 1047 */         this.bufPos++;
/*      */         
/*      */         while (true) {
/* 1050 */           ch = charAt(++this.pos);
/*      */           
/* 1052 */           if (ch == '}') {
/*      */             break;
/*      */           }
/*      */           
/* 1056 */           this.bufPos++;
/*      */         } 
/*      */ 
/*      */         
/* 1060 */         if (ch != '}') {
/* 1061 */           throw new ParserException("syntax error");
/*      */         }
/* 1063 */         this.pos++;
/* 1064 */         this.bufPos++;
/*      */         
/* 1066 */         this.ch = charAt(this.pos);
/*      */         
/* 1068 */         this.stringVal = addSymbol();
/* 1069 */         this.token = Token.VARIANT;
/*      */         return;
/*      */       }  }
/*      */     
/*      */     while (true) {
/* 1074 */       char ch = charAt(++this.pos);
/*      */       
/* 1076 */       if (!CharTypes.isIdentifierChar(ch)) {
/*      */         break;
/*      */       }
/*      */       
/* 1080 */       this.bufPos++;
/*      */     } 
/*      */ 
/*      */     
/* 1084 */     this.ch = charAt(this.pos);
/*      */     
/* 1086 */     this.stringVal = addSymbol();
/* 1087 */     this.token = Token.VARIANT;
/*      */   }
/*      */   
/*      */   public void scanComment() {
/* 1091 */     if (!this.allowComment) {
/* 1092 */       throw new NotAllowCommentException();
/*      */     }
/*      */     
/* 1095 */     if ((this.ch == '/' && charAt(this.pos + 1) == '/') || (this.ch == '-' && 
/* 1096 */       charAt(this.pos + 1) == '-')) {
/* 1097 */       scanSingleLineComment();
/* 1098 */     } else if (this.ch == '/' && charAt(this.pos + 1) == '*') {
/* 1099 */       scanMultiLineComment();
/*      */     } else {
/* 1101 */       throw new IllegalStateException();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void scanMultiLineComment() {
/* 1106 */     Token lastToken = this.token;
/*      */     
/* 1108 */     scanChar();
/* 1109 */     scanChar();
/* 1110 */     this.mark = this.pos;
/* 1111 */     this.bufPos = 0;
/*      */     
/*      */     while (true) {
/* 1114 */       if (this.ch == '*' && charAt(this.pos + 1) == '/') {
/* 1115 */         scanChar();
/* 1116 */         scanChar();
/*      */         
/*      */         break;
/*      */       } 
/*      */       
/* 1121 */       if (this.ch == '\032') {
/* 1122 */         throw new ParserException("unterminated /* comment.");
/*      */       }
/* 1124 */       scanChar();
/* 1125 */       this.bufPos++;
/*      */     } 
/*      */     
/* 1128 */     this.stringVal = subString(this.mark, this.bufPos);
/* 1129 */     this.token = Token.MULTI_LINE_COMMENT;
/* 1130 */     this.commentCount++;
/* 1131 */     if (this.keepComments) {
/* 1132 */       addComment(this.stringVal);
/*      */     }
/*      */     
/* 1135 */     if (this.commentHandler != null && this.commentHandler.handle(lastToken, this.stringVal)) {
/*      */       return;
/*      */     }
/*      */     
/* 1139 */     if (!isAllowComment() && !isSafeComment(this.stringVal)) {
/* 1140 */       throw new NotAllowCommentException();
/*      */     }
/*      */   }
/*      */   
/*      */   private void scanSingleLineComment() {
/* 1145 */     Token lastToken = this.token;
/*      */     
/* 1147 */     scanChar();
/* 1148 */     scanChar();
/* 1149 */     this.mark = this.pos;
/* 1150 */     this.bufPos = 0;
/*      */     
/*      */     while (true) {
/* 1153 */       if (this.ch == '\r') {
/* 1154 */         if (charAt(this.pos + 1) == '\n') {
/* 1155 */           this.line++;
/* 1156 */           scanChar();
/*      */           break;
/*      */         } 
/* 1159 */         this.bufPos++;
/*      */         
/*      */         break;
/*      */       } 
/* 1163 */       if (this.ch == '\n') {
/* 1164 */         this.line++;
/* 1165 */         scanChar();
/*      */         
/*      */         break;
/*      */       } 
/*      */       
/* 1170 */       if (this.ch == '\032') {
/* 1171 */         throw new ParserException("syntax error at end of input.");
/*      */       }
/*      */       
/* 1174 */       scanChar();
/* 1175 */       this.bufPos++;
/*      */     } 
/*      */     
/* 1178 */     this.stringVal = subString(this.mark, this.bufPos);
/* 1179 */     this.token = Token.LINE_COMMENT;
/* 1180 */     this.commentCount++;
/* 1181 */     if (this.keepComments) {
/* 1182 */       addComment(this.stringVal);
/*      */     }
/*      */     
/* 1185 */     if (this.commentHandler != null && this.commentHandler.handle(lastToken, this.stringVal)) {
/*      */       return;
/*      */     }
/*      */     
/* 1189 */     if (!isAllowComment() && !isSafeComment(this.stringVal)) {
/* 1190 */       throw new NotAllowCommentException();
/*      */     }
/*      */   }
/*      */   
/*      */   public void scanIdentifier() {
/* 1195 */     char first = this.ch;
/*      */     
/* 1197 */     boolean firstFlag = CharTypes.isFirstIdentifierChar(first);
/* 1198 */     if (!firstFlag) {
/* 1199 */       throw new ParserException("illegal identifier");
/*      */     }
/*      */     
/* 1202 */     this.mark = this.pos;
/* 1203 */     this.bufPos = 1;
/*      */     
/*      */     while (true) {
/* 1206 */       char ch = charAt(++this.pos);
/*      */       
/* 1208 */       if (!CharTypes.isIdentifierChar(ch)) {
/*      */         break;
/*      */       }
/*      */       
/* 1212 */       this.bufPos++;
/*      */     } 
/*      */ 
/*      */     
/* 1216 */     this.ch = charAt(this.pos);
/*      */     
/* 1218 */     this.stringVal = addSymbol();
/* 1219 */     Token tok = this.keywods.getKeyword(this.stringVal);
/* 1220 */     if (tok != null) {
/* 1221 */       this.token = tok;
/*      */     } else {
/* 1223 */       this.token = Token.IDENTIFIER;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void scanNumber() {
/* 1228 */     this.mark = this.pos;
/*      */     
/* 1230 */     if (this.ch == '-') {
/* 1231 */       this.bufPos++;
/* 1232 */       this.ch = charAt(++this.pos);
/*      */     } 
/*      */ 
/*      */     
/* 1236 */     while (this.ch >= '0' && this.ch <= '9') {
/* 1237 */       this.bufPos++;
/*      */ 
/*      */ 
/*      */       
/* 1241 */       this.ch = charAt(++this.pos);
/*      */     } 
/*      */     
/* 1244 */     boolean isDouble = false;
/*      */     
/* 1246 */     if (this.ch == '.') {
/* 1247 */       if (charAt(this.pos + 1) == '.') {
/* 1248 */         this.token = Token.LITERAL_INT;
/*      */         return;
/*      */       } 
/* 1251 */       this.bufPos++;
/* 1252 */       this.ch = charAt(++this.pos);
/* 1253 */       isDouble = true;
/*      */ 
/*      */       
/* 1256 */       while (this.ch >= '0' && this.ch <= '9') {
/* 1257 */         this.bufPos++;
/*      */ 
/*      */ 
/*      */         
/* 1261 */         this.ch = charAt(++this.pos);
/*      */       } 
/*      */     } 
/*      */     
/* 1265 */     if (this.ch == 'e' || this.ch == 'E') {
/* 1266 */       this.bufPos++;
/* 1267 */       this.ch = charAt(++this.pos);
/*      */       
/* 1269 */       if (this.ch == '+' || this.ch == '-') {
/* 1270 */         this.bufPos++;
/* 1271 */         this.ch = charAt(++this.pos);
/*      */       } 
/*      */ 
/*      */       
/* 1275 */       while (this.ch >= '0' && this.ch <= '9') {
/* 1276 */         this.bufPos++;
/*      */ 
/*      */ 
/*      */         
/* 1280 */         this.ch = charAt(++this.pos);
/*      */       } 
/*      */       
/* 1283 */       isDouble = true;
/*      */     } 
/*      */     
/* 1286 */     if (isDouble) {
/* 1287 */       this.token = Token.LITERAL_FLOAT;
/*      */     } else {
/* 1289 */       this.token = Token.LITERAL_INT;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void scanHexaDecimal() {
/* 1294 */     this.mark = this.pos;
/*      */     
/* 1296 */     if (this.ch == '-') {
/* 1297 */       this.bufPos++;
/* 1298 */       this.ch = charAt(++this.pos);
/*      */     } 
/*      */ 
/*      */     
/* 1302 */     while (CharTypes.isHex(this.ch)) {
/* 1303 */       this.bufPos++;
/*      */ 
/*      */ 
/*      */       
/* 1307 */       this.ch = charAt(++this.pos);
/*      */     } 
/*      */     
/* 1310 */     this.token = Token.LITERAL_HEX;
/*      */   }
/*      */   
/*      */   public String hexString() {
/* 1314 */     return subString(this.mark, this.bufPos);
/*      */   }
/*      */   
/*      */   public final boolean isDigit(char ch) {
/* 1318 */     return (ch >= '0' && ch <= '9');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void putChar(char ch) {
/* 1325 */     if (this.bufPos == this.buf.length) {
/* 1326 */       char[] newsbuf = new char[this.buf.length * 2];
/* 1327 */       System.arraycopy(this.buf, 0, newsbuf, 0, this.buf.length);
/* 1328 */       this.buf = newsbuf;
/*      */     } 
/* 1330 */     this.buf[this.bufPos++] = ch;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int pos() {
/* 1338 */     return this.pos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String stringVal() {
/* 1345 */     return this.stringVal;
/*      */   }
/*      */   
/*      */   public final List<String> readAndResetComments() {
/* 1349 */     List<String> comments = this.comments;
/*      */     
/* 1351 */     this.comments = null;
/*      */     
/* 1353 */     return comments;
/*      */   }
/*      */   
/*      */   private boolean isOperator(char ch) {
/* 1357 */     switch (ch) {
/*      */       case '!':
/*      */       case '%':
/*      */       case '&':
/*      */       case '*':
/*      */       case '+':
/*      */       case '-':
/*      */       case ';':
/*      */       case '<':
/*      */       case '=':
/*      */       case '>':
/*      */       case '^':
/*      */       case '|':
/*      */       case '~':
/* 1371 */         return true;
/*      */     } 
/* 1373 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1380 */   private static final int[] digits = new int[58];
/*      */   
/*      */   static {
/* 1383 */     for (int i = 48; i <= 57; i++) {
/* 1384 */       digits[i] = i - 48;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public Number integerValue() {
/* 1390 */     long limit, result = 0L;
/* 1391 */     boolean negative = false;
/* 1392 */     int i = this.mark, max = this.mark + this.bufPos;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1397 */     if (charAt(this.mark) == '-') {
/* 1398 */       negative = true;
/* 1399 */       limit = Long.MIN_VALUE;
/* 1400 */       i++;
/*      */     } else {
/* 1402 */       limit = -9223372036854775807L;
/*      */     } 
/* 1404 */     long multmin = negative ? -922337203685477580L : -922337203685477580L;
/* 1405 */     if (i < max) {
/* 1406 */       int digit = digits[charAt(i++)];
/* 1407 */       result = -digit;
/*      */     } 
/* 1409 */     while (i < max) {
/*      */       
/* 1411 */       int digit = digits[charAt(i++)];
/* 1412 */       if (result < multmin) {
/* 1413 */         return new BigInteger(numberString());
/*      */       }
/* 1415 */       result *= 10L;
/* 1416 */       if (result < limit + digit) {
/* 1417 */         return new BigInteger(numberString());
/*      */       }
/* 1419 */       result -= digit;
/*      */     } 
/*      */     
/* 1422 */     if (negative) {
/* 1423 */       if (i > this.mark + 1) {
/* 1424 */         if (result >= -2147483648L) {
/* 1425 */           return Integer.valueOf((int)result);
/*      */         }
/* 1427 */         return Long.valueOf(result);
/*      */       } 
/* 1429 */       throw new NumberFormatException(numberString());
/*      */     } 
/*      */     
/* 1432 */     result = -result;
/* 1433 */     if (result <= 2147483647L) {
/* 1434 */       return Integer.valueOf((int)result);
/*      */     }
/* 1436 */     return Long.valueOf(result);
/*      */   }
/*      */ 
/*      */   
/*      */   public int bp() {
/* 1441 */     return this.pos;
/*      */   }
/*      */   
/*      */   public char current() {
/* 1445 */     return this.ch;
/*      */   }
/*      */   
/*      */   public void reset(int mark, char markChar, Token token) {
/* 1449 */     this.pos = mark;
/* 1450 */     this.ch = markChar;
/* 1451 */     this.token = token;
/*      */   }
/*      */   
/*      */   public final String numberString() {
/* 1455 */     return subString(this.mark, this.bufPos);
/*      */   }
/*      */   
/*      */   public BigDecimal decimalValue() {
/* 1459 */     String value = subString(this.mark, this.bufPos);
/* 1460 */     if (!StringUtils.isNumber(value)) {
/* 1461 */       throw new ParserException(value + " is not a number!");
/*      */     }
/* 1463 */     return new BigDecimal(value.toCharArray());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasComment() {
/* 1471 */     return (this.comments != null);
/*      */   }
/*      */   
/*      */   public int getCommentCount() {
/* 1475 */     return this.commentCount;
/*      */   }
/*      */   
/*      */   public void skipToEOF() {
/* 1479 */     this.pos = this.text.length();
/* 1480 */     this.token = Token.EOF;
/*      */   }
/*      */   
/*      */   public boolean isEndOfComment() {
/* 1484 */     return this.endOfComment;
/*      */   }
/*      */   
/*      */   protected boolean isSafeComment(String comment) {
/* 1488 */     if (comment == null) {
/* 1489 */       return true;
/*      */     }
/* 1491 */     comment = comment.toLowerCase();
/* 1492 */     if (comment.indexOf("select") != -1 || comment
/* 1493 */       .indexOf("delete") != -1 || comment
/* 1494 */       .indexOf("insert") != -1 || comment
/* 1495 */       .indexOf("update") != -1 || comment
/* 1496 */       .indexOf("into") != -1 || comment
/* 1497 */       .indexOf("where") != -1 || comment
/* 1498 */       .indexOf("or") != -1 || comment
/* 1499 */       .indexOf("and") != -1 || comment
/* 1500 */       .indexOf("union") != -1 || comment
/* 1501 */       .indexOf('\'') != -1 || comment
/* 1502 */       .indexOf('=') != -1 || comment
/* 1503 */       .indexOf('>') != -1 || comment
/* 1504 */       .indexOf('<') != -1 || comment
/* 1505 */       .indexOf('&') != -1 || comment
/* 1506 */       .indexOf('|') != -1 || comment
/* 1507 */       .indexOf('^') != -1)
/*      */     {
/* 1509 */       return false;
/*      */     }
/* 1511 */     return true;
/*      */   }
/*      */   
/*      */   protected void addComment(String comment) {
/* 1515 */     if (this.comments == null) {
/* 1516 */       this.comments = new ArrayList<>(2);
/*      */     }
/* 1518 */     this.comments.add(this.stringVal);
/*      */   }
/*      */   
/*      */   public int getLine() {
/* 1522 */     return this.line;
/*      */   }
/*      */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\parser\Lexer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */