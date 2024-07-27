package com.example.userService.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
    @Schema(description = "email", example = "mina@gmail.com")
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    String email,

    @Schema(description = "password", example = "123456")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    String password, 
    
    @Schema(description = "phonenumber", example = "00159843264")
    String phonenumber,
    
    @Schema(description = "Street", example = "Maxstr.")
    String street,
    
    @Schema(description = "house", example = "26B")
    String house,
    
    @Schema(description = "postcode", example = "12599")
    String postcode)
    
    
    {

}
