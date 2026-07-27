package com.gpn.activitylog.controller;

import com.gpn.activitylog.model.ActivityModule;
import com.gpn.activitylog.model.dto.ActivityLogFilterRequest;
import com.gpn.activitylog.model.dto.ActivityLogResponseDto;
import com.gpn.activitylog.service.ActivityLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/activity-logs")
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    public ActivityLogController(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @GetMapping
    public List<ActivityLogResponseDto> getActivityLogs(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) ActivityModule module,
            @RequestParam(required = false) Long objectId,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) LocalDateTime dateFrom,
            @RequestParam(required = false) LocalDateTime dateTo
    ) {
        return activityLogService.getActivityLogs(
                new ActivityLogFilterRequest(userId, module, objectId, action, dateFrom, dateTo));
    }
}
