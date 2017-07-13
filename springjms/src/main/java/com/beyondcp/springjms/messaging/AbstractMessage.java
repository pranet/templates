package com.beyondcp.springjms.messaging;

import lombok.Data;

import javax.jms.Message;

/**
 * Created by pranet on 13/07/17.
 */
@Data
public abstract class AbstractMessage {

    private Message __message__;

    public abstract boolean isInvalid();

}
