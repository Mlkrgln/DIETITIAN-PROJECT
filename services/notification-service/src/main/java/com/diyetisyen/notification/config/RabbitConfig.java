package com.diyetisyen.notification.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class RabbitConfig {
    public static final String EXCHANGE = "dietitian.appointments";
    public static final String QUEUE = "dietitian.notifications.appointments";
    public static final String DEAD_LETTER_EXCHANGE = "dietitian.notifications.dlx";
    public static final String DEAD_LETTER_QUEUE = "dietitian.notifications.appointments.dlq";

    @Bean
    TopicExchange appointmentExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    Queue notificationQueue() {
        return QueueBuilder.durable(QUEUE).deadLetterExchange(DEAD_LETTER_EXCHANGE).build();
    }

    @Bean
    Binding appointmentBinding(@Qualifier("notificationQueue") Queue notificationQueue,
                               TopicExchange appointmentExchange) {
        return BindingBuilder.bind(notificationQueue).to(appointmentExchange).with("appointment.*");
    }

    @Bean
    FanoutExchange deadLetterExchange() {
        return new FanoutExchange(DEAD_LETTER_EXCHANGE, true, false);
    }

    @Bean
    Queue deadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    @Bean
    Binding deadLetterBinding(@Qualifier("deadLetterQueue") Queue deadLetterQueue,
                              FanoutExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange);
    }

    @Bean
    Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}
