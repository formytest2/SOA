/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableItem;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;

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
/*    */ public class MySqlAlterTableOption
/*    */   extends MySqlObjectImpl
/*    */   implements SQLAlterTableItem
/*    */ {
/*    */   private String name;
/*    */   private Object value;
/*    */   
/*    */   public MySqlAlterTableOption(String name, Object value) {
/* 28 */     this.name = name;
/* 29 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public MySqlAlterTableOption() {}
/*    */ 
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 37 */     visitor.visit(this);
/* 38 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public String getName() {
/* 42 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 46 */     this.name = name;
/*    */   }
/*    */   
/*    */   public Object getValue() {
/* 50 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(Object value) {
/* 54 */     this.value = value;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlAlterTableOption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */