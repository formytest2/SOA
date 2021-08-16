package com.github.bluecatlee.gs4d.sequence.dao;

import java.util.Date;

public interface SequenceTimeDao {

    void insertTime(String paramString, Date paramDate) throws Exception;

    String getSequence() throws Exception;

    void updateTime(String paramString, Date paramDate) throws Exception;

    Integer getCount() throws Exception;

}
