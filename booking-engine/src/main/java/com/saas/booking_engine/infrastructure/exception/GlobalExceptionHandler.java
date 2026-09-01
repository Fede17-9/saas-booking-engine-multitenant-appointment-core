package com.saas.booking_engine.infrastructure.exception;

import com.saas.booking_engine.domain.exception.DuplicateSlugException;
import com.saas.booking_engine.domain.exception.TenantNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones usando RFC 7807 ({@link ProblemDetail}).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DuplicateSlugException.class)
  public ProblemDetail handleDuplicateSlugException(DuplicateSlugException exception) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    problemDetail.setTitle("Conflicto de recurso");
    return problemDetail;
  }

  @ExceptionHandler(TenantNotFoundException.class)
  public ProblemDetail handleTenantNotFoundException(TenantNotFoundException exception) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    problemDetail.setTitle("Tenant no encontrado");
    return problemDetail;
  }
}
