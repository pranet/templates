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
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

import javax.jms.ConnectionFactory;
import javax.jms.Session;

/**
 * Created by pranet on 10/07/17.
 */
@Configuration
@EnableJms
public class BeanConfiguration implements JmsListenerConfigurer {

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
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
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
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        factory.setMessageConverter(jacksonJmsMessageConverter());
        return factory;
    }

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(myJmsHandlerMethodFactory());
    }

    @Bean
    public DefaultMessageHandlerMethodFactory myJmsHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setValidator(new CustomMessageValidator());
        return factory;
    }

}
