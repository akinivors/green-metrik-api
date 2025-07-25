package com.greenmetrik.greenmetrikapi.repository;

import com.greenmetrik.greenmetrikapi.dto.PublicStatsDTO;
import com.greenmetrik.greenmetrikapi.model.WaterConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WaterConsumptionRepository extends JpaRepository<WaterConsumption, Long>, JpaSpecificationExecutor<WaterConsumption> {
    // Custom query methods can be added here

    @Query("SELECT SUM(w.consumptionTon) FROM WaterConsumption w WHERE w.periodEndDate >= :startDate AND w.periodEndDate <= :endDate")
    Double findTotalConsumptionBetweenDates(LocalDate startDate, LocalDate endDate);

    @Query("SELECT new com.greenmetrik.greenmetrikapi.dto.PublicStatsDTO$MonthlyConsumptionGraphPoint(TO_CHAR(DATE_TRUNC('month', w.periodEndDate), 'YYYY-MM'), 0.0, SUM(w.consumptionTon)) FROM WaterConsumption w WHERE w.periodEndDate >= :startDate AND w.periodEndDate <= :endDate GROUP BY DATE_TRUNC('month', w.periodEndDate) ORDER BY DATE_TRUNC('month', w.periodEndDate)")
    List<PublicStatsDTO.MonthlyConsumptionGraphPoint> findMonthlyConsumptionBetweenDates(LocalDate startDate, LocalDate endDate);
}
