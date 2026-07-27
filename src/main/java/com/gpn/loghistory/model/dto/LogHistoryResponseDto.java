package com.gpn.loghistory.model.dto;

import java.time.LocalDateTime;

public record LogHistoryResponseDto(
        Long id,
        Long userId,
        String module,
        Long objectId,
        String action,
        LocalDateTime createdAt) {
}
