package com.nttbank.microservices.authjwtserver.model.request;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record RegisterRequest(

    @NotNull(message = "Username cannot be null")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    String username,

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    String email,

    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number should be valid")
    String phoneNumber,

    @NotNull(message = "Imei cannot be null")
    String imei,

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters")
    String password,

    LocalDateTime createdAt
){}
