package com.gpn.activitylog.model.dto;

import java.time.LocalDateTime;

public record ActivityLogResponseDto(
        Long id,
        Long userId,
        String module,
        Long objectId,
        String action,
        LocalDateTime createdAt) {
}
