/*     */ package com.tranboot.client.druid.sql.dialect.mysql.parser;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.parser.*;

import java.util.HashMap;
import java.util.Map;

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
/*     */ public class MySqlLexer
/*     */   extends Lexer
/*     */ {
/*     */   public static final Keywords DEFAULT_MYSQL_KEYWORDS;
/*     */   
/*     */   static {
/*  35 */     Map<String, Token> map = new HashMap<>();
/*     */     
/*  37 */     map.putAll(Keywords.DEFAULT_KEYWORDS.getKeywords());
/*     */     
/*  39 */     map.put("DUAL", Token.DUAL);
/*  40 */     map.put("FALSE", Token.FALSE);
/*  41 */     map.put("IDENTIFIED", Token.IDENTIFIED);
/*  42 */     map.put("IF", Token.IF);
/*  43 */     map.put("KILL", Token.KILL);
/*     */     
/*  45 */     map.put("LIMIT", Token.LIMIT);
/*  46 */     map.put("TRUE", Token.TRUE);
/*  47 */     map.put("BINARY", Token.BINARY);
/*  48 */     map.put("SHOW", Token.SHOW);
/*  49 */     map.put("CACHE", Token.CACHE);
/*  50 */     map.put("ANALYZE", Token.ANALYZE);
/*  51 */     map.put("OPTIMIZE", Token.OPTIMIZE);
/*  52 */     map.put("ROW", Token.ROW);
/*  53 */     map.put("BEGIN", Token.BEGIN);
/*  54 */     map.put("END", Token.END);
/*  55 */     map.put("DIV", Token.DIV);
/*     */ 
/*     */     
/*  58 */     map.put("PARTITION", Token.PARTITION);
/*     */     
/*  60 */     map.put("CONTINUE", Token.CONTINUE);
/*  61 */     map.put("UNDO", Token.UNDO);
/*  62 */     map.put("SQLSTATE", Token.SQLSTATE);
/*  63 */     map.put("CONDITION", Token.CONDITION);
/*     */     
/*  65 */     DEFAULT_MYSQL_KEYWORDS = new Keywords(map);
/*     */   }
/*     */   
/*     */   public MySqlLexer(char[] input, int inputLength, boolean skipComment) {
/*  69 */     super(input, inputLength, skipComment);
/*  70 */     this.keywods = DEFAULT_MYSQL_KEYWORDS;
/*     */   }
/*     */   
/*     */   public MySqlLexer(String input) {
/*  74 */     super(input);
/*  75 */     this.keywods = DEFAULT_MYSQL_KEYWORDS;
/*     */   }
/*     */   
/*     */   public void scanSharp() {
/*  79 */     if (this.ch != '#') {
/*  80 */       throw new ParserException("illegal stat");
/*     */     }
/*     */     
/*  83 */     if (charAt(this.pos + 1) == '{') {
/*  84 */       scanVariable();
/*     */       
/*     */       return;
/*     */     } 
/*  88 */     Token lastToken = this.token;
/*     */     
/*  90 */     scanChar();
/*  91 */     this.mark = this.pos;
/*  92 */     this.bufPos = 0;
/*     */     while (true) {
/*  94 */       if (this.ch == '\r') {
/*  95 */         if (charAt(this.pos + 1) == '\n') {
/*  96 */           this.bufPos += 2;
/*  97 */           scanChar();
/*     */           break;
/*     */         } 
/* 100 */         this.bufPos++; break;
/*     */       } 
/* 102 */       if (this.ch == '\032') {
/*     */         break;
/*     */       }
/*     */       
/* 106 */       if (this.ch == '\n') {
/* 107 */         scanChar();
/* 108 */         this.bufPos++;
/*     */         
/*     */         break;
/*     */       } 
/* 112 */       scanChar();
/* 113 */       this.bufPos++;
/*     */     } 
/*     */     
/* 116 */     this.stringVal = subString(this.mark - 1, this.bufPos + 1);
/* 117 */     this.token = Token.LINE_COMMENT;
/* 118 */     this.commentCount++;
/* 119 */     if (this.keepComments) {
/* 120 */       addComment(this.stringVal);
/*     */     }
/*     */     
/* 123 */     if (this.commentHandler != null && this.commentHandler.handle(lastToken, this.stringVal)) {
/*     */       return;
/*     */     }
/*     */     
/* 127 */     this.endOfComment = isEOF();
/*     */     
/* 129 */     if (!isAllowComment() && (isEOF() || !isSafeComment(this.stringVal))) {
/* 130 */       throw new NotAllowCommentException();
/*     */     }
/*     */   }
/*     */   
/*     */   public void scanVariable() {
/* 135 */     if (this.ch != '@' && this.ch != ':' && this.ch != '#' && this.ch != '$') {
/* 136 */       throw new ParserException("illegal variable");
/*     */     }
/*     */     
/* 139 */     this.mark = this.pos;
/* 140 */     this.bufPos = 1;
/*     */     
/* 142 */     if (charAt(this.pos + 1) == '@') {
/* 143 */       this.ch = charAt(++this.pos);
/* 144 */       this.bufPos++;
/*     */     } 
/*     */     
/* 147 */     if (charAt(this.pos + 1) == '`') {
/* 148 */       this.pos++;
/* 149 */       this.bufPos++;
/*     */       
/*     */       while (true) {
/* 152 */         char ch = charAt(++this.pos);
/*     */         
/* 154 */         if (ch == '`') {
/* 155 */           this.bufPos++;
/* 156 */           ch = charAt(++this.pos); break;
/*     */         } 
/* 158 */         if (ch == '\032') {
/* 159 */           throw new ParserException("illegal identifier");
/*     */         }
/*     */         
/* 162 */         this.bufPos++;
/*     */       } 
/*     */ 
/*     */       
/* 166 */       this.ch = charAt(this.pos);
/*     */       
/* 168 */       this.stringVal = subString(this.mark, this.bufPos);
/* 169 */       this.token = Token.VARIANT;
/* 170 */     } else if (charAt(this.pos + 1) == '{') {
/* 171 */       this.pos++;
/* 172 */       this.bufPos++;
/*     */       
/*     */       while (true) {
/* 175 */         char ch = charAt(++this.pos);
/*     */         
/* 177 */         if (ch == '}') {
/* 178 */           this.bufPos++;
/* 179 */           ch = charAt(++this.pos); break;
/*     */         } 
/* 181 */         if (ch == '\032') {
/* 182 */           throw new ParserException("illegal identifier");
/*     */         }
/*     */         
/* 185 */         this.bufPos++;
/*     */       } 
/*     */ 
/*     */       
/* 189 */       this.ch = charAt(this.pos);
/*     */       
/* 191 */       this.stringVal = subString(this.mark, this.bufPos);
/* 192 */       this.token = Token.VARIANT;
/*     */     } else {
/*     */       while (true) {
/* 195 */         this.ch = charAt(++this.pos);
/*     */         
/* 197 */         if (!isIdentifierChar(this.ch)) {
/*     */           break;
/*     */         }
/*     */         
/* 201 */         this.bufPos++;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 206 */     this.ch = charAt(this.pos);
/*     */     
/* 208 */     this.stringVal = subString(this.mark, this.bufPos);
/* 209 */     this.token = Token.VARIANT;
/*     */   }
/*     */   
/*     */   public void scanIdentifier() {
/* 213 */     char first = this.ch;
/*     */     
/* 215 */     if (this.ch == '`') {
/*     */       
/* 217 */       this.mark = this.pos;
/* 218 */       this.bufPos = 1;
/*     */       
/*     */       while (true) {
/* 221 */         char ch = charAt(++this.pos);
/*     */         
/* 223 */         if (ch == '`') {
/* 224 */           this.bufPos++;
/* 225 */           ch = charAt(++this.pos); break;
/*     */         } 
/* 227 */         if (ch == '\032') {
/* 228 */           throw new ParserException("illegal identifier");
/*     */         }
/*     */         
/* 231 */         this.bufPos++;
/*     */       } 
/*     */ 
/*     */       
/* 235 */       this.ch = charAt(this.pos);
/*     */       
/* 237 */       this.stringVal = subString(this.mark, this.bufPos);
/* 238 */       Token tok = this.keywods.getKeyword(this.stringVal);
/* 239 */       if (tok != null) {
/* 240 */         this.token = tok;
/*     */       } else {
/* 242 */         this.token = Token.IDENTIFIER;
/*     */       } 
/*     */     } else {
/*     */       
/* 246 */       boolean firstFlag = CharTypes.isFirstIdentifierChar(first);
/* 247 */       if (!firstFlag) {
/* 248 */         throw new ParserException("illegal identifier");
/*     */       }
/*     */       
/* 251 */       this.mark = this.pos;
/* 252 */       this.bufPos = 1;
/* 253 */       char ch = Character.MIN_VALUE;
/*     */       while (true) {
/* 255 */         char last_ch = ch;
/* 256 */         ch = charAt(++this.pos);
/*     */         
/* 258 */         if (!isIdentifierChar(ch)) {
/* 259 */           if (ch == '-' && this.pos < this.text.length() - 1) {
/* 260 */             if (this.mark > 0 && this.text.charAt(this.mark - 1) == '.') {
/*     */               break;
/*     */             }
/*     */             
/* 264 */             char next_char = this.text.charAt(this.pos + 1);
/* 265 */             if (isIdentifierChar(next_char)) {
/* 266 */               this.bufPos++;
/*     */               continue;
/*     */             } 
/*     */           } 
/* 270 */           if (last_ch == '-' && charAt(this.pos - 2) != '-') {
/* 271 */             ch = last_ch;
/* 272 */             this.bufPos--;
/* 273 */             this.pos--;
/*     */           } 
/*     */           
/*     */           break;
/*     */         } 
/* 278 */         this.bufPos++;
/*     */       } 
/*     */ 
/*     */       
/* 282 */       this.ch = charAt(this.pos);
/*     */       
/* 284 */       this.stringVal = addSymbol();
/* 285 */       Token tok = this.keywods.getKeyword(this.stringVal);
/* 286 */       if (tok != null) {
/* 287 */         this.token = tok;
/*     */       } else {
/* 289 */         this.token = Token.IDENTIFIER;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final void scanString() {
/* 295 */     scanString2();
/*     */   }
/*     */   
/*     */   public void scanComment() {
/* 299 */     Token lastToken = this.token;
/*     */     
/* 301 */     if (this.ch == '-') {
/* 302 */       char next_2 = charAt(this.pos + 2);
/* 303 */       if (isDigit(next_2)) {
/* 304 */         scanChar();
/* 305 */         this.token = Token.SUB;
/*     */         return;
/*     */       } 
/* 308 */     } else if (this.ch != '/') {
/* 309 */       throw new IllegalStateException();
/*     */     } 
/*     */     
/* 312 */     this.mark = this.pos;
/* 313 */     this.bufPos = 0;
/* 314 */     scanChar();
/*     */ 
/*     */     
/* 317 */     if (this.ch == '*') {
/* 318 */       scanChar();
/* 319 */       this.bufPos++;
/*     */       
/* 321 */       while (this.ch == ' ') {
/* 322 */         scanChar();
/* 323 */         this.bufPos++;
/*     */       } 
/*     */       
/* 326 */       boolean isHint = false;
/* 327 */       int startHintSp = this.bufPos + 1;
/* 328 */       if (this.ch == '!' || this.ch == '+') {
/*     */ 
/*     */         
/* 331 */         isHint = true;
/* 332 */         scanChar();
/* 333 */         this.bufPos++;
/*     */       } 
/*     */       
/*     */       while (true) {
/* 337 */         if (this.ch == '\032') {
/* 338 */           this.token = Token.ERROR;
/*     */           return;
/*     */         } 
/* 341 */         if (this.ch == '*' && charAt(this.pos + 1) == '/') {
/* 342 */           this.bufPos += 3;
/* 343 */           scanChar();
/* 344 */           scanChar();
/*     */           
/*     */           break;
/*     */         } 
/* 348 */         scanChar();
/* 349 */         this.bufPos++;
/*     */       } 
/*     */       
/* 352 */       if (isHint) {
/* 353 */         this.stringVal = subString(this.mark + startHintSp, this.bufPos - startHintSp - 2);
/* 354 */         this.token = Token.HINT;
/*     */       } else {
/* 356 */         this.stringVal = subString(this.mark, this.bufPos);
/* 357 */         this.token = Token.MULTI_LINE_COMMENT;
/* 358 */         this.commentCount++;
/* 359 */         if (this.keepComments) {
/* 360 */           addComment(this.stringVal);
/*     */         }
/*     */       } 
/*     */       
/* 364 */       this.endOfComment = isEOF();
/*     */       
/* 366 */       if (this.commentHandler != null && this.commentHandler.handle(lastToken, this.stringVal)) {
/*     */         return;
/*     */       }
/*     */       
/* 370 */       if (!isHint && !isAllowComment() && !isSafeComment(this.stringVal)) {
/* 371 */         throw new NotAllowCommentException();
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 376 */     if (this.ch == '/' || this.ch == '-') {
/* 377 */       scanChar();
/* 378 */       this.bufPos++;
/*     */       
/*     */       while (true) {
/* 381 */         if (this.ch == '\r') {
/* 382 */           if (charAt(this.pos + 1) == '\n') {
/* 383 */             this.bufPos += 2;
/* 384 */             scanChar();
/*     */             break;
/*     */           } 
/* 387 */           this.bufPos++; break;
/*     */         } 
/* 389 */         if (this.ch == '\032') {
/*     */           break;
/*     */         }
/*     */         
/* 393 */         if (this.ch == '\n') {
/* 394 */           scanChar();
/* 395 */           this.bufPos++;
/*     */           
/*     */           break;
/*     */         } 
/* 399 */         scanChar();
/* 400 */         this.bufPos++;
/*     */       } 
/*     */       
/* 403 */       this.stringVal = subString(this.mark, this.bufPos + 1);
/* 404 */       this.token = Token.LINE_COMMENT;
/* 405 */       this.commentCount++;
/* 406 */       if (this.keepComments) {
/* 407 */         addComment(this.stringVal);
/*     */       }
/*     */       
/* 410 */       if (this.commentHandler != null && this.commentHandler.handle(lastToken, this.stringVal)) {
/*     */         return;
/*     */       }
/*     */       
/* 414 */       this.endOfComment = isEOF();
/*     */       
/* 416 */       if (!isAllowComment() && (isEOF() || !isSafeComment(this.stringVal))) {
/* 417 */         throw new NotAllowCommentException();
/*     */       }
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 424 */   private static final boolean[] identifierFlags = new boolean[256];
/*     */   static {
/* 426 */     for (char c = Character.MIN_VALUE; c < identifierFlags.length; c = (char)(c + 1)) {
/* 427 */       if (c >= 'A' && c <= 'Z') {
/* 428 */         identifierFlags[c] = true;
/* 429 */       } else if (c >= 'a' && c <= 'z') {
/* 430 */         identifierFlags[c] = true;
/* 431 */       } else if (c >= '0' && c <= '9') {
/* 432 */         identifierFlags[c] = true;
/*     */       } 
/*     */     } 
/*     */     
/* 436 */     identifierFlags[95] = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isIdentifierChar(char c) {
/* 441 */     if (c <= identifierFlags.length) {
/* 442 */       return identifierFlags[c];
/*     */     }
/* 444 */     return (c != '　' && c != '，');
/*     */   }
/*     */   
/*     */   public void scanNumber() {
/* 448 */     this.mark = this.pos;
/*     */     
/* 450 */     if (this.ch == '-') {
/* 451 */       this.bufPos++;
/* 452 */       this.ch = charAt(++this.pos);
/*     */     } 
/*     */ 
/*     */     
/* 456 */     while (this.ch >= '0' && this.ch <= '9') {
/* 457 */       this.bufPos++;
/*     */ 
/*     */ 
/*     */       
/* 461 */       this.ch = charAt(++this.pos);
/*     */     } 
/*     */     
/* 464 */     boolean isDouble = false;
/*     */     
/* 466 */     if (this.ch == '.') {
/* 467 */       if (charAt(this.pos + 1) == '.') {
/* 468 */         this.token = Token.LITERAL_INT;
/*     */         return;
/*     */       } 
/* 471 */       this.bufPos++;
/* 472 */       this.ch = charAt(++this.pos);
/* 473 */       isDouble = true;
/*     */ 
/*     */       
/* 476 */       while (this.ch >= '0' && this.ch <= '9') {
/* 477 */         this.bufPos++;
/*     */ 
/*     */ 
/*     */         
/* 481 */         this.ch = charAt(++this.pos);
/*     */       } 
/*     */     } 
/*     */     
/* 485 */     if (this.ch == 'e' || this.ch == 'E') {
/* 486 */       this.bufPos++;
/* 487 */       this.ch = charAt(++this.pos);
/*     */       
/* 489 */       if (this.ch == '+' || this.ch == '-') {
/* 490 */         this.bufPos++;
/* 491 */         this.ch = charAt(++this.pos);
/*     */       } 
/*     */ 
/*     */       
/* 495 */       while (this.ch >= '0' && this.ch <= '9') {
/* 496 */         this.bufPos++;
/*     */ 
/*     */ 
/*     */         
/* 500 */         this.ch = charAt(++this.pos);
/*     */       } 
/*     */       
/* 503 */       isDouble = true;
/*     */     } 
/*     */     
/* 506 */     if (isDouble) {
/* 507 */       this.token = Token.LITERAL_FLOAT;
/*     */     }
/* 509 */     else if (CharTypes.isFirstIdentifierChar(this.ch) && (this.ch != 'b' || this.bufPos != 1 || charAt(this.pos - 1) != '0')) {
/* 510 */       this.bufPos++;
/*     */       while (true) {
/* 512 */         this.ch = charAt(++this.pos);
/*     */         
/* 514 */         if (!isIdentifierChar(this.ch)) {
/*     */           break;
/*     */         }
/*     */         
/* 518 */         this.bufPos++;
/*     */       } 
/*     */ 
/*     */       
/* 522 */       this.stringVal = addSymbol();
/* 523 */       this.token = Token.IDENTIFIER;
/*     */     } else {
/* 525 */       this.token = Token.LITERAL_INT;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\parser\MySqlLexer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */