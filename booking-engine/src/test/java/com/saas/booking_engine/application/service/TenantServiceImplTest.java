package com.saas.booking_engine.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.saas.booking_engine.application.dto.tenant.TenantResponse;
import com.saas.booking_engine.domain.exception.TenantNotFoundException;
import com.saas.booking_engine.domain.model.Tenant;
import com.saas.booking_engine.domain.repository.TenantRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TenantServiceImplTest {

  @Mock private TenantRepository tenantRepository;

  @InjectMocks private TenantServiceImpl tenantService;

  @Test
  void getTenantBySlug_shouldReturnTenantResponseWhenTenantExists() {
    Tenant tenant = Tenant.builder()
        .id(UUID.randomUUID())
        .name("Acme")
        .slug("acme")
        .contactEmail("hello@acme.com")
        .taxId("X123")
        .isActive(true)
        .build();

    when(tenantRepository.findBySlug("acme")).thenReturn(Optional.of(tenant));

    TenantResponse response = tenantService.getTenantBySlug("acme");

    assertEquals("acme", response.slug());
    assertEquals("Acme", response.name());
  }

  @Test
  void getTenantBySlug_shouldThrowWhenTenantDoesNotExist() {
    when(tenantRepository.findBySlug("missing")).thenReturn(Optional.empty());

    assertThrows(TenantNotFoundException.class, () -> tenantService.getTenantBySlug("missing"));
  }
}
