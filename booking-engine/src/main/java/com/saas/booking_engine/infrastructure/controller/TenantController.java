package com.saas.booking_engine.infrastructure.controller;

import com.saas.booking_engine.application.dto.tenant.CreateTenantRequest;
import com.saas.booking_engine.application.dto.tenant.TenantResponse;
import com.saas.booking_engine.application.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para la gestión de tenants del sistema multi-tenant.
 */
@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
public class TenantController {

  private final TenantService tenantService;

  @Operation(summary = "Crear un tenant", description = "Registra un nuevo tenant validando la unicidad del slug.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Tenant creado correctamente",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = TenantResponse.class))),
      @ApiResponse(responseCode = "409", description = "Slug duplicado", content = @Content),
      @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
  })
  @PostMapping
  @PreAuthorize("hasAuthority('SUPER_ADMIN')")
  public ResponseEntity<TenantResponse> createTenant(@Valid @RequestBody CreateTenantRequest request) {
    TenantResponse response = tenantService.createTenant(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Buscar tenant por slug", description = "Devuelve la información pública de un tenant dado su slug.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Tenant encontrado",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = TenantResponse.class))),
      @ApiResponse(responseCode = "404", description = "Tenant no encontrado", content = @Content)
  })
  @GetMapping("/{slug}")
  public ResponseEntity<TenantResponse> getTenantBySlug(@PathVariable String slug) {
    TenantResponse response = tenantService.getTenantBySlug(slug);
    return ResponseEntity.ok(response);
  }
}
