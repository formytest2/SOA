package com.github.bluecatlee.gs4d.sequence.dao;

import com.github.bluecatlee.gs4d.sequence.model.AutoSequence;
import com.github.bluecatlee.gs4d.sequence.model.PlatformAutoSequence;

import java.util.List;

public interface AutoSequenceDao {

    List<PlatformAutoSequence> getAutoSequenceInfo(String seqName, Long tenantNumId, Long dataSign);

    void updateAutoCurrentVal(Long currentNum, Long series);

    void updateAutoCurrentNum();

    List<AutoSequence> getClearAutoSeq();

}
