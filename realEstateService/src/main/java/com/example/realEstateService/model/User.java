package com.example.realEstateService.model;
import jakarta.persistence.*;
import java.util.Date;

import com.example.realEstateService.model.Role;

import jakarta.validation.constraints.Pattern;


@SuppressWarnings("preview")
@Entity
@Access(value=AccessType.FIELD)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 60) // BCrypt hash length is 60 characters
    private String password;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "name")
    private Role role;

    @Column(name = "street")
    private String Street;

    @Column(name = "house")
    private String house;
    
    @Column(name = "postcode")
    private String postcode;

    @Column(name = "phonenumber")
    private String phonenumber;

    @Column(name = "verified")
    private boolean verified;

    // Constructors

    public User(String email, String password, Role role) {
        this.password = password;
        this.email = email;
        this.role = role;
        this.verified = false;
        this.createdAt = new Date();
    }

    public User(String email, String password, Role role, String phonenumber) {
        this.password = password;
        this.email = email;
        this.role = role;
        this.verified = false;
        this.phonenumber = phonenumber;
        this.createdAt = new Date();
    }
    
    public User(String email, String password, Role role, String street,String phonenumber, String house, String postcode) {
        this.password = password;
        this.email = email;
        this.role = role;
        this.verified = false;
        this.Street = street;
        this.house = house;
        this.postcode = postcode;
        this.phonenumber = phonenumber;
        this.createdAt = new Date();
    }

    public User(){

    }

    public User(String email, String password, Role role, String street, String house, String postcode) {
        this.password = password;
        this.email = email;
        this.role = role;
        this.verified = false;
        this.Street = street;
        this.house = house;
        this.postcode = postcode;
        this.createdAt = new Date();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    
    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getStreet() {
        return Street;
    }

    public String gethouse() {
        return house;
    }

    public String getpostcode() {
        return postcode;
    }

    public void setAddress(String street, String house, String postcode) {
        this.Street = street;
        this.house = house;
        this.postcode = postcode;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Role getRole(){
        return this.role;
    }

    public void setRole(Role role){
        this.role = role;
    }

    // toString, equals, and hashCode methods can be added for better logging and functionality
}
