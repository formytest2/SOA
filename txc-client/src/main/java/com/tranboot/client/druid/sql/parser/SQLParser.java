/*     */ package com.tranboot.client.druid.sql.parser;
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
/*     */ public class SQLParser
/*     */ {
/*     */   protected final Lexer lexer;
/*     */   protected String dbType;
/*     */   private int errorEndPos;
/*     */   
/*     */   public SQLParser(String sql, String dbType) {
/*  25 */     this(new Lexer(sql, null, dbType), dbType);
/*  26 */     this.lexer.nextToken();
/*     */   }
/*     */   
/*     */   public SQLParser(String sql) {
/*  30 */     this(sql, (String)null);
/*     */   }
/*     */   
/*     */   public SQLParser(Lexer lexer) {
/*  34 */     this(lexer, (String)null);
/*     */   }
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
/*     */   public SQLParser(Lexer lexer, String dbType) {
/* 258 */     this.errorEndPos = -1;
/*     */     this.lexer = lexer;
/*     */     this.dbType = dbType; } protected void setErrorEndPos(int errPos) {
/* 261 */     if (errPos > this.errorEndPos)
/* 262 */       this.errorEndPos = errPos; 
/*     */   }
/*     */   
/*     */   public final Lexer getLexer() {
/*     */     return this.lexer;
/*     */   }
/*     */   
/*     */   public String getDbType() {
/*     */     return this.dbType;
/*     */   }
/*     */   
/*     */   protected boolean identifierEquals(String text) {
/*     */     return (this.lexer.token() == Token.IDENTIFIER && this.lexer.stringVal().equalsIgnoreCase(text));
/*     */   }
/*     */   
/*     */   protected void acceptIdentifier(String text) {
/*     */     if (identifierEquals(text)) {
/*     */       this.lexer.nextToken();
/*     */     } else {
/*     */       setErrorEndPos(this.lexer.pos());
/*     */       throw new ParserException("syntax error, expect " + text + ", actual " + this.lexer.token());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected String as() {
/*     */     String alias = null;
/*     */     if (this.lexer.token() == Token.AS) {
/*     */       this.lexer.nextToken();
/*     */       alias = alias();
/*     */       if (alias != null) {
/*     */         while (this.lexer.token() == Token.DOT) {
/*     */           this.lexer.nextToken();
/*     */           alias = alias + '.' + this.lexer.token().name();
/*     */           this.lexer.nextToken();
/*     */         } 
/*     */         return alias;
/*     */       } 
/*     */       if (this.lexer.token() == Token.LPAREN)
/*     */         return null; 
/*     */       throw new ParserException("Error : " + this.lexer.token());
/*     */     } 
/*     */     if (this.lexer.token() == Token.LITERAL_ALIAS) {
/*     */       alias = '"' + this.lexer.stringVal() + '"';
/*     */       this.lexer.nextToken();
/*     */     } else if (this.lexer.token() == Token.IDENTIFIER) {
/*     */       alias = this.lexer.stringVal();
/*     */       this.lexer.nextToken();
/*     */     } else if (this.lexer.token() == Token.LITERAL_CHARS) {
/*     */       alias = "'" + this.lexer.stringVal() + "'";
/*     */       this.lexer.nextToken();
/*     */     } else if (this.lexer.token() == Token.CASE) {
/*     */       alias = this.lexer.token.name();
/*     */       this.lexer.nextToken();
/*     */     } else if (this.lexer.token() == Token.USER) {
/*     */       alias = this.lexer.stringVal();
/*     */       this.lexer.nextToken();
/*     */     } else if (this.lexer.token() == Token.END) {
/*     */       alias = this.lexer.stringVal();
/*     */       this.lexer.nextToken();
/*     */     } 
/*     */     switch (this.lexer.token()) {
/*     */       case KEY:
/*     */       case INTERVAL:
/*     */       case CONSTRAINT:
/*     */         alias = this.lexer.token().name();
/*     */         this.lexer.nextToken();
/*     */         return alias;
/*     */     } 
/*     */     return alias;
/*     */   }
/*     */   
/*     */   protected String alias() {
/*     */     String alias = null;
/*     */     if (this.lexer.token() == Token.LITERAL_ALIAS) {
/*     */       alias = '"' + this.lexer.stringVal() + '"';
/*     */       this.lexer.nextToken();
/*     */     } else if (this.lexer.token() == Token.IDENTIFIER) {
/*     */       alias = this.lexer.stringVal();
/*     */       this.lexer.nextToken();
/*     */     } else if (this.lexer.token() == Token.LITERAL_CHARS) {
/*     */       alias = "'" + this.lexer.stringVal() + "'";
/*     */       this.lexer.nextToken();
/*     */     } else {
/*     */       switch (this.lexer.token()) {
/*     */         case KEY:
/*     */         case INTERVAL:
/*     */         case INDEX:
/*     */         case CASE:
/*     */         case MODEL:
/*     */         case PCTFREE:
/*     */         case INITRANS:
/*     */         case MAXTRANS:
/*     */         case SEGMENT:
/*     */         case CREATION:
/*     */         case IMMEDIATE:
/*     */         case DEFERRED:
/*     */         case STORAGE:
/*     */         case NEXT:
/*     */         case MINEXTENTS:
/*     */         case MAXEXTENTS:
/*     */         case MAXSIZE:
/*     */         case PCTINCREASE:
/*     */         case FLASH_CACHE:
/*     */         case CELL_FLASH_CACHE:
/*     */         case KEEP:
/*     */         case NONE:
/*     */         case LOB:
/*     */         case STORE:
/*     */         case ROW:
/*     */         case CHUNK:
/*     */         case CACHE:
/*     */         case NOCACHE:
/*     */         case LOGGING:
/*     */         case NOCOMPRESS:
/*     */         case KEEP_DUPLICATES:
/*     */         case EXCEPTIONS:
/*     */         case PURGE:
/*     */         case INITIALLY:
/*     */         case END:
/*     */         case COMMENT:
/*     */         case ENABLE:
/*     */         case DISABLE:
/*     */         case SEQUENCE:
/*     */         case USER:
/*     */         case ANALYZE:
/*     */         case OPTIMIZE:
/*     */         case GRANT:
/*     */         case REVOKE:
/*     */         case FULL:
/*     */         case TO:
/*     */         case NEW:
/*     */         case LOCK:
/*     */         case LIMIT:
/*     */         case IDENTIFIED:
/*     */         case PASSWORD:
/*     */         case BINARY:
/*     */         case WINDOW:
/*     */         case OFFSET:
/*     */         case SHARE:
/*     */         case START:
/*     */         case CONNECT:
/*     */         case MATCHED:
/*     */         case ERRORS:
/*     */         case REJECT:
/*     */         case UNLIMITED:
/*     */         case BEGIN:
/*     */         case EXCLUSIVE:
/*     */         case MODE:
/*     */         case ADVISE:
/*     */         case TYPE:
/*     */         case CLOSE:
/*     */           alias = this.lexer.stringVal();
/*     */           this.lexer.nextToken();
/*     */           return alias;
/*     */         case QUES:
/*     */           alias = "?";
/*     */           this.lexer.nextToken();
/*     */           break;
/*     */       } 
/*     */     } 
/*     */     return alias;
/*     */   }
/*     */   
/*     */   protected void printError(Token token) {
/*     */     String arround;
/*     */     if (this.lexer.mark >= 0 && this.lexer.text.length() > this.lexer.mark + 30) {
/*     */       if (this.lexer.mark - 5 > 0) {
/*     */         arround = this.lexer.text.substring(this.lexer.mark - 5, this.lexer.mark + 30);
/*     */       } else {
/*     */         arround = this.lexer.text.substring(this.lexer.mark, this.lexer.mark + 30);
/*     */       } 
/*     */     } else if (this.lexer.mark >= 0) {
/*     */       if (this.lexer.mark - 5 > 0) {
/*     */         arround = this.lexer.text.substring(this.lexer.mark - 5);
/*     */       } else {
/*     */         arround = this.lexer.text.substring(this.lexer.mark);
/*     */       } 
/*     */     } else {
/*     */       arround = this.lexer.text;
/*     */     } 
/*     */     throw new ParserException("syntax error, error in :'" + arround + "',expect " + token + ", actual " + this.lexer.token() + " " + this.lexer.stringVal());
/*     */   }
/*     */   
/*     */   public void accept(Token token) {
/*     */     if (this.lexer.token() == token) {
/*     */       this.lexer.nextToken();
/*     */     } else {
/*     */       setErrorEndPos(this.lexer.pos());
/*     */       printError(token);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void match(Token token) {
/*     */     if (this.lexer.token() != token)
/*     */       throw new ParserException("syntax error, expect " + token + ", actual " + this.lexer.token() + " " + this.lexer.stringVal()); 
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\parser\SQLParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */