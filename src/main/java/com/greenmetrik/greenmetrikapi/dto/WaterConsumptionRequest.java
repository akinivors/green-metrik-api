package com.greenmetrik.greenmetrikapi.dto;

import java.time.LocalDate;

public record WaterConsumptionRequest(
    LocalDate periodStartDate,
    LocalDate periodEndDate,
    double consumptionTon,
    Long unitId
) {}
