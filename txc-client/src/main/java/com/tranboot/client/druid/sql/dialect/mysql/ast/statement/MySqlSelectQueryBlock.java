/*     */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*     */ 
/*     */

import com.tranboot.client.druid.sql.ast.SQLCommentHint;
import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MySqlObject;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MySqlSelectQueryBlock
/*     */   extends SQLSelectQueryBlock
/*     */   implements MySqlObject
/*     */ {
/*     */   private boolean hignPriority;
/*     */   private boolean straightJoin;
/*     */   private boolean smallResult;
/*     */   private boolean bigResult;
/*     */   private boolean bufferResult;
/*     */   private Boolean cache;
/*     */   private boolean calcFoundRows;
/*     */   private SQLName procedureName;
/*     */   private List<SQLExpr> procedureArgumentList;
/*     */   private boolean lockInShareMode = false;
/*     */   private List<SQLCommentHint> hints;
/*     */   
/*     */   public int getHintsSize() {
/*  52 */     if (this.hints == null) {
/*  53 */       return 0;
/*     */     }
/*     */     
/*  56 */     return this.hints.size();
/*     */   }
/*     */   
/*     */   public List<SQLCommentHint> getHints() {
/*  60 */     if (this.hints == null) {
/*  61 */       this.hints = new ArrayList<>(2);
/*     */     }
/*  63 */     return this.hints;
/*     */   }
/*     */   
/*     */   public void setHints(List<SQLCommentHint> hints) {
/*  67 */     this.hints = hints;
/*     */   }
/*     */   
/*     */   public boolean isLockInShareMode() {
/*  71 */     return this.lockInShareMode;
/*     */   }
/*     */   
/*     */   public void setLockInShareMode(boolean lockInShareMode) {
/*  75 */     this.lockInShareMode = lockInShareMode;
/*     */   }
/*     */   
/*     */   public SQLName getProcedureName() {
/*  79 */     return this.procedureName;
/*     */   }
/*     */   
/*     */   public void setProcedureName(SQLName procedureName) {
/*  83 */     this.procedureName = procedureName;
/*     */   }
/*     */   
/*     */   public List<SQLExpr> getProcedureArgumentList() {
/*  87 */     if (this.procedureArgumentList == null) {
/*  88 */       this.procedureArgumentList = new ArrayList<>(2);
/*     */     }
/*  90 */     return this.procedureArgumentList;
/*     */   }
/*     */   
/*     */   public void setProcedureArgumentList(List<SQLExpr> procedureArgumentList) {
/*  94 */     this.procedureArgumentList = procedureArgumentList;
/*     */   }
/*     */   
/*     */   public boolean isHignPriority() {
/*  98 */     return this.hignPriority;
/*     */   }
/*     */   
/*     */   public void setHignPriority(boolean hignPriority) {
/* 102 */     this.hignPriority = hignPriority;
/*     */   }
/*     */   
/*     */   public boolean isStraightJoin() {
/* 106 */     return this.straightJoin;
/*     */   }
/*     */   
/*     */   public void setStraightJoin(boolean straightJoin) {
/* 110 */     this.straightJoin = straightJoin;
/*     */   }
/*     */   
/*     */   public boolean isSmallResult() {
/* 114 */     return this.smallResult;
/*     */   }
/*     */   
/*     */   public void setSmallResult(boolean smallResult) {
/* 118 */     this.smallResult = smallResult;
/*     */   }
/*     */   
/*     */   public boolean isBigResult() {
/* 122 */     return this.bigResult;
/*     */   }
/*     */   
/*     */   public void setBigResult(boolean bigResult) {
/* 126 */     this.bigResult = bigResult;
/*     */   }
/*     */   
/*     */   public boolean isBufferResult() {
/* 130 */     return this.bufferResult;
/*     */   }
/*     */   
/*     */   public void setBufferResult(boolean bufferResult) {
/* 134 */     this.bufferResult = bufferResult;
/*     */   }
/*     */   
/*     */   public Boolean getCache() {
/* 138 */     return this.cache;
/*     */   }
/*     */   
/*     */   public void setCache(Boolean cache) {
/* 142 */     this.cache = cache;
/*     */   }
/*     */   
/*     */   public boolean isCalcFoundRows() {
/* 146 */     return this.calcFoundRows;
/*     */   }
/*     */   
/*     */   public void setCalcFoundRows(boolean calcFoundRows) {
/* 150 */     this.calcFoundRows = calcFoundRows;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 155 */     int prime = 31;
/* 156 */     int result = 1;
/* 157 */     result = 31 * result + (this.bigResult ? 1231 : 1237);
/* 158 */     result = 31 * result + (this.bufferResult ? 1231 : 1237);
/* 159 */     result = 31 * result + ((this.cache == null) ? 0 : this.cache.hashCode());
/* 160 */     result = 31 * result + (this.calcFoundRows ? 1231 : 1237);
/* 161 */     result = 31 * result + (this.forUpdate ? 1231 : 1237);
/* 162 */     result = 31 * result + (this.hignPriority ? 1231 : 1237);
/* 163 */     result = 31 * result + ((this.hints == null) ? 0 : this.hints.hashCode());
/* 164 */     result = 31 * result + ((this.limit == null) ? 0 : this.limit.hashCode());
/* 165 */     result = 31 * result + (this.lockInShareMode ? 1231 : 1237);
/* 166 */     result = 31 * result + ((this.orderBy == null) ? 0 : this.orderBy.hashCode());
/* 167 */     result = 31 * result + ((this.procedureArgumentList == null) ? 0 : this.procedureArgumentList.hashCode());
/* 168 */     result = 31 * result + ((this.procedureName == null) ? 0 : this.procedureName.hashCode());
/* 169 */     result = 31 * result + (this.smallResult ? 1231 : 1237);
/* 170 */     result = 31 * result + (this.straightJoin ? 1231 : 1237);
/* 171 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 176 */     if (this == obj) return true; 
/* 177 */     if (obj == null) return false; 
/* 178 */     if (getClass() != obj.getClass()) return false; 
/* 179 */     MySqlSelectQueryBlock other = (MySqlSelectQueryBlock)obj;
/* 180 */     if (this.bigResult != other.bigResult) return false; 
/* 181 */     if (this.bufferResult != other.bufferResult) return false; 
/* 182 */     if (this.cache == null)
/* 183 */     { if (other.cache != null) return false;  }
/* 184 */     else if (!this.cache.equals(other.cache)) { return false; }
/* 185 */      if (this.calcFoundRows != other.calcFoundRows) return false; 
/* 186 */     if (this.forUpdate != other.forUpdate) return false; 
/* 187 */     if (this.hignPriority != other.hignPriority) return false; 
/* 188 */     if (this.hints == null)
/* 189 */     { if (other.hints != null) return false;  }
/* 190 */     else if (!this.hints.equals(other.hints)) { return false; }
/* 191 */      if (this.limit == null)
/* 192 */     { if (other.limit != null) return false;  }
/* 193 */     else if (!this.limit.equals(other.limit)) { return false; }
/* 194 */      if (this.lockInShareMode != other.lockInShareMode) return false; 
/* 195 */     if (this.orderBy == null)
/* 196 */     { if (other.orderBy != null) return false;  }
/* 197 */     else if (!this.orderBy.equals(other.orderBy)) { return false; }
/* 198 */      if (this.procedureArgumentList == null)
/* 199 */     { if (other.procedureArgumentList != null) return false;  }
/* 200 */     else if (!this.procedureArgumentList.equals(other.procedureArgumentList)) { return false; }
/* 201 */      if (this.procedureName == null)
/* 202 */     { if (other.procedureName != null) return false;  }
/* 203 */     else if (!this.procedureName.equals(other.procedureName)) { return false; }
/* 204 */      if (this.smallResult != other.smallResult) return false; 
/* 205 */     if (this.straightJoin != other.straightJoin) return false; 
/* 206 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void accept0(SQLASTVisitor visitor) {
/* 211 */     if (visitor instanceof MySqlASTVisitor) {
/* 212 */       accept0((MySqlASTVisitor)visitor);
/*     */       
/*     */       return;
/*     */     } 
/* 216 */     super.accept0(visitor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept0(MySqlASTVisitor visitor) {
/* 221 */     if (visitor.visit(this)) {
/* 222 */       acceptChild((SQLASTVisitor)visitor, this.selectList);
/* 223 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.from);
/* 224 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.where);
/* 225 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.groupBy);
/* 226 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.orderBy);
/* 227 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.limit);
/* 228 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.procedureName);
/* 229 */       acceptChild((SQLASTVisitor)visitor, this.procedureArgumentList);
/* 230 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.into);
/*     */     } 
/*     */     
/* 233 */     visitor.endVisit(this);
/*     */   }
/*     */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlSelectQueryBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */