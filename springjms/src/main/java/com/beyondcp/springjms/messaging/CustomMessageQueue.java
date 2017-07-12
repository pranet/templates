package com.beyondcp.springjms.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * Created by pranet on 11/07/17.
 */
@Component
@Slf4j
public class CustomMessageQueue {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private MessageConverter messageConverter;

//    @JmsListener(destination = "${queue.name}")
//    public void receive(@Validated CustomMessage customMessage) {
//
//    }

    /**
     * I could have simply set the messageConverter in the JmsListener bean, but by
     * converting manually, i have fine control over conversion errors (can choose to not resend them)
     * @param message
     */
    @JmsListener(destination = "${queue.name}")
    public void receive(Message message) {
        CustomMessage customMessage;
        try {
            customMessage = (CustomMessage)messageConverter.fromMessage(message);
        }
        catch (MessageConversionException|JMSException e) {
            try {
                String messageString = ((TextMessage) message).getText();
                log.error("Unable to convert " + messageString + " to a CustomMessage object. Ignoring this message");
            }
            catch (JMSException e1) {
                log.error("Unable to convert " + message + " to a CustomMessage object. Ignoring this message");
            }
            return;
        }
        System.out.println(customMessage);
    }

    public void send(CustomMessage message) {
        jmsTemplate.convertAndSend(message);
    }

}
