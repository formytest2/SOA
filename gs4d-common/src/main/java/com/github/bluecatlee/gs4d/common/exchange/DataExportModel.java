package com.github.bluecatlee.gs4d.common.exchange;

import java.util.List;

public class DataExportModel {
    public static final Long DATA_TYPE_OF_OBJECT = 0L;
    public static final Long DATA_TYPE_OF_ARY = 1L;
    public static final Long PROCCESS_TYPE_OF_FOREACH = 1L;
    public static final Long PROCCESS_TYPE_OF_COMM = 0L;
    public static final String DATA_EXCEPTION_YES = "Y";
    public static final String DATA_EXCEPTION_NO = "N";
    public static final String DATA_CHECK_YES = "Y";
    public static final String DATA_CHECK_NO = "N";
    private String exportName;
    private Long proccessType;
    private String sqlId;
    private Long dataType;
    private String noDataException = "Y";
    private String noDataCheck = "Y";
    private String useParallel = "N";
    private List<AddParamModel> addParam;
    private List<ForeachInputModel> foreachInput;
    private List<DataExportModel> childExport;
    private List<MessageSendModel> messageSendModel;

    public DataExportModel() {
    }

    public String getExportName() {
        return this.exportName;
    }

    public void setExportName(String exportName) {
        this.exportName = exportName;
    }

    public Long getProccessType() {
        return this.proccessType;
    }

    public void setProccessType(Long proccessType) {
        this.proccessType = proccessType;
    }

    public String getSqlId() {
        return this.sqlId;
    }

    public void setSqlId(String sqlId) {
        this.sqlId = sqlId;
    }

    public Long getDataType() {
        return this.dataType;
    }

    public void setDataType(Long dataType) {
        this.dataType = dataType;
    }

    public String getNoDataException() {
        return this.noDataException;
    }

    public void setNoDataException(String noDataException) {
        this.noDataException = noDataException;
    }

    public String getNoDataCheck() {
        return this.noDataCheck;
    }

    public void setNoDataCheck(String noDataCheck) {
        this.noDataCheck = noDataCheck;
    }

    public List<AddParamModel> getAddParam() {
        return this.addParam;
    }

    public void setAddParam(List<AddParamModel> addParam) {
        this.addParam = addParam;
    }

    public List<ForeachInputModel> getForeachInput() {
        return this.foreachInput;
    }

    public void setForeachInput(List<ForeachInputModel> foreachInput) {
        this.foreachInput = foreachInput;
    }

    public List<DataExportModel> getChildExport() {
        return this.childExport;
    }

    public void setChildExport(List<DataExportModel> childExport) {
        this.childExport = childExport;
    }

    public List<MessageSendModel> getMessageSendModel() {
        return this.messageSendModel;
    }

    public void setMessageSendModel(List<MessageSendModel> messageSendModel) {
        this.messageSendModel = messageSendModel;
    }

    public String getUseParallel() {
        return this.useParallel;
    }

    public void setUseParallel(String useParallel) {
        this.useParallel = useParallel;
    }
}
