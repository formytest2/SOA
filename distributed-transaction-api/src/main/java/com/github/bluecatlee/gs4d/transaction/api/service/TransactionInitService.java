package com.github.bluecatlee.gs4d.transaction.api.service;

import com.github.bluecatlee.gs4d.transaction.api.response.TransactionInitResponse;
import com.github.bluecatlee.gs4d.transaction.api.request.SharedColumnGetRequest;
import com.github.bluecatlee.gs4d.transaction.api.request.TransactionInitRequest;
import com.github.bluecatlee.gs4d.transaction.api.response.SharedColumnGetResponse;

public interface TransactionInitService {

    TransactionInitResponse initTransaction(TransactionInitRequest paramTransactionInitRequest);

    SharedColumnGetResponse getSharedColumn(SharedColumnGetRequest paramSharedColumnGetRequest);

}

