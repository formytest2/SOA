package com.github.bluecatlee.gs4d.sequence.dao.impl;

import com.github.bluecatlee.gs4d.common.utils.MyJdbcTemplate;
import com.github.bluecatlee.gs4d.sequence.dao.SequenceTimeDao;
import com.github.bluecatlee.gs4d.sequence.utils.SqlUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;

@Repository
public class SequenceTimeDaoImpl implements SequenceTimeDao {

    @Resource(name = "jdbcTemplate")
    public MyJdbcTemplate jdbcTemplate;

    @Value("#{settings['mycatGoMaster']}")
    private String mycatGoMaster;

    public void insertTime(String paramString, Date paramDate) throws Exception {
        String str = "insert into PLATFORM_SEQUENCE_TIME(SEQUENCE_TIME,LAST_UPDTME)";
        Object[] arrayOfObject = { paramString, paramDate };
        str = str + SqlUtil.genSqlValues(arrayOfObject.length);
        this.jdbcTemplate.update(this.mycatGoMaster + str, arrayOfObject);
    }

    public String getSequence() throws Exception {
        String str = "select SEQUENCE_TIME from PLATFORM_SEQUENCE_TIME limit 1";
        return (String)this.jdbcTemplate.queryForObject(this.mycatGoMaster + str, String.class);
    }

    public void updateTime(String paramString, Date paramDate) throws Exception {
        String str = "update PLATFORM_SEQUENCE_TIME set SEQUENCE_TIME = ?,LAST_UPDTME = ?";
        this.jdbcTemplate.update(this.mycatGoMaster + str, new Object[] { paramString, paramDate });
    }

    public Integer getCount() throws Exception {
        String str = "select count(1) from PLATFORM_SEQUENCE_TIME";
        return (Integer)this.jdbcTemplate.queryForObject(this.mycatGoMaster + str, Integer.class);
    }
}
