package com.gpn.loghistory.controller;

import com.gpn.loghistory.model.LogHistoryModule;
import com.gpn.loghistory.model.dto.LogHistoryFilterRequest;
import com.gpn.loghistory.model.dto.LogHistoryResponseDto;
import com.gpn.loghistory.service.LogHistoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/log-history")
public class LogHistoryController {

    private final LogHistoryService logHistoryService;

    public LogHistoryController(LogHistoryService logHistoryService) {
        this.logHistoryService = logHistoryService;
    }

    @GetMapping
    public List<LogHistoryResponseDto> getLogHistory(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) LogHistoryModule module,
            @RequestParam(required = false) Long objectId,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) LocalDateTime dateFrom,
            @RequestParam(required = false) LocalDateTime dateTo
    ) {
        return logHistoryService.getLogHistory(
                new LogHistoryFilterRequest(userId, module, objectId, action, dateFrom, dateTo));
    }
}
