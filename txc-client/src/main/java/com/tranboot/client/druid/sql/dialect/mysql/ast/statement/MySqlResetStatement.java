/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

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
/*    */ public class MySqlResetStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/* 25 */   private List<String> options = new ArrayList<>();
/*    */   
/*    */   public List<String> getOptions() {
/* 28 */     return this.options;
/*    */   }
/*    */   
/*    */   public void setOptions(List<String> options) {
/* 32 */     this.options = options;
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 36 */     visitor.visit(this);
/* 37 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlResetStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */