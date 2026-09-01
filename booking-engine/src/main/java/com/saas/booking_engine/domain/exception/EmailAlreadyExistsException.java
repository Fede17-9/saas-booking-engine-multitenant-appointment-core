package com.saas.booking_engine.domain.exception;

/**
 * Excepción de dominio cuando un email ya está asociado a un tenant.
 */
public class EmailAlreadyExistsException extends RuntimeException {

  public EmailAlreadyExistsException(String email) {
    super("Ya existe un usuario registrado con el email: " + email);
  }
}
