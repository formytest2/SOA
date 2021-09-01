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
/*    */ public class OracleDropDbLinkStatement
/*    */   extends OracleStatementImpl
/*    */ {
/*    */   private boolean isPublic;
/*    */   private SQLName name;
/*    */   
/*    */   public boolean isPublic() {
/* 28 */     return this.isPublic;
/*    */   }
/*    */   
/*    */   public void setPublic(boolean value) {
/* 32 */     this.isPublic = value;
/*    */   }
/*    */   
/*    */   public SQLName getName() {
/* 36 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(SQLName name) {
/* 40 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 45 */     if (visitor.visit(this)) {
/* 46 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.name);
/*    */     }
/* 48 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleDropDbLinkStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */