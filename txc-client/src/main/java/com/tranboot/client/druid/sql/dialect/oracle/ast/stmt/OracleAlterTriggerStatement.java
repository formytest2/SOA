/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

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
/*    */ public class OracleAlterTriggerStatement
/*    */   extends OracleStatementImpl
/*    */ {
/*    */   private SQLName name;
/*    */   private Boolean enable;
/*    */   private boolean compile;
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 31 */     if (visitor.visit(this)) {
/* 32 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.name);
/*    */     }
/* 34 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public boolean isCompile() {
/* 38 */     return this.compile;
/*    */   }
/*    */   
/*    */   public void setCompile(boolean compile) {
/* 42 */     this.compile = compile;
/*    */   }
/*    */   
/*    */   public Boolean getEnable() {
/* 46 */     return this.enable;
/*    */   }
/*    */   
/*    */   public void setEnable(Boolean enable) {
/* 50 */     this.enable = enable;
/*    */   }
/*    */   
/*    */   public SQLName getName() {
/* 54 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(SQLName name) {
/* 58 */     this.name = name;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleAlterTriggerStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */