package com.tranboot.client.druid.sql.dialect.oracle.visitor;

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLHint;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLOrderBy;
import com.tranboot.client.druid.sql.ast.SQLParameter;
import com.tranboot.client.druid.sql.ast.SQLStatement;
import com.tranboot.client.druid.sql.ast.expr.SQLAllColumnExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCharExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLLiteralExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableItem;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLBlockStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCharacterDataType;
import com.tranboot.client.druid.sql.ast.statement.SQLCheck;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnDefinition;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateProcedureStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateTableStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.tranboot.client.druid.sql.ast.statement.SQLIfStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLInsertStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLRollbackStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLSelect;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectQuery;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.tranboot.client.druid.sql.ast.statement.SQLTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLTruncateStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLUnique;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateSetItem;
import com.tranboot.client.druid.sql.ast.statement.SQLIfStatement.Else;
import com.tranboot.client.druid.sql.ast.statement.SQLIfStatement.ElseIf;
import com.tranboot.client.druid.sql.ast.statement.SQLJoinTableSource.JoinType;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleDataTypeIntervalDay;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleDataTypeIntervalYear;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleDataTypeTimestamp;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.CycleClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.OracleLobStorageClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.OracleReturningClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.OracleStorageClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.OracleWithSubqueryEntry;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.PartitionExtensionClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.SampleClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.SearchClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.FlashbackQueryClause.AsOfFlashbackQueryClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.FlashbackQueryClause.AsOfSnapshotClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.FlashbackQueryClause.VersionsFlashbackQueryClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause.CellAssignment;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause.CellAssignmentItem;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause.CellReferenceOption;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause.MainModelClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause.ModelColumn;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause.ModelColumnClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause.ModelRuleOption;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause.ModelRulesClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause.QueryPartitionClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause.ReferenceModelClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.ModelClause.ReturnRowsClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleAnalytic;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleAnalyticWindowing;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleArgumentExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleBinaryDoubleExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleBinaryFloatExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleCursorExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleDatetimeExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleDbLinkExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleIntervalExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleIsSetExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleOuterExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleRangeExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleSizeExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleSysdateExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterIndexStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterProcedureStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterSessionStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterSynonymStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableDropPartition;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableModify;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableMoveTablespace;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableSplitPartition;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableTruncatePartition;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTablespaceAddDataFile;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTablespaceStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTriggerStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterViewStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleCheck;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleCommitStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleConstraint;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleCreateDatabaseDbLinkStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleCreateIndexStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleDeleteStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleDropDbLinkStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleExceptionStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleExitStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleExplainStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleExprStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleFileSpecification;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleForStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleForeignKey;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleGotoStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleInsertStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleLabelStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleLockTableStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OraclePLSQLCommitStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OraclePrimaryKey;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSavePointStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelect;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectForUpdate;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectHierachicalQueryClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectJoin;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectPivot;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectSubqueryTableSource;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectTableReference;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectUnPivot;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSetTransactionStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleUnique;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleUpdateStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleUsingIndexClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterIndexStatement.Rebuild;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableSplitPartition.NestedTablePartitionSpec;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableSplitPartition.TableSpaceItem;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleAlterTableSplitPartition.UpdateIndexesClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement.ConditionalInsertClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement.ConditionalInsertClauseItem;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement.Entry;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement.InsertIntoClause;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectPivot.Item;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectRestriction.CheckOption;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectRestriction.ReadOnly;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectUnPivot.NullsIncludeType;
import com.tranboot.client.druid.sql.visitor.SQLASTOutputVisitor;
import java.util.Iterator;
import java.util.List;

public class OracleOutputVisitor extends SQLASTOutputVisitor implements OracleASTVisitor {
    private final boolean printPostSemi;

    public OracleOutputVisitor(Appendable appender) {
        this(appender, true);
    }

    public OracleOutputVisitor(Appendable appender, boolean printPostSemi) {
        super(appender);
        this.dbType = "oracle";
        this.printPostSemi = printPostSemi;
    }

    public boolean isPrintPostSemi() {
        return this.printPostSemi;
    }

    public void postVisit(SQLObject x) {
        if (x instanceof SQLStatement) {
            if (this.isPrintPostSemi() || x.getParent() instanceof SQLBlockStatement) {
                if (!(x instanceof OraclePLSQLCommitStatement)) {
                    if (!(x.getParent() instanceof SQLCreateProcedureStatement)) {
                        if (this.isPrettyFormat()) {
                            if (x.getParent() != null) {
                                this.print(';');
                            } else {
                                this.println(";");
                            }
                        }

                    }
                }
            }
        }
    }

    private void printHints(List<SQLHint> hints) {
        if (hints.size() > 0) {
            this.print0("/*+ ");
            this.printAndAccept(hints, ", ");
            this.print0(" */");
        }

    }

    public boolean visit(SQLAllColumnExpr x) {
        this.print('*');
        return false;
    }

    public boolean visit(OracleAnalytic x) {
        this.print0(this.ucase ? "OVER (" : "over (");
        boolean space = false;
        if (x.getPartitionBy().size() > 0) {
            this.print0(this.ucase ? "PARTITION BY " : "partition by ");
            this.printAndAccept(x.getPartitionBy(), ", ");
            space = true;
        }

        if (x.getOrderBy() != null) {
            if (space) {
                this.print(' ');
            }

            x.getOrderBy().accept(this);
            space = true;
        }

        if (x.getWindowing() != null) {
            if (space) {
                this.print(' ');
            }

            x.getWindowing().accept(this);
        }

        this.print(')');
        return false;
    }

    public boolean visit(OracleAnalyticWindowing x) {
        this.print0(x.getType().name().toUpperCase());
        this.print(' ');
        x.getExpr().accept(this);
        return false;
    }

    public boolean visit(OracleDbLinkExpr x) {
        x.getExpr().accept(this);
        this.print('@');
        this.print0(x.getDbLink());
        return false;
    }

    public boolean visit(OracleDeleteStatement x) {
        this.print0(this.ucase ? "DELETE " : "delete ");
        SQLTableSource tableSource = x.getTableSource();
        if (x.getHints().size() > 0) {
            this.printAndAccept(x.getHints(), ", ");
            this.print(' ');
        }

        this.print0(this.ucase ? "FROM " : "from ");
        if (x.isOnly()) {
            this.print0(this.ucase ? "ONLY (" : "only (");
            x.getTableName().accept(this);
            this.print(')');
        } else {
            x.getTableSource().accept(this);
        }

        this.printAlias(x.getAlias());
        if (x.getWhere() != null) {
            this.println();
            this.incrementIndent();
            this.print0(this.ucase ? "WHERE " : "where ");
            x.getWhere().setParent(x);
            x.getWhere().accept(this);
            this.decrementIndent();
        }

        if (x.getReturning() != null) {
            this.println();
            x.getReturning().accept(this);
        }

        return false;
    }

    public boolean visit(OracleIntervalExpr x) {
        if (x.getValue() instanceof SQLLiteralExpr) {
            this.print0(this.ucase ? "INTERVAL " : "interval ");
            x.getValue().accept(this);
            this.print(' ');
        } else {
            this.print('(');
            x.getValue().accept(this);
            this.print0(") ");
        }

        this.print0(x.getType().name());
        if (x.getPrecision() != null) {
            this.print('(');
            this.print(x.getPrecision());
            if (x.getFactionalSecondsPrecision() != null) {
                this.print0(", ");
                this.print(x.getFactionalSecondsPrecision());
            }

            this.print(')');
        }

        if (x.getToType() != null) {
            this.print0(this.ucase ? " TO " : " to ");
            this.print0(x.getToType().name());
            if (x.getToFactionalSecondsPrecision() != null) {
                this.print('(');
                this.print(x.getToFactionalSecondsPrecision());
                this.print(')');
            }
        }

        return false;
    }

    public boolean visit(OracleOuterExpr x) {
        x.getExpr().accept(this);
        this.print0("(+)");
        return false;
    }

    public boolean visit(OraclePLSQLCommitStatement astNode) {
        this.print('/');
        this.println();
        return false;
    }

    public boolean visit(SQLSelect x) {
        return x instanceof OracleSelect ? this.visit((OracleSelect)x) : super.visit(x);
    }

    public boolean visit(OracleSelect x) {
        if (x.getWithSubQuery() != null) {
            x.getWithSubQuery().accept(this);
            this.println();
        }

        SQLSelectQuery query = x.getQuery();
        query.accept(this);
        if (x.getRestriction() != null) {
            this.print(' ');
            x.getRestriction().accept(this);
        }

        if (x.getForUpdate() != null) {
            this.println();
            x.getForUpdate().accept(this);
        }

        SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            boolean hasFirst = false;
            if (query instanceof SQLSelectQueryBlock) {
                SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;
                hasFirst = queryBlock.getFirst() != null;
            }

            if (!hasFirst) {
                this.println();
                orderBy.accept(this);
            }
        }

