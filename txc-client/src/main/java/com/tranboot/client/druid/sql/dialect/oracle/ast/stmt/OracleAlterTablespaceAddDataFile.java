/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

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
/*    */ public class OracleAlterTablespaceAddDataFile
/*    */   extends OracleSQLObjectImpl
/*    */   implements OracleAlterTablespaceItem
/*    */ {
/* 26 */   private List<OracleFileSpecification> files = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 30 */     if (visitor.visit(this)) {
/* 31 */       acceptChild((SQLASTVisitor)visitor, this.files);
/*    */     }
/* 33 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public List<OracleFileSpecification> getFiles() {
/* 37 */     return this.files;
/*    */   }
/*    */   
/*    */   public void setFiles(List<OracleFileSpecification> files) {
/* 41 */     this.files = files;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleAlterTablespaceAddDataFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */