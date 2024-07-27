package com.example.realEstateService.model;

import jakarta.persistence.*;

@Entity
@Access(value=AccessType.FIELD)
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public Role(String name){
        this.name = name;
    }

        // Getters and setters
    public Long getId() {
            return id;
    }
    
    public void setId(Long id) {
            this.id = id;
    }

        // Getters and setters
    public String getName() {
            return this.name;
    }
    
    public void setName(String name) {
            this.name = name;
    }

}
