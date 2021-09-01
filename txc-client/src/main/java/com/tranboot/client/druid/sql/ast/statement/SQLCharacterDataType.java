/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLDataTypeImpl;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

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
/*    */ public class SQLCharacterDataType
/*    */   extends SQLDataTypeImpl
/*    */ {
/*    */   private String charSetName;
/*    */   private String collate;
/*    */   private String charType;
/*    */   private boolean hasBinary;
/*    */   public static final String CHAR_TYPE_BYTE = "BYTE";
/*    */   public static final String CHAR_TYPE_CHAR = "CHAR";
/*    */   
/*    */   public SQLCharacterDataType(String name) {
/* 33 */     super(name);
/*    */   }
/*    */   
/*    */   public String getCharSetName() {
/* 37 */     return this.charSetName;
/*    */   }
/*    */   
/*    */   public void setCharSetName(String charSetName) {
/* 41 */     this.charSetName = charSetName;
/*    */   }
/*    */   
/*    */   public boolean isHasBinary() {
/* 45 */     return this.hasBinary;
/*    */   }
/*    */   
/*    */   public void setHasBinary(boolean hasBinary) {
/* 49 */     this.hasBinary = hasBinary;
/*    */   }
/*    */   
/*    */   public String getCollate() {
/* 53 */     return this.collate;
/*    */   }
/*    */   
/*    */   public void setCollate(String collate) {
/* 57 */     this.collate = collate;
/*    */   }
/*    */   
/*    */   public String getCharType() {
/* 61 */     return this.charType;
/*    */   }
/*    */   
/*    */   public void setCharType(String charType) {
/* 65 */     this.charType = charType;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 70 */     if (visitor.visit(this)) {
/* 71 */       acceptChild(visitor, this.arguments);
/*    */     }
/*    */     
/* 74 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLCharacterDataType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */