package com.tranboot.client.druid.wall;

import java.sql.SQLException;
import java.util.Set;

public interface WallFilterMBean {
    String getDbType();

    boolean isLogViolation();

    void setLogViolation(boolean var1);

    boolean isThrowException();

    void setThrowException(boolean var1);

    boolean isInited();

    void clearProviderCache();

    Set<String> getProviderWhiteList();

    String check(String var1) throws SQLException;

    long getViolationCount();

    void resetViolationCount();

    void clearWhiteList();

    boolean checkValid(String var1);
}
