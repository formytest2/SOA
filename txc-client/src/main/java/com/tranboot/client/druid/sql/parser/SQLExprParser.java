package com.tranboot.client.druid.sql.parser;

import com.tranboot.client.druid.sql.ast.SQLCommentHint;
import com.tranboot.client.druid.sql.ast.SQLDataType;
import com.tranboot.client.druid.sql.ast.SQLDataTypeImpl;
import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLLimit;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLOrderBy;
import com.tranboot.client.druid.sql.ast.SQLOrderingSpecification;
import com.tranboot.client.druid.sql.ast.SQLOver;
import com.tranboot.client.druid.sql.ast.SQLPartitionValue;
import com.tranboot.client.druid.sql.ast.SQLOver.WindowingType;
import com.tranboot.client.druid.sql.ast.SQLPartitionValue.Operator;
import com.tranboot.client.druid.sql.ast.expr.SQLAggregateExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLAggregateOption;
import com.tranboot.client.druid.sql.ast.expr.SQLAllColumnExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLAllExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLAnyExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBetweenExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOperator;
import com.tranboot.client.druid.sql.ast.expr.SQLBooleanExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCaseExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCastExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCharExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCurrentOfCursorExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLDateExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLDefaultExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLExistsExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLGroupingSetExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLHexExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLInListExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLInSubQueryExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLIntegerExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLListExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNCharExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNotExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNullExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNumberExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLPropertyExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLQueryExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLSequenceExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLSomeExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLUnaryExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLUnaryOperator;
import com.tranboot.client.druid.sql.ast.expr.SQLVariantRefExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCaseExpr.Item;
import com.tranboot.client.druid.sql.ast.expr.SQLSequenceExpr.Function;
import com.tranboot.client.druid.sql.ast.statement.SQLAssignItem;
import com.tranboot.client.druid.sql.ast.statement.SQLCharacterDataType;
import com.tranboot.client.druid.sql.ast.statement.SQLCheck;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnCheck;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnDefinition;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnPrimaryKey;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnReference;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnUniqueKey;
import com.tranboot.client.druid.sql.ast.statement.SQLConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLForeignKeyConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.tranboot.client.druid.sql.ast.statement.SQLNotNullConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLNullConstraint;
import com.tranboot.client.druid.sql.ast.statement.SQLPrimaryKey;
import com.tranboot.client.druid.sql.ast.statement.SQLPrimaryKeyImpl;
import com.tranboot.client.druid.sql.ast.statement.SQLSelect;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectItem;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.tranboot.client.druid.sql.ast.statement.SQLUnique;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateSetItem;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectOrderByItem.NullsOrderType;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SQLExprParser extends SQLParser {
    public static final String[] AGGREGATE_FUNCTIONS = new String[]{"AVG", "COUNT", "MAX", "MIN", "STDDEV", "SUM"};
    protected String[] aggregateFunctions;

    public SQLExprParser(String sql) {
        super(sql);
        this.aggregateFunctions = AGGREGATE_FUNCTIONS;
    }

    public SQLExprParser(String sql, String dbType) {
        super(sql, dbType);
        this.aggregateFunctions = AGGREGATE_FUNCTIONS;
    }

    public SQLExprParser(Lexer lexer) {
        super(lexer);
        this.aggregateFunctions = AGGREGATE_FUNCTIONS;
    }

    public SQLExprParser(Lexer lexer, String dbType) {
        super(lexer, dbType);
        this.aggregateFunctions = AGGREGATE_FUNCTIONS;
    }

    public SQLExpr expr() {
        if (this.lexer.token() == Token.STAR) {
            this.lexer.nextToken();
            SQLExpr expr = new SQLAllColumnExpr();
            if (this.lexer.token() == Token.DOT) {
                this.lexer.nextToken();
                this.accept(Token.STAR);
                return new SQLPropertyExpr(expr, "*");
            } else {
                return expr;
            }
        } else {
            SQLExpr expr = this.primary();
            return this.lexer.token() == Token.COMMA ? expr : this.exprRest(expr);
        }
    }

    public SQLExpr exprRest(SQLExpr expr) {
        expr = this.bitXorRest(expr);
        expr = this.multiplicativeRest(expr);
        expr = this.additiveRest(expr);
        expr = this.shiftRest(expr);
        expr = this.bitAndRest(expr);
        expr = this.bitOrRest(expr);
        expr = this.inRest(expr);
        expr = this.relationalRest(expr);
        expr = this.equalityRest(expr);
        expr = this.andRest(expr);
        expr = this.orRest(expr);
        return expr;
    }

    public final SQLExpr bitXor() {
        SQLExpr expr = this.primary();
        return this.bitXorRest(expr);
    }

    public SQLExpr bitXorRest(SQLExpr expr) {
        /*      */     SQLExpr sQLExpr = null;
        /*  159 */     Token token = this.lexer.token;
        /*  160 */     if (token == Token.CARET)
            /*  161 */     { this.lexer.nextToken();
            /*  162 */       SQLExpr rightExp = primary();
            /*  163 */       SQLBinaryOpExpr sQLBinaryOpExpr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.BitwiseXor, rightExp, getDbType());
            /*  164 */       sQLExpr = bitXorRest((SQLExpr)sQLBinaryOpExpr); }
        /*  165 */     else { SQLExpr sQLExpr1 = null; if (token == Token.SUBGT)
            /*  166 */       { this.lexer.nextToken();
            /*  167 */         SQLExpr rightExp = primary();
            /*  168 */         SQLBinaryOpExpr sQLBinaryOpExpr = new SQLBinaryOpExpr(sQLExpr, SQLBinaryOperator.SubGt, rightExp, getDbType());
            /*  169 */         sQLExpr1 = bitXorRest((SQLExpr)sQLBinaryOpExpr); }
        /*  170 */       else { SQLExpr sQLExpr2 = null; if (token == Token.SUBGTGT)
            /*  171 */         { this.lexer.nextToken();
            /*  172 */           SQLExpr rightExp = primary();
            /*  173 */           SQLBinaryOpExpr sQLBinaryOpExpr = new SQLBinaryOpExpr(sQLExpr1, SQLBinaryOperator.SubGtGt, rightExp, getDbType());
            /*  174 */           sQLExpr2 = bitXorRest((SQLExpr)sQLBinaryOpExpr); }
        /*  175 */         else { SQLExpr sQLExpr3 = null; if (token == Token.POUNDGT)
            /*  176 */           { this.lexer.nextToken();
            /*  177 */             SQLExpr rightExp = primary();
            /*  178 */             SQLBinaryOpExpr sQLBinaryOpExpr = new SQLBinaryOpExpr(sQLExpr2, SQLBinaryOperator.PoundGt, rightExp, getDbType());
            /*  179 */             sQLExpr3 = bitXorRest((SQLExpr)sQLBinaryOpExpr); }
        /*  180 */           else { SQLExpr sQLExpr4 = null; if (token == Token.POUNDGTGT)
            /*  181 */             { this.lexer.nextToken();
            /*  182 */               SQLExpr rightExp = primary();
            /*  183 */               SQLBinaryOpExpr sQLBinaryOpExpr = new SQLBinaryOpExpr(sQLExpr3, SQLBinaryOperator.PoundGtGt, rightExp, getDbType());
            /*  184 */               sQLExpr4 = bitXorRest((SQLExpr)sQLBinaryOpExpr); }
        /*  185 */             else { SQLExpr sQLExpr5 = null; if (token == Token.QUESQUES)
            /*  186 */               { this.lexer.nextToken();
            /*  187 */                 SQLExpr rightExp = primary();
            /*  188 */                 SQLBinaryOpExpr sQLBinaryOpExpr = new SQLBinaryOpExpr(sQLExpr4, SQLBinaryOperator.QuesQues, rightExp, getDbType());
            /*  189 */                 sQLExpr5 = bitXorRest((SQLExpr)sQLBinaryOpExpr); }
        /*  190 */               else { SQLExpr sQLExpr6 = null; if (token == Token.QUESBAR)
            /*  191 */                 { this.lexer.nextToken();
            /*  192 */                   SQLExpr rightExp = primary();
            /*  193 */                   SQLBinaryOpExpr sQLBinaryOpExpr = new SQLBinaryOpExpr(sQLExpr5, SQLBinaryOperator.QuesBar, rightExp, getDbType());
            /*  194 */                   sQLExpr6 = bitXorRest((SQLExpr)sQLBinaryOpExpr); }
        /*  195 */                 else if (token == Token.QUESAMP)
            /*  196 */                 { this.lexer.nextToken();
            /*  197 */                   SQLExpr rightExp = primary();
            /*  198 */                   SQLBinaryOpExpr sQLBinaryOpExpr = new SQLBinaryOpExpr(sQLExpr6, SQLBinaryOperator.QuesAmp, rightExp, getDbType());
            /*  199 */                   sQLExpr = bitXorRest((SQLExpr)sQLBinaryOpExpr); }  }  }  }  }
            /*      */          }
            /*      */        }
        /*  202 */      return sQLExpr;
        /*      */   }

    public final SQLExpr multiplicative() {
        SQLExpr expr = this.bitXor();
        return this.multiplicativeRest(expr);
    }

    /*      */   public SQLExpr multiplicativeRest(SQLExpr expr) {
        /*      */     SQLExpr sQLExpr = null;
        /*  211 */     Token token = this.lexer.token();
        /*  212 */     if (token == Token.STAR)
            /*  213 */     { this.lexer.nextToken();
            /*  214 */       SQLExpr rightExp = bitXor();
            /*  215 */       SQLBinaryOpExpr sQLBinaryOpExpr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Multiply, rightExp, getDbType());
            /*  216 */       sQLExpr = multiplicativeRest((SQLExpr)sQLBinaryOpExpr); }
        /*  217 */     else { SQLExpr sQLExpr1 = null; if (token == Token.SLASH) {
            /*  218 */         this.lexer.nextToken();
            /*  219 */         SQLExpr rightExp = bitXor();
            /*  220 */         SQLBinaryOpExpr sQLBinaryOpExpr = new SQLBinaryOpExpr(sQLExpr, SQLBinaryOperator.Divide, rightExp, getDbType());
            /*  221 */         sQLExpr1 = multiplicativeRest((SQLExpr)sQLBinaryOpExpr);
            /*  222 */       } else if (token == Token.PERCENT) {
            /*  223 */         this.lexer.nextToken();
            /*  224 */         SQLExpr rightExp = bitXor();
            /*  225 */         SQLBinaryOpExpr sQLBinaryOpExpr = new SQLBinaryOpExpr(sQLExpr1, SQLBinaryOperator.Modulus, rightExp, getDbType());
            /*  226 */         sQLExpr = multiplicativeRest((SQLExpr)sQLBinaryOpExpr);
            /*      */       }  }
        /*  228 */      return sQLExpr;
        /*      */   }

    public SQLIntegerExpr integerExpr() {
        SQLIntegerExpr intExpr = new SQLIntegerExpr(this.lexer.integerValue());
        this.accept(Token.LITERAL_INT);
        return intExpr;
    }

    public SQLExpr primary() {
        List<String> beforeComments = null;
        if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
            beforeComments = this.lexer.readAndResetComments();
        }

