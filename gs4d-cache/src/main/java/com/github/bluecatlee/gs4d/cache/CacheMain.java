package com.github.bluecatlee.gs4d.cache;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class CacheMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        classPathXmlApplicationContext.start();
        System.out.println("spring 启动好了");

        while(true) {
            Thread.sleep(10000L);
        }
    }

}
