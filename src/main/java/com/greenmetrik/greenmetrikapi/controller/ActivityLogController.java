package com.greenmetrik.greenmetrikapi.controller;

import com.greenmetrik.greenmetrikapi.dto.ActivityLogResponse;
import com.greenmetrik.greenmetrikapi.service.ActivityLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/activity-log")
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    public ActivityLogController(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<ActivityLogResponse> getAllLogs(
            Pageable pageable,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) LocalDate dateAfter,
            @RequestParam(required = false) LocalDate dateBefore,
            @RequestParam(required = false) String actionType,
            @RequestParam(required = false) String dataType,
            @RequestParam(required = false) String username
    ) {
        return activityLogService.getAllLogs(pageable, userId, dateAfter, dateBefore, actionType, dataType, username);
    }
}
