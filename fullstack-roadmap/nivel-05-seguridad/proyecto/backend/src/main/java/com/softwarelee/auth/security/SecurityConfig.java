package com.softwarelee.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * CONFIGURACIÓN DE SEGURIDAD.
 *
 * Define:
 * - Qué rutas son públicas y cuáles requieren autenticación
 * - Que NO usamos sesiones (stateless, porque usamos JWT)
 * - El filtro JWT se ejecuta antes del filtro de login de Spring
 * - BCrypt para hashear passwords
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // Activa @PreAuthorize en los controllers
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desactivar CSRF (no se necesita con JWT + API stateless)
            .csrf(csrf -> csrf.disable())

            // Política de sesiones: STATELESS (no guardar sesión en servidor)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Reglas de autorización por ruta
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas (no necesitan token)
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/auth/refresh").permitAll()
                .requestMatchers("/api/auth/register").permitAll()

                // Todo lo demás requiere autenticación
                .anyRequest().authenticated()
            )

            // Agregar nuestro filtro JWT ANTES del filtro de autenticación de Spring
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
