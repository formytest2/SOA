package com.tranboot.client.model.txc;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.tranboot.client.model.SQLType;
import com.tranboot.client.sqlast.MySQLRewriteVistorAop;
import com.tranboot.client.sqlast.SQLASTVisitorAspectAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * TxcSQL转换器
 */
public class TxcSQLTransformer {
    public static final String TXC_FILED = "txc";
    private TxcSQLTransformer.TxcSQLTransform result = new TxcSQLTransformer.TxcSQLTransform();

    public TxcSQLTransformer() {
    }

    public TxcSQLTransformer.TxcSQLTransform transform(String originalSql) throws Exception {
        try {
            SQLStatement statement = (SQLStatement) SQLUtils.parseStatements(this.cleanSql(originalSql), "mysql").get(0);
            StringBuilder sbuilder = new StringBuilder();
            TxcSQLTransformer.SQLTransformHandler handler = new TxcSQLTransformer.SQLTransformHandler();
            statement.accept(new MySQLRewriteVistorAop((List)null, sbuilder, handler));
            handler.firstStepFinish();
            String sql = sbuilder.toString();
            this.result.sql = sql;
            sbuilder = new StringBuilder();
            statement = (SQLStatement)SQLUtils.parseStatements(sql, "mysql").get(0);
            statement.accept(new MySQLRewriteVistorAop((List)null, sbuilder, handler));
            return this.result;
        } catch (Exception var6) {
            throw var6;
        }
    }

    // 清理掉sql中的mycat注解
    protected String cleanSql(String sql) {
        return sql.indexOf("mycat") > -1 ? StringUtils.substringAfterLast(sql, "*/") : sql;
    }

    public static class TxcSQLTransform {
        public String sql;
        public int additionIndex;
        public SQLType sqlType;

        public TxcSQLTransform() {
        }

        public Object[] addArgs(Object[] args, String txc) {
            List<Object> aggs = new ArrayList();

            int i;
            for(i = 0; i < this.additionIndex; ++i) {
                aggs.add(args[i]);
            }

            aggs.add(txc);

            for(i = this.additionIndex; i < args.length; ++i) {
                aggs.add(args[i]);
            }

            return aggs.toArray(new Object[0]);
        }

        public static String process4BatchUpd(String sql, String txc) {
            SQLStatement statement = (SQLStatement)SQLUtils.parseStatements(sql, "mysql").get(0);
            StringBuilder sbuilder = new StringBuilder();
            statement.accept(new MySQLRewriteVistorAop(Collections.singletonList(txc), sbuilder, new SQLASTVisitorAspectAdapter()));
            return sbuilder.toString();
        }
    }

    class SQLTransformHandler extends SQLASTVisitorAspectAdapter {
        boolean firstStepFinish = false;

        SQLTransformHandler() {
        }

        public void firstStepFinish() {
            this.firstStepFinish = true;
        }

        public void updateEnterPoint(SQLUpdateStatement updateStatement) {
            TxcSQLTransformer.this.result.sqlType = SQLType.UPDATE;
            if (this.firstStepFinish) {
                TxcSQLTransformer.this.result.additionIndex = ((SQLVariantRefExpr)((SQLUpdateSetItem)updateStatement.getItems().get(updateStatement.getItems().size() - 1)).getValue()).getIndex();
            } else {
                String alias = updateStatement.getTableSource().getAlias();
                SQLUpdateSetItem txcUpd = new SQLUpdateSetItem();
                txcUpd.setParent(updateStatement);
                if (alias == null) {
                    txcUpd.setColumn(new SQLIdentifierExpr("txc"));
                } else {
                    SQLPropertyExpr property = new SQLPropertyExpr();
                    SQLIdentifierExpr owner = new SQLIdentifierExpr(alias);
                    owner.setParent(property);
                    property.setOwner(owner);
                    property.setName("txc");
                    txcUpd.setColumn(property);
                }

                SQLVariantRefExpr v = new SQLVariantRefExpr("?");
                txcUpd.setValue(v);
                updateStatement.getItems().add(txcUpd);
            }
        }

        public void insertEnterPoint(SQLInsertStatement insertStatement) {
            TxcSQLTransformer.this.result.sqlType = SQLType.INSERT;
            if (this.firstStepFinish) {
                TxcSQLTransformer.this.result.additionIndex = ((SQLVariantRefExpr)insertStatement.getValues().getValues().get(insertStatement.getValues().getValues().size() - 1)).getIndex();
            } else {
                List<SQLExpr> columns = insertStatement.getColumns();
                SQLInsertStatement.ValuesClause values = insertStatement.getValues();
                List<SQLExpr> _values = values.getValues();
                SQLIdentifierExpr txcField = new SQLIdentifierExpr("txc");
                columns.add(txcField);
                _values.add(new SQLVariantRefExpr("?"));
            }
        }

        public void deleteEnterPoint(SQLDeleteStatement deleteStatement) {
            TxcSQLTransformer.this.result.sqlType = SQLType.DELETE;
        }
    }
}

