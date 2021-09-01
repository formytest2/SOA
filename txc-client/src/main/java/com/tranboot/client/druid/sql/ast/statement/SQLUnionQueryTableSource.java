/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */ import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

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
/*    */ public class SQLUnionQueryTableSource
/*    */   extends SQLTableSourceImpl
/*    */ {
/*    */   private SQLUnionQuery union;
/*    */   
/*    */   public SQLUnionQueryTableSource() {}
/*    */   
/*    */   public SQLUnionQueryTableSource(String alias) {
/* 29 */     super(alias);
/*    */   }
/*    */   
/*    */   public SQLUnionQueryTableSource(SQLUnionQuery union, String alias) {
/* 33 */     super(alias);
/* 34 */     setUnion(union);
/*    */   }
/*    */   
/*    */   public SQLUnionQueryTableSource(SQLUnionQuery union) {
/* 38 */     setUnion(union);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 43 */     if (visitor.visit(this)) {
/* 44 */       acceptChild(visitor, this.union);
/*    */     }
/* 46 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 50 */     buf.append("(");
/* 51 */     this.union.output(buf);
/* 52 */     buf.append(")");
/*    */   }
/*    */   
/*    */   public SQLUnionQuery getUnion() {
/* 56 */     return this.union;
/*    */   }
/*    */   
/*    */   public void setUnion(SQLUnionQuery union) {
/* 60 */     if (union != null) {
/* 61 */       union.setParent(this);
/*    */     }
/* 63 */     this.union = union;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLUnionQueryTableSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */