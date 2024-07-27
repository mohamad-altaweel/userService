package com.example.userService.repository;

import com.example.userService.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@SuppressWarnings("preview")
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
        // Custom query to find a user by email
        Optional<User> findByEmail(String email);


}
