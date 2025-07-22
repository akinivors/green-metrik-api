package com.greenmetrik.greenmetrikapi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "food_data")
public class FoodData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataDate;
    private double productionKg;
    private double consumptionKg;
    private double wasteOilLt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}