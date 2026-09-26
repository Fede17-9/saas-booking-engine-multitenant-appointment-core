package com.saas.booking_engine.infrastructure.security;

import com.saas.booking_engine.domain.enums.UserRole;
import com.saas.booking_engine.domain.model.User;
import com.saas.booking_engine.domain.repository.UserRepository;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Crea de forma opcional el primer SUPER_ADMIN mediante configuración de entorno.
 */
@Component
@RequiredArgsConstructor
public class SuperAdminBootstrap implements ApplicationRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${application.security.bootstrap.super-admin.enabled:false}")
  private boolean enabled;

  @Value("${application.security.bootstrap.super-admin.email:}")
  private String email;

  @Value("${application.security.bootstrap.super-admin.password:}")
  private String password;

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    if (!enabled) {
      return;
    }

    String normalizedEmail = normalizeEmail(email);
    if (normalizedEmail.isBlank() || password.isBlank()) {
      throw new IllegalStateException(
          "El bootstrap de SUPER_ADMIN requiere email y password configurados");
    }

    if (userRepository.existsByEmailAndTenantIsNull(normalizedEmail)) {
      return;
    }

    User superAdmin = User.builder()
        .email(normalizedEmail)
        .passwordHash(passwordEncoder.encode(password))
        .firstName("Platform")
        .lastName("Administrator")
        .role(UserRole.SUPER_ADMIN)
        .isActive(true)
        .build();

    userRepository.save(superAdmin);
  }

  private String normalizeEmail(String value) {
    return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
  }
}
