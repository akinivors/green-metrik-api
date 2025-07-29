package com.greenmetrik.greenmetrikapi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "waste_data")
public class WasteData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataDate;

    private double organicProductionKg;
    private double organicConsumptionKg;
    private double organicTreatedKg;

    private double inorganicProductionKg;
    private double inorganicConsumptionKg;
    private double inorganicRecycledKg;

    private double toxicWasteKg;
    private double treatedToxicWasteKg;

    private double sewageDisposalLiters;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
