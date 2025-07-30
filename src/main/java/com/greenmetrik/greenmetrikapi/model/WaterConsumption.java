package com.greenmetrik.greenmetrikapi.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "water_consumption")
@SQLDelete(sql = "UPDATE water_consumption SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
public class WaterConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The start and end dates of the billing period.
    private LocalDate periodStartDate;
    private LocalDate periodEndDate;

    private double consumptionTon;

    // New columns
    private double recycledWaterUsageLiters;
    private double treatedWaterConsumptionLiters;

    // The building (unit) this data belongs to.
    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_deleted")
    private boolean isDeleted = false; // New soft delete field
}