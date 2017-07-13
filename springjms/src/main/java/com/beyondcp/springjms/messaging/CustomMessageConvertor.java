package com.beyondcp.springjms.messaging;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.validation.annotation.Validated;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * Created by pranet on 12/07/17.
 */
@Slf4j
public class CustomMessageConvertor extends MappingJackson2MessageConverter {

    @Override
    protected JavaType getJavaTypeForMessage(Message message) throws JMSException {
        return TypeFactory.defaultInstance().constructType(CustomMessage.class);
    }

    /**
     *  Don't allow this to throw any exceptions.
     *  If conversion fails, send a default object
     *  This is done so that I can safely use factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE)
     *  in my configuration, without having to worry about pushing incorrect messages back to my queue
     */
    public Object fromMessage(Message message) {
        try {
            return super.fromMessage(message);
        }
        catch (Exception e) {
            try {
                log.error("Unable to convert message " + ((TextMessage)message).getText() + ". Creating new blank message" );
            }
            catch (Exception e1) {
                log.error("Unable to convert given message or even log it");
            }
            return new CustomMessage();
        }
    }

}
