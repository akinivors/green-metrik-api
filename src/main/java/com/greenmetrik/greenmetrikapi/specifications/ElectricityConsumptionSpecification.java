package com.greenmetrik.greenmetrikapi.specifications;

import com.greenmetrik.greenmetrikapi.model.ElectricityConsumption;
import com.greenmetrik.greenmetrikapi.model.Unit;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ElectricityConsumptionSpecification {

    public static Specification<ElectricityConsumption> hasUnitId(Long unitId) {
        return (root, query, criteriaBuilder) -> {
            if (unitId == null) {
                return criteriaBuilder.conjunction(); // If no unitId is provided, don't filter by it
            }
            Join<ElectricityConsumption, Unit> unitJoin = root.join("unit");
            return criteriaBuilder.equal(unitJoin.get("id"), unitId);
        };
    }

    public static Specification<ElectricityConsumption> hasPeriodStartDateAfter(LocalDate startDate) {
        return (root, query, criteriaBuilder) ->
                startDate == null ? criteriaBuilder.conjunction() :
                criteriaBuilder.greaterThanOrEqualTo(root.get("periodStartDate"), startDate);
    }

    public static Specification<ElectricityConsumption> hasPeriodEndDateBefore(LocalDate endDate) {
        return (root, query, criteriaBuilder) ->
                endDate == null ? criteriaBuilder.conjunction() :
                criteriaBuilder.lessThanOrEqualTo(root.get("periodEndDate"), endDate);
    }
}
