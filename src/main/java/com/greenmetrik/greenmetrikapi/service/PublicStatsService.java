package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.PublicStatsDTO;
import com.greenmetrik.greenmetrikapi.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PublicStatsService {
    private final ElectricityConsumptionRepository elecRepo;
    private final WaterConsumptionRepository waterRepo;
    private final WasteDataRepository wasteRepo;
    private final VehicleEntryRepository vehicleRepo;

    public PublicStatsService(ElectricityConsumptionRepository elecRepo, WaterConsumptionRepository waterRepo, WasteDataRepository wasteRepo, VehicleEntryRepository vehicleRepo) {
        this.elecRepo = elecRepo;
        this.waterRepo = waterRepo;
        this.wasteRepo = wasteRepo;
        this.vehicleRepo = vehicleRepo;
    }

    public PublicStatsDTO.PublicStatsResponse getStats(String category, String period) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate;

        switch (period) {
            case "last_week":
                startDate = endDate.minusWeeks(1);
                break;
            case "last_month":
                startDate = endDate.minusMonths(1);
                break;
            case "last_year":
                startDate = endDate.minusYears(1);
                break;
            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }

        return switch (category) {
            case "consumption" -> getConsumptionStats(startDate, endDate, period);
            case "waste" -> getWasteStats(startDate, endDate, period);
            case "vehicles" -> getVehicleStats(startDate, endDate, period);
            default -> throw new IllegalArgumentException("Invalid category: " + category);
        };
    }

    private PublicStatsDTO.PublicStatsResponse getConsumptionStats(LocalDate startDate, LocalDate endDate, String period) {
        // This method's logic for fetching total consumption can be improved.
        // For now, we leave it as is, but a single query would be more efficient.
        Double totalElec = elecRepo.findAll().stream().mapToDouble(e -> e.getConsumptionKwh()).sum();
        Double totalWater = waterRepo.findAll().stream().mapToDouble(w -> w.getConsumptionTon()).sum();
        var summary = new PublicStatsDTO.ConsumptionSummary(totalElec, totalWater);

        Object graphData = null;
        if ("last_year".equals(period)) {
            List<PublicStatsDTO.MonthlyConsumptionGraphPoint> elecData = elecRepo.findMonthlyConsumptionBetweenDates(startDate, endDate);
            List<PublicStatsDTO.MonthlyConsumptionGraphPoint> waterData = waterRepo.findMonthlyConsumptionBetweenDates(startDate, endDate);

            Map<String, PublicStatsDTO.MonthlyConsumptionGraphPoint> mergedMap = Stream.concat(elecData.stream(), waterData.stream())
                .collect(Collectors.toMap(
                    PublicStatsDTO.MonthlyConsumptionGraphPoint::month,
                    p -> p,
                    (p1, p2) -> new PublicStatsDTO.MonthlyConsumptionGraphPoint(p1.month(),
                        (p1.electricityKwh() != null ? p1.electricityKwh() : 0.0) + (p2.electricityKwh() != null ? p2.electricityKwh() : 0.0),
                        (p1.waterTon() != null ? p1.waterTon() : 0.0) + (p2.waterTon() != null ? p2.waterTon() : 0.0)),
                    java.util.LinkedHashMap::new
                ));
            graphData = new ArrayList<>(mergedMap.values());
        }
        return new PublicStatsDTO.PublicStatsResponse(summary, graphData);
    }

    private PublicStatsDTO.PublicStatsResponse getWasteStats(LocalDate startDate, LocalDate endDate, String period) {
        var summary = wasteRepo.findSummaryBetweenDates(startDate, endDate);
        Object graphData = "last_year".equals(period) ?
            wasteRepo.findMonthlyStatsBetweenDates(startDate, endDate) :
            wasteRepo.findDailyStatsBetweenDates(startDate, endDate);
        return new PublicStatsDTO.PublicStatsResponse(summary, graphData);
    }

    private PublicStatsDTO.PublicStatsResponse getVehicleStats(LocalDate startDate, LocalDate endDate, String period) {
        var summary = vehicleRepo.findSummaryBetweenDates(startDate, endDate);
        Object graphData = "last_year".equals(period) ?
            vehicleRepo.findMonthlyStatsBetweenDates(startDate, endDate) :
            vehicleRepo.findDailyStatsBetweenDates(startDate, endDate);
        return new PublicStatsDTO.PublicStatsResponse(summary, graphData);
    }
}
