package com.example.realEstateService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.realEstateService.model.Property;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Assuming 'owner' is a User entity linked to the Property entity and it has a 'username' field.
    List<Property> findByOwnerUsername(String username);

    // Find by location (assuming 'location' is a single field in the Property entity).
    List<Property> findByLocation(String location);

    // Find by address components. For this example, using 'street' and 'postcode'.
    // You can define additional or alternative methods based on your address model.
    List<Property> findByStreet(String street);

    List<Property> findByPostcode(String postcode);
}
