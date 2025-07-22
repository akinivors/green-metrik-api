package com.greenmetrik.greenmetrikapi.dto;

import com.greenmetrik.greenmetrikapi.model.ElectricityConsumption;
import java.time.LocalDate;

public record ElectricityConsumptionResponse(
    Long id, LocalDate periodStartDate, LocalDate periodEndDate, double consumptionKwh,
    Long unitId, String unitName, String submittedByUsername
) {
    public static ElectricityConsumptionResponse fromEntity(ElectricityConsumption entity) {
        return new ElectricityConsumptionResponse(
            entity.getId(), entity.getPeriodStartDate(), entity.getPeriodEndDate(), entity.getConsumptionKwh(),
            entity.getUnit().getId(), entity.getUnit().getName(), entity.getUser().getUsername()
        );
    }
}
