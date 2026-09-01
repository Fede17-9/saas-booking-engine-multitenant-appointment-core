package com.saas.booking_engine.application.service;

import com.saas.booking_engine.application.dto.auth.AuthResponse;
import com.saas.booking_engine.application.dto.auth.LoginRequest;
import com.saas.booking_engine.application.dto.auth.RegisterUserRequest;

/**
 * Casos de uso de autenticación y registro de usuarios.
 */
public interface AuthService {

  AuthResponse register(RegisterUserRequest request);

  AuthResponse login(LoginRequest request);
}
