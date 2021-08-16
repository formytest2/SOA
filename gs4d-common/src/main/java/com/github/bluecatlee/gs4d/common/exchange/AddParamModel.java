package com.github.bluecatlee.gs4d.common.exchange;

public class AddParamModel {
    private String paramName;
    private String fromType;
    private String seqStoreName;
    private String oldValueFromKey;
    private String mappingName;
    private String inputParamName;
    private String storeMappingName;
    private String parentNode;
    private String constValue;
    public static final String FROM_TYPE_OF_SEQ = "seq";
    public static final String FROM_TYPE_OF_INPUT = "input";
    public static final String FROM_TYPE_OF_MAPPING = "mapping";
    public static final String FROM_TYPE_OF_CONST = "const";

    public AddParamModel() {
    }

    public String getParamName() {
        return this.paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getFromType() {
        return this.fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public String getSeqStoreName() {
        return this.seqStoreName;
    }

    public void setSeqStoreName(String seqStoreName) {
        this.seqStoreName = seqStoreName;
    }

    public String getOldValueFromKey() {
        return this.oldValueFromKey;
    }

    public void setOldValueFromKey(String oldValueFromKey) {
        this.oldValueFromKey = oldValueFromKey;
    }

    public String getMappingName() {
        return this.mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }

    public String getStoreMappingName() {
        return this.storeMappingName;
    }

    public void setStoreMappingName(String storeMappingName) {
        this.storeMappingName = storeMappingName;
    }

    public String getParentNode() {
        return this.parentNode;
    }

    public void setParentNode(String parentNode) {
        this.parentNode = parentNode;
    }

    public String getConstValue() {
        return this.constValue;
    }

    public void setConstValue(String constValue) {
        this.constValue = constValue;
    }

    public String getInputParamName() {
        return this.inputParamName;
    }

    public void setInputParamName(String inputParamName) {
        this.inputParamName = inputParamName;
    }
}
