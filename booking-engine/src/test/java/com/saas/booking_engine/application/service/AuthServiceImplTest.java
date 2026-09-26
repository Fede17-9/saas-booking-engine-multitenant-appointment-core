package com.saas.booking_engine.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.saas.booking_engine.application.dto.auth.RegisterUserRequest;
import com.saas.booking_engine.application.dto.auth.LoginRequest;
import com.saas.booking_engine.application.dto.auth.AuthResponse;
import com.saas.booking_engine.domain.enums.UserRole;
import com.saas.booking_engine.domain.model.Tenant;
import com.saas.booking_engine.domain.model.User;
import com.saas.booking_engine.domain.repository.TenantRepository;
import com.saas.booking_engine.domain.repository.UserRepository;
import com.saas.booking_engine.infrastructure.security.JwtService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

  @Mock private UserRepository userRepository;
  @Mock private TenantRepository tenantRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private AuthenticationManager authenticationManager;
  @Mock private JwtService jwtService;

  @InjectMocks private AuthServiceImpl authService;

  @Test
  void register_shouldCreateUserAndReturnAuthResponse() {
    UUID tenantId = UUID.randomUUID();
    Tenant tenant = Tenant.builder().id(tenantId).name("Acme").slug("acme").build();
    RegisterUserRequest request =
        new RegisterUserRequest(
            tenantId, "ana@acme.com", "secret123", "Ana", "Lopez", "555", UserRole.CUSTOMER);

    when(tenantRepository.findById(tenantId)).thenReturn(Optional.of(tenant));
    when(userRepository.existsByEmailAndTenantId("ana@acme.com", tenantId)).thenReturn(false);
    when(passwordEncoder.encode("secret123")).thenReturn("hashed-password");
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(jwtService.generateToken(any(), anyMap())).thenReturn("jwt-token");

    AuthResponse response = authService.register(request);

    assertNotNull(response);
    assertEquals("jwt-token", response.token());
    assertEquals("ana@acme.com", response.email());
    assertEquals(UserRole.CUSTOMER, response.role());
  }

  @Test
  void register_shouldRejectSuperAdminRole() {
    RegisterUserRequest request =
        new RegisterUserRequest(
            null, "admin@platform.com", "secret123", "Platform", "Admin", null,
            UserRole.SUPER_ADMIN);

    assertThrows(
      com.saas.booking_engine.domain.exception.PublicRegistrationRoleNotAllowedException.class,
      () -> authService.register(request));
  }

  @Test
  void login_shouldAuthenticateUserAndReturnToken() {
    UUID tenantId = UUID.randomUUID();
    Tenant tenant = Tenant.builder().id(tenantId).slug("acme").build();
    User user =
        User.builder()
            .id(UUID.randomUUID())
            .tenant(tenant)
            .email("ana@acme.com")
            .passwordHash("hashed-password")
            .firstName("Ana")
            .lastName("Lopez")
            .role(UserRole.STAFF)
            .isActive(true)
            .build();

    Authentication authentication = new UsernamePasswordAuthenticationToken("ana@acme.com", "secret123");
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    when(userRepository.findByEmail("ana@acme.com")).thenReturn(Optional.of(user));
    when(jwtService.generateToken(any(), anyMap())).thenReturn("login-jwt");

    AuthResponse response = authService.login(new LoginRequest("ana@acme.com", "secret123"));

    assertEquals("login-jwt", response.token());
    assertEquals("ana@acme.com", response.email());
    assertEquals(UserRole.STAFF, response.role());
  }
}
