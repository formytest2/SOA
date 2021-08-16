package com.github.bluecatlee.gs4d.message.consumer.utils;

import com.github.bluecatlee.gs4d.common.exception.BusinessException;
import com.github.bluecatlee.gs4d.common.exception.DatabaseOperateException;
import com.github.bluecatlee.gs4d.common.exception.ValidateBusinessException;
import com.github.bluecatlee.gs4d.common.exception.ValidateClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.transaction.TransactionTimedOutException;

public class ExceptionUtil {

    private static Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);

//    public static Long dT = 1L;
//    public static Long dU = 2L;
//    public static Long dV = 3L;
//    public static Long dW = 6L;
//    public static Long dX = 4L;
//    public static Long dY = 5L;
//    public static Long dZ = 7L;

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

    public static void processException(Exception ex, MessagePack messagePack) {
        if (ex == null) {
            throw new RuntimeException("未传入异常");
        } else {
            logger.error(ex.getMessage(), ex);
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
            }

        }
    }

    public static void processException(Throwable ex, MessagePack messagePack) {
        if (ex == null) {
            throw new RuntimeException("未传入异常");
        } else {
            logger.error(ex.getMessage(), ex);
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
            }

        }
    }

}
