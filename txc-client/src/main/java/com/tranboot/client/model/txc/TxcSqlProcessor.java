package com.tranboot.client.model.txc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface TxcSqlProcessor {
    Logger logger = LoggerFactory.getLogger(TxcSqlProcessor.class);

    TxcSQL parse();

    boolean manual();

    boolean auto();
}

