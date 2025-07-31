package com.greenmetrik.greenmetrikapi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "activity_log")
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String eventType; // e.g., "USER_DELETED", "WASTE_DATA_CREATED"

    @Column(nullable = false)
    private String description; // e.g., "Admin deleted user 'testuser'."

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // The user who performed the action

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
