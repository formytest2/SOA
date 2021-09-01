/*    */ package com.tranboot.client.druid.sql.builder.impl;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.expr.*;
import com.tranboot.client.druid.sql.builder.SQLBuilder;

/*    */
/*    */
/*    */
/*    */
/*    */
/*    */

/*    */
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SQLBuilderImpl
/*    */   implements SQLBuilder
/*    */ {
/*    */   public static SQLExpr toSQLExpr(Object obj, String dbType) {
/* 29 */     if (obj == null) {
/* 30 */       return (SQLExpr)new SQLNullExpr();
/*    */     }
/*    */     
/* 33 */     if (obj instanceof Integer) {
/* 34 */       return (SQLExpr)new SQLIntegerExpr((Integer)obj);
/*    */     }
/*    */     
/* 37 */     if (obj instanceof Number) {
/* 38 */       return (SQLExpr)new SQLNumberExpr((Number)obj);
/*    */     }
/*    */     
/* 41 */     if (obj instanceof String) {
/* 42 */       return (SQLExpr)new SQLCharExpr((String)obj);
/*    */     }
/*    */     
/* 45 */     if (obj instanceof Boolean) {
/* 46 */       return (SQLExpr)new SQLBooleanExpr(((Boolean)obj).booleanValue());
/*    */     }
/*    */     
/* 49 */     throw new IllegalArgumentException("not support : " + obj.getClass().getName());
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\builder\impl\SQLBuilderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */