package com.tranboot.client.druid.sql;

import com.tranboot.client.druid.DruidRuntimeException;
import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatement;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOperator;
import com.tranboot.client.druid.sql.ast.statement.SQLDeleteStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectItem;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectQuery;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLSetStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateSetItem;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateStatement;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleOutputVisitor;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleToMySqlOutputVisitor;
import com.tranboot.client.druid.sql.parser.Lexer;
import com.tranboot.client.druid.sql.parser.ParserException;
import com.tranboot.client.druid.sql.parser.SQLExprParser;
import com.tranboot.client.druid.sql.parser.SQLParserUtils;
import com.tranboot.client.druid.sql.parser.SQLStatementParser;
import com.tranboot.client.druid.sql.parser.Token;
import com.tranboot.client.druid.sql.visitor.SQLASTOutputVisitor;
import com.tranboot.client.druid.sql.visitor.SchemaStatVisitor;
import com.tranboot.client.druid.util.StringUtils;
import com.tranboot.client.druid.util.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLUtils {
    public static SQLUtils.FormatOption DEFAULT_FORMAT_OPTION = new SQLUtils.FormatOption(true, true);
    public static SQLUtils.FormatOption DEFAULT_LCASE_FORMAT_OPTION = new SQLUtils.FormatOption(false, true);
    private static final Logger LOG = LoggerFactory.getLogger(SQLUtils.class);

    public SQLUtils() {
    }

    public static String toSQLString(SQLObject sqlObject, String dbType) {
        return toSQLString((SQLObject)sqlObject, dbType, (SQLUtils.FormatOption)null);
    }

    public static String toSQLString(SQLObject sqlObject, String dbType, SQLUtils.FormatOption option) {
        StringBuilder out = new StringBuilder();
        SQLASTOutputVisitor visitor = createOutputVisitor(out, dbType);
        if (option == null) {
            option = DEFAULT_FORMAT_OPTION;
        }

        visitor.setUppCase(option.isUppCase());
        visitor.setPrettyFormat(option.isPrettyFormat());
        visitor.setParameterized(option.isParameterized());
        sqlObject.accept(visitor);
        String sql = out.toString();
        return sql;
    }

    public static String toSQLString(SQLObject sqlObject) {
        StringBuilder out = new StringBuilder();
        sqlObject.accept(new SQLASTOutputVisitor(out));
        String sql = out.toString();
        return sql;
    }

    public static String toOdpsString(SQLObject sqlObject) {
        return toOdpsString(sqlObject, (SQLUtils.FormatOption)null);
    }

    public static String toOdpsString(SQLObject sqlObject, SQLUtils.FormatOption option) {
        return toSQLString(sqlObject, "odps", option);
    }

    public static String toMySqlString(SQLObject sqlObject) {
        return toMySqlString(sqlObject, (SQLUtils.FormatOption)null);
    }

    public static String toMySqlString(SQLObject sqlObject, SQLUtils.FormatOption option) {
        return toSQLString(sqlObject, "mysql", option);
    }

    public static SQLExpr toMySqlExpr(String sql) {
        return toSQLExpr(sql, "mysql");
    }

    public static String formatMySql(String sql) {
        return format(sql, "mysql");
    }

    public static String formatMySql(String sql, SQLUtils.FormatOption option) {
        return format(sql, "mysql", option);
    }

    public static String formatOracle(String sql) {
        return format(sql, "oracle");
    }

    public static String formatOracle(String sql, SQLUtils.FormatOption option) {
        return format(sql, "oracle", option);
    }

    public static String formatOdps(String sql) {
        return format(sql, "odps");
    }

    public static String formatOdps(String sql, SQLUtils.FormatOption option) {
        return format(sql, "odps", option);
    }

    public static String formatSQLServer(String sql) {
        return format(sql, "sqlserver");
    }

    public static String toOracleString(SQLObject sqlObject) {
        return toOracleString(sqlObject, (SQLUtils.FormatOption)null);
    }

    public static String toOracleString(SQLObject sqlObject, SQLUtils.FormatOption option) {
        return toSQLString(sqlObject, "oracle", option);
    }

    public static String toPGString(SQLObject sqlObject) {
        return toPGString(sqlObject, (SQLUtils.FormatOption)null);
    }

    public static String toPGString(SQLObject sqlObject, SQLUtils.FormatOption option) {
        return toSQLString(sqlObject, "postgresql", option);
    }

    public static String toDB2String(SQLObject sqlObject) {
        return toDB2String(sqlObject, (SQLUtils.FormatOption)null);
    }

    public static String toDB2String(SQLObject sqlObject, SQLUtils.FormatOption option) {
        return toSQLString(sqlObject, "db2", option);
    }

    public static String toSQLServerString(SQLObject sqlObject) {
        return toSQLServerString(sqlObject, (SQLUtils.FormatOption)null);
    }

    public static String toSQLServerString(SQLObject sqlObject, SQLUtils.FormatOption option) {
        return toSQLString(sqlObject, "sqlserver", option);
    }

    public static String formatPGSql(String sql, SQLUtils.FormatOption option) {
        return format(sql, "postgresql", option);
    }

    public static SQLExpr toSQLExpr(String sql, String dbType) {
        SQLExprParser parser = SQLParserUtils.createExprParser(sql, dbType);
        SQLExpr expr = parser.expr();
        if (parser.getLexer().token() != Token.EOF) {
            throw new ParserException("illegal sql expr : " + sql);
        } else {
            return expr;
        }
    }

    public static SQLSelectOrderByItem toOrderByItem(String sql, String dbType) {
        SQLExprParser parser = SQLParserUtils.createExprParser(sql, dbType);
        SQLSelectOrderByItem orderByItem = parser.parseSelectOrderByItem();
        if (parser.getLexer().token() != Token.EOF) {
            throw new ParserException("illegal sql expr : " + sql);
        } else {
            return orderByItem;
        }
    }

    public static SQLUpdateSetItem toUpdateSetItem(String sql, String dbType) {
        SQLExprParser parser = SQLParserUtils.createExprParser(sql, dbType);
        SQLUpdateSetItem updateSetItem = parser.parseUpdateSetItem();
        if (parser.getLexer().token() != Token.EOF) {
            throw new ParserException("illegal sql expr : " + sql);
        } else {
            return updateSetItem;
        }
    }

    public static SQLSelectItem toSelectItem(String sql, String dbType) {
        SQLExprParser parser = SQLParserUtils.createExprParser(sql, dbType);
        SQLSelectItem selectItem = parser.parseSelectItem();
        if (parser.getLexer().token() != Token.EOF) {
            throw new ParserException("illegal sql expr : " + sql);
        } else {
            return selectItem;
        }
    }

    public static List<SQLStatement> toStatementList(String sql, String dbType) {
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType);
        return parser.parseStatementList();
    }

    public static SQLExpr toSQLExpr(String sql) {
        return toSQLExpr(sql, (String)null);
    }

    public static String format(String sql, String dbType) {
        return format(sql, dbType, (List)null, (SQLUtils.FormatOption)null);
    }

    public static String format(String sql, String dbType, SQLUtils.FormatOption option) {
        return format(sql, dbType, (List)null, option);
    }

    public static String format(String sql, String dbType, List<Object> parameters) {
        return format(sql, dbType, parameters, (SQLUtils.FormatOption)null);
    }

    public static String format(String sql, String dbType, List<Object> parameters, SQLUtils.FormatOption option) {
        try {
            SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType);
            parser.setKeepComments(true);
            List<SQLStatement> statementList = parser.parseStatementList();
            return toSQLString(statementList, dbType, parameters, option);
        } catch (ParserException var6) {
            LOG.warn("format error", var6);
            return sql;
        }
    }

    public static String toSQLString(List<SQLStatement> statementList, String dbType) {
        return toSQLString(statementList, dbType, (List)null);
    }

    public static String toSQLString(List<SQLStatement> statementList, String dbType, SQLUtils.FormatOption option) {
        return toSQLString(statementList, dbType, (List)null, option);
    }

    public static String toSQLString(List<SQLStatement> statementList, String dbType, List<Object> parameters) {
        return toSQLString(statementList, dbType, parameters, (SQLUtils.FormatOption)null, (Map)null);
    }

    public static String toSQLString(List<SQLStatement> statementList, String dbType, List<Object> parameters, SQLUtils.FormatOption option) {
        return toSQLString(statementList, dbType, parameters, option, (Map)null);
    }

    public static String toSQLString(List<SQLStatement> statementList, String dbType, List<Object> parameters, SQLUtils.FormatOption option, Map<String, String> tableMapping) {
        StringBuilder out = new StringBuilder();
        SQLASTOutputVisitor visitor = createFormatOutputVisitor(out, statementList, dbType);
        if (parameters != null) {
            visitor.setParameters(parameters);
        }

        if (option == null) {
            option = DEFAULT_FORMAT_OPTION;
        }

        visitor.setUppCase(option.isUppCase());
        visitor.setPrettyFormat(option.isPrettyFormat());
        visitor.setParameterized(option.isParameterized());
        if (tableMapping != null) {
            visitor.setTableMapping(tableMapping);
        }

        for(int i = 0; i < statementList.size(); ++i) {
            SQLStatement stmt = (SQLStatement)statementList.get(i);
            List comments;
            int j;
            String comment;
            if (i > 0) {
                visitor.print(";");
                SQLStatement preStmt = (SQLStatement)statementList.get(i - 1);
                comments = preStmt.getAfterCommentsDirect();
                if (comments != null) {
                    for(j = 0; j < comments.size(); ++j) {
                        comment = (String)comments.get(j);
                        if (j != 0) {
                            visitor.println();
                        }

                        visitor.print(comment);
                    }
                }

                visitor.println();
                if (!(stmt instanceof SQLSetStatement)) {
                    visitor.println();
                }
            }

            List<String> comments = stmt.getBeforeCommentsDirect();
            if (comments != null) {
                Iterator var15 = comments.iterator();

                while(var15.hasNext()) {
                    String comment = (String)var15.next();
                    visitor.println(comment);
                }
            }

            stmt.accept(visitor);
            if (i == statementList.size() - 1) {
                Boolean semi = (Boolean)stmt.getAttribute("format.semi");
                if (semi != null && semi) {
                    visitor.print(";");
                }

                comments = stmt.getAfterCommentsDirect();
                if (comments != null) {
                    for(j = 0; j < comments.size(); ++j) {
                        comment = (String)comments.get(j);
                        if (j != 0) {
                            visitor.println();
                        }

                        visitor.print(comment);
                    }
                }
            }
        }

        return out.toString();
    }

    public static SQLASTOutputVisitor createOutputVisitor(Appendable out, String dbType) {
        return createFormatOutputVisitor(out, (List)null, dbType);
    }

    public static SQLASTOutputVisitor createFormatOutputVisitor(Appendable out, List<SQLStatement> statementList, String dbType) {
        if (!"oracle".equals(dbType) && !"AliOracle".equals(dbType)) {
            return (SQLASTOutputVisitor)(!"mysql".equals(dbType) && !"mariadb".equals(dbType) && !"h2".equals(dbType) ? new SQLASTOutputVisitor(out, dbType) : new MySqlOutputVisitor(out));
        } else {
            return statementList != null && statementList.size() != 1 ? new OracleOutputVisitor(out, true) : new OracleOutputVisitor(out, false);
        }
    }

    /** @deprecated */
    @Deprecated
    public static SchemaStatVisitor createSchemaStatVisitor(List<SQLStatement> statementList, String dbType) {
        return createSchemaStatVisitor(dbType);
    }

    public static SchemaStatVisitor createSchemaStatVisitor(String dbType) {
        if (!"oracle".equals(dbType) && !"AliOracle".equals(dbType)) {
            return (SchemaStatVisitor)(!"mysql".equals(dbType) && !"mariadb".equals(dbType) && !"h2".equals(dbType) ? new SchemaStatVisitor() : new MySqlSchemaStatVisitor());
        } else {
            return new OracleSchemaStatVisitor();
        }
    }

    public static List<SQLStatement> parseStatements(String sql, String dbType) {
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType);
        List<SQLStatement> stmtList = parser.parseStatementList();
        if (parser.getLexer().token() != Token.EOF) {
            throw new DruidRuntimeException("syntax error : " + sql);
        } else {
            return stmtList;
        }
    }

    public static String buildToDate(String columnName, String tableAlias, String pattern, String dbType) {
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isEmpty(columnName)) {
            return "";
        } else {
            if (StringUtils.isEmpty(dbType)) {
                dbType = "mysql";
            }

            String formatMethod = "";
            if ("mysql".equalsIgnoreCase(dbType)) {
                formatMethod = "STR_TO_DATE";
                if (StringUtils.isEmpty(pattern)) {
                    pattern = "%Y-%m-%d %H:%i:%s";
                }
            } else {
                if (!"oracle".equalsIgnoreCase(dbType)) {
                    return "";
                }

                formatMethod = "TO_DATE";
                if (StringUtils.isEmpty(pattern)) {
                    pattern = "yyyy-mm-dd hh24:mi:ss";
                }
            }

            sql.append(formatMethod).append("(");
            if (!StringUtils.isEmpty(tableAlias)) {
                sql.append(tableAlias).append(".");
            }

            sql.append(columnName).append(",");
            sql.append("'");
            sql.append(pattern);
            sql.append("')");
            return sql.toString();
        }
    }

    public static List<SQLExpr> split(SQLBinaryOpExpr x) {
        List<SQLExpr> groupList = new ArrayList();
        groupList.add(x.getRight());

        SQLExpr left;
        SQLBinaryOpExpr binaryLeft;
        for(left = x.getLeft(); left instanceof SQLBinaryOpExpr && ((SQLBinaryOpExpr)left).getOperator() == x.getOperator(); left = binaryLeft.getLeft()) {
            binaryLeft = (SQLBinaryOpExpr)left;
            groupList.add(binaryLeft.getRight());
        }

        groupList.add(left);
        return groupList;
    }

    public static String translateOracleToMySql(String sql) {
        List<SQLStatement> stmtList = toStatementList(sql, "oracle");
        StringBuilder out = new StringBuilder();
        OracleToMySqlOutputVisitor visitor = new OracleToMySqlOutputVisitor(out, false);

        for(int i = 0; i < stmtList.size(); ++i) {
            ((SQLStatement)stmtList.get(i)).accept(visitor);
        }

        String mysqlSql = out.toString();
        return mysqlSql;
    }

    public static String addCondition(String sql, String condition, String dbType) {
        String result = addCondition(sql, condition, SQLBinaryOperator.BooleanAnd, false, dbType);
        return result;
    }

    public static String addCondition(String sql, String condition, SQLBinaryOperator op, boolean left, String dbType) {
        if (sql == null) {
            throw new IllegalArgumentException("sql is null");
        } else if (condition == null) {
            return sql;
        } else {
            if (op == null) {
                op = SQLBinaryOperator.BooleanAnd;
            }

            if (op != SQLBinaryOperator.BooleanAnd && op != SQLBinaryOperator.BooleanOr) {
                throw new IllegalArgumentException("add condition not support : " + op);
            } else {
                List<SQLStatement> stmtList = parseStatements(sql, dbType);
                if (stmtList.size() == 0) {
                    throw new IllegalArgumentException("not support empty-statement :" + sql);
                } else if (stmtList.size() > 1) {
                    throw new IllegalArgumentException("not support multi-statement :" + sql);
                } else {
                    SQLStatement stmt = (SQLStatement)stmtList.get(0);
                    SQLExpr conditionExpr = toSQLExpr(condition, dbType);
                    addCondition(stmt, op, conditionExpr, left);
                    return toSQLString((SQLObject)stmt, dbType);
                }
            }
        }
    }

    public static void addCondition(SQLStatement stmt, SQLBinaryOperator op, SQLExpr condition, boolean left) {
        if (stmt instanceof SQLSelectStatement) {
            SQLSelectQuery query = ((SQLSelectStatement)stmt).getSelect().getQuery();
            if (query instanceof SQLSelectQueryBlock) {
                SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;
                SQLExpr newCondition = buildCondition(op, condition, left, queryBlock.getWhere());
                queryBlock.setWhere(newCondition);
            } else {
                throw new IllegalArgumentException("add condition not support " + stmt.getClass().getName());
            }
        } else {
            SQLExpr newCondition;
            if (stmt instanceof SQLDeleteStatement) {
                SQLDeleteStatement delete = (SQLDeleteStatement)stmt;
                newCondition = buildCondition(op, condition, left, delete.getWhere());
                delete.setWhere(newCondition);
            } else if (stmt instanceof SQLUpdateStatement) {
                SQLUpdateStatement update = (SQLUpdateStatement)stmt;
                newCondition = buildCondition(op, condition, left, update.getWhere());
                update.setWhere(newCondition);
            } else {
                throw new IllegalArgumentException("add condition not support " + stmt.getClass().getName());
            }
        }
    }

    public static SQLExpr buildCondition(SQLBinaryOperator op, SQLExpr condition, boolean left, SQLExpr where) {
        if (where == null) {
            return condition;
        } else {
            SQLBinaryOpExpr newCondition;
            if (left) {
                newCondition = new SQLBinaryOpExpr(condition, op, where);
            } else {
                newCondition = new SQLBinaryOpExpr(where, op, condition);
            }

            return newCondition;
        }
    }

    public static String addSelectItem(String selectSql, String expr, String alias, String dbType) {
        return addSelectItem(selectSql, expr, alias, false, dbType);
    }

    public static String addSelectItem(String selectSql, String expr, String alias, boolean first, String dbType) {
        List<SQLStatement> stmtList = parseStatements(selectSql, dbType);
        if (stmtList.size() == 0) {
            throw new IllegalArgumentException("not support empty-statement :" + selectSql);
        } else if (stmtList.size() > 1) {
            throw new IllegalArgumentException("not support multi-statement :" + selectSql);
        } else {
            SQLStatement stmt = (SQLStatement)stmtList.get(0);
            SQLExpr columnExpr = toSQLExpr(expr, dbType);
            addSelectItem(stmt, columnExpr, alias, first);
            return toSQLString((SQLObject)stmt, dbType);
        }
    }

    public static void addSelectItem(SQLStatement stmt, SQLExpr expr, String alias, boolean first) {
        if (expr != null) {
            if (stmt instanceof SQLSelectStatement) {
                SQLSelectQuery query = ((SQLSelectStatement)stmt).getSelect().getQuery();
                if (query instanceof SQLSelectQueryBlock) {
                    SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;
                    addSelectItem(queryBlock, expr, alias, first);
                } else {
                    throw new IllegalArgumentException("add condition not support " + stmt.getClass().getName());
                }
            } else {
                throw new IllegalArgumentException("add selectItem not support " + stmt.getClass().getName());
            }
        }
    }

    public static void addSelectItem(SQLSelectQueryBlock queryBlock, SQLExpr expr, String alias, boolean first) {
        SQLSelectItem selectItem = new SQLSelectItem(expr, alias);
        queryBlock.getSelectList().add(selectItem);
        selectItem.setParent(selectItem);
    }

    public static String refactor(String sql, String dbType, Map<String, String> tableMapping) {
        List<SQLStatement> stmtList = parseStatements(sql, dbType);
        return toSQLString(stmtList, dbType, (List)null, (SQLUtils.FormatOption)null, tableMapping);
    }

    public static boolean containsIndexDDL(String sql, String dbType) {
        parseStatements(sql, dbType);
        return false;
    }

    public static long hash(String sql, String dbType) {
        Lexer lexer = SQLParserUtils.createLexer(sql, dbType);
        StringBuilder buf = new StringBuilder(sql.length());

        while(true) {
            lexer.nextToken();
            Token token = lexer.token();
            if (token == Token.EOF) {
                return (long)buf.hashCode();
            }

            if (token == Token.ERROR) {
                return Utils.murmurhash2_64(sql);
            }

            if (buf.length() != 0) {
            }
        }
    }

    public static class FormatOption {
        private boolean ucase;
        private boolean prettyFormat;
        private boolean parameterized;

        public FormatOption() {
            this.ucase = true;
            this.prettyFormat = true;
            this.parameterized = false;
        }

        public FormatOption(boolean ucase) {
            this(ucase, true);
        }

        public FormatOption(boolean ucase, boolean prettyFormat) {
            this(ucase, prettyFormat, false);
        }

        public FormatOption(boolean ucase, boolean prettyFormat, boolean parameterized) {
            this.ucase = true;
            this.prettyFormat = true;
            this.parameterized = false;
            this.ucase = ucase;
            this.prettyFormat = prettyFormat;
            this.parameterized = parameterized;
        }

        public boolean isUppCase() {
            return this.ucase;
        }

        public void setUppCase(boolean val) {
            this.ucase = val;
        }

        public boolean isPrettyFormat() {
            return this.prettyFormat;
        }

        public void setPrettyFormat(boolean prettyFormat) {
            this.prettyFormat = prettyFormat;
        }

        public boolean isParameterized() {
            return this.parameterized;
        }

        public void setParameterized(boolean parameterized) {
            this.parameterized = parameterized;
        }
    }
}

