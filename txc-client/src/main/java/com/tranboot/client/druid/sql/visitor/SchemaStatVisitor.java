package com.tranboot.client.druid.sql.visitor;

import com.tranboot.client.druid.sql.ast.SQLDeclareItem;
import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLOrderBy;
import com.tranboot.client.druid.sql.ast.SQLOrderingSpecification;
import com.tranboot.client.druid.sql.ast.SQLParameter;
import com.tranboot.client.druid.sql.ast.SQLPartition;
import com.tranboot.client.druid.sql.ast.SQLPartitionByHash;
import com.tranboot.client.druid.sql.ast.SQLPartitionByList;
import com.tranboot.client.druid.sql.ast.SQLPartitionByRange;
import com.tranboot.client.druid.sql.ast.SQLPartitionValue;
import com.tranboot.client.druid.sql.ast.SQLSubPartition;
import com.tranboot.client.druid.sql.ast.SQLSubPartitionByHash;
import com.tranboot.client.druid.sql.ast.expr.SQLAggregateExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLAllColumnExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLArrayExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCastExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCurrentOfCursorExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLInListExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLInSubQueryExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLIntegerExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLPropertyExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLSequenceExpr;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterDatabaseStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableAddColumn;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableAddConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableAddIndex;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableAnalyzePartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableCheckPartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableCoalescePartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableConvertCharSet;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDisableConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDiscardPartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDropConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDropForeignKey;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDropIndex;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDropKey;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDropPartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDropPrimaryKey;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableEnableConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableImportPartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableItem;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableOptimizePartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableReOrganizePartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableRebuildPartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableRename;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableRepairPartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableTruncatePartition;
import com.tranboot.client.druid.sql.ast.statement.SQLBlockStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCallStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCheck;
import com.tranboot.client.druid.sql.ast.statement.SQLCloseStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnDefinition;
import com.tranboot.client.druid.sql.ast.statement.SQLCommentStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateProcedureStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateSequenceStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateTableStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateTriggerStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateViewStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDeleteStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropDatabaseStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropFunctionStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropIndexStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropProcedureStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropSequenceStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropTableSpaceStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropTableStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropTriggerStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropUserStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropViewStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLExprTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLFetchStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.tranboot.client.druid.sql.ast.statement.SQLGrantStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLInsertStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLJoinTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLMergeStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLObjectType;
import com.tranboot.client.druid.sql.ast.statement.SQLOpenStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLPrimaryKey;
import com.tranboot.client.druid.sql.ast.statement.SQLRevokeStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLRollbackStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLSelect;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectItem;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectQuery;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLSetStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLShowTablesStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLTableElement;
import com.tranboot.client.druid.sql.ast.statement.SQLTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLTruncateStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLUnique;
import com.tranboot.client.druid.sql.ast.statement.SQLUniqueConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLUseStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.tranboot.client.druid.sql.ast.statement.SQLWithSubqueryClause.Entry;
import com.tranboot.client.druid.sql.dialect.mysql.ast.expr.MySqlExpr;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.tranboot.client.druid.sql.dialect.oracle.ast.expr.OracleExpr;
import com.tranboot.client.druid.stat.TableStat;
import com.tranboot.client.druid.stat.TableStat.Column;
import com.tranboot.client.druid.stat.TableStat.Condition;
import com.tranboot.client.druid.stat.TableStat.Mode;
import com.tranboot.client.druid.stat.TableStat.Name;
import com.tranboot.client.druid.stat.TableStat.Relationship;
import com.tranboot.client.druid.util.StringUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SchemaStatVisitor extends SQLASTVisitorAdapter {
    protected final HashMap<Name, TableStat> tableStats;
    protected final Map<Column, Column> columns;
    protected final List<Condition> conditions;
    protected final Set<Relationship> relationships;
    protected final List<Column> orderByColumns;
    protected final Set<Column> groupByColumns;
    protected final List<SQLAggregateExpr> aggregateFunctions;
    protected final List<SQLMethodInvokeExpr> functions;
    protected final Map<String, SQLObject> subQueryMap;
    protected final Map<String, SQLObject> variants;
    protected Map<String, String> aliasMap;
    protected String currentTable;
    public static final String ATTR_TABLE = "_table_";
    public static final String ATTR_COLUMN = "_column_";
    private List<Object> parameters;
    private Mode mode;

    public SchemaStatVisitor() {
        this(new ArrayList());
    }

    public SchemaStatVisitor(List<Object> parameters) {
        this.tableStats = new LinkedHashMap();
        this.columns = new LinkedHashMap();
        this.conditions = new ArrayList();
        this.relationships = new LinkedHashSet();
        this.orderByColumns = new ArrayList();
        this.groupByColumns = new LinkedHashSet();
        this.aggregateFunctions = new ArrayList();
        this.functions = new ArrayList(2);
        this.subQueryMap = new LinkedHashMap();
        this.variants = new LinkedHashMap();
        this.aliasMap = new LinkedHashMap();
        this.parameters = parameters;
    }

    public List<Object> getParameters() {
        return this.parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    public TableStat getTableStat(String ident) {
        return this.getTableStat(ident, (String)null);
    }

    public Column addColumn(String tableName, String columnName) {
        tableName = this.handleName(tableName);
        columnName = this.handleName(columnName);
        Column column = this.getColumn(tableName, columnName);
        if (column == null && columnName != null) {
            column = new Column(tableName, columnName);
            this.columns.put(column, column);
        }

        return column;
    }

    public TableStat getTableStat(String tableName, String alias) {
        if (this.variants.containsKey(tableName)) {
            return null;
        } else {
            tableName = this.handleName(tableName);
            Name tableNameObj = new Name(tableName);
            TableStat stat = (TableStat)this.tableStats.get(tableNameObj);
            if (stat == null) {
                stat = new TableStat();
                this.tableStats.put(new Name(tableName), stat);
                putAliasMap(this.aliasMap, alias, tableName);
            }

            return stat;
        }
    }

    private String handleName(String ident) {
        int len = ident.length();
        if (ident.charAt(0) == '[' && ident.charAt(len - 1) == ']') {
            ident = ident.substring(1, len - 1);
        } else {
            boolean flag0 = false;
            boolean flag1 = false;
            boolean flag2 = false;
            boolean flag3 = false;

            for(int i = 0; i < len; ++i) {
                char ch = ident.charAt(i);
                if (ch == '"') {
                    flag0 = true;
                } else if (ch == '`') {
                    flag1 = true;
                } else if (ch == ' ') {
                    flag2 = true;
                } else if (ch == '\'') {
                    flag3 = true;
                }
            }

            if (flag0) {
                ident = ident.replaceAll("\"", "");
            }

            if (flag1) {
                ident = ident.replaceAll("`", "");
            }

            if (flag2) {
                ident = ident.replaceAll(" ", "");
            }

            if (flag3) {
                ident = ident.replaceAll("'", "");
            }
        }

        ident = this.aliasWrap(ident);
        return ident;
    }

    public Map<String, SQLObject> getVariants() {
        return this.variants;
    }

    public void setAliasMap() {
        this.setAliasMap(new HashMap());
    }

    public void clearAliasMap() {
        this.aliasMap = null;
    }

    public void setAliasMap(Map<String, String> aliasMap) {
        this.aliasMap = aliasMap;
    }

    public Map<String, String> getAliasMap() {
        return this.aliasMap;
    }

    public void setCurrentTable(String table) {
        this.currentTable = table;
    }

    public void setCurrentTable(SQLObject x) {
        x.putAttribute("_old_local_", this.currentTable);
    }

    public void restoreCurrentTable(SQLObject x) {
        String table = (String)x.getAttribute("_old_local_");
        this.currentTable = table;
    }

    public void setCurrentTable(SQLObject x, String table) {
        x.putAttribute("_old_local_", this.currentTable);
        this.currentTable = table;
    }

    public String getCurrentTable() {
        return this.currentTable;
    }

    protected Mode getMode() {
        return this.mode;
    }

    protected void setModeOrigin(SQLObject x) {
        Mode originalMode = (Mode)x.getAttribute("_original_use_mode");
        this.mode = originalMode;
    }

    protected Mode setMode(SQLObject x, Mode mode) {
        Mode oldMode = this.mode;
        x.putAttribute("_original_use_mode", oldMode);
        this.mode = mode;
        return oldMode;
    }

    private boolean visitOrderBy(SQLIdentifierExpr x) {
        if (this.containsSubQuery(this.currentTable)) {
            return false;
        } else {
            String identName = x.getName();
            if (this.aliasMap != null && this.aliasMap.containsKey(identName) && this.aliasMap.get(identName) == null) {
                return false;
            } else {
                if (this.currentTable != null) {
                    this.orderByAddColumn(this.currentTable, identName, x);
                } else {
                    this.orderByAddColumn("UNKOWN", identName, x);
                }

                return false;
            }
        }
    }

    private boolean visitOrderBy(SQLPropertyExpr x) {
        if (x.getOwner() instanceof SQLIdentifierExpr) {
            String owner = ((SQLIdentifierExpr)x.getOwner()).getName();
            if (this.containsSubQuery(owner)) {
                return false;
            }

            owner = this.aliasWrap(owner);
            if (owner != null) {
                this.orderByAddColumn(owner, x.getName(), x);
            }
        }

        return false;
    }

    private void orderByAddColumn(String table, String columnName, SQLObject expr) {
        Column column = new Column(table, columnName);
        SQLObject parent = expr.getParent();
        if (parent instanceof SQLSelectOrderByItem) {
            SQLOrderingSpecification type = ((SQLSelectOrderByItem)parent).getType();
            column.getAttributes().put("orderBy.type", type);
        }

        this.orderByColumns.add(column);
    }

    public boolean visit(SQLOrderBy x) {
        SQLASTVisitor orderByVisitor = this.createOrderByVisitor(x);
        SQLSelectQueryBlock query = null;
        if (x.getParent() instanceof SQLSelectQueryBlock) {
            query = (SQLSelectQueryBlock)x.getParent();
        }

        if (query != null) {
            Iterator var4 = x.getItems().iterator();

            while(var4.hasNext()) {
                SQLSelectOrderByItem item = (SQLSelectOrderByItem)var4.next();
                SQLExpr expr = item.getExpr();
                if (expr instanceof SQLIntegerExpr) {
                    int intValue = ((SQLIntegerExpr)expr).getNumber().intValue() - 1;
                    if (intValue < query.getSelectList().size()) {
                        SQLSelectItem selectItem = (SQLSelectItem)query.getSelectList().get(intValue);
                        selectItem.getExpr().accept(orderByVisitor);
                    }
                } else if (!(expr instanceof MySqlExpr) && expr instanceof OracleExpr) {
                }
            }
        }

        x.accept(orderByVisitor);
        return true;
    }

    protected SQLASTVisitor createOrderByVisitor(SQLOrderBy x) {
        Object orderByVisitor;
        if ("mysql".equals(this.getDbType())) {
            orderByVisitor = new SchemaStatVisitor.MySqlOrderByStatVisitor(x);
        } else {
            orderByVisitor = new SchemaStatVisitor.OrderByStatVisitor(x);
        }

        return (SQLASTVisitor)orderByVisitor;
    }

    public Set<Relationship> getRelationships() {
        return this.relationships;
    }

    public List<Column> getOrderByColumns() {
        return this.orderByColumns;
    }

    public Set<Column> getGroupByColumns() {
        return this.groupByColumns;
    }

    public List<Condition> getConditions() {
        return this.conditions;
    }

    public List<SQLAggregateExpr> getAggregateFunctions() {
        return this.aggregateFunctions;
    }

    public boolean visit(SQLBinaryOpExpr x) {
        x.getLeft().setParent(x);
        x.getRight().setParent(x);
        switch(x.getOperator()) {
            case Equality:
            case NotEqual:
            case GreaterThan:
            case GreaterThanOrEqual:
            case LessThan:
            case LessThanOrGreater:
            case LessThanOrEqual:
            case LessThanOrEqualOrGreaterThan:
            case Like:
            case NotLike:
            case Is:
            case IsNot:
                this.handleCondition(x.getLeft(), x.getOperator().name, x.getRight());
                this.handleCondition(x.getRight(), x.getOperator().name, x.getLeft());
                this.handleRelationship(x.getLeft(), x.getOperator().name, x.getRight());
            default:
                return true;
        }
    }

    protected void handleRelationship(SQLExpr left, String operator, SQLExpr right) {
        Column leftColumn = this.getColumn(left);
        if (leftColumn != null) {
            Column rightColumn = this.getColumn(right);
            if (rightColumn != null) {
                Relationship relationship = new Relationship();
                relationship.setLeft(leftColumn);
                relationship.setRight(rightColumn);
                relationship.setOperator(operator);
                this.relationships.add(relationship);
            }
        }
    }

    protected void handleCondition(SQLExpr expr, String operator, List<SQLExpr> values) {
        this.handleCondition(expr, operator, (SQLExpr[])values.toArray(new SQLExpr[values.size()]));
    }

    protected void handleCondition(SQLExpr expr, String operator, SQLExpr... valueExprs) {
        if (expr instanceof SQLCastExpr) {
            expr = ((SQLCastExpr)expr).getExpr();
        }

        Column column = this.getColumn(expr);
        if (column != null) {
            Condition condition = null;
            Iterator var6 = this.getConditions().iterator();

            while(var6.hasNext()) {
                Condition item = (Condition)var6.next();
                if (item.getColumn().equals(column) && item.getOperator().equals(operator)) {
                    condition = item;
                    break;
                }
            }

            if (condition == null) {
                condition = new Condition();
                condition.setColumn(column);
                condition.setOperator(operator);
                this.conditions.add(condition);
            }

            SQLExpr[] var12 = valueExprs;
            int var13 = valueExprs.length;

            for(int var8 = 0; var8 < var13; ++var8) {
                SQLExpr item = var12[var8];
                Column valueColumn = this.getColumn(item);
                if (valueColumn == null) {
                    Object value = SQLEvalVisitorUtils.eval(this.getDbType(), item, this.parameters, false);
                    condition.getValues().add(value);
                }
            }

        }
    }

    public String getDbType() {
        return null;
    }

    protected Column getColumn(SQLExpr expr) {
        Map<String, String> aliasMap = this.getAliasMap();
        if (aliasMap == null) {
            return null;
        } else {
            if (expr instanceof SQLMethodInvokeExpr) {
                SQLMethodInvokeExpr methodInvokeExp = (SQLMethodInvokeExpr)expr;
                if (methodInvokeExp.getParameters().size() == 1) {
                    SQLExpr firstExpr = (SQLExpr)methodInvokeExp.getParameters().get(0);
                    return this.getColumn(firstExpr);
                }
            }

            if (expr instanceof SQLCastExpr) {
                expr = ((SQLCastExpr)expr).getExpr();
            }

            String column;
            String table;
            if (expr instanceof SQLPropertyExpr) {
                SQLExpr owner = ((SQLPropertyExpr)expr).getOwner();
                column = ((SQLPropertyExpr)expr).getName();
                if (owner instanceof SQLIdentifierExpr) {
                    table = ((SQLIdentifierExpr)owner).getName();
                    String tableNameLower = table.toLowerCase();
                    if (aliasMap.containsKey(tableNameLower)) {
                        table = (String)aliasMap.get(tableNameLower);
                    }

                    if (this.containsSubQuery(tableNameLower)) {
                        table = null;
                    }

                    if (this.variants.containsKey(table)) {
                        return null;
                    } else {
                        return table != null ? new Column(table, column) : this.handleSubQueryColumn(table, column);
                    }
                } else {
                    return null;
                }
            } else if (expr instanceof SQLIdentifierExpr) {
                Column attrColumn = (Column)expr.getAttribute("_column_");
                if (attrColumn != null) {
                    return attrColumn;
                } else {
                    column = ((SQLIdentifierExpr)expr).getName();
                    table = this.getCurrentTable();
                    if (table != null && aliasMap.containsKey(table)) {
                        table = (String)aliasMap.get(table);
                        if (table == null) {
                            return null;
                        }
                    }

                    if (table != null) {
                        return new Column(table, column);
                    } else {
                        return this.variants.containsKey(column) ? null : new Column("UNKNOWN", column);
                    }
                }
            } else {
                return null;
            }
        }
    }

    public boolean visit(SQLTruncateStatement x) {
        this.setMode(x, Mode.Delete);
        this.setAliasMap();
        String originalTable = this.getCurrentTable();
        Iterator var3 = x.getTableSources().iterator();

        while(var3.hasNext()) {
            SQLExprTableSource tableSource = (SQLExprTableSource)var3.next();
            SQLName name = (SQLName)tableSource.getExpr();
            String ident = name.toString();
            this.setCurrentTable(ident);
            x.putAttribute("_old_local_", originalTable);
            TableStat stat = this.getTableStat(ident);
            stat.incrementDeleteCount();
            Map<String, String> aliasMap = this.getAliasMap();
            putAliasMap(aliasMap, ident, ident);
        }

        return false;
    }

    public boolean visit(SQLDropViewStatement x) {
        this.setMode(x, Mode.Drop);
        return true;
    }

    public boolean visit(SQLDropTableStatement x) {
        this.setMode(x, Mode.Insert);
        this.setAliasMap();
        String originalTable = this.getCurrentTable();
        Iterator var3 = x.getTableSources().iterator();

        while(var3.hasNext()) {
            SQLExprTableSource tableSource = (SQLExprTableSource)var3.next();
            SQLName name = (SQLName)tableSource.getExpr();
            String ident = name.toString();
            this.setCurrentTable(ident);
            x.putAttribute("_old_local_", originalTable);
            TableStat stat = this.getTableStat(ident);
            stat.incrementDropCount();
            Map<String, String> aliasMap = this.getAliasMap();
            putAliasMap(aliasMap, ident, ident);
        }

        return false;
    }

    public boolean visit(SQLInsertStatement x) {
        this.setMode(x, Mode.Insert);
        this.setAliasMap();
        String originalTable = this.getCurrentTable();
        if (x.getTableName() instanceof SQLName) {
            String ident = x.getTableName().toString();
            this.setCurrentTable(ident);
            x.putAttribute("_old_local_", originalTable);
            TableStat stat = this.getTableStat(ident);
            stat.incrementInsertCount();
            Map<String, String> aliasMap = this.getAliasMap();
            putAliasMap(aliasMap, x.getAlias(), ident);
            putAliasMap(aliasMap, ident, ident);
        }

        this.accept(x.getColumns());
        this.accept((SQLObject)x.getQuery());
        return false;
    }

    protected static void putAliasMap(Map<String, String> aliasMap, String name, String value) {
        if (aliasMap != null && name != null) {
            aliasMap.put(name.toLowerCase(), value);
        }
    }

    protected void accept(SQLObject x) {
        if (x != null) {
            x.accept(this);
        }

    }

    protected void accept(List<? extends SQLObject> nodes) {
        int i = 0;

        for(int size = nodes.size(); i < size; ++i) {
            this.accept((SQLObject)nodes.get(i));
        }

    }

    public boolean visit(SQLSelectQueryBlock x) {
        if (x.getFrom() == null) {
            return false;
        } else {
            this.setMode(x, Mode.Select);
            if (x.getFrom() instanceof SQLSubqueryTableSource) {
                x.getFrom().accept(this);
                return false;
            } else {
                String table;
                if (x.getInto() != null && x.getInto().getExpr() instanceof SQLName) {
                    SQLName into = (SQLName)x.getInto().getExpr();
                    table = into.toString();
                    TableStat stat = this.getTableStat(table);
                    if (stat != null) {
                        stat.incrementInsertCount();
                    }
                }

                String originalTable = this.getCurrentTable();
                if (x.getFrom() instanceof SQLExprTableSource) {
                    SQLExprTableSource tableSource = (SQLExprTableSource)x.getFrom();
                    if (tableSource.getExpr() instanceof SQLName) {
                        String ident = tableSource.getExpr().toString();
                        this.setCurrentTable(x, ident);
                        x.putAttribute("_table_", ident);
                        if (x.getParent() instanceof SQLSelect) {
                            x.getParent().putAttribute("_table_", ident);
                        }

                        x.putAttribute("_old_local_", originalTable);
                    }
                }

                if (x.getFrom() != null) {
                    x.getFrom().accept(this);
                    table = (String)x.getFrom().getAttribute("_table_");
                    if (table != null) {
                        x.putAttribute("_table_", table);
                    }
                }

                if (x.getWhere() != null) {
                    x.getWhere().setParent(x);
                }

                return true;
            }
        }
    }

    public void endVisit(SQLSelectQueryBlock x) {
        String originalTable = (String)x.getAttribute("_old_local_");
        x.putAttribute("table", this.getCurrentTable());
        this.setCurrentTable(originalTable);
        this.setModeOrigin(x);
    }

    public boolean visit(SQLJoinTableSource x) {
        x.getLeft().accept(this);
        x.getRight().accept(this);
        SQLExpr condition = x.getCondition();
        if (condition != null) {
            condition.accept(this);
        }

        return false;
    }

    public boolean visit(SQLPropertyExpr x) {
        if (x.getOwner() instanceof SQLIdentifierExpr) {
            String owner = ((SQLIdentifierExpr)x.getOwner()).getName();
            if (this.containsSubQuery(owner)) {
                return false;
            }

            owner = this.aliasWrap(owner);
            if (owner != null) {
                Column column = this.addColumn(owner, x.getName());
                x.putAttribute("_column_", column);
                if (column != null) {
                    if (this.isParentGroupBy(x)) {
                        this.groupByColumns.add(column);
                    }

                    if (column != null) {
                        this.setColumn(x, column);
                    }
                }
            }
        }

        return false;
    }

    protected String aliasWrap(String name) {
        Map<String, String> aliasMap = this.getAliasMap();
        if (aliasMap != null) {
            if (aliasMap.containsKey(name)) {
                return (String)aliasMap.get(name);
            }

            String name_lcase = name.toLowerCase();
            if (name_lcase != name && aliasMap.containsKey(name_lcase)) {
                return (String)aliasMap.get(name_lcase);
            }
        }

        return name;
    }

    protected Column handleSubQueryColumn(String owner, String alias) {
        SQLObject query = this.getSubQuery(owner);
        return query == null ? null : this.handleSubQueryColumn(alias, query);
    }

    protected Column handleSubQueryColumn(String alias, SQLObject query) {
        if (query instanceof SQLSelect) {
            query = ((SQLSelect)query).getQuery();
        }

        SQLSelectQueryBlock queryBlock = null;
        List<SQLSelectItem> selectList = null;
        if (query instanceof SQLSelectQueryBlock) {
            queryBlock = (SQLSelectQueryBlock)query;
            if (queryBlock.getGroupBy() != null) {
                return null;
            }

            selectList = queryBlock.getSelectList();
        }

        if (selectList != null) {
            boolean allColumn = false;
            String allColumnOwner = null;
            Iterator var7 = selectList.iterator();

            while(var7.hasNext()) {
                SQLSelectItem item = (SQLSelectItem)var7.next();
                if (item.getClass().equals(SQLSelectItem.class)) {
                    String itemAlias = item.getAlias();
                    SQLExpr itemExpr = item.getExpr();
                    String ident = itemAlias;
                    if (itemAlias == null) {
                        if (itemExpr instanceof SQLIdentifierExpr) {
                            ident = itemAlias = itemExpr.toString();
                        } else if (itemExpr instanceof SQLPropertyExpr) {
                            itemAlias = ((SQLPropertyExpr)itemExpr).getName();
                            ident = itemExpr.toString();
                        }
                    }

                    if (alias.equalsIgnoreCase(itemAlias)) {
                        Column column = (Column)itemExpr.getAttribute("_column_");
                        if (column != null) {
                            return column;
                        }

                        SQLTableSource from = queryBlock.getFrom();
                        if (from instanceof SQLSubqueryTableSource) {
                            SQLSelect select = ((SQLSubqueryTableSource)from).getSelect();
                            Column subQueryColumn = this.handleSubQueryColumn(ident, (SQLObject)select);
                            return subQueryColumn;
                        }
                    }

                    if (itemExpr instanceof SQLAllColumnExpr) {
                        allColumn = true;
                    } else if (itemExpr instanceof SQLPropertyExpr) {
                        SQLPropertyExpr propertyExpr = (SQLPropertyExpr)itemExpr;
                        if (propertyExpr.getName().equals("*")) {
                            SQLExpr owner = propertyExpr.getOwner();
                            if (owner instanceof SQLIdentifierExpr) {
                                allColumnOwner = ((SQLIdentifierExpr)owner).getName();
                                allColumn = true;
                            }
                        }
                    }
                }
            }

            if (allColumn) {
                SQLTableSource from = queryBlock.getFrom();
                String tableName = this.getTable(from, allColumnOwner);
                if (tableName != null) {
                    return new Column(tableName, alias);
                }
            }
        }

        return null;
    }

    private String getTable(SQLTableSource from, String tableAlias) {
        if (from instanceof SQLExprTableSource) {
            boolean aliasEq = StringUtils.equals(from.getAlias(), tableAlias);
            SQLExpr tableSourceExpr = ((SQLExprTableSource)from).getExpr();
            if (tableSourceExpr instanceof SQLName) {
                String tableName = ((SQLName)tableSourceExpr).toString();
                if (tableAlias == null || aliasEq) {
                    return tableName;
                }
            }
        } else if (from instanceof SQLJoinTableSource) {
            SQLJoinTableSource joinTableSource = (SQLJoinTableSource)from;
            String leftMatchTableName = this.getTable(joinTableSource.getLeft(), tableAlias);
            if (leftMatchTableName != null) {
                return leftMatchTableName;
            }

            return this.getTable(joinTableSource.getRight(), tableAlias);
        }

        return null;
    }

    public boolean visit(SQLIdentifierExpr x) {
        String currentTable = this.getCurrentTable();
        if (this.containsSubQuery(currentTable)) {
            return false;
        } else {
            String ident = x.toString();
            if (this.variants.containsKey(ident)) {
                return false;
            } else {
                Column column = null;
                if (currentTable != null) {
                    column = this.addColumn(currentTable, ident);
                    if (column != null && this.isParentGroupBy(x)) {
                        this.groupByColumns.add(column);
                    }

                    x.putAttribute("_column_", column);
                } else {
                    boolean skip = false;

                    for(SQLObject parent = x.getParent(); parent != null; parent = parent.getParent()) {
                        if (parent instanceof SQLSelectQueryBlock) {
                            SQLTableSource var7 = ((SQLSelectQueryBlock)parent).getFrom();
                        } else if (parent instanceof SQLSelectQuery) {
                            break;
                        }
                    }

                    if (!skip) {
                        column = this.handleUnkownColumn(ident);
                    }

                    if (column != null) {
                        x.putAttribute("_column_", column);
                    }
                }

                if (column != null) {
                    SQLObject parent = x.getParent();
                    if (parent instanceof SQLPrimaryKey) {
                        column.setPrimaryKey(true);
                    } else if (parent instanceof SQLUnique) {
                        column.setUnique(true);
                    }

                    this.setColumn(x, column);
                }

                return false;
            }
        }
    }

    private boolean isParentSelectItem(SQLObject parent) {
        if (parent == null) {
            return false;
        } else if (parent instanceof SQLSelectItem) {
            return true;
        } else {
            return parent instanceof SQLSelectQueryBlock ? false : this.isParentSelectItem(parent.getParent());
        }
    }

    private boolean isParentGroupBy(SQLObject parent) {
        if (parent == null) {
            return false;
        } else {
            return parent instanceof SQLSelectGroupByClause ? true : this.isParentGroupBy(parent.getParent());
        }
    }

    private void setColumn(SQLExpr x, Column column) {
        Object current = x;

        while(true) {
            SQLObject parent = ((SQLObject)current).getParent();
            if (parent == null) {
                break;
            }

            if (parent instanceof SQLSelectQueryBlock) {
                SQLSelectQueryBlock query = (SQLSelectQueryBlock)parent;
                if (query.getWhere() == current) {
                    column.setWhere(true);
                }
                break;
            }

            if (parent instanceof SQLSelectGroupByClause) {
                SQLSelectGroupByClause groupBy = (SQLSelectGroupByClause)parent;
                if (current == groupBy.getHaving()) {
                    column.setHaving(true);
                } else if (groupBy.getItems().contains(current)) {
                    column.setGroupBy(true);
                }
                break;
            }

            if (this.isParentSelectItem(parent)) {
                column.setSelec(true);
                break;
            }

            if (parent instanceof SQLJoinTableSource) {
                SQLJoinTableSource join = (SQLJoinTableSource)parent;
                if (join.getCondition() == current) {
                    column.setJoin(true);
                }
                break;
            }

            current = parent;
        }

    }

    protected Column handleUnkownColumn(String columnName) {
        return this.addColumn("UNKNOWN", columnName);
    }

    public boolean visit(SQLAllColumnExpr x) {
        String currentTable = this.getCurrentTable();
        if (this.containsSubQuery(currentTable)) {
            return false;
        } else {
            if (x.getParent() instanceof SQLAggregateExpr) {
                SQLAggregateExpr aggregateExpr = (SQLAggregateExpr)x.getParent();
                if ("count".equalsIgnoreCase(aggregateExpr.getMethodName())) {
                    return false;
                }
            }

            if (currentTable != null) {
                Column column = this.addColumn(currentTable, "*");
                if (this.isParentSelectItem(x.getParent())) {
                    column.setSelec(true);
                }
            }

            return false;
        }
    }

    public Map<Name, TableStat> getTables() {
        return this.tableStats;
    }

    public boolean containsTable(String tableName) {
        return this.tableStats.containsKey(new Name(tableName));
    }

    public boolean containsColumn(String tableName, String columnName) {
        return this.columns.containsKey(new Column(tableName, columnName));
    }

    public Collection<Column> getColumns() {
        return this.columns.values();
    }

    public Column getColumn(String tableName, String columnName) {
        if (this.aliasMap != null && this.aliasMap.containsKey(columnName) && this.aliasMap.get(columnName) == null) {
            return null;
        } else {
            Column column = new Column(tableName, columnName);
            return (Column)this.columns.get(column);
        }
    }

    public boolean visit(SQLSelectStatement x) {
        this.setAliasMap();
        return true;
    }

    public void endVisit(SQLSelectStatement x) {
    }

    public boolean visit(Entry x) {
        String alias = x.getName().toString();
        Map<String, String> aliasMap = this.getAliasMap();
        SQLWithSubqueryClause with = (SQLWithSubqueryClause)x.getParent();
        if (Boolean.TRUE == with.getRecursive()) {
            if (aliasMap != null && alias != null) {
                putAliasMap(aliasMap, alias, (String)null);
                this.addSubQuery(alias, x.getSubQuery().getQuery());
            }

            x.getSubQuery().accept(this);
        } else {
            x.getSubQuery().accept(this);
            if (aliasMap != null && alias != null) {
                putAliasMap(aliasMap, alias, (String)null);
                this.addSubQuery(alias, x.getSubQuery().getQuery());
            }
        }

        return false;
    }

    public boolean visit(SQLSubqueryTableSource x) {
        x.getSelect().accept(this);
        SQLSelectQuery query = x.getSelect().getQuery();
        Map<String, String> aliasMap = this.getAliasMap();
        if (aliasMap != null && x.getAlias() != null) {
            putAliasMap(aliasMap, x.getAlias(), (String)null);
            this.addSubQuery(x.getAlias(), query);
        }

        return false;
    }

    protected void addSubQuery(String alias, SQLObject query) {
        String alias_lcase = alias.toLowerCase();
        this.subQueryMap.put(alias_lcase, query);
    }

    protected SQLObject getSubQuery(String alias) {
        String alias_lcase = alias.toLowerCase();
        return (SQLObject)this.subQueryMap.get(alias_lcase);
    }

    protected boolean containsSubQuery(String alias) {
        if (alias == null) {
            return false;
        } else {
            String alias_lcase = alias.toLowerCase();
            return this.subQueryMap.containsKey(alias_lcase);
        }
    }

    protected boolean isSimpleExprTableSource(SQLExprTableSource x) {
        return x.getExpr() instanceof SQLName;
    }

    public boolean visit(SQLExprTableSource x) {
        if (this.isSimpleExprTableSource(x)) {
            String ident = x.getExpr().toString();
            if (this.variants.containsKey(ident)) {
                return false;
            }

            if (this.containsSubQuery(ident)) {
                return false;
            }

            Map<String, String> aliasMap = this.getAliasMap();
            TableStat stat = this.getTableStat(ident);
            Mode mode = this.getMode();
            if (mode != null) {
                switch(mode) {
                    case Delete:
                        stat.incrementDeleteCount();
                        break;
                    case Insert:
                        stat.incrementInsertCount();
                        break;
                    case Update:
                        stat.incrementUpdateCount();
                        break;
                    case Select:
                        stat.incrementSelectCount();
                        break;
                    case Merge:
                        stat.incrementMergeCount();
                        break;
                    case Drop:
                        stat.incrementDropCount();
                }
            }

            if (aliasMap != null) {
                String alias = x.getAlias();
                if (alias != null && !aliasMap.containsKey(alias)) {
                    putAliasMap(aliasMap, alias, ident);
                }

                if (!aliasMap.containsKey(ident)) {
                    putAliasMap(aliasMap, ident, ident);
                }
            }
        } else {
            this.accept((SQLObject)x.getExpr());
        }

        return false;
    }

    public boolean visit(SQLSelectItem x) {
        x.getExpr().accept(this);
        String alias = x.getAlias();
        Map<String, String> aliasMap = this.getAliasMap();
        if (alias != null && !alias.isEmpty() && aliasMap != null) {
            if (x.getExpr() instanceof SQLName) {
                putAliasMap(aliasMap, alias, x.getExpr().toString());
            } else {
                putAliasMap(aliasMap, alias, (String)null);
            }
        }

        return false;
    }

    public void endVisit(SQLSelect x) {
        this.restoreCurrentTable(x);
    }

    public boolean visit(SQLSelect x) {
        this.setCurrentTable((SQLObject)x);
        if (x.getOrderBy() != null) {
            x.getOrderBy().setParent(x);
        }

        this.accept((SQLObject)x.getWithSubQuery());
        this.accept((SQLObject)x.getQuery());
        String originalTable = this.getCurrentTable();
        this.setCurrentTable((String)x.getQuery().getAttribute("table"));
        x.putAttribute("_old_local_", originalTable);
        this.accept((SQLObject)x.getOrderBy());
        this.setCurrentTable(originalTable);
        return false;
    }

    public boolean visit(SQLAggregateExpr x) {
        this.aggregateFunctions.add(x);
        this.accept(x.getArguments());
        this.accept((SQLObject)x.getWithinGroup());
        this.accept((SQLObject)x.getOver());
        return false;
    }

    public boolean visit(SQLMethodInvokeExpr x) {
        this.functions.add(x);
        this.accept(x.getParameters());
        return false;
    }

    public boolean visit(SQLUpdateStatement x) {
        this.setAliasMap();
        this.setMode(x, Mode.Update);
        SQLName identName = x.getTableName();
        if (identName != null) {
            String ident = identName.toString();
            this.setCurrentTable(ident);
            TableStat stat = this.getTableStat(ident);
            stat.incrementUpdateCount();
            Map<String, String> aliasMap = this.getAliasMap();
            putAliasMap(aliasMap, ident, ident);
        } else {
            x.getTableSource().accept(this);
        }

        this.accept((SQLObject)x.getFrom());
        this.accept(x.getItems());
        this.accept((SQLObject)x.getWhere());
        return false;
    }

    public boolean visit(SQLDeleteStatement x) {
        this.setAliasMap();
        this.setMode(x, Mode.Delete);
        String tableName = x.getTableName().toString();
        this.setCurrentTable(tableName);
        if (x.getAlias() != null) {
            putAliasMap(this.aliasMap, x.getAlias(), tableName);
        }

        if (x.getTableSource() instanceof SQLSubqueryTableSource) {
            SQLSelectQuery selectQuery = ((SQLSubqueryTableSource)x.getTableSource()).getSelect().getQuery();
            if (selectQuery instanceof SQLSelectQueryBlock) {
                SQLSelectQueryBlock subQueryBlock = (SQLSelectQueryBlock)selectQuery;
                subQueryBlock.getWhere().accept(this);
            }
        }

        TableStat stat = this.getTableStat(tableName);
        stat.incrementDeleteCount();
        this.accept((SQLObject)x.getWhere());
        return false;
    }

    public boolean visit(SQLInListExpr x) {
        if (x.isNot()) {
            this.handleCondition(x.getExpr(), "NOT IN", x.getTargetList());
        } else {
            this.handleCondition(x.getExpr(), "IN", x.getTargetList());
        }

        return true;
    }

    public boolean visit(SQLInSubQueryExpr x) {
        if (x.isNot()) {
            this.handleCondition(x.getExpr(), "NOT IN");
        } else {
            this.handleCondition(x.getExpr(), "IN");
        }

        return true;
    }

    public void endVisit(SQLDeleteStatement x) {
    }

    public void endVisit(SQLUpdateStatement x) {
    }

    public boolean visit(SQLCreateTableStatement x) {
        Iterator var2 = x.getTableElementList().iterator();

        while(var2.hasNext()) {
            SQLTableElement e = (SQLTableElement)var2.next();
            e.setParent(x);
        }

        String tableName = x.getName().toString();
        TableStat stat = this.getTableStat(tableName);
        stat.incrementCreateCount();
        this.setCurrentTable(x, tableName);
        this.accept(x.getTableElementList());
        this.restoreCurrentTable(x);
        if (x.getInherits() != null) {
            x.getInherits().accept(this);
        }

        if (x.getSelect() != null) {
            x.getSelect().accept(this);
        }

        return false;
    }

    public boolean visit(SQLColumnDefinition x) {
        String tableName = null;
        SQLObject parent = x.getParent();
        if (parent instanceof SQLCreateTableStatement) {
            tableName = ((SQLCreateTableStatement)parent).getName().toString();
        }

        if (tableName == null) {
            return true;
        } else {
            String columnName = x.getName().toString();
            Column column = this.addColumn(tableName, columnName);
            if (x.getDataType() != null) {
                column.setDataType(x.getDataType().getName());
            }

            Iterator var5 = x.getConstraints().iterator();

            while(var5.hasNext()) {
                SQLColumnConstraint item = (SQLColumnConstraint)var5.next();
                if (item instanceof SQLPrimaryKey) {
                    column.setPrimaryKey(true);
                } else if (item instanceof SQLUnique) {
                    column.setUnique(true);
                }
            }

            return false;
        }
    }

    public boolean visit(SQLCallStatement x) {
        return false;
    }

    public void endVisit(SQLCommentStatement x) {
    }

    public boolean visit(SQLCommentStatement x) {
        return false;
    }

    public boolean visit(SQLCurrentOfCursorExpr x) {
        return false;
    }

    public boolean visit(SQLAlterTableAddColumn x) {
        SQLAlterTableStatement stmt = (SQLAlterTableStatement)x.getParent();
        String table = stmt.getName().toString();
        Iterator var4 = x.getColumns().iterator();

        while(var4.hasNext()) {
            SQLColumnDefinition column = (SQLColumnDefinition)var4.next();
            String columnName = column.getName().toString();
            this.addColumn(table, columnName);
        }

        return false;
    }

    public void endVisit(SQLAlterTableAddColumn x) {
    }

    public boolean visit(SQLRollbackStatement x) {
        return false;
    }

    public boolean visit(SQLCreateViewStatement x) {
        x.getSubQuery().accept(this);
        return false;
    }

    public boolean visit(SQLAlterTableDropForeignKey x) {
        return false;
    }

    public boolean visit(SQLUseStatement x) {
        return false;
    }

    public boolean visit(SQLAlterTableDisableConstraint x) {
        return false;
    }

    public boolean visit(SQLAlterTableEnableConstraint x) {
        return false;
    }

    public boolean visit(SQLAlterTableStatement x) {
        String tableName = x.getName().toString();
        TableStat stat = this.getTableStat(tableName);
        stat.incrementAlterCount();
        this.setCurrentTable(x, tableName);
        Iterator var4 = x.getItems().iterator();

        while(var4.hasNext()) {
            SQLAlterTableItem item = (SQLAlterTableItem)var4.next();
            item.setParent(x);
            item.accept(this);
        }

        return false;
    }

    public boolean visit(SQLAlterTableDropConstraint x) {
        return false;
    }

    public boolean visit(SQLDropIndexStatement x) {
        this.setMode(x, Mode.DropIndex);
        SQLExprTableSource table = x.getTableName();
        if (table != null) {
            SQLName name = (SQLName)table.getExpr();
            String ident = name.toString();
            this.setCurrentTable(ident);
            TableStat stat = this.getTableStat(ident);
            stat.incrementDropIndexCount();
            Map<String, String> aliasMap = this.getAliasMap();
            putAliasMap(aliasMap, ident, ident);
        }

        return false;
    }

    public boolean visit(SQLCreateIndexStatement x) {
        this.setMode(x, Mode.CreateIndex);
        SQLName name = (SQLName)((SQLExprTableSource)x.getTable()).getExpr();
        String table = name.toString();
        this.setCurrentTable(table);
        TableStat stat = this.getTableStat(table);
        stat.incrementDropIndexCount();
        Map<String, String> aliasMap = this.getAliasMap();
        putAliasMap(aliasMap, table, table);
        Iterator var6 = x.getItems().iterator();

        while(var6.hasNext()) {
            SQLSelectOrderByItem item = (SQLSelectOrderByItem)var6.next();
            SQLExpr expr = item.getExpr();
            if (expr instanceof SQLIdentifierExpr) {
                SQLIdentifierExpr identExpr = (SQLIdentifierExpr)expr;
                String columnName = identExpr.getName();
                this.addColumn(table, columnName);
            }
        }

        return false;
    }

    public boolean visit(SQLForeignKeyImpl x) {
        Iterator var2 = x.getReferencingColumns().iterator();

        while(var2.hasNext()) {
            SQLName column = (SQLName)var2.next();
            column.accept(this);
        }

        String table = x.getReferencedTableName().getSimpleName();
        this.setCurrentTable(table);
        TableStat stat = this.getTableStat(table);
        stat.incrementReferencedCount();
        Iterator var4 = x.getReferencedColumns().iterator();

        while(var4.hasNext()) {
            SQLName column = (SQLName)var4.next();
            String columnName = column.getSimpleName();
            this.addColumn(table, columnName);
        }

        return false;
    }

    public boolean visit(SQLDropSequenceStatement x) {
        return false;
    }

    public boolean visit(SQLDropTriggerStatement x) {
        return false;
    }

    public boolean visit(SQLDropUserStatement x) {
        return false;
    }

    public boolean visit(SQLGrantStatement x) {
        if (x.getOn() != null && (x.getObjectType() == null || x.getObjectType() == SQLObjectType.TABLE)) {
            x.getOn().accept(this);
        }

        return false;
    }

    public boolean visit(SQLRevokeStatement x) {
        if (x.getOn() != null) {
            x.getOn().accept(this);
        }

        return false;
    }

    public boolean visit(SQLDropDatabaseStatement x) {
        return false;
    }

    public boolean visit(SQLAlterTableAddIndex x) {
        Iterator var2 = x.getItems().iterator();

        while(var2.hasNext()) {
            SQLSelectOrderByItem item = (SQLSelectOrderByItem)var2.next();
            item.accept(this);
        }

        String table = ((SQLAlterTableStatement)x.getParent()).getName().toString();
        TableStat tableStat = this.getTableStat(table);
        tableStat.incrementCreateIndexCount();
        return false;
    }

    public boolean visit(SQLCheck x) {
        x.getExpr().accept(this);
        return false;
    }

    public boolean visit(SQLCreateTriggerStatement x) {
        return false;
    }

    public boolean visit(SQLDropFunctionStatement x) {
        return false;
    }

    public boolean visit(SQLDropTableSpaceStatement x) {
        return false;
    }

    public boolean visit(SQLDropProcedureStatement x) {
        return false;
    }

    public boolean visit(SQLAlterTableRename x) {
        return false;
    }

    public boolean visit(SQLArrayExpr x) {
        this.accept(x.getValues());
        SQLExpr exp = x.getExpr();
        if (exp instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)exp).getName().equals("ARRAY")) {
            return false;
        } else {
            exp.accept(this);
            return false;
        }
    }

    public boolean visit(SQLOpenStatement x) {
        return false;
    }

    public boolean visit(SQLFetchStatement x) {
        return false;
    }

    public boolean visit(SQLCloseStatement x) {
        return false;
    }

    public boolean visit(SQLCreateProcedureStatement x) {
        String name = x.getName().toString();
        this.variants.put(name, x);
        this.accept((SQLObject)x.getBlock());
        return false;
    }

    public boolean visit(SQLBlockStatement x) {
        Iterator var2 = x.getParameters().iterator();

        while(var2.hasNext()) {
            SQLParameter param = (SQLParameter)var2.next();
            param.setParent(x);
            SQLExpr name = param.getName();
            this.variants.put(name.toString(), name);
        }

        return true;
    }

    public boolean visit(SQLShowTablesStatement x) {
        return false;
    }

    public boolean visit(SQLDeclareItem x) {
        return false;
    }

    public boolean visit(SQLPartitionByHash x) {
        return false;
    }

    public boolean visit(SQLPartitionByRange x) {
        return false;
    }

    public boolean visit(SQLPartitionByList x) {
        return false;
    }

    public boolean visit(SQLPartition x) {
        return false;
    }

    public boolean visit(SQLSubPartition x) {
        return false;
    }

    public boolean visit(SQLSubPartitionByHash x) {
        return false;
    }

    public boolean visit(SQLPartitionValue x) {
        return false;
    }

    public boolean visit(SQLAlterDatabaseStatement x) {
        return true;
    }

    public boolean visit(SQLAlterTableConvertCharSet x) {
        return false;
    }

    public boolean visit(SQLAlterTableDropPartition x) {
        return false;
    }

    public boolean visit(SQLAlterTableReOrganizePartition x) {
        return false;
    }

    public boolean visit(SQLAlterTableCoalescePartition x) {
        return false;
    }

    public boolean visit(SQLAlterTableTruncatePartition x) {
        return false;
    }

    public boolean visit(SQLAlterTableDiscardPartition x) {
        return false;
    }

    public boolean visit(SQLAlterTableImportPartition x) {
        return false;
    }

    public boolean visit(SQLAlterTableAnalyzePartition x) {
        return false;
    }

    public boolean visit(SQLAlterTableCheckPartition x) {
        return false;
    }

    public boolean visit(SQLAlterTableOptimizePartition x) {
        return false;
    }

    public boolean visit(SQLAlterTableRebuildPartition x) {
        return false;
    }

    public boolean visit(SQLAlterTableRepairPartition x) {
        return false;
    }

    public boolean visit(SQLSequenceExpr x) {
        return false;
    }

    public boolean visit(SQLMergeStatement x) {
        this.setAliasMap();
        String originalTable = this.getCurrentTable();
        this.setMode(x.getUsing(), Mode.Select);
        x.getUsing().accept(this);
        this.setMode(x, Mode.Merge);
        String ident = x.getInto().toString();
        this.setCurrentTable(x, ident);
        x.putAttribute("_old_local_", originalTable);
        TableStat stat = this.getTableStat(ident);
        stat.incrementMergeCount();
        Map<String, String> aliasMap = this.getAliasMap();
        if (aliasMap != null) {
            if (x.getAlias() != null) {
                putAliasMap(aliasMap, x.getAlias(), ident);
            }

            putAliasMap(aliasMap, ident, ident);
        }

        x.getOn().accept(this);
        if (x.getUpdateClause() != null) {
            x.getUpdateClause().accept(this);
        }

        if (x.getInsertClause() != null) {
            x.getInsertClause().accept(this);
        }

        return false;
    }

    public boolean visit(SQLSetStatement x) {
        return false;
    }

    public List<SQLMethodInvokeExpr> getFunctions() {
        return this.functions;
    }

    public boolean visit(SQLCreateSequenceStatement x) {
        return false;
    }

    public boolean visit(SQLAlterTableAddConstraint x) {
        SQLConstraint constraint = x.getConstraint();
        if (constraint instanceof SQLUniqueConstraint) {
            SQLAlterTableStatement stmt = (SQLAlterTableStatement)x.getParent();
            String tableName = stmt.getName().toString();
            TableStat tableStat = this.getTableStat(tableName);
            tableStat.incrementCreateIndexCount();
        }

        return true;
    }

    public boolean visit(SQLAlterTableDropIndex x) {
        SQLAlterTableStatement stmt = (SQLAlterTableStatement)x.getParent();
        String tableName = stmt.getName().toString();
        TableStat tableStat = this.getTableStat(tableName);
        tableStat.incrementDropIndexCount();
        return false;
    }

    public boolean visit(SQLAlterTableDropPrimaryKey x) {
        SQLAlterTableStatement stmt = (SQLAlterTableStatement)x.getParent();
        String tableName = stmt.getName().toString();
        TableStat tableStat = this.getTableStat(tableName);
        tableStat.incrementDropIndexCount();
        return false;
    }

    public boolean visit(SQLAlterTableDropKey x) {
        SQLAlterTableStatement stmt = (SQLAlterTableStatement)x.getParent();
        String tableName = stmt.getName().toString();
        TableStat tableStat = this.getTableStat(tableName);
        tableStat.incrementDropIndexCount();
        return false;
    }

    protected class MySqlOrderByStatVisitor extends MySqlASTVisitorAdapter {
        private final SQLOrderBy orderBy;

        public MySqlOrderByStatVisitor(SQLOrderBy orderBy) {
            this.orderBy = orderBy;
            Iterator var3 = orderBy.getItems().iterator();

            while(var3.hasNext()) {
                SQLSelectOrderByItem item = (SQLSelectOrderByItem)var3.next();
                item.getExpr().setParent(item);
            }

        }

        public SQLOrderBy getOrderBy() {
            return this.orderBy;
        }

        public boolean visit(SQLIdentifierExpr x) {
            return SchemaStatVisitor.this.visitOrderBy(x);
        }

        public boolean visit(SQLPropertyExpr x) {
            return SchemaStatVisitor.this.visitOrderBy(x);
        }
    }

    protected class OrderByStatVisitor extends SQLASTVisitorAdapter {
        private final SQLOrderBy orderBy;

        public OrderByStatVisitor(SQLOrderBy orderBy) {
            this.orderBy = orderBy;
            Iterator var3 = orderBy.getItems().iterator();

            while(var3.hasNext()) {
                SQLSelectOrderByItem item = (SQLSelectOrderByItem)var3.next();
                item.getExpr().setParent(item);
            }

        }

        public SQLOrderBy getOrderBy() {
            return this.orderBy;
        }

        public boolean visit(SQLIdentifierExpr x) {
            return SchemaStatVisitor.this.visitOrderBy(x);
        }

        public boolean visit(SQLPropertyExpr x) {
            return SchemaStatVisitor.this.visitOrderBy(x);
        }
    }
}
