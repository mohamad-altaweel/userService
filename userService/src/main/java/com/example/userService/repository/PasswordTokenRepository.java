package com.example.userService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.userService.model.Passwordtoken;

public interface PasswordTokenRepository extends JpaRepository<Passwordtoken, Long> {
    Optional<Passwordtoken> findByToken(String token);
}

