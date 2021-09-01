/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLCommentHint;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatementImpl;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

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
/*    */ public class SQLCreateDatabaseStatement
/*    */   extends SQLStatementImpl
/*    */ {
/*    */   private SQLName name;
/*    */   private String characterSet;
/*    */   private String collate;
/*    */   private List<SQLCommentHint> hints;
/*    */   protected boolean ifNotExists = false;
/*    */   
/*    */   public SQLCreateDatabaseStatement() {}
/*    */   
/*    */   public SQLCreateDatabaseStatement(String dbType) {
/* 40 */     super(dbType);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 45 */     if (visitor.visit(this)) {
/* 46 */       acceptChild(visitor, (SQLObject)this.name);
/*    */     }
/* 48 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLName getName() {
/* 52 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(SQLName name) {
/* 56 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getCharacterSet() {
/* 60 */     return this.characterSet;
/*    */   }
/*    */   
/*    */   public void setCharacterSet(String characterSet) {
/* 64 */     this.characterSet = characterSet;
/*    */   }
/*    */   
/*    */   public String getCollate() {
/* 68 */     return this.collate;
/*    */   }
/*    */   
/*    */   public void setCollate(String collate) {
/* 72 */     this.collate = collate;
/*    */   }
/*    */   
/*    */   public List<SQLCommentHint> getHints() {
/* 76 */     return this.hints;
/*    */   }
/*    */   
/*    */   public void setHints(List<SQLCommentHint> hints) {
/* 80 */     this.hints = hints;
/*    */   }
/*    */   
/*    */   public boolean isIfNotExists() {
/* 84 */     return this.ifNotExists;
/*    */   }
/*    */   
/*    */   public void setIfNotExists(boolean ifNotExists) {
/* 88 */     this.ifNotExists = ifNotExists;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLCreateDatabaseStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */