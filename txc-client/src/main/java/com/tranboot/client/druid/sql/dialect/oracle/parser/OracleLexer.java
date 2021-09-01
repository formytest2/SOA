/*     */ package com.tranboot.client.druid.sql.dialect.oracle.parser;
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
/*     */ public class OracleLexer
/*     */   extends Lexer
/*     */ {
/*     */   public static final Keywords DEFAULT_ORACLE_KEYWORDS;
/*     */   
/*     */   static {
/*  35 */     Map<String, Token> map = new HashMap<>();
/*     */     
/*  37 */     map.putAll(Keywords.DEFAULT_KEYWORDS.getKeywords());
/*     */     
/*  39 */     map.put("BEGIN", Token.BEGIN);
/*  40 */     map.put("COMMENT", Token.COMMENT);
/*  41 */     map.put("COMMIT", Token.COMMIT);
/*  42 */     map.put("CONNECT", Token.CONNECT);
/*     */     
/*  44 */     map.put("CROSS", Token.CROSS);
/*  45 */     map.put("CURSOR", Token.CURSOR);
/*  46 */     map.put("DECLARE", Token.DECLARE);
/*  47 */     map.put("ERRORS", Token.ERRORS);
/*  48 */     map.put("EXCEPTION", Token.EXCEPTION);
/*     */     
/*  50 */     map.put("EXCLUSIVE", Token.EXCLUSIVE);
/*  51 */     map.put("EXTRACT", Token.EXTRACT);
/*  52 */     map.put("GOTO", Token.GOTO);
/*  53 */     map.put("IF", Token.IF);
/*     */     
/*  55 */     map.put("LIMIT", Token.LIMIT);
/*  56 */     map.put("LOOP", Token.LOOP);
/*  57 */     map.put("MATCHED", Token.MATCHED);
/*  58 */     map.put("MERGE", Token.MERGE);
/*     */     
/*  60 */     map.put("MODE", Token.MODE);
/*  61 */     map.put("MODEL", Token.MODEL);
/*  62 */     map.put("NOWAIT", Token.NOWAIT);
/*  63 */     map.put("OF", Token.OF);
/*  64 */     map.put("PRIOR", Token.PRIOR);
/*     */     
/*  66 */     map.put("REJECT", Token.REJECT);
/*  67 */     map.put("RETURNING", Token.RETURNING);
/*  68 */     map.put("SAVEPOINT", Token.SAVEPOINT);
/*  69 */     map.put("SESSION", Token.SESSION);
/*     */     
/*  71 */     map.put("SHARE", Token.SHARE);
/*  72 */     map.put("START", Token.START);
/*  73 */     map.put("SYSDATE", Token.SYSDATE);
/*  74 */     map.put("UNLIMITED", Token.UNLIMITED);
/*  75 */     map.put("USING", Token.USING);
/*     */     
/*  77 */     map.put("WAIT", Token.WAIT);
/*  78 */     map.put("WITH", Token.WITH);
/*     */     
/*  80 */     map.put("IDENTIFIED", Token.IDENTIFIED);
/*     */     
/*  82 */     map.put("PCTFREE", Token.PCTFREE);
/*  83 */     map.put("INITRANS", Token.INITRANS);
/*  84 */     map.put("MAXTRANS", Token.MAXTRANS);
/*  85 */     map.put("SEGMENT", Token.SEGMENT);
/*  86 */     map.put("CREATION", Token.CREATION);
/*  87 */     map.put("IMMEDIATE", Token.IMMEDIATE);
/*  88 */     map.put("DEFERRED", Token.DEFERRED);
/*  89 */     map.put("STORAGE", Token.STORAGE);
/*  90 */     map.put("NEXT", Token.NEXT);
/*  91 */     map.put("MINEXTENTS", Token.MINEXTENTS);
/*  92 */     map.put("MAXEXTENTS", Token.MAXEXTENTS);
/*  93 */     map.put("MAXSIZE", Token.MAXSIZE);
/*  94 */     map.put("PCTINCREASE", Token.PCTINCREASE);
/*  95 */     map.put("FLASH_CACHE", Token.FLASH_CACHE);
/*  96 */     map.put("CELL_FLASH_CACHE", Token.CELL_FLASH_CACHE);
/*  97 */     map.put("KEEP", Token.KEEP);
/*  98 */     map.put("NONE", Token.NONE);
/*  99 */     map.put("LOB", Token.LOB);
/* 100 */     map.put("STORE", Token.STORE);
/* 101 */     map.put("ROW", Token.ROW);
/* 102 */     map.put("CHUNK", Token.CHUNK);
/* 103 */     map.put("CACHE", Token.CACHE);
/* 104 */     map.put("NOCACHE", Token.NOCACHE);
/* 105 */     map.put("LOGGING", Token.LOGGING);
/* 106 */     map.put("NOCOMPRESS", Token.NOCOMPRESS);
/* 107 */     map.put("KEEP_DUPLICATES", Token.KEEP_DUPLICATES);
/* 108 */     map.put("EXCEPTIONS", Token.EXCEPTIONS);
/* 109 */     map.put("PURGE", Token.PURGE);
/* 110 */     map.put("INITIALLY", Token.INITIALLY);
/*     */     
/* 112 */     map.put("FETCH", Token.FETCH);
/*     */     
/* 114 */     DEFAULT_ORACLE_KEYWORDS = new Keywords(map);
/*     */   }
/*     */   
/*     */   public OracleLexer(char[] input, int inputLength, boolean skipComment) {
/* 118 */     super(input, inputLength, skipComment);
/* 119 */     this.keywods = DEFAULT_ORACLE_KEYWORDS;
/*     */   }
/*     */   
/*     */   public OracleLexer(String input) {
/* 123 */     super(input);
/* 124 */     this.keywods = DEFAULT_ORACLE_KEYWORDS;
/*     */   }
/*     */   public void scanVariable() {
/*     */     char ch;
/* 128 */     if (this.ch == '@') {
/* 129 */       scanChar();
/* 130 */       this.token = Token.MONKEYS_AT;
/*     */       
/*     */       return;
/*     */     } 
/* 134 */     if (this.ch != ':' && this.ch != '#' && this.ch != '$') {
/* 135 */       throw new ParserException("illegal variable");
/*     */     }
/*     */     
/* 138 */     this.mark = this.pos;
/* 139 */     this.bufPos = 1;
/*     */ 
/*     */     
/* 142 */     boolean quoteFlag = false;
/* 143 */     boolean mybatisFlag = false;
/* 144 */     if (charAt(this.pos + 1) == '"') {
/* 145 */       this.pos++;
/* 146 */       this.bufPos++;
/* 147 */       quoteFlag = true;
/* 148 */     } else if (charAt(this.pos + 1) == '{') {
/* 149 */       this.pos++;
/* 150 */       this.bufPos++;
/* 151 */       mybatisFlag = true;
/*     */     } 
/*     */     
/*     */     while (true) {
/* 155 */       ch = charAt(++this.pos);
/*     */       
/* 157 */       if (!CharTypes.isIdentifierChar(ch)) {
/*     */         break;
/*     */       }
/*     */       
/* 161 */       this.bufPos++;
/*     */     } 
/*     */ 
/*     */     
/* 165 */     if (quoteFlag) {
/* 166 */       if (ch != '"') {
/* 167 */         throw new ParserException("syntax error");
/*     */       }
/* 169 */       this.pos++;
/* 170 */       this.bufPos++;
/* 171 */     } else if (mybatisFlag) {
/* 172 */       if (ch != '}') {
/* 173 */         throw new ParserException("syntax error");
/*     */       }
/* 175 */       this.pos++;
/* 176 */       this.bufPos++;
/*     */     } 
/*     */     
/* 179 */     this.ch = charAt(this.pos);
/*     */     
/* 181 */     this.stringVal = addSymbol();
/* 182 */     Token tok = this.keywods.getKeyword(this.stringVal);
/* 183 */     if (tok != null) {
/* 184 */       this.token = tok;
/*     */     } else {
/* 186 */       this.token = Token.VARIANT;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void scanComment() {
/* 191 */     if (this.ch != '/' && this.ch != '-') {
/* 192 */       throw new IllegalStateException();
/*     */     }
/*     */     
/* 195 */     this.mark = this.pos;
/* 196 */     this.bufPos = 0;
/* 197 */     scanChar();
/*     */ 
/*     */     
/* 200 */     if (this.ch == '*') {
/* 201 */       scanChar();
/* 202 */       this.bufPos++;
/*     */       
/* 204 */       while (this.ch == ' ') {
/* 205 */         scanChar();
/* 206 */         this.bufPos++;
/*     */       } 
/*     */       
/* 209 */       boolean isHint = false;
/* 210 */       int startHintSp = this.bufPos + 1;
/* 211 */       if (this.ch == '+') {
/* 212 */         isHint = true;
/* 213 */         scanChar();
/* 214 */         this.bufPos++;
/*     */       } 
/*     */       
/*     */       while (true) {
/* 218 */         if (this.ch == '*' && charAt(this.pos + 1) == '/') {
/* 219 */           this.bufPos += 2;
/* 220 */           scanChar();
/* 221 */           scanChar();
/*     */           
/*     */           break;
/*     */         } 
/* 225 */         scanChar();
/* 226 */         this.bufPos++;
/*     */       } 
/*     */       
/* 229 */       if (isHint) {
/* 230 */         this.stringVal = subString(this.mark + startHintSp, this.bufPos - startHintSp - 1);
/* 231 */         this.token = Token.HINT;
/*     */       } else {
/* 233 */         this.stringVal = subString(this.mark, this.bufPos);
/* 234 */         this.token = Token.MULTI_LINE_COMMENT;
/* 235 */         this.commentCount++;
/* 236 */         if (this.keepComments) {
/* 237 */           addComment(this.stringVal);
/*     */         }
/*     */       } 
/*     */       
/* 241 */       if (this.token != Token.HINT && !isAllowComment()) {
/* 242 */         throw new NotAllowCommentException();
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 248 */     if (!isAllowComment()) {
/* 249 */       throw new NotAllowCommentException();
/*     */     }
/*     */     
/* 252 */     if (this.ch == '/' || this.ch == '-') {
/* 253 */       scanChar();
/* 254 */       this.bufPos++;
/*     */       
/*     */       while (true) {
/* 257 */         if (this.ch == '\r') {
/* 258 */           if (charAt(this.pos + 1) == '\n') {
/* 259 */             this.bufPos += 2;
/* 260 */             scanChar();
/*     */             break;
/*     */           } 
/* 263 */           this.bufPos++; break;
/*     */         } 
/* 265 */         if (this.ch == '\032') {
/*     */           break;
/*     */         }
/*     */         
/* 269 */         if (this.ch == '\n') {
/* 270 */           scanChar();
/* 271 */           this.bufPos++;
/*     */           
/*     */           break;
/*     */         } 
/* 275 */         scanChar();
/* 276 */         this.bufPos++;
/*     */       } 
/*     */       
/* 279 */       this.stringVal = subString(this.mark + 1, this.bufPos);
/* 280 */       this.token = Token.LINE_COMMENT;
/* 281 */       this.commentCount++;
/* 282 */       if (this.keepComments) {
/* 283 */         addComment(this.stringVal);
/*     */       }
/* 285 */       this.endOfComment = isEOF();
/*     */       return;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void scanNumber() {
/* 291 */     this.mark = this.pos;
/*     */     
/* 293 */     if (this.ch == '-') {
/* 294 */       this.bufPos++;
/* 295 */       this.ch = charAt(++this.pos);
/*     */     } 
/*     */ 
/*     */     
/* 299 */     while (this.ch >= '0' && this.ch <= '9') {
/* 300 */       this.bufPos++;
/*     */ 
/*     */ 
/*     */       
/* 304 */       this.ch = charAt(++this.pos);
/*     */     } 
/*     */     
/* 307 */     boolean isDouble = false;
/*     */     
/* 309 */     if (this.ch == '.') {
/* 310 */       if (charAt(this.pos + 1) == '.') {
/* 311 */         this.token = Token.LITERAL_INT;
/*     */         return;
/*     */       } 
/* 314 */       this.bufPos++;
/* 315 */       this.ch = charAt(++this.pos);
/* 316 */       isDouble = true;
/*     */ 
/*     */       
/* 319 */       while (this.ch >= '0' && this.ch <= '9') {
/* 320 */         this.bufPos++;
/*     */ 
/*     */ 
/*     */         
/* 324 */         this.ch = charAt(++this.pos);
/*     */       } 
/*     */     } 
/*     */     
/* 328 */     if (this.ch == 'e' || this.ch == 'E') {
/* 329 */       this.bufPos++;
/* 330 */       this.ch = charAt(++this.pos);
/*     */       
/* 332 */       if (this.ch == '+' || this.ch == '-') {
/* 333 */         this.bufPos++;
/* 334 */         this.ch = charAt(++this.pos);
/*     */       } 
/*     */ 
/*     */       
/* 338 */       while (this.ch >= '0' && this.ch <= '9') {
/* 339 */         this.bufPos++;
/*     */ 
/*     */ 
/*     */         
/* 343 */         this.ch = charAt(++this.pos);
/*     */       } 
/*     */       
/* 346 */       isDouble = true;
/*     */     } 
/*     */     
/* 349 */     if (this.ch == 'f' || this.ch == 'F') {
/* 350 */       this.token = Token.BINARY_FLOAT;
/* 351 */       scanChar();
/*     */       
/*     */       return;
/*     */     } 
/* 355 */     if (this.ch == 'd' || this.ch == 'D') {
/* 356 */       this.token = Token.BINARY_DOUBLE;
/* 357 */       scanChar();
/*     */       
/*     */       return;
/*     */     } 
/* 361 */     if (isDouble) {
/* 362 */       this.token = Token.LITERAL_FLOAT;
/*     */     } else {
/* 364 */       this.token = Token.LITERAL_INT;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\parser\OracleLexer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */