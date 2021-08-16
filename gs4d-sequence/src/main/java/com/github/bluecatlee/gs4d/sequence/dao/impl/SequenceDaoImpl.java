package com.github.bluecatlee.gs4d.sequence.dao.impl;

import com.github.bluecatlee.gs4d.common.utils.MyJdbcTemplate;
import com.github.bluecatlee.gs4d.sequence.dao.SequenceDao;
import com.github.bluecatlee.gs4d.sequence.model.CreateSequence;
import com.github.bluecatlee.gs4d.sequence.utils.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class SequenceDaoImpl implements SequenceDao {

    @Resource(name = "jdbcTemplate")
    public MyJdbcTemplate jdbcTemplate;

    @Value("#{settings['mycatGoMaster']}")
    private String mycatGoMaster;

    protected static Logger logger = LoggerFactory.getLogger(SequenceDaoImpl.class);

    public void insertSeq(CreateSequence paramCreateSequence) throws Exception {
        String str = "insert into PLATFORM_SEQUENCE(SERIES,SEQ_NAME,SEQ_PROJECT,SEQ_PREFIX,SEQ_NUM,SEQ_VAL,CURRENT_NUM,CREATE_TIME,SEQ_NUM_START,SEQ_NUM_END)";
        Object[] arrayOfObject = { paramCreateSequence.getSeries(), paramCreateSequence.getSeqName(), paramCreateSequence.getSeqProject(), paramCreateSequence.getSeqPrefix(), paramCreateSequence.getSeqNum(), paramCreateSequence.getSeqVal(), paramCreateSequence.getCurrentNum(), paramCreateSequence.getCreateTime(), paramCreateSequence.getSeqNumStart(), paramCreateSequence.getSeqNumEnd() };
        str = str + SqlUtil.genSqlValues(arrayOfObject.length);
        this.jdbcTemplate.update(str, arrayOfObject);
    }

    public void updateSeqValnum(CreateSequence paramCreateSequence) {
        String str = this.mycatGoMaster + "update PLATFORM_SEQUENCE set CURRENT_NUM = ? where SERIES = ? and CURRENT_NUM = ?";
        int i = this.jdbcTemplate.update(str, new Object[] { paramCreateSequence.getCurrentNum(), paramCreateSequence.getSeries(), Long.valueOf(paramCreateSequence.getCurrentNum().longValue() - 1L) });
        logger.info("主从同库" + this.mycatGoMaster);
        logger.info("sql语句===" + str);
        if (i < 1) {
            throw new RuntimeException("CURRENT_NUM值更新失败");
        }
    }

    public void updateSeqValnumAndSeqnum(CreateSequence paramCreateSequence) {
        String str = this.mycatGoMaster + "update PLATFORM_SEQUENCE set CURRENT_NUM = ?,SEQ_NUM = ? where SEQ_NAME = ?";
        this.jdbcTemplate.update(str, new Object[] { paramCreateSequence.getCurrentNum(), paramCreateSequence.getSeqNum(), paramCreateSequence.getSeqName() });
    }

    public List<CreateSequence> getSequence(CreateSequence paramCreateSequence) {
        String str = this.mycatGoMaster + "select series,SEQ_NAME seqName,SEQ_PROJECT seqProject,SEQ_PREFIX seqPrefix,SEQ_NUM seqNum,SEQ_VAL seqVal,CURRENT_NUM currentNum,SEQ_NUM_START seqNumStart,SEQ_NUM_END seqNumEnd,disrupt,is_store_local isStoreLocal from PLATFORM_SEQUENCE where SEQ_NAME = '" + paramCreateSequence.getSeqName() + "'";
        return this.jdbcTemplate.query(str, (RowMapper)new BeanPropertyRowMapper(CreateSequence.class));
    }

    public Integer getCountBy(String paramString1, String paramString2) throws Exception {
        String str = this.mycatGoMaster + "select count(*) from PLATFORM_SEQUENCE where SEQ_NAME = ?";
        return (Integer)this.jdbcTemplate.queryForObject(str, new Object[] { paramString2 }, Integer.class);
    }

    public void updateCurrentNum(Long paramLong1, Long paramLong2) {
        String str = this.mycatGoMaster + "UPDATE PLATFORM_SEQUENCE SET CURRENT_NUM=? where SERIES=?";
        this.jdbcTemplate.update(str, new Object[] { paramLong1, paramLong2 });
    }

    public List<CreateSequence> getSequenceWithCurrentNum() {
        String str = this.mycatGoMaster + "select SERIES,SEQ_NAME seqName,SEQ_NUM_START seqNumStart from PLATFORM_SEQUENCE WHERE SEQ_NUM_START IS NOT NULL LIMIT 10000";
        return this.jdbcTemplate.query(str, (RowMapper)new BeanPropertyRowMapper(CreateSequence.class));
    }

    public void updateAllCurrentNum() {
        String str = this.mycatGoMaster + "UPDATE PLATFORM_SEQUENCE SET CURRENT_NUM=1 WHERE SEQ_NUM_START is null";
        this.jdbcTemplate.update(str);
    }

    public void updateCurrentVal(Long paramLong, String paramString) {
        String str = this.mycatGoMaster + "UPDATE PLATFORM_SEQUENCE SET SEQ_NUM_END=? where SEQ_NAME=?";
        int i = this.jdbcTemplate.update(str, new Object[] { paramLong, paramString });
        if (i < 1) {
            throw new RuntimeException("更新失败");
        }
    }

    public Integer getSeqStoreStatus(String paramString) throws Exception {
        String str = this.mycatGoMaster + "select is_store_local from PLATFORM_SEQUENCE where SEQ_NAME = ? limit 1";
        return (Integer)this.jdbcTemplate.queryForObject(str, new Object[] { paramString }, Integer.class);
    }

    public Integer updateSeqStartAndEndNum(String paramString, Long paramLong1, Long paramLong2) throws Exception {
        String str = this.mycatGoMaster + "update PLATFORM_SEQUENCE set SEQ_NUM_START = ?,SEQ_NUM_END = ? where SEQ_NAME = ?";
        int i = this.jdbcTemplate.update(str, new Object[] { paramLong1, paramLong2, paramString });
        logger.info("主从同库：" + this.mycatGoMaster);
        logger.info("sql语句===" + str);
        return Integer.valueOf(i);
    }
}
