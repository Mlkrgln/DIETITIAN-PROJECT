package com.diyetisyen.notification.listener;

import com.diyetisyen.appointment.event.AppointmentCancelledEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component("appointmentNotificationListenerV1")
public class AppointmentNotificationListener {

    private final RabbitTemplate rabbitTemplate;

    public AppointmentNotificationListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(AppointmentCancelledEvent event) {
        // RabbitMQ’ya event publish
        rabbitTemplate.convertAndSend(
                "appointment.exchange",  // exchange adı
                "appointment.cancelled", // routing key
                event.getAppointmentId() // payload
        );
    }
}