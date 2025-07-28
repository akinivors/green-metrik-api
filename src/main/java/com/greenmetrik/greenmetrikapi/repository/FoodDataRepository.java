package com.greenmetrik.greenmetrikapi.repository;

import com.greenmetrik.greenmetrikapi.dto.PublicStatsDTO;
import com.greenmetrik.greenmetrikapi.model.FoodData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodDataRepository extends JpaRepository<FoodData, Long>, JpaSpecificationExecutor<FoodData> {
    // Custom query methods can be added here

    @Query("SELECT new com.greenmetrik.greenmetrikapi.dto.PublicStatsDTO$FoodSummary(SUM(f.productionKg), SUM(f.consumptionKg), SUM(f.wasteOilLt)) FROM FoodData f WHERE f.dataDate >= :startDate AND f.dataDate <= :endDate")
    PublicStatsDTO.FoodSummary findSummaryBetweenDates(LocalDate startDate, LocalDate endDate);

    @Query("SELECT new com.greenmetrik.greenmetrikapi.dto.PublicStatsDTO$DailyFoodGraphPoint(f.dataDate, SUM(f.productionKg), SUM(f.consumptionKg), SUM(f.wasteOilLt)) FROM FoodData f WHERE f.dataDate >= :startDate AND f.dataDate <= :endDate GROUP BY f.dataDate ORDER BY f.dataDate ASC")
    List<PublicStatsDTO.DailyFoodGraphPoint> findDailyStatsBetweenDates(LocalDate startDate, LocalDate endDate);

    @Query("SELECT new com.greenmetrik.greenmetrikapi.dto.PublicStatsDTO$MonthlyFoodGraphPoint(TO_CHAR(f.dataDate, 'YYYY-MM'), SUM(f.productionKg), SUM(f.consumptionKg), SUM(f.wasteOilLt)) FROM FoodData f WHERE f.dataDate >= :startDate AND f.dataDate <= :endDate GROUP BY TO_CHAR(f.dataDate, 'YYYY-MM') ORDER BY TO_CHAR(f.dataDate, 'YYYY-MM') ASC")
    List<PublicStatsDTO.MonthlyFoodGraphPoint> findMonthlyStatsBetweenDates(LocalDate startDate, LocalDate endDate);
}
