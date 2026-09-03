package com.gpn.notification.sender;

import com.gpn.notification.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RateLimitedEmailSender implements NotificationSender {

    private final RateLimiter rateLimiter = RateLimiter.create(50); // 50 emails per second
    private final EmailSender delegate;

    @Override
    public void send(Notification notification) {
        rateLimiter.acquire();
        delegate.send(notification);
    }
}
