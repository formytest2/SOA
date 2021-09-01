/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLTableElement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
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
/*    */ public class MySqlTableIndex
/*    */   extends MySqlObjectImpl
/*    */   implements SQLTableElement
/*    */ {
/*    */   private SQLName name;
/*    */   private String indexType;
/* 31 */   private List<SQLExpr> columns = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SQLName getName() {
/* 38 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getIndexType() {
/* 42 */     return this.indexType;
/*    */   }
/*    */   
/*    */   public void setIndexType(String indexType) {
/* 46 */     this.indexType = indexType;
/*    */   }
/*    */   
/*    */   public void setName(SQLName name) {
/* 50 */     this.name = name;
/*    */   }
/*    */   
/*    */   public List<SQLExpr> getColumns() {
/* 54 */     return this.columns;
/*    */   }
/*    */   
/*    */   public void addColumn(SQLExpr column) {
/* 58 */     if (column != null) {
/* 59 */       column.setParent((SQLObject)this);
/*    */     }
/* 61 */     this.columns.add(column);
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 65 */     if (visitor.visit(this)) {
/* 66 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.name);
/* 67 */       acceptChild((SQLASTVisitor)visitor, this.columns);
/*    */     } 
/* 69 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlTableIndex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */