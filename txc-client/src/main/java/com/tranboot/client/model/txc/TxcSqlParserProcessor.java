package com.tranboot.client.model.txc;

import com.codahale.metrics.Timer.Context;
import com.googlecode.aviator.AviatorEvaluator;
import com.tranboot.client.druid.sql.SQLUtils;
import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLStatement;
import com.tranboot.client.druid.sql.ast.expr.SQLAllColumnExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOperator;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLLiteralExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNumericLiteralExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLPropertyExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLVariantRefExpr;
import com.tranboot.client.druid.sql.ast.statement.SQLDeleteStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLExprTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLInsertStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectItem;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateSetItem;
import com.tranboot.client.druid.sql.builder.SQLSelectBuilder;
import com.tranboot.client.druid.sql.builder.impl.SQLDeleteBuilderImpl;
import com.tranboot.client.druid.sql.builder.impl.SQLUpdateBuilderImpl;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.tranboot.client.exception.TxcNotSupportException;
import com.tranboot.client.exception.TxcSqlParseException;
import com.tranboot.client.exception.TxcTransactionException;
import com.tranboot.client.model.SQLType;
import com.tranboot.client.model.TableSchema;
import com.tranboot.client.model.txc.SQLParamExtractorPipeline.KeyValuePair;
import com.tranboot.client.model.txc.SQLParamExtractorPipeline.OriginalParamExtractor;
import com.tranboot.client.model.txc.SQLParamExtractorPipeline.PrimaryKeyExtractor;
import com.tranboot.client.model.txc.SQLParamExtractorPipeline.UpdateSetItemExtractor;
import com.tranboot.client.service.TableSchemaCacheLoader;
import com.tranboot.client.sqlast.MySQLRewriteVistorAop;
import com.tranboot.client.sqlast.SQLASTVisitorAspectAdapter;
import com.tranboot.client.sqlast.SqlInsertStatementBuilderImpl;
import com.tranboot.client.sqlast.UniqColumnSQLSelectBuilderImpl;
import com.tranboot.client.utils.LRUCache;
import com.tranboot.client.utils.MetricsReporter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

public class TxcSqlParserProcessor implements TxcSqlProcessor {
    SQLStatement statement;
    SQLUpdateBuilderImpl updateBuilder;
    SQLSelectBuilder queryBuilder;
    SQLDeleteBuilderImpl deleteBuilder;
    SqlInsertStatementBuilderImpl insertStatementBuilder;
    SQLParamExtractorPipeline pipeline;
    String tableName;
    String datasource;
    String dbType;
    JdbcTemplate jdbcTemplate;
    SqlParserTxcSQL result;

    public SQLParamExtractorPipeline getRenderPipeline() {
        return this.pipeline;
    }

    public TxcSqlParserProcessor(JdbcTemplate jdbcTemplate, SQLStatement statement, String datasource) {
        this.jdbcTemplate = jdbcTemplate;
        this.statement = statement;
        this.dbType = "mysql";
        this.datasource = datasource;
    }

    public SqlParserTxcSQL parse() {
        Context context = MetricsReporter.parserProcessorTimer.time();

        SqlParserTxcSQL var2;
        try {
            this.result = new SqlParserTxcSQL();
            this.queryBuilder = new UniqColumnSQLSelectBuilderImpl(this.dbType);
            if (this.statement instanceof MySqlUpdateStatement) {
                this.updateBuilder = new SQLUpdateBuilderImpl(this.dbType);
                this.result.rollbackSql = this.updateBuilder.getSQLUpdateStatement();
                this.result.sqlType = SQLType.UPDATE;
            } else if (this.statement instanceof MySqlInsertStatement) {
                this.deleteBuilder = new SQLDeleteBuilderImpl(this.dbType);
                this.result.rollbackSql = this.deleteBuilder.getSQLDeleteStatement();
                this.result.sqlType = SQLType.INSERT;
            } else if (this.statement instanceof SQLDeleteStatement) {
                this.insertStatementBuilder = new SqlInsertStatementBuilderImpl(this.dbType);
                this.result.rollbackSql = this.insertStatementBuilder.getSQLInsertStatement();
                this.result.sqlType = SQLType.DELETE;
            }

            this.pipeline = new SQLParamExtractorPipeline();
            this.statement.accept(new MySQLRewriteVistorAop(new TxcSqlParserProcessor.TxcSQLASTVisitorAdivce()));
            if (this.updateBuilder != null && this.updateBuilder.getSQLUpdateStatement().getWhere() == null) {
                throw new TxcSqlParseException(String.format("【UPDATE-UPDATE】回滚语句生成失败，没有where条件。原sql:%s", SQLUtils.toSQLString(this.statement, this.dbType)));
            }

            if (this.deleteBuilder != null && this.deleteBuilder.getSQLDeleteStatement().getWhere() == null) {
                throw new TxcSqlParseException(String.format("【INSERT-DELETE】回滚语句生成失败，没有where条件。原sql:%s", SQLUtils.toSQLString(this.statement, this.dbType)));
            }

            this.result.pipeline = this.pipeline;
            ((SQLSelectQueryBlock)this.queryBuilder.getSQLSelectStatement().getSelect().getQuery()).setForUpdate(false);
            this.result.querySql = this.queryBuilder.getSQLSelectStatement();
            this.result.tableName = this.tableName;
            this.result.where = null;
            logger.debug("原sql:{} 解析出来的sql:{}", SQLUtils.toSQLString(this.statement, this.dbType), SQLUtils.toSQLString(this.result.rollbackSql, this.dbType));
            var2 = this.result;
        } finally {
            context.stop();
        }

        return var2;
    }

