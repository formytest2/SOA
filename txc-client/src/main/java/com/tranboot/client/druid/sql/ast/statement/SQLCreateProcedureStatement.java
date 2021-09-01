/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.*;
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
/*    */ public class SQLCreateProcedureStatement
/*    */   extends SQLStatementImpl
/*    */ {
/*    */   private SQLName definer;
/*    */   private boolean orReplace;
/*    */   private SQLName name;
/*    */   private SQLStatement block;
/* 34 */   private List<SQLParameter> parameters = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public void accept0(SQLASTVisitor visitor) {
/* 38 */     if (visitor.visit(this)) {
/* 39 */       acceptChild(visitor, (SQLObject)this.definer);
/* 40 */       acceptChild(visitor, (SQLObject)this.name);
/* 41 */       acceptChild(visitor, this.parameters);
/* 42 */       acceptChild(visitor, (SQLObject)this.block);
/*    */     } 
/* 44 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public List<SQLParameter> getParameters() {
/* 48 */     return this.parameters;
/*    */   }
/*    */   
/*    */   public void setParameters(List<SQLParameter> parameters) {
/* 52 */     this.parameters = parameters;
/*    */   }
/*    */   
/*    */   public SQLName getName() {
/* 56 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(SQLName name) {
/* 60 */     this.name = name;
/*    */   }
/*    */   
/*    */   public SQLStatement getBlock() {
/* 64 */     return this.block;
/*    */   }
/*    */   
/*    */   public void setBlock(SQLStatement block) {
/* 68 */     this.block = block;
/*    */   }
/*    */   
/*    */   public boolean isOrReplace() {
/* 72 */     return this.orReplace;
/*    */   }
/*    */   
/*    */   public void setOrReplace(boolean orReplace) {
/* 76 */     this.orReplace = orReplace;
/*    */   }
/*    */   
/*    */   public SQLName getDefiner() {
/* 80 */     return this.definer;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDefiner(SQLName definer) {
/* 85 */     this.definer = definer;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLCreateProcedureStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */