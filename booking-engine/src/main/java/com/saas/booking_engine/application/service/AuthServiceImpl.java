package com.saas.booking_engine.application.service;

import com.saas.booking_engine.application.dto.auth.AuthResponse;
import com.saas.booking_engine.application.dto.auth.LoginRequest;
import com.saas.booking_engine.application.dto.auth.RegisterUserRequest;
import com.saas.booking_engine.domain.enums.UserRole;
import com.saas.booking_engine.domain.exception.EmailAlreadyExistsException;
import com.saas.booking_engine.domain.exception.TenantNotFoundException;
import com.saas.booking_engine.domain.model.Tenant;
import com.saas.booking_engine.domain.model.User;
import com.saas.booking_engine.domain.repository.TenantRepository;
import com.saas.booking_engine.domain.repository.UserRepository;
import com.saas.booking_engine.infrastructure.security.JwtService;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación de la autenticación de usuarios y emisión de JWT.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final TenantRepository tenantRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  /**
   * Registra un nuevo usuario, valida la unicidad del email dentro del tenant y emite un JWT.
   *
   * @param request datos del usuario a registrar
   * @return información del usuario autenticado y el token generado
   */
  @Override
  @Transactional
  public AuthResponse register(RegisterUserRequest request) {
    Tenant tenant = resolveTenant(request.tenantId(), request.role());

    if (userRepository.existsByEmailAndTenantId(request.email(), tenant.getId())) {
      throw new EmailAlreadyExistsException(request.email());
    }

    User user = User.builder()
        .tenant(tenant)
        .email(request.email())
        .passwordHash(passwordEncoder.encode(request.password()))
        .firstName(request.firstName())
        .lastName(request.lastName())
        .phone(request.phone())
        .role(request.role())
        .isActive(true)
        .build();

    User savedUser = userRepository.save(user);
    String token = jwtService.generateToken(
        org.springframework.security.core.userdetails.User.withUsername(savedUser.getEmail())
            .password(savedUser.getPasswordHash())
            .authorities(savedUser.getRole().name())
            .build(),
        Map.of("tenantId", savedUser.getTenant() != null ? savedUser.getTenant().getId().toString() : null,
            "role", savedUser.getRole().name()));

    return toAuthResponse(savedUser, token);
  }

  /**
   * Autentica un usuario con email y contraseña y devuelve el JWT asociado.
   *
   * @param request credenciales del usuario
   * @return información del usuario autenticado y el token generado
   */
  @Override
  @Transactional(readOnly = true)
  public AuthResponse login(LoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password()));

    User user = userRepository.findByEmail(request.email())
        .orElseThrow(() -> new com.saas.booking_engine.domain.exception.UserNotFoundException(request.email()));

    String token = jwtService.generateToken(
        org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
            .password(user.getPasswordHash())
            .authorities(user.getRole().name())
            .build(),
        Map.of("tenantId", user.getTenant() != null ? user.getTenant().getId().toString() : null,
            "role", user.getRole().name()));

    return toAuthResponse(user, token);
  }

  private Tenant resolveTenant(UUID tenantId, UserRole role) {
    if (role == UserRole.SUPER_ADMIN) {
      return null;
    }

    return tenantRepository.findById(tenantId)
        .orElseThrow(() -> new TenantNotFoundException(tenantId.toString()));
  }

  private AuthResponse toAuthResponse(User user, String token) {
    return new AuthResponse(
        user.getId(),
        user.getTenant() != null ? user.getTenant().getId() : null,
        user.getEmail(),
        user.getFirstName(),
        user.getLastName(),
        user.getRole(),
        token);
  }
}
