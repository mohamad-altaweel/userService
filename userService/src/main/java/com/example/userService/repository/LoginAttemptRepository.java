package com.example.userService.repository;

import java.util.List;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.example.userService.model.LoginAttempt;
import org.springframework.util.Assert;


@Repository
public class LoginAttemptRepository {
  private static final int RECENT_COUNT = 10; // can be in the config
  private static final String INSERT = "INSERT INTO users.login_attempt (email, success) VALUES(:email, :success)";
  private static final String FIND_RECENT = "SELECT * FROM users.login_attempt WHERE email = :email ORDER BY created_at DESC LIMIT :recentCount";

  private final JdbcClient jdbcClient;

  public LoginAttemptRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public void add(LoginAttempt loginAttempt) {
    long affected = jdbcClient.sql(INSERT)
        .param("email", loginAttempt.getEmail())
        .param("success", loginAttempt.getStatus())
        .update();

    Assert.isTrue(affected == 1, "Could not add login attempt.");
  }

  public List<LoginAttempt> findRecent(String email) {
    return jdbcClient.sql(FIND_RECENT)
        .param("email", email)
        .param("recentCount", RECENT_COUNT)
        .query(LoginAttempt.class)
        .list();
  }
}
