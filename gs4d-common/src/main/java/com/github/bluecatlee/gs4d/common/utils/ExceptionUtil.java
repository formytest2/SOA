package com.github.bluecatlee.gs4d.common.utils;

import com.alibaba.dubbo.rpc.RpcException;
import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.common.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.transaction.TransactionTimedOutException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

public class ExceptionUtil {
    private static Logger log = LoggerFactory.getLogger(ExceptionUtil.class);
    private static String systemName = "";
    private static boolean printFullMessage = false;

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

    /**
     * 将异常信息包装到MessagePack中
     * @param ex
     * @param messagePack
     */
    public static void processException(Exception ex, MessagePack messagePack) {
        if (ex == null) {
            throw new RuntimeException("未传入异常");
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            log.error("错误序号:" + currentTimeMillis + ";fullMessage:{},ex:{}", messagePack.getFullMessage(), ex);
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
            } else if (ex instanceof InnerException) {
                messagePack.setCode(((InnerException)ex).getCode());
                messagePack.setMessage(ex.getMessage());
            } else if (ex instanceof InvocationTargetException) {
                if (ex.getCause().getClass().equals(RpcException.class)) {
                    messagePack.setCode(MessagePack.EXCEPTION);
                    messagePack.setMessage(systemName + ":rpc服务调用失败");
                } else {
                    messagePack.setCode(MessagePack.EXCEPTION);
                    messagePack.setMessage(systemName + "系统执行异常(" + currentTimeMillis + ")" + (printFullMessage ? ex.getMessage() : ""));
                }
            } else {
                messagePack.setCode(MessagePack.EXCEPTION);
                if (ex instanceof UndeclaredThrowableException) {
                    UndeclaredThrowableException undeclaredThrowableException = (UndeclaredThrowableException)ex;
                    messagePack.setFullMessage("系统执行异常 (" + currentTimeMillis + ")" + undeclaredThrowableException.getUndeclaredThrowable().getMessage());
                } else {
                    messagePack.setMessage(systemName + "系统执行异常(" + currentTimeMillis + ")" + (printFullMessage ? ex.getMessage() : ""));
                }
            }

        }
    }

    public static void processException(Throwable ex, MessagePack messagePack) {
        if (ex == null) {
            throw new RuntimeException("未传入异常");
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            log.error("错误序号:" + currentTimeMillis + ";fullMessage:{},ex:{}", messagePack.getFullMessage(), ex);
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
            } else if (ex instanceof InnerException) {
                messagePack.setCode(((InnerException)ex).getCode());
                messagePack.setMessage(ex.getMessage());
            } else if (ex instanceof InvocationTargetException) {
                if (ex.getCause().getClass().equals(RpcException.class)) {
                    messagePack.setCode(MessagePack.EXCEPTION);
                    messagePack.setMessage(systemName + ":rpc服务调用失败");
                } else {
                    messagePack.setCode(MessagePack.EXCEPTION);
                    messagePack.setMessage(systemName + "系统执行异常(" + currentTimeMillis + ")" + (printFullMessage ? ex.getMessage() : ""));
                }
            } else {
                messagePack.setCode(MessagePack.EXCEPTION);
                if (ex instanceof UndeclaredThrowableException) {
                    UndeclaredThrowableException undeclaredThrowableException = (UndeclaredThrowableException)ex;
                    messagePack.setFullMessage("系统执行异常 (" + currentTimeMillis + ")" + undeclaredThrowableException.getUndeclaredThrowable().getMessage());
                } else {
                    messagePack.setMessage(systemName + "系统执行异常(" + currentTimeMillis + ")" + (printFullMessage ? ex.getMessage() : ""));
                }
            }

        }
    }

    /**
     * 重新抛出MessagePack中的异常
     * @param messagePack
     */
    public static void checkDubboException(MessagePack messagePack) {
        if (messagePack == null) {
            throw new RuntimeException("未传入MessagePack");
        } else {
            long code = messagePack.getCode();
            if (code != MessagePack.OK) {
                if (code == MessagePack.EXCEPTION) {
                    throw new InnerException(code, messagePack.getMessage());
                } else {
                    ExceptionType et = ExceptionType.getExceptionTypeByCode(code);
                    if (et == null) {
                        throw new InnerException(code, messagePack.getMessage());
                    } else if (et.getCategory().equals(ExceptionTypeCategory.VALIDATE_CLIENT_EXCEPTION)) {
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

    public static void checkDubboException(AbstractExceptionType aet, MessagePack messagePack) {
        if (messagePack == null) {
            throw new RuntimeException("未传入异常");
        } else {
            long code = messagePack.getCode();
            if (code != MessagePack.OK) {
                if (code == MessagePack.EXCEPTION) {
                    throw new InnerException(code, messagePack.getMessage());
                } else {
                    AbstractExceptionType et = aet.getAbstractExceptionTypeByCode(code);
                    if (et == null) {
                        checkDubboException(messagePack);
                    } else if (et.getCategory().equals(ExceptionTypeCategory.VALIDATE_CLIENT_EXCEPTION)) {
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

    static {
        String temp = System.getProperty("system.name");
        if (temp != null) {
            log.info("获取到设置system.name值为:" + temp);
            systemName = temp;
        }

        temp = System.getProperty("print.full.message");
        if (temp != null) {
            log.info("获取到设置print.full.message值为:" + temp);
            printFullMessage = Boolean.parseBoolean(temp);
        }

    }
}

