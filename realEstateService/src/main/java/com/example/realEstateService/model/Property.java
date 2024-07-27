package com.example.realEstateService.model;

import jakarta.persistence.*;
import java.util.Date;


import java.util.ArrayList;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;

import com.example.realEstateService.model.PropertyDir;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;

import jakarta.validation.constraints.Pattern;

@Entity
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;
    private double size;
    private int numberOfRooms;
    
    private PropertyDir direction;

    private int floor;
    private String street;
    private String houseNumber;
    private String postcode;

    @Lob
    private String description;
    
    @Enumerated(EnumType.STRING)
    private PropertyStatus status; // Use enum here

    private double price;

    private String owner; // Assuming there's a User entity for the owner

    @ElementCollection(fetch = FetchType.LAZY)
    private ArrayList<String> imageUrls = new ArrayList<String>();

    // Getters and Setters...
    
    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
    
    // Add a method to help add images more easily
    public void addImageUrl(String imageUrl) {
        this.imageUrls.add(imageUrl);
    }

    public Property(){

    }

    public Long getId() {
        return id;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public double getSize() {
        return size;
    }
    
    public void setSize(double size) {
        this.size = size;
    }
    
    public int getNumberOfRooms() {
        return numberOfRooms;
    }
    
    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }
    
    public PropertyDir getDirection() {
        return direction;
    }
    
    public void setDirection(PropertyDir direction) {
        this.direction = direction;
    }
    
    public int getFloor() {
        return floor;
    }
    
    public void setFloor(int floor) {
        this.floor = floor;
    }
    
    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }
    
    public String getHouseNumber() {
        return houseNumber;
    }
    
    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }
    
    public String getPostcode() {
        return postcode;
    }
    
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public PropertyStatus getStatus() {
        return status;
    }
    
    public void setStatus(PropertyStatus status) {
        this.status = status;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public String getOwner() {
        return owner;
    }
    
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
}

