package com.github.bluecatlee.gs4d.sequence.dao;

import com.github.bluecatlee.gs4d.sequence.model.CreateSequence;

import java.util.List;

public interface SequenceDao {

    void insertSeq(CreateSequence paramCreateSequence) throws Exception;

    List<CreateSequence> getSequence(CreateSequence paramCreateSequence) throws Exception;

    void updateSeqValnum(CreateSequence paramCreateSequence) throws Exception;

    void updateSeqValnumAndSeqnum(CreateSequence paramCreateSequence);

    Integer getCountBy(String paramString1, String paramString2) throws Exception;

    void updateCurrentNum(Long paramLong1, Long paramLong2);

    void updateAllCurrentNum();

    List<CreateSequence> getSequenceWithCurrentNum();

    void updateCurrentVal(Long paramLong, String paramString);

    Integer getSeqStoreStatus(String paramString) throws Exception;

    Integer updateSeqStartAndEndNum(String paramString, Long paramLong1, Long paramLong2) throws Exception;
}
