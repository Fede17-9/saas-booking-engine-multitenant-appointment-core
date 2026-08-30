package com.saas.booking_engine.domain.model;

import com.saas.booking_engine.shared.audit.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID; //esta importacion se usa para generar un UUID(Universally Unique Identifier) unico para cada inquilino
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad raíz que representa un inquilino (tenant) del sistema multi-tenant.
 */
@Entity
@Table(name = "tenants")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tenant extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, unique = true, length = 100)
  private String slug;

  @Column(name = "contact_email", nullable = false, length = 150)
  private String contactEmail;

  @Column(name = "tax_id", length = 50)
  private String taxId;

  @Column(name = "is_active", nullable = false)
  @Builder.Default
  private boolean isActive = true;
}
