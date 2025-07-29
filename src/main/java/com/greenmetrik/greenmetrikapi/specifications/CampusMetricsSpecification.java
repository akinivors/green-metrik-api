package com.greenmetrik.greenmetrikapi.specifications;

import com.greenmetrik.greenmetrikapi.model.CampusMetrics;
import com.greenmetrik.greenmetrikapi.model.MetricCategory;
import org.springframework.data.jpa.domain.Specification;

public class CampusMetricsSpecification {

    public static Specification<CampusMetrics> hasCategory(MetricCategory category) {
        return (root, query, criteriaBuilder) ->
                category == null ? criteriaBuilder.conjunction() :
                criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<CampusMetrics> hasYear(Integer year) {
        return (root, query, criteriaBuilder) ->
                year == null ? criteriaBuilder.conjunction() :
                criteriaBuilder.equal(root.get("year"), year);
    }
}
