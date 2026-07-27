package com.gpn.activitylog.mapper;

import com.gpn.activitylog.model.ActivityLogEntity;
import com.gpn.activitylog.model.dto.ActivityLogResponseDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ActivityLogMapper {

    public static ActivityLogResponseDto toDto(final ActivityLogEntity entity) {
        return new ActivityLogResponseDto(
                entity.getId(),
                entity.getUserId(),
                entity.getModule().name(),
                entity.getObjectId(),
                entity.getAction(),
                entity.getCreatedAt());
    }
}
