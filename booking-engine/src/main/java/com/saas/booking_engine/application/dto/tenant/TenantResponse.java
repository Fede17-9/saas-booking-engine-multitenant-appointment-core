package com.saas.booking_engine.application.dto.tenant;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO de salida con la representación pública de un tenant.
 */
public record TenantResponse(
    UUID id,
    String name,
    String slug,
    String contactEmail,
    String taxId,
    boolean isActive,
    Instant createdAt) {}