    private static String deleteSingleQuote(String item) {
        if (item.startsWith("'") && item.endsWith("'")) {
            item = StringUtils.substringBeforeLast(StringUtils.substringAfter(item, "'"), "'");
        }

        return item;
    }

    public boolean manual() {
        return false;
    }

    public boolean auto() {
        return true;
    }

    class TxcSQLASTVisitorAdivce extends SQLASTVisitorAspectAdapter {
        int index = 0;
        TableSchema schema;
        SQLBinaryOpExpr newWhere4Update;
        Set<String> alreadyDealPrimaryKeyStrs;

        TxcSQLASTVisitorAdivce() {
        }

        public SQLExprTableSource tableAspect(SQLExprTableSource table) {
            SQLIdentifierExpr tableName = (SQLIdentifierExpr)table.getExpr();
            String tablename = tableName.getName().toLowerCase();
            TxcSqlParserProcessor.this.queryBuilder.from(tablename, table.getAlias());
            if (TxcSqlParserProcessor.this.updateBuilder != null) {
                TxcSqlParserProcessor.this.updateBuilder.from(tablename, table.getAlias());
            }

            if (TxcSqlParserProcessor.this.deleteBuilder != null) {
                TxcSqlParserProcessor.this.deleteBuilder.from(tablename, table.getAlias());
            }

            if (TxcSqlParserProcessor.this.insertStatementBuilder != null) {
                TxcSqlParserProcessor.this.insertStatementBuilder.insert(tablename);
            }

            TxcSqlParserProcessor.this.tableName = tablename;
            TableSchema schema = LRUCache.getTableSchema(tablename, new TableSchemaCacheLoader(TxcSqlParserProcessor.this.jdbcTemplate, tablename, TxcSqlParserProcessor.this.datasource));
            this.schema = schema;
            return table;
        }

        public void deleteEnterPoint(SQLDeleteStatement deleteStatement) {
            List<String> columns = this.schema.getColumns();
            TxcSqlParserProcessor.this.insertStatementBuilder.column((String[])columns.toArray(new String[0]));

            for(int i = 0; i < columns.size(); ++i) {
                final String cname = ((String)columns.get(i)).toLowerCase();
                final int j = i;
                TxcSqlParserProcessor.this.pipeline.addExtractor(new UpdateSetItemExtractor(TxcSqlParserProcessor.this.pipeline) {
                    public void extract() {
                        Object value = this.getQueryColumn(cname);
                        this.addFinalArg(j, value);
                    }

                    public String desc() {
                        return String.format("【DELETE->INSERT】从原数据中抽取列【%s】的值", cname);
                    }
                });
                ++this.index;
            }

            TxcSqlParserProcessor.this.result.primaryKeyExtractor = new PrimaryKeyExtractor(TxcSqlParserProcessor.this.pipeline) {
                public List<KeyValuePair> extract() {
                    List<KeyValuePair> result = new ArrayList();
                    Iterator var2 = TxcSQLASTVisitorAdivce.this.schema.getPK().iterator();

                    while(var2.hasNext()) {
                        String column = (String)var2.next();
                        KeyValuePair pair = new KeyValuePair(column, this.extractFromRecord(column));
                        result.add(pair);
                    }

                    return result;
                }

                public String desc() {
                    StringBuilder sbuilder = new StringBuilder(String.format("【DELETE->INSERT】从原数据中获取列【%s】的值作为txc反查主键条件", StringUtils.join(TxcSQLASTVisitorAdivce.this.schema.getPK(), ",")));
                    if (TxcSQLASTVisitorAdivce.this.schema.getShardField() == null) {
                        sbuilder.append(";无法解析到分库字段配置");
                    } else {
                        sbuilder.append(String.format(";从原数据中获取列【%s】的值作为分库字段值", TxcSQLASTVisitorAdivce.this.schema.getShardField()));
                    }

                    return sbuilder.toString();
                }

                public String shard() {
                    return TxcSQLASTVisitorAdivce.this.schema.getShardField() == null ? null : this.extractFromRecord(TxcSQLASTVisitorAdivce.this.schema.getShardField()).toString();
                }
            };
            SQLSelectQueryBlock query = (SQLSelectQueryBlock)TxcSqlParserProcessor.this.queryBuilder.getSQLSelectStatement().getSelect().getQuery();
            SQLSelectItem selectItem = new SQLSelectItem(new SQLAllColumnExpr());
            query.addSelectItem(selectItem);
        }

