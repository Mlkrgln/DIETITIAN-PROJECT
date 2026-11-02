package com.diyetisyen.appointment.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class RabbitConfig {
    public static final String EXCHANGE = "dietitian.appointments";

    @Bean
    TopicExchange appointmentExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
