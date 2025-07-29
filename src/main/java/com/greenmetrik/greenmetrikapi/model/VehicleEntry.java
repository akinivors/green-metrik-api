package com.greenmetrik.greenmetrikapi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "vehicle_entries")
public class VehicleEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The date the entry was for.
    private LocalDate entryDate;

    private int publicTransportCount;
    private int privateVehicleCount;
    private int zevCount; // New column

    // The user who submitted this data.
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}