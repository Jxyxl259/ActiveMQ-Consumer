package com.amq.session.listener;

import com.alibaba.fastjson.JSONObject;
import com.amq.AmqConsumer;
import com.amq.entity.MailParam;
import com.amq.service.MailService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Component
public class ConsumerSessionAwareMessageListener implements SessionAwareMessageListener<Message> {

    private static Logger log = LogManager.getLogger(ConsumerSessionAwareMessageListener.class);


    @Autowired
    private JmsTemplate activeMqJmsTemplate;

    @Autowired
    private Destination sessionAwareQueue;

    @Resource(name="mailService")
    private MailService mailService;

    @Override
    public synchronized void onMessage(Message message, Session session){

        try {
            ActiveMQTextMessage msg = (ActiveMQTextMessage)message;
            final String ms = msg.getText();

            //转换成相应的对象
            MailParam mailParam = JSONObject.parseObject(ms, MailParam.class);
            if(mailParam == null ){
                return;
            }
            try {
                mailService.sendMail(mailParam);
            } catch (Exception e) {
                //发送异常，重新放回队列
                log.info("发送异常，重新放回队列 Message={} Cause={}",e.getMessage(),e.getCause());
//                activeMqJmsTemplate.send(sessionAwareQueue, new MessageCreator() {
//                    @Override
//                    public Message createMessage(Session session) throws JMSException {
//                        return session.createTextMessage(ms);
//                    }
//                });

            }
        } catch (JMSException e) {
            log.info("onMessage Exception Message={} Cause={}",e.getMessage(),e.getCause());
            e.printStackTrace();
        }

    }
}
