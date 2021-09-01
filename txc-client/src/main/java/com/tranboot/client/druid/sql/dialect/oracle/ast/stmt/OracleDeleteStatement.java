/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLHint;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLDeleteStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.OracleReturningClause;
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
/*    */ 
/*    */ 
/*    */ public class OracleDeleteStatement
/*    */   extends SQLDeleteStatement
/*    */ {
/*    */   private boolean only = false;
/* 32 */   private final List<SQLHint> hints = new ArrayList<>();
/* 33 */   private OracleReturningClause returning = null;
/*    */   
/*    */   public OracleDeleteStatement() {
/* 36 */     super("oracle");
/*    */   }
/*    */   
/*    */   public OracleReturningClause getReturning() {
/* 40 */     return this.returning;
/*    */   }
/*    */   
/*    */   public void setReturning(OracleReturningClause returning) {
/* 44 */     this.returning = returning;
/*    */   }
/*    */   
/*    */   public List<SQLHint> getHints() {
/* 48 */     return this.hints;
/*    */   }
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 52 */     accept0((OracleASTVisitor)visitor);
/*    */   }
/*    */   
/*    */   protected void accept0(OracleASTVisitor visitor) {
/* 56 */     if (visitor.visit(this)) {
/* 57 */       acceptChild((SQLASTVisitor)visitor, this.hints);
/* 58 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.tableSource);
/* 59 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getWhere());
/* 60 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.returning);
/*    */     } 
/*    */     
/* 63 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public boolean isOnly() {
/* 67 */     return this.only;
/*    */   }
/*    */   
/*    */   public void setOnly(boolean only) {
/* 71 */     this.only = only;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleDeleteStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */