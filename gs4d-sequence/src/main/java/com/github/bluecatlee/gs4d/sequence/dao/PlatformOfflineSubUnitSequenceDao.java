package com.github.bluecatlee.gs4d.sequence.dao;

import com.github.bluecatlee.gs4d.sequence.model.PlatformOfflineSequence;
import com.github.bluecatlee.gs4d.sequence.model.PlatformOfflineSubUnitSequence;

import java.util.List;

public interface PlatformOfflineSubUnitSequenceDao {

    void insertOfflineSeq(PlatformOfflineSubUnitSequence parame) throws Exception;

    List<PlatformOfflineSubUnitSequence> getOfflineSubUnitSequence(Long paramLong) throws Exception;

    List<String> getOfflineSubUnitSeqName(Long paramLong) throws Exception;

    List<PlatformOfflineSequence> getOfflineSubSequence() throws Exception;

    void updateOfflineCurrentNum(String paramString) throws Exception;

    PlatformOfflineSequence getOfflineSeqModel(String paramString) throws Exception;

}
