package com.saas.booking_engine.application.service;

import com.saas.booking_engine.application.dto.tenant.CreateTenantRequest;
import com.saas.booking_engine.application.dto.tenant.TenantResponse;
import com.saas.booking_engine.application.mapper.TenantMapper;
import com.saas.booking_engine.domain.exception.DuplicateSlugException;
import com.saas.booking_engine.domain.model.Tenant;
import com.saas.booking_engine.domain.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación de los casos de uso de gestión de tenants.
 */
@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

  private final TenantRepository tenantRepository;

  /**
   * Registra un nuevo tenant tras validar que su slug sea único en el sistema.
   *
   * @param request datos del tenant a crear
   * @return tenant persistido convertido a DTO de respuesta
   * @throws DuplicateSlugException cuando el slug ya está en uso
   */
  @Override
  @Transactional
  public TenantResponse createTenant(CreateTenantRequest request) {
    if (tenantRepository.existsBySlug(request.slug())) {
      throw new DuplicateSlugException(request.slug());
    }

    Tenant tenant = TenantMapper.toEntity(request);
    Tenant savedTenant = tenantRepository.save(tenant);
    return TenantMapper.toResponse(savedTenant);
  }

  /**
   * Recupera un tenant por su slug o lanza una excepción si no existe.
   *
   * @param slug identificador único del tenant
   * @return representación del tenant encontrado
   * @throws com.saas.booking_engine.domain.exception.TenantNotFoundException si el slug no existe
   */
  @Override
  @Transactional(readOnly = true)
  public TenantResponse getTenantBySlug(String slug) {
    return tenantRepository
        .findBySlug(slug)
        .map(TenantMapper::toResponse)
        .orElseThrow(() -> new com.saas.booking_engine.domain.exception.TenantNotFoundException(slug));
  }
}
