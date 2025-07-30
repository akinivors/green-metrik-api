package com.greenmetrik.greenmetrikapi.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "campus_metrics")
@SQLDelete(sql = "UPDATE campus_metrics SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
public class CampusMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "metric_key", nullable = false)
    private String metricKey;

    @Column(name = "metric_value", nullable = false)
    private String metricValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetricCategory category;

    @Column(name = "metric_date", nullable = false)
    private LocalDate metricDate;

    @Column
    private String description;

    @Column(name = "is_deleted")
    private boolean isDeleted = false; // New soft delete field
}
