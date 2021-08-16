package com.github.bluecatlee.gs4d.sequence.service;

import java.util.ArrayList;
import java.util.List;

import com.github.bluecatlee.gs4d.sequence.exception.SequenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 序列服务
 */
public class SequenceService {
    protected static Logger logger = LoggerFactory.getLogger(SequenceService.class);
    private SequenceCliActionService scli = new SequenceCliActionService();

    public SequenceService() {
    }

    public Long getNoSubSequence(String systemName, String SeqName) throws SequenceException {
        return this.scli.getSequence(systemName, SeqName);
    }

    public List<Object> getNoSubSequenceBath(String systemName, String seqName, Integer size) {
        List<Object> serieses = new ArrayList();

        for(int i = 0; i < size; ++i) {
            serieses.add(this.getNoSubSequence(systemName, seqName));
        }

        return serieses;
    }
}

