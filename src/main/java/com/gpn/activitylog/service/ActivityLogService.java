package com.gpn.activitylog.service;

import com.gpn.activitylog.mapper.ActivityLogMapper;
import com.gpn.activitylog.model.ActivityLogEntity;
import com.gpn.activitylog.model.ActivityModule;
import com.gpn.activitylog.model.dto.ActivityLogFilterRequest;
import com.gpn.activitylog.model.dto.ActivityLogResponseDto;
import com.gpn.activitylog.repository.ActivityLogRepository;
import com.gpn.activitylog.repository.ActivityLogSpecifications;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    @Transactional
    public void log(final Long userId, final ActivityModule module, final Long objectId, final String action) {
        activityLogRepository.save(new ActivityLogEntity(userId, module, objectId, action));
    }

    @Transactional(readOnly = true)
    public List<ActivityLogResponseDto> getActivityLogs(final ActivityLogFilterRequest filter) {
        final var spec = ActivityLogSpecifications.filter(filter);
        return activityLogRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(ActivityLogMapper::toDto)
                .toList();
    }
}
