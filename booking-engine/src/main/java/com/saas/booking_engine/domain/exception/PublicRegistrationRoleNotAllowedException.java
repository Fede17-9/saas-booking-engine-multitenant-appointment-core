package com.saas.booking_engine.domain.exception;

import com.saas.booking_engine.domain.enums.UserRole;

/**
 * Se lanza cuando el registro público intenta crear un rol administrativo.
 */
public class PublicRegistrationRoleNotAllowedException extends RuntimeException {

  public PublicRegistrationRoleNotAllowedException(UserRole role) {
    super("El registro público no permite crear usuarios con el rol: " + role);
  }
}
