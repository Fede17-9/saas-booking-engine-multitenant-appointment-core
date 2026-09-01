package com.saas.booking_engine.infrastructure.security;

import com.saas.booking_engine.domain.model.User;
import com.saas.booking_engine.domain.repository.UserRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Carga los detalles del usuario desde la base de datos para autenticación JWT.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

    return new org.springframework.security.core.userdetails.User(
        user.getEmail(),
        user.getPasswordHash(),
        Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
  }
}
