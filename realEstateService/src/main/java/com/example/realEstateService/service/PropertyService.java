package com.example.realEstateService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.realEstateService.model.Property;
import com.example.realEstateService.repository.PropertyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    // Create or Update a property
    public Property saveProperty(Property property) {
        return propertyRepository.save(property);
    }

    // Read all properties
    public List<Property> findAllProperties() {
        return propertyRepository.findAll();
    }

    // Read a single property by ID
    public Optional<Property> findPropertyById(Long id) {
        return propertyRepository.findById(id);
    }

    // Delete a property
    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }

    // Additional methods to find properties by user, location, etc.
    public List<Property> findPropertiesByOwnerUsername(String username) {
        return propertyRepository.findByOwnerUsername(username);
    }

    public List<Property> findPropertiesByLocation(String location) {
        return propertyRepository.findByLocation(location);
    }

    public List<Property> findPropertiesByStreet(String street, String postcode) {
        return propertyRepository.findByStreet(street);
    }

    public List<Property> findPropertiesByPostCode(String postcode) {
        return propertyRepository.findByPostcode(postcode);
    }
}

