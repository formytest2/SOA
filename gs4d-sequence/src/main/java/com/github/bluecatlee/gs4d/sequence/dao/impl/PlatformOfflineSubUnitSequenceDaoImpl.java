package com.github.bluecatlee.gs4d.sequence.dao.impl;

import com.github.bluecatlee.gs4d.common.utils.MyJdbcTemplate;
import com.github.bluecatlee.gs4d.sequence.dao.PlatformOfflineSubUnitSequenceDao;
import com.github.bluecatlee.gs4d.sequence.model.PlatformOfflineSequence;
import com.github.bluecatlee.gs4d.sequence.model.PlatformOfflineSubUnitSequence;
import com.github.bluecatlee.gs4d.sequence.utils.SqlUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class PlatformOfflineSubUnitSequenceDaoImpl implements PlatformOfflineSubUnitSequenceDao {

    @Resource(name = "jdbcTemplate")
    public MyJdbcTemplate jdbcTemplate;

    @Value("#{settings['mycatGoMaster']}")
    private String mycatGoMaster;

    public void insertOfflineSeq(PlatformOfflineSubUnitSequence parame) throws Exception {
        String str = "insert into platform_offline_sub_unit_sequence(SERIES,SEQ_NAME,start_num,end_num,sub_unit_num_id)";
        Object[] arrayOfObject = { parame.getSeries(), parame.getSeqName(), parame.getStartNum(), parame.getEndNum(), parame.getSubUnitNumId() };
        str = str + SqlUtil.genSqlValues(arrayOfObject.length);
        this.jdbcTemplate.update(this.mycatGoMaster + str, arrayOfObject);
    }

    public List<PlatformOfflineSubUnitSequence> getOfflineSubUnitSequence(Long paramLong) {
        String str = "select * from platform_offline_sub_unit_sequence where sub_unit_num_id = ? LIMIT 10000";
        return this.jdbcTemplate.query(this.mycatGoMaster + str, new Object[] { paramLong }, (RowMapper)new BeanPropertyRowMapper(PlatformOfflineSubUnitSequence.class));
    }

    public List<PlatformOfflineSequence> getOfflineSubSequence() throws Exception {
        String str = "select * from platform_offline_sequence LIMIT 10000";
        return this.jdbcTemplate.query(this.mycatGoMaster + str, (RowMapper)new BeanPropertyRowMapper(PlatformOfflineSequence.class));
    }

    public void updateOfflineCurrentNum(String paramString) throws Exception {
        String str = "update platform_offline_sequence set offline_current_num = offline_current_num + offline_get_num_count + 1  where seq_name = ?";
        this.jdbcTemplate.update(this.mycatGoMaster + str, new Object[] { paramString });
    }

    public List<String> getOfflineSubUnitSeqName(Long paramLong) throws Exception {
        String str = "select distinct seq_name from platform_offline_sub_unit_sequence where sub_unit_num_id = ? LIMIT 10000";
        return this.jdbcTemplate.queryForList(this.mycatGoMaster + str, new Object[] { paramLong }, String.class);
    }

    public PlatformOfflineSequence getOfflineSeqModel(String paramString) throws Exception {
        String str = "select * from platform_offline_sequence where seq_name = ? limit 1";
        return (PlatformOfflineSequence)this.jdbcTemplate.queryForObject(this.mycatGoMaster + str, new Object[] { paramString }, (RowMapper)new BeanPropertyRowMapper(PlatformOfflineSequence.class));
    }
}
