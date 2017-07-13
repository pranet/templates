package com.beyondcp.springjms.messaging;

import com.amazonaws.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by pranet on 11/07/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomMessage {

    private String username;

    private String content;

    boolean isInvalid() {
        return StringUtils.isNullOrEmpty(username) ||
                StringUtils.isNullOrEmpty(content);
    }

}
