package com.gpn.loghistory.mapper;

import com.gpn.loghistory.model.LogHistoryEntity;
import com.gpn.loghistory.model.dto.LogHistoryResponseDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LogHistoryMapper {

    public static LogHistoryResponseDto toDto(final LogHistoryEntity entity) {
        return new LogHistoryResponseDto(
                entity.getId(),
                entity.getUserId(),
                entity.getModule().name(),
                entity.getObjectId(),
                entity.getAction(),
                entity.getCreatedAt());
    }
}
