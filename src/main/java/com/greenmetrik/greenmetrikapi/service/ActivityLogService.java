package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.ActivityLogResponse;
import com.greenmetrik.greenmetrikapi.model.ActivityLog;
import com.greenmetrik.greenmetrikapi.model.User;
import com.greenmetrik.greenmetrikapi.repository.ActivityLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    public ActivityLogService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    public void logActivity(String eventType, String description, User user) {
        ActivityLog log = new ActivityLog();
        log.setEventType(eventType);
        log.setDescription(description);
        log.setUser(user);
        log.setUsername(user.getUsername()); // NEW: Set the username snapshot
        activityLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    // Update the method signature to accept userId
    public Page<ActivityLogResponse> getAllLogs(Pageable pageable, Long userId) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("timestamp").descending());

        // NEW: Add logic to filter by userId if it's provided
        Page<ActivityLog> logPage;
        if (userId != null) {
            logPage = activityLogRepository.findByUserId(userId, sortedPageable);
        } else {
            logPage = activityLogRepository.findAll(sortedPageable);
        }

        return logPage.map(ActivityLogResponse::fromEntity);
    }
}
