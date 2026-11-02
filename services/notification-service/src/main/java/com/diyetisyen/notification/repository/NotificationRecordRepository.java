package com.diyetisyen.notification.repository;

import com.diyetisyen.notification.entity.NotificationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRecordRepository extends JpaRepository<NotificationRecord, Long> {
    boolean existsByEventId(String eventId);
    List<NotificationRecord> findTop50ByStatusOrderByCreatedAtAsc(
            com.diyetisyen.notification.entity.NotificationStatus status);
}
