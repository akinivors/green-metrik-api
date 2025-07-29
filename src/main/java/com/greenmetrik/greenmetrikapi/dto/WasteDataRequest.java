package com.greenmetrik.greenmetrikapi.dto;

import java.time.LocalDate;

public record WasteDataRequest(
    LocalDate dataDate,
    double organicProductionKg,
    double organicConsumptionKg,
    double organicTreatedKg,
    double inorganicProductionKg,
    double inorganicConsumptionKg,
    double inorganicRecycledKg,
    double toxicWasteKg,
    double treatedToxicWasteKg,
    double sewageDisposalLiters
) {}
