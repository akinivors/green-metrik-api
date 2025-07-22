package com.greenmetrik.greenmetrikapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Data // Lombok annotation to create getters, setters, etc.
@Entity // Tells Spring this is a database entity.
@Table(name = "units") // Specifies the table name in the database.
public class Unit {

    @Id // Marks this field as the primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID.
    private Long id;

    private String name;
}