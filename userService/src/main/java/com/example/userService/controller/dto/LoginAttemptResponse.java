package com.example.userService.controller.dto;

import com.example.userService.model.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Date;

public record LoginAttemptResponse(
    @Schema(description = "The date and time of the login attempt") LocalDateTime createdAt,
    @Schema(description = "The login status") boolean success) {

  public static LoginAttemptResponse convertToDTO(LoginAttempt loginAttempt) {
    return new LoginAttemptResponse(loginAttempt.getCreatedAt(), loginAttempt.getStatus());
  }
}
