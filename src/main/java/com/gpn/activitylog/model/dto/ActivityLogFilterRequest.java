package com.gpn.activitylog.model.dto;

import com.gpn.activitylog.model.ActivityModule;

import java.time.LocalDateTime;

public record ActivityLogFilterRequest(
        Long userId,
        ActivityModule module,
        Long objectId,
        String action,
        LocalDateTime dateFrom,
        LocalDateTime dateTo) {
}
