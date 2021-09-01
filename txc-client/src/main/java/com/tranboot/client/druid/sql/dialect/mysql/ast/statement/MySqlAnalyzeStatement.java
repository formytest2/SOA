/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLExprTableSource;
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
/*    */ public class MySqlAnalyzeStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/*    */   private boolean noWriteToBinlog = false;
/*    */   private boolean local = false;
/* 29 */   protected final List<SQLExprTableSource> tableSources = new ArrayList<>();
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 32 */     if (visitor.visit(this)) {
/* 33 */       acceptChild((SQLASTVisitor)visitor, this.tableSources);
/*    */     }
/* 35 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public boolean isNoWriteToBinlog() {
/* 39 */     return this.noWriteToBinlog;
/*    */   }
/*    */   
/*    */   public void setNoWriteToBinlog(boolean noWriteToBinlog) {
/* 43 */     this.noWriteToBinlog = noWriteToBinlog;
/*    */   }
/*    */   
/*    */   public boolean isLocal() {
/* 47 */     return this.local;
/*    */   }
/*    */   
/*    */   public void setLocal(boolean local) {
/* 51 */     this.local = local;
/*    */   }
/*    */   
/*    */   public List<SQLExprTableSource> getTableSources() {
/* 55 */     return this.tableSources;
/*    */   }
/*    */   
/*    */   public void addTableSource(SQLExprTableSource tableSource) {
/* 59 */     if (tableSource != null) {
/* 60 */       tableSource.setParent((SQLObject)this);
/*    */     }
/* 62 */     this.tableSources.add(tableSource);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlAnalyzeStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */