package com.gpn.loghistory.service;

import com.gpn.auth.security.AuthDetailsHolder;
import com.gpn.loghistory.mapper.LogHistoryMapper;
import com.gpn.loghistory.model.LogHistoryAction;
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
import java.util.Map;

@Service
@AllArgsConstructor
public class LogHistoryService {

    private final LogHistoryRepository logHistoryRepository;

    @Transactional(readOnly = true)
    public List<LogHistoryResponseDto> getActivityLogs(final LogHistoryFilterRequest filter) {
        final var spec = LogHistorySpecification.filter(filter);
        return logHistoryRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(LogHistoryMapper::toDto)
                .toList();
    }

    @Transactional
    public void logReportGeneration(final long warehouseId, final String reportName) {
        final var currentUser = AuthDetailsHolder.getCurrentUser();
        final var log = new LogHistoryEntity();
        log.setUserId(currentUser.id());
        log.setModule(LogHistoryModule.REPORT);
        log.setObjectId(warehouseId);
        log.setAction(LogHistoryAction.GENERATE.name());
        log.setDetails(Map.of("reportName", reportName));
        logHistoryRepository.save(log);
    }
}
