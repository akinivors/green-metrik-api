package com.greenmetrik.greenmetrikapi.dto;

import java.time.LocalDate;

public record ElectricityConsumptionRequest(
    LocalDate periodStartDate,
    LocalDate periodEndDate,
    double consumptionKwh,
    Long unitId
) {}
