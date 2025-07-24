package com.greenmetrik.greenmetrikapi.specifications;

import com.greenmetrik.greenmetrikapi.model.Unit;
import com.greenmetrik.greenmetrikapi.model.WaterConsumption;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class WaterConsumptionSpecification {

    public static Specification<WaterConsumption> hasUnitId(Long unitId) {
        return (root, query, criteriaBuilder) -> {
            if (unitId == null) {
                return criteriaBuilder.conjunction();
            }
            Join<WaterConsumption, Unit> unitJoin = root.join("unit");
            return criteriaBuilder.equal(unitJoin.get("id"), unitId);
        };
    }

    public static Specification<WaterConsumption> hasPeriodStartDateAfter(LocalDate startDate) {
        return (root, query, criteriaBuilder) ->
                startDate == null ? criteriaBuilder.conjunction() :
                criteriaBuilder.greaterThanOrEqualTo(root.get("periodStartDate"), startDate);
    }

    public static Specification<WaterConsumption> hasPeriodEndDateBefore(LocalDate endDate) {
        return (root, query, criteriaBuilder) ->
                endDate == null ? criteriaBuilder.conjunction() :
                criteriaBuilder.lessThanOrEqualTo(root.get("periodEndDate"), endDate);
    }
}
