/*     */ package com.tranboot.client.druid.sql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatementImpl;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

import java.util.ArrayList;
import java.util.List;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */

/*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SQLCreateIndexStatement
/*     */   extends SQLStatementImpl
/*     */   implements SQLDDLStatement
/*     */ {
/*     */   private SQLName name;
/*     */   private SQLTableSource table;
/*  31 */   private List<SQLSelectOrderByItem> items = new ArrayList<>();
/*     */ 
/*     */   
/*     */   private String type;
/*     */   
/*     */   private String using;
/*     */ 
/*     */   
/*     */   public SQLCreateIndexStatement() {}
/*     */ 
/*     */   
/*     */   public SQLCreateIndexStatement(String dbType) {
/*  43 */     super(dbType);
/*     */   }
/*     */   
/*     */   public SQLTableSource getTable() {
/*  47 */     return this.table;
/*     */   }
/*     */   
/*     */   public void setTable(SQLName table) {
/*  51 */     setTable(new SQLExprTableSource((SQLExpr)table));
/*     */   }
/*     */   
/*     */   public void setTable(SQLTableSource table) {
/*  55 */     this.table = table;
/*     */   }
/*     */   
/*     */   public List<SQLSelectOrderByItem> getItems() {
/*  59 */     return this.items;
/*     */   }
/*     */   
/*     */   public void addItem(SQLSelectOrderByItem item) {
/*  63 */     if (item != null) {
/*  64 */       item.setParent((SQLObject)this);
/*     */     }
/*  66 */     this.items.add(item);
/*     */   }
/*     */   
/*     */   public SQLName getName() {
/*  70 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(SQLName name) {
/*  74 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getType() {
/*  78 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/*  82 */     this.type = type;
/*     */   }
/*     */   
/*     */   public String getUsing() {
/*  86 */     return this.using;
/*     */   }
/*     */   
/*     */   public void setUsing(String using) {
/*  90 */     this.using = using;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  95 */     if (visitor.visit(this)) {
/*  96 */       acceptChild(visitor, (SQLObject)getName());
/*  97 */       acceptChild(visitor, getTable());
/*  98 */       acceptChild(visitor, getItems());
/*     */     } 
/* 100 */     visitor.endVisit(this);
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLCreateIndexStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */