package com.greenmetrik.greenmetrikapi.repository;

import com.greenmetrik.greenmetrikapi.dto.PublicStatsDTO;
import com.greenmetrik.greenmetrikapi.model.VehicleEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VehicleEntryRepository extends JpaRepository<VehicleEntry, Long>, JpaSpecificationExecutor<VehicleEntry> {

    @Query("SELECT new com.greenmetrik.greenmetrikapi.dto.PublicStatsDTO$VehicleSummary(SUM(v.publicTransportCount), SUM(v.privateVehicleCount), SUM(v.motorcycleCount), SUM(v.zevCount)) FROM VehicleEntry v WHERE v.entryDate >= :startDate AND v.entryDate <= :endDate")
    PublicStatsDTO.VehicleSummary findSummaryBetweenDates(LocalDate startDate, LocalDate endDate);

    @Query("SELECT new com.greenmetrik.greenmetrikapi.dto.PublicStatsDTO$DailyVehicleGraphPoint(v.entryDate, SUM(v.publicTransportCount), SUM(v.privateVehicleCount), SUM(v.motorcycleCount), SUM(v.zevCount)) FROM VehicleEntry v WHERE v.entryDate >= :startDate AND v.entryDate <= :endDate GROUP BY v.entryDate ORDER BY v.entryDate ASC")
    List<PublicStatsDTO.DailyVehicleGraphPoint> findDailyStatsBetweenDates(LocalDate startDate, LocalDate endDate);

    @Query("SELECT new com.greenmetrik.greenmetrikapi.dto.PublicStatsDTO$MonthlyVehicleGraphPoint(TO_CHAR(v.entryDate, 'YYYY-MM'), SUM(v.publicTransportCount), SUM(v.privateVehicleCount), SUM(v.motorcycleCount), SUM(v.zevCount)) FROM VehicleEntry v WHERE v.entryDate >= :startDate AND v.entryDate <= :endDate GROUP BY TO_CHAR(v.entryDate, 'YYYY-MM') ORDER BY TO_CHAR(v.entryDate, 'YYYY-MM') ASC")
    List<PublicStatsDTO.MonthlyVehicleGraphPoint> findMonthlyStatsBetweenDates(LocalDate startDate, LocalDate endDate);

    @Query(value = "SELECT ve.* FROM vehicle_entries ve LEFT JOIN users u ON ve.user_id = u.id WHERE (?1 IS NULL OR ve.entry_date >= ?1) AND (?2 IS NULL OR ve.entry_date <= ?2)",
           countQuery = "SELECT COUNT(*) FROM vehicle_entries ve WHERE (?1 IS NULL OR ve.entry_date >= ?1) AND (?2 IS NULL OR ve.entry_date <= ?2)",
           nativeQuery = true)
    Page<VehicleEntry> findAllWithDeletedUsers(java.time.LocalDate startDate, java.time.LocalDate endDate, Pageable pageable);

    @Query("SELECT ve FROM VehicleEntry ve LEFT JOIN FETCH ve.user u WHERE (:startDate IS NULL OR ve.entryDate >= :startDate) AND (:endDate IS NULL OR ve.entryDate <= :endDate)")
    Page<VehicleEntry> findAllWithUsersIncludingDeleted(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
