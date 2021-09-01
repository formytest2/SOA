/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.statement.SQLConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLTableElement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleSQLObject;

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
/*    */ public interface OracleConstraint
/*    */   extends OracleSQLObject, SQLConstraint, SQLTableElement
/*    */ {
/*    */   SQLName getExceptionsInto();
/*    */   
/*    */   void setExceptionsInto(SQLName paramSQLName);
/*    */   
/*    */   Boolean getDeferrable();
/*    */   
/*    */   void setDeferrable(Boolean paramBoolean);
/*    */   
/*    */   Boolean getEnable();
/*    */   
/*    */   void setEnable(Boolean paramBoolean);
/*    */   
/*    */   Initially getInitially();
/*    */   
/*    */   void setInitially(Initially paramInitially);
/*    */   
/*    */   OracleUsingIndexClause getUsing();
/*    */   
/*    */   void setUsing(OracleUsingIndexClause paramOracleUsingIndexClause);
/*    */   
/*    */   public enum Initially
/*    */   {
/* 46 */     DEFERRED, IMMEDIATE;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleConstraint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */