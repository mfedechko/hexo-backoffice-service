package com.gpn.loghistory.service;

import com.gpn.loghistory.mapper.LogHistoryMapper;
import com.gpn.loghistory.model.LogHistoryEntity;
import com.gpn.loghistory.model.LogHistoryModule;
import com.gpn.loghistory.model.dto.LogHistoryFilterRequest;
import com.gpn.loghistory.model.dto.LogHistoryResponseDto;
import com.gpn.loghistory.repository.LogHistoryRepository;
import com.gpn.loghistory.repository.LogHistorySpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class LogHistoryService {

    private final LogHistoryRepository logHistoryRepository;

    @Transactional
    public void log(final Long userId, final LogHistoryModule module, final Long objectId, final String action) {
        logHistoryRepository.save(new LogHistoryEntity(userId, module, objectId, action));
    }

    @Transactional(readOnly = true)
    public List<LogHistoryResponseDto> getActivityLogs(final LogHistoryFilterRequest filter) {
        final var spec = LogHistorySpecification.filter(filter);
        return logHistoryRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(LogHistoryMapper::toDto)
                .toList();
    }
}
