/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*    */ public class SQLSubqueryTableSource
/*    */   extends SQLTableSourceImpl
/*    */ {
/*    */   protected SQLSelect select;
/*    */   
/*    */   public SQLSubqueryTableSource() {}
/*    */   
/*    */   public SQLSubqueryTableSource(String alias) {
/* 29 */     super(alias);
/*    */   }
/*    */   
/*    */   public SQLSubqueryTableSource(SQLSelect select, String alias) {
/* 33 */     super(alias);
/* 34 */     setSelect(select);
/*    */   }
/*    */   
/*    */   public SQLSubqueryTableSource(SQLSelect select) {
/* 38 */     setSelect(select);
/*    */   }
/*    */   
/*    */   public SQLSelect getSelect() {
/* 42 */     return this.select;
/*    */   }
/*    */   
/*    */   public void setSelect(SQLSelect select) {
/* 46 */     if (select != null) {
/* 47 */       select.setParent(this);
/*    */     }
/* 49 */     this.select = select;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 54 */     if (visitor.visit(this)) {
/* 55 */       acceptChild(visitor, (SQLObject)this.select);
/*    */     }
/* 57 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 61 */     buf.append("(");
/* 62 */     this.select.output(buf);
/* 63 */     buf.append(")");
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLSubqueryTableSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */