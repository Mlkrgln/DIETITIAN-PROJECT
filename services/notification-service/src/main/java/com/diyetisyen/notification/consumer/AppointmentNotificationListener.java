package com.diyetisyen.notification.consumer;

import com.diyetisyen.notification.config.RabbitConfig;
import com.diyetisyen.notification.dto.AppointmentEvent;
import com.diyetisyen.notification.service.NotificationProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AppointmentNotificationListener {
    private final NotificationProcessor processor;

    public AppointmentNotificationListener(NotificationProcessor processor) {
        this.processor = processor;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void onAppointmentEvent(AppointmentEvent event) {
        processor.process(event);
    }
}
