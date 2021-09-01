/*     */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLPrimaryKey;
import com.tranboot.client.druid.sql.ast.statement.SQLTableElement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
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
/*     */ 
/*     */ public class OraclePrimaryKey
/*     */   extends OracleSQLObjectImpl
/*     */   implements OracleConstraint, SQLPrimaryKey, SQLTableElement
/*     */ {
/*     */   private SQLName name;
/*  31 */   private List<SQLExpr> columns = new ArrayList<>();
/*     */   
/*     */   private OracleUsingIndexClause using;
/*     */   
/*     */   private SQLName exceptionsInto;
/*     */   private Boolean enable;
/*     */   private OracleConstraint.Initially initially;
/*     */   private Boolean deferrable;
/*     */   
/*     */   public void accept0(OracleASTVisitor visitor) {
/*  41 */     if (visitor.visit(this)) {
/*  42 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.name);
/*  43 */       acceptChild((SQLASTVisitor)visitor, this.columns);
/*  44 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.using);
/*  45 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.exceptionsInto);
/*     */     } 
/*  47 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public Boolean getDeferrable() {
/*  51 */     return this.deferrable;
/*     */   }
/*     */   
/*     */   public void setDeferrable(Boolean deferrable) {
/*  55 */     this.deferrable = deferrable;
/*     */   }
/*     */   
/*     */   public SQLName getName() {
/*  59 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(SQLName name) {
/*  63 */     this.name = name;
/*     */   }
/*     */   
/*     */   public List<SQLExpr> getColumns() {
/*  67 */     return this.columns;
/*     */   }
/*     */   
/*     */   public void setColumns(List<SQLExpr> columns) {
/*  71 */     this.columns = columns;
/*     */   }
/*     */   
/*     */   public OracleUsingIndexClause getUsing() {
/*  75 */     return this.using;
/*     */   }
/*     */   
/*     */   public void setUsing(OracleUsingIndexClause using) {
/*  79 */     this.using = using;
/*     */   }
/*     */   
/*     */   public SQLName getExceptionsInto() {
/*  83 */     return this.exceptionsInto;
/*     */   }
/*     */   
/*     */   public void setExceptionsInto(SQLName exceptionsInto) {
/*  87 */     this.exceptionsInto = exceptionsInto;
/*     */   }
/*     */   
/*     */   public Boolean getEnable() {
/*  91 */     return this.enable;
/*     */   }
/*     */   
/*     */   public void setEnable(Boolean enable) {
/*  95 */     this.enable = enable;
/*     */   }
/*     */   
/*     */   public OracleConstraint.Initially getInitially() {
/*  99 */     return this.initially;
/*     */   }
/*     */   
/*     */   public void setInitially(OracleConstraint.Initially initially) {
/* 103 */     this.initially = initially;
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OraclePrimaryKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */