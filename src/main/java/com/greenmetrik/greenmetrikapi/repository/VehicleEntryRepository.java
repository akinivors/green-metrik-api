package com.greenmetrik.greenmetrikapi.repository;

import com.greenmetrik.greenmetrikapi.dto.PublicStatsDTO;
import com.greenmetrik.greenmetrikapi.model.VehicleEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VehicleEntryRepository extends JpaRepository<VehicleEntry, Long>, JpaSpecificationExecutor<VehicleEntry> {

    @Query("SELECT new com.greenmetrik.greenmetrikapi.dto.PublicStatsDTO$VehicleSummary(SUM(v.publicTransportCount), SUM(v.privateVehicleCount)) FROM VehicleEntry v WHERE v.entryDate >= :startDate AND v.entryDate <= :endDate")
    PublicStatsDTO.VehicleSummary findSummaryBetweenDates(LocalDate startDate, LocalDate endDate);

    @Query("SELECT new com.greenmetrik.greenmetrikapi.dto.PublicStatsDTO$DailyVehicleGraphPoint(v.entryDate, SUM(v.publicTransportCount), SUM(v.privateVehicleCount)) FROM VehicleEntry v WHERE v.entryDate >= :startDate AND v.entryDate <= :endDate GROUP BY v.entryDate ORDER BY v.entryDate")
    List<PublicStatsDTO.DailyVehicleGraphPoint> findDailyStatsBetweenDates(LocalDate startDate, LocalDate endDate);

    @Query("SELECT new com.greenmetrik.greenmetrikapi.dto.PublicStatsDTO$MonthlyVehicleGraphPoint(TO_CHAR(v.entryDate, 'YYYY-MM'), SUM(v.publicTransportCount), SUM(v.privateVehicleCount)) FROM VehicleEntry v WHERE v.entryDate >= :startDate AND v.entryDate <= :endDate GROUP BY TO_CHAR(v.entryDate, 'YYYY-MM') ORDER BY TO_CHAR(v.entryDate, 'YYYY-MM')")
    List<PublicStatsDTO.MonthlyVehicleGraphPoint> findMonthlyStatsBetweenDates(LocalDate startDate, LocalDate endDate);
}
