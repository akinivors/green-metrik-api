package com.greenmetrik.greenmetrikapi.specifications;

import com.greenmetrik.greenmetrikapi.model.VehicleEntry;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class VehicleEntrySpecification {

    public static Specification<VehicleEntry> hasEntryDate(LocalDate entryDate) {
        return (root, query, criteriaBuilder) ->
                entryDate == null ? criteriaBuilder.conjunction() :
                criteriaBuilder.equal(root.get("entryDate"), entryDate);
    }

    public static Specification<VehicleEntry> hasEntryDateAfter(LocalDate startDate) {
        return (root, query, criteriaBuilder) ->
                startDate == null ? criteriaBuilder.conjunction() :
                criteriaBuilder.greaterThanOrEqualTo(root.get("entryDate"), startDate);
    }

    public static Specification<VehicleEntry> hasEntryDateBefore(LocalDate endDate) {
        return (root, query, criteriaBuilder) ->
                endDate == null ? criteriaBuilder.conjunction() :
                criteriaBuilder.lessThanOrEqualTo(root.get("entryDate"), endDate);
    }
}
