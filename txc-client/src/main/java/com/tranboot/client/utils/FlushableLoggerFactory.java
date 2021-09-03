package com.tranboot.client.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;

public class FlushableLoggerFactory {
    private static ThreadLocal<StringBuilder> cache = new ThreadLocal();
    private static final String SQLLOG_FILE = "/opt/sql.log";
    private static final String SQLLOG_FILE_PATTERN = "/opt/sql.%d{yyyy-MM-dd}_%i.log";
    private static final String LAYOUT_PATTERN = "%msg%n";
    private static final String MAX_LOG_FILE_SIZE = "50MB";

    public static FlushableLoggerFactory.FlushableLogger getLogger(Logger logger) {
        if (logger instanceof ch.qos.logback.classic.Logger) {
            ch.qos.logback.classic.Logger _logger = (ch.qos.logback.classic.Logger)logger;
            _logger.setAdditive(true);
            _logger.setLevel(Level.DEBUG);
            RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender();
            appender.setContext(_logger.getLoggerContext());
            appender.setName("sqlLog");
            appender.setFile("/opt/sql.log");
            appender.setAppend(true);
            TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy();
            rollingPolicy.setContext(_logger.getLoggerContext());
            rollingPolicy.setFileNamePattern("/opt/sql.%d{yyyy-MM-dd}_%i.log");
            rollingPolicy.setMaxHistory(3);
            rollingPolicy.setParent(appender);
            SizeAndTimeBasedFNATP<ILoggingEvent> timeBasedTriggering = new SizeAndTimeBasedFNATP();
            timeBasedTriggering.setMaxFileSize(FileSize.valueOf("50MB"));
            rollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(timeBasedTriggering);
            rollingPolicy.start();
            timeBasedTriggering.start();
            appender.setRollingPolicy(rollingPolicy);
            PatternLayoutEncoder encoder = new PatternLayoutEncoder();
            encoder.setPattern("%msg%n");
            encoder.setContext(_logger.getLoggerContext());
            encoder.start();
            appender.setEncoder(encoder);
            appender.start();
            _logger.addAppender(appender);
        } else {
            logger.error("sqllog only support logback implements");
        }

        return (FlushableLoggerFactory.FlushableLogger)Proxy.newProxyInstance(FlushableLoggerFactory.class.getClassLoader(), new Class[]{FlushableLoggerFactory.FlushableLogger.class}, new FlushableLoggerFactory.FlushableLoggerHandler(logger));
    }

    public static void main(String[] args) {
        PlatformTransactionManager manager = (PlatformTransactionManager)Proxy.newProxyInstance(FlushableLoggerFactory.class.getClassLoader(), new Class[]{PlatformTransactionManager.class}, new InvocationHandler() {
            private DataSourceTransactionManager manager = new DataSourceTransactionManager();

            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(this.manager, args);
            }
        });
        manager.getTransaction((TransactionDefinition)null);
    }

    public interface FlushableLogger extends Logger {
        void flush();

        void cache(String var1);
    }

    static class FlushableLoggerHandler implements InvocationHandler {
        private Logger delegator;

        public FlushableLoggerHandler(Logger logger) {
            this.delegator = logger;
        }

        public void cache(String log) {
            StringBuilder sbuilder = (StringBuilder)FlushableLoggerFactory.cache.get();
            if (sbuilder == null) {
                sbuilder = new StringBuilder();
                FlushableLoggerFactory.cache.set(sbuilder);
            }

            sbuilder.append(log).append(System.lineSeparator());
        }

        public void flush() {
            StringBuilder sbuilder = (StringBuilder)FlushableLoggerFactory.cache.get();
            if (sbuilder != null) {
                this.delegator.info(StringUtils.substringBeforeLast(sbuilder.toString(), System.lineSeparator()));
                FlushableLoggerFactory.cache.remove();
            }
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("flush")) {
                this.flush();
                return null;
            } else if (method.getName().equals("cache")) {
                if (args.length == 1) {
                    this.cache(args[0].toString());
                    return null;
                } else {
                    throw new UnsupportedOperationException("only support cache(String)");
                }
            } else {
                method.invoke(this.delegator, args);
                return null;
            }
        }
    }
}

