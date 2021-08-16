package com.github.bluecatlee.gs4d.pay;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"com.github.bluecatlee.gs4d.pay"})
@ImportResource(locations = {"classpath:dubbo.xml", "classpath:application_redis.xml"})
@EnableAsync
//@EnableDiscoveryClient
public class Pay {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Pay.class);
        app.setBannerMode(Banner.Mode.LOG);
        app.run(args);
    }

}