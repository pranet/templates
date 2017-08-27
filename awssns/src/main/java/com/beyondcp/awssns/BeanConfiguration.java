package com.beyondcp.awssns;

import com.amazonaws.services.sns.AmazonSNS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by pranet on 28/08/17.
 */
@Configuration
public class BeanConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public Sender sender(AmazonSNS amazonSNS) {
        String defaultDestination = applicationContext.getEnvironment().getProperty("awssns.sender.default-destination");
        NotificationMessagingTemplate notificationMessagingTemplate = new NotificationMessagingTemplate(amazonSNS);
        return new Sender(notificationMessagingTemplate, defaultDestination);
    }

}
