package com.github.bluecatlee.gs4d.common.datasource;

import com.alibaba.druid.filter.FilterAdapter;
import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import com.alibaba.druid.proxy.jdbc.PreparedStatementProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.github.bluecatlee.gs4d.common.utils.MyJsonMapper;
import com.github.bluecatlee.gs4d.common.model.Parameter;
import com.github.bluecatlee.gs4d.common.model.SqlAndParamters;
import com.github.bluecatlee.gs4d.common.model.SycDetailInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class JdbcListennerAplication extends FilterAdapter {
    private static MyJsonMapper mapper = new MyJsonMapper();
    private static Logger log = LoggerFactory.getLogger(JdbcListennerAplication.class);
    private Long dataSign = 1L;
    @Value("#{settings['filter.tablename']}")
    private String filterTableName;
    private String[] tableName = null;
    private boolean initFlag = false;

    public JdbcListennerAplication() {
    }

    private void initSyncTable() {
        if (!this.initFlag) {
            this.initFlag = true;
            if (this.filterTableName != null) {
                this.tableName = this.filterTableName.toLowerCase().split(",");
            }
        }

    }

    public int statement_executeUpdate(FilterChain chain, StatementProxy statement, String sql) throws SQLException {
        String tempSql = sql.toLowerCase();
        this.initSyncTable();
        String[] tableNameArr = this.tableName;
        int len = tableNameArr.length;

        for(int i = 0; i < len; ++i) {
            String name = tableNameArr[i];
            if (tempSql.contains(name)) {
                SqlAndParamters sp = new SqlAndParamters();
                List<Parameter> parameterList = new ArrayList();
                Iterator iterator = statement.getParameters().keySet().iterator();

                while(iterator.hasNext()) {
                    Integer integer = (Integer)iterator.next();
                    Parameter parameter = new Parameter();
                    if (((JdbcParameter)statement.getParameters().get(integer)).getValue() instanceof Date) {
                        parameter.setSequece(integer);
                        parameter.setType(1L);
                        parameter.setValue(((JdbcParameter)statement.getParameters().get(integer)).getValue());
                        parameterList.add(parameter);
                    } else {
                        parameter.setSequece(integer);
                        parameter.setType(0L);
                        parameter.setValue(((JdbcParameter)statement.getParameters().get(integer)).getValue());
                        parameterList.add(parameter);
                    }
                }

                sp.setParamterList(parameterList);
                sp.setSql(sql);
                SycDetailInfo sycDetailInfo = new SycDetailInfo();
                sycDetailInfo.setDataSign(1L);
                sycDetailInfo.setTenantNumId(1L);
                sycDetailInfo.setSp(sp);
                log.debug(mapper.toJson(sycDetailInfo));
//                SimpleMessage message = new SimpleMessage("OR1_005", "OR1_005_8", name, mapper.toJson(sycDetailInfo), 11L, this.dataSign, 1L);
//                MessageSendUtil.sendSimpleMessageRightNow(message);
            }
        }

        return super.statement_executeUpdate(chain, statement, sql);
    }

    public int preparedStatement_executeUpdate(FilterChain chain, PreparedStatementProxy statement) throws SQLException {
        String sql = statement.getSql().toLowerCase();
        this.initSyncTable();
        String[] tableNameArr = this.tableName;
        int len = tableNameArr.length;

        for(int i = 0; i < len; ++i) {
            String name = tableNameArr[i];
            if (sql.contains(name)) {
                SqlAndParamters sp = new SqlAndParamters();
                List<Parameter> parameterList = new ArrayList();
                Iterator iterator = statement.getParameters().keySet().iterator();

                while(iterator.hasNext()) {
                    Integer integer = (Integer)iterator.next();
                    Parameter parameter = new Parameter();
                    if (((JdbcParameter)statement.getParameters().get(integer)).getValue() instanceof Date) {
                        parameter.setSequece(integer);
                        parameter.setType(1L);
                        parameter.setValue(((JdbcParameter)statement.getParameters().get(integer)).getValue());
                        parameterList.add(parameter);
                    } else {
                        parameter.setSequece(integer);
                        parameter.setType(0L);
                        parameter.setValue(((JdbcParameter)statement.getParameters().get(integer)).getValue());
                        parameterList.add(parameter);
                    }
                }

                System.out.println(mapper.toJson(parameterList.toString()));
                sp.setSql(statement.getSql());
                sp.setParamterList(parameterList);
                SycDetailInfo sycDetailInfo = new SycDetailInfo();
                sycDetailInfo.setDataSign(1L);
                sycDetailInfo.setTenantNumId(1L);
                sycDetailInfo.setSp(sp);
                log.debug(mapper.toJson(sycDetailInfo));
//                SimpleMessage message = new SimpleMessage("OR1_005", "OR1_005_8", name, mapper.toJson(sycDetailInfo), 11L, this.dataSign, 1L);
//                if (statement.getConnection().getAutoCommit()) {
//                    MessageSendUtil.sendSimpleMessageRightNow(message);
//                } else {
//                    MessageSendUtil.sendPrepMsgUseThreadLocal(message);
//                }
            }
        }

        return super.preparedStatement_executeUpdate(chain, statement);
    }

    static {
        mapper.getMapper().setPropertyNamingStrategy(PropertyNamingStrategy.LowerCaseStrategy.SNAKE_CASE);
    }
}

