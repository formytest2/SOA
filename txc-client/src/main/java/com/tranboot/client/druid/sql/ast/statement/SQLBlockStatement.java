/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLParameter;
import com.tranboot.client.druid.sql.ast.SQLStatement;
import com.tranboot.client.druid.sql.ast.SQLStatementImpl;
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
/*    */ public class SQLBlockStatement
/*    */   extends SQLStatementImpl
/*    */ {
/*    */   private String labelName;
/* 29 */   private List<SQLParameter> parameters = new ArrayList<>();
/*    */   
/* 31 */   private List<SQLStatement> statementList = new ArrayList<>();
/*    */   
/*    */   public List<SQLStatement> getStatementList() {
/* 34 */     return this.statementList;
/*    */   }
/*    */   
/*    */   public void setStatementList(List<SQLStatement> statementList) {
/* 38 */     this.statementList = statementList;
/*    */   }
/*    */   
/*    */   public String getLabelName() {
/* 42 */     return this.labelName;
/*    */   }
/*    */   
/*    */   public void setLabelName(String labelName) {
/* 46 */     this.labelName = labelName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(SQLASTVisitor visitor) {
/* 51 */     if (visitor.visit(this)) {
/* 52 */       acceptChild(visitor, this.parameters);
/* 53 */       acceptChild(visitor, this.statementList);
/*    */     } 
/* 55 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public List<SQLParameter> getParameters() {
/* 59 */     return this.parameters;
/*    */   }
/*    */   
/*    */   public void setParameters(List<SQLParameter> parameters) {
/* 63 */     this.parameters = parameters;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLBlockStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */