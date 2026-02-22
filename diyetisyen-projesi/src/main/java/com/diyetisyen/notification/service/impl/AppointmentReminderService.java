package com.diyetisyen.notification.service.impl;

import com.diyetisyen.appointment.entity.Appointment;
import com.diyetisyen.notification.service.NotificationSender;

public class AppointmentReminderService {
    private final NotificationSender notificationSender;

    public AppointmentReminderService(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    public void remind(Appointment appointment) {
//        notificationSender.send(
//                new NotificationMessage("Randevunuz var")
//        );
    }
}
