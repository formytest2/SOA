package com.github.bluecatlee.gs4d.transaction.service;

public interface RollBackService {

    void rollBackSql(Long transactionId, String transactionRollbackFlag, Boolean wetherOutTimeJob);

}
