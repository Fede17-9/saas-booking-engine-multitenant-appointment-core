package com.saas.booking_engine.infrastructure.controller;

import com.saas.booking_engine.application.dto.auth.AuthResponse;
import com.saas.booking_engine.application.dto.auth.LoginRequest;
import com.saas.booking_engine.application.dto.auth.RegisterUserRequest;
import com.saas.booking_engine.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST público para autenticación de usuarios.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "Registrar un usuario", description = "Crea un usuario nuevo y devuelve el JWT de acceso.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = AuthResponse.class))),
      @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
      @ApiResponse(responseCode = "409", description = "El email ya existe en el tenant", content = @Content)
  })
  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterUserRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
  }

  @Operation(summary = "Iniciar sesión", description = "Valida las credenciales y devuelve un JWT para la sesión.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Credenciales válidas",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = AuthResponse.class))),
      @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
      @ApiResponse(responseCode = "401", description = "Credenciales incorrectas", content = @Content)
  })
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }
}
