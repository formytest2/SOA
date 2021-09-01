package com.tranboot.client.druid.util;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcSqlStatUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcSqlStatUtils.class);

    public JdbcSqlStatUtils() {
    }

    public static long[] rtrim(long[] array) {
        int notZeroLen = array.length;

        for(int i = array.length - 1; i >= 0 && array[i] == 0L; --notZeroLen) {
            --i;
        }

        if (notZeroLen != array.length) {
            array = Arrays.copyOf(array, notZeroLen);
        }

        return array;
    }

    public static <T> int get(T stat, AtomicIntegerFieldUpdater<T> updater, boolean reset) {
        return reset ? updater.getAndSet(stat, 0) : updater.get(stat);
    }

    public static <T> long get(T stat, AtomicLongFieldUpdater<T> updater, boolean reset) {
        return reset ? updater.getAndSet(stat, 0L) : updater.get(stat);
    }

    public static long get(AtomicLong counter, boolean reset) {
        return reset ? counter.getAndSet(0L) : counter.get();
    }

    public static int get(AtomicInteger counter, boolean reset) {
        return reset ? counter.getAndSet(0) : counter.get();
    }
}
