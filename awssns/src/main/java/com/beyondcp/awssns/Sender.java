package com.beyondcp.awssns;

import lombok.Data;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.util.Assert;

/**
 * Created by pranet on 28/08/17.
 */
@Data
public class Sender {

    private final NotificationMessagingTemplate notificationMessagingTemplate;

    private final String defaultDestination;

    public void send(String subject, String message) {
        Assert.state(this.defaultDestination != null, "No defaultDestination configured. Please set property awssns.sender.default-destination");
        this.notificationMessagingTemplate.sendNotification(defaultDestination, message, subject);
    }

    public void send(String destination, String subject, String message) {
        this.notificationMessagingTemplate.sendNotification(destination, message, subject);
    }

}
