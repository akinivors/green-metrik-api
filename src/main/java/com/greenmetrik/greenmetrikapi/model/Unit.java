package com.greenmetrik.greenmetrikapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Lombok annotation to create getters, setters, etc.
@Entity // Tells Spring this is a database entity.
@Table(name = "units") // Specifies the table name in the database.
@NoArgsConstructor // Adds the no-argument constructor
@AllArgsConstructor // Adds a constructor for all fields
public class Unit {

    @Id // Marks this field as the primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID.
    private Long id;

    private String name;

    // A convenience constructor for just the name
    public Unit(String name) {
        this.name = name;
    }
}