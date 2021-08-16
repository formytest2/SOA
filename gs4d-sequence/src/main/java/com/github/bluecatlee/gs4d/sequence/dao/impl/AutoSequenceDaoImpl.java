package com.github.bluecatlee.gs4d.sequence.dao.impl;

import com.github.bluecatlee.gs4d.common.utils.MyJdbcTemplate;
import com.github.bluecatlee.gs4d.sequence.dao.AutoSequenceDao;
import com.github.bluecatlee.gs4d.sequence.model.AutoSequence;
import com.github.bluecatlee.gs4d.sequence.model.PlatformAutoSequence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class AutoSequenceDaoImpl implements AutoSequenceDao {

    @Resource(name = "jdbcTemplate")
    public MyJdbcTemplate jdbcTemplate;

    @Value("#{settings['mycatGoMaster']}")
    private String mycatGoMaster;

    public List<PlatformAutoSequence> getAutoSequenceInfo(String seqName, Long tenantNumId, Long dataSign) {
        String str = "select * from platform_auto_sequence  where seq_name = ? and TENANT_NUM_ID = ? and DATA_SIGN = ? order by create_time desc LIMIT 10000";
        Object[] arrayOfObject = {seqName, tenantNumId, dataSign};
        return this.jdbcTemplate.query(this.mycatGoMaster + str, arrayOfObject, (RowMapper)new BeanPropertyRowMapper(PlatformAutoSequence.class));
    }

    public void updateAutoCurrentVal(Long currentNum, Long series) {
        String str = "UPDATE platform_auto_sequence SET current_num=? where series = ?";
        Object[] arrayOfObject = {currentNum, series};
        int i = this.jdbcTemplate.update(this.mycatGoMaster + str, arrayOfObject);
        if (i < 1) {
            throw new RuntimeException("更新自增序列失败！");
        }
    }

    public void updateAutoCurrentNum() {
        String str = "UPDATE platform_auto_sequence SET current_num=init_value where is_clear = 6";
        this.jdbcTemplate.update(this.mycatGoMaster + str);
    }

    public List<AutoSequence> getClearAutoSeq() {
        String str = "select SERIES,TENANT_NUM_ID,DATA_SIGN,SEQ_NAME from platform_auto_sequence  where is_clear = 6 LIMIT 10000";
        return this.jdbcTemplate.query(this.mycatGoMaster + str, (RowMapper)new BeanPropertyRowMapper(AutoSequence.class));
    }

}

