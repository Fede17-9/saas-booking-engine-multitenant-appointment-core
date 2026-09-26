package com.saas.booking_engine.application.service;

import com.saas.booking_engine.application.dto.auth.AuthResponse;
import com.saas.booking_engine.application.dto.auth.LoginRequest;
import com.saas.booking_engine.application.dto.auth.RegisterUserRequest;
import com.saas.booking_engine.domain.enums.UserRole;
import com.saas.booking_engine.domain.exception.EmailAlreadyExistsException;
import com.saas.booking_engine.domain.exception.PublicRegistrationRoleNotAllowedException;
import com.saas.booking_engine.domain.exception.TenantNotFoundException;
import com.saas.booking_engine.domain.model.Tenant;
import com.saas.booking_engine.domain.model.User;
import com.saas.booking_engine.domain.repository.TenantRepository;
import com.saas.booking_engine.domain.repository.UserRepository;
import com.saas.booking_engine.infrastructure.security.JwtService;
import java.util.HashMap;
import java.util.Locale;
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
    if (request.role() != UserRole.CUSTOMER) {
      throw new PublicRegistrationRoleNotAllowedException(request.role());
    }

    String email = normalizeEmail(request.email());
    Tenant tenant = resolveTenant(request.tenantId(), request.role());

    if (tenant == null
      ? userRepository.existsByEmailAndTenantIsNull(email)
      : userRepository.existsByEmailAndTenantId(email, tenant.getId())) {
      throw new EmailAlreadyExistsException(email);
    }

    User user = User.builder()
        .tenant(tenant)
        .email(email)
        .passwordHash(passwordEncoder.encode(request.password()))
        .firstName(request.firstName())
        .lastName(request.lastName())
        .phone(request.phone())
        .role(request.role())
        .isActive(true)
        .build();

    User savedUser = userRepository.save(user);
    String token = jwtService.generateToken(toUserDetails(savedUser), buildClaims(savedUser));

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
      new UsernamePasswordAuthenticationToken(normalizeEmail(request.email()), request.password()));

    String email = normalizeEmail(request.email());
    User user = userRepository.findByEmail(email)
      .orElseThrow(() -> new com.saas.booking_engine.domain.exception.UserNotFoundException(email));

    String token = jwtService.generateToken(toUserDetails(user), buildClaims(user));

    return toAuthResponse(user, token);
  }

  private Tenant resolveTenant(UUID tenantId, UserRole role) {
    if (role == UserRole.SUPER_ADMIN) {
      return null;
    }

    if (tenantId == null) {
      throw new TenantNotFoundException("null");
    }

    return tenantRepository.findById(tenantId)
        .orElseThrow(() -> new TenantNotFoundException(tenantId.toString()));
  }

  private String normalizeEmail(String email) {
    return email.trim().toLowerCase(Locale.ROOT);
  }

  private org.springframework.security.core.userdetails.UserDetails toUserDetails(User user) {
    return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
        .password(user.getPasswordHash())
        .authorities(user.getRole().name())
        .build();
  }

  private Map<String, Object> buildClaims(User user) {
    Map<String, Object> claims = new HashMap<>();
    if (user.getTenant() != null) {
      claims.put("tenantId", user.getTenant().getId().toString());
    }
    claims.put("role", user.getRole().name());
    return claims;
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
