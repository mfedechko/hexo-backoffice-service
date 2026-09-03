package com.gpn.notification;

import com.gpn.notification.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(Notification notification) {
        rabbitTemplate.convertAndSend(
                "notification.exchange",
                "notification.routing." + notification.getChannel().name(),
                notification
        );
    }
}
