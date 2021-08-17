package com.github.bluecatlee.gs4d.transaction.api.service;

import com.github.bluecatlee.gs4d.transaction.api.request.*;
import com.github.bluecatlee.gs4d.transaction.api.response.*;

public interface TransactionWebService {

    TransactionGetResponse getTransaction(TransactionGetRequest paramTransactionGetRequest);

    TransactionDetailGetResponse getTransactionDetail(TransactionDetailGetRequest paramTransactionDetailGetRequest);

    TransactionStateListGetResponse getTransactionStateList(TransactionStateListGetRequest paramTransactionStateListGetRequest);

    TransactionSignListGetResponse getTransactionSignList(TransactionSignListGetRequest paramTransactionSignListGetRequest);

    SharedGetResponse getShared(SharedGetRequest paramSharedGetRequest);

    SharedInsertResponse insertShared(SharedInsertRequest paramSharedInsertRequest);

    SharedDeleteResponse deleteShared(SharedDeleteRequest paramSharedDeleteRequest);

}

