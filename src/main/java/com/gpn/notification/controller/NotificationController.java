package com.gpn.notification.controller;

import com.gpn.notification.model.Notification;
import com.gpn.notification.NotificationPublisher;
import com.gpn.notification.model.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationPublisher publisher;

    @PostMapping
    public ResponseEntity<Void> sendNotification(@RequestBody NotificationRequest request) {
        Notification notification = buildNotification(request);
        publisher.publish(notification);
        return ResponseEntity.accepted().build();
    }

    private Notification buildNotification(final NotificationRequest request) {
        return Notification.builder()
                .subject(request.getSubject())
                .build();
    }
}


