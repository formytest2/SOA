/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.SQLUtils;
import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLJoinTableSource;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.FlashbackQueryClause;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
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
/*    */ public class OracleSelectJoin
/*    */   extends SQLJoinTableSource
/*    */   implements OracleSelectTableSource
/*    */ {
/*    */   protected OracleSelectPivotBase pivot;
/*    */   protected FlashbackQueryClause flashback;
/*    */   
/*    */   public OracleSelectJoin(String alias) {
/* 32 */     super(alias);
/*    */   }
/*    */ 
/*    */   
/*    */   public OracleSelectJoin() {}
/*    */ 
/*    */   
/*    */   public FlashbackQueryClause getFlashback() {
/* 40 */     return this.flashback;
/*    */   }
/*    */   
/*    */   public void setFlashback(FlashbackQueryClause flashback) {
/* 44 */     this.flashback = flashback;
/*    */   }
/*    */   
/*    */   public OracleSelectPivotBase getPivot() {
/* 48 */     return this.pivot;
/*    */   }
/*    */   
/*    */   public void setPivot(OracleSelectPivotBase pivot) {
/* 52 */     this.pivot = pivot;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 57 */     accept0((OracleASTVisitor)visitor);
/*    */   }
/*    */   
/*    */   protected void accept0(OracleASTVisitor visitor) {
/* 61 */     if (visitor.visit(this)) {
/* 62 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.left);
/* 63 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.right);
/* 64 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.condition);
/* 65 */       acceptChild((SQLASTVisitor)visitor, this.using);
/* 66 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.flashback);
/*    */     } 
/*    */     
/* 69 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 73 */     this.left.output(buf);
/* 74 */     buf.append(SQLJoinTableSource.JoinType.toString(this.joinType));
/* 75 */     this.right.output(buf);
/*    */     
/* 77 */     if (this.condition != null) {
/* 78 */       buf.append(" ON ");
/* 79 */       this.condition.output(buf);
/*    */     } 
/*    */     
/* 82 */     if (this.using.size() > 0) {
/* 83 */       buf.append(" USING (");
/* 84 */       int i = 0;
/* 85 */       for (int size = this.using.size(); i < size; i++) {
/* 86 */         if (i != 0) {
/* 87 */           buf.append(", ");
/*    */         }
/* 89 */         ((SQLExpr)this.using.get(i)).output(buf);
/*    */       } 
/* 91 */       buf.append(")");
/*    */     } 
/*    */   }
/*    */   
/*    */   public String toString() {
/* 96 */     return SQLUtils.toOracleString((SQLObject)this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleSelectJoin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */