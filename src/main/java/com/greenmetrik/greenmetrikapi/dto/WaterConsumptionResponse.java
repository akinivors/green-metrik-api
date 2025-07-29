package com.greenmetrik.greenmetrikapi.dto;

import com.greenmetrik.greenmetrikapi.model.WaterConsumption;
import java.time.LocalDate;

public record WaterConsumptionResponse(
    Long id,
    LocalDate periodStartDate,
    LocalDate periodEndDate,
    double consumptionTon,
    double recycledWaterUsageLiters,
    double treatedWaterConsumptionLiters,
    Long unitId,
    String unitName,
    String submittedByUsername
) {
    public static WaterConsumptionResponse fromEntity(WaterConsumption entity) {
        return new WaterConsumptionResponse(
            entity.getId(),
            entity.getPeriodStartDate(),
            entity.getPeriodEndDate(),
            entity.getConsumptionTon(),
            entity.getRecycledWaterUsageLiters(),
            entity.getTreatedWaterConsumptionLiters(),
            entity.getUnit().getId(),
            entity.getUnit().getName(),
            entity.getUser().getUsername()
        );
    }
}
