/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.SQLUtils;
import com.tranboot.client.druid.sql.ast.SQLCommentHint;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause;
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
/*    */ public class OracleSelectQueryBlock
/*    */   extends SQLSelectQueryBlock
/*    */ {
/* 30 */   private final List<SQLCommentHint> hints = new ArrayList<>(1);
/*    */ 
/*    */   
/*    */   private OracleSelectHierachicalQueryClause hierachicalQueryClause;
/*    */ 
/*    */   
/*    */   private ModelClause modelClause;
/*    */ 
/*    */   
/*    */   public ModelClause getModelClause() {
/* 40 */     return this.modelClause;
/*    */   }
/*    */   
/*    */   public void setModelClause(ModelClause modelClause) {
/* 44 */     this.modelClause = modelClause;
/*    */   }
/*    */   
/*    */   public OracleSelectHierachicalQueryClause getHierachicalQueryClause() {
/* 48 */     return this.hierachicalQueryClause;
/*    */   }
/*    */   
/*    */   public void setHierachicalQueryClause(OracleSelectHierachicalQueryClause hierachicalQueryClause) {
/* 52 */     this.hierachicalQueryClause = hierachicalQueryClause;
/*    */   }
/*    */   
/*    */   public List<SQLCommentHint> getHints() {
/* 56 */     return this.hints;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 61 */     if (visitor instanceof OracleASTVisitor) {
/* 62 */       accept0((OracleASTVisitor)visitor);
/*    */       
/*    */       return;
/*    */     } 
/* 66 */     super.accept0(visitor);
/*    */   }
/*    */   
/*    */   protected void accept0(OracleASTVisitor visitor) {
/* 70 */     if (visitor.visit(this)) {
/* 71 */       acceptChild((SQLASTVisitor)visitor, this.hints);
/* 72 */       acceptChild((SQLASTVisitor)visitor, this.selectList);
/* 73 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.into);
/* 74 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.from);
/* 75 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.where);
/* 76 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.hierachicalQueryClause);
/* 77 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.groupBy);
/* 78 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.modelClause);
/*    */     } 
/* 80 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 84 */     return SQLUtils.toOracleString((SQLObject)this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleSelectQueryBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */