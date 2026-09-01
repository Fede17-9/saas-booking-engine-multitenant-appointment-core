package com.saas.booking_engine.domain.exception;

/**
 * Excepción de dominio cuando no existe un usuario con el email indicado.
 */
public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String email) {
    super("No existe un usuario con el email: " + email);
  }
}
