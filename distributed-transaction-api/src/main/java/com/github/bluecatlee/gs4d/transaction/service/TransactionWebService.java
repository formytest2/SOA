package com.github.bluecatlee.gs4d.transaction.service;

import com.github.bluecatlee.gs4d.transaction.request.*;
import com.github.bluecatlee.gs4d.transaction.response.*;

public interface TransactionWebService {

    TransactionGetResponse getTransaction(TransactionGetRequest paramTransactionGetRequest);

    TransactionDetailGetResponse getTransactionDetail(TransactionDetailGetRequest paramTransactionDetailGetRequest);

    TransactionStateListGetResponse getTransactionStateList(TransactionStateListGetRequest paramTransactionStateListGetRequest);

    TransactionSignListGetResponse getTransactionSignList(TransactionSignListGetRequest paramTransactionSignListGetRequest);

    SharedGetResponse getShared(SharedGetRequest paramSharedGetRequest);

    SharedInsertResponse insertShared(SharedInsertRequest paramSharedInsertRequest);

    SharedDeleteResponse deleteShared(SharedDeleteRequest paramSharedDeleteRequest);

}

