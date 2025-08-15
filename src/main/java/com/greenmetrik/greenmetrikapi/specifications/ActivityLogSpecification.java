package com.greenmetrik.greenmetrikapi.specifications;

import com.greenmetrik.greenmetrikapi.model.ActivityLog;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ActivityLogSpecification {

    public static Specification<ActivityLog> hasTimestampAfter(LocalDate dateAfter) {
        return (root, query, criteriaBuilder) ->
                dateAfter == null ? criteriaBuilder.conjunction() :
                criteriaBuilder.greaterThanOrEqualTo(root.get("timestamp"), dateAfter.atStartOfDay());
    }

    public static Specification<ActivityLog> hasTimestampBefore(LocalDate dateBefore) {
        return (root, query, criteriaBuilder) ->
                dateBefore == null ? criteriaBuilder.conjunction() :
                criteriaBuilder.lessThanOrEqualTo(root.get("timestamp"), dateBefore.atTime(23, 59, 59));
    }

    public static Specification<ActivityLog> hasActionType(String actionType) {
        return (root, query, criteriaBuilder) -> {
            if (actionType == null || actionType.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            // Filter by the dedicated actionType field
            return criteriaBuilder.like(criteriaBuilder.upper(root.get("actionType")),
                "%" + actionType.toUpperCase() + "%");
        };
    }

    public static Specification<ActivityLog> hasDataType(String dataType) {
        return (root, query, criteriaBuilder) -> {
            if (dataType == null || dataType.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            // Now we filter by the dedicated dataType field
            return criteriaBuilder.like(criteriaBuilder.upper(root.get("dataType")),
                "%" + dataType.toUpperCase() + "%");
        };
    }

    public static Specification<ActivityLog> hasUsername(String username) {
        return (root, query, criteriaBuilder) ->
                username == null || username.trim().isEmpty() ? criteriaBuilder.conjunction() :
                criteriaBuilder.like(criteriaBuilder.lower(root.get("username")),
                    "%" + username.toLowerCase() + "%");
    }

    public static Specification<ActivityLog> hasUserId(Long userId) {
        return (root, query, criteriaBuilder) ->
                userId == null ? criteriaBuilder.conjunction() :
                criteriaBuilder.equal(root.get("user").get("id"), userId);
    }
}
