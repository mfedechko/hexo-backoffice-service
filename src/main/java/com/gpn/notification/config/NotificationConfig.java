package com.gpn.notification.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfig {

    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable("notification.email.queue")
                .withArgument("x-dead-letter-exchange", "notification.dlx.exchange")
                .withArgument("x-dead-letter-routing-key", "notification.dlx.routing")
                .build();
    }

}
