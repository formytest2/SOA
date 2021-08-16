package com.github.bluecatlee.gs4d.message.api.utils;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.common.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.transaction.TransactionTimedOutException;

public class ExceptionUtils {

    public static String FLUSH_MQ_TOPIC = "FLUSH_MQ_TOPIC";
    public static String FLUSH_MQ_TAG = "FLUSH_MQ_TAG";
    private static Logger log = LoggerFactory.getLogger(ExceptionUtils.class);

    public static String getExceptionMsg(Exception ex) {
        String msg = "";
        if (ex != null) {
            if (ex instanceof BadSqlGrammarException) {
                msg = "SQL语法错误！";
                return msg;
            }

            if (ex instanceof TransactionTimedOutException) {
                msg = "操作时间过长，事务超时！";
                return msg;
            }

            if (ex instanceof UncategorizedSQLException) {
                msg = ex.getMessage();
                return msg;
            }

            msg = ex.getMessage();
        }

        return msg;
    }

    public static void processException(Throwable ex, MessagePack messagePack) {
        if (ex == null) {
            throw new RuntimeException("未传入异常");
        } else {
            log.error(ex.getMessage(), ex);
            if (ex instanceof ValidateClientException) {
                messagePack.setCode(((ValidateClientException)ex).getCode());
                messagePack.setMessage(ex.getMessage());
            } else if (ex instanceof DatabaseOperateException) {
                messagePack.setCode(((DatabaseOperateException)ex).getCode());
                messagePack.setMessage(ex.getMessage());
            } else if (ex instanceof ValidateBusinessException) {
                messagePack.setCode(((ValidateBusinessException)ex).getCode());
                messagePack.setMessage(ex.getMessage());
            } else if (ex instanceof BusinessException) {
                messagePack.setCode(((BusinessException)ex).getCode());
                messagePack.setMessage(ex.getMessage());
            } else {
                messagePack.setCode(MessagePack.EXCEPTION);
                messagePack.setMessage("系统执行异常:" + ex.getMessage());
                messagePack.setFullMessage(ex.getMessage());
            }

        }
    }

    public static void checkDubboException(MessagePack messagePack) {
        if (messagePack == null) {
            throw new RuntimeException("未传入异常");
        } else {
            long code = messagePack.getCode();
            if (code != MessagePack.OK) {
                if (code == MessagePack.EXCEPTION) {
                    throw new RuntimeException(messagePack.getMessage());
                } else {
                    ExceptionType et = ExceptionType.getExceptionTypeByCode(code);
                    if (et.getCategory().equals(ExceptionTypeCategory.VALIDATE_CLIENT_EXCEPTION)) {
                        throw new ValidateClientException(et.getSubSystem(), et, messagePack.getMessage());
                    } else if (et.getCategory().equals(ExceptionTypeCategory.VALIDATE_BUSINESS_EXCEPTION)) {
                        throw new ValidateBusinessException(et.getSubSystem(), et, messagePack.getMessage());
                    } else if (et.getCategory().equals(ExceptionTypeCategory.DATABASE_OPERATE_EXCEPTION)) {
                        throw new DatabaseOperateException(et.getSubSystem(), et, messagePack.getMessage());
                    } else if (et.getCategory().equals(ExceptionTypeCategory.BUSINESS_EXCEPTION)) {
                        throw new BusinessException(et.getSubSystem(), et, messagePack.getMessage());
                    } else {
                        throw new RuntimeException(messagePack.getMessage());
                    }
                }
            }
        }
    }
}