//        Object sqlExpr;
//        sqlExpr = null;
        Token tok = this.lexer.token();
        SQLExpr sqlExpr = null;
        label159:
        switch(tok) {
            case LITERAL_INT:
                sqlExpr = new SQLIntegerExpr(this.lexer.integerValue());
                this.lexer.nextToken();
                break;
            case LITERAL_FLOAT:
                sqlExpr = new SQLNumberExpr(this.lexer.decimalValue());
                this.lexer.nextToken();
                break;
            case IDENTIFIER:
                String ident = this.lexer.stringVal();
                this.lexer.nextToken();
                if (!"DATE".equalsIgnoreCase(ident) || this.lexer.token() != Token.LITERAL_CHARS || !"oracle".equals(this.getDbType()) && !"postgresql".equals(this.getDbType())) {
                    sqlExpr = new SQLIdentifierExpr(ident);
                } else {
                    String literal = this.lexer.stringVal();
                    this.lexer.nextToken();
                    SQLDateExpr dateExpr = new SQLDateExpr();
                    dateExpr.setLiteral(literal);
                    sqlExpr = dateExpr;
                }
                break;
            case QUES:
                this.lexer.nextToken();
                SQLVariantRefExpr quesVarRefExpr = new SQLVariantRefExpr("?");
                quesVarRefExpr.setIndex(this.lexer.nextVarIndex());
                sqlExpr = quesVarRefExpr;
                break;
            case LPAREN:
                this.lexer.nextToken();
                sqlExpr = this.expr();
                if (this.lexer.token() == Token.COMMA) {
                    SQLListExpr listExpr = new SQLListExpr();
                    listExpr.addItem((SQLExpr)sqlExpr);

                    do {
                        this.lexer.nextToken();
                        listExpr.addItem(this.expr());
                    } while(this.lexer.token() == Token.COMMA);

                    sqlExpr = listExpr;
                }

                this.accept(Token.RPAREN);
                break;
            case INSERT:
                this.lexer.nextToken();
                if (this.lexer.token() != Token.LPAREN) {
                    throw new ParserException("syntax error");
                }

                sqlExpr = new SQLIdentifierExpr("INSERT");
                break;
            case NEW:
                throw new ParserException("TODO");
            case LITERAL_CHARS:
                sqlExpr = new SQLCharExpr(this.lexer.stringVal());
                this.lexer.nextToken();
                break;
            case LITERAL_NCHARS:
                sqlExpr = new SQLNCharExpr(this.lexer.stringVal());
                this.lexer.nextToken();
                break;
            case VARIANT:
                SQLVariantRefExpr varRefExpr = new SQLVariantRefExpr(this.lexer.stringVal());
                this.lexer.nextToken();
                if (varRefExpr.getName().equals("@") && this.lexer.token() == Token.LITERAL_CHARS) {
                    varRefExpr.setName("@'" + this.lexer.stringVal() + "'");
                    this.lexer.nextToken();
                } else if (varRefExpr.getName().equals("@@") && this.lexer.token() == Token.LITERAL_CHARS) {
                    varRefExpr.setName("@@'" + this.lexer.stringVal() + "'");
                    this.lexer.nextToken();
                }

                sqlExpr = varRefExpr;
                break;
            case DEFAULT:
                sqlExpr = new SQLDefaultExpr();
                this.lexer.nextToken();
                break;
            case DUAL:
            case KEY:
            case DISTINCT:
            case LIMIT:
            case SCHEMA:
            case COLUMN:
            case IF:
            case END:
            case COMMENT:
            case COMPUTE:
            case ENABLE:
            case DISABLE:
            case INITIALLY:
            case SEQUENCE:
            case USER:
            case EXPLAIN:
            case WITH:
            case GRANT:
            case REPLACE:
            case INDEX:
            case MODEL:
            case PCTFREE:
            case INITRANS:
            case MAXTRANS:
            case SEGMENT:
            case CREATION:
            case IMMEDIATE:
            case DEFERRED:
            case STORAGE:
            case NEXT:
            case MINEXTENTS:
            case MAXEXTENTS:
            case MAXSIZE:
            case PCTINCREASE:
            case FLASH_CACHE:
            case CELL_FLASH_CACHE:
            case KEEP:
            case NONE:
            case LOB:
            case STORE:
            case ROW:
            case CHUNK:
            case CACHE:
            case NOCACHE:
            case LOGGING:
            case NOCOMPRESS:
            case KEEP_DUPLICATES:
            case EXCEPTIONS:
            case PURGE:
            case FULL:
            case TO:
            case IDENTIFIED:
            case PASSWORD:
            case BINARY:
            case WINDOW:
            case OFFSET:
            case SHARE:
            case START:
            case CONNECT:
            case MATCHED:
            case ERRORS:
            case REJECT:
            case UNLIMITED:
            case BEGIN:
            case EXCLUSIVE:
            case MODE:
            case ADVISE:
            case VIEW:
            case ESCAPE:
            case OVER:
            case ORDER:
            case CONSTRAINT:
            case TYPE:
            case OPEN:
            case REPEAT:
            case TABLE:
            case TRUNCATE:
            case EXCEPTION:
            case FUNCTION:
            case IDENTITY:
            case EXTRACT:
                sqlExpr = new SQLIdentifierExpr(this.lexer.stringVal());
                this.lexer.nextToken();
                break;
            case CASE:
                SQLCaseExpr caseExpr = new SQLCaseExpr();
                this.lexer.nextToken();
                if (this.lexer.token() != Token.WHEN) {
                    caseExpr.setValueExpr(this.expr());
                }

                this.accept(Token.WHEN);
                SQLExpr testExpr = this.expr();
                this.accept(Token.THEN);
                SQLExpr valueExpr = this.expr();
                Item caseItem = new Item(testExpr, valueExpr);
                caseExpr.addItem(caseItem);

                while(this.lexer.token() == Token.WHEN) {
                    this.lexer.nextToken();
                    testExpr = this.expr();
                    this.accept(Token.THEN);
                    valueExpr = this.expr();
                    caseItem = new Item(testExpr, valueExpr);
                    caseExpr.addItem(caseItem);
                }

                if (this.lexer.token() == Token.ELSE) {
                    this.lexer.nextToken();
                    caseExpr.setElseExpr(this.expr());
                }

                this.accept(Token.END);
                sqlExpr = caseExpr;
                break;
            case EXISTS:
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                sqlExpr = new SQLExistsExpr(this.createSelectParser().select());
                this.accept(Token.RPAREN);
                break;
            case NOT:
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EXISTS) {
                    this.lexer.nextToken();
                    this.accept(Token.LPAREN);
                    sqlExpr = new SQLExistsExpr(this.createSelectParser().select(), true);
                    this.accept(Token.RPAREN);
                } else {
                    SQLExpr restExpr;
                    if (this.lexer.token() == Token.LPAREN) {
                        this.lexer.nextToken();
                        restExpr = this.expr();
                        this.accept(Token.RPAREN);
                        restExpr = this.relationalRest(restExpr);
                        sqlExpr = new SQLNotExpr(restExpr);
                        return this.primaryRest(sqlExpr);
                    }

                    restExpr = this.relational();
                    sqlExpr = new SQLNotExpr(restExpr);
                }
                break;
            case SELECT:
                SQLQueryExpr queryExpr = new SQLQueryExpr(this.createSelectParser().select());
                sqlExpr = queryExpr;
                break;
            case CAST:
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                SQLCastExpr cast = new SQLCastExpr();
                cast.setExpr(this.expr());
                this.accept(Token.AS);
                cast.setDataType(this.parseDataType());
                this.accept(Token.RPAREN);
                sqlExpr = cast;
                break;
            case SUB:
                this.lexer.nextToken();
                switch(this.lexer.token()) {
                    case LITERAL_INT:
                        Number integerValue = this.lexer.integerValue();
                        if (integerValue instanceof Integer) {
                            int intVal = (Integer)integerValue;
                            if (intVal == -2147483648) {
                                integerValue = (long)intVal * -1L;
                            } else {
                                integerValue = intVal * -1;
                            }
                        } else if (integerValue instanceof Long) {
                            long longVal = (Long)integerValue;
                            if (longVal == 2147483648L) {
                                integerValue = (int)(longVal * -1L);
                            } else {
                                integerValue = longVal * -1L;
                            }
                        } else {
                            integerValue = ((BigInteger)integerValue).negate();
                        }

                        sqlExpr = new SQLIntegerExpr((Number)integerValue);
                        this.lexer.nextToken();
                        break label159;
                    case LITERAL_FLOAT:
                        sqlExpr = new SQLNumberExpr(this.lexer.decimalValue().negate());
                        this.lexer.nextToken();
                        break label159;
                    case IDENTIFIER:
                        sqlExpr = new SQLIdentifierExpr(this.lexer.stringVal());
                        this.lexer.nextToken();
                        if (this.lexer.token() == Token.LPAREN || this.lexer.token() == Token.LBRACKET) {
                            sqlExpr = this.primaryRest((SQLExpr)sqlExpr);
                        }

                        sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Negative, (SQLExpr)sqlExpr);
                        break label159;
                    case QUES:
                        SQLVariantRefExpr variantRefExpr = new SQLVariantRefExpr("?");
                        variantRefExpr.setIndex(this.lexer.nextVarIndex());
                        sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Negative, variantRefExpr);
                        this.lexer.nextToken();
                        break label159;
                    case LPAREN:
                        this.lexer.nextToken();
                        sqlExpr = this.expr();
                        this.accept(Token.RPAREN);
                        sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Negative, sqlExpr);
                        break label159;
                    default:
                        throw new ParserException("TODO : " + this.lexer.token());
                }
            case PLUS:
                this.lexer.nextToken();
                switch(this.lexer.token()) {
                    case LITERAL_INT:
                        sqlExpr = new SQLIntegerExpr(this.lexer.integerValue());
                        this.lexer.nextToken();
                        break label159;
                    case LITERAL_FLOAT:
                        sqlExpr = new SQLNumberExpr(this.lexer.decimalValue());
                        this.lexer.nextToken();
                        break label159;
                    case IDENTIFIER:
                        sqlExpr = new SQLIdentifierExpr(this.lexer.stringVal());
                        sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Plus, sqlExpr);
                        this.lexer.nextToken();
                        break label159;
                    case QUES:
                    default:
                        throw new ParserException("TODO");
                    case LPAREN:
                        this.lexer.nextToken();
                        sqlExpr = this.expr();
                        this.accept(Token.RPAREN);
                        sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Plus, sqlExpr);
                        break label159;
                }
            case TILDE:
                this.lexer.nextToken();
                SQLExpr unaryValueExpr = this.expr();
                SQLUnaryExpr unary = new SQLUnaryExpr(SQLUnaryOperator.Compl, unaryValueExpr);
                sqlExpr = unary;
                break;
            case LEFT:
                sqlExpr = new SQLIdentifierExpr("LEFT");
                this.lexer.nextToken();
                break;
            case RIGHT:
                sqlExpr = new SQLIdentifierExpr("RIGHT");
                this.lexer.nextToken();
                break;
            case DATABASE:
                sqlExpr = new SQLIdentifierExpr("DATABASE");
                this.lexer.nextToken();
                break;
            case LOCK:
                sqlExpr = new SQLIdentifierExpr("LOCK");
                this.lexer.nextToken();
                break;
            case NULL:
                sqlExpr = new SQLNullExpr();
                this.lexer.nextToken();
                break;
            case BANG:
                this.lexer.nextToken();
                SQLExpr bangExpr = this.primary();
                sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Not, bangExpr);
                break;
            case LITERAL_HEX:
                String hex = this.lexer.hexString();
                sqlExpr = new SQLHexExpr(hex);
                this.lexer.nextToken();
                break;
            case INTERVAL:
                sqlExpr = this.parseInterval();
                break;
            case COLON:
                this.lexer.nextToken();
                if (this.lexer.token == Token.LITERAL_ALIAS) {
                    sqlExpr = new SQLVariantRefExpr(":\"" + this.lexer.stringVal() + "\"");
                    this.lexer.nextToken();
                }
                break;
            case ANY:
                sqlExpr = this.parseAny();
                break;
            case SOME:
                sqlExpr = this.parseSome();
                break;
            case ALL:
                sqlExpr = this.parseAll();
                break;
            case LITERAL_ALIAS:
                sqlExpr = this.parseAliasExpr(this.lexer.stringVal());
                this.lexer.nextToken();
                break;
            case EOF:
                throw new EOFParserException();
            case TRUE:
                this.lexer.nextToken();
                sqlExpr = new SQLBooleanExpr(true);
                break;
            case FALSE:
                this.lexer.nextToken();
                sqlExpr = new SQLBooleanExpr(false);
                break;
            default:
                throw new ParserException("ERROR. token : " + tok + ", pos : " + this.lexer.pos());
        }

        SQLExpr expr = this.primaryRest((SQLExpr)sqlExpr);
        if (beforeComments != null) {
            expr.addBeforeComment(beforeComments);
        }

        return expr;
    }

    protected SQLExpr parseAll() {
        this.lexer.nextToken();
        SQLAllExpr allExpr = new SQLAllExpr();
        this.accept(Token.LPAREN);
        SQLSelect allSubQuery = this.createSelectParser().select();
        allExpr.setSubQuery(allSubQuery);
        this.accept(Token.RPAREN);
        allSubQuery.setParent(allExpr);
        return allExpr;
    }

    protected SQLExpr parseSome() {
        this.lexer.nextToken();
        SQLSomeExpr someExpr = new SQLSomeExpr();
        this.accept(Token.LPAREN);
        SQLSelect someSubQuery = this.createSelectParser().select();
        someExpr.setSubQuery(someSubQuery);
        this.accept(Token.RPAREN);
        someSubQuery.setParent(someExpr);
        return someExpr;
    }

    protected SQLExpr parseAny() {
        this.lexer.nextToken();
        Object sqlExpr;
        if (this.lexer.token() == Token.LPAREN) {
            this.accept(Token.LPAREN);
            if (this.lexer.token() == Token.IDENTIFIER) {
                SQLExpr expr = this.expr();
                SQLMethodInvokeExpr methodInvokeExpr = new SQLMethodInvokeExpr("ANY");
                methodInvokeExpr.addParameter(expr);
                this.accept(Token.RPAREN);
                return methodInvokeExpr;
            }

            SQLAnyExpr anyExpr = new SQLAnyExpr();
            SQLSelect anySubQuery = this.createSelectParser().select();
            anyExpr.setSubQuery(anySubQuery);
            this.accept(Token.RPAREN);
            anySubQuery.setParent(anyExpr);
            sqlExpr = anyExpr;
        } else {
            sqlExpr = new SQLIdentifierExpr("ANY");
        }

        return (SQLExpr)sqlExpr;
    }

    protected SQLExpr parseAliasExpr(String alias) {
        return new SQLIdentifierExpr('"' + alias + '"');
    }

    protected SQLExpr parseInterval() {
        throw new ParserException("TODO");
    }

    public SQLSelectParser createSelectParser() {
        return new SQLSelectParser(this);
    }

    public SQLExpr primaryRest(SQLExpr expr) {
        if (expr == null) {
            throw new IllegalArgumentException("expr");
        } else {
            Token token = this.lexer.token;
            String text;
            if (token == Token.OF) {
                if (expr instanceof SQLIdentifierExpr) {
                    text = ((SQLIdentifierExpr)expr).getName();
                    if ("CURRENT".equalsIgnoreCase(text)) {
                        this.lexer.nextToken();
                        SQLName cursorName = this.name();
                        return new SQLCurrentOfCursorExpr(cursorName);
                    }
                }
            } else if (token == Token.FOR && expr instanceof SQLIdentifierExpr) {
                SQLIdentifierExpr idenExpr = (SQLIdentifierExpr)expr;
                String name = idenExpr.getName();
                SQLSequenceExpr seqExpr;
                SQLName seqName;
                if ("NEXTVAL".equalsIgnoreCase(name)) {
                    this.lexer.nextToken();
                    seqName = this.name();
                    seqExpr = new SQLSequenceExpr(seqName, Function.NextVal);
                    return seqExpr;
                }

                if ("CURRVAL".equalsIgnoreCase(name)) {
                    this.lexer.nextToken();
                    seqName = this.name();
                    seqExpr = new SQLSequenceExpr(seqName, Function.CurrVal);
                    return seqExpr;
                }

                if ("PREVVAL".equalsIgnoreCase(name)) {
                    this.lexer.nextToken();
                    seqName = this.name();
                    seqExpr = new SQLSequenceExpr(seqName, Function.PrevVal);
                    return seqExpr;
                }
            }

            if (token == Token.DOT) {
                this.lexer.nextToken();
                if (expr instanceof SQLCharExpr) {
                    text = ((SQLCharExpr)expr).getText();
                    expr = new SQLIdentifierExpr(text);
                }

                expr = this.dotRest((SQLExpr)expr);
                return this.primaryRest(expr);
            } else if (this.identifierEquals("SETS") && expr.getClass() == SQLIdentifierExpr.class && "GROUPING".equalsIgnoreCase(((SQLIdentifierExpr)expr).getName())) {
                SQLGroupingSetExpr groupingSets = new SQLGroupingSetExpr();
                this.lexer.nextToken();
                this.accept(Token.LPAREN);

                while(true) {
                    Object item;
                    if (this.lexer.token() == Token.LPAREN) {
                        this.lexer.nextToken();
                        SQLListExpr listExpr = new SQLListExpr();
                        this.exprList(listExpr.getItems(), listExpr);
                        item = listExpr;
                        this.accept(Token.RPAREN);
                    } else {
                        item = this.expr();
                    }

                    ((SQLExpr)item).setParent(groupingSets);
                    groupingSets.addParameter((SQLExpr)item);
                    if (this.lexer.token() == Token.RPAREN) {
                        this.exprList(groupingSets.getParameters(), groupingSets);
                        this.accept(Token.RPAREN);
                        return groupingSets;
                    }

                    this.accept(Token.COMMA);
                }
            } else {
                return (SQLExpr)(this.lexer.token() == Token.LPAREN ? this.methodRest((SQLExpr)expr, true) : expr);
            }
        }
    }

    protected SQLExpr methodRest(SQLExpr expr, boolean acceptLPAREN) {
        if (acceptLPAREN) {
            this.accept(Token.LPAREN);
        }

        if (!(expr instanceof SQLName) && !(expr instanceof SQLDefaultExpr) && !(expr instanceof SQLCharExpr)) {
            throw new ParserException("not support token:" + this.lexer.token());
        } else {
            boolean distinct = false;
            if (this.lexer.token() == Token.DISTINCT) {
                this.lexer.nextToken();
                distinct = true;
            }

            String methodName;
            SQLMethodInvokeExpr methodInvokeExpr;
            if (expr instanceof SQLPropertyExpr) {
                methodName = ((SQLPropertyExpr)expr).getName();
                methodInvokeExpr = new SQLMethodInvokeExpr(methodName);
                methodInvokeExpr.setOwner(((SQLPropertyExpr)expr).getOwner());
            } else {
                methodName = expr.toString();
                methodInvokeExpr = new SQLMethodInvokeExpr(methodName);
            }

            SQLAggregateExpr aggregateExpr;
            if (this.isAggreateFunction(methodName)) {
                aggregateExpr = this.parseAggregateExpr(methodName);
                if (distinct) {
                    aggregateExpr.setOption(SQLAggregateOption.DISTINCT);
                }

                return aggregateExpr;
            } else {
                if (this.lexer.token() != Token.RPAREN) {
                    this.exprList(methodInvokeExpr.getParameters(), methodInvokeExpr);
                }

                if (this.lexer.token() == Token.FROM) {
                    this.lexer.nextToken();
                    SQLExpr from = this.expr();
                    methodInvokeExpr.setFrom(from);
                }

                this.accept(Token.RPAREN);
                if (this.lexer.token() == Token.OVER) {
                    aggregateExpr = new SQLAggregateExpr(methodName);
                    aggregateExpr.getArguments().addAll(methodInvokeExpr.getParameters());
                    this.over(aggregateExpr);
                    return this.primaryRest(aggregateExpr);
                } else {
                    return this.primaryRest(methodInvokeExpr);
                }
            }
        }
    }

    protected SQLExpr dotRest(SQLExpr expr) {
        /*      */     SQLPropertyExpr sQLPropertyExpr = null;
        /*  881 */     if (this.lexer.token() == Token.STAR) {
            /*  882 */       this.lexer.nextToken();
            /*  883 */       sQLPropertyExpr = new SQLPropertyExpr(expr, "*");
            /*      */     } else {
            /*      */       SQLMethodInvokeExpr sQLMethodInvokeExpr = null;
            /*      */       String name;
            /*  887 */       if (this.lexer.token() == Token.IDENTIFIER || this.lexer.token() == Token.LITERAL_CHARS || this.lexer
/*  888 */         .token() == Token.LITERAL_ALIAS) {
                /*  889 */         name = this.lexer.stringVal();
                /*  890 */         this.lexer.nextToken();
                /*  891 */       } else if (this.lexer.getKeywods().containsValue(this.lexer.token())) {
                /*  892 */         name = this.lexer.stringVal();
                /*  893 */         this.lexer.nextToken();
                /*      */       } else {
                /*  895 */         throw new ParserException("error : " + this.lexer.stringVal());
                /*      */       }
            /*      */
            /*  898 */       if (this.lexer.token() == Token.LPAREN) {
                /*  899 */         this.lexer.nextToken();
                /*      */
                /*  901 */         SQLMethodInvokeExpr methodInvokeExpr = new SQLMethodInvokeExpr(name);
                /*  902 */         methodInvokeExpr.setOwner((SQLExpr)sQLPropertyExpr);
                /*  903 */         if (this.lexer.token() == Token.RPAREN) {
                    /*  904 */           this.lexer.nextToken();
                    /*      */         } else {
                    /*  906 */           if (this.lexer.token() == Token.PLUS) {
                        /*  907 */             methodInvokeExpr.addParameter((SQLExpr)new SQLIdentifierExpr("+"));
                        /*  908 */             this.lexer.nextToken();
                        /*      */           } else {
                        /*  910 */             exprList(methodInvokeExpr.getParameters(), (SQLObject)methodInvokeExpr);
                        /*      */           }
                    /*  912 */           accept(Token.RPAREN);
                    /*      */         }
                /*  914 */         sQLMethodInvokeExpr = methodInvokeExpr;
                /*      */       } else {
                /*  916 */         sQLPropertyExpr = new SQLPropertyExpr((SQLExpr)sQLMethodInvokeExpr, name);
                /*      */       }
            /*      */     }
        /*      */
        /*  920 */     return primaryRest((SQLExpr)sQLPropertyExpr);
        /*      */   }

    public final SQLExpr groupComparisionRest(SQLExpr expr) {
        return expr;
    }

    public final void names(Collection<SQLName> exprCol) {
        this.names(exprCol, (SQLObject)null);
    }

    public final void names(Collection<SQLName> exprCol, SQLObject parent) {
        if (this.lexer.token() != Token.RBRACE) {
            if (this.lexer.token() != Token.EOF) {
                SQLName name = this.name();
                name.setParent(parent);
                exprCol.add(name);

                while(this.lexer.token() == Token.COMMA) {
                    this.lexer.nextToken();
                    name = this.name();
                    name.setParent(parent);
                    exprCol.add(name);
                }

            }
        }
    }

    /** @deprecated */
    @Deprecated
    public final void exprList(Collection<SQLExpr> exprCol) {
        this.exprList(exprCol, (SQLObject)null);
    }

    public final void exprList(Collection<SQLExpr> exprCol, SQLObject parent) {
        if (this.lexer.token() != Token.RPAREN && this.lexer.token() != Token.RBRACKET) {
            if (this.lexer.token() != Token.EOF) {
                SQLExpr expr = this.expr();
                expr.setParent(parent);
                exprCol.add(expr);

                while(this.lexer.token() == Token.COMMA) {
                    this.lexer.nextToken();
                    expr = this.expr();
                    expr.setParent(parent);
                    exprCol.add(expr);
                }

            }
        }
    }

    public SQLName name() {
        String identName;
        if (this.lexer.token() == Token.LITERAL_ALIAS) {
            identName = '"' + this.lexer.stringVal() + '"';
            this.lexer.nextToken();
        } else if (this.lexer.token() == Token.IDENTIFIER) {
            identName = this.lexer.stringVal();
            this.lexer.nextToken();
        } else if (this.lexer.token() == Token.LITERAL_CHARS) {
            identName = '\'' + this.lexer.stringVal() + '\'';
            this.lexer.nextToken();
        } else if (this.lexer.token() == Token.VARIANT) {
            identName = this.lexer.stringVal();
            this.lexer.nextToken();
        } else {
            switch(this.lexer.token()) {
                case END:
                case COMMENT:
                case ENABLE:
                case DISABLE:
                case INITIALLY:
                case SEQUENCE:
                case USER:
                case GRANT:
                case MODEL:
                case PCTFREE:
                case INITRANS:
                case MAXTRANS:
                case SEGMENT:
                case CREATION:
                case IMMEDIATE:
                case DEFERRED:
                case STORAGE:
                case NEXT:
                case MINEXTENTS:
                case MAXEXTENTS:
                case MAXSIZE:
                case PCTINCREASE:
                case FLASH_CACHE:
                case CELL_FLASH_CACHE:
                case KEEP:
                case NONE:
                case LOB:
                case STORE:
                case ROW:
                case CHUNK:
                case CACHE:
                case NOCACHE:
                case LOGGING:
                case NOCOMPRESS:
                case KEEP_DUPLICATES:
                case EXCEPTIONS:
                case PURGE:
                case BINARY:
                case OVER:
                case ORDER:
                case ANALYZE:
                case OPTIMIZE:
                case REVOKE:
                    identName = this.lexer.stringVal();
                    this.lexer.nextToken();
                    break;
                case COMPUTE:
                case EXPLAIN:
                case WITH:
                case REPLACE:
                case INDEX:
                case FULL:
                case TO:
                case IDENTIFIED:
                case PASSWORD:
                case WINDOW:
                case OFFSET:
                case SHARE:
                case START:
                case CONNECT:
                case MATCHED:
                case ERRORS:
                case REJECT:
                case UNLIMITED:
                case BEGIN:
                case EXCLUSIVE:
                case MODE:
                case ADVISE:
                case VIEW:
                case ESCAPE:
                case CONSTRAINT:
                case TYPE:
                case OPEN:
                case REPEAT:
                case TABLE:
                case TRUNCATE:
                case EXCEPTION:
                case FUNCTION:
                case IDENTITY:
                case EXTRACT:
                case CASE:
                case EXISTS:
                case NOT:
                case SELECT:
                case CAST:
                case SUB:
                case PLUS:
                case TILDE:
                case LEFT:
                case RIGHT:
                case DATABASE:
                case LOCK:
                case NULL:
                case BANG:
                case LITERAL_HEX:
                case INTERVAL:
                case COLON:
                case ANY:
                case SOME:
                case ALL:
                case LITERAL_ALIAS:
                case EOF:
                case TRUE:
                case FALSE:
                default:
                    throw new ParserException("error " + this.lexer.token());
            }
        }

        SQLName name = new SQLIdentifierExpr(identName);
        name = this.nameRest(name);
        return name;
    }

    /*      */   public SQLName nameRest(SQLName name) {
        /*      */     SQLName sQLName = null;
        /* 1057 */     if (this.lexer.token() == Token.DOT) {
            /* 1058 */       SQLPropertyExpr sQLPropertyExpr = null; this.lexer.nextToken();
            /*      */
            /* 1060 */       if (this.lexer.token() == Token.KEY) {
                /* 1061 */         sQLPropertyExpr = new SQLPropertyExpr((SQLExpr)name, "KEY");
                /* 1062 */         this.lexer.nextToken();
                /* 1063 */         return (SQLName)sQLPropertyExpr;
                /*      */       }
            /*      */
            /* 1066 */       if (this.lexer.token() != Token.LITERAL_ALIAS && this.lexer.token() != Token.IDENTIFIER &&
                    /* 1067 */         !this.lexer.getKeywods().containsValue(this.lexer.token())) {
                /* 1068 */         throw new ParserException("error, " + this.lexer.token());
                /*      */       }
            /*      */
            /* 1071 */       if (this.lexer.token() == Token.LITERAL_ALIAS) {
                /* 1072 */         sQLPropertyExpr = new SQLPropertyExpr((SQLExpr)sQLPropertyExpr, '"' + this.lexer.stringVal() + '"');
                /*      */       } else {
                /* 1074 */         sQLPropertyExpr = new SQLPropertyExpr((SQLExpr)sQLPropertyExpr, this.lexer.stringVal());
                /*      */       }
            /* 1076 */       this.lexer.nextToken();
            /* 1077 */       sQLName = nameRest((SQLName)sQLPropertyExpr);
            /*      */     }
        /*      */
        /* 1080 */     return sQLName;
        /*      */   }

    public boolean isAggreateFunction(String word) {
        for(int i = 0; i < this.aggregateFunctions.length; ++i) {
            if (this.aggregateFunctions[i].compareToIgnoreCase(word) == 0) {
                return true;
            }
        }

        return false;
    }

    protected SQLAggregateExpr parseAggregateExpr(String methodName) {
        methodName = methodName.toUpperCase();
        SQLAggregateExpr aggregateExpr;
        if (this.lexer.token() == Token.ALL) {
            aggregateExpr = new SQLAggregateExpr(methodName, SQLAggregateOption.ALL);
            this.lexer.nextToken();
        } else if (this.lexer.token() == Token.DISTINCT) {
            aggregateExpr = new SQLAggregateExpr(methodName, SQLAggregateOption.DISTINCT);
            this.lexer.nextToken();
        } else if (this.identifierEquals("DEDUPLICATION")) {
            aggregateExpr = new SQLAggregateExpr(methodName, SQLAggregateOption.DEDUPLICATION);
            this.lexer.nextToken();
        } else {
            aggregateExpr = new SQLAggregateExpr(methodName);
        }

        this.exprList(aggregateExpr.getArguments(), aggregateExpr);
        this.parseAggregateExprRest(aggregateExpr);
        this.accept(Token.RPAREN);
        if (this.lexer.token() == Token.OVER) {
            this.over(aggregateExpr);
        }

        return aggregateExpr;
    }

    protected void over(SQLAggregateExpr aggregateExpr) {
        this.lexer.nextToken();
        SQLOver over = new SQLOver();
        this.accept(Token.LPAREN);
        if (this.lexer.token() == Token.PARTITION || this.identifierEquals("PARTITION")) {
            this.lexer.nextToken();
            this.accept(Token.BY);
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                this.exprList(over.getPartitionBy(), over);
                this.accept(Token.RPAREN);
            } else {
                this.exprList(over.getPartitionBy(), over);
            }
        }

        over.setOrderBy(this.parseOrderBy());
        if (this.lexer.token() == Token.OF) {
            this.lexer.nextToken();
            SQLExpr of = this.expr();
            over.setOf(of);
        }

        WindowingType windowingType = null;
        if (this.identifierEquals("ROWS")) {
            windowingType = WindowingType.ROWS;
        } else if (this.identifierEquals("RANGE")) {
            windowingType = WindowingType.RANGE;
        }

        if (windowingType != null) {
            over.setWindowingType(windowingType);
            this.lexer.nextToken();
            SQLIntegerExpr rowsExpr;
            if (this.lexer.token == Token.BETWEEN) {
                this.lexer.nextToken();
                rowsExpr = (SQLIntegerExpr)this.primary();
                over.setWindowingBetweenBegin(rowsExpr);
                if (this.identifierEquals("PRECEDING")) {
                    over.setWindowingBetweenBeginPreceding(true);
                    this.lexer.nextToken();
                } else if (this.identifierEquals("FOLLOWING")) {
                    over.setWindowingBetweenBeginFollowing(true);
                    this.lexer.nextToken();
                }

                this.accept(Token.AND);
                SQLIntegerExpr betweenEnd = (SQLIntegerExpr)this.primary();
                over.setWindowingBetweenEnd(betweenEnd);
                if (this.identifierEquals("PRECEDING")) {
                    over.setWindowingBetweenEndPreceding(true);
                    this.lexer.nextToken();
                } else if (this.identifierEquals("FOLLOWING")) {
                    over.setWindowingBetweenEndFollowing(true);
                    this.lexer.nextToken();
                }
            } else if (this.identifierEquals("CURRENT")) {
                this.lexer.nextToken();
                this.acceptIdentifier("ROW");
                over.setWindowing(new SQLIdentifierExpr("CURRENT ROW"));
            } else if (this.identifierEquals("UNBOUNDED")) {
                this.lexer.nextToken();
                over.setWindowing(new SQLIdentifierExpr("UNBOUNDED"));
                if (this.identifierEquals("PRECEDING")) {
                    over.setWindowingPreceding(true);
                    this.lexer.nextToken();
                } else if (this.identifierEquals("FOLLOWING")) {
                    over.setWindowingFollowing(true);
                    this.lexer.nextToken();
                }
            } else {
                rowsExpr = (SQLIntegerExpr)this.primary();
                over.setWindowing(rowsExpr);
                if (this.identifierEquals("PRECEDING")) {
                    over.setWindowingPreceding(true);
                    this.lexer.nextToken();
                } else if (this.identifierEquals("FOLLOWING")) {
                    over.setWindowingFollowing(true);
                    this.lexer.nextToken();
                }
            }
        }

        this.accept(Token.RPAREN);
        aggregateExpr.setOver(over);
    }

    protected SQLAggregateExpr parseAggregateExprRest(SQLAggregateExpr aggregateExpr) {
        return aggregateExpr;
    }

    public SQLOrderBy parseOrderBy() {
        if (this.lexer.token() != Token.ORDER) {
            return null;
        } else {
            SQLOrderBy orderBy = new SQLOrderBy();
            this.lexer.nextToken();
            if (this.identifierEquals("SIBLINGS")) {
                this.lexer.nextToken();
                orderBy.setSibings(true);
            }

            this.accept(Token.BY);
            orderBy.addItem(this.parseSelectOrderByItem());

            while(this.lexer.token() == Token.COMMA) {
                this.lexer.nextToken();
                orderBy.addItem(this.parseSelectOrderByItem());
            }

            return orderBy;
        }
    }

    public SQLSelectOrderByItem parseSelectOrderByItem() {
        SQLSelectOrderByItem item = new SQLSelectOrderByItem();
        item.setExpr(this.expr());
        if (this.lexer.token() == Token.ASC) {
            this.lexer.nextToken();
            item.setType(SQLOrderingSpecification.ASC);
        } else if (this.lexer.token() == Token.DESC) {
            this.lexer.nextToken();
            item.setType(SQLOrderingSpecification.DESC);
        }

        if (this.identifierEquals("NULLS")) {
            this.lexer.nextToken();
            if (this.identifierEquals("FIRST")) {
                this.lexer.nextToken();
                item.setNullsOrderType(NullsOrderType.NullsFirst);
            } else {
                if (!this.identifierEquals("LAST")) {
                    throw new ParserException("TODO " + this.lexer.token());
                }

                this.lexer.nextToken();
                item.setNullsOrderType(NullsOrderType.NullsLast);
            }
        }

        return item;
    }

    public SQLUpdateSetItem parseUpdateSetItem() {
        SQLUpdateSetItem item = new SQLUpdateSetItem();
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            SQLListExpr list = new SQLListExpr();
            this.exprList(list.getItems(), list);
            this.accept(Token.RPAREN);
            item.setColumn(list);
        } else {
            item.setColumn(this.primary());
        }

        if (this.lexer.token() == Token.COLONEQ) {
            this.lexer.nextToken();
        } else {
            this.accept(Token.EQ);
        }

        item.setValue(this.expr());
        return item;
    }

    public final SQLExpr bitAnd() {
        SQLExpr expr = this.shift();
        return this.bitAndRest(expr);
    }

    public final SQLExpr bitAndRest(SQLExpr expr) {
        while(this.lexer.token() == Token.AMP) {
            this.lexer.nextToken();
            SQLExpr rightExp = this.shift();
            expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.BitwiseAnd, rightExp, this.getDbType());
        }

        return (SQLExpr)expr;
    }

    public final SQLExpr bitOr() {
        SQLExpr expr = this.bitAnd();
        return this.bitOrRest(expr);
    }

    /*      */   public final SQLExpr bitOrRest(SQLExpr expr) {
        /*      */     SQLExpr sQLExpr = null;
        /* 1324 */     while (this.lexer.token() == Token.BAR) {
            /* 1325 */       this.lexer.nextToken();
            /* 1326 */       SQLExpr rightExp = bitAnd();
            /* 1327 */       SQLBinaryOpExpr sQLBinaryOpExpr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.BitwiseOr, rightExp, getDbType());
            /* 1328 */       sQLExpr = bitAndRest((SQLExpr)sQLBinaryOpExpr);
            /*      */     }
        /* 1330 */     return sQLExpr;
        /*      */   }

    public final SQLExpr equality() {
        SQLExpr expr = this.bitOr();
        return this.equalityRest(expr);
    }

    public SQLExpr equalityRest(SQLExpr expr) {
        SQLExpr rightExp;
        if (this.lexer.token() == Token.EQ) {
            this.lexer.nextToken();

            try {
                rightExp = this.bitOr();
            } catch (EOFParserException var4) {
                throw new ParserException("EOF, " + expr + "=", var4);
            }

            rightExp = this.equalityRest(rightExp);
            expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.Equality, rightExp, this.getDbType());
        } else if (this.lexer.token() == Token.BANGEQ) {
            this.lexer.nextToken();
            rightExp = this.bitOr();
            rightExp = this.equalityRest(rightExp);
            expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.NotEqual, rightExp, this.getDbType());
        } else if (this.lexer.token() == Token.COLONEQ) {
            this.lexer.nextToken();
            rightExp = this.expr();
            expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.Assignment, rightExp, this.getDbType());
        }

        return (SQLExpr)expr;
    }

    public final SQLExpr inRest(SQLExpr expr) {
        if (this.lexer.token() == Token.IN) {
            this.lexer.nextToken();
            SQLInListExpr inListExpr = new SQLInListExpr((SQLExpr)expr);
            SQLExpr targetExpr;
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                this.exprList(inListExpr.getTargetList(), inListExpr);
                this.accept(Token.RPAREN);
            } else {
                targetExpr = this.primary();
                targetExpr.setParent(inListExpr);
                inListExpr.getTargetList().add(targetExpr);
            }

            expr = inListExpr;
            if (inListExpr.getTargetList().size() == 1) {
                targetExpr = (SQLExpr)inListExpr.getTargetList().get(0);
                if (targetExpr instanceof SQLQueryExpr) {
                    SQLInSubQueryExpr inSubQueryExpr = new SQLInSubQueryExpr();
                    inSubQueryExpr.setExpr(inListExpr.getExpr());
                    inSubQueryExpr.setSubQuery(((SQLQueryExpr)targetExpr).getSubQuery());
                    expr = inSubQueryExpr;
                }
            }
        }

        return (SQLExpr)expr;
    }

    public final SQLExpr additive() {
        SQLExpr expr = this.multiplicative();
        return this.additiveRest(expr);
    }

    /*      */   public SQLExpr additiveRest(SQLExpr expr) {
        /*      */     SQLExpr sQLExpr = null;
        /* 1404 */     if (this.lexer.token() == Token.PLUS)
            /* 1405 */     { this.lexer.nextToken();
            /* 1406 */       SQLExpr rightExp = multiplicative();
            /*      */
            /* 1408 */       SQLBinaryOpExpr sQLBinaryOpExpr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Add, rightExp, getDbType());
            /* 1409 */       sQLExpr = additiveRest((SQLExpr)sQLBinaryOpExpr); }
        /* 1410 */     else { SQLExpr sQLExpr1 = null; if (this.lexer.token() == Token.BARBAR || this.lexer.token() == Token.CONCAT) {
            /* 1411 */         this.lexer.nextToken();
            /* 1412 */         SQLExpr rightExp = multiplicative();
            /* 1413 */         SQLBinaryOpExpr sQLBinaryOpExpr = new SQLBinaryOpExpr(sQLExpr, SQLBinaryOperator.Concat, rightExp, getDbType());
            /* 1414 */         sQLExpr1 = additiveRest((SQLExpr)sQLBinaryOpExpr);
            /* 1415 */       } else if (this.lexer.token() == Token.SUB) {
            /* 1416 */         this.lexer.nextToken();
            /* 1417 */         SQLExpr rightExp = multiplicative();
            /*      */
            /* 1419 */         SQLBinaryOpExpr sQLBinaryOpExpr = new SQLBinaryOpExpr(sQLExpr1, SQLBinaryOperator.Subtract, rightExp, getDbType());
            /* 1420 */         sQLExpr = additiveRest((SQLExpr)sQLBinaryOpExpr);
            /*      */       }  }
        /*      */
        /* 1423 */     return sQLExpr;
        /*      */   }


    public final SQLExpr shift() {
        SQLExpr expr = this.additive();
        return this.shiftRest(expr);
    }

    /*      */   public SQLExpr shiftRest(SQLExpr expr) {
        /*      */     SQLExpr sQLExpr = null;
        /* 1432 */     if (this.lexer.token() == Token.LTLT) {
            /* 1433 */       this.lexer.nextToken();
            /* 1434 */       SQLExpr rightExp = additive();
            /*      */
            /* 1436 */       SQLBinaryOpExpr sQLBinaryOpExpr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.LeftShift, rightExp, getDbType());
            /* 1437 */       sQLExpr = shiftRest((SQLExpr)sQLBinaryOpExpr);
            /* 1438 */     } else if (this.lexer.token() == Token.GTGT) {
            /* 1439 */       this.lexer.nextToken();
            /* 1440 */       SQLExpr rightExp = additive();
            /*      */
            /* 1442 */       SQLBinaryOpExpr sQLBinaryOpExpr = new SQLBinaryOpExpr(sQLExpr, SQLBinaryOperator.RightShift, rightExp, getDbType());
            /* 1443 */       sQLExpr = shiftRest((SQLExpr)sQLBinaryOpExpr);
            /*      */     }
        /*      */
        /* 1446 */     return sQLExpr;
        /*      */   }

    public SQLExpr and() {
        SQLExpr expr = this.relational();
        return this.andRest(expr);
    }

    public SQLExpr andRest(SQLExpr expr) {
        while(true) {
            Token token = this.lexer.token();
            if (token != Token.AND && token != Token.AMPAMP) {
                return (SQLExpr)expr;
            }

            if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
                ((SQLExpr)expr).addAfterComment(this.lexer.readAndResetComments());
            }

            this.lexer.nextToken();
            SQLExpr rightExp = this.relational();
            SQLBinaryOperator operator = token == Token.AMPAMP && "postgresql".equals(this.getDbType()) ? SQLBinaryOperator.PG_And : SQLBinaryOperator.BooleanAnd;
            expr = new SQLBinaryOpExpr((SQLExpr)expr, operator, rightExp, this.getDbType());
        }
    }

    public SQLExpr or() {
        SQLExpr expr = this.and();
        return this.orRest(expr);
    }

    public SQLExpr orRest(SQLExpr expr) {
        while(true) {
            SQLExpr rightExp;
            if (this.lexer.token() == Token.OR) {
                this.lexer.nextToken();
                rightExp = this.and();
                expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.BooleanOr, rightExp, this.getDbType());
            } else {
                if (this.lexer.token() != Token.XOR) {
                    return (SQLExpr)expr;
                }

                this.lexer.nextToken();
                rightExp = this.and();
                expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.BooleanXor, rightExp, this.getDbType());
            }
        }
    }

    public SQLExpr relational() {
        SQLExpr expr = this.equality();
        return this.relationalRest(expr);
    }

    public SQLExpr relationalRest(SQLExpr expr) {
        SQLExpr rightExp;
        SQLBinaryOperator op;
        if (this.lexer.token() == Token.LT) {
            op = SQLBinaryOperator.LessThan;
            this.lexer.nextToken();
            if (this.lexer.token() == Token.EQ) {
                this.lexer.nextToken();
                op = SQLBinaryOperator.LessThanOrEqual;
            }

            rightExp = this.bitOr();
            expr = new SQLBinaryOpExpr((SQLExpr)expr, op, rightExp, this.getDbType());
        } else if (this.lexer.token() == Token.LTEQ) {
            this.lexer.nextToken();
            rightExp = this.bitOr();
            expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.LessThanOrEqual, rightExp, this.getDbType());
        } else if (this.lexer.token() == Token.LTEQGT) {
            this.lexer.nextToken();
            rightExp = this.bitOr();
            expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.LessThanOrEqualOrGreaterThan, rightExp, this.getDbType());
        } else if (this.lexer.token() == Token.GT) {
            op = SQLBinaryOperator.GreaterThan;
            this.lexer.nextToken();
            if (this.lexer.token() == Token.EQ) {
                this.lexer.nextToken();
                op = SQLBinaryOperator.GreaterThanOrEqual;
            }

            rightExp = this.bitOr();
            expr = new SQLBinaryOpExpr((SQLExpr)expr, op, rightExp, this.getDbType());
        } else if (this.lexer.token() == Token.GTEQ) {
            this.lexer.nextToken();
            rightExp = this.bitOr();
            expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.GreaterThanOrEqual, rightExp, this.getDbType());
        } else if (this.lexer.token() == Token.BANGLT) {
            this.lexer.nextToken();
            rightExp = this.bitOr();
            expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.NotLessThan, rightExp, this.getDbType());
        } else if (this.lexer.token() == Token.BANGGT) {
            this.lexer.nextToken();
            rightExp = this.bitOr();
            rightExp = this.relationalRest(rightExp);
            expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.NotGreaterThan, rightExp, this.getDbType());
        } else if (this.lexer.token() == Token.LTGT) {
            this.lexer.nextToken();
            rightExp = this.bitOr();
            expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.LessThanOrGreater, rightExp, this.getDbType());
        } else if (this.lexer.token() == Token.LIKE) {
            this.lexer.nextToken();
            rightExp = this.bitOr();
            expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.Like, rightExp, this.getDbType());
            if (this.lexer.token() == Token.ESCAPE) {
                this.lexer.nextToken();
                rightExp = this.primary();
                expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.Escape, rightExp, this.getDbType());
            }
        } else if (this.identifierEquals("RLIKE")) {
            this.lexer.nextToken();
            rightExp = this.equality();
            rightExp = this.relationalRest(rightExp);
            expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.RLike, rightExp, this.getDbType());
        } else if (this.lexer.token() == Token.ILIKE) {
            this.lexer.nextToken();
            rightExp = this.equality();
            rightExp = this.relationalRest(rightExp);
            expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.ILike, rightExp, this.getDbType());
        } else if (this.lexer.token() == Token.MONKEYS_AT_AT) {
            this.lexer.nextToken();
            rightExp = this.equality();
            rightExp = this.relationalRest(rightExp);
            expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.AT_AT, rightExp, this.getDbType());
        } else if (this.lexer.token() == Token.MONKEYS_AT_GT) {
            this.lexer.nextToken();
            rightExp = this.equality();
            rightExp = this.relationalRest(rightExp);
            expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.Array_Contains, rightExp, this.getDbType());
        } else if (this.lexer.token() == Token.LT_MONKEYS_AT) {
            this.lexer.nextToken();
            rightExp = this.equality();
            rightExp = this.relationalRest(rightExp);
            expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.Array_ContainedBy, rightExp, this.getDbType());
        } else if (this.lexer.token() == Token.NOT) {
            this.lexer.nextToken();
            expr = this.notRationalRest((SQLExpr)expr);
        } else {
            SQLExpr rightExpr;
            if (this.lexer.token() == Token.BETWEEN) {
                this.lexer.nextToken();
                rightExpr = this.bitOr();
                this.accept(Token.AND);
                SQLExpr endExpr = this.bitOr();
                expr = new SQLBetweenExpr((SQLExpr)expr, rightExpr, endExpr);
            } else if (this.lexer.token() == Token.IS) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.NOT) {
                    this.lexer.nextToken();
                    rightExpr = this.primary();
                    expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.IsNot, rightExpr, this.getDbType());
                } else {
                    rightExpr = this.primary();
                    expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.Is, rightExpr, this.getDbType());
                }
            } else if (this.lexer.token() == Token.IN) {
                expr = this.inRest((SQLExpr)expr);
            } else if ("postgresql".equals(this.lexer.dbType)) {
                if (this.identifierEquals("SIMILAR")) {
                    this.lexer.nextToken();
                    this.accept(Token.TO);
                    rightExp = this.equality();
                    rightExp = this.relationalRest(rightExp);
                    expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.SIMILAR_TO, rightExp, this.getDbType());
                } else if (this.lexer.token() == Token.TILDE) {
                    this.lexer.nextToken();
                    rightExp = this.equality();
                    rightExp = this.relationalRest(rightExp);
                    expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.POSIX_Regular_Match, rightExp, this.getDbType());
                } else if (this.lexer.token() == Token.TILDE_STAR) {
                    this.lexer.nextToken();
                    rightExp = this.equality();
                    rightExp = this.relationalRest(rightExp);
                    expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.POSIX_Regular_Match_Insensitive, rightExp, this.getDbType());
                } else if (this.lexer.token() == Token.BANG_TILDE) {
                    this.lexer.nextToken();
                    rightExp = this.equality();
                    rightExp = this.relationalRest(rightExp);
                    expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.POSIX_Regular_Not_Match, rightExp, this.getDbType());
                } else if (this.lexer.token() == Token.BANG_TILDE_STAR) {
                    this.lexer.nextToken();
                    rightExp = this.equality();
                    rightExp = this.relationalRest(rightExp);
                    expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.POSIX_Regular_Not_Match_POSIX_Regular_Match_Insensitive, rightExp, this.getDbType());
                } else if (this.lexer.token() == Token.TILDE_EQ) {
                    this.lexer.nextToken();
                    rightExp = this.equality();
                    rightExp = this.relationalRest(rightExp);
                    expr = new SQLBinaryOpExpr((SQLExpr)expr, SQLBinaryOperator.SAME_AS, rightExp, this.getDbType());
                }
            }
        }

        return (SQLExpr)expr;
    }

    public SQLExpr notRationalRest(SQLExpr expr) {
        /*      */     SQLBinaryOpExpr sQLBinaryOpExpr = null;
        /*      */     SQLBetweenExpr sQLBetweenExpr = null;
        /* 1706 */     if (this.lexer.token() == Token.LIKE)
            /* 1707 */     { this.lexer.nextToken();
            /* 1708 */       SQLExpr rightExp = equality();
            /*      */
            /* 1710 */       rightExp = relationalRest(rightExp);
            /*      */
            /* 1712 */       sQLBinaryOpExpr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.NotLike, rightExp, getDbType());
            /*      */
            /* 1714 */       if (this.lexer.token() == Token.ESCAPE) {
                /* 1715 */         this.lexer.nextToken();
                /* 1716 */         rightExp = expr();
                /* 1717 */         sQLBinaryOpExpr = new SQLBinaryOpExpr((SQLExpr)sQLBinaryOpExpr, SQLBinaryOperator.Escape, rightExp, getDbType());
                /*      */       }  }
        /* 1719 */     else { SQLExpr sQLExpr = null; if (this.lexer.token() == Token.IN) {
            /* 1720 */         SQLInSubQueryExpr sQLInSubQueryExpr = null; this.lexer.nextToken();
            /* 1721 */         accept(Token.LPAREN);
            /*      */
            /* 1723 */         SQLInListExpr inListExpr = new SQLInListExpr((SQLExpr)sQLBinaryOpExpr, true);
            /* 1724 */         exprList(inListExpr.getTargetList(), (SQLObject)inListExpr);
            /* 1725 */         SQLInListExpr sQLInListExpr1 = inListExpr;
            /*      */
            /* 1727 */         accept(Token.RPAREN);
            /*      */
            /* 1729 */         if (inListExpr.getTargetList().size() == 1) {
                /* 1730 */           SQLExpr targetExpr = inListExpr.getTargetList().get(0);
                /* 1731 */           if (targetExpr instanceof SQLQueryExpr) {
                    /* 1732 */             SQLInSubQueryExpr inSubQueryExpr = new SQLInSubQueryExpr();
                    /* 1733 */             inSubQueryExpr.setNot(true);
                    /* 1734 */             inSubQueryExpr.setExpr(inListExpr.getExpr());
                    /* 1735 */             inSubQueryExpr.setSubQuery(((SQLQueryExpr)targetExpr).getSubQuery());
                    /* 1736 */             sQLInSubQueryExpr = inSubQueryExpr;
                    /*      */           }
                /*      */         }
            /*      */
            /* 1740 */         sQLExpr = relationalRest((SQLExpr)sQLInSubQueryExpr);
            /* 1741 */         return sQLExpr;
            /* 1742 */       }  if (this.lexer.token() == Token.BETWEEN) {
            /* 1743 */         this.lexer.nextToken();
            /* 1744 */         SQLExpr beginExpr = bitOr();
            /* 1745 */         accept(Token.AND);
            /* 1746 */         SQLExpr endExpr = bitOr();
            /*      */
            /* 1748 */         sQLBetweenExpr = new SQLBetweenExpr(sQLExpr, true, beginExpr, endExpr);
            /*      */
            /* 1750 */         return (SQLExpr)sQLBetweenExpr;
            /* 1751 */       }  if (identifierEquals("RLIKE")) {
            /* 1752 */         this.lexer.nextToken();
            /* 1753 */         SQLExpr rightExp = primary();
            /*      */
            /* 1755 */         rightExp = relationalRest(rightExp);
            /*      */
            /* 1757 */         return (SQLExpr)new SQLBinaryOpExpr((SQLExpr)sQLBetweenExpr, SQLBinaryOperator.NotRLike, rightExp, getDbType());
            /* 1758 */       }  if (this.lexer.token() == Token.ILIKE) {
            /* 1759 */         this.lexer.nextToken();
            /* 1760 */         SQLExpr rightExp = primary();
            /*      */
            /* 1762 */         rightExp = relationalRest(rightExp);
            /*      */
            /* 1764 */         return (SQLExpr)new SQLBinaryOpExpr((SQLExpr)sQLBetweenExpr, SQLBinaryOperator.NotILike, rightExp, getDbType());
            /*      */       }
            /* 1766 */       throw new ParserException("TODO " + this.lexer.token()); }
        /*      */
        /* 1768 */     return (SQLExpr)sQLBetweenExpr;
        /*      */   }

    public SQLDataType parseDataType() {
        if (this.lexer.token() != Token.DEFAULT && this.lexer.token() != Token.NOT && this.lexer.token() != Token.NULL) {
            SQLName typeExpr = this.name();
            String typeName = typeExpr.toString();
            if ("long".equalsIgnoreCase(typeName) && this.identifierEquals("byte") && "mysql".equals(this.getDbType())) {
                typeName = typeName + ' ' + this.lexer.stringVal();
                this.lexer.nextToken();
            } else if ("double".equalsIgnoreCase(typeName) && "postgresql".equals(this.getDbType())) {
                typeName = typeName + ' ' + this.lexer.stringVal();
                this.lexer.nextToken();
            }

            if (this.isCharType(typeName)) {
                SQLCharacterDataType charType = new SQLCharacterDataType(typeName);
                if (this.lexer.token() == Token.LPAREN) {
                    this.lexer.nextToken();
                    SQLExpr arg = this.expr();
                    arg.setParent(charType);
                    charType.addArgument(arg);
                    this.accept(Token.RPAREN);
                }

                return this.parseCharTypeRest(charType);
            } else {
                if ("character".equalsIgnoreCase(typeName) && "varying".equalsIgnoreCase(this.lexer.stringVal())) {
                    typeName = typeName + ' ' + this.lexer.stringVal();
                    this.lexer.nextToken();
                }

                SQLDataType dataType = new SQLDataTypeImpl(typeName);
                return this.parseDataTypeRest(dataType);
            }
        } else {
            return null;
        }
    }

    protected SQLDataType parseDataTypeRest(SQLDataType dataType) {
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            this.exprList(dataType.getArguments(), dataType);
            this.accept(Token.RPAREN);
        }

        if (this.identifierEquals("PRECISION") && dataType.getName().equalsIgnoreCase("DOUBLE")) {
            this.lexer.nextToken();
            dataType.setName("DOUBLE PRECISION");
        }

        return dataType;
    }

    protected boolean isCharType(String dataTypeName) {
        return "char".equalsIgnoreCase(dataTypeName) || "varchar".equalsIgnoreCase(dataTypeName) || "nchar".equalsIgnoreCase(dataTypeName) || "nvarchar".equalsIgnoreCase(dataTypeName) || "tinytext".equalsIgnoreCase(dataTypeName) || "text".equalsIgnoreCase(dataTypeName) || "mediumtext".equalsIgnoreCase(dataTypeName) || "longtext".equalsIgnoreCase(dataTypeName);
    }

    protected SQLDataType parseCharTypeRest(SQLCharacterDataType charType) {
        if (this.lexer.token() == Token.BINARY) {
            charType.setHasBinary(true);
            this.lexer.nextToken();
        }

        if (this.identifierEquals("CHARACTER")) {
            this.lexer.nextToken();
            this.accept(Token.SET);
            if (this.lexer.token() != Token.IDENTIFIER && this.lexer.token() != Token.LITERAL_CHARS) {
                throw new ParserException();
            }

            charType.setCharSetName(this.lexer.stringVal());
            this.lexer.nextToken();
        }

        if (this.lexer.token() == Token.BINARY) {
            charType.setHasBinary(true);
            this.lexer.nextToken();
        }

        if (this.lexer.token() == Token.IDENTIFIER && this.lexer.stringVal().equalsIgnoreCase("COLLATE")) {
            this.lexer.nextToken();
            if (this.lexer.token() != Token.IDENTIFIER) {
                throw new ParserException();
            }

            charType.setCollate(this.lexer.stringVal());
            this.lexer.nextToken();
        }

        return charType;
    }

    public void accept(Token token) {
        if (this.lexer.token() == token) {
            this.lexer.nextToken();
        } else {
            throw new ParserException("syntax error, expect " + token + ", actual " + this.lexer.token() + " " + this.lexer.stringVal());
        }
    }

    public SQLColumnDefinition parseColumn() {
        SQLColumnDefinition column = this.createColumnDefinition();
        column.setName(this.name());
        if (this.lexer.token() != Token.SET && this.lexer.token() != Token.DROP) {
            column.setDataType(this.parseDataType());
        }

        return this.parseColumnRest(column);
    }

    public SQLColumnDefinition createColumnDefinition() {
        SQLColumnDefinition column = new SQLColumnDefinition();
        return column;
    }

    public SQLColumnDefinition parseColumnRest(SQLColumnDefinition column) {
        if (this.lexer.token() == Token.DEFAULT) {
            this.lexer.nextToken();
            column.setDefaultExpr(this.bitOr());
            return this.parseColumnRest(column);
        } else if (this.lexer.token() == Token.NOT) {
            this.lexer.nextToken();
            this.accept(Token.NULL);
            column.addConstraint(new SQLNotNullConstraint());
            return this.parseColumnRest(column);
        } else if (this.lexer.token() == Token.NULL) {
            this.lexer.nextToken();
            column.getConstraints().add(new SQLNullConstraint());
            return this.parseColumnRest(column);
        } else if (this.lexer.token == Token.PRIMARY) {
            this.lexer.nextToken();
            this.accept(Token.KEY);
            column.addConstraint(new SQLColumnPrimaryKey());
            return this.parseColumnRest(column);
        } else if (this.lexer.token == Token.UNIQUE) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.KEY) {
                this.lexer.nextToken();
            }

            column.addConstraint(new SQLColumnUniqueKey());
            return this.parseColumnRest(column);
        } else if (this.lexer.token == Token.CONSTRAINT) {
            this.lexer.nextToken();
            SQLName name = this.name();
            if (this.lexer.token() == Token.PRIMARY) {
                this.lexer.nextToken();
                this.accept(Token.KEY);
                SQLColumnPrimaryKey pk = new SQLColumnPrimaryKey();
                pk.setName(name);
                column.addConstraint(pk);
                return this.parseColumnRest(column);
            } else if (this.lexer.token() == Token.UNIQUE) {
                this.lexer.nextToken();
                SQLColumnUniqueKey uk = new SQLColumnUniqueKey();
                uk.setName(name);
                column.addConstraint(uk);
                return this.parseColumnRest(column);
            } else if (this.lexer.token() == Token.REFERENCES) {
                this.lexer.nextToken();
                SQLColumnReference ref = new SQLColumnReference();
                ref.setName(name);
                ref.setTable(this.name());
                this.accept(Token.LPAREN);
                this.names(ref.getColumns(), ref);
                this.accept(Token.RPAREN);
                column.addConstraint(ref);
                return this.parseColumnRest(column);
            } else if (this.lexer.token() == Token.NOT) {
                this.lexer.nextToken();
                this.accept(Token.NULL);
                SQLNotNullConstraint notNull = new SQLNotNullConstraint();
                notNull.setName(name);
                column.addConstraint(notNull);
                return this.parseColumnRest(column);
            } else if (this.lexer.token == Token.CHECK) {
                SQLColumnCheck check = this.parseColumnCheck();
                check.setName(name);
                check.setParent(column);
                column.addConstraint(check);
                return this.parseColumnRest(column);
            } else if (this.lexer.token == Token.DEFAULT) {
                this.lexer.nextToken();
                SQLExpr expr = this.expr();
                column.setDefaultExpr(expr);
                return this.parseColumnRest(column);
            } else {
                throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
            }
        } else if (this.lexer.token == Token.CHECK) {
            SQLColumnCheck check = this.parseColumnCheck();
            column.addConstraint(check);
            return this.parseColumnRest(column);
        } else if (this.lexer.token() == Token.COMMENT) {
            this.lexer.nextToken();
            column.setComment(this.primary());
            return this.parseColumnRest(column);
        } else {
            return column;
        }
    }

    protected SQLColumnCheck parseColumnCheck() {
        this.lexer.nextToken();
        SQLExpr expr = this.expr();
        SQLColumnCheck check = new SQLColumnCheck(expr);
        if (this.lexer.token() == Token.DISABLE) {
            this.lexer.nextToken();
            check.setEnable(false);
        } else if (this.lexer.token() == Token.ENABLE) {
            this.lexer.nextToken();
            check.setEnable(true);
        }

        return check;
    }

    public SQLPrimaryKey parsePrimaryKey() {
        this.accept(Token.PRIMARY);
        this.accept(Token.KEY);
        SQLPrimaryKeyImpl pk = new SQLPrimaryKeyImpl();
        this.accept(Token.LPAREN);
        this.exprList(pk.getColumns(), pk);
        this.accept(Token.RPAREN);
        return pk;
    }

    public SQLUnique parseUnique() {
        this.accept(Token.UNIQUE);
        SQLUnique unique = new SQLUnique();
        this.accept(Token.LPAREN);
        this.exprList(unique.getColumns(), unique);
        this.accept(Token.RPAREN);
        return unique;
    }

    public SQLAssignItem parseAssignItem() {
        SQLAssignItem item = new SQLAssignItem();
        SQLExpr var = this.primary();
        if (var instanceof SQLIdentifierExpr) {
            var = new SQLVariantRefExpr(((SQLIdentifierExpr)var).getName());
        }

        item.setTarget((SQLExpr)var);
        if (this.lexer.token() == Token.COLONEQ) {
            this.lexer.nextToken();
        } else {
            this.accept(Token.EQ);
        }

        if (this.lexer.token() == Token.ON) {
            item.setValue(new SQLIdentifierExpr(this.lexer.stringVal()));
            this.lexer.nextToken();
        } else if (this.lexer.token() == Token.ALL) {
            item.setValue(new SQLIdentifierExpr(this.lexer.stringVal()));
            this.lexer.nextToken();
        } else {
            SQLExpr expr = this.expr();
            if (this.lexer.token() == Token.COMMA && "postgresql".equals(this.dbType)) {
                SQLListExpr listExpr = new SQLListExpr();
                listExpr.addItem(expr);
                expr.setParent(listExpr);

                do {
                    this.lexer.nextToken();
                    SQLExpr listItem = this.expr();
                    listItem.setParent(listExpr);
                    listExpr.addItem(listItem);
                } while(this.lexer.token() == Token.COMMA);

                item.setValue(listExpr);
            } else {
                item.setValue(expr);
            }
        }

        return item;
    }

    public List<SQLCommentHint> parseHints() {
        List<SQLCommentHint> hints = new ArrayList();
        this.parseHints(hints);
        return hints;
    }

    public void parseHints(List hints) {
        if (this.lexer.token() == Token.HINT) {
            hints.add(new SQLCommentHint(this.lexer.stringVal()));
            this.lexer.nextToken();
        }

    }

    public SQLConstraint parseConstaint() {
        SQLName name = null;
        if (this.lexer.token() == Token.CONSTRAINT) {
            this.lexer.nextToken();
            name = this.name();
        }

        Object constraint;
        if (this.lexer.token() == Token.PRIMARY) {
            constraint = this.parsePrimaryKey();
        } else if (this.lexer.token() == Token.UNIQUE) {
            constraint = this.parseUnique();
        } else if (this.lexer.token() == Token.FOREIGN) {
            constraint = this.parseForeignKey();
        } else {
            if (this.lexer.token() != Token.CHECK) {
                throw new ParserException("TODO : " + this.lexer.token() + " " + this.lexer.stringVal());
            }

            constraint = this.parseCheck();
        }

        ((SQLConstraint)constraint).setName(name);
        return (SQLConstraint)constraint;
    }

    public SQLCheck parseCheck() {
        this.accept(Token.CHECK);
        SQLCheck check = this.createCheck();
        this.accept(Token.LPAREN);
        check.setExpr(this.expr());
        this.accept(Token.RPAREN);
        return check;
    }

    protected SQLCheck createCheck() {
        return new SQLCheck();
    }

    public SQLForeignKeyConstraint parseForeignKey() {
        this.accept(Token.FOREIGN);
        this.accept(Token.KEY);
        SQLForeignKeyConstraint fk = this.createForeignKey();
        this.accept(Token.LPAREN);
        this.names(fk.getReferencingColumns());
        this.accept(Token.RPAREN);
        this.accept(Token.REFERENCES);
        fk.setReferencedTableName(this.name());
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            this.names(fk.getReferencedColumns(), fk);
            this.accept(Token.RPAREN);
        }

        return fk;
    }

    protected SQLForeignKeyConstraint createForeignKey() {
        return new SQLForeignKeyImpl();
    }

    public SQLSelectItem parseSelectItem() {
        /*      */     SQLExpr expr = null;
        /* 2180 */     boolean connectByRoot = false;
        /* 2181 */     if (this.lexer.token() == Token.IDENTIFIER) {
            /* 2182 */       SQLIdentifierExpr sQLIdentifierExpr = null; String ident = this.lexer.stringVal();
            /* 2183 */       this.lexer.nextTokenComma();
            /*      */
            /* 2185 */       if ("CONNECT_BY_ROOT".equalsIgnoreCase(ident)) {
                /* 2186 */         connectByRoot = true;
                /* 2187 */         sQLIdentifierExpr = new SQLIdentifierExpr(this.lexer.stringVal());
                /* 2188 */         this.lexer.nextToken();
                /* 2189 */       } else if ("DATE".equalsIgnoreCase(ident) && this.lexer
/* 2190 */         .token() == Token.LITERAL_CHARS && ("oracle"
/* 2191 */         .equals(getDbType()) || "postgresql"
/* 2192 */         .equals(getDbType()))) {
                /* 2193 */         String literal = this.lexer.stringVal();
                /* 2194 */         this.lexer.nextToken();
                /*      */
                /* 2196 */         SQLDateExpr dateExpr = new SQLDateExpr();
                /* 2197 */         dateExpr.setLiteral(literal);
                /*      */
                /* 2199 */         SQLDateExpr sQLDateExpr1 = dateExpr;
                /*      */       } else {
                /* 2201 */         sQLIdentifierExpr = new SQLIdentifierExpr(ident);
                /*      */       }
            /*      */
            /* 2204 */       if (this.lexer.token() != Token.COMMA) {
                /* 2205 */         expr = primaryRest((SQLExpr)sQLIdentifierExpr);
                /* 2206 */         expr = exprRest(expr);
                /*      */       }
            /*      */     } else {
            /* 2209 */       expr = expr();
            /*      */     }
        /* 2211 */     String alias = as();
        /*      */
        /* 2213 */     return new SQLSelectItem(expr, alias, connectByRoot);
        /*      */   }

    public SQLExpr parseGroupingSet() {
        String tmp = this.lexer.stringVal();
        this.acceptIdentifier("GROUPING");
        SQLGroupingSetExpr expr = new SQLGroupingSetExpr();
        if (this.identifierEquals("SET")) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            this.exprList(expr.getParameters(), expr);
            this.accept(Token.RPAREN);
            return expr;
        } else {
            return new SQLIdentifierExpr(tmp);
        }
    }

    public SQLPartitionValue parsePartitionValues() {
        if (this.lexer.token() != Token.VALUES) {
            return null;
        } else {
            this.lexer.nextToken();
            SQLPartitionValue values = null;
            if (this.lexer.token() == Token.IN) {
                this.lexer.nextToken();
                values = new SQLPartitionValue(Operator.In);
                this.accept(Token.LPAREN);
                this.exprList(values.getItems(), values);
                this.accept(Token.RPAREN);
            } else if (this.identifierEquals("LESS")) {
                this.lexer.nextToken();
                this.acceptIdentifier("THAN");
                values = new SQLPartitionValue(Operator.LessThan);
                if (this.identifierEquals("MAXVALUE")) {
                    SQLIdentifierExpr maxValue = new SQLIdentifierExpr(this.lexer.stringVal());
                    this.lexer.nextToken();
                    maxValue.setParent(values);
                    values.addItem(maxValue);
                } else {
                    this.accept(Token.LPAREN);
                    this.exprList(values.getItems(), values);
                    this.accept(Token.RPAREN);
                }
            } else if (this.lexer.token() == Token.LPAREN) {
                values = new SQLPartitionValue(Operator.List);
                this.lexer.nextToken();
                this.exprList(values.getItems(), values);
                this.accept(Token.RPAREN);
            }

            return values;
        }
    }

    protected static boolean isIdent(SQLExpr expr, String name) {
        if (expr instanceof SQLIdentifierExpr) {
            SQLIdentifierExpr identExpr = (SQLIdentifierExpr)expr;
            return identExpr.getName().equalsIgnoreCase(name);
        } else {
            return false;
        }
    }

    public SQLLimit parseLimit() {
        if (this.lexer.token() == Token.LIMIT) {
            this.lexer.nextToken();
            SQLLimit limit = new SQLLimit();
            SQLExpr temp = this.expr();
            if (this.lexer.token() == Token.COMMA) {
                limit.setOffset(temp);
                this.lexer.nextToken();
                limit.setRowCount(this.expr());
            } else if (this.identifierEquals("OFFSET")) {
                limit.setRowCount(temp);
                this.lexer.nextToken();
                limit.setOffset(this.expr());
            } else {
                limit.setRowCount(temp);
            }

            return limit;
        } else {
            return null;
        }
    }
}
