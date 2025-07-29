package com.greenmetrik.greenmetrikapi.specifications;

import com.greenmetrik.greenmetrikapi.model.CampusMetrics;
import com.greenmetrik.greenmetrikapi.model.MetricCategory;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;

public class CampusMetricsSpecification {

    public static Specification<CampusMetrics> hasCategory(MetricCategory category) {
        return (root, query, criteriaBuilder) ->
                category == null ? criteriaBuilder.conjunction() :
                criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<CampusMetrics> hasMetricDateAfter(LocalDate startDate) {
        return (root, query, criteriaBuilder) ->
                startDate == null ? criteriaBuilder.conjunction() :
                criteriaBuilder.greaterThanOrEqualTo(root.get("metricDate"), startDate);
    }

    public static Specification<CampusMetrics> hasMetricDateBefore(LocalDate endDate) {
        return (root, query, criteriaBuilder) ->
                endDate == null ? criteriaBuilder.conjunction() :
                criteriaBuilder.lessThanOrEqualTo(root.get("metricDate"), endDate);
    }
}
