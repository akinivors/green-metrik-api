package com.greenmetrik.greenmetrikapi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "electricity_consumption")
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
}