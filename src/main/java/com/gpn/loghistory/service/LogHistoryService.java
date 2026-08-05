package com.gpn.loghistory.service;

import com.gpn.auth.security.AuthDetailsHolder;
import com.gpn.leads.model.LeadStatus;
import com.gpn.leads.model.LeadType;
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

    private static final Long SYSTEM_USER_ID = -1L;

    private final LogHistoryRepository logHistoryRepository;

    @Transactional(readOnly = true)
    public List<LogHistoryResponseDto> getLogHistory(final LogHistoryFilterRequest filter) {
        final var spec = LogHistorySpecification.filter(filter);
        final var ids = logHistoryRepository.findIds(spec, Sort.by(Sort.Direction.DESC, "createdAt"));
        if (ids.isEmpty()) {
            return List.of();
        }

        return logHistoryRepository.findProjectionsByIds(ids).stream()
                .map(LogHistoryMapper::toDto)
                .toList();
    }

    @Transactional
    public void logReportGeneration(final long warehouseId, final String reportName) {
        final var log = createLogWithUserFields();
        log.setModule(LogHistoryModule.REPORT);
        log.setObjectId(String.valueOf(warehouseId));
        log.setAction(LogHistoryAction.GENERATE.name());
        log.setDetails(Map.of("reportName", reportName));
        logHistoryRepository.save(log);
    }

    @Transactional
    public void logLeadStatusChange(final Long leadId,
                                    final LeadStatus currentStatus,
                                    final LeadStatus newStatus) {
        final var log = createLogWithUserFields();
        log.setModule(LogHistoryModule.LEAD);
        log.setObjectId(String.valueOf(leadId));
        log.setAction(LogHistoryAction.UPDATE.name());
        log.setDetails(Map.of("oldStatus", currentStatus.name(), "newStatus", newStatus.name()));
        logHistoryRepository.save(log);
    }

    @Transactional
    public void logLeadCreation(final Long leadId, final LeadType type) {
        final var log = new LogHistoryEntity();
        log.setUserId(SYSTEM_USER_ID);
        log.setModule(LogHistoryModule.LEAD);
        log.setObjectId(String.valueOf(leadId));
        log.setAction(LogHistoryAction.ADD.name());
        log.setDetails(Map.of("type", type.name()));
        logHistoryRepository.save(log);
    }

    private LogHistoryEntity createLogWithUserFields() {
        final var log = new LogHistoryEntity();
        final var currentUser = AuthDetailsHolder.getCurrentUser();
        log.setUserId(currentUser.id());
        return log;
    }
}
