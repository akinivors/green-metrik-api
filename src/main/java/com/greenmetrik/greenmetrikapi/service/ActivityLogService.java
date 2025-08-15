package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.ActivityLogResponse;
import com.greenmetrik.greenmetrikapi.model.ActivityLog;
import com.greenmetrik.greenmetrikapi.model.User;
import com.greenmetrik.greenmetrikapi.repository.ActivityLogRepository;
import com.greenmetrik.greenmetrikapi.specifications.ActivityLogSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    public ActivityLogService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    // Clean method with separate parameters - this is the preferred approach
    public void logActivity(String actionType, String dataType, String description, User user) {
        ActivityLog log = new ActivityLog();
        log.setActionType(actionType);
        log.setDataType(dataType);
        log.setDescription(description);
        log.setUser(user);
        log.setUsername(user.getUsername());
        activityLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public Page<ActivityLogResponse> getAllLogs(Pageable pageable, Long userId, LocalDate dateAfter, LocalDate dateBefore, String actionType, String dataType, String username) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("timestamp").descending());

        // Start with a base specification
        Specification<ActivityLog> spec = Specification.where(null);

        // Add each filter parameter using .and() if the parameter has a value
        if (dateAfter != null) {
            spec = spec.and(ActivityLogSpecification.hasTimestampAfter(dateAfter));
        }

        if (dateBefore != null) {
            spec = spec.and(ActivityLogSpecification.hasTimestampBefore(dateBefore));
        }

        if (actionType != null && !actionType.trim().isEmpty()) {
            spec = spec.and(ActivityLogSpecification.hasActionType(actionType));
        }

        if (dataType != null && !dataType.trim().isEmpty()) {
            spec = spec.and(ActivityLogSpecification.hasDataType(dataType));
        }

        if (username != null && !username.trim().isEmpty()) {
            spec = spec.and(ActivityLogSpecification.hasUsername(username));
        }

        if (userId != null) {
            spec = spec.and(ActivityLogSpecification.hasUserId(userId));
        }

        // Use the combined specification to query the repository
        Page<ActivityLog> logPage = activityLogRepository.findAll(spec, sortedPageable);
        return logPage.map(ActivityLogResponse::fromEntity);
    }
}
