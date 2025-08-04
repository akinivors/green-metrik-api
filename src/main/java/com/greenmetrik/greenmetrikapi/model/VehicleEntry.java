package com.greenmetrik.greenmetrikapi.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "vehicle_entries")
@SQLDelete(sql = "UPDATE vehicle_entries SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
public class VehicleEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The date the entry was for.
    private LocalDate entryDate;

    private int publicTransportCount;
    private int privateVehicleCount;
    private int motorcycleCount;
    private int zevCount; // New column

    // The user who submitted this data.
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_deleted")
    private boolean isDeleted = false; // New soft delete field
}