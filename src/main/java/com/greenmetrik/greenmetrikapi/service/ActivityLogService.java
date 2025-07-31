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
        activityLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public Page<ActivityLogResponse> getAllLogs(Pageable pageable) {
        // Ensure logs are sorted by most recent first
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("timestamp").descending());
        return activityLogRepository.findAll(sortedPageable).map(ActivityLogResponse::fromEntity);
    }
}
