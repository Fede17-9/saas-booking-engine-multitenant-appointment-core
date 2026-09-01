package com.saas.booking_engine.application.dto.auth;

import com.saas.booking_engine.domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/**
 * DTO de entrada para registrar un usuario en el sistema.
 */
public record RegisterUserRequest(
    UUID tenantId,
    @NotBlank @Email String email,
    @NotBlank @Size(min = 8) String password,
    @NotBlank String firstName,
    @NotBlank String lastName,
    String phone,
    @NotNull UserRole role) {}
