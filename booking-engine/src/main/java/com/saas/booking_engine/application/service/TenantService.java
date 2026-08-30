package com.saas.booking_engine.application.service;

import com.saas.booking_engine.application.dto.tenant.CreateTenantRequest;
import com.saas.booking_engine.application.dto.tenant.TenantResponse;

/**
 * Casos de uso relacionados con la gestión de tenants.
 */
public interface TenantService {

  /**
   * Crea un nuevo tenant validando la unicidad del slug.
   *
   * @param request datos del tenant a registrar
   * @return representación del tenant persistido
   * @throws com.saas.booking_engine.domain.exception.DuplicateSlugException si el slug ya existe
   */
  TenantResponse createTenant(CreateTenantRequest request);
}
