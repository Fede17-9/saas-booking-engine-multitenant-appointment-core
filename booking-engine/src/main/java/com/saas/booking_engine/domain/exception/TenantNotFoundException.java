package com.saas.booking_engine.domain.exception;

/**
 * Excepción de negocio lanzada cuando no existe un tenant con el slug proporcionado.
 */
public class TenantNotFoundException extends RuntimeException {

  public TenantNotFoundException(String slug) {
    super("No existe un tenant con el slug: " + slug);
  }
}
