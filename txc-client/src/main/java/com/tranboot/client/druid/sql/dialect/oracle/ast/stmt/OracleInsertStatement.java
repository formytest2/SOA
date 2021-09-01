/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLHint;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLErrorLoggingClause;
import com.tranboot.client.druid.sql.ast.statement.SQLInsertStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.OracleReturningClause;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleOutputVisitor;
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
/*    */ public class OracleInsertStatement
/*    */   extends SQLInsertStatement
/*    */   implements OracleStatement
/*    */ {
/*    */   private OracleReturningClause returning;
/*    */   private SQLErrorLoggingClause errorLogging;
/* 33 */   private List<SQLHint> hints = new ArrayList<>();
/*    */   
/*    */   public List<SQLHint> getHints() {
/* 36 */     return this.hints;
/*    */   }
/*    */   
/*    */   public void setHints(List<SQLHint> hints) {
/* 40 */     this.hints = hints;
/*    */   }
/*    */   
/*    */   public OracleReturningClause getReturning() {
/* 44 */     return this.returning;
/*    */   }
/*    */   
/*    */   public void setReturning(OracleReturningClause returning) {
/* 48 */     this.returning = returning;
/*    */   }
/*    */   
/*    */   public SQLErrorLoggingClause getErrorLogging() {
/* 52 */     return this.errorLogging;
/*    */   }
/*    */   
/*    */   public void setErrorLogging(SQLErrorLoggingClause errorLogging) {
/* 56 */     this.errorLogging = errorLogging;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 61 */     accept0((OracleASTVisitor)visitor);
/*    */   }
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 65 */     if (visitor.visit(this)) {
/* 66 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getTableSource());
/* 67 */       acceptChild((SQLASTVisitor)visitor, getColumns());
/* 68 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getValues());
/* 69 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)getQuery());
/* 70 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.returning);
/* 71 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.errorLogging);
/*    */     } 
/*    */     
/* 74 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 78 */     (new OracleOutputVisitor(buf)).visit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleInsertStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */