//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tranboot.client.druid.sql.dialect.mysql.parser;

import com.tranboot.client.druid.sql.ast.SQLCommentHint;
import com.tranboot.client.druid.sql.ast.SQLDataTypeImpl;
import com.tranboot.client.druid.sql.ast.SQLDeclareItem;
import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLOrderBy;
import com.tranboot.client.druid.sql.ast.SQLParameter;
import com.tranboot.client.druid.sql.ast.SQLPartition;
import com.tranboot.client.druid.sql.ast.SQLStatement;
import com.tranboot.client.druid.sql.ast.SQLParameter.ParameterType;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOperator;
import com.tranboot.client.druid.sql.ast.expr.SQLCharExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLIntegerExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLLiteralExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNCharExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLQueryExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLVariantRefExpr;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterDatabaseStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableAddColumn;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableAddConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableAddIndex;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableAddPartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableAnalyzePartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableCheckPartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableCoalescePartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableConvertCharSet;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDisableConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDisableKeys;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDiscardPartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDropColumnItem;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDropConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDropForeignKey;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDropIndex;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDropKey;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDropPartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableDropPrimaryKey;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableEnableConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableEnableKeys;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableImportPartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableOptimizePartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableReOrganizePartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableRebuildPartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableRepairPartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableTruncatePartition;
import com.tranboot.client.druid.sql.ast.statement.SQLAssignItem;
import com.tranboot.client.druid.sql.ast.statement.SQLBlockStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnDefinition;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateDatabaseStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateProcedureStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateTableStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLExprTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLIfStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLInsertStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLLoopStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLPrimaryKey;
import com.tranboot.client.druid.sql.ast.statement.SQLSelect;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLSetStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLShowTablesStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLStartTransactionStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLUnique;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateSetItem;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLIfStatement.Else;
import com.tranboot.client.druid.sql.ast.statement.SQLIfStatement.ElseIf;
import com.tranboot.client.druid.sql.ast.statement.SQLInsertStatement.ValuesClause;
import com.tranboot.client.druid.sql.dialect.mysql.ast.MysqlForeignKey;
import com.tranboot.client.druid.sql.dialect.mysql.ast.clause.ConditionValue;
import com.tranboot.client.druid.sql.dialect.mysql.ast.clause.MySqlCaseStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.clause.MySqlCursorDeclareStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.clause.MySqlDeclareConditionStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.clause.MySqlDeclareHandlerStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.clause.MySqlDeclareStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.clause.MySqlHandlerType;
import com.tranboot.client.druid.sql.dialect.mysql.ast.clause.MySqlIterateStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.clause.MySqlLeaveStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.clause.MySqlRepeatStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.clause.MySqlSelectIntoStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.clause.MySqlWhileStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.clause.ConditionValue.ConditionType;
import com.tranboot.client.druid.sql.dialect.mysql.ast.clause.MySqlCaseStatement.MySqlWhenStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.CobarShowStatus;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableAlterColumn;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableChangeColumn;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableCharacter;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableDiscardTablespace;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableImportTablespace;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableModifyColumn;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableOption;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlAlterUserStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlAnalyzeStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlBinlogStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlCommitStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlCreateUserStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlDescribeStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlExecuteStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlHelpStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlHintStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlKillStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlLoadDataInFileStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlLoadXmlStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlLockTableStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlOptimizeStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlPrepareStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlRenameTableStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlReplaceStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlResetStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlRollbackStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlSetCharSetStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlSetNamesStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlSetPasswordStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlSetTransactionStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowAuthorsStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowBinLogEventsStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowBinaryLogsStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowCharacterSetStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowCollationStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowColumnsStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowContributorsStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateDatabaseStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateEventStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateFunctionStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateProcedureStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateTableStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateTriggerStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateViewStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowDatabasesStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowEngineStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowEnginesStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowErrorsStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowEventsStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowFunctionCodeStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowFunctionStatusStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowGrantsStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowIndexesStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowKeysStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowMasterLogsStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowMasterStatusStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowOpenTablesStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowPluginsStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowPrivilegesStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowProcedureCodeStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowProcedureStatusStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowProcessListStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowProfileStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowProfilesStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowRelayLogEventsStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowSlaveHostsStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowSlaveStatusStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowStatusStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowTableStatusStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowTriggersStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowVariantsStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowWarningsStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlUnlockTablesStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MysqlDeallocatePrepareStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlCreateUserStatement.UserSpecification;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlKillStatement.Type;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlLockTableStatement.LockType;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlRenameTableStatement.Item;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowEngineStatement.Option;
import com.tranboot.client.druid.sql.parser.Lexer;
import com.tranboot.client.druid.sql.parser.ParserException;
import com.tranboot.client.druid.sql.parser.SQLSelectParser;
import com.tranboot.client.druid.sql.parser.SQLStatementParser;
import com.tranboot.client.druid.sql.parser.Token;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MySqlStatementParser extends SQLStatementParser {
    private static final String AUTO_INCREMENT = "AUTO_INCREMENT";
    private static final String COLLATE2 = "COLLATE";
    private static final String CHAIN = "CHAIN";
    private static final String ENGINES = "ENGINES";
    private static final String ENGINE = "ENGINE";
    private static final String BINLOG = "BINLOG";
    private static final String EVENTS = "EVENTS";
    private static final String CHARACTER = "CHARACTER";
    private static final String SESSION = "SESSION";
    private static final String GLOBAL = "GLOBAL";
    private static final String VARIABLES = "VARIABLES";
    private static final String ERRORS = "ERRORS";
    private static final String STATUS = "STATUS";
    private static final String IGNORE = "IGNORE";
    private static final String RESET = "RESET";
    private static final String DESCRIBE = "DESCRIBE";
    private static final String WRITE = "WRITE";
    private static final String READ = "READ";
    private static final String LOCAL = "LOCAL";
    private static final String TABLES = "TABLES";
    private static final String TEMPORARY = "TEMPORARY";
    private static final String SPATIAL = "SPATIAL";
    private static final String FULLTEXT = "FULLTEXT";
    private static final String DELAYED = "DELAYED";
    private static final String LOW_PRIORITY = "LOW_PRIORITY";
    private int maxIntoClause = -1;

    public MySqlStatementParser(String sql) {
        super(new MySqlExprParser(sql));
    }

    public MySqlStatementParser(Lexer lexer) {
        super(new MySqlExprParser(lexer));
    }

    public int getMaxIntoClause() {
        return this.maxIntoClause;
    }

    public void setMaxIntoClause(int maxIntoClause) {
        this.maxIntoClause = maxIntoClause;
    }

    public SQLCreateTableStatement parseCreateTable() {
        MySqlCreateTableParser parser = new MySqlCreateTableParser(this.exprParser);
        return parser.parseCrateTable();
    }

    public SQLStatement parseSelect() {
        MySqlSelectParser selectParser = new MySqlSelectParser(this.exprParser);
        SQLSelect select = selectParser.select();
        return (SQLStatement)(selectParser.returningFlag ? selectParser.updateStmt : new SQLSelectStatement(select, "mysql"));
    }

    public SQLUpdateStatement parseUpdateStatement() {
        return (new MySqlSelectParser(this.exprParser)).parseUpdateStatment();
    }

    protected MySqlUpdateStatement createUpdateStatement() {
        return new MySqlUpdateStatement();
    }

    public MySqlDeleteStatement parseDeleteStatement() {
        MySqlDeleteStatement deleteStatement = new MySqlDeleteStatement();
        if (this.lexer.token() == Token.DELETE) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.COMMENT) {
                this.lexer.nextToken();
            }

            if (this.lexer.token() == Token.HINT) {
                this.getExprParser().parseHints(deleteStatement.getHints());
            }

            if (this.identifierEquals("LOW_PRIORITY")) {
                deleteStatement.setLowPriority(true);
                this.lexer.nextToken();
            }

            if (this.identifierEquals("QUICK")) {
                deleteStatement.setQuick(true);
                this.lexer.nextToken();
            }

            if (this.identifierEquals("IGNORE")) {
                deleteStatement.setIgnore(true);
                this.lexer.nextToken();
            }

            SQLTableSource tableSource;
            if (this.lexer.token() == Token.IDENTIFIER) {
                deleteStatement.setTableSource(this.createSQLSelectParser().parseTableSource());
                if (this.lexer.token() == Token.FROM) {
                    this.lexer.nextToken();
                    tableSource = this.createSQLSelectParser().parseTableSource();
                    deleteStatement.setFrom(tableSource);
                }
            } else {
                if (this.lexer.token() != Token.FROM) {
                    throw new ParserException("syntax error");
                }

                this.lexer.nextToken();
                deleteStatement.setTableSource(this.createSQLSelectParser().parseTableSource());
            }

            if (this.identifierEquals("USING")) {
                this.lexer.nextToken();
                tableSource = this.createSQLSelectParser().parseTableSource();
                deleteStatement.setUsing(tableSource);
            }
        }

        if (this.lexer.token() == Token.WHERE) {
            this.lexer.nextToken();
            SQLExpr where = this.exprParser.expr();
            deleteStatement.setWhere(where);
        }

        if (this.lexer.token() == Token.ORDER) {
            SQLOrderBy orderBy = this.exprParser.parseOrderBy();
            deleteStatement.setOrderBy(orderBy);
        }

        deleteStatement.setLimit(this.exprParser.parseLimit());
        return deleteStatement;
    }

    public SQLStatement parseCreate() {
        char markChar = this.lexer.current();
        int markBp = this.lexer.bp();
        this.accept(Token.CREATE);
        boolean replace = false;
        if (this.lexer.token() == Token.OR) {
            this.lexer.nextToken();
            this.accept(Token.REPLACE);
            replace = true;
        }

        List<SQLCommentHint> hints = this.exprParser.parseHints();
        if (this.lexer.token() != Token.TABLE && !this.identifierEquals("TEMPORARY")) {
            if (this.lexer.token() == Token.DATABASE) {
                if (replace) {
                    this.lexer.reset(markBp, markChar, Token.CREATE);
                }

                return this.parseCreateDatabase();
            } else if (this.lexer.token() != Token.UNIQUE && this.lexer.token() != Token.INDEX && !this.identifierEquals("FULLTEXT") && !this.identifierEquals("SPATIAL")) {
                if (this.lexer.token() == Token.USER) {
                    if (replace) {
                        this.lexer.reset(markBp, markChar, Token.CREATE);
                    }

                    return this.parseCreateUser();
                } else if (this.lexer.token() != Token.VIEW && !this.identifierEquals("ALGORITHM")) {
                    if (this.lexer.token() == Token.TRIGGER) {
                        if (replace) {
                            this.lexer.reset(markBp, markChar, Token.CREATE);
                        }

                        return this.parseCreateTrigger();
                    } else if (this.lexer.token() != Token.PROCEDURE && !this.identifierEquals("DEFINER")) {
                        throw new ParserException("TODO " + this.lexer.info());
                    } else {
                        if (replace) {
                            this.lexer.reset(markBp, markChar, Token.CREATE);
                        }

                        return this.parseCreateProcedure();
                    }
                } else {
                    if (replace) {
                        this.lexer.reset(markBp, markChar, Token.CREATE);
                    }

                    return this.parseCreateView();
                }
            } else {
                if (replace) {
                    this.lexer.reset(markBp, markChar, Token.CREATE);
                }

                return this.parseCreateIndex(false);
            }
        } else {
            if (replace) {
                this.lexer.reset(markBp, markChar, Token.CREATE);
            }

            MySqlCreateTableParser parser = new MySqlCreateTableParser(this.exprParser);
            MySqlCreateTableStatement stmt = parser.parseCrateTable(false);
            stmt.setHints(hints);
            return stmt;
        }
    }

    public SQLStatement parseCreateIndex(boolean acceptCreate) {
        if (acceptCreate) {
            this.accept(Token.CREATE);
        }

        SQLCreateIndexStatement stmt = new SQLCreateIndexStatement();
        if (this.lexer.token() == Token.UNIQUE) {
            stmt.setType("UNIQUE");
            this.lexer.nextToken();
        } else if (this.identifierEquals("FULLTEXT")) {
            stmt.setType("FULLTEXT");
            this.lexer.nextToken();
        } else if (this.identifierEquals("SPATIAL")) {
            stmt.setType("SPATIAL");
            this.lexer.nextToken();
        }

        this.accept(Token.INDEX);
        stmt.setName(this.exprParser.name());
        this.parseCreateIndexUsing(stmt);
        this.accept(Token.ON);
        stmt.setTable(this.exprParser.name());
        this.accept(Token.LPAREN);

        while(true) {
            SQLSelectOrderByItem item = this.exprParser.parseSelectOrderByItem();
            stmt.addItem(item);
            if (this.lexer.token() != Token.COMMA) {
                this.accept(Token.RPAREN);
                this.parseCreateIndexUsing(stmt);
                return stmt;
            }

            this.lexer.nextToken();
        }
    }

    private void parseCreateIndexUsing(SQLCreateIndexStatement stmt) {
        if (this.identifierEquals("USING")) {
            this.lexer.nextToken();
            if (this.identifierEquals("BTREE")) {
                stmt.setUsing("BTREE");
                this.lexer.nextToken();
            } else {
                if (!this.identifierEquals("HASH")) {
                    throw new ParserException("TODO " + this.lexer.token() + " " + this.lexer.stringVal());
                }

                stmt.setUsing("HASH");
                this.lexer.nextToken();
            }
        }

    }

    public SQLStatement parseCreateUser() {
        if (this.lexer.token() == Token.CREATE) {
            this.lexer.nextToken();
        }

        this.accept(Token.USER);
        MySqlCreateUserStatement stmt = new MySqlCreateUserStatement();

        while(true) {
            UserSpecification userSpec = new UserSpecification();
            SQLExpr expr = this.exprParser.primary();
            userSpec.setUser(expr);
            if (this.lexer.token() == Token.IDENTIFIED) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.BY) {
                    this.lexer.nextToken();
                    if (this.identifierEquals("PASSWORD")) {
                        this.lexer.nextToken();
                        userSpec.setPasswordHash(true);
                    }

                    SQLCharExpr password = (SQLCharExpr)this.exprParser.expr();
                    userSpec.setPassword(password);
                } else if (this.lexer.token() == Token.WITH) {
                    this.lexer.nextToken();
                    userSpec.setAuthPlugin(this.exprParser.expr());
                }
            }

            stmt.addUser(userSpec);
            if (this.lexer.token() != Token.COMMA) {
                return stmt;
            }

            this.lexer.nextToken();
        }
    }

    public SQLStatement parseKill() {
        this.accept(Token.KILL);
        MySqlKillStatement stmt = new MySqlKillStatement();
        if (this.identifierEquals("CONNECTION")) {
            stmt.setType(Type.CONNECTION);
            this.lexer.nextToken();
        } else if (this.identifierEquals("QUERY")) {
            stmt.setType(Type.QUERY);
            this.lexer.nextToken();
        } else if (this.lexer.token() != Token.LITERAL_INT) {
            throw new ParserException("not support kill type " + this.lexer.token());
        }

        this.exprParser.exprList(stmt.getThreadIds(), stmt);
        return stmt;
    }

    public SQLStatement parseBinlog() {
        this.acceptIdentifier("binlog");
        MySqlBinlogStatement stmt = new MySqlBinlogStatement();
        SQLExpr expr = this.exprParser.expr();
        stmt.setExpr(expr);
        return stmt;
    }

    public MySqlAnalyzeStatement parseAnalyze() {
        this.accept(Token.ANALYZE);
        this.accept(Token.TABLE);
        MySqlAnalyzeStatement stmt = new MySqlAnalyzeStatement();
        List<SQLName> names = new ArrayList();
        this.exprParser.names(names, stmt);
        Iterator var3 = names.iterator();

        while(var3.hasNext()) {
            SQLName name = (SQLName)var3.next();
            stmt.addTableSource(new SQLExprTableSource(name));
        }

        return stmt;
    }

    public MySqlOptimizeStatement parseOptimize() {
        this.accept(Token.OPTIMIZE);
        this.accept(Token.TABLE);
        MySqlOptimizeStatement stmt = new MySqlOptimizeStatement();
        List<SQLName> names = new ArrayList();
        this.exprParser.names(names, stmt);
        Iterator var3 = names.iterator();

        while(var3.hasNext()) {
            SQLName name = (SQLName)var3.next();
            stmt.addTableSource(new SQLExprTableSource(name));
        }

        return stmt;
    }

    public SQLStatement parseReset() {
        this.acceptIdentifier("RESET");
        MySqlResetStatement stmt = new MySqlResetStatement();

        while(this.lexer.token() == Token.IDENTIFIER) {
            if (this.identifierEquals("QUERY")) {
                this.lexer.nextToken();
                this.accept(Token.CACHE);
                stmt.getOptions().add("QUERY CACHE");
            } else {
                stmt.getOptions().add(this.lexer.stringVal());
                this.lexer.nextToken();
            }

            if (this.lexer.token() != Token.COMMA) {
                break;
            }

            this.lexer.nextToken();
        }

        return stmt;
    }

    public boolean parseStatementListDialect(List<SQLStatement> statementList) {
        SQLStatement stmt;
        if (this.lexer.token() == Token.KILL) {
            stmt = this.parseKill();
            statementList.add(stmt);
            return true;
        } else if (this.identifierEquals("PREPARE")) {
            MySqlPrepareStatement stmt = this.parsePrepare();
            statementList.add(stmt);
            return true;
        } else if (this.identifierEquals("EXECUTE")) {
            MySqlExecuteStatement stmt = this.parseExecute();
            statementList.add(stmt);
            return true;
        } else if (this.identifierEquals("DEALLOCATE")) {
            MysqlDeallocatePrepareStatement stmt = this.parseDeallocatePrepare();
            statementList.add(stmt);
            return true;
        } else if (this.identifierEquals("LOAD")) {
            stmt = this.parseLoad();
            statementList.add(stmt);
            return true;
        } else if (this.lexer.token() == Token.REPLACE) {
            MySqlReplaceStatement stmt = this.parseReplicate();
            statementList.add(stmt);
            return true;
        } else if (this.identifierEquals("START")) {
            SQLStartTransactionStatement stmt = this.parseStart();
            statementList.add(stmt);
            return true;
        } else if (this.lexer.token() == Token.SHOW) {
            stmt = this.parseShow();
            statementList.add(stmt);
            return true;
        } else if (this.identifierEquals("BINLOG")) {
            stmt = this.parseBinlog();
            statementList.add(stmt);
            return true;
        } else if (this.identifierEquals("RESET")) {
            stmt = this.parseReset();
            statementList.add(stmt);
            return true;
        } else if (this.lexer.token() == Token.ANALYZE) {
            SQLStatement stmt = this.parseAnalyze();
            statementList.add(stmt);
            return true;
        } else if (this.lexer.token() == Token.OPTIMIZE) {
            SQLStatement stmt = this.parseOptimize();
            statementList.add(stmt);
            return true;
        } else if (this.identifierEquals("HELP")) {
            this.lexer.nextToken();
            MySqlHelpStatement stmt = new MySqlHelpStatement();
            stmt.setContent(this.exprParser.primary());
            statementList.add(stmt);
            return true;
        } else if (this.lexer.token() != Token.DESC && !this.identifierEquals("DESCRIBE")) {
            boolean tddlSelectHints;
            boolean isUnLockTable;
            String val;
            if (this.lexer.token() == Token.LOCK) {
                this.lexer.nextToken();
                val = this.lexer.stringVal();
                tddlSelectHints = "TABLES".equalsIgnoreCase(val) && this.lexer.token() == Token.IDENTIFIER;
                isUnLockTable = "TABLE".equalsIgnoreCase(val) && this.lexer.token() == Token.TABLE;
                if (!tddlSelectHints && !isUnLockTable) {
                    this.setErrorEndPos(this.lexer.pos());
                    throw new ParserException("syntax error, expect TABLES or TABLE, actual " + this.lexer.token());
                } else {
                    this.lexer.nextToken();
                    MySqlLockTableStatement stmt = new MySqlLockTableStatement();
                    stmt.setTableSource(this.exprParser.name());
                    if (this.identifierEquals("READ")) {
                        this.lexer.nextToken();
                        if (this.identifierEquals("LOCAL")) {
                            this.lexer.nextToken();
                            stmt.setLockType(LockType.READ_LOCAL);
                        } else {
                            stmt.setLockType(LockType.READ);
                        }
                    } else if (this.identifierEquals("WRITE")) {
                        stmt.setLockType(LockType.WRITE);
                    } else {
                        if (!this.identifierEquals("LOW_PRIORITY")) {
                            throw new ParserException("syntax error, expect READ or WRITE, actual " + this.lexer.token());
                        }

                        this.lexer.nextToken();
                        this.acceptIdentifier("WRITE");
                        stmt.setLockType(LockType.LOW_PRIORITY_WRITE);
                    }

                    if (this.lexer.token() == Token.HINT) {
                        stmt.setHints(this.exprParser.parseHints());
                    }

                    statementList.add(stmt);
                    return true;
                }
            } else if (!this.identifierEquals("UNLOCK")) {
                if (this.lexer.token() == Token.HINT) {
                    List<SQLCommentHint> hints = this.exprParser.parseHints();
                    tddlSelectHints = false;
                    if (hints.size() == 1 && statementList.size() == 0 && this.lexer.token() == Token.SELECT) {
                        SQLCommentHint hint = (SQLCommentHint)hints.get(0);
                        String hintText = hint.getText();
                        if (hintText.startsWith("+TDDL")) {
                            tddlSelectHints = true;
                        }
                    }

                    if (tddlSelectHints) {
                        SQLSelectStatement stmt = (SQLSelectStatement)this.parseStatement();
                        stmt.setHeadHints(hints);
                        statementList.add(stmt);
                        return true;
                    } else {
                        MySqlHintStatement stmt = new MySqlHintStatement();
                        stmt.setHints(hints);
                        statementList.add(stmt);
                        return true;
                    }
                } else if (this.lexer.token() == Token.BEGIN) {
                    statementList.add(this.parseBlock());
                    return true;
                } else {
                    return false;
                }
            } else {
                this.lexer.nextToken();
                val = this.lexer.stringVal();
                tddlSelectHints = "TABLES".equalsIgnoreCase(val) && this.lexer.token() == Token.IDENTIFIER;
                isUnLockTable = "TABLE".equalsIgnoreCase(val) && this.lexer.token() == Token.TABLE;
                statementList.add(new MySqlUnlockTablesStatement());
                if (!tddlSelectHints && !isUnLockTable) {
                    this.setErrorEndPos(this.lexer.pos());
                    throw new ParserException("syntax error, expect TABLES or TABLE, actual " + this.lexer.token());
                } else {
                    this.lexer.nextToken();
                    return true;
                }
            }
        } else {
            SQLStatement stmt = this.parseDescribe();
            statementList.add(stmt);
            return true;
        }
    }

    public SQLBlockStatement parseBlock() {
        SQLBlockStatement block = new SQLBlockStatement();
        this.accept(Token.BEGIN);
        this.parseProcedureStatementList(block.getStatementList());
        this.accept(Token.END);
        return block;
    }

    public MySqlDescribeStatement parseDescribe() {
        if (this.lexer.token() != Token.DESC && !this.identifierEquals("DESCRIBE")) {
            throw new ParserException("expect DESC, actual " + this.lexer.token());
        } else {
            this.lexer.nextToken();
            MySqlDescribeStatement stmt = new MySqlDescribeStatement();
            stmt.setObject(this.exprParser.name());
            if (this.lexer.token() == Token.IDENTIFIER) {
                stmt.setColName(this.exprParser.name());
            }

            return stmt;
        }
    }

    public SQLStatement parseShow() {
        this.accept(Token.SHOW);
        if (this.lexer.token() == Token.COMMENT) {
            this.lexer.nextToken();
        }

        boolean full = false;
        if (this.lexer.token() == Token.FULL) {
            this.lexer.nextToken();
            full = true;
        }

        MySqlShowProcessListStatement stmt;
        if (this.identifierEquals("PROCESSLIST")) {
            this.lexer.nextToken();
            stmt = new MySqlShowProcessListStatement();
            stmt.setFull(full);
            return stmt;
        } else {
            MySqlShowColumnsStatement stmt;
            if (!this.identifierEquals("COLUMNS") && !this.identifierEquals("FIELDS")) {
                if (this.identifierEquals("COLUMNS")) {
                    this.lexer.nextToken();
                    stmt = this.parseShowColumns();
                    return stmt;
                } else if (this.identifierEquals("TABLES")) {
                    this.lexer.nextToken();
                    SQLShowTablesStatement stmt = this.parseShowTabless();
                    stmt.setFull(full);
                    return stmt;
                } else if (this.identifierEquals("DATABASES")) {
                    this.lexer.nextToken();
                    MySqlShowDatabasesStatement stmt = this.parseShowDatabases();
                    return stmt;
                } else {
                    MySqlShowWarningsStatement stmt;
                    if (this.identifierEquals("WARNINGS")) {
                        this.lexer.nextToken();
                        stmt = this.parseShowWarnings();
                        return stmt;
                    } else {
                        MySqlShowErrorsStatement stmt;
                        if (this.identifierEquals("COUNT")) {
                            this.lexer.nextToken();
                            this.accept(Token.LPAREN);
                            this.accept(Token.STAR);
                            this.accept(Token.RPAREN);
                            if (this.identifierEquals("ERRORS")) {
                                this.lexer.nextToken();
                                stmt = new MySqlShowErrorsStatement();
                                stmt.setCount(true);
                                return stmt;
                            } else {
                                this.acceptIdentifier("WARNINGS");
                                stmt = new MySqlShowWarningsStatement();
                                stmt.setCount(true);
                                return stmt;
                            }
                        } else if (this.identifierEquals("ERRORS")) {
                            this.lexer.nextToken();
                            stmt = new MySqlShowErrorsStatement();
                            stmt.setLimit(this.exprParser.parseLimit());
                            return stmt;
                        } else {
                            MySqlShowStatusStatement stmt;
                            if (this.identifierEquals("STATUS")) {
                                this.lexer.nextToken();
                                stmt = this.parseShowStatus();
                                return stmt;
                            } else {
                                MySqlShowVariantsStatement stmt;
                                if (this.identifierEquals("VARIABLES")) {
                                    this.lexer.nextToken();
                                    stmt = this.parseShowVariants();
                                    return stmt;
                                } else {
                                    if (this.identifierEquals("GLOBAL")) {
                                        this.lexer.nextToken();
                                        if (this.identifierEquals("STATUS")) {
                                            this.lexer.nextToken();
                                            stmt = this.parseShowStatus();
                                            stmt.setGlobal(true);
                                            return stmt;
                                        }

                                        if (this.identifierEquals("VARIABLES")) {
                                            this.lexer.nextToken();
                                            stmt = this.parseShowVariants();
                                            stmt.setGlobal(true);
                                            return stmt;
                                        }
                                    }

                                    if (this.identifierEquals("SESSION")) {
                                        this.lexer.nextToken();
                                        if (this.identifierEquals("STATUS")) {
                                            this.lexer.nextToken();
                                            stmt = this.parseShowStatus();
                                            stmt.setSession(true);
                                            return stmt;
                                        }

                                        if (this.identifierEquals("VARIABLES")) {
                                            this.lexer.nextToken();
                                            stmt = this.parseShowVariants();
                                            stmt.setSession(true);
                                            return stmt;
                                        }
                                    }

                                    if (this.identifierEquals("COBAR_STATUS")) {
                                        this.lexer.nextToken();
                                        return new CobarShowStatus();
                                    } else if (this.identifierEquals("AUTHORS")) {
                                        this.lexer.nextToken();
                                        return new MySqlShowAuthorsStatement();
                                    } else if (this.lexer.token() == Token.BINARY) {
                                        this.lexer.nextToken();
                                        this.acceptIdentifier("LOGS");
                                        return new MySqlShowBinaryLogsStatement();
                                    } else if (this.identifierEquals("MASTER")) {
                                        this.lexer.nextToken();
                                        if (this.identifierEquals("LOGS")) {
                                            this.lexer.nextToken();
                                            return new MySqlShowMasterLogsStatement();
                                        } else {
                                            this.acceptIdentifier("STATUS");
                                            return new MySqlShowMasterStatusStatement();
                                        }
                                    } else if (this.identifierEquals("CHARACTER")) {
                                        this.lexer.nextToken();
                                        this.accept(Token.SET);
                                        MySqlShowCharacterSetStatement stmt = new MySqlShowCharacterSetStatement();
                                        if (this.lexer.token() == Token.LIKE) {
                                            this.lexer.nextToken();
                                            stmt.setPattern(this.exprParser.expr());
                                        }

                                        if (this.lexer.token() == Token.WHERE) {
                                            this.lexer.nextToken();
                                            stmt.setWhere(this.exprParser.expr());
                                        }

                                        return stmt;
                                    } else if (this.identifierEquals("COLLATION")) {
                                        this.lexer.nextToken();
                                        MySqlShowCollationStatement stmt = new MySqlShowCollationStatement();
                                        if (this.lexer.token() == Token.LIKE) {
                                            this.lexer.nextToken();
                                            stmt.setPattern(this.exprParser.expr());
                                        }

                                        if (this.lexer.token() == Token.WHERE) {
                                            this.lexer.nextToken();
                                            stmt.setWhere(this.exprParser.expr());
                                        }

                                        return stmt;
                                    } else if (this.identifierEquals("BINLOG")) {
                                        this.lexer.nextToken();
                                        this.acceptIdentifier("EVENTS");
                                        MySqlShowBinLogEventsStatement stmt = new MySqlShowBinLogEventsStatement();
                                        if (this.lexer.token() == Token.IN) {
                                            this.lexer.nextToken();
                                            stmt.setIn(this.exprParser.expr());
                                        }

                                        if (this.lexer.token() == Token.FROM) {
                                            this.lexer.nextToken();
                                            stmt.setFrom(this.exprParser.expr());
                                        }

                                        stmt.setLimit(this.exprParser.parseLimit());
                                        return stmt;
                                    } else if (this.identifierEquals("CONTRIBUTORS")) {
                                        this.lexer.nextToken();
                                        return new MySqlShowContributorsStatement();
                                    } else if (this.lexer.token() == Token.CREATE) {
                                        this.lexer.nextToken();
                                        if (this.lexer.token() == Token.DATABASE) {
                                            this.lexer.nextToken();
                                            MySqlShowCreateDatabaseStatement stmt = new MySqlShowCreateDatabaseStatement();
                                            stmt.setDatabase(this.exprParser.name());
                                            return stmt;
                                        } else if (this.identifierEquals("EVENT")) {
                                            this.lexer.nextToken();
                                            MySqlShowCreateEventStatement stmt = new MySqlShowCreateEventStatement();
                                            stmt.setEventName(this.exprParser.name());
                                            return stmt;
                                        } else if (this.lexer.token() == Token.FUNCTION) {
                                            this.lexer.nextToken();
                                            MySqlShowCreateFunctionStatement stmt = new MySqlShowCreateFunctionStatement();
                                            stmt.setName(this.exprParser.name());
                                            return stmt;
                                        } else if (this.lexer.token() == Token.PROCEDURE) {
                                            this.lexer.nextToken();
                                            MySqlShowCreateProcedureStatement stmt = new MySqlShowCreateProcedureStatement();
                                            stmt.setName(this.exprParser.name());
                                            return stmt;
                                        } else if (this.lexer.token() == Token.TABLE) {
                                            this.lexer.nextToken();
                                            MySqlShowCreateTableStatement stmt = new MySqlShowCreateTableStatement();
                                            stmt.setName(this.exprParser.name());
                                            return stmt;
                                        } else if (this.lexer.token() == Token.VIEW) {
                                            this.lexer.nextToken();
                                            MySqlShowCreateViewStatement stmt = new MySqlShowCreateViewStatement();
                                            stmt.setName(this.exprParser.name());
                                            return stmt;
                                        } else if (this.lexer.token() == Token.TRIGGER) {
                                            this.lexer.nextToken();
                                            MySqlShowCreateTriggerStatement stmt = new MySqlShowCreateTriggerStatement();
                                            stmt.setName(this.exprParser.name());
                                            return stmt;
                                        } else {
                                            throw new ParserException("TODO " + this.lexer.stringVal());
                                        }
                                    } else {
                                        MySqlShowEngineStatement stmt;
                                        if (this.identifierEquals("ENGINE")) {
                                            this.lexer.nextToken();
                                            stmt = new MySqlShowEngineStatement();
                                            stmt.setName(this.exprParser.name());
                                            stmt.setOption(Option.valueOf(this.lexer.stringVal().toUpperCase()));
                                            this.lexer.nextToken();
                                            return stmt;
                                        } else {
                                            MySqlShowEnginesStatement stmt;
                                            if (this.identifierEquals("STORAGE")) {
                                                this.lexer.nextToken();
                                                this.acceptIdentifier("ENGINES");
                                                stmt = new MySqlShowEnginesStatement();
                                                stmt.setStorage(true);
                                                return stmt;
                                            } else if (this.identifierEquals("ENGINES")) {
                                                this.lexer.nextToken();
                                                stmt = new MySqlShowEnginesStatement();
                                                return stmt;
                                            } else if (this.identifierEquals("EVENTS")) {
                                                this.lexer.nextToken();
                                                MySqlShowEventsStatement stmt = new MySqlShowEventsStatement();
                                                if (this.lexer.token() == Token.FROM || this.lexer.token() == Token.IN) {
                                                    this.lexer.nextToken();
                                                    stmt.setSchema(this.exprParser.name());
                                                }

                                                if (this.lexer.token() == Token.LIKE) {
                                                    this.lexer.nextToken();
                                                    stmt.setLike(this.exprParser.expr());
                                                }

                                                if (this.lexer.token() == Token.WHERE) {
                                                    this.lexer.nextToken();
                                                    stmt.setWhere(this.exprParser.expr());
                                                }

                                                return stmt;
                                            } else if (this.lexer.token() == Token.FUNCTION) {
                                                this.lexer.nextToken();
                                                if (this.identifierEquals("CODE")) {
                                                    this.lexer.nextToken();
                                                    MySqlShowFunctionCodeStatement stmt = new MySqlShowFunctionCodeStatement();
                                                    stmt.setName(this.exprParser.name());
                                                    return stmt;
                                                } else {
                                                    this.acceptIdentifier("STATUS");
                                                    MySqlShowFunctionStatusStatement stmt = new MySqlShowFunctionStatusStatement();
                                                    if (this.lexer.token() == Token.LIKE) {
                                                        this.lexer.nextToken();
                                                        stmt.setLike(this.exprParser.expr());
                                                    }

                                                    if (this.lexer.token() == Token.WHERE) {
                                                        this.lexer.nextToken();
                                                        stmt.setWhere(this.exprParser.expr());
                                                    }

                                                    return stmt;
                                                }
                                            } else if (this.identifierEquals("ENGINE")) {
                                                this.lexer.nextToken();
                                                stmt = new MySqlShowEngineStatement();
                                                stmt.setName(this.exprParser.name());
                                                stmt.setOption(Option.valueOf(this.lexer.stringVal().toUpperCase()));
                                                this.lexer.nextToken();
                                                return stmt;
                                            } else if (this.identifierEquals("STORAGE")) {
                                                this.lexer.nextToken();
                                                this.accept(Token.EQ);
                                                this.accept(Token.DEFAULT);
                                                stmt = new MySqlShowEnginesStatement();
                                                stmt.setStorage(true);
                                                return stmt;
                                            } else if (this.identifierEquals("ENGINES")) {
                                                this.lexer.nextToken();
                                                stmt = new MySqlShowEnginesStatement();
                                                return stmt;
                                            } else if (this.identifierEquals("GRANTS")) {
                                                this.lexer.nextToken();
                                                MySqlShowGrantsStatement stmt = new MySqlShowGrantsStatement();
                                                if (this.lexer.token() == Token.FOR) {
                                                    this.lexer.nextToken();
                                                    stmt.setUser(this.exprParser.expr());
                                                }

                                                return stmt;
                                            } else {
                                                SQLName database;
                                                SQLName database;
                                                if (this.lexer.token() != Token.INDEX && !this.identifierEquals("INDEXES")) {
                                                    if (this.identifierEquals("KEYS")) {
                                                        this.lexer.nextToken();
                                                        MySqlShowKeysStatement stmt = new MySqlShowKeysStatement();
                                                        if (this.lexer.token() == Token.FROM || this.lexer.token() == Token.IN) {
                                                            this.lexer.nextToken();
                                                            database = this.exprParser.name();
                                                            stmt.setTable(database);
                                                            if (this.lexer.token() == Token.FROM || this.lexer.token() == Token.IN) {
                                                                this.lexer.nextToken();
                                                                database = this.exprParser.name();
                                                                stmt.setDatabase(database);
                                                            }
                                                        }

                                                        return stmt;
                                                    } else if (this.lexer.token() != Token.OPEN && !this.identifierEquals("OPEN")) {
                                                        if (this.identifierEquals("PLUGINS")) {
                                                            this.lexer.nextToken();
                                                            MySqlShowPluginsStatement stmt = new MySqlShowPluginsStatement();
                                                            return stmt;
                                                        } else if (this.identifierEquals("PRIVILEGES")) {
                                                            this.lexer.nextToken();
                                                            MySqlShowPrivilegesStatement stmt = new MySqlShowPrivilegesStatement();
                                                            return stmt;
                                                        } else if (this.lexer.token() == Token.PROCEDURE) {
                                                            this.lexer.nextToken();
                                                            if (this.identifierEquals("CODE")) {
                                                                this.lexer.nextToken();
                                                                MySqlShowProcedureCodeStatement stmt = new MySqlShowProcedureCodeStatement();
                                                                stmt.setName(this.exprParser.name());
                                                                return stmt;
                                                            } else {
                                                                this.acceptIdentifier("STATUS");
                                                                MySqlShowProcedureStatusStatement stmt = new MySqlShowProcedureStatusStatement();
                                                                if (this.lexer.token() == Token.LIKE) {
                                                                    this.lexer.nextToken();
                                                                    stmt.setLike(this.exprParser.expr());
                                                                }

                                                                if (this.lexer.token() == Token.WHERE) {
                                                                    this.lexer.nextToken();
                                                                    stmt.setWhere(this.exprParser.expr());
                                                                }

                                                                return stmt;
                                                            }
                                                        } else if (this.identifierEquals("PROCESSLIST")) {
                                                            this.lexer.nextToken();
                                                            stmt = new MySqlShowProcessListStatement();
                                                            return stmt;
                                                        } else if (this.identifierEquals("PROFILES")) {
                                                            this.lexer.nextToken();
                                                            MySqlShowProfilesStatement stmt = new MySqlShowProfilesStatement();
                                                            return stmt;
                                                        } else if (this.identifierEquals("PROFILE")) {
                                                            this.lexer.nextToken();
                                                            MySqlShowProfileStatement stmt = new MySqlShowProfileStatement();

                                                            while(true) {
                                                                if (this.lexer.token() == Token.ALL) {
                                                                    stmt.getTypes().add(com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowProfileStatement.Type.ALL);
                                                                    this.lexer.nextToken();
                                                                } else if (this.identifierEquals("BLOCK")) {
                                                                    this.lexer.nextToken();
                                                                    this.acceptIdentifier("IO");
                                                                    stmt.getTypes().add(com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowProfileStatement.Type.BLOCK_IO);
                                                                } else if (this.identifierEquals("CONTEXT")) {
                                                                    this.lexer.nextToken();
                                                                    this.acceptIdentifier("SWITCHES");
                                                                    stmt.getTypes().add(com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowProfileStatement.Type.CONTEXT_SWITCHES);
                                                                } else if (this.identifierEquals("CPU")) {
                                                                    this.lexer.nextToken();
                                                                    stmt.getTypes().add(com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowProfileStatement.Type.CPU);
                                                                } else if (this.identifierEquals("IPC")) {
                                                                    this.lexer.nextToken();
                                                                    stmt.getTypes().add(com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowProfileStatement.Type.IPC);
                                                                } else if (this.identifierEquals("MEMORY")) {
                                                                    this.lexer.nextToken();
                                                                    stmt.getTypes().add(com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowProfileStatement.Type.MEMORY);
                                                                } else if (this.identifierEquals("PAGE")) {
                                                                    this.lexer.nextToken();
                                                                    this.acceptIdentifier("FAULTS");
                                                                    stmt.getTypes().add(com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowProfileStatement.Type.PAGE_FAULTS);
                                                                } else if (this.identifierEquals("SOURCE")) {
                                                                    this.lexer.nextToken();
                                                                    stmt.getTypes().add(com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowProfileStatement.Type.SOURCE);
                                                                } else {
                                                                    if (!this.identifierEquals("SWAPS")) {
                                                                        break;
                                                                    }

                                                                    this.lexer.nextToken();
                                                                    stmt.getTypes().add(com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowProfileStatement.Type.SWAPS);
                                                                }

                                                                if (this.lexer.token() != Token.COMMA) {
                                                                    break;
                                                                }

                                                                this.lexer.nextToken();
                                                            }

                                                            if (this.lexer.token() == Token.FOR) {
                                                                this.lexer.nextToken();
                                                                this.acceptIdentifier("QUERY");
                                                                stmt.setForQuery(this.exprParser.primary());
                                                            }

                                                            stmt.setLimit(this.exprParser.parseLimit());
                                                            return stmt;
                                                        } else {
                                                            MySqlShowRelayLogEventsStatement stmt;
                                                            if (this.identifierEquals("RELAYLOG")) {
                                                                this.lexer.nextToken();
                                                                this.acceptIdentifier("EVENTS");
                                                                stmt = new MySqlShowRelayLogEventsStatement();
                                                                if (this.lexer.token() == Token.IN) {
                                                                    this.lexer.nextToken();
                                                                    stmt.setLogName(this.exprParser.primary());
                                                                }

                                                                if (this.lexer.token() == Token.FROM) {
                                                                    this.lexer.nextToken();
                                                                    stmt.setFrom(this.exprParser.primary());
                                                                }

                                                                stmt.setLimit(this.exprParser.parseLimit());
                                                                return stmt;
                                                            } else if (this.identifierEquals("RELAYLOG")) {
                                                                this.lexer.nextToken();
                                                                this.acceptIdentifier("EVENTS");
                                                                stmt = new MySqlShowRelayLogEventsStatement();
                                                                if (this.lexer.token() == Token.IN) {
                                                                    this.lexer.nextToken();
                                                                    stmt.setLogName(this.exprParser.primary());
                                                                }

                                                                if (this.lexer.token() == Token.FROM) {
                                                                    this.lexer.nextToken();
                                                                    stmt.setFrom(this.exprParser.primary());
                                                                }

                                                                stmt.setLimit(this.exprParser.parseLimit());
                                                                return stmt;
                                                            } else if (this.identifierEquals("SLAVE")) {
                                                                this.lexer.nextToken();
                                                                if (this.identifierEquals("STATUS")) {
                                                                    this.lexer.nextToken();
                                                                    return new MySqlShowSlaveStatusStatement();
                                                                } else {
                                                                    this.acceptIdentifier("HOSTS");
                                                                    MySqlShowSlaveHostsStatement stmt = new MySqlShowSlaveHostsStatement();
                                                                    return stmt;
                                                                }
                                                            } else if (this.lexer.token() != Token.TABLE) {
                                                                if (this.identifierEquals("TRIGGERS")) {
                                                                    this.lexer.nextToken();
                                                                    MySqlShowTriggersStatement stmt = new MySqlShowTriggersStatement();
                                                                    if (this.lexer.token() == Token.FROM) {
                                                                        this.lexer.nextToken();
                                                                        database = this.exprParser.name();
                                                                        stmt.setDatabase(database);
                                                                    }

                                                                    SQLExpr where;
                                                                    if (this.lexer.token() == Token.LIKE) {
                                                                        this.lexer.nextToken();
                                                                        where = this.exprParser.expr();
                                                                        stmt.setLike(where);
                                                                    }

                                                                    if (this.lexer.token() == Token.WHERE) {
                                                                        this.lexer.nextToken();
                                                                        where = this.exprParser.expr();
                                                                        stmt.setWhere(where);
                                                                    }

                                                                    return stmt;
                                                                } else {
                                                                    throw new ParserException("TODO " + this.lexer.stringVal());
                                                                }
                                                            } else {
                                                                this.lexer.nextToken();
                                                                this.acceptIdentifier("STATUS");
                                                                MySqlShowTableStatusStatement stmt = new MySqlShowTableStatusStatement();
                                                                if (this.lexer.token() == Token.FROM || this.lexer.token() == Token.IN) {
                                                                    this.lexer.nextToken();
                                                                    stmt.setDatabase(this.exprParser.name());
                                                                }

                                                                if (this.lexer.token() == Token.LIKE) {
                                                                    this.lexer.nextToken();
                                                                    stmt.setLike(this.exprParser.expr());
                                                                }

                                                                if (this.lexer.token() == Token.WHERE) {
                                                                    this.lexer.nextToken();
                                                                    stmt.setWhere(this.exprParser.expr());
                                                                }

                                                                return stmt;
                                                            }
                                                        }
                                                    } else {
                                                        this.lexer.nextToken();
                                                        this.acceptIdentifier("TABLES");
                                                        MySqlShowOpenTablesStatement stmt = new MySqlShowOpenTablesStatement();
                                                        if (this.lexer.token() == Token.FROM || this.lexer.token() == Token.IN) {
                                                            this.lexer.nextToken();
                                                            stmt.setDatabase(this.exprParser.name());
                                                        }

                                                        if (this.lexer.token() == Token.LIKE) {
                                                            this.lexer.nextToken();
                                                            stmt.setLike(this.exprParser.expr());
                                                        }

                                                        if (this.lexer.token() == Token.WHERE) {
                                                            this.lexer.nextToken();
                                                            stmt.setWhere(this.exprParser.expr());
                                                        }

                                                        return stmt;
                                                    }
                                                } else {
                                                    this.lexer.nextToken();
                                                    MySqlShowIndexesStatement stmt = new MySqlShowIndexesStatement();
                                                    if (this.lexer.token() == Token.FROM || this.lexer.token() == Token.IN) {
                                                        this.lexer.nextToken();
                                                        database = this.exprParser.name();
                                                        stmt.setTable(database);
                                                        if (this.lexer.token() == Token.FROM || this.lexer.token() == Token.IN) {
                                                            this.lexer.nextToken();
                                                            database = this.exprParser.name();
                                                            stmt.setDatabase(database);
                                                        }
                                                    }

                                                    if (this.lexer.token() == Token.HINT) {
                                                        stmt.setHints(this.exprParser.parseHints());
                                                    }

                                                    return stmt;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                this.lexer.nextToken();
                stmt = this.parseShowColumns();
                stmt.setFull(full);
                return stmt;
            }
        }
    }

    private MySqlShowStatusStatement parseShowStatus() {
        MySqlShowStatusStatement stmt = new MySqlShowStatusStatement();
        SQLExpr where;
        if (this.lexer.token() == Token.LIKE) {
            this.lexer.nextToken();
            where = this.exprParser.expr();
            stmt.setLike(where);
        }

        if (this.lexer.token() == Token.WHERE) {
            this.lexer.nextToken();
            where = this.exprParser.expr();
            stmt.setWhere(where);
        }

        return stmt;
    }

    private MySqlShowVariantsStatement parseShowVariants() {
        MySqlShowVariantsStatement stmt = new MySqlShowVariantsStatement();
        SQLExpr where;
        if (this.lexer.token() == Token.LIKE) {
            this.lexer.nextToken();
            where = this.exprParser.expr();
            stmt.setLike(where);
        }

        if (this.lexer.token() == Token.WHERE) {
            this.lexer.nextToken();
            where = this.exprParser.expr();
            stmt.setWhere(where);
        }

        return stmt;
    }

    private MySqlShowWarningsStatement parseShowWarnings() {
        MySqlShowWarningsStatement stmt = new MySqlShowWarningsStatement();
        stmt.setLimit(this.exprParser.parseLimit());
        return stmt;
    }

    private MySqlShowDatabasesStatement parseShowDatabases() {
        MySqlShowDatabasesStatement stmt = new MySqlShowDatabasesStatement();
        SQLExpr where;
        if (this.lexer.token() == Token.LIKE) {
            this.lexer.nextToken();
            where = this.exprParser.expr();
            stmt.setLike(where);
        }

        if (this.lexer.token() == Token.WHERE) {
            this.lexer.nextToken();
            where = this.exprParser.expr();
            stmt.setWhere(where);
        }

        return stmt;
    }

    private SQLShowTablesStatement parseShowTabless() {
        SQLShowTablesStatement stmt = new SQLShowTablesStatement();
        if (this.lexer.token() == Token.FROM) {
            this.lexer.nextToken();
            SQLName database = this.exprParser.name();
            stmt.setDatabase(database);
        }

        SQLExpr where;
        if (this.lexer.token() == Token.LIKE) {
            this.lexer.nextToken();
            where = this.exprParser.expr();
            stmt.setLike(where);
        }

        if (this.lexer.token() == Token.WHERE) {
            this.lexer.nextToken();
            where = this.exprParser.expr();
            stmt.setWhere(where);
        }

        return stmt;
    }

    private MySqlShowColumnsStatement parseShowColumns() {
        MySqlShowColumnsStatement stmt = new MySqlShowColumnsStatement();
        if (this.lexer.token() == Token.FROM) {
            this.lexer.nextToken();
            SQLName table = this.exprParser.name();
            stmt.setTable(table);
            if (this.lexer.token() == Token.FROM || this.lexer.token() == Token.IN) {
                this.lexer.nextToken();
                SQLName database = this.exprParser.name();
                stmt.setDatabase(database);
            }
        }

        SQLExpr where;
        if (this.lexer.token() == Token.LIKE) {
            this.lexer.nextToken();
            where = this.exprParser.expr();
            stmt.setLike(where);
        }

        if (this.lexer.token() == Token.WHERE) {
            this.lexer.nextToken();
            where = this.exprParser.expr();
            stmt.setWhere(where);
        }

        return stmt;
    }

    public SQLStartTransactionStatement parseStart() {
        this.acceptIdentifier("START");
        this.acceptIdentifier("TRANSACTION");
        SQLStartTransactionStatement stmt = new SQLStartTransactionStatement();
        if (this.lexer.token() == Token.WITH) {
            this.lexer.nextToken();
            this.acceptIdentifier("CONSISTENT");
            this.acceptIdentifier("SNAPSHOT");
            stmt.setConsistentSnapshot(true);
        }

        if (this.lexer.token() == Token.BEGIN) {
            this.lexer.nextToken();
            stmt.setBegin(true);
            if (this.identifierEquals("WORK")) {
                this.lexer.nextToken();
                stmt.setWork(true);
            }
        }

        if (this.lexer.token() == Token.HINT) {
            stmt.setHints(this.exprParser.parseHints());
        }

        return stmt;
    }

    public MySqlRollbackStatement parseRollback() {
        this.acceptIdentifier("ROLLBACK");
        MySqlRollbackStatement stmt = new MySqlRollbackStatement();
        if (this.identifierEquals("WORK")) {
            this.lexer.nextToken();
        }

        if (this.lexer.token() == Token.AND) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.NOT) {
                this.lexer.nextToken();
                this.acceptIdentifier("CHAIN");
                stmt.setChain(Boolean.FALSE);
            } else {
                this.acceptIdentifier("CHAIN");
                stmt.setChain(Boolean.TRUE);
            }
        }

        if (this.lexer.token() == Token.TO) {
            this.lexer.nextToken();
            if (this.identifierEquals("SAVEPOINT")) {
                this.lexer.nextToken();
            }

            stmt.setTo(this.exprParser.name());
        }

        return stmt;
    }

    public MySqlCommitStatement parseCommit() {
        this.acceptIdentifier("COMMIT");
        MySqlCommitStatement stmt = new MySqlCommitStatement();
        if (this.identifierEquals("WORK")) {
            this.lexer.nextToken();
            stmt.setWork(true);
        }

        if (this.lexer.token() == Token.AND) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.NOT) {
                this.lexer.nextToken();
                this.acceptIdentifier("CHAIN");
                stmt.setChain(Boolean.FALSE);
            } else {
                this.acceptIdentifier("CHAIN");
                stmt.setChain(Boolean.TRUE);
            }
        }

        return stmt;
    }

    public MySqlReplaceStatement parseReplicate() {
        MySqlReplaceStatement stmt = new MySqlReplaceStatement();
        this.accept(Token.REPLACE);
        if (this.lexer.token() == Token.COMMENT) {
            this.lexer.nextToken();
        }

        if (this.identifierEquals("LOW_PRIORITY")) {
            stmt.setLowPriority(true);
            this.lexer.nextToken();
        }

        if (this.identifierEquals("DELAYED")) {
            stmt.setDelayed(true);
            this.lexer.nextToken();
        }

        if (this.lexer.token() == Token.INTO) {
            this.lexer.nextToken();
        }

        SQLName tableName = this.exprParser.name();
        stmt.setTableName(tableName);
        SQLQueryExpr queryExpr;
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.SELECT) {
                queryExpr = (SQLQueryExpr)this.exprParser.expr();
                stmt.setQuery(queryExpr);
            } else {
                this.exprParser.exprList(stmt.getColumns(), stmt);
            }

            this.accept(Token.RPAREN);
        }

        if (this.lexer.token() != Token.VALUES && !this.identifierEquals("VALUE")) {
            if (this.lexer.token() == Token.SELECT) {
                queryExpr = (SQLQueryExpr)this.exprParser.expr();
                stmt.setQuery(queryExpr);
            } else if (this.lexer.token() == Token.SET) {
                this.lexer.nextToken();
                ValuesClause values = new ValuesClause();
                stmt.getValuesList().add(values);

                while(true) {
                    stmt.addColumn(this.exprParser.name());
                    if (this.lexer.token() == Token.COLONEQ) {
                        this.lexer.nextToken();
                    } else {
                        this.accept(Token.EQ);
                    }

                    values.addValue(this.exprParser.expr());
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }

                    this.lexer.nextToken();
                }
            } else if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                queryExpr = (SQLQueryExpr)this.exprParser.expr();
                stmt.setQuery(queryExpr);
                this.accept(Token.RPAREN);
            }
        } else {
            this.lexer.nextToken();
            this.parseValueClause(stmt.getValuesList(), 0);
        }

        return stmt;
    }

    protected SQLStatement parseLoad() {
        this.acceptIdentifier("LOAD");
        if (this.identifierEquals("DATA")) {
            SQLStatement stmt = this.parseLoadDataInFile();
            return stmt;
        } else if (this.identifierEquals("XML")) {
            SQLStatement stmt = this.parseLoadXml();
            return stmt;
        } else {
            throw new ParserException("TODO");
        }
    }

    protected MySqlLoadXmlStatement parseLoadXml() {
        this.acceptIdentifier("XML");
        MySqlLoadXmlStatement stmt = new MySqlLoadXmlStatement();
        if (this.identifierEquals("LOW_PRIORITY")) {
            stmt.setLowPriority(true);
            this.lexer.nextToken();
        }

        if (this.identifierEquals("CONCURRENT")) {
            stmt.setConcurrent(true);
            this.lexer.nextToken();
        }

        if (this.identifierEquals("LOCAL")) {
            stmt.setLocal(true);
            this.lexer.nextToken();
        }

        this.acceptIdentifier("INFILE");
        SQLLiteralExpr fileName = (SQLLiteralExpr)this.exprParser.expr();
        stmt.setFileName(fileName);
        if (this.lexer.token() == Token.REPLACE) {
            stmt.setReplicate(true);
            this.lexer.nextToken();
        }

        if (this.identifierEquals("IGNORE")) {
            stmt.setIgnore(true);
            this.lexer.nextToken();
        }

        this.accept(Token.INTO);
        this.accept(Token.TABLE);
        SQLName tableName = this.exprParser.name();
        stmt.setTableName(tableName);
        if (this.identifierEquals("CHARACTER")) {
            this.lexer.nextToken();
            this.accept(Token.SET);
            if (this.lexer.token() != Token.LITERAL_CHARS) {
                throw new ParserException("syntax error, illegal charset");
            }

            String charset = this.lexer.stringVal();
            this.lexer.nextToken();
            stmt.setCharset(charset);
        }

        if (this.identifierEquals("ROWS")) {
            this.lexer.nextToken();
            this.accept(Token.IDENTIFIED);
            this.accept(Token.BY);
            SQLExpr rowsIdentifiedBy = this.exprParser.expr();
            stmt.setRowsIdentifiedBy(rowsIdentifiedBy);
        }

        if (this.identifierEquals("IGNORE")) {
            throw new ParserException("TODO");
        } else if (this.lexer.token() == Token.SET) {
            throw new ParserException("TODO");
        } else {
            return stmt;
        }
    }

    protected MySqlLoadDataInFileStatement parseLoadDataInFile() {
        this.acceptIdentifier("DATA");
        MySqlLoadDataInFileStatement stmt = new MySqlLoadDataInFileStatement();
        if (this.identifierEquals("LOW_PRIORITY")) {
            stmt.setLowPriority(true);
            this.lexer.nextToken();
        }

        if (this.identifierEquals("CONCURRENT")) {
            stmt.setConcurrent(true);
            this.lexer.nextToken();
        }

        if (this.identifierEquals("LOCAL")) {
            stmt.setLocal(true);
            this.lexer.nextToken();
        }

        this.acceptIdentifier("INFILE");
        SQLLiteralExpr fileName = (SQLLiteralExpr)this.exprParser.expr();
        stmt.setFileName(fileName);
        if (this.lexer.token() == Token.REPLACE) {
            stmt.setReplicate(true);
            this.lexer.nextToken();
        }

        if (this.identifierEquals("IGNORE")) {
            stmt.setIgnore(true);
            this.lexer.nextToken();
        }

        this.accept(Token.INTO);
        this.accept(Token.TABLE);
        SQLName tableName = this.exprParser.name();
        stmt.setTableName(tableName);
        if (this.identifierEquals("CHARACTER")) {
            this.lexer.nextToken();
            this.accept(Token.SET);
            if (this.lexer.token() != Token.LITERAL_CHARS) {
                throw new ParserException("syntax error, illegal charset");
            }

            String charset = this.lexer.stringVal();
            this.lexer.nextToken();
            stmt.setCharset(charset);
        }

        if (this.identifierEquals("FIELDS") || this.identifierEquals("COLUMNS")) {
            this.lexer.nextToken();
            if (this.identifierEquals("TERMINATED")) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                stmt.setColumnsTerminatedBy(new SQLCharExpr(this.lexer.stringVal()));
                this.lexer.nextToken();
            }

            if (this.identifierEquals("OPTIONALLY")) {
                stmt.setColumnsEnclosedOptionally(true);
                this.lexer.nextToken();
            }

            if (this.identifierEquals("ENCLOSED")) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                stmt.setColumnsEnclosedBy(new SQLCharExpr(this.lexer.stringVal()));
                this.lexer.nextToken();
            }

            if (this.identifierEquals("ESCAPED")) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                stmt.setColumnsEscaped(new SQLCharExpr(this.lexer.stringVal()));
                this.lexer.nextToken();
            }
        }

        if (this.identifierEquals("LINES")) {
            this.lexer.nextToken();
            if (this.identifierEquals("STARTING")) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                stmt.setLinesStartingBy(new SQLCharExpr(this.lexer.stringVal()));
                this.lexer.nextToken();
            }

            if (this.identifierEquals("TERMINATED")) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                stmt.setLinesTerminatedBy(new SQLCharExpr(this.lexer.stringVal()));
                this.lexer.nextToken();
            }
        }

        if (this.identifierEquals("IGNORE")) {
            this.lexer.nextToken();
            stmt.setIgnoreLinesNumber(this.exprParser.expr());
            this.acceptIdentifier("LINES");
        }

        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            this.exprParser.exprList(stmt.getColumns(), stmt);
            this.accept(Token.RPAREN);
        }

        if (this.lexer.token() == Token.SET) {
            this.lexer.nextToken();
            this.exprParser.exprList(stmt.getSetList(), stmt);
        }

        return stmt;
    }

    public MySqlPrepareStatement parsePrepare() {
        this.acceptIdentifier("PREPARE");
        SQLName name = this.exprParser.name();
        this.accept(Token.FROM);
        SQLExpr from = this.exprParser.expr();
        return new MySqlPrepareStatement(name, from);
    }

    public MySqlExecuteStatement parseExecute() {
        this.acceptIdentifier("EXECUTE");
        MySqlExecuteStatement stmt = new MySqlExecuteStatement();
        SQLName statementName = this.exprParser.name();
        stmt.setStatementName(statementName);
        if (this.identifierEquals("USING")) {
            this.lexer.nextToken();
            this.exprParser.exprList(stmt.getParameters(), stmt);
        }

        return stmt;
    }

    public MysqlDeallocatePrepareStatement parseDeallocatePrepare() {
        this.acceptIdentifier("DEALLOCATE");
        this.acceptIdentifier("PREPARE");
        MysqlDeallocatePrepareStatement stmt = new MysqlDeallocatePrepareStatement();
        SQLName statementName = this.exprParser.name();
        stmt.setStatementName(statementName);
        return stmt;
    }

    public SQLInsertStatement parseInsert() {
        MySqlInsertStatement insertStatement = new MySqlInsertStatement();
        if (this.lexer.token() == Token.INSERT) {
            this.lexer.nextToken();

            label80:
            while(true) {
                while(!this.identifierEquals("LOW_PRIORITY")) {
                    if (this.identifierEquals("DELAYED")) {
                        insertStatement.setDelayed(true);
                        this.lexer.nextToken();
                    } else if (this.identifierEquals("HIGH_PRIORITY")) {
                        insertStatement.setHighPriority(true);
                        this.lexer.nextToken();
                    } else if (this.identifierEquals("IGNORE")) {
                        insertStatement.setIgnore(true);
                        this.lexer.nextToken();
                    } else {
                        if (!this.identifierEquals("ROLLBACK_ON_FAIL")) {
                            if (this.lexer.token() == Token.INTO) {
                                this.lexer.nextToken();
                            }

                            SQLName tableName = this.exprParser.name();
                            insertStatement.setTableName(tableName);
                            if (this.lexer.token() == Token.IDENTIFIER && !this.identifierEquals("VALUE")) {
                                insertStatement.setAlias(this.lexer.stringVal());
                                this.lexer.nextToken();
                            }
                            break label80;
                        }

                        insertStatement.setRollbackOnFail(true);
                        this.lexer.nextToken();
                    }
                }

                insertStatement.setLowPriority(true);
                this.lexer.nextToken();
            }
        }

        int columnSize = 0;
        SQLSelect select;
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.SELECT) {
                select = this.exprParser.createSelectParser().select();
                select.setParent(insertStatement);
                insertStatement.setQuery(select);
            } else {
                this.exprParser.exprList(insertStatement.getColumns(), insertStatement);
                columnSize = insertStatement.getColumns().size();
            }

            this.accept(Token.RPAREN);
        }

        if (this.lexer.token() != Token.VALUES && !this.identifierEquals("VALUE")) {
            if (this.lexer.token() == Token.SET) {
                this.lexer.nextToken();
                ValuesClause values = new ValuesClause();
                insertStatement.getValuesList().add(values);

                while(true) {
                    SQLName name = this.exprParser.name();
                    insertStatement.addColumn(name);
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    } else {
                        this.accept(Token.COLONEQ);
                    }

                    values.addValue(this.exprParser.expr());
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }

                    this.lexer.nextToken();
                }
            } else if (this.lexer.token() == Token.SELECT) {
                select = this.exprParser.createSelectParser().select();
                select.setParent(insertStatement);
                insertStatement.setQuery(select);
            } else if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                select = this.exprParser.createSelectParser().select();
                select.setParent(insertStatement);
                insertStatement.setQuery(select);
                this.accept(Token.RPAREN);
            }
        } else {
            this.lexer.nextTokenLParen();
            this.parseValueClause(insertStatement.getValuesList(), columnSize);
        }

        if (this.lexer.token() == Token.ON) {
            this.lexer.nextToken();
            this.acceptIdentifier("DUPLICATE");
            this.accept(Token.KEY);
            this.accept(Token.UPDATE);
            this.exprParser.exprList(insertStatement.getDuplicateKeyUpdate(), insertStatement);
        }

        return insertStatement;
    }

    private void parseValueClause(List<ValuesClause> valueClauseList, int columnSize) {
        int var3 = 0;

        while(true) {
            if (this.lexer.token() != Token.LPAREN) {
                throw new ParserException("syntax error, expect ')'");
            }

            this.lexer.nextTokenValue();
            if (this.lexer.token() == Token.RPAREN) {
                ValuesClause values = new ValuesClause(new ArrayList(0));
                valueClauseList.add(values);
            } else {
                ArrayList valueExprList;
                if (columnSize > 0) {
                    valueExprList = new ArrayList(columnSize);
                } else {
                    valueExprList = new ArrayList();
                }

                label61:
                while(true) {
                    while(true) {
                        Object expr;
                        if (this.lexer.token() == Token.LITERAL_INT) {
                            expr = new SQLIntegerExpr(this.lexer.integerValue());
                            this.lexer.nextTokenComma();
                        } else if (this.lexer.token() == Token.LITERAL_CHARS) {
                            expr = new SQLCharExpr(this.lexer.stringVal());
                            this.lexer.nextTokenComma();
                        } else if (this.lexer.token() == Token.LITERAL_NCHARS) {
                            expr = new SQLNCharExpr(this.lexer.stringVal());
                            this.lexer.nextTokenComma();
                        } else {
                            expr = this.exprParser.expr();
                        }

                        if (this.lexer.token() != Token.COMMA) {
                            if (this.lexer.token() == Token.RPAREN) {
                                valueExprList.add(expr);
                                break label61;
                            }

                            SQLExpr expr = this.exprParser.primaryRest((SQLExpr)expr);
                            if (this.lexer.token() != Token.COMMA && this.lexer.token() != Token.RPAREN) {
                                expr = this.exprParser.exprRest(expr);
                            }

                            valueExprList.add(expr);
                            if (this.lexer.token() != Token.COMMA) {
                                break label61;
                            }

                            this.lexer.nextToken();
                        } else {
                            valueExprList.add(expr);
                            this.lexer.nextTokenValue();
                        }
                    }
                }

                ValuesClause values = new ValuesClause(valueExprList);
                valueClauseList.add(values);
            }

            if (this.lexer.token() != Token.RPAREN) {
                throw new ParserException("syntax error");
            }

            if (!this.parseCompleteValues && valueClauseList.size() >= this.parseValuesSize) {
                this.lexer.skipToEOF();
                break;
            }

            this.lexer.nextTokenComma();
            if (this.lexer.token() != Token.COMMA) {
                break;
            }

            this.lexer.nextTokenLParen();
            ++var3;
        }

    }

    public SQLSelectParser createSQLSelectParser() {
        return new MySqlSelectParser(this.exprParser);
    }

    public SQLStatement parseSet() {
        this.accept(Token.SET);
        if (this.identifierEquals("PASSWORD")) {
            this.lexer.nextToken();
            MySqlSetPasswordStatement stmt = new MySqlSetPasswordStatement();
            if (this.lexer.token() == Token.FOR) {
                this.lexer.nextToken();
                stmt.setUser(this.exprParser.name());
            }

            this.accept(Token.EQ);
            stmt.setPassword(this.exprParser.expr());
            return stmt;
        } else {
            Boolean global = null;
            if (this.identifierEquals("GLOBAL")) {
                global = Boolean.TRUE;
                this.lexer.nextToken();
            } else if (this.identifierEquals("SESSION")) {
                global = Boolean.FALSE;
                this.lexer.nextToken();
            }

            if (this.identifierEquals("TRANSACTION")) {
                MySqlSetTransactionStatement stmt = new MySqlSetTransactionStatement();
                stmt.setGlobal(global);
                this.lexer.nextToken();
                if (this.identifierEquals("ISOLATION")) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("LEVEL");
                    if (this.identifierEquals("READ")) {
                        this.lexer.nextToken();
                        if (this.identifierEquals("UNCOMMITTED")) {
                            stmt.setIsolationLevel("READ UNCOMMITTED");
                            this.lexer.nextToken();
                        } else if (this.identifierEquals("WRITE")) {
                            stmt.setIsolationLevel("READ WRITE");
                            this.lexer.nextToken();
                        } else if (this.identifierEquals("ONLY")) {
                            stmt.setIsolationLevel("READ ONLY");
                            this.lexer.nextToken();
                        } else {
                            if (!this.identifierEquals("COMMITTED")) {
                                throw new ParserException("UNKOWN TRANSACTION LEVEL : " + this.lexer.stringVal());
                            }

                            stmt.setIsolationLevel("READ COMMITTED");
                            this.lexer.nextToken();
                        }
                    } else if (this.identifierEquals("SERIALIZABLE")) {
                        stmt.setIsolationLevel("SERIALIZABLE");
                        this.lexer.nextToken();
                    } else {
                        if (!this.identifierEquals("REPEATABLE")) {
                            throw new ParserException("UNKOWN TRANSACTION LEVEL : " + this.lexer.stringVal());
                        }

                        this.lexer.nextToken();
                        if (!this.identifierEquals("READ")) {
                            throw new ParserException("UNKOWN TRANSACTION LEVEL : " + this.lexer.stringVal());
                        }

                        stmt.setIsolationLevel("REPEATABLE READ");
                        this.lexer.nextToken();
                    }
                } else if (this.identifierEquals("READ")) {
                    this.lexer.nextToken();
                    if (this.identifierEquals("ONLY")) {
                        stmt.setAccessModel("ONLY");
                        this.lexer.nextToken();
                    } else {
                        if (!this.identifierEquals("WRITE")) {
                            throw new ParserException("UNKOWN ACCESS MODEL : " + this.lexer.stringVal());
                        }

                        stmt.setAccessModel("WRITE");
                        this.lexer.nextToken();
                    }
                }

                return stmt;
            } else {
                String collate;
                String charSet;
                if (this.identifierEquals("NAMES")) {
                    this.lexer.nextToken();
                    MySqlSetNamesStatement stmt = new MySqlSetNamesStatement();
                    if (this.lexer.token() == Token.DEFAULT) {
                        this.lexer.nextToken();
                        stmt.setDefault(true);
                    } else {
                        charSet = this.lexer.stringVal();
                        stmt.setCharSet(charSet);
                        this.lexer.nextToken();
                        if (this.identifierEquals("COLLATE")) {
                            this.lexer.nextToken();
                            collate = this.lexer.stringVal();
                            stmt.setCollate(collate);
                            this.lexer.nextToken();
                        }
                    }

                    return stmt;
                } else if (this.identifierEquals("CHARACTER")) {
                    this.lexer.nextToken();
                    this.accept(Token.SET);
                    MySqlSetCharSetStatement stmt = new MySqlSetCharSetStatement();
                    if (this.lexer.token() == Token.DEFAULT) {
                        this.lexer.nextToken();
                        stmt.setDefault(true);
                    } else {
                        charSet = this.lexer.stringVal();
                        stmt.setCharSet(charSet);
                        this.lexer.nextToken();
                        if (this.identifierEquals("COLLATE")) {
                            this.lexer.nextToken();
                            collate = this.lexer.stringVal();
                            stmt.setCollate(collate);
                            this.lexer.nextToken();
                        }
                    }

                    return stmt;
                } else {
                    SQLSetStatement stmt = new SQLSetStatement(this.getDbType());
                    this.parseAssignItems(stmt.getItems(), stmt);
                    if (global != null && global) {
                        SQLVariantRefExpr varRef = (SQLVariantRefExpr)((SQLAssignItem)stmt.getItems().get(0)).getTarget();
                        varRef.setGlobal(true);
                    }

                    if (this.lexer.token() == Token.HINT) {
                        stmt.setHints(this.exprParser.parseHints());
                    }

                    return stmt;
                }
            }
        }
    }

    public SQLStatement parseAlter() {
        this.accept(Token.ALTER);
        if (this.lexer.token() == Token.USER) {
            return this.parseAlterUser();
        } else {
            boolean ignore = false;
            if (this.identifierEquals("IGNORE")) {
                ignore = true;
                this.lexer.nextToken();
            }

            if (this.lexer.token() == Token.TABLE) {
                return this.parseAlterTable(ignore);
            } else if (this.lexer.token() == Token.DATABASE) {
                return this.parseAlterDatabase();
            } else {
                throw new ParserException("TODO " + this.lexer.token() + " " + this.lexer.stringVal());
            }
        }
    }

    protected SQLStatement parseAlterTable(boolean ignore) {
        this.lexer.nextToken();
        SQLAlterTableStatement stmt = new SQLAlterTableStatement(this.getDbType());
        stmt.setIgnore(ignore);
        stmt.setName(this.exprParser.name());

        while(true) {
            if (this.lexer.token() == Token.DROP) {
                this.parseAlterDrop(stmt);
            } else if (this.lexer.token() == Token.TRUNCATE) {
                this.lexer.nextToken();
                this.accept(Token.PARTITION);
                SQLAlterTableTruncatePartition item = new SQLAlterTableTruncatePartition();
                if (this.lexer.token() == Token.ALL) {
                    item.getPartitions().add(new SQLIdentifierExpr("ALL"));
                    this.lexer.nextToken();
                } else {
                    this.exprParser.names(item.getPartitions(), item);
                }

                stmt.addItem(item);
            } else {
                SQLPartition partition;
                if (this.identifierEquals("ADD")) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.COLUMN) {
                        this.lexer.nextToken();
                        this.parseAlterTableAddColumn(stmt);
                    } else {
                        SQLAlterTableAddIndex item;
                        if (this.lexer.token() == Token.INDEX) {
                            item = this.parseAlterTableAddIndex();
                            item.setParent(stmt);
                            stmt.addItem(item);
                        } else if (this.lexer.token() == Token.UNIQUE) {
                            item = this.parseAlterTableAddIndex();
                            item.setParent(stmt);
                            stmt.addItem(item);
                        } else if (this.lexer.token() == Token.PRIMARY) {
                            SQLPrimaryKey primaryKey = this.exprParser.parsePrimaryKey();
                            SQLAlterTableAddConstraint item = new SQLAlterTableAddConstraint(primaryKey);
                            stmt.addItem(item);
                        } else if (this.lexer.token() == Token.KEY) {
                            item = this.parseAlterTableAddIndex();
                            item.setParent(stmt);
                            stmt.addItem(item);
                        } else if (this.lexer.token() == Token.CONSTRAINT) {
                            this.lexer.nextToken();
                            SQLName constraintName = this.exprParser.name();
                            SQLAlterTableAddConstraint item;
                            if (this.lexer.token() == Token.PRIMARY) {
                                SQLPrimaryKey primaryKey = ((MySqlExprParser)this.exprParser).parsePrimaryKey();
                                primaryKey.setName(constraintName);
                                item = new SQLAlterTableAddConstraint(primaryKey);
                                item.setParent(stmt);
                                stmt.addItem(item);
                            } else if (this.lexer.token() == Token.FOREIGN) {
                                MysqlForeignKey fk = this.getExprParser().parseForeignKey();
                                fk.setName(constraintName);
                                fk.setHasConstraint(true);
                                item = new SQLAlterTableAddConstraint(fk);
                                stmt.addItem(item);
                            } else {
                                if (this.lexer.token() != Token.UNIQUE) {
                                    throw new ParserException("TODO " + this.lexer.token() + " " + this.lexer.stringVal());
                                }

                                SQLUnique unique = this.exprParser.parseUnique();
                                item = new SQLAlterTableAddConstraint(unique);
                                stmt.addItem(item);
                            }
                        } else if (this.lexer.token() == Token.PARTITION) {
                            this.lexer.nextToken();
                            SQLAlterTableAddPartition item = new SQLAlterTableAddPartition();
                            if (this.identifierEquals("PARTITIONS")) {
                                this.lexer.nextToken();
                                item.setPartitionCount(this.exprParser.integerExpr());
                            }

                            if (this.lexer.token() == Token.LPAREN) {
                                this.lexer.nextToken();
                                partition = this.getExprParser().parsePartition();
                                this.accept(Token.RPAREN);
                                item.addPartition(partition);
                            }

                            stmt.addItem(item);
                        } else {
                            if (this.identifierEquals("FULLTEXT")) {
                                throw new ParserException("TODO " + this.lexer.token() + " " + this.lexer.stringVal());
                            }

                            if (this.identifierEquals("SPATIAL")) {
                                throw new ParserException("TODO " + this.lexer.token() + " " + this.lexer.stringVal());
                            }

                            this.parseAlterTableAddColumn(stmt);
                        }
                    }
                } else if (this.lexer.token() == Token.ALTER) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.COLUMN) {
                        this.lexer.nextToken();
                    }

                    MySqlAlterTableAlterColumn alterColumn = new MySqlAlterTableAlterColumn();
                    alterColumn.setColumn(this.exprParser.name());
                    if (this.lexer.token() == Token.SET) {
                        this.lexer.nextToken();
                        this.accept(Token.DEFAULT);
                        alterColumn.setDefaultExpr(this.exprParser.expr());
                    } else {
                        this.accept(Token.DROP);
                        this.accept(Token.DEFAULT);
                        alterColumn.setDropDefault(true);
                    }

                    stmt.addItem(alterColumn);
                } else if (this.identifierEquals("CHANGE")) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.COLUMN) {
                        this.lexer.nextToken();
                    }

                    MySqlAlterTableChangeColumn item = new MySqlAlterTableChangeColumn();
                    item.setColumnName(this.exprParser.name());
                    item.setNewColumnDefinition(this.exprParser.parseColumn());
                    if (this.identifierEquals("AFTER")) {
                        this.lexer.nextToken();
                        item.setAfterColumn(this.exprParser.name());
                    } else if (this.identifierEquals("FIRST")) {
                        this.lexer.nextToken();
                        if (this.lexer.token() == Token.IDENTIFIER) {
                            item.setFirstColumn(this.exprParser.name());
                        } else {
                            item.setFirst(true);
                        }
                    }

                    stmt.addItem(item);
                } else if (this.identifierEquals("MODIFY")) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.COLUMN) {
                        this.lexer.nextToken();
                    }

                    boolean paren = false;
                    if (this.lexer.token() == Token.LPAREN) {
                        paren = true;
                        this.lexer.nextToken();
                    }

                    while(true) {
                        MySqlAlterTableModifyColumn item = new MySqlAlterTableModifyColumn();
                        item.setNewColumnDefinition(this.exprParser.parseColumn());
                        if (this.identifierEquals("AFTER")) {
                            this.lexer.nextToken();
                            item.setAfterColumn(this.exprParser.name());
                        } else if (this.identifierEquals("FIRST")) {
                            this.lexer.nextToken();
                            if (this.lexer.token() == Token.IDENTIFIER) {
                                item.setFirstColumn(this.exprParser.name());
                            } else {
                                item.setFirst(true);
                            }
                        }

                        stmt.addItem(item);
                        if (!paren || this.lexer.token() != Token.COMMA) {
                            if (paren) {
                                this.accept(Token.RPAREN);
                            }
                            break;
                        }

                        this.lexer.nextToken();
                    }
                } else if (this.lexer.token() == Token.DISABLE) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.CONSTRAINT) {
                        this.lexer.nextToken();
                        SQLAlterTableDisableConstraint item = new SQLAlterTableDisableConstraint();
                        item.setConstraintName(this.exprParser.name());
                        stmt.addItem(item);
                    } else {
                        this.acceptIdentifier("KEYS");
                        SQLAlterTableDisableKeys item = new SQLAlterTableDisableKeys();
                        stmt.addItem(item);
                    }
                } else if (this.lexer.token() == Token.ENABLE) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.CONSTRAINT) {
                        this.lexer.nextToken();
                        SQLAlterTableEnableConstraint item = new SQLAlterTableEnableConstraint();
                        item.setConstraintName(this.exprParser.name());
                        stmt.addItem(item);
                    } else {
                        this.acceptIdentifier("KEYS");
                        SQLAlterTableEnableKeys item = new SQLAlterTableEnableKeys();
                        stmt.addItem(item);
                    }
                } else {
                    if (this.identifierEquals("RENAME")) {
                        this.lexer.nextToken();
                        if (this.lexer.token() == Token.TO || this.lexer.token() == Token.AS) {
                            this.lexer.nextToken();
                        }

                        MySqlRenameTableStatement renameStmt = new MySqlRenameTableStatement();
                        Item item = new Item();
                        item.setName(stmt.getTableSource().getExpr());
                        item.setTo(this.exprParser.name());
                        renameStmt.addItem(item);
                        return renameStmt;
                    }

                    if (this.lexer.token() == Token.ORDER) {
                        throw new ParserException("TODO " + this.lexer.token() + " " + this.lexer.stringVal());
                    }

                    if (this.identifierEquals("CONVERT")) {
                        this.lexer.nextToken();
                        this.accept(Token.TO);
                        this.acceptIdentifier("CHARACTER");
                        this.accept(Token.SET);
                        SQLAlterTableConvertCharSet item = new SQLAlterTableConvertCharSet();
                        SQLExpr charset = this.exprParser.primary();
                        item.setCharset(charset);
                        if (this.identifierEquals("COLLATE")) {
                            this.lexer.nextToken();
                            SQLExpr collate = this.exprParser.primary();
                            item.setCollate(collate);
                        }

                        stmt.addItem(item);
                    } else {
                        if (this.lexer.token() == Token.DEFAULT) {
                            throw new ParserException("TODO " + this.lexer.token() + " " + this.lexer.stringVal());
                        }

                        if (this.identifierEquals("DISCARD")) {
                            this.lexer.nextToken();
                            if (this.lexer.token() == Token.PARTITION) {
                                this.lexer.nextToken();
                                SQLAlterTableDiscardPartition item = new SQLAlterTableDiscardPartition();
                                if (this.lexer.token() == Token.ALL) {
                                    this.lexer.nextToken();
                                    item.getPartitions().add(new SQLIdentifierExpr("ALL"));
                                } else {
                                    this.exprParser.names(item.getPartitions(), item);
                                }

                                stmt.addItem(item);
                            } else {
                                this.accept(Token.TABLESPACE);
                                MySqlAlterTableDiscardTablespace item = new MySqlAlterTableDiscardTablespace();
                                stmt.addItem(item);
                            }
                        } else if (this.lexer.token() == Token.CHECK) {
                            this.lexer.nextToken();
                            this.accept(Token.PARTITION);
                            SQLAlterTableCheckPartition item = new SQLAlterTableCheckPartition();
                            if (this.lexer.token() == Token.ALL) {
                                this.lexer.nextToken();
                                item.getPartitions().add(new SQLIdentifierExpr("ALL"));
                            } else {
                                this.exprParser.names(item.getPartitions(), item);
                            }

                            stmt.addItem(item);
                        } else if (this.identifierEquals("IMPORT")) {
                            this.lexer.nextToken();
                            if (this.lexer.token() == Token.PARTITION) {
                                this.lexer.nextToken();
                                SQLAlterTableImportPartition item = new SQLAlterTableImportPartition();
                                if (this.lexer.token() == Token.ALL) {
                                    this.lexer.nextToken();
                                    item.getPartitions().add(new SQLIdentifierExpr("ALL"));
                                } else {
                                    this.exprParser.names(item.getPartitions(), item);
                                }

                                stmt.addItem(item);
                            } else {
                                this.accept(Token.TABLESPACE);
                                MySqlAlterTableImportTablespace item = new MySqlAlterTableImportTablespace();
                                stmt.addItem(item);
                            }
                        } else if (this.lexer.token() == Token.ANALYZE) {
                            this.lexer.nextToken();
                            this.accept(Token.PARTITION);
                            SQLAlterTableAnalyzePartition item = new SQLAlterTableAnalyzePartition();
                            if (this.lexer.token() == Token.ALL) {
                                this.lexer.nextToken();
                                item.getPartitions().add(new SQLIdentifierExpr("ALL"));
                            } else {
                                this.exprParser.names(item.getPartitions(), item);
                            }

                            stmt.addItem(item);
                        } else {
                            if (this.identifierEquals("FORCE")) {
                                throw new ParserException("TODO " + this.lexer.token() + " " + this.lexer.stringVal());
                            }

                            if (this.identifierEquals("COALESCE")) {
                                this.lexer.nextToken();
                                this.accept(Token.PARTITION);
                                SQLAlterTableCoalescePartition item = new SQLAlterTableCoalescePartition();
                                SQLIntegerExpr countExpr = this.exprParser.integerExpr();
                                item.setCount(countExpr);
                                stmt.addItem(item);
                            } else if (this.identifierEquals("REORGANIZE")) {
                                this.lexer.nextToken();
                                this.accept(Token.PARTITION);
                                SQLAlterTableReOrganizePartition item = new SQLAlterTableReOrganizePartition();
                                this.exprParser.names(item.getNames(), item);
                                this.accept(Token.INTO);
                                this.accept(Token.LPAREN);

                                while(true) {
                                    partition = this.getExprParser().parsePartition();
                                    item.addPartition(partition);
                                    if (this.lexer.token() != Token.COMMA) {
                                        this.accept(Token.RPAREN);
                                        stmt.addItem(item);
                                        break;
                                    }

                                    this.lexer.nextToken();
                                }
                            } else {
                                if (this.identifierEquals("EXCHANGE")) {
                                    throw new ParserException("TODO " + this.lexer.token() + " " + this.lexer.stringVal());
                                }

                                if (this.lexer.token() == Token.OPTIMIZE) {
                                    this.lexer.nextToken();
                                    this.accept(Token.PARTITION);
                                    SQLAlterTableOptimizePartition item = new SQLAlterTableOptimizePartition();
                                    if (this.lexer.token() == Token.ALL) {
                                        this.lexer.nextToken();
                                        item.getPartitions().add(new SQLIdentifierExpr("ALL"));
                                    } else {
                                        this.exprParser.names(item.getPartitions(), item);
                                    }

                                    stmt.addItem(item);
                                } else if (this.identifierEquals("REBUILD")) {
                                    this.lexer.nextToken();
                                    this.accept(Token.PARTITION);
                                    SQLAlterTableRebuildPartition item = new SQLAlterTableRebuildPartition();
                                    if (this.lexer.token() == Token.ALL) {
                                        this.lexer.nextToken();
                                        item.getPartitions().add(new SQLIdentifierExpr("ALL"));
                                    } else {
                                        this.exprParser.names(item.getPartitions(), item);
                                    }

                                    stmt.addItem(item);
                                } else if (this.identifierEquals("REPAIR")) {
                                    this.lexer.nextToken();
                                    this.accept(Token.PARTITION);
                                    SQLAlterTableRepairPartition item = new SQLAlterTableRepairPartition();
                                    if (this.lexer.token() == Token.ALL) {
                                        this.lexer.nextToken();
                                        item.getPartitions().add(new SQLIdentifierExpr("ALL"));
                                    } else {
                                        this.exprParser.names(item.getPartitions(), item);
                                    }

                                    stmt.addItem(item);
                                } else if (this.identifierEquals("REMOVE")) {
                                    this.lexer.nextToken();
                                    this.acceptIdentifier("PARTITIONING");
                                    stmt.setRemovePatiting(true);
                                } else if (this.identifierEquals("UPGRADE")) {
                                    this.lexer.nextToken();
                                    this.acceptIdentifier("PARTITIONING");
                                    stmt.setUpgradePatiting(true);
                                } else if (this.identifierEquals("ALGORITHM")) {
                                    this.lexer.nextToken();
                                    this.accept(Token.EQ);
                                    stmt.addItem(new MySqlAlterTableOption("ALGORITHM", this.lexer.stringVal()));
                                    this.lexer.nextToken();
                                } else if (this.identifierEquals("ENGINE")) {
                                    this.lexer.nextToken();
                                    this.accept(Token.EQ);
                                    stmt.addItem(new MySqlAlterTableOption("ENGINE", this.lexer.stringVal()));
                                    this.lexer.nextToken();
                                } else if (this.identifierEquals("AUTO_INCREMENT")) {
                                    this.lexer.nextToken();
                                    this.accept(Token.EQ);
                                    stmt.addItem(new MySqlAlterTableOption("AUTO_INCREMENT", this.lexer.integerValue()));
                                    this.lexer.nextToken();
                                } else if (this.identifierEquals("COLLATE")) {
                                    this.lexer.nextToken();
                                    this.accept(Token.EQ);
                                    stmt.addItem(new MySqlAlterTableOption("COLLATE", this.lexer.stringVal()));
                                    this.lexer.nextToken();
                                } else if (this.identifierEquals("PACK_KEYS")) {
                                    this.lexer.nextToken();
                                    this.accept(Token.EQ);
                                    if (this.identifierEquals("PACK")) {
                                        this.lexer.nextToken();
                                        this.accept(Token.ALL);
                                        stmt.addItem(new MySqlAlterTableOption("PACK_KEYS", "PACK ALL"));
                                    } else {
                                        stmt.addItem(new MySqlAlterTableOption("PACK_KEYS", this.lexer.stringVal()));
                                        this.lexer.nextToken();
                                    }
                                } else if (this.identifierEquals("CHARACTER")) {
                                    this.lexer.nextToken();
                                    this.accept(Token.SET);
                                    this.accept(Token.EQ);
                                    MySqlAlterTableCharacter item = new MySqlAlterTableCharacter();
                                    item.setCharacterSet(this.exprParser.primary());
                                    if (this.lexer.token() == Token.COMMA) {
                                        this.lexer.nextToken();
                                        this.acceptIdentifier("COLLATE");
                                        this.accept(Token.EQ);
                                        item.setCollate(this.exprParser.primary());
                                    }

                                    stmt.addItem(item);
                                } else if (this.lexer.token() == Token.COMMENT) {
                                    this.lexer.nextToken();
                                    if (this.lexer.token() == Token.EQ) {
                                        this.accept(Token.EQ);
                                    }

                                    stmt.addItem(new MySqlAlterTableOption("COMMENT", '\'' + this.lexer.stringVal() + '\''));
                                    this.lexer.nextToken();
                                } else if (this.lexer.token() == Token.UNION) {
                                    this.lexer.nextToken();
                                    if (this.lexer.token() == Token.EQ) {
                                        this.lexer.nextToken();
                                    }

                                    this.accept(Token.LPAREN);
                                    SQLTableSource tableSrc = this.createSQLSelectParser().parseTableSource();
                                    stmt.getTableOptions().put("UNION", tableSrc);
                                    this.accept(Token.RPAREN);
                                } else {
                                    if (!this.identifierEquals("ROW_FORMAT")) {
                                        break;
                                    }

                                    this.lexer.nextToken();
                                    if (this.lexer.token() == Token.EQ) {
                                        this.lexer.nextToken();
                                    }

                                    if (this.lexer.token() != Token.DEFAULT && this.lexer.token() != Token.IDENTIFIER) {
                                        throw new ParserException("illegal syntax.");
                                    }

                                    SQLIdentifierExpr rowFormat = new SQLIdentifierExpr(this.lexer.stringVal());
                                    this.lexer.nextToken();
                                    stmt.getTableOptions().put("ROW_FORMAT", rowFormat);
                                }
                            }
                        }
                    }
                }
            }

            if (this.lexer.token() != Token.COMMA) {
                break;
            }

            this.lexer.nextToken();
        }

        return stmt;
    }

    protected void parseAlterTableAddColumn(SQLAlterTableStatement stmt) {
        boolean parenFlag = false;
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            parenFlag = true;
        }

        SQLAlterTableAddColumn item = new SQLAlterTableAddColumn();

        while(true) {
            SQLColumnDefinition columnDef = this.exprParser.parseColumn();
            item.addColumn(columnDef);
            if (this.identifierEquals("AFTER")) {
                this.lexer.nextToken();
                item.setAfterColumn(this.exprParser.name());
            } else if (this.identifierEquals("FIRST")) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.IDENTIFIER) {
                    item.setFirstColumn(this.exprParser.name());
                } else {
                    item.setFirst(true);
                }
            }

            if (!parenFlag || this.lexer.token() != Token.COMMA) {
                stmt.addItem(item);
                if (parenFlag) {
                    this.accept(Token.RPAREN);
                }

                return;
            }

            this.lexer.nextToken();
        }
    }

    public void parseAlterDrop(SQLAlterTableStatement stmt) {
        this.lexer.nextToken();
        SQLName keyName;
        if (this.lexer.token() == Token.INDEX) {
            this.lexer.nextToken();
            keyName = this.exprParser.name();
            SQLAlterTableDropIndex item = new SQLAlterTableDropIndex();
            item.setIndexName(keyName);
            stmt.addItem(item);
        } else if (this.lexer.token() == Token.FOREIGN) {
            this.lexer.nextToken();
            this.accept(Token.KEY);
            keyName = this.exprParser.name();
            SQLAlterTableDropForeignKey item = new SQLAlterTableDropForeignKey();
            item.setIndexName(keyName);
            stmt.addItem(item);
        } else if (this.lexer.token() == Token.KEY) {
            this.lexer.nextToken();
            keyName = this.exprParser.name();
            SQLAlterTableDropKey item = new SQLAlterTableDropKey();
            item.setKeyName(keyName);
            stmt.addItem(item);
        } else if (this.lexer.token() == Token.PRIMARY) {
            this.lexer.nextToken();
            this.accept(Token.KEY);
            SQLAlterTableDropPrimaryKey item = new SQLAlterTableDropPrimaryKey();
            stmt.addItem(item);
        } else if (this.lexer.token() == Token.CONSTRAINT) {
            this.lexer.nextToken();
            SQLAlterTableDropConstraint item = new SQLAlterTableDropConstraint();
            item.setConstraintName(this.exprParser.name());
            stmt.addItem(item);
        } else {
            SQLAlterTableDropColumnItem item;
            if (this.lexer.token() == Token.COLUMN) {
                this.lexer.nextToken();
                item = new SQLAlterTableDropColumnItem();
                SQLName name = this.exprParser.name();
                name.setParent(item);
                item.addColumn(name);

                while(this.lexer.token() == Token.COMMA) {
                    this.lexer.mark();
                    this.lexer.nextToken();
                    if (this.identifierEquals("CHANGE")) {
                        this.lexer.reset();
                        break;
                    }

                    if (this.lexer.token() != Token.IDENTIFIER) {
                        this.lexer.reset();
                        break;
                    }

                    name = this.exprParser.name();
                    name.setParent(item);
                }

                stmt.addItem(item);
            } else if (this.lexer.token() == Token.PARTITION) {
                SQLAlterTableDropPartition dropPartition = this.parseAlterTableDropPartition(false);
                stmt.addItem(dropPartition);
            } else if (this.lexer.token() == Token.IDENTIFIER) {
                item = new SQLAlterTableDropColumnItem();
                this.exprParser.names(item.getColumns());
                stmt.addItem(item);
            } else {
                super.parseAlterDrop(stmt);
            }
        }

    }

    public SQLStatement parseRename() {
        MySqlRenameTableStatement stmt = new MySqlRenameTableStatement();
        this.acceptIdentifier("RENAME");
        this.accept(Token.TABLE);

        while(true) {
            Item item = new Item();
            item.setName(this.exprParser.name());
            this.accept(Token.TO);
            item.setTo(this.exprParser.name());
            stmt.addItem(item);
            if (this.lexer.token() != Token.COMMA) {
                return stmt;
            }

            this.lexer.nextToken();
        }
    }

    public SQLStatement parseCreateDatabase() {
        if (this.lexer.token() == Token.CREATE) {
            this.lexer.nextToken();
        }

        this.accept(Token.DATABASE);
        SQLCreateDatabaseStatement stmt = new SQLCreateDatabaseStatement("mysql");
        if (this.lexer.token() == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.NOT);
            this.accept(Token.EXISTS);
            stmt.setIfNotExists(true);
        }

        stmt.setName(this.exprParser.name());
        if (this.lexer.token() == Token.DEFAULT) {
            this.lexer.nextToken();
        }

        if (this.lexer.token() == Token.HINT) {
            stmt.setHints(this.exprParser.parseHints());
        }

        if (this.lexer.token() == Token.DEFAULT) {
            this.lexer.nextToken();
        }

        String collate;
        if (this.identifierEquals("CHARACTER")) {
            this.lexer.nextToken();
            this.accept(Token.SET);
            collate = this.lexer.stringVal();
            this.accept(Token.IDENTIFIER);
            stmt.setCharacterSet(collate);
        } else if (this.identifierEquals("CHARSET")) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.EQ) {
                this.lexer.nextToken();
            }

            collate = this.lexer.stringVal();
            this.accept(Token.IDENTIFIER);
            stmt.setCharacterSet(collate);
        }

        if (this.lexer.token() == Token.DEFAULT) {
            this.lexer.nextToken();
        }

        if (this.identifierEquals("COLLATE")) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.EQ) {
                this.lexer.nextToken();
            }

            collate = this.lexer.stringVal();
            this.accept(Token.IDENTIFIER);
            stmt.setCollate(collate);
        }

        return stmt;
    }

    protected void parseUpdateSet(SQLUpdateStatement update) {
        this.accept(Token.SET);

        while(true) {
            SQLUpdateSetItem item = this.exprParser.parseUpdateSetItem();
            update.addItem(item);
            if (this.lexer.token() != Token.COMMA) {
                return;
            }

            this.lexer.nextToken();
        }
    }

    public SQLStatement parseAlterDatabase() {
        this.accept(Token.DATABASE);
        SQLAlterDatabaseStatement stmt = new SQLAlterDatabaseStatement();
        SQLName name = this.exprParser.name();
        stmt.setName(name);
        if (this.identifierEquals("UPGRADE")) {
            this.lexer.nextToken();
            this.acceptIdentifier("DATA");
            this.acceptIdentifier("DIRECTORY");
            this.acceptIdentifier("NAME");
            stmt.setUpgradeDataDirectoryName(true);
        }

        return stmt;
    }

    public MySqlAlterUserStatement parseAlterUser() {
        this.accept(Token.USER);
        MySqlAlterUserStatement stmt = new MySqlAlterUserStatement();

        while(true) {
            SQLExpr user = this.exprParser.expr();
            this.acceptIdentifier("PASSWORD");
            this.acceptIdentifier("EXPIRE");
            stmt.addUser(user);
            if (this.lexer.token() != Token.COMMA) {
                return stmt;
            }

            this.lexer.nextToken();
        }
    }

    public MySqlExprParser getExprParser() {
        return (MySqlExprParser)this.exprParser;
    }

    public SQLCreateProcedureStatement parseCreateProcedure() {
        SQLCreateProcedureStatement stmt = new SQLCreateProcedureStatement();
        if (this.identifierEquals("DEFINER")) {
            this.lexer.nextToken();
            this.accept(Token.EQ);
            SQLName definer = this.exprParser.name();
            stmt.setDefiner(definer);
        } else {
            this.accept(Token.CREATE);
            if (this.lexer.token() == Token.OR) {
                this.lexer.nextToken();
                this.accept(Token.REPLACE);
                stmt.setOrReplace(true);
            }
        }

        this.accept(Token.PROCEDURE);
        stmt.setName(this.exprParser.name());
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            this.parserParameters(stmt.getParameters());
            this.accept(Token.RPAREN);
        }

        SQLBlockStatement block = this.parseBlock();
        stmt.setBlock(block);
        return stmt;
    }

    private void parserParameters(List<SQLParameter> parameters) {
        if (this.lexer.token() != Token.RPAREN) {
            do {
                SQLParameter parameter = new SQLParameter();
                if (this.lexer.token() == Token.CURSOR) {
                    this.lexer.nextToken();
                    parameter.setName(this.exprParser.name());
                    this.accept(Token.IS);
                    SQLSelect select = this.createSQLSelectParser().select();
                    SQLDataTypeImpl dataType = new SQLDataTypeImpl();
                    dataType.setName("CURSOR");
                    parameter.setDataType(dataType);
                    parameter.setDefaultValue(new SQLQueryExpr(select));
                } else if (this.lexer.token() != Token.IN && this.lexer.token() != Token.OUT && this.lexer.token() != Token.INOUT) {
                    parameter.setParamType(ParameterType.DEFAULT);
                    parameter.setName(this.exprParser.name());
                    parameter.setDataType(this.exprParser.parseDataType());
                    if (this.lexer.token() == Token.COLONEQ) {
                        this.lexer.nextToken();
                        parameter.setDefaultValue(this.exprParser.expr());
                    }
                } else {
                    if (this.lexer.token() == Token.IN) {
                        parameter.setParamType(ParameterType.IN);
                    } else if (this.lexer.token() == Token.OUT) {
                        parameter.setParamType(ParameterType.OUT);
                    } else if (this.lexer.token() == Token.INOUT) {
                        parameter.setParamType(ParameterType.INOUT);
                    }

                    this.lexer.nextToken();
                    parameter.setName(this.exprParser.name());
                    parameter.setDataType(this.exprParser.parseDataType());
                }

                parameters.add(parameter);
                if (this.lexer.token() == Token.COMMA || this.lexer.token() == Token.SEMI) {
                    this.lexer.nextToken();
                }
            } while(this.lexer.token() != Token.BEGIN && this.lexer.token() != Token.RPAREN);

        }
    }

    private void parseProcedureStatementList(List<SQLStatement> statementList) {
        this.parseProcedureStatementList(statementList, -1);
    }

    private void parseProcedureStatementList(List<SQLStatement> statementList, int max) {
        while(max == -1 || statementList.size() < max) {
            if (this.lexer.token() == Token.EOF) {
                return;
            }

            if (this.lexer.token() == Token.END) {
                return;
            }

            if (this.lexer.token() == Token.ELSE) {
                return;
            }

            if (this.lexer.token() == Token.SEMI) {
                this.lexer.nextToken();
            } else {
                if (this.lexer.token() == Token.WHEN) {
                    return;
                }

                if (this.lexer.token() == Token.UNTIL) {
                    return;
                }

                if (this.lexer.token() == Token.SELECT) {
                    statementList.add(this.parseSelectInto());
                } else if (this.lexer.token() == Token.UPDATE) {
                    statementList.add(this.parseUpdateStatement());
                } else if (this.lexer.token() == Token.CREATE) {
                    statementList.add(this.parseCreate());
                } else if (this.lexer.token() == Token.INSERT) {
                    statementList.add(this.parseInsert());
                } else if (this.lexer.token() == Token.DELETE) {
                    statementList.add(this.parseDeleteStatement());
                } else if (this.lexer.token() != Token.LBRACE && !this.identifierEquals("CALL")) {
                    if (this.lexer.token() == Token.BEGIN) {
                        statementList.add(this.parseBlock());
                    } else if (this.lexer.token() == Token.VARIANT) {
                        SQLExpr variant = this.exprParser.primary();
                        SQLSetStatement stmt;
                        if (variant instanceof SQLBinaryOpExpr) {
                            SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr)variant;
                            if (binaryOpExpr.getOperator() == SQLBinaryOperator.Assignment) {
                                stmt = new SQLSetStatement(binaryOpExpr.getLeft(), binaryOpExpr.getRight(), this.getDbType());
                                statementList.add(stmt);
                                continue;
                            }
                        }

                        this.accept(Token.COLONEQ);
                        SQLExpr value = this.exprParser.expr();
                        stmt = new SQLSetStatement(variant, value, this.getDbType());
                        statementList.add(stmt);
                    } else {
                        char markChar;
                        int markBp;
                        if (this.lexer.token() == Token.LPAREN) {
                            markChar = this.lexer.current();
                            markBp = this.lexer.bp();
                            this.lexer.nextToken();
                            if (this.lexer.token() != Token.SELECT) {
                                throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
                            }

                            this.lexer.reset(markBp, markChar, Token.LPAREN);
                            statementList.add(this.parseSelect());
                        } else if (this.lexer.token() == Token.SET) {
                            statementList.add(this.parseAssign());
                        } else if (this.lexer.token() == Token.WHILE) {
                            statementList.add(this.parseWhile());
                        } else if (this.lexer.token() == Token.LOOP) {
                            statementList.add(this.parseLoop());
                        } else if (this.lexer.token() == Token.IF) {
                            statementList.add(this.parseIf());
                        } else if (this.lexer.token() == Token.CASE) {
                            statementList.add(this.parseCase());
                        } else if (this.lexer.token() == Token.DECLARE) {
                            markChar = this.lexer.current();
                            markBp = this.lexer.bp();
                            this.lexer.nextToken();
                            this.lexer.nextToken();
                            if (this.lexer.token() == Token.CURSOR) {
                                this.lexer.reset(markBp, markChar, Token.DECLARE);
                                statementList.add(this.parseCursorDeclare());
                            } else if (this.identifierEquals("HANDLER")) {
                                this.lexer.reset(markBp, markChar, Token.DECLARE);
                                statementList.add(this.parseDeclareHandler());
                            } else if (this.lexer.token() == Token.CONDITION) {
                                this.lexer.reset(markBp, markChar, Token.DECLARE);
                                statementList.add(this.parseDeclareCondition());
                            } else {
                                this.lexer.reset(markBp, markChar, Token.DECLARE);
                                statementList.add(this.parseDeclare());
                            }
                        } else if (this.lexer.token() == Token.LEAVE) {
                            statementList.add(this.parseLeave());
                        } else if (this.lexer.token() == Token.ITERATE) {
                            statementList.add(this.parseIterate());
                        } else if (this.lexer.token() == Token.REPEAT) {
                            statementList.add(this.parseRepeat());
                        } else if (this.lexer.token() == Token.OPEN) {
                            statementList.add(this.parseOpen());
                        } else if (this.lexer.token() == Token.CLOSE) {
                            statementList.add(this.parseClose());
                        } else if (this.lexer.token() == Token.FETCH) {
                            statementList.add(this.parseFetch());
                        } else {
                            if (this.lexer.token() == Token.IDENTIFIER) {
                                String label = this.lexer.stringVal();
                                char ch = this.lexer.current();
                                int bp = this.lexer.bp();
                                this.lexer.nextToken();
                                if (this.lexer.token() == Token.VARIANT && this.lexer.stringVal().equals(":")) {
                                    this.lexer.nextToken();
                                    if (this.lexer.token() == Token.LOOP) {
                                        statementList.add(this.parseLoop(label));
                                        continue;
                                    }

                                    if (this.lexer.token() == Token.WHILE) {
                                        statementList.add(this.parseWhile(label));
                                        continue;
                                    }

                                    if (this.lexer.token() == Token.BEGIN) {
                                        statementList.add(this.parseBlock(label));
                                        continue;
                                    }

                                    if (this.lexer.token() == Token.REPEAT) {
                                        statementList.add(this.parseRepeat(label));
                                    }
                                    continue;
                                }

                                this.lexer.reset(bp, ch, Token.IDENTIFIER);
                            }

                            throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
                        }
                    }
                } else {
                    statementList.add(this.parseCall());
                }
            }
        }

    }

    public SQLIfStatement parseIf() {
        this.accept(Token.IF);
        SQLIfStatement stmt = new SQLIfStatement();
        stmt.setCondition(this.exprParser.expr());
        this.accept(Token.THEN);
        this.parseProcedureStatementList(stmt.getStatements());

        while(this.lexer.token() == Token.ELSE) {
            this.lexer.nextToken();
            if (this.lexer.token() != Token.IF) {
                Else elseItem = new Else();
                this.parseProcedureStatementList(elseItem.getStatements());
                stmt.setElseItem(elseItem);
                break;
            }

            this.lexer.nextToken();
            ElseIf elseIf = new ElseIf();
            elseIf.setCondition(this.exprParser.expr());
            this.accept(Token.THEN);
            this.parseProcedureStatementList(elseIf.getStatements());
            stmt.getElseIfList().add(elseIf);
        }

        this.accept(Token.END);
        this.accept(Token.IF);
        this.accept(Token.SEMI);
        return stmt;
    }

    public MySqlWhileStatement parseWhile() {
        this.accept(Token.WHILE);
        MySqlWhileStatement stmt = new MySqlWhileStatement();
        stmt.setCondition(this.exprParser.expr());
        this.accept(Token.DO);
        this.parseProcedureStatementList(stmt.getStatements());
        this.accept(Token.END);
        this.accept(Token.WHILE);
        this.accept(Token.SEMI);
        return stmt;
    }

    public MySqlWhileStatement parseWhile(String label) {
        this.accept(Token.WHILE);
        MySqlWhileStatement stmt = new MySqlWhileStatement();
        stmt.setLabelName(label);
        stmt.setCondition(this.exprParser.expr());
        this.accept(Token.DO);
        this.parseProcedureStatementList(stmt.getStatements());
        this.accept(Token.END);
        this.accept(Token.WHILE);
        this.acceptIdentifier(label);
        this.accept(Token.SEMI);
        return stmt;
    }

    public MySqlCaseStatement parseCase() {
        MySqlCaseStatement stmt = new MySqlCaseStatement();
        this.accept(Token.CASE);
        MySqlWhenStatement when;
        Else elseStmt;
        if (this.lexer.token() != Token.WHEN) {
            stmt.setCondition(this.exprParser.expr());

            while(this.lexer.token() == Token.WHEN) {
                this.accept(Token.WHEN);
                when = new MySqlWhenStatement();
                when.setCondition(this.exprParser.expr());
                this.accept(Token.THEN);
                this.parseProcedureStatementList(when.getStatements());
                stmt.addWhenStatement(when);
            }

            if (this.lexer.token() == Token.ELSE) {
                this.accept(Token.ELSE);
                elseStmt = new Else();
                this.parseProcedureStatementList(elseStmt.getStatements());
                stmt.setElseItem(elseStmt);
            }
        } else {
            while(true) {
                if (this.lexer.token() != Token.WHEN) {
                    if (this.lexer.token() == Token.ELSE) {
                        elseStmt = new Else();
                        this.parseProcedureStatementList(elseStmt.getStatements());
                        stmt.setElseItem(elseStmt);
                    }
                    break;
                }

                when = new MySqlWhenStatement();
                when.setCondition(this.exprParser.expr());
                this.accept(Token.THEN);
                this.parseProcedureStatementList(when.getStatements());
                stmt.addWhenStatement(when);
            }
        }

        this.accept(Token.END);
        this.accept(Token.CASE);
        this.accept(Token.SEMI);
        return stmt;
    }

    public MySqlDeclareStatement parseDeclare() {
        MySqlDeclareStatement stmt = new MySqlDeclareStatement();
        this.accept(Token.DECLARE);

        while(true) {
            SQLDeclareItem item = new SQLDeclareItem();
            item.setName(this.exprParser.name());
            stmt.addVar(item);
            if (this.lexer.token() != Token.COMMA) {
                if (this.lexer.token() != Token.EOF) {
                    item.setDataType(this.exprParser.parseDataType());
                    if (this.lexer.token() == Token.DEFAULT) {
                        this.lexer.nextToken();
                        SQLExpr defaultValue = this.exprParser.primary();
                        item.setValue(defaultValue);
                    }

                    return stmt;
                } else {
                    throw new ParserException("TODO");
                }
            }

            this.accept(Token.COMMA);
        }
    }

    public SQLSetStatement parseAssign() {
        this.accept(Token.SET);
        SQLSetStatement stmt = new SQLSetStatement(this.getDbType());
        this.parseAssignItems(stmt.getItems(), stmt);
        return stmt;
    }

    public MySqlSelectIntoStatement parseSelectInto() {
        MySqlSelectIntoParser parse = new MySqlSelectIntoParser(this.exprParser);
        return parse.parseSelectInto();
    }

    public SQLLoopStatement parseLoop() {
        SQLLoopStatement loopStmt = new SQLLoopStatement();
        this.accept(Token.LOOP);
        this.parseProcedureStatementList(loopStmt.getStatements());
        this.accept(Token.END);
        this.accept(Token.LOOP);
        this.accept(Token.SEMI);
        return loopStmt;
    }

    public SQLLoopStatement parseLoop(String label) {
        SQLLoopStatement loopStmt = new SQLLoopStatement();
        loopStmt.setLabelName(label);
        this.accept(Token.LOOP);
        this.parseProcedureStatementList(loopStmt.getStatements());
        this.accept(Token.END);
        this.accept(Token.LOOP);
        this.acceptIdentifier(label);
        this.accept(Token.SEMI);
        return loopStmt;
    }

    public SQLBlockStatement parseBlock(String label) {
        SQLBlockStatement block = new SQLBlockStatement();
        block.setLabelName(label);
        this.accept(Token.BEGIN);
        this.parseProcedureStatementList(block.getStatementList());
        this.accept(Token.END);
        this.acceptIdentifier(label);
        return block;
    }

    public MySqlLeaveStatement parseLeave() {
        this.accept(Token.LEAVE);
        MySqlLeaveStatement leaveStmt = new MySqlLeaveStatement();
        leaveStmt.setLabelName(this.exprParser.name().getSimpleName());
        this.accept(Token.SEMI);
        return leaveStmt;
    }

    public MySqlIterateStatement parseIterate() {
        this.accept(Token.ITERATE);
        MySqlIterateStatement iterateStmt = new MySqlIterateStatement();
        iterateStmt.setLabelName(this.exprParser.name().getSimpleName());
        this.accept(Token.SEMI);
        return iterateStmt;
    }

    public MySqlRepeatStatement parseRepeat() {
        MySqlRepeatStatement repeatStmt = new MySqlRepeatStatement();
        this.accept(Token.REPEAT);
        this.parseProcedureStatementList(repeatStmt.getStatements());
        this.accept(Token.UNTIL);
        repeatStmt.setCondition(this.exprParser.expr());
        this.accept(Token.END);
        this.accept(Token.REPEAT);
        this.accept(Token.SEMI);
        return repeatStmt;
    }

    public MySqlRepeatStatement parseRepeat(String label) {
        MySqlRepeatStatement repeatStmt = new MySqlRepeatStatement();
        repeatStmt.setLabelName(label);
        this.accept(Token.REPEAT);
        this.parseProcedureStatementList(repeatStmt.getStatements());
        this.accept(Token.UNTIL);
        repeatStmt.setCondition(this.exprParser.expr());
        this.accept(Token.END);
        this.accept(Token.REPEAT);
        this.acceptIdentifier(label);
        this.accept(Token.SEMI);
        return repeatStmt;
    }

    public MySqlCursorDeclareStatement parseCursorDeclare() {
        MySqlCursorDeclareStatement stmt = new MySqlCursorDeclareStatement();
        this.accept(Token.DECLARE);
        stmt.setCursorName(this.exprParser.name().getSimpleName());
        this.accept(Token.CURSOR);
        this.accept(Token.FOR);
        SQLSelectStatement selelctStmt = (SQLSelectStatement)this.parseSelect();
        stmt.setSelect(selelctStmt);
        this.accept(Token.SEMI);
        return stmt;
    }

    public SQLStatement parseSpStatement() {
        if (this.lexer.token() == Token.UPDATE) {
            return this.parseUpdateStatement();
        } else if (this.lexer.token() == Token.CREATE) {
            return this.parseCreate();
        } else if (this.lexer.token() == Token.INSERT) {
            return this.parseInsert();
        } else if (this.lexer.token() == Token.DELETE) {
            return this.parseDeleteStatement();
        } else if (this.lexer.token() == Token.BEGIN) {
            return this.parseBlock();
        } else if (this.lexer.token() == Token.LPAREN) {
            char ch = this.lexer.current();
            int bp = this.lexer.bp();
            this.lexer.nextToken();
            if (this.lexer.token() == Token.SELECT) {
                this.lexer.reset(bp, ch, Token.LPAREN);
                return this.parseSelect();
            } else {
                throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
            }
        } else if (this.lexer.token() == Token.SET) {
            return this.parseAssign();
        } else {
            throw new ParserException("error sp_statement");
        }
    }

    public MySqlDeclareHandlerStatement parseDeclareHandler() {
        MySqlDeclareHandlerStatement stmt = new MySqlDeclareHandlerStatement();
        this.accept(Token.DECLARE);
        if (this.lexer.token() == Token.CONTINUE) {
            stmt.setHandleType(MySqlHandlerType.CONTINUE);
        } else if (this.lexer.token() == Token.EXIT) {
            stmt.setHandleType(MySqlHandlerType.CONTINUE);
        } else {
            if (this.lexer.token() != Token.UNDO) {
                throw new ParserException("unkown handle type");
            }

            stmt.setHandleType(MySqlHandlerType.CONTINUE);
        }

        this.lexer.nextToken();
        this.acceptIdentifier("HANDLER");
        this.accept(Token.FOR);

        while(true) {
            String tokenName = this.lexer.stringVal();
            ConditionValue condition = new ConditionValue();
            if (tokenName.equalsIgnoreCase("NOT")) {
                this.lexer.nextToken();
                this.acceptIdentifier("HANDLE");
                condition.setType(ConditionType.SYSTEM);
                condition.setValue("NOT FOUND");
            } else if (tokenName.equalsIgnoreCase("SQLSTATE")) {
                condition.setType(ConditionType.SQLSTATE);
                this.lexer.nextToken();
                condition.setValue(this.exprParser.name().toString());
            } else if (this.identifierEquals("SQLEXCEPTION")) {
                condition.setType(ConditionType.SYSTEM);
                condition.setValue(this.lexer.stringVal());
                this.lexer.nextToken();
            } else if (this.identifierEquals("SQLWARNING")) {
                condition.setType(ConditionType.SYSTEM);
                condition.setValue(this.lexer.stringVal());
                this.lexer.nextToken();
            } else {
                if (this.lexer.token() == Token.LITERAL_INT) {
                    condition.setType(ConditionType.MYSQL_ERROR_CODE);
                    condition.setValue(this.lexer.integerValue().toString());
                } else {
                    condition.setType(ConditionType.SELF);
                    condition.setValue(tokenName);
                }

                this.lexer.nextToken();
            }

            stmt.getConditionValues().add(condition);
            if (this.lexer.token() != Token.COMMA) {
                if (this.lexer.token() != Token.EOF) {
                    stmt.setSpStatement(this.parseSpStatement());
                    if (!(stmt.getSpStatement() instanceof SQLBlockStatement)) {
                        this.accept(Token.SEMI);
                    }

                    return stmt;
                }

                throw new ParserException("declare handle not eof");
            }

            this.accept(Token.COMMA);
        }
    }

    public MySqlDeclareConditionStatement parseDeclareCondition() {
        MySqlDeclareConditionStatement stmt = new MySqlDeclareConditionStatement();
        this.accept(Token.DECLARE);
        stmt.setConditionName(this.exprParser.name().toString());
        this.accept(Token.CONDITION);
        this.accept(Token.FOR);
        String tokenName = this.lexer.stringVal();
        ConditionValue condition = new ConditionValue();
        if (tokenName.equalsIgnoreCase("SQLSTATE")) {
            condition.setType(ConditionType.SQLSTATE);
            this.lexer.nextToken();
            condition.setValue(this.exprParser.name().toString());
        } else {
            if (this.lexer.token() != Token.LITERAL_INT) {
                throw new ParserException("declare condition grammer error.");
            }

            condition.setType(ConditionType.MYSQL_ERROR_CODE);
            condition.setValue(this.lexer.integerValue().toString());
            this.lexer.nextToken();
        }

        stmt.setConditionValue(condition);
        this.accept(Token.SEMI);
        return stmt;
    }
}
