package com.greenmetrik.greenmetrikapi.dto;

import java.time.LocalDate;

public class PublicStatsDTO {

    // A generic response wrapper
    public record PublicStatsResponse(Object summary, Object graphData) {}

    // DTOs for Summary data
    public record ConsumptionSummary(Double totalElectricityKwh, Double totalWaterTon) {}
    public record FoodSummary(Double totalProductionKg, Double totalConsumptionKg, Double totalWasteOilLt) {}
    public record VehicleSummary(Long totalPublicTransport, Long totalPrivateVehicle) {}

    // DTOs for Graph data points
    public record MonthlyConsumptionGraphPoint(String month, Double electricityKwh, Double waterTon) {}
    public record DailyFoodGraphPoint(LocalDate date, Double productionKg, Double consumptionKg, Double wasteOilLt) {}
    public record MonthlyFoodGraphPoint(String month, Double productionKg, Double consumptionKg, Double wasteOilLt) {}
    public record DailyVehicleGraphPoint(LocalDate date, Long publicTransport, Long privateVehicle) {}
    public record MonthlyVehicleGraphPoint(String month, Long publicTransport, Long privateVehicle) {}
}
