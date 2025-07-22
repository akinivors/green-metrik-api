package com.greenmetrik.greenmetrikapi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "water_consumption")
public class WaterConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The start and end dates of the billing period.
    private LocalDate periodStartDate;
    private LocalDate periodEndDate;

    private double consumptionTon;

    // The building (unit) this data belongs to.
    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}