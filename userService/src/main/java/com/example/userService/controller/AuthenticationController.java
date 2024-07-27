package com.example.userService.controller;

import com.example.userService.controller.dto.SignupRequest;
import com.example.userService.security.JwtHelper;
import com.example.userService.service.UserService;
import com.example.userService.controller.dto.ApiErrorResponse;
import com.example.userService.controller.dto.LoginAttemptResponse;
import com.example.userService.controller.dto.LoginRequest;
import com.example.userService.controller.dto.LoginResponse;
import com.example.userService.model.LoginAttempt;
import com.example.userService.model.User;
import com.example.userService.service.EmailService;
import com.example.userService.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final LoginService loginService;
  private final EmailService emailService;

  public AuthenticationController(AuthenticationManager authenticationManager, UserService userService, LoginService loginService, EmailService emailService) {
    this.authenticationManager = authenticationManager;
    this.userService = userService;
    this.loginService = loginService;
    this.emailService = emailService;
  }

  @Operation(summary = "Signup user")
  @ApiResponse(responseCode = "201")
  @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @PostMapping("/signup")
  public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest requestDto) {
    userService.signup(requestDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(summary = "Authenticate user and return token")
  @CrossOrigin
  @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
  @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @PostMapping(value = "/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
    } catch (BadCredentialsException e) {
      loginService.addLoginAttempt(request.email(), false);
      throw e;
    }
    String token = JwtHelper.generateToken(request.email(),"USER" );
    return ResponseEntity.ok(new LoginResponse(request.email(), token));
  }


  @Operation(summary = "Authenticate user and return token")
  @CrossOrigin
  @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
  @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @PostMapping(value = "/loginAdmin")
  public ResponseEntity<LoginResponse> loginAdmin(@Valid @RequestBody LoginRequest request) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
    } catch (BadCredentialsException e) {
      loginService.addLoginAttempt(request.email(), false);
      throw e;
    }
    String token = JwtHelper.generateToken(request.email(),"ADMIN" );
    return ResponseEntity.ok(new LoginResponse(request.email(), token));
  }

  @Operation(summary = "Get recent login attempts")
  @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
  @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))//forbidden
  @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @GetMapping(value = "/loginAttempts")
  public ResponseEntity<List<LoginAttemptResponse>> loginAttempts(@RequestHeader("Authorization") String token) {
    String email = JwtHelper.extractUsername(token.replace("Bearer ", ""));
    List<LoginAttempt> loginAttempts = loginService.findRecentLoginAttempts(email);
    return ResponseEntity.ok(convertToDTOs(loginAttempts));
  }

  private List<LoginAttemptResponse> convertToDTOs(List<LoginAttempt> loginAttempts) {
    return loginAttempts.stream()
        .map(LoginAttemptResponse::convertToDTO)
        .collect(Collectors.toList());
  }

  @Operation(summary = "Test")
  @GetMapping("/welcome")
  public String welcome() {
      return "Welcome to our Service! You logged in";
  }

  @PostMapping("/reset-password")
  public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody String newPassword) {
      if (!userService.isPasswordResetTokenValid(token)) {
          return ResponseEntity.badRequest().body("Invalid or expired password reset token");
      }
      userService.resetPassword(token, newPassword);
      return ResponseEntity.ok().body("Password has been reset successfully");
    }

  @PostMapping("/requestpassword")
  public ResponseEntity<String> requestPasswordToken(@RequestBody String email){
    this.emailService.sendPasswordResetEmail(email);
    return ResponseEntity.ok().body("Password reset email sent.");
  }
  
  
    // Endpoint to initiate the email verification process
    @PostMapping("/sendVerificationEmail")
    public ResponseEntity<?> sendVerificationEmail(@RequestParam String email) {
      emailService.sendVerificationToken(email);
      return ResponseEntity.ok("Verification email sent.");
    }

    // Endpoint to verify the user's email
    @GetMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        boolean isVerified = userService.verifyUser(token);
        if (isVerified) {
            return ResponseEntity.ok("User verified successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired verification token.");
        }
    }
}