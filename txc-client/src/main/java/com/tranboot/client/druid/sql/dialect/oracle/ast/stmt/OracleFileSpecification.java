/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
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
/*    */ public class OracleFileSpecification
/*    */   extends OracleSQLObjectImpl
/*    */ {
/* 27 */   private List<SQLExpr> fileNames = new ArrayList<>();
/*    */   
/*    */   private SQLExpr size;
/*    */   
/*    */   private boolean autoExtendOff = false;
/*    */   
/*    */   private SQLExpr autoExtendOn;
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 37 */     if (visitor.visit(this)) {
/* 38 */       acceptChild((SQLASTVisitor)visitor, this.fileNames);
/* 39 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.size);
/* 40 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.autoExtendOn);
/*    */     } 
/* 42 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLExpr getAutoExtendOn() {
/* 46 */     return this.autoExtendOn;
/*    */   }
/*    */   
/*    */   public void setAutoExtendOn(SQLExpr autoExtendOn) {
/* 50 */     this.autoExtendOn = autoExtendOn;
/*    */   }
/*    */   
/*    */   public SQLExpr getSize() {
/* 54 */     return this.size;
/*    */   }
/*    */   
/*    */   public void setSize(SQLExpr size) {
/* 58 */     this.size = size;
/*    */   }
/*    */   
/*    */   public boolean isAutoExtendOff() {
/* 62 */     return this.autoExtendOff;
/*    */   }
/*    */   
/*    */   public void setAutoExtendOff(boolean autoExtendOff) {
/* 66 */     this.autoExtendOff = autoExtendOff;
/*    */   }
/*    */   
/*    */   public List<SQLExpr> getFileNames() {
/* 70 */     return this.fileNames;
/*    */   }
/*    */   
/*    */   public void setFileNames(List<SQLExpr> fileNames) {
/* 74 */     this.fileNames = fileNames;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleFileSpecification.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */