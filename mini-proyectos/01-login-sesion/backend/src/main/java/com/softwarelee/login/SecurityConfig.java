package com.softwarelee.login;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * CONFIGURACIÓN DE SEGURIDAD CON SESIÓN.
 *
 * Este es el tipo de autenticación MÁS SIMPLE de Spring Security:
 * - El usuario envía username+password en un formulario HTML
 * - Spring Security valida las credenciales
 * - Si son correctas: crea una SESIÓN en el servidor + cookie JSESSIONID
 * - En cada request siguiente, el navegador envía la cookie automáticamente
 * - Spring Security ve la cookie → sabe que ya estás autenticado
 *
 * DIFERENCIA CON JWT:
 * - Sesión: el SERVIDOR recuerda quién eres (stateful)
 * - JWT: el CLIENTE guarda quién es en un token (stateless)
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/css/**").permitAll()  // Público
                .requestMatchers("/admin/**").hasRole("ADMIN")           // Solo ADMIN
                .anyRequest().authenticated()                            // Todo lo demás: login requerido
            )
            .formLogin(form -> form
                .loginPage("/login")                    // Nuestra página de login (no la default)
                .defaultSuccessUrl("/dashboard", true)  // A dónde ir después de login exitoso
                .failureUrl("/login?error=true")        // A dónde ir si falla
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)            // Borrar la sesión
                .deleteCookies("JSESSIONID")            // Borrar la cookie
            )
            .build();
    }

    /**
     * Usuarios en memoria (para aprendizaje).
     * En producción: se cargarían de la base de datos.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        var admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("Admin123!"))
            .roles("ADMIN", "USER")
            .build();

        var usuario = User.builder()
            .username("usuario")
            .password(passwordEncoder().encode("User123!"))
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(admin, usuario);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
