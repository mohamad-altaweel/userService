package com.example.userService.model;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate;


    public EmailVerificationToken(User user, Date expiryDate, String token){
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public String getToken(){
        return this.token;
    }

    public User getUser(){
        return this.user;
    }

    public Date getExpiryDate(){
        return this.expiryDate;
    }
}
