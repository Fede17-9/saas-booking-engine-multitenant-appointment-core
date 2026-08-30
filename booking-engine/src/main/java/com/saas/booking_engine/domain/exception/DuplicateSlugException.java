package com.saas.booking_engine.domain.exception;

/**
 * Excepción de negocio lanzada cuando se intenta registrar un slug de tenant duplicado.
 */
public class DuplicateSlugException extends RuntimeException {

  public DuplicateSlugException(String slug) {
    super("Ya existe un tenant con el slug: " + slug);
  }
}
