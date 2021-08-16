package com.github.bluecatlee.gs4d.export.api.service;

import com.github.bluecatlee.gs4d.export.api.request.DataCheckRequest;
import com.github.bluecatlee.gs4d.export.api.request.DataExportRequest;
import com.github.bluecatlee.gs4d.export.api.request.MessageCommonRefoundRequest;
import com.github.bluecatlee.gs4d.export.api.response.CommonExcuteBySqlIdResponse;
import com.github.bluecatlee.gs4d.export.api.response.DataCheckResponse;
import com.github.bluecatlee.gs4d.export.api.response.DataExportResponse;
import com.github.bluecatlee.gs4d.export.api.request.CommonExcuteBySqlIdRequest;
import com.github.bluecatlee.gs4d.export.api.response.MessageCommonRefoundResponse;

public interface ExportDataService {
    
//    DataExportResponse exportData(DataExportRequest request);

    DataCheckResponse checkData(DataCheckRequest request);

//    ExportBillNoReceiptResponse receiptExportBillNo(ExportBillNoReceiptRequest request);

    CommonExcuteBySqlIdResponse commonExcuteBySqlId(CommonExcuteBySqlIdRequest request);

//    CommonBatchUpdateExcuteResponse excuteCommonBatchUpdate(CommonBatchUpdateExcuteRequest request);
//
    MessageCommonRefoundResponse messageCommonRefound(MessageCommonRefoundRequest request);
//
//    SequenceClientSeriesGetResponse getNextSequence(SequenceClientSeriesGetRequest request);
//
//    SequenceSeriesListGetResponse getListSequences(SequenceClientSeriesGetRequest request);
//
//    CcoreAutoGrowSequenceResponse getAutoGrowSequence(CcoreAutoGrowSequenceRequest request);
}
