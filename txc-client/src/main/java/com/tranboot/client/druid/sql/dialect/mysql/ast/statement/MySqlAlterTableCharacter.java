/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
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
/*    */ public class MySqlAlterTableCharacter
/*    */   extends MySqlObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/*    */   private SQLExpr characterSet;
/*    */   private SQLExpr collate;
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 30 */     if (visitor.visit(this)) {
/* 31 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.characterSet);
/* 32 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.collate);
/*    */     } 
/* 34 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLExpr getCharacterSet() {
/* 38 */     return this.characterSet;
/*    */   }
/*    */   
/*    */   public void setCharacterSet(SQLExpr characterSet) {
/* 42 */     this.characterSet = characterSet;
/*    */   }
/*    */   
/*    */   public SQLExpr getCollate() {
/* 46 */     return this.collate;
/*    */   }
/*    */   
/*    */   public void setCollate(SQLExpr collate) {
/* 50 */     this.collate = collate;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlAlterTableCharacter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */