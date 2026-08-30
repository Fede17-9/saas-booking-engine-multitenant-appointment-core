package com.saas.booking_engine.application.dto.tenant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO de entrada para la creación de un nuevo tenant.
 */
public record CreateTenantRequest(
    @NotBlank String name,
    @NotBlank String slug,
    @NotBlank @Email String contactEmail,
    String taxId) {}
