package com.greenmetrik.greenmetrikapi.dto;

import com.greenmetrik.greenmetrikapi.model.WasteData;
import java.time.LocalDate;

public record WasteDataResponse(
    Long id,
    LocalDate dataDate,
    double organicProductionKg,
    double organicConsumptionKg,
    double organicTreatedKg,
    double inorganicProductionKg,
    double inorganicConsumptionKg,
    double inorganicRecycledKg,
    double toxicWasteKg,
    double treatedToxicWasteKg,
    double sewageDisposalLiters,
    String submittedByUsername
) {
    public static WasteDataResponse fromEntity(WasteData entity) {
        return new WasteDataResponse(
            entity.getId(),
            entity.getDataDate(),
            entity.getOrganicProductionKg(),
            entity.getOrganicConsumptionKg(),
            entity.getOrganicTreatedKg(),
            entity.getInorganicProductionKg(),
            entity.getInorganicConsumptionKg(),
            entity.getInorganicRecycledKg(),
            entity.getToxicWasteKg(),
            entity.getTreatedToxicWasteKg(),
            entity.getSewageDisposalLiters(),
            entity.getUser().getUsername()
        );
    }
}
