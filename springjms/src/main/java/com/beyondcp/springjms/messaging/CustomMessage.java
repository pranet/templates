package com.beyondcp.springjms.messaging;

import com.amazonaws.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by pranet on 11/07/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomMessage extends AbstractMessage {

    private String username;

    private String content;

    @Override
    public boolean isInvalid() {
        return StringUtils.isNullOrEmpty(username) ||
                StringUtils.isNullOrEmpty(content);
    }

}
