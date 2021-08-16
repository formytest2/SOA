package com.github.bluecatlee.gs4d.sequence.service;

import com.github.bluecatlee.gs4d.sequence.exception.SequenceException;

/**
 * 从序列中获取subId
 */
public class SubIdGetService {
    public SubIdGetService() {
    }

    public Long getSubIdBySeq(String seq) {
        Long subId = null;

        try {
            subId = Long.valueOf(seq.substring(seq.length() - 3, seq.length()));
            return subId;
        } catch (Exception e) {
            throw new SequenceException("传入的序列号至少三位且最后三位必须是数字");
        }
    }
}

