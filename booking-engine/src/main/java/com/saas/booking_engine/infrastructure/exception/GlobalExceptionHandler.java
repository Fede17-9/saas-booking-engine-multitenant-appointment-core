package com.saas.booking_engine.infrastructure.exception;

import com.saas.booking_engine.domain.exception.DuplicateSlugException;
import com.saas.booking_engine.domain.exception.EmailAlreadyExistsException;
import com.saas.booking_engine.domain.exception.PublicRegistrationRoleNotAllowedException;
import com.saas.booking_engine.domain.exception.TenantNotFoundException;
import com.saas.booking_engine.domain.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
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

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ProblemDetail handleEmailAlreadyExistsException(EmailAlreadyExistsException exception) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    problemDetail.setTitle("Email ya registrado");
    return problemDetail;
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ProblemDetail handleUserNotFoundException(UserNotFoundException exception) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    problemDetail.setTitle("Usuario no encontrado");
    return problemDetail;
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ProblemDetail handleBadCredentialsException(BadCredentialsException exception) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
    problemDetail.setTitle("Credenciales inválidas");
    return problemDetail;
  }

  @ExceptionHandler(PublicRegistrationRoleNotAllowedException.class)
  public ProblemDetail handlePublicRegistrationRoleNotAllowedException(
      PublicRegistrationRoleNotAllowedException exception) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, exception.getMessage());
    problemDetail.setTitle("Rol no permitido en el registro público");
    return problemDetail;
  }
}
