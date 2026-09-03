package com.gpn.notification.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class Notification {
    private String id;
    private String userId;
    private Channel channel;
    private String subject;
    private String body;
    private Map<String, String> metadata;
}
