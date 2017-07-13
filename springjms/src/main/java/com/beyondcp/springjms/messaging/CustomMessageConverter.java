package com.beyondcp.springjms.messaging;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * Created by pranet on 12/07/17.
 */
@Slf4j
public class CustomMessageConverter extends MappingJackson2MessageConverter {

    @Override
    protected JavaType getJavaTypeForMessage(Message message) throws JMSException {
        return TypeFactory.defaultInstance().constructType(CustomMessage.class);
    }

    /**
     *  Try and convert Message object to CustomMessage object. Always store the original message.
     *  If conversion fails, return default object.
     */
    @Override
    public Object fromMessage(Message message) {
        CustomMessage customMessage;
        try {
            customMessage = (CustomMessage)super.fromMessage(message);
        }
        catch (Exception e) {
            customMessage = new CustomMessage();
        }
        customMessage.set__message__(message);
        return customMessage;
    }

}
