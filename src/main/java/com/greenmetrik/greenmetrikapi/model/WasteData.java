package com.greenmetrik.greenmetrikapi.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "waste_data")
@SQLDelete(sql = "UPDATE waste_data SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
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

    @Column(name = "is_deleted")
    private boolean isDeleted = false; // New soft delete field
}
