package com.greenmetrik.greenmetrikapi.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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
    private String actionType; // e.g., "CREATE", "DELETE", "UPDATE"

    @Column(nullable = false)
    private String dataType; // e.g., "USER", "WASTE_DATA", "VEHICLE_ENTRY", "METRIC"

    @Column(nullable = false)
    private String description; // e.g., "Admin deleted user 'testuser'."

    @Column(nullable = false, updatable = false)
    private String username; // NEW FIELD to store the username snapshot

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true) // CHANGE: nullable = true
    @OnDelete(action = OnDeleteAction.SET_NULL) // NEW: Set user to null on delete
    private User user; // The user who performed the action

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
