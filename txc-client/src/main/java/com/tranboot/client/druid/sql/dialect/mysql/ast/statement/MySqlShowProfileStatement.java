/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLLimit;
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
/*    */ public class MySqlShowProfileStatement
/*    */   extends MySqlStatementImpl
/*    */   implements MySqlShowStatement
/*    */ {
/* 27 */   private List<Type> types = new ArrayList<>();
/*    */   
/*    */   private SQLExpr forQuery;
/*    */   
/*    */   private SQLLimit limit;
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 34 */     visitor.visit(this);
/* 35 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public List<Type> getTypes() {
/* 39 */     return this.types;
/*    */   }
/*    */   
/*    */   public SQLExpr getForQuery() {
/* 43 */     return this.forQuery;
/*    */   }
/*    */   
/*    */   public void setForQuery(SQLExpr forQuery) {
/* 47 */     this.forQuery = forQuery;
/*    */   }
/*    */   
/*    */   public SQLLimit getLimit() {
/* 51 */     return this.limit;
/*    */   }
/*    */   
/*    */   public void setLimit(SQLLimit limit) {
/* 55 */     this.limit = limit;
/*    */   }
/*    */   
/*    */   public enum Type {
/* 59 */     ALL("ALL"), BLOCK_IO("BLOCK IO"), CONTEXT_SWITCHES("CONTEXT SWITCHES"), CPU("CPU"), IPC("IPC"),
/* 60 */     MEMORY("MEMORY"), PAGE_FAULTS("PAGE FAULTS"), SOURCE("SOURCE"), SWAPS("SWAPS");
/*    */     
/*    */     public final String name;
/*    */     
/*    */     Type(String name) {
/* 65 */       this.name = name;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlShowProfileStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */