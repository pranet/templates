package com.beyondcp.springjms.messaging;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by pranet on 12/07/17.
 */
public class CustomMessageValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
        return CustomMessage.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        System.out.println("here");
    }

}
