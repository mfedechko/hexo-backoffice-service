package com.gpn.notification;

import com.gpn.notification.model.Notification;
import com.gpn.notification.sender.EmailSender;
import com.gpn.notification.sender.PushSender;
import com.gpn.notification.sender.SmsSender;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationWorker {

    private final EmailSender emailSender;
    private final PushSender pushSender;
    private final SmsSender smsSender;

    @RabbitListener(queues = "notification.email.queue")
    public void handleEmail(Notification notification) {
        emailSender.send(notification);
    }

    @RabbitListener(queues = "notification.push.queue")
    public void handlePush(Notification notification) {
        pushSender.send(notification);
    }

    @RabbitListener(queues = "notification.sms.queue")
    public void handleSms(Notification notification) {
        smsSender.send(notification);
    }
}
