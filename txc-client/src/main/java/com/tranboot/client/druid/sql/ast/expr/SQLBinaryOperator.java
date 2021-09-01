/*     */ package com.tranboot.client.druid.sql.ast.expr;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum SQLBinaryOperator
/*     */ {
/*  24 */   Union("UNION", 0),
/*  25 */   COLLATE("COLLATE", 20),
/*  26 */   BitwiseXor("^", 50),
/*     */   
/*  28 */   Multiply("*", 60),
/*  29 */   Divide("/", 60),
/*  30 */   DIV("DIV", 60),
/*  31 */   Modulus("%", 60),
/*  32 */   Mod("MOD", 60),
/*     */   
/*  34 */   Add("+", 70),
/*  35 */   Subtract("-", 70),
/*     */   
/*  37 */   SubGt("->", 20),
/*  38 */   SubGtGt("->>", 20),
/*  39 */   PoundGt("#>", 20),
/*  40 */   PoundGtGt("#>>", 20),
/*  41 */   QuesQues("??", 20),
/*  42 */   QuesBar("?|", 20),
/*  43 */   QuesAmp("?&", 20),
/*     */   
/*  45 */   LeftShift("<<", 80),
/*  46 */   RightShift(">>", 80),
/*     */   
/*  48 */   BitwiseAnd("&", 90),
/*  49 */   BitwiseOr("|", 100),
/*     */   
/*  51 */   GreaterThan(">", 110),
/*  52 */   GreaterThanOrEqual(">=", 110),
/*  53 */   Is("IS", 110),
/*  54 */   LessThan("<", 110),
/*  55 */   LessThanOrEqual("<=", 110),
/*  56 */   LessThanOrEqualOrGreaterThan("<=>", 110),
/*  57 */   LessThanOrGreater("<>", 110),
/*     */   
/*  59 */   Like("LIKE", 110),
/*  60 */   NotLike("NOT LIKE", 110),
/*     */   
/*  62 */   ILike("ILIKE", 110),
/*  63 */   NotILike("NOT ILIKE", 110),
/*  64 */   AT_AT("@@", 110),
/*  65 */   SIMILAR_TO("SIMILAR TO", 110),
/*  66 */   POSIX_Regular_Match("~", 110),
/*  67 */   POSIX_Regular_Match_Insensitive("~*", 110),
/*  68 */   POSIX_Regular_Not_Match("!~", 110),
/*  69 */   POSIX_Regular_Not_Match_POSIX_Regular_Match_Insensitive("!~*", 110),
/*  70 */   Array_Contains("@>", 110),
/*  71 */   Array_ContainedBy("<@", 110),
/*  72 */   SAME_AS("~=", 110),
/*     */   
/*  74 */   RLike("RLIKE", 110),
/*  75 */   NotRLike("NOT RLIKE", 110),
/*     */   
/*  77 */   NotEqual("!=", 110),
/*  78 */   NotLessThan("!<", 110),
/*  79 */   NotGreaterThan("!>", 110),
/*  80 */   IsNot("IS NOT", 110),
/*  81 */   Escape("ESCAPE", 110),
/*  82 */   RegExp("REGEXP", 110),
/*  83 */   NotRegExp("NOT REGEXP", 110),
/*  84 */   Equality("=", 110),
/*     */   
/*  86 */   BitwiseNot("!", 130),
/*  87 */   Concat("||", 140),
/*     */   
/*  89 */   BooleanAnd("AND", 140),
/*  90 */   BooleanXor("XOR", 150),
/*  91 */   BooleanOr("OR", 160),
/*  92 */   Assignment(":=", 169),
/*     */   
/*  94 */   PG_And("&&", 140);
/*     */   public final String name;
/*     */   
/*     */   public static int getPriority(SQLBinaryOperator operator) {
/*  98 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String name_lcase;
/*     */ 
/*     */   
/*     */   public final int priority;
/*     */ 
/*     */   
/*     */   SQLBinaryOperator(String name, int priority) {
/* 110 */     this.name = name;
/* 111 */     this.name_lcase = name.toLowerCase();
/* 112 */     this.priority = priority;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 116 */     return this.name;
/*     */   }
/*     */   
/*     */   public int getPriority() {
/* 120 */     return this.priority;
/*     */   }
/*     */   
/*     */   public boolean isRelational() {
/* 124 */     switch (this) {
/*     */       case Equality:
/*     */       case Like:
/*     */       case NotEqual:
/*     */       case GreaterThan:
/*     */       case GreaterThanOrEqual:
/*     */       case LessThan:
/*     */       case LessThanOrEqual:
/*     */       case LessThanOrGreater:
/*     */       case NotLike:
/*     */       case NotLessThan:
/*     */       case NotGreaterThan:
/*     */       case RLike:
/*     */       case NotRLike:
/*     */       case RegExp:
/*     */       case NotRegExp:
/* 140 */         return true;
/*     */     } 
/* 142 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLogical() {
/* 147 */     return (this == BooleanAnd || this == BooleanOr || this == BooleanXor);
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLBinaryOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */