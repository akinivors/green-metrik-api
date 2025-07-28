package com.greenmetrik.greenmetrikapi.repository;

import com.greenmetrik.greenmetrikapi.dto.PublicStatsDTO;
import com.greenmetrik.greenmetrikapi.model.ElectricityConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ElectricityConsumptionRepository extends JpaRepository<ElectricityConsumption, Long>, JpaSpecificationExecutor<ElectricityConsumption> {

    @Query("SELECT SUM(e.consumptionKwh) FROM ElectricityConsumption e WHERE e.periodEndDate >= :startDate AND e.periodEndDate <= :endDate")
    Double findTotalConsumptionBetweenDates(LocalDate startDate, LocalDate endDate);

    @Query("SELECT new com.greenmetrik.greenmetrikapi.dto.PublicStatsDTO$MonthlyConsumptionGraphPoint(TO_CHAR(DATE_TRUNC('month', e.periodEndDate), 'YYYY-MM'), SUM(e.consumptionKwh), 0.0) FROM ElectricityConsumption e WHERE e.periodEndDate >= :startDate AND e.periodEndDate <= :endDate GROUP BY DATE_TRUNC('month', e.periodEndDate) ORDER BY DATE_TRUNC('month', e.periodEndDate) ASC")
    List<PublicStatsDTO.MonthlyConsumptionGraphPoint> findMonthlyConsumptionBetweenDates(LocalDate startDate, LocalDate endDate);
}
