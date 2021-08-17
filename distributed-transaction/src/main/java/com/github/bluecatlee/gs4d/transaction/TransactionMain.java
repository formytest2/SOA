package com.github.bluecatlee.gs4d.transaction;


import java.io.IOException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TransactionMain {

    public static void main(String[] paramArrayOfString) throws IOException, InterruptedException {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        classPathXmlApplicationContext.start();
        System.out.println("spring 启动好了");
        while (true)
            Thread.sleep(10000L);
    }

}
