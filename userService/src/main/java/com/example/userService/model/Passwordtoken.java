package com.example.userService.model;
import jakarta.persistence.*;
import java.util.Date;
import java.security.SecureRandom;
import java.util.Base64;

@Entity
public class Passwordtoken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate;
    



    public Passwordtoken(User user, Date expiryDate, String token){
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
