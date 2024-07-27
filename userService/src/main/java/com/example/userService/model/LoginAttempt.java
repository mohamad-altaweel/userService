package com.example.userService.model;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.*;


@SuppressWarnings("preview")
@Entity
@Access(value=AccessType.FIELD)
@Table(name = "login_attempt")
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private boolean success;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    public LoginAttempt(String email, boolean success) {
        this.email = email;
        this.success = success;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public Boolean getStatus(){
        return success;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // toString, equals, and hashCode methods can be added for better logging and functionality
}
