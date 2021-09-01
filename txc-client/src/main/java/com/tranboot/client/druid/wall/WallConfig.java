package com.tranboot.client.druid.wall;

import com.tranboot.client.druid.util.Utils;
import com.tranboot.client.druid.wall.spi.WallVisitorUtils;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class WallConfig implements WallConfigMBean {
    private boolean noneBaseStatementAllow = false;
    private boolean callAllow = true;
    private boolean selelctAllow = true;
    private boolean selectIntoAllow = true;
    private boolean selectIntoOutfileAllow = false;
    private boolean selectWhereAlwayTrueCheck = true;
    private boolean selectHavingAlwayTrueCheck = true;
    private boolean selectUnionCheck = true;
    private boolean selectMinusCheck = true;
    private boolean selectExceptCheck = true;
    private boolean selectIntersectCheck = true;
    private boolean createTableAllow = true;
    private boolean dropTableAllow = true;
    private boolean alterTableAllow = true;
    private boolean renameTableAllow = true;
    private boolean hintAllow = true;
    private boolean lockTableAllow = true;
    private boolean startTransactionAllow = true;
    private boolean blockAllow = true;
    private boolean conditionAndAlwayTrueAllow = false;
    private boolean conditionAndAlwayFalseAllow = false;
    private boolean conditionDoubleConstAllow = false;
    private boolean conditionLikeTrueAllow = true;
    private boolean selectAllColumnAllow = true;
    private boolean deleteAllow = true;
    private boolean deleteWhereAlwayTrueCheck = true;
    private boolean deleteWhereNoneCheck = false;
    private boolean updateAllow = true;
    private boolean updateWhereAlayTrueCheck = true;
    private boolean updateWhereNoneCheck = false;
    private boolean insertAllow = true;
    private boolean mergeAllow = true;
    private boolean minusAllow = true;
    private boolean intersectAllow = true;
    private boolean replaceAllow = true;
    private boolean setAllow = true;
    private boolean commitAllow = true;
    private boolean rollbackAllow = true;
    private boolean useAllow = true;
    private boolean multiStatementAllow = false;
    private boolean truncateAllow = true;
    private boolean commentAllow = false;
    private boolean strictSyntaxCheck = true;
    private boolean constArithmeticAllow = true;
    private boolean limitZeroAllow = false;
    private boolean describeAllow = true;
    private boolean showAllow = true;
    private boolean schemaCheck = true;
    private boolean tableCheck = true;
    private boolean functionCheck = true;
    private boolean objectCheck = true;
    private boolean variantCheck = true;
    private boolean mustParameterized = false;
    private boolean doPrivilegedAllow = false;
    protected final Set<String> denyFunctions = new ConcurrentSkipListSet();
    protected final Set<String> denyTables = new ConcurrentSkipListSet();
    protected final Set<String> denySchemas = new ConcurrentSkipListSet();
    protected final Set<String> denyVariants = new ConcurrentSkipListSet();
    protected final Set<String> denyObjects = new ConcurrentSkipListSet();
    protected final Set<String> permitFunctions = new ConcurrentSkipListSet();
    protected final Set<String> permitTables = new ConcurrentSkipListSet();
    protected final Set<String> permitSchemas = new ConcurrentSkipListSet();
    protected final Set<String> permitVariants = new ConcurrentSkipListSet();
    protected final Set<String> readOnlyTables = new ConcurrentSkipListSet();
    private String dir;
    private boolean inited;
    private String tenantTablePattern;
    private String tenantColumn;
    private WallConfig.TenantCallBack tenantCallBack;
    private boolean wrapAllow = true;
    private boolean metadataAllow = true;
    private boolean conditionOpXorAllow = false;
    private boolean conditionOpBitwseAllow = true;
    private boolean caseConditionConstAllow = false;
    private boolean completeInsertValuesCheck = false;
    private int insertValuesCheckSize = 3;

    public WallConfig() {
        this.configFromProperties(System.getProperties());
    }

    public boolean isCaseConditionConstAllow() {
        return this.caseConditionConstAllow;
    }

    public void setCaseConditionConstAllow(boolean caseConditionConstAllow) {
        this.caseConditionConstAllow = caseConditionConstAllow;
    }

    public boolean isConditionDoubleConstAllow() {
        return this.conditionDoubleConstAllow;
    }

    public void setConditionDoubleConstAllow(boolean conditionDoubleConstAllow) {
        this.conditionDoubleConstAllow = conditionDoubleConstAllow;
    }

    public boolean isConditionLikeTrueAllow() {
        return this.conditionLikeTrueAllow;
    }

    public void setConditionLikeTrueAllow(boolean conditionLikeTrueAllow) {
        this.conditionLikeTrueAllow = conditionLikeTrueAllow;
    }

    public boolean isLimitZeroAllow() {
        return this.limitZeroAllow;
    }

    public void setLimitZeroAllow(boolean limitZero) {
        this.limitZeroAllow = limitZero;
    }

    public boolean isUseAllow() {
        return this.useAllow;
    }

    public void setUseAllow(boolean useAllow) {
        this.useAllow = useAllow;
    }

    public boolean isCommitAllow() {
        return this.commitAllow;
    }

    public void setCommitAllow(boolean commitAllow) {
        this.commitAllow = commitAllow;
    }

    public boolean isRollbackAllow() {
        return this.rollbackAllow;
    }

    public void setRollbackAllow(boolean rollbackAllow) {
        this.rollbackAllow = rollbackAllow;
    }

    public boolean isIntersectAllow() {
        return this.intersectAllow;
    }

    public void setIntersectAllow(boolean intersectAllow) {
        this.intersectAllow = intersectAllow;
    }

    public boolean isMinusAllow() {
        return this.minusAllow;
    }

    public void setMinusAllow(boolean minusAllow) {
        this.minusAllow = minusAllow;
    }

    public boolean isConditionOpXorAllow() {
        return this.conditionOpXorAllow;
    }

    public void setConditionOpXorAllow(boolean conditionOpXorAllow) {
        this.conditionOpXorAllow = conditionOpXorAllow;
    }

    public String getTenantTablePattern() {
        return this.tenantTablePattern;
    }

    public void setTenantTablePattern(String tenantTablePattern) {
        this.tenantTablePattern = tenantTablePattern;
    }

    public String getTenantColumn() {
        return this.tenantColumn;
    }

    public void setTenantColumn(String tenantColumn) {
        this.tenantColumn = tenantColumn;
    }

    public WallConfig.TenantCallBack getTenantCallBack() {
        return this.tenantCallBack;
    }

    public void setTenantCallBack(WallConfig.TenantCallBack tenantCallBack) {
        this.tenantCallBack = tenantCallBack;
    }

    public boolean isMetadataAllow() {
        return this.metadataAllow;
    }

    public void setMetadataAllow(boolean metadataAllow) {
        this.metadataAllow = metadataAllow;
    }

    public boolean isWrapAllow() {
        return this.wrapAllow;
    }

    public void setWrapAllow(boolean wrapAllow) {
        this.wrapAllow = wrapAllow;
    }

    public boolean isDoPrivilegedAllow() {
        return this.doPrivilegedAllow;
    }

    public void setDoPrivilegedAllow(boolean doPrivilegedAllow) {
        this.doPrivilegedAllow = doPrivilegedAllow;
    }

    public boolean isSelectAllColumnAllow() {
        return this.selectAllColumnAllow;
    }

    public void setSelectAllColumnAllow(boolean selectAllColumnAllow) {
        this.selectAllColumnAllow = selectAllColumnAllow;
    }

    public boolean isInited() {
        return this.inited;
    }

    public WallConfig(String dir) {
        this.dir = dir;
        this.init();
    }

    public String getDir() {
        return this.dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public final void init() {
        this.loadConfig(this.dir);
    }

    public void loadConfig(String dir) {
        if (dir.endsWith("/")) {
            dir = dir.substring(0, dir.length() - 1);
        }

        WallVisitorUtils.loadResource(this.denyVariants, dir + "/deny-variant.txt");
        WallVisitorUtils.loadResource(this.denySchemas, dir + "/deny-schema.txt");
        WallVisitorUtils.loadResource(this.denyFunctions, dir + "/deny-function.txt");
        WallVisitorUtils.loadResource(this.denyTables, dir + "/deny-table.txt");
        WallVisitorUtils.loadResource(this.denyObjects, dir + "/deny-object.txt");
        WallVisitorUtils.loadResource(this.readOnlyTables, dir + "/readonly-table.txt");
        WallVisitorUtils.loadResource(this.permitFunctions, dir + "/permit-function.txt");
        WallVisitorUtils.loadResource(this.permitTables, dir + "/permit-table.txt");
        WallVisitorUtils.loadResource(this.permitSchemas, dir + "/permit-schema.txt");
        WallVisitorUtils.loadResource(this.permitVariants, dir + "/permit-variant.txt");
    }

    public boolean isNoneBaseStatementAllow() {
        return this.noneBaseStatementAllow;
    }

    public void setNoneBaseStatementAllow(boolean noneBaseStatementAllow) {
        this.noneBaseStatementAllow = noneBaseStatementAllow;
    }

    public boolean isDescribeAllow() {
        return this.describeAllow;
    }

    public void setDescribeAllow(boolean describeAllow) {
        this.describeAllow = describeAllow;
    }

    public boolean isShowAllow() {
        return this.showAllow;
    }

    public void setShowAllow(boolean showAllow) {
        this.showAllow = showAllow;
    }

    public boolean isTruncateAllow() {
        return this.truncateAllow;
    }

    public void setTruncateAllow(boolean truncateAllow) {
        this.truncateAllow = truncateAllow;
    }

    public boolean isSelectIntoAllow() {
        return this.selectIntoAllow;
    }

    public void setSelectIntoAllow(boolean selectIntoAllow) {
        this.selectIntoAllow = selectIntoAllow;
    }

    public boolean isSelectIntoOutfileAllow() {
        return this.selectIntoOutfileAllow;
    }

    public void setSelectIntoOutfileAllow(boolean selectIntoOutfileAllow) {
        this.selectIntoOutfileAllow = selectIntoOutfileAllow;
    }

    public boolean isCreateTableAllow() {
        return this.createTableAllow;
    }

    public void setCreateTableAllow(boolean createTableAllow) {
        this.createTableAllow = createTableAllow;
    }

    public boolean isDropTableAllow() {
        return this.dropTableAllow;
    }

    public void setDropTableAllow(boolean dropTableAllow) {
        this.dropTableAllow = dropTableAllow;
    }

    public boolean isAlterTableAllow() {
        return this.alterTableAllow;
    }

    public void setAlterTableAllow(boolean alterTableAllow) {
        this.alterTableAllow = alterTableAllow;
    }

    public boolean isRenameTableAllow() {
        return this.renameTableAllow;
    }

    public void setRenameTableAllow(boolean renameTableAllow) {
        this.renameTableAllow = renameTableAllow;
    }

    public boolean isSelectUnionCheck() {
        return this.selectUnionCheck;
    }

    public void setSelectUnionCheck(boolean selectUnionCheck) {
        this.selectUnionCheck = selectUnionCheck;
    }

    public boolean isSelectMinusCheck() {
        return this.selectMinusCheck;
    }

    public void setSelectMinusCheck(boolean selectMinusCheck) {
        this.selectMinusCheck = selectMinusCheck;
    }

    public boolean isSelectExceptCheck() {
        return this.selectExceptCheck;
    }

    public void setSelectExceptCheck(boolean selectExceptCheck) {
        this.selectExceptCheck = selectExceptCheck;
    }

    public boolean isSelectIntersectCheck() {
        return this.selectIntersectCheck;
    }

    public void setSelectIntersectCheck(boolean selectIntersectCheck) {
        this.selectIntersectCheck = selectIntersectCheck;
    }

    public boolean isDeleteAllow() {
        return this.deleteAllow;
    }

    public void setDeleteAllow(boolean deleteAllow) {
        this.deleteAllow = deleteAllow;
    }

    public boolean isDeleteWhereNoneCheck() {
        return this.deleteWhereNoneCheck;
    }

    public void setDeleteWhereNoneCheck(boolean deleteWhereNoneCheck) {
        this.deleteWhereNoneCheck = deleteWhereNoneCheck;
    }

    public boolean isUpdateAllow() {
        return this.updateAllow;
    }

    public void setUpdateAllow(boolean updateAllow) {
        this.updateAllow = updateAllow;
    }

    public boolean isUpdateWhereNoneCheck() {
        return this.updateWhereNoneCheck;
    }

    public void setUpdateWhereNoneCheck(boolean updateWhereNoneCheck) {
        this.updateWhereNoneCheck = updateWhereNoneCheck;
    }

    public boolean isInsertAllow() {
        return this.insertAllow;
    }

    public void setInsertAllow(boolean insertAllow) {
        this.insertAllow = insertAllow;
    }

    public boolean isReplaceAllow() {
        return this.replaceAllow;
    }

    public void setReplaceAllow(boolean replaceAllow) {
        this.replaceAllow = replaceAllow;
    }

    public boolean isSetAllow() {
        return this.setAllow;
    }

    public void setSetAllow(boolean value) {
        this.setAllow = value;
    }

    public boolean isMergeAllow() {
        return this.mergeAllow;
    }

    public void setMergeAllow(boolean mergeAllow) {
        this.mergeAllow = mergeAllow;
    }

    public boolean isMultiStatementAllow() {
        return this.multiStatementAllow;
    }

    public void setMultiStatementAllow(boolean multiStatementAllow) {
        this.multiStatementAllow = multiStatementAllow;
    }

    public boolean isSchemaCheck() {
        return this.schemaCheck;
    }

    public void setSchemaCheck(boolean schemaCheck) {
        this.schemaCheck = schemaCheck;
    }

    public boolean isTableCheck() {
        return this.tableCheck;
    }

    public void setTableCheck(boolean tableCheck) {
        this.tableCheck = tableCheck;
    }

    public boolean isFunctionCheck() {
        return this.functionCheck;
    }

    public void setFunctionCheck(boolean functionCheck) {
        this.functionCheck = functionCheck;
    }

    public boolean isVariantCheck() {
        return this.variantCheck;
    }

    public void setVariantCheck(boolean variantCheck) {
        this.variantCheck = variantCheck;
    }

    public boolean isObjectCheck() {
        return this.objectCheck;
    }

    public void setObjectCheck(boolean objectCheck) {
        this.objectCheck = objectCheck;
    }

    public boolean isCommentAllow() {
        return this.commentAllow;
    }

    public void setCommentAllow(boolean commentAllow) {
        this.commentAllow = commentAllow;
    }

    public boolean isStrictSyntaxCheck() {
        return this.strictSyntaxCheck;
    }

    public void setStrictSyntaxCheck(boolean strictSyntaxCheck) {
        this.strictSyntaxCheck = strictSyntaxCheck;
    }

    public boolean isConstArithmeticAllow() {
        return this.constArithmeticAllow;
    }

    public void setConstArithmeticAllow(boolean constArithmeticAllow) {
        this.constArithmeticAllow = constArithmeticAllow;
    }

    public Set<String> getDenyFunctions() {
        return this.denyFunctions;
    }

    public Set<String> getDenyTables() {
        return this.denyTables;
    }

    public Set<String> getDenySchemas() {
        return this.denySchemas;
    }

    public Set<String> getDenyVariants() {
        return this.denyVariants;
    }

    public Set<String> getDenyObjects() {
        return this.denyObjects;
    }

    public Set<String> getReadOnlyTables() {
        return this.readOnlyTables;
    }

    public void addReadOnlyTable(String tableName) {
        this.readOnlyTables.add(tableName);
    }

    public boolean isReadOnly(String tableName) {
        return this.readOnlyTables.contains(tableName);
    }

    public Set<String> getPermitFunctions() {
        return this.permitFunctions;
    }

    public Set<String> getPermitTables() {
        return this.permitTables;
    }

    public Set<String> getPermitSchemas() {
        return this.permitSchemas;
    }

    public Set<String> getPermitVariants() {
        return this.permitVariants;
    }

    public boolean isMustParameterized() {
        return this.mustParameterized;
    }

    public void setMustParameterized(boolean mustParameterized) {
        this.mustParameterized = mustParameterized;
    }

    public boolean isDenyObjects(String name) {
        if (!this.objectCheck) {
            return false;
        } else {
            name = WallVisitorUtils.form(name);
            return this.denyObjects.contains(name);
        }
    }

    public boolean isDenySchema(String name) {
        if (!this.schemaCheck) {
            return false;
        } else {
            name = WallVisitorUtils.form(name);
            return this.denySchemas.contains(name);
        }
    }

    public boolean isDenyFunction(String name) {
        if (!this.functionCheck) {
            return false;
        } else {
            name = WallVisitorUtils.form(name);
            return this.denyFunctions.contains(name);
        }
    }

    public boolean isCallAllow() {
        return this.callAllow;
    }

    public void setCallAllow(boolean callAllow) {
        this.callAllow = callAllow;
    }

    public boolean isHintAllow() {
        return this.hintAllow;
    }

    public void setHintAllow(boolean hintAllow) {
        this.hintAllow = hintAllow;
    }

    public boolean isSelelctAllow() {
        return this.selelctAllow;
    }

    public void setSelelctAllow(boolean selelctAllow) {
        this.selelctAllow = selelctAllow;
    }

    public boolean isSelectWhereAlwayTrueCheck() {
        return this.selectWhereAlwayTrueCheck;
    }

    public void setSelectWhereAlwayTrueCheck(boolean selectWhereAlwayTrueCheck) {
        this.selectWhereAlwayTrueCheck = selectWhereAlwayTrueCheck;
    }

    public boolean isSelectHavingAlwayTrueCheck() {
        return this.selectHavingAlwayTrueCheck;
    }

    public void setSelectHavingAlwayTrueCheck(boolean selectHavingAlwayTrueCheck) {
        this.selectHavingAlwayTrueCheck = selectHavingAlwayTrueCheck;
    }

    public boolean isConditionAndAlwayTrueAllow() {
        return this.conditionAndAlwayTrueAllow;
    }

    public void setConditionAndAlwayTrueAllow(boolean conditionAndAlwayTrueAllow) {
        this.conditionAndAlwayTrueAllow = conditionAndAlwayTrueAllow;
    }

    public boolean isConditionAndAlwayFalseAllow() {
        return this.conditionAndAlwayFalseAllow;
    }

    public void setConditionAndAlwayFalseAllow(boolean conditionAndAlwayFalseAllow) {
        this.conditionAndAlwayFalseAllow = conditionAndAlwayFalseAllow;
    }

    public boolean isDeleteWhereAlwayTrueCheck() {
        return this.deleteWhereAlwayTrueCheck;
    }

    public void setDeleteWhereAlwayTrueCheck(boolean deleteWhereAlwayTrueCheck) {
        this.deleteWhereAlwayTrueCheck = deleteWhereAlwayTrueCheck;
    }

    public boolean isUpdateWhereAlayTrueCheck() {
        return this.updateWhereAlayTrueCheck;
    }

    public void setUpdateWhereAlayTrueCheck(boolean updateWhereAlayTrueCheck) {
        this.updateWhereAlayTrueCheck = updateWhereAlayTrueCheck;
    }

    public boolean isConditionOpBitwseAllow() {
        return this.conditionOpBitwseAllow;
    }

    public void setConditionOpBitwseAllow(boolean conditionOpBitwseAllow) {
        this.conditionOpBitwseAllow = conditionOpBitwseAllow;
    }

    public void setInited(boolean inited) {
        this.inited = inited;
    }

    public boolean isLockTableAllow() {
        return this.lockTableAllow;
    }

    public void setLockTableAllow(boolean lockTableAllow) {
        this.lockTableAllow = lockTableAllow;
    }

    public boolean isStartTransactionAllow() {
        return this.startTransactionAllow;
    }

    public void setStartTransactionAllow(boolean startTransactionAllow) {
        this.startTransactionAllow = startTransactionAllow;
    }

    public boolean isCompleteInsertValuesCheck() {
        return this.completeInsertValuesCheck;
    }

    public void setCompleteInsertValuesCheck(boolean completeInsertValuesCheck) {
        this.completeInsertValuesCheck = completeInsertValuesCheck;
    }

    public int getInsertValuesCheckSize() {
        return this.insertValuesCheckSize;
    }

    public void setInsertValuesCheckSize(int insertValuesCheckSize) {
        this.insertValuesCheckSize = insertValuesCheckSize;
    }

    public boolean isBlockAllow() {
        return this.blockAllow;
    }

    public void setBlockAllow(boolean blockAllow) {
        this.blockAllow = blockAllow;
    }

    public void configFromProperties(Properties properties) {
        String str = properties.getProperty("druid.wall.tenantColumn");
        if (str != null) {
            this.setTenantColumn(str);
        }

        Boolean propertyValue = Utils.getBoolean(properties, "druid.wall.selelctAllow");
        if (propertyValue != null) {
            this.setSelelctAllow(propertyValue);
        }

        propertyValue = Utils.getBoolean(properties, "druid.wall.updateAllow");
        if (propertyValue != null) {
            this.setUpdateAllow(propertyValue);
        }

        propertyValue = Utils.getBoolean(properties, "druid.wall.deleteAllow");
        if (propertyValue != null) {
            this.setDeleteAllow(propertyValue);
        }

        propertyValue = Utils.getBoolean(properties, "druid.wall.insertAllow");
        if (propertyValue != null) {
            this.setInsertAllow(propertyValue);
        }

        propertyValue = Utils.getBoolean(properties, "druid.wall.multiStatementAllow");
        if (propertyValue != null) {
            this.setMultiStatementAllow(propertyValue);
        }

    }

    public interface TenantCallBack {
        Object getTenantValue(WallConfig.TenantCallBack.StatementType var1, String var2);

        String getTenantColumn(WallConfig.TenantCallBack.StatementType var1, String var2);

        String getHiddenColumn(String var1);

        void filterResultsetTenantColumn(Object var1);

        public static enum StatementType {
            SELECT,
            UPDATE,
            INSERT,
            DELETE;

            private StatementType() {
            }
        }
    }
}
