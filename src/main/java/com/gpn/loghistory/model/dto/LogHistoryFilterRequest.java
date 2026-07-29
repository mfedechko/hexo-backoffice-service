package com.gpn.loghistory.model.dto;

import com.gpn.loghistory.model.LogHistoryModule;

import java.time.LocalDateTime;

public record LogHistoryFilterRequest(
        Long userId,
        LogHistoryModule module,
        Long objectId,
        String action,
        LocalDateTime dateFrom,
        LocalDateTime dateTo) {
}
