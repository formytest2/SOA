package com.github.bluecatlee.gs4d.common.utils;

public class BatchUtil {

    /**
     * 获取批次数
     * @param totalCount    总数量
     * @param batchSize     每批数量
     * @return
     */
    public static Long batch(Long totalCount, Long batchSize) {
        Long batchCount = 0L;
        if (totalCount > batchSize) {
            Long mod = totalCount % batchSize;
            batchCount = totalCount / batchSize;
            if (mod > 0L) {
                batchCount = batchCount + 1L;
            }
        } else if (totalCount > 0L && totalCount <= batchSize) {
            batchCount = 1L;
        }

        return batchCount;
    }
}

