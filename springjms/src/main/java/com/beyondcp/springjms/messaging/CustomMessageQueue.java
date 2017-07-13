package com.beyondcp.springjms.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

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
            log.info("Message not valid" + customMessage.toString());
            return;
        }
        log.info(customMessage.toString());
    }

    public void send(CustomMessage message) {
        jmsTemplate.convertAndSend(message);
    }

}
