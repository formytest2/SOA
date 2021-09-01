/*     */ package com.tranboot.client.druid.sql.visitor.functions;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLEvalVisitor;
import com.tranboot.client.druid.sql.visitor.SQLEvalVisitorUtils;
import com.tranboot.client.druid.util.Utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */

/*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OneParamFunctions
/*     */   implements Function
/*     */ {
/*  32 */   public static final OneParamFunctions instance = new OneParamFunctions();
/*     */   
/*     */   public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
/*  35 */     if (x.getParameters().size() == 0) {
/*  36 */       return SQLEvalVisitor.EVAL_ERROR;
/*     */     }
/*     */     
/*  39 */     SQLExpr param = x.getParameters().get(0);
/*  40 */     param.accept((SQLASTVisitor)visitor);
/*     */     
/*  42 */     Object paramValue = param.getAttributes().get("eval.value");
/*  43 */     if (paramValue == null) {
/*  44 */       return SQLEvalVisitor.EVAL_ERROR;
/*     */     }
/*     */     
/*  47 */     if (paramValue == SQLEvalVisitor.EVAL_VALUE_NULL) {
/*  48 */       return SQLEvalVisitor.EVAL_VALUE_NULL;
/*     */     }
/*     */     
/*  51 */     String method = x.getMethodName();
/*  52 */     if ("md5".equalsIgnoreCase(method)) {
/*  53 */       String text = paramValue.toString();
/*  54 */       return Utils.md5(text);
/*     */     } 
/*     */     
/*  57 */     if ("bit_count".equalsIgnoreCase(method)) {
/*  58 */       if (paramValue instanceof BigInteger) {
/*  59 */         return Integer.valueOf(((BigInteger)paramValue).bitCount());
/*     */       }
/*     */       
/*  62 */       if (paramValue instanceof BigDecimal) {
/*  63 */         BigDecimal decimal = (BigDecimal)paramValue;
/*  64 */         BigInteger bigInt = decimal.setScale(0, 4).toBigInteger();
/*  65 */         return Integer.valueOf(bigInt.bitCount());
/*     */       } 
/*  67 */       Long val = SQLEvalVisitorUtils.castToLong(paramValue);
/*  68 */       return Integer.valueOf(Long.bitCount(val.longValue()));
/*     */     } 
/*     */     
/*  71 */     if ("soundex".equalsIgnoreCase(method)) {
/*  72 */       String text = paramValue.toString();
/*  73 */       return soundex(text);
/*     */     } 
/*     */     
/*  76 */     if ("space".equalsIgnoreCase(method)) {
/*  77 */       int intVal = SQLEvalVisitorUtils.castToInteger(paramValue).intValue();
/*  78 */       char[] chars = new char[intVal];
/*  79 */       for (int i = 0; i < chars.length; i++) {
/*  80 */         chars[i] = ' ';
/*     */       }
/*  82 */       return new String(chars);
/*     */     } 
/*     */     
/*  85 */     throw new UnsupportedOperationException(method);
/*     */   }
/*     */   
/*     */   public static String soundex(String str) {
/*  89 */     if (str == null) {
/*  90 */       return null;
/*     */     }
/*  92 */     str = clean(str);
/*  93 */     if (str.length() == 0) {
/*  94 */       return str;
/*     */     }
/*  96 */     char[] out = { '0', '0', '0', '0' };
/*     */     
/*  98 */     int incount = 1, count = 1;
/*  99 */     out[0] = str.charAt(0);
/*     */     
/* 101 */     char last = getMappingCode(str, 0);
/* 102 */     while (incount < str.length() && count < out.length) {
/* 103 */       char mapped = getMappingCode(str, incount++);
/* 104 */       if (mapped != '\000') {
/* 105 */         if (mapped != '0' && mapped != last) {
/* 106 */           out[count++] = mapped;
/*     */         }
/* 108 */         last = mapped;
/*     */       } 
/*     */     } 
/* 111 */     return new String(out);
/*     */   }
/*     */   
/*     */   static String clean(String str) {
/* 115 */     if (str == null || str.length() == 0) {
/* 116 */       return str;
/*     */     }
/* 118 */     int len = str.length();
/* 119 */     char[] chars = new char[len];
/* 120 */     int count = 0;
/* 121 */     for (int i = 0; i < len; i++) {
/* 122 */       if (Character.isLetter(str.charAt(i))) {
/* 123 */         chars[count++] = str.charAt(i);
/*     */       }
/*     */     } 
/* 126 */     if (count == len) {
/* 127 */       return str.toUpperCase(Locale.ENGLISH);
/*     */     }
/* 129 */     return (new String(chars, 0, count)).toUpperCase(Locale.ENGLISH);
/*     */   }
/*     */ 
/*     */   
/*     */   private static char getMappingCode(String str, int index) {
/* 134 */     char mappedChar = map(str.charAt(index));
/*     */     
/* 136 */     if (index > 1 && mappedChar != '0') {
/* 137 */       char hwChar = str.charAt(index - 1);
/* 138 */       if ('H' == hwChar || 'W' == hwChar) {
/* 139 */         char preHWChar = str.charAt(index - 2);
/* 140 */         char firstCode = map(preHWChar);
/* 141 */         if (firstCode == mappedChar || 'H' == preHWChar || 'W' == preHWChar) {
/* 142 */           return Character.MIN_VALUE;
/*     */         }
/*     */       } 
/*     */     } 
/* 146 */     return mappedChar;
/*     */   }
/*     */   
/*     */   private static char map(char ch) {
/* 150 */     String soundexMapping = "01230120022455012623010202";
/* 151 */     int index = ch - 65;
/* 152 */     if (index < 0 || index >= soundexMapping.length()) {
/* 153 */       throw new IllegalArgumentException("The character is not mapped: " + ch);
/*     */     }
/* 155 */     return soundexMapping.charAt(index);
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\visitor\functions\OneParamFunctions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */