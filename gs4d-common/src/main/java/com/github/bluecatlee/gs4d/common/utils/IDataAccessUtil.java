package com.github.bluecatlee.gs4d.common.utils;

import java.util.Collection;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

class IDataAccessUtil {

    public static <T> T requiredSingleResult(Collection<T> results) throws IncorrectResultSizeDataAccessException {
        int size = results != null ? results.size() : 0;
        if (size == 0) {
            return null;
        } else if (results.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, size);
        } else {
            return results.iterator().next();
        }
    }
}

