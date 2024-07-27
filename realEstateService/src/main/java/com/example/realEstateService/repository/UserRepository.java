package com.example.realEstateService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.realEstateService.model.User;

import java.util.Optional;

@SuppressWarnings("preview")
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
        // Custom query to find a user by email
        Optional<User> findByEmail(String email);


}
