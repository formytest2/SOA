/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObjectImpl;

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
/*    */ public abstract class SQLConstraintImpl
/*    */   extends SQLObjectImpl
/*    */   implements SQLConstraint
/*    */ {
/*    */   private SQLName name;
/*    */   private Boolean enable;
/*    */   
/*    */   public SQLName getName() {
/* 31 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(SQLName name) {
/* 35 */     this.name = name;
/*    */   }
/*    */   
/*    */   public Boolean getEnable() {
/* 39 */     return this.enable;
/*    */   }
/*    */   
/*    */   public void setEnable(Boolean enable) {
/* 43 */     this.enable = enable;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLConstraintImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */