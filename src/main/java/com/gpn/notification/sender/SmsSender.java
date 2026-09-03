package com.gpn.notification.sender;

import com.gpn.notification.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmsSender implements NotificationSender {


    @Override
    public void send(Notification notification) {
    }
}
