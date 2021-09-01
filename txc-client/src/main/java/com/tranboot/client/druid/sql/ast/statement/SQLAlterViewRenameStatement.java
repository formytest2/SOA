/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatementImpl;
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
/*    */ public class SQLAlterViewRenameStatement
/*    */   extends SQLStatementImpl
/*    */ {
/*    */   private SQLName name;
/*    */   private SQLName to;
/*    */   
/*    */   public SQLName getName() {
/* 28 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(SQLName name) {
/* 32 */     if (name != null) {
/* 33 */       name.setParent((SQLObject)this);
/*    */     }
/* 35 */     this.name = name;
/*    */   }
/*    */   
/*    */   public SQLName getTo() {
/* 39 */     return this.to;
/*    */   }
/*    */   
/*    */   public void setTo(SQLName to) {
/* 43 */     if (to != null) {
/* 44 */       to.setParent((SQLObject)this);
/*    */     }
/* 46 */     this.to = to;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 51 */     if (visitor.visit(this)) {
/* 52 */       acceptChild(visitor, (SQLObject)this.name);
/* 53 */       acceptChild(visitor, (SQLObject)this.to);
/*    */     } 
/* 55 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterViewRenameStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */