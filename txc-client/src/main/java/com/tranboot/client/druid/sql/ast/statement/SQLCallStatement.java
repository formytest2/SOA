/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatementImpl;
import com.tranboot.client.druid.sql.ast.expr.SQLVariantRefExpr;
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
/*    */ 
/*    */ public class SQLCallStatement
/*    */   extends SQLStatementImpl
/*    */ {
/*    */   private boolean brace = false;
/*    */   private SQLVariantRefExpr outParameter;
/*    */   private SQLName procedureName;
/* 35 */   private final List<SQLExpr> parameters = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public SQLCallStatement() {}
/*    */ 
/*    */   
/*    */   public SQLCallStatement(String dbType) {
/* 42 */     super(dbType);
/*    */   }
/*    */   
/*    */   public SQLVariantRefExpr getOutParameter() {
/* 46 */     return this.outParameter;
/*    */   }
/*    */   
/*    */   public void setOutParameter(SQLVariantRefExpr outParameter) {
/* 50 */     this.outParameter = outParameter;
/*    */   }
/*    */   
/*    */   public SQLName getProcedureName() {
/* 54 */     return this.procedureName;
/*    */   }
/*    */   
/*    */   public void setProcedureName(SQLName procedureName) {
/* 58 */     this.procedureName = procedureName;
/*    */   }
/*    */   
/*    */   public List<SQLExpr> getParameters() {
/* 62 */     return this.parameters;
/*    */   }
/*    */   
/*    */   public boolean isBrace() {
/* 66 */     return this.brace;
/*    */   }
/*    */   
/*    */   public void setBrace(boolean brace) {
/* 70 */     this.brace = brace;
/*    */   }
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 74 */     if (visitor.visit(this)) {
/* 75 */       acceptChild(visitor, (SQLObject)this.outParameter);
/* 76 */       acceptChild(visitor, (SQLObject)this.procedureName);
/* 77 */       acceptChild(visitor, this.parameters);
/*    */     } 
/* 79 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLCallStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */