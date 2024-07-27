package com.example.userService.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.userService.exception.UserNotFoundException;
import com.example.userService.model.*;

import com.example.userService.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserDetailsService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImp(UserRepository repository) {
      this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = repository.findByEmail(email).orElseThrow(() ->
            new UserNotFoundException(String.format("User does not exist, email: %s", email)));

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .build();
    }
    
    
}
