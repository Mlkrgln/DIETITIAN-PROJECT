package com.diyetisyen.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String APPOINTMENT_EXCHANGE = "appointment.exchange";
    public static final String CANCELLED_QUEUE = "appointment.cancelled.queue";
    public static final String CANCELLED_ROUTING_KEY = "appointment.cancelled";

    @Bean
    public DirectExchange appointmentExchange() {
        return new DirectExchange(APPOINTMENT_EXCHANGE);
    }

    @Bean
    public Queue cancelledQueue() {
        return new Queue(CANCELLED_QUEUE, true);
    }

    @Bean
    public Binding cancelledBinding(Queue cancelledQueue, DirectExchange appointmentExchange) {
        return BindingBuilder.bind(cancelledQueue)
                .to(appointmentExchange)
                .with(CANCELLED_ROUTING_KEY);
    }
}