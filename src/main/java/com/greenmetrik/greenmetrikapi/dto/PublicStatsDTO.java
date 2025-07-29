package com.greenmetrik.greenmetrikapi.dto;

import java.time.LocalDate;

public class PublicStatsDTO {

    // A generic response wrapper
    public record PublicStatsResponse(Object summary, Object graphData) {}

    // DTOs for Summary data
    public record ConsumptionSummary(Double totalElectricityKwh, Double totalWaterTon) {}
    public record WasteSummary(Double totalOrganicWasteKg, Double totalInorganicWasteKg, Double totalToxicWasteKg) {}
    public record VehicleSummary(Long totalPublicTransport, Long totalPrivateVehicle, Long totalZev) {}

    // DTOs for Graph data points
    public record MonthlyConsumptionGraphPoint(String month, Double electricityKwh, Double waterTon) {}
    public record DailyWasteGraphPoint(LocalDate date, Double organicWaste, Double inorganicWaste, Double toxicWaste) {}
    public record MonthlyWasteGraphPoint(String month, Double organicWaste, Double inorganicWaste, Double toxicWaste) {}
    public record DailyVehicleGraphPoint(LocalDate date, Long publicTransport, Long privateVehicle, Long zev) {}
    public record MonthlyVehicleGraphPoint(String month, Long publicTransport, Long privateVehicle, Long zev) {}
}
