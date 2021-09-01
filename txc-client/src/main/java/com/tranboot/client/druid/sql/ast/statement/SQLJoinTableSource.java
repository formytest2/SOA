/*     */ package com.tranboot.client.druid.sql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*     */ public class SQLJoinTableSource
/*     */   extends SQLTableSourceImpl
/*     */ {
/*     */   protected SQLTableSource left;
/*     */   protected JoinType joinType;
/*     */   protected SQLTableSource right;
/*     */   protected SQLExpr condition;
/*  30 */   protected final List<SQLExpr> using = new ArrayList<>();
/*     */   
/*     */   public SQLJoinTableSource(String alias) {
/*  33 */     super(alias);
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLJoinTableSource() {}
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/*  41 */     if (visitor.visit(this)) {
/*  42 */       acceptChild(visitor, this.left);
/*  43 */       acceptChild(visitor, this.right);
/*  44 */       acceptChild(visitor, (SQLObject)this.condition);
/*  45 */       acceptChild(visitor, this.using);
/*     */     } 
/*     */     
/*  48 */     visitor.endVisit(this);
/*     */   }
/*     */   
/*     */   public JoinType getJoinType() {
/*  52 */     return this.joinType;
/*     */   }
/*     */   
/*     */   public void setJoinType(JoinType joinType) {
/*  56 */     this.joinType = joinType;
/*     */   }
/*     */   
/*     */   public SQLTableSource getLeft() {
/*  60 */     return this.left;
/*     */   }
/*     */   
/*     */   public void setLeft(SQLTableSource left) {
/*  64 */     if (left != null) {
/*  65 */       left.setParent(this);
/*     */     }
/*  67 */     this.left = left;
/*     */   }
/*     */   
/*     */   public SQLTableSource getRight() {
/*  71 */     return this.right;
/*     */   }
/*     */   
/*     */   public void setRight(SQLTableSource right) {
/*  75 */     if (right != null) {
/*  76 */       right.setParent(this);
/*     */     }
/*  78 */     this.right = right;
/*     */   }
/*     */   
/*     */   public SQLExpr getCondition() {
/*  82 */     return this.condition;
/*     */   }
/*     */   
/*     */   public void setCondition(SQLExpr condition) {
/*  86 */     if (condition != null) {
/*  87 */       condition.setParent(this);
/*     */     }
/*  89 */     this.condition = condition;
/*     */   }
/*     */   
/*     */   public List<SQLExpr> getUsing() {
/*  93 */     return this.using;
/*     */   }
/*     */   
/*     */   public void output(StringBuffer buf) {
/*  97 */     this.left.output(buf);
/*  98 */     buf.append(' ');
/*  99 */     buf.append(JoinType.toString(this.joinType));
/* 100 */     buf.append(' ');
/* 101 */     this.right.output(buf);
/*     */     
/* 103 */     if (this.condition != null) {
/* 104 */       buf.append(" ON ");
/* 105 */       this.condition.output(buf);
/*     */     } 
/*     */   }
/*     */   
/*     */   public enum JoinType {
/* 110 */     COMMA(","),
/* 111 */     JOIN("JOIN"),
/* 112 */     INNER_JOIN("INNER JOIN"),
/* 113 */     CROSS_JOIN("CROSS JOIN"),
/* 114 */     NATURAL_JOIN("NATURAL JOIN"),
/* 115 */     NATURAL_INNER_JOIN("NATURAL INNER JOIN"),
/* 116 */     LEFT_OUTER_JOIN("LEFT JOIN"),
/* 117 */     RIGHT_OUTER_JOIN("RIGHT JOIN"),
/* 118 */     FULL_OUTER_JOIN("FULL JOIN"),
/* 119 */     STRAIGHT_JOIN("STRAIGHT_JOIN"),
/* 120 */     OUTER_APPLY("OUTER APPLY"),
/* 121 */     CROSS_APPLY("CROSS APPLY");
/*     */     
/*     */     public final String name;
/*     */     public final String name_lcase;
/*     */     
/*     */     JoinType(String name) {
/* 127 */       this.name = name;
/* 128 */       this.name_lcase = name.toLowerCase();
/*     */     }
/*     */     
/*     */     public static String toString(JoinType joinType) {
/* 132 */       return joinType.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLJoinTableSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */