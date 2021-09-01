/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLHint;
import com.tranboot.client.druid.sql.ast.SQLObjectImpl;

import java.util.ArrayList;
import java.util.List;

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
/*    */ 
/*    */ public abstract class SQLTableSourceImpl
/*    */   extends SQLObjectImpl
/*    */   implements SQLTableSource
/*    */ {
/*    */   protected String alias;
/*    */   protected List<SQLHint> hints;
/*    */   
/*    */   public SQLTableSourceImpl() {}
/*    */   
/*    */   public SQLTableSourceImpl(String alias) {
/* 36 */     this.alias = alias;
/*    */   }
/*    */   
/*    */   public String getAlias() {
/* 40 */     return this.alias;
/*    */   }
/*    */   
/*    */   public void setAlias(String alias) {
/* 44 */     this.alias = alias;
/*    */   }
/*    */   
/*    */   public int getHintsSize() {
/* 48 */     if (this.hints == null) {
/* 49 */       return 0;
/*    */     }
/*    */     
/* 52 */     return this.hints.size();
/*    */   }
/*    */   
/*    */   public List<SQLHint> getHints() {
/* 56 */     if (this.hints == null) {
/* 57 */       this.hints = new ArrayList<>(2);
/*    */     }
/* 59 */     return this.hints;
/*    */   }
/*    */   
/*    */   public void setHints(List<SQLHint> hints) {
/* 63 */     this.hints = hints;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLTableSourceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */