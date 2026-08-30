package com.saas.booking_engine.domain.repository;

import com.saas.booking_engine.domain.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio de acceso a datos para la entidad {@link User}.
 */
public interface UserRepository extends JpaRepository<User, UUID> {

  @Query("SELECT u FROM User u WHERE u.email = :email AND u.tenant.id = :tenantId")
  Optional<User> findByEmailAndTenantId(@Param("email") String email, @Param("tenantId") UUID tenantId);

  Optional<User> findByEmail(String email);

  @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u "
      + "WHERE u.email = :email AND u.tenant.id = :tenantId")
  boolean existsByEmailAndTenantId(@Param("email") String email, @Param("tenantId") UUID tenantId);
}
