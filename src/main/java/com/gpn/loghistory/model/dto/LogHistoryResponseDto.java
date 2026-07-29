package com.gpn.loghistory.model.dto;

import com.gpn.loghistory.model.LogHistoryAction;

import java.time.LocalDateTime;
import java.util.Map;

public record LogHistoryResponseDto(
        Long id,
        Long userId,
        String module,
        String objectId,
        LogHistoryAction action,
        Map<String, String> details,
        LocalDateTime createdAt) {
}
