/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.clause;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;
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
/*    */ 
/*    */ public class CycleClause
/*    */   extends OracleSQLObjectImpl
/*    */ {
/* 27 */   private final List<SQLExpr> aliases = new ArrayList<>();
/*    */   private SQLExpr mark;
/*    */   private SQLExpr value;
/*    */   private SQLExpr defaultValue;
/*    */   
/*    */   public SQLExpr getMark() {
/* 33 */     return this.mark;
/*    */   }
/*    */   
/*    */   public void setMark(SQLExpr mark) {
/* 37 */     this.mark = mark;
/*    */   }
/*    */   
/*    */   public SQLExpr getValue() {
/* 41 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(SQLExpr value) {
/* 45 */     this.value = value;
/*    */   }
/*    */   
/*    */   public SQLExpr getDefaultValue() {
/* 49 */     return this.defaultValue;
/*    */   }
/*    */   
/*    */   public void setDefaultValue(SQLExpr defaultValue) {
/* 53 */     this.defaultValue = defaultValue;
/*    */   }
/*    */   
/*    */   public List<SQLExpr> getAliases() {
/* 57 */     return this.aliases;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 62 */     if (visitor.visit(this)) {
/* 63 */       acceptChild((SQLASTVisitor)visitor, this.aliases);
/* 64 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.mark);
/* 65 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.value);
/* 66 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.defaultValue);
/*    */     } 
/* 68 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\clause\CycleClause.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */