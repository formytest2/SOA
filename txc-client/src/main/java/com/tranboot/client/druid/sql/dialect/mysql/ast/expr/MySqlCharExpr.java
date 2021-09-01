/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.expr.SQLCharExpr;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
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
/*    */ public class MySqlCharExpr
/*    */   extends SQLCharExpr
/*    */   implements MySqlExpr
/*    */ {
/*    */   private String charset;
/*    */   private String collate;
/*    */   
/*    */   public MySqlCharExpr() {}
/*    */   
/*    */   public MySqlCharExpr(String text) {
/* 32 */     super(text);
/*    */   }
/*    */   
/*    */   public String getCharset() {
/* 36 */     return this.charset;
/*    */   }
/*    */   
/*    */   public void setCharset(String charset) {
/* 40 */     this.charset = charset;
/*    */   }
/*    */   
/*    */   public String getCollate() {
/* 44 */     return this.collate;
/*    */   }
/*    */   
/*    */   public void setCollate(String collate) {
/* 48 */     this.collate = collate;
/*    */   }
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 52 */     if (this.charset != null) {
/* 53 */       buf.append(this.charset);
/* 54 */       buf.append(' ');
/*    */     } 
/* 56 */     if (this.text != null) {
/* 57 */       super.output(buf);
/*    */     }
/*    */     
/* 60 */     if (this.collate != null) {
/* 61 */       buf.append(" COLLATE ");
/* 62 */       buf.append(this.collate);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 68 */     if (visitor instanceof MySqlASTVisitor) {
/* 69 */       accept0((MySqlASTVisitor)visitor);
/*    */     } else {
/* 71 */       visitor.visit(this);
/* 72 */       visitor.endVisit(this);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 77 */     visitor.visit(this);
/* 78 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 82 */     StringBuffer buf = new StringBuffer();
/* 83 */     output(buf);
/* 84 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\expr\MySqlCharExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */