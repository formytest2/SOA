/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLObjectImpl;
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
/*    */ 
/*    */ public class SQLAlterTableConvertCharSet
/*    */   extends SQLObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/*    */   private SQLExpr charset;
/*    */   private SQLExpr collate;
/*    */   
/*    */   public SQLExpr getCharset() {
/* 32 */     return this.charset;
/*    */   }
/*    */   
/*    */   public void setCharset(SQLExpr charset) {
/* 36 */     if (charset != null) {
/* 37 */       charset.setParent(this);
/*    */     }
/* 39 */     this.charset = charset;
/*    */   }
/*    */   
/*    */   public SQLExpr getCollate() {
/* 43 */     return this.collate;
/*    */   }
/*    */   
/*    */   public void setCollate(SQLExpr collate) {
/* 47 */     if (collate != null) {
/* 48 */       collate.setParent(this);
/*    */     }
/* 50 */     this.collate = collate;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 55 */     if (visitor.visit(this)) {
/* 56 */       acceptChild(visitor, (SQLObject)this.charset);
/* 57 */       acceptChild(visitor, (SQLObject)this.collate);
/*    */     } 
/* 59 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterTableConvertCharSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */