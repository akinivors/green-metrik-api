package com.greenmetrik.greenmetrikapi.repository;

import com.greenmetrik.greenmetrikapi.model.CampusMetrics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface CampusMetricsRepository extends JpaRepository<CampusMetrics, Long>, JpaSpecificationExecutor<CampusMetrics> {

    // This is your more robust query for the public dashboards
    @Query("SELECT cm FROM CampusMetrics cm WHERE cm.metricKey = :metricKey ORDER BY cm.id DESC LIMIT 1")
    Optional<CampusMetrics> findByMetricKey(@Param("metricKey") String metricKey);

    // New method to find all metrics by metricKey ordered by metricDate descending, then id descending
    Page<CampusMetrics> findByMetricKeyOrderByMetricDateDescIdDesc(String metricKey, Pageable pageable);

    // This is also your more robust query, which the admin service will now use
    @Query(
        value = "SELECT cm.* FROM campus_metrics cm " +
                "INNER JOIN (" +
                "    SELECT metric_key, MAX(id) as max_id " +
                "    FROM campus_metrics " +
                "    GROUP BY metric_key" +
                ") latest ON cm.metric_key = latest.metric_key AND cm.id = latest.max_id " +
                "WHERE (:category IS NULL OR cm.category = :category) " +
                "AND (CAST(:startDate AS date) IS NULL OR cm.metric_date >= :startDate) " +
                "AND (CAST(:endDate AS date) IS NULL OR cm.metric_date <= :endDate) " +
                "ORDER BY cm.category, cm.id",
        nativeQuery = true
    )
    Page<CampusMetrics> findLatestFiltered(
        Pageable pageable,
        @Param("category") String category,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    // This is your coworker's new feature for a future admin panel
    @Query("SELECT cm.metricKey as metricKey, MAX(cm.metricDate) as maxDate FROM CampusMetrics cm GROUP BY cm.metricKey")
    List<Map<String, Object>> findLatestMetricDates();
}