        return false;
    }

    public boolean visit(OracleSelectForUpdate x) {
        this.print0(this.ucase ? "FOR UPDATE" : "for update");
        if (x.getOf().size() > 0) {
            this.print('(');
            this.printAndAccept(x.getOf(), ", ");
            this.print(')');
        }

        if (x.isNotWait()) {
            this.print0(this.ucase ? " NOWAIT" : " nowait");
        } else if (x.isSkipLocked()) {
            this.print0(this.ucase ? " SKIP LOCKED" : " skip locked");
        } else if (x.getWait() != null) {
            this.print0(this.ucase ? " WAIT " : " wait ");
            x.getWait().accept(this);
        }

        return false;
    }

    public boolean visit(OracleSelectHierachicalQueryClause x) {
        if (x.getStartWith() != null) {
            this.print0(this.ucase ? "START WITH " : "start with ");
            x.getStartWith().accept(this);
            this.println();
        }

        this.print0(this.ucase ? "CONNECT BY " : "connect by ");
        if (x.isNoCycle()) {
            this.print0(this.ucase ? "NOCYCLE " : "nocycle ");
        }

        if (x.isPrior()) {
            this.print0(this.ucase ? "PRIOR " : "prior ");
        }

        x.getConnectBy().accept(this);
        return false;
    }

    public boolean visit(OracleSelectJoin x) {
        x.getLeft().accept(this);
        if (x.getJoinType() == JoinType.COMMA) {
            this.print0(", ");
            x.getRight().accept(this);
        } else {
            boolean isRoot = x.getParent() instanceof SQLSelectQueryBlock;
            if (isRoot) {
                this.incrementIndent();
            }

            this.println();
            this.print0(this.ucase ? x.getJoinType().name : x.getJoinType().name_lcase);
            this.print(' ');
            x.getRight().accept(this);
            if (isRoot) {
                this.decrementIndent();
            }

            if (x.getCondition() != null) {
                this.print0(this.ucase ? " ON " : " on ");
                x.getCondition().accept(this);
                this.print(' ');
            }

            if (x.getUsing().size() > 0) {
                this.print0(this.ucase ? " USING (" : " using (");
                this.printAndAccept(x.getUsing(), ", ");
                this.print(')');
            }

            if (x.getFlashback() != null) {
                this.println();
                x.getFlashback().accept(this);
            }
        }

        return false;
    }

    public boolean visit(SQLSelectOrderByItem x) {
        x.getExpr().accept(this);
        if (x.getType() != null) {
            this.print(' ');
            String typeName = x.getType().name();
            this.print0(this.ucase ? typeName.toUpperCase() : typeName.toLowerCase());
        }

        if (x.getNullsOrderType() != null) {
            this.print(' ');
            this.print0(x.getNullsOrderType().toFormalString());
        }

        return false;
    }

    public boolean visit(OracleSelectPivot x) {
        this.print0(this.ucase ? "PIVOT" : "pivot");
        if (x.isXml()) {
            this.print0(this.ucase ? " XML" : " xml");
        }

        this.print0(" (");
        this.printAndAccept(x.getItems(), ", ");
        if (x.getPivotFor().size() > 0) {
            this.print0(this.ucase ? " FOR " : " for ");
            if (x.getPivotFor().size() == 1) {
                ((SQLExpr)x.getPivotFor().get(0)).accept(this);
            } else {
                this.print('(');
                this.printAndAccept(x.getPivotFor(), ", ");
                this.print(')');
            }
        }

        if (x.getPivotIn().size() > 0) {
            this.print0(this.ucase ? " IN (" : " in (");
            this.printAndAccept(x.getPivotIn(), ", ");
            this.print(')');
        }

        this.print(')');
        return false;
    }

    public boolean visit(Item x) {
        x.getExpr().accept(this);
        if (x.getAlias() != null && x.getAlias().length() > 0) {
            this.print0(this.ucase ? " AS " : " as ");
            this.print0(x.getAlias());
        }

        return false;
    }

    public boolean visit(SQLSelectQueryBlock select) {
        return select instanceof OracleSelectQueryBlock ? this.visit((OracleSelectQueryBlock)select) : super.visit(select);
    }

    public boolean visit(OracleSelectQueryBlock x) {
        this.print0(this.ucase ? "SELECT " : "select ");
        if (x.getHints().size() > 0) {
            this.printAndAccept(x.getHints(), ", ");
            this.print(' ');
        }

        if (1 == x.getDistionOption()) {
            this.print0(this.ucase ? "ALL " : "all ");
        } else if (2 == x.getDistionOption()) {
            this.print0(this.ucase ? "DISTINCT " : "distinct ");
        } else if (3 == x.getDistionOption()) {
            this.print0(this.ucase ? "UNIQUE " : "unique ");
        }

        this.printSelectList(x.getSelectList());
        if (x.getInto() != null) {
            this.println();
            this.print0(this.ucase ? "INTO " : "into ");
            x.getInto().accept(this);
        }

        this.println();
        this.print0(this.ucase ? "FROM " : "from ");
        if (x.getFrom() == null) {
            this.print0(this.ucase ? "DUAL" : "dual");
        } else {
            x.getFrom().setParent(x);
            x.getFrom().accept(this);
        }

        if (x.getWhere() != null) {
            this.println();
            this.print0(this.ucase ? "WHERE " : "where ");
            x.getWhere().setParent(x);
            x.getWhere().accept(this);
        }

        if (x.getHierachicalQueryClause() != null) {
            this.println();
            x.getHierachicalQueryClause().accept(this);
        }

        if (x.getGroupBy() != null) {
            this.println();
            x.getGroupBy().accept(this);
        }

        if (x.getModelClause() != null) {
            this.println();
            x.getModelClause().accept(this);
        }

        this.printFetchFirst(x);
        return false;
    }

    public boolean visit(CheckOption x) {
        this.print0(this.ucase ? "CHECK OPTION" : "check option");
        if (x.getConstraint() != null) {
            this.print(' ');
            x.getConstraint().accept(this);
        }

        return false;
    }

    public boolean visit(ReadOnly x) {
        this.print0(this.ucase ? "READ ONLY" : "read only");
        return false;
    }

    public boolean visit(OracleSelectSubqueryTableSource x) {
        this.print('(');
        this.incrementIndent();
        this.println();
        x.getSelect().accept(this);
        this.decrementIndent();
        this.println();
        this.print(')');
        if (x.getPivot() != null) {
            this.println();
            x.getPivot().accept(this);
        }

        if (x.getFlashback() != null) {
            this.println();
            x.getFlashback().accept(this);
        }

        if (x.getAlias() != null && x.getAlias().length() != 0) {
            this.print(' ');
            this.print0(x.getAlias());
        }

        return false;
    }

    public boolean visit(OracleSelectTableReference x) {
        if (x.isOnly()) {
            this.print0(this.ucase ? "ONLY (" : "only (");
            this.printTableSourceExpr(x.getExpr());
            if (x.getPartition() != null) {
                this.print(' ');
                x.getPartition().accept(this);
            }

            this.print(')');
        } else {
            this.printTableSourceExpr(x.getExpr());
            if (x.getPartition() != null) {
                this.print(' ');
                x.getPartition().accept(this);
            }
        }

        if (x.getHints().size() > 0) {
            this.printHints(x.getHints());
        }

        if (x.getSampleClause() != null) {
            this.print(' ');
            x.getSampleClause().accept(this);
        }

        if (x.getPivot() != null) {
            this.println();
            x.getPivot().accept(this);
        }

        if (x.getFlashback() != null) {
            this.println();
            x.getFlashback().accept(this);
        }

        this.printAlias(x.getAlias());
        return false;
    }

    public boolean visit(OracleSelectUnPivot x) {
        this.print0(this.ucase ? "UNPIVOT" : "unpivot");
        if (x.getNullsIncludeType() != null) {
            this.print(' ');
            this.print0(NullsIncludeType.toString(x.getNullsIncludeType(), this.ucase));
        }

        this.print0(" (");
        if (x.getItems().size() == 1) {
            ((SQLExpr)x.getItems().get(0)).accept(this);
        } else {
            this.print0(" (");
            this.printAndAccept(x.getItems(), ", ");
            this.print(')');
        }

        if (x.getPivotFor().size() > 0) {
            this.print0(this.ucase ? " FOR " : " for ");
            if (x.getPivotFor().size() == 1) {
                ((SQLExpr)x.getPivotFor().get(0)).accept(this);
            } else {
                this.print('(');
                this.printAndAccept(x.getPivotFor(), ", ");
                this.print(')');
            }
        }

        if (x.getPivotIn().size() > 0) {
            this.print0(this.ucase ? " IN (" : " in (");
            this.printAndAccept(x.getPivotIn(), ", ");
            this.print(')');
        }

        this.print(')');
        return false;
    }

    public boolean visit(OracleUpdateStatement x) {
        this.print0(this.ucase ? "UPDATE " : "update ");
        if (x.getHints().size() > 0) {
            this.printAndAccept(x.getHints(), ", ");
            this.print(' ');
        }

        if (x.isOnly()) {
            this.print0(this.ucase ? "ONLY (" : "only (");
            x.getTableSource().accept(this);
            this.print(')');
        } else {
            x.getTableSource().accept(this);
        }

        this.printAlias(x.getAlias());
        this.println();
        this.print0(this.ucase ? "SET " : "set ");
        int i = 0;

        for(int size = x.getItems().size(); i < size; ++i) {
            if (i != 0) {
                this.print0(", ");
            }

            ((SQLUpdateSetItem)x.getItems().get(i)).accept(this);
        }

        if (x.getWhere() != null) {
            this.println();
            this.print0(this.ucase ? "WHERE " : "where ");
            this.incrementIndent();
            x.getWhere().setParent(x);
            x.getWhere().accept(this);
            this.decrementIndent();
        }

        if (x.getReturning().size() > 0) {
            this.println();
            this.print0(this.ucase ? "RETURNING " : "returning ");
            this.printAndAccept(x.getReturning(), ", ");
            this.print0(this.ucase ? " INTO " : " into ");
            this.printAndAccept(x.getReturningInto(), ", ");
        }

        return false;
    }

    public void endVisit(OraclePLSQLCommitStatement astNode) {
    }

    public void endVisit(OracleAnalytic x) {
    }

    public void endVisit(OracleAnalyticWindowing x) {
    }

    public void endVisit(OracleDbLinkExpr x) {
    }

    public void endVisit(OracleDeleteStatement x) {
    }

    public void endVisit(OracleIntervalExpr x) {
    }

    public void endVisit(SQLMethodInvokeExpr x) {
    }

    public void endVisit(OracleOuterExpr x) {
    }

    public void endVisit(OracleSelectForUpdate x) {
    }

    public void endVisit(OracleSelectHierachicalQueryClause x) {
    }

    public void endVisit(OracleSelectJoin x) {
    }

    public void endVisit(OracleSelectPivot x) {
    }

    public void endVisit(Item x) {
    }

    public void endVisit(CheckOption x) {
    }

    public void endVisit(ReadOnly x) {
    }

    public void endVisit(OracleSelectSubqueryTableSource x) {
    }

    public void endVisit(OracleSelectUnPivot x) {
    }

    public void endVisit(OracleUpdateStatement x) {
    }

    public boolean visit(SampleClause x) {
        this.print0(this.ucase ? "SAMPLE " : "sample ");
        if (x.isBlock()) {
            this.print0(this.ucase ? "BLOCK " : "block ");
        }

        this.print('(');
        this.printAndAccept(x.getPercent(), ", ");
        this.print(')');
        if (x.getSeedValue() != null) {
            this.print0(this.ucase ? " SEED (" : " seed (");
            x.getSeedValue().accept(this);
            this.print(')');
        }

        return false;
    }

    public void endVisit(SampleClause x) {
    }

    public void endVisit(OracleSelectTableReference x) {
    }

    public boolean visit(PartitionExtensionClause x) {
        if (x.isSubPartition()) {
            this.print0(this.ucase ? "SUBPARTITION " : "subpartition ");
        } else {
            this.print0(this.ucase ? "PARTITION " : "partition ");
        }

        if (x.getPartition() != null) {
            this.print('(');
            x.getPartition().accept(this);
            this.print(')');
        } else {
            this.print0(this.ucase ? "FOR (" : "for (");
            this.printAndAccept(x.getFor(), ",");
            this.print(')');
        }

        return false;
    }

    public void endVisit(PartitionExtensionClause x) {
    }

    public boolean visit(VersionsFlashbackQueryClause x) {
        this.print0(this.ucase ? "VERSIONS BETWEEN " : "versions between ");
        this.print0(x.getType().name());
        this.print(' ');
        x.getBegin().accept(this);
        this.print0(this.ucase ? " AND " : " and ");
        x.getEnd().accept(this);
        return false;
    }

    public void endVisit(VersionsFlashbackQueryClause x) {
    }

    public boolean visit(AsOfFlashbackQueryClause x) {
        this.print0(this.ucase ? "AS OF " : "as of ");
        this.print0(x.getType().name());
        this.print0(" (");
        x.getExpr().accept(this);
        this.print(')');
        return false;
    }

    public void endVisit(AsOfFlashbackQueryClause x) {
    }

    public boolean visit(OracleWithSubqueryEntry x) {
        x.getName().accept(this);
        if (x.getColumns().size() > 0) {
            this.print0(" (");
            this.printAndAccept(x.getColumns(), ", ");
            this.print(')');
        }

        this.println();
        this.print0(this.ucase ? "AS" : "as");
        this.println();
        this.print('(');
        this.incrementIndent();
        this.println();
        x.getSubQuery().accept(this);
        this.decrementIndent();
        this.println();
        this.print(')');
        if (x.getSearchClause() != null) {
            this.println();
            x.getSearchClause().accept(this);
        }

        if (x.getCycleClause() != null) {
            this.println();
            x.getCycleClause().accept(this);
        }

        return false;
    }

    public void endVisit(OracleWithSubqueryEntry x) {
    }

    public boolean visit(SearchClause x) {
        this.print0(this.ucase ? "SEARCH " : "search ");
        this.print0(x.getType().name());
        this.print0(this.ucase ? " FIRST BY " : " first by ");
        this.printAndAccept(x.getItems(), ", ");
        this.print0(this.ucase ? " SET " : " set ");
        x.getOrderingColumn().accept(this);
        return false;
    }

    public void endVisit(SearchClause x) {
    }

    public boolean visit(CycleClause x) {
        this.print0(this.ucase ? "CYCLE " : "cycle ");
        this.printAndAccept(x.getAliases(), ", ");
        this.print0(this.ucase ? " SET " : " set ");
        x.getMark().accept(this);
        this.print0(this.ucase ? " TO " : " to ");
        x.getValue().accept(this);
        this.print0(this.ucase ? " DEFAULT " : " default ");
        x.getDefaultValue().accept(this);
        return false;
    }

    public void endVisit(CycleClause x) {
    }

    public boolean visit(OracleBinaryFloatExpr x) {
        this.print0(x.getValue().toString());
        this.print('F');
        return false;
    }

    public void endVisit(OracleBinaryFloatExpr x) {
    }

    public boolean visit(OracleBinaryDoubleExpr x) {
        this.print0(x.getValue().toString());
        this.print('D');
        return false;
    }

    public void endVisit(OracleBinaryDoubleExpr x) {
    }

    public void endVisit(OracleSelect x) {
    }

    public boolean visit(OracleCursorExpr x) {
        this.print0(this.ucase ? "CURSOR(" : "cursor(");
        this.incrementIndent();
        this.println();
        x.getQuery().accept(this);
        this.decrementIndent();
        this.println();
        this.print(')');
        return false;
    }

    public void endVisit(OracleCursorExpr x) {
    }

    public boolean visit(OracleIsSetExpr x) {
        x.getNestedTable().accept(this);
        this.print0(this.ucase ? " IS A SET" : " is a set");
        return false;
    }

    public void endVisit(OracleIsSetExpr x) {
    }

    public boolean visit(ReturnRowsClause x) {
        if (x.isAll()) {
            this.print0(this.ucase ? "RETURN ALL ROWS" : "return all rows");
        } else {
            this.print0(this.ucase ? "RETURN UPDATED ROWS" : "return updated rows");
        }

        return false;
    }

    public void endVisit(ReturnRowsClause x) {
    }

    public boolean visit(ModelClause x) {
        this.print0(this.ucase ? "MODEL" : "model");
        this.incrementIndent();
        Iterator var2 = x.getCellReferenceOptions().iterator();

        while(var2.hasNext()) {
            CellReferenceOption opt = (CellReferenceOption)var2.next();
            this.print(' ');
            this.print0(opt.name);
        }

        if (x.getReturnRowsClause() != null) {
            this.print(' ');
            x.getReturnRowsClause().accept(this);
        }

        var2 = x.getReferenceModelClauses().iterator();

        while(var2.hasNext()) {
            ReferenceModelClause item = (ReferenceModelClause)var2.next();
            this.print(' ');
            item.accept(this);
        }

        x.getMainModel().accept(this);
        this.decrementIndent();
        return false;
    }

    public void endVisit(ModelClause x) {
    }

    public boolean visit(MainModelClause x) {
        if (x.getMainModelName() != null) {
            this.print0(this.ucase ? " MAIN " : " main ");
            x.getMainModelName().accept(this);
        }

        this.println();
        x.getModelColumnClause().accept(this);
        Iterator var2 = x.getCellReferenceOptions().iterator();

        while(var2.hasNext()) {
            CellReferenceOption opt = (CellReferenceOption)var2.next();
            this.println();
            this.print0(opt.name);
        }

        this.println();
        x.getModelRulesClause().accept(this);
        return false;
    }

    public void endVisit(MainModelClause x) {
    }

    public boolean visit(ModelColumnClause x) {
        if (x.getQueryPartitionClause() != null) {
            x.getQueryPartitionClause().accept(this);
            this.println();
        }

        this.print0(this.ucase ? "DIMENSION BY (" : "dimension by (");
        this.printAndAccept(x.getDimensionByColumns(), ", ");
        this.print(')');
        this.println();
        this.print0(this.ucase ? "MEASURES (" : "measures (");
        this.printAndAccept(x.getMeasuresColumns(), ", ");
        this.print(')');
        return false;
    }

    public void endVisit(ModelColumnClause x) {
    }

    public boolean visit(QueryPartitionClause x) {
        this.print0(this.ucase ? "PARTITION BY (" : "partition by (");
        this.printAndAccept(x.getExprList(), ", ");
        this.print(')');
        return false;
    }

    public void endVisit(QueryPartitionClause x) {
    }

    public boolean visit(ModelColumn x) {
        x.getExpr().accept(this);
        if (x.getAlias() != null) {
            this.print(' ');
            this.print0(x.getAlias());
        }

        return false;
    }

    public void endVisit(ModelColumn x) {
    }

    public boolean visit(ModelRulesClause x) {
        if (x.getOptions().size() > 0) {
            this.print0(this.ucase ? "RULES" : "rules");
            Iterator var2 = x.getOptions().iterator();

            while(var2.hasNext()) {
                ModelRuleOption opt = (ModelRuleOption)var2.next();
                this.print(' ');
                this.print0(opt.name);
            }
        }

        if (x.getIterate() != null) {
            this.print0(this.ucase ? " ITERATE (" : " iterate (");
            x.getIterate().accept(this);
            this.print(')');
            if (x.getUntil() != null) {
                this.print0(this.ucase ? " UNTIL (" : " until (");
                x.getUntil().accept(this);
                this.print(')');
            }
        }

        this.print0(" (");
        this.printAndAccept(x.getCellAssignmentItems(), ", ");
        this.print(')');
        return false;
    }

    public void endVisit(ModelRulesClause x) {
    }

    public boolean visit(CellAssignmentItem x) {
        if (x.getOption() != null) {
            this.print0(x.getOption().name);
            this.print(' ');
        }

        x.getCellAssignment().accept(this);
        if (x.getOrderBy() != null) {
            this.print(' ');
            x.getOrderBy().accept(this);
        }

        this.print0(" = ");
        x.getExpr().accept(this);
        return false;
    }

    public void endVisit(CellAssignmentItem x) {
    }

    public boolean visit(CellAssignment x) {
        x.getMeasureColumn().accept(this);
        this.print0("[");
        this.printAndAccept(x.getConditions(), ", ");
        this.print0("]");
        return false;
    }

    public void endVisit(CellAssignment x) {
    }

    public boolean visit(OracleReturningClause x) {
        this.print0(this.ucase ? "RETURNING " : "returning ");
        this.printAndAccept(x.getItems(), ", ");
        this.print0(this.ucase ? " INTO " : " into ");
        this.printAndAccept(x.getValues(), ", ");
        return false;
    }

    public void endVisit(OracleReturningClause x) {
    }

    public boolean visit(OracleInsertStatement x) {
        this.print0(this.ucase ? "INSERT " : "insert ");
        if (x.getHints().size() > 0) {
            this.printAndAccept(x.getHints(), ", ");
            this.print(' ');
        }

        this.print0(this.ucase ? "INTO " : "into ");
        x.getTableSource().accept(this);
        this.printInsertColumns(x.getColumns());
        if (x.getValues() != null) {
            this.println();
            this.print0(this.ucase ? "VALUES " : "values ");
            x.getValues().accept(this);
        } else if (x.getQuery() != null) {
            this.println();
            x.getQuery().setParent(x);
            x.getQuery().accept(this);
        }

        if (x.getReturning() != null) {
            this.println();
            x.getReturning().accept(this);
        }

        if (x.getErrorLogging() != null) {
            this.println();
            x.getErrorLogging().accept(this);
        }

        return false;
    }

    public void endVisit(OracleInsertStatement x) {
        this.endVisit((SQLInsertStatement)x);
    }

    public boolean visit(InsertIntoClause x) {
        this.print0(this.ucase ? "INTO " : "into ");
        x.getTableSource().accept(this);
        if (x.getColumns().size() > 0) {
            this.incrementIndent();
            this.println();
            this.print('(');
            int i = 0;

            for(int size = x.getColumns().size(); i < size; ++i) {
                if (i != 0) {
                    if (i % 5 == 0) {
                        this.println();
                    }

                    this.print0(", ");
                }

                ((SQLExpr)x.getColumns().get(i)).accept(this);
            }

            this.print(')');
            this.decrementIndent();
        }

        if (x.getValues() != null) {
            this.println();
            this.print0(this.ucase ? "VALUES " : "values ");
            x.getValues().accept(this);
        } else if (x.getQuery() != null) {
            this.println();
            x.getQuery().setParent(x);
            x.getQuery().accept(this);
        }

        return false;
    }

    public void endVisit(InsertIntoClause x) {
    }

    public boolean visit(OracleMultiInsertStatement x) {
        this.print0(this.ucase ? "INSERT " : "insert ");
        if (x.getHints().size() > 0) {
            this.printHints(x.getHints());
        }

        if (x.getOption() != null) {
            this.print0(x.getOption().name());
            this.print(' ');
        }

        int i = 0;

        for(int size = x.getEntries().size(); i < size; ++i) {
            this.incrementIndent();
            this.println();
            ((Entry)x.getEntries().get(i)).accept(this);
            this.decrementIndent();
        }

        this.println();
        x.getSubQuery().accept(this);
        return false;
    }

    public void endVisit(OracleMultiInsertStatement x) {
    }

    public boolean visit(ConditionalInsertClause x) {
        int i = 0;

        for(int size = x.getItems().size(); i < size; ++i) {
            if (i != 0) {
                this.println();
            }

            ConditionalInsertClauseItem item = (ConditionalInsertClauseItem)x.getItems().get(i);
            item.accept(this);
        }

        if (x.getElseItem() != null) {
            this.println();
            this.print0(this.ucase ? "ELSE" : "else");
            this.incrementIndent();
            this.println();
            x.getElseItem().accept(this);
            this.decrementIndent();
        }

        return false;
    }

    public void endVisit(ConditionalInsertClause x) {
    }

    public boolean visit(ConditionalInsertClauseItem x) {
        this.print0(this.ucase ? "WHEN " : "when ");
        x.getWhen().accept(this);
        this.print0(this.ucase ? " THEN" : " then");
        this.incrementIndent();
        this.println();
        x.getThen().accept(this);
        this.decrementIndent();
        return false;
    }

    public void endVisit(ConditionalInsertClauseItem x) {
    }

    public void endVisit(OracleSelectQueryBlock x) {
    }

    public boolean visit(SQLBlockStatement x) {
        int i;
        int size;
        if (x.getParameters().size() != 0) {
            this.print0(this.ucase ? "DECLARE" : "declare");
            this.incrementIndent();
            this.println();
            i = 0;

            for(size = x.getParameters().size(); i < size; ++i) {
                if (i != 0) {
                    this.println();
                }

                SQLParameter param = (SQLParameter)x.getParameters().get(i);
                param.accept(this);
                this.print(';');
            }

            this.decrementIndent();
            this.println();
        }

        this.print0(this.ucase ? "BEGIN" : "begin");
        this.incrementIndent();
        this.println();
        i = 0;

        for(size = x.getStatementList().size(); i < size; ++i) {
            if (i != 0) {
                this.println();
            }

            SQLStatement stmt = (SQLStatement)x.getStatementList().get(i);
            stmt.setParent(x);
            stmt.accept(this);
        }

        this.decrementIndent();
        this.println();
        this.print0(this.ucase ? "END" : "end");
        return false;
    }

    public void endVisit(SQLBlockStatement x) {
    }

    public boolean visit(OracleLockTableStatement x) {
        this.print0(this.ucase ? "LOCK TABLE " : "lock table ");
        x.getTable().accept(this);
        this.print0(this.ucase ? " IN " : " in ");
        this.print0(x.getLockMode().name());
        this.print0(this.ucase ? " MODE " : " mode ");
        if (x.isNoWait()) {
            this.print0(this.ucase ? "NOWAIT" : "nowait");
        } else if (x.getWait() != null) {
            this.print0(this.ucase ? "WAIT " : "wait ");
            x.getWait().accept(this);
        }

        return false;
    }

    public void endVisit(OracleLockTableStatement x) {
    }

    public boolean visit(OracleAlterSessionStatement x) {
        this.print0(this.ucase ? "ALTER SESSION SET " : "alter session set ");
        this.printAndAccept(x.getItems(), ", ");
        return false;
    }

    public void endVisit(OracleAlterSessionStatement x) {
    }

    public boolean visit(OracleExprStatement x) {
        x.getExpr().accept(this);
        return false;
    }

    public void endVisit(OracleExprStatement x) {
    }

    public boolean visit(OracleDatetimeExpr x) {
        x.getExpr().accept(this);
        SQLExpr timeZone = x.getTimeZone();
        if (timeZone instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)timeZone).getName().equalsIgnoreCase("LOCAL")) {
            this.print0(this.ucase ? " AT LOCAL" : "alter session set ");
            return false;
        } else {
            this.print0(this.ucase ? " AT TIME ZONE " : " at time zone ");
            timeZone.accept(this);
            return false;
        }
    }

    public void endVisit(OracleDatetimeExpr x) {
    }

    public boolean visit(OracleSysdateExpr x) {
        this.print0(this.ucase ? "SYSDATE" : "sysdate");
        if (x.getOption() != null) {
            this.print('@');
            this.print0(x.getOption());
        }

        return false;
    }

    public void endVisit(OracleSysdateExpr x) {
    }

    public void endVisit(com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleExceptionStatement.Item x) {
    }

    public boolean visit(com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleExceptionStatement.Item x) {
        this.print0(this.ucase ? "WHEN " : "when ");
        x.getWhen().accept(this);
        this.incrementIndent();
        int i = 0;

        for(int size = x.getStatements().size(); i < size; ++i) {
            this.println();
            SQLStatement stmt = (SQLStatement)x.getStatements().get(i);
            stmt.setParent(x);
            stmt.accept(this);
        }

        this.decrementIndent();
        return false;
    }

    public boolean visit(OracleExceptionStatement x) {
        this.print0(this.ucase ? "EXCEPTION" : "exception");
        this.incrementIndent();
        Iterator var2 = x.getItems().iterator();

        while(var2.hasNext()) {
            com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleExceptionStatement.Item item = (com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleExceptionStatement.Item)var2.next();
            this.println();
            item.accept(this);
        }

        this.decrementIndent();
        return false;
    }

    public void endVisit(OracleExceptionStatement x) {
    }

    public boolean visit(OracleArgumentExpr x) {
        this.print0(x.getArgumentName());
        this.print0(" => ");
        x.getValue().accept(this);
        return false;
    }

    public void endVisit(OracleArgumentExpr x) {
    }

    public boolean visit(OracleSetTransactionStatement x) {
        if (x.isReadOnly()) {
            this.print0(this.ucase ? "SET TRANSACTION READ ONLY NAME " : "set transaction read only name ");
        } else {
            this.print0(this.ucase ? "SET TRANSACTION NAME " : "set transaction name ");
        }

        x.getName().accept(this);
        return false;
    }

    public void endVisit(OracleSetTransactionStatement x) {
    }

    public boolean visit(OracleExplainStatement x) {
        this.print0(this.ucase ? "EXPLAIN PLAN" : "explain plan");
        this.incrementIndent();
        this.println();
        if (x.getStatementId() != null) {
            this.print0(this.ucase ? "SET STATEMENT_ID = " : "set statement_id = ");
            x.getStatementId().accept(this);
            this.println();
        }

        if (x.getInto() != null) {
            this.print0(this.ucase ? "INTO " : "into ");
            x.getInto().accept(this);
            this.println();
        }

        this.print0(this.ucase ? "FRO" : "fro");
        this.println();
        x.getStatement().accept(this);
        this.decrementIndent();
        return false;
    }

    public void endVisit(OracleExplainStatement x) {
    }

    public boolean visit(OracleAlterProcedureStatement x) {
        this.print0(this.ucase ? "ALTER PROCEDURE " : "alter procedure ");
        x.getName().accept(this);
        if (x.isCompile()) {
            this.print0(this.ucase ? " COMPILE" : " compile");
        }

        if (x.isReuseSettings()) {
            this.print0(this.ucase ? " REUSE SETTINGS" : " reuse settings");
        }

        return false;
    }

    public void endVisit(OracleAlterProcedureStatement x) {
    }

    public boolean visit(OracleAlterTableDropPartition x) {
        this.print0(this.ucase ? "DROP PARTITION " : "drop partition ");
        x.getName().accept(this);
        return false;
    }

    public void endVisit(OracleAlterTableDropPartition x) {
    }

    public boolean visit(SQLAlterTableStatement x) {
        this.print0(this.ucase ? "ALTER TABLE " : "alter table ");
        this.printTableSourceExpr(x.getName());
        this.incrementIndent();
        Iterator var2 = x.getItems().iterator();

        while(var2.hasNext()) {
            SQLAlterTableItem item = (SQLAlterTableItem)var2.next();
            this.println();
            item.accept(this);
        }

        if (x.isUpdateGlobalIndexes()) {
            this.println();
            this.print0(this.ucase ? "UPDATE GLOABL INDEXES" : "update gloabl indexes");
        }

        this.decrementIndent();
        return false;
    }

    public boolean visit(OracleAlterTableTruncatePartition x) {
        this.print0(this.ucase ? "TRUNCATE PARTITION " : "truncate partition ");
        x.getName().accept(this);
        return false;
    }

    public void endVisit(OracleAlterTableTruncatePartition x) {
    }

    public boolean visit(TableSpaceItem x) {
        this.print0(this.ucase ? "TABLESPACE " : "tablespace ");
        x.getTablespace().accept(this);
        return false;
    }

    public void endVisit(TableSpaceItem x) {
    }

    public boolean visit(UpdateIndexesClause x) {
        this.print0(this.ucase ? "UPDATE INDEXES" : "update indexes");
        if (x.getItems().size() > 0) {
            this.print('(');
            this.printAndAccept(x.getItems(), ", ");
            this.print(')');
        }

        return false;
    }

    public void endVisit(UpdateIndexesClause x) {
    }

    public boolean visit(OracleAlterTableSplitPartition x) {
        this.print0(this.ucase ? "SPLIT PARTITION " : "split partition ");
        x.getName().accept(this);
        if (x.getAt().size() > 0) {
            this.incrementIndent();
            this.println();
            this.print0(this.ucase ? "AT (" : "at (");
            this.printAndAccept(x.getAt(), ", ");
            this.print(')');
            this.decrementIndent();
        }

        if (x.getInto().size() > 0) {
            this.println();
            this.incrementIndent();
            this.print0(this.ucase ? "INTO (" : "into (");
            this.printAndAccept(x.getInto(), ", ");
            this.print(')');
            this.decrementIndent();
        }

        if (x.getUpdateIndexes() != null) {
            this.println();
            this.incrementIndent();
            x.getUpdateIndexes().accept(this);
            this.decrementIndent();
        }

        return false;
    }

    public void endVisit(OracleAlterTableSplitPartition x) {
    }

    public boolean visit(NestedTablePartitionSpec x) {
        this.print0(this.ucase ? "PARTITION " : "partition ");
        x.getPartition().accept(this);
        Iterator var2 = x.getSegmentAttributeItems().iterator();

        while(var2.hasNext()) {
            SQLObject item = (SQLObject)var2.next();
            this.print(' ');
            item.accept(this);
        }

        return false;
    }

    public void endVisit(NestedTablePartitionSpec x) {
    }

    public boolean visit(OracleAlterTableModify x) {
        this.print0(this.ucase ? "MODIFY (" : "modify (");
        this.incrementIndent();
        int i = 0;

        for(int size = x.getColumns().size(); i < size; ++i) {
            this.println();
            SQLColumnDefinition column = (SQLColumnDefinition)x.getColumns().get(i);
            column.accept(this);
            if (i != size - 1) {
                this.print0(", ");
            }
        }

        this.decrementIndent();
        this.println();
        this.print(')');
        return false;
    }

    public void endVisit(OracleAlterTableModify x) {
    }

    public boolean visit(OracleCreateIndexStatement x) {
        this.print0(this.ucase ? "CREATE " : "create ");
        if (x.getType() != null) {
            this.print0(x.getType());
            this.print(' ');
        }

        this.print0(this.ucase ? "INDEX " : "index ");
        x.getName().accept(this);
        this.print0(this.ucase ? " ON " : " on ");
        x.getTable().accept(this);
        this.print('(');
        this.printAndAccept(x.getItems(), ", ");
        this.print(')');
        if (x.isIndexOnlyTopLevel()) {
            this.println();
            this.print0(this.ucase ? "INDEX ONLY TOPLEVEL" : "index only toplevel");
        }

        if (x.getPtcfree() != null) {
            this.println();
            this.print0(this.ucase ? "PCTFREE " : "pctfree ");
            x.getPtcfree().accept(this);
        }

        if (x.getInitrans() != null) {
            this.println();
            this.print0(this.ucase ? "INITRANS " : "initrans ");
            x.getInitrans().accept(this);
        }

        if (x.getMaxtrans() != null) {
            this.println();
            this.print0(this.ucase ? "MAXTRANS " : "maxtrans ");
            x.getMaxtrans().accept(this);
        }

        if (x.isComputeStatistics()) {
            this.println();
            this.print0(this.ucase ? "COMPUTE STATISTICS" : "compute statistics");
        }

        if (x.getTablespace() != null) {
            this.println();
            this.print0(this.ucase ? "TABLESPACE " : "tablespace ");
            x.getTablespace().accept(this);
        }

        if (x.isOnline()) {
            this.print0(this.ucase ? " ONLINE" : " online");
        }

        if (x.isNoParallel()) {
            this.print0(this.ucase ? " NOPARALLEL" : " noparallel");
        } else if (x.getParallel() != null) {
            this.print0(this.ucase ? " PARALLEL " : " parallel ");
            x.getParallel().accept(this);
        }

        return false;
    }

    public void endVisit(OracleCreateIndexStatement x) {
    }

    public boolean visit(OracleAlterIndexStatement x) {
        this.print0(this.ucase ? "ALTER INDEX " : "alter index ");
        x.getName().accept(this);
        if (x.getRenameTo() != null) {
            this.print0(this.ucase ? " RENAME TO " : " rename to ");
            x.getRenameTo().accept(this);
        }

        if (x.getMonitoringUsage() != null) {
            this.print0(this.ucase ? " MONITORING USAGE" : " monitoring usage");
        }

        if (x.getRebuild() != null) {
            this.print(' ');
            x.getRebuild().accept(this);
        }

        if (x.getParallel() != null) {
            this.print0(this.ucase ? " PARALLEL" : " parallel");
            x.getParallel().accept(this);
        }

        return false;
    }

    public void endVisit(OracleAlterIndexStatement x) {
    }

    public boolean visit(Rebuild x) {
        this.print0(this.ucase ? "REBUILD" : "rebuild");
        if (x.getOption() != null) {
            this.print(' ');
            x.getOption().accept(this);
        }

        return false;
    }

    public void endVisit(Rebuild x) {
    }

    public boolean visit(OracleForStatement x) {
        this.print0(this.ucase ? "FOR " : "for ");
        x.getIndex().accept(this);
        this.print0(this.ucase ? " IN " : " in ");
        x.getRange().accept(this);
        this.println();
        this.print0(this.ucase ? "LOOP" : "loop");
        this.incrementIndent();
        this.println();
        int i = 0;

        for(int size = x.getStatements().size(); i < size; ++i) {
            SQLStatement item = (SQLStatement)x.getStatements().get(i);
            item.setParent(x);
            item.accept(this);
            if (i != size - 1) {
                this.println();
            }
        }

        this.decrementIndent();
        this.println();
        this.print0(this.ucase ? "END LOOP" : "end loop");
        return false;
    }

    public void endVisit(OracleForStatement x) {
    }

    public boolean visit(Else x) {
        this.print0(this.ucase ? "ELSE" : "else");
        this.incrementIndent();
        this.println();
        int i = 0;

        for(int size = x.getStatements().size(); i < size; ++i) {
            if (i != 0) {
                this.println();
            }

            SQLStatement item = (SQLStatement)x.getStatements().get(i);
            item.setParent(x);
            item.accept(this);
        }

        this.print(';');
        this.decrementIndent();
        return false;
    }

    public boolean visit(ElseIf x) {
        this.print0(this.ucase ? "ELSE IF " : "else if ");
        x.getCondition().accept(this);
        this.print0(this.ucase ? " THEN" : " then");
        this.incrementIndent();
        this.println();
        int i = 0;

        for(int size = x.getStatements().size(); i < size; ++i) {
            if (i != 0) {
                this.println();
            }

            SQLStatement item = (SQLStatement)x.getStatements().get(i);
            item.setParent(x);
            item.accept(this);
        }

        this.print(';');
        this.decrementIndent();
        return false;
    }

    public boolean visit(SQLIfStatement x) {
        this.print0(this.ucase ? "IF " : "if ");
        x.getCondition().accept(this);
        this.print0(this.ucase ? " THEN" : " then");
        this.incrementIndent();
        this.println();
        int i = 0;

        for(int size = x.getStatements().size(); i < size; ++i) {
            SQLStatement item = (SQLStatement)x.getStatements().get(i);
            item.setParent(x);
            item.accept(this);
            if (i != size - 1) {
                this.println();
            }
        }

        this.print(';');
        this.decrementIndent();
        Iterator var5 = x.getElseIfList().iterator();

        while(var5.hasNext()) {
            ElseIf elseIf = (ElseIf)var5.next();
            this.println();
            elseIf.accept(this);
        }

        if (x.getElseItem() != null) {
            this.println();
            x.getElseItem().accept(this);
        }

        this.println();
        this.print0(this.ucase ? "END IF" : "end if");
        return false;
    }

    public boolean visit(OracleRangeExpr x) {
        x.getLowBound().accept(this);
        this.print0("..");
        x.getUpBound().accept(this);
        return false;
    }

    public void endVisit(OracleRangeExpr x) {
    }

    protected void visitColumnDefault(SQLColumnDefinition x) {
        if (x.getParent() instanceof SQLBlockStatement) {
            this.print0(" := ");
        } else {
            this.print0(this.ucase ? " DEFAULT " : " default ");
        }

        x.getDefaultExpr().accept(this);
    }

    public boolean visit(OraclePrimaryKey x) {
        if (x.getName() != null) {
            this.print0(this.ucase ? "CONSTRAINT " : "constraint ");
            x.getName().accept(this);
            this.print(' ');
        }

        this.print0(this.ucase ? "PRIMARY KEY (" : "primary key (");
        this.printAndAccept(x.getColumns(), ", ");
        this.print(')');
        this.printConstraintState(x);
        return false;
    }

    protected void printConstraintState(OracleConstraint x) {
        if (x.getUsing() != null) {
            this.println();
            x.getUsing().accept(this);
        }

        if (x.getExceptionsInto() != null) {
            this.println();
            this.print0(this.ucase ? "EXCEPTIONS INTO " : "exceptions into ");
            x.getExceptionsInto().accept(this);
        }

        if (x.getEnable() != null) {
            if (x.getEnable()) {
                this.print0(this.ucase ? " ENABLE" : " enable");
            } else {
                this.print0(this.ucase ? " DIABLE" : " diable");
            }
        }

        if (x.getInitially() != null) {
            this.print0(this.ucase ? " INITIALLY " : " initially ");
            this.print0(x.getInitially().name());
        }

        if (x.getDeferrable() != null) {
            if (x.getDeferrable()) {
                this.print0(this.ucase ? " DEFERRABLE" : " deferrable");
            } else {
                this.print0(this.ucase ? " NOT DEFERRABLE" : " not deferrable");
            }
        }

    }

    public void endVisit(OraclePrimaryKey x) {
    }

    public boolean visit(OracleCreateTableStatement x) {
        this.visit((SQLCreateTableStatement)x);
        if (x.isOrganizationIndex()) {
            this.println();
            this.print0(this.ucase ? "ORGANIZATION INDEX" : "organization index");
        }

        if (x.getPtcfree() != null) {
            this.println();
            this.print0(this.ucase ? "PCTFREE " : "pctfree ");
            x.getPtcfree().accept(this);
        }

        if (x.getInitrans() != null) {
            this.println();
            this.print0(this.ucase ? "INITRANS " : "initrans ");
            x.getInitrans().accept(this);
        }

        if (x.getMaxtrans() != null) {
            this.println();
            this.print0(this.ucase ? "MAXTRANS " : "maxtrans ");
            x.getMaxtrans().accept(this);
        }

        if (x.isInMemoryMetadata()) {
            this.println();
            this.print0(this.ucase ? "IN_MEMORY_METADATA" : "in_memory_metadata");
        }

        if (x.isCursorSpecificSegment()) {
            this.println();
            this.print0(this.ucase ? "CURSOR_SPECIFIC_SEGMENT" : "cursor_specific_segment");
        }

        if (x.getParallel() == Boolean.TRUE) {
            this.println();
            this.print0(this.ucase ? "PARALLEL" : "parallel");
        } else if (x.getParallel() == Boolean.FALSE) {
            this.println();
            this.print0(this.ucase ? "NOPARALLEL" : "noparallel");
        }

        if (x.getCache() == Boolean.TRUE) {
            this.println();
            this.print0(this.ucase ? "CACHE" : "cache");
        } else if (x.getCache() == Boolean.FALSE) {
            this.println();
            this.print0(this.ucase ? "NOCACHE" : "nocache");
        }

        if (x.getCompress() == Boolean.TRUE) {
            this.println();
            this.print0(this.ucase ? "COMPRESS" : "compress");
        } else if (x.getCompress() == Boolean.FALSE) {
            this.println();
            this.print0(this.ucase ? "NOCOMPRESS" : "nocompress");
        }

        if (x.getLogging() == Boolean.TRUE) {
            this.println();
            this.print0(this.ucase ? "LOGGING" : "logging");
        } else if (x.getLogging() == Boolean.FALSE) {
            this.println();
            this.print0(this.ucase ? "NOLOGGING" : "nologging");
        }

        if (x.getTablespace() != null) {
            this.println();
            this.print0(this.ucase ? "TABLESPACE " : "tablespace ");
            x.getTablespace().accept(this);
        }

        if (x.getStorage() != null) {
            this.println();
            x.getStorage().accept(this);
        }

        if (x.getLobStorage() != null) {
            this.println();
            x.getLobStorage().accept(this);
        }

        if (x.isOnCommit()) {
            this.println();
            this.print0(this.ucase ? "ON COMMIT" : "on commit");
        }

        if (x.isPreserveRows()) {
            this.println();
            this.print0(this.ucase ? "PRESERVE ROWS" : "preserve rows");
        }

        if (x.getPartitioning() != null) {
            this.println();
            x.getPartitioning().accept(this);
        }

        if (x.getSelect() != null) {
            this.println();
            this.print0(this.ucase ? "AS" : "as");
            this.println();
            x.getSelect().accept(this);
        }

        return false;
    }

    public void endVisit(OracleCreateTableStatement x) {
    }

    public boolean visit(OracleStorageClause x) {
        this.print0(this.ucase ? "STORAGE (" : "storage (");
        boolean first = true;
        if (x.getInitial() != null) {
            if (!first) {
                this.print(' ');
            }

            this.print0(this.ucase ? "INITIAL " : "initial ");
            x.getInitial().accept(this);
            first = false;
        }

        if (x.getMaxSize() != null) {
            if (!first) {
                this.print(' ');
            }

            this.print0(this.ucase ? "MAXSIZE " : "maxsize ");
            x.getMaxSize().accept(this);
            first = false;
        }

        if (x.getFreeLists() != null) {
            if (!first) {
                this.print(' ');
            }

            this.print0(this.ucase ? "FREELISTS " : "freelists ");
            x.getFreeLists().accept(this);
            first = false;
        }

        if (x.getFreeListGroups() != null) {
            if (!first) {
                this.print(' ');
            }

            this.print0(this.ucase ? "FREELIST GROUPS " : "freelist groups ");
            x.getFreeListGroups().accept(this);
            first = false;
        }

        if (x.getBufferPool() != null) {
            if (!first) {
                this.print(' ');
            }

            this.print0(this.ucase ? "BUFFER_POOL " : "buffer_pool ");
            x.getBufferPool().accept(this);
            first = false;
        }

        if (x.getObjno() != null) {
            if (!first) {
                this.print(' ');
            }

            this.print0(this.ucase ? "OBJNO " : "objno ");
            x.getObjno().accept(this);
            first = false;
        }

        this.print(')');
        return false;
    }

    public void endVisit(OracleStorageClause x) {
    }

    public boolean visit(OracleGotoStatement x) {
        this.print0(this.ucase ? "GOTO " : "GOTO ");
        x.getLabel().accept(this);
        return false;
    }

    public void endVisit(OracleGotoStatement x) {
    }

    public boolean visit(OracleLabelStatement x) {
        this.print0("<<");
        x.getLabel().accept(this);
        this.print0(">>");
        return false;
    }

    public void endVisit(OracleLabelStatement x) {
    }

    public boolean visit(OracleCommitStatement x) {
        this.print0(this.ucase ? "COMMIT" : "commit");
        if (x.isWrite()) {
            this.print0(this.ucase ? " WRITE" : " write");
            if (x.getWait() != null) {
                if (x.getWait()) {
                    this.print0(this.ucase ? " WAIT" : " wait");
                } else {
                    this.print0(this.ucase ? " NOWAIT" : " nowait");
                }
            }

            if (x.getImmediate() != null) {
                if (x.getImmediate()) {
                    this.print0(this.ucase ? " IMMEDIATE" : " immediate");
                } else {
                    this.print0(this.ucase ? " BATCH" : " batch");
                }
            }
        }

        return false;
    }

    public void endVisit(OracleCommitStatement x) {
    }

    public boolean visit(OracleAlterTriggerStatement x) {
        this.print0(this.ucase ? "ALTER TRIGGER " : "alter trigger ");
        x.getName().accept(this);
        if (x.isCompile()) {
            this.print0(this.ucase ? " COMPILE" : " compile");
        }

        if (x.getEnable() != null) {
            if (x.getEnable()) {
                this.print0(this.ucase ? "ENABLE" : "enable");
            } else {
                this.print0(this.ucase ? "DISABLE" : "disable");
            }
        }

        return false;
    }

    public void endVisit(OracleAlterTriggerStatement x) {
    }

    public boolean visit(OracleAlterSynonymStatement x) {
        this.print0(this.ucase ? "ALTER SYNONYM " : "alter synonym ");
        x.getName().accept(this);
        if (x.isCompile()) {
            this.print0(this.ucase ? " COMPILE" : " compile");
        }

        if (x.getEnable() != null) {
            if (x.getEnable()) {
                this.print0(this.ucase ? "ENABLE" : "enable");
            } else {
                this.print0(this.ucase ? "DISABLE" : "disable");
            }
        }

        return false;
    }

    public void endVisit(OracleAlterSynonymStatement x) {
    }

    public boolean visit(AsOfSnapshotClause x) {
        this.print0(this.ucase ? "AS OF SNAPSHOT(" : "as of snapshot(");
        x.getExpr().accept(this);
        this.print(')');
        return false;
    }

    public void endVisit(AsOfSnapshotClause x) {
    }

    public boolean visit(OracleAlterViewStatement x) {
        this.print0(this.ucase ? "ALTER VIEW " : "alter view ");
        x.getName().accept(this);
        if (x.isCompile()) {
            this.print0(this.ucase ? " COMPILE" : " compile");
        }

        if (x.getEnable() != null) {
            if (x.getEnable()) {
                this.print0(this.ucase ? "ENABLE" : "enable");
            } else {
                this.print0(this.ucase ? "DISABLE" : "disable");
            }
        }

        return false;
    }

    public void endVisit(OracleAlterViewStatement x) {
    }

    public boolean visit(OracleAlterTableMoveTablespace x) {
        this.print0(this.ucase ? " MOVE TABLESPACE " : " move tablespace ");
        x.getName().accept(this);
        return false;
    }

    public void endVisit(OracleAlterTableMoveTablespace x) {
    }

    public boolean visit(OracleSizeExpr x) {
        x.getValue().accept(this);
        this.print0(x.getUnit().name());
        return false;
    }

    public void endVisit(OracleSizeExpr x) {
    }

    public boolean visit(OracleFileSpecification x) {
        this.printAndAccept(x.getFileNames(), ", ");
        if (x.getSize() != null) {
            this.print0(this.ucase ? " SIZE " : " size ");
            x.getSize().accept(this);
        }

        if (x.isAutoExtendOff()) {
            this.print0(this.ucase ? " AUTOEXTEND OFF" : " autoextend off");
        } else if (x.getAutoExtendOn() != null) {
            this.print0(this.ucase ? " AUTOEXTEND ON " : " autoextend on ");
            x.getAutoExtendOn().accept(this);
        }

        return false;
    }

    public void endVisit(OracleFileSpecification x) {
    }

    public boolean visit(OracleAlterTablespaceAddDataFile x) {
        this.print0(this.ucase ? "ADD DATAFILE" : "add datafile");
        this.incrementIndent();
        Iterator var2 = x.getFiles().iterator();

        while(var2.hasNext()) {
            OracleFileSpecification file = (OracleFileSpecification)var2.next();
            this.println();
            file.accept(this);
        }

        this.decrementIndent();
        return false;
    }

    public void endVisit(OracleAlterTablespaceAddDataFile x) {
    }

    public boolean visit(OracleAlterTablespaceStatement x) {
        this.print0(this.ucase ? "ALTER TABLESPACE " : "alter tablespace ");
        x.getName().accept(this);
        this.println();
        x.getItem().accept(this);
        return false;
    }

    public void endVisit(OracleAlterTablespaceStatement x) {
    }

    public boolean visit(SQLTruncateStatement x) {
        this.print0(this.ucase ? "TRUNCATE TABLE " : "truncate table ");
        this.printAndAccept(x.getTableSources(), ", ");
        if (x.isPurgeSnapshotLog()) {
            this.print0(this.ucase ? " PURGE SNAPSHOT LOG" : " purge snapshot log");
        }

        return false;
    }

    public boolean visit(OracleExitStatement x) {
        this.print0(this.ucase ? "EXIT" : "exit");
        if (x.getWhen() != null) {
            this.print0(this.ucase ? " WHEN " : " when ");
            x.getWhen().accept(this);
        }

        return false;
    }

    public void endVisit(OracleExitStatement x) {
    }

    public void endVisit(SQLRollbackStatement x) {
    }

    public boolean visit(OracleSavePointStatement x) {
        this.print0(this.ucase ? "ROLLBACK" : "rollback");
        if (x.getTo() != null) {
            this.print0(this.ucase ? " TO " : " to ");
            x.getTo().accept(this);
        }

        return false;
    }

    public void endVisit(OracleSavePointStatement x) {
    }

    public boolean visit(SQLCreateProcedureStatement x) {
        if (x.isOrReplace()) {
            this.print0(this.ucase ? "CREATE OR REPLACE PROCEDURE " : "create or replace procedure ");
        } else {
            this.print0(this.ucase ? "CREATE PROCEDURE " : "create procedure ");
        }

        x.getName().accept(this);
        int paramSize = x.getParameters().size();
        if (paramSize > 0) {
            this.print0(" (");
            this.incrementIndent();
            this.println();

            for(int i = 0; i < paramSize; ++i) {
                if (i != 0) {
                    this.print0(", ");
                    this.println();
                }

                SQLParameter param = (SQLParameter)x.getParameters().get(i);
                param.accept(this);
            }

            this.decrementIndent();
            this.println();
            this.print(')');
        }

        this.println();
        x.getBlock().setParent(x);
        x.getBlock().accept(this);
        return false;
    }

    public void endVisit(SQLCreateProcedureStatement x) {
    }

    public boolean visit(OracleCreateDatabaseDbLinkStatement x) {
        this.print0(this.ucase ? "CREATE " : "create ");
        if (x.isShared()) {
            this.print0(this.ucase ? "SHARE " : "share ");
        }

        if (x.isPublic()) {
            this.print0(this.ucase ? "PUBLIC " : "public ");
        }

        this.print0(this.ucase ? "DATABASE LINK " : "database link ");
        x.getName().accept(this);
        if (x.getUser() != null) {
            this.print0(this.ucase ? " CONNECT TO " : " connect to ");
            x.getUser().accept(this);
            if (x.getPassword() != null) {
                this.print0(this.ucase ? " IDENTIFIED BY " : " identified by ");
                this.print0(x.getPassword());
            }
        }

        if (x.getAuthenticatedUser() != null) {
            this.print0(this.ucase ? " AUTHENTICATED BY " : " authenticated by ");
            x.getAuthenticatedUser().accept(this);
            if (x.getAuthenticatedPassword() != null) {
                this.print0(this.ucase ? " IDENTIFIED BY " : " identified by ");
                this.print0(x.getAuthenticatedPassword());
            }
        }

        if (x.getUsing() != null) {
            this.print0(this.ucase ? " USING " : " using ");
            x.getUsing().accept(this);
        }

        return false;
    }

    public void endVisit(OracleCreateDatabaseDbLinkStatement x) {
    }

    public boolean visit(OracleDropDbLinkStatement x) {
        this.print0(this.ucase ? "DROP " : "drop ");
        if (x.isPublic()) {
            this.print0(this.ucase ? "PUBLIC " : "public ");
        }

        this.print0(this.ucase ? "DATABASE LINK " : "database link ");
        x.getName().accept(this);
        return false;
    }

    public void endVisit(OracleDropDbLinkStatement x) {
    }

    public boolean visit(SQLCharacterDataType x) {
        this.print0(x.getName());
        if (x.getArguments().size() > 0) {
            this.print('(');
            ((SQLExpr)x.getArguments().get(0)).accept(this);
            if (x.getCharType() != null) {
                this.print(' ');
                this.print0(x.getCharType());
            }

            this.print(')');
        }

        return false;
    }

    public boolean visit(OracleDataTypeTimestamp x) {
        this.print0(x.getName());
        if (x.getArguments().size() > 0) {
            this.print('(');
            ((SQLExpr)x.getArguments().get(0)).accept(this);
            this.print(')');
        }

        if (x.isWithTimeZone()) {
            this.print0(this.ucase ? " WITH TIME ZONE" : " with time zone");
        } else if (x.isWithLocalTimeZone()) {
            this.print0(this.ucase ? " WITH LOCAL TIME ZONE" : " with local time zone");
        }

        return false;
    }

    public void endVisit(OracleDataTypeTimestamp x) {
    }

    public boolean visit(OracleDataTypeIntervalYear x) {
        this.print0(x.getName());
        if (x.getArguments().size() > 0) {
            this.print('(');
            ((SQLExpr)x.getArguments().get(0)).accept(this);
            this.print(')');
        }

        this.print0(this.ucase ? " TO MONTH" : " to month");
        return false;
    }

    public void endVisit(OracleDataTypeIntervalYear x) {
    }

    public boolean visit(OracleDataTypeIntervalDay x) {
        this.print0(x.getName());
        if (x.getArguments().size() > 0) {
            this.print('(');
            ((SQLExpr)x.getArguments().get(0)).accept(this);
            this.print(')');
        }

        this.print0(this.ucase ? " TO SECOND" : " to second");
        if (x.getFractionalSeconds().size() > 0) {
            this.print('(');
            ((SQLExpr)x.getFractionalSeconds().get(0)).accept(this);
            this.print(')');
        }

        return false;
    }

    public void endVisit(OracleDataTypeIntervalDay x) {
    }

    public boolean visit(OracleUsingIndexClause x) {
        this.print0(this.ucase ? "USING INDEX" : "using index");
        if (x.getIndex() != null) {
            this.print(' ');
            x.getIndex().accept(this);
        } else {
            if (x.getPtcfree() != null) {
                this.print0(this.ucase ? " PCTFREE " : " pctfree ");
                x.getPtcfree().accept(this);
            }

            if (x.getInitrans() != null) {
                this.print0(this.ucase ? " INITRANS " : " initrans ");
                x.getInitrans().accept(this);
            }

            if (x.getMaxtrans() != null) {
                this.print0(this.ucase ? " MAXTRANS " : " maxtrans ");
                x.getMaxtrans().accept(this);
            }

            if (x.isComputeStatistics()) {
                this.print0(this.ucase ? " COMPUTE STATISTICS" : " compute statistics");
            }

            if (x.getTablespace() != null) {
                this.print0(this.ucase ? " TABLESPACE " : " tablespace ");
                x.getTablespace().accept(this);
            }

            if (x.getEnable() != null) {
                if (x.getEnable()) {
                    this.print0(this.ucase ? " ENABLE" : " enable");
                } else {
                    this.print0(this.ucase ? " DISABLE" : " disable");
                }
            }

            if (x.getStorage() != null) {
                this.println();
                x.getStorage().accept(this);
            }
        }

        return false;
    }

    public void endVisit(OracleUsingIndexClause x) {
    }

    public boolean visit(OracleLobStorageClause x) {
        this.print0(this.ucase ? "LOB (" : "lob (");
        this.printAndAccept(x.getItems(), ",");
        this.print0(this.ucase ? ") STORE AS " : ") store as ");
        if (x.isSecureFile()) {
            this.print0(this.ucase ? "SECUREFILE " : "securefile ");
        }

        if (x.isBasicFile()) {
            this.print0(this.ucase ? "BASICFILE " : "basicfile ");
        }

        boolean first = true;
        this.print('(');
        if (x.getTableSpace() != null) {
            if (!first) {
                this.print(' ');
            }

            this.print0(this.ucase ? "TABLESPACE " : "tablespace ");
            x.getTableSpace().accept(this);
            first = false;
        }

        if (x.getEnable() != null) {
            if (!first) {
                this.print(' ');
            }

            if (x.getEnable()) {
                this.print0(this.ucase ? "ENABLE STORAGE IN ROW" : "enable storage in row");
            } else {
                this.print0(this.ucase ? "DISABLE STORAGE IN ROW" : "disable storage in row");
            }
        }

        if (x.getChunk() != null) {
            if (!first) {
                this.print(' ');
            }

            this.print0(this.ucase ? "CHUNK " : "chunk ");
            x.getChunk().accept(this);
        }

        if (x.getCache() != null) {
            if (!first) {
                this.print(' ');
            }

            if (x.getCache()) {
                this.print0(this.ucase ? "CACHE" : "cache");
            } else {
                this.print0(this.ucase ? "NOCACHE" : "nocache");
            }

            if (x.getLogging() != null) {
                if (x.getLogging()) {
                    this.print0(this.ucase ? " LOGGING" : " logging");
                } else {
                    this.print0(this.ucase ? " NOLOGGING" : " nologging");
                }
            }
        }

        if (x.getCompress() != null) {
            if (!first) {
                this.print(' ');
            }

            if (x.getCompress()) {
                this.print0(this.ucase ? "COMPRESS" : "compress");
            } else {
                this.print0(this.ucase ? "NOCOMPRESS" : "nocompress");
            }
        }

        if (x.getKeepDuplicate() != null) {
            if (!first) {
                this.print(' ');
            }

            if (x.getKeepDuplicate()) {
                this.print0(this.ucase ? "KEEP_DUPLICATES" : "keep_duplicates");
            } else {
                this.print0(this.ucase ? "DEDUPLICATE" : "deduplicate");
            }
        }

        this.print(')');
        return false;
    }

    public void endVisit(OracleLobStorageClause x) {
    }

    public boolean visit(OracleUnique x) {
        this.visit((SQLUnique)x);
        this.printConstraintState(x);
        return false;
    }

    public void endVisit(OracleUnique x) {
    }

    public boolean visit(OracleForeignKey x) {
        this.visit((SQLForeignKeyImpl)x);
        this.printConstraintState(x);
        return false;
    }

    public void endVisit(OracleForeignKey x) {
    }

    public boolean visit(OracleCheck x) {
        this.visit((SQLCheck)x);
        this.printConstraintState(x);
        return false;
    }

    public void endVisit(OracleCheck x) {
    }

    protected void printCascade() {
        this.print0(this.ucase ? " CASCADE CONSTRAINTS" : " cascade constraints");
    }

    public boolean visit(SQLMethodInvokeExpr x) {
        if ("trim".equalsIgnoreCase(x.getMethodName())) {
            SQLExpr trim_character = (SQLExpr)x.getAttribute("trim_character");
            if (trim_character != null) {
                this.print0(x.getMethodName());
                this.print('(');
                String trim_option = (String)x.getAttribute("trim_option");
                if (trim_option != null && trim_option.length() != 0) {
                    this.print0(trim_option);
                    this.print(' ');
                }

                trim_character.accept(this);
                if (x.getParameters().size() > 0) {
                    this.print0(this.ucase ? " FROM " : " from ");
                    ((SQLExpr)x.getParameters().get(0)).accept(this);
                }

                this.print(')');
                return false;
            }
        }

        return super.visit(x);
    }

    public boolean visit(SQLCharExpr x) {
        if (x.getText() != null && x.getText().length() == 0) {
            this.print0(this.ucase ? "NULL" : "null");
        } else {
            super.visit(x);
        }

        return false;
    }
}