        public void insertEnterPoint(SQLInsertStatement insertStatement) {
            List<SQLExpr> columns = insertStatement.getColumns();
            List<SQLExpr> _values = insertStatement.getValues().getValues();
            Set<String> _columns = new HashSet();
            Iterator var5 = columns.iterator();

            while(var5.hasNext()) {
                SQLExpr column = (SQLExpr)var5.next();
                _columns.add(((SQLName)column).getSimpleName().toLowerCase());
            }

            List<String> matchedPK = this.schema.matchPK(_columns);
            TxcSqlProcessor.logger.debug("{} 【INSERT】matchedPK主键条件:{}", SQLUtils.toSQLString(TxcSqlParserProcessor.this.statement, TxcSqlParserProcessor.this.dbType), matchedPK);
            if (matchedPK != null && matchedPK.size() > 0) {
                int i = 0;
                SQLExpr where = null;
                final Map<String, SQLExpr> pkExpr = new HashMap();
                SQLExpr shardExpr = null;

                SQLVariantRefExpr right;
                int _index;
                for(Iterator var10 = columns.iterator(); var10.hasNext(); ++i) {
                    SQLExpr columnx = (SQLExpr)var10.next();
                    String _columnName = ((SQLName)columnx).getSimpleName().toLowerCase();
                    if (this.schema.getShardField() != null && this.schema.getShardField().toLowerCase().equals(_columnName)) {
                        shardExpr = (SQLExpr)_values.get(i);
                        if (!(shardExpr instanceof SQLVariantRefExpr) && !(shardExpr instanceof SQLLiteralExpr)) {
                            throw new TxcSqlParseException("insert statement中包含不支持的参数类型，目前仅支持占位符和一般的SqlLiteralExpr参数类型");
                        }
                    }

                    if (matchedPK.contains(_columnName)) {
                        right = new SQLVariantRefExpr("?");
                        right.setIndex(this.index);
                        _index = this.index++;
                        final int _indexF = _index;
                        if (_values.get(i) instanceof SQLVariantRefExpr) {
                            SQLVariantRefExpr primaryKeyVariantRef = (SQLVariantRefExpr)_values.get(i);
                            final int _indexx = primaryKeyVariantRef.getIndex();
                            TxcSqlParserProcessor.this.pipeline.addExtractor(new OriginalParamExtractor(TxcSqlParserProcessor.this.pipeline) {
                                public void extract() {
                                    Object[] args = (Object[])((Object[])this.context.getParam("outter_args"));
                                    Object value = args[_indexx];
                                    this.addFinalArg(_indexF, value);
                                }

                                public String desc() {
                                    return String.format("【INSERT->DELETE】从原始参数中提取主键值作为delete条件,下标为%d", _indexx);
                                }
                            });
                            pkExpr.put(_columnName, (SQLVariantRefExpr)_values.get(i));
                        } else {
                            if (!(_values.get(i) instanceof SQLLiteralExpr)) {
                                throw new TxcSqlParseException("insert statement中包含不支持的参数类型，目前仅支持占位符和一般的SqlLiteralExpr参数类型");
                            }

                            final String primaryKeyValue = ((SQLExpr)_values.get(i)).toString();
                            TxcSqlParserProcessor.this.pipeline.addExtractor(new OriginalParamExtractor(TxcSqlParserProcessor.this.pipeline) {
                                public void extract() {
                                    this.addFinalArg(_indexF, primaryKeyValue);
                                }

                                public String desc() {
                                    return String.format("【INSERT->DELETE】从原始SQL中提取主键值:%s作为条件", primaryKeyValue);
                                }
                            });
                            pkExpr.put(_columnName, (SQLLiteralExpr)_values.get(i));
                        }

                        if (where != null) {
                            SQLExpr andRight = new SQLBinaryOpExpr(new SQLIdentifierExpr(_columnName), SQLBinaryOperator.Equality, right);
                            where = new SQLBinaryOpExpr(where, SQLBinaryOperator.BooleanAnd, andRight);
                        } else {
                            where = new SQLBinaryOpExpr(new SQLIdentifierExpr(_columnName), SQLBinaryOperator.Equality, right);
                        }
                    }
                }

                if (where == null) {
                    TxcSqlProcessor.logger.warn("【INSERT-DELETE】原语句{}中没有解析到主键字段", SQLUtils.toSQLString(TxcSqlParserProcessor.this.statement, TxcSqlParserProcessor.this.dbType));
                    throw new TxcTransactionException("txc insert->delete 没有解析到主键字段，不能执行");
                } else {
                    if (shardExpr == null && this.schema.getShardField() != null) {
                        TxcSqlProcessor.logger.warn("当前语句解析不到分库字段值:表【{}】分库字段【{}】", TxcSqlParserProcessor.this.tableName, this.schema.getShardField());
                    }

                    if (shardExpr != null && this.schema.getShardField() != null) {
                        SQLVariantRefExpr rightx = new SQLVariantRefExpr("?");
                        rightx.setIndex(this.index);
                        final int tmpIndex = this.index++;
                        if (shardExpr instanceof SQLVariantRefExpr) {
                            right = (SQLVariantRefExpr)shardExpr;
                            _index = right.getIndex();
                            final int _indexF = _index;
                            TxcSqlParserProcessor.this.pipeline.addExtractor(new OriginalParamExtractor(TxcSqlParserProcessor.this.pipeline) {
                                public void extract() {
                                    Object[] args = (Object[])((Object[])this.context.getParam("outter_args"));
                                    Object value = args[_indexF];
                                    this.addFinalArg(tmpIndex, value);
                                }

                                public String desc() {
                                    return String.format("【INSERT->DELETE】从原始参数中提取分库字段作为delete条件,下标为%d", _indexF);
                                }
                            });
                        } else if (shardExpr instanceof SQLLiteralExpr) {
                            final String shardValue = TxcSqlParserProcessor.deleteSingleQuote(shardExpr.toString());
                            TxcSqlParserProcessor.this.pipeline.addExtractor(new OriginalParamExtractor(TxcSqlParserProcessor.this.pipeline) {
                                public void extract() {
                                    this.addFinalArg(tmpIndex, shardValue);
                                }

                                public String desc() {
                                    return String.format("从sql中取值【%s】作为分库字段【%s】", shardValue, TxcSQLASTVisitorAdivce.this.schema.getShardField());
                                }
                            });
                        }

                        where = new SQLBinaryOpExpr(where, SQLBinaryOperator.BooleanAnd, new SQLBinaryOpExpr(new SQLIdentifierExpr(this.schema.getShardField()), SQLBinaryOperator.Equality, rightx));
                    }

                    TxcSqlParserProcessor.this.deleteBuilder.getSQLDeleteStatement().setWhere(where);
                    final SQLExpr shardExprF = shardExpr;
                    TxcSqlParserProcessor.this.result.primaryKeyExtractor = new PrimaryKeyExtractor(TxcSqlParserProcessor.this.pipeline) {
                        public List<KeyValuePair> extract() {
                            List<KeyValuePair> result = new ArrayList();
                            Iterator var2 = pkExpr.entrySet().iterator();

                            while(var2.hasNext()) {
                                Entry<String, SQLExpr> expr = (Entry)var2.next();
                                if (expr.getValue() instanceof SQLVariantRefExpr) {
                                    int index = ((SQLVariantRefExpr)expr.getValue()).getIndex();
                                    result.add(new KeyValuePair((String)expr.getKey(), this.extractFromArgs(index)));
                                } else if (expr.getValue() instanceof SQLLiteralExpr) {
                                    String value = TxcSqlParserProcessor.deleteSingleQuote(((SQLLiteralExpr)expr.getValue()).toString());
                                    result.add(new KeyValuePair((String)expr.getKey(), value));
                                }
                            }

                            return result;
                        }

                        public String desc() {
                            StringBuilder sbuilder = new StringBuilder("【INSERT->DELETE】");
                            Iterator var2 = pkExpr.entrySet().iterator();

                            while(var2.hasNext()) {
                                Entry<String, SQLExpr> expr = (Entry)var2.next();
                                if (expr.getValue() instanceof SQLVariantRefExpr) {
                                    int index = ((SQLVariantRefExpr)expr.getValue()).getIndex();
                                    sbuilder.append(String.format("从原参数中取下标为【%d】作为txc反查主键条件【%s】;", index, expr.getKey()));
                                } else if (expr.getValue() instanceof SQLLiteralExpr) {
                                    sbuilder.append(String.format("从sql中取值【%s】作为txc反查主键条件【%s】;", TxcSqlParserProcessor.deleteSingleQuote(((SQLLiteralExpr)expr.getValue()).toString()), expr.getKey()));
                                }
                            }

                            if (TxcSQLASTVisitorAdivce.this.schema.getShardField() == null) {
                                sbuilder.append(String.format("无法解析到分库字段配置"));
                            } else if (shardExprF == null) {
                                sbuilder.append(String.format("无法解析到分库字段【%s】", TxcSQLASTVisitorAdivce.this.schema.getShardField()));
                            } else if (shardExprF instanceof SQLVariantRefExpr) {
                                int indexx = ((SQLVariantRefExpr)shardExprF).getIndex();
                                sbuilder.append(String.format("从原参数中取下标为【%d】作为分库字段【%s】", indexx, TxcSQLASTVisitorAdivce.this.schema.getShardField()));
                            } else if (shardExprF instanceof SQLLiteralExpr) {
                                sbuilder.append(String.format("从sql中取值【%s】作为分库字段【%s】", TxcSqlParserProcessor.deleteSingleQuote(((SQLLiteralExpr)shardExprF).toString()), TxcSQLASTVisitorAdivce.this.schema.getShardField()));
                            }

                            return sbuilder.toString();
                        }

                        public String shard() {
                            if (shardExprF == null) {
                                return null;
                            } else if (shardExprF instanceof SQLVariantRefExpr) {
                                int index = ((SQLVariantRefExpr)shardExprF).getIndex();
                                return this.extractFromArgs(index).toString();
                            } else {
                                return shardExprF instanceof SQLLiteralExpr ? TxcSqlParserProcessor.deleteSingleQuote(((SQLLiteralExpr)shardExprF).toString()) : null;
                            }
                        }
                    };
                }
            } else {
                throw new TxcNotSupportException("insert statement中没有包含主键！");
            }
        }

