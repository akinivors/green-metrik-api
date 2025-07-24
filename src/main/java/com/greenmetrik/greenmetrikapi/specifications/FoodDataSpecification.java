package com.greenmetrik.greenmetrikapi.specifications;

import com.greenmetrik.greenmetrikapi.model.FoodData;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class FoodDataSpecification {

    public static Specification<FoodData> hasDataDateAfter(LocalDate startDate) {
        return (root, query, criteriaBuilder) ->
                startDate == null ? criteriaBuilder.conjunction() :
                criteriaBuilder.greaterThanOrEqualTo(root.get("dataDate"), startDate);
    }

    public static Specification<FoodData> hasDataDateBefore(LocalDate endDate) {
        return (root, query, criteriaBuilder) ->
                endDate == null ? criteriaBuilder.conjunction() :
                criteriaBuilder.lessThanOrEqualTo(root.get("dataDate"), endDate);
    }
}
