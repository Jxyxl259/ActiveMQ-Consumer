package com.amq;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AmqConsumer {

    private static Logger log = LogManager.getLogger(AmqConsumer.class);

    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext ioc = new ClassPathXmlApplicationContext("classpath:/Spring-context.xml");
            ioc.start();
            log.info("?????????");
        } catch (BeansException e) {
            log.info("AmqConsumer Exception Message={} Cause={}",e.getMessage(),e.getCause());
            System.exit(0);
        }
    }

}
