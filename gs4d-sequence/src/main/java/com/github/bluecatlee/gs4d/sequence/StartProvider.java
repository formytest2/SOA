package com.github.bluecatlee.gs4d.sequence;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartProvider {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        classPathXmlApplicationContext.start();
        System.out.println("spring 启动");
        while (true) {
            Thread.sleep(10000L);
        }
    }

}

