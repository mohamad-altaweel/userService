package com.example.userService.service;

import com.example.userService.model.LoginAttempt;
import com.example.userService.repository.LoginAttemptRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LoginService {

  private final LoginAttemptRepository repository;

  public LoginService(LoginAttemptRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public void addLoginAttempt(String email, boolean success) {
    LoginAttempt loginAttempt = new LoginAttempt(email, success);
    repository.add(loginAttempt);
  }

  public List<LoginAttempt> findRecentLoginAttempts(String email) {
    return repository.findRecent(email);
  }
}
