/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnDefinition;
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
/*    */ public class OracleAlterTableModify
/*    */   extends OracleAlterTableItem
/*    */ {
/* 26 */   private List<SQLColumnDefinition> columns = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 30 */     if (visitor.visit(this)) {
/* 31 */       acceptChild((SQLASTVisitor)visitor, this.columns);
/*    */     }
/* 33 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public List<SQLColumnDefinition> getColumns() {
/* 37 */     return this.columns;
/*    */   }
/*    */   
/*    */   public void addColumn(SQLColumnDefinition column) {
/* 41 */     if (column != null) {
/* 42 */       column.setParent((SQLObject)this);
/*    */     }
/* 44 */     this.columns.add(column);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleAlterTableModify.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */