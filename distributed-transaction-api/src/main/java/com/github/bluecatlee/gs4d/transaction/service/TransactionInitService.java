package com.github.bluecatlee.gs4d.transaction.service;

import com.github.bluecatlee.gs4d.transaction.request.SharedColumnGetRequest;
import com.github.bluecatlee.gs4d.transaction.request.TransactionInitRequest;
import com.github.bluecatlee.gs4d.transaction.response.SharedColumnGetResponse;
import com.github.bluecatlee.gs4d.transaction.response.TransactionInitResponse;

public interface TransactionInitService {

    TransactionInitResponse initTransaction(TransactionInitRequest paramTransactionInitRequest);

    SharedColumnGetResponse getSharedColumn(SharedColumnGetRequest paramSharedColumnGetRequest);

}

