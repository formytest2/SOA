package com.github.bluecatlee.gs4d.pay.config.db;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Value("${db.payment.url}")
    private String paymentUrl;
    @Value("${db.payment.driverClassName}")
    private String paymentDriverClassName;

//    @Value("${db.tenantdoss.url}")
//    private String tenantdossUrl = null;
//    @Value("${db.tenantdoss.driverClassName}")
//    private String tenantdossDriverClassName = null;
//
//    @Value("${db.mdm.url}")
//    private String mdmUrl = null;
//    @Value("${db.mdm.driverClassName}")
//    private String mdmDriverClassName = null;
//
//    @Value("${db.memorder.url}")
//    private String memorderUrl = null;
//    @Value("${db.memorder.driverClassName}")
//    private String memorderDriverClassName = null;

    @Primary
    @Bean("payment")
//    @ConfigurationProperties("db.payment")
    public DataSource dataSourcePayment() {
        DataSource dataSource = DataSourceBuilder.create().driverClassName(paymentDriverClassName).url(paymentUrl).build();
        return dataSource;
    }

//    @Bean("tenantdoss")
//    @ConfigurationProperties("db.tenantdoss")
//    public DataSource dataSourceTenantdoss() {
//        return DataSourceBuilder.create().driverClassName(tenantdossDriverClassName).url(tenantdossUrl).build();
//    }
//
//    @Bean("mdm")
//    @ConfigurationProperties("db.mdm")
//    public DataSource dataSourceMdm() {
//        return DataSourceBuilder.create().driverClassName(mdmDriverClassName).url(mdmUrl).build();
//    }
//
//    @Bean("memorder")
//    @ConfigurationProperties("db.memorder")
//    public DataSource dataSourceMemorder() {
//        return DataSourceBuilder.create().driverClassName(memorderDriverClassName).url(memorderUrl).build();
//    }
}
