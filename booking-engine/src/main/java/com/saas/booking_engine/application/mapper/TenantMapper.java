package com.saas.booking_engine.application.mapper;

import com.saas.booking_engine.application.dto.tenant.CreateTenantRequest;
import com.saas.booking_engine.application.dto.tenant.TenantResponse;
import com.saas.booking_engine.domain.model.Tenant;

/**
 * Transformaciones entre entidades {@link Tenant} y sus DTOs.
 */
public final class TenantMapper {

  private TenantMapper() {}

  public static Tenant toEntity(CreateTenantRequest request) {
    return Tenant.builder()
        .name(request.name())
        .slug(request.slug())
        .contactEmail(request.contactEmail())
        .taxId(request.taxId())
        .isActive(true)
        .build();
  }

  public static TenantResponse toResponse(Tenant tenant) {
    return new TenantResponse(
        tenant.getId(),
        tenant.getName(),
        tenant.getSlug(),
        tenant.getContactEmail(),
        tenant.getTaxId(),
        tenant.isActive(),
        tenant.getCreatedAt());
  }
}
