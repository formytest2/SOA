/*     */ package com.tranboot.client.druid.sql.parser;
/*     */ 
/*     */

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
/*     */ public class Keywords
/*     */ {
/*     */   private final Map<String, Token> keywords;
/*     */   public static final Keywords DEFAULT_KEYWORDS;
/*     */   public static final Keywords SQLITE_KEYWORDS;
/*     */   
/*     */   static {
/*  33 */     Map<String, Token> map = new HashMap<>();
/*     */     
/*  35 */     map.put("ALL", Token.ALL);
/*  36 */     map.put("ALTER", Token.ALTER);
/*  37 */     map.put("AND", Token.AND);
/*  38 */     map.put("ANY", Token.ANY);
/*  39 */     map.put("AS", Token.AS);
/*     */     
/*  41 */     map.put("ENABLE", Token.ENABLE);
/*  42 */     map.put("DISABLE", Token.DISABLE);
/*     */     
/*  44 */     map.put("ASC", Token.ASC);
/*  45 */     map.put("BETWEEN", Token.BETWEEN);
/*  46 */     map.put("BY", Token.BY);
/*  47 */     map.put("CASE", Token.CASE);
/*  48 */     map.put("CAST", Token.CAST);
/*     */     
/*  50 */     map.put("CHECK", Token.CHECK);
/*  51 */     map.put("CONSTRAINT", Token.CONSTRAINT);
/*  52 */     map.put("CREATE", Token.CREATE);
/*  53 */     map.put("DATABASE", Token.DATABASE);
/*  54 */     map.put("DEFAULT", Token.DEFAULT);
/*  55 */     map.put("COLUMN", Token.COLUMN);
/*  56 */     map.put("TABLESPACE", Token.TABLESPACE);
/*  57 */     map.put("PROCEDURE", Token.PROCEDURE);
/*  58 */     map.put("FUNCTION", Token.FUNCTION);
/*     */     
/*  60 */     map.put("DELETE", Token.DELETE);
/*  61 */     map.put("DESC", Token.DESC);
/*  62 */     map.put("DISTINCT", Token.DISTINCT);
/*  63 */     map.put("DROP", Token.DROP);
/*  64 */     map.put("ELSE", Token.ELSE);
/*  65 */     map.put("EXPLAIN", Token.EXPLAIN);
/*  66 */     map.put("EXCEPT", Token.EXCEPT);
/*     */     
/*  68 */     map.put("END", Token.END);
/*  69 */     map.put("ESCAPE", Token.ESCAPE);
/*  70 */     map.put("EXISTS", Token.EXISTS);
/*  71 */     map.put("FOR", Token.FOR);
/*  72 */     map.put("FOREIGN", Token.FOREIGN);
/*     */     
/*  74 */     map.put("FROM", Token.FROM);
/*  75 */     map.put("FULL", Token.FULL);
/*  76 */     map.put("GROUP", Token.GROUP);
/*  77 */     map.put("HAVING", Token.HAVING);
/*  78 */     map.put("IN", Token.IN);
/*     */     
/*  80 */     map.put("INDEX", Token.INDEX);
/*  81 */     map.put("INNER", Token.INNER);
/*  82 */     map.put("INSERT", Token.INSERT);
/*  83 */     map.put("INTERSECT", Token.INTERSECT);
/*  84 */     map.put("INTERVAL", Token.INTERVAL);
/*     */     
/*  86 */     map.put("INTO", Token.INTO);
/*  87 */     map.put("IS", Token.IS);
/*  88 */     map.put("JOIN", Token.JOIN);
/*  89 */     map.put("KEY", Token.KEY);
/*  90 */     map.put("LEFT", Token.LEFT);
/*     */     
/*  92 */     map.put("LIKE", Token.LIKE);
/*  93 */     map.put("LOCK", Token.LOCK);
/*  94 */     map.put("MINUS", Token.MINUS);
/*  95 */     map.put("NOT", Token.NOT);
/*     */     
/*  97 */     map.put("NULL", Token.NULL);
/*  98 */     map.put("ON", Token.ON);
/*  99 */     map.put("OR", Token.OR);
/* 100 */     map.put("ORDER", Token.ORDER);
/* 101 */     map.put("OUTER", Token.OUTER);
/*     */     
/* 103 */     map.put("PRIMARY", Token.PRIMARY);
/* 104 */     map.put("REFERENCES", Token.REFERENCES);
/* 105 */     map.put("RIGHT", Token.RIGHT);
/* 106 */     map.put("SCHEMA", Token.SCHEMA);
/* 107 */     map.put("SELECT", Token.SELECT);
/*     */     
/* 109 */     map.put("SET", Token.SET);
/* 110 */     map.put("SOME", Token.SOME);
/* 111 */     map.put("TABLE", Token.TABLE);
/* 112 */     map.put("THEN", Token.THEN);
/* 113 */     map.put("TRUNCATE", Token.TRUNCATE);
/*     */     
/* 115 */     map.put("UNION", Token.UNION);
/* 116 */     map.put("UNIQUE", Token.UNIQUE);
/* 117 */     map.put("UPDATE", Token.UPDATE);
/* 118 */     map.put("VALUES", Token.VALUES);
/* 119 */     map.put("VIEW", Token.VIEW);
/* 120 */     map.put("SEQUENCE", Token.SEQUENCE);
/* 121 */     map.put("TRIGGER", Token.TRIGGER);
/* 122 */     map.put("USER", Token.USER);
/*     */     
/* 124 */     map.put("WHEN", Token.WHEN);
/* 125 */     map.put("WHERE", Token.WHERE);
/* 126 */     map.put("XOR", Token.XOR);
/*     */     
/* 128 */     map.put("OVER", Token.OVER);
/* 129 */     map.put("TO", Token.TO);
/* 130 */     map.put("USE", Token.USE);
/*     */     
/* 132 */     map.put("REPLACE", Token.REPLACE);
/*     */     
/* 134 */     map.put("COMMENT", Token.COMMENT);
/* 135 */     map.put("COMPUTE", Token.COMPUTE);
/* 136 */     map.put("WITH", Token.WITH);
/* 137 */     map.put("GRANT", Token.GRANT);
/* 138 */     map.put("REVOKE", Token.REVOKE);
/*     */ 
/*     */     
/* 141 */     map.put("WHILE", Token.WHILE);
/* 142 */     map.put("DO", Token.DO);
/* 143 */     map.put("DECLARE", Token.DECLARE);
/* 144 */     map.put("LOOP", Token.LOOP);
/* 145 */     map.put("LEAVE", Token.LEAVE);
/* 146 */     map.put("ITERATE", Token.ITERATE);
/* 147 */     map.put("REPEAT", Token.REPEAT);
/* 148 */     map.put("UNTIL", Token.UNTIL);
/* 149 */     map.put("OPEN", Token.OPEN);
/* 150 */     map.put("CLOSE", Token.CLOSE);
/* 151 */     map.put("CURSOR", Token.CURSOR);
/* 152 */     map.put("FETCH", Token.FETCH);
/* 153 */     map.put("OUT", Token.OUT);
/* 154 */     map.put("INOUT", Token.INOUT);
/*     */     
/* 156 */     DEFAULT_KEYWORDS = new Keywords(map);
/*     */     
/* 158 */     Map<String, Token> sqlitemap = new HashMap<>();
/*     */     
/* 160 */     sqlitemap.putAll(DEFAULT_KEYWORDS.getKeywords());
/*     */     
/* 162 */     sqlitemap.put("LIMIT", Token.LIMIT);
/* 163 */     SQLITE_KEYWORDS = new Keywords(sqlitemap);
/*     */   }
/*     */   
/*     */   public boolean containsValue(Token token) {
/* 167 */     return this.keywords.containsValue(token);
/*     */   }
/*     */   
/*     */   public Keywords(Map<String, Token> keywords) {
/* 171 */     this.keywords = keywords;
/*     */   }
/*     */   
/*     */   public Token getKeyword(String key) {
/* 175 */     key = key.toUpperCase();
/* 176 */     return this.keywords.get(key);
/*     */   }
/*     */   
/*     */   public Map<String, Token> getKeywords() {
/* 180 */     return this.keywords;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\parser\Keywords.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */