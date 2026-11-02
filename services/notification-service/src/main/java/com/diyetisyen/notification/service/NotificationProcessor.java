package com.diyetisyen.notification.service;

import com.diyetisyen.notification.dto.AppointmentEvent;
import com.diyetisyen.notification.entity.NotificationRecord;
import com.diyetisyen.notification.entity.NotificationStatus;
import com.diyetisyen.notification.repository.NotificationRecordRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.format.DateTimeFormatter;

@Service
public class NotificationProcessor {
    private static final Logger log = LoggerFactory.getLogger(NotificationProcessor.class);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final NotificationRecordRepository repository;
    private final ObjectProvider<JavaMailSender> mailSenderProvider;
    private final String fromAddress;

    public NotificationProcessor(NotificationRecordRepository repository,
                                  ObjectProvider<JavaMailSender> mailSenderProvider,
                                  @Value("${app.email.from:no-reply@localhost}") String fromAddress) {
        this.repository = repository;
        this.mailSenderProvider = mailSenderProvider;
        this.fromAddress = fromAddress;
    }

    @Transactional
    public void process(AppointmentEvent event) {
        if (repository.existsByEventId(event.eventId())) {
            return;
        }

        NotificationRecord record = new NotificationRecord(event.eventId(), event.appointmentId(),
                event.type(), event.recipientEmail(), event.appointmentDate());
        repository.save(record);

        if (event.recipientEmail() == null || event.recipientEmail().isBlank()) {
            record.mark(NotificationStatus.NO_RECIPIENT);
            log.info("Notification {} for appointment {} has no recipient email", event.type(), event.appointmentId());
            return;
        }

        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
        if (mailSender == null) {
            record.mark(NotificationStatus.WAITING_CONFIGURATION);
            log.info("SMTP is not configured; notification {} for appointment {} was recorded",
                    event.type(), event.appointmentId());
            return;
        }

        deliver(record, mailSender);
    }

    @Scheduled(fixedDelayString = "${app.email.retry-delay-ms:30000}")
    @Transactional
    public void retryWaitingNotifications() {
        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
        if (mailSender == null) return;
        for (NotificationRecord record : repository.findTop50ByStatusOrderByCreatedAtAsc(
                NotificationStatus.WAITING_CONFIGURATION)) {
            if (record.getRecipientEmail() != null && !record.getRecipientEmail().isBlank()) {
                deliver(record, mailSender);
            }
        }
    }

    private void deliver(NotificationRecord record, JavaMailSender mailSender) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(record.getRecipientEmail());
        message.setSubject(subjectFor(record.getEventType()));
        message.setText("Randevu numaranız: " + record.getAppointmentId() + "\n"
                + "Randevu zamanı: " + record.getAppointmentDate().format(DATE_FORMAT) + "\n"
                + "Durum: " + record.getEventType());
        mailSender.send(message);
        record.mark(NotificationStatus.SENT);
    }

    private String subjectFor(String type) {
        return switch (type) {
            case "created" -> "Randevunuz oluşturuldu";
            case "cancelled" -> "Randevunuz iptal edildi";
            case "completed" -> "Randevunuz tamamlandı";
            default -> "Randevu durumunuz güncellendi";
        };
    }

}
