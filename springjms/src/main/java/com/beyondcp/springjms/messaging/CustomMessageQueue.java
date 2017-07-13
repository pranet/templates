package com.beyondcp.springjms.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.TextMessage;

/**
 * Created by pranet on 11/07/17.
 */
@Component
@Slf4j
public class CustomMessageQueue {

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * This ensures that syntactically bad messages are not pushed back to queue automatically.
     */
    @JmsListener(destination = "${queue.name}")
    public void receive(CustomMessage customMessage) {
        if (customMessage.isInvalid()) {
            try {
                String textMessage = ((TextMessage)customMessage.get__message__()).getText();
                log.error("Unable to convert " + textMessage + " to a valid CustomMessage");
            }
            catch (Exception e) {
                log.error("Unable to convert or log given input " + customMessage.get__message__());
            }
            return;
        }
        log.info(customMessage.toString());
    }

    public void send(CustomMessage message) {
        jmsTemplate.convertAndSend(message);
    }

}
