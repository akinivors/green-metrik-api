package com.greenmetrik.greenmetrikapi.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false) // Ensures username is unique and not null.
    private String username;

    @Column(nullable = false) // Ensures password is not null.
    private String password;

    private String fullName;

    @Enumerated(EnumType.STRING) // Stores the enum as a string (e.g., "ADMIN").
    @Column(nullable = false)
    private Role role;

    @ManyToOne // Defines a many-to-one relationship (many users can be in one unit).
    @JoinColumn(name = "unit_id") // Specifies the foreign key column.
    private Unit unit;

    @Column(nullable = false)
    private boolean isTemporaryPassword = true; // Default to true for new users
}