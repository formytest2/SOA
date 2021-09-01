/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableItem;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MySqlObjectImpl;
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
/*    */ 
/*    */ 
/*    */ public class MySqlAlterTableAlterColumn
/*    */   extends MySqlObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/*    */   private SQLName column;
/*    */   private boolean dropDefault = false;
/*    */   private SQLExpr defaultExpr;
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 33 */     if (visitor.visit(this)) {
/* 34 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.column);
/* 35 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.defaultExpr);
/*    */     } 
/* 37 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public boolean isDropDefault() {
/* 41 */     return this.dropDefault;
/*    */   }
/*    */   
/*    */   public void setDropDefault(boolean dropDefault) {
/* 45 */     this.dropDefault = dropDefault;
/*    */   }
/*    */   
/*    */   public SQLExpr getDefaultExpr() {
/* 49 */     return this.defaultExpr;
/*    */   }
/*    */   
/*    */   public void setDefaultExpr(SQLExpr defaultExpr) {
/* 53 */     this.defaultExpr = defaultExpr;
/*    */   }
/*    */   
/*    */   public SQLName getColumn() {
/* 57 */     return this.column;
/*    */   }
/*    */   
/*    */   public void setColumn(SQLName column) {
/* 61 */     this.column = column;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlAlterTableAlterColumn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */