package com.greenmetrik.greenmetrikapi.repository;

import com.greenmetrik.greenmetrikapi.dto.PublicStatsDTO;
import com.greenmetrik.greenmetrikapi.model.WasteData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WasteDataRepository extends JpaRepository<WasteData, Long>, JpaSpecificationExecutor<WasteData> {

    @Query("SELECT new com.greenmetrik.greenmetrikapi.dto.PublicStatsDTO$WasteSummary(" +
           "SUM(w.organicProductionKg), SUM(w.organicConsumptionKg), SUM(w.organicTreatedKg), " +
           "SUM(w.inorganicProductionKg), SUM(w.inorganicConsumptionKg), SUM(w.inorganicRecycledKg), " +
           "SUM(w.toxicWasteKg), SUM(w.treatedToxicWasteKg), SUM(w.sewageDisposalLiters)" +
           ") FROM WasteData w WHERE w.dataDate >= :startDate AND w.dataDate <= :endDate")
    PublicStatsDTO.WasteSummary findSummaryBetweenDates(LocalDate startDate, LocalDate endDate);

    @Query("SELECT new com.greenmetrik.greenmetrikapi.dto.PublicStatsDTO$DailyWasteGraphPoint(w.dataDate, SUM(w.organicProductionKg), SUM(w.inorganicProductionKg), SUM(w.toxicWasteKg)) FROM WasteData w WHERE w.dataDate >= :startDate AND w.dataDate <= :endDate GROUP BY w.dataDate ORDER BY w.dataDate ASC")
    List<PublicStatsDTO.DailyWasteGraphPoint> findDailyStatsBetweenDates(LocalDate startDate, LocalDate endDate);

    @Query("SELECT new com.greenmetrik.greenmetrikapi.dto.PublicStatsDTO$MonthlyWasteGraphPoint(TO_CHAR(w.dataDate, 'YYYY-MM'), SUM(w.organicProductionKg), SUM(w.inorganicProductionKg), SUM(w.toxicWasteKg)) FROM WasteData w WHERE w.dataDate >= :startDate AND w.dataDate <= :endDate GROUP BY TO_CHAR(w.dataDate, 'YYYY-MM') ORDER BY TO_CHAR(w.dataDate, 'YYYY-MM') ASC")
    List<PublicStatsDTO.MonthlyWasteGraphPoint> findMonthlyStatsBetweenDates(LocalDate startDate, LocalDate endDate);
}
