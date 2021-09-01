/*      */ package com.tranboot.client.druid.sql.dialect.mysql.visitor;
/*      */ 
/*      */

import com.tranboot.client.druid.sql.ast.*;
import com.tranboot.client.druid.sql.ast.expr.SQLAggregateExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCharExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLVariantRefExpr;
import com.tranboot.client.druid.sql.ast.statement.*;
import com.tranboot.client.druid.sql.dialect.mysql.ast.*;
import com.tranboot.client.druid.sql.dialect.mysql.ast.clause.*;
import com.tranboot.client.druid.sql.dialect.mysql.ast.expr.*;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.*;
import com.tranboot.client.druid.sql.visitor.ExportParameterVisitorUtils;
import com.tranboot.client.druid.sql.visitor.ParameterizedOutputVisitorUtils;
import com.tranboot.client.druid.sql.visitor.SQLASTOutputVisitor;

import java.security.AccessControlException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */

/*      */
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class MySqlOutputVisitor
/*      */   extends SQLASTOutputVisitor
/*      */   implements MySqlASTVisitor
/*      */ {
/*      */   public MySqlOutputVisitor(Appendable appender) {
/*  182 */     super(appender);
/*      */   }
/*      */   
/*      */   public MySqlOutputVisitor(Appendable appender, boolean parameterized) {
/*  186 */     super(appender, parameterized);
/*      */     
/*      */     try {
/*  189 */       configFromProperty(System.getProperties());
/*  190 */     } catch (AccessControlException accessControlException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void configFromProperty(Properties properties) {
/*  196 */     if (this.parameterized) {
/*  197 */       String property = properties.getProperty("druid.parameterized.shardingSupport");
/*  198 */       if ("true".equals(property)) {
/*  199 */         setShardingSupport(true);
/*  200 */       } else if ("false".equals(property)) {
/*  201 */         setShardingSupport(false);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isShardingSupport() {
/*  207 */     return this.shardingSupport;
/*      */   }
/*      */   
/*      */   public void setShardingSupport(boolean shardingSupport) {
/*  211 */     this.shardingSupport = shardingSupport;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(SQLSelectQueryBlock select) {
/*  216 */     if (select instanceof MySqlSelectQueryBlock) {
/*  217 */       return visit((MySqlSelectQueryBlock)select);
/*      */     }
/*      */     
/*  220 */     return super.visit(select);
/*      */   }
/*      */   
/*      */   public boolean visit(MySqlSelectQueryBlock x) {
/*  224 */     if (x.getOrderBy() != null) {
/*  225 */       x.getOrderBy().setParent((SQLObject)x);
/*      */     }
/*      */     
/*  228 */     print0(this.ucase ? "SELECT " : "select ");
/*      */     
/*  230 */     for (int i = 0, size = x.getHintsSize(); i < size; i++) {
/*  231 */       SQLCommentHint hint = x.getHints().get(i);
/*  232 */       hint.accept(this);
/*  233 */       print(' ');
/*      */     } 
/*      */     
/*  236 */     if (1 == x.getDistionOption()) {
/*  237 */       print0(this.ucase ? "ALL " : "all ");
/*  238 */     } else if (2 == x.getDistionOption()) {
/*  239 */       print0(this.ucase ? "DISTINCT " : "distinct ");
/*  240 */     } else if (4 == x.getDistionOption()) {
/*  241 */       print0(this.ucase ? "DISTINCTROW " : "distinctrow ");
/*      */     } 
/*      */     
/*  244 */     if (x.isHignPriority()) {
/*  245 */       print0(this.ucase ? "HIGH_PRIORITY " : "high_priority ");
/*      */     }
/*      */     
/*  248 */     if (x.isStraightJoin()) {
/*  249 */       print0(this.ucase ? "STRAIGHT_JOIN " : "straight_join ");
/*      */     }
/*      */     
/*  252 */     if (x.isSmallResult()) {
/*  253 */       print0(this.ucase ? "SQL_SMALL_RESULT " : "sql_small_result ");
/*      */     }
/*      */     
/*  256 */     if (x.isBigResult()) {
/*  257 */       print0(this.ucase ? "SQL_BIG_RESULT " : "sql_big_result ");
/*      */     }
/*      */     
/*  260 */     if (x.isBufferResult()) {
/*  261 */       print0(this.ucase ? "SQL_BUFFER_RESULT " : "sql_buffer_result ");
/*      */     }
/*      */     
/*  264 */     if (x.getCache() != null) {
/*  265 */       if (x.getCache().booleanValue()) {
/*  266 */         print0(this.ucase ? "SQL_CACHE " : "sql_cache ");
/*      */       } else {
/*  268 */         print0(this.ucase ? "SQL_NO_CACHE " : "sql_no_cache ");
/*      */       } 
/*      */     }
/*      */     
/*  272 */     if (x.isCalcFoundRows()) {
/*  273 */       print0(this.ucase ? "SQL_CALC_FOUND_ROWS " : "sql_calc_found_rows ");
/*      */     }
/*      */     
/*  276 */     printSelectList(x.getSelectList());
/*      */     
/*  278 */     if (x.getInto() != null) {
/*  279 */       println();
/*  280 */       print0(this.ucase ? "INTO " : "into ");
/*  281 */       x.getInto().accept(this);
/*      */     } 
/*      */     
/*  284 */     if (x.getFrom() != null) {
/*  285 */       println();
/*  286 */       print0(this.ucase ? "FROM " : "from ");
/*  287 */       x.getFrom().accept(this);
/*      */     } 
/*      */     
/*  290 */     if (x.getWhere() != null) {
/*  291 */       println();
/*  292 */       print0(this.ucase ? "WHERE " : "where ");
/*  293 */       x.getWhere().setParent((SQLObject)x);
/*  294 */       x.getWhere().accept(this);
/*      */     } 
/*      */     
/*  297 */     if (x.getGroupBy() != null) {
/*  298 */       println();
/*  299 */       x.getGroupBy().accept(this);
/*      */     } 
/*      */     
/*  302 */     if (x.getOrderBy() != null) {
/*  303 */       println();
/*  304 */       x.getOrderBy().accept(this);
/*      */     } 
/*      */     
/*  307 */     if (x.getLimit() != null) {
/*  308 */       println();
/*  309 */       x.getLimit().accept(this);
/*      */     } 
/*      */     
/*  312 */     if (x.getProcedureName() != null) {
/*  313 */       print0(this.ucase ? " PROCEDURE " : " procedure ");
/*  314 */       x.getProcedureName().accept(this);
/*  315 */       if (!x.getProcedureArgumentList().isEmpty()) {
/*  316 */         print('(');
/*  317 */         printAndAccept(x.getProcedureArgumentList(), ", ");
/*  318 */         print(')');
/*      */       } 
/*      */     } 
/*      */     
/*  322 */     if (x.isForUpdate()) {
/*  323 */       println();
/*  324 */       print0(this.ucase ? "FOR UPDATE" : "for update");
/*  325 */       if (x.isNoWait()) {
/*  326 */         print0(this.ucase ? " NO_WAIT" : " no_wait");
/*  327 */       } else if (x.getWaitTime() != null) {
/*  328 */         print0(this.ucase ? " WAIT " : " wait ");
/*  329 */         x.getWaitTime().accept(this);
/*      */       } 
/*      */     } 
/*      */     
/*  333 */     if (x.isLockInShareMode()) {
/*  334 */       println();
/*  335 */       print0(this.ucase ? "LOCK IN SHARE MODE" : "lock in share mode");
/*      */     } 
/*      */     
/*  338 */     return false;
/*      */   }
/*      */   
/*      */   public boolean visit(SQLColumnDefinition x) {
/*  342 */     x.getName().accept(this);
/*      */     
/*  344 */     SQLDataType dataType = x.getDataType();
/*  345 */     if (dataType != null) {
/*  346 */       print(' ');
/*  347 */       dataType.accept(this);
/*      */     } 
/*      */     
/*  350 */     if (x.getCharsetExpr() != null) {
/*  351 */       print0(this.ucase ? " CHARSET " : " charset ");
/*  352 */       x.getCharsetExpr().accept(this);
/*      */     } 
/*      */     
/*  355 */     for (SQLColumnConstraint item : x.getConstraints()) {
/*  356 */       print(' ');
/*  357 */       item.accept(this);
/*      */     } 
/*      */     
/*  360 */     if (x.getDefaultExpr() != null) {
/*  361 */       print0(this.ucase ? " DEFAULT " : " default ");
/*  362 */       x.getDefaultExpr().accept(this);
/*      */     } 
/*      */     
/*  365 */     if (x.getStorage() != null) {
/*  366 */       print0(this.ucase ? " STORAGE " : " storage ");
/*  367 */       x.getStorage().accept(this);
/*      */     } 
/*      */     
/*  370 */     if (x.getOnUpdate() != null) {
/*  371 */       print0(this.ucase ? " ON UPDATE " : " on update ");
/*      */       
/*  373 */       x.getOnUpdate().accept(this);
/*      */     } 
/*      */     
/*  376 */     if (x.isAutoIncrement()) {
/*  377 */       print0(this.ucase ? " AUTO_INCREMENT" : " auto_increment");
/*      */     }
/*      */     
/*  380 */     if (x.getComment() != null) {
/*  381 */       print0(this.ucase ? " COMMENT " : " comment ");
/*  382 */       x.getComment().accept(this);
/*      */     } 
/*      */     
/*  385 */     if (x.getAsExpr() != null) {
/*  386 */       print0(this.ucase ? " AS (" : " as (");
/*  387 */       x.getAsExpr().accept(this);
/*  388 */       print(')');
/*      */     } 
/*      */     
/*  391 */     if (x.isSorted()) {
/*  392 */       print0(this.ucase ? " SORTED" : " sorted");
/*      */     }
/*      */     
/*  395 */     return false;
/*      */   }
/*      */   
/*      */   public boolean visit(SQLDataType x) {
/*  399 */     print0(x.getName());
/*  400 */     if (!x.getArguments().isEmpty()) {
/*  401 */       print('(');
/*  402 */       printAndAccept(x.getArguments(), ", ");
/*  403 */       print(')');
/*      */     } 
/*      */     
/*  406 */     if (Boolean.TRUE == x.getAttribute("UNSIGNED")) {
/*  407 */       print0(this.ucase ? " UNSIGNED" : " unsigned");
/*      */     }
/*      */     
/*  410 */     if (Boolean.TRUE == x.getAttribute("ZEROFILL")) {
/*  411 */       print0(this.ucase ? " ZEROFILL" : " zerofill");
/*      */     }
/*      */     
/*  414 */     if (x instanceof SQLCharacterDataType) {
/*  415 */       SQLCharacterDataType charType = (SQLCharacterDataType)x;
/*  416 */       if (charType.getCharSetName() != null) {
/*  417 */         print0(this.ucase ? " CHARACTER SET " : " character set ");
/*  418 */         print0(charType.getCharSetName());
/*      */         
/*  420 */         if (charType.getCollate() != null) {
/*  421 */           print0(this.ucase ? " COLLATE " : " collate ");
/*  422 */           print0(charType.getCollate());
/*      */         } 
/*      */       } 
/*      */     } 
/*  426 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(SQLCharacterDataType x) {
/*  431 */     print0(x.getName());
/*  432 */     if (!x.getArguments().isEmpty()) {
/*  433 */       print('(');
/*  434 */       printAndAccept(x.getArguments(), ", ");
/*  435 */       print(')');
/*      */     } 
/*      */     
/*  438 */     if (x.isHasBinary()) {
/*  439 */       print0(this.ucase ? " BINARY " : " binary ");
/*      */     }
/*      */     
/*  442 */     if (x.getCharSetName() != null) {
/*  443 */       print0(this.ucase ? " CHARACTER SET " : " character set ");
/*  444 */       print0(x.getCharSetName());
/*  445 */       if (x.getCollate() != null) {
/*  446 */         print0(this.ucase ? " COLLATE " : " collate ");
/*  447 */         print0(x.getCollate());
/*      */       } 
/*  449 */     } else if (x.getCollate() != null) {
/*  450 */       print0(this.ucase ? " COLLATE " : " collate ");
/*  451 */       print0(x.getCollate());
/*      */     } 
/*      */     
/*  454 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlTableIndex x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlTableIndex x) {
/*  464 */     print0(this.ucase ? "INDEX" : "index");
/*  465 */     if (x.getName() != null) {
/*  466 */       print(' ');
/*  467 */       x.getName().accept(this);
/*      */     } 
/*      */     
/*  470 */     if (x.getIndexType() != null) {
/*  471 */       print0(this.ucase ? " USING " : " using ");
/*  472 */       print0(x.getIndexType());
/*      */     } 
/*      */     
/*  475 */     print('(');
/*  476 */     for (int i = 0, size = x.getColumns().size(); i < size; i++) {
/*  477 */       if (i != 0) {
/*  478 */         print0(", ");
/*      */       }
/*  480 */       ((SQLExpr)x.getColumns().get(i)).accept(this);
/*      */     } 
/*  482 */     print(')');
/*  483 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCreateTableStatement x) {
/*  488 */     print0(this.ucase ? "CREATE " : "create ");
/*      */     
/*  490 */     for (SQLCommentHint hint : x.getHints()) {
/*  491 */       hint.accept(this);
/*  492 */       print(' ');
/*      */     } 
/*      */     
/*  495 */     if (SQLCreateTableStatement.Type.GLOBAL_TEMPORARY.equals(x.getType())) {
/*  496 */       print0(this.ucase ? "TEMPORARY TABLE " : "temporary table ");
/*      */     } else {
/*  498 */       print0(this.ucase ? "TABLE " : "table ");
/*      */     } 
/*      */     
/*  501 */     if (x.isIfNotExiists()) {
/*  502 */       print0(this.ucase ? "IF NOT EXISTS " : "if not exists ");
/*      */     }
/*      */     
/*  505 */     printTableSourceExpr((SQLExpr)x.getName());
/*      */     
/*  507 */     if (x.getLike() != null) {
/*  508 */       print0(this.ucase ? " LIKE " : " like ");
/*  509 */       x.getLike().accept(this);
/*      */     } 
/*      */     
/*  512 */     int size = x.getTableElementList().size();
/*  513 */     if (size > 0) {
/*  514 */       print0(" (");
/*  515 */       incrementIndent();
/*  516 */       println();
/*  517 */       for (int i = 0; i < size; i++) {
/*  518 */         if (i != 0) {
/*  519 */           print0(", ");
/*  520 */           println();
/*      */         } 
/*  522 */         ((SQLTableElement)x.getTableElementList().get(i)).accept(this);
/*      */       } 
/*  524 */       decrementIndent();
/*  525 */       println();
/*  526 */       print(')');
/*      */     } 
/*      */     
/*  529 */     for (Map.Entry<String, SQLObject> option : (Iterable<Map.Entry<String, SQLObject>>)x.getTableOptions().entrySet()) {
/*  530 */       String key = option.getKey();
/*      */       
/*  532 */       print(' ');
/*  533 */       print0(this.ucase ? key : key.toLowerCase());
/*      */       
/*  535 */       if ("TABLESPACE".equals(key)) {
/*  536 */         print(' ');
/*  537 */         ((SQLObject)option.getValue()).accept(this); continue;
/*      */       } 
/*  539 */       if ("UNION".equals(key)) {
/*  540 */         print0(" = (");
/*  541 */         ((SQLObject)option.getValue()).accept(this);
/*  542 */         print(')');
/*      */         
/*      */         continue;
/*      */       } 
/*  546 */       print0(" = ");
/*      */       
/*  548 */       ((SQLObject)option.getValue()).accept(this);
/*      */     } 
/*      */     
/*  551 */     if (x.getPartitioning() != null) {
/*  552 */       println();
/*  553 */       x.getPartitioning().accept(this);
/*      */     } 
/*      */     
/*  556 */     if (x.getTableGroup() != null) {
/*  557 */       println();
/*  558 */       print0(this.ucase ? "TABLEGROUP " : "tablegroup ");
/*  559 */       x.getTableGroup().accept(this);
/*      */     } 
/*      */     
/*  562 */     if (x.getSelect() != null) {
/*  563 */       incrementIndent();
/*  564 */       println();
/*  565 */       x.getSelect().accept(this);
/*  566 */       decrementIndent();
/*      */     } 
/*      */     
/*  569 */     for (SQLCommentHint hint : x.getOptionHints()) {
/*  570 */       print(' ');
/*  571 */       hint.accept(this);
/*      */     } 
/*  573 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlKey x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlPrimaryKey x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MysqlForeignKey x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlKey x) {
/*  593 */     if (x.isHasConstaint()) {
/*  594 */       print0(this.ucase ? "CONSTRAINT " : "constraint ");
/*  595 */       if (x.getName() != null) {
/*  596 */         x.getName().accept(this);
/*  597 */         print(' ');
/*      */       } 
/*      */     } 
/*      */     
/*  601 */     print0(this.ucase ? "KEY" : "key");
/*      */     
/*  603 */     if (x.getIndexName() != null) {
/*  604 */       print(' ');
/*  605 */       x.getIndexName().accept(this);
/*      */     } 
/*      */     
/*  608 */     if (x.getIndexType() != null) {
/*  609 */       print0(this.ucase ? " USING " : " using ");
/*  610 */       print0(x.getIndexType());
/*      */     } 
/*      */     
/*  613 */     print0(" (");
/*      */     
/*  615 */     for (int i = 0, size = x.getColumns().size(); i < size; i++) {
/*  616 */       if (i != 0) {
/*  617 */         print0(", ");
/*      */       }
/*  619 */       ((SQLExpr)x.getColumns().get(i)).accept(this);
/*      */     } 
/*  621 */     print(')');
/*      */     
/*  623 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlPrimaryKey x) {
/*  628 */     if (x.getName() != null) {
/*  629 */       print0(this.ucase ? "CONSTRAINT " : "constraint ");
/*  630 */       x.getName().accept(this);
/*  631 */       print(' ');
/*      */     } 
/*      */     
/*  634 */     print0(this.ucase ? "PRIMARY KEY" : "primary key");
/*      */     
/*  636 */     if (x.getIndexType() != null) {
/*  637 */       print0(this.ucase ? " USING " : " using ");
/*  638 */       print0(x.getIndexType());
/*      */     } 
/*      */     
/*  641 */     print0(" (");
/*      */     
/*  643 */     for (int i = 0, size = x.getColumns().size(); i < size; i++) {
/*  644 */       if (i != 0) {
/*  645 */         print0(", ");
/*      */       }
/*  647 */       ((SQLExpr)x.getColumns().get(i)).accept(this);
/*      */     } 
/*  649 */     print(')');
/*      */     
/*  651 */     return false;
/*      */   }
/*      */   
/*      */   public boolean visit(SQLCharExpr x) {
/*  655 */     if (this.parameterized && 
/*  656 */       ParameterizedOutputVisitorUtils.checkParameterize((SQLObject)x)) {
/*  657 */       print('?');
/*  658 */       incrementReplaceCunt();
/*  659 */       if (this instanceof com.tranboot.client.druid.sql.visitor.ExportParameterVisitor || this.parameters != null) {
/*  660 */         ExportParameterVisitorUtils.exportParameter(this.parameters, (SQLExpr)x);
/*      */       }
/*  662 */       return false;
/*      */     } 
/*      */     
/*  665 */     print('\'');
/*      */     
/*  667 */     String text = x.getText();
/*      */     
/*  669 */     StringBuilder buf = new StringBuilder(text.length());
/*  670 */     for (int i = 0; i < text.length(); i++) {
/*  671 */       char ch = text.charAt(i);
/*  672 */       if (ch == '\'') {
/*  673 */         buf.append('\'');
/*  674 */         buf.append('\'');
/*  675 */       } else if (ch == '\\') {
/*  676 */         buf.append('\\');
/*  677 */         buf.append('\\');
/*  678 */       } else if (ch == '\000') {
/*  679 */         buf.append('\\');
/*  680 */         buf.append('0');
/*      */       } else {
/*  682 */         buf.append(ch);
/*      */       } 
/*      */     } 
/*  685 */     if (buf.length() != text.length()) {
/*  686 */       text = buf.toString();
/*      */     }
/*      */     
/*  689 */     print0(text);
/*      */     
/*  691 */     print('\'');
/*  692 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(SQLVariantRefExpr x) {
/*  697 */     int parametersSize = getParametersSize();
/*  698 */     int index = x.getIndex();
/*      */     
/*  700 */     if (index >= 0 && index < parametersSize)
/*      */     {
/*  702 */       return super.visit(x);
/*      */     }
/*      */ 
/*      */     
/*  706 */     String varName = x.getName();
/*  707 */     if (x.isGlobal()) {
/*  708 */       print0("@@global.");
/*      */     }
/*  710 */     else if (!varName.startsWith("@") && 
/*  711 */       !varName.equals("?") && 
/*  712 */       !varName.startsWith("#") && 
/*  713 */       !varName.startsWith("$") && 
/*  714 */       !varName.startsWith(":")) {
/*      */       
/*  716 */       boolean subPartitionOption = false;
/*  717 */       if (x.getParent() != null) {
/*  718 */         subPartitionOption = x.getParent().getParent() instanceof com.tranboot.client.druid.sql.ast.SQLSubPartitionBy;
/*      */       }
/*      */       
/*  721 */       if (!subPartitionOption) {
/*  722 */         print0("@@");
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  727 */     for (int i = 0; i < x.getName().length(); i++) {
/*  728 */       char ch = x.getName().charAt(i);
/*  729 */       if (ch == '\'') {
/*  730 */         if (x.getName().startsWith("@@") && i == 2) {
/*  731 */           print(ch);
/*  732 */         } else if (x.getName().startsWith("@") && i == 1) {
/*  733 */           print(ch);
/*  734 */         } else if (i != 0 && i != x.getName().length() - 1) {
/*  735 */           print0("\\'");
/*      */         } else {
/*  737 */           print(ch);
/*      */         } 
/*      */       } else {
/*  740 */         print(ch);
/*      */       } 
/*      */     } 
/*      */     
/*  744 */     String collate = (String)x.getAttribute("COLLATE");
/*  745 */     if (collate != null) {
/*  746 */       print0(this.ucase ? " COLLATE " : " collate ");
/*  747 */       print0(collate);
/*      */     } 
/*      */     
/*  750 */     return false;
/*      */   }
/*      */   
/*      */   public boolean visit(SQLMethodInvokeExpr x) {
/*  754 */     if ("SUBSTRING".equalsIgnoreCase(x.getMethodName())) {
/*  755 */       if (x.getOwner() != null) {
/*  756 */         x.getOwner().accept(this);
/*  757 */         print('.');
/*      */       } 
/*  759 */       print0(x.getMethodName());
/*  760 */       print('(');
/*  761 */       printAndAccept(x.getParameters(), ", ");
/*  762 */       SQLExpr from = (SQLExpr)x.getAttribute("FROM");
/*  763 */       if (from != null) {
/*  764 */         print0(this.ucase ? " FROM " : " from ");
/*  765 */         from.accept(this);
/*      */       } 
/*      */       
/*  768 */       SQLExpr forExpr = (SQLExpr)x.getAttribute("FOR");
/*  769 */       if (forExpr != null) {
/*  770 */         print0(this.ucase ? " FOR " : " for ");
/*  771 */         forExpr.accept(this);
/*      */       } 
/*  773 */       print(')');
/*      */       
/*  775 */       return false;
/*      */     } 
/*      */     
/*  778 */     if ("TRIM".equalsIgnoreCase(x.getMethodName())) {
/*  779 */       if (x.getOwner() != null) {
/*  780 */         x.getOwner().accept(this);
/*  781 */         print('.');
/*      */       } 
/*  783 */       print0(x.getMethodName());
/*  784 */       print('(');
/*      */       
/*  786 */       String trimType = (String)x.getAttribute("TRIM_TYPE");
/*  787 */       if (trimType != null) {
/*  788 */         print0(trimType);
/*  789 */         print(' ');
/*      */       } 
/*      */       
/*  792 */       printAndAccept(x.getParameters(), ", ");
/*      */       
/*  794 */       SQLExpr from = (SQLExpr)x.getAttribute("FROM");
/*  795 */       if (from != null) {
/*  796 */         print0(this.ucase ? " FROM " : " from ");
/*  797 */         from.accept(this);
/*      */       } 
/*      */       
/*  800 */       print(')');
/*      */       
/*  802 */       return false;
/*      */     } 
/*      */     
/*  805 */     if ("CONVERT".equalsIgnoreCase(x.getMethodName()) || "CHAR".equalsIgnoreCase(x.getMethodName())) {
/*  806 */       if (x.getOwner() != null) {
/*  807 */         x.getOwner().accept(this);
/*  808 */         print('.');
/*      */       } 
/*  810 */       print0(x.getMethodName());
/*  811 */       print('(');
/*  812 */       printAndAccept(x.getParameters(), ", ");
/*      */       
/*  814 */       String charset = (String)x.getAttribute("USING");
/*  815 */       if (charset != null) {
/*  816 */         print0(this.ucase ? " USING " : " using ");
/*  817 */         print0(charset);
/*      */       } 
/*  819 */       print(')');
/*  820 */       return false;
/*      */     } 
/*      */     
/*  823 */     return super.visit(x);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlIntervalExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlIntervalExpr x) {
/*  833 */     print0(this.ucase ? "INTERVAL " : "interval ");
/*  834 */     SQLExpr value = x.getValue();
/*  835 */     value.accept(this);
/*  836 */     print(' ');
/*  837 */     print0(this.ucase ? x.getUnit().name() : (x.getUnit()).name_lcase);
/*  838 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlExtractExpr x) {
/*  843 */     print0(this.ucase ? "EXTRACT(" : "extract(");
/*  844 */     print0(x.getUnit().name());
/*  845 */     print0(this.ucase ? " FROM " : " from ");
/*  846 */     x.getValue().accept(this);
/*  847 */     print(')');
/*  848 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlExtractExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlMatchAgainstExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlMatchAgainstExpr x) {
/*  863 */     print0(this.ucase ? "MATCH (" : "match (");
/*  864 */     printAndAccept(x.getColumns(), ", ");
/*  865 */     print(')');
/*      */     
/*  867 */     print0(this.ucase ? " AGAINST (" : " against (");
/*  868 */     x.getAgainst().accept(this);
/*  869 */     if (x.getSearchModifier() != null) {
/*  870 */       print(' ');
/*  871 */       print0(this.ucase ? (x.getSearchModifier()).name : (x.getSearchModifier()).name_lcase);
/*      */     } 
/*  873 */     print(')');
/*      */     
/*  875 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlPrepareStatement x) {}
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlPrepareStatement x) {
/*  884 */     print0(this.ucase ? "PREPARE " : "prepare ");
/*  885 */     x.getName().accept(this);
/*  886 */     print0(this.ucase ? " FROM " : " from ");
/*  887 */     x.getFrom().accept(this);
/*  888 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlExecuteStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlExecuteStatement x) {
/*  898 */     print0(this.ucase ? "EXECUTE " : "execute ");
/*  899 */     x.getStatementName().accept(this);
/*  900 */     if (x.getParameters().size() > 0) {
/*  901 */       print0(this.ucase ? " USING " : " using ");
/*      */       
/*  903 */       printAndAccept(x.getParameters(), ", ");
/*      */     } 
/*  905 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MysqlDeallocatePrepareStatement x) {}
/*      */ 
/*      */   
/*      */   public boolean visit(MysqlDeallocatePrepareStatement x) {
/*  914 */     print0(this.ucase ? "DEALLOCATE PREPARE " : "deallocate prepare ");
/*  915 */     x.getStatementName().accept(this);
/*  916 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlDeleteStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlDeleteStatement x) {
/*  926 */     print0(this.ucase ? "DELETE " : "delete ");
/*      */     
/*  928 */     for (int i = 0, size = x.getHintsSize(); i < size; i++) {
/*  929 */       SQLCommentHint hint = x.getHints().get(i);
/*  930 */       hint.accept(this);
/*  931 */       print(' ');
/*      */     } 
/*      */     
/*  934 */     if (x.isLowPriority()) {
/*  935 */       print0(this.ucase ? "LOW_PRIORITY " : "low_priority ");
/*      */     }
/*      */     
/*  938 */     if (x.isQuick()) {
/*  939 */       print0(this.ucase ? "QUICK " : "quick ");
/*      */     }
/*      */     
/*  942 */     if (x.isIgnore()) {
/*  943 */       print0(this.ucase ? "IGNORE " : "ignore ");
/*      */     }
/*      */     
/*  946 */     if (x.getFrom() == null) {
/*  947 */       print0(this.ucase ? "FROM " : "from ");
/*  948 */       x.getTableSource().accept(this);
/*      */     } else {
/*  950 */       x.getTableSource().accept(this);
/*  951 */       println();
/*  952 */       print0(this.ucase ? "FROM " : "from ");
/*  953 */       x.getFrom().accept(this);
/*      */     } 
/*      */     
/*  956 */     if (x.getUsing() != null) {
/*  957 */       println();
/*  958 */       print0(this.ucase ? "USING " : "using ");
/*  959 */       x.getUsing().accept(this);
/*      */     } 
/*      */     
/*  962 */     if (x.getWhere() != null) {
/*  963 */       println();
/*  964 */       incrementIndent();
/*  965 */       print0(this.ucase ? "WHERE " : "where ");
/*  966 */       x.getWhere().setParent((SQLObject)x);
/*  967 */       x.getWhere().accept(this);
/*  968 */       decrementIndent();
/*      */     } 
/*      */     
/*  971 */     if (x.getOrderBy() != null) {
/*  972 */       println();
/*  973 */       x.getOrderBy().accept(this);
/*      */     } 
/*      */     
/*  976 */     if (x.getLimit() != null) {
/*  977 */       println();
/*  978 */       x.getLimit().accept(this);
/*      */     } 
/*      */     
/*  981 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlInsertStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlInsertStatement x) {
/*  991 */     print0(this.ucase ? "INSERT " : "insert ");
/*      */     
/*  993 */     if (x.isLowPriority()) {
/*  994 */       print0(this.ucase ? "LOW_PRIORITY " : "low_priority ");
/*      */     }
/*      */     
/*  997 */     if (x.isDelayed()) {
/*  998 */       print0(this.ucase ? "DELAYED " : "delayed ");
/*      */     }
/*      */     
/* 1001 */     if (x.isHighPriority()) {
/* 1002 */       print0(this.ucase ? "HIGH_PRIORITY " : "high_priority ");
/*      */     }
/*      */     
/* 1005 */     if (x.isIgnore()) {
/* 1006 */       print0(this.ucase ? "IGNORE " : "ignore ");
/*      */     }
/*      */     
/* 1009 */     if (x.isRollbackOnFail()) {
/* 1010 */       print0(this.ucase ? "ROLLBACK_ON_FAIL " : "rollback_on_fail ");
/*      */     }
/*      */     
/* 1013 */     print0(this.ucase ? "INTO " : "into ");
/*      */     
/* 1015 */     x.getTableSource().accept(this);
/*      */     
/* 1017 */     if (x.getColumns().size() > 0) {
/* 1018 */       incrementIndent();
/* 1019 */       print0(" (");
/* 1020 */       for (int i = 0, size = x.getColumns().size(); i < size; i++) {
/* 1021 */         if (i != 0) {
/* 1022 */           if (i % 5 == 0) {
/* 1023 */             println();
/*      */           }
/* 1025 */           print0(", ");
/*      */         } 
/*      */         
/* 1028 */         ((SQLExpr)x.getColumns().get(i)).accept(this);
/*      */       } 
/* 1030 */       print(')');
/* 1031 */       decrementIndent();
/*      */     } 
/*      */     
/* 1034 */     if (!x.getValuesList().isEmpty()) {
/* 1035 */       println();
/* 1036 */       printValuesList(x);
/*      */     } 
/*      */     
/* 1039 */     if (x.getQuery() != null) {
/* 1040 */       println();
/* 1041 */       x.getQuery().accept(this);
/*      */     } 
/*      */     
/* 1044 */     if (x.getDuplicateKeyUpdate().size() != 0) {
/* 1045 */       println();
/* 1046 */       print0(this.ucase ? "ON DUPLICATE KEY UPDATE " : "on duplicate key update ");
/* 1047 */       for (int i = 0, size = x.getDuplicateKeyUpdate().size(); i < size; i++) {
/* 1048 */         if (i != 0) {
/* 1049 */           if (i % 5 == 0) {
/* 1050 */             println();
/*      */           }
/* 1052 */           print0(", ");
/*      */         } 
/* 1054 */         ((SQLExpr)x.getDuplicateKeyUpdate().get(i)).accept(this);
/*      */       } 
/*      */     } 
/*      */     
/* 1058 */     return false;
/*      */   }
/*      */   
/*      */   protected void printValuesList(MySqlInsertStatement x) {
/* 1062 */     List<SQLInsertStatement.ValuesClause> valuesList = x.getValuesList();
/*      */     
/* 1064 */     if (this.parameterized) {
/* 1065 */       print0(this.ucase ? "VALUES " : "values ");
/* 1066 */       incrementIndent();
/* 1067 */       ((SQLInsertStatement.ValuesClause)valuesList.get(0)).accept(this);
/* 1068 */       decrementIndent();
/* 1069 */       if (valuesList.size() > 1) {
/* 1070 */         incrementReplaceCunt();
/*      */       }
/*      */       
/*      */       return;
/*      */     } 
/* 1075 */     print0(this.ucase ? "VALUES " : "values ");
/* 1076 */     if (x.getValuesList().size() > 1) {
/* 1077 */       incrementIndent();
/*      */     }
/* 1079 */     for (int i = 0, size = valuesList.size(); i < size; i++) {
/* 1080 */       if (i != 0) {
/* 1081 */         print(',');
/* 1082 */         println();
/*      */       } 
/* 1084 */       ((SQLInsertStatement.ValuesClause)valuesList.get(i)).accept(this);
/*      */     } 
/* 1086 */     if (valuesList.size() > 1) {
/* 1087 */       decrementIndent();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlLoadDataInFileStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlLoadDataInFileStatement x) {
/* 1098 */     print0(this.ucase ? "LOAD DATA " : "load data ");
/*      */     
/* 1100 */     if (x.isLowPriority()) {
/* 1101 */       print0(this.ucase ? "LOW_PRIORITY " : "low_priority ");
/*      */     }
/*      */     
/* 1104 */     if (x.isConcurrent()) {
/* 1105 */       print0(this.ucase ? "CONCURRENT " : "concurrent ");
/*      */     }
/*      */     
/* 1108 */     if (x.isLocal()) {
/* 1109 */       print0(this.ucase ? "LOCAL " : "local ");
/*      */     }
/*      */     
/* 1112 */     print0(this.ucase ? "INFILE " : "infile ");
/*      */     
/* 1114 */     x.getFileName().accept(this);
/*      */     
/* 1116 */     if (x.isReplicate()) {
/* 1117 */       print0(this.ucase ? " REPLACE " : " replace ");
/*      */     }
/*      */     
/* 1120 */     if (x.isIgnore()) {
/* 1121 */       print0(this.ucase ? " IGNORE " : " ignore ");
/*      */     }
/*      */     
/* 1124 */     print0(this.ucase ? " INTO TABLE " : " into table ");
/* 1125 */     x.getTableName().accept(this);
/*      */     
/* 1127 */     if (x.getColumnsTerminatedBy() != null || x.getColumnsEnclosedBy() != null || x.getColumnsEscaped() != null) {
/* 1128 */       print0(this.ucase ? " COLUMNS" : " columns");
/* 1129 */       if (x.getColumnsTerminatedBy() != null) {
/* 1130 */         print0(this.ucase ? " TERMINATED BY " : " terminated by ");
/* 1131 */         x.getColumnsTerminatedBy().accept(this);
/*      */       } 
/*      */       
/* 1134 */       if (x.getColumnsEnclosedBy() != null) {
/* 1135 */         if (x.isColumnsEnclosedOptionally()) {
/* 1136 */           print0(this.ucase ? " OPTIONALLY" : " optionally");
/*      */         }
/* 1138 */         print0(this.ucase ? " ENCLOSED BY " : " enclosed by ");
/* 1139 */         x.getColumnsEnclosedBy().accept(this);
/*      */       } 
/*      */       
/* 1142 */       if (x.getColumnsEscaped() != null) {
/* 1143 */         print0(this.ucase ? " ESCAPED BY " : " escaped by ");
/* 1144 */         x.getColumnsEscaped().accept(this);
/*      */       } 
/*      */     } 
/*      */     
/* 1148 */     if (x.getLinesStartingBy() != null || x.getLinesTerminatedBy() != null) {
/* 1149 */       print0(this.ucase ? " LINES" : " lines");
/* 1150 */       if (x.getLinesStartingBy() != null) {
/* 1151 */         print0(this.ucase ? " STARTING BY " : " starting by ");
/* 1152 */         x.getLinesStartingBy().accept(this);
/*      */       } 
/*      */       
/* 1155 */       if (x.getLinesTerminatedBy() != null) {
/* 1156 */         print0(this.ucase ? " TERMINATED BY " : " terminated by ");
/* 1157 */         x.getLinesTerminatedBy().accept(this);
/*      */       } 
/*      */     } 
/*      */     
/* 1161 */     if (x.getIgnoreLinesNumber() != null) {
/* 1162 */       print0(this.ucase ? " IGNORE " : " ignore ");
/* 1163 */       x.getIgnoreLinesNumber().accept(this);
/* 1164 */       print0(this.ucase ? " LINES" : " lines");
/*      */     } 
/*      */     
/* 1167 */     if (x.getColumns().size() != 0) {
/* 1168 */       print0(" (");
/* 1169 */       printAndAccept(x.getColumns(), ", ");
/* 1170 */       print(')');
/*      */     } 
/*      */     
/* 1173 */     if (x.getSetList().size() != 0) {
/* 1174 */       print0(this.ucase ? " SET " : " set ");
/* 1175 */       printAndAccept(x.getSetList(), ", ");
/*      */     } 
/*      */     
/* 1178 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlReplaceStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlReplaceStatement x) {
/* 1188 */     print0(this.ucase ? "REPLACE " : "replace ");
/*      */     
/* 1190 */     if (x.isLowPriority()) {
/* 1191 */       print0(this.ucase ? "LOW_PRIORITY " : "low_priority ");
/*      */     }
/*      */     
/* 1194 */     if (x.isDelayed()) {
/* 1195 */       print0(this.ucase ? "DELAYED " : "delayed ");
/*      */     }
/*      */     
/* 1198 */     print0(this.ucase ? "INTO " : "into ");
/*      */     
/* 1200 */     x.getTableName().accept(this);
/*      */     
/* 1202 */     if (x.getColumns().size() > 0) {
/* 1203 */       print0(" (");
/* 1204 */       for (int i = 0, size = x.getColumns().size(); i < size; i++) {
/* 1205 */         if (i != 0) {
/* 1206 */           print0(", ");
/*      */         }
/* 1208 */         ((SQLExpr)x.getColumns().get(i)).accept(this);
/*      */       } 
/* 1210 */       print(')');
/*      */     } 
/*      */     
/* 1213 */     if (x.getValuesList().size() != 0) {
/* 1214 */       println();
/* 1215 */       print0(this.ucase ? "VALUES " : "values ");
/* 1216 */       int size = x.getValuesList().size();
/* 1217 */       if (size == 0) {
/* 1218 */         print0("()");
/*      */       } else {
/* 1220 */         for (int i = 0; i < size; i++) {
/* 1221 */           if (i != 0) {
/* 1222 */             print0(", ");
/*      */           }
/* 1224 */           ((SQLInsertStatement.ValuesClause)x.getValuesList().get(i)).accept(this);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1229 */     if (x.getQuery() != null) {
/* 1230 */       x.getQuery().accept(this);
/*      */     }
/*      */     
/* 1233 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(SQLStartTransactionStatement x) {
/* 1238 */     print0(this.ucase ? "START TRANSACTION" : "start transaction");
/* 1239 */     if (x.isConsistentSnapshot()) {
/* 1240 */       print0(this.ucase ? " WITH CONSISTENT SNAPSHOT" : " with consistent snapshot");
/*      */     }
/*      */     
/* 1243 */     if (x.getHints() != null && x.getHints().size() > 0) {
/* 1244 */       print(' ');
/* 1245 */       printAndAccept(x.getHints(), " ");
/*      */     } 
/*      */     
/* 1248 */     if (x.isBegin()) {
/* 1249 */       print0(this.ucase ? " BEGIN" : " begin");
/*      */     }
/*      */     
/* 1252 */     if (x.isWork()) {
/* 1253 */       print0(this.ucase ? " WORK" : " work");
/*      */     }
/*      */     
/* 1256 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCommitStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCommitStatement x) {
/* 1266 */     print0(this.ucase ? "COMMIT" : "commit");
/*      */     
/* 1268 */     if (x.isWork()) {
/* 1269 */       print0(this.ucase ? " WORK" : " work");
/*      */     }
/*      */     
/* 1272 */     if (x.getChain() != null) {
/* 1273 */       if (x.getChain().booleanValue()) {
/* 1274 */         print0(this.ucase ? " AND CHAIN" : " and chain");
/*      */       } else {
/* 1276 */         print0(this.ucase ? " AND NO CHAIN" : " and no chain");
/*      */       } 
/*      */     }
/*      */     
/* 1280 */     if (x.getRelease() != null) {
/* 1281 */       if (x.getRelease().booleanValue()) {
/* 1282 */         print0(this.ucase ? " AND RELEASE" : " and release");
/*      */       } else {
/* 1284 */         print0(this.ucase ? " AND NO RELEASE" : " and no release");
/*      */       } 
/*      */     }
/*      */     
/* 1288 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlRollbackStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlRollbackStatement x) {
/* 1298 */     print0(this.ucase ? "ROLLBACK" : "rollback");
/*      */     
/* 1300 */     if (x.getChain() != null) {
/* 1301 */       if (x.getChain().booleanValue()) {
/* 1302 */         print0(this.ucase ? " AND CHAIN" : " and chain");
/*      */       } else {
/* 1304 */         print0(this.ucase ? " AND NO CHAIN" : " and no chain");
/*      */       } 
/*      */     }
/*      */     
/* 1308 */     if (x.getRelease() != null) {
/* 1309 */       if (x.getRelease().booleanValue()) {
/* 1310 */         print0(this.ucase ? " AND RELEASE" : " and release");
/*      */       } else {
/* 1312 */         print0(this.ucase ? " AND NO RELEASE" : " and no release");
/*      */       } 
/*      */     }
/*      */     
/* 1316 */     if (x.getTo() != null) {
/* 1317 */       print0(this.ucase ? " TO " : " to ");
/* 1318 */       x.getTo().accept(this);
/*      */     } 
/*      */     
/* 1321 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowColumnsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowColumnsStatement x) {
/* 1331 */     if (x.isFull()) {
/* 1332 */       print0(this.ucase ? "SHOW FULL COLUMNS" : "show full columns");
/*      */     } else {
/* 1334 */       print0(this.ucase ? "SHOW COLUMNS" : "show columns");
/*      */     } 
/*      */     
/* 1337 */     if (x.getTable() != null) {
/* 1338 */       print0(this.ucase ? " FROM " : " from ");
/* 1339 */       if (x.getDatabase() != null) {
/* 1340 */         x.getDatabase().accept(this);
/* 1341 */         print('.');
/*      */       } 
/* 1343 */       x.getTable().accept(this);
/*      */     } 
/*      */     
/* 1346 */     if (x.getLike() != null) {
/* 1347 */       print0(this.ucase ? " LIKE " : " like ");
/* 1348 */       x.getLike().accept(this);
/*      */     } 
/*      */     
/* 1351 */     if (x.getWhere() != null) {
/* 1352 */       print0(this.ucase ? " WHERE " : " where ");
/* 1353 */       x.getWhere().setParent((SQLObject)x);
/* 1354 */       x.getWhere().accept(this);
/*      */     } 
/*      */     
/* 1357 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(SQLShowTablesStatement x) {
/* 1362 */     if (x.isFull()) {
/* 1363 */       print0(this.ucase ? "SHOW FULL TABLES" : "show full tables");
/*      */     } else {
/* 1365 */       print0(this.ucase ? "SHOW TABLES" : "show tables");
/*      */     } 
/*      */     
/* 1368 */     if (x.getDatabase() != null) {
/* 1369 */       print0(this.ucase ? " FROM " : " from ");
/* 1370 */       x.getDatabase().accept(this);
/*      */     } 
/*      */     
/* 1373 */     if (x.getLike() != null) {
/* 1374 */       print0(this.ucase ? " LIKE " : " like ");
/* 1375 */       x.getLike().accept(this);
/*      */     } 
/*      */     
/* 1378 */     if (x.getWhere() != null) {
/* 1379 */       print0(this.ucase ? " WHERE " : " where ");
/* 1380 */       x.getWhere().setParent((SQLObject)x);
/* 1381 */       x.getWhere().accept(this);
/*      */     } 
/*      */     
/* 1384 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowDatabasesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowDatabasesStatement x) {
/* 1394 */     print0(this.ucase ? "SHOW DATABASES" : "show databases");
/*      */     
/* 1396 */     if (x.getLike() != null) {
/* 1397 */       print0(this.ucase ? " LIKE " : " like ");
/* 1398 */       x.getLike().accept(this);
/*      */     } 
/*      */     
/* 1401 */     if (x.getWhere() != null) {
/* 1402 */       print0(this.ucase ? " WHERE " : " where ");
/* 1403 */       x.getWhere().setParent((SQLObject)x);
/* 1404 */       x.getWhere().accept(this);
/*      */     } 
/*      */     
/* 1407 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowWarningsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowWarningsStatement x) {
/* 1417 */     if (x.isCount()) {
/* 1418 */       print0(this.ucase ? "SHOW COUNT(*) WARNINGS" : "show count(*) warnings");
/*      */     } else {
/* 1420 */       print0(this.ucase ? "SHOW WARNINGS" : "show warnings");
/* 1421 */       if (x.getLimit() != null) {
/* 1422 */         print(' ');
/* 1423 */         x.getLimit().accept(this);
/*      */       } 
/*      */     } 
/*      */     
/* 1427 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowStatusStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowStatusStatement x) {
/* 1437 */     print0(this.ucase ? "SHOW " : "show ");
/*      */     
/* 1439 */     if (x.isGlobal()) {
/* 1440 */       print0(this.ucase ? "GLOBAL " : "global ");
/*      */     }
/*      */     
/* 1443 */     if (x.isSession()) {
/* 1444 */       print0(this.ucase ? "SESSION " : "session ");
/*      */     }
/*      */     
/* 1447 */     print0(this.ucase ? "STATUS" : "status");
/*      */     
/* 1449 */     if (x.getLike() != null) {
/* 1450 */       print0(this.ucase ? " LIKE " : " like ");
/* 1451 */       x.getLike().accept(this);
/*      */     } 
/*      */     
/* 1454 */     if (x.getWhere() != null) {
/* 1455 */       print0(this.ucase ? " WHERE " : " where ");
/* 1456 */       x.getWhere().setParent((SQLObject)x);
/* 1457 */       x.getWhere().accept(this);
/*      */     } 
/*      */     
/* 1460 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlLoadXmlStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlLoadXmlStatement x) {
/* 1470 */     print0(this.ucase ? "LOAD XML " : "load xml ");
/*      */     
/* 1472 */     if (x.isLowPriority()) {
/* 1473 */       print0(this.ucase ? "LOW_PRIORITY " : "low_priority ");
/*      */     }
/*      */     
/* 1476 */     if (x.isConcurrent()) {
/* 1477 */       print0(this.ucase ? "CONCURRENT " : "concurrent ");
/*      */     }
/*      */     
/* 1480 */     if (x.isLocal()) {
/* 1481 */       print0(this.ucase ? "LOCAL " : "local ");
/*      */     }
/*      */     
/* 1484 */     print0(this.ucase ? "INFILE " : "infile ");
/*      */     
/* 1486 */     x.getFileName().accept(this);
/*      */     
/* 1488 */     if (x.isReplicate()) {
/* 1489 */       print0(this.ucase ? " REPLACE " : " replace ");
/*      */     }
/*      */     
/* 1492 */     if (x.isIgnore()) {
/* 1493 */       print0(this.ucase ? " IGNORE " : " ignore ");
/*      */     }
/*      */     
/* 1496 */     print0(this.ucase ? " INTO TABLE " : " into table ");
/* 1497 */     x.getTableName().accept(this);
/*      */     
/* 1499 */     if (x.getCharset() != null) {
/* 1500 */       print0(this.ucase ? " CHARSET " : " charset ");
/* 1501 */       print0(x.getCharset());
/*      */     } 
/*      */     
/* 1504 */     if (x.getRowsIdentifiedBy() != null) {
/* 1505 */       print0(this.ucase ? " ROWS IDENTIFIED BY " : " rows identified by ");
/* 1506 */       x.getRowsIdentifiedBy().accept(this);
/*      */     } 
/*      */     
/* 1509 */     if (x.getSetList().size() != 0) {
/* 1510 */       print0(this.ucase ? " SET " : " set ");
/* 1511 */       printAndAccept(x.getSetList(), ", ");
/*      */     } 
/*      */     
/* 1514 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(CobarShowStatus x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(CobarShowStatus x) {
/* 1524 */     print0(this.ucase ? "SHOW COBAR_STATUS" : "show cobar_status");
/* 1525 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlKillStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlKillStatement x) {
/* 1535 */     if (MySqlKillStatement.Type.CONNECTION.equals(x.getType())) {
/* 1536 */       print0(this.ucase ? "KILL CONNECTION " : "kill connection ");
/* 1537 */     } else if (MySqlKillStatement.Type.QUERY.equals(x.getType())) {
/* 1538 */       print0(this.ucase ? "KILL QUERY " : "kill query ");
/*      */     } else {
/* 1540 */       print0(this.ucase ? "KILL " : "kill ");
/*      */     } 
/*      */     
/* 1543 */     printAndAccept(x.getThreadIds(), ", ");
/* 1544 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlBinlogStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlBinlogStatement x) {
/* 1554 */     print0(this.ucase ? "BINLOG " : "binlog ");
/* 1555 */     x.getExpr().accept(this);
/* 1556 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlResetStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlResetStatement x) {
/* 1566 */     print0(this.ucase ? "RESET " : "reset ");
/* 1567 */     for (int i = 0; i < x.getOptions().size(); i++) {
/* 1568 */       if (i != 0) {
/* 1569 */         print0(", ");
/*      */       }
/* 1571 */       print0(x.getOptions().get(i));
/*      */     } 
/* 1573 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCreateUserStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCreateUserStatement x) {
/* 1583 */     print0(this.ucase ? "CREATE USER " : "create user ");
/* 1584 */     printAndAccept(x.getUsers(), ", ");
/* 1585 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCreateUserStatement.UserSpecification x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCreateUserStatement.UserSpecification x) {
/* 1595 */     x.getUser().accept(this);
/*      */     
/* 1597 */     if (x.getPassword() != null) {
/* 1598 */       print0(this.ucase ? " IDENTIFIED BY " : " identified by ");
/* 1599 */       if (x.isPasswordHash()) {
/* 1600 */         print0(this.ucase ? "PASSWORD " : "password ");
/*      */       }
/* 1602 */       x.getPassword().accept(this);
/*      */     } 
/*      */     
/* 1605 */     if (x.getAuthPlugin() != null) {
/* 1606 */       print0(this.ucase ? " IDENTIFIED WITH " : " identified with ");
/* 1607 */       x.getAuthPlugin().accept(this);
/*      */     } 
/* 1609 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlPartitionByKey x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlPartitionByKey x) {
/* 1619 */     if (x.isLinear()) {
/* 1620 */       print0(this.ucase ? "PARTITION BY LINEAR KEY (" : "partition by linear key (");
/*      */     } else {
/* 1622 */       print0(this.ucase ? "PARTITION BY KEY (" : "partition by key (");
/*      */     } 
/* 1624 */     printAndAccept(x.getColumns(), ", ");
/* 1625 */     print(')');
/*      */     
/* 1627 */     printPartitionsCountAndSubPartitions((SQLPartitionBy)x);
/* 1628 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSelectQueryBlock x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlOutFileExpr x) {
/* 1643 */     print0(this.ucase ? "OUTFILE " : "outfile ");
/* 1644 */     x.getFile().accept(this);
/*      */     
/* 1646 */     if (x.getCharset() != null) {
/* 1647 */       print0(this.ucase ? " CHARACTER SET " : " character set ");
/* 1648 */       print0(x.getCharset());
/*      */     } 
/*      */     
/* 1651 */     if (x.getColumnsTerminatedBy() != null || x.getColumnsEnclosedBy() != null || x.getColumnsEscaped() != null) {
/* 1652 */       print0(this.ucase ? " COLUMNS" : " columns");
/* 1653 */       if (x.getColumnsTerminatedBy() != null) {
/* 1654 */         print0(this.ucase ? " TERMINATED BY " : " terminated by ");
/* 1655 */         x.getColumnsTerminatedBy().accept(this);
/*      */       } 
/*      */       
/* 1658 */       if (x.getColumnsEnclosedBy() != null) {
/* 1659 */         if (x.isColumnsEnclosedOptionally()) {
/* 1660 */           print0(this.ucase ? " OPTIONALLY" : " optionally");
/*      */         }
/* 1662 */         print0(this.ucase ? " ENCLOSED BY " : " enclosed by ");
/* 1663 */         x.getColumnsEnclosedBy().accept(this);
/*      */       } 
/*      */       
/* 1666 */       if (x.getColumnsEscaped() != null) {
/* 1667 */         print0(this.ucase ? " ESCAPED BY " : " escaped by ");
/* 1668 */         x.getColumnsEscaped().accept(this);
/*      */       } 
/*      */     } 
/*      */     
/* 1672 */     if (x.getLinesStartingBy() != null || x.getLinesTerminatedBy() != null) {
/* 1673 */       print0(this.ucase ? " LINES" : " lines");
/* 1674 */       if (x.getLinesStartingBy() != null) {
/* 1675 */         print0(this.ucase ? " STARTING BY " : " starting by ");
/* 1676 */         x.getLinesStartingBy().accept(this);
/*      */       } 
/*      */       
/* 1679 */       if (x.getLinesTerminatedBy() != null) {
/* 1680 */         print0(this.ucase ? " TERMINATED BY " : " terminated by ");
/* 1681 */         x.getLinesTerminatedBy().accept(this);
/*      */       } 
/*      */     } 
/*      */     
/* 1685 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlOutFileExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlDescribeStatement x) {
/* 1695 */     print0(this.ucase ? "DESC " : "desc ");
/* 1696 */     x.getObject().accept(this);
/* 1697 */     if (x.getColName() != null) {
/* 1698 */       print(' ');
/* 1699 */       x.getColName().accept(this);
/*      */     } 
/* 1701 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlDescribeStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlUpdateStatement x) {
/* 1711 */     if (x.getReturning() != null && x.getReturning().size() > 0) {
/* 1712 */       print0(this.ucase ? "SELECT " : "select ");
/* 1713 */       printAndAccept(x.getReturning(), ", ");
/* 1714 */       println();
/* 1715 */       print0(this.ucase ? "FROM " : "from ");
/*      */     } 
/*      */     
/* 1718 */     print0(this.ucase ? "UPDATE " : "update ");
/*      */     
/* 1720 */     if (x.isLowPriority()) {
/* 1721 */       print0(this.ucase ? "LOW_PRIORITY " : "low_priority ");
/*      */     }
/*      */     
/* 1724 */     if (x.isIgnore()) {
/* 1725 */       print0(this.ucase ? "IGNORE " : "ignore ");
/*      */     }
/*      */     
/* 1728 */     if (x.isCommitOnSuccess()) {
/* 1729 */       print0(this.ucase ? "COMMIT_ON_SUCCESS " : "commit_on_success ");
/*      */     }
/*      */     
/* 1732 */     if (x.isRollBackOnFail()) {
/* 1733 */       print0(this.ucase ? "ROLLBACK_ON_FAIL " : "rollback_on_fail ");
/*      */     }
/*      */     
/* 1736 */     if (x.isQueryOnPk()) {
/* 1737 */       print0(this.ucase ? "QUEUE_ON_PK " : "queue_on_pk ");
/*      */     }
/*      */     
/* 1740 */     if (x.getTargetAffectRow() != null) {
/* 1741 */       print0(this.ucase ? "TARGET_AFFECT_ROW " : "target_affect_row ");
/* 1742 */       x.getTargetAffectRow().accept(this);
/* 1743 */       print(' ');
/*      */     } 
/*      */     
/* 1746 */     x.getTableSource().accept(this);
/*      */     
/* 1748 */     println();
/* 1749 */     print0(this.ucase ? "SET " : "set ");
/* 1750 */     for (int i = 0, size = x.getItems().size(); i < size; i++) {
/* 1751 */       if (i != 0) {
/* 1752 */         print0(", ");
/*      */       }
/* 1754 */       ((SQLUpdateSetItem)x.getItems().get(i)).accept(this);
/*      */     } 
/*      */     
/* 1757 */     if (x.getWhere() != null) {
/* 1758 */       println();
/* 1759 */       incrementIndent();
/* 1760 */       print0(this.ucase ? "WHERE " : "where ");
/* 1761 */       x.getWhere().setParent((SQLObject)x);
/* 1762 */       x.getWhere().accept(this);
/* 1763 */       decrementIndent();
/*      */     } 
/*      */     
/* 1766 */     if (x.getOrderBy() != null) {
/* 1767 */       println();
/* 1768 */       x.getOrderBy().accept(this);
/*      */     } 
/*      */     
/* 1771 */     if (x.getLimit() != null) {
/* 1772 */       println();
/* 1773 */       x.getLimit().accept(this);
/*      */     } 
/* 1775 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlUpdateStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSetTransactionStatement x) {
/* 1785 */     if (x.getGlobal() == null) {
/* 1786 */       print0(this.ucase ? "SET TRANSACTION " : "set transaction ");
/* 1787 */     } else if (x.getGlobal().booleanValue()) {
/* 1788 */       print0(this.ucase ? "SET GLOBAL TRANSACTION " : "set global transaction ");
/*      */     } else {
/* 1790 */       print0(this.ucase ? "SET SESSION TRANSACTION " : "set session transaction ");
/*      */     } 
/*      */     
/* 1793 */     if (x.getIsolationLevel() != null) {
/* 1794 */       print0(this.ucase ? "ISOLATION LEVEL " : "isolation level ");
/* 1795 */       print0(x.getIsolationLevel());
/*      */     } 
/*      */     
/* 1798 */     if (x.getAccessModel() != null) {
/* 1799 */       print0(this.ucase ? "READ " : "read ");
/* 1800 */       print0(x.getAccessModel());
/*      */     } 
/*      */     
/* 1803 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSetTransactionStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSetNamesStatement x) {
/* 1813 */     print0(this.ucase ? "SET NAMES " : "set names ");
/* 1814 */     if (x.isDefault()) {
/* 1815 */       print0(this.ucase ? "DEFAULT" : "default");
/*      */     } else {
/* 1817 */       print0(x.getCharSet());
/* 1818 */       if (x.getCollate() != null) {
/* 1819 */         print0(this.ucase ? " COLLATE " : " collate ");
/* 1820 */         print0(x.getCollate());
/*      */       } 
/*      */     } 
/* 1823 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSetNamesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSetCharSetStatement x) {
/* 1833 */     print0(this.ucase ? "SET CHARACTER SET " : "set character set ");
/* 1834 */     if (x.isDefault()) {
/* 1835 */       print0(this.ucase ? "DEFAULT" : "default");
/*      */     } else {
/* 1837 */       print0(x.getCharSet());
/* 1838 */       if (x.getCollate() != null) {
/* 1839 */         print0(this.ucase ? " COLLATE " : " collate ");
/* 1840 */         print0(x.getCollate());
/*      */       } 
/*      */     } 
/* 1843 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSetCharSetStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowAuthorsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowAuthorsStatement x) {
/* 1858 */     print0(this.ucase ? "SHOW AUTHORS" : "show authors");
/* 1859 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowBinaryLogsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowBinaryLogsStatement x) {
/* 1869 */     print0(this.ucase ? "SHOW BINARY LOGS" : "show binary logs");
/* 1870 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowMasterLogsStatement x) {
/* 1875 */     print0(this.ucase ? "SHOW MASTER LOGS" : "show master logs");
/* 1876 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowMasterLogsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCollationStatement x) {
/* 1886 */     print0(this.ucase ? "SHOW COLLATION" : "show collation");
/* 1887 */     if (x.getPattern() != null) {
/* 1888 */       print0(this.ucase ? " LIKE " : " like ");
/* 1889 */       x.getPattern().accept(this);
/*      */     } 
/* 1891 */     if (x.getWhere() != null) {
/* 1892 */       print0(this.ucase ? " WHERE " : " where ");
/* 1893 */       x.getWhere().accept(this);
/*      */     } 
/* 1895 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCollationStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowBinLogEventsStatement x) {
/* 1905 */     print0(this.ucase ? "SHOW BINLOG EVENTS" : "show binlog events");
/* 1906 */     if (x.getIn() != null) {
/* 1907 */       print0(this.ucase ? " IN " : " in ");
/* 1908 */       x.getIn().accept(this);
/*      */     } 
/* 1910 */     if (x.getFrom() != null) {
/* 1911 */       print0(this.ucase ? " FROM " : " from ");
/* 1912 */       x.getFrom().accept(this);
/*      */     } 
/* 1914 */     if (x.getLimit() != null) {
/* 1915 */       print(' ');
/* 1916 */       x.getLimit().accept(this);
/*      */     } 
/* 1918 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowBinLogEventsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCharacterSetStatement x) {
/* 1928 */     print0(this.ucase ? "SHOW CHARACTER SET" : "show character set");
/* 1929 */     if (x.getPattern() != null) {
/* 1930 */       print0(this.ucase ? " LIKE " : " like ");
/* 1931 */       x.getPattern().accept(this);
/*      */     } 
/* 1933 */     if (x.getWhere() != null) {
/* 1934 */       print0(this.ucase ? " WHERE " : " where ");
/* 1935 */       x.getWhere().accept(this);
/*      */     } 
/* 1937 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCharacterSetStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowContributorsStatement x) {
/* 1947 */     print0(this.ucase ? "SHOW CONTRIBUTORS" : "show contributors");
/* 1948 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowContributorsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCreateDatabaseStatement x) {
/* 1958 */     print0(this.ucase ? "SHOW CREATE DATABASE " : "show create database ");
/* 1959 */     x.getDatabase().accept(this);
/* 1960 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCreateDatabaseStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCreateEventStatement x) {
/* 1970 */     print0(this.ucase ? "SHOW CREATE EVENT " : "show create event ");
/* 1971 */     x.getEventName().accept(this);
/* 1972 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCreateEventStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCreateFunctionStatement x) {
/* 1982 */     print0(this.ucase ? "SHOW CREATE FUNCTION " : "show create function ");
/* 1983 */     x.getName().accept(this);
/* 1984 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCreateFunctionStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCreateProcedureStatement x) {
/* 1994 */     print0(this.ucase ? "SHOW CREATE PROCEDURE " : "show create procedure ");
/* 1995 */     x.getName().accept(this);
/* 1996 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCreateProcedureStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCreateTableStatement x) {
/* 2006 */     print0(this.ucase ? "SHOW CREATE TABLE " : "show create table ");
/* 2007 */     x.getName().accept(this);
/* 2008 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCreateTableStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCreateTriggerStatement x) {
/* 2018 */     print0(this.ucase ? "SHOW CREATE TRIGGER " : "show create trigger ");
/* 2019 */     x.getName().accept(this);
/* 2020 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCreateTriggerStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowCreateViewStatement x) {
/* 2030 */     print0(this.ucase ? "SHOW CREATE VIEW " : "show create view ");
/* 2031 */     x.getName().accept(this);
/* 2032 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowCreateViewStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowEngineStatement x) {
/* 2042 */     print0(this.ucase ? "SHOW ENGINE " : "show engine ");
/* 2043 */     x.getName().accept(this);
/* 2044 */     print(' ');
/* 2045 */     print0(x.getOption().name());
/* 2046 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowEngineStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowEventsStatement x) {
/* 2056 */     print0(this.ucase ? "SHOW EVENTS" : "show events");
/* 2057 */     if (x.getSchema() != null) {
/* 2058 */       print0(this.ucase ? " FROM " : " from ");
/* 2059 */       x.getSchema().accept(this);
/*      */     } 
/*      */     
/* 2062 */     if (x.getLike() != null) {
/* 2063 */       print0(this.ucase ? " LIKE " : " like ");
/* 2064 */       x.getLike().accept(this);
/*      */     } 
/*      */     
/* 2067 */     if (x.getWhere() != null) {
/* 2068 */       print0(this.ucase ? " WHERE " : " where ");
/* 2069 */       x.getWhere().accept(this);
/*      */     } 
/*      */     
/* 2072 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowEventsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowFunctionCodeStatement x) {
/* 2082 */     print0(this.ucase ? "SHOW FUNCTION CODE " : "show function code ");
/* 2083 */     x.getName().accept(this);
/* 2084 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowFunctionCodeStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowFunctionStatusStatement x) {
/* 2094 */     print0(this.ucase ? "SHOW FUNCTION STATUS" : "show function status");
/* 2095 */     if (x.getLike() != null) {
/* 2096 */       print0(this.ucase ? " LIKE " : " like ");
/* 2097 */       x.getLike().accept(this);
/*      */     } 
/*      */     
/* 2100 */     if (x.getWhere() != null) {
/* 2101 */       print0(this.ucase ? " WHERE " : " where ");
/* 2102 */       x.getWhere().accept(this);
/*      */     } 
/*      */     
/* 2105 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowFunctionStatusStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowEnginesStatement x) {
/* 2115 */     if (x.isStorage()) {
/* 2116 */       print0(this.ucase ? "SHOW STORAGE ENGINES" : "show storage engines");
/*      */     } else {
/* 2118 */       print0(this.ucase ? "SHOW ENGINES" : "show engines");
/*      */     } 
/* 2120 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowEnginesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowErrorsStatement x) {
/* 2130 */     if (x.isCount()) {
/* 2131 */       print0(this.ucase ? "SHOW COUNT(*) ERRORS" : "show count(*) errors");
/*      */     } else {
/* 2133 */       print0(this.ucase ? "SHOW ERRORS" : "show errors");
/* 2134 */       if (x.getLimit() != null) {
/* 2135 */         print(' ');
/* 2136 */         x.getLimit().accept(this);
/*      */       } 
/*      */     } 
/* 2139 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowErrorsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowGrantsStatement x) {
/* 2149 */     print0(this.ucase ? "SHOW GRANTS" : "show grants");
/* 2150 */     if (x.getUser() != null) {
/* 2151 */       print0(this.ucase ? " FOR " : " for ");
/* 2152 */       x.getUser().accept(this);
/*      */     } 
/* 2154 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowGrantsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlUserName x) {
/* 2164 */     print0(x.getUserName());
/* 2165 */     if (x.getHost() != null) {
/* 2166 */       print('@');
/* 2167 */       print0(x.getHost());
/*      */     } 
/* 2169 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlUserName x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowIndexesStatement x) {
/* 2179 */     print0(this.ucase ? "SHOW INDEX" : "show index");
/*      */     
/* 2181 */     if (x.getTable() != null) {
/* 2182 */       print0(this.ucase ? " FROM " : " from ");
/* 2183 */       if (x.getDatabase() != null) {
/* 2184 */         x.getDatabase().accept(this);
/* 2185 */         print('.');
/*      */       } 
/* 2187 */       x.getTable().accept(this);
/*      */     } 
/*      */     
/* 2190 */     if (x.getHints() != null && x.getHints().size() > 0) {
/* 2191 */       print(' ');
/* 2192 */       printAndAccept(x.getHints(), " ");
/*      */     } 
/*      */     
/* 2195 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowIndexesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowKeysStatement x) {
/* 2205 */     print0(this.ucase ? "SHOW KEYS" : "show keys");
/*      */     
/* 2207 */     if (x.getTable() != null) {
/* 2208 */       print0(this.ucase ? " FROM " : " from ");
/* 2209 */       if (x.getDatabase() != null) {
/* 2210 */         x.getDatabase().accept(this);
/* 2211 */         print('.');
/*      */       } 
/* 2213 */       x.getTable().accept(this);
/*      */     } 
/* 2215 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowKeysStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowMasterStatusStatement x) {
/* 2225 */     print0(this.ucase ? "SHOW MASTER STATUS" : "show master status");
/* 2226 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowMasterStatusStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowOpenTablesStatement x) {
/* 2236 */     print0(this.ucase ? "SHOW OPEN TABLES" : "show open tables");
/*      */     
/* 2238 */     if (x.getDatabase() != null) {
/* 2239 */       print0(this.ucase ? " FROM " : " from ");
/* 2240 */       x.getDatabase().accept(this);
/*      */     } 
/*      */     
/* 2243 */     if (x.getLike() != null) {
/* 2244 */       print0(this.ucase ? " LIKE " : " like ");
/* 2245 */       x.getLike().accept(this);
/*      */     } 
/*      */     
/* 2248 */     if (x.getWhere() != null) {
/* 2249 */       print0(this.ucase ? " WHERE " : " where ");
/* 2250 */       x.getWhere().accept(this);
/*      */     } 
/*      */     
/* 2253 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowOpenTablesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowPluginsStatement x) {
/* 2263 */     print0(this.ucase ? "SHOW PLUGINS" : "show plugins");
/* 2264 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowPluginsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowPrivilegesStatement x) {
/* 2274 */     print0(this.ucase ? "SHOW PRIVILEGES" : "show privileges");
/* 2275 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowPrivilegesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowProcedureCodeStatement x) {
/* 2285 */     print0(this.ucase ? "SHOW PROCEDURE CODE " : "show procedure code ");
/* 2286 */     x.getName().accept(this);
/* 2287 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowProcedureCodeStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowProcedureStatusStatement x) {
/* 2297 */     print0(this.ucase ? "SHOW PROCEDURE STATUS" : "show procedure status");
/* 2298 */     if (x.getLike() != null) {
/* 2299 */       print0(this.ucase ? " LIKE " : " like ");
/* 2300 */       x.getLike().accept(this);
/*      */     } 
/*      */     
/* 2303 */     if (x.getWhere() != null) {
/* 2304 */       print0(this.ucase ? " WHERE " : " where ");
/* 2305 */       x.getWhere().accept(this);
/*      */     } 
/* 2307 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowProcedureStatusStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowProcessListStatement x) {
/* 2317 */     if (x.isFull()) {
/* 2318 */       print0(this.ucase ? "SHOW FULL PROCESSLIST" : "show full processlist");
/*      */     } else {
/* 2320 */       print0(this.ucase ? "SHOW PROCESSLIST" : "show processlist");
/*      */     } 
/* 2322 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowProcessListStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowProfileStatement x) {
/* 2332 */     print0(this.ucase ? "SHOW PROFILE" : "show profile");
/* 2333 */     for (int i = 0; i < x.getTypes().size(); i++) {
/* 2334 */       if (i == 0) {
/* 2335 */         print(' ');
/*      */       } else {
/* 2337 */         print0(", ");
/*      */       } 
/* 2339 */       print0(((MySqlShowProfileStatement.Type)x.getTypes().get(i)).name);
/*      */     } 
/*      */     
/* 2342 */     if (x.getForQuery() != null) {
/* 2343 */       print0(this.ucase ? " FOR QUERY " : " for query ");
/* 2344 */       x.getForQuery().accept(this);
/*      */     } 
/*      */     
/* 2347 */     if (x.getLimit() != null) {
/* 2348 */       print(' ');
/* 2349 */       x.getLimit().accept(this);
/*      */     } 
/* 2351 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowProfileStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowProfilesStatement x) {
/* 2361 */     print0(this.ucase ? "SHOW PROFILES" : "show profiles");
/* 2362 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowProfilesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowRelayLogEventsStatement x) {
/* 2372 */     print0("SHOW RELAYLOG EVENTS");
/*      */     
/* 2374 */     if (x.getLogName() != null) {
/* 2375 */       print0(this.ucase ? " IN " : " in ");
/* 2376 */       x.getLogName().accept(this);
/*      */     } 
/*      */     
/* 2379 */     if (x.getFrom() != null) {
/* 2380 */       print0(this.ucase ? " FROM " : " from ");
/* 2381 */       x.getFrom().accept(this);
/*      */     } 
/*      */     
/* 2384 */     if (x.getLimit() != null) {
/* 2385 */       print(' ');
/* 2386 */       x.getLimit().accept(this);
/*      */     } 
/*      */     
/* 2389 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowRelayLogEventsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowSlaveHostsStatement x) {
/* 2399 */     print0(this.ucase ? "SHOW SLAVE HOSTS" : "show slave hosts");
/* 2400 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowSlaveHostsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowSlaveStatusStatement x) {
/* 2410 */     print0(this.ucase ? "SHOW SLAVE STATUS" : "show slave status");
/* 2411 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowSlaveStatusStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowTableStatusStatement x) {
/* 2421 */     print0(this.ucase ? "SHOW TABLE STATUS" : "show table status");
/* 2422 */     if (x.getDatabase() != null) {
/* 2423 */       print0(this.ucase ? " FROM " : " from ");
/* 2424 */       x.getDatabase().accept(this);
/*      */     } 
/*      */     
/* 2427 */     if (x.getLike() != null) {
/* 2428 */       print0(this.ucase ? " LIKE " : " like ");
/* 2429 */       x.getLike().accept(this);
/*      */     } 
/*      */     
/* 2432 */     if (x.getWhere() != null) {
/* 2433 */       print0(this.ucase ? " WHERE " : " where ");
/* 2434 */       x.getWhere().accept(this);
/*      */     } 
/*      */     
/* 2437 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowTableStatusStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowTriggersStatement x) {
/* 2447 */     print0(this.ucase ? "SHOW TRIGGERS" : "show triggers");
/*      */     
/* 2449 */     if (x.getDatabase() != null) {
/* 2450 */       print0(this.ucase ? " FROM " : " from ");
/* 2451 */       x.getDatabase().accept(this);
/*      */     } 
/*      */     
/* 2454 */     if (x.getLike() != null) {
/* 2455 */       print0(this.ucase ? " LIKE " : " like ");
/* 2456 */       x.getLike().accept(this);
/*      */     } 
/*      */     
/* 2459 */     if (x.getWhere() != null) {
/* 2460 */       print0(this.ucase ? " WHERE " : " where ");
/* 2461 */       x.getWhere().setParent((SQLObject)x);
/* 2462 */       x.getWhere().accept(this);
/*      */     } 
/*      */     
/* 2465 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowTriggersStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlShowVariantsStatement x) {
/* 2475 */     print0(this.ucase ? "SHOW " : "show ");
/*      */     
/* 2477 */     if (x.isGlobal()) {
/* 2478 */       print0(this.ucase ? "GLOBAL " : "global ");
/*      */     }
/*      */     
/* 2481 */     if (x.isSession()) {
/* 2482 */       print0(this.ucase ? "SESSION " : "session ");
/*      */     }
/*      */     
/* 2485 */     print0(this.ucase ? "VARIABLES" : "variables");
/*      */     
/* 2487 */     if (x.getLike() != null) {
/* 2488 */       print0(this.ucase ? " LIKE " : " like ");
/* 2489 */       x.getLike().accept(this);
/*      */     } 
/*      */     
/* 2492 */     if (x.getWhere() != null) {
/* 2493 */       print0(this.ucase ? " WHERE " : " where ");
/* 2494 */       x.getWhere().setParent((SQLObject)x);
/* 2495 */       x.getWhere().accept(this);
/*      */     } 
/*      */     
/* 2498 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlShowVariantsStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(SQLAlterTableStatement x) {
/* 2508 */     if (x.isIgnore()) {
/* 2509 */       print0(this.ucase ? "ALTER IGNORE TABLE " : "alter ignore table ");
/*      */     } else {
/* 2511 */       print0(this.ucase ? "ALTER TABLE " : "alter table ");
/*      */     } 
/* 2513 */     printTableSourceExpr((SQLExpr)x.getName());
/* 2514 */     incrementIndent(); int i;
/* 2515 */     for (i = 0; i < x.getItems().size(); i++) {
/* 2516 */       SQLAlterTableItem item = x.getItems().get(i);
/* 2517 */       if (i != 0) {
/* 2518 */         print(',');
/*      */       }
/* 2520 */       println();
/* 2521 */       item.accept(this);
/*      */     } 
/*      */     
/* 2524 */     if (x.isRemovePatiting()) {
/* 2525 */       println();
/* 2526 */       print0(this.ucase ? "REMOVE PARTITIONING" : "remove partitioning");
/*      */     } 
/*      */     
/* 2529 */     if (x.isUpgradePatiting()) {
/* 2530 */       println();
/* 2531 */       print0(this.ucase ? "UPGRADE PARTITIONING" : "upgrade partitioning");
/*      */     } 
/*      */     
/* 2534 */     if (x.getTableOptions().size() > 0) {
/* 2535 */       println();
/*      */     }
/*      */     
/* 2538 */     decrementIndent();
/*      */     
/* 2540 */     i = 0;
/* 2541 */     for (Map.Entry<String, SQLObject> option : (Iterable<Map.Entry<String, SQLObject>>)x.getTableOptions().entrySet()) {
/* 2542 */       String key = option.getKey();
/* 2543 */       if (i != 0) {
/* 2544 */         print(' ');
/*      */       }
/* 2546 */       print0(this.ucase ? key : key.toLowerCase());
/*      */       
/* 2548 */       if ("TABLESPACE".equals(key)) {
/* 2549 */         print(' ');
/* 2550 */         ((SQLObject)option.getValue()).accept(this); continue;
/*      */       } 
/* 2552 */       if ("UNION".equals(key)) {
/* 2553 */         print0(" = (");
/* 2554 */         ((SQLObject)option.getValue()).accept(this);
/* 2555 */         print(')');
/*      */         
/*      */         continue;
/*      */       } 
/* 2559 */       print0(" = ");
/*      */       
/* 2561 */       ((SQLObject)option.getValue()).accept(this);
/* 2562 */       i++;
/*      */     } 
/*      */     
/* 2565 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(SQLAlterTableAddColumn x) {
/* 2570 */     print0(this.ucase ? "ADD COLUMN " : "add column ");
/*      */     
/* 2572 */     if (x.getColumns().size() > 1) {
/* 2573 */       print('(');
/*      */     }
/* 2575 */     printAndAccept(x.getColumns(), ", ");
/* 2576 */     if (x.getFirstColumn() != null) {
/* 2577 */       print0(this.ucase ? " FIRST " : " first ");
/* 2578 */       x.getFirstColumn().accept(this);
/* 2579 */     } else if (x.getAfterColumn() != null) {
/* 2580 */       print0(this.ucase ? " AFTER " : " after ");
/* 2581 */       x.getAfterColumn().accept(this);
/* 2582 */     } else if (x.isFirst()) {
/* 2583 */       print0(this.ucase ? " FIRST" : " first");
/*      */     } 
/*      */     
/* 2586 */     if (x.getColumns().size() > 1) {
/* 2587 */       print(')');
/*      */     }
/* 2589 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlRenameTableStatement.Item x) {
/* 2594 */     x.getName().accept(this);
/* 2595 */     print0(this.ucase ? " TO " : " to ");
/* 2596 */     x.getTo().accept(this);
/* 2597 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlRenameTableStatement.Item x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlRenameTableStatement x) {
/* 2607 */     print0(this.ucase ? "RENAME TABLE " : "rename table ");
/* 2608 */     printAndAccept(x.getItems(), ", ");
/* 2609 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlRenameTableStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlUnionQuery x) {
/* 2620 */     boolean needParen = false;
/* 2621 */     if (x.getLeft() instanceof MySqlSelectQueryBlock) {
/* 2622 */       MySqlSelectQueryBlock right = (MySqlSelectQueryBlock)x.getLeft();
/* 2623 */       if (right.getOrderBy() != null || right.getLimit() != null) {
/* 2624 */         needParen = true;
/*      */       }
/*      */     } 
/* 2627 */     if (needParen) {
/* 2628 */       print('(');
/* 2629 */       x.getLeft().accept(this);
/* 2630 */       print(')');
/*      */     } else {
/* 2632 */       x.getLeft().accept(this);
/*      */     } 
/*      */     
/* 2635 */     println();
/* 2636 */     print0(this.ucase ? (x.getOperator()).name : (x.getOperator()).name_lcase);
/* 2637 */     println();
/*      */     
/* 2639 */     needParen = false;
/*      */     
/* 2641 */     if (x.getOrderBy() != null || x.getLimit() != null) {
/* 2642 */       needParen = true;
/* 2643 */     } else if (x.getRight() instanceof MySqlSelectQueryBlock) {
/* 2644 */       MySqlSelectQueryBlock right = (MySqlSelectQueryBlock)x.getRight();
/* 2645 */       if (right.getOrderBy() != null || right.getLimit() != null) {
/* 2646 */         needParen = true;
/*      */       }
/*      */     } 
/*      */     
/* 2650 */     if (needParen) {
/* 2651 */       print('(');
/* 2652 */       x.getRight().accept(this);
/* 2653 */       print(')');
/*      */     } else {
/* 2655 */       x.getRight().accept(this);
/*      */     } 
/*      */     
/* 2658 */     if (x.getOrderBy() != null) {
/* 2659 */       println();
/* 2660 */       x.getOrderBy().accept(this);
/*      */     } 
/*      */     
/* 2663 */     if (x.getLimit() != null) {
/* 2664 */       println();
/* 2665 */       x.getLimit().accept(this);
/*      */     } 
/*      */     
/* 2668 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlUnionQuery x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlUseIndexHint x) {
/* 2678 */     print0(this.ucase ? "USE INDEX " : "use index ");
/* 2679 */     if (x.getOption() != null) {
/* 2680 */       print0(this.ucase ? "FOR " : "for ");
/* 2681 */       print0((x.getOption()).name);
/* 2682 */       print(' ');
/*      */     } 
/* 2684 */     print('(');
/* 2685 */     printAndAccept(x.getIndexList(), ", ");
/* 2686 */     print(')');
/* 2687 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlUseIndexHint x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlIgnoreIndexHint x) {
/* 2697 */     print0(this.ucase ? "IGNORE INDEX " : "ignore index ");
/* 2698 */     if (x.getOption() != null) {
/* 2699 */       print0(this.ucase ? "FOR " : "for ");
/* 2700 */       print0(this.ucase ? (x.getOption()).name : (x.getOption()).name_lcase);
/* 2701 */       print(' ');
/*      */     } 
/* 2703 */     print('(');
/* 2704 */     printAndAccept(x.getIndexList(), ", ");
/* 2705 */     print(')');
/* 2706 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlIgnoreIndexHint x) {}
/*      */ 
/*      */   
/*      */   public boolean visit(SQLExprTableSource x) {
/* 2715 */     printTableSourceExpr(x.getExpr());
/*      */     
/* 2717 */     if (x.getAlias() != null) {
/* 2718 */       print(' ');
/* 2719 */       print0(x.getAlias());
/*      */     } 
/*      */     
/* 2722 */     for (int i = 0; i < x.getHintsSize(); i++) {
/* 2723 */       print(' ');
/* 2724 */       ((SQLHint)x.getHints().get(i)).accept(this);
/*      */     } 
/*      */     
/* 2727 */     if (x.getPartitionSize() > 0) {
/* 2728 */       print0(this.ucase ? " PARTITION (" : " partition (");
/* 2729 */       printlnAndAccept(x.getPartitions(), ", ");
/* 2730 */       print(')');
/*      */     } 
/*      */     
/* 2733 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlLockTableStatement x) {
/* 2738 */     print0(this.ucase ? "LOCK TABLES " : "lock tables ");
/* 2739 */     x.getTableSource().accept(this);
/* 2740 */     if (x.getLockType() != null) {
/* 2741 */       print(' ');
/* 2742 */       print0((x.getLockType()).name);
/*      */     } 
/*      */     
/* 2745 */     if (x.getHints() != null && x.getHints().size() > 0) {
/* 2746 */       print(' ');
/* 2747 */       printAndAccept(x.getHints(), " ");
/*      */     } 
/* 2749 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlLockTableStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlUnlockTablesStatement x) {
/* 2759 */     print0(this.ucase ? "UNLOCK TABLES" : "unlock tables");
/* 2760 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlUnlockTablesStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlForceIndexHint x) {
/* 2770 */     print0(this.ucase ? "FORCE INDEX " : "force index ");
/* 2771 */     if (x.getOption() != null) {
/* 2772 */       print0(this.ucase ? "FOR " : "for ");
/* 2773 */       print0((x.getOption()).name);
/* 2774 */       print(' ');
/*      */     } 
/* 2776 */     print('(');
/* 2777 */     printAndAccept(x.getIndexList(), ", ");
/* 2778 */     print(')');
/* 2779 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlForceIndexHint x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterTableChangeColumn x) {
/* 2789 */     print0(this.ucase ? "CHANGE COLUMN " : "change column ");
/* 2790 */     x.getColumnName().accept(this);
/* 2791 */     print(' ');
/* 2792 */     x.getNewColumnDefinition().accept(this);
/* 2793 */     if (x.getFirstColumn() != null) {
/* 2794 */       print0(this.ucase ? " FIRST " : " first ");
/* 2795 */       x.getFirstColumn().accept(this);
/* 2796 */     } else if (x.getAfterColumn() != null) {
/* 2797 */       print0(this.ucase ? " AFTER " : " after ");
/* 2798 */       x.getAfterColumn().accept(this);
/* 2799 */     } else if (x.isFirst()) {
/* 2800 */       print0(this.ucase ? " FIRST" : " first");
/*      */     } 
/*      */     
/* 2803 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterTableChangeColumn x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterTableModifyColumn x) {
/* 2813 */     print0(this.ucase ? "MODIFY COLUMN " : "modify column ");
/* 2814 */     x.getNewColumnDefinition().accept(this);
/* 2815 */     if (x.getFirstColumn() != null) {
/* 2816 */       print0(this.ucase ? " FIRST " : " first ");
/* 2817 */       x.getFirstColumn().accept(this);
/* 2818 */     } else if (x.getAfterColumn() != null) {
/* 2819 */       print0(this.ucase ? " AFTER " : " after ");
/* 2820 */       x.getAfterColumn().accept(this);
/* 2821 */     } else if (x.isFirst()) {
/* 2822 */       print0(this.ucase ? " FIRST" : " first");
/*      */     } 
/*      */     
/* 2825 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterTableModifyColumn x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterTableCharacter x) {
/* 2835 */     print0(this.ucase ? "CHARACTER SET = " : "character set = ");
/* 2836 */     x.getCharacterSet().accept(this);
/*      */     
/* 2838 */     if (x.getCollate() != null) {
/* 2839 */       print0(this.ucase ? ", COLLATE = " : ", collate = ");
/* 2840 */       x.getCollate().accept(this);
/*      */     } 
/*      */     
/* 2843 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterTableCharacter x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterTableOption x) {
/* 2853 */     print0(x.getName());
/* 2854 */     print0(" = ");
/* 2855 */     print0(x.getValue().toString());
/* 2856 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterTableOption x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCreateTableStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlHelpStatement x) {
/* 2871 */     print0(this.ucase ? "HELP " : "help ");
/* 2872 */     x.getContent().accept(this);
/* 2873 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlHelpStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCharExpr x) {
/* 2883 */     if (this.parameterized && ParameterizedOutputVisitorUtils.checkParameterize((SQLObject)x)) {
/* 2884 */       print('?');
/* 2885 */       incrementReplaceCunt();
/* 2886 */       if (this instanceof com.tranboot.client.druid.sql.visitor.ExportParameterVisitor || this.parameters != null) {
/* 2887 */         ExportParameterVisitorUtils.exportParameter(this.parameters, (SQLExpr)x);
/*      */       }
/* 2889 */       return false;
/*      */     } 
/*      */     
/* 2892 */     print0(x.toString());
/* 2893 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCharExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlUnique x) {
/* 2903 */     if (x.isHasConstaint()) {
/* 2904 */       print0(this.ucase ? "CONSTRAINT " : "constraint ");
/* 2905 */       if (x.getName() != null) {
/* 2906 */         x.getName().accept(this);
/* 2907 */         print(' ');
/*      */       } 
/*      */     } 
/*      */     
/* 2911 */     print0(this.ucase ? "UNIQUE" : "unique");
/*      */     
/* 2913 */     if (x.getIndexName() != null) {
/* 2914 */       print(' ');
/* 2915 */       x.getIndexName().accept(this);
/*      */     } 
/*      */     
/* 2918 */     if (x.getIndexType() != null) {
/* 2919 */       print0(this.ucase ? " USING " : " using ");
/*      */       
/* 2921 */       print0(x.getIndexType());
/*      */     } 
/*      */     
/* 2924 */     print0(" (");
/* 2925 */     printAndAccept(x.getColumns(), ", ");
/* 2926 */     print(')');
/*      */     
/* 2928 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(MysqlForeignKey x) {
/* 2933 */     if (x.isHasConstraint()) {
/* 2934 */       print0(this.ucase ? "CONSTRAINT " : "constraint ");
/* 2935 */       if (x.getName() != null) {
/* 2936 */         x.getName().accept(this);
/* 2937 */         print(' ');
/*      */       } 
/*      */     } 
/*      */     
/* 2941 */     print0(this.ucase ? "FOREIGN KEY" : "foreign key");
/*      */     
/* 2943 */     if (x.getIndexName() != null) {
/* 2944 */       print(' ');
/* 2945 */       x.getIndexName().accept(this);
/*      */     } 
/*      */     
/* 2948 */     print0(" (");
/* 2949 */     printAndAccept(x.getReferencingColumns(), ", ");
/* 2950 */     print(')');
/*      */     
/* 2952 */     print0(this.ucase ? " REFERENCES " : " references ");
/* 2953 */     x.getReferencedTableName().accept(this);
/*      */     
/* 2955 */     print0(" (");
/* 2956 */     printAndAccept(x.getReferencedColumns(), ", ");
/* 2957 */     print(')');
/*      */     
/* 2959 */     MysqlForeignKey.Match match = x.getReferenceMatch();
/* 2960 */     if (match != null) {
/* 2961 */       print0(this.ucase ? " MATCH " : " match ");
/* 2962 */       print0(this.ucase ? match.name : match.name_lcase);
/*      */     } 
/*      */     
/* 2965 */     if (x.getOnDelete() != null) {
/* 2966 */       print0(this.ucase ? " ON DELETE " : " on delete ");
/* 2967 */       print0(this.ucase ? (x.getOnDelete()).name : (x.getOnDelete()).name_lcase);
/*      */     } 
/*      */     
/* 2970 */     if (x.getOnDelete() != null) {
/* 2971 */       print0(this.ucase ? " ON UPDATE " : " on update ");
/* 2972 */       print0(this.ucase ? (x.getOnDelete()).name : (x.getOnDelete()).name_lcase);
/*      */     } 
/* 2974 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlUnique x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterTableDiscardTablespace x) {
/* 2984 */     print0(this.ucase ? "DISCARD TABLESPACE" : "discard tablespace");
/* 2985 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterTableDiscardTablespace x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterTableImportTablespace x) {
/* 2995 */     print0(this.ucase ? "IMPORT TABLESPACE" : "import tablespace");
/* 2996 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterTableImportTablespace x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(SQLAssignItem x) {
/* 3006 */     x.getTarget().accept(this);
/* 3007 */     if (!"NAMES".equalsIgnoreCase(x.getTarget().toString())) {
/* 3008 */       print0(" = ");
/*      */     }
/* 3010 */     x.getValue().accept(this);
/* 3011 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCreateTableStatement.TableSpaceOption x) {
/* 3016 */     x.getName().accept(this);
/*      */     
/* 3018 */     if (x.getStorage() != null) {
/* 3019 */       print(' ');
/* 3020 */       x.getStorage().accept(this);
/*      */     } 
/* 3022 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCreateTableStatement.TableSpaceOption x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   protected void visitAggreateRest(SQLAggregateExpr aggregateExpr) {
/* 3032 */     SQLOrderBy sQLOrderBy = (SQLOrderBy)aggregateExpr.getAttribute("ORDER BY");
/* 3033 */     if (sQLOrderBy != null) {
/* 3034 */       print(' ');
/* 3035 */       sQLOrderBy.accept(this);
/*      */     } 
/*      */ 
/*      */     
/* 3039 */     Object value = aggregateExpr.getAttribute("SEPARATOR");
/* 3040 */     if (value != null) {
/* 3041 */       print0(this.ucase ? " SEPARATOR " : " separator ");
/* 3042 */       ((SQLObject)value).accept(this);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAnalyzeStatement x) {
/* 3049 */     print0(this.ucase ? "ANALYZE " : "analyze ");
/* 3050 */     if (x.isNoWriteToBinlog()) {
/* 3051 */       print0(this.ucase ? "NO_WRITE_TO_BINLOG " : "no_write_to_binlog ");
/*      */     }
/*      */     
/* 3054 */     if (x.isLocal()) {
/* 3055 */       print0(this.ucase ? "LOCAL " : "local ");
/*      */     }
/*      */     
/* 3058 */     print0(this.ucase ? "TABLE " : "table ");
/*      */     
/* 3060 */     printAndAccept(x.getTableSources(), ", ");
/* 3061 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAnalyzeStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlOptimizeStatement x) {
/* 3071 */     print0(this.ucase ? "OPTIMIZE " : "optimize ");
/* 3072 */     if (x.isNoWriteToBinlog()) {
/* 3073 */       print0(this.ucase ? "NO_WRITE_TO_BINLOG " : "No_write_to_binlog ");
/*      */     }
/*      */     
/* 3076 */     if (x.isLocal()) {
/* 3077 */       print0(this.ucase ? "LOCAL " : "local ");
/*      */     }
/*      */     
/* 3080 */     print0(this.ucase ? "TABLE " : "table ");
/*      */     
/* 3082 */     printAndAccept(x.getTableSources(), ", ");
/* 3083 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlOptimizeStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterUserStatement x) {
/* 3093 */     print0(this.ucase ? "ALTER USER" : "alter user");
/* 3094 */     for (SQLExpr user : x.getUsers()) {
/* 3095 */       print(' ');
/* 3096 */       user.accept(this);
/* 3097 */       print0(this.ucase ? " PASSWORD EXPIRE" : " password expire");
/*      */     } 
/* 3099 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterUserStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSetPasswordStatement x) {
/* 3109 */     print0(this.ucase ? "SET PASSWORD " : "set password ");
/*      */     
/* 3111 */     if (x.getUser() != null) {
/* 3112 */       print0(this.ucase ? "FOR " : "for ");
/* 3113 */       x.getUser().accept(this);
/* 3114 */       print(' ');
/*      */     } 
/*      */     
/* 3117 */     print0("= ");
/*      */     
/* 3119 */     if (x.getPassword() != null) {
/* 3120 */       x.getPassword().accept(this);
/*      */     }
/* 3122 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSetPasswordStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlHintStatement x) {
/* 3132 */     List<SQLCommentHint> hints = x.getHints();
/*      */     
/* 3134 */     for (SQLCommentHint hint : hints) {
/* 3135 */       hint.accept(this);
/*      */     }
/* 3137 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlHintStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlOrderingExpr x) {
/* 3147 */     x.getExpr().accept(this);
/* 3148 */     if (x.getType() != null) {
/* 3149 */       print(' ');
/* 3150 */       print0(this.ucase ? (x.getType()).name : (x.getType()).name_lcase);
/*      */     } 
/*      */     
/* 3153 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlOrderingExpr x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(SQLBlockStatement x) {
/* 3163 */     if (x.getLabelName() != null && !x.getLabelName().equals("")) {
/* 3164 */       print0(x.getLabelName());
/* 3165 */       print0(": ");
/*      */     } 
/* 3167 */     print0(this.ucase ? "BEGIN" : "begin");
/* 3168 */     incrementIndent();
/* 3169 */     println();
/* 3170 */     for (int i = 0, size = x.getStatementList().size(); i < size; i++) {
/* 3171 */       if (i != 0) {
/* 3172 */         println();
/*      */       }
/* 3174 */       SQLStatement stmt = x.getStatementList().get(i);
/* 3175 */       stmt.setParent((SQLObject)x);
/* 3176 */       stmt.accept(this);
/* 3177 */       print(';');
/*      */     } 
/* 3179 */     decrementIndent();
/* 3180 */     println();
/* 3181 */     print0(this.ucase ? "END" : "end");
/* 3182 */     if (x.getLabelName() != null && !x.getLabelName().equals("")) {
/* 3183 */       print(' ');
/* 3184 */       print0(x.getLabelName());
/*      */     } 
/* 3186 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(SQLCreateProcedureStatement x) {
/* 3194 */     if (x.isOrReplace()) {
/* 3195 */       print0(this.ucase ? "CREATE OR REPLACE PROCEDURE " : "create or replace procedure ");
/*      */     } else {
/* 3197 */       print0(this.ucase ? "CREATE PROCEDURE " : "create procedure ");
/*      */     } 
/* 3199 */     x.getName().accept(this);
/*      */     
/* 3201 */     int paramSize = x.getParameters().size();
/*      */     
/* 3203 */     print0(" (");
/* 3204 */     if (paramSize > 0) {
/* 3205 */       incrementIndent();
/* 3206 */       println();
/*      */       
/* 3208 */       for (int i = 0; i < paramSize; i++) {
/* 3209 */         if (i != 0) {
/* 3210 */           print0(", ");
/* 3211 */           println();
/*      */         } 
/* 3213 */         SQLParameter param = x.getParameters().get(i);
/* 3214 */         param.accept(this);
/*      */       } 
/*      */       
/* 3217 */       decrementIndent();
/* 3218 */       println();
/*      */     } 
/* 3220 */     print(')');
/*      */     
/* 3222 */     println();
/* 3223 */     x.getBlock().setParent((SQLObject)x);
/* 3224 */     x.getBlock().accept(this);
/* 3225 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlWhileStatement x) {
/* 3230 */     if (x.getLabelName() != null && !x.getLabelName().equals("")) {
/* 3231 */       print0(x.getLabelName());
/* 3232 */       print0(": ");
/*      */     } 
/* 3234 */     print0(this.ucase ? "WHILE " : "while ");
/* 3235 */     x.getCondition().accept(this);
/* 3236 */     print0(this.ucase ? " DO" : " do");
/* 3237 */     println();
/* 3238 */     for (int i = 0, size = x.getStatements().size(); i < size; i++) {
/* 3239 */       SQLStatement item = x.getStatements().get(i);
/* 3240 */       item.setParent((SQLObject)x);
/* 3241 */       item.accept(this);
/* 3242 */       if (i != size - 1) {
/* 3243 */         println();
/*      */       }
/*      */     } 
/* 3246 */     println();
/* 3247 */     print0(this.ucase ? "END WHILE" : "end while");
/* 3248 */     if (x.getLabelName() != null && !x.getLabelName().equals("")) print(' '); 
/* 3249 */     print0(x.getLabelName());
/* 3250 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlWhileStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(SQLIfStatement x) {
/* 3260 */     print0(this.ucase ? "IF " : "if ");
/* 3261 */     x.getCondition().accept(this);
/* 3262 */     print0(this.ucase ? " THEN" : " then");
/* 3263 */     println();
/* 3264 */     for (int i = 0, size = x.getStatements().size(); i < size; i++) {
/* 3265 */       SQLStatement item = x.getStatements().get(i);
/* 3266 */       item.setParent((SQLObject)x);
/* 3267 */       item.accept(this);
/* 3268 */       if (i != size - 1) {
/* 3269 */         println();
/*      */       }
/*      */     } 
/* 3272 */     println();
/* 3273 */     for (SQLIfStatement.ElseIf iterable_element : x.getElseIfList()) {
/* 3274 */       iterable_element.accept(this);
/*      */     }
/*      */     
/* 3277 */     if (x.getElseItem() != null) x.getElseItem().accept(this);
/*      */     
/* 3279 */     print0(this.ucase ? "END IF" : "end if");
/* 3280 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(SQLIfStatement.ElseIf x) {
/* 3285 */     print0(this.ucase ? "ELSE IF " : "else if ");
/* 3286 */     x.getCondition().accept(this);
/* 3287 */     print0(this.ucase ? " THEN" : " then");
/* 3288 */     println();
/* 3289 */     for (int i = 0, size = x.getStatements().size(); i < size; i++) {
/* 3290 */       SQLStatement item = x.getStatements().get(i);
/* 3291 */       item.setParent((SQLObject)x);
/* 3292 */       item.accept(this);
/* 3293 */       if (i != size - 1) {
/* 3294 */         println();
/*      */       }
/*      */     } 
/* 3297 */     println();
/* 3298 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(SQLIfStatement.Else x) {
/* 3303 */     print0(this.ucase ? "ELSE " : "else ");
/* 3304 */     println();
/* 3305 */     for (int i = 0, size = x.getStatements().size(); i < size; i++) {
/* 3306 */       SQLStatement item = x.getStatements().get(i);
/* 3307 */       item.setParent((SQLObject)x);
/* 3308 */       item.accept(this);
/* 3309 */       if (i != size - 1) {
/* 3310 */         println();
/*      */       }
/*      */     } 
/* 3313 */     println();
/* 3314 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCaseStatement x) {
/* 3319 */     print0(this.ucase ? "CASE " : "case ");
/* 3320 */     x.getCondition().accept(this);
/* 3321 */     println();
/* 3322 */     for (int i = 0; i < x.getWhenList().size(); i++) {
/* 3323 */       ((MySqlCaseStatement.MySqlWhenStatement)x.getWhenList().get(i)).accept(this);
/*      */     }
/* 3325 */     if (x.getElseItem() != null) x.getElseItem().accept(this); 
/* 3326 */     print0(this.ucase ? "END CASE" : "end case");
/* 3327 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCaseStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlDeclareStatement x) {
/* 3337 */     print0(this.ucase ? "DECLARE " : "declare ");
/* 3338 */     printAndAccept(x.getVarList(), ", ");
/* 3339 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlDeclareStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSelectIntoStatement x) {
/* 3349 */     x.getSelect().accept(this);
/* 3350 */     print0(this.ucase ? " INTO " : " into ");
/* 3351 */     for (int i = 0; i < x.getVarList().size(); i++) {
/* 3352 */       ((SQLExpr)x.getVarList().get(i)).accept(this);
/* 3353 */       if (i != x.getVarList().size() - 1) print0(", "); 
/*      */     } 
/* 3355 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSelectIntoStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCaseStatement.MySqlWhenStatement x) {
/* 3365 */     print0(this.ucase ? "WHEN " : "when ");
/* 3366 */     x.getCondition().accept(this);
/* 3367 */     print0(" THEN");
/* 3368 */     println();
/* 3369 */     for (int i = 0; i < x.getStatements().size(); i++) {
/* 3370 */       ((SQLStatement)x.getStatements().get(i)).accept(this);
/* 3371 */       if (i != x.getStatements().size() - 1) {
/* 3372 */         println();
/*      */       }
/*      */     } 
/* 3375 */     println();
/* 3376 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCaseStatement.MySqlWhenStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(SQLLoopStatement x) {
/* 3386 */     if (x.getLabelName() != null && !x.getLabelName().equals("")) {
/* 3387 */       print0(x.getLabelName());
/* 3388 */       print0(": ");
/*      */     } 
/*      */     
/* 3391 */     print0(this.ucase ? "LOOP " : "loop ");
/* 3392 */     println();
/* 3393 */     for (int i = 0, size = x.getStatements().size(); i < size; i++) {
/* 3394 */       SQLStatement item = x.getStatements().get(i);
/* 3395 */       item.setParent((SQLObject)x);
/* 3396 */       item.accept(this);
/* 3397 */       if (i != size - 1) {
/* 3398 */         println();
/*      */       }
/*      */     } 
/* 3401 */     println();
/* 3402 */     print0(this.ucase ? "END LOOP" : "end loop");
/* 3403 */     if (x.getLabelName() != null && !x.getLabelName().equals("")) {
/* 3404 */       print0(" ");
/* 3405 */       print0(x.getLabelName());
/*      */     } 
/* 3407 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlLeaveStatement x) {
/* 3412 */     print0(this.ucase ? "LEAVE " : "leave ");
/* 3413 */     print0(x.getLabelName());
/* 3414 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlLeaveStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlIterateStatement x) {
/* 3425 */     print0(this.ucase ? "ITERATE " : "iterate ");
/* 3426 */     print0(x.getLabelName());
/* 3427 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlIterateStatement x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlRepeatStatement x) {
/* 3439 */     if (x.getLabelName() != null && !x.getLabelName().equals("")) {
/* 3440 */       print0(x.getLabelName());
/* 3441 */       print0(": ");
/*      */     } 
/*      */     
/* 3444 */     print0(this.ucase ? "REPEAT " : "repeat ");
/* 3445 */     println();
/* 3446 */     for (int i = 0, size = x.getStatements().size(); i < size; i++) {
/* 3447 */       SQLStatement item = x.getStatements().get(i);
/* 3448 */       item.setParent((SQLObject)x);
/* 3449 */       item.accept(this);
/* 3450 */       if (i != size - 1) {
/* 3451 */         println();
/*      */       }
/*      */     } 
/* 3454 */     println();
/* 3455 */     print0(this.ucase ? "UNTIL " : "until ");
/* 3456 */     x.getCondition().accept(this);
/* 3457 */     println();
/* 3458 */     print0(this.ucase ? "END REPEAT" : "end repeat");
/* 3459 */     if (x.getLabelName() != null && !x.getLabelName().equals("")) {
/* 3460 */       print(' ');
/* 3461 */       print0(x.getLabelName());
/*      */     } 
/* 3463 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlRepeatStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlCursorDeclareStatement x) {
/* 3474 */     print0(this.ucase ? "DECLARE " : "declare ");
/* 3475 */     print0(x.getCursorName());
/* 3476 */     print0(this.ucase ? " CURSOR FOR " : " cursor for ");
/* 3477 */     x.getSelect().accept(this);
/* 3478 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlCursorDeclareStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlUpdateTableSource x) {
/* 3489 */     MySqlUpdateStatement update = x.getUpdate();
/* 3490 */     if (update != null) {
/* 3491 */       update.accept0(this);
/*      */     }
/* 3493 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlUpdateTableSource x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlAlterTableAlterColumn x) {
/* 3503 */     print0(this.ucase ? "ALTER COLUMN " : "alter column ");
/* 3504 */     x.getColumn().accept(this);
/* 3505 */     if (x.getDefaultExpr() != null) {
/* 3506 */       print0(this.ucase ? " SET DEFAULT " : " set default ");
/* 3507 */       x.getDefaultExpr().accept(this);
/* 3508 */     } else if (x.isDropDefault()) {
/* 3509 */       print0(this.ucase ? " DROP DEFAULT" : " drop default");
/*      */     } 
/* 3511 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlAlterTableAlterColumn x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSubPartitionByKey x) {
/* 3521 */     if (x.isLinear()) {
/* 3522 */       print0(this.ucase ? "SUBPARTITION BY LINEAR KEY (" : "subpartition by linear key (");
/*      */     } else {
/* 3524 */       print0(this.ucase ? "SUBPARTITION BY KEY (" : "subpartition by key (");
/*      */     } 
/* 3526 */     printAndAccept(x.getColumns(), ", ");
/* 3527 */     print(')');
/*      */     
/* 3529 */     if (x.getSubPartitionsCount() != null) {
/* 3530 */       print0(this.ucase ? " SUBPARTITIONS " : " subpartitions ");
/* 3531 */       x.getSubPartitionsCount().accept(this);
/*      */     } 
/* 3533 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSubPartitionByKey x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlSubPartitionByList x) {
/* 3543 */     print0(this.ucase ? "SUBPARTITION BY LIST " : "subpartition by list ");
/* 3544 */     if (x.getExpr() != null) {
/* 3545 */       print('(');
/* 3546 */       x.getExpr().accept(this);
/* 3547 */       print0(") ");
/*      */     } else {
/* 3549 */       if (x.getColumns().size() == 1 && Boolean.TRUE.equals(x.getAttribute("ads.subPartitionList"))) {
/* 3550 */         print('(');
/*      */       } else {
/* 3552 */         print0(this.ucase ? "COLUMNS (" : "columns (");
/*      */       } 
/* 3554 */       printAndAccept(x.getColumns(), ", ");
/* 3555 */       print(")");
/*      */     } 
/*      */     
/* 3558 */     if (x.getOptions().size() != 0) {
/* 3559 */       println();
/* 3560 */       print0(this.ucase ? "SUBPARTITION OPTIONS (" : "subpartition options (");
/* 3561 */       printAndAccept(x.getOptions(), ", ");
/* 3562 */       print(')');
/*      */     } 
/*      */     
/* 3565 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlSubPartitionByList x) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlDeclareHandlerStatement x) {
/* 3577 */     print0(this.ucase ? "DECLARE " : "declare ");
/* 3578 */     print0(this.ucase ? x.getHandleType().toString().toUpperCase() : x.getHandleType().toString().toLowerCase());
/* 3579 */     print0(this.ucase ? " HANDLER FOR " : " handler for ");
/* 3580 */     for (int i = 0; i < x.getConditionValues().size(); i++) {
/* 3581 */       ConditionValue cv = x.getConditionValues().get(i);
/* 3582 */       if (cv.getType() == ConditionValue.ConditionType.SQLSTATE) {
/* 3583 */         print0(this.ucase ? " SQLSTATE " : " sqlstate ");
/* 3584 */         print0(cv.getValue());
/* 3585 */       } else if (cv.getType() == ConditionValue.ConditionType.MYSQL_ERROR_CODE) {
/* 3586 */         print0(cv.getValue());
/* 3587 */       } else if (cv.getType() == ConditionValue.ConditionType.SELF) {
/* 3588 */         print0(cv.getValue());
/* 3589 */       } else if (cv.getType() == ConditionValue.ConditionType.SYSTEM) {
/* 3590 */         print0(this.ucase ? cv.getValue().toUpperCase() : cv.getValue().toLowerCase());
/*      */       } 
/*      */       
/* 3593 */       if (i != x.getConditionValues().size() - 1) {
/* 3594 */         print0(", ");
/*      */       }
/*      */     } 
/*      */     
/* 3598 */     println();
/* 3599 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endVisit(MySqlDeclareHandlerStatement x) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean visit(MySqlDeclareConditionStatement x) {
/* 3609 */     print0(this.ucase ? "DECLARE " : "declare ");
/* 3610 */     print0(x.getConditionName());
/* 3611 */     print0(this.ucase ? " CONDITION FOR " : " condition for ");
/*      */     
/* 3613 */     if (x.getConditionValue().getType() == ConditionValue.ConditionType.SQLSTATE) {
/* 3614 */       print0(this.ucase ? "SQLSTATE " : "sqlstate ");
/* 3615 */       print0(x.getConditionValue().getValue());
/*      */     } else {
/* 3617 */       print0(x.getConditionValue().getValue());
/*      */     } 
/*      */     
/* 3620 */     println();
/* 3621 */     return false;
/*      */   }
/*      */   
/*      */   public void endVisit(MySqlDeclareConditionStatement x) {}
/*      */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\visitor\MySqlOutputVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */