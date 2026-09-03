package com.gpn.notification.sender;

import com.gpn.notification.model.Notification;

public interface NotificationSender {

    void send(Notification notification);
}
