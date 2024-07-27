package com.example.userService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.userService.model.Role;
import com.example.userService.model.User;
import com.example.userService.repository.RoleRepository;
import com.example.userService.repository.UserRepository;

@Component
public class SetupLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;
 
    @Autowired
    private RoleRepository roleRepository;
 
    @Autowired
    private PasswordEncoder passwordEncoder;
 
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
 
        if (alreadySetup)
            return;
 
        createRoleIfNotFound("ADMIN");
        createRoleIfNotFound("USER");

        Role adminRole = roleRepository.findByName("ADMIN");
        User user = new User();
        user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("admin@test.com");
        user.setVerified(true);
        user.setRole(adminRole);
        userRepository.save(user);

        alreadySetup = true;
    }

    @Transactional
    Role createRoleIfNotFound(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            roleRepository.save(role);
        }
        return role;
    }
}
