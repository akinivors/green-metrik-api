package com.greenmetrik.greenmetrikapi.dto;

import com.greenmetrik.greenmetrikapi.model.FoodData;
import java.time.LocalDate;

public record FoodDataResponse(
    Long id, LocalDate dataDate, double productionKg, double consumptionKg, double wasteOilLt, String submittedByUsername
) {
    public static FoodDataResponse fromEntity(FoodData entity) {
        return new FoodDataResponse(
            entity.getId(), entity.getDataDate(), entity.getProductionKg(),
            entity.getConsumptionKg(), entity.getWasteOilLt(), entity.getUser().getUsername()
        );
    }
}
