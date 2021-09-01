/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLHint;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateStatement;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

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
/*    */ public class OracleUpdateStatement
/*    */   extends SQLUpdateStatement
/*    */   implements OracleStatement
/*    */ {
/* 30 */   private final List<SQLHint> hints = new ArrayList<>(1);
/*    */   
/*    */   private boolean only = false;
/*    */   private String alias;
/* 34 */   private List<SQLExpr> returningInto = new ArrayList<>();
/*    */   
/*    */   public OracleUpdateStatement() {
/* 37 */     super("oracle");
/*    */   }
/*    */   
/*    */   public List<SQLExpr> getReturningInto() {
/* 41 */     return this.returningInto;
/*    */   }
/*    */   
/*    */   public void setReturningInto(List<SQLExpr> returningInto) {
/* 45 */     this.returningInto = returningInto;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 50 */     if (visitor instanceof OracleASTVisitor) {
/* 51 */       accept0((OracleASTVisitor)visitor);
/*    */       
/*    */       return;
/*    */     } 
/* 55 */     accept(visitor);
/*    */   }
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 59 */     if (visitor.visit(this)) {
/* 60 */       acceptChild((SQLASTVisitor)visitor, this.hints);
/* 61 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.tableSource);
/* 62 */       acceptChild((SQLASTVisitor)visitor, this.items);
/* 63 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.where);
/* 64 */       acceptChild((SQLASTVisitor)visitor, this.returning);
/* 65 */       acceptChild((SQLASTVisitor)visitor, this.returningInto);
/*    */     } 
/*    */     
/* 68 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public String getAlias() {
/* 72 */     return this.alias;
/*    */   }
/*    */   
/*    */   public void setAlias(String alias) {
/* 76 */     this.alias = alias;
/*    */   }
/*    */   
/*    */   public boolean isOnly() {
/* 80 */     return this.only;
/*    */   }
/*    */   
/*    */   public void setOnly(boolean only) {
/* 84 */     this.only = only;
/*    */   }
/*    */   
/*    */   public List<SQLHint> getHints() {
/* 88 */     return this.hints;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleUpdateStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */