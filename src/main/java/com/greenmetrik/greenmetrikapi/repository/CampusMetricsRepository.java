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
import java.util.Optional;

@Repository
public interface CampusMetricsRepository extends JpaRepository<CampusMetrics, Long>, JpaSpecificationExecutor<CampusMetrics> {
    // Custom query method to find a metric by its key
    Optional<CampusMetrics> findByMetricKey(String metricKey);

    // Custom query method to find the latest entry for each metric key
    @Query("SELECT cm FROM CampusMetrics cm WHERE cm.id IN " +
           "(SELECT MAX(cm2.id) FROM CampusMetrics cm2 GROUP BY cm2.metricKey)")
    List<CampusMetrics> findLatestMetrics();

    // ** REPLACED "findLatestMetrics" with this more powerful native query **
    @Query(
        value = "SELECT cm.* FROM campus_metrics cm " +
                "INNER JOIN (" +
                "    SELECT metric_key, MAX(id) as max_id " +
                "    FROM campus_metrics " +
                "    GROUP BY metric_key" +
                ") latest ON cm.metric_key = latest.metric_key AND cm.id = latest.max_id " +
                "WHERE (:category IS NULL OR cm.category = :category) " +
                "AND (CAST(:startDate AS date) IS NULL OR cm.metric_date >= :startDate) " +
                "AND (CAST(:endDate AS date) IS NULL OR cm.metric_date <= :endDate)",
        nativeQuery = true
    )
    Page<CampusMetrics> findLatestFiltered(
        Pageable pageable,
        @Param("category") String category,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}
