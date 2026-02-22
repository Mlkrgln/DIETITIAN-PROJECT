package com.diyetisyen.appointment.notification;

import com.diyetisyen.appointment.event.AppointmentCancelledEvent;
import com.diyetisyen.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component("appointmentNotificationListenerV2")  // BURADA ÖZEL İSİM
@Primary
public class AppointmentNotificationListener {

    private final RabbitTemplate rabbitTemplate;

    public AppointmentNotificationListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @TransactionalEventListener
    public void handle(AppointmentCancelledEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.APPOINTMENT_EXCHANGE,
                RabbitMQConfig.CANCELLED_ROUTING_KEY,
                event.getAppointmentId()
        );
    }
}