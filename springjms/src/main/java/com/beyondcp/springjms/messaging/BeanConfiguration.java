package com.beyondcp.springjms.messaging;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.ConnectionFactory;
import javax.jms.Session;

/**
 * Created by pranet on 10/07/17.
 */
@Configuration
@EnableJms
public class BeanConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    @ConditionalOnProperty(name="queue.type", havingValue = "AWSSQS")
    public ConnectionFactory AWSSQSconnectionFactory() {
        String region = applicationContext.getEnvironment().getProperty("awssqs.region", "US_EAST_1");
        return new SQSConnectionFactory(
                new ProviderConfiguration(),
                AmazonSQSClientBuilder.standard()
                        .withRegion(region)
                        .withCredentials(new DefaultAWSCredentialsProviderChain())
        );
    }

    @Bean
    @ConditionalOnProperty(name="queue.type", havingValue = "ActiveMQ")
    public ConnectionFactory ActiveMQConnectionFactory() {
        String username = applicationContext.getEnvironment().getProperty("activemq.username");
        String password = applicationContext.getEnvironment().getProperty("activemq.password");
        String brokerurl = applicationContext.getEnvironment().getRequiredProperty("activemq.brokerurl");
        return new ActiveMQConnectionFactory(username, password, brokerurl);
    }

    @Bean
    public MessageConverter customMessageConverter() {
        CustomMessageConverter converter = new CustomMessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        ConnectionFactory connectionFactory = applicationContext.getBean(ConnectionFactory.class);
        String name = applicationContext.getEnvironment().getRequiredProperty("queue.name");
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory);
        jmsTemplate.setDefaultDestinationName(name);
        jmsTemplate.setMessageConverter(customMessageConverter());
        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        factory.setMessageConverter(customMessageConverter());
        return factory;
    }

}
