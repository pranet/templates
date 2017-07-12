package com.beyondcp.springjms.messaging;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by pranet on 11/07/17.
 */
@Data
public class CustomMessage {

    private final String username;

    private final String content;

}
