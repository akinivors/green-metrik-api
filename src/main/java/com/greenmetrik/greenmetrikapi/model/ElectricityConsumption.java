package com.greenmetrik.greenmetrikapi.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "electricity_consumption")
@SQLDelete(sql = "UPDATE electricity_consumption SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
public class ElectricityConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate periodStartDate;
    private LocalDate periodEndDate;

    private double consumptionKwh;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_deleted")
    private boolean isDeleted = false; // New soft delete field
}