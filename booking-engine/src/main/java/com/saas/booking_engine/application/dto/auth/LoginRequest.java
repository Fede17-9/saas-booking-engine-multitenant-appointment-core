package com.saas.booking_engine.application.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO de entrada para iniciar sesión con email y contraseña.
 */
public record LoginRequest(
    @NotBlank @Email String email,
    @NotBlank String password) {}
