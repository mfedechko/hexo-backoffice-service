package com.gpn.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class IdempotencyChecker {

    private final StringRedisTemplate redisTemplate;

    public boolean isDuplicate(String messageId) {
        Boolean wasSet = redisTemplate.opsForValue()
                .setIfAbsent("notification:" + messageId, "processed", Duration.ofHours(24));
        return Boolean.FALSE.equals(wasSet);
    }
}
