package com.example.userService.repository;
import com.example.userService.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("preview")
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
            // Custom query to find a role by value
        Role findByName(String role);    
}
