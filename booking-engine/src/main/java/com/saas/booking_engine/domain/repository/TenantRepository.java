package com.saas.booking_engine.domain.repository;

import com.saas.booking_engine.domain.model.Tenant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de acceso a datos para la entidad {@link Tenant}.
 */
public interface TenantRepository extends JpaRepository<Tenant, UUID> {

  Optional<Tenant> findBySlug(String slug);

  boolean existsBySlug(String slug);
}
