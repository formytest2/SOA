package com.tranboot.client.spring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({TxcModuleInitializeRegistrar.class})
public @interface EnableTxc {
    String systemName() default "default_system_name";

    int systemId() default 0;

    String txcMqNameServerAddr();
}

