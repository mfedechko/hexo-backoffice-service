package com.gpn.loghistory.mapper;

import com.gpn.loghistory.model.LogHistoryAction;
import com.gpn.loghistory.model.dto.LogHistoryProjection;
import com.gpn.loghistory.model.dto.LogHistoryResponseDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LogHistoryMapper {

    public static LogHistoryResponseDto toDto(final LogHistoryProjection projection) {
        return new LogHistoryResponseDto(
                projection.id(),
                projection.username(),
                projection.module().name(),
                projection.objectId(),
                LogHistoryAction.valueOf(projection.action()),
                projection.details(),
                projection.createdAt());
    }
}
