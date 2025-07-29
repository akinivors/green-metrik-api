package com.greenmetrik.greenmetrikapi.repository;

import com.greenmetrik.greenmetrikapi.model.CampusMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CampusMetricsRepository extends JpaRepository<CampusMetrics, Long>, JpaSpecificationExecutor<CampusMetrics> {
    // Custom query methods can be added here in the future
}
