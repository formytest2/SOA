package com.github.bluecatlee.gs4d.sequence.service;

import com.github.bluecatlee.gs4d.sequence.exception.SequenceException;
import com.github.bluecatlee.gs4d.sequence.utils.SeqStringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 子序列服务
 *      子序列就是在序列后追加三位标识subId(附加了业务相关性 便于数据库分片)
 */
public class SubSequenceService {
    private SequenceService seq = new SequenceService();

    public SubSequenceService() {
    }

    public String getSequence(String systemName, String SeqName, String subId) throws SequenceException {
        if (IniteZkConfigService.zk == null) {
            throw new SequenceException("请先初始化zookeeper");
        } else if (subId == null) {
            throw new SequenceException("分库号不能为null");
        } else {
            if (subId.toString().length() >= 3) {
                // 如果长度大于3 则取最后三位
                subId = subId.toString().substring(subId.toString().length() - 3, subId.toString().length());
            } else {
                // 否则 左填充0 直到满足长度为3
                subId = SeqStringUtil.leftPad(subId.toString(), 3, '0');
            }

            Long nosubseq = this.seq.getNoSubSequence(systemName, SeqName); // 调用序列服务
            String newSeq = this.makeSeqFunction(nosubseq, subId);
            return newSeq;
        }
    }

    private String makeSeqFunction(Long nosubseq, String subId) {
        return nosubseq.toString() + subId;
    }

    public List<String> getSequenceBatch(String systemName, String seqName, String routeId, Integer size) {
        List<String> serieses = new ArrayList();

        for(int i = 0; i < size; ++i) {
            serieses.add(this.getSequence(systemName, seqName, routeId));
        }

        return serieses;
    }

    public Long getNoDateSequence(String systemName, String seqName) throws SequenceException {
        if (IniteZkConfigService.zk == null) {
            throw new SequenceException("请先初始化zookeeper");
        } else {
            Long nosubseq = this.seq.getNoSubSequence(systemName, seqName);
            String newYear = nosubseq.toString().substring(1, 2); // 0,2 ？
            String newMonth = String.valueOf(Integer.valueOf(nosubseq.toString().substring(2, 4)) + 55);
            String newDay = String.valueOf(Integer.valueOf(nosubseq.toString().substring(4, 6)) + 21);
            String newSeq = nosubseq.toString().substring(6);
            return Long.valueOf(newYear + newMonth + newDay + newSeq);
        }
    }

}
