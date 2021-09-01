package com.tranboot.client.druid.wall;

import java.util.Set;

public interface WallConfigMBean {
    boolean isInited();

    String getDir();

    void setDir(String var1);

    void init();

    void loadConfig(String var1);

    boolean isNoneBaseStatementAllow();

    void setNoneBaseStatementAllow(boolean var1);

    boolean isTruncateAllow();

    void setTruncateAllow(boolean var1);

    boolean isSelelctAllow();

    void setSelelctAllow(boolean var1);

    boolean isSelectIntoAllow();

    void setSelectIntoAllow(boolean var1);

    boolean isSelectIntoOutfileAllow();

    void setSelectIntoOutfileAllow(boolean var1);

    boolean isSelectUnionCheck();

    void setSelectUnionCheck(boolean var1);

    boolean isSelectWhereAlwayTrueCheck();

    void setSelectWhereAlwayTrueCheck(boolean var1);

    boolean isSelectHavingAlwayTrueCheck();

    void setSelectHavingAlwayTrueCheck(boolean var1);

    boolean isDeleteAllow();

    void setDeleteAllow(boolean var1);

    boolean isDeleteWhereAlwayTrueCheck();

    void setDeleteWhereAlwayTrueCheck(boolean var1);

    boolean isUpdateAllow();

    void setUpdateAllow(boolean var1);

    boolean isUpdateWhereAlayTrueCheck();

    void setUpdateWhereAlayTrueCheck(boolean var1);

    boolean isInsertAllow();

    void setInsertAllow(boolean var1);

    boolean isMergeAllow();

    void setMergeAllow(boolean var1);

    boolean isMultiStatementAllow();

    void setMultiStatementAllow(boolean var1);

    boolean isSchemaCheck();

    void setSchemaCheck(boolean var1);

    boolean isTableCheck();

    void setTableCheck(boolean var1);

    boolean isFunctionCheck();

    void setFunctionCheck(boolean var1);

    boolean isVariantCheck();

    void setVariantCheck(boolean var1);

    boolean isObjectCheck();

    void setObjectCheck(boolean var1);

    boolean isCommentAllow();

    void setCommentAllow(boolean var1);

    Set<String> getDenyFunctions();

    Set<String> getDenyTables();

    Set<String> getDenySchemas();

    Set<String> getDenyVariants();

    Set<String> getDenyObjects();

    Set<String> getReadOnlyTables();

    boolean isDenyObjects(String var1);

    boolean isDenySchema(String var1);

    boolean isDenyFunction(String var1);
}
