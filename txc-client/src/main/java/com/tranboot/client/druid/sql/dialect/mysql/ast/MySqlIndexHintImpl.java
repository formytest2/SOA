/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;

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
/*    */ public abstract class MySqlIndexHintImpl
/*    */   extends MySqlObjectImpl
/*    */   implements MySqlIndexHint
/*    */ {
/*    */   private MySqlIndexHint.Option option;
/* 28 */   private List<SQLName> indexList = new ArrayList<>();
/*    */
/*    */
/*    */   public abstract void accept0(MySqlASTVisitor paramMySqlASTVisitor);
/*    */
/*    */   public MySqlIndexHint.Option getOption() {
/* 34 */     return this.option;
/*    */   }
/*    */
/*    */   public void setOption(MySqlIndexHint.Option option) {
/* 38 */     this.option = option;
/*    */   }
/*    */   
/*    */   public List<SQLName> getIndexList() {
/* 42 */     return this.indexList;
/*    */   }
/*    */   
/*    */   public void setIndexList(List<SQLName> indexList) {
/* 46 */     this.indexList = indexList;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\MySqlIndexHintImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */