package com.github.bluecatlee.gs4d.message.consumer.dao;

import com.github.bluecatlee.gs4d.common.exception.DatabaseOperateException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.message.api.model.MonitorModel;
import com.github.bluecatlee.gs4d.message.api.model.TopicConfigModel;
import com.github.bluecatlee.gs4d.message.consumer.utils.Constants;
import com.github.bluecatlee.gs4d.message.consumer.utils.SeqUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExArcDocSystemDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String querySystemNumIdBySystemNameSql = "select system_num_id systemId from ex_arc_doc_system where system_name=? and tenant_num_id=?";
    private static final String queryAllSystemNamesSql = "select system_name systemName from ex_arc_doc_system where  tenant_num_id=? group by system_name limit 100000";
    private static final String querySystemNameAndIdSql = "select e.system_name systemName,e.system_num_id systemId from ex_arc_doc_system e INNER JOIN platform_mq_topic p on e.system_num_id=p.system_num_id where e.tenant_num_id=? and p.cancelsign='N' group by e.system_name ,e.system_num_id";
    private static final String queryBySystemNumIdSql = "select system_num_id systemId from ex_arc_doc_system where system_num_id=? and tenant_num_Id=? ";
    private static final String queryEmailByTopicTagSql = "select e.email_address emailAddress from ex_arc_doc_system e INNER JOIN platform_mq_topic p on e.system_num_id=p.system_num_id where p.topic=? and p.tag=?";
    private static final String insertSql = "insert into ex_arc_doc_system(system_num_id,tenant_num_id,data_sign,email_address,system_name,create_dtme,last_updtme,create_user_id,last_update_user_id)values(?,?,0,'',?,sysdate(),sysdate(),0,0)";

    public Long querySystemNumIdBySystemName(String systemName, Long tenantNumId) {
        List list = this.jdbcTemplate.query(querySystemNumIdBySystemNameSql, new Object[]{systemName, tenantNumId}, new BeanPropertyRowMapper(MonitorModel.class));
        if (CollectionUtils.isEmpty(list)) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30045, "根据中文查询英文名失败!");
        } else {
            return ((MonitorModel)list.get(0)).getSystemId();
        }
    }

    public List<TopicConfigModel> queryAllSystemNames(Long tenantNumId) {
        List list = this.jdbcTemplate.query(queryAllSystemNamesSql, new Object[]{tenantNumId}, new BeanPropertyRowMapper(TopicConfigModel.class));
        return list;
    }

    public List<MonitorModel> querySystemNameAndId(Long tenantNumId) {
        List list = this.jdbcTemplate.query(querySystemNameAndIdSql, new Object[]{tenantNumId}, new BeanPropertyRowMapper(MonitorModel.class));
        return list;
    }

    public List<MonitorModel> query(Long systemNumId, Long series, Long tenantNumId) {
        String sql = "select a.system_name systemName,a.system_num_id systemId,b.topic ,b.series,b.remark from ex_arc_doc_system a left join platform_mq_topic b on a.system_num_id=b.system_num_id where a.system_num_id=? and a.tenant_num_id=? ";
        ArrayList args = new ArrayList();
        args.add(systemNumId);
        args.add(tenantNumId);
        if (StringUtil.isAllNotNullOrBlank(new Object[]{series})) {
            args.add(series);
            sql = sql + " and b.series=?";
        }

        List list = this.jdbcTemplate.query(sql, args.toArray(), new BeanPropertyRowMapper(MonitorModel.class));
        return list;
    }

    public List<MonitorModel> queryBySystemNumId(String systemNumId, Long tenantNumId) {
        List list = this.jdbcTemplate.query(queryBySystemNumIdSql, new Object[]{systemNumId, tenantNumId}, new BeanPropertyRowMapper(MonitorModel.class));
        return list;
    }

    public List<MonitorModel> queryEmailByTopicTag(String topic, String tag) {
        List list = this.jdbcTemplate.query(queryEmailByTopicTagSql, new Object[]{topic, tag}, new BeanPropertyRowMapper(MonitorModel.class));
        return list;
    }

    public void insert(String systemName, Long tenantNumId) {
        Long series = SeqUtil.getNoSubSequence(SeqUtil.EX_ARC_DOC_SYSTEM_ID);
        if (this.jdbcTemplate.update(insertSql, new Object[]{series, tenantNumId, systemName}) <= 0) {
            throw new RuntimeException("插入失败!");
        }
    }

    public void querySystemNumIdBySystemNameStrict(String systemName, Long tenantNumId) {
        List list = this.jdbcTemplate.query(querySystemNumIdBySystemNameSql, new Object[]{systemName, tenantNumId}, new BeanPropertyRowMapper(MonitorModel.class));
        if (!CollectionUtils.isEmpty(list)) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30045, "根据中文查询英文名失败!");
        }
    }
}

