package com.diyetisyen.appointment.event;

import com.diyetisyen.appointment.config.RabbitConfig;
import com.diyetisyen.appointment.entity.AppointmentOutboxMessage;
import com.diyetisyen.appointment.repository.AppointmentOutboxRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class AppointmentEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final AppointmentOutboxRepository outboxRepository;

    public AppointmentEventPublisher(RabbitTemplate rabbitTemplate, AppointmentOutboxRepository outboxRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.outboxRepository = outboxRepository;
    }

    @Scheduled(fixedDelayString = "${app.outbox.delay-ms:5000}")
    @Transactional
    public void publishPending() {
        for (AppointmentOutboxMessage message : outboxRepository.findTop50ByPublishedAtIsNullOrderByCreatedAtAsc()) {
            AppointmentDomainEvent event = new AppointmentDomainEvent(
                    message.getEventId(), message.getAppointmentId(), message.getCustomerUserId(),
                    message.getRecipientEmail(), message.getEventType(),
                    message.getAppointmentDate(), message.getOccurredAt());
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE,
                    "appointment." + message.getEventType(), event);
            message.markPublished(LocalDateTime.now());
        }
    }
}
