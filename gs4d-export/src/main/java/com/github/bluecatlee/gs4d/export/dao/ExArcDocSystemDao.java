package com.github.bluecatlee.gs4d.export.dao;

import com.github.bluecatlee.gs4d.common.datasource.DataSourceContextHolder;
import com.github.bluecatlee.gs4d.common.exception.DatabaseOperateException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.export.utils.ExportUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class ExArcDocSystemDao {

    @Resource(name = "dynamicJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据sysNumId查系统的数据源
     * @param tenantNumId
     * @param dataSign
     * @param sysNumId
     * @return
     */
    public String getDatasourceNameBySysNumId(Long tenantNumId, Long dataSign, Long sysNumId) {
        DataSourceContextHolder.clearDataSourceType();
        DataSourceContextHolder.setDataSourceType("platformProductionDataSource");
        String sql = "select datasouce_name from ex_arc_doc_system where tenant_num_id=? and data_sign=? and system_num_id=?";
        List list = this.jdbcTemplate.queryForList(sql, new Object[]{tenantNumId, dataSign, sysNumId}, String.class);
        if (list != null && list.size() != 0) {
            return (String)list.get(0);
        } else {
            throw new DatabaseOperateException(ExportUtil.SUB_SYSTEM, ExceptionType.DOE30052, "查询表ex_arc_doc_system失败!系统编号:" + sysNumId);
        }
    }

    /**
     * 消息发送日志series回查
     * @param series
     * @param datasourceName
     * @return
     */
    public String refindSysMsgTransRefindId(String series, String datasourceName) {
        DataSourceContextHolder.clearDataSourceType();
        DataSourceContextHolder.setDataSourceType(datasourceName);
        String sql = "select series from sys_msg_trans_refind_id where  series=?";
        List list = this.jdbcTemplate.queryForList(sql, new Object[]{series}, String.class);
        return list != null && list.size() != 0 ? (String)list.get(0) : null;
    }

}

