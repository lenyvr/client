package com.devsu.fintech.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // accounts microservice topology (outbound RPC)
    public static final String EXCHANGE = "accounts.exchange";
    public static final String CHECK_REQUEST_QUEUE = "accounts.check-request";
    public static final String DEACTIVATION_RESPONSE_QUEUE = "client.deactivation-response";
    public static final String CHECK_REQUEST_ROUTING_KEY = "accounts.check";
    public static final String DEACTIVATION_RESPONSE_ROUTING_KEY = "client.deactivation";

    // client-report topology (inbound RPC consumer)
    public static final String CLIENT_REPORT_EXCHANGE = "clients.exchange";
    public static final String CLIENT_REPORT_REQUEST_QUEUE = "clients.report-request";
    public static final String CLIENT_REPORT_ROUTING_KEY = "clients.report-request";

    private static final String TRUSTED_PACKAGES = "*";

    @Bean
    public DirectExchange accountsExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue checkRequestQueue() {
        return new Queue(CHECK_REQUEST_QUEUE, true);
    }

    @Bean
    public Queue deactivationResponseQueue() {
        return new Queue(DEACTIVATION_RESPONSE_QUEUE, true);
    }

    @Bean
    public Binding checkRequestBinding(Queue checkRequestQueue, DirectExchange accountsExchange) {
        return BindingBuilder.bind(checkRequestQueue).to(accountsExchange).with(CHECK_REQUEST_ROUTING_KEY);
    }

    @Bean
    public Binding deactivationResponseBinding(Queue deactivationResponseQueue, DirectExchange accountsExchange) {
        return BindingBuilder.bind(deactivationResponseQueue).to(accountsExchange).with(DEACTIVATION_RESPONSE_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setAlwaysConvertToInferredType(true);
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages(TRUSTED_PACKAGES);
        converter.setJavaTypeMapper(typeMapper);

        return converter;
    }

    @Bean
    public SimpleMessageListenerContainer replyContainer(ConnectionFactory connectionFactory, RabbitTemplate rabbitTemplate) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames(DEACTIVATION_RESPONSE_QUEUE);
        container.setMessageListener(rabbitTemplate);
        return container;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        template.setReplyAddress(DEACTIVATION_RESPONSE_QUEUE);
        template.setReplyTimeout(5000);
        return template;
    }
}