        public SQLUpdateSetItem updateItemAspect(SQLUpdateSetItem updateItem) {
            SQLUpdateSetItem newItem = new SQLUpdateSetItem();
            newItem.setColumn(updateItem.getColumn());
            SQLName column = (SQLName)updateItem.getColumn();
            final String columnName = column.getSimpleName().toLowerCase();
            final SQLExpr value = updateItem.getValue();
            if (this.schema.getPK().contains(columnName)) {
                if (this.alreadyDealPrimaryKeyStrs == null) {
                    this.alreadyDealPrimaryKeyStrs = new HashSet();
                }

                this.alreadyDealPrimaryKeyStrs.add(columnName);
                SQLVariantRefExpr right = new SQLVariantRefExpr("?");
                right.setIndex(this.index);
                final int tmpIndexx = this.index++;
                if (value instanceof SQLVariantRefExpr) {
                    final int oldIndex = ((SQLVariantRefExpr)value).getIndex();
                    TxcSqlParserProcessor.this.pipeline.addExtractor(new OriginalParamExtractor(TxcSqlParserProcessor.this.pipeline) {
                        public void extract() {
                            Object[] args = (Object[])((Object[])this.context.getParam("outter_args"));
                            Object value = args[oldIndex];
                            this.addFinalArg(tmpIndexx, value);
                        }

                        public String desc() {
                            return String.format("【WHERE】update语句存在主键更新，从原参数数组中获取下标【%d】的值作为回滚条件", oldIndex);
                        }
                    });
                } else if (value instanceof SQLLiteralExpr) {
                    TxcSqlParserProcessor.this.pipeline.addExtractor(new OriginalParamExtractor(TxcSqlParserProcessor.this.pipeline) {
                        public void extract() {
                            this.addFinalArg(tmpIndexx, value.toString());
                        }

                        public String desc() {
                            return String.format("【WHERE】update语句存在主键更新，从原sql语句中获取值%s作为回滚条件,并放入下标【%d】", TxcSqlParserProcessor.deleteSingleQuote(value.toString()), tmpIndexx);
                        }
                    });
                } else {
                    if (value instanceof SQLMethodInvokeExpr) {
                        throw new TxcNotSupportException("txc暂不支持的sql语法");
                    }

                    SQLName left;
                    if (!(value instanceof SQLIdentifierExpr) && !(value instanceof SQLPropertyExpr)) {
                        if (value instanceof SQLBinaryOpExpr) {
                            left = (SQLName)((SQLBinaryOpExpr)value).getLeft();
                            final SQLBinaryOperator operator = ((SQLBinaryOpExpr)value).getOperator();
                            final SQLExpr _right = ((SQLBinaryOpExpr)value).getRight();
                            if (!(_right instanceof SQLVariantRefExpr) && !(_right instanceof SQLLiteralExpr)) {
                                throw new TxcNotSupportException("txc暂不支持的sql语法");
                            }

                            String leftField = left.getSimpleName();
                            final String fieldToExtract;
                            if (!leftField.toLowerCase().equals(columnName)) {
                                TxcSqlParserProcessor.this.queryBuilder.select(new String[]{leftField});
                                fieldToExtract = leftField;
                            } else {
                                fieldToExtract = columnName;
                            }

                            TxcSqlParserProcessor.this.pipeline.addExtractor(new UpdateSetItemExtractor(TxcSqlParserProcessor.this.pipeline) {
                                private boolean type = _right instanceof SQLVariantRefExpr;

                                public void extract() {
                                    Object operateData = null;
                                    if (_right instanceof SQLVariantRefExpr) {
                                        int oldIndex = ((SQLVariantRefExpr)_right).getIndex();
                                        Object[] args = (Object[])((Object[])this.context.getParam("outter_args"));
                                        operateData = args[oldIndex];
                                    } else {
                                        if (!(_right instanceof SQLLiteralExpr)) {
                                            throw new TxcNotSupportException("不支持的sql语法");
                                        }

                                        operateData = ((SQLLiteralExpr)_right).toString();
                                    }

                                    Object value = this.getQueryColumn(fieldToExtract);
                                    Object result = AviatorEvaluator.execute(value + operator.getName() + operateData);
                                    this.addFinalArg(tmpIndexx, result);
                                }

                                public String desc() {
                                    return String.format("【WHERE】update语句存在主键更新,从原数据中获取列【%s】的值 作为左操作数，运算符为【%s】,%s", fieldToExtract, operator.getName(), this.type ? String.format("从原参数中获取下标为【%d】的参数做为右操作数", ((SQLVariantRefExpr)_right).getIndex()) : String.format("从原sql语句中获取【%s】做为右操作数", ((SQLLiteralExpr)_right).toString()));
                                }
                            });
                        }
                    } else {
                        left = (SQLName)value;
                        final String valueColumnName = left.getSimpleName().toLowerCase();
                        TxcSqlParserProcessor.this.queryBuilder.select(new String[]{valueColumnName});
                        TxcSqlParserProcessor.this.pipeline.addExtractor(new UpdateSetItemExtractor(TxcSqlParserProcessor.this.pipeline) {
                            public void extract() {
                                Object value = this.getQueryColumn(valueColumnName);
                                this.addFinalArg(tmpIndexx, value);
                            }

                            public String desc() {
                                return String.format("【WHERE】update语句存在主键更新,从原数据中获取列【%s】的值", valueColumnName);
                            }
                        });
                    }
                }

                if (this.newWhere4Update == null) {
                    this.newWhere4Update = new SQLBinaryOpExpr(new SQLIdentifierExpr(columnName), SQLBinaryOperator.Equality, right);
                } else {
                    this.newWhere4Update = new SQLBinaryOpExpr(this.newWhere4Update, SQLBinaryOperator.BooleanAnd, new SQLBinaryOpExpr(new SQLIdentifierExpr(columnName), SQLBinaryOperator.Equality, right));
                }
            }

            SQLExpr newValue = null;
            if (!(value instanceof SQLVariantRefExpr) && !(value instanceof SQLLiteralExpr) && !(value instanceof SQLMethodInvokeExpr) && !(value instanceof SQLIdentifierExpr) && !(value instanceof SQLPropertyExpr)) {
                if (value instanceof SQLBinaryOpExpr) {
                    final SQLBinaryOpExpr op = (SQLBinaryOpExpr)value;
                    if (!(op.getLeft() instanceof SQLName) || !((SQLName)op.getLeft()).getSimpleName().equals(columnName) || !(op.getRight() instanceof SQLNumericLiteralExpr) && !(op.getRight() instanceof SQLVariantRefExpr) || op.getOperator() != SQLBinaryOperator.Add && op.getOperator() != SQLBinaryOperator.Subtract) {
                        TxcSqlParserProcessor.this.queryBuilder.select(new String[]{columnName});
                        SQLVariantRefExpr tmpx = new SQLVariantRefExpr();
                        tmpx.setName("?");
                        tmpx.setParent(newItem);
                        tmpx.setIndex(this.index);
                        final int tmpIndexxx = this.index;
                        TxcSqlParserProcessor.this.pipeline.addExtractor(new UpdateSetItemExtractor(TxcSqlParserProcessor.this.pipeline) {
                            public void extract() {
                                Object value = this.getQueryColumn(columnName);
                                this.addFinalArg(tmpIndexxx, value);
                            }

                            public String desc() {
                                return String.format("【UPDATE】从原数据中获取列【%s】的值", columnName);
                            }
                        });
                        ++this.index;
                        newValue = tmpx;
                    } else {
                        SQLBinaryOpExpr newOp = new SQLBinaryOpExpr();
                        newOp.setLeft(op.getLeft());
                        final SQLBinaryOperator operator = op.getOperator();
                        if (operator == SQLBinaryOperator.Add) {
                            newOp.setOperator(SQLBinaryOperator.Subtract);
                        } else if (operator == SQLBinaryOperator.Subtract) {
                            newOp.setOperator(SQLBinaryOperator.Add);
                        }

                        if (op.getRight() instanceof SQLVariantRefExpr) {
                            SQLVariantRefExpr _v = new SQLVariantRefExpr();
                            _v.setName("?");
                            _v.setParent(newOp);
                            _v.setIndex(this.index);
                            final int tmpIndex = this.index;
                            TxcSqlParserProcessor.this.pipeline.addExtractor(new OriginalParamExtractor(TxcSqlParserProcessor.this.pipeline) {
                                public void extract() {
                                    Object[] args = (Object[])((Object[])this.context.getParam("outter_args"));
                                    Object value = args[((SQLVariantRefExpr)op.getRight()).getIndex()];
                                    this.addFinalArg(tmpIndex, value);
                                }

                                public String desc() {
                                    return String.format("【UPDATE】从原参数数组中获取下标【%d】的值", ((SQLVariantRefExpr)op.getRight()).getIndex());
                                }
                            });
                            ++this.index;
                            newOp.setRight(_v);
                        } else {
                            newOp.setRight(op.getRight());
                        }

                        newValue = newOp;
                    }
                }
            } else {
                TxcSqlParserProcessor.this.queryBuilder.select(new String[]{columnName});
                SQLVariantRefExpr tmp = new SQLVariantRefExpr();
                tmp.setName("?");
                tmp.setParent(newItem);
                tmp.setIndex(this.index);
                final int oldIndex = this.index;
                TxcSqlParserProcessor.this.pipeline.addExtractor(new UpdateSetItemExtractor(TxcSqlParserProcessor.this.pipeline) {
                    public void extract() {
                        Object value = this.getQueryColumn(columnName);
                        this.addFinalArg(oldIndex, value);
                    }

                    public String desc() {
                        return String.format("【UPDATE】从原数据中获取列【%s】的值", columnName);
                    }
                });
                ++this.index;
                newValue = tmp;
            }

            newItem.setValue((SQLExpr)newValue);
            TxcSqlParserProcessor.this.updateBuilder.getSQLUpdateStatement().addItem(newItem);
            return updateItem;
        }

