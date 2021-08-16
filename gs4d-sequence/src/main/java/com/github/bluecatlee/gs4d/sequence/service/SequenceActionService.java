package com.github.bluecatlee.gs4d.sequence.service;

import com.github.bluecatlee.gs4d.sequence.model.CreateSequence;

import java.util.List;
import java.util.Map;

public interface SequenceActionService {

    CreateSequence getSequenceToClient(CreateSequence createSequence);

    List<CreateSequence> getSequenceModelToClientCheck(CreateSequence createSequence);

    String getAutomicSeq(String seqName, Integer num, Long tenantNumId, Long dataSign);

    Integer getSeqStoreStatus(String seqName);

    @Deprecated
    List<Map<String, Object>> getOfflineSeqInfo(Long subUnitNumId);
}
