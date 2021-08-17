package com.github.bluecatlee.gs4d.transaction.service.impl;

import java.util.List;

import com.github.bluecatlee.gs4d.transaction.api.request.MessagecenterRecallRequest;
import com.github.bluecatlee.gs4d.transaction.api.response.MessagecenterRecallResponse;
import com.github.bluecatlee.gs4d.transaction.api.service.MessagecenterRecallService;
import com.github.bluecatlee.gs4d.transaction.dao.TransactionLogDao;
import com.github.bluecatlee.gs4d.transaction.model.TRANSACTION_LOG;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("messagecenterRecallService")
public class MessagecenterRecallServiceImpl implements MessagecenterRecallService {
    
    protected static Logger logger = LoggerFactory.getLogger(MessagecenterRecallServiceImpl.class);

    @Autowired
    private TransactionLogDao transactionLogDao;

    public MessagecenterRecallResponse recallMessagecenter(MessagecenterRecallRequest messagecenterRecallRequest) {
        MessagecenterRecallResponse messagecenterRecallResponse = new MessagecenterRecallResponse();

        try {
            List<TRANSACTION_LOG> transactionLogList = this.transactionLogDao.queryByTransactionId(messagecenterRecallRequest.getTransactionId());
            if (transactionLogList != null && !transactionLogList.isEmpty()) {
                messagecenterRecallResponse.setState(((TRANSACTION_LOG)transactionLogList.get(0)).getTRANSACTION_STATE());
            } else {
                messagecenterRecallResponse.setCode(-1L);
                messagecenterRecallResponse.setMessage("分布式事务回调失败,分布式事务表不存在此事务编号:" + messagecenterRecallRequest.getTransactionId());
            }
        } catch (Throwable e) {
            messagecenterRecallResponse.setCode(-1L);
            messagecenterRecallResponse.setMessage("分布式事务回调失败,理由" + e.getMessage());
            logger.error("分布式事务回调失败,理由" + e.getMessage(), e);
        }

        return messagecenterRecallResponse;
    }
}