        public void whereBinaryOpAspect(SQLVariantRefExpr right) {
            final int oldIndex = right.getIndex();
            right.setIndex(this.index);
            final int tmpIndex = this.index;
            TxcSqlParserProcessor.this.pipeline.addExtractor(new OriginalParamExtractor(TxcSqlParserProcessor.this.pipeline) {
                public void extract() {
                    Object[] args = (Object[])((Object[])this.context.getParam("outter_args"));
                    Object value = args[oldIndex];
                    this.addFinalArg(tmpIndex, value);
                }

                public String desc() {
                    return String.format("【QUERY-WHERE】从原参数数组中获取下标【%d】的值", oldIndex);
                }
            });
            ++this.index;
        }

        public void whereEnterPoint(SQLBinaryOpExpr x) {
            SQLSelectQueryBlock query = (SQLSelectQueryBlock)TxcSqlParserProcessor.this.queryBuilder.getSQLSelectStatement().getSelect().getQuery();
            query.setWhere(x);
            if (TxcSqlParserProcessor.this.updateBuilder != null) {
                TxcSqlParserProcessor.this.queryBuilder.select(new String[]{this.schema.getShardField()});
                Iterator var3 = this.schema.getPK().iterator();

                while(true) {
                    String primaryKey;
                    do {
                        if (!var3.hasNext()) {
                            if (this.schema.getShardField() != null) {
                                SQLVariantRefExpr right = new SQLVariantRefExpr("?");
                                right.setIndex(this.index);
                                final int tmpIndexx = this.index++;
                                this.newWhere4Update = new SQLBinaryOpExpr(this.newWhere4Update, SQLBinaryOperator.BooleanAnd, new SQLBinaryOpExpr(new SQLIdentifierExpr(this.schema.getShardField()), SQLBinaryOperator.Equality, right));
                                TxcSqlParserProcessor.this.pipeline.addExtractor(new UpdateSetItemExtractor(TxcSqlParserProcessor.this.pipeline) {
                                    public void extract() {
                                        Object value = this.getQueryColumn(TxcSQLASTVisitorAdivce.this.schema.getShardField());
                                        this.addFinalArg(tmpIndexx, value);
                                    }

                                    public String desc() {
                                        return String.format("【UPDATE-WHERE】从原数据中获取列【%s】的值作为回滚语句中的分库条件", TxcSQLASTVisitorAdivce.this.schema.getShardField());
                                    }
                                });
                            }

                            TxcSqlParserProcessor.this.result.primaryKeyExtractor = new PrimaryKeyExtractor(TxcSqlParserProcessor.this.pipeline) {
                                public List<KeyValuePair> extract() {
                                    List<KeyValuePair> result = new ArrayList();
                                    Iterator var2 = TxcSQLASTVisitorAdivce.this.schema.getPK().iterator();

                                    while(var2.hasNext()) {
                                        String column = (String)var2.next();
                                        KeyValuePair pair = new KeyValuePair(column, this.extractFromRecord(column));
                                        result.add(pair);
                                    }

                                    return result;
                                }

                                public String desc() {
                                    StringBuilder sbuilder = new StringBuilder(String.format("【UPDATE】从原数据中获取列【%s】的值作为txc反查主键条件", StringUtils.join(TxcSQLASTVisitorAdivce.this.schema.getPK(), ",")));
                                    if (TxcSQLASTVisitorAdivce.this.schema.getShardField() == null) {
                                        sbuilder.append(";无法解析到分库字段配置");
                                    } else {
                                        sbuilder.append(String.format(";从原数据中获取列【%s】的值作为分库字段值", TxcSQLASTVisitorAdivce.this.schema.getShardField()));
                                    }

                                    return sbuilder.toString();
                                }

                                public String shard() {
                                    return TxcSQLASTVisitorAdivce.this.schema.getShardField() == null ? null : this.extractFromRecord(TxcSQLASTVisitorAdivce.this.schema.getShardField()).toString();
                                }
                            };
                            TxcSqlParserProcessor.this.updateBuilder.getSQLUpdateStatement().setWhere(this.newWhere4Update);
                            return;
                        }

                        primaryKey = (String)var3.next();
                        TxcSqlParserProcessor.this.queryBuilder.select(new String[]{primaryKey});
                    } while(this.alreadyDealPrimaryKeyStrs != null && this.alreadyDealPrimaryKeyStrs.contains(primaryKey));

                    SQLVariantRefExpr rightx = new SQLVariantRefExpr("?");
                    rightx.setIndex(this.index);
                    final int tmpIndex = this.index++;
                    final String primaryKeyF = primaryKey;
                    TxcSqlParserProcessor.this.pipeline.addExtractor(new UpdateSetItemExtractor(TxcSqlParserProcessor.this.pipeline) {
                        public void extract() {
                            Object value = this.getQueryColumn(primaryKeyF);
                            this.addFinalArg(tmpIndex, value);
                        }

                        public String desc() {
                            return String.format("【UPDATE-WHERE】从原数据中获取列【%s】的值作为回滚语句中的主键条件", primaryKeyF);
                        }
                    });
                    if (this.newWhere4Update == null) {
                        this.newWhere4Update = new SQLBinaryOpExpr(new SQLIdentifierExpr(primaryKey), SQLBinaryOperator.Equality, rightx);
                    } else {
                        this.newWhere4Update = new SQLBinaryOpExpr(this.newWhere4Update, SQLBinaryOperator.BooleanAnd, new SQLBinaryOpExpr(new SQLIdentifierExpr(primaryKey), SQLBinaryOperator.Equality, rightx));
                    }
                }
            }
        }
    }
}

