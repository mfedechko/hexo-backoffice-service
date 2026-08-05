package com.gpn.loghistory.model.dto;

import com.gpn.loghistory.model.LogHistoryModule;

import java.time.LocalDateTime;
import java.util.Map;

public record LogHistoryProjection(
        Long id,
        String username,
        LogHistoryModule module,
        String objectId,
        String action,
        Map<String, String> details,
        LocalDateTime createdAt) {
}
