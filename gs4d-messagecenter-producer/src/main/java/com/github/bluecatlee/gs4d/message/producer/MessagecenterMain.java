package com.github.bluecatlee.gs4d.message.producer;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class MessagecenterMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        classPathXmlApplicationContext.start();
        System.out.println("spring 启动好了");

        while(true) {
            Thread.sleep(10000L);
        }
    }
}
