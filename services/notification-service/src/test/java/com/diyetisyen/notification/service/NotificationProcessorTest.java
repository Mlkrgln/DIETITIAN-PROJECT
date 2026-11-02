package com.diyetisyen.notification.service;

import com.diyetisyen.notification.dto.AppointmentEvent;
import com.diyetisyen.notification.entity.NotificationRecord;
import com.diyetisyen.notification.entity.NotificationStatus;
import com.diyetisyen.notification.repository.NotificationRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationProcessorTest {
    @Mock NotificationRecordRepository repository;
    @Mock ObjectProvider<JavaMailSender> senderProvider;
    @Captor ArgumentCaptor<NotificationRecord> recordCaptor;

    private NotificationProcessor processor;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        processor = new NotificationProcessor(repository, senderProvider, "no-reply@example.test");
    }

    @Test
    void recordsEventWhenNoRecipientIsAvailable() {
        when(repository.existsByEventId("event-1")).thenReturn(false);
        when(repository.save(any(NotificationRecord.class))).thenAnswer(call -> call.getArgument(0));

        processor.process(new AppointmentEvent("event-1", 11L, 5L, null, "created",
                LocalDateTime.now().plusDays(2), LocalDateTime.now()));

        verify(repository).save(recordCaptor.capture());
        assertEquals(NotificationStatus.NO_RECIPIENT, recordCaptor.getValue().getStatus());
    }

    @Test
    void ignoresAlreadyProcessedEvent() {
        when(repository.existsByEventId("event-1")).thenReturn(true);

        processor.process(new AppointmentEvent("event-1", 11L, 5L, null, "created",
                LocalDateTime.now().plusDays(2), LocalDateTime.now()));

        verify(repository, org.mockito.Mockito.never()).save(any());
    }
}
