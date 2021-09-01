/*    */ package com.tranboot.client.druid.sql.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import com.tranboot.client.druid.util.HexBin;

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
/*    */ public class SQLHexExpr
/*    */   extends SQLExprImpl
/*    */   implements SQLLiteralExpr
/*    */ {
/*    */   private final String hex;
/*    */   
/*    */   public SQLHexExpr(String hex) {
/* 27 */     this.hex = hex;
/*    */   }
/*    */   
/*    */   public String getHex() {
/* 31 */     return this.hex;
/*    */   }
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 35 */     buf.append("0x");
/* 36 */     buf.append(this.hex);
/*    */     
/* 38 */     String charset = (String)getAttribute("USING");
/* 39 */     if (charset != null) {
/* 40 */       buf.append(" USING ");
/* 41 */       buf.append(charset);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 46 */     visitor.visit(this);
/* 47 */     visitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 52 */     int prime = 31;
/* 53 */     int result = 1;
/* 54 */     result = 31 * result + ((this.hex == null) ? 0 : this.hex.hashCode());
/* 55 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 60 */     if (this == obj) {
/* 61 */       return true;
/*    */     }
/* 63 */     if (obj == null) {
/* 64 */       return false;
/*    */     }
/* 66 */     if (getClass() != obj.getClass()) {
/* 67 */       return false;
/*    */     }
/* 69 */     SQLHexExpr other = (SQLHexExpr)obj;
/* 70 */     if (this.hex == null) {
/* 71 */       if (other.hex != null) {
/* 72 */         return false;
/*    */       }
/* 74 */     } else if (!this.hex.equals(other.hex)) {
/* 75 */       return false;
/*    */     } 
/* 77 */     return true;
/*    */   }
/*    */   
/*    */   public byte[] toBytes() {
/* 81 */     return HexBin.decode(this.hex);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLHexExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */