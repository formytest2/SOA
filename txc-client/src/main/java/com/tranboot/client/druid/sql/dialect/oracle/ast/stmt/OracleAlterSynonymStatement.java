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
/*    */ public class OracleAlterSynonymStatement
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
/*    */   public Boolean getEnable() {
/* 38 */     return this.enable;
/*    */   }
/*    */   
/*    */   public void setEnable(Boolean enable) {
/* 42 */     this.enable = enable;
/*    */   }
/*    */   
/*    */   public SQLName getName() {
/* 46 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(SQLName name) {
/* 50 */     this.name = name;
/*    */   }
/*    */   
/*    */   public boolean isCompile() {
/* 54 */     return this.compile;
/*    */   }
/*    */   
/*    */   public void setCompile(boolean compile) {
/* 58 */     this.compile = compile;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleAlterSynonymStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */