package com.amq.service;

import com.amq.entity.MailParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Component("mailService")
public class MailService {

    /**
     * Spring配置中定义
     */
    @Resource
    private JavaMailSender mailSender;

    /**
     * Spring配置中定义
     */
    @Autowired
    private SimpleMailMessage simpleMailMessage;

    /**
     * 线程池
     */
    @Autowired
    private ThreadPoolTaskExecutor threadPool;

    /**
     * 发送模板邮件
     *
     *
     */
    public void sendMail(final MailParam mailParam){
        threadPool.execute(new Runnable(){
            @Override
            public void run() {
                try{
                    //发送人，从配置文件中获取
                    simpleMailMessage.setFrom(simpleMailMessage.getFrom());
                    simpleMailMessage.setSubject(mailParam.getSubject());
                    simpleMailMessage.setTo(mailParam.getTo());
                    simpleMailMessage.setText(mailParam.getContent());
                    mailSender.send(simpleMailMessage);
                }catch(MailException e){
                    throw e;
                }
            }
        });
    }

}
