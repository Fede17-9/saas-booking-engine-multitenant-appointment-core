package com.saas.booking_engine.application.dto.auth;

import com.saas.booking_engine.domain.enums.UserRole;
import java.util.UUID;

/**
 * DTO de salida con la información del usuario autenticado y el JWT emitido.
 */
public record AuthResponse(
    UUID id,
    UUID tenantId,
    String email,
    String firstName,
    String lastName,
    UserRole role,
    String token) {}
